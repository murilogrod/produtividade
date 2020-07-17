package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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

import com.google.common.base.Strings;

import br.gov.caixa.simtr.controle.excecao.SimtrCadastroException;
import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.helper.CadastroHelper;
import br.gov.caixa.simtr.controle.vo.excecao.PendenciasVO;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.OpcaoAtributo;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.SICPFCampoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SICPFModoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.AtributoExtracaoManutencaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.TipoDocumentoManutencaoDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class TipoDocumentoServico extends AbstractService<TipoDocumento, Integer> {

    @Inject
    private EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(TipoDocumentoServico.class.getName());

    @EJB
    private FuncaoDocumentalServico funcaoDocumentalServico;
    
    @EJB
    private AtributoExtracaoServico atributoExtracaoServico;
    
    @EJB
    private OpcaoAtributoServico opcaoAtributoServico;
    
    @EJB
    private CadastroHelper cadastroHelper;

    private Calendar dataHoraUltimaAlteracao;
    private final Map<Integer, TipoDocumento> mapaById = new HashMap<>();
    private final Map<String, TipoDocumento> mapaByNome = new HashMap<>();
    private final Map<String, TipoDocumento> mapaByTipologia = new HashMap<>();

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @PermitAll
    public Calendar getDataHoraUltimaAlteracao() {
        return dataHoraUltimaAlteracao;
    }

    @PostConstruct
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void carregarMapas() {
        // Captura a data de ultima atualização realizada com qualquer registro da tabela de tipo de documento
        String jpqlUltimaAlteracao = " SELECT MAX(td.dataHoraUltimaAlteracao) FROM TipoDocumento td ";
        TypedQuery<Calendar> queryUltimaAlteracao = this.entityManager.createQuery(jpqlUltimaAlteracao, Calendar.class);
        Calendar ultimaAlteracao = queryUltimaAlteracao.getSingleResult();

        // Verifica se a data armazenada no EJB como base de ultima atualização é nula ou menor do que a retornada na consulta
        if (Objects.isNull(this.dataHoraUltimaAlteracao) || dataHoraUltimaAlteracao.before(ultimaAlteracao)) {

            StringBuilder jpqlTipoDoc = new StringBuilder();
            jpqlTipoDoc.append(" SELECT DISTINCT td FROM TipoDocumento td ");
            jpqlTipoDoc.append(" LEFT JOIN FETCH td.funcoesDocumentais fd ");
            jpqlTipoDoc.append(" LEFT JOIN FETCH td.atributosExtracao ae ");
            jpqlTipoDoc.append(" LEFT JOIN FETCH ae.opcoesAtributo oa ");
            jpqlTipoDoc.append(" WHERE (td.atributosExtracao IS EMPTY) OR (ae.ativo = true) ");

            List<TipoDocumento> tipologia = this.entityManager.createQuery(jpqlTipoDoc.toString(), TipoDocumento.class).getResultList();

            // Limpa os mapas de armazenamento da tipologia
            this.mapaById.clear();
            this.mapaByNome.clear();

            // Recarrega os mapas pelas diferentes visões possiveis
            tipologia.forEach(tipoDocumento -> {
                this.mapaById.put(tipoDocumento.getId(), tipoDocumento);
                this.mapaByNome.put(tipoDocumento.getNome(), tipoDocumento);
                if(tipoDocumento.getCodigoTipologia() != null) {
                    this.mapaByTipologia.put(tipoDocumento.getCodigoTipologia(), tipoDocumento);
                }
            });

            // Atualiza a data armazenada no EJB como base para comparação da ultima alteração
            this.dataHoraUltimaAlteracao = ultimaAlteracao;
        }
    }

    @PermitAll
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public TipoDocumento getById(final Integer id) {
        this.carregarMapas();
        return mapaById.get(id);
    }

    @PermitAll
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public TipoDocumento getByNome(final String nome) {
        return this.getByNome(nome, Boolean.FALSE);
    }

    @PermitAll
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public TipoDocumento getByNome(final String nome, final boolean validaExistencia) {
        this.carregarMapas();
        TipoDocumento tipoDocumento = mapaByNome.get(nome);
        if (tipoDocumento == null) {
            throw new SimtrRequisicaoException(MessageFormat.format("TDS.gBN.001 - Tipo de Documento {0} não foi encontrado na base de dados", nome));
        }
        return tipoDocumento;
    }

    @PermitAll
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public TipoDocumento getByTipologia(final String codigoTipologia) {
        this.carregarMapas();
        return mapaByTipologia.get(codigoTipologia);
    }
    
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public TipoDocumento getByTipologia(final String codigoTipologia, final boolean validaExistencia) {
        this.carregarMapas();
        TipoDocumento tipoDocumento = mapaByTipologia.get(codigoTipologia);
        if (tipoDocumento == null) {
            throw new SimtrRequisicaoException(MessageFormat.format("TDS.gBT.001 - Tipologia de Documento {0} não foi encontrado na base de dados", codigoTipologia));
        }
        return tipoDocumento;
    }

    /**
     * Metodo utilizado para localizar um tipo de documento baseado em seu identificador respeitando a condição do contexto transacional. O tipo de documento é
     * capturado com as funções documentais e atributos extração vinculados
     *
     * @param id Identificador do tipo de documento a ser localizada
     * @return Tipo de Documento localizado ou nulo o mesmo não seja localizada
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public TipoDocumento findById(final Integer id) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT td FROM TipoDocumento td ");
        jpql.append(" LEFT JOIN FETCH td.funcoesDocumentais fd ");
        jpql.append(" LEFT JOIN FETCH td.atributosExtracao ae ");
        jpql.append(" LEFT JOIN FETCH ae.opcoesAtributo oa ");
        jpql.append(" WHERE td.id = :id ");

        TypedQuery<TipoDocumento> query = this.entityManager.createQuery(jpql.toString(), TipoDocumento.class);
        query.setParameter("id", id);

        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }

    /**
     * Retorna a lista de objetos com todos os tipos documentais cadastrados e atualizados até o momento da consulta. O retorno dos atributos não inclui os
     * inativos.
     *
     * @return Lista de Tipos de Documentos localizados
     */
    @Override
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoDocumento> list() {
        this.carregarMapas();
        List<TipoDocumento> lista = this.mapaById.values().stream()
                                                 .filter(tipoDocumento -> !ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_PF.equals(tipoDocumento.getCodigoTipologia()))
                                                 .filter(tipoDocumento -> !ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_PJ.equals(tipoDocumento.getCodigoTipologia()))
                                                 .collect(Collectors.toList());
        return new ArrayList<>(lista);
    }

    /**
     * Retorna a lista de objetos com todos os tipos documentais cadastrados e atualizados até o momento da consulta. O retorno dos atributos inclui os inativos.
     *
     * @return Lista de Tipos de Documentos localizados
     */
    @PermitAll
    public List<TipoDocumento> listInativosIncluidos() {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT td FROM TipoDocumento td ");
        jpql.append(" LEFT JOIN FETCH td.funcoesDocumentais fd ");
        jpql.append(" LEFT JOIN FETCH td.atributosExtracao ae ");
        jpql.append(" LEFT JOIN FETCH ae.opcoesAtributo oa ");

        TypedQuery<TipoDocumento> query = this.entityManager.createQuery(jpql.toString(), TipoDocumento.class);

        return query.getResultList();
    }

    /**
     * Retorna a lista de tipos de documentos que tenham caracteristicas definidas que atendam aos indicadores encaminhados como verdadeiro.
     *
     * @param usoApoioNegocio Indicador se deve ser incluido os tipos de documento utilizados na visão do apoio ao negócio.
     * @param usoDossieDigital Indicador se deve ser incluido os tipos de documento utilizados na visão do dossiê digital.
     * @param usoProcessoAdministrativo Indicador se deve ser incluido os tipos de documento utilizados na visão do processo administrativo.
     * @return Lista de tipos de documentos identificados que atendem aos filtros encaminhados
     */
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<TipoDocumento> listTipologiaByCategoria(boolean usoApoioNegocio, boolean usoDossieDigital, boolean usoProcessoAdministrativo) {
        this.carregarMapas();
        return this.mapaById.values().stream()
                            .filter(td -> {
                                // Retorna verdade indicando que o tipo analisado deve ser mapeado no retorno se o tipo indicar uso na cateria solicitada
                                if ((usoApoioNegocio) && (td.getUsoApoioNegocio())) {
                                    boolean inclui = Boolean.TRUE;
                                    inclui = inclui && !ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_PF.equals(td.getCodigoTipologia());
                                    inclui = inclui && !ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_PJ.equals(td.getCodigoTipologia());
                                    return inclui;
                                } else if ((usoDossieDigital) && (td.getUsoDossieDigital())) {
                                    return Boolean.TRUE;
                                } else if ((usoProcessoAdministrativo) && (td.getUsoProcessoAdministrativo())) {
                                    return Boolean.TRUE;
                                }

                                // Retorna falso indicando que o tipo analisado não deve ser mapeado no retorno
                                return Boolean.FALSE;
                            }).collect(Collectors.toList());
    }

    /**
     * Método utilizado para salvar um novo registro de tipo de documento com a lista de identificadores de funções documentais que devem ser associados iniclamente
     * ao tipo e a lista de atributos extração inicialmente definidos.
     *
     * @param tipoDocumento Objeto que representa o prototipo do tipo de documento a ser persistido.
     * @param identificadoresFuncaoDocumentalInclusaoVinculo Lista de identificadores das funções documentais que devem ser associadas ao tipo de documento em
     *        persistência.
     * @throws SimtrCadastroException Lançada caso ocorra algum erro de validação das informações encaminhadas no objeto que representa o tipo de documento a ser
     *         persistido
     * @throws SimtrEstadoImpeditivoException Lançada caso ocorra algum problema de persistência relativo a integridade dos dados na inclusão do tipo de documento.
     * @throws SimtrRecursoDesconhecidoException Lançada caso alguma função documental não seja localizada pelo identificador informado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void save(TipoDocumento tipoDocumento, List<Integer> identificadoresFuncaoDocumentalInclusaoVinculo) {

        // Atualiza a data e hora de ultima alteração do registro para viabilizar a sensibilização de nova carga dos mapas
        tipoDocumento.setDataHoraUltimaAlteracao(Calendar.getInstance());

        // Realiza a validação do objeto encaminhado
        this.validaInclusaoTipoDocumento(tipoDocumento);

        try {
            // Salva o registro incluindo no contexto transacional
            this.save(tipoDocumento);

            // Percorre todos os elementos indicados na lista, captura o tipo de documento no contexto transacional e inclui o vinculo com a função
            if (Objects.nonNull(identificadoresFuncaoDocumentalInclusaoVinculo) && !identificadoresFuncaoDocumentalInclusaoVinculo.isEmpty()) {
                this.insereVinculosFuncoes(tipoDocumento, identificadoresFuncaoDocumentalInclusaoVinculo);
            }

        } catch (PersistenceException pe) {
            // Percorre as exceções até identificar a causa raiz do problema
            Throwable problema = pe.getCause();
            while ((Objects.nonNull(problema.getCause())) && !(PSQLException.class.equals(problema.getClass()))) {
                problema = problema.getCause();
            }
            // Lança uma exceção indicando o estado impeditivo de exclusão devido a integridade de dados
            throw new SimtrEstadoImpeditivoException("TDS.s.001 - ".concat(problema.getLocalizedMessage()), pe);
        }
    }

    /**
     * Método utilizado para salvar um novo registro de tipo de documento com a lista de identificadores de funções documentais que devem ser associados iniclamente
     * ao tipo e a lista de atributos extração inicialmente definidos.
     *
     * @param idTipoDocumento Identificador do tipo de documento a ser utilizado como referência na inclusão do atributo de extração.
     * @param idAtributoPartilha Identificador do atributo a ser utilizado como referência na partilha da informação.
     * @param atributoExtracao Prototipo de atributos extração que deve ser criado e associado ao tipo de documento indicado.
     * @throws SimtrCadastroException Lançada caso ocorra algum erro de validação das informações encaminhadas no objeto que representa o atributo extração a ser
     *         persistido
     * @throws SimtrEstadoImpeditivoException Lançada caso ocorra algum problema de persistência relativo a integridade dos dados na inclusão do atributo.
     * @throws SimtrRecursoDesconhecidoException Lançada caso o tipo de documento ou atributo partilha não sejam localizados pelos identificadores informados
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void saveAtributoExtracao(Integer idTipoDocumento, Integer idAtributoPartilha, AtributoExtracao atributoExtracao) {

        // Captura o tipo de documento dentro de um contexto transacional baseado no identificador e verifica se trata-se de um tipo de documento valido
        TipoDocumento tipoDocumento = this.findById(idTipoDocumento);
        String mensagemValidacaoTipoDocumento = MessageFormat.format("TDS.sAE.001 - Tipo Documento não localizado sob o identificador informado. ID = {0}", idTipoDocumento);
        this.validaRecursoLocalizado(tipoDocumento, mensagemValidacaoTipoDocumento);

        // Caso tenha sido indicado identificador para atributo de partilha, captura o atributo indicado vinculado ao tipo de documento localizado e lança exceção caso
        // não tenha relação.
        if (Objects.nonNull(idAtributoPartilha) && idAtributoPartilha > 0) {
        	
        	String mensagemValidacaoAtributoPartilha = MessageFormat.format("TDS.sAE.002 - Atributo indicado para partilha não localizado sob o identificador informado. ID = {0}", idAtributoPartilha);
            AtributoExtracao atributoPartilha = tipoDocumento.getAtributosExtracao().stream()
                                                             .filter(ae -> idAtributoPartilha.equals(ae.getId())).findFirst()
                                                             .orElseThrow(() -> new SimtrRecursoDesconhecidoException(mensagemValidacaoAtributoPartilha));
            
            atributoExtracao.setAtributoPartilha(atributoPartilha);
        }

        // Atualiza a data e hora de ultima alteração do registro para viabilizar a sensibilização de nova carga dos mapas
        // tipoDocumento.setDataHoraUltimaAlteracao(Calendar.getInstance());
        try {
            // Valida o atributo extração enviado
            this.validaInclusaoAtributoExtracao(tipoDocumento, atributoExtracao);
            
            atributoExtracao.setTipoDocumento(tipoDocumento);
            this.entityManager.persist(atributoExtracao);
        } catch (PersistenceException pe) {
            // Percorre as exceções até identificar a causa raiz do problema
            Throwable problema = pe.getCause();
            while ((Objects.nonNull(problema.getCause())) && !(PSQLException.class.equals(problema.getClass()))) {
                problema = problema.getCause();
            }
            // Lança uma exceção indicando o estado impeditivo de exclusão devido a integridade de dados
            throw new SimtrEstadoImpeditivoException("TDS.sAE.003 - ".concat(problema.getLocalizedMessage()), pe);
        }
    }

    /**
     * Remove o tipo de documento solicitado e todos os seus atributos vinculados. A relação com as funções são removidas por consequência.
     *
     * @param id Identificador do tipo de documento a ser excluido
     * @throws SimtrEstadoImpeditivoException Lançada caso ocorra algum problema de persistência relativo a integridade dos dados na exclusão do tipo de documento
     * @throws SimtrRecursoDesconhecidoException Lançada caso o tipo de documento não seja localizado pelo identificador informado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void delete(Integer id) {
        // Captura o tipo de documento dentro de um contexto transacional baseado no identificador e verifica se trata-se de um tipo de documento valido
        TipoDocumento tipoDocumentoExclusao = this.findById(id);
        String mensagemValidacao = MessageFormat.format("TDS.d.001 - Tipo Documento não localizado para o identificador informado. ID = {0}", id);
        this.validaRecursoLocalizado(tipoDocumentoExclusao, mensagemValidacao);

        try {
            // Captura a lista de atributos vinculados ao tipo de documento como um array
            AtributoExtracao[] listaAtributos = new AtributoExtracao[] {};
            if(tipoDocumentoExclusao.getAtributosExtracao() != null) {
              tipoDocumentoExclusao.getAtributosExtracao().toArray(new AtributoExtracao[] {});
            }

            // Realiza a exclusão de todos os atributos extração vinculados ao tipo de documento previamente para garantir a integridade de dados
            this.removeAtributosExtracao(tipoDocumentoExclusao, listaAtributos);

            // Realiza a exclusão do tipo de documento indicado
            this.delete(tipoDocumentoExclusao);

            // Altera a data hora da ultima atualização de um registro qualquer que tenha permanecido no sistema para forçar a atualiação dos mapas.
            this.list().stream().filter(td -> !id.equals(td.getId())).findAny().ifPresent(tipoDocumento -> {
                this.findById(tipoDocumento.getId()).setDataHoraUltimaAlteracao(Calendar.getInstance());
            });

        } catch (PersistenceException pe) {
            // Percorre as exceções até identificar a causa raiz do problema
            Throwable problema = pe.getCause();
            while ((Objects.nonNull(problema.getCause())) && !(PSQLException.class.equals(problema.getClass()))) {
                problema = problema.getCause();
            }
            // Lança uma exceção indicando o estado impeditivo de exclusão devido a integridade de dados
            String mensagem = MessageFormat.format("TDS.d.002 - O Tipo de documento não pode ser excluido devido a vinculos pré-existentes, devendo neste caso inativar o registro. Motivo: [{0}]", problema.getLocalizedMessage());
            throw new SimtrEstadoImpeditivoException(mensagem, pe);
        }
    }

    /**
     * Remove o atributo extração vinculado ao tipo de documento indicado.
     *
     * @param idTipoDocumento Identificador do tipo de documento de vinculação do atributo a ser excluido
     * @param idAtributo Identificador do atributo de extração a ser excluido
     * @throws SimtrEstadoImpeditivoException Lançada caso ocorra algum problema de persistência relativo a integridade dos dados na exclusão do atributo.
     * @throws SimtrRecursoDesconhecidoException Lançada caso o tipo de documento não seja localizado pelo identificador informado ou o atributo indicado não esteja
     *         vinculado ao tipo de documento informado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void deleteAtributoExtracao(Integer idTipoDocumento, Integer idAtributo) {
        // Captura o tipo de documento dentro de um contexto transacional baseado no identificador e verifica se trata-se de um tipo de documento valido
        TipoDocumento tipoDocumento = this.findById(idTipoDocumento);
        String mensagemValidacaoTipoDocumento = MessageFormat.format("TDS.dAE.001 - Tipo Documento não localizado para o identificador informado. ID = {0}", idTipoDocumento);
        this.validaRecursoLocalizado(tipoDocumento, mensagemValidacaoTipoDocumento);

        // Verifica se o atributo informado esta vinculado ao tipo de documento
        String mensagemValidacaoAtributo = MessageFormat.format("TDS.dAE.002 - Atributo não localizado para o identificador informado. ID = {0}", idAtributo);
        AtributoExtracao atributo = tipoDocumento.getAtributosExtracao().stream()
                                                 .filter(ae -> ae.getId().equals(idAtributo))
                                                 .findFirst().orElseThrow(() -> new SimtrRecursoDesconhecidoException(mensagemValidacaoAtributo));

        
        try {
            //Remove as opções do atributo
            atributo.getOpcoesAtributo().forEach(opcao -> {
              this.removeOpcoesAtributoExtracao(atributo, opcao);
            });
            
            // Remove o atributo do tipo de documento
            this.removeAtributosExtracao(tipoDocumento, atributo);
        } catch (PersistenceException pe) {
            // Percorre as exceções até identificar a causa raiz do problema
            Throwable problema = pe.getCause();
            while ((Objects.nonNull(problema.getCause())) && !(PSQLException.class.equals(problema.getClass()))) {
                problema = problema.getCause();
            }
            // Lança uma exceção indicando o estado impeditivo de exclusão devido a integridade de dados
            throw new SimtrEstadoImpeditivoException("TDS.dAE.003 - ".concat(problema.getLocalizedMessage()), pe);
        }

        // Atualiza a data/hora de realização da ultima alteração realizada no tipo de documento
        // tipoDocumento.setDataHoraUltimaAlteracao(Calendar.getInstance());
    }

    /**
     * 
     * @param idTipoDocumento
     * @param idAtributo
     * @param idOpcao
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void deleteOpcaoAtributoExtracao(Integer idTipoDocumento, Integer idAtributo, Integer idOpcao) {
        TipoDocumento tipoDocumento = this.findById(idTipoDocumento);
        String mensagemValidacaoTipoDocumento = MessageFormat.format("TDS.dOAE.001 -Tipo de Documento não localizado sob o identificador informado. ID = {0}", idTipoDocumento);
        this.validaRecursoLocalizado(tipoDocumento, mensagemValidacaoTipoDocumento);

        String mensagemValidacaoAtributo = MessageFormat.format("TDS.dOAE.002 - O atributo extração informado não foi localizado vinculado ao tipo de documento informado. ID = {0}", idAtributo);
        AtributoExtracao atributo = tipoDocumento.getAtributosExtracao().stream()
                                                 .filter(ae -> ae.getId().equals(idAtributo))
                                                 .findFirst().orElseThrow(() -> new SimtrRecursoDesconhecidoException(mensagemValidacaoAtributo));

        String mensagemValidacaoOpcaoAtributo = MessageFormat.format("TDS.dOAE.003 - A opção do atributo não foi localizado vinculado ao atributo extração informado. ID = {0}", idOpcao);
        OpcaoAtributo opcaoAtributo = atributo.getOpcoesAtributo().stream()
                .filter(opcAtrib -> opcAtrib.getId().equals(idOpcao))
                .findFirst().orElseThrow(() -> new SimtrRecursoDesconhecidoException(mensagemValidacaoOpcaoAtributo));
        try {
            this.removeOpcoesAtributoExtracao(atributo, opcaoAtributo);
        } catch (PersistenceException pe) {
            Throwable problema = pe.getCause();
            while ((Objects.nonNull(problema.getCause())) && !(PSQLException.class.equals(problema.getClass()))) {
                problema = problema.getCause();
            }
            throw new SimtrEstadoImpeditivoException("TDS.dOAE.004 - ".concat(problema.getLocalizedMessage()), pe);
        }

        // tipoDocumento.setDataHoraUltimaAlteracao(Calendar.getInstance());
    }
    
    /**
     * Aplica as alterações encaminhadas no tipo de documento indicado.
     *
     * @param id Identificador do tipo de documento a ser alterado.
     * @param tipoDocumentoManutencaoDTO Objeto contendo os atributos a serem alterados na aplicação do patch
     * @throws SimtrCadastroException Lançada caso exista alguma pendência impeditiva para inclusão indicando qual o problema relacionado identificado no momento da
     *         validação das informações.
     * @throws SimtrEstadoImpeditivoException Lançada caso ocorra algum problema de persistência relativo a integridade dos dados na alteração do tipo de documento
     * @throws SimtrRecursoDesconhecidoException Lançada caso o tipo de documento não seja localizado pelo identificador informado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void aplicaPatch(Integer id, TipoDocumentoManutencaoDTO tipoDocumentoManutencaoDTO) {

        // Captura o tipo de documento no contexto transacional para viabilizar as alterações de dados e valida se o mesmo foi identificado
        TipoDocumento tipoDocumento = this.findById(id);
        String mensagemValidacao = MessageFormat.format("TDS.aP.001 - Tipo Documento não localizado para o identificador informado. ID = {0}", id);
        this.validaRecursoLocalizado(tipoDocumento, mensagemValidacao);

        // Valida as alterações propostas se possuem atendem as regras negociais definidas
        this.validaManutencaoTipoDocumento(tipoDocumento, tipoDocumentoManutencaoDTO);

        // Atualiza a data e hora de ultima alteração do registro para viabilizar a sensibilização de nova carga dos mapas
        tipoDocumento.setDataHoraUltimaAlteracao(Calendar.getInstance());

        // Atualiza o nome do tipo de documento
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getNome())) {
            tipoDocumento.setNome(tipoDocumentoManutencaoDTO.getNome());
        }

        // Altera o tipo de pessoa associado ao registro do tipo de documento
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getTipoPessoa())) {
            tipoDocumento.setTipoPessoaEnum(tipoDocumentoManutencaoDTO.getTipoPessoa());
        } else {
        	tipoDocumento.setTipoPessoaEnum(null);
        }
        
        // Define o prazo de validade se por ventura enviado. Caso encaminhado valor negativo, define o campo como nulo
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getPrazoValidadeDias())) {
            tipoDocumento.setPrazoValidade(tipoDocumentoManutencaoDTO.getPrazoValidadeDias() < 0 ? null : tipoDocumentoManutencaoDTO.getPrazoValidadeDias());
        }

        // Caso seja indicado valor para data de validade autocontida, atribui o valor apropriado e anula o campo do prazo se indição de autocontida for verdadeiro
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getValidadeAutoContida())) {
            if (tipoDocumentoManutencaoDTO.getValidadeAutoContida()) {
                tipoDocumento.setValidadeAutoContida(Boolean.TRUE);
                tipoDocumento.setPrazoValidade(null);
            } else {
                tipoDocumento.setValidadeAutoContida(Boolean.FALSE);
            }
        }

        // Atribui o valor para o codigo de tipologia. Caso enviado uma string vazia, define o campo como nulo.
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getCodigoTipologia())) {
            tipoDocumento.setCodigoTipologia(tipoDocumentoManutencaoDTO.getCodigoTipologia().isEmpty() ? null : tipoDocumentoManutencaoDTO.getCodigoTipologia());
        }

        // Atribui o valor para o nome da classe GED. Caso enviado uma string vazia, define o campo como nulo
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getNomeClasseSIECM())) {
            tipoDocumento.setNomeClasseSIECM(tipoDocumentoManutencaoDTO.getNomeClasseSIECM().isEmpty() ? null : tipoDocumentoManutencaoDTO.getNomeClasseSIECM());
        }

        // Atribui o valor definido para o indicador de possibilidade de reuso para o documento
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getIndicadorReuso())) {
            tipoDocumento.setReuso(tipoDocumentoManutencaoDTO.getIndicadorReuso());
        }

        // Atribui o valor definido para o indicador de utilizaçao do tipo de documento no contexto do Apoio ao Negócio
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getIndicadorUsoApoioNegocio())) {
            tipoDocumento.setUsoApoioNegocio(tipoDocumentoManutencaoDTO.getIndicadorUsoApoioNegocio());
        }

        // Atribui o valor definido para o indicador de utilizaçao do tipo de documento no contexto do Dossiê Digital
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getIndicadorUsoDossieDigital())) {
            tipoDocumento.setUsoDossieDigital(tipoDocumentoManutencaoDTO.getIndicadorUsoDossieDigital());
        }

        // Atribui o valor definido para o indicador de utilizaçao do tipo de documento no contexto do Processo Administrativo Eletrônico
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getIndicadorUsoProcessoAdministrativo())) {
            tipoDocumento.setUsoProcessoAdministrativo(tipoDocumentoManutencaoDTO.getIndicadorUsoProcessoAdministrativo());
        }

        // Atribui o valor definido para o a minuta de relatório associada ao tipo de documento. Caso enviado uma string vazia, define o campo como nulo
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getNomeArquivoMinuta())) {
            tipoDocumento.setNomeArquivoMinuta(tipoDocumentoManutencaoDTO.getNomeArquivoMinuta().isEmpty() ? null : tipoDocumentoManutencaoDTO.getNomeArquivoMinuta());
        }

        // Atribui o valor definido para as tags associadas ao tipo de documento. Caso enviado uma string vazia, define o campo como nulo
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getTags())) {
            tipoDocumento.setTags(tipoDocumentoManutencaoDTO.getTags().isEmpty() ? null : tipoDocumentoManutencaoDTO.tagsToCSV());
        }

        // Atribui o valor encaminhado para o indicador de encaminhamento a solução de validação cadastral dos dados do documento.
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getIndicadorValidacaoCadastral())) {
            tipoDocumento.setEnviaAvaliacaoCadastral(tipoDocumentoManutencaoDTO.getIndicadorValidacaoCadastral());
        }

        // Atribui o valor encaminhado para o indicador de encaminhamento a solução de validação documental quanto ao indice de fraude.
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getIndicadorValidacaoDocumental())) {
            tipoDocumento.setEnviaAvaliacaoDocumental(tipoDocumentoManutencaoDTO.getIndicadorValidacaoDocumental());
        }

        // Atribui o valor encaminhado para o indicador de encaminhamento a solução de validação no SICOD.
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getIndicadorValidacaoSicod())) {
            tipoDocumento.setEnviaAvaliacaoSICOD(tipoDocumentoManutencaoDTO.getIndicadorValidacaoSicod());
        }

        // Atribui o valor encaminhado para o indicador de encaminhamento a solução de extração de dados externa.
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getIndicadorExtracaoExterna())) {
            tipoDocumento.setEnviaExtracaoExterna(tipoDocumentoManutencaoDTO.getIndicadorExtracaoExterna());
        }

        // Atribui o valor encaminhado para o indicador de permissão de extração de dados na janela M+0
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getIndicadorExtracaoM0())) {
            tipoDocumento.setPermiteExtracaoM0(tipoDocumentoManutencaoDTO.getIndicadorExtracaoM0());
        }

        // Atribui o valor encaminhado para o indicador de permissão para manutenção de multiplos registro de documento ativos e validos para novos negócios.
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getIndicadorMultiplos())) {
            tipoDocumento.setPermiteMultiplosAtivos(tipoDocumentoManutencaoDTO.getIndicadorMultiplos());
        }

        // Atribui o valor encaminhado para a definição do icone da caixa de extração de dados do tipo de documento
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getAvatar())) {
            tipoDocumento.setAvatar(tipoDocumentoManutencaoDTO.getAvatar());
        }

        // Atribui o valor encaminhado para a definição de cor da caixa de extração de dados do tipo de documento
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getCorRGB())) {
            tipoDocumento.setCorRGB(tipoDocumentoManutencaoDTO.getCorRGB());
        }

        if(Objects.nonNull(tipoDocumentoManutencaoDTO.getIndicadorGuardaBinarioOutsourcing())) {
            tipoDocumento.setGuardaBinarioOutsourcing(tipoDocumentoManutencaoDTO.getIndicadorGuardaBinarioOutsourcing());
        }
        
		if (Objects.nonNull(tipoDocumentoManutencaoDTO.getAtivo())) {
			tipoDocumento.setAtivo(tipoDocumentoManutencaoDTO.getAtivo());
		}  

        // Remove os vinculos com funções documentais enviados.
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getIdentificadoresFuncaoDocumentalExclusaoVinculo())
            && !tipoDocumentoManutencaoDTO.getIdentificadoresFuncaoDocumentalExclusaoVinculo().isEmpty()) {
            this.removeVinculosFuncoes(tipoDocumento, tipoDocumentoManutencaoDTO.getIdentificadoresFuncaoDocumentalExclusaoVinculo());
        }

        // Insere os novos vinculos com funções documentais enviados.
        if (Objects.nonNull(tipoDocumentoManutencaoDTO.getIdentificadoresFuncaoDocumentalInclusaoVinculo())
            && !tipoDocumentoManutencaoDTO.getIdentificadoresFuncaoDocumentalInclusaoVinculo().isEmpty()) {
            this.insereVinculosFuncoes(tipoDocumento, tipoDocumentoManutencaoDTO.getIdentificadoresFuncaoDocumentalInclusaoVinculo());
        }
        
        try {
            // Executa atualização do objeto
            this.update(tipoDocumento);
        } catch (PersistenceException pe) {
            // Percorre as exceções até identificar a causa raiz do problema
            Throwable problema = pe.getCause();
            while ((Objects.nonNull(problema.getCause())) && !(PSQLException.class.equals(problema.getClass()))) {
                problema = problema.getCause();
            }
            // Lança uma exceção indicando o estado impeditivo de exclusão devido a integridade de dados
            throw new SimtrEstadoImpeditivoException("TDS.aP.002 - ".concat(problema.getLocalizedMessage()), pe);
        }
    }

    /**
     * Aplica as alterações encaminhadas no atributo de documento vinculado ao tipo de documento indicado.
     *
     * @param idTipoDocumento Identificador do tipo de documento a ser verificado o vinculo do atributo a ser alterado.
     * @param idAtributoExtracao Identificador do atributo a ser aplicado a alteração.
     * @param atributoExtracaoManutencaoDTO Objeto contendo os atributos a serem alterados na aplicação do patch
     * @throws SimtrCadastroException Lançada caso exista alguma pendência impeditiva para inclusão indicando qual o problema relacionado identificado no momento da
     *         validação das informações.
     * @throws SimtrEstadoImpeditivoException Lançada caso ocorra algum problema de persistência relativo a integridade dos dados na alteração do atributo do
     *         atributo.
     * @throws SimtrRecursoDesconhecidoException Lançada caso o tipo de documento não seja localizado pelo identificador informado ou o atributo indicado não esteja
     *         vinculado ao tipo de documento informado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void aplicaPatchAtributoExtracao(Integer idTipoDocumento, Integer idAtributoExtracao, AtributoExtracaoManutencaoDTO atributoExtracaoManutencaoDTO) {

        // Captura o tipo de documento no contexto transacional para viabilizar as alterações de dados e valida se o mesmo foi identificado
        TipoDocumento tipoDocumento = this.findById(idTipoDocumento);
        String mensagemValidacaoTipoDocumento = MessageFormat.format("TDS.aPAE.001 - Tipo Documento não localizado para o identificador informado. ID Tipo Documento = {0}", idTipoDocumento);
        this.validaRecursoLocalizado(tipoDocumento, mensagemValidacaoTipoDocumento);

        // Verifica se o atributo indicado esta vinculado ao tipo de documento informado
        String mensagemValidacaoAtributoLocalizado = MessageFormat.format("TDS.aPAE.002 - Atributo Extração não vinculado sob o identificador informado. ID Atributo Extração = {0}", idAtributoExtracao);
        AtributoExtracao atributoExtracao = tipoDocumento.getAtributosExtracao().stream()
                                                         .filter(ae -> idAtributoExtracao.equals(ae.getId()))
                                                         .findFirst().orElseThrow(() -> new SimtrRequisicaoException(mensagemValidacaoAtributoLocalizado));
        
        // Valida as alterações propostas se possuem atendem as regras negociais definidas
        this.validaManutencaoAtributoExtracao(tipoDocumento, atributoExtracao, atributoExtracaoManutencaoDTO);

        /**
         * GERAL
         */
        // Atribui o valor definido para o nome documento utilizado como chave do registro que fica armazenado na tabela de Atributos do Documento
        if (atributoExtracaoManutencaoDTO.getNomeAtributoDocumento() != null) {
            atributoExtracao.setNomeAtributoDocumento(atributoExtracaoManutencaoDTO.getNomeAtributoDocumento());
        }
        
        // Atribui o valor definido para identificação do campo no retorno do serviço externo de extração de dados
        if (atributoExtracaoManutencaoDTO.getNomeAtributoRetorno() != null) {
            String nomeAtributoRetorno = atributoExtracaoManutencaoDTO.getNomeAtributoRetorno();
            boolean removeAtributoRetorno = nomeAtributoRetorno.isEmpty();
            
            atributoExtracao.setNomeAtributoRetorno(removeAtributoRetorno ? null : nomeAtributoRetorno);
        }
        
        // Atribui o valor encaminhado para indicar a obrigatoriedade de captura do atributo ou não.
        if (atributoExtracaoManutencaoDTO.getObrigatorio() != null) {
            atributoExtracao.setObrigatorio(atributoExtracaoManutencaoDTO.getObrigatorio());
        }

        // Atribui o valor definido para o indicador de atributo ativo
        if (atributoExtracaoManutencaoDTO.getAtivo() != null) {
            atributoExtracao.setAtivo(atributoExtracaoManutencaoDTO.getAtivo());
        }
        
        // Atribui o valor encaminhado para indicar o valor padrão em comunicações com outros sistemas.
        if (atributoExtracaoManutencaoDTO.getValorPadrao()!= null) {
            String valorPadrao = atributoExtracaoManutencaoDTO.getValorPadrao();
            atributoExtracao.setValorPadrao(valorPadrao.isEmpty() ? null : valorPadrao);
        }
        
        // Atribui o valor encaminhado para indicar o tipo de dados que deve ser encaminhado ao GED.
        if (atributoExtracaoManutencaoDTO.getTipoAtributoGeral() != null) {
            atributoExtracao.setTipoAtributoGeralEnum(atributoExtracaoManutencaoDTO.getTipoAtributoGeral());
        }
        
        // Atribui o valor encaminhado associação de campo do SICPF utilizado para validação, além da sua estrategia de validação (modo).
        if (atributoExtracaoManutencaoDTO.getSicpfCampoEnum() == null) {
            atributoExtracao.setSicpfCampoEnum(null);
            atributoExtracao.setSicpfModoEnum(null);
        } else {
            SICPFCampoEnum sicpfCampoEnum = atributoExtracaoManutencaoDTO.getSicpfCampoEnum();
            SICPFModoEnum sicpfModoEnum = atributoExtracaoManutencaoDTO.getSicpfModoEnum();
            if (sicpfModoEnum == null){
                sicpfModoEnum = atributoExtracao.getSicpfModoEnum();
            }

            atributoExtracao.setSicpfCampoEnum(sicpfCampoEnum);
            atributoExtracao.setSicpfModoEnum(sicpfModoEnum);
        }

        // Atribui o valor encaminhado para indicar se o campo deve ser utilizado no calculo da validade para novos negócios
        if (atributoExtracaoManutencaoDTO.getCalculoData() != null) {
            atributoExtracao.setUtilizadoCalculoValidade(atributoExtracaoManutencaoDTO.getCalculoData());
        }

        // Atribui o valor encaminhado para indicar se o campo trata-se de um elemento identificador de pessoa
        if (atributoExtracaoManutencaoDTO.getIdentificadorPessoa() != null) {
            atributoExtracao.setUtilizadoIdentificadorPessoa(atributoExtracaoManutencaoDTO.getIdentificadorPessoa());
        }
        
        /**
         * SIECM
         */
        // Definição do tipo de atributo
        TipoAtributoEnum tipoAtributoSIECM = atributoExtracaoManutencaoDTO.getTipoAtributoSIECM();
        if(tipoAtributoSIECM == null){
            tipoAtributoSIECM = atributoExtracao.getTipoAtributoSiecmEnum();
        } else {
            atributoExtracao.setTipoAtributoSiecmEnum(tipoAtributoSIECM);
        }
        
        // Definição de obrigatoriedade
        Boolean obrigatorioSIECM = atributoExtracaoManutencaoDTO.getObrigatorioSIECM();
        if(obrigatorioSIECM != null) {
            atributoExtracao.setObrigatorioSIECM(obrigatorioSIECM);
        }
        
        // Definição do nome de atributo e caso os não seja enviado, mantem o valor pré existente na base de dados.
        if (atributoExtracaoManutencaoDTO.getNomeAtributoSIECM() != null) {
            String nomeAtributoSIECM = atributoExtracaoManutencaoDTO.getNomeAtributoSIECM();
            boolean removeEnvioSIECM = nomeAtributoSIECM.isEmpty();
            
            atributoExtracao.setNomeAtributoSIECM(removeEnvioSIECM ? null : nomeAtributoSIECM);
            atributoExtracao.setTipoAtributoSiecmEnum(removeEnvioSIECM ? null : tipoAtributoSIECM);
            atributoExtracao.setObrigatorioSIECM(removeEnvioSIECM ? Boolean.FALSE : atributoExtracaoManutencaoDTO.getObrigatorioSIECM());
        }
        
        /**
         * SICLI
         */
        // Define utilização do tipo de atributo enviado
        TipoAtributoEnum tipoAtributoSICLI = atributoExtracaoManutencaoDTO.getTipoAtributoSICLI();
        if(tipoAtributoSICLI == null){
            tipoAtributoSICLI = atributoExtracao.getTipoAtributoSicliEnum();
        } else{ 
            atributoExtracao.setTipoAtributoSicliEnum(tipoAtributoSICLI);
        }

        // Atribui o valor encaminhado para estrutura de corpo necessaria a comunicação com o SICLI
        String nomeObjetoSICLI = atributoExtracaoManutencaoDTO.getNomeObjetoSICLI();
        if (nomeObjetoSICLI == null) {
            nomeObjetoSICLI = atributoExtracao.getNomeObjetoSICLI();
        } else {
            atributoExtracao.setNomeObjetoSICLI(nomeObjetoSICLI);
        }

        // Atribui o valor encaminhado para o elemento de ponta necessário a comunicação com o SICLI
        if (atributoExtracaoManutencaoDTO.getNomeAtributoSICLI() != null) {
            boolean removeEnvioSICLI = atributoExtracaoManutencaoDTO.getNomeAtributoSICLI().isEmpty();

            atributoExtracao.setNomeAtributoSICLI(removeEnvioSICLI ? null : atributoExtracaoManutencaoDTO.getNomeAtributoSICLI());
            atributoExtracao.setNomeObjetoSICLI(removeEnvioSICLI ? null : nomeObjetoSICLI);
            atributoExtracao.setTipoAtributoSicliEnum(removeEnvioSICLI ? null : tipoAtributoSICLI);
        }
        
        /**
         * SICOD
         */
        // Define utilização do tipo de atributo enviado
        TipoAtributoEnum tipoAtributoSICOD = atributoExtracaoManutencaoDTO.getTipoAtributoSICOD();
        if(tipoAtributoSICOD == null){
            tipoAtributoSICOD = atributoExtracao.getTipoAtributoSicodEnum();
        } else{ 
            atributoExtracao.setTipoAtributoSicodEnum(tipoAtributoSICOD);
        }
        
        // Atribui o valor encaminhado para o elemento de ponta necessário a comunicação com o SICOD
        if (atributoExtracaoManutencaoDTO.getNomeAtributoSICOD() != null) {
            boolean removeEnvioSICOD = atributoExtracaoManutencaoDTO.getNomeAtributoSICOD().isEmpty();

            atributoExtracao.setNomeAtributoSICOD(removeEnvioSICOD ? null : atributoExtracaoManutencaoDTO.getNomeAtributoSICOD());
            atributoExtracao.setTipoAtributoSicodEnum(removeEnvioSICOD ? null : tipoAtributoSICOD);
        }
       
        /**
         * PARTILHA DE DADOS
         */
        // Caso seja encaminhado o valor de um atributo de partilha, vincula o atributo indicado pelo identificador e atribui o valor definido para o modo
        if (atributoExtracaoManutencaoDTO.getIdentificadorAtributoPartilha() != null) {
            // Caso o valor encaminhado seja negativo, define os campos de atributoPartilha e modoPartilha como nulos
            if (atributoExtracaoManutencaoDTO.getIdentificadorAtributoPartilha() <= 0) {
                atributoExtracao.setAtributoPartilha(null);
                atributoExtracao.setModoPartilhaEnum(null);
                atributoExtracao.setEstrategiaPartilhaEnum(null);
            } else {
                AtributoExtracao atributoPartilha = tipoDocumento.getAtributosExtracao().stream()
                                                                 .filter(ae -> atributoExtracaoManutencaoDTO.getIdentificadorAtributoPartilha().equals(ae.getId()))
                                                                 .findFirst().get();
                
            	atributoExtracao.setAtributoPartilha(atributoPartilha);
                atributoExtracao.setModoPartilhaEnum(atributoExtracaoManutencaoDTO.getModoPartilhaEnum());
                atributoExtracao.setEstrategiaPartilhaEnum(atributoExtracaoManutencaoDTO.getEstrategiaPartilhaEnum());
            }
        }

        /**
         * FORMULARIO
         */
        // Atribui o valor encaminhado para exibição do label na contrução de formularios dinamicos baseado nessas tabelas
        if (atributoExtracaoManutencaoDTO.getNomeAtributoNegocial() != null) {
            atributoExtracao.setNomeNegocial(atributoExtracaoManutencaoDTO.getNomeAtributoNegocial());
        }
        
        // Atribui o valor encaminhado para indicar o tipo de campo a ser apresentado nos casos de montagem de formulário para extração de dados.
        if (atributoExtracaoManutencaoDTO.getTipoCampoEnum() != null) {
            atributoExtracao.setTipoCampoEnum(atributoExtracaoManutencaoDTO.getTipoCampoEnum());
        }

        // Atribui o valor encaminhado para indicar a orientação apresentada ao operador na montagem de formulários dinamicos para o tipo de documento.
        if (atributoExtracaoManutencaoDTO.getOrientacaoPreenchimento() != null) {
            String orientacao = atributoExtracaoManutencaoDTO.getOrientacaoPreenchimento();
            atributoExtracao.setOrientacaoPreenchimento(orientacao.isEmpty() ? null : orientacao);
        }
        
        // Atribui o valor encaminhado para indicar a expressão de interface utilizada na montagem de formulários dinamicos para o tipo de documento.
        if (atributoExtracaoManutencaoDTO.getExpressaoInterface() != null) {
            String expressaoInterface = atributoExtracaoManutencaoDTO.getExpressaoInterface();
            atributoExtracao.setExpressaoInterface(expressaoInterface.isEmpty() ? null : expressaoInterface);
        }

        // Atribui o valor encaminhado para indicar o grupo de informações que o atributo pertence.
        if (atributoExtracaoManutencaoDTO.getGrupoAtributo() != null) {
            Integer grupoAtributo = atributoExtracaoManutencaoDTO.getGrupoAtributo();
            atributoExtracao.setGrupoAtributo(grupoAtributo < 0 ? null : grupoAtributo);
        }
        
        // Atribui o valor encaminhado para indicar a ordem de apresentação do campo na montagem do formulário dinamico.
        if (atributoExtracaoManutencaoDTO.getOrdemApresentacao() != null) {
            Integer ordemApresntacao = atributoExtracaoManutencaoDTO.getOrdemApresentacao();
            
            
            atributoExtracao.setOrdemApresentacao(ordemApresntacao < 0 ? null : ordemApresntacao);
        }
        
        // Atribui o valor encaminhado para indicar se o campo trata-se de um elemento presente no documento
        if (atributoExtracaoManutencaoDTO.getPresenteDocumento() != null) {
            atributoExtracao.setPresenteDocumento(atributoExtracaoManutencaoDTO.getPresenteDocumento());
        }

        // Atribui o valor encaminhado para indicar o percentual de informação permitido modificar no fluxo de ajuste dos dados.
        if (atributoExtracaoManutencaoDTO.getPercentualAlteracaoPermitido() != null) {
            Integer percentualAlteracao = atributoExtracaoManutencaoDTO.getPercentualAlteracaoPermitido();
            atributoExtracao.setPercentualAlteracaoPermitido(percentualAlteracao < 0 ? null : percentualAlteracao);
        }
        
        // Atualiza a data e hora de ultima alteração do registro para viabilizar a sensibilização de nova carga dos mapas
        // tipoDocumento.setDataHoraUltimaAlteracao(Calendar.getInstance());
        
        try {
        	// Executa a atualização do objeto
        	this.entityManager.merge(atributoExtracao);
        } catch (PersistenceException pe) {
            // Percorre as exceções até identificar a causa raiz do problema
            Throwable problema = pe.getCause();
            while ((Objects.nonNull(problema.getCause())) && !(PSQLException.class.equals(problema.getClass()))) {
                problema = problema.getCause();
            }
            // Lança uma exceção indicando o estado impeditivo de exclusão devido a integridade de dados
            throw new SimtrEstadoImpeditivoException("TDS.aP.002 - ".concat(problema.getLocalizedMessage()), pe);
        }
    }

    /**
     * Realiza a validação relativa as regras que devem ser atendidas na inclusão de um tipo de documento
     *
     * @param tipoDocumento Objeto que representa o tipo de documento a ser persistido
     * @throws SimtrCadastroException Lançada caso exista alguma pendência impeditiva para inclusão do tipo de documento indicando qual o problema relacionado
     *         identificado no momento da validação das informações
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    private void validaInclusaoTipoDocumento(TipoDocumento tipoDocumento) {
        // Inicializa a lista para armazenar as pendencias identificadas durante a validação
        Map<String, List<String>> mapaPendencias = new HashMap<>();

        // ******** Inicio das validações obrigatoriedade de campos independentes ***********//
        if (Objects.isNull(tipoDocumento.getNome())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.NOME_TIPO_DOCUMENTO, "Nome do tipo de documento é obrigatório");
        }

        if (Objects.isNull(tipoDocumento.getValidadeAutoContida())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_VALIDADE_AUTOCONTIDA, "Indicação de validade autocontida é obrigatório");
        }

        if (Objects.isNull(tipoDocumento.getUsoApoioNegocio())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_APOIO_NEGOCIO, "Indicação de uso na visão do apoio ao negócio é obrigatório");
        }

        if (Objects.isNull(tipoDocumento.getUsoDossieDigital())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_DOSSIE_DIGITAL, "Indicação de uso na visão do dossiê digital é obrigatório");
        }

        if (Objects.isNull(tipoDocumento.getUsoProcessoAdministrativo())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_PROCESSO_ADMINISTRATIVO, "Indicação de uso na visão do processo admiistrativo eletrônico é obrigatório");
        }

        if (Objects.isNull(tipoDocumento.getEnviaAvaliacaoCadastral())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_CADASTRAL, "Indicação de envio para avaliação cadastral é obrigatório");
        }

        if (Objects.isNull(tipoDocumento.getEnviaExtracaoExterna())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_EXTRACAO_EXTERNA, "Indicação de envio para extração de dados por empresa externa é obrigatório");
        }

        if (Objects.isNull(tipoDocumento.getPermiteMultiplosAtivos())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_MULTIPLOS, "Indicação de possibilidade de manutenção de múltipos registros ativos válidos para novos negócios é obrigatório");
        }

        if (Objects.isNull(tipoDocumento.getAvatar()) || tipoDocumento.getAvatar().isEmpty()) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.AVATAR, "Indicação de valor do icone (avatar) a ser utilizado para a montagem da tela de extração de dados é obrigatório");
        }

        if (Objects.isNull(tipoDocumento.getCorRGB()) || tipoDocumento.getCorRGB().isEmpty()) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.COR_BOX, "Indicação de valor utilizado na cor da caixa para a montagem da tela de extração de dados é obrigatório");
        }
        
        if (Objects.isNull(tipoDocumento.getGuardaBinarioOutsourcing())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_GUARDA_BINARIO_OUTSOURCING, "Atributo do binário do documento encaminhado para atendimento ao serviço outsourcing documental é obrigatório.");
        }
        
        if (Objects.isNull(tipoDocumento.getAtivo())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.ATIVO, "Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema  é obrigatório.");
        }    
        
        // ******** Fim das validações obrigatoriedade de campos independentes ***********//

        // Valida a obrigatóriedade do campo "ClasseGED" caso o registro tenha indicação de armazenamento dos documentos deste tipo no GED
        boolean usoDossie = Objects.nonNull(tipoDocumento.getUsoDossieDigital()) && tipoDocumento.getUsoDossieDigital();
        boolean usoPAE = Objects.nonNull(tipoDocumento.getUsoProcessoAdministrativo()) && tipoDocumento.getUsoProcessoAdministrativo();
        if ((usoDossie || usoPAE) && (Objects.isNull(tipoDocumento.getNomeClasseSIECM()) || tipoDocumento.getNomeClasseSIECM().isEmpty())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.CLASSE_SIECM, "Necessário indicar uma classe SIECM para o tipo de documento habilitado para uso junto ao Dossiê Digital e/ou Processo Administrativo");
        }

        // Valida a obrigatóriedade do campo "PermissaoExtracaoM0", caso seja indicado de extração de dados em fornecedor externo
        if ((Objects.nonNull(tipoDocumento.getEnviaExtracaoExterna())) && (tipoDocumento.getEnviaExtracaoExterna()) && (Objects.isNull(tipoDocumento.getPermiteExtracaoM0()))) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_EXTRACAO_M0, "Necessário informar se tipo de documento pode ser encaminhado para extração de dados na janela M0");
        }
        
        // Varifica se o processo gerou pendencias para algum atributo e em caso positivo lança uma exceção indicando as pendencias mapeadas.
        List<PendenciasVO> listaPendencias = mapaPendencias.entrySet().stream().map(registro -> new PendenciasVO(registro.getKey(), registro.getValue()))
                                                           .collect(Collectors.toList());
        if (!listaPendencias.isEmpty()) {
            throw new SimtrCadastroException("TDS.vITD.001 - Problemas identificados na execução da alteração.", listaPendencias);
        }
    }

    /**
     * Realiza a validação relativa as regras que devem ser atendidas na inclusão de um atributo extração
     *
     * @param tipoDocumento Objeto que representa o tipo de documento vinculador do atributo
     * @param atributoExtracao Objeto que representa o atributo extração a ser persistido
     * @throws SimtrCadastroException Lançada caso exista alguma pendência impeditiva para inclusão do atributo extração indicando qual o problema relacionado
     *         identificado no momento da validação das informações
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    private void validaInclusaoAtributoExtracao(TipoDocumento tipoDocumento, AtributoExtracao atributoExtracao) {
        // Inicializa a lista para armazenar as pendencias identificadas durante a validação
        Map<String, List<String>> mapaPendencias = new HashMap<>();

        // ******** Inicio das validações obrigatoriedade de campos independentes ***********//
        if (Objects.isNull(atributoExtracao.getNomeNegocial()) || atributoExtracao.getNomeNegocial().isEmpty()) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_NEGOCIAL, "Nome negocial utilizado para apresentação nos formularios dinâmicos para o atributo é obrigatório");
        }

        if (Objects.isNull(atributoExtracao.getAtivo())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.ATIVO, "Indicação de atributo ativo é obrigatório");
        }

        if (Objects.isNull(atributoExtracao.getUtilizadoCalculoValidade())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_CALCULO_DATA_VALIDADE, "Indicação de uso no calculo da validade para novos negócios é obrigatório");
        }

        if (Objects.isNull(atributoExtracao.getNomeAtributoDocumento()) || atributoExtracao.getNomeAtributoDocumento().isEmpty()) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_DOCUMENTO, "Nome utilizado para armazenamento do atributo é obrigatório");
        }

        if (Objects.isNull(atributoExtracao.getObrigatorio())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_OBRIGATORIO, "Indicação obrigatoriedade para o atributo é obrigatório");
        }

        if (Objects.isNull(atributoExtracao.getUtilizadoIdentificadorPessoa())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_IDENTIFICADOR_PESSOA, "Indicação de atributo utilizado para identificação de pessoa é obrigatório");
        }

        if (Objects.isNull(atributoExtracao.getPresenteDocumento())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_PRESENTE_DOCUMENTO, "Indicação de atributo presente no documento é obrigatório");
        }
        
        if (Objects.isNull(atributoExtracao.getTipoAtributoGeralEnum())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_GERAL, "Indicação do tipo de atributo geral para manipulação da informação é obrigatório");
        }
        
        if (Objects.isNull(atributoExtracao.getOrdemApresentacao())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.ORDEM_APRESENTACAO, "Indicação de ordem de apresentação para montagem de formlário dinamico é obrigatório");
        }
        
        if (Objects.isNull(atributoExtracao.getTipoCampoEnum())) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.TIPO_CAMPO, "Indicação do tipo de campo para montagem de formlário dinamico é obrigatório");
        }
        
        // ******** Fim das validações obrigatoriedade de campos independentes ***********//

        if ((Objects.nonNull(atributoExtracao.getNomeAtributoSIECM())) && !(atributoExtracao.getNomeAtributoSIECM().isEmpty())){
            // Valida a obrigatoriedade do campo "TipoAtributoSIECM" caso o registro tenha indicação de armazenamento do deste atributo junto ao GED
            if (Objects.isNull(atributoExtracao.getTipoAtributoSiecmEnum())) {
                this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SIECM, "Necessário indicar o tipo do atributo junto a classe do SIECM quando definido atributo de classe para envio");
            }

            // Valida a obrigatoriedade do campo "ObrigatorioSIECM" caso o registro tenha indicação de armazenamento do deste atributo junto ao GED
            if (Objects.isNull(atributoExtracao.getObrigatorioSIECM())) {
                this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_OBRIGATORIO_SIECM, "Necessário indicar se o atributo é obrigatório junto a classe SIECM quando habilitado para envio");
            }
        }
        
        if ((Objects.nonNull(atributoExtracao.getNomeAtributoSICOD())) && !(atributoExtracao.getNomeAtributoSICOD().isEmpty())){
            // Valida a obrigatoriedade do campo "TipoAtributoSICOD" caso o registro tenha indicação de comunicação junto ao SICOD
            if (Objects.isNull(atributoExtracao.getTipoAtributoSicodEnum())) {
                this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SICOD, "Necessário indicar o tipo do atributo junto ao SICOD quando definido atributo de atributo de comunicação");
            }
        }
        
        // Valida a obrigatoriedade do campo "ModoSICPF" caso o registro tenha definido campo para comparação do valor extraido do documento com informação oriunda do cadastro da Receita Federal
        if ((Objects.nonNull(atributoExtracao.getSicpfCampoEnum())) && (Objects.isNull(atributoExtracao.getSicpfModoEnum()))) {
            this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_CAMPO_COMPARACAO_RECEITA, "Necessário informar o modo de comparação junto a Receita Federal quando indicado o campo de comparação");
        }

        // Valida a obrigatoriedade do campo "TipoAtributoSICLI" caso o registro tenha definido valor para o campo que representa o campo de mapeamento na montagem da mensagem pata o SICLI
        if ((Objects.nonNull(atributoExtracao.getNomeAtributoSICLI())) && (!atributoExtracao.getNomeAtributoSICLI().isEmpty())){
            if (Objects.isNull(atributoExtracao.getNomeObjetoSICLI())) {
                this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.NOME_OBJETO_SICLI, "Necessário informar a estrutura de objeto do SICLI quando indicado campo para alimentação");
            }
            if (Objects.isNull(atributoExtracao.getTipoAtributoSicliEnum())) {
                this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SICLI, "Necessário informar o tipo do atributo junto ao SICLI quando indicado campo para alimentação");
            }
        }

        // Validação do campo AtributoPartilha
        if (Objects.nonNull(atributoExtracao.getAtributoPartilha())) {
        	
        	// Localiza o atributo indicado para partilha perante o tipo de documento informado
            AtributoExtracao atributoPartilha = tipoDocumento.getAtributosExtracao().stream()
                                                             .filter(ae -> atributoExtracao.getAtributoPartilha().equals(ae))
                                                             .findFirst().orElse(null);
                                                            

            // Caso não seja identificado o atributo partilha baseado no identificador informado, inclui registro na lista de pendências
            if (Objects.isNull(atributoPartilha)) {
                List<String> pendencias = mapaPendencias.getOrDefault(ConstantesCadastroTipoDocumento.IDENTIFICADOR_ATRIBUTO_PARTILHA, new ArrayList<>());
                pendencias.add(MessageFormat.format("Atributo Extração indicado para partilha não vinculado ao tipo de documento. ID Atributo Partilha = {0}", atributoExtracao.getAtributoPartilha()
                                                                                                                                                                               .getId()));
                mapaPendencias.put(ConstantesCadastroTipoDocumento.IDENTIFICADOR_ATRIBUTO_PARTILHA, pendencias);
            } else {
            	
                // Verifica se algum outro atributo já faz utilização do atributo indicado para partilha e em caso positivo lança uma exceção
            	AtributoExtracao atributoPartilhaUtilizador = tipoDocumento.getAtributosExtracao().stream()
                                                                           .filter(ae -> Objects.nonNull(ae.getAtributoPartilha())
                                                                                         && atributoExtracao.getAtributoPartilha().equals(ae.getAtributoPartilha()))
                                                                           .findFirst().orElse(null);
            	
                if (Objects.nonNull(atributoPartilhaUtilizador) && !Objects.equals(atributoPartilhaUtilizador.getId(), atributoExtracao.getId())) {
                    String mensagem = "Atributo Extração indicado para partilha já esta sendo partilhado por outro atribbuto. Atributo Indicado = {0} - {1} | Atributo em utilização = {2} - {3}";
                    List<String> pendencias = mapaPendencias.getOrDefault(ConstantesCadastroTipoDocumento.IDENTIFICADOR_ATRIBUTO_PARTILHA, new ArrayList<>());
                    pendencias.add(MessageFormat.format(mensagem, atributoPartilha.getId(), atributoPartilha.getNomeNegocial(), atributoPartilhaUtilizador.getId(), atributoPartilhaUtilizador.getNomeNegocial()));
                    mapaPendencias.put(ConstantesCadastroTipoDocumento.IDENTIFICADOR_ATRIBUTO_PARTILHA, pendencias);
                }
            }

            // Valida se o modo de partilha aplicado foi encaminhado ou já possui registro prévio da informação.
            if (Objects.isNull(atributoExtracao.getModoPartilhaEnum())) {
                List<String> pendencias = mapaPendencias.getOrDefault(ConstantesCadastroTipoDocumento.INDICADOR_MODO_PARTILHA, new ArrayList<>());
                pendencias.add("Modo de partilha é obrigatorio para vinculação de um atributo a ser partilhado");
                mapaPendencias.put(ConstantesCadastroTipoDocumento.INDICADOR_MODO_PARTILHA, pendencias);
            }

            // Valida se o modo de partilha aplicado foi encaminhado ou já possui registro prévio da informação.
            if (Objects.isNull(atributoExtracao.getEstrategiaPartilhaEnum())) {
                List<String> pendencias = mapaPendencias.getOrDefault(ConstantesCadastroTipoDocumento.IDENTIFICADOR_ESTRATEGIA_PARTILHA, new ArrayList<>());
                pendencias.add("Estratégia de partilha é obrigatorio para vinculação de um atributo a ser partilhado");
                mapaPendencias.put(ConstantesCadastroTipoDocumento.IDENTIFICADOR_ESTRATEGIA_PARTILHA, pendencias);
            }
        }
        
        // valida se existe mais de um atributo extração marcado como true CalculoDataValidade.
        if(Objects.nonNull(atributoExtracao.getUtilizadoCalculoValidade()) && atributoExtracao.getUtilizadoCalculoValidade()) {
        	
        	AtributoExtracao atributoJaExisteComCalculoValidade = tipoDocumento.getAtributosExtracao().stream()
        			.filter(ae -> ae.getUtilizadoCalculoValidade())
        			.findFirst().orElse(null);
        	
        	if(Objects.nonNull(atributoJaExisteComCalculoValidade)) {
        		this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_CALCULO_VALIDADE, "Já existe Atributo Extração com Cálculo Data Validade");
        	}
        }
        
        // valida se existe mais de um atributo extração marcado como true IdentificadorPessoa.
        if(Objects.nonNull(atributoExtracao.getUtilizadoIdentificadorPessoa()) && atributoExtracao.getUtilizadoIdentificadorPessoa()) {
        	
        	AtributoExtracao atributoJaExisteIdentificadorPessoa = tipoDocumento.getAtributosExtracao().stream()
        			.filter(ae -> ae.getUtilizadoIdentificadorPessoa())
        			.findFirst().orElse(null);
        	
        	if(Objects.nonNull(atributoJaExisteIdentificadorPessoa)) {
        		this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_IDENTIFICADOR_PESSOA, "Já existe Atributo Extração com Identificador Pessoa");
        	}
        }

        // Varifica se o processo gerou pendencias para algum atributo e em caso positivo lança uma exceção indicando as pendencias mapeadas.
        List<PendenciasVO> listaPendencias = mapaPendencias.entrySet().stream().map(registro -> new PendenciasVO(registro.getKey(), registro.getValue()))
                                                           .collect(Collectors.toList());
        if (!listaPendencias.isEmpty()) {
            throw new SimtrCadastroException("TDS.vIAD.001 - Problemas identificados na execução da inclusão do atributo.", listaPendencias);
        }
    }

    /**
     * Realiza a validação relativa as regras que devem ser atendidas na atualiação de um tipo de documento
     *
     * @param tipoDocumento Objeto que representa o tipo de documento a ser modificado
     * @param tipoDocumentoManutencaoDTO Objeto que contem as informações relativa as modificações que devem ser aplicadas. Apenas os atributos não nulos
     *        sensibilizam a substituição da informação
     * @throws SimtrCadastroException Lançada caso exista alguma pendência impeditiva para alteração do tipo de documento indicando qual o problema relacionado
     *         identificado no momento da validação das informações
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    private void validaManutencaoTipoDocumento(TipoDocumento tipoDocumento, TipoDocumentoManutencaoDTO tipoDocumentoManutencaoDTO) {
        // Inicializa a lista para armazenar as pendencias identificadas durante a validação
        Map<String, List<String>> mapaPendencias = new HashMap<>();

        // Aprovado se as seguintes condições forem definidas:
        // 1 - Uso do tipo de documento no universo do dossiê digital ou processo administrativo
        // 2 - Objeto com modificações encaminhou nome da classe GED a ser utilizado; ou
        // 3 - Tipo de documento já possui informação prévia relativo a classe GED
        boolean avaliacaoDossie = Objects.nonNull(tipoDocumentoManutencaoDTO.getIndicadorUsoDossieDigital()) && tipoDocumentoManutencaoDTO.getIndicadorUsoDossieDigital();
        boolean avaliacaoPAE = Objects.nonNull(tipoDocumentoManutencaoDTO.getIndicadorUsoProcessoAdministrativo()) && tipoDocumentoManutencaoDTO.getIndicadorUsoProcessoAdministrativo();
        if ((avaliacaoDossie || avaliacaoPAE)
            && (Objects.isNull(tipoDocumentoManutencaoDTO.getNomeClasseSIECM()))
            && (Objects.isNull(tipoDocumento.getNomeClasseSIECM()))) {
            List<String> pendencias = mapaPendencias.getOrDefault(ConstantesCadastroTipoDocumento.CLASSE_SIECM, new ArrayList<>());
            pendencias.add("Necessário indicar uma classe SIECM para o tipo de documento habilitado para uso junto ao Dossiê Digital e/ou Processo Administrativo");
            mapaPendencias.put(ConstantesCadastroTipoDocumento.CLASSE_SIECM, pendencias);
        }

        // Aprovado se as seguintes condições forem definidas:
        // 1 - Atualização indica Encaminhamoento para extração externa
        // 2 - Objeto com modificações encaminhou indicador para extração em M+0; ou
        // 3 - Tipo de documento já possui informação prévia possibilidade de extração de dados em M+0
        if ((Objects.nonNull(tipoDocumentoManutencaoDTO.getIndicadorExtracaoExterna())) && (tipoDocumentoManutencaoDTO.getIndicadorExtracaoExterna())
            && (Objects.isNull(tipoDocumentoManutencaoDTO.getIndicadorExtracaoM0()))
            && (Objects.isNull(tipoDocumento.getPermiteExtracaoM0()))) {
            List<String> pendencias = mapaPendencias.getOrDefault(ConstantesCadastroTipoDocumento.INDICADOR_EXTRACAO_M0, new ArrayList<>());
            pendencias.add("Necessário informar se tipo de documento pode ser encaminhado para extração de dados na janela M0");
            mapaPendencias.put(ConstantesCadastroTipoDocumento.INDICADOR_EXTRACAO_M0, pendencias);
        }

        // Varifica se o processo gerou pendencias para algum atributo e em caso positivo lança uma exceção indicando as pendencias mapeadas.
        List<PendenciasVO> listaPendencias = mapaPendencias.entrySet().stream().map(registro -> new PendenciasVO(registro.getKey(), registro.getValue()))
                                                           .collect(Collectors.toList());
        if (!listaPendencias.isEmpty()) {
            throw new SimtrCadastroException("TDS.vMTD.001 - Problemas identificados na execução da alteração.", listaPendencias);
        }
    }

    /**
     * Realiza a validação relativa as regras que devem ser atendidas na atualização de um atributo extração
     *
     * @param tipoDocumento Objeto que representa o tipo de documento vinculado ao atributo em modificação
     * @param atributoExtracao Objeto que representa o atributo extração a ser modificado
     * @param atributoExtracaoManutencaoDTO Objeto que contem as informações relativa as modificações que devem ser aplicadas. Apenas os atributos não nulos
     *        sensibilizam a substituição da informação
     * @throws SimtrCadastroException Lançada caso exista alguma pendência impeditiva para alteração do tipo de documento indicando qual o problema relacionado
     *         identificado no momento da validação das informações
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    private void validaManutencaoAtributoExtracao(TipoDocumento tipoDocumento, AtributoExtracao atributoExtracao, AtributoExtracaoManutencaoDTO atributoExtracaoManutencaoDTO) {
        // Inicializa a lista para armazenar as pendencias identificadas durante a validação
        Map<String, List<String>> mapaPendencias = new HashMap<>();

        // Caso seja enviado o nome do atributo para o SIECM: 
        // Valida se o registro já possui definição previa do tipo de atributo no SIECM ou se o objeto de modificações encaminhou a informação
        if ((atributoExtracaoManutencaoDTO.getNomeAtributoSIECM() != null && !atributoExtracaoManutencaoDTO.getNomeAtributoSIECM().isEmpty()) && 
                (atributoExtracaoManutencaoDTO.getTipoAtributoSIECM() == null) && 
                (atributoExtracao.getTipoAtributoSiecmEnum() == null)) {
            List<String> pendencias = mapaPendencias.getOrDefault(ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SIECM, new ArrayList<>());
            pendencias.add("Tipo de atributo no SIECM é obrigatorio para indicação de envio ao GED");
            mapaPendencias.put(ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SIECM, pendencias);
        }

        // Valida se o registro já possui definição previa da indicação de obrigatoriedade da informação junto ao SIECM ou se o objeto de modificações encaminhou a informação
        if ((atributoExtracaoManutencaoDTO.getNomeAtributoSIECM() != null) && 
                (atributoExtracao.getObrigatorioSIECM() == null) && 
                (atributoExtracaoManutencaoDTO.getObrigatorioSIECM() == null)) {
            List<String> pendencias = mapaPendencias.getOrDefault(ConstantesCadastroTipoDocumento.INDICADOR_OBRIGATORIO_SIECM, new ArrayList<>());
            pendencias.add("A Indicação de obrigatoriedade no SIECM é necessaria para controle de envio ao SIECM");
            mapaPendencias.put(ConstantesCadastroTipoDocumento.INDICADOR_OBRIGATORIO_SIECM, pendencias);
        }

        // Verifica se o objeto de modificações indicou identificador para vinculação com o atributo de partilha e o mesmo não é negativo.
        // OBS: Valor negativo fará com que a vinculação seja anulada.
        if (Objects.nonNull(atributoExtracaoManutencaoDTO.getIdentificadorAtributoPartilha()) && 
                (atributoExtracaoManutencaoDTO.getIdentificadorAtributoPartilha() > 0)) {
            // Captura o identificador do atributo de partilha encaminhado
            Integer idAtributoPartilha = atributoExtracaoManutencaoDTO.getIdentificadorAtributoPartilha();

            // Valida a existência prévia do atributo no tipo de documento indicado
            AtributoExtracao atributoPartilha = tipoDocumento.getAtributosExtracao().stream()
                                                             .filter(ae -> atributoExtracaoManutencaoDTO.getIdentificadorAtributoPartilha().equals(ae.getId()))
                                                             .findFirst().orElse(null);
            
            if (Objects.isNull(atributoPartilha)) {
                List<String> pendencias = mapaPendencias.getOrDefault(ConstantesCadastroTipoDocumento.IDENTIFICADOR_ATRIBUTO_PARTILHA, new ArrayList<>());
                pendencias.add(MessageFormat.format("Atributo Extração indicado para partilha não vinculado ao tipo de documento. ID Atributo Partilha = {0}", atributoExtracaoManutencaoDTO.getIdentificadorAtributoPartilha()));
                mapaPendencias.put(ConstantesCadastroTipoDocumento.IDENTIFICADOR_ATRIBUTO_PARTILHA, pendencias);
            } else {
                
            	// Valida se o atributo indicado já não esta em utilização por outro atributo
                AtributoExtracao atributoPartilhaUtilizador = tipoDocumento.getAtributosExtracao().stream()
                                                                           .filter(ae -> Objects.nonNull(ae.getAtributoPartilha())
                                                                                         && idAtributoPartilha.equals(ae.getAtributoPartilha().getId()))
                                                                           .findFirst().orElse(null);
                                                                          
                if (Objects.nonNull(atributoPartilhaUtilizador) && !Objects.equals(atributoPartilhaUtilizador.getId(), atributoExtracao.getId())) {
                    String mensagem = "Atributo Extração indicado para partilha já esta sendo partilhado por outro atribbuto. Atributo Indicado = {0} - {1} | Atributo em utilização = {2} - {3}";
                    List<String> pendencias = mapaPendencias.getOrDefault(ConstantesCadastroTipoDocumento.IDENTIFICADOR_ATRIBUTO_PARTILHA, new ArrayList<>());
                    pendencias.add(MessageFormat.format(mensagem, atributoPartilha.getId(), atributoPartilha.getNomeNegocial(), atributoPartilhaUtilizador.getId(), atributoPartilhaUtilizador.getNomeNegocial()));
                    mapaPendencias.put(ConstantesCadastroTipoDocumento.IDENTIFICADOR_ATRIBUTO_PARTILHA, pendencias);
                }
            }

            // Valida se o modo de partilha aplicado foi encaminhado ou já possui registro prévio da informação.
            if (Objects.isNull(atributoExtracaoManutencaoDTO.getModoPartilhaEnum()) && Objects.isNull(atributoExtracao.getModoPartilhaEnum())) {
                List<String> pendencias = mapaPendencias.getOrDefault(ConstantesCadastroTipoDocumento.INDICADOR_MODO_PARTILHA, new ArrayList<>());
                pendencias.add("Modo de partilha é obrigatorio para vinculação de um atributo a ser partilhado");
                mapaPendencias.put(ConstantesCadastroTipoDocumento.INDICADOR_MODO_PARTILHA, pendencias);
            }

            // Valida se o modo de partilha aplicado foi encaminhado ou já possui registro prévio da informação.
            if (Objects.isNull(atributoExtracaoManutencaoDTO.getEstrategiaPartilhaEnum()) && Objects.isNull(atributoExtracao.getEstrategiaPartilhaEnum())) {
                List<String> pendencias = mapaPendencias.getOrDefault(ConstantesCadastroTipoDocumento.IDENTIFICADOR_ESTRATEGIA_PARTILHA, new ArrayList<>());
                pendencias.add("Estratégia de partilha é obrigatorio para vinculação de um atributo a ser partilhado");
                mapaPendencias.put(ConstantesCadastroTipoDocumento.IDENTIFICADOR_ESTRATEGIA_PARTILHA, pendencias);
            }
        }

        // Aprovado se as seguintes condições forem definidas:
        // 1 - Objeto com modificações a serem aplicadas defina o nome do atributo do SICLI de associação
        // 2 - Objeto com modificações encaminhou indicação do tipo de atributo do SICLI; ou
        // 3 - Registro já possui informação prévia relativo ao tipo de atributo do SICLI
        if (Objects.nonNull(atributoExtracaoManutencaoDTO.getNomeAtributoSICLI()) && !atributoExtracaoManutencaoDTO.getNomeAtributoSICLI().isEmpty()) {
            if (Objects.isNull(atributoExtracao.getNomeObjetoSICLI()) && Objects.isNull(atributoExtracaoManutencaoDTO.getNomeObjetoSICLI())) {
                List<String> pendencias = mapaPendencias.getOrDefault(ConstantesCadastroTipoDocumento.NOME_OBJETO_SICLI, new ArrayList<>());
                pendencias.add("Nome do objeto SICLI é obrigatorio para indicação de envio a atualização junto ao sistema");
                mapaPendencias.put(ConstantesCadastroTipoDocumento.NOME_OBJETO_SICLI, pendencias);
            }
            
            if (Objects.isNull(atributoExtracao.getTipoAtributoSicliEnum()) && Objects.isNull(atributoExtracaoManutencaoDTO.getTipoAtributoSICLI())) {
                List<String> pendencias = mapaPendencias.getOrDefault(ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SICLI, new ArrayList<>());
                pendencias.add("Tipo Atributo SICLI é obrigatorio para indicação de envio a atualização junto ao sistema");
                mapaPendencias.put(ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SICLI, pendencias);
            }
        }

        // Valida se o valor informado para o campo NomeNegocial é diferente de nulo e de vazio ("")
        if ((Objects.isNull(atributoExtracaoManutencaoDTO.getNomeAtributoNegocial()) || atributoExtracaoManutencaoDTO.getNomeAtributoNegocial().isEmpty()) 
        		&& (Objects.isNull(atributoExtracao.getNomeNegocial()))) { 
            List<String> pendencias = mapaPendencias.getOrDefault(ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_NEGOCIAL, new ArrayList<>());
            pendencias.add("Nome negocial é obrigatório.");
            mapaPendencias.put(ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_NEGOCIAL, pendencias);
        }

        // Aprovado se as seguintes condições forem definidas:
        // 1 - Objeto com modificações a serem aplicadas defina nome do campo no SICPF para comparação
        // 2 - Objeto com modificações encaminhou indicação do modo de comparação do campo; ou
        // 3 - Registro já possui informação prévia relativo forma de comparar os campos
        if ((Objects.nonNull(atributoExtracaoManutencaoDTO.getSicpfCampoEnum()))
            && (Objects.isNull(atributoExtracao.getSicpfModoEnum()))
            && (Objects.isNull(atributoExtracaoManutencaoDTO.getSicpfModoEnum()))) {
            List<String> pendencias = mapaPendencias.getOrDefault(ConstantesCadastroTipoDocumento.INDICADOR_MODO_COMPARACAO_RECEITA, new ArrayList<>());
            pendencias.add("Modo de comparação do atributo no SICPF é obrigatorio para permitir a verificação do dado junto ao sistema");
            mapaPendencias.put(ConstantesCadastroTipoDocumento.INDICADOR_MODO_COMPARACAO_RECEITA, pendencias);
        }
        
        // valida se existe mais de um atributo extração marcado como true CalculoDataValidade.
        if(Objects.nonNull(atributoExtracaoManutencaoDTO.getCalculoData()) &&  atributoExtracaoManutencaoDTO.getCalculoData()) {
        	
        	AtributoExtracao atributoJaExisteComCalculoValidade = tipoDocumento.getAtributosExtracao().stream()
        			.filter(ae -> ae.getUtilizadoCalculoValidade() &&  !ae.getId().equals(atributoExtracao.getId()))
        			.findFirst().orElse(null); 
        	
        	if(Objects.nonNull(atributoJaExisteComCalculoValidade)) {
        		this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_CALCULO_VALIDADE, "Já existe Atributo Extração com Cálculo Data Validadade");
        	}
        }
        
        // valida se existe mais de um atributo extração marcado como true IdentificadorPessoa.
        if(Objects.nonNull(atributoExtracaoManutencaoDTO.getIdentificadorPessoa()) &&  atributoExtracaoManutencaoDTO.getIdentificadorPessoa()) {
        	
        	AtributoExtracao atributoJaExisteIdentificadorPessoa = tipoDocumento.getAtributosExtracao().stream()
        			.filter(ae -> ae.getUtilizadoIdentificadorPessoa() &&  !ae.getId().equals(atributoExtracao.getId()))
        			.findFirst().orElse(null);
        	
        	if(Objects.nonNull(atributoJaExisteIdentificadorPessoa)) {
        		this.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.INDICADOR_IDENTIFICADOR_PESSOA, "Já existe Atributo Extração com Identificador Pessoa");
        	}
        }

        // Varifica se o processo gerou pendencias para algum atributo e em caso positivo lança uma exceção indicando as pendencias mapeadas.
        List<PendenciasVO> listaPendencias = mapaPendencias.entrySet().stream().map(registro -> new PendenciasVO(registro.getKey(), registro.getValue()))
                                                           .collect(Collectors.toList());
        if (!listaPendencias.isEmpty()) {
            throw new SimtrCadastroException("TDS.vMAD.001 - Problemas identificados na execução da alteração.", listaPendencias);
        }
    }

    /**
     * Captura as funções documentais vinculadas a lista de identificadores enviado e vincula o tipo de documento encaminhado
     *
     * @param tipoDocumento Tipo de documento a ser vinculado
     * @param identificadoresFuncaoDocumentalInclusaoVinculo Lista de identificadores das funções documentais a ser associadas
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    private void insereVinculosFuncoes(TipoDocumento tipoDocumento, List<Integer> identificadoresFuncaoDocumentalInclusaoVinculo) {
        // Percorre todos os elementos indicados na lista, captura a função no contexto transacional e inclui o vinculo com a função
        identificadoresFuncaoDocumentalInclusaoVinculo.forEach(idInclusao -> {
            FuncaoDocumental funcaoDocumental = this.funcaoDocumentalServico.findById(idInclusao);
            tipoDocumento.addFuncoesDocumentais(funcaoDocumental);

            // Caso o tipo de documento não esteja vinculado a função adiciona o vinculo a atualiza a data/hora de ultima atualização da função
            if (!funcaoDocumental.getTiposDocumento().contains(tipoDocumento)) {
                funcaoDocumental.addTiposDocumento(tipoDocumento);
                funcaoDocumental.setDataHoraUltimaAlteracao(Calendar.getInstance());
            }
        });
    }

    /**
     * Desvincula as funções documentais relacionadas com o tipo de documento encaminhado vinculadas aos identificadores informados
     *
     * @param tipoDocumento Tipo de documento a ter as funções documentais desvinculadas desvinculados
     * @param identificadoresFuncaoDocumentalExclusaoVinculo Lista de identificadores das funções documentais a ser desassociadas do tipo de documento
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    private void removeVinculosFuncoes(TipoDocumento tipoDocumento, List<Integer> identificadoresFuncaoDocumentalExclusaoVinculo) {
        // Percorre todos os elementos indicados na lista, captura a função documental no contexto transacional e remove o vinculo com a função
        identificadoresFuncaoDocumentalExclusaoVinculo.forEach(idInclusao -> {
            FuncaoDocumental funcaoDocumental = this.funcaoDocumentalServico.getById(idInclusao);
            tipoDocumento.removeFuncoesDocumentais(funcaoDocumental);

            if (funcaoDocumental.getTiposDocumento().contains(tipoDocumento)) {
                funcaoDocumental.removeTiposDocumento(tipoDocumento);
            }
            funcaoDocumental.setDataHoraUltimaAlteracao(Calendar.getInstance());
            this.funcaoDocumentalServico.update(funcaoDocumental);
        });
    }

    /**
     * Persiste os atributos extração encaminihados vinculando-os ao tipo de documento enviado
     *
     * @param tipoDocumento Tipo de documento a ser vinculado os atributos extração
     * @param atributosExtracao Objetos que representam os prototipos dos novos atributos de extração a serem persistidos vinculados ao tipo de documento
     *        encaminhado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    private void insereAtributosExtracao(TipoDocumento tipoDocumento, AtributoExtracao... atributosExtracao) {
        // Percorre a lista de atributos extração enviados, vincula o atributo ao tipo de documento e persiste o registro
        new ArrayList<>(Arrays.asList(atributosExtracao)).forEach(atributoNovo -> {
            atributoNovo.setTipoDocumento(tipoDocumento);
            if (!tipoDocumento.getAtributosExtracao().contains(atributoNovo)) {
                tipoDocumento.addAtributosExtracao(atributoNovo);
            }
            this.entityManager.persist(atributoNovo);
        });
        //this.entityManager.flush();
    }

    /**
     * Desvincula os atributos extração encaminihados do tipo de documento enviado e remove os mesmos da base de dados
     * @param tipoDocumento Tipo de documento a ter os atributos extração desvinculados
     * @param atributosExtracao Objetos que representam os atributos de extração a serem desvinculados do tipo de documento encaminhado e removidos da base dados
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    private void removeAtributosExtracao(TipoDocumento tipoDocumento, AtributoExtracao... atributosExtracao) {
        // Percorre a lista de atributos extração enviados, desvincula o atributo ao tipo de documento e remove o registro pelo entity manager
        new ArrayList<>(Arrays.asList(atributosExtracao)).stream().map((atributoExtracao) -> {
        	tipoDocumento.removeAtributosExtracao(atributoExtracao);
            return atributoExtracao;
        }).forEachOrdered((atributoExtracao) -> {
            this.entityManager.remove(atributoExtracao);
        });
    }
    
    /**
     * Desvincula opções de atributo da entidade AtributoExtracao e os remove da base de dados. 
     * @param atributoExtracao contém as opções de atributos a serem desvinculadas
     * @param opcoesAtributo lista a ser desvinculada da entidade AtributoExtracao e removidas da base de dados.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    private void removeOpcoesAtributoExtracao(AtributoExtracao atributoExtracao, OpcaoAtributo... opcoesAtributo) {
        new ArrayList<>(Arrays.asList(opcoesAtributo)).stream().map((opcaoAtributo) -> {
            atributoExtracao.removeOpcoesAtributo(opcaoAtributo);
            return opcaoAtributo;
        }).forEachOrdered((opcaoAtributo) -> {
            this.entityManager.remove(opcaoAtributo);
        });
    }

    /**
     * Realiza a inclusão no mapa de pendencias encaminhada, uma pendencia indicada para o campo chave informado
     *
     * @param mapaPendencias Mapa de pendencia a ter o registro associado, onde a chave é o nome do campo e o valor é uma lista de Strings que representam as
     *        pendências identificadas. Caso ainda não exista nenhuma pendencia relacionada com o campo indicado, uma nova lista é criada, incluida a pendência e
     *        associada ao campo informado
     * @param campo Campo indicado para associação da pendência
     * @param pendencia Texto que representa a pendencia localizada
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    private void incluiPendenciaMapa(Map<String, List<String>> mapaPendencias, String campo, String pendencia) {
        // Captura a lista de pendencias pré-existentes no mapa vinculada ao campo, mas caso não exista, cria uma nova
        List<String> pendencias = mapaPendencias.getOrDefault(campo, new ArrayList<>());

        // Adiciona a pendência na lista
        pendencias.add(pendencia);

        // Inclui ou sobrepôe a lista de pendencias associada ao campo
        mapaPendencias.put(campo, pendencias);
    }
    
    /**
     * Método utilizado para salvar um nova opção de atributo de extração.
     *
     * @param idTipoDocumento Identificador do tipo de documento a ser utilizado como referência na inclusão do atributo de extração.
     * @param idAtributo Identificador do atributo a ser utilizado como referência na partilha da informação.
     * @param opcaoAtributo Prototipo de opção de atributo que deve ser criado e associado ao extração atributo indicado.
     *
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void insereNovaOpcaoAtributo(Integer idTipoDocumento, Integer idAtributo, OpcaoAtributo opcaoAtributo) {
        TipoDocumento tipoDocumento = this.findById(idTipoDocumento);
        
        if(Objects.isNull(tipoDocumento)) {
            String mensagem = MessageFormat.format("TDS.iNOA.001 - Tipo de Documento não localizado sob o identificador informado. ID = {0}", idTipoDocumento);
            throw new SimtrRecursoDesconhecidoException(mensagem);
        }
        
        AtributoExtracao atributoExtracao = null;
        
        if(tipoDocumento.getAtributosExtracao() != null || !tipoDocumento.getAtributosExtracao().isEmpty()) {
            atributoExtracao = tipoDocumento.getAtributosExtracao().stream()
                                            .filter(elemento -> elemento.getId().equals(idAtributo))
                                            .findFirst().orElse(null);
        }
        
        if(Objects.isNull(atributoExtracao)) {
            String mensagem = MessageFormat.format("TDS.iNOA.002 - O atributo extração informado não foi localizado vinculado ao tipo de documento informado. ID = {0}", idAtributo);
            throw new SimtrRecursoDesconhecidoException(mensagem);
        }else {
            
            // Verifica se Opção já existente com o mesmo valor vinculada ao atributo extração
            if((atributoExtracao.getOpcoesAtributo() != null || !atributoExtracao.getOpcoesAtributo().isEmpty()) && !Strings.isNullOrEmpty(opcaoAtributo.getDescricaoOpcao())) {
                boolean opcaoAtributoJaExiste = atributoExtracao.getOpcoesAtributo().stream().anyMatch(elemento -> elemento.getValorOpcao().equalsIgnoreCase(opcaoAtributo.getValorOpcao()));
                
                if(opcaoAtributoJaExiste) {
                    throw new SimtrRequisicaoException ("TDS.iNOA.003 - Opção com o mesmo valor já vinculada ao atributo extração indicado");
                }
            }
        }
        
        this.validaInclusaoOpcaoAtributo(opcaoAtributo);
        
        // Atualiza a data e hora de ultima alteração do registro para viabilizar a sensibilização de nova carga dos mapas
        //tipoDocumento.setDataHoraUltimaAlteracao(Calendar.getInstance());

        opcaoAtributo.setAtributoExtracao(atributoExtracao);
        
        this.entityManager.persist(opcaoAtributo);
    }
    
    /**
     * Realiza a validação relativa as regras que devem ser atendidas na inclusão da opção atributo
     *
     * @param opcaoAtributo Objeto que representa a opção atributo a ser persistido
     * @throws SimtrCadastroException Lançada caso exista alguma pendência impeditiva para inclusão da opção atributo indicando qual o problema relacionado
     *         identificado no momento da validação das informações
     */
    public void validaInclusaoOpcaoAtributo(OpcaoAtributo opcaoAtributo) {
        Map<String, List<String>> mapaPendencias = new HashMap<>();
        
        if(Strings.isNullOrEmpty(opcaoAtributo.getDescricaoOpcao())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.DESCRICAO, "Descrição da opção é obrigatório.");
        }else {
            
            int tamanho = opcaoAtributo.getDescricaoOpcao().trim().length();
            
            if(tamanho > 255){
                this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.DESCRICAO, "Descrição da opção deve ser limitado a 255 caracteres.");
            }
        }
        
        if(Strings.isNullOrEmpty(opcaoAtributo.getValorOpcao())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.VALOR, "O valor da opção é obrigatório.");
        }else {
            
            int tamanho = opcaoAtributo.getValorOpcao().trim().length();
            
            if(tamanho > 50) {
                this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoDocumento.VALOR, "O valor da opção deve ser limitado a 50 caracteres.");
            }
        }
        
        List<PendenciasVO> listaPendencias = mapaPendencias.entrySet().stream().map(registro -> new PendenciasVO(registro.getKey(), registro.getValue()))
                .collect(Collectors.toList());
        
        if (!listaPendencias.isEmpty()) {
            throw new SimtrCadastroException("TDS.vIOA.001 - Problemas identificados na execução da inclusão de um Opção Atributo.", listaPendencias);
        }
    }
    
    /**
     * Método sincronizado pra evitar error de concorrência, por espera uma lista de atualização
     * 
     * @param idTipoDocumento
     */
    public synchronized void atualizarDataHoraUiltimaAlteracaoTipoDocumento(Integer idTipoDocumento) {
    	// Atualiza a data e hora de ultima alteração do registro para viabilizar a sensibilização de nova carga dos mapas
    	TipoDocumento entidade = this.findById(idTipoDocumento);
    	entidade.setDataHoraUltimaAlteracao(Calendar.getInstance());
    	
    	this.update(entidade);
    }

