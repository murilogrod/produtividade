package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao;

import br.gov.caixa.simtr.modelo.entidade.ElementoConteudo;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoValidacao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoValidacao.XML_ROOT_ELEMENT_ELEMENTO_CONTEUDO_PENDENTE)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoValidacao.API_MODEL_V1_ELEMENTO_CONTEUDO_PENDENTE,
        description = "Objeto utilizado para representar o Tipo de Documento no contexto da validação de um dossiê de produto"
)
public class ElementoConteudoPendenteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.ID, required = true, value = "Valor que identifica o Tipo de Documento.")
    private Long id;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.NOME, required = true, value = "Valor que reprensenta o nome do Tipo de Documento.")
    private String nome;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.ELEMENTO_CONTEUDO_LOCALIZADO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.ELEMENTO_CONTEUDO_LOCALIZADO, required = true, value = "Indica se o elemento conteudo informado foi identificado associado a estrutura processo/produto ou não")
    private boolean elementoConteudoLocalizado;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.QUANTIDADE_PENDENTES)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.QUANTIDADE_PENDENTES, required = true, value = "Indicação de quantos documentos estão pendentes para o elemento de conteudo indicado")
    private Integer quantidadePendente;
    
    public ElementoConteudoPendenteDTO(Long identificadorElementoPendente) {
        super();
        this.id = identificadorElementoPendente;
        this.elementoConteudoLocalizado = Boolean.FALSE;
    }

    public ElementoConteudoPendenteDTO(ElementoConteudo elementoConteudo, Integer quantidadePendente) {
        super();
        if (elementoConteudo != null) {
            this.id = elementoConteudo.getId();
            this.nome = elementoConteudo.getTipoDocumento() != null ? elementoConteudo.getTipoDocumento().getNome() : elementoConteudo.getNomeElemento();
            this.quantidadePendente = quantidadePendente;
            this.elementoConteudoLocalizado = Boolean.TRUE;
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

    public Integer getQuantidadePendente() {
        return quantidadePendente;
    }

    public void setQuantidadePendente(Integer quantidadePendente) {
        this.quantidadePendente = quantidadePendente;
    }

    public boolean isElementoConteudoLocalizado() {
        return elementoConteudoLocalizado;
    }

    public void setElementoConteudoLocalizado(boolean elementoConteudoLocalizado) {
        this.elementoConteudoLocalizado = elementoConteudoLocalizado;
    }
}
