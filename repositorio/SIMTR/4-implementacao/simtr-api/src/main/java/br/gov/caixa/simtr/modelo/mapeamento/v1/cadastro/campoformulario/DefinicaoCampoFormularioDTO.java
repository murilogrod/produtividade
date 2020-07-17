package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesConsultaCampoFormulario;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = ConstantesConsultaCampoFormulario.API_MODEL_DEFINICAO_CAMPO_FORMULARIO,
        description = "Objeto utilizado para representar uma definição completa de um campo formulário com suas opções e formas de apresentação.")
public class DefinicaoCampoFormularioDTO implements Serializable {
    private static final long serialVersionUID = 1L;
 
    @JsonProperty(value = ConstantesConsultaCampoFormulario.ID)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.ID, value = "Atributo que representa o identificador do campo.")
    private Long id;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.ORDEM)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.ORDEM, value = "Atributo utilizado para definir a ordem de exibição dos campos do formulário.")
    private Integer ordem;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.OBRIGATORIO)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.OBRIGATORIO, value = "Atributo que armazena o indicativo do obrigatoriedade do campo no formulário.")
    private Boolean obrigatorio;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.EXPRESSAO_INTERFACE)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.EXPRESSAO_INTERFACE, value = "Atributo que armazena a expressão a ser aplicada pelo Java script para determinar a exposição ou não do campo no formulário.")
    private String expressaoInterface;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.ATIVO)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.ATIVO, value = "Atributo que indica se o campo de entrada está apto ou não para ser inserido no formulário.")
    private Boolean ativo;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.IDENTIFICADOR_BPM)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.IDENTIFICADOR_BPM, value = "Atributo utilizado para que a solução de BPM possa capturar o campo de formulário independente do seu identificador serial que pode diferir de acordo com cada ambiente.")
    private Integer identificadorBpm;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.NOME)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.NOME, value = "Atributo utilizado para armazenar o nome do atributo retornado pela rotina/serviço de extração de dados do documento. Esse valor armazena o nome utilizado no campo de retorno da informação.")
    private String nome;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.NOME_ATRIBUTO_SICLI)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.NOME_ATRIBUTO_SICLI, value = "Nome do atributo utilizado na atualização de dados do SICLI.")
    private String nomeAtributoSICLI;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.NOME_OBJETO_SICLI)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.NOME_OBJETO_SICLI, value = "Atributo utilizado para definir o objeto que agrupa o atributo utilizado na atualização de dados do SICLI.")
    private String nomeObjetoSICLI;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.TIPO_ATRIBUTO_SICLI)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.TIPO_ATRIBUTO_SICLI, value = "Atributo utilizado para indicar qual o tipo de atributo definido junto ao objeto a ser enviado na atualização de dados do SICLI.")
    private TipoAtributoEnum tipoAtributoSicliEnum;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.TIPO_CAMPO)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.TIPO_CAMPO, value = "Atributo utilizado para armazenar o tipo de campo de formulario que será gerado.")
    private TipoCampoEnum tipoCampo;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.CHAVE)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.CHAVE, value = "Atributo que indica se o campo do formulário pode ser utilizado como chave de pesquisa posterior.")
    private Boolean chave;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.LABEL)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.LABEL, value = "Atributo que armazena o valor a ser exibido no label do campo do formulário para o usuário final.")
    private String label;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.MASCARA)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.MASCARA, value = "Atributo que armazena o valor da máscara de formatação do campo de for o caso.")
    private String mascara;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.PLACEHOLDER)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.PLACEHOLDER, value = "Atributo que armazena o valor do placeholder para exibição no campo de for o caso.")
    private String placeholder;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.TAMANHO_MINIMO)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.TAMANHO_MINIMO, value = "Atributo que armazena o número de caracteres mínimo utilizados em campos de texto livre.")
    private Integer tamanhoMinimo;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.TAMANHO_MAXIMO)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.TAMANHO_MAXIMO, value = "Atributo que armazena o número de caracteres máximo utilizados em campos de texto livre.")
    private Integer tamanhoMaximo;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.OPCOES_CAMPO)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.OPCOES_CAMPO, value = "Atributo que armazena uma lista de opções válidadas de um campo a serem visualizadas para escolha.")
    private List<OpcaoCampoDTO> opcoesCampo;
    
    @JsonProperty(value = ConstantesConsultaCampoFormulario.FORMAS_APRESENTACAO)
    @ApiModelProperty(name = ConstantesConsultaCampoFormulario.FORMAS_APRESENTACAO, value = "Atributo que armazena uma lista de formas de apresentação de um campo que difere de acordo com o dispositivo usado pelo usuário.")
    private List<FormaApresentacaoDTO> formasApresentacao;
    
    public DefinicaoCampoFormularioDTO(CampoFormulario campoFormulario) {
        this.opcoesCampo = new ArrayList<>();
        this.formasApresentacao = new ArrayList<>();
        this.id = campoFormulario.getId();
        this.ordem = campoFormulario.getOrdemApresentacao();
        this.obrigatorio = campoFormulario.getObrigatorio();
        this.expressaoInterface = campoFormulario.getExpressaoInterface();
        this.ativo = campoFormulario.getAtivo();
        this.identificadorBpm = campoFormulario.getIdentificadorBPM();
        this.nome = campoFormulario.getNomeCampo();
        this.nomeAtributoSICLI = campoFormulario.getNomeAtributoSICLI();
        this.nomeObjetoSICLI = campoFormulario.getNomeObjetoSICLI();
        this.tipoAtributoSicliEnum = campoFormulario.getTipoAtributoSicliEnum();
        this.tipoCampo = campoFormulario.getCampoEntrada().getTipo();
        this.chave = campoFormulario.getCampoEntrada().getChave();
        this.label = campoFormulario.getCampoEntrada().getLabel();
        this.mascara = campoFormulario.getCampoEntrada().getMascara();
        this.placeholder = campoFormulario.getCampoEntrada().getPlaceholder();
        this.tamanhoMinimo = campoFormulario.getCampoEntrada().getTamanhoMinimo();
        this.tamanhoMaximo = campoFormulario.getCampoEntrada().getTamanhoMaximo();
        if(Objects.nonNull(campoFormulario.getCampoEntrada().getOpcoesCampo()) && 
                !campoFormulario.getCampoEntrada().getOpcoesCampo().isEmpty()) {
            this.opcoesCampo = campoFormulario.getCampoEntrada().getOpcoesCampo().stream().map(opcaoCampo -> new OpcaoCampoDTO(opcaoCampo)).collect(Collectors.toList());
        }
        if(Objects.nonNull(campoFormulario.getCamposApresentacao()) &&
                !campoFormulario.getCamposApresentacao().isEmpty()) {
            this.formasApresentacao = campoFormulario.getCamposApresentacao().stream().map(campoApresentacao -> new FormaApresentacaoDTO(campoApresentacao)).collect(Collectors.toList());
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getIdentificadorBpm() {
        return identificadorBpm;
    }

    public void setIdentificadorBpm(Integer identificadorBpm) {
        this.identificadorBpm = identificadorBpm;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getNomeAtributoSICLI() {
        return nomeAtributoSICLI;
    }

    public void setNomeAtributoSICLI(String nomeAtributoSICLI) {
        this.nomeAtributoSICLI = nomeAtributoSICLI;
    }

    public String getNomeObjetoSICLI() {
        return nomeObjetoSICLI;
    }

    public void setNomeObjetoSICLI(String nomeObjetoSICLI) {
        this.nomeObjetoSICLI = nomeObjetoSICLI;
    }

    public TipoAtributoEnum getTipoAtributoSicliEnum() {
        return tipoAtributoSicliEnum;
    }

    public void setTipoAtributoSicliEnum(TipoAtributoEnum tipoAtributoSicliEnum) {
        this.tipoAtributoSicliEnum = tipoAtributoSicliEnum;
    }


    public TipoCampoEnum getTipoCampo() {
        return tipoCampo;
    }

    public void setTipoCampo(TipoCampoEnum tipoCampo) {
        this.tipoCampo = tipoCampo;
    }

    public Boolean getChave() {
        return chave;
    }

    public void setChave(Boolean chave) {
        this.chave = chave;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMascara() {
        return mascara;
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public Integer getTamanhoMinimo() {
        return tamanhoMinimo;
    }

    public void setTamanhoMinimo(Integer tamanhoMinimo) {
        this.tamanhoMinimo = tamanhoMinimo;
    }

    public Integer getTamanhoMaximo() {
        return tamanhoMaximo;
    }

    public void setTamanhoMaximo(Integer tamanhoMaximo) {
        this.tamanhoMaximo = tamanhoMaximo;
    }

    public List<OpcaoCampoDTO> getOpcoesCampo() {
        return opcoesCampo;
    }

    public void setOpcoesCampo(List<OpcaoCampoDTO> opcoesCampo) {
        this.opcoesCampo = opcoesCampo;
    }

    public List<FormaApresentacaoDTO> getFormasApresentacao() {
        return formasApresentacao;
    }

    public void setFormasApresentacao(List<FormaApresentacaoDTO> formasApresentacao) {
        this.formasApresentacao = formasApresentacao;
    }
}
