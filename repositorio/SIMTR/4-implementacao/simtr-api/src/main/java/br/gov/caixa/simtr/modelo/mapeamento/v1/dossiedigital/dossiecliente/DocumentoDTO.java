package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalDossieCliente;
import br.gov.caixa.simtr.util.ConstantesUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalDossieCliente.XML_ROOT_ELEMENT_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesDossieDigitalDossieCliente.API_MODEL_V1_DOCUMENTO,
          description = "Objeto utilizado para representar o documento vinculado ao dossiê do cliente no retorno as consultas realizadas no contexto do Dossiê do Cliente")
public class DocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.ID)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.ID, required = true, value = "Identificador único do documento na plataforma do SIMTR")
    private Long id;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.TIPO_DOCUMENTO, required = true, value = "Tipo do documento categorizado")
    private TipoDocumentoDTO tipoDocumento;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.IDENTIFICADOR_SIECM)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.IDENTIFICADOR_SIECM, required = true, value = "Identificador do documento perante a solução de GED (SIECM)")
    private String codigoGED;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.OBJECT_STORE)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.OBJECT_STORE, required = true, value = "Object Store do SIECM que deverá ser utilizado para captura do documento")
    private String objectStore;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalDossieCliente.DATA_HORA_CAPTURA)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.DATA_HORA_CAPTURA, required = true, value = "Data/Hora de captura do documento", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraCaptura;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.MATRICULA_CAPTURA)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.MATRICULA_CAPTURA, required = false, value = "Matricula do usuário responsável pela captura do documento", example = "c999999")
    private String matricula;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalDossieCliente.DATA_HORA_VALIDADE)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.DATA_HORA_VALIDADE, required = false, value = "Data/Hora da validade do documento para novos negocios",
                      example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraValidade;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.DOSSIE_DIGITAL)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.DOSSIE_DIGITAL, required = true, value = "Indicador de documento avaliado pelo fluxo do Dossiê Digital")
    private Boolean dossieDigital;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.ORIGEM_DOCUMENTO)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.ORIGEM_DOCUMENTO, required = true, value = "Identificador do tipo de midia origem do documento digitalizado")
    private OrigemDocumentoEnum origemDocumentoEnum;
    
    @XmlElement(name = ConstantesDossieDigitalDossieCliente.ANALISE_OUTSOURCING)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.ANALISE_OUTSOURCING, value = "Indica se o documento esta sob analise do servico de outsourcing documental.", required = false)
    private boolean analiseOutsourcing;
    
    @XmlElement(name = ConstantesDossieDigitalDossieCliente.MIMETYPE)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.MIMETYPE, required = true, value = "Identificador do mimetype definido para o binario")
    private String mimetype;
    
    // *********************************************
    public DocumentoDTO() {
        super();
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
        this.dataHoraCaptura = documento.getDataHoraCaptura();
        this.matricula = documento.getResponsavel();
        this.dataHoraValidade = documento.getDataHoraValidade();
        this.dossieDigital = documento.getDossieDigital();
        this.origemDocumentoEnum = documento.getOrigemDocumento();
        
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
}
