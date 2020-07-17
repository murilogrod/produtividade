package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.pedesgo.arquitetura.anotacao.IgnoreClassLazyInterceptor;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb001_pessoa_fisica")
@PrimaryKeyJoinColumn(name = "nu_dossie_cliente")
@IgnoreClassLazyInterceptor
public class DossieClientePF extends DossieCliente implements Serializable {

    private static final long serialVersionUID = 1L;

    private Calendar dataNascimento;
    private String nomeMae;

    // *****************************************
    public DossieClientePF() {
        super();
        this.setTipoPessoa(TipoPessoaEnum.F);
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "dt_nascimento", nullable = false)
    public Calendar getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Calendar dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Column(name = "no_mae", nullable = false, length = 255)
    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
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
        final DossieClientePF other = (DossieClientePF) obj;
        if (!Objects.equals(this.nomeMae, other.nomeMae)) {
            return false;
        }
        return !Objects.equals(this.dataNascimento, other.dataNascimento);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 47 * hash + Objects.hashCode(this.dataNascimento);
        hash = 47 * hash + Objects.hashCode(this.nomeMae);
        return hash;
    }
}
