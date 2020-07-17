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

@XmlRootElement(name = ConstantesNegocioDossieProdutoValidacao.XML_ROOT_ELEMENT_PENDENCIA_VINCULO_PESSOA)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoValidacao.API_MODEL_V1_PENDENCIA_VINCULO_PESSOA,
        description = "Objeto utilizado para representar um problema apresentado na validação realizada sob os vinculos de pessoas em um dossiê de produto a ser incluido ou alterado."
)
public class PendenciaVinculoPessoaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.TIPO_RELACIONAMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.TIPO_RELACIONAMENTO, required = true, value = "Tipo de relacionamento associado a pendência")
    private TipoRelacionamentoDTO tipoRelacionamentoDTO;
   
    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.IDENTIFICADOR_DOSSIE_CLIENTE)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.IDENTIFICADOR_DOSSIE_CLIENTE, required = true, value = "Idenificador do dossiê cliente associado a pendência")
    private Long identificadorDossieCliente;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.FUNCAO_DOCUMENTAL)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoValidacao.FUNCOES_DOCUMENTAIS)
    @JsonProperty(value = ConstantesNegocioDossieProdutoValidacao.FUNCOES_DOCUMENTAIS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.FUNCOES_DOCUMENTAIS, required = false, value = "Funções documentais associadas a pendência para os casos em que a parametrização do registro de documento permite a associação de um tipo de documento dentre um conjunto indicado")
    private List<FuncaoDocumentalDTO> funcoesDocumentaisPendentes;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.TIPO_DOCUMENTO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoValidacao.TIPOS_DOCUMENTO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoValidacao.TIPOS_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.TIPOS_DOCUMENTO, required = false, value = "Nome dos tipos de documentos associados a pendência para os casos em que a parametrização do registro de documento determina a associação de um tipo de documento especifico")
    private List<TipoDocumentoDTO> tiposDocumentoPendentes;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.DOSSIE_CLIENTE_LOCALIZADO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.DOSSIE_CLIENTE_LOCALIZADO, required = true, value = "Indica se o dossiê cliente informado foi identificado")
    private boolean recursoLocalizado;
    
    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.RESPOSTA_FORMULARIO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoValidacao.RESPOSTAS_FORMULARIO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoValidacao.RESPOSTAS_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.RESPOSTAS_FORMULARIO, required = true, value = "Respostas de formulário associados a pendência")
    private List<RespostaFormularioPendenteDTO> respostasFormularioPendentes;

    public PendenciaVinculoPessoaDTO() {
        super();
        this.funcoesDocumentaisPendentes = new ArrayList<>();
        this.tiposDocumentoPendentes = new ArrayList<>();
        this.respostasFormularioPendentes = new ArrayList<>();
    }

    public TipoRelacionamentoDTO getTipoRelacionamentoDTO() {
        return tipoRelacionamentoDTO;
    }

    public void setTipoRelacionamentoDTO(TipoRelacionamentoDTO tipoRelacionamentoDTO) {
        this.tipoRelacionamentoDTO = tipoRelacionamentoDTO;
    }

    public Long getIdentificadorDossieCliente() {
        return identificadorDossieCliente;
    }

    public void setIdentificadorDossieCliente(Long identificadorDossieCliente) {
        this.identificadorDossieCliente = identificadorDossieCliente;
    }

    public List<FuncaoDocumentalDTO> getFuncoesDocumentaisPendentes() {
        return funcoesDocumentaisPendentes;
    }

    public void setFuncoesDocumentaisPendentes(List<FuncaoDocumentalDTO> funcoesDocumentaisPendentes) {
        this.funcoesDocumentaisPendentes = funcoesDocumentaisPendentes;
    }

    public List<TipoDocumentoDTO> getTiposDocumentoPendentes() {
        return tiposDocumentoPendentes;
    }

    public List<RespostaFormularioPendenteDTO> getRespostasFormularioPendentes() {
        return respostasFormularioPendentes;
    }

    public void setTiposDocumentoPendentes(List<TipoDocumentoDTO> tiposDocumentoPendente) {
        this.tiposDocumentoPendentes = tiposDocumentoPendente;
    }

    public boolean isRecursoLocalizado() {
        return recursoLocalizado;
    }

    public void setRecursoLocalizado(boolean recursoLocalizado) {
        this.recursoLocalizado = recursoLocalizado;
    }
    
    //********************************
    
    public void addTipoDocumentoPendente(TipoDocumentoDTO tipoDocumentoDTO){
	this.tiposDocumentoPendentes.add(tipoDocumentoDTO);
    }
    
    public void addFuncaoDocumentalPendente(FuncaoDocumentalDTO funcaoDocumentalDTO){
	this.funcoesDocumentaisPendentes.add(funcaoDocumentalDTO);
    }
    
    public void addRespostasFormularioPendentes(RespostaFormularioPendenteDTO respostaFormularioPendenteDTO) {
        this.respostasFormularioPendentes.add(respostaFormularioPendenteDTO);
    }
}
