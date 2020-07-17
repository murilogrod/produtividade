package br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.outsourcing;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.controle.vo.extracaodados.ResultadoAvaliacaoExtracaoVO;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesRetaguardaAvaliacaoDocumento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

@XmlRootElement(name = ConstantesRetaguardaAvaliacaoDocumento.XML_ROOT_ELEMENT_RESULTADO_AVALIACAO_EXTRACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesRetaguardaAvaliacaoDocumento.API_MODEL_V1_RESULTADO_AVALIACAO_EXTRACAO,
        description = "Objeto utilizado para representar o resultado do serviço de classificação, extração de dados e avaliação avaliação documental sob a otica dos serviços públicos."
)
public class ResultadoAvaliacaoExtracaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.TIPO_DOCUMENTO, required = true)
    @JsonProperty(value = ConstantesRetaguardaAvaliacaoDocumento.TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.TIPO_DOCUMENTO, required = true, value = "Tipo do documento definido para o documento. Essa informação determinará a classificação do documento encaminhada.")
    private String tipoDocumento;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.MIMETYPE, required = false)
    @JsonProperty(value = ConstantesRetaguardaAvaliacaoDocumento.MIMETYPE)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.MIMETYPE, required = false, value = "Formato do conteudo do documento encaminhado.")
    private String mimetype;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.INDICE_AVALIACAO_AUTENTICIDADE, required = false)
    @JsonProperty(value = ConstantesRetaguardaAvaliacaoDocumento.INDICE_AVALIACAO_AUTENTICIDADE)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.INDICE_AVALIACAO_AUTENTICIDADE, required = false, value = "Indice retornado pelo serviço de avaliação de autenticidade documental.")
    private BigDecimal indiceAvaliacaoAutenticidade;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.CODIGO_REJEICAO, required = false)
    @JsonProperty(value = ConstantesRetaguardaAvaliacaoDocumento.CODIGO_REJEICAO)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.CODIGO_REJEICAO, required = false, value = "Codigo de identificação do problema apresentado no documento que impediu a execução do serviço.")
    private String codigoRejeicao;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.BINARIO, required = false)
    @JsonProperty(value = ConstantesRetaguardaAvaliacaoDocumento.BINARIO)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.BINARIO, required = false, value = "String em formato base64 vinculada ao registro do documento que representa o binario tratado com a remoção de borda, area não utilizavel, etc")
    private String binario;

    // *********************************************
    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.ATRIBUTO)
    @XmlElementWrapper(name = ConstantesRetaguardaAvaliacaoDocumento.ATRIBUTOS)
    @JsonProperty(value = ConstantesRetaguardaAvaliacaoDocumento.ATRIBUTOS)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.ATRIBUTOS, required = false, value = "Lista dos atributos extraidos do documento.")
    private List<AtributoDocumentoDTO> atributosDocumentoDTO;

    public ResultadoAvaliacaoExtracaoDTO() {
        super();
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public BigDecimal getIndiceAvaliacaoAutenticidade() {
        return indiceAvaliacaoAutenticidade;
    }

    public void setIndiceAvaliacaoAutenticidade(BigDecimal indiceAvaliacaoAutenticidade) {
        this.indiceAvaliacaoAutenticidade = indiceAvaliacaoAutenticidade;
    }

    public String getCodigoRejeicao() {
        return codigoRejeicao;
    }

    public void setCodigoRejeicao(String codigoRejeicao) {
        this.codigoRejeicao = codigoRejeicao;
    }

    public String getBinario() {
        return binario;
    }

    public void setBinario(String binario) {
        this.binario = binario;
    }

    public List<AtributoDocumentoDTO> getAtributosDocumentoDTO() {
        return atributosDocumentoDTO;
    }

    public void setAtributosDocumentoDTO(List<AtributoDocumentoDTO> atributosDocumentoDTO) {
        this.atributosDocumentoDTO = atributosDocumentoDTO;
    }

    public ResultadoAvaliacaoExtracaoVO prototype() {
        ResultadoAvaliacaoExtracaoVO resultadoAvaliacaoExtracaoVO = new ResultadoAvaliacaoExtracaoVO();
        resultadoAvaliacaoExtracaoVO.setTipoDocumento(this.tipoDocumento);
        resultadoAvaliacaoExtracaoVO.setMimetype(this.mimetype);
        resultadoAvaliacaoExtracaoVO.setIndiceAvaliacaoAutenticidade(this.indiceAvaliacaoAutenticidade);
        resultadoAvaliacaoExtracaoVO.setCodigoRejeicao(this.codigoRejeicao);
        resultadoAvaliacaoExtracaoVO.setBinario(this.binario);
        if (Objects.nonNull(this.atributosDocumentoDTO)) {
            this.atributosDocumentoDTO.forEach(ad -> resultadoAvaliacaoExtracaoVO.addAtributosDocumentoVO(ad.prototype()));
        }

        return resultadoAvaliacaoExtracaoVO;
    }
}
