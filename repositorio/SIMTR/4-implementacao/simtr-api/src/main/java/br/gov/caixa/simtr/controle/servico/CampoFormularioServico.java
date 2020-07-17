package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.controle.excecao.SimtrCadastroException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.servico.helper.CadastroHelper;
import br.gov.caixa.simtr.controle.servico.helper.CampoFormularioServicoHelper;
import br.gov.caixa.simtr.controle.vo.campoformulario.CamposEntradaValidadoVO;
import br.gov.caixa.simtr.controle.vo.excecao.PendenciasVO;
import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.RespostaDossie;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesAlteracaoCadastroDefinicaoCampoFormulario;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroDefinicaoCampoFormulario;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario.AlteracaoCadastroDefinicaoCampoFormularioDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario.CampoFormularioDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario.GarantiaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario.ProcessoFaseDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario.ProcessoOriginadorDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario.ProdutoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario.TipoRelacionamentoDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRTEC,
    ConstantesUtil.PERFIL_MTRAUD,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE,
    ConstantesUtil.PERFIL_MTRSDNTTO,
    ConstantesUtil.PERFIL_MTRSDNTTG
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class CampoFormularioServico extends AbstractService<CampoFormulario, Long> {

    @Inject
    private EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(CampoFormulario.class.getName());

    @EJB
    private CampoFormularioServicoHelper campoFormularioServicoHelper;

    @EJB
    private CadastroHelper cadastroHelper;

    @EJB
    private ProcessoServico processoServico;

    @EJB
    private TipoRelacionamentoServico tipoRelacionamentoServico;

    @EJB
    private ProdutoServico produtoServico;

    @EJB
    private GarantiaServico garantiaServico;

    @EJB
    private CampoEntradaServico campoEntradaServico;

    @EJB
    private OpcaoCampoServico opcaoCampoServico;

    @EJB
    private CampoApresentacaoServico campoApresentacaoServico;

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Realiza a captura de um Campo Formulario baseado no identificador.
     *
     * @param id Identificador do campo formulario desejado.
     * @param vinculacaoProcesso Indica se as informações do processo associado devem ser carregadas
     * @param vinculacaoCampoEntrada Indica se as informações do campo de entrada associado devem ser carregadas
     * @param vinculacaoCampoApresentacao Indica se as informações dos campos de apresentação (forma de exibição em tela) associados devem ser carregados
     * @return Campo Formulario localizado com as informaçõs parametrizadas carregadas ou null caso o campo formulario não seja localizado
     */
    public CampoFormulario getById(final Long id, final boolean vinculacaoProcesso, final boolean vinculacaoCampoEntrada, final boolean vinculacaoCampoApresentacao) {
        final StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT cf FROM CampoFormulario cf ");
        if (vinculacaoProcesso) {
            jpql.append(" LEFT JOIN FETCH cf.processo p ");
        }
        if (vinculacaoCampoEntrada) {
            jpql.append(" LEFT JOIN FETCH cf.campoEntrada ce ");
            jpql.append(" LEFT JOIN FETCH ce.opcoesCampo oc ");
        }
        if (vinculacaoCampoApresentacao) {
            jpql.append(" LEFT JOIN FETCH cf.camposApresentacao ca ");
        }
        jpql.append(" WHERE cf.id = :id ");
        TypedQuery<CampoFormulario> query = this.entityManager.createQuery(jpql.toString(), CampoFormulario.class);
        query.setParameter("id", id);

        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public List<CampoFormularioDTO> listaFormulario() {
        List<Processo> processos = this.processoServico.listaProcessos();

        List<CampoFormularioDTO> camposFormulario = new ArrayList<>();
        List<CampoFormularioDTO> camposFormularioFinal = new ArrayList<>();

        processos.stream().forEach(proc -> {
            proc.getCamposFormulario().stream().forEach(campo -> {
                camposFormulario.add(new CampoFormularioDTO(campo));
            });
        });

        // realizar group by entre processo orginador e processo fase
        Map<ProcessoOriginadorDTO, Map<ProcessoFaseDTO, List<CampoFormularioDTO>>> mapaProcessoFase = camposFormulario.stream()
                                                                                                                      .filter(campo -> campo.getProcessoFase() != null)
                                                                                                                      .collect(Collectors.groupingBy(CampoFormularioDTO::getProcessoOriginador, Collectors.groupingBy(CampoFormularioDTO::getProcessoFase)));

        // realizar group by entre processo orginador e tipo relacionadmento
        Map<ProcessoOriginadorDTO, Map<TipoRelacionamentoDTO, List<CampoFormularioDTO>>> mapaProcessoTipoRelacionamento = camposFormulario.stream()
                                                                                                                                          .filter(campo -> campo.getTipoRelacionamento() != null)
                                                                                                                                          .collect(Collectors.groupingBy(CampoFormularioDTO::getProcessoOriginador, Collectors.groupingBy(CampoFormularioDTO::getTipoRelacionamento)));

        // realizar group by entre processo orginador e produto
        Map<ProcessoOriginadorDTO, Map<ProdutoDTO, List<CampoFormularioDTO>>> mapaProcessoProduto = camposFormulario.stream()
                                                                                                                    .filter(campo -> campo.getProduto() != null)
                                                                                                                    .collect(Collectors.groupingBy(CampoFormularioDTO::getProcessoOriginador, Collectors.groupingBy(CampoFormularioDTO::getProduto)));

        // realizar group by entre processo orginador e garantia
        Map<ProcessoOriginadorDTO, Map<GarantiaDTO, List<CampoFormularioDTO>>> mapaProcessoGarantia = camposFormulario.stream()
                                                                                                                      .filter(campo -> campo.getGarantia() != null)
                                                                                                                      .collect(Collectors.groupingBy(CampoFormularioDTO::getProcessoOriginador, Collectors.groupingBy(CampoFormularioDTO::getGarantia)));

        // pecorrer o mapa pra preencher a quantidade por processo fase
        for (ProcessoOriginadorDTO processoOriginador : mapaProcessoFase.keySet()) {
            Map<ProcessoFaseDTO, List<CampoFormularioDTO>> mapaFase = mapaProcessoFase.get(processoOriginador);

            for (ProcessoFaseDTO procesoFase : mapaFase.keySet()) {
                List<CampoFormularioDTO> listCampos = mapaFase.get(procesoFase);
                int quantidade = listCampos != null ? listCampos.size() : 0;
                CampoFormularioDTO campoFormularioDTO = new CampoFormularioDTO();
                campoFormularioDTO.setProcessoOriginador(processoOriginador);
                campoFormularioDTO.setProcessoFase(procesoFase);
                campoFormularioDTO.setQuantidade(quantidade);

                camposFormularioFinal.add(campoFormularioDTO);
            }
        }

        // pecorrer o mapa pra preencher a quantidade por tipo relacionamento
        for (ProcessoOriginadorDTO processoOriginador : mapaProcessoTipoRelacionamento.keySet()) {
            Map<TipoRelacionamentoDTO, List<CampoFormularioDTO>> mapaTiposRelacionamentos = mapaProcessoTipoRelacionamento.get(processoOriginador);

            for (TipoRelacionamentoDTO tipoRelacionamento : mapaTiposRelacionamentos.keySet()) {
                List<CampoFormularioDTO> listCampos = mapaTiposRelacionamentos.get(tipoRelacionamento);
                int quantidade = listCampos != null ? listCampos.size() : 0;
                CampoFormularioDTO campoFormularioDTO = new CampoFormularioDTO();
                campoFormularioDTO.setProcessoOriginador(processoOriginador);
                campoFormularioDTO.setTipoRelacionamento(tipoRelacionamento);
                campoFormularioDTO.setQuantidade(quantidade);

                camposFormularioFinal.add(campoFormularioDTO);
            }
        }

        // pecorrer o mapa pra preencher a quantidade por produto
        for (ProcessoOriginadorDTO processoOriginador : mapaProcessoProduto.keySet()) {
            Map<ProdutoDTO, List<CampoFormularioDTO>> mapaProdutos = mapaProcessoProduto.get(processoOriginador);

            for (ProdutoDTO produto : mapaProdutos.keySet()) {
                List<CampoFormularioDTO> listCampos = mapaProdutos.get(produto);
                int quantidade = listCampos != null ? listCampos.size() : 0;
                CampoFormularioDTO campoFormularioDTO = new CampoFormularioDTO();
                campoFormularioDTO.setProcessoOriginador(processoOriginador);
                campoFormularioDTO.setProduto(produto);
                campoFormularioDTO.setQuantidade(quantidade);

                camposFormularioFinal.add(campoFormularioDTO);
            }
        }

        // pecorrer o mapa pra preencher a quantidade por garantia
        for (ProcessoOriginadorDTO processoOriginador : mapaProcessoGarantia.keySet()) {
            Map<GarantiaDTO, List<CampoFormularioDTO>> mapaGarantias = mapaProcessoGarantia.get(processoOriginador);

            for (GarantiaDTO garantia : mapaGarantias.keySet()) {
                List<CampoFormularioDTO> listCampos = mapaGarantias.get(garantia);
                int quantidade = listCampos != null ? listCampos.size() : 0;
                CampoFormularioDTO campoFormularioDTO = new CampoFormularioDTO();
                campoFormularioDTO.setProcessoOriginador(processoOriginador);
                campoFormularioDTO.setGarantia(garantia);
                campoFormularioDTO.setQuantidade(quantidade);

                camposFormularioFinal.add(campoFormularioDTO);
            }
        }

        return camposFormularioFinal;
    }

    /**
     * Consulta uma lista de campos formulário filtrando-se pelos parâmetros passados.
     * 
     * @param idProcessoOrigem
     * @param idProcessoFase
     * @param idTipoRelacionamento
     * @param idProduto
     * @param idGarantia
     * @return
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public List<CampoFormulario> consultaFormularioPorProcessoOrigem(Integer idProcessoOrigem, Integer idProcessoFase, Integer idTipoRelacionamento, Integer idProduto, Integer idGarantia) {
        this.campoFormularioServicoHelper.validaIntegridadeValoresEntrada(idProcessoFase, idTipoRelacionamento, idProduto, idGarantia);
        CamposEntradaValidadoVO camposEntradaValidadoVO = this.campoFormularioServicoHelper.validaExistenciaValoresEntradaBaseDados(idProcessoOrigem, idProcessoFase, idTipoRelacionamento, idProduto, idGarantia);
        return carregaFormularioBaseDados(camposEntradaValidadoVO);
    }

    /**
     * Cadastra um campo de formulário com as definições de fase, tipo relacionamento, produto ou garantia.
     * 
     * @param idProcessoOrigem
     * @param idProcessoFase
     * @param idTipoRelacionamento
     * @param idProduto
     * @param idGarantia
     * @return
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void cadastraFormularioPorProcessoOrigem(CampoFormulario campoFormulario, Integer idProcessoOrigem, Integer idProcessoFase, Integer idTipoRelacionamento, Integer idProduto, Integer idGarantia) {
        this.campoFormularioServicoHelper.validaIntegridadeValoresEntrada(idProcessoFase, idTipoRelacionamento, idProduto, idGarantia);
        CamposEntradaValidadoVO camposEntradaValidadoVO = this.campoFormularioServicoHelper.validaExistenciaValoresEntradaBaseDados(idProcessoOrigem, idProcessoFase, idTipoRelacionamento, idProduto, idGarantia);
        this.populaVinculacoesCampoFormulario(campoFormulario, camposEntradaValidadoVO);

        this.validaInclusaoDefinicaoCampoFormulario(campoFormulario);
        if(Objects.nonNull(camposEntradaValidadoVO.getProcessoFase())){
            List<CampoFormulario> listaDefinicaoCampoFormularioBaseDados = carregaFormularioBaseDados(camposEntradaValidadoVO);
    
            List<CampoFormulario> camposFormularioOrdemApresentacaoSuperior = listaDefinicaoCampoFormularioBaseDados.stream()
                                                                                                                    .filter(campoFormularioBase -> campoFormularioBase.getOrdemApresentacao() >= campoFormulario.getOrdemApresentacao())
                                                                                                                    .sorted(Comparator.comparing(CampoFormulario::getOrdemApresentacao).reversed())
                                                                                                                    .collect(Collectors.toList());
    
            camposFormularioOrdemApresentacaoSuperior.forEach(campoFormularioOrdemApresentacao -> {
                campoFormularioOrdemApresentacao.setOrdemApresentacao(campoFormularioOrdemApresentacao.getOrdemApresentacao() + 1);
                this.update(campoFormularioOrdemApresentacao);
            });
        }
        this.campoEntradaServico.save(campoFormulario.getCampoEntrada());
        this.opcaoCampoServico.cadastraOpcoesCampo(campoFormulario.getCampoEntrada());
        this.save(campoFormulario);
        this.campoApresentacaoServico.cadastraCamposApresentacao(campoFormulario);
        campoFormulario.getProcesso().setDataHoraUltimaAlteracao(Calendar.getInstance());
        this.getEntityManager().merge(campoFormulario.getProcesso());
    }

    /**
     * Valida inclusão de um novo campo de formulário.
     * 
     * @param campoFormulario
     */
    private void validaInclusaoDefinicaoCampoFormulario(CampoFormulario campoFormulario) {
        Map<String, List<String>> mapaPendencias = new HashMap<>();

        if (Objects.isNull(campoFormulario.getOrdemApresentacao())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.ORDEM, "A informação da ordem de apresentação do campo de formulário é obrigatória.");
        } else if (campoFormulario.getOrdemApresentacao() <= 0) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.ORDEM, "O número de ordem do campo deve ser um valor inteiro positivo.");
        }

        if (Objects.isNull(campoFormulario.getObrigatorio())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.OBRIGATORIO, "Indicador de obrigatoriedade deve ser informado.");
        }

        if (Objects.nonNull(campoFormulario.getNomeCampo()) && campoFormulario.getNomeCampo().trim().length() > 100) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.NOME, "O nome do campo não deve ser superior a 100 caracteres.");
        }

        if (Objects.nonNull(campoFormulario.getIdentificadorBPM()) && this.vericaExistenciaIdentificadorBPM(campoFormulario.getIdentificadorBPM())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.IDENTIFICADOR_BPM, "Identificador do campo junto ao BPM já definido para outro campo de formulário.");
        }

        this.campoEntradaServico.validaInclusaoCampoEntrada(campoFormulario.getCampoEntrada());

        List<PendenciasVO> listaPendencias = mapaPendencias.entrySet().stream().map(registro -> new PendenciasVO(registro.getKey(), registro.getValue()))
                                                           .collect(Collectors.toList());

        if (!listaPendencias.isEmpty()) {
            throw new SimtrCadastroException("CFS.vIDCF.001 - Problemas identificados na execução da inclusão de um campo de formulário.", listaPendencias);
        }
    }

    /**
     * Altera um campo de formulário já existente.
     * 
     * @param idCampoFormulario
     * @param alteracaoCadastroCampoFormularioDTO
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void alteraCampoFormulario(Long idCampoFormulario, AlteracaoCadastroDefinicaoCampoFormularioDTO alteracaoCadastroCampoFormularioDTO) {
        CampoFormulario campoFormulario = this.consultaCampoFormularioRespostasAssociadas(idCampoFormulario);
        if (Objects.isNull(campoFormulario)) {
            throw new SimtrRecursoDesconhecidoException("Campo de formulário não localizado sob o identificador informado.");
        }

        this.campoFormularioServicoHelper.verificaExisteRespostasDossieCampoFormularioAtual(campoFormulario, alteracaoCadastroCampoFormularioDTO);

        this.campoFormularioServicoHelper.validaIntegridadeValoresEntrada(alteracaoCadastroCampoFormularioDTO.getIdentificadorProcessoFase(), alteracaoCadastroCampoFormularioDTO.getIdentificadorTipoRelacionamento(), alteracaoCadastroCampoFormularioDTO.getIdentificadorProduto(), alteracaoCadastroCampoFormularioDTO.getIdentificadorGarantia());

        CamposEntradaValidadoVO camposEntradaValidadoVO = this.campoFormularioServicoHelper.validaExistenciaValoresEntradaBaseDados(alteracaoCadastroCampoFormularioDTO.getIdentificadorProcessoDossie(), alteracaoCadastroCampoFormularioDTO.getIdentificadorProcessoFase(), alteracaoCadastroCampoFormularioDTO.getIdentificadorTipoRelacionamento(), alteracaoCadastroCampoFormularioDTO.getIdentificadorProduto(), alteracaoCadastroCampoFormularioDTO.getIdentificadorGarantia());

        this.validaAlteracaoDefinicaoCampoFormulario(campoFormulario, alteracaoCadastroCampoFormularioDTO);
        this.populaVinculacoesCampoFormulario(campoFormulario, camposEntradaValidadoVO);
        
        this.campoEntradaServico.update(campoFormulario.getCampoEntrada());
        this.update(campoFormulario);
        this.campoApresentacaoServico.atualizaCamposApresentacao(campoFormulario, alteracaoCadastroCampoFormularioDTO);
        campoFormulario.getProcesso().setDataHoraUltimaAlteracao(Calendar.getInstance());
        this.getEntityManager().merge(campoFormulario.getProcesso());
    }

    /**
     * Valida alteração de um novo campo de formulário.
     * 
     * @param mapaPendencias
     * @param alteracaoCadasdroCampoFormularioDTO
     */
    private void validaAlteracaoDefinicaoCampoFormulario(CampoFormulario campoFormulario, AlteracaoCadastroDefinicaoCampoFormularioDTO alteracaoCadasdroCampoFormularioDTO) {
        Map<String, List<String>> mapaPendencias = new HashMap<>();

        if (Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getOrdem()) && alteracaoCadasdroCampoFormularioDTO.getOrdem() <= 0) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.ORDEM, "O número de ordem do campo deve ser um valor inteiro positivo.");
        } else if (Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getOrdem()) && alteracaoCadasdroCampoFormularioDTO.getOrdem() > 0) {
            campoFormulario.setOrdemApresentacao(alteracaoCadasdroCampoFormularioDTO.getOrdem());
        }

        if (Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getObrigatorio())) {
            campoFormulario.setObrigatorio(alteracaoCadasdroCampoFormularioDTO.getObrigatorio());
        }

        if (Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getExpressaoInterface()) && !alteracaoCadasdroCampoFormularioDTO.getExpressaoInterface().isEmpty()) {
            campoFormulario.setExpressaoInterface(alteracaoCadasdroCampoFormularioDTO.getExpressaoInterface());
        } else if (Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getExpressaoInterface()) && alteracaoCadasdroCampoFormularioDTO.getExpressaoInterface().isEmpty()) {
            campoFormulario.setExpressaoInterface(null);
        }

        if (Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getAtivo())) {
            campoFormulario.setAtivo(alteracaoCadasdroCampoFormularioDTO.getAtivo());
        }

        if (Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getNome()) && alteracaoCadasdroCampoFormularioDTO.getNome().trim().length() > 100) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.NOME, "O nome do campo não deve ser superior a 100 caracteres.");
        } else if (Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getNome()) && alteracaoCadasdroCampoFormularioDTO.getNome().isEmpty()) {
            campoFormulario.setNomeCampo(null);
        } else if (Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getNome()) && !alteracaoCadasdroCampoFormularioDTO.getNome().isEmpty()) {
            campoFormulario.setNomeCampo(alteracaoCadasdroCampoFormularioDTO.getNome());
        }
        
        if(Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getNomeAtributoSICLI())){
            campoFormulario.setNomeAtributoSICLI(alteracaoCadasdroCampoFormularioDTO.getNomeAtributoSICLI());
        }
        
        if(Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getNomeObjetoSICLI())){
            campoFormulario.setNomeObjetoSICLI(alteracaoCadasdroCampoFormularioDTO.getNomeObjetoSICLI());
        }
        
        if(Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getTipoAtributoSicliEnum())){
            campoFormulario.setTipoAtributoSicliEnum(alteracaoCadasdroCampoFormularioDTO.getTipoAtributoSicliEnum());
        }

        if (Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getIdentificadorBpm()) && this.vericaExistenciaIdentificadorBPM(alteracaoCadasdroCampoFormularioDTO.getIdentificadorBpm())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.IDENTIFICADOR_BPM, "Identificador do campo junto ao BPM já definido para outro campo de formulário.");
        } else if (Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getIdentificadorBpm()) && !this.vericaExistenciaIdentificadorBPM(alteracaoCadasdroCampoFormularioDTO.getIdentificadorBpm())) {
            campoFormulario.setIdentificadorBPM(alteracaoCadasdroCampoFormularioDTO.getIdentificadorBpm());
        }

        this.campoEntradaServico.validaAlteracaoCampoEntrada(campoFormulario.getCampoEntrada(), alteracaoCadasdroCampoFormularioDTO);

        this.validaIntegridadeAlteracaoVinculacoes(mapaPendencias, alteracaoCadasdroCampoFormularioDTO.getIdentificadorProcessoDossie(), alteracaoCadasdroCampoFormularioDTO.getIdentificadorProcessoFase(), alteracaoCadasdroCampoFormularioDTO.getIdentificadorTipoRelacionamento(), alteracaoCadasdroCampoFormularioDTO.getIdentificadorProduto(), alteracaoCadasdroCampoFormularioDTO.getIdentificadorGarantia());

        List<PendenciasVO> listaPendencias = mapaPendencias.entrySet().stream().map(registro -> new PendenciasVO(registro.getKey(), registro.getValue()))
                                                           .collect(Collectors.toList());

        if (!listaPendencias.isEmpty()) {
            throw new SimtrCadastroException("CFS.vADCF.001 - Problemas identificados na execução da alteração de um campo de formulário.", listaPendencias);
        }
    }

    /**
     * Verifica se os parâmetros de vinculações de processo dossiê juntamente com o processo fase, tipo de relacionamento, produto ou garantia estão de acordo para dar prosseguimento a alteração do
     * campo de formulário.
     * 
     * @param idProcessoDossie
     * @param idProcessoFase
     * @param idTipoRelacionamento
     * @param idProduto
     * @param idGarantia
     */
    public void validaIntegridadeAlteracaoVinculacoes(Map<String, List<String>> mapaPendencias, Integer idProcessoDossie, Integer idProcessoFase, Integer idTipoRelacionamento, Integer idProduto, Integer idGarantia) {
        List<Integer> parametros = new ArrayList<>();
        parametros.add(idProcessoFase);
        parametros.add(idTipoRelacionamento);
        parametros.add(idProduto);
        parametros.add(idGarantia);

        Integer quantidadesParamentrosRecebidos = 0;
        for (Integer parametro : parametros) {
            if (Objects.nonNull(parametro)) {
                quantidadesParamentrosRecebidos++;
            }
        }

        if (Objects.nonNull(idProcessoDossie) && (quantidadesParamentrosRecebidos > 1 || quantidadesParamentrosRecebidos.equals(0))) {
            String mensagem = MessageFormat.format("CFS.vIAV.001 - Deverá ser informado o processo originador somado a apenas mais um identificador entre o Processo Fase, Tipo de Relacionamento, Produto ou Garantia {0} | {1} | {2} | {3} | {4}", idProcessoDossie, idProcessoFase, idTipoRelacionamento, idProduto, idGarantia);
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesAlteracaoCadastroDefinicaoCampoFormulario.IDENTIFICADOR_PROCESSO_DOSSIE, mensagem);
        } else if (Objects.isNull(idProcessoDossie) && quantidadesParamentrosRecebidos > 0) {
            String mensagem = MessageFormat.format("CFS.vIAV.002 - Deverá ser informado o processo originador somado a apenas mais um identificador entre o Processo Fase, Tipo de Relacionamento, Produto ou Garantia {0} | {1} | {2} | {3} | {4}", idProcessoDossie, idProcessoFase, idTipoRelacionamento, idProduto, idGarantia);
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesAlteracaoCadastroDefinicaoCampoFormulario.IDENTIFICADOR_PROCESSO_DOSSIE, mensagem);
        }
    }

    /**
     * Atualiza as vinculações do campo de formulário atual
     * 
     * @param campoFormulario
     * @param camposEntradaValidadoVO
     */
    private void populaVinculacoesCampoFormulario(CampoFormulario campoFormulario, CamposEntradaValidadoVO camposEntradaValidadoVO) {
        campoFormulario.setProcesso(camposEntradaValidadoVO.getProcessoOrigem());
        campoFormulario.setProcessoFase(camposEntradaValidadoVO.getProcessoFase());
        campoFormulario.setTipoRelacionamento(camposEntradaValidadoVO.getTipoRelacionamento());
        campoFormulario.setProduto(camposEntradaValidadoVO.getProduto());
        campoFormulario.setGarantia(camposEntradaValidadoVO.getGarantia());
    }

    /**
     * Deleta um campo de formulário
     * 
     * @param idCampoFormulario
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void excluiCampoFormulario(Long idCampoFormulario) {
        CampoFormulario campoFormulario = this.consultaCampoFormularioRespostasAssociadas(idCampoFormulario);
        if (Objects.isNull(campoFormulario)) {
            throw new SimtrRecursoDesconhecidoException("Campo de formulário não localizado sob o identificador informado.");
        }

        if (this.campoFormularioServicoHelper.existeRespostasDossieCampoFormularioAtual(campoFormulario)) {
            campoFormulario.setAtivo(false);
            this.update(campoFormulario);
        } else {
            this.campoApresentacaoServico.deletaCamposApresentacao(campoFormulario.getCamposApresentacao());
            this.delete(campoFormulario);
            this.opcaoCampoServico.deletaOpcoesCampo(campoFormulario.getCampoEntrada().getOpcoesCampo());
            this.campoEntradaServico.delete(campoFormulario.getCampoEntrada());
        }

        campoFormulario.getProcesso().setDataHoraUltimaAlteracao(Calendar.getInstance());
        this.getEntityManager().merge(campoFormulario.getProcesso());
    }

    /**
     * 
     * @param identificadorBPM
     * @return
     */
    private Boolean vericaExistenciaIdentificadorBPM(Integer identificadorBPM) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT COUNT(cf) FROM CampoFormulario cf WHERE cf.identificadorBPM = :identBPM ");

        TypedQuery<Long> query = this.entityManager.createQuery(jpql.toString(), Long.class);
        query.setParameter("identBPM", identificadorBPM);

        return query.getSingleResult().intValue() > 0;
    }

    /**
     * Consulta um campo de formulário com suas possíveis respostas associadas.
     * 
     * @param id
     * @return
     */
    public CampoFormulario consultaCampoFormularioRespostasAssociadas(Long id) {
        final StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT cf FROM CampoFormulario cf ");
        jpql.append(" LEFT JOIN FETCH cf.processo p ");
        jpql.append(" LEFT JOIN FETCH cf.campoEntrada ce ");
        jpql.append(" LEFT JOIN FETCH ce.opcoesCampo oc ");
        jpql.append(" LEFT JOIN FETCH oc.respostasDossie rdoc ");
        jpql.append(" LEFT JOIN FETCH cf.camposApresentacao ca ");
        jpql.append(" LEFT JOIN FETCH cf.respostasDossie rdcf ");
        jpql.append(" WHERE cf.id = :id ");

        TypedQuery<CampoFormulario> query = this.entityManager.createQuery(jpql.toString(), CampoFormulario.class);
        query.setParameter("id", id);

        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }

    /**
     * Retorna uma lista de CampoFormulario da base de dados filtrados pelos parâmetros de entrada.
     * 
     * @param camposEntradaValidadoVO
     * @return
     */
    private List<CampoFormulario> carregaFormularioBaseDados(CamposEntradaValidadoVO camposEntradaValidadoVO) {
        TypedQuery<CampoFormulario> query = null;
        final StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT cf FROM CampoFormulario cf ");
        jpql.append(" LEFT JOIN FETCH cf.campoEntrada ce ");
        jpql.append(" LEFT JOIN FETCH ce.opcoesCampo oc ");
        jpql.append(" LEFT JOIN FETCH cf.camposApresentacao ca ");
        jpql.append(" WHERE cf.processo.id = :id ");

        if (Objects.nonNull(camposEntradaValidadoVO.getProcessoFase())) {
            jpql.append(" AND cf.processoFase.id = :idProcessoFase ");
            query = this.entityManager.createQuery(jpql.toString(), CampoFormulario.class);
            query.setParameter("idProcessoFase", camposEntradaValidadoVO.getProcessoFase().getId());
        }
        if (Objects.nonNull(camposEntradaValidadoVO.getTipoRelacionamento())) {
            jpql.append(" AND cf.tipoRelacionamento.idTipoRelacionamento = :idTipoRelacionamento ");
            query = this.entityManager.createQuery(jpql.toString(), CampoFormulario.class);
            query.setParameter("idTipoRelacionamento", camposEntradaValidadoVO.getTipoRelacionamento().getId());
        }
        if (Objects.nonNull(camposEntradaValidadoVO.getProduto())) {
            jpql.append(" AND cf.produto.id = :idProduto ");
            query = this.entityManager.createQuery(jpql.toString(), CampoFormulario.class);
            query.setParameter("idProduto", camposEntradaValidadoVO.getProduto().getId());
        }
        if (Objects.nonNull(camposEntradaValidadoVO.getGarantia())) {
            jpql.append(" AND cf.garantia.id = :idGarantia ");
            query = this.entityManager.createQuery(jpql.toString(), CampoFormulario.class);
            query.setParameter("idGarantia", camposEntradaValidadoVO.getGarantia().getId());
        }
        query.setParameter("id", camposEntradaValidadoVO.getProcessoOrigem().getId());
        try {
            return query.getResultList();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }
    
    public RespostaDossie retornaRespostaPorProtocoloSiric(String protocolo){
        final StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT rd FROM RespostaDossie rd ");
        jpql.append(" LEFT JOIN FETCH rd.campoFormulario cf ");
        jpql.append(" LEFT JOIN FETCH rd.dossieProduto dp ");
        jpql.append(" WHERE rd.respostaAberta = :protocolo ");
        
        TypedQuery<RespostaDossie> query = this.entityManager.createQuery(jpql.toString(), RespostaDossie.class);
        query.setParameter("protocolo", protocolo);
        query.setMaxResults(1);
        
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }
    
    public CampoFormulario retornaCampoFormularioPorProcessoGeradorEProcessoFaseEIdentificadorBPM(Integer idProcessoGerador, Integer idProcessoFase, Integer nuIdentificadorBpmSiric){
        final StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT cf FROM CampoFormulario cf ");
        jpql.append(" LEFT JOIN FETCH cf.processo p ");
        jpql.append(" LEFT JOIN FETCH cf.processoFase pf ");
        jpql.append(" WHERE p.id = :idProcessoGerador AND pf.id = :idProcessoFase AND cf.identificadorBPM = :identificadorBPM ");
        
        TypedQuery<CampoFormulario> query = this.entityManager.createQuery(jpql.toString(), CampoFormulario.class);
        query.setParameter("identificadorBPM", nuIdentificadorBpmSiric);
        query.setParameter("idProcessoGerador", idProcessoGerador);
        query.setParameter("idProcessoFase", idProcessoFase);
        
        query.setMaxResults(1);
        
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }
  
    public void inseriRespostaDossie(RespostaDossie entidade) {
        this.getEntityManager().persist(entidade);
    }
}
