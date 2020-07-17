package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtr.modelo.enumerator.CanalCaixaEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb006_canal", indexes = {
    @Index(name = "ix_mtrtb006_02", unique = true, columnList = "no_cliente_sso")
})
public class Canal extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String sigla;
    private String descricao;
    private CanalCaixaEnum canalCaixa;
    private String clienteSSO;
    private Boolean janelaExtracaoM0;
    private Boolean janelaExtracaoM30;
    private Boolean janelaExtracaoM60;
    private Boolean indicadorAvaliacaoAutenticidade;
    private Boolean indicadorAtualizacaoDocumento;
    private Boolean indicadorOutorgaReceita;
    private Boolean indicadorOutorgaCadastroCaixa;
    private Boolean indicadorOutorgaDossieDigital;
    private Boolean indicadorOutorgaApimanager;
    private Boolean indicadorOutorgaSiric;
    private String urlCallbackDocumento;
    private String urlCallbackDossie;
    private Integer unidadeCallbackDossie;
    private Calendar dataHoraUltimaAlteracao;
    private String token;
    
    // ************************************
    private Set<Documento> documentos;

    public Canal() {
        super();
        this.dataHoraUltimaAlteracao = Calendar.getInstance();
        this.documentos = new HashSet<>();
    }

    @Id
    @Column(name = "nu_canal")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "sg_canal", length = 10, nullable = false)
    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @Column(name = "de_canal", length = 255, nullable = false)
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_canal_caixa", nullable = false, length = 3)
    public CanalCaixaEnum getCanalCaixa() {
        return canalCaixa;
    }

    public void setCanalCaixa(CanalCaixaEnum canalCaixa) {
        this.canalCaixa = canalCaixa;
    }

    @Column(name = "no_cliente_sso", length = 20, nullable = false)
    public String getClienteSSO() {
        return clienteSSO;
    }

    public void setClienteSSO(String clienteSSO) {
        this.clienteSSO = clienteSSO;
    }

    @Column(name = "ic_janela_extracao_m0", nullable = false)
    public Boolean getJanelaExtracaoM0() {
        return janelaExtracaoM0;
    }

    public void setJanelaExtracaoM0(Boolean janelaExtracaoM0) {
        this.janelaExtracaoM0 = janelaExtracaoM0;
    }

    @Column(name = "ic_janela_extracao_m30", nullable = false)
    public Boolean getJanelaExtracaoM30() {
        return janelaExtracaoM30;
    }

    public void setJanelaExtracaoM30(Boolean janelaExtracaoM30) {
        this.janelaExtracaoM30 = janelaExtracaoM30;
    }

    @Column(name = "ic_janela_extracao_m60", nullable = false)
    public Boolean getJanelaExtracaoM60() {
        return janelaExtracaoM60;
    }

    public void setJanelaExtracaoM60(Boolean janelaExtracaoM60) {
        this.janelaExtracaoM60 = janelaExtracaoM60;
    }

    @Column(name = "ic_avaliacao_autenticidade", nullable = false)
    public Boolean getIndicadorAvaliacaoAutenticidade() {
        return indicadorAvaliacaoAutenticidade;
    }

    public void setIndicadorAvaliacaoAutenticidade(Boolean indicadorAvaliacaoAutenticidade) {
        this.indicadorAvaliacaoAutenticidade = indicadorAvaliacaoAutenticidade;
    }

    @Column(name = "ic_atualizacao_documento", nullable = false)
    public Boolean getIndicadorAtualizacaoDocumento() {
        return indicadorAtualizacaoDocumento;
    }

    public void setIndicadorAtualizacaoDocumento(Boolean indicadorAtualizacaoDocumento) {
        this.indicadorAtualizacaoDocumento = indicadorAtualizacaoDocumento;
    }

    @Column(name = "ic_outorga_receita", nullable = false)
    public Boolean getIndicadorOutorgaReceita() {
        return indicadorOutorgaReceita;
    }

    public void setIndicadorOutorgaReceita(Boolean indicadorOutorgaReceita) {
        this.indicadorOutorgaReceita = indicadorOutorgaReceita;
    }

    @Column(name = "ic_outorga_cadastro_caixa", nullable = false)
    public Boolean getIndicadorOutorgaCadastroCaixa() {
        return indicadorOutorgaCadastroCaixa;
    }

    public void setIndicadorOutorgaCadastroCaixa(Boolean indicadorOutorgaCadastroCaixa) {
        this.indicadorOutorgaCadastroCaixa = indicadorOutorgaCadastroCaixa;
    }

    @Column(name = "ic_outorga_dossie_digital", nullable = false)
    public Boolean getIndicadorOutorgaDossieDigital() {
        return indicadorOutorgaDossieDigital;
    }

    public void setIndicadorOutorgaDossieDigital(Boolean indicadorOutorgaDossieDigital) {
        this.indicadorOutorgaDossieDigital = indicadorOutorgaDossieDigital;
    }

    @Column(name = "ic_outorga_apimanager", nullable = false)
    public Boolean getIndicadorOutorgaApimanager() {
        return indicadorOutorgaApimanager;
    }

    public void setIndicadorOutorgaApimanager(Boolean indicadorOutorgaApimanager) {
        this.indicadorOutorgaApimanager = indicadorOutorgaApimanager;
    }

    @Column(name = "ic_outorga_siric", nullable = false)
    public Boolean getIndicadorOutorgaSiric() {
        return indicadorOutorgaSiric;
    }

    public void setIndicadorOutorgaSiric(Boolean indicadorOutorgaSiric) {
        this.indicadorOutorgaSiric = indicadorOutorgaSiric;
    }

    @Column(name = "de_url_callback_documento", length = 255, nullable = true)
    public String getUrlCallbackDocumento() {
        return urlCallbackDocumento;
    }

    public void setUrlCallbackDocumento(String urlCallbackDocumento) {
        this.urlCallbackDocumento = urlCallbackDocumento;
    }

    @Column(name = "de_url_callback_dossie", length = 255, nullable = true)
    public String getUrlCallbackDossie() {
        return urlCallbackDossie;
    }

    public void setUrlCallbackDossie(String urlCallbackDossie) {
        this.urlCallbackDossie = urlCallbackDossie;
    }

    @Column(name = "nu_unidade_callback_dossie", nullable = true)
    public Integer getUnidadeCallbackDossie() {
        return unidadeCallbackDossie;
    }

    public void setUnidadeCallbackDossie(Integer unidadeCallbackDossie) {
        this.unidadeCallbackDossie = unidadeCallbackDossie;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_ultima_alteracao")
    public Calendar getDataHoraUltimaAlteracao() {
        return dataHoraUltimaAlteracao;
    }

    public void setDataHoraUltimaAlteracao(Calendar dataHoraUltimaAlteracao) {
        this.dataHoraUltimaAlteracao = dataHoraUltimaAlteracao;
    }
    
    @Column(name = "de_token", columnDefinition="TEXT", nullable = true)
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // ************************************
    @OneToMany(targetEntity = Documento.class, mappedBy = "canalCaptura", fetch = FetchType.LAZY)
    public Set<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(Set<Documento> documentos) {
        this.documentos = documentos;
    }


    // ************************************
    public boolean addDocumento(Documento... documentos) {
        return this.documentos.addAll(Arrays.asList(documentos));
    }

    public boolean removeDocumento(Documento... documentos) {
        return this.documentos.removeAll(Arrays.asList(documentos));
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
        final Canal other = (Canal) obj;
        return Objects.equals(this.id, other.id);
    }

}
