package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import java.io.Serializable;


import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiModel(
        value = ConstantesCadastroTipoDocumento.API_MODEL_TIPO_DOCUMENTO,
        description = "Objeto utilizado para representar um tipo de documento nas consultas realizadas sob a ótica dos cadastros"
)
public class TipoDocumentoDadosDeclaradosDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.IDENTIFICADOR_TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.IDENTIFICADOR_TIPO_DOCUMENTO, required = true, value = "Atributo que representa a chave primaria da entidade")
    private Integer id;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME_TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_TIPO_DOCUMENTO, required = true, value = "Atributo que identifica o tipo de documento vinculado")
    private String nome;
    
    @JsonProperty(value = ConstantesCadastroTipoDocumento.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATIVO, value = "Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema.")    
    private Boolean ativo;    
    

    //****************************************************
    @JsonInclude(Include.NON_NULL)
    @JsonProperty(ConstantesCadastroTipoDocumento.ATRIBUTOS_EXTRACAO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATRIBUTOS_EXTRACAO, required = false, value = "Lista de atributos previstos (extração), associadas ao tipo de documento")
    private List<AtributoDadosDeclaradosDTO> atributosDadosDeclaradosDTO;

    public TipoDocumentoDadosDeclaradosDTO() {
        super();
        this.atributosDadosDeclaradosDTO = new ArrayList<>();
    }

    public TipoDocumentoDadosDeclaradosDTO(TipoDocumento tipoDocumento, boolean incluirAtributosExtracao) {
        this();
        if (Objects.nonNull(tipoDocumento)) {
            this.id = tipoDocumento.getId();
            this.nome = tipoDocumento.getNome();
            this.ativo = tipoDocumento.getAtivo();

            if (!incluirAtributosExtracao) {
                this.atributosDadosDeclaradosDTO = null;
            } else if (Objects.nonNull(tipoDocumento.getAtributosExtracao())) {
                tipoDocumento.getAtributosExtracao().stream()
                        .forEach(atributo -> this.atributosDadosDeclaradosDTO.add(new AtributoDadosDeclaradosDTO(atributo)));
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
    
    public Boolean getAtivo() {
		return ativo;
	}
    
    public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

    public List<AtributoDadosDeclaradosDTO> getAtributosDadosDeclaradosDTO() {
        return atributosDadosDeclaradosDTO;
    }

    public void setAtributosDadosDeclaradosDTO(List<AtributoDadosDeclaradosDTO> atributosDadosDeclaradosDTO) {
        this.atributosDadosDeclaradosDTO = atributosDadosDeclaradosDTO;
    }

}
