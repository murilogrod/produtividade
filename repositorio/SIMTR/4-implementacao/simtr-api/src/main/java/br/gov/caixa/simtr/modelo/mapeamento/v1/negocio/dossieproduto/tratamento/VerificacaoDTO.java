package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.tratamento;

import br.gov.caixa.simtr.modelo.entidade.ChecklistAssociado;
import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.Verificacao;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoTratamento;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.ChecklistDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.InstanciaDocumentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.ParecerDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.ProcessoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Calendar;
import java.util.Objects;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = ConstantesNegocioDossieProdutoTratamento.XML_ROOT_ELEMENT_VERIFICACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoTratamento.API_MODEL_V1_VERIFICACAO,
        description = "Objeto utilizado para representar a verificação realizada no retorno as consultas realizadas sob a ótica Apoio ao Negocio a partir do Dossiê do Produto."
)
public class VerificacaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(VerificacaoDTO.class.getName());

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.IDENTIFICADOR_VERIFICACAO, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.IDENTIFICADOR_VERIFICACAO, value = "Identificador único da verificação.", required = true)
    private Long id;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.CHECKLIST, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.CHECKLIST, value = "Objeto que representa o checklist relacionado com a verificação.", required = true)
    private ChecklistDTO checklistDTO;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.PROCESSO_FASE, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.PROCESSO_FASE, value = "Objeto que representa o processo fase em que a verificação foi realizada.", required = true)
    private ProcessoDTO processoFaseDTO;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.INSTANCIA_DOCUMENTO, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.INSTANCIA_DOCUMENTO, value = "Objeto que representa a instância de documento analisada. Caso o registro seja relacionado a uma verificação não documental esse atributo será nulo", required = false)
    private InstanciaDocumentoDTO instanciaDocumentoDTO;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.DATA_HORA_VERIFICACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.DATA_HORA_VERIFICACAO, value = "Data/Hora de realização da verificação", required = true, example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraVerificacao;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.UNIDADE, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.UNIDADE, value = "CGC da unidade responsável pela verificaçãoo.", required = true)
    private Integer unidade;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.REALIZADA, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.REALIZADA, value = "Indicador de verificação realizada ou não pelo operador. Nos casos de documentos opcionais esse atributo pode ser falso indicando que o operador optou por não realizar a verificação.", required = true)
    private Boolean verificacaoRealizada;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.APROVADA, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.APROVADA, value = "Indicador de verificação aprovada. nos casos em que a verificação não foi realizada esse atributo terá o valor nulo.", required = false)
    private Boolean verificacaoAprovada;

    // *********************************************
    @XmlElementWrapper(name = "parecer_apontamentos")
    @XmlElement(name = "parecer_apontamento")
    @JsonProperty("parecer_apontamentos")
    @JsonInclude(value = Include.NON_EMPTY)
    @ApiModelProperty(name = "parecer_apontamentos", value = "Lista de pareceres (resultados) dos apontamentos vinculados a verificação realizada. Quando a realização não for verificada essa lista será enviada vazia.", required = false)
    private List<ParecerDTO> pareceresDTO;

    public VerificacaoDTO() {
        super();
        this.pareceresDTO = new ArrayList<>();
    }

    public VerificacaoDTO(Verificacao verificacao) {
        this();
        if (verificacao != null) {
            this.id = verificacao.getId();
            this.dataHoraVerificacao = verificacao.getDataHoraVerificacao();
            this.unidade = verificacao.getUnidadeOperador();
            this.verificacaoRealizada = verificacao.getIndicacaoRealizacao();
            //Inicia o valor sem resultado, inclui abaixo apenas de a verificação tiver indicação de aprovação
            this.verificacaoAprovada = null;

            if (verificacaoRealizada) {
                this.verificacaoAprovada = verificacao.getIndicacaoVerificacao();
            }

            if (Objects.nonNull(verificacao.getChecklistAssociado())) {
                ChecklistAssociado checklistAssociado = verificacao.getChecklistAssociado();

                //Captura as informações do checklist
                if (Objects.nonNull(checklistAssociado.getChecklist())) {
                    this.checklistDTO = new ChecklistDTO(checklistAssociado.getChecklist());
                }

                //Captura as informações do fase do processo em que a realização foi verificada
                if (Objects.nonNull(checklistAssociado.getProcessoFase())) {

                    //Não deve carregar as instancias de documentos e respostas da fase da fase neste momento.
                    this.processoFaseDTO = new ProcessoDTO(checklistAssociado.getProcessoFase(), null, null);
                }

                //Captura as informações da instancia de documento se a verificação se tratar de um checklist documental
                if (Objects.nonNull(checklistAssociado.getInstanciaDocumento())) {
                    InstanciaDocumento instanciaDocumento = checklistAssociado.getInstanciaDocumento();
                    this.instanciaDocumentoDTO = new InstanciaDocumentoDTO(instanciaDocumento, Boolean.FALSE);
                }

                try {
                    if (Objects.nonNull(verificacao.getPareceres())) {
                        verificacao.getPareceres().forEach(parecer -> this.pareceresDTO.add(new ParecerDTO(parecer)));
                    }
                } catch (RuntimeException re) {
                    //Lazy Exception ou atributos não carregados
                    LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                    this.pareceresDTO = null;
                }
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChecklistDTO getChecklistDTO() {
        return checklistDTO;
    }

    public void setChecklistDTO(ChecklistDTO checklistDTO) {
        this.checklistDTO = checklistDTO;
    }

    public ProcessoDTO getProcessoFaseDTO() {
        return processoFaseDTO;
    }

    public void setProcessoFaseDTO(ProcessoDTO processoFaseDTO) {
        this.processoFaseDTO = processoFaseDTO;
    }

    public InstanciaDocumentoDTO getInstanciaDocumentoDTO() {
        return instanciaDocumentoDTO;
    }

    public void setInstanciaDocumentoDTO(InstanciaDocumentoDTO instanciaDocumentoDTO) {
        this.instanciaDocumentoDTO = instanciaDocumentoDTO;
    }

    public Calendar getDataHoraVerificacao() {
        return dataHoraVerificacao;
    }

    public void setDataHoraVerificacao(Calendar dataHoraVerificacao) {
        this.dataHoraVerificacao = dataHoraVerificacao;
    }

    public Integer getUnidade() {
        return unidade;
    }

    public void setUnidade(Integer unidade) {
        this.unidade = unidade;
    }

    public Boolean getVerificacaoRealizada() {
        return verificacaoRealizada;
    }

    public void setVerificacaoRealizada(Boolean verificacaoRealizada) {
        this.verificacaoRealizada = verificacaoRealizada;
    }

    public Boolean getVerificacaoAprovada() {
        return verificacaoAprovada;
    }

    public void setVerificacaoAprovada(Boolean verificacaoAprovada) {
        this.verificacaoAprovada = verificacaoAprovada;
    }

    public List<ParecerDTO> getPareceresDTO() {
        return pareceresDTO;
    }

    public void setPareceresDTO(List<ParecerDTO> pareceresDTO) {
        this.pareceresDTO = pareceresDTO;
    }
}
