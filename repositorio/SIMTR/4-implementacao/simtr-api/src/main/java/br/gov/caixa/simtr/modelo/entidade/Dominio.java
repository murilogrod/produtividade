package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb104_dominio")
public class Dominio extends GenericEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer versao;
	private String dominio;
	private TipoCampoEnum tipo;
	private Boolean multiplos;
	private Calendar dataHoraUltimaAlteracao;
	private Set<OpcaoDominio> opcoesDominio; 
	
	public Dominio() {
		super();
		this.multiplos = true;
		this.dataHoraUltimaAlteracao = Calendar.getInstance();
		this.opcoesDominio = new HashSet<>();
	}
	
	@Id
    @Column(name = "nu_dominio")
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
	
	@Column(name = "no_dominio", length = 100, nullable = false)
	public String getDominio() {
		return dominio;
	}
	
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}
	
	@Enumerated(EnumType.STRING)
    @Column(name = "ic_tipo", length = 20, nullable = false)
	public TipoCampoEnum getTipo() {
		return tipo;
	}
	
	public void setTipo(TipoCampoEnum tipo) {
		this.tipo = tipo;
	}
	
	@Column(name = "ic_multiplos", nullable = false)
	public Boolean getMultiplos() {
		return multiplos;
	}
	
	public void setMultiplos(Boolean multiplos) {
		this.multiplos = multiplos;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_ultima_alteracao")
	public Calendar getDataHoraUltimaAlteracao() {
		return dataHoraUltimaAlteracao;
	}
	
	public void setDataHoraUltimaAlteracao(Calendar dataHoraUltimaAlteracao) {
		this.dataHoraUltimaAlteracao = dataHoraUltimaAlteracao;
	}

	@OneToMany(targetEntity = OpcaoDominio.class, mappedBy = "dominio", fetch = FetchType.LAZY)
	public Set<OpcaoDominio> getOpcoesDominio() {
		return opcoesDominio;
	}

	public void setOpcoesDominio(Set<OpcaoDominio> opcoesDominio) {
		this.opcoesDominio = opcoesDominio;
	}
}
