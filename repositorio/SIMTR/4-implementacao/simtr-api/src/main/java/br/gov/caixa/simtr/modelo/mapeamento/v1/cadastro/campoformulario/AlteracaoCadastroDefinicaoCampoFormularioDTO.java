package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesAlteracaoCadastroDefinicaoCampoFormulario;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
          value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.API_MODEL_ALTERACAO_CADASTRO_DEFINICAO_CAMPO_FORMULARIO,
          description = "Objeto utilizado para atualizar uma definição completa de um campo formulário com suas formas de apresentação.")
public class AlteracaoCadastroDefinicaoCampoFormularioDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.IDENTIFICADOR_PROCESSO_DOSSIE)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.IDENTIFICADOR_PROCESSO_DOSSIE, value = "Identificador do processo originador ao qual o campo formulário está vinculado.")
    private Integer identificadorProcessoDossie;
    
    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.IDENTIFICADOR_PROCESSO_FASE)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.IDENTIFICADOR_PROCESSO_FASE, value = "Identificador do processo fase ao qual o campo de formulário está vinculado.")
    private Integer identificadorProcessoFase;
    
    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.IDENTIFICADOR_TIPO_RELACIONAMENTO)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.IDENTIFICADOR_TIPO_RELACIONAMENTO, value = "Identificador do tipo de relacionamento ao qual o campo de formulário está vinculado.")
    private Integer identificadorTipoRelacionamento;
    
    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.IDENTIFICADOR_PRODUTO)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.IDENTIFICADOR_PRODUTO, value = "Identificador do produto ao qual o campo de formulário está vinculado.")
    private Integer identificadorProduto;
    
    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.IDENTIFICADOR_GARANTIA)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.IDENTIFICADOR_GARANTIA, value = "Identificador da garantia fase ao qual o campo de formulário está vinculado.")
    private Integer identificadorGarantia;                       
    
    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.ORDEM)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.ORDEM, value = "Atributo utilizado para definir a ordem de exibição dos campos do formulário.")
    private Integer ordem;

    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.OBRIGATORIO)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.OBRIGATORIO, value = "Atributo que armazena o indicativo do obrigatoriedade do campo no formulário.")
    private Boolean obrigatorio;

    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.EXPRESSAO_INTERFACE)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.EXPRESSAO_INTERFACE,
                      value = "Atributo que armazena a expressão a ser aplicada pelo Java script para determinar a exposição ou não do campo no formulário.")
    private String expressaoInterface;

    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.ATIVO)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.ATIVO, value = "Atributo que indica se o campo de entrada está apto ou não para ser inserido no formulário.")
    private Boolean ativo;

    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.IDENTIFICADOR_BPM)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.IDENTIFICADOR_BPM,
                      value = "Atributo utilizado para que a solução de BPM possa capturar o campo de formulário independente do seu identificador serial que pode diferir de acordo com cada ambiente.")
    private Integer identificadorBpm;

    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.NOME)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.NOME, value = "Atributo utilizado para armazenar o nome do atributo retornado pela rotina/serviço de extração de dados do documento. Esse valor armazena o nome utilizado no campo de retorno da informação.")
    private String nome;

    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.NOME_ATRIBUTO_SICLI)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.NOME_ATRIBUTO_SICLI, value = "Nome do atributo utilizado na atualização de dados do SICLI.")
    private String nomeAtributoSICLI;
    
    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.NOME_OBJETO_SICLI)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.NOME_OBJETO_SICLI, value = "Atributo utilizado para definir o objeto que agrupa o atributo utilizado na atualização de dados do SICLI.")
    private String nomeObjetoSICLI;
    
    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.TIPO_ATRIBUTO_SICLI)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.TIPO_ATRIBUTO_SICLI, value = "Atributo utilizado para indicar qual o tipo de atributo definido junto ao objeto a ser enviado na atualização de dados do SICLI.")
    private TipoAtributoEnum tipoAtributoSicliEnum;
    
    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.TIPO_CAMPO)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.TIPO_CAMPO, value = "Atributo utilizado para armazenar o tipo de campo de formulario que será gerado.")
    private TipoCampoEnum tipoCampo;

    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.CHAVE)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.CHAVE, value = "Atributo que indica se o campo do formulário pode ser utilizado como chave de pesquisa posterior.")
    private Boolean chave;

    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.LABEL)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.LABEL, value = "Atributo que armazena o valor a ser exibido no label do campo do formulário para o usuário final.")
    private String label;

    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.MASCARA)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.MASCARA, value = "Atributo que armazena o valor da máscara de formatação do campo de for o caso.")
    private String mascara;

    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.PLACEHOLDER)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.PLACEHOLDER, value = "Atributo que armazena o valor do placeholder para exibição no campo de for o caso.")
    private String placeholder;

    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.TAMANHO_MINIMO)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.TAMANHO_MINIMO, value = "Atributo que armazena o número de caracteres mínimo utilizados em campos de texto livre.")
    private Integer tamanhoMinimo;

    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.TAMANHO_MAXIMO)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.TAMANHO_MAXIMO, value = "Atributo que armazena o número de caracteres máximo utilizados em campos de texto livre.")
    private Integer tamanhoMaximo;

    @JsonProperty(value = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.FORMAS_APRESENTACAO)
    @ApiModelProperty(name = ConstantesAlteracaoCadastroDefinicaoCampoFormulario.FORMAS_APRESENTACAO,
                      value = "Atributo que armazena uma lista de formas de apresentação de um campo que difere de acordo com o dispositivo usado pelo usuário.")
    private List<FormaApresentacaoDTO> formasApresentacao;
    
    public Integer getIdentificadorProcessoDossie() {
        return identificadorProcessoDossie;
    }

    public void setIdentificadorProcessoDossie(Integer identificadorProcessoDossie) {
        this.identificadorProcessoDossie = identificadorProcessoDossie;
    }

    public Integer getIdentificadorProcessoFase() {
        return identificadorProcessoFase;
    }

    public void setIdentificadorProcessoFase(Integer identificadorProcessoFase) {
        this.identificadorProcessoFase = identificadorProcessoFase;
    }

    public Integer getIdentificadorTipoRelacionamento() {
        return identificadorTipoRelacionamento;
    }

    public void setIdentificadorTipoRelacionamento(Integer identificadorTipoRelacionamento) {
        this.identificadorTipoRelacionamento = identificadorTipoRelacionamento;
    }

    public Integer getIdentificadorProduto() {
        return identificadorProduto;
    }

    public void setIdentificadorProduto(Integer identificadorProduto) {
        this.identificadorProduto = identificadorProduto;
    }

    public Integer getIdentificadorGarantia() {
        return identificadorGarantia;
    }

    public void setIdentificadorGarantia(Integer identificadorGarantia) {
        this.identificadorGarantia = identificadorGarantia;
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

    public List<FormaApresentacaoDTO> getFormasApresentacao() {
        return formasApresentacao;
    }

    public void setFormasApresentacao(List<FormaApresentacaoDTO> formasApresentacao) {
        this.formasApresentacao = formasApresentacao;
    }

}
