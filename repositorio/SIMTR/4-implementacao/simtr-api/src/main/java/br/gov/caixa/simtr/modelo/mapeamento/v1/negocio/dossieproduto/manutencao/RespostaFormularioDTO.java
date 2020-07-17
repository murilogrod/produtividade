package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoManutencao;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoManutencao.XML_ROOT_ELEMENT_RESPOSTA_FORMULARIO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoManutencao.API_MODEL_V1_RESPOSTA_FORMULARIO,
        description = "Objeto utilizado para representar a resposta a uma pergunta do formulario do dossiê de produto na ótica do Apoio ao Negócio."
)
public class RespostaFormularioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.CAMPO_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.CAMPO_FORMULARIO, required = true, value = "Identificador único do campo do formulario que a resposta esta vinculada")
    private Long idCampoFomulario;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.RESPOSTA)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.RESPOSTA, required = false, value = "Descrição da resposta. Utilizado nos casos de perguntas abertas. Não deve ser enviado caso o campo \"opcoes_selecionadas\" seja preenchido")
    private String resposta;

    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.OPCOES_SELECIONADAS)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.OPCAO_SELECIONADA)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.OPCOES_SELECIONADAS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.OPCOES_SELECIONADAS, required = false, value = "Lista de identificadores das opções selecionadas. Utilizado nos casos de perguntas objetivas. Não deve ser enviado caso o campo \"resposta\" seja preenchido")
    private List<String> opcoesSelecionadas;

    public RespostaFormularioDTO() {
    }

    public RespostaFormularioDTO(Long idCampoFormulario, String resposta) {
        super();
        this.idCampoFomulario = idCampoFormulario;
        this.resposta = resposta;
    }

    public RespostaFormularioDTO(Long idCampoFormulario, String... opcoesSelecionadas) {
        super();
        this.idCampoFomulario = idCampoFormulario;
        if (opcoesSelecionadas != null) {
            this.opcoesSelecionadas = Arrays.asList(opcoesSelecionadas);
        }
    }

    public Long getIdCampoFomulario() {
        return idCampoFomulario;
    }

    public void setIdCampoFomulario(Long idCampoFomulario) {
        this.idCampoFomulario = idCampoFomulario;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public List<String> getOpcoesSelecionadas() {
        return opcoesSelecionadas;
    }

    public void setOpcoesSelecionadas(List<String> opcoesSelecionadas) {
        this.opcoesSelecionadas = opcoesSelecionadas;
    }
}
