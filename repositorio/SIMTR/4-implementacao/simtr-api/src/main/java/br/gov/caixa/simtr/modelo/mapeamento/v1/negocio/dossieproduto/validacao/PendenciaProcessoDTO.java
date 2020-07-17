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

@XmlRootElement(name = ConstantesNegocioDossieProdutoValidacao.XML_ROOT_ELEMENT_PENDENCIA_VINCULO_PROCESSO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
          value = ConstantesNegocioDossieProdutoValidacao.API_MODEL_V1_PENDENCIA_VINCULO_PROCESSO,
          description = "Objeto utilizado para representar um problema apresentado na validação realizada sob os vinculos documentais do processo em um dossiê de produto a ser incluido ou alterado.")
public class PendenciaProcessoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.IDENTIFICADOR_PROCESSO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.IDENTIFICADOR_PROCESSO, required = true, value = "Identificador do processo (gerador de dossiê ou fase) associado a pendência")
    private Integer identificadorProcesso;

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

    public PendenciaProcessoDTO() {
        super();
        this.elementosConteudoPendentes = new ArrayList<>();
        this.respostasFormularioPendentes = new ArrayList<>();
    }

    public Integer getIdentificadorProcesso() {
        return identificadorProcesso;
    }

    public void setIdentificadorProcesso(Integer identificadorProcesso) {
        this.identificadorProcesso = identificadorProcesso;
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

    // ****************************
    public void addElementoConteudoPendente(ElementoConteudoPendenteDTO elementoConteudoPendenteDTO) {
        this.elementosConteudoPendentes.add(elementoConteudoPendenteDTO);
    }

    public void addRespostaFormularioPendente(RespostaFormularioPendenteDTO respostaFormularioPendenteDTO) {
        this.respostasFormularioPendentes.add(respostaFormularioPendenteDTO);
    }

    public void addDocumentosPendente(TipoDocumentoDTO tipoDocumentoPendenteDTO) {
        this.tiposDocumentoPendentes.add(tipoDocumentoPendenteDTO);
    }
}
