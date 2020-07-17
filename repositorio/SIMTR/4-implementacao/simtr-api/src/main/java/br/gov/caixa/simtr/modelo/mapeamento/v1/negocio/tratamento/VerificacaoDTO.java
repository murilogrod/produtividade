package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tratamento;

import br.gov.caixa.simtr.controle.vo.checklist.VerificacaoVO;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTratamento;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

@XmlRootElement(name = ConstantesNegocioTratamento.XML_ROOT_ELEMENT_VERIFICACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTratamento.XML_ROOT_ELEMENT_VERIFICACAO,
        description = "Objeto utilizado para representar uma verificação realizada em um checklist no momento do tratamento de um dossiê de produto sob a ótica Apoio ao Negocio."
)
public class VerificacaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioTratamento.IDENTIFICADOR_INSTANCIA_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioTratamento.IDENTIFICADOR_INSTANCIA_DOCUMENTO, required = false, value = "Código de identificação da instância de documento analisada. Enviado apenas se o checklist analisado não tenha vinculação com uma instancia de documento.")
    private Long identificadorInstanciaDocumento;

    @XmlElement(name = ConstantesNegocioTratamento.IDENTIFICADOR_CHECKLIST)
    @ApiModelProperty(name = ConstantesNegocioTratamento.IDENTIFICADOR_CHECKLIST, required = true, value = "Código de identificação do checklist analisado ao qual esta sendo encaminhado parecer dos apontamentos vinculados.")
    private Integer identificadorChecklist;

    @XmlElement(name = ConstantesNegocioTratamento.ANALISE_REALIZADA)
    @ApiModelProperty(name = ConstantesNegocioTratamento.ANALISE_REALIZADA, required = true, value = "Indica se a verificação encaminhada foi pelo operador ou não. Caso a mesma não tenha sido realizada, a lista de apontamentos se encaminhada será descartada. Esse cenário só é possivel no caso de documentos não obrigatórios.")
    private boolean analiseRealizada;

    // *********************************************
    @XmlElementWrapper(name = ConstantesNegocioTratamento.PARECER_APONTAMENTOS)
    @XmlElement(name = ConstantesNegocioTratamento.PARECER_APONTAMENTO)
    @JsonProperty(value = ConstantesNegocioTratamento.PARECER_APONTAMENTOS)
    @ApiModelProperty(name = ConstantesNegocioTratamento.PARECER_APONTAMENTOS, required = false, value = "Lista de objetos que representa o parecer emitido para cada um dos apontamento vinculados ao checklist indicado.")
    private List<ParecerApontamentoDTO> parecerApontamentosDTO;

    public VerificacaoDTO() {
        super();
        this.parecerApontamentosDTO = new ArrayList<>();
    }

    public Long getIdentificadorInstanciaDocumento() {
        return identificadorInstanciaDocumento;
    }

    public void setIdentificadorInstanciaDocumento(Long identificadorInstanciaDocumento) {
        this.identificadorInstanciaDocumento = identificadorInstanciaDocumento;
    }

    public Integer getIdentificadorChecklist() {
        return identificadorChecklist;
    }

    public void setIdentificadorChecklist(Integer identificadorChecklist) {
        this.identificadorChecklist = identificadorChecklist;
    }

    public boolean isAnaliseRealizada() {
        return analiseRealizada;
    }

    public void setAnaliseRealizada(boolean analiseRealizada) {
        this.analiseRealizada = analiseRealizada;
    }

    public List<ParecerApontamentoDTO> getParecerApontamentosDTO() {
        return parecerApontamentosDTO;
    }

    public void setParecerApontamentosDTO(List<ParecerApontamentoDTO> parecerApontamentosDTO) {
        this.parecerApontamentosDTO = parecerApontamentosDTO;
    }

    //*******************************************
    public VerificacaoVO prototype() {
        VerificacaoVO verificacaoVO = new VerificacaoVO();
        verificacaoVO.setIdentificadorChecklist(this.identificadorChecklist);
        verificacaoVO.setIdentificadorInstanciaDocumento(this.identificadorInstanciaDocumento);
        verificacaoVO.setAnaliseRealizada(this.analiseRealizada);
        if (Objects.nonNull(this.parecerApontamentosDTO)) {
            parecerApontamentosDTO.forEach(pa -> {
                verificacaoVO.addParecerApontamentosVO(pa.prototype());
            });
        }
        return verificacaoVO;
    }
}
