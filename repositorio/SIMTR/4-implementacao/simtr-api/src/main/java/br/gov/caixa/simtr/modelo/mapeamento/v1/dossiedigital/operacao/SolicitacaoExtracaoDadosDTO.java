package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.operacao;

import java.io.Serializable;
import java.util.ArrayList;
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

@XmlRootElement(name = ConstantesDossieDigitalOperacao.XML_ROOT_ELEMENT_SOLICITACAO_EXTRACAO_DADOS)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesDossieDigitalOperacao.API_MODEL_SOLICITACAO_EXTRACAO_DADOS,
        description = "Objeto utilizado para representar a solicitação de extração de dados de um documento peranto o fluxo do Dossiê Digital."
)
public class SolicitacaoExtracaoDadosDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    

    @XmlElement(name = ConstantesDossieDigitalOperacao.INTEGRACAO)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.INTEGRACAO, required = true, value = "Código de integração para identificar o canal solicitante")
    private Long codigoIntegracao;
    

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CPFAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalOperacao.CPF_CLIENTE)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.CPF_CLIENTE, required = false, value = "CPF do cliente caso o mesmo seja pessoa fisica. Deve ser informado se o cliente for PF. Zeros a esquerda não devem ser incluidos.", example = "11122233399")
    private Long cpfCliente;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CNPJAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalOperacao.CNPJ_CLIENTE)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.CNPJ_CLIENTE, required = false, value = "CNPJ do cliente pessoa fisica. Deve ser informado se o cliente for PJ. Zeros a esquerda não devem ser incluidos.", example = "11222333000099")
    private Long cnpjCliente;

    @XmlElement(name = ConstantesDossieDigitalOperacao.FORMATO)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.FORMATO, required = true, value = "Formato da imagem.")
    private FormatoConteudoEnum formato;

    @XmlElement(name = ConstantesDossieDigitalOperacao.TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.TIPO_DOCUMENTO, required = true, value = "Classe documental da imagem.")
    private String tipoDocumento;

    @JsonProperty(value = ConstantesDossieDigitalOperacao.IMAGENS)
    @XmlElement(name = ConstantesDossieDigitalOperacao.IMAGEM)
    @XmlElementWrapper(name = ConstantesDossieDigitalOperacao.IMAGENS)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.IMAGENS, required = true, value = "Lista das imagens em base64.")
    private List<String> imagens;

    public SolicitacaoExtracaoDadosDTO() {
        super();
        this.imagens = new ArrayList<>();
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

    public Long getCodigoIntegracao() {
        return codigoIntegracao;
    }

    public void setCodigoIntegracao(Long codigoIntegracao) {
        this.codigoIntegracao = codigoIntegracao;
    }

    public FormatoConteudoEnum getFormato() {
        return formato;
    }

    public void setFormato(FormatoConteudoEnum formato) {
        this.formato = formato;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String classe) {
        this.tipoDocumento = classe;
    }

    public List<String> getImagens() {
        return imagens;
    }

    public void setImagens(List<String> imagens) {
        this.imagens = imagens;
    }
}
