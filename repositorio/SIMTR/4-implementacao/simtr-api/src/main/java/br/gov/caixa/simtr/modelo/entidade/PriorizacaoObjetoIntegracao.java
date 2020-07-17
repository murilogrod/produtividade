package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;

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
import javax.persistence.Table;

import br.gov.caixa.simtr.modelo.enumerator.SICLITipoFonteEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

/**
 * 
 * @author f525904
 *
 */

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb061_priorizacao_objeto_integracao", indexes = {
    @Index(name = "ix_mtrtb061_01", unique = true, columnList = "nu_processo_dossie, nu_objeto_integracao, nu_ordem_prioridade")
})
public class PriorizacaoObjetoIntegracao extends GenericEntity implements Serializable  {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Processo processoDossie;
    private ObjetoIntegracao objetoIntegracao;
    private TipoDocumento tipoDocumento;
    private SICLITipoFonteEnum fonte;
    private Integer ordemPrioridade;
    private Integer versao;
    
    public PriorizacaoObjetoIntegracao() {
        super();
    }

    @Id
    @Column(name = "nu_priorizacao_atributo_integracao")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = Processo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_processo_dossie", nullable = false)
    public Processo getProcessoDossie() {
        return processoDossie;
    }

    public void setProcessoDossie(Processo processoDossie) {
        this.processoDossie = processoDossie;
    }

    @ManyToOne(targetEntity = ObjetoIntegracao.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_objeto_integracao", nullable = false)
    public ObjetoIntegracao getObjetoIntegracao() {
        return objetoIntegracao;
    }

    public void setObjetoIntegracao(ObjetoIntegracao objetoIntegracao) {
        this.objetoIntegracao = objetoIntegracao;
    }

    @ManyToOne(targetEntity = TipoDocumento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_tipo_documento", nullable = true)
    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_fonte", length = 1, nullable = true)
    public SICLITipoFonteEnum getFonte() {
        return fonte;
    }

    public void setFonte(SICLITipoFonteEnum fonte) {
        this.fonte = fonte;
    }

    @Column(name = "nu_ordem_prioridade", nullable = false)
    public Integer getOrdemPrioridade() {
        return ordemPrioridade;
    }

    public void setOrdemPrioridade(Integer ordemPrioridade) {
        this.ordemPrioridade = ordemPrioridade;
    }

    @Column(name = "nu_versao", nullable = false)
    public Integer getVersao() {
        return versao;
    }

    public void setVersao(Integer versao) {
        this.versao = versao;
    }
}
