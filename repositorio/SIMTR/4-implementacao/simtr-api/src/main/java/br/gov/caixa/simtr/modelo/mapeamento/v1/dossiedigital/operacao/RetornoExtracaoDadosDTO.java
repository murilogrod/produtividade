package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.operacao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalOperacao;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CNPJAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CPFAdapter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalOperacao.XML_ROOT_ELEMENT_RETORNO_EXTRACAO_DADOS)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesDossieDigitalOperacao.API_MODEL_RETORNO_EXTRACAO_DADOS,
        description = "Objeto utilizado para representar a resposta a uma solicitação de extração de dados de um documento peranto o fluxo do Dossiê Digital."
)
public class RetornoExtracaoDadosDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CPFAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalOperacao.CPF_CLIENTE)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.CPF_CLIENTE, required = false, value = "CPF do cliente caso o mesmo seja pessoa fisica. Deve ser informado se o cliente for PF. Zeros a esquerda não devem ser incluidos.", example = "11122233399")
    protected Long cpfCliente;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CNPJAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalOperacao.CNPJ_CLIENTE)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.CNPJ_CLIENTE, required = false, value = "CNPJ do cliente pessoa fisica. Deve ser informado se o cliente for PJ. Zeros a esquerda não devem ser incluidos.", example = "11222333000099")
    protected Long cnpjCliente;

    @XmlElement(name = ConstantesDossieDigitalOperacao.FORMATO)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.FORMATO, required = true, value = "Formato da imagem")
    private FormatoConteudoEnum formato;

    @XmlElement(name = ConstantesDossieDigitalOperacao.TAMANHO)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.TAMANHO, required = true, value = "Tamanho em bytes do documento processado")
    private Long tamanhoDocumento;

    @XmlElement(name = ConstantesDossieDigitalOperacao.IDENTIFICADOR)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.IDENTIFICADOR, required = true, value = "Código de identificação do documento junto ao GED")
    private String identificador;

    @XmlElement(name = ConstantesDossieDigitalOperacao.LINK)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.LINK, required = true, value = "Link em formato URL para visualização do documento")
    private String link;

    @JsonProperty(value = ConstantesDossieDigitalOperacao.DADOS)
    @XmlElement(name = ConstantesDossieDigitalOperacao.DADO)
    @XmlElementWrapper(name = ConstantesDossieDigitalOperacao.DADOS)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.DADOS, required = true, value = "Lista dos dados extraídos das imagens")
    private List<AtributoExtraidoDTO> dadosDocumento;

    public RetornoExtracaoDadosDTO() {
        super();
        dadosDocumento = new ArrayList<>();
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

    public FormatoConteudoEnum getFormato() {
        return formato;
    }

    public void setFormato(FormatoConteudoEnum formato) {
        this.formato = formato;
    }

    public Long getTamanhoDocumento() {
        return tamanhoDocumento;
    }

    public void setTamanhoDocumento(Long tamanhoDocumento) {
        this.tamanhoDocumento = tamanhoDocumento;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<AtributoExtraidoDTO> getDadosDocumento() {
        return dadosDocumento;
    }

    public void setDadosDocumento(List<AtributoExtraidoDTO> dadosExtracaoDocumento) {
        this.dadosDocumento = dadosExtracaoDocumento;
    }

    //*************************************
    public boolean addAtributosDocumentoDTO(AtributoExtraidoDTO... atributosDocumentoExtracaoDTO) {
        return this.dadosDocumento.addAll(Arrays.asList(atributosDocumentoExtracaoDTO));
    }
}
