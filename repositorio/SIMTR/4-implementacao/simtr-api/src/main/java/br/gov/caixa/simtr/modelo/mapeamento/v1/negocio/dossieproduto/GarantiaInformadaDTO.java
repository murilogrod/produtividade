package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto;

import br.gov.caixa.simtr.modelo.entidade.DossieClientePF;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePJ;
import java.io.Serializable;
import java.math.BigDecimal;
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

import br.gov.caixa.simtr.modelo.entidade.GarantiaInformada;
import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.RespostaDossie;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CodigoModalidadeAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CodigoOperacaoAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProduto.XML_ROOT_ELEMENT_GARANTIA_INFORMADA)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
          value = ConstantesNegocioDossieProduto.API_MODEL_V1_GARANTIA_INFORMADA,
          description = "Objeto utilizado para representar o a garantia informada perante um dossiê de produto no retorno as consultas realizadas sob a ótica Apoio ao Negocio.")
public class GarantiaInformadaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(GarantiaInformadaDTO.class.getName());

    @XmlElement(name = ConstantesNegocioDossieProduto.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ID, required = true, value = "Identificador único da garantia informada")
    private Long id;

    @XmlElement(name = ConstantesNegocioDossieProduto.GARANTIA)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.GARANTIA, required = true, value = "Identificador da garantia.")
    private Integer idGarantia;

    @XmlElement(name = ConstantesNegocioDossieProduto.CODIGO_BACEN)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.CODIGO_BACEN, required = true,
                      value = "Codigo de identificação da garantia junto ao BACEN, também utilizado como corporativo do garantia CAIXA")
    @XmlJavaTypeAdapter(value = CodigoOperacaoAdapter.class)
    private Integer codigoBacen;

    @XmlElement(name = ConstantesNegocioDossieProduto.PRODUTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.PRODUTO, required = false, value = "Identificador do produto ao qual a garantia esta relacionada.")
    private Integer idProduto;

    @XmlElement(name = ConstantesNegocioDossieProduto.NOME_GARANTIA)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.NOME_GARANTIA, required = true, value = "Nome da garantia CAIXA")
    private String garantiaNome;

    @XmlJavaTypeAdapter(value = CodigoOperacaoAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProduto.PRODUTO_OPERACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.PRODUTO_OPERACAO, required = false,
                      value = "Codigo da operação corporativo do produto CAIXA vinculado a garantia informada.")
    private Integer produtoOperacao;

    @XmlJavaTypeAdapter(value = CodigoModalidadeAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProduto.PRODUTO_MODALIDADE)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.PRODUTO_MODALIDADE, required = false,
                      value = "Codigo da modalidade do do produto CAIXA vinculado a garantia informada.")
    private Integer produtoModalidade;

    @XmlElement(name = ConstantesNegocioDossieProduto.PRODUTO_NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.PRODUTO_NOME, required = false, value = "Nome do produto CAIXA vinculado a garantia informada.")
    private String produtoNome;

    @XmlElement(name = ConstantesNegocioDossieProduto.VALOR)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.VALOR, required = false, value = "Valor da garantia apresentada.")
    private BigDecimal valor;

    // ************************************
    @XmlElement(name = ConstantesNegocioDossieProduto.INSTANCIA_DOCUMENTO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProduto.INSTANCIAS_DOCUMENTO)
    @JsonProperty(value = ConstantesNegocioDossieProduto.INSTANCIAS_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.INSTANCIAS_DOCUMENTO, required = false,
                      value = "Lista de instancias de documentos vinculadas ao dossiê de produto para a garatia informadacom respectiva indicação das situações aplicadas e do registro documento com demais informações ineretes.")
    private List<InstanciaDocumentoDTO> instanciasDocumentoDTO;

    @XmlElement(name = ConstantesNegocioDossieProduto.DOSSIE_CLIENTE)
    @XmlElementWrapper(name = ConstantesNegocioDossieProduto.DOSSIES_CLIENTE)
    @JsonProperty(value = ConstantesNegocioDossieProduto.DOSSIES_CLIENTE)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.DOSSIES_CLIENTE, required = true,
                      value = "Lista de dossiês de cliente relacionados com a garantia fidejussoria definida na operação")
    private List<DossieClienteDTO> dossiesClienteDTO;

    @XmlElement(name = ConstantesNegocioDossieProduto.RESPOSTA_FORMULARIO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProduto.RESPOSTAS_FORMULARIO)
    @JsonProperty(value = ConstantesNegocioDossieProduto.RESPOSTAS_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.RESPOSTAS_FORMULARIO, required = true,
                      value = "Lista de respostas de formulario relacionadas com o vinculo de garantia informada definida na operação")
    private List<RespostaFormularioDTO> respostasFormularioDTO;

    public GarantiaInformadaDTO() {
        super();
        this.instanciasDocumentoDTO = new ArrayList<>();
        this.dossiesClienteDTO = new ArrayList<>();
        this.respostasFormularioDTO = new ArrayList<>();
    }

    public GarantiaInformadaDTO(GarantiaInformada garantiaInformada) {
        this();
        if (garantiaInformada != null) {
            this.id = garantiaInformada.getId();
            this.valor = garantiaInformada.getValorGarantia();
            if (garantiaInformada.getGarantia() != null) {
                this.idGarantia = garantiaInformada.getGarantia().getId();
                this.codigoBacen = garantiaInformada.getGarantia().getGarantiaBacen();
                this.garantiaNome = garantiaInformada.getGarantia().getNome();
            }
            if (garantiaInformada.getProduto() != null) {
                this.idProduto = garantiaInformada.getProduto().getId();
                this.produtoOperacao = garantiaInformada.getProduto().getOperacao();
                this.produtoModalidade = garantiaInformada.getProduto().getModalidade();
                this.produtoNome = garantiaInformada.getProduto().getNome();
            }
            if (garantiaInformada.getDossiesCliente() != null) {
                garantiaInformada.getDossiesCliente().forEach(dossie -> {
                    if (dossie instanceof DossieClientePF) {
                        this.dossiesClienteDTO.add(new DossieClientePFDTO((DossieClientePF) dossie));
                    } else {
                        this.dossiesClienteDTO.add(new DossieClientePJDTO((DossieClientePJ) dossie));
                    }
                });
            }
            
        }
    }

    public GarantiaInformadaDTO(GarantiaInformada garantiaInformada, Set<InstanciaDocumento> instanciasDocumento, Set<RespostaDossie> respostasDossie) {
        this(garantiaInformada);
        if ((garantiaInformada != null) && (instanciasDocumento != null)) {
            try {
                instanciasDocumento.forEach(instanciaDocumento -> {
                    if ((instanciaDocumento.getGarantiaInformada() != null) && (instanciaDocumento.getGarantiaInformada().equals(garantiaInformada))) {
                        this.instanciasDocumentoDTO.add(new InstanciaDocumentoDTO(instanciaDocumento, Boolean.TRUE));
                    }
                });
            } catch (RuntimeException re) {
                // Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.instanciasDocumentoDTO = null;
            }
        }
        
        if ((garantiaInformada != null) && (respostasDossie != null)) {
            try {
                respostasDossie.forEach(resposta -> {
                    if ((resposta.getGarantiaInformada() != null) && (resposta.getGarantiaInformada().equals(garantiaInformada))) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdGarantia() {
        return idGarantia;
    }

    public void setIdGarantia(Integer idGarantia) {
        this.idGarantia = idGarantia;
    }

    public Integer getCodigoBacen() {
        return codigoBacen;
    }

    public void setCodigoBacen(Integer codigoBacen) {
        this.codigoBacen = codigoBacen;
    }

    public String getGarantiaNome() {
        return garantiaNome;
    }

    public void setGarantiaNome(String garantiaNome) {
        this.garantiaNome = garantiaNome;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public Integer getProdutoOperacao() {
        return produtoOperacao;
    }

    public void setProdutoOperacao(Integer produtoOperacao) {
        this.produtoOperacao = produtoOperacao;
    }

    public Integer getProdutoModalidade() {
        return produtoModalidade;
    }

    public void setProdutoModalidade(Integer produtoModalidade) {
        this.produtoModalidade = produtoModalidade;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public List<InstanciaDocumentoDTO> getInstanciasDocumentoDTO() {
        return instanciasDocumentoDTO;
    }

    public void setInstanciasDocumentoDTO(List<InstanciaDocumentoDTO> instanciasDocumentoDTO) {
        this.instanciasDocumentoDTO = instanciasDocumentoDTO;
    }

    public List<DossieClienteDTO> getDossiesClienteDTO() {
        return dossiesClienteDTO;
    }

    public void setDossiesClienteDTO(List<DossieClienteDTO> dossiesClienteDTO) {
        this.dossiesClienteDTO = dossiesClienteDTO;
    }

    public List<RespostaFormularioDTO> getRespostasFormularioDTO() {
        return respostasFormularioDTO;
    }

    public void setRespostasFormularioDTO(List<RespostaFormularioDTO> respostasFormularioDTO) {
        this.respostasFormularioDTO = respostasFormularioDTO;
    }
}
