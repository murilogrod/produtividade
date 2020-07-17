package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.documento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDocumento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDocumento.XML_ROOT_ELEMENT_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDocumento.API_MODEL_V1_DOCUMENTO,
        description = "Objeto utilizado para representar o Documento nas consultas realizadas sob a ótica Apoio ao Negocio."
)
public class DocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDocumento.ID, required = true)
    @ApiModelProperty(name = ConstantesNegocioDocumento.ID, value = "Identificador único do documento.", required = true)
    private Long id;

    @XmlElement(name = ConstantesNegocioDocumento.TIPO_DOCUMENTO, required = true)
    @ApiModelProperty(name = ConstantesNegocioDocumento.TIPO_DOCUMENTO, value = "Tipo do documento categorizado.", required = true)
    private TipoDocumentoDTO tipoDocumento;

    @XmlElement(name = ConstantesNegocioDocumento.CANAL_CAPTURA, required = true)
    @ApiModelProperty(name = ConstantesNegocioDocumento.CANAL_CAPTURA, value = "Canal de captura do documento.", required = true)
    private CanalDTO canalCaptura;

    @XmlElement(name = ConstantesNegocioDocumento.CODIGO_GED, required = true)
    @ApiModelProperty(name = ConstantesNegocioDocumento.CODIGO_GED, value = "Identificador do documento perante a solução de GED.", required = true)
    private String codigoGED;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDocumento.DATA_HORA_CAPTURA)
    @ApiModelProperty(name = ConstantesNegocioDocumento.DATA_HORA_CAPTURA, value = "Data/Hora de captura do documento.", required = true, example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraCaptura;

    @XmlElement(name = ConstantesNegocioDocumento.MATRICULA_CAPTURA)
    @ApiModelProperty(name = ConstantesNegocioDocumento.MATRICULA_CAPTURA, value = "Matricula do usuário responsável pela captura do documento.", required = true, example = "c999999")
    private String matricula;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDocumento.DATA_HORA_VALIDADE)
    @ApiModelProperty(name = ConstantesNegocioDocumento.DATA_HORA_VALIDADE, value = "Data/Hora da validade do documento para novos negocios.", required = false, example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraValidade;

    @XmlElement(name = ConstantesNegocioDocumento.DOSSIE_DIGITAL)
    @ApiModelProperty(name = ConstantesNegocioDocumento.DOSSIE_DIGITAL, value = "Indicador de documento avaliado pelo fluxo do Dossiê Digital.", required = true)
    private Boolean dossieDigital;

    @XmlElement(name = ConstantesNegocioDocumento.ORIGEM_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioDocumento.ORIGEM_DOCUMENTO, value = "Identificador do tipo de midia origem do documento digitalizado.", required = true)
    private OrigemDocumentoEnum origemDocumentoEnum;

    @Deprecated
    @XmlElement(name = ConstantesNegocioDocumento.MIME_TYPE)
    @ApiModelProperty(name = ConstantesNegocioDocumento.MIME_TYPE, value = "Identificador do tipo do mime type do documento.", required = false)
    private FormatoConteudoEnum formatoConteudo;
    
    @XmlElement(name = ConstantesNegocioDocumento.MIMETYPE)
    @ApiModelProperty(name = ConstantesNegocioDocumento.MIMETYPE, value = "Identificador do tipo do mime type do documento.", required = false)
    private String mimetype;
    
    @XmlElement(name = ConstantesNegocioDocumento.ANALISE_OUTSOURCING)
    @ApiModelProperty(name = ConstantesNegocioDocumento.ANALISE_OUTSOURCING, value = "Indica se o documento esta sob analise do servico de outsourcing documental.", required = false)
    private boolean analiseOutsourcing;

    // *********************************************
    @XmlElement(name = ConstantesNegocioDocumento.ATRIBUTO)
    @XmlElementWrapper(name = ConstantesNegocioDocumento.ATRIBUTOS)
    @JsonProperty(value = ConstantesNegocioDocumento.ATRIBUTOS)
    @ApiModelProperty(name = ConstantesNegocioDocumento.ATRIBUTOS, value = "Lista de atributos extraidos do documento", required = false)
    private List<AtributoDocumentoDTO> atributosDocumento;

    @JsonProperty(value = ConstantesNegocioDocumento.BINARIO)
    @ApiModelProperty(name = ConstantesNegocioDocumento.BINARIO, value = "Lista de conteudos vinculados ao registro do documento", required = false)
    private String binario;

    public DocumentoDTO() {
        super();
        this.atributosDocumento = new ArrayList<>();
    }

    public DocumentoDTO(Documento documento, String binario) {
        this();
        if (documento != null) {
            this.id = documento.getId();
            if (documento.getTipoDocumento() != null) {
                this.tipoDocumento = new TipoDocumentoDTO(documento.getTipoDocumento());
            }
            if (documento.getCanalCaptura() != null) {
                this.canalCaptura = new CanalDTO(documento.getCanalCaptura());
            }
            if (documento.getAtributosDocumento() != null) {
                documento.getAtributosDocumento().forEach(atributo -> {
                    this.atributosDocumento.add(new AtributoDocumentoDTO(atributo));
                });
            }
            this.codigoGED = documento.getCodigoGED();
            this.dataHoraCaptura = documento.getDataHoraCaptura();
            this.matricula = documento.getResponsavel();
            this.dataHoraValidade = documento.getDataHoraValidade();
            this.dossieDigital = documento.getDossieDigital();
            this.origemDocumentoEnum = documento.getOrigemDocumento();
            this.formatoConteudo = documento.getFormatoConteudoEnum();
            this.mimetype = documento.getFormatoConteudoEnum() == null ? null : documento.getFormatoConteudoEnum().getMimeType();
            this.binario = binario;
            
            this.analiseOutsourcing = documento.getControlesDocumento().stream()
                    .filter(cd -> cd.getDataHoraRetornoRejeicao() == null)
                    .filter(cd -> (cd.getIndicativoClassificacao().equals(Boolean.TRUE) && cd.getDataHoraRetornoClassificacao() == null)
                                  || (cd.getIndicativoExtracao().equals(Boolean.TRUE)  && cd.getDataHoraRetornoExtracao() == null)
                                  || (cd.getIndicativoAvaliacaoAutenticidade().equals(Boolean.TRUE) && cd.getDataHoraRetornoAvaliacaoAutenticidade() == null)
                                  || (cd.getIndicativoAvaliacaoCadastral().equals(Boolean.TRUE)  && cd.getDataHoraRetornoAvaliacaoCadastral() == null))
                    .findAny().isPresent();
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

    public CanalDTO getCanalCaptura() {
        return canalCaptura;
    }

    public void setCanalCaptura(CanalDTO canalCaptura) {
        this.canalCaptura = canalCaptura;
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

    public FormatoConteudoEnum getFormatoConteudo() {
        return formatoConteudo;
    }

    public void setFormatoConteudo(FormatoConteudoEnum formatoConteudo) {
        this.formatoConteudo = formatoConteudo;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }
    
    public boolean isAnaliseOutsourcing() {
        return analiseOutsourcing;
    }

    public void setAnaliseOutsourcing(boolean analiseOutsourcing) {
        this.analiseOutsourcing = analiseOutsourcing;
    }

    public List<AtributoDocumentoDTO> getAtributosDocumento() {
        return atributosDocumento;
    }

    public void setAtributosDocumento(List<AtributoDocumentoDTO> atributosDocumento) {
        this.atributosDocumento = atributosDocumento;
    }

    public String getBinario() {
        return binario;
    }

    public void setBinario(String binario) {
	this.binario = binario;
    }
}
