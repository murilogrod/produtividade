package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import java.io.Serializable;

import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = ConstantesCadastroTipoDocumento.API_MODEL_FUNCAO_DOCUMENTAL,
        description = "Objeto utilizado para representar uma função documental vinculada a um tipo de documento nas consultas realizadas sob a ótica dos cadastros."
)
public class FuncaoDocumentalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.ID)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ID, required = true, value = "Atributo que representa a chave primaria da entidade.")
    private Integer id;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME, value = "Atributo definido para armazenar o nome da função documental.", required = true)
    private String nome;

    public FuncaoDocumentalDTO() {
        super();
    }

    public FuncaoDocumentalDTO(FuncaoDocumental funcaoDocumental) {
        this();
        this.id = funcaoDocumental.getId();
        this.nome = funcaoDocumental.getNome();
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
}
