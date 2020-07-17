package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalAutorizacao;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalAutorizacao.XML_ROOT_ELEMENT_MENSAGEM_ORIENTACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesDossieDigitalAutorizacao.API_MODEL_V1_MENSAGEM_ORIENTACAO)
public class MensagemOrientacaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalAutorizacao.SISTEMA)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.SISTEMA, required = true, value = "Nome do sistema relacionado com o SIPES que orignou a orientação", example = "SICOW")
    private String sistema;

    @XmlElement(name = ConstantesDossieDigitalAutorizacao.OCORRENCIA)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.OCORRENCIA, required = true, value = "Indicativo do tipo de ocorrência identificada.", example = "empregados_trabalho_escravo")
    private String ocorrencia;

    @XmlElement(name = ConstantesDossieDigitalAutorizacao.MENSAGEM)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.MENSAGEM, required = true, value = "Mensagem definida nas parametrizações da operações indicando uma orientação a ser adotada para o problema apresentado.")
    private String mensagem;

    public MensagemOrientacaoDTO(String sistema, String ocorrencia, String mensagem) {
        super();
        this.sistema = sistema;
        this.ocorrencia = ocorrencia;
        this.mensagem = mensagem;
    }

    public String getSistema() {
        return sistema;
    }

    public void setSistema(String sistema) {
        this.sistema = sistema;
    }

    public String getOcorrencia() {
        return ocorrencia;
    }

    public void setOcorrencia(String ocorrencia) {
        this.ocorrencia = ocorrencia;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