//    /**
//     * Realiza consulta do Tipo de Documento Dados Declarados
//     *
//     * @param tipoPessoaEnum Indica o tipo de pessoa definido para captura do documento se Física ou Jurídica
//     * @param dossieDigital Indica se o documento consultado esta sob o contexto do dossiê digital ou não
//     * @return Tipo de Documento Dados declarados ou nulo caso não seja encontrado
//     */
//    @RolesAllowed({
//        ConstantesUtil.PERFIL_MTRADM,
//        ConstantesUtil.PERFIL_MTRAUD,
//        ConstantesUtil.PERFIL_MTRTEC,
//        ConstantesUtil.PERFIL_MTRSDNINT,
//        ConstantesUtil.PERFIL_MTRSDNMTZ,
//        ConstantesUtil.PERFIL_MTRSDNOPE
//    })
//    public TipoDocumento consultarDadosDeclarados(TipoPessoaEnum tipoPessoaEnum, boolean dossieDigital) {
//        this.carregarMapas();
//        if (TipoPessoaEnum.F.equals(tipoPessoaEnum) && dossieDigital) {
//            return this.mapaById.values().stream()
//                    .filter(tipoDocumento -> ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_DOSSIE_DIGITAL.equals(tipoDocumento.getCodigoTipologia()))
//                    .findFirst().orElse(null);
//        } else if (TipoPessoaEnum.J.equals(tipoPessoaEnum) && dossieDigital) {
//            return null;
//        } else if (TipoPessoaEnum.F.equals(tipoPessoaEnum) && !dossieDigital) {
//            return this.mapaById.values().stream()
//                                .filter(tipoDocumento -> ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_PF.equals(tipoDocumento.getCodigoTipologia()))
//                                .findFirst().orElse(null);
//        } else {
//            return this.mapaById.values().stream()
//                                .filter(tipoDocumento -> ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_PJ.equals(tipoDocumento.getCodigoTipologia()))
//                                .findFirst().orElse(null);
//        }
//    }
    
