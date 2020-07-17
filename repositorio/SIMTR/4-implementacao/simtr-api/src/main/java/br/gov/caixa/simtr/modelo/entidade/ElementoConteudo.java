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
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb032_elemento_conteudo", indexes = {
    @Index(name = "ix_mtrtb032_01", unique = true, columnList = "nu_processo, no_campo"),
    @Index(name = "ix_mtrtb032_02", unique = true, columnList = "nu_produto, no_campo"),
    @Index(name = "ix_mtrtb032_03", unique = true, columnList = "nu_processo, nu_tipo_documento"),
    @Index(name = "ix_mtrtb032_04", unique = true, columnList = "nu_produto, nu_tipo_documento"),
    @Index(name = "ix_mtrtb032_05", unique = true, columnList = "nu_identificador_bpm")
})
public class ElementoConteudo extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private ElementoConteudo elementoVinculador;
    private Produto produto;
    private Processo processo;
    private TipoDocumento tipoDocumento;
    private Boolean obrigatorio;
    private Integer quantidadeObrigatorios;
    private String nomeCampo;
    private String expressao;
    private Integer identificadorBPM;
    private String nomeElemento;

    // ************************************
    private Set<ElementoConteudo> elementosVinculados;
    private Set<InstanciaDocumento> instanciasDocumento;

    public ElementoConteudo() {
        super();
        this.elementosVinculados = new HashSet<>();
        this.instanciasDocumento = new HashSet<>();
        this.obrigatorio = false;
    }

    @Id
    @Column(name = "nu_elemento_conteudo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = ElementoConteudo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_elemento_vinculador", nullable = true)
    public ElementoConteudo getElementoVinculador() {
        return elementoVinculador;
    }

    public void setElementoVinculador(ElementoConteudo elementoVinculado) {
        this.elementoVinculador = elementoVinculado;
    }

    @ManyToOne(targetEntity = Produto.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_produto", nullable = true)
    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @ManyToOne(targetEntity = Processo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_processo", nullable = true)
    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    @ManyToOne(targetEntity = TipoDocumento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_tipo_documento", nullable = false)
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Column(name = "ic_obrigatorio", nullable = false)
    public Boolean getObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(Boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    @Column(name = "qt_elemento_obrigatorio", nullable = true)
    public Integer getQuantidadeObrigatorios() {
        return quantidadeObrigatorios;
    }

    public void setQuantidadeObrigatorios(Integer quantidadeObrigatorios) {
        this.quantidadeObrigatorios = quantidadeObrigatorios;
    }

    @Column(name = "no_campo", nullable = false, length = 100)
    public String getNomeCampo() {
        return nomeCampo;
    }

    public void setNomeCampo(String nomeCampo) {
        this.nomeCampo = nomeCampo;
    }

    @Column(name = "de_expressao", nullable = true, columnDefinition = "text")
    public String getExpressao() {
        return expressao;
    }

    public void setExpressao(String expressao) {
        this.expressao = expressao;
    }
    
    @Column(name = "nu_identificador_bpm", nullable = true)
    public Integer getIdentificadorBPM() {
        return identificadorBPM;
    }

    public void setIdentificadorBPM(Integer identificadorBPM) {
        this.identificadorBPM = identificadorBPM;
    }

    @Column(name = "no_elemento", length = 100, nullable = true)
    public String getNomeElemento() {
        return nomeElemento;
    }

    public void setNomeElemento(String nomeElemento) {
        this.nomeElemento = nomeElemento;
    }

    // ************************************
    @OneToMany(targetEntity = ElementoConteudo.class, mappedBy = "elementoVinculador", fetch = FetchType.EAGER)
    public Set<ElementoConteudo> getElementosVinculados() {
        return elementosVinculados;
    }

    public void setElementosVinculados(Set<ElementoConteudo> elementosVinculados) {
        this.elementosVinculados = elementosVinculados;
    }

    @OneToMany(targetEntity = InstanciaDocumento.class, mappedBy = "elementoConteudo", fetch = FetchType.LAZY)
    public Set<InstanciaDocumento> getInstanciasDocumento() {
        return instanciasDocumento;
    }

    public void setInstanciasDocumento(Set<InstanciaDocumento> instanciasDocumento) {
        this.instanciasDocumento = instanciasDocumento;
    }

    // ************************************
    public boolean addElementosVinculados(ElementoConteudo... elementosVinculados) {
        return this.elementosVinculados.addAll(Arrays.asList(elementosVinculados));
    }

    public boolean removeElementosVinculados(ElementoConteudo... elementosVinculados) {
        return this.elementosVinculados.removeAll(Arrays.asList(elementosVinculados));
    }

    public boolean addInstanciasDocumento(InstanciaDocumento... instanciasDocumento) {
        return this.instanciasDocumento.addAll(Arrays.asList(instanciasDocumento));
    }

    public boolean removeInstanciasDocumento(InstanciaDocumento... instanciasDocumento) {
        return this.instanciasDocumento.removeAll(Arrays.asList(instanciasDocumento));
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
        final ElementoConteudo other = (ElementoConteudo) obj;
        return Objects.equals(this.id, other.id);
    }
}
