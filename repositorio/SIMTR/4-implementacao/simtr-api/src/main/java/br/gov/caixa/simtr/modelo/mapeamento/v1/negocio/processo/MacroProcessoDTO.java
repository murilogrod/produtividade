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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.RelacaoProcesso;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioProcesso;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioProcesso.XML_ROOT_ELEMENT_MACRO_PROCESSO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioProcesso.API_MODEL_V1_MACRO_PROCESSO,
        description = "Objeto utilizado para representar os processos caraterizados como macroprocessos."
)
public class MacroProcessoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(MacroProcessoDTO.class.getName());

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
    private boolean indicadorGeracaoDossie;
    
    @XmlElement(name = ConstantesNegocioProcesso.INDICADOR_INTERFACE)
    @ApiModelProperty(name = ConstantesNegocioProcesso.INDICADOR_INTERFACE, required = true, value = "Indica se o processo deve ser apresentado para criação na interface gráfica.")
    private boolean indicadorInterface;

    @XmlElement(name = ConstantesNegocioProcesso.TIPO_PESSOA)
    @ApiModelProperty(name = ConstantesNegocioProcesso.TIPO_PESSOA, required = true, value = "Indica o tipo de pessoa definido para o processo. F = Fisica | J = Juridica | A = Ambos")
    private TipoPessoaEnum tipoPessoaEnum;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @XmlElement(name = ConstantesNegocioProcesso.PRIORIZADO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.PRIORIZADO, required = false, value = "Indica que o processo esta priorizado e por isso a visão de captura de dossiês para tratamento será diferenciada.")
    private boolean priorizado;
    
    @XmlElement(name = ConstantesNegocioProcesso.UNIDADE_AUTORIZADA)
    @ApiModelProperty(name = ConstantesNegocioProcesso.UNIDADE_AUTORIZADA, required = false, value = "Indica se a unidade do solicitante esta apta a realizar ações neste processo.")
    private Boolean unidadeAutorizada;
    // *********************************************
    @XmlElementWrapper(name = ConstantesNegocioProcesso.PROCESSOS_FILHO)
    @XmlElement(name = ConstantesNegocioProcesso.PROCESSO_FILHO)
    @JsonProperty(value = ConstantesNegocioProcesso.PROCESSOS_FILHO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.PROCESSOS_FILHO, required = false, value = "Lista de processos filho vinculados neste processo.")
    private List<ProcessoDossieDTO> processosFilhoDTO;

    public MacroProcessoDTO() {
        super();
        this.processosFilhoDTO = new ArrayList<>();
    }

    public MacroProcessoDTO(Processo processo, Integer lotacaoAdministrativa, Integer lotacaoFisica) {
        this();
        if (processo != null) {
            this.id = processo.getId();
            this.nome = processo.getNome();
            this.avatar = processo.getAvatar();
            this.indicadorGeracaoDossie = processo.getIndicadorGeracaoDossie();
            this.indicadorInterface = processo.getIndicadorInterface();
            this.tipoPessoaEnum = processo.getTipoPessoa();
            
            if(processo.getUnidadesAutorizadas() != null){
                this.unidadeAutorizada = processo.getUnidadesAutorizadas().stream()
                            .anyMatch(ua -> ua.getUnidade().equals(lotacaoAdministrativa) || ua.getUnidade().equals(lotacaoFisica));
            } else {
                this.unidadeAutorizada = Boolean.FALSE;
            }

            try {
                if (processo.getRelacoesProcessoVinculoPai() != null) {
                    List<RelacaoProcesso> relacoes = Arrays.asList(processo.getRelacoesProcessoVinculoPai().toArray(new RelacaoProcesso[processo.getRelacoesProcessoVinculoPai().size()]));
                    relacoes.sort(Comparator.nullsFirst(Comparator.comparing(RelacaoProcesso::getOrdem, Comparator.nullsFirst(Comparator.naturalOrder()))));
                    relacoes.forEach(relacaoProcesso -> {
                        this.priorizado = relacaoProcesso.getPrioridade() != null;
                        this.processosFilhoDTO.add(new ProcessoDossieDTO(relacaoProcesso.getProcessoFilho(), (relacaoProcesso.getPrioridade() != null), lotacaoAdministrativa, lotacaoFisica));
                    });
                }
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.processosFilhoDTO = null;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isIndicadorGeracaoDossie() {
        return indicadorGeracaoDossie;
    }

    public void setIndicadorGeracaoDossie(boolean indicadorGeracaoDossie) {
        this.indicadorGeracaoDossie = indicadorGeracaoDossie;
    }
    
    public boolean isIndicadorInterface() {
        return indicadorInterface;
    }

    public void setIndicadorInterface(boolean indicadorInterface) {
        this.indicadorInterface = indicadorInterface;
    }

    public TipoPessoaEnum getTipoPessoaEnum() {
        return tipoPessoaEnum;
    }

    public void setTipoPessoaEnum(TipoPessoaEnum tipoPessoaEnum) {
        this.tipoPessoaEnum = tipoPessoaEnum;
    }

    public boolean isPriorizado() {
        return priorizado;
    }

    public void setPriorizado(boolean priorizado) {
        this.priorizado = priorizado;
    }

    public Boolean getUnidadeAutorizada() {
        return unidadeAutorizada;
    }

    public void setUnidadeAutorizada(Boolean unidadeAutorizada) {
        this.unidadeAutorizada = unidadeAutorizada;
    }

    public List<ProcessoDossieDTO> getProcessosFilhoDTO() {
        return processosFilhoDTO;
    }

    public void setProcessosFilhoDTO(List<ProcessoDossieDTO> processosFilhoDTO) {
        this.processosFilhoDTO = processosFilhoDTO;
    }

}
