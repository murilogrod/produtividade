package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente;

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
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalDossieCliente.XML_ROOT_ELEMENT_DOSSIE_CLIENTE)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = ConstantesDossieDigitalDossieCliente.TIPO_PESSOA)
@JsonSubTypes({
    @JsonSubTypes.Type(value = DossieClientePFDTO.class, name = "F")
})
@ApiModel(value = ConstantesDossieDigitalDossieCliente.API_MODEL_V1_DOSSIE_CLIENTE,
          description = "Objeto utilizado para representar o dossiê cliente (comum a PF e PJ) no retorno as consultas realizadas no contexto do dossiê digital",
          subTypes =
          {
              DossieClientePFDTO.class,
              DossieClientePJDTO.class
          }, discriminator = ConstantesDossieDigitalDossieCliente.TIPO_PESSOA)
public abstract class DossieClienteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.ID)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.ID, required = true, value = "Identificador do dossiê do cliente. Valor gerado e devolvido pelo sistema",
                      accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    protected Long id;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.NOME)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.NOME, required = true, value = "Nome de identificação do cliente")
    protected String nome;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.EMAIL)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.EMAIL, required = true, value = "Email de contato do cliente utilizado nas rotinas de comunicação com o SSO")
    protected String email;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalDossieCliente.DATA_HORA_APURACAO_NIVEL)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.DATA_HORA_APURACAO_NIVEL, required = false, value = "Data e hora de apuração do nivel documental", example = "dd/MM/yyyy hh:mm:ss")
    protected Calendar dataApuracaoNivel;

    // *****************************************
    @XmlTransient
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.TIPO_PESSOA, required = false, value = "Identificador do tipo de pessoa", allowableValues = "F,J")
    protected TipoPessoaEnum tipoPessoa;

    // *****************************************
    @XmlElement(name = ConstantesDossieDigitalDossieCliente.DOCUMENTO)
    @XmlElementWrapper(name = ConstantesDossieDigitalDossieCliente.DOCUMENTOS)
    @JsonProperty(value = ConstantesDossieDigitalDossieCliente.DOCUMENTOS)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.DOCUMENTOS, required = false, value = "Lista de documentos vinculados ao cliente")
    protected List<DocumentoDTO> documentosDTO;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.PRODUTO)
    @XmlElementWrapper(name = ConstantesDossieDigitalDossieCliente.PRODUTOS_HABILITADOS)
    @JsonProperty(value = ConstantesDossieDigitalDossieCliente.PRODUTOS_HABILITADOS)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.PRODUTOS_HABILITADOS, required = false, value = "Lista de produtos habilitados para o cliente sob as regras do dossiê digital")
    protected List<ProdutoHabilitadoDTO> produtosHabilitadosDTO;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.PENDENCIA_CADASTRO)
    @XmlElementWrapper(name = ConstantesDossieDigitalDossieCliente.PENDENCIAS_CADASTRO)
    @JsonProperty(value = ConstantesDossieDigitalDossieCliente.PENDENCIAS_CADASTRO)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.PENDENCIAS_CADASTRO, required = false,
                      value = "Lista de possiveis pendnecias de documentos necessárias para realização do cadastro CAIXA sob as regras do dossiê digital")
    protected List<PendenciaCadastroDTO> pendenciasCadastroDTO;

    public DossieClienteDTO() {
        super();
        this.documentosDTO = new ArrayList<>();
        this.produtosHabilitadosDTO = new ArrayList<>();
        this.pendenciasCadastroDTO = new ArrayList<>();
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
                    composicaoDocumental.getProdutos().forEach(produto -> this.produtosHabilitadosDTO.add(new ProdutoHabilitadoDTO(produto)));
                }
            });
        }
    }

    public DossieClienteDTO(DossieCliente dossieCliente, List<PendenciaCadastroDTO> pendenciasCadastrais) {
        this(dossieCliente);
        if (pendenciasCadastrais != null) {
            this.pendenciasCadastroDTO = pendenciasCadastrais;
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

    public List<PendenciaCadastroDTO> getPendenciasCadastroDTO() {
        return pendenciasCadastroDTO;
    }

    public void setPendenciasCadastroDTO(List<PendenciaCadastroDTO> pendenciasCadastroDTO) {
        this.pendenciasCadastroDTO = pendenciasCadastroDTO;
    }
}
