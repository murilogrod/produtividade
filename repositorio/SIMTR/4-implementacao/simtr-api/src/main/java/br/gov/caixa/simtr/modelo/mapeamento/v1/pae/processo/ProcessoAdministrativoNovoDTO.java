package br.gov.caixa.simtr.modelo.mapeamento.v1.pae.processo;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.gov.caixa.simtr.modelo.entidade.ProcessoAdministrativo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesPAE;

@XmlRootElement(name = ConstantesPAE.XML_ROOT_ELEMENT_PROCESSO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesPAE.VISAO_PROCESSO_API_MODEL_PROCESSO_ADMINISTRATIVO_NOVO,
        description = "Objeto utilizado para realizar a inclusão de um Processo Administrativo inicial limitando as possibilidades de definição dos atributos."
)
public class ProcessoAdministrativoNovoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesPAE.NUMERO_PROCESSO)
    @ApiModelProperty(name = ConstantesPAE.NUMERO_PROCESSO, required = true, value = "Valor que representa o numero do Processo Administrativo. Utilizado para localização.")
    private Integer numeroProcesso;

    @XmlElement(name = ConstantesPAE.ANO_PROCESSO)
    @ApiModelProperty(name = ConstantesPAE.ANO_PROCESSO, required = true, value = "Valor que representa o ano de criação do Processo Administrativo. Utilizado para localização.")
    private Integer anoProcesso;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @XmlElement(name = ConstantesPAE.NUMERO_PREGAO)
    @ApiModelProperty(name = ConstantesPAE.NUMERO_PREGAO, required = false, value = "Valor que representa o numero do pregão originado pelo Processo Administrativo.")
    private Integer numeroPregao;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    @XmlElement(name = ConstantesPAE.UNIDADE_CONTRATACAO)
    @ApiModelProperty(name = ConstantesPAE.UNIDADE_CONTRATACAO, required = true, value = "Valor que representa a unidade CAIXA responsável pela execução do pregão.")
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
    @ApiModelProperty(name = ConstantesPAE.OBJETO_CONTRATACAO, required = true, value = "Descrição do objeto de contratação para o pregão em referência.")
    private String objetoContratacao;

    @XmlElement(name = ConstantesPAE.PROTOCOLO_SICLG)
    @ApiModelProperty(name = ConstantesPAE.PROTOCOLO_SICLG, required = true, value = "Codigo de identificação da demanda inicial junto ao SICLG. Dado informativo, não há integração entre os sistemas.")
    private String protocoloSICLG;

    public ProcessoAdministrativoNovoDTO() {
        super();
    }

    public Integer getNumeroProcesso() {
        return numeroProcesso;
    }

    public void setNumeroProcesso(Integer numeroProcesso) {
        this.numeroProcesso = numeroProcesso;
    }

    public Integer getAnoProcesso() {
        return anoProcesso;
    }

    public void setAnoProcesso(Integer anoProcesso) {
        this.anoProcesso = anoProcesso;
    }

    public Integer getNumeroPregao() {
        return numeroPregao;
    }

    public void setNumeroPregao(Integer numeroPregao) {
        this.numeroPregao = numeroPregao;
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

    public String getProtocoloSICLG() {
        return protocoloSICLG;
    }

    public void setProtocoloSICLG(String protocoloSICLG) {
        this.protocoloSICLG = protocoloSICLG;
    }

    public ProcessoAdministrativo prototype() {
        ProcessoAdministrativo processoAdministrativo = new ProcessoAdministrativo();
        processoAdministrativo.setNumeroProcesso(this.numeroProcesso);
        processoAdministrativo.setAnoProcesso(this.anoProcesso);
        processoAdministrativo.setNumeroPregao(this.numeroPregao);
        processoAdministrativo.setUnidadeContratacao(this.unidadeContratacao);
        processoAdministrativo.setUnidadeDemandante(this.unidadeDemandante);
        processoAdministrativo.setAnoPregao(this.anoPregao);
        processoAdministrativo.setObjetoContratacao(this.objetoContratacao);
        processoAdministrativo.setProtocoloSICLG(this.protocoloSICLG);
        processoAdministrativo.setDataHoraInclusao(Calendar.getInstance());

        return processoAdministrativo;
    }
}
