package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dashboard;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.gov.caixa.simtr.modelo.entidade.SituacaoDossie;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CGCUnidadeAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDashboard;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDashboard.XML_ROOT_ELEMENT_SITUACAO_DOSSIE)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDashboard.API_MODEL_V1_SITUACAO_DOSSIE,
        description = "Objeto utilizado para representar a situação definida a um dossiê no retorno as consultas realizadas sob a ótica Apoio ao Negocio para um Dashboard."
)
public class SituacaoDossieDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDashboard.NOME)
    @ApiModelProperty(name = ConstantesNegocioDashboard.NOME, required = true, value = "Nome de identificação da situação do dossiê")
    private String nome;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDashboard.DATA_HORA_INCLUSAO)
    @ApiModelProperty(name = ConstantesNegocioDashboard.DATA_HORA_INCLUSAO, required = true, value = "Data/Hora de inclusão da situação no dossiê", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraInclusao;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDashboard.DATA_HORA_SAIDA)
    @ApiModelProperty(name = ConstantesNegocioDashboard.DATA_HORA_SAIDA, required = false, value = "Data/Hora de saida da situação no dossiê", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraSaida;

    @XmlJavaTypeAdapter(value = CGCUnidadeAdapter.class)
    @XmlElement(name = ConstantesNegocioDashboard.UNIDADE)
    @ApiModelProperty(name = ConstantesNegocioDashboard.UNIDADE, required = true, value = "CGC da unidade que incluiu a situação no dossiê")
    private Integer unidade;

    @XmlElement(name = ConstantesNegocioDashboard.OBSERVACAO)
    @ApiModelProperty(name = ConstantesNegocioDashboard.OBSERVACAO, required = false, value = "Observação livre relacionada a situação")
    private String observacao;

    public SituacaoDossieDTO() {
        super();
    }

    public SituacaoDossieDTO(SituacaoDossie situacaoDossie) {
        this();
        if (situacaoDossie != null) {
            if (situacaoDossie.getTipoSituacaoDossie() != null) {
                this.nome = situacaoDossie.getTipoSituacaoDossie().getNome();
            }
            this.dataHoraInclusao = situacaoDossie.getDataHoraInclusao();
            this.dataHoraSaida = situacaoDossie.getDataHoraSaida();
            this.unidade = situacaoDossie.getUnidade();
            this.observacao = situacaoDossie.getObservacao();
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Calendar getDataHoraInclusao() {
        return dataHoraInclusao;
    }

    public void setDataHoraInclusao(Calendar dataHoraInclusao) {
        this.dataHoraInclusao = dataHoraInclusao;
    }

    public Calendar getDataHoraSaida() {
        return dataHoraSaida;
    }

    public void setDataHoraSaida(Calendar dataHoraSaida) {
        this.dataHoraSaida = dataHoraSaida;
    }

    public Integer getUnidade() {
        return unidade;
    }

    public void setUnidade(Integer unidade) {
        this.unidade = unidade;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
