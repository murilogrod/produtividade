package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoValidacao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoValidacao.XML_ROOT_ELEMENT_FUNCAO_DOCUMENTAL)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoValidacao.API_MODEL_V1_FUNCAO_DOCUMENTAL,
        description = "Objeto utilizado para representar a Função Documental no contexto da validação de um dossiê de produto"
)
public class FuncaoDocumentalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.ID)
    @ApiModelProperty(value = "Identificador da função documental.", required = true)
    private Integer id;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.NOME)
    @ApiModelProperty(value = "Nome negocial da função documental.", required = true)
    private String nome;
    
    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.DESCRICAO_PENDENCIA)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.DESCRICAO_PENDENCIA, required = true, value = "Apresenta uma descrição do problema identificado com a função documental")
    private String descricaoPendencia;

    public FuncaoDocumentalDTO() {
        super();
    }

    public FuncaoDocumentalDTO(FuncaoDocumental funcaoDocumental, String descricaoPendencia) {
        this();
        this.id = funcaoDocumental.getId();
        this.nome = funcaoDocumental.getNome();
        this.descricaoPendencia = descricaoPendencia;
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
}
