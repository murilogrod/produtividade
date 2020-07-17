package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo;

import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.Garantia;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioProcesso;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CodigoModalidadeAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CodigoOperacaoAdapter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement(name = ConstantesNegocioProcesso.XML_ROOT_ELEMENT_PRODUTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioProcesso.API_MODEL_V1_PRODUTO,
        description = "Objeto utilizado para representar as definições do produto associados ao processo"
)
public class ProdutoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ProdutoDTO.class.getName());

    @XmlElement(name = ConstantesNegocioProcesso.ID)
    @ApiModelProperty(name = ConstantesNegocioProcesso.ID, required = true, value = "Identificador único do produto")
    private Integer id;

    @XmlElement(name = ConstantesNegocioProcesso.CODIGO_OPERACAO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.CODIGO_OPERACAO, required = true, value = "Código de operação corporativo do produto CAIXA")
    @XmlJavaTypeAdapter(value = CodigoOperacaoAdapter.class)
    private Integer operacao;

    @XmlElement(name = ConstantesNegocioProcesso.CODIGO_MODALIDADE)
    @ApiModelProperty(name = ConstantesNegocioProcesso.CODIGO_MODALIDADE, required = true, value = "Código da modalidade do do produto CAIXA. Este valor indica uma subdivisão do produto.")
    @XmlJavaTypeAdapter(value = CodigoModalidadeAdapter.class)
    private Integer modalidade;

    @XmlElement(name = ConstantesNegocioProcesso.NOME)
    @ApiModelProperty(name = ConstantesNegocioProcesso.NOME, required = true, value = "Nome do produto CAIXA")
    private String nome;

    //***************************
    @XmlElementWrapper(name = ConstantesNegocioProcesso.CAMPOS_FORMULARIO)
    @XmlElement(name = ConstantesNegocioProcesso.CAMPO_FORMULARIO)
    @JsonProperty(value = ConstantesNegocioProcesso.CAMPOS_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.CAMPOS_FORMULARIO, required = false, value = "Lista de objetos que representam os campos de formulario utilizados na fase do processo.")
    private List<CampoFormularioDTO> camposFormularioDTO;

    @XmlElementWrapper(name = ConstantesNegocioProcesso.ELEMENTOS_CONTEUDO)
    @XmlElement(name = ConstantesNegocioProcesso.ELEMENTO_CONTEUDO)
    @JsonProperty(value = ConstantesNegocioProcesso.ELEMENTOS_CONTEUDO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.ELEMENTOS_CONTEUDO, required = false, value = "Lista de objetos que representam os documentos necessários a serem carregados quando o produto é vinculado ao dossiê.")
    private List<ElementoConteudoDTO> elementosConteudoDTO;

    @XmlElementWrapper(name = ConstantesNegocioProcesso.GARANTIAS_VINCULADAS)
    @XmlElement(name = ConstantesNegocioProcesso.GARANTIA_VINCULADA)
    @JsonProperty(value = ConstantesNegocioProcesso.GARANTIAS_VINCULADAS)
    @ApiModelProperty(name = ConstantesNegocioProcesso.GARANTIAS_VINCULADAS, required = false, value = "Lista de garantias possíveis de indicação para subsidiar a contratação do produto.")
    private List<GarantiaDTO> garantiasDTO;

    public ProdutoDTO() {
        super();
        this.camposFormularioDTO = new ArrayList<>();
        this.elementosConteudoDTO = new ArrayList<>();
        this.garantiasDTO = new ArrayList<>();
    }

    public ProdutoDTO(Produto produto, Processo processo) {
        this();
        if (produto != null) {
            this.id = produto.getId();
            this.operacao = produto.getOperacao();
            this.modalidade = produto.getModalidade();
            this.nome = produto.getNome();

            try {
                if (produto.getCamposFormulario() != null) {
                    produto.getCamposFormulario().stream()
                            .filter(cf -> processo.equals(cf.getProcesso()))
                            .sorted(Comparator.comparing(CampoFormulario::getOrdemApresentacao))
                            .forEachOrdered(campoFormulario -> this.camposFormularioDTO.add(new CampoFormularioDTO(campoFormulario)));
                }
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.camposFormularioDTO = null;
            }

            try {
                if (produto.getElementosConteudo() != null) {
                    produto.getElementosConteudo().forEach(elementoConteudo -> this.elementosConteudoDTO.add(new ElementoConteudoDTO(elementoConteudo)));
                }
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.elementosConteudoDTO = null;
            }

            try {
                if (produto.getGarantias() != null) {
                    produto.getGarantias().forEach(garantia -> {
                        if (garantia.getDocumentosGarantia() != null) {
                            Map<Garantia, GarantiaDTO> mapaGarantias = new HashMap<>();

                            garantia.getDocumentosGarantia().stream()
                                    .forEach(documentoGarantia -> {

                                        TipoDocumento tipoDocumento = null;
                                        FuncaoDocumental funcaoDocumental = null;

                                        if (documentoGarantia.getProcesso().equals(processo)) {
                                            try {
                                                if (documentoGarantia.getFuncaoDocumental() != null) {
                                                    funcaoDocumental = documentoGarantia.getFuncaoDocumental();
                                                }
                                            } catch (RuntimeException re) {
                                                //Lazy Exception ou atributos não carregados
                                                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                                                funcaoDocumental = null;
                                            }

                                            try {
                                                if (documentoGarantia.getTipoDocumento() != null) {
                                                    tipoDocumento = documentoGarantia.getTipoDocumento();
                                                }
                                            } catch (RuntimeException re) {
                                                //Lazy Exception ou atributos não carregados
                                                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                                                tipoDocumento = null;
                                            }
                                        }

                                        //Verifica se já existe um objeto mapeado tendo a garantia associada ao registro em analise como chave
                                        //Caso não localize, cria um novo registro representando a garantia
                                        //Caso localize, adiciona o tipo de documento ou função documental ao registro a ser enviado no retorno
                                        GarantiaDTO garantiaDTO = mapaGarantias.get(documentoGarantia.getGarantia());
                                        if (garantiaDTO == null) {
                                            garantiaDTO = new GarantiaDTO(garantia, tipoDocumento, funcaoDocumental, processo);
                                        } else {
                                            if (tipoDocumento != null) {
                                                garantiaDTO.addTipoDocumentoDTO(new TipoDocumentoDTO(tipoDocumento));
                                            } else if (funcaoDocumental != null) {
                                                garantiaDTO.addFuncaoDocumentalDTO(new FuncaoDocumentalDTO(funcaoDocumental));
                                            }
                                        }
                                        mapaGarantias.put(garantia, garantiaDTO);

                                    });

                            //Adiciona a lista de registros montados representando as garantias com as coleções de documentos
                            this.garantiasDTO.addAll(mapaGarantias.values());
                        }
                    });
                }
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.garantiasDTO = null;
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

    public List<CampoFormularioDTO> getCamposFormularioDTO() {
        return camposFormularioDTO;
    }

    public void setCamposFormularioDTO(List<CampoFormularioDTO> camposFormularioDTO) {
        this.camposFormularioDTO = camposFormularioDTO;
    }

    public List<ElementoConteudoDTO> getElementosConteudoDTO() {
        return elementosConteudoDTO;
    }

    public void setElementosConteudoDTO(List<ElementoConteudoDTO> elementosConteudoDTO) {
        this.elementosConteudoDTO = elementosConteudoDTO;
    }

    public List<GarantiaDTO> getGarantiasDTO() {
        return garantiasDTO;
    }

    public void setGarantiasDTO(List<GarantiaDTO> garantiasDTO) {
        this.garantiasDTO = garantiasDTO;
    }
}
