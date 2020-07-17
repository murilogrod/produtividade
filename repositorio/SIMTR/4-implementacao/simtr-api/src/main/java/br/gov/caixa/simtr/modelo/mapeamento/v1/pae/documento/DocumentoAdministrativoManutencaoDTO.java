package br.gov.caixa.simtr.modelo.mapeamento.v1.pae.documento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElementWrapper;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesPAE;

@XmlRootElement(name = ConstantesPAE.XML_ROOT_ELEMENT_DOCUMENTO_ADMINISTRATIVO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesPAE.API_MODEL_DOCUMENTO_ADMINISTRATIVO_MANUTENCAO,
        description = "Objeto utilizado para representar o Documento Administrativo utilizado nas operações de alteração do documento sob a otica geral do PAE."
)
public class DocumentoAdministrativoManutencaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesPAE.TIPO_DOCUMENTO, required = true)
    @ApiModelProperty(name = ConstantesPAE.TIPO_DOCUMENTO, required = true, value = "Identificação do tipo de documento encaminhado. Para conhecer a lista de tipologia acesse: GET /processoadministrativo/v1/documento/tipologia")
    private Integer codigoTipoDocumento;

    @XmlElement(name = ConstantesPAE.ORIGEM_DOCUMENTO, required = true)
    @ApiModelProperty(name = ConstantesPAE.ORIGEM_DOCUMENTO, required = true, value = "Indica a midia de origem do documento indicada pelo usuário de captura.", notes = "O = Original | S = Copia Simples | C = Autenticado em Cartório | A = Autenticado Administrativamente")
    private OrigemDocumentoEnum origemDocumentoEnum;

    @XmlElement(name = ConstantesPAE.CONFIDENCIAL)
    @ApiModelProperty(name = ConstantesPAE.CONFIDENCIAL, required = true, value = "Indicador se o documento administrativo é confidencial. Nestes casos o documento só terá os seus atributos e conteudos incluidos para visualização se o usuário tiver perfil autorizado.")
    private Boolean confidencial;

    @XmlElement(name = ConstantesPAE.VALIDO)
    @ApiModelProperty(name = ConstantesPAE.VALIDO, required = true, value = "Indicador se o documento administrativo é valido. Quando um documento estiver definido como não valido, o mesmo conterá uma marca sem efeito na exportação. Também só será possivel 'reativar' um documento se o mesmo não estiver substituido")
    private Boolean valido;

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

    public DocumentoAdministrativoManutencaoDTO() {
        super();
        this.atributosDocumento = new ArrayList<>();
    }

    public Integer getCodigoTipoDocumento() {
        return codigoTipoDocumento;
    }

    public void setCodigoTipoDocumento(Integer codigoTipoDocumento) {
        this.codigoTipoDocumento = codigoTipoDocumento;
    }

    public OrigemDocumentoEnum getOrigemDocumentoEnum() {
        return origemDocumentoEnum;
    }

    public void setOrigemDocumentoEnum(OrigemDocumentoEnum origemDocumentoEnum) {
        this.origemDocumentoEnum = origemDocumentoEnum;
    }

    public Boolean getConfidencial() {
        return confidencial;
    }

    public void setConfidencial(Boolean confidencial) {
        this.confidencial = confidencial;
    }

    public Boolean isValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
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
}