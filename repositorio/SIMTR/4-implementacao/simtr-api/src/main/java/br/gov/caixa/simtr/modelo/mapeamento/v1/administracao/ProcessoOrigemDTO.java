package br.gov.caixa.simtr.modelo.mapeamento.v1.administracao;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesConsultaFalhaBPM;
import io.swagger.annotations.ApiModelProperty;

public class ProcessoOrigemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesConsultaFalhaBPM.PROCESSO_ORIGEM_ID)
    @ApiModelProperty(name = ConstantesConsultaFalhaBPM.PROCESSO_ORIGEM_ID, value = "Atributo que representa o identificador do processo.")
    private Integer id;

    @JsonProperty(value = ConstantesConsultaFalhaBPM.PROCESSO_ORIGEM_NOME)
    @ApiModelProperty(name = ConstantesConsultaFalhaBPM.PROCESSO_ORIGEM_NOME, value = "Atributo que representa o nome dado processo.")
    private String nome;

    public ProcessoOrigemDTO(Processo processo) {
	super();
	if (Objects.nonNull(processo)) {
	    this.id = processo.getId();
	    this.nome = processo.getNome();
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

}
