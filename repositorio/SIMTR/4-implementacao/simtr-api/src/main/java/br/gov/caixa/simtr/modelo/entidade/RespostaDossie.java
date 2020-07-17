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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb030_resposta_dossie")
public class RespostaDossie extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private DossieProduto dossieProduto;
    private CampoFormulario campoFormulario;
    private Processo processoFase;
    private DossieClienteProduto dossieClienteProduto;
    private ProdutoDossie produtoDossie;
    private GarantiaInformada garantiaInformada;
    private String respostaAberta;
    // ************************************
    private Set<OpcaoCampo> opcoesCampo;

    public RespostaDossie() {
        super();
        this.opcoesCampo = new HashSet<>();
    }

    @Id
    @Column(name = "nu_resposta_dossie")
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

    @ManyToOne(targetEntity = CampoFormulario.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_campo_formulario")
    public CampoFormulario getCampoFormulario() {
        return this.campoFormulario;
    }

    public void setCampoFormulario(CampoFormulario campoFormulario) {
        this.campoFormulario = campoFormulario;
    }

    @ManyToOne(targetEntity = Processo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_processo_fase")
    public Processo getProcessoFase() {
        return processoFase;
    }

    public void setProcessoFase(Processo processoFase) {
        this.processoFase = processoFase;
    }

    @ManyToOne(targetEntity = DossieClienteProduto.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_dossie_cliente_produto")
    public DossieClienteProduto getDossieClienteProduto() {
        return dossieClienteProduto;
    }

    public void setDossieClienteProduto(DossieClienteProduto dossieClienteProduto) {
        this.dossieClienteProduto = dossieClienteProduto;
    }

    @ManyToOne(targetEntity = ProdutoDossie.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_produto_dossie")
    public ProdutoDossie getProdutoDossie() {
        return produtoDossie;
    }

    public void setProdutoDossie(ProdutoDossie produtoDossie) {
        this.produtoDossie = produtoDossie;
    }

    @ManyToOne(targetEntity = GarantiaInformada.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_garantia_informada")
    public GarantiaInformada getGarantiaInformada() {
        return garantiaInformada;
    }

    public void setGarantiaInformada(GarantiaInformada garantiaInformada) {
        this.garantiaInformada = garantiaInformada;
    }

    @Column(name = "de_resposta", nullable = true, columnDefinition = "text")
    public String getRespostaAberta() {
        return respostaAberta;
    }

    public void setRespostaAberta(String respostaAberta) {
        this.respostaAberta = respostaAberta;
    }

    // ************************************
    @ManyToMany(targetEntity = OpcaoCampo.class, fetch = FetchType.LAZY)
    @JoinTable(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb031_resposta_opcao", joinColumns = {
        @JoinColumn(name = "nu_resposta_dossie", referencedColumnName = "nu_resposta_dossie")
    }, inverseJoinColumns = {
        @JoinColumn(name = "nu_opcao_campo", referencedColumnName = "nu_opcao_campo")
    })
    public Set<OpcaoCampo> getOpcoesCampo() {
        return opcoesCampo;
    }

    public void setOpcoesCampo(Set<OpcaoCampo> opcoesCampo) {
        this.opcoesCampo = opcoesCampo;
    }

    // ************************************
    public boolean addOpoesCampo(OpcaoCampo... opcoesCampo) {
        return this.opcoesCampo.addAll(Arrays.asList(opcoesCampo));
    }

    public boolean removeOpcoesCampo(OpcaoCampo... opcoesCampo) {
        return this.opcoesCampo.removeAll(Arrays.asList(opcoesCampo));
    }

    // ************************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final RespostaDossie other = (RespostaDossie) obj;
        return Objects.equals(this.id, other.id);
    }

}
