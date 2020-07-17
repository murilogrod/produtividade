package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tratamento.apuracao;

import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTratamento;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

@XmlRootElement(name = ConstantesNegocioTratamento.XML_ROOT_ELEMENT_INSTANCIA_DOCUMENTO_REPLICADA)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTratamento.API_MODEL_V1_APURACAO__INSTANCIA_DOCUMENTO_REPLICADA,
        description = "Objeto utilizado para representar a replicação apresentada para a instâcia do documento no retorno da apuração da execução do tratamentosob a ótica Apoio ao Negocio."
)
public class InstanciaDocumentoReplicadaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioTratamento.IDENTIFICADOR_INSTANCIA)
    @ApiModelProperty(name = ConstantesNegocioTratamento.IDENTIFICADOR_INSTANCIA, required = true, value = "Codigo de identificação da instancia de documento encaminhada.")
    private Long identificadorInstancia;

    @XmlElement(name = ConstantesNegocioTratamento.TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioTratamento.TIPO_DOCUMENTO, required = true, value = "Nome do tipo de documento vinculado a instancia de documento.")
    private String tipoDocumento;

    @XmlElement(name = ConstantesNegocioTratamento.QUANTIDADE)
    @ApiModelProperty(name = ConstantesNegocioTratamento.QUANTIDADE, required = true, value = "Indicação de quantas vezes a instancia de documento em referência foi identificada na apuração encaminhada.")
    private Integer quantidade;

    public InstanciaDocumentoReplicadaDTO() {
        super();
    }

    public InstanciaDocumentoReplicadaDTO(InstanciaDocumento instanciaDocumento, Integer quantidade) {
        this();
        if (Objects.nonNull(instanciaDocumento)) {
            this.identificadorInstancia = instanciaDocumento.getId();
            if (Objects.nonNull(instanciaDocumento.getDocumento()) && Objects.nonNull(instanciaDocumento.getDocumento().getTipoDocumento())) {
                this.tipoDocumento = instanciaDocumento.getDocumento().getTipoDocumento().getNome();
            }
        }
        this.quantidade = quantidade;
    }

    public Long getIdentificadorInstancia() {
        return identificadorInstancia;
    }

    public void setIdentificadorInstancia(Long identificadorInstancia) {
        this.identificadorInstancia = identificadorInstancia;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
