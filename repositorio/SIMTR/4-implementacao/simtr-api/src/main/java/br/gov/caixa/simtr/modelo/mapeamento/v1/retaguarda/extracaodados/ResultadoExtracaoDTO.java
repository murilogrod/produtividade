package br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.extracaodados;

import br.gov.caixa.simtr.controle.vo.extracaodados.ResultadoAvaliacaoExtracaoVO;
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesRetaguardaExtracaoDados;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesRetaguardaExtracaoDados.XML_ROOT_ELEMENT_RESULTADO_EXTRACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesRetaguardaExtracaoDados.API_MODEL_RESULTADO_AVALIACAO_EXTRACAO,
        description = "Objeto utilizado para representar o resultado do serviço de classificação, extração de dados e avaliação avaliação documental sob a otica dos serviços públicos."
)
public class ResultadoExtracaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesRetaguardaExtracaoDados.TIPO_DOCUMENTO, required = true)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.TIPO_DOCUMENTO, required = true, value = "Tipo do documento definido para o documento. Essa informação determinará a classificação do documento encaminhada.")
    private String tipoDocumento;

    @XmlElement(name = ConstantesRetaguardaExtracaoDados.CODIGO_REJEICAO, required = false)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.CODIGO_REJEICAO, required = false, value = "Codigo de identificação do problema apresentado no documento que impediu a execução do serviço.")
    private String codigoRejeicao;

    // *********************************************
    @XmlElement(name = ConstantesRetaguardaExtracaoDados.ATRIBUTO)
    @XmlElementWrapper(name = ConstantesRetaguardaExtracaoDados.ATRIBUTOS)
    @JsonProperty(value = ConstantesRetaguardaExtracaoDados.ATRIBUTOS)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.ATRIBUTOS, required = false, value = "Lista dos atributos extraidos do documento.")
    private List<AtributoDocumentoResultadoDTO> atributosDocumentoDTO;

    public ResultadoExtracaoDTO() {
        super();
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getCodigoRejeicao() {
        return codigoRejeicao;
    }

    public void setCodigoRejeicao(String codigoRejeicao) {
        this.codigoRejeicao = codigoRejeicao;
    }

    public List<AtributoDocumentoResultadoDTO> getAtributosDocumentoDTO() {
        return atributosDocumentoDTO;
    }

    public void setAtributosDocumentoDTO(List<AtributoDocumentoResultadoDTO> atributosDocumentoDTO) {
        this.atributosDocumentoDTO = atributosDocumentoDTO;
    }

    public ResultadoAvaliacaoExtracaoVO prototype() {
        ResultadoAvaliacaoExtracaoVO resultadoAvaliacaoExtracaoVO = new ResultadoAvaliacaoExtracaoVO();
        resultadoAvaliacaoExtracaoVO.setTipoDocumento(this.tipoDocumento);
        resultadoAvaliacaoExtracaoVO.setCodigoRejeicao(this.codigoRejeicao);
        if (this.atributosDocumentoDTO != null) {
            this.atributosDocumentoDTO.forEach(ad -> resultadoAvaliacaoExtracaoVO.addAtributosDocumentoVO(ad.prototype()));
        }

        return resultadoAvaliacaoExtracaoVO;
    }
}
