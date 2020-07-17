package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.processo;

import java.io.Serializable;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.ComposicaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.visao.PrototypeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = "processo")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = "dossie.cadastro.ManterProcessoDTO",
        description = "Representa o Processo a ser criado ou alterado."
)
public class ManterProcessoDTO implements Serializable, PrototypeDTO<Processo>{
	
	private static final long serialVersionUID = -3320812177001397125L;

	@XmlElement(name = "id_processo")
	@ApiModelProperty(name = "id_processo", value="Identificador do processo a ser alterado, campo vazio quando solicitado criação de processo")
    private Integer id;
	
	@XmlElement(name = "nome_processo")
	@ApiModelProperty(name = "nome_processo", value="Descrição do Processo")
    private String nome;
	
	@XmlElement(name = "dossie_digital")
	@ApiModelProperty(name = "dossie_digital", value = "Processo do Dossie Digital", example = "true")
    private Boolean dossieDigital;
	
	@XmlElement(name = "controla_validade_documento")
	@ApiModelProperty(name = "controla_validade_documento", value = "Controla Validade de Documento", example = "true")
    private Boolean controlaValidadeDocumento;
	
	@XmlElement(name = "tipo_pessoa")
	@ApiModelProperty(name = "tipo_pessoa", value = "Tipo Pessoa", example = "F")
	private TipoPessoaEnum tipoPessoa;
	
	@XmlElement(name = "processo_bpm")
	@ApiModelProperty(name = "processo_bpm", value = "Processo BPM")
    private String processoBpm;
	
	@XmlElement(name = "no_container_bpm")
	@ApiModelProperty(name = "no_container_bpm", value = "Container BPM")
    private String containerBpm;
	
	@XmlElement(name = "avatar")
	@ApiModelProperty(name = "avatar", value = "Avatar Processo")
    private String avatar;
	
	@XmlElement(name = "processos")
	@ApiModelProperty(name = "processos")
	private Set<Integer> processos;
	
		
    public ManterProcessoDTO() {
    	super();
    }
    
    public ManterProcessoDTO(ComposicaoDocumental composicaoDocumental) {
    	this();
    }
    

	@Override
	public Processo prototype() {
		final Processo processo = new Processo();
		processo.setId(this.getId());
		processo.setAvatar(this.getAvatar());
		processo.setNome(this.getNome());
		processo.setIndicadorGeracaoDossie(this.getDossieDigital());
		processo.setControlaValidade(this.getControlaValidadeDocumento());
		processo.setTipoPessoa(this.getTipoPessoa());
		processo.setNomeProcessoBPM(this.getProcessoBpm());
		processo.setNomeContainerBPM(this.getContainerBpm());
		return processo;
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

	public Boolean getDossieDigital() {
		return dossieDigital;
	}

	public void setDossieDigital(Boolean dossieDigital) {
		this.dossieDigital = dossieDigital;
	}

	public Boolean getControlaValidadeDocumento() {
		return controlaValidadeDocumento;
	}

	public void setControlaValidadeDocumento(Boolean controlaValidadeDocumento) {
		this.controlaValidadeDocumento = controlaValidadeDocumento;
	}

	public TipoPessoaEnum getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoaEnum tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getProcessoBpm() {
		return processoBpm;
	}

	public void setProcessoBpm(String processoBpm) {
		this.processoBpm = processoBpm;
	}

	public String getContainerBpm() {
		return containerBpm;
	}

	public void setContainerBpm(String containerBpm) {
		this.containerBpm = containerBpm;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}


}
