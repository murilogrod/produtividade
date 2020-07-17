package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.documento;

import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.AtributoDocumentoDTO;
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
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente.TipoDocumentoDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalDocumento.XML_ROOT_ELEMENT_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesDossieDigitalDocumento.API_MODEL_V1_DOCUMENTO,
          description = "Objeto utilizado para representar o documento vinculado ao dossiê do cliente no retorno as consultas realizadas no contexto do Dossiê do Cliente")
public class DocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalDocumento.ID)
    @ApiModelProperty(name = ConstantesDossieDigitalDocumento.ID, required = true, value = "Identificador único do documento na plataforma do SIMTR")
    private Long id;

    @XmlElement(name = ConstantesDossieDigitalDocumento.TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesDossieDigitalDocumento.TIPO_DOCUMENTO, required = true, value = "Tipo do documento categorizado")
    private TipoDocumentoDTO tipoDocumento;

    @XmlElement(name = ConstantesDossieDigitalDocumento.IDENTIFICADOR_SIECM)
    @ApiModelProperty(name = ConstantesDossieDigitalDocumento.IDENTIFICADOR_SIECM, required = true, value = "Identificador do documento perante a solução de GED (SIECM)")
    private String codigoGED;

    @XmlElement(name = ConstantesDossieDigitalDocumento.OBJECT_STORE)
    @ApiModelProperty(name = ConstantesDossieDigitalDocumento.OBJECT_STORE, required = true, value = "Object Store do SIECM que deverá ser utilizado para captura do documento")
    private String objectStore;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalDocumento.DATA_HORA_CAPTURA)
    @ApiModelProperty(name = ConstantesDossieDigitalDocumento.DATA_HORA_CAPTURA, required = true, value = "Data/Hora de captura do documento", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraCaptura;

    @XmlElement(name = ConstantesDossieDigitalDocumento.MATRICULA_CAPTURA)
    @ApiModelProperty(name = ConstantesDossieDigitalDocumento.MATRICULA_CAPTURA, required = false, value = "Matricula do usuário responsável pela captura do documento", example = "c999999")
    private String matricula;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalDocumento.DATA_HORA_VALIDADE)
    @ApiModelProperty(name = ConstantesDossieDigitalDocumento.DATA_HORA_VALIDADE, required = false, value = "Data/Hora da validade do documento para novos negocios",
                      example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraValidade;

    @XmlElement(name = ConstantesDossieDigitalDocumento.DOSSIE_DIGITAL)
    @ApiModelProperty(name = ConstantesDossieDigitalDocumento.DOSSIE_DIGITAL, required = true, value = "Indicador de documento avaliado pelo fluxo do Dossiê Digital")
    private Boolean dossieDigital;

    @XmlElement(name = ConstantesDossieDigitalDocumento.ORIGEM_DOCUMENTO)
    @ApiModelProperty(name = ConstantesDossieDigitalDocumento.ORIGEM_DOCUMENTO, required = true, value = "Identificador do tipo de midia origem do documento digitalizado")
    private OrigemDocumentoEnum origemDocumentoEnum;
    
    @XmlElement(name = ConstantesDossieDigitalDocumento.ANALISE_OUTSOURCING)
    @ApiModelProperty(name = ConstantesDossieDigitalDocumento.ANALISE_OUTSOURCING, value = "Indica se o documento esta sob analise do servico de outsourcing documental.", required = false)
    private boolean analiseOutsourcing;
    
    @XmlElement(name = ConstantesDossieDigitalDocumento.MIMETYPE)
    @ApiModelProperty(name = ConstantesDossieDigitalDocumento.MIMETYPE, required = true, value = "Identificador do mimetype definido para o binario")
    private String mimetype;
    
    @XmlElement(name = ConstantesDossieDigitalDocumento.BINARIO)
    @ApiModelProperty(name = ConstantesDossieDigitalDocumento.BINARIO, required = true, value = "Binario em formato Base64 associado ao documento. Este campo não estará presente ")
    private String binario;
    
    // *********************************************
    @XmlElement(name = ConstantesDossieDigitalDocumento.ATRIBUTO_DOCUMENTO)
    @XmlElementWrapper(name = ConstantesDossieDigitalDocumento.ATRIBUTOS_DOCUMENTO)
    @JsonProperty(value = ConstantesDossieDigitalDocumento.ATRIBUTOS_DOCUMENTO)
    @ApiModelProperty(name = ConstantesDossieDigitalDocumento.ATRIBUTOS_DOCUMENTO, value = "Lista de atributos extraidos do documento", required = false)
    private List<AtributoDocumentoDTO> atributosDocumento;

    public DocumentoDTO() {
        super();
        this.atributosDocumento= new ArrayList<>();
    }

    public DocumentoDTO(Documento documento) {
        this();
        this.id = documento.getId();
        if (documento.getTipoDocumento() != null) {
            this.tipoDocumento = new TipoDocumentoDTO(documento.getTipoDocumento());
        }
        if(documento.getCodigoSiecmReuso() != null) {
            this.codigoGED = documento.getCodigoSiecmReuso();
            this.objectStore = ConstantesUtil.SIECM_OS_REUSO;
            this.mimetype = documento.getFormatoConteudoTratadoEnum().getMimeType();
        } else if (documento.getCodigoSiecmTratado() != null) {
            this.codigoGED = documento.getCodigoSiecmTratado();
            this.objectStore = ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL;
            this.mimetype = documento.getFormatoConteudoTratadoEnum().getMimeType();
        } else if (documento.getCodigoSiecmCaixa() != null) {
            this.codigoGED = documento.getCodigoSiecmCaixa();
            this.objectStore = ConstantesUtil.SIECM_OS_CAIXA;
            this.mimetype = documento.getFormatoConteudoEnum().getMimeType();
        } else {
            this.codigoGED = documento.getCodigoGED();
            this.objectStore = ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL;
            this.mimetype = documento.getFormatoConteudoEnum().getMimeType();
        }
        if (documento.getAtributosDocumento() != null) {
            documento.getAtributosDocumento().forEach(atributo -> {
                this.atributosDocumento.add(new AtributoDocumentoDTO(atributo));
            });
        }
        this.dataHoraCaptura = documento.getDataHoraCaptura();
        this.matricula = documento.getResponsavel();
        this.dataHoraValidade = documento.getDataHoraValidade();
        this.dossieDigital = documento.getDossieDigital();
        this.origemDocumentoEnum = documento.getOrigemDocumento();
        this.binario = documento.getConteudos().stream().map(c -> c.getBase64()).findFirst().orElse(null);
        
        this.analiseOutsourcing = documento.getControlesDocumento().stream()
                .filter(cd -> cd.getDataHoraRetornoRejeicao() == null)
                .filter(cd -> (cd.getIndicativoClassificacao().equals(Boolean.TRUE) && cd.getDataHoraRetornoClassificacao() == null)
                              || (cd.getIndicativoExtracao().equals(Boolean.TRUE)  && cd.getDataHoraRetornoExtracao() == null)
                              || (cd.getIndicativoAvaliacaoAutenticidade().equals(Boolean.TRUE) && cd.getDataHoraRetornoAvaliacaoAutenticidade() == null)
                              || (cd.getIndicativoAvaliacaoCadastral().equals(Boolean.TRUE)  && cd.getDataHoraRetornoAvaliacaoCadastral() == null))
                .findAny().isPresent();
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

    public String getCodigoGED() {
        return codigoGED;
    }

    public void setCodigoGED(String codigoGED) {
        this.codigoGED = codigoGED;
    }

    public String getObjectStore() {
        return objectStore;
    }

    public void setObjectStore(String objectStore) {
        this.objectStore = objectStore;
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

    public boolean isAnaliseOutsourcing() {
        return analiseOutsourcing;
    }

    public void setAnaliseOutsourcing(boolean analiseOutsourcing) {
        this.analiseOutsourcing = analiseOutsourcing;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getBinario() {
        return binario;
    }

    public void setBinario(String binario) {
        this.binario = binario;
    }

    public List<AtributoDocumentoDTO> getAtributosDocumento() {
        return atributosDocumento;
    }

    public void setAtributosDocumento(List<AtributoDocumentoDTO> atributosDocumento) {
        this.atributosDocumento = atributosDocumento;
    }
}
