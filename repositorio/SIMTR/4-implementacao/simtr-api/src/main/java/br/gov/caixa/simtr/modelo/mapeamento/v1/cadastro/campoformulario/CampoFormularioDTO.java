package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroCampoFormulario;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = ConstantesCadastroCampoFormulario.API_MODEL_CAMPO_FORMULARIO,
        description = "Objeto utilizado para representar um campo formulário nas consultas realizadas sob a ótica dos cadastros")
public class CampoFormularioDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = ConstantesCadastroCampoFormulario.PROCESSO_ORIGINADOR)
	@ApiModelProperty(name = ConstantesCadastroCampoFormulario.PROCESSO_ORIGINADOR, value = "Atributo que representa processo originador da entidade")
	private ProcessoOriginadorDTO processoOriginador;
	
	@JsonProperty(value = ConstantesCadastroCampoFormulario.PROCESSO_FASE)
	@ApiModelProperty(name = ConstantesCadastroCampoFormulario.PROCESSO_FASE, value = "Atributo que representa processo fase da entidade")
	private ProcessoFaseDTO processoFase;
	
	@JsonProperty(value = ConstantesCadastroCampoFormulario.TIPO_RELACIONAMENTO)
	@ApiModelProperty(name = ConstantesCadastroCampoFormulario.TIPO_RELACIONAMENTO, value = "Atributo que representa tipo de relacionamento da entidade")
	private TipoRelacionamentoDTO tipoRelacionamento;
	
	@JsonProperty(value = ConstantesCadastroCampoFormulario.PRODUTO)
	@ApiModelProperty(name = ConstantesCadastroCampoFormulario.PRODUTO, value = "Atributo que representa produto da entidade")
	private ProdutoDTO produto;
	
	@JsonProperty(value = ConstantesCadastroCampoFormulario.GARANTIA)
	@ApiModelProperty(name = ConstantesCadastroCampoFormulario.GARANTIA, value = "Atributo que representa garantia da entidade")
	private GarantiaDTO garantia;
	
	@JsonProperty(value = ConstantesCadastroCampoFormulario.QUANTIDADE)
	@ApiModelProperty(name = ConstantesCadastroCampoFormulario.QUANTIDADE, value = "Atributo que representa quantidade")
	private Integer quantidade;
	
	public CampoFormularioDTO() {
		super();
	}
	
	public CampoFormularioDTO(CampoFormulario entidade) {
		super();
		
		this.processoOriginador = new ProcessoOriginadorDTO(entidade.getProcesso().getId(), entidade.getProcesso().getNome());
		
		if(Objects.nonNull(entidade.getProcessoFase())) {
			this.processoFase = new ProcessoFaseDTO(entidade.getProcessoFase().getId(), entidade.getProcessoFase().getNome());
		}
		
		if(Objects.nonNull(entidade.getTipoRelacionamento())) {
			this.tipoRelacionamento = new TipoRelacionamentoDTO(entidade.getTipoRelacionamento().getId(), entidade.getTipoRelacionamento().getNome());
		}
		
		if(Objects.nonNull(entidade.getProduto())) {
			this.produto = new ProdutoDTO(entidade.getProduto().getId(), entidade.getProduto().getNome());
		}
		
		if(Objects.nonNull(entidade.getGarantia())) {
			this.garantia = new GarantiaDTO(entidade.getGarantia().getId(), entidade.getGarantia().getNome());
		}
		
		this.quantidade = 0;
	}

	public ProcessoOriginadorDTO getProcessoOriginador() {
		return processoOriginador;
	}

	public void setProcessoOriginador(ProcessoOriginadorDTO processoOriginador) {
		this.processoOriginador = processoOriginador;
	}

	public ProcessoFaseDTO getProcessoFase() {
		return processoFase;
	}

	public void setProcessoFase(ProcessoFaseDTO processoFase) {
		this.processoFase = processoFase;
	}

	public TipoRelacionamentoDTO getTipoRelacionamento() {
		return tipoRelacionamento;
	}

	public void setTipoRelacionamento(TipoRelacionamentoDTO tipoRelacionamento) {
		this.tipoRelacionamento = tipoRelacionamento;
	}

	public ProdutoDTO getProduto() {
		return produto;
	}

	public void setProduto(ProdutoDTO produto) {
		this.produto = produto;
	}

	public GarantiaDTO getGarantia() {
		return garantia;
	}

	public void setGarantia(GarantiaDTO garantia) {
		this.garantia = garantia;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}
}
