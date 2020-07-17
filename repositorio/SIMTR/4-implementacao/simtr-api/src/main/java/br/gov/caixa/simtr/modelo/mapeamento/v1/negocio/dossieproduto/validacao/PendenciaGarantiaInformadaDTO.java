package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao;

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
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoValidacao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoValidacao.XML_ROOT_ELEMENT_PENDENCIA_GARANTIA_INFORMADA)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoValidacao.API_MODEL_V1_PENDENCIA_GARANTIA_INFORMADA,
        description = "Objeto utilizado para representar um problema apresentado na validação realizada sob os vinculos de garantias em um dossiê de produto a ser incluido ou alterado."
)
public class PendenciaGarantiaInformadaDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.IDENTIFICADOR_GARANTIA)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.IDENTIFICADOR_GARANTIA, required = true, value = "Identificador único da garantia escolhida")
    private Integer identificadorGarantia;
    
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.DESCRICAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.DESCRICAO, required = true, value = "Descrição da garantia escolhida")
    private String descricao;
    
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

    public PendenciaGarantiaInformadaDTO() {
        super();
        this.respostasFormularioPendentes = new ArrayList<>();
    }

    public Integer getIdentificadorGarantia() {
        return identificadorGarantia;
    }

    public void setIdentificadorGarantia(Integer identificadorGarantia) {
        this.identificadorGarantia = identificadorGarantia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public void addRespostaFormularioPendente(RespostaFormularioPendenteDTO respostaFormularioPendenteDTO) {
        this.respostasFormularioPendentes.add(respostaFormularioPendenteDTO);
    }

    public void addDocumentosPendente(TipoDocumentoDTO tipoDocumentoPendenteDTO) {
        this.tiposDocumentoPendentes.add(tipoDocumentoPendenteDTO);
    }    
}
