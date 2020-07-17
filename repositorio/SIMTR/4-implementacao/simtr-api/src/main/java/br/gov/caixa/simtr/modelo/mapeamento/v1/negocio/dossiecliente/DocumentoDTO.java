package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente;

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
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieCliente;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieCliente.XML_ROOT_ELEMENT_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesNegocioDossieCliente.API_MODEL_V1_DOCUMENTO, description = "Objeto utilizado para representar o documento vinculado ao dossiê do cliente no retorno as consultas realizadas a partir do Dossiê do Cliente")
public class DocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(DocumentoDTO.class.getName());

    @XmlElement(name = ConstantesNegocioDossieCliente.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.ID, required = true, value = "Identificador único do documento")
    private Long id;

    @XmlElement(name = ConstantesNegocioDossieCliente.TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.TIPO_DOCUMENTO, required = true, value = "Tipo do documento categorizado")
    private TipoDocumentoDTO tipoDocumento;

    @XmlElement(name = ConstantesNegocioDossieCliente.CANAL_CAPTURA)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.CANAL_CAPTURA, required = true, value = "Canal de captura do documento")
    private CanalDTO canalCapturaDTO;

    @XmlElement(name = ConstantesNegocioDossieCliente.CODIGO_GED)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.CODIGO_GED, required = true, value = "Identificador do documento perante a solução de GED")
    private String codigoGED;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieCliente.DATA_HORA_CAPTURA)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.DATA_HORA_CAPTURA, required = true, value = "Data/Hora de captura do documento", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraCaptura;

    @XmlElement(name = ConstantesNegocioDossieCliente.MATRICULA_CAPTURA)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.MATRICULA_CAPTURA, required = false, value = "Matricula do usuário responsável pela captura do documento", example = "c999999")
    private String matricula;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieCliente.DATA_HORA_VALIDADE)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.DATA_HORA_VALIDADE, required = false, value = "Data/Hora da validade do documento para novos negocios", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraValidade;

    @XmlElement(name = ConstantesNegocioDossieCliente.DOSSIE_DIGITAL)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.DOSSIE_DIGITAL, required = true, value = "Indicador de documento avaliado pelo fluxo do Dossiê Digital")
    private Boolean dossieDigital;

    @XmlElement(name = ConstantesNegocioDossieCliente.ORIGEM_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.ORIGEM_DOCUMENTO, required = true, value = "Identificador do tipo de midia origem do documento digitalizado")
    private OrigemDocumentoEnum origemDocumentoEnum;

    @XmlElement(name = ConstantesNegocioDossieCliente.MIME_TYPE)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.MIME_TYPE, required = false, value = "Identificador do tipo da extensão do documento")
    private FormatoConteudoEnum mimeType;

    // *********************************************
    @JsonInclude(value = Include.NON_EMPTY)
    @XmlElement(name = ConstantesNegocioDossieCliente.BINARIO)
    @XmlElementWrapper(name = ConstantesNegocioDossieCliente.BINARIO)
    @JsonProperty(value = ConstantesNegocioDossieCliente.BINARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.BINARIO, required = false, value = "Representa o binario do documento em padrão BASE64")
    private String binario;

    @XmlElement(name = ConstantesNegocioDossieProduto.QUANTIDADE_CONTEUDOS)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.QUANTIDADE_CONTEUDOS, value = "Quantidade de páginas do documento.", required = false)
    private Integer quantidadeConteudos;

    public DocumentoDTO() {
	super();
    }

    public DocumentoDTO(Documento documento) {
	this();
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
	this.mimeType = documento.getFormatoConteudoEnum();
	this.quantidadeConteudos = documento.getQuantidadeConteudos();

	try {
	    if (documento.getConteudos() != null && documento.getConteudos().size() > 0) {
		this.binario = documento.getConteudos().iterator().next().getBase64();
	    }
	} catch (RuntimeException re) {
	    LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
	    // Captura de LazyInitializationException - Utilizada RuntimeException pois a
	    // Lazy é especifica do pacote hinernate
	    this.binario = null;
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

    public FormatoConteudoEnum getMimeType() {
	return mimeType;
    }

    public void setMimeType(FormatoConteudoEnum mimeType) {
	this.mimeType = mimeType;
    }

    public String getBinario() {
	return binario;
    }

    public void setBinario(String binario) {
	this.binario = binario;
    }

    public Integer getQuantidadeConteudos() {
	return quantidadeConteudos;
    }

    public void setQuantidadeConteudos(Integer quantidadeConteudos) {
	this.quantidadeConteudos = quantidadeConteudos;
    }
}
