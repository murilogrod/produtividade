package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalOperacao;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CNPJAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CPFAdapter;
import io.swagger.annotations.ApiModelProperty;

public abstract class AbstractBaseDTO {

    
    @XmlElement(name = ConstantesDossieDigitalOperacao.INTEGRACAO)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.INTEGRACAO, required = true, value = "Código de integração para identificar o canal solicitante")
    protected Long codigoIntegracao;

    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CPFAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalOperacao.CPF_CLIENTE)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.CPF_CLIENTE, required = false, value = "CPF do cliente caso o mesmo seja pessoa fisica. Deve ser informado se o cliente for PF. Zeros a esquerda não devem ser incluidos.", example = "11122233399")
    protected Long cpfCliente;

    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CNPJAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalOperacao.CNPJ_CLIENTE)
    @ApiModelProperty(name = ConstantesDossieDigitalOperacao.CNPJ_CLIENTE, required = false, value = "CNPJ do cliente pessoa fisica. Deve ser informado se o cliente for PJ. Zeros a esquerda não devem ser incluidos.", example = "11222333000099")
    protected Long cnpjCliente;

    
    public AbstractBaseDTO() {
        super();
    }

    public Long getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(Long cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public Long getCnpjCliente() {
        return cnpjCliente;
    }

    public void setCnpjCliente(Long cnpjCliente) {
        this.cnpjCliente = cnpjCliente;
    }

    public Long getCodigoIntegracao() {
        return codigoIntegracao;
    }

    public void setCodigoIntegracao(Long codigoIntegracao) {
        this.codigoIntegracao = codigoIntegracao;
    }
    
}
