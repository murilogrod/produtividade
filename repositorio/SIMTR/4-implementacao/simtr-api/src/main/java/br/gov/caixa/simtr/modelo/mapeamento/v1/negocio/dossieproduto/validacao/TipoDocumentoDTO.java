package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoValidacao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoValidacao.XML_ROOT_ELEMENT_TIPO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoValidacao.API_MODEL_V1_TIPO_DOCUMENTO,
        description = "Objeto utilizado para representar o Tipo de Documento no contexto da validação de um dossiê de produto"
)
public class TipoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.ID, required = true, value = "Valor que identifica o Tipo de Documento.")
    private Integer id;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.NOME, required = true, value = "Valor que reprensenta o nome do Tipo de Documento.")
    private String nome;
    
    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.DESCRICAO_PENDENCIA)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.DESCRICAO_PENDENCIA, required = true, value = "Apresenta uma descrição do problema identificado com o tipo de documento")
    private String descricaoPendencia;
    
    @XmlElement(name = ConstantesCadastroTipoDocumento.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATIVO, value = "Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema.")    
    private Boolean ativo;        

    public TipoDocumentoDTO() {
        super();
    }

    public TipoDocumentoDTO(Integer id, String descricaoPendencia) {
        this();
        this.id = id;
        this.nome = "TIPOLOGIA NÃO RECONHECIDA PARA VINCULO INFORMADO";
        this.descricaoPendencia = descricaoPendencia;
    }
    
    public TipoDocumentoDTO(TipoDocumento tipoDocumento, String descricaoPendencia) {
        this();
        this.id = tipoDocumento.getId();
        this.nome = tipoDocumento.getNome();
        this.descricaoPendencia = descricaoPendencia;
        this.ativo = tipoDocumento.getAtivo();
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

    public String getDescricaoPendencia() {
        return descricaoPendencia;
    }

    public void setDescricaoPendencia(String descricaoPendencia) {
        this.descricaoPendencia = descricaoPendencia;
    }
    
    public Boolean getAtivo() {
		return ativo;
	}
    
    public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
}
