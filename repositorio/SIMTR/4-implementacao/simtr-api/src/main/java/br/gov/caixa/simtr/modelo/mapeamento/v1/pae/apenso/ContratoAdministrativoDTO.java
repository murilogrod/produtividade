package br.gov.caixa.simtr.modelo.mapeamento.v1.pae.apenso;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.gov.caixa.simtr.modelo.entidade.ContratoAdministrativo;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CNPJAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CPFAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesPAE;

@XmlRootElement(name = ConstantesPAE.XML_ROOT_ELEMENT_CONTRATO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesPAE.VISAO_APENSO_API_MODEL_CONTRATO_ADMINISTRATIVO,
        description = "Objeto utilizado para representar o Contrato Adminstrativo no retorno as consultas realizadas sob a otica da consulta pelo Apenso."
)
public class ContratoAdministrativoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesPAE.ID)
    @ApiModelProperty(name = ConstantesPAE.ID, required = true, value = "Valor que representa o identificador do Contrato Administrativo.")
    private Long id;

    @XmlElement(name = ConstantesPAE.NUMERO_CONTRATO)
    @ApiModelProperty(name = ConstantesPAE.NUMERO_CONTRATO, required = true, value = "Valor que representa o numero do Contrato Adminstrativo.")
    private Integer numeroContrato;

    @XmlElement(name = ConstantesPAE.ANO_CONTRATO)
    @ApiModelProperty(name = ConstantesPAE.ANO_CONTRATO, required = true, value = "Valor que representa o ano de criação do Contrato Adminstrativo.")
    private Integer anoContrato;

    @XmlElement(name = ConstantesPAE.DESCRICAO_CONTRATO)
    @ApiModelProperty(name = ConstantesPAE.DESCRICAO_CONTRATO, required = true, value = "Descrição do livre vinculada ao Contrato Administrativo.")
    private String descricaoContrato;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesPAE.DATA_HORA_INCLUSAO)
    @ApiModelProperty(name = ConstantesPAE.DATA_HORA_INCLUSAO, required = false, value = "Data/Hora de criação do Contrato Administrativo.", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraInclusao;

    @XmlElement(name = ConstantesPAE.MATRICULA_INCLUSAO)
    @ApiModelProperty(name = ConstantesPAE.MATRICULA_INCLUSAO, required = true, value = "Matricula do empregado responsavel pela realização do cadastro do Contrato Administrativo.", example = "c999999")
    private String matriculaInclusao;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @XmlJavaTypeAdapter(value = CPFAdapter.class)
    @XmlElement(name = ConstantesPAE.CPF_FORNECEDOR)
    @ApiModelProperty(name = ConstantesPAE.CPF_FORNECEDOR, value = "CPF do fornecedor caso o mesmo seja pessoa fisica.", example = "999.999.999-99")
    private String cpfFornecedor;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @XmlJavaTypeAdapter(value = CNPJAdapter.class)
    @XmlElement(name = ConstantesPAE.CNPJ_FORNECEDOR)
    @ApiModelProperty(name = ConstantesPAE.CNPJ_FORNECEDOR, value = "CNPJ do fornecedor caso o mesmo seja pessoa fisica.", example = "99.999.999/9999-99")
    private String cnpjFornecedor;

    @XmlElement(name = ConstantesPAE.UNIDADE_OPERACIONAL)
    @ApiModelProperty(name = ConstantesPAE.UNIDADE_OPERACIONAL, required = true, value = "CGC da unidade operacional do Contrato Adminstrativo.")
    private Integer unidadeOperacional;

    public ContratoAdministrativoDTO() {
        super();
    }

    public ContratoAdministrativoDTO(ContratoAdministrativo contrato) {
        this();
        this.id = contrato.getId();
        this.numeroContrato = contrato.getNumeroContrato();
        this.anoContrato = contrato.getAnoContrato();
        this.descricaoContrato = contrato.getDescricaoContrato();
        this.dataHoraInclusao = contrato.getDataHoraInclusao();
        this.matriculaInclusao = contrato.getMatriculaInclusao();
        if (ConstantesUtil.TAMANHO_CPF.equals(contrato.getCpfCnpjFornecedor().length())) {
            this.cpfFornecedor = contrato.getCpfCnpjFornecedor();
        } else if (ConstantesUtil.TAMANHO_CNPJ.equals(contrato.getCpfCnpjFornecedor().length())) {
            this.cnpjFornecedor = contrato.getCpfCnpjFornecedor();
        }
        this.unidadeOperacional = contrato.getUnidadeOperacional();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Calendar getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(Calendar dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public String getMatriculaInclusao() {
        return matriculaInclusao;
    }

    public void setMatriculaInclusao(String matriculaInclusao) {
        this.matriculaInclusao = matriculaInclusao;
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
}
