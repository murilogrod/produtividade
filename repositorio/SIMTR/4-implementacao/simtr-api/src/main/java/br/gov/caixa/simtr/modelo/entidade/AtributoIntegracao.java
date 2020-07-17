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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.caixa.simtr.modelo.enumerator.AcaoComparacaoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OperadorComparacaoEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

/**
 * 
 * @author f525904
 *
 */

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb060_atributo_integracao")
public class AtributoIntegracao extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private ObjetoIntegracao objetoIntegracao;
    private String atributo;
    private String tipoAtributo;
    private Boolean obrigatorio;
    private String acao;
    private Boolean ativo;
    private Boolean chaveComparacao;
    private OperadorComparacaoEnum operadorComparacao;
    private AcaoComparacaoEnum acaoComparacao;
    private String atributoConsulta;
    
    private Set<AtributoExtracao> atributoExtracoes;

    public AtributoIntegracao() {
        super();
    }

    @Id
    @Column(name = "nu_atributo_integracao")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = ObjetoIntegracao.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_objeto_integracao", nullable = false)
    public ObjetoIntegracao getObjetoIntegracao() {
        return objetoIntegracao;
    }

    public void setObjetoIntegracao(ObjetoIntegracao objetoIntegracao) {
        this.objetoIntegracao = objetoIntegracao;
    }

    @Column(name = "no_atributo_integracao", length = 100, nullable = true)
    public String getAtributo() {
        return atributo;
    }

    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    @Column(name = "tipo_atributo_integracao", length = 20, nullable = true)
    public String getTipoAtributo() {
        return tipoAtributo;
    }

    public void setTipoAtributo(String tipoAtributo) {
        this.tipoAtributo = tipoAtributo;
    }

    @Column(name = "ic_obrigatorio", nullable = false)
    public Boolean getObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(Boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    @Column(name = "ic_acao", length = 1, nullable = true)
    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    @Column(name = "ic_ativo", nullable = false)
    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Column(name = "ic_chave_comparacao", nullable = false)
    public Boolean getChaveComparacao() {
        return chaveComparacao;
    }

    public void setChaveComparacao(Boolean chaveComparacao) {
        this.chaveComparacao = chaveComparacao;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_operador_comparacao", length = 10, nullable = true)
    public OperadorComparacaoEnum getOperadorComparacao() {
        return operadorComparacao;
    }

    public void setOperadorComparacao(OperadorComparacaoEnum operadorComparacao) {
        this.operadorComparacao = operadorComparacao;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_acao_comparacao", length = 1, nullable = true)
    public AcaoComparacaoEnum getAcaoComparacao() {
        return acaoComparacao;
    }

    public void setAcaoComparacao(AcaoComparacaoEnum acaoComparacao) {
        this.acaoComparacao = acaoComparacao;
    }

    @Column(name = "no_atributo_consulta", length = 100, nullable = true)
    public String getAtributoConsulta() {
        return atributoConsulta;
    }

    public void setAtributoConsulta(String atributoConsulta) {
        this.atributoConsulta = atributoConsulta;
    }

    /**
     * <p>Retorna o valor do atributo atributoExtracoes</p>.
     *
     * @return atributoExtracoes
    */
    @OneToMany(targetEntity = AtributoExtracao.class, mappedBy = "atributoIntegracao", fetch = FetchType.LAZY)
    public Set<AtributoExtracao> getAtributoExtracoes() {
        return this.atributoExtracoes;
    }

    /**
     * <p>Define o valor do atributo atributoExtracoes</p>.
     *
     * @param atributoExtracoes valor a ser atribuído
    */
    public void setAtributoExtracoes(Set<AtributoExtracao> atributoExtracoes) {
        this.atributoExtracoes = atributoExtracoes;
    }

    
    
    
}
