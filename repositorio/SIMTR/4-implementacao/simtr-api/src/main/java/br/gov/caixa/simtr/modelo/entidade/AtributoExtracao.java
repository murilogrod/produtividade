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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.gov.caixa.pedesgo.arquitetura.anotacao.IgnoreClassLazyInterceptor;
import br.gov.caixa.simtr.modelo.enumerator.EstrategiaPartilhaEnum;
import br.gov.caixa.simtr.modelo.enumerator.ModoPartilhaEnum;
import br.gov.caixa.simtr.modelo.enumerator.SICPFCampoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SICPFModoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.OneToMany;

@Entity
@Table(schema = ConstantesUtil.SCHEMA_MTRSM001, name = "mtrtb045_atributo_extracao")
@IgnoreClassLazyInterceptor
public class AtributoExtracao extends GenericEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private TipoDocumento tipoDocumento;
    private String nomeAtributoDocumento;
    private String nomeNegocial;
    private String nomeAtributoRetorno;
    private String nomeAtributoSIECM;
    private String nomeAtributoSICOD;
    private String nomeAtributoSICLI;
    private String nomeObjetoSICLI;
    private String valorPadrao;
    private String orientacaoPreenchimento;
    private TipoAtributoEnum tipoAtributoGeralEnum;
    private TipoAtributoEnum tipoAtributoSicliEnum;
    private TipoAtributoEnum tipoAtributoSicodEnum;
    private TipoAtributoEnum tipoAtributoSiecmEnum;
    private Boolean ativo;
    private TipoCampoEnum tipoCampoEnum;
    private Boolean obrigatorio;
    private Boolean obrigatorioSIECM;
    private Boolean utilizadoCalculoValidade;
    private Boolean utilizadoIdentificadorPessoa;
    private Boolean presenteDocumento;
    private SICPFCampoEnum sicpfCampoEnum;
    private SICPFModoEnum sicpfModoEnum;
    private Integer percentualAlteracaoPermitido;
    private EstrategiaPartilhaEnum estrategiaPartilhaEnum;
    private ModoPartilhaEnum modoPartilhaEnum;
    private AtributoExtracao atributoPartilha;
    private Integer grupoAtributo;
    private Integer ordemApresentacao;
    private String expressaoInterface;
    private AtributoIntegracao atributoIntegracao;
    private String objetoDocumento;
    //******************************************
    private Set<OpcaoAtributo> opcoesAtributo;

    //******************************************
    public AtributoExtracao() {
        this.opcoesAtributo = new HashSet<>();
    }

    @Id
    @Column(name = "nu_atributo_extracao")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(targetEntity = TipoDocumento.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_tipo_documento", nullable = false)
    public TipoDocumento getTipoDocumento() {
        return this.tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Column(name = "no_atributo_documento", length = 100, nullable = false)
    public String getNomeAtributoDocumento() {
        return nomeAtributoDocumento;
    }

    public void setNomeAtributoDocumento(String nomeAtributoDocumento) {
        this.nomeAtributoDocumento = nomeAtributoDocumento;
    }

    @Column(name = "no_atributo_negocial", length = 100, nullable = false)
    public String getNomeNegocial() {
        return nomeNegocial;
    }

    public void setNomeNegocial(String nomeNegocial) {
        this.nomeNegocial = nomeNegocial;
    }

    @Column(name = "no_atributo_retorno", length = 100, nullable = true)
    public String getNomeAtributoRetorno() {
        return nomeAtributoRetorno;
    }

    public void setNomeAtributoRetorno(String nomeAtributoRetorno) {
        this.nomeAtributoRetorno = nomeAtributoRetorno;
    }

    @Column(name = "no_atributo_siecm", length = 100, nullable = true)
    public String getNomeAtributoSIECM() {
        return nomeAtributoSIECM;
    }

    public void setNomeAtributoSIECM(String nomeAtributoSIECM) {
        this.nomeAtributoSIECM = nomeAtributoSIECM;
    }

    @Column(name = "no_atributo_sicod", length = 100, nullable = true)
    public String getNomeAtributoSICOD() {
        return nomeAtributoSICOD;
    }

    public void setNomeAtributoSICOD(String nomeAtributoSICOD) {
        this.nomeAtributoSICOD = nomeAtributoSICOD;
    }

    @Column(name = "no_atributo_sicli", length = 100, nullable = true)
    public String getNomeAtributoSICLI() {
        return nomeAtributoSICLI;
    }

    public void setNomeAtributoSICLI(String nomeAtributoSICLI) {
        this.nomeAtributoSICLI = nomeAtributoSICLI;
    }

    @Column(name = "no_objeto_sicli", length = 100, nullable = true)
    public String getNomeObjetoSICLI() {
        return nomeObjetoSICLI;
    }

    public void setNomeObjetoSICLI(String nomeObjetoSICLI) {
        this.nomeObjetoSICLI = nomeObjetoSICLI;
    }

    @Column(name = "vr_padrao", length = 30, nullable = true)
    public String getValorPadrao() {
        return valorPadrao;
    }

    public void setValorPadrao(String valorPadrao) {
        this.valorPadrao = valorPadrao;
    }

    @Column(name = "de_orientacao", length = 255, nullable = true)
    public String getOrientacaoPreenchimento() {
        return orientacaoPreenchimento;
    }

    public void setOrientacaoPreenchimento(String orientacaoPreenchimento) {
        this.orientacaoPreenchimento = orientacaoPreenchimento;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_tipo_geral", length = 50, nullable = true)
    public TipoAtributoEnum getTipoAtributoGeralEnum() {
        return tipoAtributoGeralEnum;
    }

    public void setTipoAtributoGeralEnum(TipoAtributoEnum tipoAtributoGeralEnum) {
        this.tipoAtributoGeralEnum = tipoAtributoGeralEnum;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_tipo_sicli", length = 50, nullable = true)
    public TipoAtributoEnum getTipoAtributoSicliEnum() {
        return tipoAtributoSicliEnum;
    }

    public void setTipoAtributoSicliEnum(TipoAtributoEnum tipoAtributoSicliEnum) {
        this.tipoAtributoSicliEnum = tipoAtributoSicliEnum;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_tipo_sicod", length = 50, nullable = true)
    public TipoAtributoEnum getTipoAtributoSicodEnum() {
        return tipoAtributoSicodEnum;
    }

    public void setTipoAtributoSicodEnum(TipoAtributoEnum tipoAtributoSicodEnum) {
        this.tipoAtributoSicodEnum = tipoAtributoSicodEnum;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_tipo_siecm", length = 20, nullable = true)
    public TipoAtributoEnum getTipoAtributoSiecmEnum() {
        return tipoAtributoSiecmEnum;
    }

    public void setTipoAtributoSiecmEnum(TipoAtributoEnum tipoAtributoSiecmEnum) {
        this.tipoAtributoSiecmEnum = tipoAtributoSiecmEnum;
    }

    @Column(name = "ic_ativo", nullable = false)
    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_tipo_campo", length = 20, nullable = false)
    public TipoCampoEnum getTipoCampoEnum() {
        return tipoCampoEnum;
    }

    public void setTipoCampoEnum(TipoCampoEnum tipoCampoEnum) {
        this.tipoCampoEnum = tipoCampoEnum;
    }

    @Column(name = "ic_obrigatorio", nullable = false)
    public Boolean getObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(Boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    @Column(name = "ic_obrigatorio_siecm", nullable = true)
    public Boolean getObrigatorioSIECM() {
        return obrigatorioSIECM;
    }

    public void setObrigatorioSIECM(Boolean obrigatorioSIECM) {
        this.obrigatorioSIECM = obrigatorioSIECM;
    }

    @Column(name = "ic_calculo_data", nullable = false)
    public Boolean getUtilizadoCalculoValidade() {
        return utilizadoCalculoValidade;
    }

    public void setUtilizadoCalculoValidade(Boolean utilizadoCalculoValidade) {
        this.utilizadoCalculoValidade = utilizadoCalculoValidade;
    }

    @Column(name = "ic_identificador_pessoa", nullable = false)
    public Boolean getUtilizadoIdentificadorPessoa() {
        return utilizadoIdentificadorPessoa;
    }

    public void setUtilizadoIdentificadorPessoa(Boolean utilizadoIdentificadorPessoa) {
        this.utilizadoIdentificadorPessoa = utilizadoIdentificadorPessoa;
    }

    @Column(name = "ic_presente_documento", nullable = false)
    public Boolean getPresenteDocumento() {
        return presenteDocumento;
    }

    public void setPresenteDocumento(Boolean presenteDocumento) {
        this.presenteDocumento = presenteDocumento;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_campo_sicpf", length = 50, nullable = true)
    public SICPFCampoEnum getSicpfCampoEnum() {
        return sicpfCampoEnum;
    }

    public void setSicpfCampoEnum(SICPFCampoEnum sicpfCampoEnum) {
        this.sicpfCampoEnum = sicpfCampoEnum;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_modo_sicpf", length = 1, nullable = true)
    public SICPFModoEnum getSicpfModoEnum() {
        return sicpfModoEnum;
    }

    public void setSicpfModoEnum(SICPFModoEnum sicpfModoEnum) {
        this.sicpfModoEnum = sicpfModoEnum;
    }

    @Column(name = "pc_alteracao_permitido", nullable = true)
    public Integer getPercentualAlteracaoPermitido() {
        return percentualAlteracaoPermitido;
    }

    public void setPercentualAlteracaoPermitido(Integer percentualAlteracaoPermitido) {
        this.percentualAlteracaoPermitido = percentualAlteracaoPermitido;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_estrategia_partilha", length = 30, nullable = true)
    public EstrategiaPartilhaEnum getEstrategiaPartilhaEnum() {
        return estrategiaPartilhaEnum;
    }

    public void setEstrategiaPartilhaEnum(EstrategiaPartilhaEnum estrategiaPartilhaEnum) {
        this.estrategiaPartilhaEnum = estrategiaPartilhaEnum;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ic_modo_partilha", length = 1, nullable = true)
    public ModoPartilhaEnum getModoPartilhaEnum() {
        return modoPartilhaEnum;
    }

    public void setModoPartilhaEnum(ModoPartilhaEnum modoPartilhaEnum) {
        this.modoPartilhaEnum = modoPartilhaEnum;
    }

    @OneToOne(targetEntity = AtributoExtracao.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "nu_atributo_partilha", nullable = true)
    public AtributoExtracao getAtributoPartilha() {
        return atributoPartilha;
    }

    public void setAtributoPartilha(AtributoExtracao atributoPartilha) {
        this.atributoPartilha = atributoPartilha;
    }

    @Column(name = "nu_grupo_atributo", nullable = true)
    public Integer getGrupoAtributo() {
        return grupoAtributo;
    }

    public void setGrupoAtributo(Integer grupoAtributo) {
        this.grupoAtributo = grupoAtributo;
    }

    @Column(name = "nu_ordem", nullable = false)
    public Integer getOrdemApresentacao() {
        return this.ordemApresentacao;
    }

    public void setOrdemApresentacao(Integer ordemApresentacao) {
        this.ordemApresentacao = ordemApresentacao;
    }

    @Column(name = "de_expressao", nullable = true, columnDefinition = "text")
    public String getExpressaoInterface() {
        return expressaoInterface;
    }

    public void setExpressaoInterface(String expressaoInterface) {
        this.expressaoInterface = expressaoInterface;
    }
    
    @ManyToOne(targetEntity = AtributoIntegracao.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "nu_atributo_integracao", nullable = true)
    public AtributoIntegracao getAtributoIntegracao() {
        return atributoIntegracao;
    }

    public void setAtributoIntegracao(AtributoIntegracao atributoIntegracao) {
        this.atributoIntegracao = atributoIntegracao;
    }

    @Column(name = "no_objeto_documento", length = 100, nullable = true)
    public String getObjetoDocumento() {
		return objetoDocumento;
	}

	public void setObjetoDocumento(String objetoDocumento) {
		this.objetoDocumento = objetoDocumento;
	}


    //******************************************
    @OneToMany(targetEntity = OpcaoAtributo.class, mappedBy = "atributoExtracao", fetch = FetchType.LAZY)
    public Set<OpcaoAtributo> getOpcoesAtributo() {
        return opcoesAtributo;
    }

	public void setOpcoesAtributo(Set<OpcaoAtributo> opcoesAtributo) {
        this.opcoesAtributo = opcoesAtributo;
    }

    //******************************************
    public boolean addOpcoesAtributo(OpcaoAtributo... opcoesAtributo) {
        return this.opcoesAtributo.addAll(Arrays.asList(opcoesAtributo));
    }

    public boolean removeOpcoesAtributo(OpcaoAtributo... opcoesAtributo) {
        return this.opcoesAtributo.removeAll(Arrays.asList(opcoesAtributo));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        hash = 37 * hash + Objects.hashCode(this.tipoDocumento);
        hash = 37 * hash + Objects.hashCode(this.nomeAtributoDocumento);
        hash = 37 * hash + Objects.hashCode(this.nomeNegocial);
        hash = 37 * hash + Objects.hashCode(this.nomeAtributoRetorno);
        hash = 37 * hash + Objects.hashCode(this.nomeAtributoSIECM);
        hash = 37 * hash + Objects.hashCode(this.nomeAtributoSICOD);
        hash = 37 * hash + Objects.hashCode(this.nomeAtributoSICLI);
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
        final AtributoExtracao other = (AtributoExtracao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nomeAtributoDocumento, other.nomeAtributoDocumento)) {
            return false;
        }
        if (!Objects.equals(this.nomeNegocial, other.nomeNegocial)) {
            return false;
        }
        if (!Objects.equals(this.nomeAtributoRetorno, other.nomeAtributoRetorno)) {
            return false;
        }
        if (!Objects.equals(this.nomeAtributoSIECM, other.nomeAtributoSIECM)) {
            return false;
        }
        if (!Objects.equals(this.nomeAtributoSICOD, other.nomeAtributoSICOD)) {
            return false;
        }
        if (!Objects.equals(this.nomeAtributoSICLI, other.nomeAtributoSICLI)) {
            return false;
        }
        return Objects.equals(this.tipoDocumento, other.tipoDocumento);
    }

}
