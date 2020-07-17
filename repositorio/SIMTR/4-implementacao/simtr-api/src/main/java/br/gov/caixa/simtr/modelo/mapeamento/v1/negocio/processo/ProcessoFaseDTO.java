package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.RelacaoProcesso;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioProcesso;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

@XmlRootElement(name = ConstantesNegocioProcesso.XML_ROOT_ELEMENT_PROCESSO_FASE)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioProcesso.API_MODEL_V1_PROCESSO_FASE,
        description = "Objeto utilizado para representar o Processo Fase do dossiê de produto no retorno as consultas realizadas sob a ótica Apoio ao Negocio."
)
public class ProcessoFaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ProcessoFaseDTO.class.getName());

    @XmlElement(name = ConstantesNegocioProcesso.ID)
    @ApiModelProperty(name = ConstantesNegocioProcesso.ID, required = true, value = "Codigo de identificação do processo.")
    private Integer id;

    @XmlElement(name = ConstantesNegocioProcesso.NOME)
    @ApiModelProperty(name = ConstantesNegocioProcesso.NOME, required = true, value = "Nome de identificação do processo.")
    private String nome;

    @XmlElement(name = ConstantesNegocioProcesso.AVATAR)
    @ApiModelProperty(name = ConstantesNegocioProcesso.AVATAR, required = false, value = "Identificação do nome da icone que representa o processo.")
    private String avatar;

    @XmlElement(name = ConstantesNegocioProcesso.GERA_DOSSIE)
    @ApiModelProperty(name = ConstantesNegocioProcesso.GERA_DOSSIE, required = true, value = "Indica se o processo pode gerar dossiê.")
    private Boolean indicadorGeracaoDossie;

    @XmlElement(name = ConstantesNegocioProcesso.SEQUENCIA)
    @ApiModelProperty(name = ConstantesNegocioProcesso.SEQUENCIA, required = false, value = "Indica a sequencia que o processo ocupa no fluxo de execução nos casos de processo fase.")
    private Integer sequencia;

    @XmlElement(name = ConstantesNegocioProcesso.ORIENTACAO_USUARIO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.ORIENTACAO_USUARIO, required = false, value = "Informação textual utilizada para apresentar ao usuário o que deve ser feito nesta fase da operação.")
    private String orientacao;

    // *********************************************
    @XmlElementWrapper(name = ConstantesNegocioProcesso.CAMPOS_FORMULARIO)
    @XmlElement(name = ConstantesNegocioProcesso.CAMPO_FORMULARIO)
    @JsonProperty(value = ConstantesNegocioProcesso.CAMPOS_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.CAMPOS_FORMULARIO, required = false, value = "Lista de objetos que representam os campos de formulario utilizados na fase do processo.")
    private List<CampoFormularioDTO> camposFormularioDTO;

    @XmlElementWrapper(name = ConstantesNegocioProcesso.ELEMENTOS_CONTEUDO)
    @XmlElement(name = ConstantesNegocioProcesso.ELEMENTO_CONTEUDO)
    @JsonProperty(value = ConstantesNegocioProcesso.ELEMENTOS_CONTEUDO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.ELEMENTOS_CONTEUDO, required = false, value = "Lista de objetos que representam os documentos necessarios a serem carregados na fase do processo.")
    private List<ElementoConteudoDTO> elementosConteudoDTO;

    @XmlElementWrapper(name = ConstantesNegocioProcesso.CHECKLISTS)
    @XmlElement(name = ConstantesNegocioProcesso.CHECKLIST)
    @JsonProperty(value = ConstantesNegocioProcesso.CHECKLISTS)
    @ApiModelProperty(name = ConstantesNegocioProcesso.CHECKLISTS, required = false, value = "Lista de checklists associados ao processo fase. Esses checklists tratam-se de verificações não documentais ue devem ser realizadas no momento de verificação da operação.")
    private List<ChecklistDTO> checklistsDTO;

    public ProcessoFaseDTO() {
        super();
        this.camposFormularioDTO = new ArrayList<>();
        this.elementosConteudoDTO = new ArrayList<>();
        this.checklistsDTO = new ArrayList<>();
    }

    public ProcessoFaseDTO(Processo processo, Processo processoPai) {
        this();
        if (processo != null) {
            this.id = processo.getId();
            this.nome = processo.getNome();
            this.avatar = processo.getAvatar();
            this.indicadorGeracaoDossie = processo.getIndicadorGeracaoDossie();
            this.orientacao = processo.getOrientacao();

            try {
                if (processo.getCamposFormularioFase() != null) {
                    processo.getCamposFormularioFase().stream().sorted(Comparator.comparing(CampoFormulario::getOrdemApresentacao))
                            .forEachOrdered(campoFormulario -> this.camposFormularioDTO.add(new CampoFormularioDTO(campoFormulario)));
                }
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.camposFormularioDTO = null;
            }

            try {
                if (processo.getElementosConteudo() != null) {
                    processo.getElementosConteudo()
                            .forEach(elementoConteudo -> this.elementosConteudoDTO.add(new ElementoConteudoDTO(elementoConteudo)));
                }
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.elementosConteudoDTO = null;
            }

            try {
                if (Objects.nonNull(processo.getVinculacoesChecklistsFase())) {
                    processo.getVinculacoesChecklistsFase().stream()
                            .filter(vinculo -> vinculo.getProcessoDossie().equals(processoPai))
                            .forEach(vinculo -> this.checklistsDTO.add(new ChecklistDTO(vinculo)));
                }
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.elementosConteudoDTO = null;
            }

            if (processo.getRelacoesProcessoVinculoPai() != null) {
                List<RelacaoProcesso> relacoes = Arrays.asList(processo.getRelacoesProcessoVinculoPai().toArray(new RelacaoProcesso[processo.getRelacoesProcessoVinculoPai().size()]));
                relacoes.sort(Comparator.nullsFirst(Comparator.comparing(RelacaoProcesso::getOrdem, Comparator.nullsFirst(Comparator.naturalOrder()))));
                relacoes.forEach(relacaoProcesso -> {
                    this.sequencia = relacaoProcesso.getOrdem();
                });
            }
        }
    }

    public ProcessoFaseDTO(Processo processo, Integer sequencia, Processo processoPai) {
        this(processo, processoPai);
        this.sequencia = sequencia;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getIndicadorGeracaoDossie() {
        return indicadorGeracaoDossie;
    }

    public void setIndicadorGeracaoDossie(Boolean indicadorGeracaoDossie) {
        this.indicadorGeracaoDossie = indicadorGeracaoDossie;
    }

    public Integer getSequencia() {
        return sequencia;
    }

    public void setSequencia(Integer sequencia) {
        this.sequencia = sequencia;
    }

    public String getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(String orientacao) {
        this.orientacao = orientacao;
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

    public List<ChecklistDTO> getChecklistsDTO() {
        return checklistsDTO;
    }

    public void setChecklistsDTO(List<ChecklistDTO> checklistsDTO) {
        this.checklistsDTO = checklistsDTO;
    }
}
