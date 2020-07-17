package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoManutencao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoManutencao.XML_ROOT_ELEMENT_GARANTIA_INFORMADA)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoManutencao.API_MODEL_V1_GARANTIA_INFORMADA,
        description = "Objeto utilizado para representar a garantia ofertada na contratação de um dossiê de produto sob a ótica do apoio ao negócio."
)
public class GarantiaInformadaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.IDENTIFICADOR_GARANTIA)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.IDENTIFICADOR_GARANTIA, required = true, value = "Identificador único da garantia escolhida")
    private Integer identificadorGarantia;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.IDENTIFICADOR_PRODUTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.IDENTIFICADOR_PRODUTO, required = false, value = "Identificador do produto selecionado para vinculação com o dossiê de produto. Caso não seja informado, é possível informar em conjunto os atributos <codigo_operacao> e <codigo_modalidade>. Caso a garantia seja geral a contratação, não informar este campo")
    private Integer identificadorProduto;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.VALOR_GARANTIA)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.VALOR_GARANTIA, required = true, value = "Valor da garantia ofertada")
    private BigDecimal valor;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.CLIENTES_AVALISTAS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.CLIENTES_AVALISTAS, required = false, value = "Para os casos de garantia fidejussoria, é necessario indicar o(s) cliente(s) que esta(m) avalizando a operação")
    private List<Long> identificadoresClientesAvalistas;

    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.DOCUMENTOS_UTILIZADOS)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.DOCUMENTO_UTILIZADO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.DOCUMENTOS_UTILIZADOS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.DOCUMENTOS_UTILIZADOS, required = false, value = "Lista dos identificadores de documentos já existentes na base de dados indicados para comprovação da garantia indicada, aplicando o conceito de reuso dos documentos")
    private List<Long> documentosUtilizados;

    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.DOCUMENTOS_NOVOS)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.DOCUMENTO_NOVO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.DOCUMENTOS_NOVOS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.DOCUMENTOS_NOVOS, required = false, value = "Lista dos documentos encaminhados como comprovação da garantia ofertada")
    private List<DocumentoDTO> documentosNovosDTO;
    
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.RESPOSTAS_FORMULARIO)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.RESPOSTA_FORMULARIO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.RESPOSTAS_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.RESPOSTAS_FORMULARIO, required = false, value = "Lista de respostas de formulario submetidas ao dossiê de produto associadas a garantia informada")
    private List<RespostaFormularioDTO> respostasFormularioDTO;

    public GarantiaInformadaDTO() {
        super();
        this.identificadoresClientesAvalistas = new ArrayList<>();
        this.documentosUtilizados = new ArrayList<>();
        this.documentosNovosDTO = new ArrayList<>();
        this.respostasFormularioDTO = new ArrayList<>();
    }

    public Integer getIdentificadorGarantia() {
        return identificadorGarantia;
    }

    public void setIdentificadorGarantia(Integer identificadorGarantia) {
        this.identificadorGarantia = identificadorGarantia;
    }

    public Integer getIdentificadorProduto() {
        return identificadorProduto;
    }

    public void setIdentificadorProduto(Integer identificadorProduto) {
        this.identificadorProduto = identificadorProduto;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public List<Long> getIdentificadoresClientesAvalistas() {
        return identificadoresClientesAvalistas;
    }

    public void setIdentificadoresClientesAvalistas(List<Long> identificadoresClientesAvalistas) {
        this.identificadoresClientesAvalistas = identificadoresClientesAvalistas;
    }

    public List<Long> getDocumentosUtilizados() {
        return documentosUtilizados;
    }

    public void setDocumentosUtilizados(List<Long> documentosUtilizados) {
        this.documentosUtilizados = documentosUtilizados;
    }

    public List<DocumentoDTO> getDocumentosNovosDTO() {
        return documentosNovosDTO;
    }

    public void setDocumentosNovosDTO(List<DocumentoDTO> documentosNovosDTO) {
        this.documentosNovosDTO = documentosNovosDTO;
    }

    public List<RespostaFormularioDTO> getRespostasFormularioDTO() {
        return respostasFormularioDTO;
    }

    public void setRespostasFormularioDTO(List<RespostaFormularioDTO> respostasFormularioDTO) {
        this.respostasFormularioDTO = respostasFormularioDTO;
    }
}
