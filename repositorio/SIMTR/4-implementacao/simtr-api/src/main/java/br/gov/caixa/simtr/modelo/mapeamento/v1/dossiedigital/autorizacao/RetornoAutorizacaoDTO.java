package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SipesResponseDTO;
import br.gov.caixa.simtr.controle.vo.autorizacao.AutorizacaoDocumentoVO;
import br.gov.caixa.simtr.controle.vo.autorizacao.MensagemOrientacaoVO;
import br.gov.caixa.simtr.modelo.entidade.Autorizacao;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalAutorizacao;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CNPJAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CPFAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalAutorizacao.XML_ROOT_ELEMENT_RETORNO_AUTORIZACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesDossieDigitalAutorizacao.API_MODEL_V1_RETORNO_AUTORIZACAO)
public class RetornoAutorizacaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonInclude(value = Include.NON_EMPTY)
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.AUTORIZACAO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.AUTORIZACAO, required = false, value = "Identificador da autorização quando concedida")
    private Long codigoAutorizacao;

    @XmlElement(name = ConstantesDossieDigitalAutorizacao.AUTORIZADO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.AUTORIZADO, required = true, value = "Indicativo se a operação foi autorizada ou não")
    private boolean autorizado;

    @JsonInclude(value = Include.NON_EMPTY)
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.PROSSEGUIR)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.PROSSEGUIR, required = false, value = "Indicador se a operação deve prosseguir na captura de documentos ausentes ou não.")
    private Boolean indicadorProsseguimento;

    @XmlElement(name = ConstantesDossieDigitalAutorizacao.PRODUTO_LOCALIZADO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.PRODUTO_LOCALIZADO, required = false, value = "Indicativo se o produto foi localizado")
    private boolean produtoLocalizado;

    @XmlJavaTypeAdapter(CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.DATA_HORA)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.DATA_HORA, required = true, value = "Data e hora da autorização no sistema", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraAutorizacao;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CPFAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.CPF_CLIENTE)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.CPF_CLIENTE, required = false, value = "CPF do cliente caso o mesmo seja pessoa fisica. Deve ser informado se o cliente for PF. Zeros a esquerda não devem ser incluidos.", example = "11122233399")
    private Long cpfCliente;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CNPJAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.CNPJ_CLIENTE)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.CNPJ_CLIENTE, required = false, value = "CNPJ do cliente pessoa fisica. Deve ser informado se o cliente for PJ. Zeros a esquerda não devem ser incluidos.", example = "11222333000099")
    private Long cnpjCliente;

    @XmlElement(name = ConstantesDossieDigitalAutorizacao.OPERACAO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.OPERACAO, required = true, value = "Número da operação do produto solicitado.")
    private Integer operacao;

    @XmlElement(name = ConstantesDossieDigitalAutorizacao.MODALIDADE)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.MODALIDADE, required = true, value = "Número da Modalidade do produto solicitado.")
    private Integer modalidade;

    @JsonInclude(Include.NON_EMPTY)
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.PRODUTO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.PRODUTO, required = false, value = "Nome do produto solicitado, quando localizado")
    private String nomeProduto;

    @JsonProperty(value = ConstantesDossieDigitalAutorizacao.DOCUMENTOS_UTILIZADOS)
    @XmlElementWrapper(name = ConstantesDossieDigitalAutorizacao.DOCUMENTOS_UTILIZADOS)
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.DOCUMENTO_UTILIZADO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.DOCUMENTOS_UTILIZADOS, required = true, value = "Lista de documentos utilizados na operação")
    private List<DocumentoUtilizadoDTO> documentosUtilizados;

    @JsonInclude(value = Include.NON_EMPTY)
    @JsonProperty(value = ConstantesDossieDigitalAutorizacao.DOCUMENTOS_PENDENTES)
    @XmlElementWrapper(name = ConstantesDossieDigitalAutorizacao.DOCUMENTOS_PENDENTES)
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.DOCUMENTO_PENDENTE)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.DOCUMENTOS_PENDENTES, required = false, value = "Lista de documentos pendentes em caso de rejeição da operação")
    private List<String> documentosAusentes;

    @JsonInclude(value = Include.NON_EMPTY)
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.OBSERVACAO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.OBSERVACAO, required = false, value = "Descrição da mensagem de erro ou observação definida para ser encaminhada ao solicitante")
    private String observacao;

    @JsonInclude(value = Include.NON_EMPTY)
    @JsonProperty(value = ConstantesDossieDigitalAutorizacao.MENSAGENS_ORIENTACAO)
    @XmlElementWrapper(name = ConstantesDossieDigitalAutorizacao.MENSAGENS_ORIENTACAO)
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.MENSAGEM_ORIENTACAO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.MENSAGENS_ORIENTACAO, required = false, value = "Lista de mensagens de orientação encaminhados conforme resultado da consulta as pesquisas cadastrais")
    private List<MensagemOrientacaoDTO> mensagensOrientacao;

    @JsonInclude(value = Include.NON_EMPTY)
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.RESULTADO_PESQUISA)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.RESULTADO_PESQUISA, required = false, value = "Objeto do SIPES transformado em JSON obtido no ato da pesquisa da operação de autorização")
    private SipesResponseDTO resultadoPesquisa;

    public RetornoAutorizacaoDTO() {
        super();
        this.dataHoraAutorizacao = Calendar.getInstance();
        this.produtoLocalizado = Boolean.FALSE;
        this.indicadorProsseguimento = Boolean.TRUE;
        this.documentosAusentes = new ArrayList<>();
        this.documentosUtilizados = new ArrayList<>();
        this.mensagensOrientacao = new ArrayList<>();
    }

    public RetornoAutorizacaoDTO(Autorizacao autorizacao, List<AutorizacaoDocumentoVO> documentosUtilizados, List<MensagemOrientacaoVO> mensagensOrientacao, SipesResponseDTO resultadoPesquisaSIPES) {
        this();
        if (TipoPessoaEnum.F.equals(autorizacao.getTipoPessoa())) {
            this.cpfCliente = autorizacao.getCpfCnpj();
        } else if (TipoPessoaEnum.J.equals(autorizacao.getTipoPessoa())) {
            this.cnpjCliente = autorizacao.getCpfCnpj();
        }
        this.dataHoraAutorizacao = autorizacao.getDataHoraRegistro();
        this.operacao = autorizacao.getProdutoOperacao();
        this.modalidade = autorizacao.getProdutoModalidade();
        this.nomeProduto = autorizacao.getProdutoNome();
        this.produtoLocalizado = autorizacao.getProdutoNome() != null;
        this.autorizado = autorizacao.getCodigoNSU() != null;
        this.codigoAutorizacao = autorizacao.getCodigoNSU();
        if (documentosUtilizados != null) {
            documentosUtilizados.forEach(documentoUtilizado -> this.documentosUtilizados.add(new DocumentoUtilizadoDTO(documentoUtilizado)));
        }
        if (mensagensOrientacao != null) {
            mensagensOrientacao.forEach(orientacao -> this.mensagensOrientacao.add(new MensagemOrientacaoDTO(orientacao.getSistemaPesquisa().name(), orientacao.getGrupoOcorrencia(), orientacao.getMensagemOrientacao())));
        }
        this.resultadoPesquisa = resultadoPesquisaSIPES;
    }

    public Long getCodigoAutorizacao() {
        return codigoAutorizacao;
    }

    public void setCodigoAutorizacao(Long codigoAutorizacao) {
        this.codigoAutorizacao = codigoAutorizacao;
    }

    public boolean isAutorizado() {
        return autorizado;
    }

    public void setAutorizado(boolean autorizado) {
        this.autorizado = autorizado;
    }

    public Boolean getIndicadorProsseguimento() {
        return indicadorProsseguimento;
    }

    public void setIndicadorProsseguimento(Boolean indicadorProsseguimento) {
        this.indicadorProsseguimento = indicadorProsseguimento;
    }

    public boolean isProdutoLocalizado() {
        return produtoLocalizado;
    }

    public void setProdutoLocalizado(boolean produtoLocalizado) {
        this.produtoLocalizado = produtoLocalizado;
    }

    public Calendar getDataHoraAutorizacao() {
        return dataHoraAutorizacao;
    }

    public void setDataHoraAutorizacao(Calendar dataHoraAutorizacao) {
        this.dataHoraAutorizacao = dataHoraAutorizacao;
    }

    public Long getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(Long cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public Long getCnpjCliente() {
        return cnpjCliente;
    }

    public void setCnpjCliente(Long cnpjCliente) {
        this.cnpjCliente = cnpjCliente;
    }

    public Integer getOperacao() {
        return operacao;
    }

    public void setOperacao(Integer operacao) {
        this.operacao = operacao;
    }

    public Integer getModalidade() {
        return modalidade;
    }

    public void setModalidade(Integer modalidade) {
        this.modalidade = modalidade;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public List<DocumentoUtilizadoDTO> getDocumentosUtilizados() {
        return documentosUtilizados;
    }

    public void setDocumentosUtilizados(List<DocumentoUtilizadoDTO> documentosUtilizados) {
        this.documentosUtilizados = documentosUtilizados;
    }

    public List<String> getDocumentosAusentes() {
        return documentosAusentes;
    }

    public void setDocumentosAusentes(List<String> documentosAusentes) {
        this.documentosAusentes = documentosAusentes;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public List<MensagemOrientacaoDTO> getMensagensOrientacao() {
        return mensagensOrientacao;
    }

    public void setMensagensOrientacao(List<MensagemOrientacaoDTO> mensagensOrientacao) {
        this.mensagensOrientacao = mensagensOrientacao;
    }

    public SipesResponseDTO getResultadoPesquisa() {
        return resultadoPesquisa;
    }

    public void setResultadoPesquisa(SipesResponseDTO resultadoPesquisa) {
        this.resultadoPesquisa = resultadoPesquisa;
    }

    //*********************************
    public void addDocumentosAusentes(String... documentoAusente) {
        this.documentosAusentes.addAll(Arrays.asList(documentoAusente));
    }

    public void addDocumentosUtilizados(DocumentoUtilizadoDTO... documentoUtilizadoDTO) {
        this.documentosUtilizados.addAll(Arrays.asList(documentoUtilizadoDTO));
    }

    public void addMensagensOrientacoes(MensagemOrientacaoDTO... mensagensOrientacoesDTO) {
        this.mensagensOrientacao.addAll(Arrays.asList(mensagensOrientacoesDTO));
    }
}
