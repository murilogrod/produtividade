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

import br.gov.caixa.simtr.modelo.entidade.ProcessoAdministrativo;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.documento.DocumentoAdministrativoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.pae.UnidadeAutorizadaDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesPAE;

@XmlRootElement(name = ConstantesPAE.XML_ROOT_ELEMENT_PROCESSO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesPAE.VISAO_PROCESSO_API_MODEL_PROCESSO_ADMINISTRATIVO,
        description = "Objeto utilizado para representar o Processo Adminstrativo no retorno as consultas realizadas sob a otica da consulta pelo Processo."
)
public class ProcessoAdministrativoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesPAE.ID)
    @ApiModelProperty(name = ConstantesPAE.ID, required = true, value = "Valor que representa o identificador do Processo Administrativo.")
    private Long id;

    @XmlElement(name = ConstantesPAE.NUMERO_PROCESSO)
    @ApiModelProperty(name = ConstantesPAE.NUMERO_PROCESSO, required = true, value = "Valor que representa o numero do Processo Adminstrativo.")
    private Integer numeroProcesso;

    @XmlElement(name = ConstantesPAE.ANO_PROCESSO)
    @ApiModelProperty(name = ConstantesPAE.ANO_PROCESSO, required = true, value = "Valor que representa o ano de criação do Processo Adminstrativo.")
    private Integer anoProcesso;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @XmlElement(name = ConstantesPAE.NUMERO_PREGAO)
    @ApiModelProperty(name = ConstantesPAE.NUMERO_PREGAO, required = false, value = "Valor que representa o numero do pregão originado pelo Processo Administrativo.")
    private Integer numeroPregao;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @XmlElement(name = ConstantesPAE.UNIDADE_CONTRATACAO)
    @ApiModelProperty(name = ConstantesPAE.UNIDADE_CONTRATACAO, required = false, value = "Valor que representa a unidade CAIXA responsável pela execução do pregão.")
    private Integer unidadePregao;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @XmlElement(name = ConstantesPAE.UNIDADE_DEMANDANTE)
    @ApiModelProperty(name = ConstantesPAE.UNIDADE_DEMANDANTE, required = true, value = "Valor que representa a unidade CAIXA responsável solicitação do produto/serviço.")
    private Integer unidadeDemandante;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @XmlElement(name = ConstantesPAE.ANO_PREGAO)
    @ApiModelProperty(name = ConstantesPAE.ANO_PREGAO, required = false, value = "Valor que representa o ano de execução do pregão.")
    private Integer anoPregao;

    @XmlElement(name = ConstantesPAE.OBJETO_CONTRATACAO)
    @ApiModelProperty(name = ConstantesPAE.OBJETO_CONTRATACAO, required = true, value = "Descrição do objeto de contratação para o pregão em referência.")
    private String objetoContratacao;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesPAE.DATA_HORA_INCLUSAO)
    @ApiModelProperty(name = ConstantesPAE.DATA_HORA_INCLUSAO, required = false, value = "Data/Hora de criação do Processo Administrativo.", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraInclusao;

    @XmlElement(name = ConstantesPAE.MATRICULA_INCLUSAO)
    @ApiModelProperty(name = ConstantesPAE.MATRICULA_INCLUSAO, required = true, value = "Matricula do empregado responsavel pela realização do cadastro do Processo Administrativo.", example = "c999999")
    private String matriculaInclusao;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesPAE.DATA_HORA_FINALIZACAO)
    @ApiModelProperty(name = ConstantesPAE.DATA_HORA_FINALIZACAO, required = false, value = "Data/Hora de finalização do Processo Administrativo.", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraFinalizacao;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @XmlElement(name = ConstantesPAE.MATRICULA_FINALIZACAO)
    @ApiModelProperty(name = ConstantesPAE.MATRICULA_FINALIZACAO, required = true, value = "Matricula do empregado responsavel pela realização da finalização do Processo Administrativo.", example = "c999999")
    private String matriculaFinalizacao;

    @XmlElement(name = ConstantesPAE.PROTOCOLO_SICLG)
    @ApiModelProperty(name = ConstantesPAE.PROTOCOLO_SICLG, required = false, value = "Codigo de identificação da demanda inicial junto ao SICLG. Dado informativo, não há integração entre os sistemas.")
    private String protocoloSICLG;

    //*******************************************
    @JsonProperty(value = ConstantesPAE.DOCUMENTOS)
    @XmlElement(name = ConstantesPAE.DOCUMENTO)
    @XmlElementWrapper(name = ConstantesPAE.DOCUMENTOS)
    @ApiModelProperty(name = ConstantesPAE.DOCUMENTOS, required = false, value = "Lista de Documentos Administrativos vinculados ao processo.")
    private Set<DocumentoAdministrativoDTO> documentosAdministrativosDTO;

    @JsonProperty(value = ConstantesPAE.CONTRATOS)
    @XmlElement(name = ConstantesPAE.CONTRATO)
    @XmlElementWrapper(name = ConstantesPAE.CONTRATOS)
    @ApiModelProperty(name = ConstantesPAE.CONTRATOS, required = false, value = "Lista de Documentos Administrativos vinculados ao processo.")
    private Set<ContratoAdministrativoDTO> contratosAdministrativosDTO;

    @JsonProperty(value = ConstantesPAE.APENSOS)
    @XmlElement(name = ConstantesPAE.APENSO)
    @XmlElementWrapper(name = ConstantesPAE.APENSOS)
    @ApiModelProperty(name = ConstantesPAE.APENSOS, required = false, value = "Lista de Apensos Administrativos vinculados ao processo.")
    private Set<ApensoAdministrativoDTO> apensosAdministrativosDTO;

    @JsonProperty(value = ConstantesPAE.AUTORIZADAS)
    @XmlElement(name = ConstantesPAE.AUTORIZADA)
    @XmlElementWrapper(name = ConstantesPAE.AUTORIZADAS)
    @ApiModelProperty(name = ConstantesPAE.AUTORIZADAS, required = false, value = "Lista de unidades autorizadas a manipular o processo.")
    private Set<UnidadeAutorizadaDTO> unidadesAutorizadasDTO;

    public ProcessoAdministrativoDTO() {
        super();
        this.documentosAdministrativosDTO = new HashSet<>();
        this.contratosAdministrativosDTO = new HashSet<>();
        this.apensosAdministrativosDTO = new HashSet<>();
        this.unidadesAutorizadasDTO = new HashSet<>();
    }

    public ProcessoAdministrativoDTO(ProcessoAdministrativo processo) {
        this();
        this.id = processo.getId();
        this.numeroProcesso = processo.getNumeroProcesso();
        this.anoProcesso = processo.getAnoProcesso();
        this.numeroPregao = processo.getNumeroPregao();
        this.unidadePregao = processo.getUnidadeContratacao();
        this.unidadeDemandante = processo.getUnidadeDemandante();
        this.anoPregao = processo.getAnoPregao();
        this.objetoContratacao = processo.getObjetoContratacao();
        this.dataHoraInclusao = processo.getDataHoraInclusao();
        this.matriculaInclusao = processo.getMatriculaInclusao();
        this.dataHoraFinalizacao = processo.getDataHoraFinalizacao();
        this.matriculaFinalizacao = processo.getMatriculaFinalizacao();
        this.protocoloSICLG = processo.getProtocoloSICLG();

        if (processo.getDocumentosAdministrativos() != null) {
            processo.getDocumentosAdministrativos().stream()
                    .filter(documento -> documento.getDataHoraExclusao() == null)
                    .forEach(documento -> this.documentosAdministrativosDTO.add(new DocumentoAdministrativoDTO(documento)));
        }

        if (processo.getContratosAdministrativos() != null) {
            processo.getContratosAdministrativos()
                    .forEach(contrato -> this.contratosAdministrativosDTO.add(new ContratoAdministrativoDTO(contrato)));
        }

        if (processo.getApensosAdministrativos() != null) {
            processo.getApensosAdministrativos()
                    .forEach(apenso -> this.apensosAdministrativosDTO.add(new ApensoAdministrativoDTO(apenso)));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(Integer numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public Integer getAnoProcesso() {
        return anoProcesso;
    }

    public void setAnoProcesso(Integer anoProcesso) {
        this.anoProcesso = anoProcesso;
    }

    public Integer getNumeroPregao() {
        return numeroPregao;
    }

    public void setNumeroPregao(Integer numeroPregao) {
        this.numeroPregao = numeroPregao;
    }

    public Integer getUnidadePregao() {
        return unidadePregao;
    }

    public void setUnidadePregao(Integer unidadePregao) {
        this.unidadePregao = unidadePregao;
    }

    public Integer getUnidadeDemandante() {
        return unidadeDemandante;
    }

    public void setUnidadeDemandante(Integer unidadeDemandante) {
        this.unidadeDemandante = unidadeDemandante;
    }

    public Integer getAnoPregao() {
        return anoPregao;
    }

    public void setAnoPregao(Integer anoPregao) {
        this.anoPregao = anoPregao;
    }

    public String getObjetoContratacao() {
        return objetoContratacao;
    }

    public void setObjetoContratacao(String objetoContratacao) {
        this.objetoContratacao = objetoContratacao;
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

    public Calendar getDataHoraFinalizacao() {
        return dataHoraFinalizacao;
    }

    public void setDataHoraFinalizacao(Calendar dataHoraFinalizacao) {
        this.dataHoraFinalizacao = dataHoraFinalizacao;
    }

    public String getMatriculaFinalizacao() {
        return matriculaFinalizacao;
    }

    public void setMatriculaFinalizacao(String matriculaFinalizacao) {
        this.matriculaFinalizacao = matriculaFinalizacao;
    }

    public String getProtocoloSICLG() {
        return protocoloSICLG;
    }

    public void setProtocoloSICLG(String protocoloSICLG) {
        this.protocoloSICLG = protocoloSICLG;
    }

    public Set<DocumentoAdministrativoDTO> getDocumentosAdministrativosDTO() {
        return documentosAdministrativosDTO;
    }

    public void setDocumentosAdministrativosDTO(Set<DocumentoAdministrativoDTO> documentosAdministrativosDTO) {
        this.documentosAdministrativosDTO = documentosAdministrativosDTO;
    }

    public Set<ContratoAdministrativoDTO> getContratosAdministrativosDTO() {
        return contratosAdministrativosDTO;
    }

    public void setContratosAdministrativosDTO(Set<ContratoAdministrativoDTO> contratosAdministrativosDTO) {
        this.contratosAdministrativosDTO = contratosAdministrativosDTO;
    }

    public Set<ApensoAdministrativoDTO> getApensosAdministrativosDTO() {
        return apensosAdministrativosDTO;
    }

    public void setApensosAdministrativosDTO(Set<ApensoAdministrativoDTO> apensosAdministrativosDTO) {
        this.apensosAdministrativosDTO = apensosAdministrativosDTO;
    }

    public Set<UnidadeAutorizadaDTO> getUnidadesAutorizadasDTO() {
        return unidadesAutorizadasDTO;
    }

    public void setUnidadesAutorizadasDTO(Set<UnidadeAutorizadaDTO> unidadesAutorizadasDTO) {
        this.unidadesAutorizadasDTO = unidadesAutorizadasDTO;
    }
}
