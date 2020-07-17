package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente;

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

import br.gov.caixa.simtr.modelo.entidade.DossieClienteProduto;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.ProcessoFaseDossie;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDossie;
import br.gov.caixa.simtr.modelo.enumerator.CanalCaixaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CGCUnidadeAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieCliente.XML_ROOT_ELEMENT_DOSSIE_PRODUTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesNegocioDossieCliente.API_MODEL_V1_DOSSIE_PRODUTO, description = "Objeto utilizado para representar o dossiê de produto no retorno as consultas realizadas a partir do Dossiê do Cliente")
public class DossieProdutoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = ConstantesNegocioDossieCliente.ID_DOSSIE_PRODUTO)
	@ApiModelProperty(name = ConstantesNegocioDossieCliente.ID_DOSSIE_PRODUTO, required = true, value = "Identificador do dossiê de produto")
	private Long id;

	@XmlElement(name = ConstantesNegocioDossieCliente.PROCESSO_DOSSIE)
	@ApiModelProperty(name = ConstantesNegocioDossieCliente.PROCESSO_DOSSIE, required = true, value = "Nome do processo originador do dossiê de produto")
	private ProcessoDTO processoDossieDTO;

	@XmlElement(name = ConstantesNegocioDossieCliente.PROCESSO_PATRIARCA)
	@ApiModelProperty(name = ConstantesNegocioDossieCliente.PROCESSO_PATRIARCA, required = true, value = "Nome do processo patriarca que agrupa os dossiês de produto do mesmo tipo")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private ProcessoDTO processoPatriarcaDTO;

	@XmlElement(name = ConstantesNegocioDossieCliente.PROCESSO_FASE)
	@ApiModelProperty(name = ConstantesNegocioDossieCliente.PROCESSO_FASE, required = true, value = "Nome do processo que identifica a fase atual do dossiê de produto")
	private ProcessoDTO processoFaseDTO;

	@XmlJavaTypeAdapter(value = CGCUnidadeAdapter.class)
	@XmlElement(name = ConstantesNegocioDossieCliente.UNIDADE_CRIACAO)
	@ApiModelProperty(name = ConstantesNegocioDossieCliente.UNIDADE_CRIACAO, required = true, value = "CGC de identificação da unidade de criação do dossiê de produto")
	private Integer unidadeCriacao;

	@XmlElement(name = ConstantesNegocioDossieCliente.UNIDADE_PRIORIZADO)
	@ApiModelProperty(name = ConstantesNegocioDossieCliente.UNIDADE_PRIORIZADO, required = false, value = "CGC de identificação da unidade de priorização para tratamento do dossiê de produto")
	@XmlJavaTypeAdapter(value = CGCUnidadeAdapter.class)
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Integer unidadePriorizado;

	@XmlElement(name = ConstantesNegocioDossieCliente.CANAL_CAIXA)
	@ApiModelProperty(name = ConstantesNegocioDossieCliente.CANAL_CAIXA, required = true, value = "Identificador do canal fisico de origem do dossiê de produto")
	private CanalCaixaEnum canalCaixaEnum;

	@XmlElement(name = ConstantesNegocioDossieCliente.DATA_HORA_FINALIZACAO)
	@ApiModelProperty(name = ConstantesNegocioDossieCliente.DATA_HORA_FINALIZACAO, required = false, value = "Data/Hora de finalização do ciclo de vida do dossiê de produto", example = "dd/MM/yyyy HH:mm:ss")
	@XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
	private Calendar dataHoraFinalizacao;

	// ************************************
	@XmlElement(name = ConstantesNegocioDossieCliente.TIPO_RELACIONAMENTO)
	@ApiModelProperty(name = ConstantesNegocioDossieCliente.TIPO_RELACIONAMENTO, required = true, value = "Identificação da tipo de relacionamento que o Dossiê de Cliente mantem com o dossiê de produto")
	private TipoRelacionamentoDTO tipoRelacionamento;

	@XmlElement(name = ConstantesNegocioDossieCliente.SITUACAO_ATUAL)
	@ApiModelProperty(name = ConstantesNegocioDossieCliente.SITUACAO_ATUAL, required = true, value = "Identificação da situação atual em que se encontra o dossiê de produto")
	private String situacaoAtual;

	@XmlElement(name = ConstantesNegocioDossieCliente.DATA_HORA_SITUACAO)
	@ApiModelProperty(name = ConstantesNegocioDossieCliente.DATA_HORA_SITUACAO, required = true, value = "Data/Hora de atribuição da situação atual em que se encontra o dossiê de produto", example = "dd/MM/yyyy HH:mm:ss")
	@XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
	private Calendar dataHoraSituacao;

	@XmlElement(name = ConstantesNegocioDossieCliente.UNIDADE_SITUACAO)
	@ApiModelProperty(name = ConstantesNegocioDossieCliente.UNIDADE_SITUACAO, required = false, value = "CGC de identificação da unidade que incluiu a situasção ao dossiê de produto")
	@XmlJavaTypeAdapter(value = CGCUnidadeAdapter.class)
	private Integer unidadeSituacao;

	// ***********************************
	@XmlElement(name = ConstantesNegocioDossieCliente.PRODUTO_CONTRATADO)
	@XmlElementWrapper(name = ConstantesNegocioDossieCliente.PRODUTOS_CONTRATADOS)
	@JsonProperty(value = ConstantesNegocioDossieCliente.PRODUTOS_CONTRATADOS)
	@ApiModelProperty(name = ConstantesNegocioDossieCliente.PRODUTOS_CONTRATADOS, required = false, value = "Lista de produtos contratados vinculados ao dossiê de produto")
	private List<ProdutoContratadoDTO> produtosContratadosDTO;

	@XmlJavaTypeAdapter(value = CGCUnidadeAdapter.class)
	@XmlElementWrapper(name = ConstantesNegocioDossieCliente.UNIDADES_TRATAMENTO)
	@XmlElement(name = ConstantesNegocioDossieCliente.UNIDADE_TRATAMENTO)
	@JsonProperty(value = ConstantesNegocioDossieCliente.UNIDADES_TRATAMENTO)
	@ApiModelProperty(name = ConstantesNegocioDossieCliente.UNIDADES_TRATAMENTO, required = false, value = "Lista contendo CGCs das unidades definidas para tratamento neste momento para o dossiê de produto.")
	private List<Integer> unidadesTratamentoDTO;

	public DossieProdutoDTO() {
		super();
		this.produtosContratadosDTO = new ArrayList<>();
		this.unidadesTratamentoDTO = new ArrayList<>();
	}

	public DossieProdutoDTO(DossieProduto dossieProduto) {
		this();
		if (dossieProduto != null) {
			this.id = dossieProduto.getId();
			if (dossieProduto.getProcesso() != null) {
				this.processoDossieDTO = new ProcessoDTO(dossieProduto.getProcesso());
			}
			if (dossieProduto.getProcessosFaseDossie() != null && !dossieProduto.getProcessosFaseDossie().isEmpty()) { 
                                ProcessoFaseDossie processoFaseAtual = dossieProduto.getProcessosFaseDossie().stream()
                                        .max(Comparator.comparing(ProcessoFaseDossie::getId)).get();
				this.processoFaseDTO = new ProcessoDTO(processoFaseAtual.getProcessoFase());
			}
			this.unidadeCriacao = dossieProduto.getUnidadeCriacao();
			this.unidadePriorizado = dossieProduto.getUnidadePriorizado();
			this.canalCaixaEnum = dossieProduto.getCanal().getCanalCaixa();
			this.dataHoraFinalizacao = dossieProduto.getDataHoraFinalizacao();
			if (dossieProduto.getSituacoesDossie() != null) {
				SituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream()
						.max(Comparator.comparing(SituacaoDossie::getId)).get();
				this.situacaoAtual = situacaoAtualDossie.getTipoSituacaoDossie().getNome();
				this.dataHoraSituacao = situacaoAtualDossie.getDataHoraInclusao();
				this.unidadeSituacao = situacaoAtualDossie.getUnidade();
			}
			if (dossieProduto.getProdutosDossie() != null) {
				dossieProduto.getProdutosDossie().forEach(produtoContratado -> this.produtosContratadosDTO
						.add(new ProdutoContratadoDTO(produtoContratado)));
			}
			if (dossieProduto.getUnidadesTratamento() != null) {
				dossieProduto.getUnidadesTratamento()
						.forEach(unidadeTratamento -> this.unidadesTratamentoDTO.add(unidadeTratamento.getUnidade()));
			}
		}
	}

	public DossieProdutoDTO(DossieClienteProduto dossieClienteProduto) {
		this(dossieClienteProduto.getDossieProduto());
		this.tipoRelacionamento = new TipoRelacionamentoDTO(dossieClienteProduto.getTipoRelacionamento());
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

	public ProcessoDTO getProcessoPatriarcaDTO() {
		return processoPatriarcaDTO;
	}

	public void setProcessoPatriarcaDTO(ProcessoDTO processoPatriarcaDTO) {
		this.processoPatriarcaDTO = processoPatriarcaDTO;
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

	public Integer getUnidadeSituacao() {
		return unidadeSituacao;
	}

	public void setUnidadeSituacao(Integer unidadeSituacao) {
		this.unidadeSituacao = unidadeSituacao;
	}

	public List<ProdutoContratadoDTO> getProdutosContratadosDTO() {
		return produtosContratadosDTO;
	}

	public void setProdutosContratadosDTO(List<ProdutoContratadoDTO> produtosContratadosDTO) {
		this.produtosContratadosDTO = produtosContratadosDTO;
	}

	public TipoRelacionamentoDTO getTipoRelacionamento() {
		return tipoRelacionamento;
	}

	public void setTipoRelacionamento(TipoRelacionamentoDTO tipoRelacionamento) {
		this.tipoRelacionamento = tipoRelacionamento;
	}

	public List<Integer> getUnidadesTratamentoDTO() {
		return unidadesTratamentoDTO;
	}

	public void setUnidadesTratamentoDTO(List<Integer> unidadesTratamento) {
		this.unidadesTratamentoDTO = unidadesTratamento;
	}

}
