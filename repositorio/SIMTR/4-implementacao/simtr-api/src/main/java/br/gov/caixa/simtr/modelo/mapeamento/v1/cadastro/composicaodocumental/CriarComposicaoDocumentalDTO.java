package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.composicaodocumental;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.ComposicaoDocumental;
import br.gov.caixa.simtr.visao.PrototypeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = "composicao-documental")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = "dossie.cadastro.ManterComposicaoDocumentalDTO",
        description = "Representa a Composição Documental."
)
public class CriarComposicaoDocumentalDTO implements Serializable, PrototypeDTO<ComposicaoDocumental>{
	
	private static final long serialVersionUID = -3320812177001397125L;

	@XmlElement(name = "nome_composicao_documental")
	@ApiModelProperty(name = "nome_composicao_documental")
    private String nome;
	
	@XmlElement(name = "indicador_conclusao")
	@ApiModelProperty(name = "indicador_conclusao")
	private Boolean indicadorConclusao;
	
	@XmlElement(name = "regras_documentais")
	@ApiModelProperty(name = "regras_documentais")
    private List<RegraDocumentalDTO> regrasDocumentais;
	
    public CriarComposicaoDocumentalDTO() {
     super();
     this.regrasDocumentais = new ArrayList<>();
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
		return regrasDocumentais;
	}

	public void setRegrasInclusao(List<RegraDocumentalDTO> regrasInclusao) {
		this.regrasDocumentais = regrasInclusao;
	}
	
	@Override
	public ComposicaoDocumental prototype() {
		final ComposicaoDocumental composicaoDocumental = new ComposicaoDocumental();
		composicaoDocumental.setNome(this.nome);
		composicaoDocumental.setIndicadorConclusao(this.indicadorConclusao);
		
		this.regrasDocumentais.forEach(re -> composicaoDocumental.addRegrasDocumentais(re.prototype()));
		
		return composicaoDocumental;
	}
    

}
