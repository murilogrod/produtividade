package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieCliente.XML_ROOT_ELEMENT_DOSSIE_CLIENTE)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = ConstantesNegocioDossieCliente.TIPO_PESSOA)
@JsonSubTypes({ @JsonSubTypes.Type(value = DossieClientePFDTO.class, name = "F"),
	@JsonSubTypes.Type(value = DossieClientePJDTO.class, name = "J") })
@ApiModel(value = ConstantesNegocioDossieCliente.API_MODEL_V1_DOSSIE_CLIENTE, description = "Objeto utilizado para representar o dossiê cliente (comum a PF e PJ) no retorno as consultas realizadas a partir do Dossiê do Cliente", subTypes = {
	DossieClientePFDTO.class,
	DossieClientePJDTO.class }, discriminator = ConstantesNegocioDossieCliente.TIPO_PESSOA)
public abstract class DossieClienteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieCliente.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.ID, required = true, value = "Identificador do dossiê do cliente. Valor gerado e devolvido pelo sistema", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    private Long id;

    @XmlElement(name = ConstantesNegocioDossieCliente.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.NOME, required = true, value = "Nome de identificação do cliente")
    private String nome;

    @XmlElement(name = ConstantesNegocioDossieCliente.EMAIL)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.EMAIL, required = true, value = "Email de contato do cliente utilizado nas rotinas de comunicação com o SSO")
    private String email;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieCliente.DATA_HORA_APURACAO_NIVEL)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.DATA_HORA_APURACAO_NIVEL, required = false, value = "Data e hora de apuração do nivel documental", example = "dd/MM/yyyy hh:mm:ss")
    private Calendar dataApuracaoNivel;

    // *****************************************
    @XmlTransient
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.TIPO_PESSOA, required = false, value = "Identificador do tipo de pessoa", allowableValues = "F,J")
    protected TipoPessoaEnum tipoPessoa;

    // *****************************************
    @XmlElement(name = ConstantesNegocioDossieCliente.DOCUMENTO)
    @XmlElementWrapper(name = ConstantesNegocioDossieCliente.DOCUMENTOS)
    @JsonProperty(value = ConstantesNegocioDossieCliente.DOCUMENTOS)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.DOCUMENTOS, required = false, value = "Lista de documentos vinculados ao cliente")
    private List<DocumentoDTO> documentosDTO;

    @XmlElement(name = ConstantesNegocioDossieCliente.PRODUTO)
    @XmlElementWrapper(name = ConstantesNegocioDossieCliente.PRODUTOS_HABILITADOS)
    @JsonProperty(value = ConstantesNegocioDossieCliente.PRODUTOS_HABILITADOS)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.PRODUTOS_HABILITADOS, required = false, value = "Lista de produtos habilitados para o cliente sob as regras do dossiê digital")
    private List<ProdutoHabilitadoDTO> produtosHabilitadosDTO;

    @XmlElement(name = ConstantesNegocioDossieCliente.DOSSIE_PRODUTO)
    @XmlElementWrapper(name = ConstantesNegocioDossieCliente.DOSSIES_PRODUTO)
    @JsonProperty(value = ConstantesNegocioDossieCliente.DOSSIES_PRODUTO)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.DOSSIES_PRODUTO, required = false, value = "Lista de dossiês de produto vinculados ao cliente")
    private List<DossieProdutoDTO> dossiesProdutoDTO;

    public DossieClienteDTO() {
	super();
	this.documentosDTO = new ArrayList<>();
	this.produtosHabilitadosDTO = new ArrayList<>();
	this.dossiesProdutoDTO = new ArrayList<>();
    }

    public DossieClienteDTO(DossieCliente dossieCliente) {
	this();
	this.id = dossieCliente.getId();
	this.nome = dossieCliente.getNome();
	this.email = dossieCliente.getEmail();
	this.dataApuracaoNivel = dossieCliente.getDataHoraApuracaoNivel();

	if (dossieCliente.getDocumentos() != null) {
	    dossieCliente.getDocumentos().forEach(documento -> this.documentosDTO.add(new DocumentoDTO(documento)));
	}

	if (dossieCliente.getComposicoesDocumentais() != null) {
	    dossieCliente.getComposicoesDocumentais().forEach(composicaoDocumental -> {
		if (composicaoDocumental.getProdutos() != null) {
		    composicaoDocumental.getProdutos()
			    .forEach(produto -> this.produtosHabilitadosDTO.add(new ProdutoHabilitadoDTO(produto)));
		}
	    });
	}
	if (dossieCliente.getDossiesClienteProduto() != null) {
	    dossieCliente.getDossiesClienteProduto().forEach(
		    dossieClienteProduto -> this.dossiesProdutoDTO.add(new DossieProdutoDTO(dossieClienteProduto)));
	}
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getNome() {
	return nome;
    }

    public void setNome(String nome) {
	this.nome = nome;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public Calendar getDataApuracaoNivel() {
	return dataApuracaoNivel;
    }

    public void setDataApuracaoNivel(Calendar dataApuracaoNivel) {
	this.dataApuracaoNivel = dataApuracaoNivel;
    }

    public TipoPessoaEnum getTipoPessoa() {
	return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoaEnum tipoPessoa) {
	this.tipoPessoa = tipoPessoa;
    }

    public List<DocumentoDTO> getDocumentosDTO() {
	return documentosDTO;
    }

    public void setDocumentosDTO(List<DocumentoDTO> documentosDTO) {
	this.documentosDTO = documentosDTO;
    }

    public List<ProdutoHabilitadoDTO> getProdutosHabilitadosDTO() {
	return produtosHabilitadosDTO;
    }

    public void setProdutosHabilitadosDTO(List<ProdutoHabilitadoDTO> produtosHabilitadosDTO) {
	this.produtosHabilitadosDTO = produtosHabilitadosDTO;
    }

    public List<DossieProdutoDTO> getDossiesProdutoDTO() {
	return dossiesProdutoDTO;
    }

    public void setDossiesProdutoDTO(List<DossieProdutoDTO> dossiesProdutoDTO) {
	this.dossiesProdutoDTO = dossiesProdutoDTO;
    }
}
