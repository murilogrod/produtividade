package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioProcesso;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioProcesso.XML_ROOT_ELEMENT_FUNCAO_DOCUMENTAL)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioProcesso.API_MODEL_V1_FUNCAO_DOCUMENTAL,
        description = "Objeto utilizado para representar a função documental relacionada ao vinculo de pessoas na definição do processo."
)
public class FuncaoDocumentalDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(FuncaoDocumentalDTO.class.getName());

    @XmlElement(name = ConstantesNegocioProcesso.ID)
    @ApiModelProperty(name = ConstantesNegocioProcesso.ID, value = "Identificador único da função documental.", required = true)
    private Integer id;

    @XmlElement(name = ConstantesNegocioProcesso.NOME)
    @ApiModelProperty(name = ConstantesNegocioProcesso.NOME, value = "Nome descritivo da função documental.", required = true)
    private String nome;

    // ***********************************
    @XmlElementWrapper(name = ConstantesNegocioProcesso.TIPOS_DOCUMENTO)
    @XmlElement(name = ConstantesNegocioProcesso.TIPO_DOCUMENTO)
    @JsonProperty(value = ConstantesNegocioProcesso.TIPOS_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.TIPOS_DOCUMENTO, value = "Lista dos tipos de documentos vinculados a função documental.")
    private List<TipoDocumentoDTO> tiposDocumentoDTO;

    public FuncaoDocumentalDTO() {
        super();
        this.tiposDocumentoDTO = new ArrayList<>();
    }

    public FuncaoDocumentalDTO(FuncaoDocumental funcaoDocumental) {
        this();
        if (funcaoDocumental != null) {
            this.id = funcaoDocumental.getId();
            this.nome = funcaoDocumental.getNome();

            try {
                if (funcaoDocumental.getTiposDocumento() != null) {
                    funcaoDocumental.getTiposDocumento().forEach(tipoDocumento -> this.tiposDocumentoDTO.add(new TipoDocumentoDTO(tipoDocumento)));
                }
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.tiposDocumentoDTO = null;
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<TipoDocumentoDTO> getTiposDocumentoDTO() {
        return tiposDocumentoDTO;
    }

    public void setTiposDocumentoDTO(List<TipoDocumentoDTO> tiposDocumentoDTO) {
        this.tiposDocumentoDTO = tiposDocumentoDTO;
    }

}
