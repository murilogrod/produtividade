package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.ElementoConteudo;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioProcesso;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioProcesso.XML_ROOT_ELEMENT_ELEMENTO_CONTEUDO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioProcesso.API_MODEL_V1_ELEMENTO_CONTEUDO,
        description = "Objeto utilizado para representar o elemento de conteudo vinculado a um produto ou a uma etapa do processo (fase)"
)
public class ElementoConteudoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ElementoConteudoDTO.class.getName());

    @XmlElement(name = ConstantesNegocioProcesso.IDENTIFICADOR_ELEMENTO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.IDENTIFICADOR_ELEMENTO, required = true, value = "Identificador único do elemento de conteudo esperado na definição de uma etapa do processo ou vinculação de um produto")
    private Long id;

    @XmlElement(name = ConstantesNegocioProcesso.IDENTIFICADOR_ELEMENTO_VINCULADOR)
    @ApiModelProperty(name = ConstantesNegocioProcesso.IDENTIFICADOR_ELEMENTO_VINCULADOR, required = true, value = "Identificador único do elemento de conteudo vinculador do elemento de conteudo, viabilizando montar uma hierarquia (arvore) de elementos dependentes")
    private Long idElementoVinculador;

    @XmlElement(name = ConstantesNegocioProcesso.OBRIGATORIO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.OBRIGATORIO, required = true, value = "Indica para se este elemento tem obrigatoriedade de carga na submissão do criação/atualização do dossiê de produto")
    private Boolean obrigatorio;

    @XmlElement(name = ConstantesNegocioProcesso.QUANTIDADE_OBRIGATORIOS)
    @ApiModelProperty(name = ConstantesNegocioProcesso.QUANTIDADE_OBRIGATORIOS, required = false, value = "Nos casos em que o elemento de conteudo trata-se um agrupador (pasta) indica quantos elementos minimos são necessários daquele item")
    private Integer quantidadeObrigatorios;

    @XmlElement(name = ConstantesNegocioProcesso.LABEL)
    @ApiModelProperty(name = ConstantesNegocioProcesso.LABEL, required = false, value = "Determina o nome do label a ser exibido nos casos de elementos agrupadores (pasta). Quando o elemento for folha, deverá utilizado o nome definido pelo objeto tipo de documento e este campo será nulo.")
    private String label;

    @XmlElement(name = ConstantesNegocioProcesso.NOME_ELEMENTO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.NOME_ELEMENTO, required = false, value = "Nome do campo utilizado para fins de identificação programatica perante a interface na montagem da arvore de elementos conteudos.")
    private String nomeCampo;

    @XmlElement(name = ConstantesNegocioProcesso.EXPRESSAO_INTERFACE)
    @ApiModelProperty(name = ConstantesNegocioProcesso.EXPRESSAO_INTERFACE, required = false, value = "Determina uma expressão a ser utilizada pela interface para definir uma ação sobre este elemento de conteudo, como por exemplo, ser apresentado ou ocultado na interface. Trata-se de uma AngularExpression.")
    private String expressao;

    @XmlElement(name = ConstantesNegocioProcesso.TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.TIPO_DOCUMENTO, required = false, value = "Identifica o tipo de documento a ser carregado nos casos de elementos folha e deverá ser utilizado essa estrutura para detalhar o elemento de conteudo. Quando o elemento for agrupador (pasta), este elemento será nulo.")
    private TipoDocumentoDTO tipoDocumentoDTO;

    public ElementoConteudoDTO() {
        super();
    }

    public ElementoConteudoDTO(ElementoConteudo elementoConteudo) {
        this();
        this.id = elementoConteudo.getId();
        this.obrigatorio = elementoConteudo.getObrigatorio();
        this.quantidadeObrigatorios = elementoConteudo.getQuantidadeObrigatorios();
        this.nomeCampo = elementoConteudo.getNomeCampo();
        this.expressao = elementoConteudo.getExpressao();
        this.label = elementoConteudo.getNomeElemento();

        try {
            if (elementoConteudo.getTipoDocumento() != null) {
                this.tipoDocumentoDTO = new TipoDocumentoDTO(elementoConteudo.getTipoDocumento());
            }
        } catch (RuntimeException re) {
            //Lazy Exception ou atributos não carregados
            LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
            this.tipoDocumentoDTO = null;
        }

        try {
            if (elementoConteudo.getElementoVinculador() != null) {
                this.idElementoVinculador = elementoConteudo.getElementoVinculador().getId();
            }
        } catch (RuntimeException re) {
            //Lazy Exception ou atributos não carregados
            LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
            this.idElementoVinculador = null;
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdElementoVinculador() {
        return idElementoVinculador;
    }

    public void setIdElementoVinculador(Long idElementoVinculador) {
        this.idElementoVinculador = idElementoVinculador;
    }

    public Boolean getObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(Boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public Integer getQuantidadeObrigatorios() {
        return quantidadeObrigatorios;
    }

    public void setQuantidadeObrigatorios(Integer quantidadeObrigatorios) {
        this.quantidadeObrigatorios = quantidadeObrigatorios;
    }

    public String getNomeCampo() {
        return nomeCampo;
    }

    public void setNomeCampo(String nomeCampo) {
        this.nomeCampo = nomeCampo;
    }

    public String getExpressao() {
        return expressao;
    }

    public void setExpressao(String expressao) {
        this.expressao = expressao;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public TipoDocumentoDTO getTipoDocumentoDTO() {
        return tipoDocumentoDTO;
    }

    public void setTipoDocumentoDTO(TipoDocumentoDTO tipoDocumentoDTO) {
        this.tipoDocumentoDTO = tipoDocumentoDTO;
    }
}
