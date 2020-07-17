package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.CampoApresentacao;
import br.gov.caixa.simtr.modelo.entidade.CampoEntrada;
import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.entidade.OpcaoCampo;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroDefinicaoCampoFormulario;
import br.gov.caixa.simtr.visao.PrototypeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = ConstantesCadastroDefinicaoCampoFormulario.API_MODEL_CADASTRO_DEFINICAO_CAMPO_FORMULARIO,
        description = "Objeto utilizado para cadastrar uma definição completa de um campo formulário com suas opções e formas de apresentação.")
public class CadasdroDefinicaoCampoFormularioDTO implements Serializable, PrototypeDTO<CampoFormulario> {
    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.ORDEM)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.ORDEM, value = "Atributo utilizado para definir a ordem de exibição dos campos do formulário.")
    private Integer ordem;
    
    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.OBRIGATORIO)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.OBRIGATORIO, value = "Atributo que armazena o indicativo do obrigatoriedade do campo no formulário.")
    private Boolean obrigatorio;
    
    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.EXPRESSAO_INTERFACE)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.EXPRESSAO_INTERFACE, value = "Atributo que armazena a expressão a ser aplicada pelo Java script para determinar a exposição ou não do campo no formulário.")
    private String expressaoInterface;
    
    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.ATIVO, value = "Atributo que indica se o campo de entrada está apto ou não para ser inserido no formulário.")
    private Boolean ativo;
    
    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.IDENTIFICADOR_BPM)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.IDENTIFICADOR_BPM, value = "Atributo utilizado para que a solução de BPM possa capturar o campo de formulário independente do seu identificador serial que pode diferir de acordo com cada ambiente.")
    private Integer identificadorBpm;
    
    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.NOME)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.NOME, value = "Atributo utilizado para armazenar o nome do atributo retornado pela rotina/serviço de extração de dados do documento. Esse valor armazena o nome utilizado no campo de retorno da informação.")
    private String nome;
    
    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.NOME_ATRIBUTO_SICLI)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.NOME_ATRIBUTO_SICLI, value = "Nome do atributo utilizado na atualização de dados do SICLI.")
    private String nomeAtributoSICLI;
    
    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.NOME_OBJETO_SICLI)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.NOME_OBJETO_SICLI, value = "Atributo utilizado para definir o objeto que agrupa o atributo utilizado na atualização de dados do SICLI.")
    private String nomeObjetoSICLI;
    
    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.TIPO_ATRIBUTO_SICLI)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.TIPO_ATRIBUTO_SICLI, value = "Atributo utilizado para indicar qual o tipo de atributo definido junto ao objeto a ser enviado na atualização de dados do SICLI.")
    private TipoAtributoEnum tipoAtributoSicliEnum;
    
    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.TIPO_CAMPO)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.TIPO_CAMPO, value = "Atributo utilizado para armazenar o tipo de campo de formulario que será gerado.")
    private TipoCampoEnum tipoCampo;
    
    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.CHAVE)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.CHAVE, value = "Atributo que indica se o campo do formulário pode ser utilizado como chave de pesquisa posterior.")
    private Boolean chave;
    
    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.LABEL)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.LABEL, value = "Atributo que armazena o valor a ser exibido no label do campo do formulário para o usuário final.")
    private String label;
    
    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.MASCARA)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.MASCARA, value = "Atributo que armazena o valor da máscara de formatação do campo de for o caso.")
    private String mascara;
    
    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.PLACEHOLDER)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.PLACEHOLDER, value = "Atributo que armazena o valor do placeholder para exibição no campo de for o caso.")
    private String placeholder;
    
    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.TAMANHO_MINIMO)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.TAMANHO_MINIMO, value = "Atributo que armazena o número de caracteres mínimo utilizados em campos de texto livre.")
    private Integer tamanhoMinimo;
       
    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.TAMANHO_MAXIMO)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.TAMANHO_MAXIMO, value = "Atributo que armazena o número de caracteres máximo utilizados em campos de texto livre.")
    private Integer tamanhoMaximo;
    
    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.OPCOES_CAMPO)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.OPCOES_CAMPO, value = "Atributo que armazena uma lista de opções válidadas de um campo a serem visualizadas para escolha.")
    private List<OpcaoCampoDTO> opcoesCampo;
    
    @JsonProperty(value = ConstantesCadastroDefinicaoCampoFormulario.FORMAS_APRESENTACAO)
    @ApiModelProperty(name = ConstantesCadastroDefinicaoCampoFormulario.FORMAS_APRESENTACAO, value = "Atributo que armazena uma lista de formas de apresentação de um campo que difere de acordo com o dispositivo usado pelo usuário.")
    private List<FormaApresentacaoDTO> formasApresentacao;

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
    
    @Override
    public CampoFormulario prototype() {
        CampoFormulario campoFormulario = new CampoFormulario();
        campoFormulario.setOrdemApresentacao(this.getOrdem());
        campoFormulario.setObrigatorio(this.getObrigatorio());
        campoFormulario.setExpressaoInterface(this.getExpressaoInterface());
        campoFormulario.setAtivo(this.getAtivo());
        campoFormulario.setIdentificadorBPM(this.getIdentificadorBpm());
        campoFormulario.setNomeCampo(this.getNome());
        campoFormulario.setNomeAtributoSICLI(this.getNomeAtributoSICLI());
        campoFormulario.setNomeObjetoSICLI(this.getNomeObjetoSICLI());
        campoFormulario.setTipoAtributoSicliEnum(this.getTipoAtributoSicliEnum());
        
        CampoEntrada campoEntrada = new CampoEntrada();
        campoEntrada.setTipo(this.getTipoCampo());
        campoEntrada.setChave(this.getChave());
        campoEntrada.setLabel(this.getLabel());
        campoEntrada.setMascara(this.getMascara());
        campoEntrada.setPlaceholder(this.getPlaceholder());
        campoEntrada.setTamanhoMinimo(this.getTamanhoMinimo());
        campoEntrada.setTamanhoMaximo(this.getTamanhoMaximo());
        if(Objects.nonNull(this.getOpcoesCampo()) && !this.getOpcoesCampo().isEmpty()) {
            List<OpcaoCampo> opcoesCampo = this.getOpcoesCampo().stream().map(opcaoCampoDTO -> new OpcaoCampo(opcaoCampoDTO.getValor(), opcaoCampoDTO.getDescricao(), opcaoCampoDTO.getAtivo())).collect(Collectors.toList());
            campoEntrada.addOpoesCampo(opcoesCampo.toArray(new OpcaoCampo[opcoesCampo.size()]));
        }
        campoFormulario.setCampoEntrada(campoEntrada);
        
        if(Objects.nonNull(this.getFormasApresentacao()) && !this.getFormasApresentacao().isEmpty()) {
           List<CampoApresentacao> camposApresentacao =  this.getFormasApresentacao().stream().map(formaApresentacaoDTO -> new CampoApresentacao(formaApresentacaoDTO.getTipoDispositivo(), formaApresentacaoDTO.getLargura())).collect(Collectors.toList());
           campoFormulario.addCamposApresentacao(camposApresentacao.toArray(new CampoApresentacao[camposApresentacao.size()]));
        }
        return campoFormulario;
    }

}
