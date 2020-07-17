package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioProcesso;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioProcesso.XML_ROOT_ELEMENT_TIPO_RELACIONAMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
          value = ConstantesNegocioProcesso.API_MODEL_V1_TIPO_RELACIONAMENTO,
          description = "Objeto utilizado para o tipo de relacionamento possível de ser definido na vinculação de um dossiê de cliente ao dossiê de produto e os parametros que definem o comportamento esperado nessa inclusão.")
public class TipoRelacionamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ProdutoDTO.class.getName());

    @XmlElement(name = ConstantesNegocioProcesso.ID)
    @ApiModelProperty(name = ConstantesNegocioProcesso.ID, required = true, value = "Identificador único do tipo de relacionamento definido para o vinculo.")
    private Integer id;

    @XmlElement(name = ConstantesNegocioProcesso.NOME)
    @ApiModelProperty(name = ConstantesNegocioProcesso.NOME, required = true, value = "Identifica o tipo de relacionamento definido para o vinculo.")
    private String nomeTipoRelacionamento;

    @XmlElement(name = ConstantesNegocioProcesso.PRINCIPAL, required = true)
    @ApiModelProperty(name = ConstantesNegocioProcesso.PRINCIPAL, required = true, value = "Indica se o tipo de relacionamento tem carater de atribuição imediata ao cliente contextualizado.")
    private boolean definicaoPrincipal;

    @XmlElement(name = ConstantesNegocioProcesso.INDICA_RELACIONADO, required = true)
    @ApiModelProperty(name = ConstantesNegocioProcesso.INDICA_RELACIONADO, required = true, value = "Indica se o tipo de relacionamento exige a identificação de um dossiê de cliente relacionado.")
    private boolean definicaoRelacionado;

    @XmlElement(name = ConstantesNegocioProcesso.INDICA_SEQUENCIA, required = true)
    @ApiModelProperty(name = ConstantesNegocioProcesso.INDICA_SEQUENCIA, required = true,
                      value = "Indica se o tipo de relacionamento exige a identificação de sequenciamento do mesmo tipo de vinculo.")
    private boolean definicaoSequencia;

    @XmlElement(name = ConstantesNegocioProcesso.INDICA_RECEITA_PF, required = true)
    @ApiModelProperty(name = ConstantesNegocioProcesso.INDICA_RECEITA_PF, required = true,
                      value = "Indica se o tipo de relacionamento será criado para os socios pessoas fisicas indicados após a consulta de quadro societário junto a Receita.")
    private boolean indica_receita_pf;

    @XmlElement(name = ConstantesNegocioProcesso.INDICA_RECEITA_PJ, required = true)
    @ApiModelProperty(name = ConstantesNegocioProcesso.INDICA_RECEITA_PJ, required = true,
                      value = "Indica se o tipo de relacionamento será criado para os socios pessoas fisicas indicados após a consulta de quadro societário junto a Receita.")
    private boolean indica_receita_pj;

    @XmlElement(name = ConstantesNegocioProcesso.TIPO_PESSOA, required = true)
    @ApiModelProperty(name = ConstantesNegocioProcesso.TIPO_PESSOA, required = true, value = "Indica o tipo de pessoa passível de associação com o vinculo em referência.")
    private TipoPessoaEnum tipoPessoaEnum;

    // ***************************
    @XmlElementWrapper(name = ConstantesNegocioProcesso.CAMPOS_FORMULARIO)
    @XmlElement(name = ConstantesNegocioProcesso.CAMPO_FORMULARIO)
    @JsonProperty(value = ConstantesNegocioProcesso.CAMPOS_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.CAMPOS_FORMULARIO, required = false, value = "Lista de objetos que representam os campos de formulario utilizados na fase do processo.")
    private List<CampoFormularioDTO> camposFormularioDTO;

    public TipoRelacionamentoDTO() {
        super();
        this.camposFormularioDTO = new ArrayList<>();
    }

    public TipoRelacionamentoDTO(TipoRelacionamento tipoRelacionamento, Processo processo) {
        this();
        if (tipoRelacionamento != null) {
            this.id = tipoRelacionamento.getId();
            this.nomeTipoRelacionamento = tipoRelacionamento.getNome();
            this.definicaoPrincipal = tipoRelacionamento.getIndicadorPrincipal();
            this.definicaoRelacionado = tipoRelacionamento.getIndicadorRelacionado();
            this.definicaoSequencia = tipoRelacionamento.getIndicadorSequencia();
            this.indica_receita_pf = tipoRelacionamento.getIndicadorReceitaPessoaFisica();
            this.indica_receita_pj = tipoRelacionamento.getIndicadorReceitaPessoaJuridica();
            this.tipoPessoaEnum = tipoRelacionamento.getTipoPessoaEnum();

            try {
                if (tipoRelacionamento.getCamposFormulario() != null) {
                    tipoRelacionamento.getCamposFormulario().stream()
                                      .filter(cf -> processo.equals(cf.getProcesso()))
                                      .sorted(Comparator.comparing(CampoFormulario::getOrdemApresentacao))
                                      .forEachOrdered(campoFormulario -> this.camposFormularioDTO.add(new CampoFormularioDTO(campoFormulario)));
                }
            } catch (RuntimeException re) {
                // Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.camposFormularioDTO = null;
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeTipoRelacionamento() {
        return nomeTipoRelacionamento;
    }

    public void setNomeTipoRelacionamento(String nomeTipoRelacionamento) {
        this.nomeTipoRelacionamento = nomeTipoRelacionamento;
    }

    public boolean isDefinicaoPrincipal() {
        return definicaoPrincipal;
    }

    public void setDefinicaoPrincipal(boolean definicaoPrincipal) {
        this.definicaoPrincipal = definicaoPrincipal;
    }

    public boolean isDefinicaoRelacionado() {
        return definicaoRelacionado;
    }

    public void setDefinicaoRelacionado(boolean definicaoRelacionado) {
        this.definicaoRelacionado = definicaoRelacionado;
    }

    public boolean isDefinicaoSequencia() {
        return definicaoSequencia;
    }

    public void setDefinicaoSequencia(boolean definicaoSequencia) {
        this.definicaoSequencia = definicaoSequencia;
    }

    public boolean isIndica_receita_pf() {
        return indica_receita_pf;
    }

    public void setIndica_receita_pf(boolean indica_receita_pf) {
        this.indica_receita_pf = indica_receita_pf;
    }

    public boolean isIndica_receita_pj() {
        return indica_receita_pj;
    }

    public void setIndica_receita_pj(boolean indica_receita_pj) {
        this.indica_receita_pj = indica_receita_pj;
    }

    public TipoPessoaEnum getTipoPessoaEnum() {
        return tipoPessoaEnum;
    }

    public void setTipoPessoaEnum(TipoPessoaEnum tipoPessoaEnum) {
        this.tipoPessoaEnum = tipoPessoaEnum;
    }

    public List<CampoFormularioDTO> getCamposFormularioDTO() {
        return camposFormularioDTO;
    }

    public void setCamposFormularioDTO(List<CampoFormularioDTO> camposFormularioDTO) {
        this.camposFormularioDTO = camposFormularioDTO;
    }
}
