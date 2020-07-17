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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.RespostaDossie;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProduto.XML_ROOT_ELEMENT_PROCESSO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProduto.API_MODEL_V1_PROCESSO,
        description = "Objeto utilizado para representar o Processo de vinculação e das fases do dossiê no retorno as consultas realizadas sob a ótica Apoio ao Negocio a partir do Dossiê de Produto."
)
public class ProcessoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ProcessoDTO.class.getName());

    @XmlElement(name = ConstantesNegocioDossieProduto.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ID, required = true, value = "Codigo de identificação do processo.")
    private Integer id;

    @XmlElement(name = ConstantesNegocioDossieProduto.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.NOME, required = true, value = "Nome de identificação do processo.")
    private String nome;

    // ************************************
    @JsonInclude(value = Include.NON_EMPTY)
    @XmlElement(name = ConstantesNegocioDossieProduto.INSTANCIA_DOCUMENTO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProduto.INSTANCIAS_DOCUMENTO)
    @JsonProperty(value = ConstantesNegocioDossieProduto.INSTANCIAS_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.INSTANCIAS_DOCUMENTO, required = false, value = "Lista de instancias de documentos vinculadas ao dossiê durante este registro de processo com respectiva indicação das situações aplicadas e do registro documento com demais informações ineretes.")
    private List<InstanciaDocumentoDTO> instanciasDocumentoDTO;

    @JsonInclude(value = Include.NON_EMPTY)
    @XmlElement(name = ConstantesNegocioDossieProduto.RESPOSTA_FORMULARIO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProduto.RESPOSTAS_FORMULARIO)
    @JsonProperty(value = ConstantesNegocioDossieProduto.RESPOSTAS_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.RESPOSTAS_FORMULARIO, required = false, value = "Lista de respostas fornecidas durante o processo fase de vinculação.")
    private List<RespostaFormularioDTO> respostasFormularioDTO;

    public ProcessoDTO() {
        super();
        this.instanciasDocumentoDTO = new ArrayList<>();
        this.respostasFormularioDTO = new ArrayList<>();
    }

    public ProcessoDTO(Processo processo) {
        this();
        if (processo != null) {
            this.id = processo.getId();
            this.nome = processo.getNome();
        }
    }

    public ProcessoDTO(Processo processo, Set<InstanciaDocumento> instanciasDocumento, Set<RespostaDossie> respostasDossie) {
        this(processo);
        if ((processo != null) && (instanciasDocumento != null)) {
            try {
                instanciasDocumento.forEach(instanciaDocumento -> {
                    if (instanciaDocumento.getElementoConteudo() != null && instanciaDocumento.getElementoConteudo().getProcesso() != null
                            && instanciaDocumento.getElementoConteudo().getProcesso().equals(processo)) {
                        this.instanciasDocumentoDTO.add(new InstanciaDocumentoDTO(instanciaDocumento, Boolean.TRUE));
                    }
                });
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.instanciasDocumentoDTO = null;
            }
        }

        if ((processo != null) && (respostasDossie != null)) {
            try {
                respostasDossie.forEach(respostaDossie -> {
                    if (respostaDossie.getCampoFormulario() != null && respostaDossie.getCampoFormulario().getProcessoFase() != null
                            && respostaDossie.getCampoFormulario().getProcessoFase().equals(processo)) {
                        this.respostasFormularioDTO.add(new RespostaFormularioDTO(respostaDossie));
                    }
                });
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
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
