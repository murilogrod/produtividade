package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dashboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

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
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CGCUnidadeAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDashboard;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDashboard.XML_ROOT_ELEMENT_DOSSIE_PRODUTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesNegocioDashboard.API_MODEL_V1_DOSSIE_PRODUTO, description = "Objeto utilizado para representar o dossiê no retorno as consultas realizadas sob a ótica Apoio ao Negocio para um Dashboard.")
public class DossieProdutoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDashboard.ID)
    @ApiModelProperty(name = ConstantesNegocioDashboard.ID, required = true, value = "Identificador do dossiê de produto.")
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlElement(name = ConstantesNegocioDashboard.CPF)
    @ApiModelProperty(name = ConstantesNegocioDashboard.CPF, required = false, value = "CPF dossiê cliente definido como relacionamento principal do dossiê.")
    private Long cpf;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlElement(name = ConstantesNegocioDashboard.CNPJ)
    @ApiModelProperty(name = ConstantesNegocioDashboard.CNPJ, required = false, value = "CNPJ dossiê cliente definido como relacionamento principal do dossiê.")
    private Long cnpj;

    @XmlElement(name = ConstantesNegocioDashboard.NOME_CLIENTE)
    @ApiModelProperty(name = ConstantesNegocioDashboard.NOME_CLIENTE, required = true, value = "Nome do cliente.")
    private String nomeCliente;

    @XmlElement(name = ConstantesNegocioDashboard.MATRICULA)
    @ApiModelProperty(name = ConstantesNegocioDashboard.MATRICULA, required = true, value = "Matricula de identificação do usuário de criação do dossiê de produto.")
    private String matricula;

    @XmlJavaTypeAdapter(value = CGCUnidadeAdapter.class)
    @XmlElement(name = ConstantesNegocioDashboard.UNIDADE_CRIACAO)
    @ApiModelProperty(name = ConstantesNegocioDashboard.UNIDADE_CRIACAO, required = true, value = "CGC de identificação da unidade de criação do dossiê de produto.")
    private Integer unidadeCriacao;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CGCUnidadeAdapter.class)
    @XmlElement(name = ConstantesNegocioDashboard.UNIDADE_PRIORIZADO)
    @ApiModelProperty(name = ConstantesNegocioDashboard.UNIDADE_PRIORIZADO, required = false, value = "CGC de identificação da unidade de priorização para tratamento do dossiê de produto.")
    private Integer unidadePriorizado;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlElement(name = ConstantesNegocioDashboard.PESO_PRIORIZADO)
    @ApiModelProperty(name = ConstantesNegocioDashboard.PESO_PRIORIZADO, required = false, value = "Valor que indica o peso atribuido a priorização para tratamento do dossiê de produto. Quanto maior o peso, maior será a prioridade atribuida.")
    private Integer pesoPrioridade;

    @XmlElement(name = ConstantesNegocioDashboard.CANAL_CAIXA)
    @ApiModelProperty(name = ConstantesNegocioDashboard.CANAL_CAIXA, required = true, value = "Identificador do canal fisico de origem do dossiê de produto.")
    private CanalCaixaEnum canalCaixaEnum;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDashboard.DATA_HORA_FINALIZACAO)
    @ApiModelProperty(name = ConstantesNegocioDashboard.DATA_HORA_FINALIZACAO, required = false, value = "Data/Hora de finalização do ciclo de vida do dossiê de produto.", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraFinalizacao;

    @XmlJavaTypeAdapter(value = CGCUnidadeAdapter.class)
    @XmlElementWrapper(name = ConstantesNegocioDashboard.UNIDADES_TRATAMENTO)
    @XmlElement(name = ConstantesNegocioDashboard.UNIDADE_TRATAMENTO)
    @JsonProperty(value = ConstantesNegocioDashboard.UNIDADES_TRATAMENTO)
    @ApiModelProperty(name = ConstantesNegocioDashboard.UNIDADES_TRATAMENTO, required = false, value = "Lista contendo CGCs das unidades definidas para tratamento neste momento para o dossiê de produto.")
    private List<Integer> unidadesTratamento;

    // ************************************
    @XmlElement(name = ConstantesNegocioDashboard.SITUACAO_ATUAL)
    @ApiModelProperty(name = ConstantesNegocioDashboard.SITUACAO_ATUAL, value = "Identificação da situação atual em que se encontra o dossiê de produto")
    private String situacaoAtual;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDashboard.DATA_HORA_SITUACAO)
    @ApiModelProperty(name = ConstantesNegocioDashboard.DATA_HORA_SITUACAO, value = "Data/Hora de atribuição da situação atual em que se encontra o dossiê de produto", required = true, example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraSituacao;

    @XmlElementWrapper(name = ConstantesNegocioDashboard.HISTORICO_SITUACOES)
    @XmlElement(name = ConstantesNegocioDashboard.SITUACAO_DOSSIE)
    @JsonProperty(value = ConstantesNegocioDashboard.HISTORICO_SITUACOES)
    @ApiModelProperty(name = ConstantesNegocioDashboard.HISTORICO_SITUACOES, required = true, value = "Lista de situações que compõem o hitorico relacionado ao dossiê de produto. A lista é ordenada em ordem cronologica pela data de inclusão da situação.")
    private List<SituacaoDossieDTO> situacoesDossieDTO;

    // ***********************************
    @XmlElement(name = ConstantesNegocioDashboard.PROCESSO_DOSSIE)
    @ApiModelProperty(name = ConstantesNegocioDashboard.PROCESSO_DOSSIE, required = true, value = "Nome do processo originador do dossiê de produto.")
    private String processoDossieDTO;

    @XmlElement(name = ConstantesNegocioDashboard.PROCESSO_FASE)
    @ApiModelProperty(name = ConstantesNegocioDashboard.PROCESSO_FASE, required = true, value = "Nome do processo que identifica a fase atual do dossiê de produto.")
    private String processoFaseDTO;

    @XmlElement(name = ConstantesNegocioDashboard.PROCESSO_FASE_ID)
    @ApiModelProperty(name = ConstantesNegocioDashboard.PROCESSO_FASE_ID, required = true, value = "Código de identificação da fase atual do dossiê de produto.")
    private Integer processoFaseIdDTO;

    @XmlElementWrapper(name = ConstantesNegocioDashboard.PRODUTOS_CONTRATADOS)
    @XmlElement(name = ConstantesNegocioDashboard.PRODUTO_CONTRATADO)
    @JsonProperty(value = ConstantesNegocioDashboard.PRODUTOS_CONTRATADOS)
    @ApiModelProperty(name = ConstantesNegocioDashboard.PRODUTOS_CONTRATADOS, required = false, value = "Lista de produtos contratados vinculados ao dossiê de produto")
    private List<ProdutoContratadoDTO> produtosContratadosDTO;

    public DossieProdutoDTO() {
	super();
	this.unidadesTratamento = new ArrayList<>();
	this.produtosContratadosDTO = new ArrayList<>();
	this.situacoesDossieDTO = new ArrayList<>();
    }

    public DossieProdutoDTO(DossieProduto dossieProduto) {
	this();
	if (dossieProduto != null) {
	    this.id = dossieProduto.getId();
	    this.unidadeCriacao = dossieProduto.getUnidadeCriacao();
	    this.unidadePriorizado = dossieProduto.getUnidadePriorizado();
	    this.pesoPrioridade = dossieProduto.getPesoPrioridade();
	    this.canalCaixaEnum = dossieProduto.getCanal().getCanalCaixa();
	    this.dataHoraFinalizacao = dossieProduto.getDataHoraFinalizacao();
	    if (dossieProduto.getUnidadesTratamento() != null) {
		dossieProduto.getUnidadesTratamento()
			.forEach(unidadeTratamento -> this.unidadesTratamento.add(unidadeTratamento.getUnidade()));
	    }

	    if (dossieProduto.getProcesso() != null) {
		this.processoDossieDTO = dossieProduto.getProcesso().getNome();
	    }
	    if (dossieProduto.getProcessosFaseDossie() != null) {
                ProcessoFaseDossie processoFaseAtual = dossieProduto.getProcessosFaseDossie().stream()
                        .max(Comparator.comparing(ProcessoFaseDossie::getId)).get();
                
		this.processoFaseIdDTO = processoFaseAtual.getProcessoFase().getId();
		this.processoFaseDTO = processoFaseAtual.getProcessoFase().getNome();
	    }

	    if (dossieProduto.getProdutosDossie() != null) {
		dossieProduto.getProdutosDossie().forEach(
			produtoDossie -> this.produtosContratadosDTO.add(new ProdutoContratadoDTO(produtoDossie)));
	    }

	    if (dossieProduto.getSituacoesDossie() != null) {
		SituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream()
			.max(Comparator.comparing(SituacaoDossie::getId)).get();
		this.situacaoAtual = situacaoAtualDossie.getTipoSituacaoDossie().getNome();
		this.dataHoraSituacao = situacaoAtualDossie.getDataHoraInclusao();
		dossieProduto.getSituacoesDossie().stream().sorted(Comparator.comparing(SituacaoDossie::getId))
			.forEachOrdered(
				situacaoDossie -> this.situacoesDossieDTO.add(new SituacaoDossieDTO(situacaoDossie)));

		// Captura a matricula do usário que realizaou a primeira guarda do documento
		SituacaoDossie situacaoInicialDossie = dossieProduto.getSituacoesDossie().stream()
			.min(Comparator.comparing(SituacaoDossie::getId)).get();
		this.matricula = situacaoInicialDossie.getResponsavel();
	    }

	    // Captura o CPF/CNPJ do dossiê de cliente relacionado ao dossi~e de produto que
	    // possui o vinculo definido como principal
	    if (dossieProduto.getDossiesClienteProduto() != null) {
		dossieProduto.getDossiesClienteProduto().forEach(dcp -> {
		    if (dcp.getTipoRelacionamento().getIndicadorPrincipal()) {
			if (TipoPessoaEnum.F.equals(dcp.getDossieCliente().getTipoPessoa())) {
			    this.cpf = dcp.getDossieCliente().getCpfCnpj();
			} else {
			    this.cnpj = dcp.getDossieCliente().getCpfCnpj();
			}
			this.nomeCliente = dcp.getDossieCliente().getNome();
		    }
		});
	    }
	}
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getCpf() {
	return cpf;
    }

    public void setCpf(Long cpf) {
	this.cpf = cpf;
    }

    public Long getCnpj() {
	return cnpj;
    }

    public void setCnpj(Long cnpj) {
	this.cnpj = cnpj;
    }

    public String getNomeCliente() {
	return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
	this.nomeCliente = nomeCliente;
    }

    public String getMatricula() {
	return matricula;
    }

    public void setMatricula(String matricula) {
	this.matricula = matricula;
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

    public List<Integer> getUnidadesTratamento() {
	return unidadesTratamento;
    }

    public void setUnidadesTratamento(List<Integer> unidadesTratamento) {
	this.unidadesTratamento = unidadesTratamento;
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

    public List<SituacaoDossieDTO> getSituacoesDossieDTO() {
	return situacoesDossieDTO;
    }

    public void setSituacoesDossieDTO(List<SituacaoDossieDTO> situacoesDossieDTO) {
	this.situacoesDossieDTO = situacoesDossieDTO;
    }

    public String getProcessoDossieDTO() {
	return processoDossieDTO;
    }

    public void setProcessoDossieDTO(String processoDossieDTO) {
	this.processoDossieDTO = processoDossieDTO;
    }

    public Integer getProcessoFaseIdDTO() {
	return processoFaseIdDTO;
    }

    public void setProcessoFaseIdDTO(Integer processoFaseIdDTO) {
	this.processoFaseIdDTO = processoFaseIdDTO;
    }

    public String getProcessoFaseDTO() {
	return processoFaseDTO;
    }

    public void setProcessoFaseDTO(String processoFaseDTO) {
	this.processoFaseDTO = processoFaseDTO;
    }

    public List<ProdutoContratadoDTO> getProdutosContratadosDTO() {
	return produtosContratadosDTO;
    }

    public void setProdutosContratadosDTO(List<ProdutoContratadoDTO> produtosContratadosDTO) {
	this.produtosContratadosDTO = produtosContratadosDTO;
    }
}
