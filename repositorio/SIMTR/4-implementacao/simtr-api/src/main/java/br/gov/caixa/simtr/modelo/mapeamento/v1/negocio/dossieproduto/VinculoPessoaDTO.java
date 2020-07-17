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

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.DossieClientePF;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePJ;
import br.gov.caixa.simtr.modelo.entidade.DossieClienteProduto;
import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.RespostaDossie;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProduto.XML_ROOT_ELEMENT_VINCULO_PESSOA)  
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProduto.API_MODEL_V1_VINCULO_PESSOA,
        description = "Objeto utilizado para representar o vinculo de um dossiê de cliente no retorno as consultas realizadas sob a ótica Apoio ao Negocio a partir do Dossiê do Produto."
)
public class VinculoPessoaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(VinculoPessoaDTO.class.getName());

    @XmlElement(name = ConstantesNegocioDossieProduto.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ID, required = true, value = "Identificador do vinculo existente entre o dossiê de cliente e o dossiê de produto.")
    private Long identificadorDossieClienteProduto;

    @XmlElement(name = ConstantesNegocioDossieProduto.TIPO_RELACIONAMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.TIPO_RELACIONAMENTO, required = true, value = "Identifica o tipo de relacionamento exercido pelo dossiê cliente perante do dossiê de produto.")
    private TipoRelacionamentoDTO tipoRelacionamentoDossieEnum;

    @XmlElement(name = ConstantesNegocioDossieProduto.DOSSIE_CLIENTE)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.DOSSIE_CLIENTE, required = true, value = "Identifica o dossiiê de cliente oriundo da vinculação perante o dossiê de produto.")
    private DossieClienteDTO dossieClienteDTO;

    @XmlElement(name = ConstantesNegocioDossieProduto.DOSSIE_CLIENTE_RELACIONADO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.DOSSIE_CLIENTE_RELACIONADO, required = false, value = "Identifica outro dossiê de cliente para o qual o tipo de vinculo tem relação perante o dossiê de produto. Ex: Nos casos de AVALISTA, este atributo representa o dossiê de cliente AVALIZADO.")
    private DossieClienteDTO dossieClienteRelacionadoDTO;

    @XmlElement(name = ConstantesNegocioDossieProduto.SEQUENCIA_TITULARIDADE)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.SEQUENCIA_TITULARIDADE, required = false, value = "Identifica a sequencia de titularidade para os casos de tipo de relacionamento com este fim.")
    private Integer sequenciaTitularidade;

    // ************************************
    @XmlElement(name = ConstantesNegocioDossieProduto.INSTANCIA_DOCUMENTO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProduto.INSTANCIAS_DOCUMENTO)
    @JsonProperty(value = ConstantesNegocioDossieProduto.INSTANCIAS_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.INSTANCIAS_DOCUMENTO, required = false, value = "Lista de instancias de documentos vinculadas ao dossiê de produto com respectiva indicação das situações aplicadas e do registro documento com demais informações inerentes.")
    private List<InstanciaDocumentoDTO> instanciasDocumentoDTO;
    
    @XmlElement(name = ConstantesNegocioDossieProduto.RESPOSTA_FORMULARIO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProduto.RESPOSTAS_FORMULARIO)
    @JsonProperty(value = ConstantesNegocioDossieProduto.RESPOSTAS_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.RESPOSTAS_FORMULARIO, required = true, value = "Lista de respostas de formulario relacionadas com o vinculo de garantia informada definida na operação")
    private List<RespostaFormularioDTO> respostasFormularioDTO;

    public VinculoPessoaDTO() {
        super();
        this.instanciasDocumentoDTO = new ArrayList<>();
        this.respostasFormularioDTO = new ArrayList<>();
    }

    public VinculoPessoaDTO(DossieClienteProduto dossieClienteProduto) {
        this();
        if (dossieClienteProduto != null) {
            this.identificadorDossieClienteProduto = dossieClienteProduto.getId();
            this.tipoRelacionamentoDossieEnum = new TipoRelacionamentoDTO(dossieClienteProduto.getTipoRelacionamento());
            this.sequenciaTitularidade = dossieClienteProduto.getSequenciaTitularidade();
            if (dossieClienteProduto.getDossieCliente() != null) {
                if (DossieClientePF.class.equals(dossieClienteProduto.getDossieCliente().getClass())) {
                    this.dossieClienteDTO = new DossieClientePFDTO((DossieClientePF) dossieClienteProduto.getDossieCliente());
                } else if (DossieClientePJ.class.equals(dossieClienteProduto.getDossieCliente().getClass())) {
                    this.dossieClienteDTO = new DossieClientePJDTO((DossieClientePJ) dossieClienteProduto.getDossieCliente());
                }
            }

            if (dossieClienteProduto.getDossieClienteRelacionado() != null) {
                if (DossieClientePF.class.equals(dossieClienteProduto.getDossieClienteRelacionado().getClass())) {
                    this.dossieClienteRelacionadoDTO = new DossieClientePFDTO((DossieClientePF) dossieClienteProduto.getDossieClienteRelacionado());
                } else if (DossieClientePJ.class.equals(dossieClienteProduto.getDossieClienteRelacionado().getClass())) {
                    this.dossieClienteRelacionadoDTO = new DossieClientePJDTO((DossieClientePJ) dossieClienteProduto.getDossieClienteRelacionado());
                }
            }
        }
    }

    public VinculoPessoaDTO(DossieClienteProduto dossieClienteProduto, Set<InstanciaDocumento> instanciasDocumento, Set<RespostaDossie> respostasDossie) {
        this(dossieClienteProduto);
        if ((dossieClienteProduto != null) && (instanciasDocumento != null)) {
            try {
                instanciasDocumento.forEach(instanciaDocumento -> {
                    if ((instanciaDocumento.getDossieClienteProduto() != null) && (instanciaDocumento.getDossieClienteProduto().equals(dossieClienteProduto))) {
                        this.instanciasDocumentoDTO.add(new InstanciaDocumentoDTO(instanciaDocumento, Boolean.TRUE));
                    }
                });
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.instanciasDocumentoDTO = null;
            }
        }
        
        if ((dossieClienteProduto != null) && (respostasDossie != null)) {
            try {
                respostasDossie.forEach(resposta -> {
                    if ((resposta.getDossieClienteProduto() != null) && (resposta.getDossieClienteProduto().equals(dossieClienteProduto))) {
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

    public Long getIdentificadorDossieClienteProduto() {
        return identificadorDossieClienteProduto;
    }

    public void setIdentificadorDossieClienteProduto(Long identificadorDossieClienteProduto) {
        this.identificadorDossieClienteProduto = identificadorDossieClienteProduto;
    }

    public TipoRelacionamentoDTO getTipoRelacionamentoDossieEnum() {
        return tipoRelacionamentoDossieEnum;
    }

    public void setTipoRelacionamentoDossieEnum(TipoRelacionamentoDTO tipoRelacionamentoDossieEnum) {
        this.tipoRelacionamentoDossieEnum = tipoRelacionamentoDossieEnum;
    }

    public DossieClienteDTO getDossieClienteDTO() {
        return dossieClienteDTO;
    }

    public void setDossieClienteDTO(DossieClienteDTO dossieClienteDTO) {
        this.dossieClienteDTO = dossieClienteDTO;
    }

    public DossieClienteDTO getDossieClienteRelacionadoDTO() {
        return dossieClienteRelacionadoDTO;
    }

    public void setDossieClienteRelacionadoDTO(DossieClienteDTO dossieClienteRelacionadoDTO) {
        this.dossieClienteRelacionadoDTO = dossieClienteRelacionadoDTO;
    }

    public Integer getSequenciaTitularidade() {
        return sequenciaTitularidade;
    }

    public void setSequenciaTitularidade(Integer sequenciaTitularidade) {
        this.sequenciaTitularidade = sequenciaTitularidade;
    }

    public List<InstanciaDocumentoDTO> getInstanciasDocumentoDTO() {
        return instanciasDocumentoDTO;
    }

    public void setInstanciasDocumentoDTO(List<InstanciaDocumentoDTO> instanciasDocumentoDTO) {
        this.instanciasDocumentoDTO = instanciasDocumentoDTO;
    }
}
