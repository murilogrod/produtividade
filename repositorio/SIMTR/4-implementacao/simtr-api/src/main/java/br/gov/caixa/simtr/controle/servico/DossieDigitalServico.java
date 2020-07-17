/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.caixa.simtr.controle.servico;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.siiso.dto.RetornoPessoasFisicasDTO;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrPreCondicaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.helper.NivelDocumentalComposicaoHelper;
import br.gov.caixa.simtr.controle.vo.DocumentoVO;
import br.gov.caixa.simtr.controle.vo.outsourcing.SubmissaoDocumentoVO;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.Autorizacao;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.ComposicaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.ControleDocumento;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePF;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePJ;
import br.gov.caixa.simtr.modelo.entidade.DossieClienteProduto;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.OpcaoSelecionada;
import br.gov.caixa.simtr.modelo.entidade.RegraDocumental;
import br.gov.caixa.simtr.modelo.entidade.RespostaDossie;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.JanelaTemporalExtracaoEnum;
import br.gov.caixa.simtr.modelo.enumerator.ModoPartilhaEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.AtributoDocumentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.DossieClienteDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.DossieClientePFDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.DossieClientePJDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.PendenciaCadastroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.SolicitacaoExtracaoDadosDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v2.dossiedigital.autorizacao.DocumentoConclusaoDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

/**
 * Classe criada para realizar os ajustes na chamada do dosiê digital.
 *
 * @author c090347
 */
