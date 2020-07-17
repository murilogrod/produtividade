package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.ProdutoDossie;
import br.gov.caixa.simtr.modelo.entidade.RespostaDossie;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CodigoModalidadeAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CodigoOperacaoAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProduto.XML_ROOT_ELEMENT_PRODUTO_CONTRATADO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
          value = ConstantesNegocioDossieProduto.API_MODEL_V1_PRODUTO_CONTRATADO,
          description = "Objeto utilizado para representar o produto contratado e vinculado a um dossiê de produto no retorno as consultas realizadas sob a ótica Apoio ao Negocio a partir do Dossiê do Produto.")
public class ProdutoContratadoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ProdutoContratadoDTO.class.getName());

    @XmlElement(name = ConstantesNegocioDossieProduto.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ID, required = true, value = "Identificador do produto contratado junto a tabela de cadastra de produtos")
    private Integer id;

    @XmlJavaTypeAdapter(value = CodigoOperacaoAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProduto.CODIGO_OPERACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.CODIGO_OPERACAO, required = true, value = "Codigo da operação corporativo do produto CAIXA")
    private Integer operacao;

    @XmlJavaTypeAdapter(value = CodigoModalidadeAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProduto.CODIGO_MODALIDADE)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.CODIGO_MODALIDADE, required = true, value = "Codigo da modalidade do do produto CAIXA")
    private Integer modalidade;

    @XmlElement(name = ConstantesNegocioDossieProduto.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.NOME, required = true, value = "Nome do produto CAIXA")
    private String nome;

    // ************************************
    @XmlElement(name = ConstantesNegocioDossieProduto.INSTANCIA_DOCUMENTO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProduto.INSTANCIAS_DOCUMENTO)
    @JsonProperty(value = ConstantesNegocioDossieProduto.INSTANCIAS_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.INSTANCIAS_DOCUMENTO, required = false,
                      value = "Lista de instancias de documentos vinculadas ao dossiê de produto com respectiva indicação das situações aplicadas e do registro documento com demais informações ineretes.")
    private List<InstanciaDocumentoDTO> instanciasDocumentoDTO;

    @XmlElement(name = ConstantesNegocioDossieProduto.RESPOSTA_FORMULARIO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProduto.RESPOSTAS_FORMULARIO)
    @JsonProperty(value = ConstantesNegocioDossieProduto.RESPOSTAS_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.RESPOSTAS_FORMULARIO, required = true,
                      value = "Lista de respostas de formulario relacionadas com o vinculo de garantia informada definida na operação")
    private List<RespostaFormularioDTO> respostasFormularioDTO;

    public ProdutoContratadoDTO() {
        super();
        this.instanciasDocumentoDTO = new ArrayList<>();
        this.respostasFormularioDTO = new ArrayList<>();
    }

    public ProdutoContratadoDTO(ProdutoDossie produtoDossie) {
        this();
        if (produtoDossie != null) {
            if (produtoDossie.getProduto() != null) {
                this.id = produtoDossie.getProduto().getId();
                this.operacao = produtoDossie.getProduto().getOperacao();
                this.modalidade = produtoDossie.getProduto().getModalidade();
                this.nome = produtoDossie.getProduto().getNome();
            }
        }
    }

    public ProdutoContratadoDTO(ProdutoDossie produtoDossie, Set<InstanciaDocumento> instanciasDocumento, Set<RespostaDossie> respostasDossie) {
        this(produtoDossie);
        if ((produtoDossie != null) && (instanciasDocumento != null)) {
            try {
                instanciasDocumento.forEach(instanciaDocumento -> {
                    if (instanciaDocumento.getElementoConteudo() != null && instanciaDocumento.getElementoConteudo().getProduto() != null
                        && instanciaDocumento.getElementoConteudo().getProduto().equals(produtoDossie.getProduto())) {
                        this.instanciasDocumentoDTO.add(new InstanciaDocumentoDTO(instanciaDocumento, Boolean.TRUE));
                    }
                });
            } catch (RuntimeException re) {
                // Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.instanciasDocumentoDTO = null;
            }
        }
        
        if ((produtoDossie != null) && (respostasDossie != null)) {
            try {
                respostasDossie.forEach(resposta -> {
                    if (resposta.getProdutoDossie() != null && resposta.getProdutoDossie().equals(produtoDossie)) {
                        this.respostasFormularioDTO.add(new RespostaFormularioDTO(resposta));
                    }
                });
            } catch (RuntimeException re) {
                // Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.respostasFormularioDTO = null;
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOperacao() {
        return operacao;
    }

    public void setOperacao(Integer operacao) {
        this.operacao = operacao;
    }

    public Integer getModalidade() {
        return modalidade;
    }

    public void setModalidade(Integer modalidade) {
        this.modalidade = modalidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<InstanciaDocumentoDTO> getInstanciasDocumentoDTO() {
        return instanciasDocumentoDTO;
    }

    public void setInstanciasDocumentoDTO(List<InstanciaDocumentoDTO> instanciasDocumentoDTO) {
        this.instanciasDocumentoDTO = instanciasDocumentoDTO;
    }

    public List<RespostaFormularioDTO> getRespostasFormularioDTO() {
        return respostasFormularioDTO;
    }

    public void setRespostasFormularioDTO(List<RespostaFormularioDTO> respostasFormularioDTO) {
        this.respostasFormularioDTO = respostasFormularioDTO;
    }    
}
