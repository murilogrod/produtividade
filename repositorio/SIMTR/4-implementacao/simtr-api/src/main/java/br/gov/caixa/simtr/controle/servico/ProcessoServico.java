package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.postgresql.util.PSQLException;

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.modelo.entidade.DocumentoGarantia;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.ProcessoDocumento;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.entidade.VinculacaoChecklist;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.processo.ManterProcessoDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRAUD,
    ConstantesUtil.PERFIL_MTRTEC,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE,
    ConstantesUtil.PERFIL_MTRSDNTTG,
    ConstantesUtil.PERFIL_MTRSDNTTO
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ProcessoServico extends AbstractService<Processo, Integer> {

    @EJB
    private DossieProdutoServico dossieProdutoServico;

    @EJB
    private KeycloakService keycloackService;

    @Inject
    private EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(ProcessoServico.class.getName());

    private Calendar dataHoraUltimaAlteracao;
    // Mapa contendo todos os processos identificados pelo seu ID
    private final Map<Integer, Processo> mapaProcessos = new HashMap<>();
    private final Map<Integer, Processo> mapaProcessosTemp = new HashMap<>();
    // Mapa contendo todos os processos identificados pelo seu nome
    private final Map<String, Processo> mapaNome = new HashMap<>();
    private final Map<String, Processo> mapaNomeTemp = new HashMap<>();
    // Mapa contendo os processos pai imediato dos processos identificados pelo seu ID
    private final Map<Integer, Processo> mapaPai = new HashMap<>();
    private final Map<Integer, Processo> mapaPaiTemp = new HashMap<>();
    // Mapa indicando qual o processo patriarca, independente de sua posição na arvore de processos.
    private final Map<Integer, Processo> mapaPatriarcas = new HashMap<>();
    private final Map<Integer, Processo> mapaPatriarcasTemp = new HashMap<>();
    // Os mapas temporarios são carreghados durante o processo para caso alguem solicite um processo, o mapa oficial ainda possa responder mesmo durante a recarga

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @PermitAll
    public Calendar getDataHoraUltimaAlteracao() {
        return this.dataHoraUltimaAlteracao;
    }

    /**
     * Limpa os mapas de processos e recarrega a os mesmos com informações oriundas do banco de dados
     */
    @PostConstruct
    @PermitAll
    private void carregarMapas() {

        String jpqlUltimaAlteracao = " SELECT MAX(p.dataHoraUltimaAlteracao) FROM Processo p ";
        TypedQuery<Calendar> queryUltimaAlteracao = this.entityManager.createQuery(jpqlUltimaAlteracao, Calendar.class);
        Calendar ultimaAlteracao = queryUltimaAlteracao.getSingleResult();

        if (Objects.isNull(this.dataHoraUltimaAlteracao) || dataHoraUltimaAlteracao.before(ultimaAlteracao)) {

            this.mapaProcessosTemp.clear();
            this.mapaNomeTemp.clear();
            this.mapaPaiTemp.clear();
            this.mapaPatriarcasTemp.clear();

            StringBuilder jpql = new StringBuilder();
            jpql.append(" SELECT DISTINCT p1 FROM Processo p1 ");
            jpql.append(" WHERE p1.relacoesProcessoVinculoFilho IS EMPTY ");

            TypedQuery<Processo> query = this.entityManager.createQuery(jpql.toString(), Processo.class);

            List<Processo> processosPatriarca = query.getResultList();
            processosPatriarca.forEach(processoPatriarca -> {
                Processo processoCarregado = this.carregaProcesso(processoPatriarca.getId(), null, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
                // Inclui o registro de pai imadiato do processo patriarca carregado, no caso nulo.
                this.mapaPaiTemp.put(processoCarregado.getId(), null);
            });

            this.mapaProcessos.clear();
            this.mapaProcessos.putAll(mapaProcessosTemp);
            this.mapaNome.clear();
            this.mapaNome.putAll(mapaNomeTemp);
            this.mapaPai.clear();
            this.mapaPai.putAll(mapaPaiTemp);
            this.mapaPatriarcas.clear();
            this.mapaPatriarcas.putAll(mapaPatriarcasTemp);

            this.dataHoraUltimaAlteracao = ultimaAlteracao;
        }
    }

    /**
     * Carrega um processo com as informalçoes definidas conforme grupo de vinculação e retorna o processo carregado baseado no identificador informado. Utilizado
     * também na estrutura recursiva para carregar os mapas de processos.
     *
     * @param id Identificador do processo a ser carregado.
     * @param processoPatriarca Processo patriarca repassado na estrutura recursiva para carregar os mapas. Caso o valor seja nulo, os mapas não serão alimentados.
     * @param carregaMapa Indica seos mapas devem ser carregados.
     * @param carregaProcessosFilhos Identifica se os processos filhos devem ser carregados recursivamente
     * @param vinculacaoProdutos Indica se os produtos permitidos a contratação associados com o processo devem ser carregados.
     * @param vinculacaoElementosConteudo Identifica se os elementos de conteudo utilizados na montagem da arvore de documentos a ser carregados que estão
     *        associados com o processo devem ser carregados
     * @param vinculacaoFormulario Identifica se os campos de formulario necessarios a montagem do formulario dinamico vinculados ao processo devem ser carregados
     * @param vinculacaoUnidadeAutorizada Identifica se as unidades autorizadas com as respectivas autorizacoes devem ser carregadas
     * @param vinculacaoDocumentoVinculo Identifica se os documentos necessarios definidos pelo processo, conforme vinculo estabelecido entre o dossiê de cliente e
     *        dossiê de produto, devem ser carregados.
     * @return
     */
    @PermitAll
    private Processo carregaProcesso(Integer id, Processo processoPatriarca, boolean carregaMapa, boolean carregaProcessosFilhos, boolean vinculacaoProdutos, boolean vinculacaoElementosConteudo, boolean vinculacaoFormulario, boolean vinculacaoUnidadeAutorizada, boolean vinculacaoDocumentoVinculo, boolean vinculacaoDocumentoGarantia, boolean vinculacaoChecklist) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT p FROM Processo p ");
        if (carregaProcessosFilhos) {
            jpql.append(" LEFT JOIN FETCH p.relacoesProcessoVinculoPai rpvp ");
            jpql.append(" LEFT JOIN FETCH rpvp.processoFilho pf ");
        }

        if (vinculacaoElementosConteudo) {
            jpql.append(" LEFT JOIN FETCH p.elementosConteudo ec ");
            jpql.append(" LEFT JOIN FETCH ec.tipoDocumento tdec ");
        }
        if (vinculacaoFormulario) {
            // Vinculos pelo processo gerador de dossiê
            jpql.append(" LEFT JOIN FETCH p.camposFormulario cf ");
            jpql.append(" LEFT JOIN FETCH cf.campoEntrada ce ");
            jpql.append(" LEFT JOIN FETCH cf.camposApresentacao ca ");
            jpql.append(" LEFT JOIN FETCH ce.opcoesCampo oc ");

            // Vinculos pelo processo fase
            jpql.append(" LEFT JOIN FETCH p.camposFormularioFase cff ");
            jpql.append(" LEFT JOIN FETCH cff.campoEntrada cef ");
            jpql.append(" LEFT JOIN FETCH cff.camposApresentacao caf ");
            jpql.append(" LEFT JOIN FETCH cef.opcoesCampo ocf ");
        }
        if (vinculacaoUnidadeAutorizada) {
            jpql.append(" LEFT JOIN FETCH p.unidadesAutorizadas ua ");
        }

        jpql.append(" WHERE p.id = :id ");
        jpql.append(" ORDER BY p.nome ");

        TypedQuery<Processo> queryProcesso = this.entityManager.createQuery(jpql.toString(), Processo.class);
        queryProcesso.setParameter("id", id);
        try {
            Processo processoEmCarga = queryProcesso.getSingleResult();

            // Realizado consulta de produtos separadamente para melhorar a performance da busca pois numa unica consulta gera lock de recursos e apresenta resultados
            // invalidos pelo path do JPQL
            if (vinculacaoProdutos) {
                jpql = new StringBuilder();
                jpql.append(" SELECT DISTINCT prd FROM Produto prd");
                jpql.append(" LEFT JOIN prd.processos p");
                if (vinculacaoElementosConteudo) {
                    jpql.append(" LEFT JOIN FETCH prd.elementosConteudo ecp ");
                    jpql.append(" LEFT JOIN FETCH ecp.tipoDocumento tdecp ");
                }
                jpql.append(" LEFT JOIN FETCH prd.garantias g ");
                jpql.append(" LEFT JOIN FETCH prd.camposFormulario cf ");
                jpql.append(" LEFT JOIN FETCH cf.processo pcf ");
                jpql.append(" LEFT JOIN FETCH cf.campoEntrada ce ");
                jpql.append(" LEFT JOIN FETCH cf.camposApresentacao ca ");
                jpql.append(" LEFT JOIN FETCH ce.opcoesCampo oc ");
                jpql.append(" LEFT JOIN FETCH g.documentosGarantia dgg ");
                jpql.append(" LEFT JOIN FETCH dgg.processo pdgg ");
                jpql.append(" LEFT JOIN FETCH dgg.tipoDocumento tddgg ");
                jpql.append(" LEFT JOIN FETCH tddgg.funcoesDocumentais fdtddgg ");
                jpql.append(" LEFT JOIN FETCH dgg.funcaoDocumental fddgg ");
                jpql.append(" LEFT JOIN FETCH fddgg.tiposDocumento tdfddgg ");
                jpql.append(" WHERE p.id = :id");

                TypedQuery<Produto> queryProdutos = this.entityManager.createQuery(jpql.toString(), Produto.class);
                queryProdutos.setParameter("id", id);
                processoEmCarga.setProdutos(new HashSet<>(queryProdutos.getResultList()));
            }

            // Realizado consulta de documentos por tipo de vinculo no processo separadamente para melhorar a performance da busca pois numa unica consulta gera lock de
            // recursos e apresenta resultados invalidos pelo path do JPQL
            if (vinculacaoDocumentoVinculo) {
                jpql = new StringBuilder();
                jpql.append(" SELECT DISTINCT pd FROM ProcessoDocumento pd");
                jpql.append(" LEFT JOIN pd.processo p ");
                jpql.append(" LEFT JOIN FETCH pd.tipoDocumento tdpd ");
                jpql.append(" LEFT JOIN FETCH tdpd.funcoesDocumentais fdtdpd ");
                jpql.append(" LEFT JOIN FETCH pd.funcaoDocumental fdpd ");
                jpql.append(" LEFT JOIN FETCH fdpd.tiposDocumento tdfdpd ");
                jpql.append(" LEFT JOIN FETCH pd.tipoRelacionamento tr ");
                jpql.append(" LEFT JOIN FETCH tr.camposFormulario cf ");
                jpql.append(" LEFT JOIN FETCH cf.processo pcf ");
                jpql.append(" LEFT JOIN FETCH cf.campoEntrada ce ");
                jpql.append(" LEFT JOIN FETCH cf.camposApresentacao ca ");
                jpql.append(" LEFT JOIN FETCH ce.opcoesCampo oc ");
                jpql.append(" WHERE p.id = :id ");
                
                TypedQuery<ProcessoDocumento> queryDocumentosVinculos = this.entityManager.createQuery(jpql.toString(), ProcessoDocumento.class);
                queryDocumentosVinculos.setParameter("id", id);
                processoEmCarga.setProcessoDocumentos(new HashSet<>(queryDocumentosVinculos.getResultList()));
            }

            // Realizado consulta da vinculacao de documentos por garantia separadamente para melhorar a performance da busca pois numa unica consulta gera lock de recursos
            // e apresenta resultados invalidos pelo path do JPQL
            if (vinculacaoDocumentoGarantia) {
                jpql = new StringBuilder();
                jpql.append(" SELECT DISTINCT dg FROM DocumentoGarantia dg");
                jpql.append(" LEFT JOIN dg.processo p ");
                jpql.append(" LEFT JOIN FETCH dg.garantia g ");
                jpql.append(" LEFT JOIN FETCH g.camposFormulario cf ");
                jpql.append(" LEFT JOIN FETCH cf.processo pcf ");
                jpql.append(" LEFT JOIN FETCH cf.camposApresentacao ca ");
                jpql.append(" LEFT JOIN FETCH cf.campoEntrada ce ");
                jpql.append(" LEFT JOIN FETCH ce.opcoesCampo oc ");
                jpql.append(" LEFT JOIN FETCH dg.tipoDocumento tddg ");
                jpql.append(" LEFT JOIN FETCH tddg.funcoesDocumentais fdtddg ");
                jpql.append(" LEFT JOIN FETCH dg.funcaoDocumental fddg ");
                jpql.append(" LEFT JOIN FETCH fddg.tiposDocumento tdfddg ");
                jpql.append(" WHERE p.id = :id");

                TypedQuery<DocumentoGarantia> queryDocumentosGarantia = this.entityManager.createQuery(jpql.toString(), DocumentoGarantia.class);
                queryDocumentosGarantia.setParameter("id", id);
                processoEmCarga.setDocumentosGarantia(new HashSet<>(queryDocumentosGarantia.getResultList()));
            }

            // Realizado consulta da vinculacao de checklists separadamente para melhorar a performance da busca pois numa unica consulta gera lock de recursos e apresenta
            // resultados invalidos pelo path do JPQL
            if (vinculacaoChecklist) {
                jpql = new StringBuilder();
                jpql.append(" SELECT DISTINCT vc FROM VinculacaoChecklist vc ");
                jpql.append(" LEFT JOIN FETCH vc.processoDossie pd ");
                jpql.append(" LEFT JOIN FETCH vc.processoFase pf ");
                jpql.append(" LEFT JOIN FETCH vc.checklist cl ");
                jpql.append(" LEFT JOIN FETCH cl.apontamentos a ");
                jpql.append(" LEFT JOIN FETCH vc.tipoDocumento tdvc ");
                jpql.append(" LEFT JOIN FETCH vc.funcaoDocumental fdvc ");
                jpql.append(" WHERE (pd.id = :id) OR (pf.id = :id) ");

                TypedQuery<VinculacaoChecklist> queryChecklists = this.entityManager.createQuery(jpql.toString(), VinculacaoChecklist.class);
                queryChecklists.setParameter("id", id);
                List<VinculacaoChecklist> vinculacoes = queryChecklists.getResultList();

                List<VinculacaoChecklist> vinculosChecklistDossie = new ArrayList<>();
                List<VinculacaoChecklist> vinculosChecklistFase = new ArrayList<>();
                vinculacoes.forEach(v -> {
                    if (processoEmCarga.getId().equals(v.getProcessoDossie().getId())) {
                        vinculosChecklistDossie.add(v);
                    } else if (processoEmCarga.getId().equals(v.getProcessoFase().getId())) {
                        vinculosChecklistFase.add(v);
                    }
                });
                processoEmCarga.setVinculacoesChecklists(new HashSet<>(vinculosChecklistDossie));
                processoEmCarga.setVinculacoesChecklistsFase(new HashSet<>(vinculosChecklistFase));
            }

            if (carregaMapa) {
                // Inclui o processo carregado no mapa de processos por ID.
                mapaProcessosTemp.put(processoEmCarga.getId(), processoEmCarga);
                // Inclui o processo carregado no mapa de processos por nome
                mapaNomeTemp.put(processoEmCarga.getNome().toUpperCase(), processoEmCarga);
                // Inclui o processo patriarca da arvore vinculado aoidentificador do processo carregado no mapa de processos patriarcas.
                mapaPatriarcasTemp.put(processoEmCarga.getId(), processoPatriarca);
            }

            // Atualiza o processo patriarca caso seja o primeiro ciclo para enviar como patriarca o processo que estava processo de carga
            Processo processoPatriarcaDefinido = Objects.isNull(processoPatriarca) ? processoEmCarga : processoPatriarca;

            if (carregaProcessosFilhos && processoEmCarga.getRelacoesProcessoVinculoPai() != null) {
                processoEmCarga.getRelacoesProcessoVinculoPai().forEach(relacaoProcesso -> {
                    Processo processoFilho = relacaoProcesso.getProcessoFilho();
                    processoFilho = this.carregaProcesso(processoFilho.getId(), processoPatriarcaDefinido, carregaMapa, carregaProcessosFilhos, vinculacaoProdutos, vinculacaoElementosConteudo, vinculacaoFormulario, vinculacaoUnidadeAutorizada, vinculacaoDocumentoVinculo, vinculacaoDocumentoGarantia, vinculacaoChecklist);
                    relacaoProcesso.setProcessoFilho(processoFilho);
                    if (carregaMapa) {
                        // Inclui a relação entre o processo carregado e o processo pai imadiato.
                        mapaPaiTemp.put(processoFilho.getId(), processoEmCarga);
                    }
                });
            }
            return processoEmCarga;
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }

    /**
     * Carrega as informações do processo indicado para viabilizar a montagem, da estrutura de tratamento de processos. Realiza a ação de forma recursiva carregando
     * as informações basicas dos dossiês de produto vinculados como fase de execução ao processo.
     *
     * @param processo Processo em que se deseja carregar as informações
     * @param situacaoDossieEnum Indica a situação desejada para o dossiê. Ex: AGUARDANDO TRATAMENTO, AGUARDANDO COMPLEMENTAÇÃO
     * @return Processo carregado com as informações necessarias a estruturação do tratamento.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    private Processo carregaProcessoBySituacao(final Processo processo, SituacaoDossieEnum situacaoDossieEnum) {

        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT p FROM Processo p ");
        jpql.append(" LEFT JOIN FETCH p.unidadesAutorizadas ua ");
        jpql.append(" LEFT JOIN FETCH p.dossiesProduto dp ");
        jpql.append(" LEFT JOIN FETCH p.relacoesProcessoVinculoPai rpvp ");
        jpql.append(" LEFT JOIN FETCH rpvp.processoFilho pf ");
        jpql.append(" LEFT JOIN FETCH p.processosFaseDossie pfd ");
        jpql.append(" LEFT JOIN FETCH pfd.dossieProduto pfddp ");
        jpql.append(" WHERE p.id = :id ");
        jpql.append(" AND ua.unidade IN (:lotacaoAdministrativa, :lotacaoFisica) ");
        jpql.append(" ORDER BY p.nome ");

        TypedQuery<Processo> query = this.entityManager.createQuery(jpql.toString(), Processo.class);
        query.setParameter("id", processo.getId());
        query.setParameter("lotacaoAdministrativa", this.keycloackService.getLotacaoAdministrativa());
        query.setParameter("lotacaoFisica", this.keycloackService.getLotacaoFisica());

        try {
            // Captura o processo carregado com a relação de processos filhos vinculados e a lista de unidades autorizadas baseado na lotação do usuario (tb021)
            Processo processoCarregado = query.getSingleResult();

            // Se não houver vinculo uma exceção é gerada, mas havendo o vinculo a lista de dossiês relacionados ao processo em analise como processo fase e que estejam na
            // situação indicada em parametro
            List<DossieProduto> dossiesProduto = this.dossieProdutoServico.listByProcessoAndSituacao(processoCarregado, situacaoDossieEnum);
            
            // Caso o processo defina a indicação de geração de dossiê, remove os registros pré carregados de dossiê de produto que não estão na lista de habilitados
            // Caso contrario, remove os registros de processo fase, que estão pré carregados com associação a um dossiê de produto que não esteja na lista de habilitados  
            if (processoCarregado.getIndicadorGeracaoDossie()) {
                // Remove os dossiês pré carregados no processo que não estão presentes na lista retornada
                processoCarregado.getDossiesProduto().removeIf(dossieCandidato -> {
                    return !dossiesProduto.stream().anyMatch(dp -> dp.getId().equals(dossieCandidato.getId()));
                });
                processoCarregado.setProcessosFaseDossie(new HashSet<>());
            } else {
                processoCarregado.setDossiesProduto(new HashSet<>());
                // Remove os registro de processo fase vinculados pré carregados no processo que estão associados a dossiês não estão presentes na lista retornada
                processoCarregado.getProcessosFaseDossie().removeIf(processoCandidato -> {
                    return !dossiesProduto.stream().anyMatch(dp -> dp.getId().equals(processoCandidato.getDossieProduto().getId()));
                });
            }

            // Caso o processo em analise possua processos filhos, carrega cada processo filho recursivamente.
            if (processoCarregado.getRelacoesProcessoVinculoPai() != null) {
                processoCarregado.getRelacoesProcessoVinculoPai().forEach(relacaoProcesso -> {
                    Processo processoFilho = relacaoProcesso.getProcessoFilho();
                    processoFilho = this.carregaProcessoBySituacao(processoFilho, situacaoDossieEnum);
                    relacaoProcesso.setProcessoFilho(processoFilho);
                });
            }

            // Retorna o processo carregado após repetir o processo recursivamente por todos os processos filho
            return processoCarregado;
        } catch (NoResultException nre) {
            // Inibe a exibição do log e retorna null para os casos de não localizar os processos vom vinculo e autorização para unidade do operador.
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }

    /**
     * <p>
     * Lista todos os processos em estrutura hierarquicca com os quantitativos de dossiês que estão na situação \"Aguardando Tratamento\".
     * </p>
     * <p>
     * Também é levado em consideração a unidade de vinculação do usuario (fisica ou administrativa) que precisa estar definida como unidade designada para
     * tratamento dos dossiês no momento da consulta, ou os mesmos não serão contabilizados.
     * </p>
     *
     * @param situacaoDossieEnum Indica a situação desejada para o dossiê. Ex: AGUARDANDO TRATAMENTO, AGUARDANDO COMPLEMENTAÇÃO
     * @return Lista de processos em estrutura hierarquica com os dossiês de produtos vinculados aos mesmos.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    public List<Processo> listaProcessosPorSituacao(final SituacaoDossieEnum situacaoDossieEnum) {

        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT p2 FROM Processo p2 ");
        jpql.append(" LEFT JOIN p2.unidadesAutorizadas ua ");
        jpql.append(" WHERE p2.relacoesProcessoVinculoFilho IS EMPTY ");
        jpql.append(" AND ua.unidade IN (:lotacaoAdministrativa, :lotacaoFisica) ");

        TypedQuery<Processo> query = this.entityManager.createQuery(jpql.toString(), Processo.class);
        query.setParameter("lotacaoAdministrativa", this.keycloackService.getLotacaoAdministrativa());
        query.setParameter("lotacaoFisica", this.keycloackService.getLotacaoFisica());

        List<Processo> processosPatriarca = query.getResultList();

        List<Processo> processosCarregados = new ArrayList<>();
        processosPatriarca.forEach(processoPatriarca -> {
            Processo processoCarregado = this.carregaProcessoBySituacao(processoPatriarca, situacaoDossieEnum);
            if (processoCarregado != null) {
                processosCarregados.add(processoCarregado);
            }
        });
        return processosCarregados;
    }

    /**
     * Captura a lista de processos identificados como patriarca, ou seja, aqueles que não possuem nenhum processo definido como pai na relação entre processos
     *
     * @return Lista de processos identificados como patriarca com toda sua estrutura vinculada
     */
    @PermitAll
    public List<Processo> listPatriarcas() {
        this.carregarMapas();
        final List<Processo> processosPatriarca = new ArrayList<>();
        this.mapaPatriarcas.entrySet().forEach(registro -> {
            if (registro.getValue() == null) {
                processosPatriarca.add(mapaProcessos.get(registro.getKey()));
            }
        });
        return processosPatriarca;
    }

    /**
     * Captura a lista de processos identificados como patriarca, ou seja, aqueles que não possuem nenhum processo definido como pai na relação entre processos
     *
     * @return Lista de processos identificados como patriarca com toda sua estrutura vinculada
     */
    @PermitAll
    public List<Processo> listaProcessos() {
        this.carregarMapas();
        final List<Processo> processos = new ArrayList<>();
        this.mapaProcessos.entrySet().forEach(registro -> {
            processos.add(mapaProcessos.get(registro.getKey()));
        });
        return processos;
    }

    /**
     * Captura um processo contendo toda sua estrutura relacionada baseado em seu identificador definido
     *
     * @param id Identificador do processo que se deseja capturar
     * @return Processo identificado
     */
    @Override
    @PermitAll
    public Processo getById(final Integer id) {
        this.carregarMapas();
        return mapaProcessos.get(id);
    }

    /**
     * Captura um processo contendo toda sua estrutura relacionada baseado em seu nome cadastrado
     *
     * @param nome Nome do processo que se deseja capturar
     * @return Processo identificado
     */
    @PermitAll
    public Processo getByNome(final String nome) {
        this.carregarMapas();
        return mapaNome.get(nome.toUpperCase());
    }

    /**
     * Captura o processo patriarca a partir do codigo de identificação de um processo
     *
     * @param id Identificador do processo que se deseja conhecer qual o processo patriarca da arvore
     * @return Processo patriarca identificado
     */
    @PermitAll
    public Processo getPatriarcaById(final Integer id) {
        return mapaPatriarcas.get(id);
    }

    /**
     * Captura um processo pai contendo toda sua estrutura relacionada
     *
     * @param id Identificador do processo que se deseja conhecer o processo pai imediato
     * @return Processo identificado
     */
    @PermitAll
    public Processo getPaiById(final Integer id) {
        this.carregarMapas();
        return mapaPai.get(id);
    }
    
    /**
     * Lista os processo hierarquicamente vinculados ao processo informado de forma recursiva.
     *
     * @param processo Processo desejado capturar a lista de vinculados
     * @return Lista de procesos vinculado ao processo informado.
     */
    @PermitAll
    public List<Processo> listProcessosVinculados(Processo processo) {
        List<Processo> processosVinculados = new ArrayList<>();
        processosVinculados.add(processo);
        processo.getRelacoesProcessoVinculoPai().forEach(relacaoProcesso -> {
            Processo p = relacaoProcesso.getProcessoFilho();
            processosVinculados.addAll(this.listProcessosVinculados(p));
        });
        return processosVinculados;
    }

    /**
     * Realiza a exclusão de um processo
     *
     * @param id Identificador do processo a ser excluido
     * @throws SimtrRecursoDesconhecidoException Lançada caso o processo não seja localizado sob o identificador informado.
     * @throws SimtrEstadoImpeditivoException Lançada caso identificado vinculos com registros de outras entidades existentes com o processo que impedem a sua
     *         exclusão
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(Integer id) {
        try {
            Processo processo = this.getById(id);
            if (Objects.nonNull(processo)) {
                super.delete(processo);
            } else {
                String mensagem = MessageFormat.format("PS.d.001 - Processo não localizado sob identificador informado. ID = {0}", id);
                throw new SimtrRecursoDesconhecidoException(mensagem);
            }
        } catch (PersistenceException pe) {
            Throwable problema = pe.getCause();
            while ((Objects.nonNull(problema.getCause())) && !(PSQLException.class.equals(problema.getClass()))) {
                problema = problema.getCause();
            }
            throw new SimtrEstadoImpeditivoException("PS.d.001 - ".concat(problema.getLocalizedMessage()), pe);
        }
    }

    /**
     * Aplica as alterações encaminhadas no canal de comunicação indicada.
     *
     * @param processoManutencaoDTO Objeto contendo os atributos a serem alterados na aplicação do patch. Caso os atributos sejam nulos nenhuma alteração será
     *        realizada no mesmo.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void aplicarPatch(ManterProcessoDTO processoManutencaoDTO) {
        Processo processo = mapaProcessos.get(processoManutencaoDTO.getId());
        if (Objects.nonNull(processoManutencaoDTO.getNome())) {
            processo.setNome(processoManutencaoDTO.getNome());
        }
        if (Objects.nonNull(processoManutencaoDTO.getAvatar())) {
            processo.setAvatar(processoManutencaoDTO.getAvatar());
        }
        if (Objects.nonNull(processoManutencaoDTO.getContainerBpm())) {
            processo.setNomeContainerBPM(processoManutencaoDTO.getContainerBpm());
        }
        if (Objects.nonNull(processoManutencaoDTO.getControlaValidadeDocumento())) {
            processo.setControlaValidade(processoManutencaoDTO.getControlaValidadeDocumento());
        }
        if (Objects.nonNull(processoManutencaoDTO.getDossieDigital())) {
            processo.setIndicadorGeracaoDossie(processoManutencaoDTO.getDossieDigital());
        }
        if (Objects.nonNull(processoManutencaoDTO.getProcessoBpm())) {
            processo.setNomeProcessoBPM(processoManutencaoDTO.getProcessoBpm());
        }
        if (Objects.nonNull(processoManutencaoDTO.getTipoPessoa())) {
            processo.setTipoPessoa(processoManutencaoDTO.getTipoPessoa());
        }
        this.update(processo);
    }
    // *********** Métodos Privados ***********//
}
