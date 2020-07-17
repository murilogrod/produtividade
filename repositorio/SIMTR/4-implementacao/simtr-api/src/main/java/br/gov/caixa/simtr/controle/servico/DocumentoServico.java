package br.gov.caixa.simtr.controle.servico;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.jboss.ejb3.annotation.SecurityDomain;

import com.itextpdf.text.pdf.PdfReader;

import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.DadosDocumentoLocalizadoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.RetornoPesquisaDTO;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.GEDService;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.pedesgo.arquitetura.siiso.dto.CnaeDTO;
import br.gov.caixa.pedesgo.arquitetura.siiso.dto.SocioDTO;
import br.gov.caixa.pedesgo.arquitetura.siiso.dto.SucessaoDTO;
import br.gov.caixa.pedesgo.arquitetura.util.UtilCnpj;
import br.gov.caixa.pedesgo.arquitetura.util.UtilCpf;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.pedesgo.arquitetura.util.UtilWS;
import br.gov.caixa.simtr.controle.excecao.SiecmException;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.vo.AtributoComplementarRelatorioVO;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.Autorizacao;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.Conteudo;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDocumento;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDossie;
import br.gov.caixa.simtr.modelo.entidade.SituacaoInstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieClienteManutencao;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.manutencao.MotivoReprovacaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.manutencao.ProdutoAvalidadorTomadorDTO;
import br.gov.caixa.simtr.modelo.relatorio.ConstantesRelatorio;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.util.KeycloakUtil;

