package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.manutencao;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieClienteManutencao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieClienteManutencao.XML_ROOT_ELEMENT_ATRIBUTO_MOTIVO_REPROVACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
@ApiModel(
          value = ConstantesNegocioDossieClienteManutencao.API_MODEL_V1_ATRIBUTO_MOTIVO_REPROVACAO,
          description = "Objeto utilizado para representar um atributo com o valor extraido de um documento ou como dados de ajuste para atualização da informação perante o documento")
public class MotivoReprovacaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.MORIVOS_REPROVACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.MORIVOS_REPROVACAO, required = true, value = "Nome do atributo do documento")
    private String motivoReprovacao;

	public String getMotivoReprovacao() {
		return motivoReprovacao;
	}

	public void setMotivoReprovacao(String motivoReprovacao) {
		this.motivoReprovacao = motivoReprovacao;
	}

}