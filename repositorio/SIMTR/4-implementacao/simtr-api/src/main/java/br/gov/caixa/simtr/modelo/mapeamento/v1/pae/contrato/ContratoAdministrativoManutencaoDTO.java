package br.gov.caixa.simtr.modelo.mapeamento.v1.pae.contrato;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.gov.caixa.simtr.modelo.entidade.ContratoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.ProcessoAdministrativo;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CNPJAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CPFAdapter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesPAE;

@XmlRootElement(name = ConstantesPAE.XML_ROOT_ELEMENT_CONTRATO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesPAE.VISAO_CONTRATO_API_MODEL_CONTRATO_ADMINISTRATIVO_MANUTENCAO,
        description = "Objeto utilizado para aplicar ajustes em um Contrato Administrativo modificando as definições iniciais dos atributos do registro."
)
public class ContratoAdministrativoManutencaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlElement(name = ConstantesPAE.ID_PROCESSO_VINCULADO)
    @ApiModelProperty(name = ConstantesPAE.ID_PROCESSO_VINCULADO, required = false, value = "Valor que representa o identificador do Processo Adminstrativo. Utilizado alterar a vinculação.")
    private Long idProcessoAdministrativo;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlElement(name = ConstantesPAE.NUMERO_CONTRATO)
    @ApiModelProperty(name = ConstantesPAE.NUMERO_CONTRATO, required = false, value = "Valor que representa o numero do Contrato Adminstrativo.")
    private Integer numeroContrato;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlElement(name = ConstantesPAE.ANO_CONTRATO)
    @ApiModelProperty(name = ConstantesPAE.ANO_CONTRATO, required = false, value = "Valor que representa o ano de criação do Contrato Adminstrativo.")
    private Integer anoContrato;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlElement(name = ConstantesPAE.DESCRICAO_CONTRATO)
    @ApiModelProperty(name = ConstantesPAE.DESCRICAO_CONTRATO, required = false, value = "Descrição do livre vinculada ao Contrato Administrativo.")
    private String descricaoContrato;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CPFAdapter.class)
    @XmlElement(name = ConstantesPAE.CPF_FORNECEDOR)
    @ApiModelProperty(name = ConstantesPAE.CPF_FORNECEDOR, required = false, value = "CPF do fornecedor caso o mesmo seja pessoa fisica.", example = "999.999.999-99")
    private String cpfFornecedor;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CNPJAdapter.class)
    @XmlElement(name = ConstantesPAE.CNPJ_FORNECEDOR)
    @ApiModelProperty(name = ConstantesPAE.CNPJ_FORNECEDOR, required = false, value = "CNPJ do fornecedor caso o mesmo seja pessoa fisica.", example = "99.999.999/9999-99")
    private String cnpjFornecedor;

    @XmlElement(name = ConstantesPAE.UNIDADE_OPERACIONAL)
    @ApiModelProperty(name = ConstantesPAE.UNIDADE_OPERACIONAL, required = false, value = "Valor que representa a unidade operacional do Contrato Adminstrativo.")
    private Integer unidadeOperacional;

    public ContratoAdministrativoManutencaoDTO() {
        super();
    }

    public ContratoAdministrativoManutencaoDTO(ContratoAdministrativo contrato) {
        this();

        if (contrato.getProcessoAdministrativo() != null) {
            this.idProcessoAdministrativo = contrato.getProcessoAdministrativo().getId();
        }
        this.numeroContrato = contrato.getNumeroContrato();
        this.anoContrato = contrato.getAnoContrato();
        this.descricaoContrato = contrato.getDescricaoContrato();
        if (ConstantesUtil.TAMANHO_CPF.equals(contrato.getCpfCnpjFornecedor().length())) {
            this.cpfFornecedor = contrato.getCpfCnpjFornecedor();
        } else if (ConstantesUtil.TAMANHO_CNPJ.equals(contrato.getCpfCnpjFornecedor().length())) {
            this.cnpjFornecedor = contrato.getCpfCnpjFornecedor();
        }
        this.unidadeOperacional = contrato.getUnidadeOperacional();
    }

    public Long getIdProcessoAdministrativo() {
        return idProcessoAdministrativo;
    }

    public void setIdProcessoAdministrativo(Long idProcessoAdministrativo) {
        this.idProcessoAdministrativo = idProcessoAdministrativo;
    }

    public Integer getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(Integer numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

    public Integer getAnoContrato() {
        return anoContrato;
    }

    public void setAnoContrato(Integer anoContrato) {
        this.anoContrato = anoContrato;
    }

    public String getDescricaoContrato() {
        return descricaoContrato;
    }

    public void setDescricaoContrato(String descricaoContrato) {
        this.descricaoContrato = descricaoContrato;
    }

    public String getCpfFornecedor() {
        return cpfFornecedor;
    }

    public void setCpfFornecedor(String cpfFornecedor) {
        this.cpfFornecedor = cpfFornecedor;
    }

    public String getCnpjFornecedor() {
        return cnpjFornecedor;
    }

    public void setCnpjFornecedor(String cnpjFornecedor) {
        this.cnpjFornecedor = cnpjFornecedor;
    }

    public Integer getUnidadeOperacional() {
        return unidadeOperacional;
    }

    public void setUnidadeOperacional(Integer unidadeOperacional) {
        this.unidadeOperacional = unidadeOperacional;
    }

    public ContratoAdministrativo prototype() {
        ContratoAdministrativo contratoAdministrativo = new ContratoAdministrativo();

        contratoAdministrativo.setNumeroContrato(this.numeroContrato);
        contratoAdministrativo.setAnoContrato(this.anoContrato);
        contratoAdministrativo.setDescricaoContrato(this.descricaoContrato);
        contratoAdministrativo.setCpfCnpjFornecedor(this.cpfFornecedor != null ? this.cpfFornecedor : this.cnpjFornecedor);
        contratoAdministrativo.setUnidadeOperacional(this.unidadeOperacional);

        if (this.idProcessoAdministrativo != null) {
            ProcessoAdministrativo processoAdministrativo = new ProcessoAdministrativo();
            processoAdministrativo.setId(this.idProcessoAdministrativo);

            contratoAdministrativo.setProcessoAdministrativo(processoAdministrativo);
        }

        return contratoAdministrativo;
    }

}
