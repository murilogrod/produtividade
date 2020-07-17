package br.gov.caixa.simtr.modelo.mapeamento.v1.pae.processo;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.gov.caixa.simtr.modelo.entidade.ProcessoAdministrativo;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesPAE;

@XmlRootElement(name = ConstantesPAE.XML_ROOT_ELEMENT_PROCESSO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesPAE.VISAO_PROCESSO_API_MODEL_PROCESSO_ADMINISTRATIVO_MANUTENCAO)
public class ProcessoAdministrativoManutencaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @XmlElement(name = ConstantesPAE.NUMERO_PREGAO)
    @ApiModelProperty(name = ConstantesPAE.NUMERO_PREGAO, required = false, value = "Valor que representa o numero do pregão originado pelo processo.")
    private Integer numeroPregao;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @XmlElement(name = ConstantesPAE.UNIDADE_CONTRATACAO)
    @ApiModelProperty(name = ConstantesPAE.UNIDADE_CONTRATACAO, required = false, value = "Valor que representa a unidade CAIXA responsável pela execução do pregão.")
    private Integer unidadeContratacao;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @XmlElement(name = ConstantesPAE.UNIDADE_DEMANDANTE)
    @ApiModelProperty(name = ConstantesPAE.UNIDADE_DEMANDANTE, required = true, value = "Valor que representa a unidade CAIXA responsável solicitação do produto/serviço.")
    private Integer unidadeDemandante;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @XmlElement(name = ConstantesPAE.ANO_PREGAO)
    @ApiModelProperty(name = ConstantesPAE.ANO_PREGAO, required = false, value = "Valor que representa o ano de execução do pregão.")
    private Integer anoPregao;

    @XmlElement(name = ConstantesPAE.OBJETO_CONTRATACAO)
    @ApiModelProperty(name = ConstantesPAE.OBJETO_CONTRATACAO, required = false, value = "Descrição do objeto de contratação para o pregão em referência.")
    private String objetoContratacao;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesPAE.DATA_HORA_FINALIZACAO)
    @ApiModelProperty(name = ConstantesPAE.DATA_HORA_FINALIZACAO, required = false, value = "Data/Hora de finalização do contrato. Caso essa informação seja preenchida, este processo não poderá mais ser alterado.", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraFinalizacao;

    @XmlElement(name = ConstantesPAE.MATRICULA_FINALIZACAO)
    @ApiModelProperty(name = ConstantesPAE.MATRICULA_FINALIZACAO, required = false, value = "Matricula do empregado responsavel pela realização do finalização do processo.", example = "c999999")
    private String matriculaFinalizacao;

    @XmlElement(name = ConstantesPAE.PROTOCOLO_SICLG)
    @ApiModelProperty(name = ConstantesPAE.PROTOCOLO_SICLG, required = true, value = "Codigo de identificação da demanda inicial junto ao SICLG. Dado informativo, não há integração entre os sistemas.")
    private String protocoloSICLG;

    public ProcessoAdministrativoManutencaoDTO() {
        super();
    }

    public ProcessoAdministrativoManutencaoDTO(ProcessoAdministrativo processo) {
        this();
        this.numeroPregao = processo.getNumeroPregao();
        this.unidadeContratacao = processo.getUnidadeContratacao();
        this.unidadeDemandante = processo.getUnidadeDemandante();
        this.anoPregao = processo.getAnoPregao();
        this.objetoContratacao = processo.getObjetoContratacao();
        this.dataHoraFinalizacao = processo.getDataHoraFinalizacao() != null ? processo.getDataHoraFinalizacao()
                : Calendar.getInstance();
        this.matriculaFinalizacao = processo.getMatriculaFinalizacao();
    }

    public Integer getNumeroPregao() {
        return numeroPregao;
    }

    public void setNumeroPregao(Integer numeroContratacao) {
        this.numeroPregao = numeroContratacao;
    }

    public Integer getUnidadeContratacao() {
        return unidadeContratacao;
    }

    public void setUnidadeContratacao(Integer unidadeContratacao) {
        this.unidadeContratacao = unidadeContratacao;
    }

    public Integer getUnidadeDemandante() {
        return unidadeDemandante;
    }

    public void setUnidadeDemandante(Integer unidadeDemandante) {
        this.unidadeDemandante = unidadeDemandante;
    }

    public Integer getAnoPregao() {
        return anoPregao;
    }

    public void setAnoPregao(Integer anoPregao) {
        this.anoPregao = anoPregao;
    }

    public String getObjetoContratacao() {
        return objetoContratacao;
    }

    public void setObjetoContratacao(String objetoContratacao) {
        this.objetoContratacao = objetoContratacao;
    }

    public Calendar getDataHoraFinalizacao() {
        return dataHoraFinalizacao;
    }

    public void setDataHoraFinalizacao(Calendar dataHoraFinalizacao) {
        this.dataHoraFinalizacao = dataHoraFinalizacao;
    }

    public String getMatriculaFinalizacao() {
        return matriculaFinalizacao;
    }

    public void setMatriculaFinalizacao(String matriculaFinalizacao) {
        this.matriculaFinalizacao = matriculaFinalizacao;
    }

    public String getProtocoloSICLG() {
        return protocoloSICLG;
    }

    public void setProtocoloSICLG(String protocoloSICLG) {
        this.protocoloSICLG = protocoloSICLG;
    }

    public ProcessoAdministrativo prototype() {
        ProcessoAdministrativo processoAdministrativo = new ProcessoAdministrativo();
        processoAdministrativo.setNumeroPregao(this.numeroPregao);
        processoAdministrativo.setUnidadeContratacao(this.unidadeContratacao);
        processoAdministrativo.setUnidadeDemandante(this.unidadeDemandante);
        processoAdministrativo.setAnoPregao(this.anoPregao);
        processoAdministrativo.setObjetoContratacao(this.objetoContratacao);
        processoAdministrativo.setDataHoraFinalizacao(this.dataHoraFinalizacao);
        processoAdministrativo.setMatriculaFinalizacao(this.matriculaFinalizacao);
        processoAdministrativo.setProtocoloSICLG(this.protocoloSICLG);

        return processoAdministrativo;
    }
}
