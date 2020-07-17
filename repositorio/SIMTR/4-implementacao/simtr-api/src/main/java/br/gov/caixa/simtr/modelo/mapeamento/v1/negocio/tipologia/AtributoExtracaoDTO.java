package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tipologia;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.OpcaoAtributo;
import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTipologia;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlRootElement(name = ConstantesNegocioTipologia.XML_ROOT_ELEMENT_ATRIBUTO_EXTRACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTipologia.API_MODEL_V1_ATRIBUTO_EXTRACAO,
        description = "Objeto utilizado para representar o atributo definido para extração de dados do documento."
)
public class AtributoExtracaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AtributoExtracaoDTO.class.getName());

    @XmlElement(name = ConstantesNegocioTipologia.ID)
    @ApiModelProperty(name = ConstantesNegocioTipologia.ID, required = true, value = "Identificador do atributo do documento.")
    private Integer id;

    @XmlElement(name = ConstantesNegocioTipologia.NOME_NEGOCIAL)
    @ApiModelProperty(name = ConstantesNegocioTipologia.NOME_NEGOCIAL, required = true, value = "Nome negocial do atributo do documento.")
    private String nomeNegocial;

    @XmlElement(name = ConstantesNegocioTipologia.NOME_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioTipologia.NOME_DOCUMENTO, required = false, value = "Nome do atributo esperado no retorno e envio de dados para a intgração e que será armazenado como nome do atributo perante o registro de atributos do documento.")
    private String nomeDocumento;

    @XmlElement(name = ConstantesNegocioTipologia.OBRIGATORIO)
    @ApiModelProperty(name = ConstantesNegocioTipologia.OBRIGATORIO, required = true, value = "Indica se o atributo deve ser considerado obrigatorio na extração de dados de documentos.")
    private boolean obrigatorio;

    @XmlElement(name = ConstantesNegocioTipologia.ORIENTACAO_PREENCHIMENTO)
    @ApiModelProperty(name = ConstantesNegocioTipologia.ORIENTACAO_PREENCHIMENTO, required = false, value = "Indica uma orientação de preenchimento do campo a ser apresentada ao usuário da interface de forma a auxilia-lo na correta captura da informação.")
    private String orientacaoPreenchimento;

    @XmlElement(name = ConstantesNegocioTipologia.GRUPO_ATRIBUTO)
    @ApiModelProperty(name = ConstantesNegocioTipologia.GRUPO_ATRIBUTO, required = true, value = "Indica o grupo ao qual o atributo faz parte. Caso este atributo faça parte de um grupo, todos os demais atributos do grupo deverão ser infordos, caso ao menos um deles seja informado.")
    private Integer grupoAtributo;

    @XmlElement(name = ConstantesNegocioTipologia.ORDEM_APRESENTACAO)
    @ApiModelProperty(name = ConstantesNegocioTipologia.ORDEM_APRESENTACAO, required = true, value = "Indica a sequencia de apresentação deste campo perante o formulario.")
    private Integer ordemApresentacao;

    @XmlElement(name = ConstantesNegocioTipologia.EXPRESSAO_INTERFACE)
    @ApiModelProperty(name = ConstantesNegocioTipologia.EXPRESSAO_INTERFACE, required = false, value = "Determina uma expressão a ser utilizada pela interface para definir a condição para o campo ser apresentado ou ocultado no formulario.")
    private String expressaoInterface;

    @XmlElement(name = ConstantesNegocioTipologia.TIPO_CAMPO)
    @ApiModelProperty(name = ConstantesNegocioTipologia.TIPO_CAMPO, required = true, value = "Indica qual modelo de campo de entrada deve ser utilizado em uma montagem de formulário na interface para correta captura da informação.")
    private TipoCampoEnum tipoCampoEnum;

    @XmlElement(name = ConstantesNegocioTipologia.PRESENTE_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioTipologia.PRESENTE_DOCUMENTO, required = true, value = "Indica se o atributo trata-se de informação presente no documento.")
    private Boolean presenteDocumento;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @XmlElement(name = ConstantesNegocioTipologia.OPCAO)
    @XmlElementWrapper(name = ConstantesNegocioTipologia.OPCOES)
    @JsonProperty(value = ConstantesNegocioTipologia.OPCOES)
    @ApiModelProperty(name = ConstantesNegocioTipologia.OPCOES, required = false, value = "Lista de atributos previstos (extração), associadas ao tipo de documento")
    private List<OpcaoAtributoDTO> opcoesAtributoDTO;

    public AtributoExtracaoDTO() {
        super();
        this.opcoesAtributoDTO = new ArrayList<>();
    }

    public AtributoExtracaoDTO(AtributoExtracao atributoExtracao) {
        this();
        this.id = atributoExtracao.getId();
        this.nomeDocumento = atributoExtracao.getNomeAtributoDocumento();
        this.nomeNegocial = atributoExtracao.getNomeNegocial();
        this.obrigatorio = atributoExtracao.getObrigatorio();
        this.orientacaoPreenchimento = atributoExtracao.getOrientacaoPreenchimento();
        this.grupoAtributo = atributoExtracao.getGrupoAtributo();
        this.ordemApresentacao = atributoExtracao.getOrdemApresentacao();
        this.expressaoInterface = atributoExtracao.getExpressaoInterface();
        this.tipoCampoEnum = atributoExtracao.getTipoCampoEnum();
        this.presenteDocumento = atributoExtracao.getPresenteDocumento();

        try {
            atributoExtracao.getOpcoesAtributo().stream()
                    .forEach(op -> this.opcoesAtributoDTO.add(new OpcaoAtributoDTO(op.getValorOpcao(), op.getDescricaoOpcao())));
        } catch (RuntimeException re) {
            //Lazy Exception ou atributos não carregados
            LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
            this.opcoesAtributoDTO = null;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeNegocial() {
        return nomeNegocial;
    }

    public void setNomeNegocial(String nomeNegocial) {
        this.nomeNegocial = nomeNegocial;
    }

    public String getNomeDocumento() {
        return nomeDocumento;
    }

    public void setNomeDocumento(String nomeDocumento) {
        this.nomeDocumento = nomeDocumento;
    }

    public boolean isObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public String getOrientacaoPreenchimento() {
        return orientacaoPreenchimento;
    }

    public void setOrientacaoPreenchimento(String orientacaoPreenchimento) {
        this.orientacaoPreenchimento = orientacaoPreenchimento;
    }

    public Integer getGrupoAtributo() {
        return grupoAtributo;
    }

    public void setGrupoAtributo(Integer grupoAtributo) {
        this.grupoAtributo = grupoAtributo;
    }

    public Integer getOrdemApresentacao() {
        return ordemApresentacao;
    }

    public void setOrdemApresentacao(Integer ordemApresentacao) {
        this.ordemApresentacao = ordemApresentacao;
    }

    public String getExpressaoInterface() {
        return expressaoInterface;
    }

    public void setExpressaoInterface(String expressaoInterface) {
        this.expressaoInterface = expressaoInterface;
    }

    public TipoCampoEnum getTipoCampoEnum() {
        return tipoCampoEnum;
    }

    public void setTipoCampoEnum(TipoCampoEnum tipoCampoEnum) {
        this.tipoCampoEnum = tipoCampoEnum;
    }

    public Boolean getPresenteDocumento() {
        return presenteDocumento;
    }

    public void setPresenteDocumento(Boolean presenteDocumento) {
        this.presenteDocumento = presenteDocumento;
    }

    public List<OpcaoAtributoDTO> getOpcoesAtributoDTO() {
        return opcoesAtributoDTO;
    }

    public void setOpcoesAtributoDTO(List<OpcaoAtributoDTO> opcoesAtributoDTO) {
        this.opcoesAtributoDTO = opcoesAtributoDTO;
    }
}
