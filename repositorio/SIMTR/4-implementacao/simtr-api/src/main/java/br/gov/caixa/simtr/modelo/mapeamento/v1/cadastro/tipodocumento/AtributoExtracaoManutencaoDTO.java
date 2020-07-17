package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.EstrategiaPartilhaEnum;
import java.io.Serializable;

import br.gov.caixa.simtr.modelo.enumerator.ModoPartilhaEnum;
import br.gov.caixa.simtr.modelo.enumerator.SICPFCampoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SICPFModoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = ConstantesCadastroTipoDocumento.API_MODEL_ATRIBUTO_EXTRACAO_MANUTENCAO,
        description = "Objeto utilizado para representar as alteracoes um atributo previsto (chamado de atributo extração) vinculado a um tipo de documento no envio de inclusão sob a ótica dos cadastros."
)
public class AtributoExtracaoManutencaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_NEGOCIAL)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_NEGOCIAL, required = true, value = "Atributo utilizado para armazenar o nome do atributo identificado pelo negocio. Esse valor armazena o nome utilizado no dia a dia e que pode ser apresentado como algum label no sistema quando necessario.")
    private String nomeAtributoNegocial;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_DOCUMENTO, required = true, value = "Atributo utilizado para armazenar o nome do atributo identificado pela integração com outros sistemas")
    private String nomeAtributoDocumento;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_RETORNO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_RETORNO, required = false, value = "Atributo utilizado para armazenar o nome do atributo retornado pela rotina/serviço de extração de dados do documento. Esse valor armazena o nome utilizado no campo de retorno da informação")
    private String nomeAtributoRetorno;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.TIPO_CAMPO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.TIPO_CAMPO, required = false, value = "Atributo utilizado para indicar o tipo de campo que deverá ser utilizado pela interface para os casos de captura da informação quando inserida de forma manual")
    private TipoCampoEnum tipoCampoEnum;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_GERAL)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_GERAL, required = false, value = "Atributo utilizado para indicar qual o tipo de atributo definido para ações internas do SIMTR como geração de minutas, ")
    private TipoAtributoEnum tipoAtributoGeral;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_OBRIGATORIO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_OBRIGATORIO, required = true, value = "Atributo utilizado para indicar se esta informação é de captura obrigatorio para o tipo de documento associado")
    private Boolean obrigatorio;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATIVO, required = true, value = "Atributo utilizado para definir se o atributo deve ser procurado para captura no objeto de retorno dos dados extaidos do documento.")
    private Boolean ativo;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_SIECM)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_SIECM, required = false, value = "Atributo utilizado para definir o nome do atributo no SIECM. Caso seja enviado uma string vazia (\"\") que o campo será definido como nulo.")
    private String nomeAtributoSIECM;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SIECM)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SIECM, required = false, value = "Atributo utilizado para indicar qual o tipo de dado definido junto a classe SIECM.")
    private TipoAtributoEnum tipoAtributoSIECM;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_OBRIGATORIO_SIECM)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_OBRIGATORIO_SIECM, required = false, value = "Atributo utilizado para indicar se esta informação junto ao SIECM tem cunho obrigatorio.")
    private Boolean obrigatorioSIECM;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_SICLI)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_SICLI, required = false, value = "Atributo utilizado para definir o nome do atributo utilizado na atualização de dados do SICLI. "
            + "Esse atributo deve ser utilizado em conjunto com o atributo nome_objeto_sicli e tipo_atributo_sicli que determina o agrupamento do atributo a ser enviado na mensagem de atualização. "
            + "Esse campo determina o comportamento dos atributos nome_objeto_sicli e tipo_atributo_sicli e caso informado o tipo_atributo_sicli se torna obrigatório. "
            + "Caso seja enviado uma string vazia (\"\") o campo será definido como nulo e os campos relacionados também serão anulados.")
    private String nomeAtributoSICLI;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME_OBJETO_SICLI)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_OBJETO_SICLI, required = false, value = "Atributo utilizado para definir o objeto que agrupa o atributo utilizado na atualização de dados do SICLI. Esse atributo deve ser utilizado em conjunto com o atributo nome_atributo_sicli e tipo_sicli que determina o agrupamento do atributo a ser enviado na mensagem de atualização")
    private String nomeObjetoSICLI;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SICLI)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SICLI, required = false, value = "Atributo utilizado para indicar como (tipo de dado) este registro deverá ser definido para envio ao SICLI na rotina de atualização de dados.")
    private TipoAtributoEnum tipoAtributoSICLI;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_SICOD)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_SICOD, required = false, value = "Atributo utilizado para definir o nome do atributo a ser referencia na rotina de comunicação com o SICOD. Caso seja enviado uma string vazia (\"\") que o campo será definido como nulo.")
    private String nomeAtributoSICOD;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SICOD)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SICOD, required = false, value = "Atributo utilizado para indicar como (tipo de dado) este registro deverá ser definido para envio ao SICOD na rotina de conferencia documental do serviço do Dossiê Digital.")
    private TipoAtributoEnum tipoAtributoSICOD;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_CAMPO_COMPARACAO_RECEITA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_CAMPO_COMPARACAO_RECEITA, required = false, value = "Atributo utilizado para indicar qual campo de retorno o SICPF deve ser utilizado para uma possivel comparação. "
            + "Este campo deve ser enviado mesmo não havendo modificação ou o mesmo será anulado e anulará o atributo modo_comparacao_sicpf.")
    private SICPFCampoEnum sicpfCampoEnum;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_MODO_COMPARACAO_RECEITA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_MODO_COMPARACAO_RECEITA, required = false, value = "Atributo utilizado para indicar a forma de comparação com o campo de retorno o SICPF deve ser utilizado para uma possivel comparação sendo que [E] indica que nome_dccumento deve ter o valor EXATO (igual) ao valor retornado no SICPF e o [P] indica que o valor deve estar contido na informação retornada.")
    private SICPFModoEnum sicpfModoEnum;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_CALCULO_DATA_VALIDADE)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_CALCULO_DATA_VALIDADE, required = true, value = "Atrinuto utilizado para indicar se o atributo extraido do documento deve ser utilizado no calculo da data de validade baseado nas regras do tipo de documento quanto a <validade autocontida> e o <prazo de validade em dias>. Para este atributo deverá ter apenas um regsitro marcado como true para o mesmo tipo de documento")
    private Boolean calculoData;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_IDENTIFICADOR_PESSOA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_IDENTIFICADOR_PESSOA, required = true, value = "Atributo utilizado para indicar se o atributo extraido do documento deve ser utilizado como identificador de CPF/CNPJ para associação do documento a um determinado dossiê de cliente")
    private Boolean identificadorPessoa;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.VALOR_PADRAO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.VALOR_PADRAO, required = false, value = "Atributo utilizado para manter opcionalmente um valor padrão a ser utilizado no envio das informação para as integrações quando não informado ou não capturado no documento.")
    private String valorPadrao;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_PRESENTE_DOCUMENTO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_PRESENTE_DOCUMENTO, required = true, value = "Atributo utilizado para indicar se a informação pode ser encontrada no documento ou trata-se de um metadado necessario para disponibilização da informação em algum sistema de integração.")
    private Boolean presenteDocumento;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.ORIENTACAO_PREENCHIMENTO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ORIENTACAO_PREENCHIMENTO, required = true, value = "Atributo utilizado para armazenar uma orientação de preenchimento do campo a ser exibida nos formulários dinâmicos apresentados ao operador que realiza extração de dados do documento.")
    private String orientacaoPreenchimento;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.GRUPO_ATRIBUTO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.GRUPO_ATRIBUTO, required = false, value = "Indica o grupo ao qual o atributo faz parte. Caso este atributo faça parte de um grupo, todos os demais atributos do grupo deverão ser informados, caso ao menos um deles seja informado.")
    private Integer grupoAtributo;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.ORDEM_APRESENTACAO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ORDEM_APRESENTACAO, required = true, value = "Indica a sequencia de apresentação deste campo perante o formulario.")
    private Integer ordemApresentacao;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.EXPRESSAO_INTERFACE)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.EXPRESSAO_INTERFACE, required = false, value = "Determina uma expressão a ser utilizada pela interface para definir a condição para o campo ser apresentado ou ocultado no formulario.")
    private String expressaoInterface;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_MODO_PARTILHA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_MODO_PARTILHA, required = false, value = "Atributo utilizado para indicar a forma de partilha da informação de um atributo quando comparado com o campo do SICLI que representa o nome da mãe indicando qual parte da informação deve ser enviada para outro atributo, sendo [S] para a SOBRA , ou seja tudo al´m do que foi localizado [L] para enviar ao novo campo o valor localizado")
    private ModoPartilhaEnum modoPartilhaEnum;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.IDENTIFICADOR_ATRIBUTO_PARTILHA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.IDENTIFICADOR_ATRIBUTO_PARTILHA, required = false, value = "Representa a relação com o atributo que deve ser utilizado na partilha da informação quando comparado ao nome da mãe do SICPF na forma indicada no atributo modo_partilha. Para remover o vinculo existente deve ser enviado o valor -1.")
    private Integer identificadorAtributoPartilha;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.IDENTIFICADOR_ESTRATEGIA_PARTILHA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.IDENTIFICADOR_ESTRATEGIA_PARTILHA, required = true, value = "Atributo utilizado para indicar como a partilha do campo deverá ser feita, quando for o caso. Ex: RECEITA_MAE signfica que a informalção do campo deve ser analisada baseado no nome da mãe obtido junto ao cadastro da receita federal.")
    private EstrategiaPartilhaEnum estrategiaPartilhaEnum;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.PERCENTUAL_ALTERACAO_PERMITIDO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.PERCENTUAL_ALTERACAO_PERMITIDO, required = false, value = "Atributo que representa o percentual adicional de alteração permitido sobre o valor recebido do OCR, no formato inteiro. Para definir o atributo como nulo deve ser enviado o valor -1.")
    private Integer percentualAlteracaoPermitido;

    public AtributoExtracaoManutencaoDTO() {
        super();
    }

    public String getNomeAtributoNegocial() {
        return nomeAtributoNegocial;
    }

    public void setNomeAtributoNegocial(String nomeAtributoNegocial) {
        this.nomeAtributoNegocial = nomeAtributoNegocial;
    }

    public String getNomeAtributoDocumento() {
        return nomeAtributoDocumento;
    }

    public void setNomeAtributoDocumento(String nomeAtributoDocumento) {
        this.nomeAtributoDocumento = nomeAtributoDocumento;
    }

    public String getNomeAtributoRetorno() {
        return nomeAtributoRetorno;
    }

    public void setNomeAtributoRetorno(String nomeAtributoRetorno) {
        this.nomeAtributoRetorno = nomeAtributoRetorno;
    }

    public TipoCampoEnum getTipoCampoEnum() {
        return tipoCampoEnum;
    }

    public void setTipoCampoEnum(TipoCampoEnum tipoCampoEnum) {
        this.tipoCampoEnum = tipoCampoEnum;
    }

    public TipoAtributoEnum getTipoAtributoGeral() {
        return tipoAtributoGeral;
    }

    public void setTipoAtributoGeral(TipoAtributoEnum tipoAtributoGeral) {
        this.tipoAtributoGeral = tipoAtributoGeral;
    }

    public Boolean getObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(Boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getNomeAtributoSIECM() {
        return nomeAtributoSIECM;
    }

    public void setNomeAtributoSIECM(String nomeAtributoSIECM) {
        this.nomeAtributoSIECM = nomeAtributoSIECM;
    }

    public TipoAtributoEnum getTipoAtributoSIECM() {
        return tipoAtributoSIECM;
    }

    public void setTipoAtributoSIECM(TipoAtributoEnum tipoAtributoSIECM) {
        this.tipoAtributoSIECM = tipoAtributoSIECM;
    }

    public Boolean getObrigatorioSIECM() {
        return obrigatorioSIECM;
    }

    public void setObrigatorioSIECM(Boolean obrigatorioSIECM) {
        this.obrigatorioSIECM = obrigatorioSIECM;
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

    public TipoAtributoEnum getTipoAtributoSICLI() {
        return tipoAtributoSICLI;
    }

    public void setTipoAtributoSICLI(TipoAtributoEnum tipoAtributoSICLI) {
        this.tipoAtributoSICLI = tipoAtributoSICLI;
    }

    public String getNomeAtributoSICOD() {
        return nomeAtributoSICOD;
    }

    public void setNomeAtributoSICOD(String nomeAtributoSICOD) {
        this.nomeAtributoSICOD = nomeAtributoSICOD;
    }

    public TipoAtributoEnum getTipoAtributoSICOD() {
        return tipoAtributoSICOD;
    }

    public void setTipoAtributoSICOD(TipoAtributoEnum tipoAtributoSICOD) {
        this.tipoAtributoSICOD = tipoAtributoSICOD;
    }

    public SICPFCampoEnum getSicpfCampoEnum() {
        return sicpfCampoEnum;
    }

    public void setSicpfCampoEnum(SICPFCampoEnum sicpfCampoEnum) {
        this.sicpfCampoEnum = sicpfCampoEnum;
    }

    public SICPFModoEnum getSicpfModoEnum() {
        return sicpfModoEnum;
    }

    public void setSicpfModoEnum(SICPFModoEnum sicpfModoEnum) {
        this.sicpfModoEnum = sicpfModoEnum;
    }

    public Boolean getCalculoData() {
        return calculoData;
    }

    public void setCalculoData(Boolean calculoData) {
        this.calculoData = calculoData;
    }

    public Boolean getIdentificadorPessoa() {
        return identificadorPessoa;
    }

    public void setIdentificadorPessoa(Boolean identificadorPessoa) {
        this.identificadorPessoa = identificadorPessoa;
    }

    public String getValorPadrao() {
        return valorPadrao;
    }

    public void setValorPadrao(String valorPadrao) {
        this.valorPadrao = valorPadrao;
    }

    public Boolean getPresenteDocumento() {
        return presenteDocumento;
    }

    public void setPresenteDocumento(Boolean presenteDocumento) {
        this.presenteDocumento = presenteDocumento;
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

    public ModoPartilhaEnum getModoPartilhaEnum() {
        return modoPartilhaEnum;
    }

    public void setModoPartilhaEnum(ModoPartilhaEnum modoPartilhaEnum) {
        this.modoPartilhaEnum = modoPartilhaEnum;
    }

    public Integer getIdentificadorAtributoPartilha() {
        return identificadorAtributoPartilha;
    }

    public void setIdentificadorAtributoPartilha(Integer identificadorAtributoPartilha) {
        this.identificadorAtributoPartilha = identificadorAtributoPartilha;
    }

    public EstrategiaPartilhaEnum getEstrategiaPartilhaEnum() {
        return estrategiaPartilhaEnum;
    }

    public void setEstrategiaPartilhaEnum(EstrategiaPartilhaEnum estrategiaPartilhaEnum) {
        this.estrategiaPartilhaEnum = estrategiaPartilhaEnum;
    }

    public Integer getPercentualAlteracaoPermitido() {
        return percentualAlteracaoPermitido;
    }

    public void setPercentualAlteracaoPermitido(Integer percentualAlteracaoPermitido) {
        this.percentualAlteracaoPermitido = percentualAlteracaoPermitido;
    }
}
