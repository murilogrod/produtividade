package br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.extracaodados;

import br.gov.caixa.simtr.modelo.entidade.Conteudo;
import br.gov.caixa.simtr.modelo.entidade.ControleDocumento;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesRetaguardaExtracaoDados;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlRootElement(name = ConstantesRetaguardaExtracaoDados.XML_ROOT_ELEMENT_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesRetaguardaExtracaoDados.API_MODEL_DOCUMENTO,
        description = "Objeto utilizado para representar o documento na captura pelo operador de backoffice para realizar as atividades de classificação, extração de dados e avaliação avaliação de autenticidade."
)
public class DocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesRetaguardaExtracaoDados.CODIGO_CONTROLE, required = true)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.CODIGO_CONTROLE, required = true, value = "Código utilizado para identificar o documento no retorno do serviço de extração de dados.")
    private String codigoControle;

    @XmlElement(name = ConstantesRetaguardaExtracaoDados.TIPO_DOCUMENTO, required = false)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.TIPO_DOCUMENTO, required = false, value = "Tipo do documento definido para o documento. Caso o tipo de documento não seja definido o serviço será solicitado incluindo a atividade de classificação documental.")
    private TipoDocumentoDTO tipoDocumento;

    @XmlElement(name = ConstantesRetaguardaExtracaoDados.EXECUTA_CLASSIFICACAO, required = true)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.EXECUTA_CLASSIFICACAO, required = true, value = "Indicador de execução da atividade de classificação documental.")
    private boolean executaClassificacao;

    @XmlElement(name = ConstantesRetaguardaExtracaoDados.MIMETYPE, required = true)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.MIMETYPE, required = true, value = "Mimetype referente ao conteudo do documento.")
    private String mimetype;

    @XmlElement(name = ConstantesRetaguardaExtracaoDados.BINARIO, required = true)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.BINARIO, required = true, value = "Conteudos que representam o binario do documento em formato base64.")
    private String binario;

    // *********************************************
    @XmlElement(name = ConstantesRetaguardaExtracaoDados.ATRIBUTO)
    @XmlElementWrapper(name = ConstantesRetaguardaExtracaoDados.ATRIBUTOS)
    @JsonProperty(value = ConstantesRetaguardaExtracaoDados.ATRIBUTOS)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.ATRIBUTOS, required = false, value = "Lista dos atributos já extraidos e associados ao documento.")
    private List<AtributoDocumentoDTO> atributosDocumentoDTO;

    public DocumentoDTO() {
        super();
        this.atributosDocumentoDTO = new ArrayList<>();
    }

    public DocumentoDTO(Documento documento) {
        this();
        if (documento != null) {
            if (Objects.nonNull(documento.getTipoDocumento())) {
                TipoDocumento tipoDocumentoDocumento = new TipoDocumento();
                tipoDocumentoDocumento.setId(documento.getTipoDocumento().getId());
                tipoDocumentoDocumento.setNome(documento.getTipoDocumento().getNome());
                this.tipoDocumento = new TipoDocumentoDTO(tipoDocumentoDocumento);
            }

            if (Objects.nonNull(documento.getControlesDocumento())) {
                documento.getControlesDocumento().stream()
                        .max(Comparator.comparing(ControleDocumento::getDataHoraEnvio))
                        .ifPresent(cd -> {
                            this.codigoControle = cd.getCodigoFornecedor();
                            this.executaClassificacao = cd.getIndicativoClassificacao();
                        });
            }

            if (Objects.nonNull(documento.getAtributosDocumento())) {
                documento.getAtributosDocumento().forEach(atributo -> {
                    this.atributosDocumentoDTO.add(new AtributoDocumentoDTO(atributo));
                });
            }

            if (Objects.nonNull(documento.getConteudos())) {
                Conteudo c = documento.getConteudos().stream()
                        .sorted(Comparator.comparing(Conteudo::getOrdem))
                        .findFirst().get();
                this.binario = c.getBase64();

                if (Objects.nonNull(documento.getFormatoConteudoEnum())) {
                    this.mimetype = documento.getFormatoConteudoEnum().getMimeType();
                }
            }
        }
    }

    public String getCodigoControle() {
        return codigoControle;
    }

    public void setCodigoControle(String codigoControle) {
        this.codigoControle = codigoControle;
    }

    public TipoDocumentoDTO getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumentoDTO tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public boolean isExecutaClassificacao() {
        return executaClassificacao;
    }

    public void setExecutaClassificacao(boolean executaClassificacao) {
        this.executaClassificacao = executaClassificacao;
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
}
