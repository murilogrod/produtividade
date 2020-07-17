package br.gov.caixa.simtr.controle.vo.portalempreendedor;

import java.io.Serializable;

public class EnderecoComercialVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String tipoLogradouro;
    private String logradouro;
    private String complemento;
    private String cep;
    private String bairroDistrito;
    private MunicipioVO municipio;
    private String numero;
    private String referencia;
    private String uf;

    public String getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(String tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }
    
    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
    
    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }
    
    public String getBairroDistrito() {
        return bairroDistrito;
    }

    public void setBairroDistrito(String bairroDistrito) {
        this.bairroDistrito = bairroDistrito;
    }

    public MunicipioVO getMunicipio() {
        return municipio;
    }

    public void setMunicipio(MunicipioVO municipio) {
        this.municipio = municipio;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}
