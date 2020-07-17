package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProduto.API_MODEL_V1_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProduto.API_MODEL_V1_DOCUMENTO,
        description = "Objeto utilizado para representar o Documento vinculado ao dossiê do cliente no retorno as consultas realizadas sob a ótica Apoio ao Negocio a partir do Dossiê do Cliente."
)
public class DocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(DocumentoDTO.class.getName());

    @XmlElement(name = ConstantesNegocioDossieProduto.ID, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ID, value = "Identificador único do documento.", required = true)
    private Long id;

    @XmlElement(name = ConstantesNegocioDossieProduto.TIPO_DOCUMENTO, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.TIPO_DOCUMENTO, value = "Tipo do documento categorizado.", required = true)
    private TipoDocumentoDTO tipoDocumento;

    @XmlElement(name = ConstantesNegocioDossieProduto.CANAL_CAPTURA, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.CANAL_CAPTURA, value = "Canal de captura do documento.", required = true)
    private CanalDTO canalCapturaDTO;

    @XmlElement(name = ConstantesNegocioDossieProduto.CODIGO_GED, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.CODIGO_GED, value = "Identificador do documento perante a solução de GED.", required = true)
    private String codigoGED;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProduto.DATA_HORA_CAPTURA)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.DATA_HORA_CAPTURA, value = "Data/Hora de captura do documento.", required = true, example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraCaptura;

    @XmlElement(name = ConstantesNegocioDossieProduto.MATRICULA_CAPTURA)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.MATRICULA_CAPTURA, value = "Matricula do usuário responsável pela captura do documento.", required = true, example = "c999999")
    private String matricula;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProduto.DATA_HORA_VALIDADE)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.DATA_HORA_VALIDADE, value = "Data/Hora da validade do documento para novos negocios.", required = false, example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraValidade;

    @XmlElement(name = ConstantesNegocioDossieProduto.DOSSIE_DIGITAL)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.DOSSIE_DIGITAL, value = "Indicador de documento avaliado pelo fluxo do Dossiê Digital.", required = true)
    private Boolean dossieDigital;

    @XmlElement(name = ConstantesNegocioDossieProduto.ORIGEM_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ORIGEM_DOCUMENTO, value = "Identificador do tipo de midia origem do documento digitalizado.", required = true)
    private OrigemDocumentoEnum origemDocumentoEnum;
    
    @XmlElement(name = ConstantesNegocioDossieProduto.QUANTIDADE_CONTEUDOS)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.QUANTIDADE_CONTEUDOS, value = "Quantidade de páginas do documento.", required = false)
    private Integer quantidadeConteudos;

    // *********************************************
    @JsonInclude(value = Include.NON_EMPTY)
    @XmlElement(name = ConstantesNegocioDossieProduto.BINARIO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProduto.BINARIO)
    @JsonProperty(value = ConstantesNegocioDossieProduto.BINARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.BINARIO, value = "Representa o binario do documento em padrão BASE64", required = false)
    private String binario;
    
    public DocumentoDTO() {
        super();
    }

    public DocumentoDTO(Documento documento) {
        this();
        if (documento != null) {
            this.id = documento.getId();
            if (documento.getTipoDocumento() != null) {
                this.tipoDocumento = new TipoDocumentoDTO(documento.getTipoDocumento());
            }
            if (documento.getCanalCaptura() != null) {
                this.canalCapturaDTO = new CanalDTO(documento.getCanalCaptura());
            }
            this.codigoGED = documento.getCodigoGED();
            this.dataHoraCaptura = documento.getDataHoraCaptura();
            this.matricula = documento.getResponsavel();
            this.dataHoraValidade = documento.getDataHoraValidade();
            this.dossieDigital = documento.getDossieDigital();
            this.origemDocumentoEnum = documento.getOrigemDocumento();
            this.quantidadeConteudos = documento.getQuantidadeConteudos();
            
            try {
                if (documento.getConteudos() != null && documento.getConteudos().size() > 0) {
                	this.binario = documento.getConteudos().iterator().next().getBase64();
                }
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                documento.setConteudos(null);
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoDocumentoDTO getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumentoDTO tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public CanalDTO getCanalCapturaDTO() {
        return canalCapturaDTO;
    }

    public void setCanalCapturaDTO(CanalDTO canalCapturaDTO) {
        this.canalCapturaDTO = canalCapturaDTO;
    }

    public String getCodigoGED() {
        return codigoGED;
    }

    public void setCodigoGED(String codigoGED) {
        this.codigoGED = codigoGED;
    }

    public Calendar getDataHoraCaptura() {
        return dataHoraCaptura;
    }

    public void setDataHoraCaptura(Calendar dataHoraCaptura) {
        this.dataHoraCaptura = dataHoraCaptura;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Calendar getDataHoraValidade() {
        return dataHoraValidade;
    }

    public void setDataHoraValidade(Calendar dataHoraValidade) {
        this.dataHoraValidade = dataHoraValidade;
    }

    public Boolean getDossieDigital() {
        return dossieDigital;
    }

    public void setDossieDigital(Boolean dossieDigital) {
        this.dossieDigital = dossieDigital;
    }

    public OrigemDocumentoEnum getOrigemDocumentoEnum() {
        return origemDocumentoEnum;
    }

    public void setOrigemDocumentoEnum(OrigemDocumentoEnum origemDocumentoEnum) {
        this.origemDocumentoEnum = origemDocumentoEnum;
    }

    public String getBinario() {
	return binario;
    }

    public void setBinario(String binario) {
	this.binario = binario;
    }
}
