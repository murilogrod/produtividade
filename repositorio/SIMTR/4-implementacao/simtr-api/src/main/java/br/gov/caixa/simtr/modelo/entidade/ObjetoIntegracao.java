package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
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

import br.gov.caixa.simtr.modelo.enumerator.AcaoComparacaoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OperadorComparacaoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SistemaIntegracaoEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

/**
 * 
 * @author f525904
 *
 */

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb059_objeto_integracao", indexes = {
    @Index(name = "ix_mtrtb059_01", unique = true, columnList = "ic_sistema_integracao")
})
public class ObjetoIntegracao extends GenericEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    private SistemaIntegracaoEnum sistemaIntegracao;
    private String objetoIntegracao;
    private String acao;
    private Integer versao;
    private Boolean comparacaoCompleta;
    private OperadorComparacaoEnum operadorComparacao;
    private AcaoComparacaoEnum acaoComparacao;
    private String objetoConsulta;
    
    private Set<AtributoIntegracao> atributosIntegracao;
    private Set<PriorizacaoObjetoIntegracao> priorizacaoObjetoIntegracaos;
    
    public ObjetoIntegracao() {
        super();
    }

    @Id
    @Column(name = "nu_objeto_integracao")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_sistema_integracao", length = 100, nullable = true)
    public SistemaIntegracaoEnum getSistemaIntegracao() {
        return sistemaIntegracao;
    }

    public void setSistemaIntegracao(SistemaIntegracaoEnum sistemaIntegracao) {
        this.sistemaIntegracao = sistemaIntegracao;
    }

    @Column(name = "no_objeto_integracao", length = 100, nullable = true)
    public String getObjetoIntegracao() {
        return objetoIntegracao;
    }

    public void setObjetoIntegracao(String objetoIntegracao) {
        this.objetoIntegracao = objetoIntegracao;
    }

    @Column(name = "ic_acao", length = 1, nullable = true)
    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }
    
    @Column(name = "ic_comparacao_completa", nullable = true)
    public Boolean getComparacaoCompleta() {
        return comparacaoCompleta;
    }

    public void setComparacaoCompleta(Boolean comparacaoCompleta) {
        this.comparacaoCompleta = comparacaoCompleta;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_operador_comparacao", nullable = true)
    public OperadorComparacaoEnum getOperadorComparacao() {
        return operadorComparacao;
    }

    public void setOperadorComparacao(OperadorComparacaoEnum operadorComparacao) {
        this.operadorComparacao = operadorComparacao;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_acao_comparacao", nullable = true)
    public AcaoComparacaoEnum getAcaoComparacao() {
        return acaoComparacao;
    }

    public void setAcaoComparacao(AcaoComparacaoEnum acaoComparacao) {
        this.acaoComparacao = acaoComparacao;
    }
    
    @Column(name = "nu_versao", nullable = false)
    public Integer getVersao() {
            return versao;
    }
    
    public void setVersao(Integer versao) {
            this.versao = versao;
    }
    
    @Column(name = "no_objeto_consulta", length = 100, nullable = true)
    public String getObjetoConsulta() {
        return objetoConsulta;
    }

    public void setObjetoConsulta(String objetoConsulta) {
        this.objetoConsulta = objetoConsulta;
    }

    @OneToMany(targetEntity = AtributoIntegracao.class, mappedBy = "objetoIntegracao", fetch = FetchType.LAZY)
    public Set<AtributoIntegracao> getAtributosIntegracao() {
        return atributosIntegracao;
    }

    public void setAtributosIntegracao(Set<AtributoIntegracao> atributosIntegracao) {
        this.atributosIntegracao = atributosIntegracao;
    }

    /**
     * <p>Retorna o valor do atributo priorizacaoObjetoIntegracaos</p>.
     *
     * @return priorizacaoObjetoIntegracaos
    */
    @OneToMany(targetEntity = PriorizacaoObjetoIntegracao.class, mappedBy = "objetoIntegracao", fetch = FetchType.LAZY)
    public Set<PriorizacaoObjetoIntegracao> getPriorizacaoObjetoIntegracaos() {
        return this.priorizacaoObjetoIntegracaos;
    }

    /**
     * <p>Define o valor do atributo priorizacaoObjetoIntegracaos</p>.
     *
     * @param priorizacaoObjetoIntegracaos valor a ser atribuído
    */
    public void setPriorizacaoObjetoIntegracaos(Set<PriorizacaoObjetoIntegracao> priorizacaoObjetoIntegracaos) {
        this.priorizacaoObjetoIntegracaos = priorizacaoObjetoIntegracaos;
    }
    
    
}
