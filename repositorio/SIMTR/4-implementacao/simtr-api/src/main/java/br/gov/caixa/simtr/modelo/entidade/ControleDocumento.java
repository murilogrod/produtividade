package br.gov.caixa.simtr.modelo.entidade;

import br.gov.caixa.simtr.modelo.enumerator.JanelaTemporalExtracaoEnum;
import java.io.Serializable;
import java.util.Calendar;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.caixa.simtr.util.ConstantesUtil;
import java.math.BigDecimal;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb050_controle_documento")

public class ControleDocumento extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String PROCESSO_PADRAO = "PADRAO";

    private Long id;
    private Documento documento;
    private Boolean indicativoClassificacao;
    private Boolean indicativoExtracao;
    private Boolean indicativoAvaliacaoCadastral;
    private Boolean indicativoAvaliacaoAutenticidade;
    private JanelaTemporalExtracaoEnum janelaTemporalExtracaoEnum;
    private String nomeProcesso;
    private Boolean execucaoCaixa;
    private String codigoFornecedor;
    private Calendar dataHoraEnvio;
    private Calendar dataHoraRetornoClassificacao;
    private Calendar dataHoraRetornoExtracao;
    private Calendar dataHoraRetornoAvaliacaoCadastral;
    private Calendar dataHoraRetornoAvaliacaoAutenticidade;
    private Calendar dataHoraRetornoRejeicao;
    private Calendar dataHoraRetornoConteudo;
    private String valorRetornoClassificacao;
    private String valorRetornoExtracao;
    private String valorRetornoAvaliacaoCadastral;
    private BigDecimal valorRetornoAvaliacaoAutenticidade;
    private BigDecimal percentualAssertividadeDocumento;
    private String codigoRejeicao;
    // *********************************************

    public ControleDocumento() {
        super();
        this.indicativoClassificacao = Boolean.FALSE;
        this.indicativoExtracao = Boolean.FALSE;
        this.indicativoAvaliacaoCadastral = Boolean.FALSE;
        this.indicativoAvaliacaoAutenticidade = Boolean.FALSE;
        this.execucaoCaixa = Boolean.FALSE;
        this.nomeProcesso = PROCESSO_PADRAO;
    }

    @Id
    @Column(name = "nu_controle_documento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Documento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_documento", nullable = false)
    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    @Column(name = "ic_classificacao", nullable = false)
    public Boolean getIndicativoClassificacao() {
        return indicativoClassificacao;
    }

    public void setIndicativoClassificacao(Boolean indicativoClassificacao) {
        this.indicativoClassificacao = indicativoClassificacao;
    }

    @Column(name = "ic_extracao", nullable = false)
    public Boolean getIndicativoExtracao() {
        return indicativoExtracao;
    }

    public void setIndicativoExtracao(Boolean indicativoExtracao) {
        this.indicativoExtracao = indicativoExtracao;
    }

    @Column(name = "ic_avaliacao_cadastral", nullable = false)
    public Boolean getIndicativoAvaliacaoCadastral() {
        return indicativoAvaliacaoCadastral;
    }

    public void setIndicativoAvaliacaoCadastral(Boolean indicativoAvaliacaoCadastral) {
        this.indicativoAvaliacaoCadastral = indicativoAvaliacaoCadastral;
    }

    @Column(name = "ic_avaliacao_autenticidade", nullable = false)
    public Boolean getIndicativoAvaliacaoAutenticidade() {
        return indicativoAvaliacaoAutenticidade;
    }

    public void setIndicativoAvaliacaoAutenticidade(Boolean indicativoAvaliacaoAutenticidade) {
        this.indicativoAvaliacaoAutenticidade = indicativoAvaliacaoAutenticidade;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_janela_extracao", nullable = true, length = 5)
    public JanelaTemporalExtracaoEnum getJanelaTemporalExtracaoEnum() {
        return janelaTemporalExtracaoEnum;
    }

    public void setJanelaTemporalExtracaoEnum(JanelaTemporalExtracaoEnum janelaTemporalExtracaoEnum) {
        this.janelaTemporalExtracaoEnum = janelaTemporalExtracaoEnum;
    }
    
    @Column(name = "no_processo", length = 100, nullable = true)
    public String getNomeProcesso() {
        return nomeProcesso;
    }

    public void setNomeProcesso(String nomeProcesso) {
        this.nomeProcesso = nomeProcesso;
    }

    @Column(name = "ic_execucao_caixa", nullable = false)
    public Boolean getExecucaoCaixa() {
        return execucaoCaixa;
    }

    public void setExecucaoCaixa(Boolean execucaoCaixa) {
        this.execucaoCaixa = execucaoCaixa;
    }

    @Column(name = "co_fornecedor", nullable = true, length = 100)
    public String getCodigoFornecedor() {
        return codigoFornecedor;
    }

    public void setCodigoFornecedor(String codigoFornecedor) {
        this.codigoFornecedor = codigoFornecedor;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_envio", nullable = false)
    public Calendar getDataHoraEnvio() {
        return dataHoraEnvio;
    }

    public void setDataHoraEnvio(Calendar dataHoraEnvio) {
        this.dataHoraEnvio = dataHoraEnvio;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_retorno_classificacao", nullable = true)
    public Calendar getDataHoraRetornoClassificacao() {
        return dataHoraRetornoClassificacao;
    }

    public void setDataHoraRetornoClassificacao(Calendar dataHoraRetornoClassificacao) {
        this.dataHoraRetornoClassificacao = dataHoraRetornoClassificacao;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_retorno_extracao", nullable = true)
    public Calendar getDataHoraRetornoExtracao() {
        return dataHoraRetornoExtracao;
    }

    public void setDataHoraRetornoExtracao(Calendar dataHoraRetornoExtracao) {
        this.dataHoraRetornoExtracao = dataHoraRetornoExtracao;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_retorno_avaliacao_cadastral", nullable = true)
    public Calendar getDataHoraRetornoAvaliacaoCadastral() {
        return dataHoraRetornoAvaliacaoCadastral;
    }

    public void setDataHoraRetornoAvaliacaoCadastral(Calendar dataHoraRetornoAvaliacaoCadastral) {
        this.dataHoraRetornoAvaliacaoCadastral = dataHoraRetornoAvaliacaoCadastral;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_retorno_autenticidade", nullable = true)
    public Calendar getDataHoraRetornoAvaliacaoAutenticidade() {
        return dataHoraRetornoAvaliacaoAutenticidade;
    }

    public void setDataHoraRetornoAvaliacaoAutenticidade(Calendar dataHoraRetornoAvaliacaoAutenticidade) {
        this.dataHoraRetornoAvaliacaoAutenticidade = dataHoraRetornoAvaliacaoAutenticidade;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_retorno_rejeicao", nullable = true)
    public Calendar getDataHoraRetornoRejeicao() {
        return dataHoraRetornoRejeicao;
    }

    public void setDataHoraRetornoRejeicao(Calendar dataHoraRetornoRejeicao) {
        this.dataHoraRetornoRejeicao = dataHoraRetornoRejeicao;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ts_retorno_conteudo", nullable = true)
    public Calendar getDataHoraRetornoConteudo() {
        return dataHoraRetornoConteudo;
    }

    public void setDataHoraRetornoConteudo(Calendar dataHoraRetornoConteudo) {
        this.dataHoraRetornoConteudo = dataHoraRetornoConteudo;
    }

    @Column(name = "de_retorno_classificacao", nullable = true, length = 100)
    public String getValorRetornoClassificacao() {
        return valorRetornoClassificacao;
    }

    public void setValorRetornoClassificacao(String valorRetornoClassificacao) {
        this.valorRetornoClassificacao = valorRetornoClassificacao;
    }

    @Column(name = "de_retorno_extracao", nullable = true, columnDefinition = "text")
    public String getValorRetornoExtracao() {
        return valorRetornoExtracao;
    }

    public void setValorRetornoExtracao(String valorRetornoExtracao) {
        this.valorRetornoExtracao = valorRetornoExtracao;
    }

    @Column(name = "de_retorno_avaliacao_cadastral", nullable = true, length = 255)
    public String getValorRetornoAvaliacaoCadastral() {
        return valorRetornoAvaliacaoCadastral;
    }

    public void setValorRetornoAvaliacaoCadastral(String valorRetornoAvaliacaoCadastral) {
        this.valorRetornoAvaliacaoCadastral = valorRetornoAvaliacaoCadastral;
    }

    @Column(name = "ix_retorno_autenticidade", nullable = true, scale = 5, precision = 2)
    public BigDecimal getValorRetornoAvaliacaoAutenticidade() {
        return valorRetornoAvaliacaoAutenticidade;
    }

    public void setValorRetornoAvaliacaoAutenticidade(BigDecimal valorRetornoAvaliacaoAutenticidade) {
        this.valorRetornoAvaliacaoAutenticidade = valorRetornoAvaliacaoAutenticidade;
    }
    
    @Column(name = "ix_pad", nullable = true, scale = 5, precision = 2)
    public BigDecimal getPercentualAssertividadeDocumento() {
        return percentualAssertividadeDocumento;
    }

    public void setPercentualAssertividadeDocumento(BigDecimal percentualAssertividadeDocumento) {
        this.percentualAssertividadeDocumento = percentualAssertividadeDocumento;
    }

    @Column(name = "co_rejeicao", nullable = true, length = 10)
    public String getCodigoRejeicao() {
        return codigoRejeicao;
    }

    public void setCodigoRejeicao(String codigoRejeicao) {
        this.codigoRejeicao = codigoRejeicao;
    }

    //*****************************************************
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
        final ControleDocumento other = (ControleDocumento) obj;
        return Objects.equals(this.id, other.id);
    }

}
