package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieCliente.XML_ROOT_ELEMENT_TIPO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieCliente.API_MODEL_V1_TIPO_DOCUMENTO,
        description = "Objeto utilizado para representar o Tipo de Documento no retorno as consultas realizadas a partir do Dossiê do Cliente"
)
public class TipoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieCliente.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.ID, required = true, value = "Valor que identifica o Tipo de Documento")
    private Integer id;

    @XmlElement(name = ConstantesNegocioDossieCliente.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.NOME, required = true, value = "Valor que reprensenta o nome do Tipo de Documento")
    private String nome;

    @XmlElement(name = "permite_reuso")
    @ApiModelProperty(name = "permite_reuso", required = true, value = "Valor que indica se o tipo de documento possibilita o reuso.")
    private Boolean indicacaoReuso;
    
    @JsonProperty(value = ConstantesCadastroTipoDocumento.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATIVO, value = "Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema.")    
    private Boolean ativo;

    // ***********************************
    @XmlElement(name = ConstantesNegocioDossieCliente.FUNCAO_DOCUMENTAL)
    @XmlElementWrapper(name = ConstantesNegocioDossieCliente.FUNCOES_DOCUMENTAIS)
    @JsonProperty(value = ConstantesNegocioDossieCliente.FUNCOES_DOCUMENTAIS)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.FUNCOES_DOCUMENTAIS, required = true, value = "Valor que reprensenta o nome do Tipo de Documento")
    private List<FuncaoDocumentalDTO> funcoesDocumentais;

    public TipoDocumentoDTO() {
        super();
        this.funcoesDocumentais = new ArrayList<>();
    }

    public TipoDocumentoDTO(TipoDocumento tipoDocumento) {
        this();
        this.id = tipoDocumento.getId();
        this.nome = tipoDocumento.getNome();
        this.indicacaoReuso = tipoDocumento.getReuso();
        this.ativo = tipoDocumento.getAtivo();

        if (tipoDocumento.getFuncoesDocumentais() != null) {
            tipoDocumento.getFuncoesDocumentais().forEach(funcaoDocumental -> this.funcoesDocumentais.add(new FuncaoDocumentalDTO(funcaoDocumental)));
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getIndicacaoReuso() {
        return indicacaoReuso;
    }

    public void setIndicacaoReuso(Boolean indicacaoReuso) {
        this.indicacaoReuso = indicacaoReuso;
    }

    public List<FuncaoDocumentalDTO> getFuncoesDocumentais() {
        return funcoesDocumentais;
    }

    public void setFuncoesDocumentais(List<FuncaoDocumentalDTO> funcoesDocumentais) {
        this.funcoesDocumentais = funcoesDocumentais;
    }
}
