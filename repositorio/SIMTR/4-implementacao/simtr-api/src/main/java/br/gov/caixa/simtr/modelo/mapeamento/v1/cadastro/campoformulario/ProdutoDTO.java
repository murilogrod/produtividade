package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroCampoFormulario;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = ConstantesCadastroCampoFormulario.API_MODEL_PRODUTO,
        description = "Objeto utilizado para representar um produto originador nas consultas realizadas sob a ótica dos cadastros")
public class ProdutoDTO implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = ConstantesCadastroCampoFormulario.ID)
	@ApiModelProperty(name = ConstantesCadastroCampoFormulario.ID, required = true, value = "Atributo que representa a chave primaria da entidade")
	private Integer id;
	
	@JsonProperty(value = ConstantesCadastroCampoFormulario.NOME)
    @ApiModelProperty(name = ConstantesCadastroCampoFormulario.NOME, required = true, value = "Atributo que identifica o produto vinculado")
	private String nome;

	public ProdutoDTO() {
		super();
	}
	
	public ProdutoDTO(Integer id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoDTO other = (ProdutoDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}