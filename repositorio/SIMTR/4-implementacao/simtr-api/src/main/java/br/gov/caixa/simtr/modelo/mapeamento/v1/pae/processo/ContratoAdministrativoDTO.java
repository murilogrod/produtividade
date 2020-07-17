package br.gov.caixa.simtr.modelo.mapeamento.v1.pae.processo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.ContratoAdministrativo;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CNPJAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CPFAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.documento.DocumentoAdministrativoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.UnidadeAutorizadaDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesPAE;

@XmlRootElement(name = ConstantesPAE.XML_ROOT_ELEMENT_CONTRATO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesPAE.VISAO_PROCESSO_API_MODEL_CONTRATO_ADMINISTRATIVO,
        description = "Objeto utilizado para representar o Contrato Adminstrativo no retorno as consultas realizadas sob a otica da consulta pelo Processo."
)
public class ContratoAdministrativoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesPAE.ID)
    @ApiModelProperty(name = ConstantesPAE.ID, accessMode = AccessMode.READ_ONLY, value = "Valor que representa o identificador do Contrato Administrativo.")
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

    //*******************************************
    @JsonProperty(value = ConstantesPAE.APENSOS)
    @XmlElement(name = ConstantesPAE.APENSO)
    @XmlElementWrapper(name = ConstantesPAE.APENSOS)
    @ApiModelProperty(name = ConstantesPAE.APENSOS, required = false, value = "Lista de Apensos Administrativos vinculados ao contrato.")
    private Set<ApensoAdministrativoDTO> apensosAdministrativosDTO;

    @JsonProperty(value = ConstantesPAE.DOCUMENTOS)
    @XmlElement(name = ConstantesPAE.DOCUMENTO)
    @XmlElementWrapper(name = ConstantesPAE.DOCUMENTOS)
    @ApiModelProperty(name = ConstantesPAE.DOCUMENTOS, required = false, value = "Lista de Documentos Administrativos vinculados ao contrato.")
    private Set<DocumentoAdministrativoDTO> documentosAdministrativosDTO;

    @JsonProperty(value = ConstantesPAE.AUTORIZADAS)
    @XmlElement(name = ConstantesPAE.AUTORIZADA)
    @XmlElementWrapper(name = ConstantesPAE.AUTORIZADAS)
    @ApiModelProperty(name = ConstantesPAE.AUTORIZADAS, required = false, value = "Lista de unidades autorizadas a manipular o contrato.")
    private Set<UnidadeAutorizadaDTO> unidadesAutorizadasDTO;

    public ContratoAdministrativoDTO() {
        super();
        this.apensosAdministrativosDTO = new HashSet<>();
        this.documentosAdministrativosDTO = new HashSet<>();
        this.unidadesAutorizadasDTO = new HashSet<>();
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

        if (contrato.getApensosAdministrativos() != null) {
            contrato.getApensosAdministrativos()
                    .forEach(apenso -> this.apensosAdministrativosDTO.add(new ApensoAdministrativoDTO(apenso)));
        }

        if (contrato.getDocumentosAdministrativos() != null) {
            contrato.getDocumentosAdministrativos().stream()
                    .filter(documento -> documento.getDataHoraExclusao() == null)
                    .forEach(documento -> this.documentosAdministrativosDTO.add(new DocumentoAdministrativoDTO(documento)));
        }
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

    public Set<ApensoAdministrativoDTO> getApensosAdministrativosDTO() {
        return apensosAdministrativosDTO;
    }

    public void setApensosAdministrativosDTO(Set<ApensoAdministrativoDTO> apensosAdministrativosDTO) {
        this.apensosAdministrativosDTO = apensosAdministrativosDTO;
    }

    public Set<DocumentoAdministrativoDTO> getDocumentosAdministrativosDTO() {
        return documentosAdministrativosDTO;
    }

    public void setDocumentosAdministrativosDTO(Set<DocumentoAdministrativoDTO> documentosAdministrativosDTO) {
        this.documentosAdministrativosDTO = documentosAdministrativosDTO;
    }

    public Set<UnidadeAutorizadaDTO> getUnidadesAutorizadasDTO() {
        return unidadesAutorizadasDTO;
    }

    public void setUnidadesAutorizadasDTO(Set<UnidadeAutorizadaDTO> unidadesAutorizadasDTO) {
        this.unidadesAutorizadasDTO = unidadesAutorizadasDTO;
    }

}
