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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb027_campo_entrada")
public class CampoEntrada extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private TipoCampoEnum tipo;
    private Boolean chave;
    private String label;
    private String mascara;
    private String placeholder;
    private Integer tamanhoMinimo;
    private Integer tamanhoMaximo;
    // ************************************
    private Set<OpcaoCampo> opcoesCampo;
    private Set<CampoFormulario> camposFormulario;

    public CampoEntrada() {
        super();
        this.opcoesCampo = new HashSet<>();
        this.camposFormulario = new HashSet<>();
        this.chave = false;
    }

    @Id
    @Column(name = "nu_campo_entrada")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_tipo", nullable = false, length = 20)
    public TipoCampoEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoCampoEnum tipo) {
        this.tipo = tipo;
    }

    @Column(name = "ic_chave", nullable = false)
    public Boolean getChave() {
        return chave;
    }

    public void setChave(Boolean chave) {
        this.chave = chave;
    }

    @Column(name = "no_label", nullable = false, length = 50)
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Column(name = "de_mascara", nullable = true, length = 100)
    public String getMascara() {
        return mascara;
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    @Column(name = "de_placeholder", nullable = true, length = 100)
    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Column(name = "nu_tamanho_minimo", nullable = true)
    public Integer getTamanhoMinimo() {
        return tamanhoMinimo;
    }

    public void setTamanhoMinimo(Integer tamanhoMinimo) {
        this.tamanhoMinimo = tamanhoMinimo;
    }

    @Column(name = "nu_tamanho_maximo", nullable = true)
    public Integer getTamanhoMaximo() {
        return tamanhoMaximo;
    }

    public void setTamanhoMaximo(Integer tamanhoMaximo) {
        this.tamanhoMaximo = tamanhoMaximo;
    }
    
     // ************************************
    @OneToMany(targetEntity = OpcaoCampo.class, mappedBy = "campoEntrada", fetch = FetchType.LAZY)
    public Set<OpcaoCampo> getOpcoesCampo() {
        return opcoesCampo;
    }

    public void setOpcoesCampo(Set<OpcaoCampo> opcoesCampo) {
        this.opcoesCampo = opcoesCampo;
    }

    @OneToMany(targetEntity = CampoFormulario.class, mappedBy = "campoEntrada", fetch = FetchType.LAZY)
    public Set<CampoFormulario> getCamposFormulario() {
        return camposFormulario;
    }

    public void setCamposFormulario(Set<CampoFormulario> camposApresentacao) {
        this.camposFormulario = camposApresentacao;
    }

    // ************************************
    public boolean addOpoesCampo(OpcaoCampo... opcoesCampo) {
        return this.opcoesCampo.addAll(Arrays.asList(opcoesCampo));
    }

    public boolean removeOpcoesCampo(OpcaoCampo... opcoesCampo) {
        return this.opcoesCampo.removeAll(Arrays.asList(opcoesCampo));
    }

    public boolean addCamposFormulario(CampoFormulario... camposApresentacao) {
        return this.camposFormulario.addAll(Arrays.asList(camposApresentacao));
    }

    public boolean removeCamposFormulario(CampoFormulario... camposApresentacao) {
        return this.camposFormulario.removeAll(Arrays.asList(camposApresentacao));
    }

    // ************************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final CampoEntrada other = (CampoEntrada) obj;
        return Objects.equals(this.id, other.id);
    }

	
}
