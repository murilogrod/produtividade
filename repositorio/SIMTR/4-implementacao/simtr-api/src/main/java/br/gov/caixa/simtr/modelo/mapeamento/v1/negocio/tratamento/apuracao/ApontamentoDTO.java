package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tratamento.apuracao;

import br.gov.caixa.simtr.modelo.entidade.Apontamento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTratamento;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioTratamento.XML_ROOT_ELEMENT_APONTAMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTratamento.API_MODEL_V1_APURACAO__APONTAMENTO,
        description = "Objeto utilizado para representar apontamento vinculado a um checklist no retorno da apuracao da execução do tratamentosob a ótica Apoio ao Negocio."
)
public class ApontamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioTratamento.IDENTIFICADOR_APONTAMENTO)
    @ApiModelProperty(name = ConstantesNegocioTratamento.IDENTIFICADOR_APONTAMENTO, required = true, value = "Codigo de identificação do checklist encaminhado.")
    private Long identificadorApontamento;

    @XmlElement(name = ConstantesNegocioTratamento.NOME)
    @ApiModelProperty(name = ConstantesNegocioTratamento.NOME, required = true, value = "Nome do checklist definido para identificação.")
    private String nome;

    public ApontamentoDTO() {
        super();
    }

    public ApontamentoDTO(Apontamento apontamento) {
        this();
        this.identificadorApontamento = apontamento.getId();
        this.nome = apontamento.getNome();
    }

    public Long getIdentificadorApontamento() {
        return identificadorApontamento;
    }

    public void setIdentificadorApontamento(Long identificadorApontamento) {
        this.identificadorApontamento = identificadorApontamento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