@Stateless
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class DocumentoServico extends AbstractService<Documento, Long> {

    @Inject
    private EntityManager entityManager;

    @Inject
    private CalendarUtil calendarUtil;

    @EJB
    private CanalServico canalServico;

    @EJB
    private DossieClienteServico dossieClienteServico;

    @EJB
    private GEDService gedService;

    @EJB
    private KeycloakService keycloakService;

    @EJB
    private TipoDocumentoServico tipoDocumentoServico;

    @EJB
    private RelatorioServico relatorioServico;

    @EJB
    private SiecmServico siecmServico;

    @EJB
    private SituacaoDocumentoServico situacaoDocumentoServico;

    @Inject
    private KeycloakUtil keycloakUtil;

    private static final String PREFIXO_PASTA_NEGOCIO = "NEGOCIO";
    private static final Logger LOGGER = Logger.getLogger(DocumentoServico.class.getName());

	/** Atributo CAMINHO_RELATORIO. */
	private static final String CAMINHO_RELATORIO = "relatorios/negocio/";
    
    // SOCIOS
    private static final String SOCIO = "socio";
    private static final String NOME_SOCIO = "nome-socio";
    private static final String CODIGO_QUALIFICACAO = "codigo-qualificacao";
    private static final String DESCRICAO_QUALIFICACAO = "descricao-qualificacao";
    private static final String CPF_CNPJ = "cpf-cnpj";
    private static final String PC_CAPITAL_SOCIAL = "pc-capital-social";
    private static final String DATA_INICIO_SOCIO = "data-inicio-socio";
    private static final String CPF_REPRESENTANTE = "cpf-representante";
    private static final String NOME_REPRESENTANTE = "nome-representante";
    private static final String VINCULO_PENDENTE = "vinculo-pendente";
    // CNAE
    private static final String CNAE = "cnae";
    private static final String DESCRICAO_CNAE = "descricao-cnae";
    
    // SUCESSAO
    private static final String SUCESSAO = "sucessao";
    private static final String TIPO_VINCULO = "tipo-vinculo";
    private static final String DATA_INICIO_SUCESSAO = "data-inicio-sucessao";
    private static final String CNPJ_VINCULADOR = "cnpj-vinculador";
    private static final String NOME_VINCULADOR = "nome-vinculador";
    private static final String CNPJ_VINCULADO = "cnpj-vinculado";
    private static final String NOME_VINCULADO = "nome-vinculado";
    
    // PRODUTO
    private static final String PRODUTO = "produto";
    private static final String MOTIVO_REPROVACAO = "motivo_reprovacao";
    
    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Utilizado para realizar a captura de um Documento baseado no identificador único do GED.
     *
     * @param gedID Identificador único do documento no GED utilizado como parametro da localização
     * @param vinculacaoDossiesCliente Indicador se a vinculação relativas aos dossiês de cliente associados ao documento deve ser carregada
     * @param vinculacaoCanal Indicador se a vinculação relativas ao canal de captura do documento deve ser carregada
     * @param vinculacaoTipoDocumento Indicador se a vinculação relativas ao tipo do documento e suas funções exercidas deve ser carregada
     * @param vinculacaoAtributos Indicador se a vinculação relativas aos atributos do documento deve ser carregada
     * @param vinculacaoInstancias Indicador se as vinculações relativas as instancias do documento devem ser carregadas
     * @return Documento carregado conforme parametros indicados ou null caso o documento não seja localizado com o identificador informado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public Documento getByGedId(final String gedID, boolean vinculacaoDossiesCliente, boolean vinculacaoCanal, boolean vinculacaoTipoDocumento, boolean vinculacaoAtributos, boolean vinculacaoInstancias) {
        String query = this.getQueryDocumento(Boolean.FALSE, vinculacaoDossiesCliente, vinculacaoCanal, vinculacaoTipoDocumento, vinculacaoAtributos, vinculacaoInstancias);
        StringBuilder jpql = new StringBuilder(query);

        jpql.append(" WHERE d.codigoSiecmCaixa = :codigoGED");
        jpql.append(" OR d.codigoSiecmTratado = :codigoGED");
        jpql.append(" OR d.codigoGED = :codigoGED");
        jpql.append(" OR d.codigoSiecmCaixa = :codigoGED");

        try {
            TypedQuery<Documento> queryDocumento = this.entityManager.createQuery(jpql.toString(), Documento.class);
            queryDocumento.setParameter("codigoGED", gedID);
            return queryDocumento.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            LOGGER.log(Level.WARNING, "Documento não localizado sob identificador: {0}", gedID);
            return null;
        }
    }

    /**
     * Utilizado para realizar a captura de um Documento baseado no identificador único do SIMTR.
     *
     * @param id Identificador único do documento no SIMTR utilizado como parametro da localização
     * @param vinculacaoConteudos Indicador se os conteudos do documento devem ser carregados
     * @param vinculacaoDossiesCliente Indicador se a vinculação relativas aos dossiês de cliente associados ao documento deve ser carregada
     * @param vinculacaoCanal Indicador se a vinculação relativas ao canal de captura do documento deve ser carregada
     * @param vinculacaoTipoDocumento Indicador se a vinculação relativas ao tipo do documento e suas funções exercidas deve ser carregada
     * @param vinculacaoAtributos Indicador se a vinculação relativas aos atributos do documento deve ser carregada
     * @param vinculacaoInstancias Indicador se as vinculações relativas as instancias do documento devem ser carregadas
     * @return Documento carregado conforme parametros indicados ou null caso o documento não seja localizado com o identificador informado
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRPAEOPE,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    public Documento getById(final Long id, boolean vinculacaoConteudos, boolean vinculacaoDossiesCliente, boolean vinculacaoCanal, boolean vinculacaoTipoDocumento, boolean vinculacaoAtributos, boolean vinculacaoInstancias) {
        String query = this.getQueryDocumento(vinculacaoConteudos, vinculacaoDossiesCliente, vinculacaoCanal, vinculacaoTipoDocumento, vinculacaoAtributos, vinculacaoInstancias);
        StringBuilder jpql = new StringBuilder(query);
        jpql.append(" WHERE d.id = :id");

        Documento documento;
        try {
            TypedQuery<Documento> queryDocumento = this.entityManager.createQuery(jpql.toString(), Documento.class);
            queryDocumento.setParameter("id", id);
            documento = queryDocumento.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            LOGGER.log(Level.WARNING, "Documento não localizado sob identificador: {0}", id);
            return null;
        }

        // Captura o código do SIECM a ser verificado na busca da imagem, dando preferencia na ordem:
        // 1 - Reuso | 2 - Tratado | 3 - Prorpio | 4 - Caixa
        String codigoSiecm = documento.getCodigoSiecmReuso();
        String objectStore = ConstantesUtil.SIECM_OS_REUSO;
        
        if(codigoSiecm == null) {
            codigoSiecm = documento.getCodigoSiecmTratado();
            objectStore = ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL;
        }
        
        if(codigoSiecm == null) {
            codigoSiecm = documento.getCodigoGED();
            objectStore = ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL;
        }
        
        if(codigoSiecm == null) {
            codigoSiecm = documento.getCodigoSiecmCaixa();
            objectStore = ConstantesUtil.SIECM_OS_CAIXA;
        }
        
        RetornoPesquisaDTO retornoGED = null;
        try {
	        if (vinculacaoConteudos && Objects.nonNull(codigoSiecm)) {
	            documento.getConteudos().clear();
	            
	            String token = this.keycloakUtil.getTokenServico();
                    String ip = this.keycloakUtil.getIpFromToken(token);
                    retornoGED = this.gedService.searchDocument(codigoSiecm, objectStore, ip, token);
	
	            // Verifica se o documento foi localizado junto ao SIECM
	            if (retornoGED == null || retornoGED.getQuantidade() == 0) {
	
	                if (retornoGED == null) {
	                    throw new SiecmException("DS.gBI.001 - Serviço GED indisponível", Boolean.FALSE);
	                } else {
	                    String mensagem = MessageFormat.format("DS.gBI.002 - Conteudo do documento não localizado no SIECM. ID: {0} | OS: {1}", codigoSiecm, objectStore);
	                    throw new SiecmException(mensagem, Boolean.TRUE);
	                }
	            }
	
	            int ordem = 1;
	            for (DadosDocumentoLocalizadoDTO dadosDocumento : retornoGED.getDadosDocumentoLocalizados()) {
	                String link = dadosDocumento.getLink();
	                byte[] bytes = UtilWS.obterBytes(link);
	                String conteudoBase64 = Base64.getEncoder().encodeToString(bytes);
	                Conteudo c = new Conteudo(conteudoBase64, ordem++);
	                documento.addConteudos(c);
	            }
	        }
        } catch(Exception e) {
        	String mensagem = MessageFormat.format("DS.gBI.003 - Falha ao obter conteúdo no SIECM. ID: {0} | OS: {1}", codigoSiecm, objectStore);
        	throw new SiecmException(mensagem, e, retornoGED != null);
        }

        return documento;        
    }

    /**
     * Utilizado para realizar a captura de um Documento baseado no identificador único do SIMTR ou ou identificador único do GED.
     *
     * @param id Identificador único do documento no SIMTR utilizado como parametro da localização
     * @param gedID Identificador único do documento no GED utilizado como parametro da localização
     * @param vinculacaoConteudos Indicador se os conteudos do documento devem ser carregados
     * @param vinculacaoDossiesCliente Indicador se a vinculação relativas aos dossiês de cliente associados ao documento deve ser carregada
     * @param vinculacaoCanal Indicador se a vinculação relativas ao canal de captura do documento deve ser carregada
     * @param vinculacaoTipoDocumento Indicador se a vinculação relativas ao tipo do documento e suas funções exercidas deve ser carregada
     * @param vinculacaoAtributos Indicador se a vinculação relativas aos atributos do documento deve ser carregada
     * @param vinculacaoInstancias Indicador se as vinculações relativas as instancias do documento devem ser carregadas
     * @return String que representa query de execução da consulta, pendente apenas de inclusão da clausula WHERE conforme cada caso.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    private String getQueryDocumento(boolean vinculacaoConteudos, boolean vinculacaoDossiesCliente, boolean vinculacaoCanal, boolean vinculacaoTipoDocumento, boolean vinculacaoAtributos, boolean vinculacaoInstancias) {
        final StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT d FROM Documento d ");
        jpql.append(" LEFT JOIN FETCH d.controlesDocumento cd ");
        if (vinculacaoConteudos) {
            jpql.append(" LEFT JOIN FETCH d.conteudos c ");
        }
        if (vinculacaoDossiesCliente) {
            jpql.append(" LEFT JOIN FETCH d.dossiesCliente dc ");
        }
        if (vinculacaoCanal) {
            jpql.append(" LEFT OUTER JOIN FETCH d.canalCaptura cc ");
        }
        if (vinculacaoTipoDocumento) {
            jpql.append(" LEFT JOIN FETCH d.tipoDocumento td ");
            jpql.append(" LEFT JOIN FETCH td.funcoesDocumentais fd ");
            jpql.append(" LEFT JOIN FETCH td.atributosExtracao ae ");
        }
        if (vinculacaoAtributos) {
            jpql.append(" LEFT JOIN FETCH d.atributosDocumento ad ");
            jpql.append(" LEFT JOIN FETCH ad.opcoesSelecionadas os ");
        }
        if (vinculacaoInstancias) {
            jpql.append(" LEFT JOIN FETCH d.instanciasDocumento id ");
            jpql.append(" LEFT JOIN FETCH id.situacoesInstanciaDocumento sid ");
            jpql.append(" LEFT JOIN FETCH sid.situacaoDocumento sd ");
        }
        return jpql.toString();
    }

    /**
     * Armazena um registro de documento e vincula o mesmo a um dossiê de cliente no ambito do Apoio ao Negocio.
     *
     * @param cpfCnpj CPF/CNPJ de identificação do cliente a ser asociado ao documento no GED
     * @param tipoPessoaEnum Identificação do tipo de pessoa
     * @param documento Documento a ser gravado. Necessario possuir os atributos definidos e o Tipo de Documento preenchido com os atributos de extração
     * @throws SimtrRequisicaoException Lançada em caso de parametros invalidos
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public void insereDocumentoClienteNegocio(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, Documento documento) {
        // Validando CPF/CNPJ informado e formatando
        if ((TipoPessoaEnum.F.equals(tipoPessoaEnum)) && (!UtilCpf.isValidCpf(cpfCnpj))) {
            Object[] params = new Object[] {
                ConstantesUtil.MSG_CPF_INVALIDO
            };
            LOGGER.log(Level.WARNING, "DS.iDCN.001 - {0}", params);
            throw new SimtrRequisicaoException(ConstantesUtil.MSG_CPF_INVALIDO);

        } else if ((TipoPessoaEnum.J.equals(tipoPessoaEnum)) && (!UtilCnpj.isValidCnpj(cpfCnpj))) {
            Object[] params = new Object[] {
                ConstantesUtil.MSG_CNPJ_INVALIDO
            };
            LOGGER.log(Level.WARNING, "DS.iDCN.001 - {0}", params);
            throw new SimtrRequisicaoException(ConstantesUtil.MSG_CNPJ_INVALIDO);
        }

        if (documento == null) {
            Object[] params = new Object[] {
                ConstantesUtil.MSG_DOCUMENTO_ENCAMINHADO_NULO
            };
            LOGGER.log(Level.WARNING, "DS.iDCN.002 - {0}", params);
            throw new SimtrRequisicaoException(ConstantesUtil.MSG_DOCUMENTO_ENCAMINHADO_NULO);
        }

        if (documento.getTipoDocumento() == null) {
            Object[] params = new Object[] {
                ConstantesUtil.MSG_DOCUMENTO_NAO_CONTEM_TIPO
            };
            LOGGER.log(Level.WARNING, "DS.iDCN.003 - {0}", params);
            throw new SimtrRequisicaoException(ConstantesUtil.MSG_DOCUMENTO_NAO_CONTEM_TIPO);
        }

        if (documento.getAtributosDocumento() == null) {
            Object[] params = new Object[] {
                ConstantesUtil.MSG_DOCUMENTO_NAO_CONTEM_ATRIBUTOS
            };
            LOGGER.log(Level.WARNING, "DS.iDCN.004 - {0}", params);
            throw new SimtrRequisicaoException(ConstantesUtil.MSG_DOCUMENTO_NAO_CONTEM_ATRIBUTOS);
        }

        if (documento.getCanalCaptura() == null) {
            Object[] params = new Object[] {
                ConstantesUtil.MSG_DOCUMENTO_SEM_CANAL
            };
            LOGGER.log(Level.WARNING, "DS.iDCN.005 - {0}", params);
            throw new SimtrRequisicaoException(ConstantesUtil.MSG_DOCUMENTO_SEM_CANAL);
        }
        
        if(!documento.getAtributosDocumento().isEmpty()) {
            this.validaAtributosDocumento(documento);
        }

        String tipologiaDadosDeclarados = ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_PF;
        String tipologiaDadosSIISO = ConstantesUtil.TIPOLOGIA_DADOS_SIISO;
        String tipologiaDadosSIRIC = ConstantesUtil.TIPOLOGIA_DADOS_AVALIADOR_TOMADOR;
        String tipologiaDadosDeclaradosConjuge = ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_PF_CONJUGE;
        TipoDocumento tipoDocumentoDadosPDF = null;
        if (TipoPessoaEnum.J.equals(tipoPessoaEnum) && Objects.nonNull(documento.getTipoDocumento()) && Objects.nonNull(documento.getTipoDocumento().getCodigoTipologia())
        		&& !documento.getTipoDocumento().getCodigoTipologia().equals(tipologiaDadosSIISO)
        		 && !documento.getTipoDocumento().getCodigoTipologia().equals(tipologiaDadosSIRIC)) {            
        	tipologiaDadosDeclarados = ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_PJ;
        	tipoDocumentoDadosPDF = this.tipoDocumentoServico.getByTipologia(tipologiaDadosDeclarados);
        }else {
        	if(Objects.nonNull(documento.getTipoDocumento())  && Objects.nonNull(documento.getTipoDocumento().getCodigoTipologia()) 
        			&& Objects.nonNull(documento.getTipoDocumento().getCodigoTipologia()) && documento.getTipoDocumento().getCodigoTipologia().equals(tipologiaDadosSIISO)) {
        		tipoDocumentoDadosPDF = this.tipoDocumentoServico.getByTipologia(tipologiaDadosSIISO);        		
        	}
        	if(Objects.nonNull(documento.getTipoDocumento())  && Objects.nonNull(documento.getTipoDocumento().getCodigoTipologia())
        			&& Objects.nonNull(documento.getTipoDocumento().getCodigoTipologia()) && documento.getTipoDocumento().getCodigoTipologia().equals(tipologiaDadosSIRIC)) {
        		tipoDocumentoDadosPDF = this.tipoDocumentoServico.getByTipologia(tipologiaDadosSIRIC); 
        	}
        }
        TipoDocumento tipoDocumentoDadosDeclaradosConjuge = this.tipoDocumentoServico.getByTipologia(tipologiaDadosDeclaradosConjuge);

        // Caso o documento seja de dados declarados, cria o PDF para armazenamento
        if ((Objects.nonNull(tipoDocumentoDadosPDF) && tipoDocumentoDadosPDF.equals(documento.getTipoDocumento())) 
                || (Objects.nonNull(tipoDocumentoDadosDeclaradosConjuge) && tipoDocumentoDadosDeclaradosConjuge.equals(documento.getTipoDocumento()))) {

            String cpfCnpjFormatado;
            String chaveAtributo;

            if (TipoPessoaEnum.F.equals(tipoPessoaEnum)) {
                chaveAtributo = ConstantesRelatorio.CPF;
                cpfCnpjFormatado = StringUtils.leftPad(String.valueOf(cpfCnpj), 11, '0');
                cpfCnpjFormatado = cpfCnpjFormatado.substring(0, 3).concat(".")
                                                   .concat(cpfCnpjFormatado.substring(3, 6)).concat(".")
                                                   .concat(cpfCnpjFormatado.substring(6, 9)).concat("-")
                                                   .concat(cpfCnpjFormatado.substring(9, 11));
            } else {
                chaveAtributo = ConstantesRelatorio.CNPJ;
                cpfCnpjFormatado = StringUtils.leftPad(String.valueOf(cpfCnpj), 14, '0');
                cpfCnpjFormatado = cpfCnpjFormatado.substring(0, 2).concat(".")
                                                   .concat(cpfCnpjFormatado.substring(2, 5)).concat(".")
                                                   .concat(cpfCnpjFormatado.substring(5, 8)).concat("/")
                                                   .concat(cpfCnpjFormatado.substring(8, 12)).concat("-")
                                                   .concat(cpfCnpjFormatado.substring(12, 14));
            }

            // Inclui o CPF/CNPJ do cliente no documento para apresentar com oidentificação do mesmo
            AtributoComplementarRelatorioVO atributoCpfCnpj = new AtributoComplementarRelatorioVO(chaveAtributo, cpfCnpjFormatado, TipoAtributoEnum.STRING);

            // Realiza a criação do documento PDF para armazenamento junto AO GED
            String dadosDeclaradosPDFBase64 = new String();
            try {
                byte[] relatorio = this.gerarMinutaDocumento(documento, atributoCpfCnpj);
                dadosDeclaradosPDFBase64 = new String(Base64.getEncoder().encode(relatorio));
            } catch (Exception e) {
            	if(!documento.getTipoDocumento().getCodigoTipologia().equals(tipologiaDadosSIISO)) {
            		throw new SimtrConfiguracaoException("DS.iDCN.001 - Falha ao criar binario de Dados Declarados para atualização.", e);            		
            	}else {
            		throw new SimtrConfiguracaoException("DS.iDCN.001 - Falha ao criar binario de Dados Pesquisa SIISO para atualização.", e);  
            	}
            }

            Conteudo conteudo = new Conteudo();
            conteudo.setBase64(dadosDeclaradosPDFBase64);
            conteudo.setDocumento(documento);
            conteudo.setOrdem(1);

            documento.addConteudos(conteudo);
            documento.setFormatoConteudoEnum(FormatoConteudoEnum.PDF);
        }

        // Invalida os documentos anteriores de mesma tipologia para novos negócios, caso a tipologia não permita manutenção de multiplos documentos.
        this.invalidarDocumentosAtivosCliente(null, cpfCnpj, tipoPessoaEnum, documento.getTipoDocumento(), Boolean.TRUE);

        boolean armazenaLocal = Boolean.getBoolean("simtr_imagens_local");
        if (!armazenaLocal && !documento.getConteudos().isEmpty()) {
            String binario = documento.getConteudos().stream()
                                      .sorted(Comparator.comparing(Conteudo::getOrdem))
                                      .map(c -> c.getBase64())
                                      .findFirst().orElse(null);

            // Limpa os conteudos do documento antes de salva-los visto que os mesmo serão enviados para o GED
            documento.getConteudos().clear();

            // Realiza a gravação do documento no GED
            String codigoSIECM = this.siecmServico.armazenaDocumentoPessoalSIECM(cpfCnpj, tipoPessoaEnum, documento, TemporalidadeDocumentoEnum.VALIDO, binario, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
            documento.setCodigoGED(codigoSIECM);
        }

        // Salva o registro do documento
        this.save(documento);
    }

    /**
     * Armazena um registro de documento e cria um documento no GED vinculado a pasta da autorização.
     *
     * @param autorizacao Autorizacao de vinculação da operacao
     * @param documento Documento a ser gravado. Necessario possuir os atributos definidos e o Tipo de Documento preenchido com os atributos de extração
     * @param binario Conteudo em formato Base64 que representa o documento.
     * @return Objeto que representa o documento armazenado
     * @throws SimtrRequisicaoException Lançada c
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public Documento insereDocumentoOperacaoDossieDigital(Autorizacao autorizacao, Documento documento, String binario) {
        if (autorizacao == null) {
            throw new SimtrRequisicaoException("DS.iDODD.001 - Autorização encaminhada vinculação do documento é nulo");
        }
        validarDocumento(documento);

        String pastaAutorizacao = String.valueOf(autorizacao.getCodigoNSU());

        // Realiza a gravação do documento no GED
        String identificadorSIECM = this.siecmServico.armazenaDocumentoOperacaoSIECM(documento, TemporalidadeDocumentoEnum.VALIDO, binario, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL, pastaAutorizacao);
        documento.setCodigoGED(identificadorSIECM);

        // Salva o registro do documento encaminhado
        this.save(documento);

        return documento;
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    private void validarDocumento(Documento documento) {
        if (documento == null) {
            throw new SimtrRequisicaoException("DS.vD.002 - ".concat(ConstantesUtil.MSG_DOCUMENTO_ENCAMINHADO_NULO));
        }
        
        if (documento.getTipoDocumento() == null) {
            throw new SimtrRequisicaoException("DS.vD.003 - ".concat(ConstantesUtil.MSG_DOCUMENTO_NAO_CONTEM_TIPO));
        }
        
        if (documento.getAtributosDocumento() == null) {
            throw new SimtrRequisicaoException("DS.vD.004 - ".concat(ConstantesUtil.MSG_DOCUMENTO_NAO_CONTEM_ATRIBUTOS));
        }        

        if (documento.getCanalCaptura() == null) {
            throw new SimtrRequisicaoException("DS.vD.005 - ".concat(ConstantesUtil.MSG_DOCUMENTO_SEM_CANAL));
        }

        if(!documento.getAtributosDocumento().isEmpty()) {
            this.validaAtributosDocumento(documento);
        }
    }

    /**
     * Armazena um registro de documento e cria um documento no GED vinculado a pasta dos documentos de apoio ao negocio.
     *
     * @param documento Documento a ser gravado. Necessario possuir os atributos definidos e o Tipo de Documento preenchido com os atributos de extração
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public void insereDocumentoOperacaoNegocio(Long idDossieProduto, Documento documento) {
        validarDocumento(documento);

        boolean armazenaLocal = Boolean.getBoolean("simtr_imagens_local");
        if (!armazenaLocal && !documento.getConteudos().isEmpty()) {
            String imagemBase64 = documento.getConteudos().stream()
                                           .sorted(Comparator.comparing(Conteudo::getOrdem))
                                           .map(c -> c.getBase64())
                                           .findFirst().orElse(null);

            // Limpa os conteudos do documento antes de salva-los visto que os mesmo serão
            // enviados para o GED
            documento.getConteudos().clear();

            // Realiza a gravação do documento no GED
            String folder = PREFIXO_PASTA_NEGOCIO.concat("_").concat(String.valueOf(idDossieProduto));
            String identificadorSiecm = this.siecmServico.armazenaDocumentoOperacaoSIECM(documento, TemporalidadeDocumentoEnum.VALIDO, imagemBase64, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL, folder);
            documento.setCodigoGED(identificadorSiecm);
        }

        // Salva o registro do documento encaminhado
        this.save(documento);

    }

    /**
     * Cria um prototipo de objeto do Documento baseado nos parametros informados. São validados os atributos definidospara o documento e definida a data de validade conforme configuração para o tipo
     * de documento
     *
     * @param canal Canal de captura das informações do documento
     * @param dossieDigital Indica se o documento esta vinculado ao dossiê digital
     * @param temporalidadeDocumentoEnum Identifica a temporalidade que deve ser aplicada ao documento criado
     * @param tipoDocumento Tipo de documento a ser associado ao documento criado
     * @param origemDocumentoEnum Identifica a midia origem a ser associada a captura do documento do documento
     * @param formatoConteudoEnum Indica o formato do conteudo do documento a ser armazenado.
     * @param atributosDocumento Mapa de objetos chave e valor que represetam a lista de atributos a ser vinculado ao mesmo
     * @param binario formato base64
     * @return Retorna objeto que representa o documento montado, ainda não persistido.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Documento prototype(Canal canal, boolean dossieDigital, TipoDocumento tipoDocumento, TemporalidadeDocumentoEnum temporalidadeDocumentoEnum, OrigemDocumentoEnum origemDocumentoEnum, FormatoConteudoEnum formatoConteudoEnum, Map<String, String> atributosDocumento, String binario) {

        List<AtributoDocumento> atributos = atributosDocumento.entrySet().stream()
                                                              .map(registro -> {
                                                                  AtributoDocumento atributoDocumento = new AtributoDocumento();
                                                                  atributoDocumento.setDescricao(registro.getKey());
                                                                  atributoDocumento.setConteudo(registro.getValue());
                                                                  atributoDocumento.setAcertoManual(false);

                                                                  return atributoDocumento;

                                                              }).collect(Collectors.toList());

        return this.prototype(canal, dossieDigital, tipoDocumento, temporalidadeDocumentoEnum, origemDocumentoEnum, formatoConteudoEnum, atributos, binario);
    }

    /**
     * Cria um prototipo de objeto do Documento baseado nos parametros informados. São validados os atributos definidospara o documento e definida a data de
     * validade conforme configuração para o tipo de documento
     *
     * @param canal Canal de captura das informações do documento
     * @param dossieDigital Indica se o documento esta vinculado ao dossiê digital
     * @param temporalidadeDocumentoEnum Identifica a temporalidade que deve ser aplicada ao documento criado
     * @param tipoDocumento Tipo de documento a ser associado ao documento criado
     * @param origemDocumentoEnum Identifica a midia origem a ser associada a captura do documento do documento
     * @param formatoConteudoEnum Indica o formato do conteudo do documento a ser armazenado.
     * @param atributosDocumento Lista de Atrributos do documento a ser vinculado ao mesmo
     * @param binario em formato base64
     * @return Retorna objeto que representa o documento montado, ainda não persistido.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Documento prototype(Canal canal, boolean dossieDigital, TipoDocumento tipoDocumento, TemporalidadeDocumentoEnum temporalidadeDocumentoEnum, OrigemDocumentoEnum origemDocumentoEnum, FormatoConteudoEnum formatoConteudoEnum, List<AtributoDocumento> atributosDocumento, String binario) {

        Documento documento = new Documento();

        // Define os atributos necessarios ao documento
        documento.setCanalCaptura(canal);
        documento.setDossieDigital(dossieDigital);
        documento.setTipoDocumento(tipoDocumento);
        documento.setDataHoraCaptura(Calendar.getInstance());
        String responsavel = this.keycloakService.getMatricula().length() < 20 ? this.keycloakService.getMatricula() : canal.getSigla();
        documento.setResponsavel(responsavel);
        documento.setSituacaoTemporalidade(temporalidadeDocumentoEnum);
        documento.setOrigemDocumento(origemDocumentoEnum);
        
        if (atributosDocumento != null) {
            
            atributosDocumento.forEach(atributoDocumento -> {
                atributoDocumento.setDocumento(documento);
                documento.addAtributosDocumento(atributoDocumento);
            });
        }

        long quantidadeBytes = 0;
        int ordem = 0;
        if (binario != null) {
            Conteudo conteudo = new Conteudo();
            conteudo.setBase64(binario);
            conteudo.setOrdem(++ordem);
            conteudo.setDocumento(documento);
            int quantidadeConteudos = this.defindeQuantidadePaginasDocumento(binario, formatoConteudoEnum);
            int tamanhoString = binario.length();
            int quantidadeComplemento = binario.endsWith("==") ? 2 : this.endsWithSymbolEqual(binario);
            quantidadeBytes += (3 * (tamanhoString / 4)) - quantidadeComplemento;
            
            documento.setFormatoConteudoEnum(formatoConteudoEnum);
            documento.addConteudos(conteudo);
            documento.setQuantidadeConteudos(quantidadeConteudos);
        }
        documento.setQuantidadeBytes(quantidadeBytes);
        
        // caso a tipologia seja definida, valida os atributos e define a data de
        // validade para novos negocios
        if (documento.getTipoDocumento() != null) {

            // Define a data de validade do documento baseado nos atributos do documento
            this.defineDataValidade(documento);
        }

        return documento;

    }

    /**
     * Localiza o registro de documento mais recente para o tipo de documento especificado vinculado a um cliente especifico.
     *
     * @param cpfCnpj CPF/CNPJ utilizado na identificação do dossiê de cliente
     * @param tipoPessoaEnum Indicador de tipo de pessoa de F ou J
     * @param tipoDocumento Tipo de documento desejado
     * @param vinculacaoCanal Indicação para carregamento do canal vinculado ao documento
     * @param vinculacaoTipoDocumental Indicação para carregamento do tipo de documento vinculado ao documento com as funções associadas e os atributos de extração
     *        definidos
     * @param vinculacaoAtributos Indicação para carregamento dos atributos vinculados ao documento
     * @param vinculacaoInstancias Indicação para carregamento das instancias vinculadas ao documento com as situações atribuidas as instancias e motivos dessas
     *        situações
     * @param vinculacaoDossiesCliente Indicação para carregamento dos dossiês de cliente vinculados ao documento
     * @return Documento carregado com as indicações definidas ou null caso não tenha documento não seja localizado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public Documento localizaDocumentoMaisRecenteClienteByCpfCnpj(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, TipoDocumento tipoDocumento, boolean vinculacaoCanal, boolean vinculacaoTipoDocumental, boolean vinculacaoAtributos, boolean vinculacaoInstancias) {
        if (Objects.isNull(cpfCnpj)) {
            throw new IllegalArgumentException("O CPF/CNPJ informado não pode ser nulo.");
        }
        if (Objects.isNull(tipoDocumento)) {
            throw new IllegalArgumentException("O tipo de documento informado não pode ser nulo.");
        }

        return this.localizaDocumentoMaisRecenteCliente(null, cpfCnpj, tipoPessoaEnum, tipoDocumento, vinculacaoCanal, vinculacaoTipoDocumental, vinculacaoAtributos, vinculacaoInstancias);
    }

    /**
     * Localiza o registro de documento mais recente para o tipo de documento especificado vinculado a um cliente especifico.
     *
     * @param id Identificador unico do dossiê de cliente
     * @param tipoDocumento Tipo de documento desejado
     * @param vinculacaoCanal Indicação para carregamento do canal vinculado ao documento
     * @param vinculacaoTipoDocumental Indicação para carregamento do tipo de documento vinculado ao documento com as funções associadas e os atributos de extração
     *        definidos
     * @param vinculacaoAtributos Indicação para carregamento dos atributos vinculados ao documento
     * @param vinculacaoInstancias Indicação para carregamento das instancias vinculadas ao documento com as situações atribuidas as instancias e motivos dessas
     *        situações
     * @param vinculacaoDossiesCliente Indicação para carregamento dos dossiês de cliente vinculados ao documento
     * @return Documento carregado com as indicações definidas ou null caso não tenha documento não seja localizado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public Documento localizaDocumentoClienteMaisRecenteByIdDossie(Long id, TipoDocumento tipoDocumento, boolean vinculacaoCanal, boolean vinculacaoTipoDocumental, boolean vinculacaoAtributos, boolean vinculacaoInstancias) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("O Id informado não pode ser nulo.");
        }
        if (Objects.isNull(tipoDocumento)) {
            throw new IllegalArgumentException("O tipo de documento informado não pode ser nulo.");
        }

        return this.localizaDocumentoMaisRecenteCliente(id, null, null, tipoDocumento, vinculacaoCanal, vinculacaoTipoDocumental, vinculacaoAtributos, vinculacaoInstancias);

    }

    /**
     * Localiza o registro de documento mais recente para o tipo de documento especificado vinculado a um cliente especifico.
     *
     * @param id Identificador unico do dossiê de cliente
     * @param cpfCnpj CPF/CNPJ utilizado na identificação do dossiê de cliente
     * @param tipoPessoaEnum Identificador do tipo de pessoa, se fisica ou juridica
     * @param tipoDocumento Tipo de documento desejado
     * @param vinculacaoCanal Indicação para carregamento do canal vinculado ao documento
     * @param vinculacaoTipoDocumental Indicação para carregamento do tipo de documento vinculado ao documento com as funções associadas e os atributos de extração
     *        definidos
     * @param vinculacaoAtributos Indicação para carregamento dos atributos vinculados ao documento
     * @param vinculacaoInstancias Indicação para carregamento das instancias vinculadas ao documento com as situações atribuidas as instancias e motivos dessas
     *        situações
     * @param vinculacaoDossiesCliente Indicação para carregamento dos dossiês de cliente vinculados ao documento
     * @return Documento carregado com as indicações definidas ou null caso não tenha documento não seja localizado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    private Documento localizaDocumentoMaisRecenteCliente(Long id, Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, TipoDocumento tipoDocumento, boolean vinculacaoCanal, boolean vinculacaoTipoDocumental, boolean vinculacaoAtributos, boolean vinculacaoInstancias) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT d FROM Documento d ");
        jpql.append(" LEFT JOIN FETCH d.dossiesCliente dc1 ");
        if (vinculacaoCanal) {
            jpql.append(" LEFT JOIN FETCH d.canalCaptura cc ");
        }
        if (vinculacaoTipoDocumental) {
            jpql.append(" LEFT JOIN FETCH d.tipoDocumento td1 ");
            jpql.append(" LEFT JOIN FETCH td1.funcoesDocumentais fd ");
            jpql.append(" LEFT JOIN FETCH td1.atributosExtracao ae ");
        }
        if (vinculacaoAtributos) {
            jpql.append(" LEFT JOIN FETCH d.atributosDocumento ad ");
            jpql.append(" LEFT JOIN FETCH ad.opcoesSelecionadas os ");

        }

        if (vinculacaoInstancias) {
            jpql.append(" LEFT JOIN FETCH d.instanciasDocumento id ");
            jpql.append(" LEFT JOIN FETCH id.situacoesInstanciaDocumento sid ");
            jpql.append(" LEFT JOIN FETCH sid.situacaoDocumento sd ");
        }

        jpql.append(" WHERE d.tipoDocumento = :tipo ");
        if (Objects.nonNull(id)) {
            jpql.append(" AND dc1.id = :id ");
        } else {
            jpql.append(" AND dc1.cpfCnpj = :cpfCnpj ");
            jpql.append(" AND dc1.tipoPessoa = :tipoPessoa ");

        }

        jpql.append(" ORDER BY d.dataHoraCaptura DESC ");

        TypedQuery<Documento> queryDocumento = this.entityManager.createQuery(jpql.toString(), Documento.class);

        if (Objects.nonNull(id)) {
            queryDocumento.setParameter("id", id);
        } else {
            queryDocumento.setParameter("cpfCnpj", cpfCnpj);
            queryDocumento.setParameter("tipoPessoa", tipoPessoaEnum);
        }

        queryDocumento.setParameter("tipo", tipoDocumento);

        try {
            // Retorna o Documento.
            return queryDocumento.setMaxResults(1).getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    private List<Documento> localizaDocumentosValidosCliente(Long id, Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, TipoDocumento tipoDocumento, boolean vinculacaoCanal, boolean vinculacaoTipoDocumental, boolean vinculacaoAtributos, boolean vinculacaoInstancias, boolean vinculacaoDossiesCliente) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT d FROM Documento d ");
        jpql.append(" LEFT JOIN FETCH d.dossiesCliente dc1 ");
        if (vinculacaoCanal) {
            jpql.append(" LEFT JOIN FETCH d.canalCaptura cc ");
        }
        if (vinculacaoTipoDocumental) {
            jpql.append(" LEFT JOIN FETCH d.tipoDocumento td1 ");
            jpql.append(" LEFT JOIN FETCH td1.funcoesDocumentais fd ");
            jpql.append(" LEFT JOIN FETCH td1.atributosExtracao ae ");
        }
        if (vinculacaoAtributos) {
            jpql.append(" LEFT JOIN FETCH d.atributosDocumento ad ");
        }

        if (vinculacaoInstancias) {
            jpql.append(" LEFT JOIN FETCH d.instanciasDocumento id ");
            jpql.append(" LEFT JOIN FETCH id.situacoesInstanciaDocumento sid ");
            jpql.append(" LEFT JOIN FETCH sid.situacaoDocumento sd ");
        }

        if (vinculacaoDossiesCliente) {
            jpql.append(" LEFT JOIN FETCH d.dossiesCliente dc1 ");
        }

        jpql.append(" WHERE (d.dataHoraValidade IS NULL OR d.dataHoraValidade > :momento) ");
        jpql.append(" AND d.tipoDocumento = :tipo ");

        if (Objects.nonNull(id)) {
            jpql.append(" AND dc1.id = :id ");
        } else {
            jpql.append(" AND dc1.cpfCnpj = :cpfCnpj ");
            jpql.append(" AND dc1.tipoPessoa = :tipoPessoa ");
        }

        jpql.append(" ORDER BY d.dataHoraCaptura DESC ");

        TypedQuery<Documento> queryDocumento = this.entityManager.createQuery(jpql.toString(), Documento.class);

        if (Objects.nonNull(id)) {
            queryDocumento.setParameter("id", id);
        } else {
            queryDocumento.setParameter("cpfCnpj", cpfCnpj);
            queryDocumento.setParameter("tipoPessoa", tipoPessoaEnum);
        }

        queryDocumento.setParameter("momento", Calendar.getInstance(), TemporalType.TIMESTAMP);
        queryDocumento.setParameter("tipo", tipoDocumento);

        try {
            // Retorna a lista de Documentos.
            return queryDocumento.getResultList();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public void deleteDocumentoDossieCliente(Long idDossieCliente, Long idDocumento) {

        Documento documento = this.getById(idDocumento, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
        if (documento != null) {

            boolean localizado = false;
            for (DossieCliente dossieCliente : documento.getDossiesCliente()) {
                // Verifica se o documento tem relação com o Dossiê de Cliente informado.
                if (idDossieCliente.equals(dossieCliente.getId())) {
                    localizado = true;
                    // Caso existam instancias vinculadas, o documento deve ser invalidado e não excluido
                    if ((documento.getInstanciasDocumento() != null)
                        && (!documento.getInstanciasDocumento().isEmpty())) {
                        if ((documento.getDataHoraValidade() != null)
                            && (Calendar.getInstance().after(documento.getDataHoraValidade()))) {
                            throw new SimtrRequisicaoException("O documento solicitado já não tem validade para novos negócios.");
                        }
                        documento.setDataHoraValidade(Calendar.getInstance());
                        this.update(documento);
                        return;
                    }

                    if (Objects.nonNull(documento.getCodigoGED())) {
                        this.siecmServico.alteraSituacaoTemporalidadeDocumentoSIECM(documento.getCodigoGED(), TemporalidadeDocumentoEnum.REMOVER, ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
                    }
                    dossieCliente.removeDocumentos(documento);
                    this.delete(documento);
                }
            }

            if (!localizado) {
                Object[] params = new Object[] {
                    idDocumento,
                    idDossieCliente
                };
                throw new SimtrRecursoDesconhecidoException(MessageFormat.format("Documento não localizado para exclusão sob identificador {0} vinculado ao dossie de cliente {1}", params));
            }
        }
    }

    /**
     * Define a data de validade para o documento informado. Necessario que o documento esteja:
     * <ul>
     * <li>Tipo de Documento com os atributos de extração</li>
     * <li>Atributos do documento preenchidos</li>
     * </ul>
     *
     * @param documento Documento a ser definida a data de validade
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRPAEOPE,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public void defineDataValidade(Documento documento) {
        if ((documento.getTipoDocumento() == null) || (documento.getAtributosDocumento() == null)) {
            throw new IllegalArgumentException(MessageFormat.format("Documento ausente de tipo de documento e de atributos. Não é possivel calcular a data de validade do mesmo. ID = {0}", documento.getId()));
        }

        if (documento.getAtributosDocumento().isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            if (documento.getTipoDocumento().getPrazoValidade() != null) {
                LocalDateTime ldt = LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
                LocalDateTime validade = ldt.plusDays(documento.getTipoDocumento().getPrazoValidade());
                calendar.setTime(Date.from(validade.toInstant(ZoneId.systemDefault().getRules().getOffset(ldt))));
                documento.setDataHoraValidade(calendar);
            }
            return;
        }

        AtributoExtracao atributoValidade = documento.getTipoDocumento().getAtributosExtracao().stream()
                                                     .filter(a -> a.getUtilizadoCalculoValidade() != null)
                                                     .filter(a -> a.getUtilizadoCalculoValidade())
                                                     .findFirst().orElse(null);
        if (atributoValidade != null) {
            documento.getAtributosDocumento().stream()
                     .filter(a -> (a.getDescricao().equals(atributoValidade.getNomeAtributoDocumento())))
                     .filter(a -> (a.getConteudo() != null))
                     .findFirst().ifPresent(atributoDocumento -> {
                         Calendar calendar = null;
                         try {
                             calendar = this.calendarUtil.toCalendar(atributoDocumento.getConteudo(), true);
                             if (!documento.getTipoDocumento().getValidadeAutoContida()) {
                                 LocalDateTime ldt = LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
                                 LocalDateTime validade = ldt.plusDays(documento.getTipoDocumento().getPrazoValidade());
                                 calendar.setTime(Date.from(validade.toInstant(ZoneId.systemDefault().getRules().getOffset(ldt))));
                             }
                         } catch (ParseException | EJBException e) {
                             String mensagem = MessageFormat.format("Falha ao definir o valor de validade do documento. Conteudo do atributo: {0} | Identificador do documento: {1}", atributoDocumento.getConteudo(), documento.getId());
                             LOGGER.log(Level.WARNING, mensagem, e.getCause());
                         }
                         documento.setDataHoraValidade(calendar);
                     });
        }
    }

    /**
     * Realiza a validação dos atributos do documento verificando se os atributos informados para o documento estão definidos no tipo de documento associado
     *
     * @param documento Documento a ser validado
     * @throws SimtrRequisicaoException Lançada em caso de algum atributo informado para o documento não estar definido para o tipo de documento associado
     * @throws SimtrEstadoImpeditivoException Lançada caso o tipo de documento não esteja definido o documento encaminhado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public void validaAtributosDocumento(Documento documento) {
        // Verifica se o documento esta carregado corretamente para validação.
        if (Objects.isNull(documento.getTipoDocumento())) {
            throw new SimtrEstadoImpeditivoException("Documento ausente de tipo de documento. Não é possivel validar o documento.");
        }

        if ((Objects.nonNull(documento.getAtributosDocumento())) && (!documento.getAtributosDocumento().isEmpty())) {

            // Transforma todos os atributos efetivamente do documento em um Map para facilitar a busca
            final List<String> listaAtributosDocumento = documento.getAtributosDocumento().stream()
                                                                  .map(ad -> ad.getDescricao())
                                                                  .collect(Collectors.toList());

            // Obtem codigo de tipologia e nome para utilizar nas mensagens de retorno
            String codigoTipologia = documento.getTipoDocumento().getCodigoTipologia();
            String nomeTipoDocumento = documento.getTipoDocumento().getNome(); 
            
            // Verifica se todos os atributos encaminhados para o documento são esperados pela tipologia
            List<String> atributosNaoEsperados = this.validaAtributosPertinentesDocumento(documento.getTipoDocumento(), listaAtributosDocumento);
            if (!atributosNaoEsperados.isEmpty()) {
                String mensagem = MessageFormat.format("DS.vAD.001 - Atributos {0} do documento [{1}] não estão definidos para este o tipo de documento.", Arrays.toString(atributosNaoEsperados.toArray()), codigoTipologia, nomeTipoDocumento);
                throw new SimtrRequisicaoException(mensagem);
            }

            // Verifica se todos os atributos obrigatórios definidos para o tipo de documento foram encaminhados com os atributos do documento
            List<String> atributosObrigatorioNaoLocalizados = this.validaAtributosObrigatoriosDocumento(documento.getTipoDocumento(), listaAtributosDocumento);
            if (!atributosObrigatorioNaoLocalizados.isEmpty()) {
                String mensagem = MessageFormat.format("DS.vAD.002 - Atributos {0} obrigatorios para o tipo de documento [{1} - {2}] não estão definido.", Arrays.toString(atributosObrigatorioNaoLocalizados.toArray()), codigoTipologia, nomeTipoDocumento);
                throw new SimtrRequisicaoException(mensagem);
            }
        }
    }

    /**
     * Valida se a lista de atributos encaminhados esta definida na lista de atributos esperados do tipo de documento informado com base no atributo "TipoDocumento.nomeAtributoDocumento"
     * 
     * @param tipoDocumento Tipo de documento utilizado como base na verificação
     * @param atributosEncaminhdos Lista contendo o nomes dos atributos a serem verificados
     * @return Lista de atributos não reconhecidos no documento. Lista vazia se todos os atributos foram identificados
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public List<String> validaAtributosPertinentesDocumento(TipoDocumento tipoDocumento, List<String> atributosEncaminhdos) {
        // Transforma todos os atributos definido no tipo de documento em um Map para facilitar a busca
        final Map<String, AtributoExtracao> mapaAtributosTipoDocumento = tipoDocumento.getAtributosExtracao().stream()
                                                                                      .collect(Collectors.toMap(ae -> ae.getNomeAtributoDocumento(), ae -> ae));
        // Inicializa a lista a ser devolvida indicando os atributos não reconhecidos
        List<String> listaAtributosNaoReconhecidos = new ArrayList<>();

        // Percorre os campos definidos para o tipo de documento verificando se estão definidos para o tipo de documento
        atributosEncaminhdos.forEach(atributo -> {
            // Identifica o atributo definido no tipo do documento dentre os informados no documento;
            AtributoExtracao atributoExtracao = mapaAtributosTipoDocumento.get(atributo);
            if (atributoExtracao == null) {
                listaAtributosNaoReconhecidos.add(atributo);
            }
        });

        return listaAtributosNaoReconhecidos;
    }

    /**
     * Valida se todos os atributos obrigatório definidos para o tipo de documento foram encaminhados no documento informado com base no atributo "TipoDocumento.nomeAtributoDocumento"
     * 
     * @param tipoDocumento Tipo de documento utilizado como base na verificação
     * @param atributosEncaminhdos Lista contendo o nomes dos atributos a serem verificados
     * @return Lista de atributos obrigatorios não reconhecidos na lista encaminhada. Lista vazia se todos os atributos obrigatórios foram identificados.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public List<String> validaAtributosObrigatoriosDocumento(TipoDocumento tipoDocumento, List<String> atributosEncaminhdos) {
        // Identifica todos os atributos definidos como obrigatorios para o tipo de documento
        final Map<String, AtributoExtracao> mapaAtributosObrigatorios = tipoDocumento.getAtributosExtracao().stream()
                                                                                     .filter(ae -> ae.getObrigatorio())
                                                                                     .collect(Collectors.toMap(ae -> ae.getNomeAtributoDocumento(), ae -> ae));

        // Inicializa a lista a ser devolvida indicando os atributos não reconhecidos
        List<String> listaAtributosNaoReconhecidos = new ArrayList<>();

        // Percorre os campos definidos para o tipo de documento verificando se estão definidos para o tipo de documento
        mapaAtributosObrigatorios.entrySet().forEach(registro -> {
            AtributoExtracao atributoExtracao = registro.getValue();

            // Verifica se o atributo obrigatorio esta presente no documento e adiciona na lista caso não seja encontrado
            if (!atributosEncaminhdos.contains(atributoExtracao.getNomeAtributoDocumento())) {
                listaAtributosNaoReconhecidos.add(atributoExtracao.getNomeAtributoDocumento());
            }
        });

        return listaAtributosNaoReconhecidos;
    }

    /**
     * Lista todos os documentos em situação "VALIDO" vinculado ao CPF/CNPJ informado que possuem marcação com o dossiê digital
     *
     * @param cpfCnpj CPF/CNPJ do cliente que deseja relacionar os documentos
     * @param tipoPessoaEnum Indicador se pessoa fisica ou juridica
     * @return Mapa de documentos localizados vinculados ao CPF/CNPJ informado em situação valida e com os links de acesso junto ao SIECM
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public Map<Documento, String> listDocumentosDefinitivosDossieDigital(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT d FROM Documento d ");
        jpql.append(" LEFT JOIN FETCH d.tipoDocumento td2 ");
        jpql.append(" LEFT JOIN d.dossiesCliente dc ");
        jpql.append(" WHERE dc.cpfCnpj = :cpfCnpj ");
        jpql.append(" AND dc.tipoPessoa = :tipoPessoa ");
        jpql.append(" AND d.dossieDigital = TRUE ");
        jpql.append(" AND ((d.dataHoraValidade IS NULL) OR (d.dataHoraValidade >= :dataValidade)) ");
        jpql.append(" AND d.situacaoTemporalidade = :situacaoTemporalidade ");

        TypedQuery<Documento> queryDocumento = this.entityManager.createQuery(jpql.toString(), Documento.class);
        queryDocumento.setParameter("cpfCnpj", cpfCnpj);
        queryDocumento.setParameter("tipoPessoa", tipoPessoaEnum);
        queryDocumento.setParameter("dataValidade", Calendar.getInstance());
        queryDocumento.setParameter("situacaoTemporalidade", TemporalidadeDocumentoEnum.VALIDO);

        List<Documento> documentosCliente = queryDocumento.getResultList();
        Map<Documento, String> documentosRetorno = new HashMap<>();

        // Percorre todos os documentos retonados e realiza uma busca no SIECM atualizando a situação dos documentos conforme identificado no SIECM
        // Isso torna-se necessario pois o Enterprise Records pode realizar expurgo de documentos e não comunica o SIMTR sobre a ação
        documentosCliente.forEach(documentoCliente -> {
            // Verifica se o documento esta presente localizado no SIECM
            RetornoPesquisaDTO searchDocument = this.gedService.searchDocument(documentoCliente.getCodigoGED(), ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL);
            if (Objects.nonNull(searchDocument)) {
                // Caso o documento não seja encontrado, atualiza o mesmo com a situação de VENCIDO
                if (searchDocument.getQuantidade().equals(0)) {
                    documentoCliente.setDataHoraValidade(Calendar.getInstance());
                    documentoCliente.setSituacaoTemporalidade(TemporalidadeDocumentoEnum.VENCIDO);
                } else if (searchDocument.getQuantidade() > 0) {
                    // Caso o documento seja encontrado, atualiza o mesmo com a situação localizada no SIECM.
                    // Se o mesmo for VALIDO inclui na lista de documentos para retorno
                    DadosDocumentoLocalizadoDTO documentoSIECM = searchDocument.getDadosDocumentoLocalizados().get(0);
                    String linkDocumentoSIECM = documentoSIECM.getLink();
                    if (TemporalidadeDocumentoEnum.VALIDO.equals(documentoCliente.getSituacaoTemporalidade())) {
                        documentosRetorno.put(documentoCliente, linkDocumentoSIECM);
                    }
                }
            }
        });
        return documentosRetorno;
    }

    /**
     * Utilizado para realizar a captura de uma lista de documentos baseado no identificador único do SIMTR.
     *
     * @param ids Lista de identificadores únicos dos documentos no SIMTR utilizados como parametro da localização
     * @param vinculacaoConteudos Indicador se os conteudos do documento devem ser carregados
     * @param vinculacaoDossiesCliente Indicador se a vinculação relativas aos dossiês de cliente associados ao documento deve ser carregada
     * @param vinculacaoCanal Indicador se a vinculação relativas ao canal de captura do documento deve ser carregada
     * @param vinculacaoTipoDocumento Indicador se a vinculação relativas ao tipo do documento e suas funções exercidas deve ser carregada
     * @param vinculacaoAtributos Indicador se a vinculação relativas aos atributos do documento deve ser carregada
     * @param vinculacaoInstancias Indicador se as vinculações relativas as instancias do documento devem ser carregadas
     * @return Documento carregado conforme parametros indicados ou null caso o documento não seja localizado com o identificador informado
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRPAEOPE,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    public List<Documento> listById(final List<Long> ids, boolean vinculacaoConteudos, boolean vinculacaoDossiesCliente, boolean vinculacaoCanal, boolean vinculacaoTipoDocumento, boolean vinculacaoAtributos, boolean vinculacaoInstancias) {
        if (ids != null && ids.isEmpty()) {
            throw new IllegalArgumentException("DS.lBI.001 - A lista de dentificadores não pode ser nula ou vazia");
        }

        String query = this.getQueryDocumento(vinculacaoConteudos, vinculacaoDossiesCliente, vinculacaoCanal, vinculacaoTipoDocumento, vinculacaoAtributos, vinculacaoInstancias);
        StringBuilder jpql = new StringBuilder(query);
        jpql.append(" WHERE d.id IN (:id)");

        TypedQuery<Documento> queryDocumento = this.entityManager.createQuery(jpql.toString(), Documento.class);
        queryDocumento.setParameter("id", ids);
        List<Documento> documentosLocalizados = queryDocumento.getResultList();

        List<Long> documentosAusentesGED = new ArrayList<>();
        for (Documento documento : documentosLocalizados) {
            // Caso a solicitação tenha sido feita para capturar os conteudos dos documentos e este documento possua referência de armazenamento no GED
            if (vinculacaoConteudos && Objects.nonNull(documento.getCodigoGED())) {
                documento.getConteudos().clear();
                RetornoPesquisaDTO retornoGED = this.gedService.searchDocument(documento.getCodigoGED(), ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL, keycloakUtil.getIpFromToken());

                // Verifica se o documento foi localizado junto ao SIECM
                if (retornoGED == null || retornoGED.getQuantidade() == 0) {
                    if (retornoGED == null) {
                        throw new SiecmException("Serviço GED indisponível", Boolean.FALSE);
                    } else {
                        LOGGER.log(Level.FINE, MessageFormat.format("Documento não localizado no SIECM sob ID: {0}", documento.getCodigoGED()));
                        documentosAusentesGED.add(documento.getId());
                    }
                }

                int ordem = 1;
                for (DadosDocumentoLocalizadoDTO dadosDocumento : retornoGED.getDadosDocumentoLocalizados()) {
                    String link = dadosDocumento.getLink();
                    byte[] bytes = UtilWS.obterBytes(link);
                    String conteudoBase64 = Base64.getEncoder().encodeToString(bytes);
                    Conteudo c = new Conteudo(conteudoBase64, ordem++);
                    documento.addConteudos(c);
                }
            }
        }

        if (!documentosAusentesGED.isEmpty()) {
            String mensagem = MessageFormat.format("Conteudo do documento não localizado no SIECM. IDs: {0}", documentosAusentesGED.toArray());
            throw new SiecmException(mensagem, Boolean.FALSE);
        }
        return documentosLocalizados;
    }

    /**
     * Retorna o Documento Dados Declarado mais recente do Dossie De Cliente Identificado
     *
     * @param id Identificador Unico do Dossie do Cliente detentor do Documento de Dados Declarados.
     * @return Documento Dados Declarado mais recente unico do dossie do cliente
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public Documento getDadosDeclarados(Long id) {
        DossieCliente dossieCliente = this.dossieClienteServico.getById(id);
        
        String tipologiaDadosDeclarados = ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_PF;
        if(TipoPessoaEnum.J.equals(dossieCliente.getTipoPessoa())) {
            tipologiaDadosDeclarados = ConstantesUtil.TIPOLOGIA_DADOS_DECLARADOS_PJ;
        }

        TipoDocumento tipoDocumento = tipoDocumentoServico.getByTipologia(tipologiaDadosDeclarados);
        return this.localizaDocumentoClienteMaisRecenteByIdDossie(id, tipoDocumento, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
    }

    /**
     * Invalida para reuso em novos negócios os documentos pessoais do cliente de mesma tipologia
     *
     * @param idDossieCliente Identificador unico do dossiê de cliente
     * @param cpfCnpj CPF/CNPJ utilizado na identificação do dossiê de cliente
     * @param tipoPessoaEnum Identificador do tipo de pessoa, se fisica ou juridica
     * @param tipoDocumento Tipologia indicada para localização de documentos associados ao dossiê de cliente para invalidação
     * @param invalidarMaisRecente Indicar true se o documento mais recente localizado deverá ser invalidado, ou false caso deva se manter ativo
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public void invalidarDocumentosAtivosCliente(Long idDossieCliente, Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, TipoDocumento tipoDocumento, boolean invalidarMaisRecente) {
        
        // Captura o canal de comunicação baseado no client ID para usar a sigla caso a matricula seja superior a 20 caracteres
        Canal canal = this.canalServico.getByClienteSSO();
        
        // Caso o tipo de documento não permita armazenamento de múltiplas instancias, invalida aquelas que estejam ativas presentes em dossiês não finalizados.
        if (!tipoDocumento.getPermiteMultiplosAtivos()) {
            Calendar dataHoraSubstituicao = Calendar.getInstance();
            Integer unidadeUsuario = this.keycloakService.getLotacaoAdministrativa();
            SituacaoDocumento situacaoSubstituido = this.situacaoDocumentoServico.getBySituacaoDocumentoEnum(SituacaoDocumentoEnum.SUBSTITUIDO);

            // Localiza documento mais recente afim de invalidar o mesmo para novos negócios.
            List<Documento> documentosAtivos = this.localizaDocumentosValidosCliente(idDossieCliente, cpfCnpj, tipoPessoaEnum, tipoDocumento, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            if(!invalidarMaisRecente){
                Documento documentoMaisRecente =  documentosAtivos.stream()
                        .max(Comparator.comparing(Documento::getDataHoraCaptura)).get();
                
                documentosAtivos.remove(documentoMaisRecente);
            }
            for (Documento documentoLocalizado : documentosAtivos) {
                documentoLocalizado.setDataHoraValidade(dataHoraSubstituicao);
                documentoLocalizado.setSituacaoTemporalidade(TemporalidadeDocumentoEnum.VENCIDO);

                // Captura a lista de instancias
                List<InstanciaDocumento> instanciasSituacaoNaoFinal = documentoLocalizado.getInstanciasDocumento().stream()
                                                                                         .filter(id -> !id.getDossieProduto().getSituacoesDossie().stream()
                                                                                                          .max(Comparator.comparing(SituacaoDossie::getId))
                                                                                                          .get().getTipoSituacaoDossie().getTipoFinal())
                                                                                         .filter(id -> !id.getSituacoesInstanciaDocumento().stream()
                                                                                                          .max(Comparator.comparing(SituacaoInstanciaDocumento::getId))
                                                                                                          .get().getSituacaoDocumento().getSituacaoFinal())
                                                                                         .collect(Collectors.toList());

                // Inclui uma nova situação de substituição para as instâncias identificadas
                instanciasSituacaoNaoFinal.forEach(instancia -> {
                    SituacaoInstanciaDocumento novaSituacaoSubstituicao = new SituacaoInstanciaDocumento();
                    novaSituacaoSubstituicao.setDataHoraInclusao(dataHoraSubstituicao);
                    novaSituacaoSubstituicao.setInstanciaDocumento(instancia);
                    String responsavel = this.keycloakService.getMatricula().length() < 20 ? this.keycloakService.getMatricula() : canal.getSigla();
                    novaSituacaoSubstituicao.setResponsavel(responsavel);
                    novaSituacaoSubstituicao.setSituacaoDocumento(situacaoSubstituido);
                    novaSituacaoSubstituicao.setUnidade(unidadeUsuario);

                    instancia.addSituacoesInstanciaDocumento(novaSituacaoSubstituicao);
                });
                this.update(documentoLocalizado);
            }
        }
    }

    /**
     * Utilização para atender SONAR
     *
     * @param binario
     * @return
     */
    @PermitAll
    private int endsWithSymbolEqual(String binario) {
        return binario.endsWith("=") ? 1 : 0;
    }

    private byte[] gerarMinutaDocumento(Documento documento, AtributoComplementarRelatorioVO... atributosComplementares) throws ParseException {
    	
        // Transforma os atributos encaminhados no documento em um mapa para facilitar a busca
        Map<String, String> atributos = documento.getAtributosDocumento().stream()
        		.filter(ad -> Objects.isNull(ad.getDeObjeto()) || ad.getDeObjeto().isEmpty())
        		.collect(Collectors.toMap(
        		ad -> ad.getDescricao(), ad -> Objects.nonNull(ad.getConteudo()) ?  ad.getConteudo() : 
            ad.getOpcoesSelecionadas().stream().map(Object::toString).collect(Collectors.joining(","))
            ));

        // Transforma os atributos esperados ao tipo documental em um mapa para facilitar a busca
        Map<String, TipoAtributoEnum> mapaTiposAtributo = documento.getTipoDocumento().getAtributosExtracao().stream()
                                                                   .filter(a -> a.getTipoAtributoGeralEnum() != null)
                                                                   .collect(Collectors.toMap(a -> a.getNomeAtributoDocumento(), a -> a.getTipoAtributoGeralEnum()));

        // Monta o mapa de objetos ue subsdiara a criação do datasource para a geração da minuta.
        // Valida os atributos enviados se estão definidos para o tipo documental.
        Map<String, Object> dados = new HashMap<>();
        atributos.entrySet().forEach(a -> {
            TipoAtributoEnum tipoAtributoEnum = mapaTiposAtributo.get(a.getKey());
            try {
                Object valor = this.converteValorAtributo(tipoAtributoEnum, a.getValue());
                dados.put(a.getKey(), valor);
            } catch (ParseException ex) {
                String mensagem = MessageFormat.format("DS.gMD.001 - Atributo com valor inválido. Atributo: {0} | Tipo Dado: {1} | Valor Atributo: {2}", a.getKey(), tipoAtributoEnum.name(), a.getValue());
                throw new SimtrRequisicaoException(mensagem, ex);
            } catch (NullPointerException npe) {
                String mensagem = MessageFormat.format("DS.gMD.002 - Atributo não possui tipo definido para geração da minuta. Atributo: {0} | Valor Atributo: {1}", a.getKey(), a.getValue());
                throw new SimtrRequisicaoException(mensagem, npe);
            }
        });

        for (AtributoComplementarRelatorioVO atributo : atributosComplementares) {
            try {
                Object valor = this.converteValorAtributo(atributo.getTipoAtributoEnum(), atributo.getValor());
                dados.put(atributo.getChave(), valor);
            } catch (ParseException ex) {
                String mensagem = MessageFormat.format("DS.gMD.001 - Atributo com valor inválido. Atributo: {0} | Tipo Dado: {1} | Valor Atributo: {2}", atributo.getChave(), atributo.getTipoAtributoEnum()
                                                                                                                                                                                      .name(), atributo.getValor());
                throw new SimtrRequisicaoException(mensagem, ex);
            } catch (NullPointerException npe) {
                String mensagem = MessageFormat.format("DS.gMD.002 - Atributo não possui tipo definido para geração da minuta. Atributo: {0} | Valor Atributo: {1}", atributo.getChave(), atributo.getValor());
                throw new SimtrRequisicaoException(mensagem, npe);
            }
        }

        // Efetua a emissão da minuta do documento solicitado.
        try {
            String json = UtilJson.converterParaJson(dados);

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("LOGO_CAIXA", ConstantesUtil.RELATORIO_CAMINHO_IMAGENS.concat("caixa.png"));            
    		parametros.put("SUBREPORT_DIR", CAMINHO_RELATORIO);

            String tipologiaDadosSIISO = ConstantesUtil.TIPOLOGIA_DADOS_SIISO;
            String tipologiaDadosAvalicaoTomador = ConstantesUtil.TIPOLOGIA_DADOS_AVALIADOR_TOMADOR;
            
            List<AtributoDocumento> listaAtributoComArray = montarListaAtributoComArray(documento);
            
            if(documento.getTipoDocumento().getCodigoTipologia().equals(tipologiaDadosSIISO) && !listaAtributoComArray.isEmpty()) {
                
            	List<SocioDTO> listaSocio = new ArrayList<>();
            	List<CnaeDTO> listaCnaeSecundaria = new ArrayList<>();
            	List<SucessaoDTO> listaSucessao = new ArrayList<>();
            	
            	// PROCESSAR SOCIOS            	
            	List <String> listaSocioQtd = new ArrayList<>();
            	montarListaStringPorTipoArray(listaAtributoComArray, DocumentoServico.SOCIO, listaSocioQtd);
            	for (String string : listaSocioQtd) {
            		SocioDTO socioDto = new SocioDTO();
            		for (int i = 0; i < listaAtributoComArray.size(); i++) {
            			if(listaAtributoComArray.get(i).getDeObjeto().equals(string)) {
                			montarSocioDTO(socioDto, listaAtributoComArray.get(i).getConteudo(), listaAtributoComArray.get(i).getDescricao());
            			}
            		}
            		listaSocio.add(socioDto);
				}            	 
            	 // PROCESSAR CNAE           	
            	List <String> listaCnaeQtd = new ArrayList<>();
            	montarListaStringPorTipoArray(listaAtributoComArray, DocumentoServico.CNAE, listaCnaeQtd);
            	for (String string : listaCnaeQtd) {
            		CnaeDTO cnaeDto = new CnaeDTO();
            		for (int i = 0; i < listaAtributoComArray.size(); i++) {
            			if(listaAtributoComArray.get(i).getDeObjeto().equals(string)) {
            				montarCnaeDTO(cnaeDto, listaAtributoComArray.get(i).getConteudo(), listaAtributoComArray.get(i).getDescricao());
            			}
            		}
            		listaCnaeSecundaria.add(cnaeDto);
				}
            	 
            	// PROCESSAR SUCESSAO          	
            	List <String> listaSucessaoQtd = new ArrayList<>();
            	montarListaStringPorTipoArray(listaAtributoComArray, DocumentoServico.SUCESSAO, listaSucessaoQtd);
            	for (String string : listaSucessaoQtd) {
            		SucessaoDTO sucessaoDto = new SucessaoDTO(); 
            		for (int i = 0; i < listaAtributoComArray.size(); i++) {
            			if(listaAtributoComArray.get(i).getDeObjeto().equals(string)) {
            				montarSucessaoDTO(sucessaoDto, listaAtributoComArray.get(i).getConteudo(), listaAtributoComArray.get(i).getDescricao());
            			}
            		}
            		listaSucessao.add(sucessaoDto);
				}

                parametros.put("LISTA_SOCIO", listaSocio);
                parametros.put("LISTA_CNAE_SECUNDARIO", listaCnaeSecundaria);
                parametros.put("LISTA_SUCESSAO", listaSucessao);
            }
            
            if(documento.getTipoDocumento().getCodigoTipologia().equals(tipologiaDadosAvalicaoTomador) && !listaAtributoComArray.isEmpty()) {
            	
            	List<ProdutoAvalidadorTomadorDTO> listaProduto = new ArrayList<>();
            	List<MotivoReprovacaoDTO> listaMotivos = new ArrayList<>();
            	
            	// PROCESSAR produtos          	
            	List <String> listaprodutoQtd = new ArrayList<>();
            	montarListaStringPorTipoArray(listaAtributoComArray, DocumentoServico.PRODUTO, listaprodutoQtd);
            	for (String string : listaprodutoQtd) {
            		ProdutoAvalidadorTomadorDTO produtoAvalidadorTomadorDto = new ProdutoAvalidadorTomadorDTO(); 
            		for (int i = 0; i < listaAtributoComArray.size(); i++) {
            			if(listaAtributoComArray.get(i).getDeObjeto().equals(string)) {
            				montarProdutoDTO(produtoAvalidadorTomadorDto, listaAtributoComArray.get(i).getConteudo(), listaAtributoComArray.get(i).getDescricao());
            			}
            		}
            		listaProduto.add(produtoAvalidadorTomadorDto);
				}
                
            	// PROCESSAR Motivos Reprovação
            	List <String> listaMotivoReprovacaoQtd = new ArrayList<>();
            	montarListaStringPorTipoArray(listaAtributoComArray, DocumentoServico.MOTIVO_REPROVACAO, listaMotivoReprovacaoQtd);
            	for (String string : listaMotivoReprovacaoQtd) {
            		MotivoReprovacaoDTO motivoReprovacaoDto = new MotivoReprovacaoDTO(); 
            		for (int i = 0; i < listaAtributoComArray.size(); i++) {
            			if(listaAtributoComArray.get(i).getDeObjeto().equals(string)) {
            				motivoReprovacaoDto.setMotivoReprovacao(listaAtributoComArray.get(i).getDescricao());
            			}
            		}
            		listaMotivos.add(motivoReprovacaoDto);
				}
            	
            	
                
                parametros.put("LISTA_PRODUTO", listaProduto);
                parametros.put("LISTA_MOTIVO_REPROVACAO", listaMotivos);
            }

            String reportName = "negocio/".concat(documento.getTipoDocumento().getNomeArquivoMinuta());

            return this.relatorioServico.gerarRelatorioPDFJsonDataSource(reportName, json, parametros);

        } catch (Exception e) {
            String mensagem = MessageFormat.format("DS.gMD.002 - Falha ao gerar minuta de documento. Documento Solicitado = {0}", documento.getTipoDocumento().getNome());
            throw new SimtrRequisicaoException(mensagem, e);
        }
    }

	private void montarListaStringPorTipoArray(List<AtributoDocumento> listaAtributoComArray, final String tipo,
			List<String> listaSocioQtd) {
		for (int i = 0; i < listaAtributoComArray.size(); i++) {
			if(listaAtributoComArray.get(i).getDeObjeto().contains(tipo) && !listaSocioQtd.contains(listaAtributoComArray.get(i).getDeObjeto())) {
				listaSocioQtd.add(listaAtributoComArray.get(i).getDeObjeto());
			}
			
		}
	}
    


    private void montarSocioDTO(SocioDTO socioDto, Object valor, String propriedade) {
        if(propriedade.equals(NOME_SOCIO)) {
             socioDto.setNomeSocio(Objects.nonNull(valor) && !valor.equals("") ? valor.toString() : "");
         }
         
         if(propriedade.equals(CPF_CNPJ)) {
             socioDto.setCpfCnpj(Objects.nonNull(valor) && !valor.equals("") ? valor.toString() : "");
         }
                       
         if(propriedade.equals(CPF_REPRESENTANTE)) {
             socioDto.setCpfRepresentante(Objects.nonNull(valor) && !valor.equals("") ? valor.toString() : "");
         }
         
         if(propriedade.equals(CODIGO_QUALIFICACAO)) {
             socioDto.setCodigoQualificacao(Objects.nonNull(valor) && !valor.equals("") ? valor.toString() : "");
         }
         
         if(propriedade.equals(DESCRICAO_QUALIFICACAO)) {
             socioDto.setDescricaoQualificacao(Objects.nonNull(valor) && !valor.equals("") ? valor.toString() : "");
         }
         
         if(propriedade.equals(PC_CAPITAL_SOCIAL)) {
             socioDto.setParticipacaoCapitalSocial(Objects.nonNull(valor) && !valor.equals("") ? valor.toString() : "");
         }
         
         if(propriedade.equals(NOME_REPRESENTANTE)) {
             socioDto.setNomeRepresentante(Objects.nonNull(valor) && !valor.equals("") ? valor.toString() : "");
         }
         
         if(propriedade.equals(VINCULO_PENDENTE)) {
             socioDto.setVinculoPendente(Objects.nonNull(valor) && !valor.equals("") ? valor.toString() : "");
         }
         
         if(propriedade.equals(DATA_INICIO_SOCIO) && Objects.nonNull(valor) && !valor.equals("") && valor instanceof Calendar) {
             Calendar dataInicio = (Calendar) valor; 
             socioDto.setDataInicio(dataInicio);
         }
    }
    
    private void montarCnaeDTO(CnaeDTO cnae, Object valor, String propriedade) {
        if(propriedade.equals(CNAE)) {
            cnae.setCnae(Objects.nonNull(valor) && !valor.equals("") ? valor.toString() : "");
         }
         
         if(propriedade.equals(DESCRICAO_CNAE)) {
             cnae.setDescricaoCnae(Objects.nonNull(valor) && !valor.equals("") ? valor.toString() : "");
         }
    }
    
    private void montarSucessaoDTO(SucessaoDTO sucessaoDTO, Object valor, String propriedade) {
        if(propriedade.equals(TIPO_VINCULO)) {
            sucessaoDTO.setTipoVinculo(valor.toString());
         }
         
        if(propriedade.equals(DATA_INICIO_SUCESSAO) && Objects.nonNull(valor) && !valor.equals("") && valor instanceof Calendar) {
            Calendar dataInicio = (Calendar) valor;
            sucessaoDTO.setDataInicio(dataInicio);
         }
         
         if(propriedade.equals(CNPJ_VINCULADOR)) {
             sucessaoDTO.setCnpjVinculador(Objects.nonNull(valor) && !valor.equals("") ? valor.toString() : "");
         }
         
         if(propriedade.equals(NOME_VINCULADOR)) {
             sucessaoDTO.setNomeVinculador(Objects.nonNull(valor) && !valor.equals("") ? valor.toString() : "");
         }
         
         if(propriedade.equals(CNPJ_VINCULADO)) {
             sucessaoDTO.setCnpjVinculado(Objects.nonNull(valor) && !valor.equals("") ? valor.toString() : "");
         }
         
         if(propriedade.equals(NOME_VINCULADO)) {
             sucessaoDTO.setNomeVinculado(Objects.nonNull(valor) && !valor.equals("") ? valor.toString() : "");
         }
    }
    
    private void montarProdutoDTO(ProdutoAvalidadorTomadorDTO produtoDTO, Object valor, String propriedade) {
        if(propriedade.equals(ConstantesNegocioDossieClienteManutencao.RATING) && Objects.nonNull(valor) && !valor.equals("")) {
            produtoDTO.setRating(valor.toString());
         }
         
        if(propriedade.equals(ConstantesNegocioDossieClienteManutencao.CODIGO_MODALIDADE) && Objects.nonNull(valor) && !valor.equals("")) {
            produtoDTO.setCodigoModalidade(valor.toString());
        }
        
        if(propriedade.equals(ConstantesNegocioDossieClienteManutencao.CODIGO_PRODUTO) && Objects.nonNull(valor) && !valor.equals("")) {
            produtoDTO.setCodigoProduto(valor.toString());
        }
        
        if(propriedade.equals(ConstantesNegocioDossieClienteManutencao.RESULTADO_AVALIACAO) && Objects.nonNull(valor) && !valor.equals("")) {
            produtoDTO.setResultadoAvaliacao(valor.toString());
        }
        
        if(propriedade.equals(ConstantesNegocioDossieClienteManutencao.VALOR_DISPONIVEL) && Objects.nonNull(valor) && !valor.equals("")) {
            produtoDTO.setValorDisponivel(valor.toString());
        }
        
        if(propriedade.equals(ConstantesNegocioDossieClienteManutencao.VALOR_CALCULADO_TOTAL) && Objects.nonNull(valor) && !valor.equals("")) {
            produtoDTO.setValorCalculadoTotal(valor.toString());
        }
        
        if(propriedade.equals(ConstantesNegocioDossieClienteManutencao.PRAZO_MESES) && Objects.nonNull(valor) && !valor.equals("")) {
            produtoDTO.setPrazoMeses(valor.toString());
        }
    }
    
    /**
     * Monta uma lista apenas com atributos que contem o atributo array
     **/
	private List<AtributoDocumento> montarListaAtributoComArray(Documento documento) {
		List<AtributoDocumento> listaAtributoComArray = new ArrayList<>();
		for (AtributoDocumento atributo : documento.getAtributosDocumento()) {
			if(Objects.nonNull(atributo.getDeObjeto()) && !atributo.getDeObjeto().isEmpty()) {
				listaAtributoComArray.add(atributo);
			}
		}
		return listaAtributoComArray;
	}

    private Object converteValorAtributo(TipoAtributoEnum tipoAtributoEnum, String valor) throws ParseException {
        switch (tipoAtributoEnum) {
            case BOOLEAN:
                return Boolean.valueOf(valor);
            case DATE:
                return valor.isEmpty() ? valor : this.calendarUtil.toCalendar(valor, Boolean.FALSE);
            case DECIMAL:
                return new BigDecimal(valor);
            case LONG:
                return Long.valueOf(valor);
            case STRING:
            default:
                return valor;
        }
    }
    
    /**
     * Define a quantidade de paginas dos documentos recebidos
     *
     * @param binario Documento a ser definido a quanidade de conteudos vinculados
     * @param formatoConteudoEnum Formato definido para o documento
     */
    private int defindeQuantidadePaginasDocumento(String binario, FormatoConteudoEnum formatoConteudoEnum) {
        int qtdeConteudos = 1;
        if (FormatoConteudoEnum.PDF.equals(formatoConteudoEnum)) {
            try {
                byte[] pdfByteArray = Base64.getDecoder().decode(binario);
                PdfReader pdfReader = new PdfReader(pdfByteArray);
                qtdeConteudos = pdfReader.getNumberOfPages();
            } catch (IOException ioex) {
                String mensagem = "DS.rQPPDF.001 - Não foi possível ler a quantidade de páginas do pdf.";
                throw new SimtrRequisicaoException(mensagem, ioex);
            }
        }
        return qtdeConteudos;
    }
}
