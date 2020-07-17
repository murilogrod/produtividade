package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.funcaodocumental;

import java.io.Serializable;


import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroFuncaoDocumental;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = ConstantesCadastroFuncaoDocumental.API_MODEL_TIPO_DOCUMENTO,
        description = "Objeto utilizado para representar um tipo de documento relacionado a uma função documental nas consultas realizadas sob a ótica dos cadastros."
)
public class TipoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(ConstantesCadastroFuncaoDocumental.IDENTIFICADOR_TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesCadastroFuncaoDocumental.IDENTIFICADOR_TIPO_DOCUMENTO, required = true)
    private Integer id;

    @JsonProperty(ConstantesCadastroFuncaoDocumental.NOME_TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesCadastroFuncaoDocumental.NOME_TIPO_DOCUMENTO, required = true)
    private String nome;
    
    @JsonProperty(value = ConstantesCadastroTipoDocumento.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATIVO, value = "Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema.")    
    private Boolean ativo;        

    public TipoDocumentoDTO() {

    }

    public TipoDocumentoDTO(TipoDocumento tipoDocumento) {
        this.id = tipoDocumento.getId();
        this.nome = tipoDocumento.getNome();
        this.ativo = tipoDocumento.getAtivo();
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
    
    public Boolean getAtivo() {
		return ativo;
	}
    
    public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}
