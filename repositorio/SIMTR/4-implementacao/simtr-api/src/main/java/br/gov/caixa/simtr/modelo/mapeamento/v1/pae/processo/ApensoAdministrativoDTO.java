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

import br.gov.caixa.simtr.modelo.entidade.ApensoAdministrativo;
import br.gov.caixa.simtr.modelo.enumerator.TipoApensoEnum;
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

@XmlRootElement(name = ConstantesPAE.XML_ROOT_ELEMENT_APENSO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesPAE.VISAO_PROCESSO_API_MODEL_APENSO_ADMINISTRATIVO,
        description = "Objeto utilizado para representar o Apenso Adminstrativo no retorno as consultas realizadas sob a otica da consulta pelo Processo."
)
public class ApensoAdministrativoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesPAE.ID)
    @ApiModelProperty(name = ConstantesPAE.ID, required = true, accessMode = AccessMode.READ_ONLY, value = "Valor que representa o identificador do Apenso Administrativo.")
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CPFAdapter.class)
    @XmlElement(name = ConstantesPAE.CPF_FORNECEDOR)
    @ApiModelProperty(name = ConstantesPAE.CPF_FORNECEDOR, value = "CPF do fornecedor caso o mesmo seja pessoa fisica.", example = "999.999.999-99")
    private String cpfFornecedor;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CNPJAdapter.class)
    @XmlElement(name = ConstantesPAE.CNPJ_FORNECEDOR)
    @ApiModelProperty(name = ConstantesPAE.CNPJ_FORNECEDOR, value = "CNPJ do fornecedor caso o mesmo seja pessoa fisica.", example = "99.999.999/9999-99")
    private String cnpjFornecedor;

    @XmlElement(name = ConstantesPAE.TIPO_APENSO)
    @ApiModelProperty(name = ConstantesPAE.TIPO_APENSO, required = true, value = "Indicador do tipo Apenso Administrativo.")
    private TipoApensoEnum tipoApenso;

    @XmlElement(name = ConstantesPAE.DESCRICAO_APENSO)
    @ApiModelProperty(name = ConstantesPAE.DESCRICAO_APENSO, required = false, value = "Descrição do livre vinculada ao Contrato Administrativo.")
    private String descricaoApenso;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesPAE.DATA_HORA_INCLUSAO)
    @ApiModelProperty(name = ConstantesPAE.DATA_HORA_INCLUSAO, required = true, value = "Data/Hora de criação do Apenso Administrativo.", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraInclusao;

    @XmlElement(name = ConstantesPAE.MATRICULA_INCLUSAO)
    @ApiModelProperty(name = ConstantesPAE.MATRICULA_INCLUSAO, required = true, value = "Matricula do empregado responsavel pela realização do cadastro do Apenso Administrativo.", example = "c999999")
    private String matriculaInclusao;

    @XmlElement(name = ConstantesPAE.PROTOCOLO_SICLG)
    @ApiModelProperty(name = ConstantesPAE.PROTOCOLO_SICLG, required = true, value = "Codigo de identificação da demanda inicial junto ao SICLG. Dado informativo, não há integração entre os sistemas.")
    private String protocoloSICLG;

    @XmlElement(name = ConstantesPAE.TITULO_APENSO)
    @ApiModelProperty(name = ConstantesPAE.TITULO_APENSO, required = false, value = "Descrição do livre vinculada ao titulo de um Apenso Administrativo.")
    private String tituloApenso;

    //*******************************************
    @JsonProperty(value = ConstantesPAE.DOCUMENTOS)
    @XmlElement(name = ConstantesPAE.DOCUMENTO)
    @XmlElementWrapper(name = ConstantesPAE.DOCUMENTOS)
    @ApiModelProperty(name = ConstantesPAE.DOCUMENTOS, required = false, value = "Lista de Documentos Administrativos vinculados ao apenso.")
    private Set<DocumentoAdministrativoDTO> documentosAdministrativoDTO;

    @JsonProperty(value = ConstantesPAE.AUTORIZADAS)
    @XmlElement(name = ConstantesPAE.AUTORIZADA)
    @XmlElementWrapper(name = ConstantesPAE.AUTORIZADAS)
    private Set<UnidadeAutorizadaDTO> unidadesAutorizadasDTO;

    public ApensoAdministrativoDTO() {
        super();
        this.documentosAdministrativoDTO = new HashSet<>();
        this.unidadesAutorizadasDTO = new HashSet<>();
    }

    public ApensoAdministrativoDTO(ApensoAdministrativo apenso) {
        this();
        this.id = apenso.getId();
        if (apenso.getCpfCnpjFornecedor() != null) {
            if (ConstantesUtil.TAMANHO_CPF.equals(apenso.getCpfCnpjFornecedor().length())) {
                this.cpfFornecedor = apenso.getCpfCnpjFornecedor();
            } else if (ConstantesUtil.TAMANHO_CNPJ.equals(apenso.getCpfCnpjFornecedor().length())) {
                this.cnpjFornecedor = apenso.getCpfCnpjFornecedor();
            }
        }
        this.tipoApenso = apenso.getTipoApenso();
        this.descricaoApenso = apenso.getDescricaoApenso();
        this.dataHoraInclusao = apenso.getDataHoraInclusao();
        this.matriculaInclusao = apenso.getMatriculaInclusao();
        this.protocoloSICLG = apenso.getProtocoloSICLG();
        this.tituloApenso = apenso.getTitulo();

        if (apenso.getDocumentosAdministrativos() != null) {
            apenso.getDocumentosAdministrativos().stream()
                    .filter(documento -> documento.getDataHoraExclusao() == null)
                    .forEach(documento -> this.documentosAdministrativoDTO.add(new DocumentoAdministrativoDTO(documento)));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public TipoApensoEnum getTipoApenso() {
        return tipoApenso;
    }

    public void setTipoApenso(TipoApensoEnum tipoApenso) {
        this.tipoApenso = tipoApenso;
    }

    public String getDescricaoApenso() {
        return descricaoApenso;
    }

    public void setDescricaoApenso(String descricaoApenso) {
        this.descricaoApenso = descricaoApenso;
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

    public String getProtocoloSICLG() {
        return protocoloSICLG;
    }

    public void setProtocoloSICLG(String protocoloSICLG) {
        this.protocoloSICLG = protocoloSICLG;
    }

    public String getTituloApenso() {
        return tituloApenso;
    }

    public void setTituloApenso(String tituloApenso) {
        this.tituloApenso = tituloApenso;
    }

    public Set<DocumentoAdministrativoDTO> getDocumentosAdministrativoDTO() {
        return documentosAdministrativoDTO;
    }

    public void setDocumentosAdministrativoDTO(Set<DocumentoAdministrativoDTO> documentosAdministrativoDTO) {
        this.documentosAdministrativoDTO = documentosAdministrativoDTO;
    }

    public Set<UnidadeAutorizadaDTO> getUnidadesAutorizadasDTO() {
        return unidadesAutorizadasDTO;
    }

    public void setUnidadesAutorizadasDTO(Set<UnidadeAutorizadaDTO> unidadesAutorizadasDTO) {
        this.unidadesAutorizadasDTO = unidadesAutorizadasDTO;
    }
}
