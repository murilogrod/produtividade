package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoValidacao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlRootElement(name = ConstantesNegocioDossieProdutoValidacao.XML_ROOT_ELEMENT_PENDENCIA_VINCULO_PRODUTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoValidacao.API_MODEL_V1_PENDENCIA_VINCULO_PRODUTO,
        description = "Objeto utilizado para representar um problema apresentado na validação realizada sob os vinculos de produtos em um dossiê de produto a ser incluido ou alterado."
)
public class PendenciaVinculoProdutoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.IDENTIFICADOR_PRODUTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.IDENTIFICADOR_PRODUTO, required = true, value = "Identificador do produto associado a pendência")
    private Integer identificadorProduto;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.CODIGO_OPERACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.CODIGO_OPERACAO, required = true, value = "Código de operação SIICO do produto associado a pendência")
    private Integer codigoOperacaoProduto;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.CODIGO_MODALIDADE)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.CODIGO_MODALIDADE, required = true, value = "Código da modalidade SIICO do produto associado a pendência")
    private Integer codigoModalidadeProduto;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.PRODUTO_LOCALIZADO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.PRODUTO_LOCALIZADO, required = true, value = "Indica se o produto informado foi identificado")
    private boolean produtoLocalizado;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.ELEMENTO_CONTEUDO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoValidacao.ELEMENTOS_CONTEUDO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoValidacao.ELEMENTOS_CONTEUDO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.ELEMENTOS_CONTEUDO, required = true, value = "Elementos de Conteúdo associados a pendência")
    private List<ElementoConteudoPendenteDTO> elementosConteudoPendentes;
    
    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.RESPOSTA_FORMULARIO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoValidacao.RESPOSTAS_FORMULARIO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoValidacao.RESPOSTAS_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.RESPOSTAS_FORMULARIO, required = true, value = "Respostas de formulário associados a pendência")
    private List<RespostaFormularioPendenteDTO> respostasFormularioPendentes;
    
    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.TIPO_DOCUMENTO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoValidacao.TIPOS_DOCUMENTO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoValidacao.TIPOS_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.TIPOS_DOCUMENTO, required = false, value = "Tipo de Documentos encaminhados associados a pendência")
    private List<TipoDocumentoDTO> tiposDocumentoPendentes;

    public PendenciaVinculoProdutoDTO() {
        super();
        this.elementosConteudoPendentes = new ArrayList<>();
        this.respostasFormularioPendentes = new ArrayList<>();
    }

    public Integer getIdentificadorProduto() {
        return identificadorProduto;
    }

    public void setIdentificadorProduto(Integer identificadorProduto) {
        this.identificadorProduto = identificadorProduto;
    }

    public Integer getCodigoOperacaoProduto() {
        return codigoOperacaoProduto;
    }

    public void setCodigoOperacaoProduto(Integer codigoOperacaoProduto) {
        this.codigoOperacaoProduto = codigoOperacaoProduto;
    }

    public Integer getCodigoModalidadeProduto() {
        return codigoModalidadeProduto;
    }

    public void setCodigoModalidadeProduto(Integer codigoModalidadeProduto) {
        this.codigoModalidadeProduto = codigoModalidadeProduto;
    }

    public boolean isProdutoLocalizado() {
        return produtoLocalizado;
    }

    public void setProdutoLocalizado(boolean produtoLocalizado) {
        this.produtoLocalizado = produtoLocalizado;
    }

    public List<ElementoConteudoPendenteDTO> getElementosConteudoPendentes() {
        return elementosConteudoPendentes;
    }

    public void setElementosConteudoPendentes(List<ElementoConteudoPendenteDTO> elementosConteudoPendentes) {
        this.elementosConteudoPendentes = elementosConteudoPendentes;
    }
    
    public List<RespostaFormularioPendenteDTO> getRespostasFormularioPendentes() {
        return respostasFormularioPendentes;
    }
    
    public void setRespostasFormularioPendentes(List<RespostaFormularioPendenteDTO> respostasFormularioPendentes) {
        this.respostasFormularioPendentes = respostasFormularioPendentes;
    }
    
    public List<TipoDocumentoDTO> getTiposDocumentoPendentes() {
        return tiposDocumentoPendentes;
    }

    public void setTiposDocumentoPendentes(List<TipoDocumentoDTO> tiposDocumentoPendentes) {
        this.tiposDocumentoPendentes = tiposDocumentoPendentes;
    }

    //****************************

    public void addElementoConteudoPendente(ElementoConteudoPendenteDTO elementoConteudoPendenteDTO){
	this.elementosConteudoPendentes.add(elementoConteudoPendenteDTO);	
    }
    
    public void addRespostasFormularioPendentes(RespostaFormularioPendenteDTO respostaFormularioPendenteDTO){
        this.respostasFormularioPendentes.add(respostaFormularioPendenteDTO);       
    }

    public void addDocumentosPendente(TipoDocumentoDTO tipoDocumentoPendenteDTO){
        this.tiposDocumentoPendentes.add(tipoDocumentoPendenteDTO);       
    }
}
