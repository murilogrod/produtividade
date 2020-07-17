package br.gov.caixa.simtr.modelo.mapeamento.v1.pae;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesPAE;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.UnidadeAutorizada;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesPAE.XML_ROOT_ELEMENT_UNIDADE_AUTORIZADA)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesPAE.API_MODEL_UNIDADE_AUTORIZADA,
        description = "Objeto utilizado para representar a Unidade Autorizada no retorno as consultas realizadas sob a otica geral do PAE."
)
public class UnidadeAutorizadaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesPAE.CGC_UNIDADE)
    @ApiModelProperty(name = ConstantesPAE.CGC_UNIDADE, required = true, value = "Valor que representa o CGC da unidade CAIXA vinculada a autorização.")
    private Integer unidade;

    // *********************************************
    @JsonInclude(value = Include.NON_NULL)
    @JsonProperty(value = ConstantesPAE.TRATAMENTOS_AUTORIZADOS)
    @XmlElement(name = ConstantesPAE.TRATAMENTO_AUTORIZADO)
    @XmlElementWrapper(name = ConstantesPAE.TRATAMENTOS_AUTORIZADOS)
    @ApiModelProperty(name = ConstantesPAE.TRATAMENTOS_AUTORIZADOS, required = true, value = "Lista de tratamentos autorizados a unidade CAIXA vinculada a autorização.")
    private List<String> tiposTratamentoAutorizados;

    public UnidadeAutorizadaDTO() {
        super();
        this.tiposTratamentoAutorizados = new ArrayList<>();
    }

    public UnidadeAutorizadaDTO(UnidadeAutorizada unidadeAutorizada) {
        this();
        this.unidade = unidadeAutorizada.getUnidade();
//        TODO Equalizacao_042018 - Contruir Encode/Decode TratamentosAutorizados
//		this.tiposTratamentoAutorizados = unidadeAutorizada.getTiposTratamentoAutorizados();
    }

    public Integer getUnidade() {
        return unidade;
    }

    public void setUnidade(Integer unidade) {
        this.unidade = unidade;
    }

    public List<String> getTiposTratamentoAutorizados() {
        return tiposTratamentoAutorizados;
    }

    public void setTiposTratamentoAutorizados(List<String> tiposTratamentoAutorizados) {
        this.tiposTratamentoAutorizados = tiposTratamentoAutorizados;
    }

}
