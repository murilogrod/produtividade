package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesCadastroTipoDocumento.XML_ROOT_ELEMENT_ATRIBUTO_DADOS_DECLARADOS)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesCadastroTipoDocumento.API_MODEL_DADOS_DECLARADOS,
        description = "Objeto utilizado para representar os campos a serem apresentados na tela de Dados declarados."
)
public class AtributoDadosDeclaradosDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AtributoDadosDeclaradosDTO.class.getName());

    @XmlElement(name = ConstantesCadastroTipoDocumento.NOME_NEGOCIAL)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_NEGOCIAL, value = "Nome negocial do atributo do documento.", required = true)
    private String nomeNegocial;

    @Deprecated
    @XmlElement(name = ConstantesCadastroTipoDocumento.NOME_RETORNO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_RETORNO, value = "Nome do atributo esperado no retorno e envio de dados para a integração e que será armazenado como nome do atributo perante o registro de atributos do documento.", required = true)
    private String nomeRetorno;

    @XmlElement(name = ConstantesCadastroTipoDocumento.NOME_DOCUMENTO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_DOCUMENTO, value = "Nome do atributo esperado no retorno e envio de dados para a integração e que será armazenado como nome do atributo perante o registro de atributos do documento.", required = true)
    private String nomeDocumento;

    @XmlElement(name = ConstantesCadastroTipoDocumento.INDICADOR_OBRIGATORIO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_OBRIGATORIO, value = "Indica se o atributo é Obrigatorio na extração de dados de documentos.", required = true)
    private Boolean indicadorObrigatorio;

    @XmlElement(name = ConstantesCadastroTipoDocumento.TIPO_CAMPO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.TIPO_CAMPO, value = "Indica o tipo do campo a ser preenchido.", required = true)
    private TipoCampoEnum tipoCampo;

    @XmlElement(name = ConstantesCadastroTipoDocumento.ORIENTACAO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ORIENTACAO, value = "Informa uma orientação a ser exibida para o usuário sobre como preencher o campo.", required = false)
    private String orientacaoPreenchimento;

    @XmlElement(name = ConstantesCadastroTipoDocumento.GRUPO_ATRIBUTO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.GRUPO_ATRIBUTO, required = false, value = "Indica o grupo ao qual o atributo faz parte. Caso este atributo faça parte de um grupo, todos os demais atributos do grupo deverão ser informados, caso ao menos um deles seja informado.")
    private Integer grupoAtributo;

    @XmlElement(name = ConstantesCadastroTipoDocumento.ORDEM_APRESENTACAO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ORDEM_APRESENTACAO, required = true, value = "Indica a sequencia de apresentação deste campo perante o formulario.")
    private Integer ordemApresentacao;

    @XmlElement(name = ConstantesCadastroTipoDocumento.EXPRESSAO_INTERFACE)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.EXPRESSAO_INTERFACE, required = false, value = "Determina uma expressão a ser utilizada pela interface para definir a condição para o campo ser apresentado ou ocultado no formulario.")
    private String expressaoInterface;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = ConstantesCadastroTipoDocumento.OPCOES)
    @XmlElement(name = ConstantesCadastroTipoDocumento.OPCAO)
    @XmlElementWrapper(name = ConstantesCadastroTipoDocumento.OPCOES)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.OPCOES, required = false, value = "Lista de atributos previstos (extração), associadas ao tipo de documento")
    private List<OpcaoAtributoDadosDeclaradosDTO> opcoesAtributoDTO;

    public AtributoDadosDeclaradosDTO() {
        super();
        this.opcoesAtributoDTO = new ArrayList<>();
    }

    public AtributoDadosDeclaradosDTO(AtributoExtracao atributoExtracao) {
        this();
        this.nomeNegocial = atributoExtracao.getNomeNegocial();
        this.nomeRetorno = atributoExtracao.getNomeAtributoRetorno();
        this.nomeDocumento = atributoExtracao.getNomeAtributoDocumento();
        this.indicadorObrigatorio = atributoExtracao.getObrigatorio();
        this.tipoCampo = atributoExtracao.getTipoCampoEnum();
        this.orientacaoPreenchimento = atributoExtracao.getOrientacaoPreenchimento();
        this.grupoAtributo = atributoExtracao.getGrupoAtributo();
        this.ordemApresentacao = atributoExtracao.getOrdemApresentacao();
        this.expressaoInterface = atributoExtracao.getExpressaoInterface();

        try {
            atributoExtracao.getOpcoesAtributo().stream()
                    .forEach(op -> this.opcoesAtributoDTO.add(new OpcaoAtributoDadosDeclaradosDTO(op.getValorOpcao(), op.getDescricaoOpcao())));
        } catch (RuntimeException re) {
            //Lazy Exception ou atributos não carregados
            LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
            this.opcoesAtributoDTO = null;
        }
    }

    public String getNomeNegocial() {
        return nomeNegocial;
    }

    public void setNomeNegocial(String nomeNegocial) {
        this.nomeNegocial = nomeNegocial;
    }

    public String getNomeRetorno() {
        return nomeRetorno;
    }

    public void setNomeRetorno(String nomeRetorno) {
        this.nomeRetorno = nomeRetorno;
    }

    public String getNomeDocumento() {
        return nomeDocumento;
    }

    public void setNomeDocumento(String nomeDocumento) {
        this.nomeDocumento = nomeDocumento;
    }

    public Boolean getIndicadorObrigatorio() {
        return indicadorObrigatorio;
    }

    public void setIndicadorObrigatorio(Boolean indicadorObrigatorio) {
        this.indicadorObrigatorio = indicadorObrigatorio;
    }

    public TipoCampoEnum getTipoCampo() {
        return tipoCampo;
    }

    public void setTipoCampo(TipoCampoEnum tipoCampo) {
        this.tipoCampo = tipoCampo;
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

    public List<OpcaoAtributoDadosDeclaradosDTO> getOpcoesAtributoDTO() {
        return opcoesAtributoDTO;
    }

    public void setOpcoesAtributoDTO(List<OpcaoAtributoDadosDeclaradosDTO> opcoesAtributoDTO) {
        this.opcoesAtributoDTO = opcoesAtributoDTO;
    }
}
