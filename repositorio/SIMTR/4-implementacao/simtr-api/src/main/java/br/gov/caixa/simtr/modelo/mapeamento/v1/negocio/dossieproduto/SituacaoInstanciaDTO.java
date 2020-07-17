package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.gov.caixa.simtr.modelo.entidade.SituacaoInstanciaDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CGCUnidadeAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProduto.XML_ROOT_ELEMENT_SITUACAO_INSTANCIA)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProduto.API_MODEL_V1_SITUACAO_INSTANCIA,
        description = "Objeto utilizado para representar a situação definida a uma instancia de documento no retorno as consultas realizadas ao Dossiê de Produto sob a ótica Apoio ao Negocio."
)
public class SituacaoInstanciaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProduto.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.NOME, required = true, value = "Nome de identificação da situação da instancia de documento.")
    private String nome;

    @XmlElement(name = ConstantesNegocioDossieProduto.MOTIVO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.MOTIVO, required = false, value = "Nome de identificação do motivo relacionado a situação da instancia do documento.")
    private String motivo;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProduto.DATA_HORA_INCLUSAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.DATA_HORA_INCLUSAO, required = true, value = "Data/Hora de inclusão da situação no dossiê")
    private Calendar dataHoraInclusao;

    @XmlJavaTypeAdapter(value = CGCUnidadeAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProduto.UNIDADE)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.UNIDADE, required = true, value = "CGC da unidade que incluiu a situação no dossiê")
    private Integer unidade;

    public SituacaoInstanciaDTO() {
        super();
    }

    public SituacaoInstanciaDTO(SituacaoInstanciaDocumento situacaoInstanciaDocumento) {
        this();
        if (situacaoInstanciaDocumento != null) {
            if (situacaoInstanciaDocumento.getSituacaoDocumento() != null) {
                this.nome = situacaoInstanciaDocumento.getSituacaoDocumento().getNome();
            }
            this.dataHoraInclusao = situacaoInstanciaDocumento.getDataHoraInclusao();
            this.unidade = situacaoInstanciaDocumento.getUnidade();
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Calendar getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(Calendar dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public Integer getUnidade() {
        return unidade;
    }

    public void setUnidade(Integer unidade) {
        this.unidade = unidade;
    }
}
