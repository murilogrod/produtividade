package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.composicaodocumental;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.gov.caixa.simtr.modelo.entidade.ComposicaoDocumental;
import br.gov.caixa.simtr.visao.PrototypeDTO;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = "composicao-documental")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = "dossie.cadastro.ComposicaoDocumentalDTO",
        description = "Representa a Composição Documental nas consultas realizadas sob a ótica do Dossiê Digital."
)
public class ComposicaoDocumentalDTO implements Serializable, PrototypeDTO<ComposicaoDocumental>{
	
	private static final long serialVersionUID = -3320812177001397125L;

	@XmlElement(name = "id_composicao_documental")
	@ApiModelProperty(name = "id_composicao_documental", required = true)
    private Long id;
	
	@XmlElement(name = "nome_composicao_documental")
	@ApiModelProperty(name = "nome_composicao_documental")
    private String nome;
	
	@XmlElement(name = "data_hora_inclusao")
	@XmlJavaTypeAdapter(CalendarFullBRAdapter.class)
    @ApiModelProperty(name = "data_hora_inclusao", value = "Data e hora da inclusao no sistema", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraInclusao;
	
	@XmlElement(name = "data_hora_revogacao")
	@XmlJavaTypeAdapter(CalendarFullBRAdapter.class)
    @ApiModelProperty(name = "data_hora_revogacao", value = "Data e hora da inclusao no sistema", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraRevogacao;
	
	@XmlElement(name = "matricula_inclusao")
	@ApiModelProperty(name = "matricula_inclusao")
    private String matriculaInclusao;
	
	@XmlElement(name = "matricula_revogacao")
	@ApiModelProperty(name = "matricula_revogacao")
    private String matriculaRevogacao;
	
	@XmlElement(name = "indicador_conclusao")
	@ApiModelProperty(name = "indicador_conclusao")
	private Boolean indicadorConclusao;
	
	@XmlElement(name = "regras_associadas")
	@ApiModelProperty(name = "regras_associadas")
    private List<RegraDocumentalDTO> regrasAssociadas;
	
    public ComposicaoDocumentalDTO() {
     super();
     this.regrasAssociadas = new ArrayList<>();
    }
    
    public ComposicaoDocumentalDTO(ComposicaoDocumental composicaoDocumental, boolean carregarRegras) {
    	this();
    	this.id = composicaoDocumental.getId();
    	this.nome = composicaoDocumental.getNome();
    	this.dataHoraInclusao = composicaoDocumental.getDataHoraInclusao();
    	this.dataHoraRevogacao = composicaoDocumental.getDataHoraRevogacao();
    	this.matriculaInclusao = composicaoDocumental.getMatriculaInclusao();
    	this.matriculaRevogacao = composicaoDocumental.getMatriculaRevogacao();
    	this.indicadorConclusao = composicaoDocumental.getIndicadorConclusao();
    	if(carregarRegras) {
    		if(composicaoDocumental.getRegrasDocumentais() != null) {
    			composicaoDocumental.getRegrasDocumentais().stream()
    			.forEach(r -> this.regrasAssociadas.add(new RegraDocumentalDTO(r)  ));
    		}
    	}
    }
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Calendar getDataHoraInclusao() {
		return dataHoraInclusao;
	}
	public void setDataHoraInclusao(Calendar dataHoraInclusao) {
		this.dataHoraInclusao = dataHoraInclusao;
	}
	public Calendar getDataHoraRevogacao() {
		return dataHoraRevogacao;
	}
	public void setDataHoraRevogacao(Calendar dataHoraRevogacao) {
		this.dataHoraRevogacao = dataHoraRevogacao;
	}
	public String getMatriculaInclusao() {
		return matriculaInclusao;
	}
	public void setMatriculaInclusao(String matriculaInclusao) {
		this.matriculaInclusao = matriculaInclusao;
	}
	public String getMatriculaRevogacao() {
		return matriculaRevogacao;
	}
	public void setMatriculaRevogacao(String matriculaRevogacao) {
		this.matriculaRevogacao = matriculaRevogacao;
	}
	
	public Boolean getIndicadorConclusao() {
		return indicadorConclusao;
	}

	public void setIndicadorConclusao(Boolean indicadorConclusao) {
		this.indicadorConclusao = indicadorConclusao;
	}

	public List<RegraDocumentalDTO> getRegrasAssociadas() {
		return regrasAssociadas;
	}
	public void setRegrasAssociadas(List<RegraDocumentalDTO> regrasAssociadas) {
		this.regrasAssociadas = regrasAssociadas;
	}

	@Override
	public ComposicaoDocumental prototype() {
		final ComposicaoDocumental composicaoDocumental = new ComposicaoDocumental();
		composicaoDocumental.setNome(this.nome);
		composicaoDocumental.setDataHoraInclusao(this.dataHoraInclusao);
		composicaoDocumental.setDataHoraRevogacao(this.dataHoraRevogacao);
		composicaoDocumental.setMatriculaInclusao(this.matriculaInclusao);
		composicaoDocumental.setMatriculaRevogacao(this.matriculaRevogacao);
		composicaoDocumental.setIndicadorConclusao(this.indicadorConclusao);
		
		this.regrasAssociadas.forEach(re -> composicaoDocumental.addRegrasDocumentais(re.prototype()));
		
		return composicaoDocumental;
	}
    
    
    

}
