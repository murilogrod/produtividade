package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb004_dossie_cliente_produto")
public class DossieClienteProduto extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private DossieProduto dossieProduto;
    private TipoRelacionamento tipoRelacionamento;
    private DossieCliente dossieCliente;
    private DossieCliente dossieClienteRelacionado;
    private Integer sequenciaTitularidade;
    // *****************************************
    private Set<InstanciaDocumento> instanciasDocumento;
    private Set<RespostaDossie> respostasDossies;

    public DossieClienteProduto() {
        super();
        this.instanciasDocumento = new HashSet<>();
        this.respostasDossies = new HashSet<>();
    }

    @Id
    @Column(name = "nu_dossie_cliente_produto")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = DossieProduto.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_dossie_produto", nullable = false)
    public DossieProduto getDossieProduto() {
        return dossieProduto;
    }

    public void setDossieProduto(DossieProduto dossieProduto) {
        this.dossieProduto = dossieProduto;
    }

    @ManyToOne(targetEntity = TipoRelacionamento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_tipo_relacionamento", nullable = false)
    public TipoRelacionamento getTipoRelacionamento() {
        return tipoRelacionamento;
    }

    public void setTipoRelacionamento(TipoRelacionamento tipoRelacionamento) {
        this.tipoRelacionamento = tipoRelacionamento;
    }

    @ManyToOne(targetEntity = DossieCliente.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_dossie_cliente", nullable = false)
    public DossieCliente getDossieCliente() {
        return dossieCliente;
    }

    public void setDossieCliente(DossieCliente dossieCliente) {
        this.dossieCliente = dossieCliente;
    }

    @ManyToOne(targetEntity = DossieCliente.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_dossie_cliente_relacionado", nullable = true)
    public DossieCliente getDossieClienteRelacionado() {
        return dossieClienteRelacionado;
    }

    public void setDossieClienteRelacionado(DossieCliente dossieClienteRelacionado) {
        this.dossieClienteRelacionado = dossieClienteRelacionado;
    }

    @Column(name = "nu_sequencia_titularidade", nullable = true)
    public Integer getSequenciaTitularidade() {
        return sequenciaTitularidade;
    }

    public void setSequenciaTitularidade(Integer sequenciaTitularidade) {
        this.sequenciaTitularidade = sequenciaTitularidade;
    }

    // *****************************************
    @OneToMany(targetEntity = InstanciaDocumento.class, mappedBy = "dossieClienteProduto", fetch = FetchType.LAZY)
    public Set<InstanciaDocumento> getInstanciasDocumento() {
        return instanciasDocumento;
    }

    public void setInstanciasDocumento(Set<InstanciaDocumento> instanciasDocumento) {
        this.instanciasDocumento = instanciasDocumento;
    }
    
    @OneToMany(targetEntity = RespostaDossie.class, mappedBy = "dossieClienteProduto", fetch = FetchType.LAZY)
    public Set<RespostaDossie> getRespostasDossies() {
        return respostasDossies;
    }

    public void setRespostasDossies(Set<RespostaDossie> respostasDossies) {
        this.respostasDossies = respostasDossies;
    }

    // *****************************************
    public boolean addInstanciasDocumento(InstanciaDocumento... instanciasDocumento) {
        return this.instanciasDocumento.addAll(Arrays.asList(instanciasDocumento));
    }

    public boolean removeInstanciasDocumento(InstanciaDocumento... instanciasDocumentos) {
        return this.instanciasDocumento.removeAll(Arrays.asList(instanciasDocumentos));
    }
    
    public boolean addRespostaDossie(RespostaDossie... respostasDossies) {
        return this.respostasDossies.addAll(Arrays.asList(respostasDossies));
    }

    public boolean removeRespostaDossie(RespostaDossie... respostasDossies) {
        return this.respostasDossies.removeAll(Arrays.asList(respostasDossies));
    }

    // *****************************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DossieClienteProduto other = (DossieClienteProduto) obj;
        if (other.id == null) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

}
