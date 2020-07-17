package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.gov.caixa.simtr.modelo.entidade.ProcessoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioProcesso;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioProcesso.API_MODEL_V1_DOCUMENTO_VINCULO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioProcesso.XML_ROOT_ELEMENT_DOCUMENTO_VINCULO,
        description = "Objeto utilizado para representar a estrutura de documentação definida para o vinculo determinado no processo."
)
public class DocumentoVinculoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(DocumentoVinculoDTO.class.getName());

    @XmlElement(name = ConstantesNegocioProcesso.TIPO_RELACIONAMENTO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.TIPO_RELACIONAMENTO, required = true, value = "Identifica o tipo de relacionamento definido para o vinculo.")
    private Integer tipoRelacionamento;

    @XmlElement(name = ConstantesNegocioProcesso.OBRIGATORIO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.OBRIGATORIO, required = true, value = "Indica para a interface se este elemento tem obrigatoriedade de carga.")
    private Boolean obrigatorio;

    @XmlElement(name = ConstantesNegocioProcesso.FUNCAO_DOCUMENTAL)
    @ApiModelProperty(name = ConstantesNegocioProcesso.FUNCAO_DOCUMENTAL, required = true, value = "Função documental definida para o vinculo. Qualquer tipo de documento que atenda a essa função poderá ser vinculado.")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FuncaoDocumentalDTO funcaoDocumentalDTO;

    @XmlElement(name = ConstantesNegocioProcesso.TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.TIPO_DOCUMENTO, required = true, value = "Tipo de Documento especifico definido para o vinculo. Nesta situação, apenas o documento definido poderá ser utilizado no atendimento ao vinculo.")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TipoDocumentoDTO tipoDocumentoDTO;

    public DocumentoVinculoDTO() {
        super();
    }

    public DocumentoVinculoDTO(ProcessoDocumento processoDocumento) {
        this();
        if (processoDocumento != null) {
            this.obrigatorio = processoDocumento.getObrigatorio();

            try {
                this.tipoRelacionamento = processoDocumento.getTipoRelacionamento().getId();
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.tipoRelacionamento = null;
            }

            try {
                if (processoDocumento.getFuncaoDocumental() != null) {
                    this.funcaoDocumentalDTO = new FuncaoDocumentalDTO(processoDocumento.getFuncaoDocumental());
                }
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.funcaoDocumentalDTO = null;
            }

            try {
                if (processoDocumento.getTipoDocumento() != null) {
                    this.tipoDocumentoDTO = new TipoDocumentoDTO(processoDocumento.getTipoDocumento());
                }
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.tipoDocumentoDTO = null;
            }
        }
    }

    public Integer getTipoRelacionamento() {
        return tipoRelacionamento;
    }

    public void setTipoRelacionamento(Integer tipoRelacionamento) {
        this.tipoRelacionamento = tipoRelacionamento;
    }

    public Boolean getObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(Boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public FuncaoDocumentalDTO getFuncaoDocumentalDTO() {
        return funcaoDocumentalDTO;
    }

    public void setFuncaoDocumentalDTO(FuncaoDocumentalDTO funcaoDocumentalDTO) {
        this.funcaoDocumentalDTO = funcaoDocumentalDTO;
    }

    public TipoDocumentoDTO getTipoDocumentoDTO() {
        return tipoDocumentoDTO;
    }

    public void setTipoDocumentoDTO(TipoDocumentoDTO tipoDocumentoDTO) {
        this.tipoDocumentoDTO = tipoDocumentoDTO;
    }
}
