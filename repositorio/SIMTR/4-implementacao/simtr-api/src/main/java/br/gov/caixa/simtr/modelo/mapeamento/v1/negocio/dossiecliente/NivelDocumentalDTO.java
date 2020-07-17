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

import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Calendar;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = ConstantesNegocioDossieCliente.XML_ROOT_ELEMENT_NIVEL_DOCUMENTAL)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieCliente.API_MODEL_V1_NIVEL_DOCUMENTAL,
        description = "Objeto utilizado para representar a apuração do nivel documental do cliente indicando os produtos aptos do ponto de vista documental no retorno as consultas realizadas a partir do Dossiê do Cliente"
)
public class NivelDocumentalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieCliente.DATA_HORA_APURACAO_NIVEL)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.DATA_HORA_APURACAO_NIVEL, required = false, value = "Data e hora de apuração do nivel documental", example = "dd/MM/yyyy hh:mm:ss")
    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    private Calendar dataApuracaoNivel;

    // *****************************************
    @XmlElement(name = ConstantesNegocioDossieCliente.PRODUTO)
    @XmlElementWrapper(name = ConstantesNegocioDossieCliente.PRODUTOS_HABILITADOS)
    @JsonProperty(value = ConstantesNegocioDossieCliente.PRODUTOS_HABILITADOS)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.PRODUTOS_HABILITADOS, required = false, value = "Lista de produtos habilitados para o cliente sob as regras do dossiê digital")
    private List<ProdutoHabilitadoDTO> produtosHabilitadosDTO;

    public NivelDocumentalDTO() {
        super();
        this.produtosHabilitadosDTO = new ArrayList<>();
    }

    public NivelDocumentalDTO(DossieCliente dossieCliente) {
        this();
        this.dataApuracaoNivel = dossieCliente.getDataHoraApuracaoNivel();

        if (dossieCliente.getComposicoesDocumentais() != null) {
            dossieCliente.getComposicoesDocumentais().forEach(composicaoDocumental -> {
                if (composicaoDocumental.getProdutos() != null) {
                    composicaoDocumental.getProdutos().forEach(produto -> this.produtosHabilitadosDTO.add(new ProdutoHabilitadoDTO(produto)));
                }
            });
        }
    }

    public Calendar getDataApuracaoNivel() {
        return dataApuracaoNivel;
    }

    public void setDataApuracaoNivel(Calendar dataApuracaoNivel) {
        this.dataApuracaoNivel = dataApuracaoNivel;
    }

    public List<ProdutoHabilitadoDTO> getProdutosHabilitadosDTO() {
        return produtosHabilitadosDTO;
    }

    public void setProdutosHabilitadosDTO(List<ProdutoHabilitadoDTO> produtosHabilitadosDTO) {
        this.produtosHabilitadosDTO = produtosHabilitadosDTO;
    }
}
