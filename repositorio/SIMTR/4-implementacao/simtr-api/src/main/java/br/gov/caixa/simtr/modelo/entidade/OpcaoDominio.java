package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb105_opcoes_dominio")
public class OpcaoDominio extends GenericEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer versao;
	private Dominio dominio;
	private String value;
	private String label;
	private String sicli;
	private String siric;
	
	public OpcaoDominio() {
		super();
	}
	
	@Id
    @Column(name = "nu_opcao_dominio")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "nu_versao", nullable = false)
	public Integer getVersao() {
		return versao;
	}
	
	public void setVersao(Integer versao) {
		this.versao = versao;
	}
	
	@ManyToOne(targetEntity = Dominio.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_dominio", nullable = true)
	public Dominio getDominio() {
		return dominio;
	}
	
	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}
	
	@Column(name = "no_value", length = 50, nullable = false)
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	@Column(name = "no_opcao", length = 255, nullable = false)
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Column(name = "de_sicli", length = 255, nullable = true)
	public String getSicli() {
		return sicli;
	}
	
	public void setSicli(String sicli) {
		this.sicli = sicli;
	}
	
	@Column(name = "de_siric", length = 255, nullable = false)
	public String getSiric() {
		return siric;
	}
	
	public void setSiric(String siric) {
		this.siric = siric;
	}
}
