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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb028_opcao_campo")
public class OpcaoCampo extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private CampoEntrada campoEntrada;
    private String value;
    private String nome;
    private Boolean ativo;
    //*************************************
    private Set<RespostaDossie> respostasDossie;

    public OpcaoCampo() {
        super();
        this.respostasDossie = new HashSet<>();
        this.ativo = true;
    }
    
    public OpcaoCampo(String value, String descricao, Boolean ativo) {
        super();
        this.respostasDossie = new HashSet<>();
        this.value = value;
        this.nome = descricao;
        this.ativo = ativo;
    }

    @Id
    @Column(name = "nu_opcao_campo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = CampoEntrada.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_campo_entrada", nullable = false)
    public CampoEntrada getCampoEntrada() {
        return campoEntrada;
    }

    public void setCampoEntrada(CampoEntrada campoEntrada) {
        this.campoEntrada = campoEntrada;
    }

    @Column(name = "no_value", nullable = false, length = 50)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "no_opcao", nullable = false, length = 255)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "ic_ativo", nullable = false)
    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    //*************************************
    @ManyToMany(targetEntity = RespostaDossie.class, mappedBy = "opcoesCampo", fetch = FetchType.LAZY)
    public Set<RespostaDossie> getRespostasDossie() {
        return respostasDossie;
    }

    public void setRespostasDossie(Set<RespostaDossie> respostasDossie) {
        this.respostasDossie = respostasDossie;
    }

    //*************************************
    public boolean addRespostasDossie(RespostaDossie... respostasDossie) {
        return this.respostasDossie.addAll(Arrays.asList(respostasDossie));
    }

    public boolean removeRespostasProcesso(RespostaDossie... respostasDossie) {
        return this.respostasDossie.removeAll(Arrays.asList(respostasDossie));
    }

    //*************************************    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
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
        final OpcaoCampo other = (OpcaoCampo) obj;
        return Objects.equals(this.id, other.id);
    }
}
