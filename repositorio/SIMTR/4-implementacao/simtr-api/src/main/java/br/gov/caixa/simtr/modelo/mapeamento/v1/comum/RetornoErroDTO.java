package br.gov.caixa.simtr.modelo.mapeamento.v1.comum;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesComum;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesComum.XML_ROOT_RETORNO_ERRO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesComum.API_MODEL_RETORNO_ERRO,
        description = "Objeto utilizado para representar falhas nas requisições e devolver a indicação do problema para o usuário.."
)
public class RetornoErroDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesComum.CODIGO_HTTP)
    @ApiModelProperty(name = ConstantesComum.CODIGO_HTTP, required = true, value = "Codigo HTTP definido para identificar o tipo de problema apresentado.")
    private int codigoHTTP;

    @XmlElement(name = ConstantesComum.MENSAGEM)
    @ApiModelProperty(name = ConstantesComum.MENSAGEM, required = true, value = "Mensagem de identificação do problema.")
    private String mensagem;

    @XmlElement(name = ConstantesComum.DETALHE)
    @ApiModelProperty(name = ConstantesComum.DETALHE, required = true, value = "Detalhamento da causa do ocorrido e possivelmente proposta de solução.")
    private String detail;

    @XmlElement(name = ConstantesComum.STACKTRACE)
    @ApiModelProperty(name = ConstantesComum.STACKTRACE, required = true, value = "Pilha de erro apresentada pela exceção ocorrida.")
    private String stacktrace;

    public RetornoErroDTO() {
    }

    public RetornoErroDTO(int codigoHTTP, String mensagem, String detail) {
        this.codigoHTTP = codigoHTTP;
        this.mensagem = mensagem;
        this.detail = detail;
    }

    public RetornoErroDTO(int codigoHTTP, String mensagem, String detail, String stacktrace) {
        this.codigoHTTP = codigoHTTP;
        this.mensagem = mensagem;
        this.detail = detail;
        this.stacktrace = stacktrace;
    }

    public int getCodigoHTTP() {
        return codigoHTTP;
    }

    public void setCodigoHTTP(int codigoHTTP) {
        this.codigoHTTP = codigoHTTP;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }
}
