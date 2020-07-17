package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoManutencao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoManutencao.XML_ROOT_ELEMENT_VINCULO_PESSOA)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoManutencao.API_MODEL_V1_VINCULO_PESSOA,
        description = "Objeto utilizado para representar o vinculo em criação entre o Dossiê de Produto e o Dossiê Cliente sob a ótica do Dossiê de Produto"
)
public class VinculoPessoaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.DOSSIE_CLIENTE)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.DOSSIE_CLIENTE, required = true, value = "Identificador do dossiê de cliente relacionado diretamente no vinculo")
    private Long idDossieCliente;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.DOSSIE_CLIENTE_RELACIONADO, required = false)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.DOSSIE_CLIENTE_RELACIONADO, required = false, value = "Identificador do dossiê de cliente relacionado de forma secundaria no vinculo. Esta caso só deve ser utilizado quando o tipo de vinculo requer identificação. Ex. Socio (Neste campo deve ser indicado a PJ em que o dossie_cleinte compões sociedade)")
    private Long idDossieClienteRelacionado;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.SEQUENCIA_TITULARIDADE, required = false)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.SEQUENCIA_TITULARIDADE, required = false, value = "Indica a sequencia de titularidade quando da contratação de produtos com multipla titularidade")
    private Integer sequenciaTitularidade;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.TIPO_RELACIONAMENTO, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.TIPO_RELACIONAMENTO, required = true, value = "Identificador da forma de relacionamento do dossiê de cliente perante o dossiê de produto em criação")
    private Integer tipoRelacionamento;

    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.DOCUMENTOS_UTILIZADOS)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.DOCUMENTO_UTILIZADO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.DOCUMENTOS_UTILIZADOS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.DOCUMENTOS_UTILIZADOS, required = false, value = "Lista dos documentos já vinculados ao dossiê do cliente e utilizados na vinculação com o dossiê de produto caracterizando o reuso")
    private List<Long> documentosUtilizados;

    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.DOCUMENTOS_NOVOS)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.DOCUMENTO_NOVO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.DOCUMENTOS_NOVOS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.DOCUMENTOS_NOVOS, required = false, value = "Lista dos documentos novos carregados para utilização no vinculo")
    private List<DocumentoDTO> documentosNovosDTO;
    
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.RESPOSTAS_FORMULARIO)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.RESPOSTA_FORMULARIO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.RESPOSTAS_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.RESPOSTAS_FORMULARIO, required = false, value = "Lista de respostas de formulario submetidas ao dossiê de produto associadas ao vinculo de pessoa")
    private List<RespostaFormularioDTO> respostasFormularioDTO;

    public VinculoPessoaDTO() {
        super();
        this.documentosUtilizados = new ArrayList<>();
        this.documentosNovosDTO = new ArrayList<>();
        this.respostasFormularioDTO = new ArrayList<>();
    }

    public Long getIdDossieCliente() {
        return idDossieCliente;
    }

    public void setIdDossieCliente(Long idDossieCliente) {
        this.idDossieCliente = idDossieCliente;
    }

    public Long getIdDossieClienteRelacionado() {
        return idDossieClienteRelacionado;
    }

    public void setIdDossieClienteRelacionado(Long idDossieClienteRelacionado) {
        this.idDossieClienteRelacionado = idDossieClienteRelacionado;
    }

    public Integer getSequenciaTitularidade() {
        return sequenciaTitularidade;
    }

    public void setSequenciaTitularidade(Integer sequenciaTitularidade) {
        this.sequenciaTitularidade = sequenciaTitularidade;
    }

    public Integer getTipoRelacionamento() {
        return tipoRelacionamento;
    }

    public void setTipoRelacionamento(Integer tipoRelacionamento) {
        this.tipoRelacionamento = tipoRelacionamento;
    }

    public List<Long> getDocumentosUtilizados() {
        return documentosUtilizados;
    }

    public void setDocumentosUtilizados(List<Long> documentosUtilizados) {
        this.documentosUtilizados = documentosUtilizados;
    }

    public List<DocumentoDTO> getDocumentosNovosDTO() {
        return documentosNovosDTO;
    }

    public void setDocumentosNovosDTO(List<DocumentoDTO> documentosNovosDTO) {
        this.documentosNovosDTO = documentosNovosDTO;
    }

    public List<RespostaFormularioDTO> getRespostasFormularioDTO() {
        return respostasFormularioDTO;
    }

    public void setRespostasFormularioDTO(List<RespostaFormularioDTO> respostasFormularioDTO) {
        this.respostasFormularioDTO = respostasFormularioDTO;
    }
}
