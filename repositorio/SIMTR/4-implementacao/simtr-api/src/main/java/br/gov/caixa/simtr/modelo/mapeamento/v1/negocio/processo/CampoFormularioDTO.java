package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.CampoEntrada;
import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioProcesso;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlRootElement(name = ConstantesNegocioProcesso.XML_ROOT_ELEMENT_CAMPO_FORMULARIO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioProcesso.API_MODEL_V1_CAMPO_FORMULARIO,
        description = "Objeto utilizado para representar o campo de formulario vinculado a um processo fase"
)
public class CampoFormularioDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(CampoFormularioDTO.class.getName());

    @XmlElement(name = ConstantesNegocioProcesso.ID)
    @ApiModelProperty(name = ConstantesNegocioProcesso.ID, required = true, value = "Identificador único do campo que representa a entrada de um formulario do processo")
    private Long id;

    @XmlElement(name = ConstantesNegocioProcesso.NOME_CAMPO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.NOME_CAMPO, required = true, value = "Nome do campo para fins de identificação programatica perante o formulario.")
    private String nome;

    @XmlElement(name = ConstantesNegocioProcesso.LABEL)
    @ApiModelProperty(name = ConstantesNegocioProcesso.LABEL, required = true, value = "Label da campo a ser apresentado na interface para o usuário.")
    private String label;

    @XmlElement(name = ConstantesNegocioProcesso.OBRIGATORIO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.OBRIGATORIO, required = true, value = "Indica se o campo deve ser apresentado na interface com restrição de obrigatoriedade no preenchimento. Essa obrigatoriedade deverá ser validada.")
    private Boolean obrigatorio;

    @XmlElement(name = ConstantesNegocioProcesso.EXPRESSAO_INTERFACE)
    @ApiModelProperty(name = ConstantesNegocioProcesso.EXPRESSAO_INTERFACE, required = false, value = "Determina uma expressão a ser utilizada pela interface para definir a condição para o campo ser apresentado ou ocultado no formulario.")
    private String expressaoInterface;

    @XmlElement(name = ConstantesNegocioProcesso.ORDEM_APRESENTACAO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.ORDEM_APRESENTACAO, required = true, value = "Indica a sequencia de apresentação deste campo perante o formulario.")
    private Integer ordemApresentacao;

    @XmlElement(name = ConstantesNegocioProcesso.TIPO_CAMPO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.TIPO_CAMPO, required = true, value = "Indica o tipo de componente a ser utilizado na montagem do formulario para apresentação do campo.Ex: INPUT, RADIO, CHECK, etc")
    private TipoCampoEnum tipoCampo;

    @XmlElement(name = ConstantesNegocioProcesso.MASCARA_CAMPO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.MASCARA_CAMPO, required = false, value = "Caso possua valor, determina a mascara a ser aplicada na entrada de dados pelo usuário.")
    private String mascaraCampo;

    @XmlElement(name = ConstantesNegocioProcesso.PLACEHOLDER_CAMPO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.PLACEHOLDER_CAMPO, required = false, value = "Caso possua valor, determina um exemplo de valor a ser preenchido pelo usuário de forma exibir no campo quando este esta vazio como um atributo placeholder previsto no HTML.")
    private String placeholderCampo;

    @XmlElement(name = ConstantesNegocioProcesso.TAMANHO_MINIMO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.TAMANHO_MINIMO, required = false, value = "Caso possua valor, determina uma validação de quantidade minima de caracteres a ser preenchida no campo do formulário.")
    private Integer tamanhoMinimoCampo;

    @XmlElement(name = ConstantesNegocioProcesso.TAMANHO_MAXIMO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.TAMANHO_MAXIMO, required = false, value = "Caso possua valor, determina uma validação de quantidade máxima de caracteres a ser preenchida no campo do formulário.")
    private Integer tamanhoMaximoCampo;
    
    // ************************************
    @XmlElementWrapper(name = ConstantesNegocioProcesso.OPCOES_DISPONIVEIS)
    @XmlElement(name = ConstantesNegocioProcesso.OPCAO_DISPONIVEL)
    @JsonProperty(value = ConstantesNegocioProcesso.OPCOES_DISPONIVEIS)
    @ApiModelProperty(name = ConstantesNegocioProcesso.OPCOES_DISPONIVEIS, required = false, value = "Caso o campo do formulario seja de respostas objetivas, essa será a lista de opções disponiveis para seleção pelo usuário.")
    private List<OpcaoCampoDTO> opcoesCampoVinculadas;

    @XmlElementWrapper(name = ConstantesNegocioProcesso.LISTA_APRESENTACOES)
    @XmlElement(name = ConstantesNegocioProcesso.FORMA_APRESENTACAO)
    @JsonProperty(value = ConstantesNegocioProcesso.LISTA_APRESENTACOES)
    @ApiModelProperty(name = ConstantesNegocioProcesso.LISTA_APRESENTACOES, required = true, value = "Contém a relação de modelos de apresentação previstos para o campo nas diversas resoluções de dispositivos.")
    private List<CampoApresentacaoDTO> camposApresentacaoVinculados;

    public CampoFormularioDTO() {
        super();
        this.opcoesCampoVinculadas = new ArrayList<>();
        this.camposApresentacaoVinculados = new ArrayList<>();
    }

    public CampoFormularioDTO(CampoFormulario campoFormulario) {
        this();
        this.id = campoFormulario.getId();
        this.obrigatorio = campoFormulario.getObrigatorio();
        this.expressaoInterface = campoFormulario.getExpressaoInterface();
        this.ordemApresentacao = campoFormulario.getOrdemApresentacao();
        this.nome = campoFormulario.getNomeCampo();

        if (campoFormulario.getCampoEntrada() != null) {
            CampoEntrada campoEntrada = campoFormulario.getCampoEntrada();
            this.label = campoEntrada.getLabel();
            this.tipoCampo = campoEntrada.getTipo();
            this.mascaraCampo = campoEntrada.getMascara();
            this.placeholderCampo = campoEntrada.getPlaceholder();
            this.tamanhoMinimoCampo = campoEntrada.getTamanhoMaximo();
            this.tamanhoMaximoCampo = campoEntrada.getTamanhoMaximo();
            try {
                if (campoEntrada.getOpcoesCampo() != null) {
                    campoEntrada.getOpcoesCampo().forEach(opcaoCampo -> this.opcoesCampoVinculadas.add(new OpcaoCampoDTO(opcaoCampo)));
                }
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.opcoesCampoVinculadas = null;
            }
        }

        try {
            if (campoFormulario.getCamposApresentacao() != null) {
                campoFormulario.getCamposApresentacao()
                        .forEach(campoApresentacao -> this.camposApresentacaoVinculados.add(new CampoApresentacaoDTO(campoApresentacao)));
            }
        } catch (RuntimeException re) {
            //Lazy Exception ou atributos não carregados
            LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
            this.camposApresentacaoVinculados = null;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(Boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public String getExpressaoInterface() {
        return expressaoInterface;
    }

    public void setExpressaoInterface(String expressaoInterface) {
        this.expressaoInterface = expressaoInterface;
    }

    public Integer getOrdemApresentacao() {
        return ordemApresentacao;
    }

    public void setOrdemApresentacao(Integer ordemApresentacao) {
        this.ordemApresentacao = ordemApresentacao;
    }

    public TipoCampoEnum getTipoCampo() {
        return tipoCampo;
    }

    public void setTipoCampo(TipoCampoEnum tipoCampo) {
        this.tipoCampo = tipoCampo;
    }

    public String getMascaraCampo() {
        return mascaraCampo;
    }

    public void setMascaraCampo(String mascaraCampo) {
        this.mascaraCampo = mascaraCampo;
    }

    public String getPlaceholderCampo() {
        return placeholderCampo;
    }

    public void setPlaceholderCampo(String placeholderCampo) {
        this.placeholderCampo = placeholderCampo;
    }

    public Integer getTamanhoMinimoCampo() {
        return tamanhoMinimoCampo;
    }

    public void setTamanhoMinimoCampo(Integer tamanhoMinimoCampo) {
        this.tamanhoMinimoCampo = tamanhoMinimoCampo;
    }

    public Integer getTamanhoMaximoCampo() {
        return tamanhoMaximoCampo;
    }

    public void setTamanhoMaximoCampo(Integer tamanhoMaximoCampo) {
        this.tamanhoMaximoCampo = tamanhoMaximoCampo;
    }

    public List<OpcaoCampoDTO> getOpcoesCampoVinculadas() {
        return opcoesCampoVinculadas;
    }

    public void setOpcoesCampoVinculadas(List<OpcaoCampoDTO> opcoesCampoVinculadas) {
        this.opcoesCampoVinculadas = opcoesCampoVinculadas;
    }

    public List<CampoApresentacaoDTO> getCamposApresentacaoVinculados() {
        return camposApresentacaoVinculados;
    }

    public void setCamposApresentacaoVinculados(List<CampoApresentacaoDTO> camposApresentacaoVinculados) {
        this.camposApresentacaoVinculados = camposApresentacaoVinculados;
    }

}
