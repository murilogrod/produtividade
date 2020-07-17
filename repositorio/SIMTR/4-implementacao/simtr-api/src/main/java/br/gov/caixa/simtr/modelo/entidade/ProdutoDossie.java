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
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb024_produto_dossie")
public class ProdutoDossie extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private DossieProduto dossieProduto;
    private Produto produto;

    // ************************************
    private Set<RespostaDossie> respostasDossies;
    
    public ProdutoDossie() {
        super();
        this.respostasDossies = new HashSet<>();
    }
    
    @Id
    @Column(name = "nu_produto_dossie")
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

    @ManyToOne(targetEntity = Produto.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_produto", nullable = false)
    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    // ************************************
    @OneToMany(targetEntity = RespostaDossie.class, mappedBy = "produtoDossie", fetch = FetchType.LAZY)
    public Set<RespostaDossie> getRespostasDossies() {
        return respostasDossies;
    }

    public void setRespostasDossies(Set<RespostaDossie> respostasDossies) {
        this.respostasDossies = respostasDossies;
    }
    
    // ************************************
    public boolean addRespostaDossie(RespostaDossie... respostasDossies) {
        return this.respostasDossies.addAll(Arrays.asList(respostasDossies));
    }

    public boolean removeRespostaDossie(RespostaDossie... respostasDossies) {
        return this.respostasDossies.removeAll(Arrays.asList(respostasDossies));
    }    
    
    // ************************************
    @Override
    public int hashCode() {
        int hash = 9;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final ProdutoDossie other = (ProdutoDossie) obj;
        return Objects.equals(this.id, other.id);
    }
}
