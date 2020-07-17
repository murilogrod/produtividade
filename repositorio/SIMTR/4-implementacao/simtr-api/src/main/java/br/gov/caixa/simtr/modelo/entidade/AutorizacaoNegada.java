package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb102_autorizacao_negada")
public class AutorizacaoNegada extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Calendar dataHoraRegistro;
    private Integer produtoOperacao;
    private Integer produtoModalidade;
    private String produtoNome;
    private Long cpfCnpj;
    private TipoPessoaEnum tipoPessoa;
    private String motivo;
    private String siglaCanalSolicitacao;

    //*****************************************
    @Id
    @Column(name = "nu_autorizacao_negada")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_registro", nullable = false)
    public Calendar getDataHoraRegistro() {
        return dataHoraRegistro;
    }

    public void setDataHoraRegistro(Calendar dataHoraRegistro) {
        this.dataHoraRegistro = dataHoraRegistro;
    }

    @Column(name = "nu_operacao", nullable = false)
    public Integer getProdutoOperacao() {
        return produtoOperacao;
    }

    public void setProdutoOperacao(Integer produtoOperacao) {
        this.produtoOperacao = produtoOperacao;
    }

    @Column(name = "nu_modalidade", nullable = false)
    public Integer getProdutoModalidade() {
        return produtoModalidade;
    }

    public void setProdutoModalidade(Integer produtoModalidade) {
        this.produtoModalidade = produtoModalidade;
    }

    @Column(name = "no_produto", nullable = false, length = 255)
    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    @Column(name = "nu_cpf_cnpj", nullable = false)
    public Long getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(Long cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_tipo_pessoa", nullable = false, length = 1)
    public TipoPessoaEnum getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoaEnum tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    @Column(name = "de_motivo", nullable = false, columnDefinition = "text")
    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    @Column(name = "sg_canal_solicitacao", nullable = false, length = 100)
    public String getSiglaCanalSolicitacao() {
        return siglaCanalSolicitacao;
    }

    public void setSiglaCanalSolicitacao(String siglaCanalSolicitacao) {
        this.siglaCanalSolicitacao = siglaCanalSolicitacao;
    }

    //*****************************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final AutorizacaoNegada other = (AutorizacaoNegada) obj;
        
        return Objects.equals(this.id, other.id);
    }
}