//    /**
//     * Realiza consulta do Tipo de Documento Cartão de Assinatura
//     *
//     * @param tipoPessoaEnum Indica o tipo de pessoa definido para captura do documento se Física ou Jurídica
//     * @param dossieDigital Indica se o documento consultado esta sob o conytexto do dossiê digital ou não
//     * @return Tipo de Documento Cartão de Assinatura ou nulo caso não seja encontrado
//     */
//    @RolesAllowed({
//        ConstantesUtil.PERFIL_MTRADM,
//        ConstantesUtil.PERFIL_MTRAUD,
//        ConstantesUtil.PERFIL_MTRTEC,
//        ConstantesUtil.PERFIL_MTRSDNINT,
//        ConstantesUtil.PERFIL_MTRSDNMTZ,
//        ConstantesUtil.PERFIL_MTRSDNOPE
//    })
//    public TipoDocumento consultarCartaoAssinatura(TipoPessoaEnum tipoPessoaEnum) {
//        this.carregarMapas();
//        if (TipoPessoaEnum.F.equals(tipoPessoaEnum)) {
//            return this.mapaById.values().stream()
//                    .filter(tipoDocumento -> ConstantesUtil.TIPOLOGIA_CARTAO_ASSINATURA.equals(tipoDocumento.getCodigoTipologia()))
//                    .findFirst().orElse(null);
//        } else {
//            return null;
//        }
//    }
}
