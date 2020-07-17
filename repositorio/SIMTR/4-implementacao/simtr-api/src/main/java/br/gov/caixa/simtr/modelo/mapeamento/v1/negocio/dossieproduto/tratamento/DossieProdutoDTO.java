package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.tratamento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.ProcessoFaseDossie;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDossie;
import br.gov.caixa.simtr.modelo.enumerator.CanalCaixaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CGCUnidadeAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoTratamento;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.GarantiaInformadaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.InstanciaDocumentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.ProcessoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.ProdutoContratadoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.RespostaFormularioDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.SituacaoDossieDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.VinculoPessoaDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoTratamento.XML_ROOT_ELEMENT_DOSSIE_PRODUTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoTratamento.API_MODEL_V1_DOSSIE_PRODUTO,
        description = "Objeto utilizado para representar o dossiê no retorno as consultas realizadas sob a ótica Apoio ao Negocio a partir do Dossiê do Produto."
)
public class DossieProdutoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(DossieProdutoDTO.class.getName());

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.ALTERACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.ALTERACAO, required = true, value = "Indica se o dossiê foi capturado em modo somente leitura (false) ou com permissão de alteração (true).")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean alteracao;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.ID, required = true, value = "Identificador do dossiê de produto.")
    private Long id;

    @XmlJavaTypeAdapter(value = CGCUnidadeAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.UNIDADE_CRIACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.UNIDADE_CRIACAO, required = true, value = "CGC de identificação da unidade de criação do dossiê de produto.")
    private Integer unidadeCriacao;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CGCUnidadeAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.UNIDADE_PRIORIZADO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.UNIDADE_PRIORIZADO, required = false, value = "CGC de identificação da unidade de priorização para tratamento do dossiê de produto.")
    private Integer unidadePriorizado;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.MATRICULA_PRIORIZADO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.MATRICULA_PRIORIZADO, required = false, value = "CGC de identificação da unidade de priorização para tratamento do dossiê de produto.")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String matriculaPriorizado;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.PESO_PRIORIDADE)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.PESO_PRIORIDADE, required = false, value = "Valor que indica o peso atribuido a priorização para tratamento do dossiê de produto. Quanto maior o peso, maior será a prioridade atribuida.")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Integer pesoPrioridade;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.CANAL_CAIXA)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.CANAL_CAIXA, required = true, value = "Identificador do canal fisico de origem do dossiê de produto.")
    private CanalCaixaEnum canalCaixaEnum;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.DATA_HORA_FINALIZACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.DATA_HORA_FINALIZACAO, required = false, value = "Data/Hora de finalização do ciclo de vida do dossiê de produto.", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraFinalizacao;

    @XmlJavaTypeAdapter(value = CGCUnidadeAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.UNIDADE)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoTratamento.UNIDADES_TRATAMENTO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoTratamento.UNIDADES_TRATAMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.UNIDADES_TRATAMENTO, required = false, value = "Lista contendo CGCs das unidades definidas para tratamento neste momento para o dossiê de produto.")
    private List<Integer> unidadesTratamento;

    //************************************
    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.SITUACAO_ATUAL)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.SITUACAO_ATUAL, value = "Identificação da situação atual em que se encontra o dossiê de produto")
    private String situacaoAtual;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.DATA_HORA_SITUACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.DATA_HORA_SITUACAO, value = "Data/Hora de atribuição da situação atual em que se encontra o dossiê de produto", required = true, example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraSituacao;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.SITUACAO_DOSSIE)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoTratamento.HISTORICO_SITUACOES)
    @JsonProperty(ConstantesNegocioDossieProdutoTratamento.HISTORICO_SITUACOES)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.HISTORICO_SITUACOES, required = true, value = "Lista de situações que compõem o hitorico relacionado ao dossiê de produto. A lista é ordenada em ordem cronologica pela data de inclusão da situação.")
    private List<SituacaoDossieDTO> situacoesDossieDTO;

    // ***********************************
    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.PROCESSO_DOSSIE)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.PROCESSO_DOSSIE, required = true, value = "Nome do processo originador do dossiê de produto.")
    private ProcessoDTO processoDossieDTO;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.PROCESSO_FASE)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.PROCESSO_FASE, required = true, value = "Nome do processo que identifica a fase atual do dossiê de produto.")
    private ProcessoDTO processoFaseDTO;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.PRODUTO_CONTRATADO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoTratamento.PRODUTOS_CONTRATADOS)
    @JsonProperty(value = ConstantesNegocioDossieProdutoTratamento.PRODUTOS_CONTRATADOS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.PRODUTOS_CONTRATADOS, required = false, value = "Lista de produtos contratados vinculados ao dossiê de produto")
    private List<ProdutoContratadoDTO> produtosContratadosDTO;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.VINCULO_PESSOA)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoTratamento.VINCULOS_PESSOAS)
    @JsonProperty(value = ConstantesNegocioDossieProdutoTratamento.VINCULOS_PESSOAS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.VINCULOS_PESSOAS, required = false, value = "Lista de dossiês de cliente vinculados ao dossiê de produto com a respectiva identificação do tipo de vinculo.")
    private List<VinculoPessoaDTO> vinculosPessoasDTO;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.GARANTIA_INFORMADA)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoTratamento.GARANTIAS_INFORMADAS)
    @JsonProperty(value = ConstantesNegocioDossieProdutoTratamento.GARANTIAS_INFORMADAS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.GARANTIAS_INFORMADAS, required = false, value = "Lista de garantias informadas vinculadas ao dossiê de produto com respectiva indicação da garantia e o produto especifico associado quando for o caso.")
    private List<GarantiaInformadaDTO> garantiasInformadasDTO;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.INSTANCIA_DOCUMENTO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoTratamento.INSTANCIAS_DOCUMENTO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoTratamento.INSTANCIAS_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.INSTANCIAS_DOCUMENTO, required = false, value = "Lista de instancias de documentos vinculadas ao dossiê de produto com respectiva indicação das situações aplicadas e do registro documento com demais informações ineretes.")
    private List<InstanciaDocumentoDTO> instanciasDocumentoDTO;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.RESPOSTA_FORMULARIO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoTratamento.RESPOSTAS_FORMULARIO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoTratamento.RESPOSTAS_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.RESPOSTAS_FORMULARIO, required = false, value = "Lista de respostas fornecidas durante o ciclo de vida do dossiê de produto com respectiva indicação do processo fase de vinculação.")
    private List<RespostaFormularioDTO> respostasFormularioDTO;

    @XmlElement(name = ConstantesNegocioDossieProdutoTratamento.VERIFICACAO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoTratamento.VERIFICACOES)
    @JsonProperty(value = ConstantesNegocioDossieProdutoTratamento.VERIFICACOES)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoTratamento.VERIFICACOES, required = false, value = "Lista de verificacoes realizadas durante o ciclo de vida do dossiê de produto com respectiva indicação do processo fase de vinculação.")
    private List<VerificacaoDTO> verificacoesDTO;

    public DossieProdutoDTO() {
        super();
        this.unidadesTratamento = new ArrayList<>();
        this.produtosContratadosDTO = new ArrayList<>();
        this.vinculosPessoasDTO = new ArrayList<>();
        this.situacoesDossieDTO = new ArrayList<>();
        this.garantiasInformadasDTO = new ArrayList<>();
        this.instanciasDocumentoDTO = new ArrayList<>();
        this.respostasFormularioDTO = new ArrayList<>();
        this.verificacoesDTO = new ArrayList<>();
    }

    public DossieProdutoDTO(DossieProduto dossieProduto, boolean modoAlteracao) {
        this();
        this.alteracao = modoAlteracao;
        if (dossieProduto != null) {
            this.id = dossieProduto.getId();
            this.unidadeCriacao = dossieProduto.getUnidadeCriacao();
            this.unidadePriorizado = dossieProduto.getUnidadePriorizado();
            this.matriculaPriorizado = dossieProduto.getMatriculaPriorizado();
            this.pesoPrioridade = dossieProduto.getPesoPrioridade();
            this.canalCaixaEnum = dossieProduto.getCanal().getCanalCaixa();
            this.dataHoraFinalizacao = dossieProduto.getDataHoraFinalizacao();
            if (dossieProduto.getUnidadesTratamento() != null) {
                dossieProduto.getUnidadesTratamento()
                        .forEach(unidadeTratamento -> this.unidadesTratamento.add(unidadeTratamento.getUnidade()));
            }

            if (dossieProduto.getProcesso() != null) {
                this.processoDossieDTO = new ProcessoDTO(dossieProduto.getProcesso(), dossieProduto.getInstanciasDocumento(), dossieProduto.getRespostasDossie());
            }
            if (dossieProduto.getProcessosFaseDossie() != null) {
                ProcessoFaseDossie processoFaseAtual = dossieProduto.getProcessosFaseDossie().stream()
                        .max(Comparator.comparing(ProcessoFaseDossie::getId)).get();
                this.processoFaseDTO = new ProcessoDTO(processoFaseAtual.getProcessoFase(), dossieProduto.getInstanciasDocumento(), dossieProduto.getRespostasDossie());
            }

            if (dossieProduto.getProdutosDossie() != null) {
                try {
                    dossieProduto.getProdutosDossie()
                            .forEach(produtoDossie -> this.produtosContratadosDTO.add(new ProdutoContratadoDTO(produtoDossie, dossieProduto.getInstanciasDocumento(), dossieProduto.getRespostasDossie())));
                } catch (RuntimeException re) {
                    //Lazy Exception ou atributos não carregados
                    LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                    this.produtosContratadosDTO = null;
                }
            }

            if (dossieProduto.getDossiesClienteProduto() != null) {
                try {
                    dossieProduto.getDossiesClienteProduto()
                            .forEach(dossieClienteProduto -> this.vinculosPessoasDTO.add(new VinculoPessoaDTO(dossieClienteProduto, dossieProduto.getInstanciasDocumento(), dossieProduto.getRespostasDossie())));
                } catch (RuntimeException re) {
                    //Lazy Exception ou atributos não carregados
                    LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                    this.vinculosPessoasDTO = null;
                }
            }

            if (dossieProduto.getSituacoesDossie() != null) {
                SituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get();
                this.situacaoAtual = situacaoAtualDossie.getTipoSituacaoDossie().getNome();
                this.dataHoraSituacao = situacaoAtualDossie.getDataHoraInclusao();
                dossieProduto.getSituacoesDossie().stream().sorted(Comparator.comparing(SituacaoDossie::getId))
                        .forEachOrdered(situacaoDossie -> this.situacoesDossieDTO.add(new SituacaoDossieDTO(situacaoDossie)));
            }

            if (dossieProduto.getGarantiasInformadas() != null) {
                try {
                    dossieProduto.getGarantiasInformadas()
                            .forEach(garantiaInformada -> this.garantiasInformadasDTO.add(new GarantiaInformadaDTO(garantiaInformada, dossieProduto.getInstanciasDocumento(), dossieProduto.getRespostasDossie())));
                } catch (RuntimeException re) {
                    //Lazy Exception ou atributos não carregados
                    LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                    this.garantiasInformadasDTO = null;
                }
            }

            if (dossieProduto.getInstanciasDocumento() != null) {
                try {
                    dossieProduto.getInstanciasDocumento()
                            .forEach(instanciaDocumento -> this.instanciasDocumentoDTO.add(new InstanciaDocumentoDTO(instanciaDocumento, Boolean.TRUE)));
                } catch (RuntimeException re) {
                    //Lazy Exception ou atributos não carregados
                    LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                    this.instanciasDocumentoDTO = null;
                }
            }

            if (dossieProduto.getRespostasDossie() != null) {
                try {
                    dossieProduto.getRespostasDossie()
                            .forEach(respostaDossie -> this.respostasFormularioDTO.add(new RespostaFormularioDTO(respostaDossie)));
                } catch (RuntimeException re) {
                    //Lazy Exception ou atributos não carregados
                    LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                    this.respostasFormularioDTO = null;
                }
            }

            if (Objects.nonNull(dossieProduto.getChecklistsAssociados())) {
                try {
                    dossieProduto.getChecklistsAssociados()
                            .forEach(ca -> {
                                if (Objects.nonNull(ca.getVerificacoes())) {
                                    ca.getVerificacoes().forEach(verificacao -> this.verificacoesDTO.add(new VerificacaoDTO(verificacao)));
                                }
                            });
                } catch (RuntimeException re) {
                    //Lazy Exception ou atributos não carregados
                    LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                    this.verificacoesDTO = null;
                }
            }
        }
    }

    public Boolean getAlteracao() {
        return alteracao;
    }

    public void setAlteracao(Boolean alteracao) {
        this.alteracao = alteracao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcessoDTO getProcessoDossieDTO() {
        return processoDossieDTO;
    }

    public void setProcessoDossieDTO(ProcessoDTO processoDossieDTO) {
        this.processoDossieDTO = processoDossieDTO;
    }

    public ProcessoDTO getProcessoFaseDTO() {
        return processoFaseDTO;
    }

    public void setProcessoFaseDTO(ProcessoDTO processoFaseDTO) {
        this.processoFaseDTO = processoFaseDTO;
    }

    public Integer getUnidadeCriacao() {
        return unidadeCriacao;
    }

    public void setUnidadeCriacao(Integer unidadeCriacao) {
        this.unidadeCriacao = unidadeCriacao;
    }

    public Integer getUnidadePriorizado() {
        return unidadePriorizado;
    }

    public void setUnidadePriorizado(Integer unidadePriorizado) {
        this.unidadePriorizado = unidadePriorizado;
    }

    public String getMatriculaPriorizado() {
        return matriculaPriorizado;
    }

    public void setMatriculaPriorizado(String matriculaPriorizado) {
        this.matriculaPriorizado = matriculaPriorizado;
    }

    public Integer getPesoPrioridade() {
        return pesoPrioridade;
    }

    public void setPesoPrioridade(Integer pesoPrioridade) {
        this.pesoPrioridade = pesoPrioridade;
    }

    public CanalCaixaEnum getCanalCaixaEnum() {
        return canalCaixaEnum;
    }

    public void setCanalCaixaEnum(CanalCaixaEnum canalCaixaEnum) {
        this.canalCaixaEnum = canalCaixaEnum;
    }

    public Calendar getDataHoraFinalizacao() {
        return dataHoraFinalizacao;
    }

    public void setDataHoraFinalizacao(Calendar dataHoraFinalizacao) {
        this.dataHoraFinalizacao = dataHoraFinalizacao;
    }

    public String getSituacaoAtual() {
        return situacaoAtual;
    }

    public void setSituacaoAtual(String situacaoAtual) {
        this.situacaoAtual = situacaoAtual;
    }

    public Calendar getDataHoraSituacao() {
        return dataHoraSituacao;
    }

    public void setDataHoraSituacao(Calendar dataHoraSituacao) {
        this.dataHoraSituacao = dataHoraSituacao;
    }

    public List<Integer> getUnidadesTratamento() {
        return unidadesTratamento;
    }

    public void setUnidadesTratamento(List<Integer> unidadesTratamento) {
        this.unidadesTratamento = unidadesTratamento;
    }

    public List<ProdutoContratadoDTO> getProdutosContratadosDTO() {
        return produtosContratadosDTO;
    }

    public void setProdutosContratadosDTO(List<ProdutoContratadoDTO> produtosContratadosDTO) {
        this.produtosContratadosDTO = produtosContratadosDTO;
    }

    public List<VinculoPessoaDTO> getVinculosPessoasDTO() {
        return vinculosPessoasDTO;
    }

    public void setVinculosPessoasDTO(List<VinculoPessoaDTO> vinculosPessoasDTO) {
        this.vinculosPessoasDTO = vinculosPessoasDTO;
    }

    public List<SituacaoDossieDTO> getSituacoesDossieDTO() {
        return situacoesDossieDTO;
    }

    public void setSituacoesDossieDTO(List<SituacaoDossieDTO> situacoesDossieDTO) {
        this.situacoesDossieDTO = situacoesDossieDTO;
    }

    public List<GarantiaInformadaDTO> getGarantiasInformadasDTO() {
        return garantiasInformadasDTO;
    }

    public void setGarantiasInformadasDTO(List<GarantiaInformadaDTO> garantiasInformadasDTO) {
        this.garantiasInformadasDTO = garantiasInformadasDTO;
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
