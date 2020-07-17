package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.caixa.simtr.modelo.enumerator.TipoDispositivoEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb029_campo_apresentacao")
public class CampoApresentacao extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private CampoFormulario campoFormulario;
    private Integer largura;
    private TipoDispositivoEnum tipoDispositivoEnum;

    // ************************************
    public CampoApresentacao() {
        super();
    }

    public CampoApresentacao(TipoDispositivoEnum tipoDispositivoEnum, Integer largura) {
        super();
        this.tipoDispositivoEnum = tipoDispositivoEnum;
        this.largura = largura;
    }
    
    @Id
    @Column(name = "nu_campo_apresentacao")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = CampoFormulario.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_campo_formulario", nullable = false)
    public CampoFormulario getCampoFormulario() {
        return campoFormulario;
    }

    public void setCampoFormulario(CampoFormulario campoEntrada) {
        this.campoFormulario = campoEntrada;
    }

    @Column(name = "nu_largura", nullable = false)
    public Integer getLargura() {
        return largura;
    }

    public void setLargura(Integer largura) {
        this.largura = largura;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_dispositivo", nullable = false, length = 1)
    public TipoDispositivoEnum getTipoDispositivoEnum() {
        return tipoDispositivoEnum;
    }

    public void setTipoDispositivoEnum(TipoDispositivoEnum tipoDispositivo) {
        this.tipoDispositivoEnum = tipoDispositivo;
    }
    // ************************************

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.id);
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
        final CampoApresentacao other = (CampoApresentacao) obj;
        return Objects.equals(this.id, other.id);
    }
}
