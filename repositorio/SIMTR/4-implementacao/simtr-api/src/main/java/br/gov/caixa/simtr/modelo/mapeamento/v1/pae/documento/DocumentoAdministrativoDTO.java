package br.gov.caixa.simtr.modelo.mapeamento.v1.pae.documento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.gov.caixa.simtr.modelo.entidade.Conteudo;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DocumentoAdministrativo;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullISOAdapter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElementWrapper;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesPAE;

@XmlRootElement(name = ConstantesPAE.XML_ROOT_ELEMENT_DOCUMENTO_ADMINISTRATIVO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesPAE.API_MODEL_DOCUMENTO_ADMINISTRATIVO,
        description = "Objeto utilizado para representar o Documento Administrativo no retorno as consultas realizadas sob a otica geral do PAE."
)
public class DocumentoAdministrativoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(DocumentoAdministrativoDTO.class.getName());

    @XmlElement(name = ConstantesPAE.ID, required = true)
    @ApiModelProperty(name = ConstantesPAE.ID, required = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY, value = "Valor que representa o identificador do Documento Administrativo.")
    private Long id;

    @XmlElement(name = ConstantesPAE.TIPO_DOCUMENTO, required = true)
    @ApiModelProperty(name = ConstantesPAE.TIPO_DOCUMENTO, required = true, value = "Elemento que representa o tipo de documento vinculado com o documento administrativo")
    private TipoDocumentoDTO tipoDocumento;

    @XmlElement(name = ConstantesPAE.MIME_TYPE, required = true)
    @ApiModelProperty(name = ConstantesPAE.MIME_TYPE, required = true, value = "Indica o mime type do conteudo do documento.", allowableValues = "image/bmp, image/jpg, image/jpeg, application/pdf, image/png,image/tiff")
    private String mimeType;

    @XmlJavaTypeAdapter(value = CalendarFullISOAdapter.class)
    @XmlElement(name = ConstantesPAE.DATA_HORA_CAPTURA, required = true)
    @ApiModelProperty(name = ConstantesPAE.DATA_HORA_CAPTURA, required = true, value = "Indica a data e hora de captura do documento", example = "yyyy-MM-dd HH:mm:ss")
    private Calendar dataHoraCaptura;

    @XmlElement(name = ConstantesPAE.MATRICULA_CAPTURA, required = true)
    @ApiModelProperty(name = ConstantesPAE.MATRICULA_CAPTURA, required = true, value = "Indica a matricula do usuario que realizou a captura do documento", example = "C000000")
    private String matricula;

    @XmlElement(name = ConstantesPAE.ORIGEM_DOCUMENTO, required = true)
    @ApiModelProperty(name = ConstantesPAE.ORIGEM_DOCUMENTO, required = true, value = "Indica a midia de origem do documento indicada pelo usuário de captura.", notes = "O = Original | S = Copia Simples | C = Autenticado em Cartório | A = Autenticado Administrativamente")
    private OrigemDocumentoEnum origemDocumento;

    @XmlElement(name = ConstantesPAE.DOCUMENTO_SUBSTITUTO)
    @ApiModelProperty(name = ConstantesPAE.DOCUMENTO_SUBSTITUTO, required = true, value = "Atributo que representa o registro do Documento Administrativo que substituiu o documento original.")
    private Long codigoDocumentoSubstituto;

    @XmlElement(name = ConstantesPAE.VALIDO)
    @ApiModelProperty(name = ConstantesPAE.VALIDO, required = true, value = "Indicador se o documento administrativo ainda é valido e não foi substituido por outro. Verdadeito sempre que o documento substituto existir.")
    private Boolean valido;

    @XmlElement(name = ConstantesPAE.CONFIDENCIAL)
    @ApiModelProperty(name = ConstantesPAE.CONFIDENCIAL, required = true, value = "Indicador se o documento administrativo é confidencial. Nestes casos o documento só terá o seu conteudo incluido nos atributos de documento se o usuário tiver perfil autorizado.")
    private Boolean confidencial;

    @XmlElement(name = ConstantesPAE.DESCRICAO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesPAE.DESCRICAO_DOCUMENTO, required = true, value = "Descrição do livre vinculada ao Documento Administrativo.")
    private String descricaoDocumento;

    @XmlElement(name = ConstantesPAE.JUSTIFICATIVA_SUBSTITUICAO)
    @ApiModelProperty(name = ConstantesPAE.JUSTIFICATIVA_SUBSTITUICAO, required = true, value = "Justificativa da substituição do Documento Administrativo.")
    private String justificativaSubstituicao;

    @JsonProperty(value = ConstantesPAE.ATRIBUTOS_DOCUMENTO)
    @XmlElement(name = ConstantesPAE.ATRIBUTO_DOCUMENTO, required = false)
    @XmlElementWrapper(name = ConstantesPAE.ATRIBUTOS_DOCUMENTO)
    @ApiModelProperty(name = ConstantesPAE.ATRIBUTOS_DOCUMENTO, required = false, value = "Atributos do documento.")
    private List<AtributoDocumentoDTO> atributosDocumento;

    @JsonProperty(value = ConstantesPAE.CONTEUDOS)
    @XmlElement(name = ConstantesPAE.CONTEUDO, required = false)
    @XmlElementWrapper(name = ConstantesPAE.CONTEUDOS)
    @ApiModelProperty(name = ConstantesPAE.CONTEUDOS, required = false, value = "Conteudos do documento em formato base64.")
    private List<String> conteudos;

    public DocumentoAdministrativoDTO() {
        super();
        this.atributosDocumento = new ArrayList<>();
        this.conteudos = new ArrayList<>();
    }

    public DocumentoAdministrativoDTO(DocumentoAdministrativo documentoAdministrativo) {
        this();
        if (documentoAdministrativo != null) {
            this.id = documentoAdministrativo.getId();
            this.valido = documentoAdministrativo.getValido();
            this.confidencial = documentoAdministrativo.getConfidencial();
            this.descricaoDocumento = documentoAdministrativo.getDescricao();
            this.justificativaSubstituicao = documentoAdministrativo.getJustificativaSubstituicao();
            try {
                if (documentoAdministrativo.getDocumentoSubstituto() != null) {
                    this.codigoDocumentoSubstituto = documentoAdministrativo.getDocumentoSubstituto().getId();
                }
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.codigoDocumentoSubstituto = null;
            }
            if (documentoAdministrativo.getDocumento() != null) {
                Documento documento = documentoAdministrativo.getDocumento();
                this.dataHoraCaptura = documento.getDataHoraCaptura();
                this.mimeType = documento.getFormatoConteudoEnum() != null ? documento.getFormatoConteudoEnum().getMimeType() : null;
                this.matricula = documento.getResponsavel();
                this.origemDocumento = documento.getOrigemDocumento();
                try {
                    this.tipoDocumento = new TipoDocumentoDTO(documento.getTipoDocumento());
                } catch (RuntimeException re) {
                    //Lazy Exception ou atributos não carregados
                    LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                    this.tipoDocumento = null;
                }
                try {
                    documento.getAtributosDocumento().forEach(ad -> this.atributosDocumento.add(new AtributoDocumentoDTO(ad)));
                } catch (RuntimeException re) {
                    //Lazy Exception ou atributos não carregados
                    LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                    this.atributosDocumento = null;
                }
                try {
                    documento.getConteudos().stream().sorted(Comparator.comparing(Conteudo::getOrdem)).forEachOrdered(c -> this.conteudos.add(c.getBase64()));
                } catch (RuntimeException re) {
                    //Lazy Exception ou atributos não carregados
                    LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                    this.conteudos = null;
                }
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

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
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

    public OrigemDocumentoEnum getOrigemDocumento() {
        return origemDocumento;
    }

    public void setOrigemDocumento(OrigemDocumentoEnum origemDocumento) {
        this.origemDocumento = origemDocumento;
    }

    public Long getCodigoDocumentoSubstituto() {
        return codigoDocumentoSubstituto;
    }

    public void setCodigoDocumentoSubstituto(Long codigoDocumentoSubstituto) {
        this.codigoDocumentoSubstituto = codigoDocumentoSubstituto;
    }

    public Boolean isValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
    }

    public Boolean isConfidencial() {
        return confidencial;
    }

    public void setConfidencial(Boolean confidencial) {
        this.confidencial = confidencial;
    }

    public String getDescricaoDocumento() {
        return descricaoDocumento;
    }

    public void setDescricaoDocumento(String descricaoDocumento) {
        this.descricaoDocumento = descricaoDocumento;
    }

    public String getJustificativaSubstituicao() {
        return justificativaSubstituicao;
    }

    public void setJustificativaSubstituicao(String justificativaSubstituicao) {
        this.justificativaSubstituicao = justificativaSubstituicao;
    }

    public List<AtributoDocumentoDTO> getAtributosDocumento() {
        return atributosDocumento;
    }

    public void setAtributosDocumento(List<AtributoDocumentoDTO> atributosDocumento) {
        this.atributosDocumento = atributosDocumento;
    }

    public List<String> getConteudos() {
        return conteudos;
    }

    public void setConteudos(List<String> conteudos) {
        this.conteudos = conteudos;
    }
}