@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRAUD,
    ConstantesUtil.PERFIL_MTRTEC,
    ConstantesUtil.PERFIL_MTRDOSINT,
    ConstantesUtil.PERFIL_MTRDOSMTZ,
    ConstantesUtil.PERFIL_MTRDOSOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class DossieDigitalServico {

    @EJB
    private AutorizacaoServico autorizacaoServico;
    
    @EJB
    private AvaliacaoFraudeServico avaliacaoFraudeServico;
    
    @EJB
    private AvaliacaoExtracaoServico avaliacaoExtracaoServico;

    @EJB
    private CadastroCaixaServico cadastroCaixaServico;

    @EJB
    private CadastroReceitaServico cadastroReceitaServico;

    @EJB
    private CanalServico canalServico;

    @EJB
    private ComposicaoDocumentalServico composicaoDocumentalServico;

    @EJB
    private DocumentoServico documentoServico;

    @EJB
    private DossieDigitalServico _self;

    @EJB
    private DossieClienteServico dossieClienteServico;
    
    @EJB
    private DossieProdutoServico dossieProdutoServico;

    @EJB
    private NivelDocumentalServico nivelDocumentalServico;

    @EJB
    private NivelDocumentalComposicaoHelper nivelDocumentalComposicaoHelper;

    @EJB
    private RelatorioServico relatorioServico;

    @EJB
    private SiecmServico siecmServico;

    @EJB
    private TipoDocumentoServico tipoDocumentoServico;

    @Inject
    private EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(DossieDigitalServico.class.getName());

    private static final String MENSAGEM_CANAL_NAO_LOCALIZADO = "Canal de comunicação não localizado para client ID vinculado ao token de autenticação.";
    private static final String MENSAGEM_CANAL_NAO_AUTORIZADO = "Canal de comunicação não autorizado para consumo dos serviços do dossiê digital.";
    
    private static final String CARTAO_ASSINATURA_CPF = "cpf";
    private static final String CARTAO_ASSINATURA_NOME = "nome";

    public DossieClienteDTO getByCpfCnpj(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum) {
        DossieClienteDTO dossieClienteDTO = this.getByIdOrCpfCnpj(null, cpfCnpj, tipoPessoaEnum);
        if(TipoPessoaEnum.F.equals(tipoPessoaEnum)){
            RetornoPessoasFisicasDTO sicpfResponseDTO = this.cadastroReceitaServico.consultaCadastroPF(cpfCnpj);
            String situacaoReceita = sicpfResponseDTO.getCodigoSituacaoCpf().concat(":").concat(sicpfResponseDTO.getSituacaoCpf());
            ((DossieClientePFDTO) dossieClienteDTO).setSituacaoReceita(situacaoReceita);
        }
        return dossieClienteDTO;
    }

    public DossieClienteDTO getById(Long id) {
        return this.getByIdOrCpfCnpj(id, null, null);
    }
    
    public Documento getDocumentoById(Long idDossieCliente, Long idDocumento) {
        // Captura o canal de comunicação para utilizar na vinculação do documento
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "DDS.eDDDD.001 - ".concat(MENSAGEM_CANAL_NAO_LOCALIZADO));
        this.canalServico.validaOutorgaCanal(canal, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, "DDS.eDDDD.003 - ".concat(MENSAGEM_CANAL_NAO_AUTORIZADO));

        DossieCliente dossieCliente = this.dossieClienteServico.getById(idDossieCliente, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (dossieCliente == null) {
            throw new SimtrRecursoDesconhecidoException("DDS.eDDDD.002 - Dossiê de cliente não localizado sob identificador informado para vinculação com o documento");
        }        
        
        Documento documento = this.documentoServico.getById(idDocumento, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
        
        boolean vinculacaoValida = documento.getDossiesCliente().stream().anyMatch(dc -> dc.getId().equals(idDossieCliente));
        if(!vinculacaoValida) {
            throw new SimtrRequisicaoException("DDS.gDBI.003 - O documento solicitado não possui vinculo com o dossiê de cliente indicado");
        }
        
        return documento;
    }
    
    public DocumentoVO extraiDadosDocumentoDossieDigitalExterno (Long idDossieCliente, SolicitacaoExtracaoDadosDTO solicitacaoExtracaoDadosDTO , FormatoConteudoEnum formatoConteudoEnum, String processo) {
        // Captura o canal de comunicação para utilizar na vinculação do documento
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "DDS.eDDDD.001 - ".concat(MENSAGEM_CANAL_NAO_LOCALIZADO));
        this.canalServico.validaOutorgaCanal(canal, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, "DDS.eDDDD.003 - ".concat(MENSAGEM_CANAL_NAO_AUTORIZADO));

        // Captura o dossiê de cliente com base no seu identificador para realizar a associação do documento
        DossieCliente dossieCliente = this.dossieClienteServico.getById(idDossieCliente, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (dossieCliente == null) {
            throw new SimtrRecursoDesconhecidoException("DDS.eDDDD.002 - Dossiê de cliente não localizado sob identificador informado para vinculação com o documento");
        }

        // Valida o conteudo encaminhado do documento para extração
        this.validaBinario(solicitacaoExtracaoDadosDTO.getBinario());

        // Captura do tipo de documento
        final TipoDocumento tipoDocumento = this.tipoDocumentoServico.getByTipologia(solicitacaoExtracaoDadosDTO.getTipoDocumento(), Boolean.TRUE);
               
        //Captura o binario encaminhado na solicitação
        String binario = solicitacaoExtracaoDadosDTO.getBinario();
        
        // Cria registro documento no SIMTR
        Documento documento = this.documentoServico.prototype(canal, Boolean.FALSE, tipoDocumento, TemporalidadeDocumentoEnum.TEMPORARIO_OCR, OrigemDocumentoEnum.S, formatoConteudoEnum, new HashMap<>(), binario);
        
        // Verifica se o os documentos estão sendo armazenados localmente ou no SIECM
        boolean armazenaLocal = Boolean.getBoolean("simtr_imagens_local");
        if (!armazenaLocal) {
            // Efetua o armazenamento do documento perante o GED
            String codigoGED = this.siecmServico.armazenaDocumentoPessoalSIECM(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), documento, TemporalidadeDocumentoEnum.TEMPORARIO_OCR, solicitacaoExtracaoDadosDTO.getBinario(), ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);

            // Atribui o codigo GED no documento do SIMTR
            documento.setCodigoGED(codigoGED);
        }
        
        //Caso o tipologia definida indique envio para o serviço de extração externa, realiza o envio para a atividade de outsourcing documental
        if (tipoDocumento.getEnviaExtracaoExterna()) {
            // Cria registro documento no SIMTR
            ControleDocumento controleDocumento = new ControleDocumento();
            controleDocumento.setIndicativoExtracao(Boolean.TRUE);
            controleDocumento.setDataHoraEnvio(Calendar.getInstance());
            controleDocumento.setIndicativoAvaliacaoAutenticidade(tipoDocumento.getEnviaAvaliacaoDocumental());

            SubmissaoDocumentoVO submissaoDocumentoVO = new SubmissaoDocumentoVO(tipoDocumento.getCodigoTipologia(), processo, Boolean.TRUE, JanelaTemporalExtracaoEnum.M30, tipoDocumento.getEnviaAvaliacaoDocumental(), binario, controleDocumento);
            String codigoControle = this.avaliacaoExtracaoServico.submeteDocumentoOutsourcing(submissaoDocumentoVO);
            controleDocumento.setCodigoFornecedor(codigoControle);
        
            // Vincula o documento do SIMTR
            controleDocumento.setDocumento(documento);
            documento.addControlesDocumento(controleDocumento);
        }

        String link = this.siecmServico.obtemLinkAcessoDocumentoSIECM(documento.getCodigoGED(), ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);

        _self.armazenaDocumentoCliente(dossieCliente.getId(), documento);

        DocumentoVO documentoVO = new DocumentoVO();
        documentoVO.setDocumentoSIMTR(documento);
        documentoVO.setLinkGED(link);
        
        return documentoVO;
    }
    
    public DocumentoVO extraiDadosDocumentoDossieDigital(Long idDossieCliente, FormatoConteudoEnum formatoConteudoEnum, String tipologiaDocumento, String binario) {
        DossieCliente dossieCliente = this.dossieClienteServico.getById(idDossieCliente, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (dossieCliente == null) {
            throw new SimtrRecursoDesconhecidoException("DDS.eDDDD.001 - Dossiê de cliente não localizado sob identificador informado para vinculação com o documento");
        }

        // Valida o conteudo encaminhado do documento para extração
        this.validaBinario(binario);

        // Captura o canal de comunicação para utilizar na vinculação do documento
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "DDS.eDDDD.002 - ".concat(MENSAGEM_CANAL_NAO_LOCALIZADO));
        this.canalServico.validaOutorgaCanal(canal, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, "DDS.eDDDD.003 - ".concat(MENSAGEM_CANAL_NAO_AUTORIZADO));

        // Captura do tipo de documento
        final TipoDocumento tipoDocumento = this.tipoDocumentoServico.getByNome(tipologiaDocumento, Boolean.TRUE);

        // Realiza extração de dados junto ao SIECM para cada imagem recebida do documento
        ControleDocumento controleDocumento = new ControleDocumento();
        controleDocumento.setIndicativoExtracao(Boolean.TRUE);
        controleDocumento.setDataHoraEnvio(Calendar.getInstance());

        Map<String, String> dadosExtraidos;
	try {
            dadosExtraidos = this.siecmServico.executaExtracaoDadosDocumento(tipoDocumento, formatoConteudoEnum, binario);
	    controleDocumento.setValorRetornoExtracao(UtilJson.converterParaJson(dadosExtraidos));
	} catch (Exception ex) {
	    throw new SimtrRequisicaoException("DDS.eDDDD.004 - ".concat(ConstantesUtil.MSG_FALHA_ATUALIZACAO_ATRIBUTOS_DOCUMENTO), ex);
	}
        controleDocumento.setDataHoraRetornoExtracao(Calendar.getInstance());

        // Cria registro documento no SIMTR
        Documento documento = this.documentoServico.prototype(canal, Boolean.FALSE, tipoDocumento, TemporalidadeDocumentoEnum.TEMPORARIO_OCR, OrigemDocumentoEnum.S, formatoConteudoEnum, dadosExtraidos, binario);
        // Vincula o documento do SIMTR
        controleDocumento.setDocumento(documento);
        documento.addControlesDocumento(controleDocumento);

        // Verifica se o os documentos estão sendo armazenados localmente ou no SIECM
        boolean armazenaLocal = Boolean.getBoolean("simtr_imagens_local");
        String link = null;
        if (!armazenaLocal) {
            // Efetua o armazenamento do documento perante o GED
            String codigoGED = this.siecmServico.armazenaDocumentoPessoalSIECM(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), documento, TemporalidadeDocumentoEnum.TEMPORARIO_OCR, binario, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);

            // Atribui o codigo GED no documento do SIMTR
            documento.setCodigoGED(codigoGED);
            
            link = this.siecmServico.obtemLinkAcessoDocumentoSIECM(documento.getCodigoGED(), ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
        }

        _self.armazenaDocumentoCliente(dossieCliente.getId(), documento);

        DocumentoVO documentoVO = new DocumentoVO();
        documentoVO.setDocumentoSIMTR(documento);
        documentoVO.setLinkGED(link);
        
        return documentoVO;
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public byte[] geraCartaoAssinatura(Long id) {

        // Captura o dossi~e de cliente e valida se o recurso foi localizado
        DossieCliente dossieCliente = this.dossieClienteServico.getById(id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (dossieCliente == null) {
            throw new SimtrRecursoDesconhecidoException("DDS.gCA.001 - Dossiê de Cliente não localizado sob identificador informado.");
        }

        // Valida se o tipo de pessoa é fisica
        if (!TipoPessoaEnum.F.equals(dossieCliente.getTipoPessoa())) {
            throw new SimtrRequisicaoException("DDS.gCA.002 - Cartões de Assinatura no modelo dossiê digital só está disponível para pessoas físicas.");
        }

        // Captura o canal de comunicação para utilizar na vinculação do documento
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "DDS.gCA.003 - ".concat(MENSAGEM_CANAL_NAO_LOCALIZADO));
        this.canalServico.validaOutorgaCanal(canal, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, "DDS.gCA.004 - ".concat(MENSAGEM_CANAL_NAO_AUTORIZADO));

        // Localiza a tipologia documental no cadastro do SIMTR
        final TipoDocumento tipoDocumentoCartaoAssinatura = this.tipoDocumentoServico.getByTipologia(ConstantesUtil.TIPOLOGIA_CARTAO_ASSINATURA);
        if (tipoDocumentoCartaoAssinatura == null) {
            throw new SimtrConfiguracaoException("DDS.gCA.004 - Tipo de documento cartão assinatura não encontrado no sistema");
        }
        
        String cpfFormatado = StringUtils.leftPad(String.valueOf(dossieCliente.getCpfCnpj()), ConstantesUtil.TAMANHO_CPF, '0');
        try {
            RetornoPessoasFisicasDTO retornoPessoasFisicasDTO = this.cadastroReceitaServico.consultaCadastroPF(dossieCliente.getCpfCnpj());
            Map<String, String> dados = new HashMap<>();
            dados.put("NOME", retornoPessoasFisicasDTO.getNomeContribuinte());
            dados.put("CPF", cpfFormatado);
            String json = UtilJson.converterParaJson(dados);

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("LOGO_CAIXA", ConstantesUtil.RELATORIO_CAMINHO_IMAGENS.concat("caixa.png"));

            // Retorna o relatório.
            return this.relatorioServico.gerarRelatorioPDFJsonDataSource("dossiedigital/".concat(tipoDocumentoCartaoAssinatura.getNomeArquivoMinuta()), json, parametros);

        } catch (Exception e) {
            throw new SimtrConfiguracaoException(MessageFormat.format("Falha ao gerar cartão de assinaturas. CPF = {0}", cpfFormatado), e);
        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public void atualizaCartaoAssinatura(Long idDossieCliente, FormatoConteudoEnum formatoConteudoEnum, String binario) {
        // Captura o canal de comunicação para utilizar na vinculação do documento
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "DDS.aCA.001 - ".concat(MENSAGEM_CANAL_NAO_LOCALIZADO));
        this.canalServico.validaOutorgaCanal(canal, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, "DDS.aCA.002 - ".concat(MENSAGEM_CANAL_NAO_AUTORIZADO));

        // Realiza captura do dossiê do cliente com base no CPF/CNPJ informado.
        DossieCliente dossieCliente = this.dossieClienteServico.getById(idDossieCliente, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (dossieCliente == null) {
            throw new SimtrRequisicaoException("DDS.aCA.003 - Dossiê de cliente não localizado sob identificador informado");
        }

        if (!TipoPessoaEnum.F.equals(dossieCliente.getTipoPessoa())) {
            throw new SimtrRequisicaoException("DDS.aCA.004 - Cartões de Assinatura no modelo dossiê digital só está disponível para pessoas físicas.");
        }

        // Localiza a tipologia documental no cadastro do SIMTR
        final TipoDocumento tipoDocumentoCartaoAssinatura = this.tipoDocumentoServico.getByTipologia(ConstantesUtil.TIPOLOGIA_CARTAO_ASSINATURA);
        
        // Cria o documento de cartão de assinaturas
        Documento cartaoAssinatura;
        if (tipoDocumentoCartaoAssinatura == null) {
            throw new SimtrConfiguracaoException("DDS.aCADD.004 - Tipo de documento cartão assinatura não encontrado no sistema");
        } else {

            // Formata o CPF com 11 digitos para armazenar no atributo do documento
            String cpf = StringUtils.leftPad(String.valueOf(dossieCliente.getCpfCnpj()), ConstantesUtil.TAMANHO_CPF, '0');
            
            //Cria os atributos que representam o nome do cliente a ser enviado para o GED
            AtributoDocumento atributoDocumentoCPF = new AtributoDocumento();
            atributoDocumentoCPF.setAcertoManual(Boolean.FALSE);
            atributoDocumentoCPF.setConteudo(cpf);
            atributoDocumentoCPF.setDescricao(CARTAO_ASSINATURA_CPF);
            atributoDocumentoCPF.setIndiceAssertividade(BigDecimal.valueOf(100.00));
            
            AtributoDocumento atributoDocumento = new AtributoDocumento();
            atributoDocumento.setAcertoManual(Boolean.FALSE);
            atributoDocumento.setConteudo(dossieCliente.getNome());
            atributoDocumento.setDescricao(CARTAO_ASSINATURA_NOME);
            atributoDocumento.setIndiceAssertividade(BigDecimal.valueOf(100.00));
            
            List<AtributoDocumento> atributos = Arrays.asList(atributoDocumento);
            
            cartaoAssinatura = this.documentoServico.prototype(canal, Boolean.TRUE, tipoDocumentoCartaoAssinatura, TemporalidadeDocumentoEnum.VALIDO, OrigemDocumentoEnum.O, formatoConteudoEnum, atributos, binario);
        }
        
        // Envia o documento do cartão assinaturas para armazenamento junto ao SIECM
        String identificadorSiecm = this.siecmServico.armazenaDocumentoPessoalSIECM(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), cartaoAssinatura, TemporalidadeDocumentoEnum.VALIDO, binario, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);

        // Cria Documento a ser submetido ao SIECM
        final Calendar calendar = Calendar.getInstance();

        // Localiza o documento atual de dados declarados mais recente vinculado ao cliente pelo
        Documento cartaoAssinaturaAtual = this.documentoServico.localizaDocumentoClienteMaisRecenteByIdDossie(idDossieCliente, tipoDocumentoCartaoAssinatura, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);

        // Invalida o documento atual de cartão assinatura no SIMTR e no GED
        if (cartaoAssinaturaAtual != null) {
            cartaoAssinaturaAtual.setDataHoraValidade(calendar);
            cartaoAssinaturaAtual.setSituacaoTemporalidade(TemporalidadeDocumentoEnum.VENCIDO);
            this.documentoServico.update(cartaoAssinaturaAtual);

            this.siecmServico.alteraSituacaoTemporalidadeDocumentoSIECM(cartaoAssinaturaAtual.getCodigoGED(), TemporalidadeDocumentoEnum.VENCIDO, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
        }

        // Atribui o codigo GED no documento do SIMTR
        cartaoAssinatura.setCodigoGED(identificadorSiecm);

        // Atribui datas de captura e validade
        cartaoAssinatura.setDataHoraCaptura(calendar);
        this.documentoServico.defineDataValidade(cartaoAssinatura);

        // Salva o registro do documento no SIMTR
        cartaoAssinatura.addDossiesCliente(dossieCliente);
        this.documentoServico.save(cartaoAssinatura);

        // Cria o vinculo entre o novo cartão assinatura e o dossiê do cliente
        dossieCliente.addDocumentos(cartaoAssinatura);
        this.dossieClienteServico.update(dossieCliente);
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public void executaAtualizacaoDocumentoDossieDigital(Long idDossieCliente, Long identificadorDocumento, Map<String, String> mapaDadosAtualizacao) {

        // Realiza captura do dossiê do cliente com base no CPF/CNPJ informado.
        DossieCliente dossieCliente = this.dossieClienteServico.getById(idDossieCliente, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (dossieCliente == null) {
            throw new SimtrRequisicaoException("DDS.eADDD.001 - Dossiê de cliente não localizado sob identificador informado");
        }

        // Captura o canal de comunicação para utilizar na vinculação do documento
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "DDS.eADDD.002 - ".concat(MENSAGEM_CANAL_NAO_LOCALIZADO));
        this.canalServico.validaOutorgaCanal(canal, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, "DDS.eADDD.003 - ".concat(MENSAGEM_CANAL_NAO_AUTORIZADO));

        // Executa as chamadas de atualização de dados e validação de documento via EJB injetado para garantir a execução de um commit após a atualização dos dados
        // Nesse cenário, caso ocorra uma exceção na validação os dados jé encontram-se atualizados
        _self.atualizaDadosDocumentoDossieDigital(dossieCliente, identificadorDocumento, mapaDadosAtualizacao);
        _self.validaDocumentoDossieDigital(dossieCliente, identificadorDocumento);

    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public void atualizaDadosDocumentoDossieDigital(DossieCliente dossieCliente, Long identificadorDocumento, Map<String, String> mapaDadosAtualizacao) {
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "MDDS.aDDDD.001 - Canal de comunicação não localizado para o Cliente ID Autenticado.");
        this.canalServico.validaOutorgaCanal(canal, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, "DDS.aDDDD.002 - ".concat(MENSAGEM_CANAL_NAO_AUTORIZADO));

        // Realiza captura do documento com base no identificador informado.
        Documento documento = this.documentoServico.getById(identificadorDocumento, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
        if (documento == null) {
            String mensagem = MessageFormat.format("DDS.aDDDD.002 - Documento não localizado sob identificador informado. ID = {0}", identificadorDocumento);
            throw new SimtrRequisicaoException(mensagem);
        }

        // Verifica se o documento possui relação com o CPF/CNPJ informado.
        boolean clienteLocalizado = documento.getDossiesCliente().stream().anyMatch(dc -> dossieCliente.getId().equals(dc.getId()));

        if (!clienteLocalizado) {
            throw new SimtrRequisicaoException("DDS.aDDDD.003 - Documento solicitado não possui vinculo com o dossiê de cliente informado.");
        }

        // Transforma todos os atributos definido no tipo de documento em um Map para facilitar a busca
        final Map<String, AtributoExtracao> mapaAtributosTipoDocumento = new HashMap<>();
        documento.getTipoDocumento().getAtributosExtracao().forEach(atributoExtracao -> {
            if (atributoExtracao.getNomeAtributoDocumento() != null) {
                mapaAtributosTipoDocumento.put(atributoExtracao.getNomeAtributoDocumento(), atributoExtracao);
            }
        });

        // Transforma todos os atributos existentes do documento em um Map para facilitar a busca
        final Map<String, AtributoDocumento> mapaAtributosDocumento = new HashMap<>();
        documento.getAtributosDocumento().forEach(atributoDocumento -> mapaAtributosDocumento.put(atributoDocumento.getDescricao(), atributoDocumento));

        // Alimenta a coleção de campos do documento a para atualização das informações no SIMTR e GED
        //final List<CampoDTO> camposAtualizacaoECM = new ArrayList<>();
        mapaDadosAtualizacao.entrySet().forEach(dadoAtualizacao -> {
            // Identifica o atributo definido no tipo do documento a ser atualizado
            AtributoExtracao atributoExtracao = mapaAtributosTipoDocumento.get(dadoAtualizacao.getKey());
            if (atributoExtracao == null) {
                String mensagem = MessageFormat.format("DDS.aDDDD.004 - Atributo informado não identificado para o tipo de documento. Chave = {0}", dadoAtualizacao.getKey());
                throw new SimtrRequisicaoException(mensagem);
            }
            
            // Atualiza informação junto ao atributo do documento do SIMTR
            AtributoDocumento atributoDocumento = mapaAtributosDocumento.get(atributoExtracao.getNomeAtributoDocumento());
            if (atributoDocumento != null) {
                atributoDocumento.setConteudo(dadoAtualizacao.getValue());
                atributoDocumento.setAcertoManual(Boolean.TRUE);
            } else {
                AtributoDocumento atributoNovo = new AtributoDocumento();
                atributoNovo.setAcertoManual(Boolean.TRUE);
                atributoNovo.setConteudo(dadoAtualizacao.getValue());
                atributoNovo.setDescricao(atributoExtracao.getNomeAtributoDocumento());
                atributoNovo.setDocumento(documento);
                documento.addAtributosDocumento(atributoNovo);
            }

            // Verifica se o atributo realiza a partilha da informação com outro atributo
            if(atributoExtracao.getAtributoPartilha() != null) {
                // Caso o atributo deva ser partilhado, mas não defina a estrategia/forma de execução levanta-se uma exceção indicando a falha de configuração do registro
                if (atributoExtracao.getEstrategiaPartilhaEnum() == null || atributoExtracao.getModoPartilhaEnum() == null) {
                    String mensagem = MessageFormat.format("DDS.aDDDD.005 - Não foi definido forma de partilha de informação para o atributo. Chave = {0}", dadoAtualizacao.getKey());
                    throw new SimtrConfiguracaoException(mensagem);
                }
                
                String valorPartilhado = null;
                // Verifica se o atributo esta definido para comparar com a informação de nome da mãe retornado do SICPF
                switch (atributoExtracao.getEstrategiaPartilhaEnum()) {
                    case RECEITA_MAE:
                        // Captura a informação do SICPF para utilizar o nome da mãe enviado como base
                        try {
                            RetornoPessoasFisicasDTO retornoPessoasFisicasDTO = this.cadastroReceitaServico.consultaCadastroPF(dossieCliente.getCpfCnpj());
                            if (ModoPartilhaEnum.S.equals(atributoExtracao.getModoPartilhaEnum())) {
                                valorPartilhado = dadoAtualizacao.getValue().replace(retornoPessoasFisicasDTO.getNomeMae(), "").trim();
                                if (dadoAtualizacao.getValue().toUpperCase().contains(retornoPessoasFisicasDTO.getNomeMae().toUpperCase())) {
                                    dadoAtualizacao.setValue(retornoPessoasFisicasDTO.getNomeMae().trim());
                                }
                            } else if (ModoPartilhaEnum.V.equals(atributoExtracao.getModoPartilhaEnum())) {
                                valorPartilhado = retornoPessoasFisicasDTO.getNomeMae().trim();
                                dadoAtualizacao.setValue(dadoAtualizacao.getValue().replace(retornoPessoasFisicasDTO.getNomeMae(), "").trim());
                            }
                        } catch (Exception e) {
                            throw new SimtrConfiguracaoException(e.getLocalizedMessage(), e);
                        }
                        break;
                    default:
                        break;
                }

                // Atualiza informação do atributo partilhado junto ao atributo do documento do SIMTR
                if (valorPartilhado != null) {
                    AtributoExtracao atributoExtracaoPartilha = atributoExtracao.getAtributoPartilha();
                    AtributoDocumento atributoPartilhado = mapaAtributosDocumento.get(atributoExtracaoPartilha.getNomeAtributoDocumento());
                    if (atributoPartilhado != null) {
                        atributoPartilhado.setConteudo(valorPartilhado);
                        atributoPartilhado.setAcertoManual(Boolean.FALSE);
                    } else {
                        AtributoDocumento atributoNovo = new AtributoDocumento();
                        atributoNovo.setAcertoManual(Boolean.FALSE);
                        atributoNovo.setConteudo(valorPartilhado);
                        atributoNovo.setDescricao(atributoExtracaoPartilha.getNomeAtributoDocumento());
                        atributoNovo.setDocumento(documento);
                        documento.addAtributosDocumento(atributoNovo);
                    }
                }
            }
        });

        //Define a data de validade do documento para fins de novos negocios
        this.documentoServico.defineDataValidade(documento);
        
        // Valida os atributos definidos para o documento
        this.documentoServico.validaAtributosDocumento(documento);
        
        // Realiza validação do documento com o SICPF, caso seja um documento vinculado a PF
        if (TipoPessoaEnum.F.equals(dossieCliente.getTipoPessoa()) && documento.getTipoDocumento().getEnviaAvaliacaoCadastral()) {
            this.cadastroReceitaServico.validaDocumentoSICPF(dossieCliente.getCpfCnpj(), documento);
        }

        // Verifica se o documento esta apto para novos negocios conforme regra documental.
        if ((documento.getDataHoraValidade() != null) && (documento.getDataHoraValidade().before(Calendar.getInstance()))) {
            String codigoTipologia = documento.getTipoDocumento().getCodigoTipologia();
            String nomeTipoDocumento = documento.getTipoDocumento().getCodigoTipologia();
            String mensagem = MessageFormat.format("Documento {0} do tipo {1} - {2} não autorizado pelo prazo apto de utilização para novos negócios.", String.valueOf(documento.getId()), codigoTipologia, nomeTipoDocumento);
            throw new SimtrEstadoImpeditivoException(mensagem);
        }

        // Realiza a chamada de alteração do atributo e do status de temporalidade no SIECM
        this.siecmServico.atualizaAtributosDocumento(documento);
        this.siecmServico.alteraSituacaoTemporalidadeDocumentoSIECM(documento.getCodigoGED(), TemporalidadeDocumentoEnum.VALIDO, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);

        // Define a situação de temporalidade no SIMTR
        documento.setSituacaoTemporalidade(TemporalidadeDocumentoEnum.VALIDO);
        
        // Atualiza os atributos ajustados do documento no SIMTR
        this.documentoServico.update(documento);
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
    })
    public void validaDocumentoDossieDigital(DossieCliente dossieCliente, Long identificadorDocumento) {
        // Realiza captura do documento com base no identificador informado.
        Documento documento = this.documentoServico.getById(identificadorDocumento, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
        if (documento == null) {
            String mensagem = ConstantesUtil.MSG_FALHA_ID_NAO_LOCALIZADO_SIECM + identificadorDocumento;
            throw new SimtrRequisicaoException(mensagem);
        }

        // Realiza chamada ao serviço de avaliação de autenticidade documental para capturar retorno da avaliação solicitada.
        boolean documentoAutorizadoAvaliacaoCadastral = Boolean.TRUE;
        boolean documentoAutorizadoAvaliacaoDocumental = Boolean.TRUE;

        documentoAutorizadoAvaliacaoDocumental = this.capturarAvaliacaoAutenticidade(dossieCliente, documento, documentoAutorizadoAvaliacaoDocumental);
        documentoAutorizadoAvaliacaoCadastral = this.avaliarDadosCadastraisSIFRC(dossieCliente, documento, documentoAutorizadoAvaliacaoCadastral);

        if ((documentoAutorizadoAvaliacaoCadastral) && (documentoAutorizadoAvaliacaoDocumental)) {

            // Define a temporalidade do documento como temporario pós resultado do antifraude e atualiza o documento junto AO SIECM para modificar agora a temporalidade
            this.siecmServico.alteraSituacaoTemporalidadeDocumentoSIECM(documento.getCodigoGED(), TemporalidadeDocumentoEnum.TEMPORARIO_ANTIFRAUDE, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);

            // Define a temporalidade do documento como temporario pós resultado do antifraude e atualiza o documento junto AO SIMTR para modificar agora a temporalidade
            documento.setSituacaoTemporalidade(TemporalidadeDocumentoEnum.TEMPORARIO_ANTIFRAUDE);
        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void armazenaDocumentoCliente(Long id, Documento documento) {

        // Realiza captura do dossiê do cliente com base no identificador informado.
        DossieCliente dossieCliente = this.dossieClienteServico.getById(id, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
        if (dossieCliente == null) {
            throw new SimtrRecursoDesconhecidoException("DDS.aDC.001 - Dossiê de cliente não localizado sob identificador informado para vinculação com o documento");
        }

        // Invalida os documentos anteriores de mesma tipologia para novos negócios, caso a tipologia não permita manutenção de multiplos documentos.
        this.documentoServico.invalidarDocumentosAtivosCliente(id, null, null, documento.getTipoDocumento(), Boolean.TRUE);

        // Cria o vinculo entre o documento e o dossiê do cliente
        dossieCliente.addDocumentos(documento);
        documento.addDossiesCliente(dossieCliente);

        // Salva o registro do documento no SIMTR
        this.documentoServico.save(documento);
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public List<AtributoDocumentoDTO> consultaDadosDeclaradosDossieDigital(Long id) {
        // Captura o canal de comunicação para utilizar na vinculação do documento
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "DDS.cDDDD.001 - ".concat(MENSAGEM_CANAL_NAO_LOCALIZADO));
        this.canalServico.validaOutorgaCanal(canal, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, "DDS.cDDDD.002 - ".concat(MENSAGEM_CANAL_NAO_AUTORIZADO));

        // Realiza captura do dossiê do cliente com base no CPF/CNPJ informado.
        DossieCliente dossieCliente = this.dossieClienteServico.getById(id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (dossieCliente == null) {
            String mensagem = MessageFormat.format("DDS.cDDDD.003 - Dossiê do cliente não localizado para o identificador informado. ID = {0}", id);
            throw new SimtrRequisicaoException(mensagem);
        }

        // Localiza a tipologia documental no cadastro do SIMTR
        final TipoDocumento tipoDocumentoDadosDeclarados = this.tipoDocumentoServico.getByTipologia(ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_DD);
        if (tipoDocumentoDadosDeclarados == null) {
            throw new SimtrConfiguracaoException("DDS.cDDDD.004 - Tipo de documento de Dados Declarados não encontrado no sistema");
        }

        // Localiza o documento atual de dados declarados mais recente vinculado ao cliente pelo
        Documento doc = this.documentoServico.localizaDocumentoClienteMaisRecenteByIdDossie(id, tipoDocumentoDadosDeclarados, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
        List<AtributoDocumentoDTO> atributos = new ArrayList<>();
        if (doc != null) {
            for (AtributoDocumento atr : doc.getAtributosDocumento()) {
                AtributoDocumentoDTO atributo = new AtributoDocumentoDTO(atr);
                atributos.add(atributo);
            }
        }
        return atributos;
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public Documento atualizaDadosDeclaradosDossieDigital(Long id, List<AtributoDocumentoDTO> listaAtributosDeclarados) {
        // Captura o canal de comunicação para utilizar na vinculação do documento
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "DDS.aDDDD.001 - ".concat(MENSAGEM_CANAL_NAO_LOCALIZADO));
        this.canalServico.validaOutorgaCanal(canal, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, "DDS.aDDDD.002 - ".concat(MENSAGEM_CANAL_NAO_AUTORIZADO));

        // Realiza captura do dossiê do cliente com base no CPF/CNPJ informado.
        DossieCliente dossieCliente = this.dossieClienteServico.getById(id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (dossieCliente == null) {
            throw new SimtrRequisicaoException("DDS.eADDD.003 - Dossiê de cliente não localizado sob identificador informado");
        }

        // Localiza a tipologia documental no cadastro do SIMTR
        final TipoDocumento tipoDocumentoDadosDeclarados = this.tipoDocumentoServico.getByTipologia(ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_DD);
        if (tipoDocumentoDadosDeclarados == null) {
            throw new SimtrConfiguracaoException("DDS.aDDDD.004 - Tipo de documento  de Dados Declarados não encontrado no sistema");
        }

        // Localiza o documento atual de dados declarados mais recente vinculado ao cliente pelo
        Documento documentoAtualDadosDeclaradosSIMTR = this.documentoServico.localizaDocumentoClienteMaisRecenteByIdDossie(id, tipoDocumentoDadosDeclarados, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);

        List<br.gov.caixa.simtr.modelo.mapeamento.v2.dossiedigital.AtributoDocumentoDTO> ListaAtributosV2 = listaAtributosDeclarados.stream().map(ad -> {
            br.gov.caixa.simtr.modelo.mapeamento.v2.dossiedigital.AtributoDocumentoDTO atributoV2 = new br.gov.caixa.simtr.modelo.mapeamento.v2.dossiedigital.AtributoDocumentoDTO();
            atributoV2.setChave(ad.getChave());
            atributoV2.setValor(ad.getValor());
            atributoV2.setOpcoesSelecionadas(ad.getOpcoesSelecionadas());
           return atributoV2; 
        }).collect(Collectors.toList());
        
        // Inicializa a lista de atributos a ser vinculada ao documento de dados declarados
        List<AtributoDocumento> listaAtributosDocumento = this.montaListaAtributosDocumento(tipoDocumentoDadosDeclarados, ListaAtributosV2);

        // Cria registro do novo documento no SIMTR
        Documento documentoNovoDadosDeclaradosSIMTR = this.documentoServico.prototype(canal, Boolean.FALSE, tipoDocumentoDadosDeclarados, TemporalidadeDocumentoEnum.VALIDO, OrigemDocumentoEnum.O, null, listaAtributosDocumento, null);

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("LOGO_CAIXA", ConstantesUtil.RELATORIO_CAMINHO_IMAGENS.concat("caixa.png"));
        
        // Realiza a criação do documento PDF para armazenamento junto AO GED
        byte[] relatorio = this.relatorioServico.gerarRelatorioPDF("dossiedigital", tipoDocumentoDadosDeclarados, listaAtributosDocumento, parametros);
        String dadosDeclaradosPDFBase64 = new String(Base64.getEncoder().encode(relatorio));
        documentoNovoDadosDeclaradosSIMTR.setFormatoConteudoEnum(FormatoConteudoEnum.PDF);

        // Verifica se o os documentos estão sendo armazenados localmente ou no SIECM
        boolean armazenaLocal = Boolean.getBoolean("simtr_imagens_local");
        if (!armazenaLocal) {
            // Realiza a guarda do documento de dados declarados gerado no SIECM vinculado ao CPF/CNPJ do dossiê do cliente
            String codigoGED = this.siecmServico.armazenaDocumentoPessoalSIECM(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), documentoNovoDadosDeclaradosSIMTR, TemporalidadeDocumentoEnum.VALIDO, dadosDeclaradosPDFBase64, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);

            // Atribui o codigo GED no documento do SIMTR
            documentoNovoDadosDeclaradosSIMTR.setCodigoGED(codigoGED);
        }

        // Invalida o documento Atual de Dados Declarados no SIMTR
        if (documentoAtualDadosDeclaradosSIMTR != null) {
            documentoAtualDadosDeclaradosSIMTR.setDataHoraValidade(Calendar.getInstance());
            documentoAtualDadosDeclaradosSIMTR.setSituacaoTemporalidade(TemporalidadeDocumentoEnum.VENCIDO);
            this.documentoServico.update(documentoAtualDadosDeclaradosSIMTR);
        }

        // Salva o registro do documento no SIMTR
        documentoNovoDadosDeclaradosSIMTR.addDossiesCliente(dossieCliente);
        this.documentoServico.save(documentoNovoDadosDeclaradosSIMTR);

        // Persiste os atributos vinculados ao documento e suas referidas opções selecionadas
        listaAtributosDocumento.forEach(atributoDocumento -> {
            this.entityManager.persist(atributoDocumento);
            if (!atributoDocumento.getOpcoesSelecionadas().isEmpty()) {
                atributoDocumento.getOpcoesSelecionadas().forEach(opcaoSelecionada -> this.entityManager.persist(opcaoSelecionada));
            }
        });

        // Cria o vinculo entre o novo documento e o dossiê do cliente
        dossieCliente.addDocumentos(documentoNovoDadosDeclaradosSIMTR);
        this.dossieClienteServico.update(dossieCliente);

        return documentoNovoDadosDeclaradosSIMTR;
    }

    public void atualizaCadastroCaixaDossieCliente(Long id, List<Long> identificadoresDocumentoSIMTR, List<String> identificadoresDocumentoSIECM) {
        // Caso o elemento seja enviado nulo, inicializa uma lista vazia para inibir a ocorrencia de NullPointerException
        if(identificadoresDocumentoSIMTR == null) {
            identificadoresDocumentoSIMTR = new ArrayList<>();
        }
        
        // Caso o elemento seja enviado nulo, inicializa uma lista vazia para inibir a ocorrencia de NullPointerException
        if(identificadoresDocumentoSIECM == null) {
            identificadoresDocumentoSIECM = new ArrayList<>();
        }

        // Captura o canal de comunicação para utilizar na vinculação do documento
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "DDS.aCCDC.001 - ".concat(MENSAGEM_CANAL_NAO_LOCALIZADO));
        this.canalServico.validaOutorgaCanal(canal, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, "DDS.aCC.002 - ".concat(MENSAGEM_CANAL_NAO_AUTORIZADO));

        DossieCliente dossieCliente = this.dossieClienteServico.getById(id, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
        this.dossieClienteServico.validaRecursoLocalizado(dossieCliente, "DDS.aCCDC.002 - Dossiê de cliente não localizado pelo identificador informado");

        List<TipoDocumento> tipologiaIdentificada = dossieCliente.getDocumentos().stream()
                                                                 .map(d -> d.getTipoDocumento())
                                                                 .distinct().collect(Collectors.toList());

        List<Documento> documentosAptos = new ArrayList<>();

        tipologiaIdentificada.forEach(td -> {
            Optional<Documento> documento = dossieCliente.getDocumentos().stream()
                                                         .filter(d -> td.equals(d.getTipoDocumento()))
                                                         .filter(d -> !d.getAtributosDocumento().isEmpty())
                                                         .filter(d -> TemporalidadeDocumentoEnum.TEMPORARIO_ANTIFRAUDE.equals(d.getSituacaoTemporalidade()) ||
                                                                      TemporalidadeDocumentoEnum.VALIDO.equals(d.getSituacaoTemporalidade()))
                                                         .max(Comparator.comparing(Documento::getDataHoraCaptura));

            if (documento.isPresent()) {
                documentosAptos.add(documento.get());
            }
        });

        if (documentosAptos.isEmpty()) {
            throw new SimtrPreCondicaoException("DDS.aCCDC.004 - Não foram identificados documentos aptos para atualização cadastral do cliente solicitado.");
        }

        List<Long> identificadoresPendentesSIMTR = new ArrayList<>();
        List<String> identificadoresPendentesSIECM = new ArrayList<>();

        List<Documento> documentosAtualizacao = new ArrayList<>();

        if (identificadoresDocumentoSIMTR.isEmpty() && identificadoresDocumentoSIECM.isEmpty()) {
            documentosAptos.forEach(d -> {
                documentosAtualizacao.add(this.documentoServico.getById(d.getId(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
            });
        } else {
            identificadoresDocumentoSIMTR.forEach(idDoc -> {
                Optional<Documento> documento = documentosAptos.stream()
                                                               .filter(d -> idDoc.equals(d.getId()))
                                                               .findFirst();
                if (documento.isPresent()) {
                    documentosAtualizacao.add(documento.get());
                } else {
                    identificadoresPendentesSIMTR.add(idDoc);
                }
            });

            identificadoresDocumentoSIECM.forEach(idDoc -> {
                Optional<Documento> documento = documentosAptos.stream()
                                                               .filter(d -> idDoc.equals(d.getCodigoGED()) ||
                                                                            idDoc.equals(d.getCodigoSiecmCaixa()) ||
                                                                            idDoc.equals(d.getCodigoSiecmReuso()) ||
                                                                            idDoc.equals(d.getCodigoSiecmTratado()))
                                                               .findFirst();
                if (documento.isPresent()) {
                    documentosAtualizacao.add(documento.get());
                } else {
                    identificadoresPendentesSIECM.add(idDoc);
                }
            });

            if (!identificadoresPendentesSIMTR.isEmpty() || !identificadoresPendentesSIECM.isEmpty()) {
                String mensagem = MessageFormat.format("DDS.aCCDC.005 - Foram informados documentos não aptos a execução da atualização cadastral do cliente. Identificadores SIMTR: [{0}] | Identificadores SIECM: [{1}]", identificadoresPendentesSIMTR, identificadoresPendentesSIECM);
                throw new SimtrRequisicaoException(mensagem);
            }
        }

        this.cadastroCaixaServico.atualizaDadosDossieDigital(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), new ArrayList<>(), documentosAtualizacao);

        Calendar dataHoraAtualizacaoCadastro = Calendar.getInstance();
        documentosAtualizacao.forEach(documento -> {
            documento.setDataHoraCadastroCliente(dataHoraAtualizacaoCadastro);
            documento.setDossieDigital(Boolean.TRUE);
            documento.setSituacaoTemporalidade(TemporalidadeDocumentoEnum.VALIDO);
            this.documentoServico.update(documento);
        });
    }
    
    /**
     * Metodo chamado apenas apos atualizar Dossie Principal PJ
     *  @param idDossieProduto
     **/
    public void atualizarDCadastroCaixaDossieProdutoPf(Long idDossieProduto) {

        // Captura o canal de comunicação para utilizar na vinculação do documento
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "DDS.aCCDP.001 - ".concat(MENSAGEM_CANAL_NAO_LOCALIZADO));
        this.canalServico.validaOutorgaCanal(canal, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "DDS.aCC.002 - ".concat(MENSAGEM_CANAL_NAO_AUTORIZADO));

        DossieProduto dossieProduto = this.dossieProdutoServico.getById(idDossieProduto, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
        
        this.dossieProdutoServico.validaRecursoLocalizado(dossieProduto, "DDS.aCCDP.002 - Dossiê de produto não localizado pelo identificador informado");
        
        List<DossieClienteProduto> listaDossieClientePf =  dossieProduto.getDossiesClienteProduto().stream()
        		.filter(ds -> ds.getDossieCliente().getTipoPessoa().equals(TipoPessoaEnum.F))
        		.collect(Collectors.toList());
        
        for (DossieClienteProduto dossieCliente : listaDossieClientePf) {
            this.dossieClienteServico.atualizaCadastroCaixa(dossieCliente.getId());
		}
    }
    
    /**
     * 
     * @param idDossieProduto
     */
    public void atualizaCadastroCaixaDossieProduto(Long idDossieProduto) {

        // Captura o canal de comunicação para utilizar na vinculação do documento
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "DDS.aCCDP.001 - ".concat(MENSAGEM_CANAL_NAO_LOCALIZADO));
        this.canalServico.validaOutorgaCanal(canal, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "DDS.aCC.002 - ".concat(MENSAGEM_CANAL_NAO_AUTORIZADO));

        DossieProduto dossieProduto = this.dossieProdutoServico.getById(idDossieProduto, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
        this.dossieProdutoServico.validaRecursoLocalizado(dossieProduto, "DDS.aCCDP.002 - Dossiê de produto não localizado pelo identificador informado");

        List<RespostaDossie> respostasDossieCamposFormularioSICLI = dossieProduto.getRespostasDossie().stream()
                                                          .filter(respostaDossie -> Objects.nonNull(respostaDossie.getCampoFormulario().getNomeAtributoSICLI()) 
                                                                  && Objects.nonNull(respostaDossie.getCampoFormulario().getNomeObjetoSICLI())
                                                                  && Objects.nonNull(respostaDossie.getCampoFormulario().getTipoAtributoSicliEnum()))
                                                          .collect(Collectors.toList());
        
        DossieClienteProduto dossieClientePrincipal = dossieProduto.getDossiesClienteProduto().stream().filter(dossieClienteProduto -> dossieClienteProduto.getTipoRelacionamento().getIndicadorPrincipal()).findFirst().get();
        
        List<InstanciaDocumento> instanciasDocumentoDossieClientePrincipal = new ArrayList<>();
        dossieProduto.getInstanciasDocumento().forEach(instanciaDocumento -> {
            if ((instanciaDocumento.getDossieClienteProduto() != null) && (instanciaDocumento.getDossieClienteProduto().equals(dossieClientePrincipal))) {
                instanciasDocumentoDossieClientePrincipal.add(instanciaDocumento);
            }
        });
        
        List<TipoDocumento> tipologiaIdentificada = instanciasDocumentoDossieClientePrincipal.stream()
                                                    .filter(distinctByKey(instanciaDoc -> instanciaDoc.getDocumento().getTipoDocumento().getId()))
                                                    .map(instanciaDoc -> instanciaDoc.getDocumento().getTipoDocumento())
                                                    .collect(Collectors.toList());

        List<Documento> documentosAptos = new ArrayList<>();
        tipologiaIdentificada.forEach(tipIdent -> {
            Optional<Documento> documento = instanciasDocumentoDossieClientePrincipal.stream()
                    .map(instanciaDoc -> instanciaDoc.getDocumento())
                    .filter(doc -> tipIdent.equals(doc.getTipoDocumento()))
                    .filter(doc -> !doc.getAtributosDocumento().isEmpty())
                    .filter(doc -> TemporalidadeDocumentoEnum.TEMPORARIO_ANTIFRAUDE.equals(doc.getSituacaoTemporalidade()) ||
                                 TemporalidadeDocumentoEnum.VALIDO.equals(doc.getSituacaoTemporalidade()))
                    .max(Comparator.comparing(Documento::getDataHoraCaptura));

            if (documento.isPresent()) {
                documentosAptos.add(documento.get());
            }
        });

        if (documentosAptos.isEmpty()) {
            throw new SimtrPreCondicaoException("DDS.aCC.004 - Não foram identificados documentos aptos para atualização cadastral do cliente solicitado.");
        }
        
        List<Documento> documentosAtualizacao = new ArrayList<>();
        
        documentosAptos.forEach(d -> {
            documentosAtualizacao.add(this.documentoServico.getById(d.getId(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        });
        
        this.cadastroCaixaServico.atualizaDadosDossieDigital(dossieClientePrincipal.getDossieCliente().getCpfCnpj(), dossieClientePrincipal.getDossieCliente().getTipoPessoa(), respostasDossieCamposFormularioSICLI ,documentosAtualizacao);

        Calendar dataHoraAtualizacaoCadastro = Calendar.getInstance();
        documentosAtualizacao.forEach(documento -> {
            documento.setDataHoraCadastroCliente(dataHoraAtualizacaoCadastro);
            documento.setDossieDigital(Boolean.TRUE);
            documento.setSituacaoTemporalidade(TemporalidadeDocumentoEnum.VALIDO);
            this.documentoServico.update(documento);
        });
    }
    
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public void guardaDocumentoOperacaoDossieDigital(Long codigoAutorizacao, List<DocumentoConclusaoDTO> documentosConclusao) {
        // Captura o canal de comunicação
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "AS.gDODD.001 - ".concat(MENSAGEM_CANAL_NAO_LOCALIZADO));
        this.canalServico.validaOutorgaCanal(canal, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, "AS.vDA.002 - ".concat(MENSAGEM_CANAL_NAO_AUTORIZADO));


        // Captura da autorização concedida
        Autorizacao autorizacao = this.autorizacaoServico.getByCodigoAutorizacao(codigoAutorizacao, Boolean.FALSE, Boolean.FALSE);
        if (autorizacao == null) {
            throw new SimtrRequisicaoException("Autorização não localizada para codigo de autorização fornecido.");
        }

        List<Documento> documentosOperacao = documentosConclusao.stream().map(doc -> {
            TipoDocumento tipoDocumento = this.tipoDocumentoServico.getByNome(doc.getTipoDocumento());
            FormatoConteudoEnum formatoConteudoEnum = doc.getMimetype() != null ? FormatoConteudoEnum.getByMimeType(doc.getMimetype()) : null;
            List<AtributoDocumento> atributosDocumento = this.montaListaAtributosDocumento(tipoDocumento, doc.getAtributosDocumento());
            Documento documento = this.documentoServico.prototype(canal, Boolean.TRUE, tipoDocumento, TemporalidadeDocumentoEnum.VALIDO, OrigemDocumentoEnum.O, formatoConteudoEnum, atributosDocumento, doc.getConteudo());

            return documento;
        }).collect(Collectors.toList());
        
        // Verifica se todos os documentos definidos para a conclusão da operação foram encaminhados
        List<ComposicaoDocumental> composicoesConclusao = this.composicaoDocumentalServico.getComposicoesByProduto(autorizacao.getProdutoOperacao(), autorizacao.getProdutoModalidade(), Boolean.TRUE);
        
        List<RegraDocumental> regrasNaoAtendidas = new ArrayList<>();
        List<String> documentosAusentes = new ArrayList<>();

        // Percorre todas as composições de conclusão localizadas para o produto solicitado
        for (ComposicaoDocumental composicaoDocumental : composicoesConclusao) {
            
            List<RegraDocumental> regrasPendentes = this.nivelDocumentalComposicaoHelper.analisaComposicaoAtendida(composicaoDocumental, documentosOperacao);

            // Se todas as regras da composição foram atendidas, a coleção estara vazia, logo não precisa analisar a proxima composição
            if (regrasPendentes.isEmpty()) {
                break;
            } else {
                List<String> tiposAusentes = regrasPendentes.stream()
                        .filter(rd -> rd.getTipoDocumento() != null)
                        .map(rd -> rd.getTipoDocumento().getNome())
                        .collect(Collectors.toList());
                
                List<String> funcoesAusentes = regrasPendentes.stream()
                        .filter(rd -> rd.getFuncaoDocumental() != null)
                        .map(rd -> rd.getFuncaoDocumental().getNome())
                        .collect(Collectors.toList());
                
                regrasNaoAtendidas.addAll(regrasPendentes);
                documentosAusentes.addAll(tiposAusentes);
                documentosAusentes.addAll(funcoesAusentes);
            }
        }

        //Verifica se houveram regras não atendidas e lança uma exceção interrompendo o fluxo da operação
        if (!regrasNaoAtendidas.isEmpty()) {
            String mensagem = MessageFormat.format("Não foram identificados os seguintes documentos necessarios a conclusão da operação: {0}", documentosAusentes.toString());
            throw new SimtrRequisicaoException(mensagem);
        }

        //Percorre todos os documentos encaminhados para finalização da operação realização o armazenamento
        documentosOperacao.forEach(doc -> {            
            String binario = doc.getConteudos().stream().map(c -> c.getBase64()).findFirst().orElse(null);
            this.documentoServico.insereDocumentoOperacaoDossieDigital(autorizacao, doc, binario);
        });
        
        //Finaliza a autorização atribuindo uma data de conclusão junto a mesma
        this.autorizacaoServico.finalizaOperacao(autorizacao);
    }

    // ***** Métodos privados *******/
    private void validaBinario(String binario) {
        if (binario == null) {
            throw new SimtrRequisicaoException("DDS.vB.001 - A imagem encaminhada é nula");
        } else if (!binario.matches(ConstantesUtil.REGEX_BASE64)) {
            throw new SimtrRequisicaoException("DDS.vB.002 - A imagem encaminhada não trata-se de um formato Base64 válido");
        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    private boolean avaliarDadosCadastraisSIFRC(DossieCliente dossieCliente, Documento documento, boolean documentoAutorizadoCadastral) {
        // Realiza chamada ao SIFRC para avaliação dos dados cadastrais do documento
        if (documento.getTipoDocumento().getEnviaAvaliacaoCadastral()) {
            // Realiza chamada ao SIFRC para avaliação dos dados cadastrais do documento
            if (!this.avaliacaoFraudeServico.submeteDocumentoSIFRC(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), documento, Boolean.TRUE)) {
                LOGGER.log(Level.INFO, "Documento {0} do tipo {1} ainda não autorizado pelo avaiação de Autenticidade Cadastral", new Object[] {
                    documento.getId(),
                    documento.getTipoDocumento().getNome()
                });
                documentoAutorizadoCadastral = Boolean.FALSE;
            }
        } else {
            documentoAutorizadoCadastral = Boolean.TRUE;
        }
        return documentoAutorizadoCadastral;
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    private boolean capturarAvaliacaoAutenticidade(DossieCliente dossieCliente, Documento documento, boolean documentoAutorizadoDocumental) {
        if (documento.getTipoDocumento().getEnviaAvaliacaoDocumental()) {
            // Realiza chamada ao serviço de avaliação de autenticidade documental para capturar retorno da avaliação solicitada.
            if (!this.avaliacaoFraudeServico.recuperaResultadoAvaliacaoAutenticidade(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), documento)) {
                Object[] params = new Object[] {
                    documento.getId(),
                    documento.getTipoDocumento().getNome()
                };
                LOGGER.log(Level.INFO, "Documento {0} do tipo {1} ainda não autorizado pelo avaiação de Autenticidade Documental", params);
                documentoAutorizadoDocumental = Boolean.FALSE;
            }
        } else {
            documentoAutorizadoDocumental = Boolean.TRUE;
        }
        return documentoAutorizadoDocumental;
    }

    private DossieClienteDTO getByIdOrCpfCnpj(Long id, Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum) {
        DossieCliente dossieCliente;
        if (id != null) {
            this.nivelDocumentalServico.atualizaNiveisDocumentaisCliente(id);
        } else {
            this.nivelDocumentalServico.atualizaNiveisDocumentaisCliente(cpfCnpj, tipoPessoaEnum);
        }

        if (id != null) {
            dossieCliente = this.dossieClienteServico.getById(id, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
            tipoPessoaEnum = dossieCliente.getTipoPessoa();
        } else {
            dossieCliente = this.dossieClienteServico.getByCpfCnpj(cpfCnpj, tipoPessoaEnum, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
        }

        List<ComposicaoDocumental> composicoesCadastroCaixa = this.composicaoDocumentalServico.listComposicoesCadastroCaixa(tipoPessoaEnum);
        List<PendenciaCadastroDTO> pendenciasCadastroCaixa = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (ComposicaoDocumental composicao : composicoesCadastroCaixa) {
            List<Documento> documentosDossieDigital = dossieCliente.getDocumentos().stream()
                                                                   .filter(doc -> doc.getDataHoraValidade() == null || doc.getDataHoraValidade().after(calendar))
                                                                   .filter(doc -> doc.getDossieDigital())
                                                                   .collect(Collectors.toList());
            List<RegraDocumental> regrasNaoAtendidas = this.nivelDocumentalComposicaoHelper.analisaComposicaoAtendida(composicao, documentosDossieDigital);
            if (!regrasNaoAtendidas.isEmpty()) {
                regrasNaoAtendidas.forEach(regra -> {
                    if (regra.getTipoDocumento() != null) {
                        pendenciasCadastroCaixa.add(new PendenciaCadastroDTO(regra.getTipoDocumento(), composicao.getId()));
                    } else {
                        pendenciasCadastroCaixa.add(new PendenciaCadastroDTO(regra.getFuncaoDocumental(), composicao.getId()));
                    }
                });
            }
        }

        if (TipoPessoaEnum.F.equals(dossieCliente.getTipoPessoa())) {
            return new DossieClientePFDTO((DossieClientePF) dossieCliente, pendenciasCadastroCaixa);
        } else {
            return new DossieClientePJDTO((DossieClientePJ) dossieCliente, pendenciasCadastroCaixa);
        }
    }
    
    private List<AtributoDocumento> montaListaAtributosDocumento(TipoDocumento tipoDocumento, List<br.gov.caixa.simtr.modelo.mapeamento.v2.dossiedigital.AtributoDocumentoDTO> listaAtributosDeclarados){
     // Inicializa a lista de atributos a ser vinculada ao documento de dados declarados
        List<AtributoDocumento> listaAtributosDocumento = new ArrayList<>();

        // Percorre a lista de atributos esperados para o tipo de documento e monta a lista de Atributos do documento a ser associada ao mesmo
        tipoDocumento.getAtributosExtracao().forEach(atributoExtracao -> {
            // Captura o valor enviado na requisição como nome de negocio, caso não encontre utiliza o nome do atributo no GED
            br.gov.caixa.simtr.modelo.mapeamento.v2.dossiedigital.AtributoDocumentoDTO atributoDocumentoDTO = listaAtributosDeclarados.stream()
                                                                                .filter(ad -> ad.getChave().equals(atributoExtracao.getNomeAtributoDocumento()))
                                                                                .findFirst().orElse(null);

            // Caso o atributo esperado tenha sido enviado e o mesmo não é um atributo objetico, utiliza a resposta abertz como esperada
            if (atributoDocumentoDTO != null) {
                // Verifica se o atributo analisado possui retorno esperado na lista de atributos retornados do serviço de extração
                AtributoDocumento atributoDocumento = new AtributoDocumento();
                atributoDocumento.setDescricao(atributoExtracao.getNomeAtributoDocumento());
                atributoDocumento.setAcertoManual(Boolean.TRUE);

                // Atribui a resposta enviada como subjetiva ou objetica conforme defiição do atributo extração
                if (atributoExtracao.getOpcoesAtributo().isEmpty()) {
                    // Atribui o valor encaminhado como resposta aberta ao AtributoDocumento
                    atributoDocumento.setConteudo(atributoDocumentoDTO.getValor());
                } else {
                    // Percorre cada opcao esperada pelo atributo e se localizado valor enviado, incui uma entidade OpcaoSelecionada vinculada ao AtributoDocumento criado
                    atributoExtracao.getOpcoesAtributo().forEach(opcao -> {
                        atributoDocumentoDTO.getOpcoesSelecionadas().stream()
                                            .filter(os -> opcao.getValorOpcao().equals(os)).findFirst()
                                            .ifPresent(valor -> {
                                                OpcaoSelecionada opcaoSelecionada = new OpcaoSelecionada();
                                                opcaoSelecionada.setAtributoDocumento(atributoDocumento);
                                                opcaoSelecionada.setDescricaoOpcao(opcao.getDescricaoOpcao());
                                                opcaoSelecionada.setValorOpcao(valor);
                                                atributoDocumento.addOpcaoSelecionada(opcaoSelecionada);
                                            });
                    });
                }
                listaAtributosDocumento.add(atributoDocumento);
            }
        });
        
        return listaAtributosDocumento;
        
    }
    
    //Utility function
    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) 
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
