package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;
import java.util.Objects;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb100_autorizacao", indexes = {
    @Index(name = "ix_mtrtb100_01", unique = true, columnList = "co_autorizacao")
})
public class Autorizacao extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long codigoNSU;
    private Long codigoNSUConjunta;
    private Calendar dataHoraRegistro;
    private Calendar dataHoraConclusao;
    private Calendar dataHoraInformeNegocio;
    private String protocoloNegocio;
    private Calendar dataHoraInformeECM;
    private String protocoloECM;
    private Integer produtoOperacao;
    private Integer produtoModalidade;
    private String produtoNome;
    private Long cpfCnpj;
    private TipoPessoaEnum tipoPessoa;
    private String siglaCanalSolicitacao;

    // ************************************
    private Set<DocumentoAutorizacao> documentosAutorizacao;
    private Set<AutorizacaoOrientacao> autorizacaoOrientacoes;

    public Autorizacao() {
        super();
        this.documentosAutorizacao = new HashSet<>();
        this.autorizacaoOrientacoes = new HashSet<>();
    }

    @Id
    @Column(name = "nu_autorizacao")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "co_autorizacao", nullable = false)
    public Long getCodigoNSU() {
        return codigoNSU;
    }

    public void setCodigoNSU(Long codigoNSU) {
        this.codigoNSU = codigoNSU;
    }

    @Column(name = "co_autorizacao_conjunta", nullable = true)
    public Long getCodigoNSUConjunta() {
        return codigoNSUConjunta;
    }

    public void setCodigoNSUConjunta(Long codigoNSUConjunta) {
        this.codigoNSUConjunta = codigoNSUConjunta;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_registro", nullable = false)
    public Calendar getDataHoraRegistro() {
        return dataHoraRegistro;
    }

    public void setDataHoraRegistro(Calendar dataHoraRegistro) {
        this.dataHoraRegistro = dataHoraRegistro;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_conclusao", nullable = true)
    public Calendar getDataHoraConclusao() {
        return dataHoraConclusao;
    }

    public void setDataHoraConclusao(Calendar dataHoraConclusao) {
        this.dataHoraConclusao = dataHoraConclusao;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_informe_negocio", nullable = true)
    public Calendar getDataHoraInformeNegocio() {
        return dataHoraInformeNegocio;
    }

    public void setDataHoraInformeNegocio(Calendar dataHoraInformeNegocio) {
        this.dataHoraInformeNegocio = dataHoraInformeNegocio;
    }

    @Column(name = "co_protocolo_negocio", length = 100, nullable = true)
    public String getProtocoloNegocio() {
        return protocoloNegocio;
    }

    public void setProtocoloNegocio(String protocoloNegocio) {
        this.protocoloNegocio = protocoloNegocio;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_informe_ecm", nullable = true)
    public Calendar getDataHoraInformeECM() {
        return dataHoraInformeECM;
    }

    public void setDataHoraInformeECM(Calendar dataHoraInformeECM) {
        this.dataHoraInformeECM = dataHoraInformeECM;
    }

    @Column(name = "co_protocolo_ecm", length = 100, nullable = true)
    public String getProtocoloECM() {
        return protocoloECM;
    }

    public void setProtocoloECM(String protocoloECM) {
        this.protocoloECM = protocoloECM;
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

    @Column(name = "no_produto", length = 255, nullable = false)
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

    @Column(name = "sg_canal_solicitacao", nullable = false, length = 10)
    public String getSiglaCanalSolicitacao() {
        return siglaCanalSolicitacao;
    }

    public void setSiglaCanalSolicitacao(String siglaCanalSolicitacao) {
        this.siglaCanalSolicitacao = siglaCanalSolicitacao;
    }

    // ************************************
    @OneToMany(targetEntity = DocumentoAutorizacao.class, mappedBy = "autorizacao", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public Set<DocumentoAutorizacao> getDocumentosAutorizacao() {
        return documentosAutorizacao;
    }

    public void setDocumentosAutorizacao(Set<DocumentoAutorizacao> documentosAutorizacao) {
        this.documentosAutorizacao = documentosAutorizacao;
    }

    @OneToMany(targetEntity = AutorizacaoOrientacao.class, mappedBy = "autorizacao", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public Set<AutorizacaoOrientacao> getAutorizacaoOrientacoes() {
        return autorizacaoOrientacoes;
    }

    public void setAutorizacaoOrientacoes(Set<AutorizacaoOrientacao> autorizacaoOrientacoes) {
        this.autorizacaoOrientacoes = autorizacaoOrientacoes;
    }

    // ************************************
    public boolean addDocumentosAutorizacao(DocumentoAutorizacao... documentosAutorizacao) {
        return this.documentosAutorizacao.addAll(Arrays.asList(documentosAutorizacao));
    }

    public boolean removeDocumentosAutorizacao(DocumentoAutorizacao... documentosAutorizacao) {
        return this.documentosAutorizacao.removeAll(Arrays.asList(documentosAutorizacao));
    }

    public boolean addAutorizacaoOrientacoes(AutorizacaoOrientacao... autorizacaoOrientacoes) {
        return this.autorizacaoOrientacoes.addAll(Arrays.asList(autorizacaoOrientacoes));
    }

    public boolean removeAutorizacaoOrientacoes(AutorizacaoOrientacao... autorizacaoOrientacoes) {
        return this.autorizacaoOrientacoes.removeAll(Arrays.asList(autorizacaoOrientacoes));
    }

    // ************************************
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.codigoNSU);
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
        final Autorizacao other = (Autorizacao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.codigoNSU, other.codigoNSU);
    }

}
