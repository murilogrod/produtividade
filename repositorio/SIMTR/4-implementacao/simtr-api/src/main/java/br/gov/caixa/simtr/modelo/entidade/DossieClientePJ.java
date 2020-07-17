package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.pedesgo.arquitetura.anotacao.IgnoreClassLazyInterceptor;
import br.gov.caixa.simtr.modelo.enumerator.PortePessoaJuridicaEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb001_pessoa_juridica")
@PrimaryKeyJoinColumn(name = "nu_dossie_cliente")
@IgnoreClassLazyInterceptor
public class DossieClientePJ extends DossieCliente implements Serializable {

    private static final long serialVersionUID = 1L;

    private String razaoSocial;
    private Calendar dataFundacao;
    private PortePessoaJuridicaEnum porte;
    private Boolean conglomerado;

    // *****************************************
    public DossieClientePJ() {
        super();
        this.setTipoPessoa(TipoPessoaEnum.J);
        this.porte = PortePessoaJuridicaEnum.DEMAIS;
    }

    @Column(name = "no_razao_social", nullable = false, length = 255)
    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "dt_fundacao", nullable = true)
    public Calendar getDataFundacao() {
        return dataFundacao;
    }

    public void setDataFundacao(Calendar dataFundacao) {
        this.dataFundacao = dataFundacao;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_porte", nullable = true, length = 10)
    public PortePessoaJuridicaEnum getPorte() {
        return porte;
    }

    public void setPorte(PortePessoaJuridicaEnum porte) {
        this.porte = porte;
    }
    
    @Column(name = "ic_conglomerado", nullable = true)
    public Boolean getConglomerado() {
        return conglomerado;
    }

    public void setConglomerado(Boolean conglomerado) {
        this.conglomerado = conglomerado;
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
        final DossieClientePJ other = (DossieClientePJ) obj;
        if (!Objects.equals(this.razaoSocial, other.razaoSocial)) {
            return false;
        }
        if (!Objects.equals(this.dataFundacao, other.dataFundacao)) {
            return false;
        }
        return Objects.equals(this.conglomerado, other.conglomerado);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.razaoSocial);
        hash = 97 * hash + Objects.hashCode(this.dataFundacao);
        hash = 97 * hash + Objects.hashCode(this.conglomerado);
        return hash;
    }
}
