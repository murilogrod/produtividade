package br.gov.caixa.simtr.controle.vo.portalempreendedor;

import java.io.Serializable;
import java.util.List;

public class PessoaJuridicaVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private CnaeVO cnaePrincipal;
    private List<CnaeVO> cnaesSecundarias;
    private String cnpj;
    private String dataAbertura;
    private String nomeEmpresarial;
    private String nomeFantasia;
    private TelefoneCelularVO telefoneCelular;
    private String email;
    private EnderecoComercialVO enderecoComercial;
    private String porte;
    private NaturezaVO natureza;
    private List<QuadroSocietarioVO> quadroSocietario;
    private ResponsavelVO responsavel;

    public CnaeVO getCnaePrincipal() {
        return cnaePrincipal;
    }

    public void setCnaePrincipal(CnaeVO cnaePrincipal) {
        this.cnaePrincipal = cnaePrincipal;
    }

    public List<CnaeVO> getCnaesSecundarias() {
        return cnaesSecundarias;
    }

    public void setCnaesSecundarias(List<CnaeVO> cnaesSecundarias) {
        this.cnaesSecundarias = cnaesSecundarias;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(String dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public String getNomeEmpresarial() {
        return nomeEmpresarial;
    }

    public void setNomeEmpresarial(String nomeEmpresarial) {
        this.nomeEmpresarial = nomeEmpresarial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public TelefoneCelularVO getTelefoneCelular() {
        return telefoneCelular;
    }

    public void setTelefoneCelular(TelefoneCelularVO telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EnderecoComercialVO getEnderecoComercial() {
        return enderecoComercial;
    }

    public void setEnderecoComercial(EnderecoComercialVO enderecoComercial) {
        this.enderecoComercial = enderecoComercial;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public NaturezaVO getNatureza() {
        return natureza;
    }

    public void setNatureza(NaturezaVO natureza) {
        this.natureza = natureza;
    }
    
    public List<QuadroSocietarioVO> getQuadroSocietario() {
        return quadroSocietario;
    }

    public void setQuadroSocietario(List<QuadroSocietarioVO> quadroSocietario) {
        this.quadroSocietario = quadroSocietario;
    }

    public ResponsavelVO getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(ResponsavelVO responsavel) {
        this.responsavel = responsavel;
    }
}
