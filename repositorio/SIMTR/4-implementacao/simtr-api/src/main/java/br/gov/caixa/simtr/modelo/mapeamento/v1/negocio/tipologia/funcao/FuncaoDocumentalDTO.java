package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tipologia.funcao;

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
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTipologia;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioTipologia.XML_ROOT_ELEMENT_FUNCAO_DOCUMENTAL)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTipologia.VISAO_FUNCAO_API_MODEL_V1_FUNCAO_DOCUMENTAL,
        description = "Objeto utilizado para representar a Função Documental no contexto da Função Documental sob a visão da sustentação ao negócio."
)
public class FuncaoDocumentalDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(FuncaoDocumentalDTO.class.getName());

    @XmlElement(name = ConstantesNegocioTipologia.ID)
    @ApiModelProperty(name = ConstantesNegocioTipologia.ID, value = "Identificador da função documental.", required = true)
    private Integer id;

    @XmlElement(name = ConstantesNegocioTipologia.NOME)
    @ApiModelProperty(name = ConstantesNegocioTipologia.NOME, value = "Nome negocial da função documental.", required = true)
    private String nome;

    // ***********************************
    @XmlElement(name = ConstantesNegocioTipologia.TIPO_DOCUMENTO)
    @XmlElementWrapper(name = ConstantesNegocioTipologia.TIPOS_DOCUMENTOS)
    @JsonProperty(value = ConstantesNegocioTipologia.TIPOS_DOCUMENTOS)
    @ApiModelProperty(name = ConstantesNegocioTipologia.TIPOS_DOCUMENTOS, value = "Lista dos tipos de documentos vinculados a função.")
    private List<TipoDocumentoDTO> tiposDocumentoDTO;

    public FuncaoDocumentalDTO() {
        super();
        this.tiposDocumentoDTO = new ArrayList<>();
    }

    public FuncaoDocumentalDTO(FuncaoDocumental funcaoDocumental) {
        this();
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
