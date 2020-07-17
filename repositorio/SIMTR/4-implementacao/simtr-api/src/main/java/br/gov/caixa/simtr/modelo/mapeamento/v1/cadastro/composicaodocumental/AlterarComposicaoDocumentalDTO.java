package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.composicaodocumental;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = "composicao-documental")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = "dossie.cadastro.AlterarComposicaoDocumentalDTO",
        description = "Representa a Composição Documental nas consultas realizadas sob a ótica do Dossiê Digital."
)
public class AlterarComposicaoDocumentalDTO implements Serializable{
	
	private static final long serialVersionUID = -3320812177001397125L;

	@XmlElement(name = "nome_composicao_documental")
	@ApiModelProperty(name = "nome_composicao_documental")
    private String nome;
	
	@XmlElement(name = "indicador_conclusao")
	@ApiModelProperty(name = "indicador_conclusao")
	private Boolean indicadorConclusao;
	
	@XmlElement(name = "regras_documentais_inclusao")
	@ApiModelProperty(name = "regras_documentais_inclusao")
    private List<RegraDocumentalDTO> regrasInclusao;
	
	@XmlElement(name = "regras_documentais_exclusao")
	@ApiModelProperty(name = "regras_documentais_exclusao")
    private List<Long> regrasExclusao;
	
    public AlterarComposicaoDocumentalDTO() {
     super();
     this.regrasInclusao = new ArrayList<>();
     this.regrasExclusao = new ArrayList<>();
    }
    
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Boolean getIndicadorConclusao() {
		return indicadorConclusao;
	}

	public void setIndicadorConclusao(Boolean indicadorConclusao) {
		this.indicadorConclusao = indicadorConclusao;
	}

	public List<RegraDocumentalDTO> getRegrasInclusao() {
		return regrasInclusao;
	}

	public void setRegrasInclusao(List<RegraDocumentalDTO> regrasInclusao) {
		this.regrasInclusao = regrasInclusao;
	}

	public List<Long> getRegrasExclusao() {
		return regrasExclusao;
	}

	public void setRegrasExclusao(List<Long> regrasExclusao) {
		this.regrasExclusao = regrasExclusao;
	}

}
