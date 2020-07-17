package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb019_campo_formulario", indexes = {
    @Index(name = "ix_mtrtb019_02", unique = true, columnList = "nu_identificador_bpm")
})
public class CampoFormulario extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Processo processo;
    private Processo processoFase;
    private TipoRelacionamento tipoRelacionamento;
    private Produto produto;
    private Garantia garantia;
    private CampoEntrada campoEntrada;
    private Boolean obrigatorio;
    private String expressaoInterface;
    private Boolean ativo;
    private Integer ordemApresentacao;
    private Integer identificadorBPM;
    private String nomeCampo;
    private String nomeAtributoSICLI;
    private String nomeObjetoSICLI;
    private TipoAtributoEnum tipoAtributoSicliEnum;
    private AtributoIntegracao atributoIntegracao;

    // ********************************************
    private Set<CampoApresentacao> camposApresentacao;
    private Set<RespostaDossie> respostasDossie;

    public CampoFormulario() {
        super();
        this.camposApresentacao = new HashSet<>();
    }

    @Id
    @Column(name = "nu_campo_formulario")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Processo.class)
    @JoinColumn(name = "nu_processo", nullable = false)
    public Processo getProcesso() {
        return processo;
    }

    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    @ManyToOne(targetEntity = Processo.class)
    @JoinColumn(name = "nu_processo_fase", nullable = true)
    public Processo getProcessoFase() {
        return processoFase;
    }

    public void setProcessoFase(Processo processoFase) {
        this.processoFase = processoFase;
    }

    @ManyToOne(targetEntity = TipoRelacionamento.class)
    @JoinColumn(name = "nu_tipo_relacionamento", nullable = true)
    public TipoRelacionamento getTipoRelacionamento() {
        return tipoRelacionamento;
    }

    public void setTipoRelacionamento(TipoRelacionamento tipoRelacionamento) {
        this.tipoRelacionamento = tipoRelacionamento;
    }

    @ManyToOne(targetEntity = Produto.class)
    @JoinColumn(name = "nu_produto", nullable = true)
    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @ManyToOne(targetEntity = Garantia.class)
    @JoinColumn(name = "nu_garantia", nullable = false)
    public Garantia getGarantia() {
        return garantia;
    }

    public void setGarantia(Garantia garantia) {
        this.garantia = garantia;
    }

    @ManyToOne(targetEntity = CampoEntrada.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_campo_entrada", nullable = false)
    public CampoEntrada getCampoEntrada() {
        return campoEntrada;
    }

    public void setCampoEntrada(CampoEntrada campoEntrada) {
        this.campoEntrada = campoEntrada;
    }

    @Column(name = "ic_obrigatorio", nullable = false)
    public Boolean getObrigatorio() {
        return this.obrigatorio;
    }

    public void setObrigatorio(Boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    @Column(name = "de_expressao", nullable = true, columnDefinition = "text")
    public String getExpressaoInterface() {
        return expressaoInterface;
    }

    public void setExpressaoInterface(String expressaoInterface) {
        this.expressaoInterface = expressaoInterface;
    }

    @Column(name = "ic_ativo", nullable = false)
    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Column(name = "nu_ordem", nullable = false)
    public Integer getOrdemApresentacao() {
        return this.ordemApresentacao;
    }

    public void setOrdemApresentacao(Integer ordemApresentacao) {
        this.ordemApresentacao = ordemApresentacao;
    }

    @Column(name = "nu_identificador_bpm", nullable = true)
    public Integer getIdentificadorBPM() {
        return identificadorBPM;
    }

    public void setIdentificadorBPM(Integer identificadorBPM) {
        this.identificadorBPM = identificadorBPM;
    }

    @Column(name = "no_campo", nullable = true, length = 100)
    public String getNomeCampo() {
        return nomeCampo;
    }

    public void setNomeCampo(String nomeCampo) {
        this.nomeCampo = nomeCampo;
    }
    
    @Column(name = "no_atributo_sicli", length = 100, nullable = true)
    public String getNomeAtributoSICLI() {
        return nomeAtributoSICLI;
    }

    public void setNomeAtributoSICLI(String nomeAtributoSICLI) {
        this.nomeAtributoSICLI = nomeAtributoSICLI;
    }

    @Column(name = "no_objeto_sicli", length = 100, nullable = true)
    public String getNomeObjetoSICLI() {
        return nomeObjetoSICLI;
    }
    
    public void setNomeObjetoSICLI(String nomeObjetoSICLI) {
        this.nomeObjetoSICLI = nomeObjetoSICLI;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_tipo_sicli", length = 50, nullable = true)
    public TipoAtributoEnum getTipoAtributoSicliEnum() {
        return tipoAtributoSicliEnum;
    }
    
    public void setTipoAtributoSicliEnum(TipoAtributoEnum tipoAtributoSicliEnum) {
        this.tipoAtributoSicliEnum = tipoAtributoSicliEnum;
    }
    
    @ManyToOne(targetEntity = AtributoIntegracao.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_atributo_integracao", nullable = true)
    public AtributoIntegracao getAtributoIntegracao() {
        return atributoIntegracao;
    }

    public void setAtributoIntegracao(AtributoIntegracao atributoIntegracao) {
        this.atributoIntegracao = atributoIntegracao;
    }

    // ********************************************
    @OneToMany(targetEntity = CampoApresentacao.class, mappedBy = "campoFormulario", fetch = FetchType.LAZY)
    public Set<CampoApresentacao> getCamposApresentacao() {
        return camposApresentacao;
    }

    public void setCamposApresentacao(Set<CampoApresentacao> camposApresentacao) {
        this.camposApresentacao = camposApresentacao;
    }

    @OneToMany(targetEntity = RespostaDossie.class, mappedBy = "campoFormulario", fetch = FetchType.LAZY)
    public Set<RespostaDossie> getRespostasDossie() {
        return respostasDossie;
    }

    public void setRespostasDossie(Set<RespostaDossie> respostasDossie) {
        this.respostasDossie = respostasDossie;
    }

    // ********************************************
    public boolean addCamposApresentacao(CampoApresentacao... camposApresentacao) {
        return this.camposApresentacao.addAll(Arrays.asList(camposApresentacao));
    }

    public boolean removeCamposApresentacao(CampoApresentacao... camposApresentacao) {
        return this.camposApresentacao.removeAll(Arrays.asList(camposApresentacao));
    }

    public boolean addRespostaDossie(RespostaDossie... respostasDossie) {
        return this.respostasDossie.addAll(Arrays.asList(respostasDossie));
    }

    public boolean removeRespostaDossie(RespostaDossie... respostasDossie) {
        return this.respostasDossie.removeAll(Arrays.asList(respostasDossie));
    }

    // ********************************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final CampoFormulario other = (CampoFormulario) obj;
        return Objects.equals(this.id, other.id);
    }

}
