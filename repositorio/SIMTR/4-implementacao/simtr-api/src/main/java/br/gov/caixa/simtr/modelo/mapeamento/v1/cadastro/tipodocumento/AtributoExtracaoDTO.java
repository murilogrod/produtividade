package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.enumerator.EstrategiaPartilhaEnum;
import br.gov.caixa.simtr.modelo.enumerator.ModoPartilhaEnum;
import br.gov.caixa.simtr.modelo.enumerator.SICPFCampoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SICPFModoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = ConstantesCadastroTipoDocumento.API_MODEL_ATRIBUTO_EXTRACAO,
        description = "Objeto utilizado para representar um atributo previsto (chamado de atributo extração) vinculada a um tipo de documento nas consultas realizadas sob a ótica dos cadastros."
)
public class AtributoExtracaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = Logger.getLogger(AtributoExtracaoDTO.class.getName());

    @JsonProperty(value = ConstantesCadastroTipoDocumento.ID)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ID, required = true, value = "Atributo que representa a chave primaria da entidade.")
    private Integer id;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_NEGOCIAL)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_NEGOCIAL, required = true, value = "Atributo utilizado para armazenar o nome do atributo identificado pelo negocio. Esse valor armazena o nome utilizado no dia a dia e que pode ser apresentado como algum label no sistema quando necessario.")
    private String nomeNegocial;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_RETORNO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_RETORNO, required = false, value = "Atributo utilizado para armazenar o nome do atributo retornado pela rotina/serviço de extração de dados do documento. Esse valor armazena o nome utilizado no campo de retorno da informação")
    private String nomeRetornoExtracao;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_SIECM)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_SIECM, required = false, value = "Atributo utilizado para definir o nome do atributo no Siecm")
    private String nomeSiecm;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_DOCUMENTO, required = true, value = "Atributo utilizado para armazenar o nome do atributo identificado pela integração com outros sistemas")
    private String nomeDocumento;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME_OBJETO_SICLI)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_OBJETO_SICLI, required = false, value = "Atributo utilizado para definir o objeto que agrupa o atributo utilizado na atualização de dados do SICLI. Esse atributo deve ser utilizado em conjunto com o atributo nome_atributo_sicli e tipo_sicli que determina o agrupamento do atributo a ser enviado na mensagem de atualização")
    private String nomeObjetoSICLI;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_SICLI)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_SICLI, required = false, value = "Atributo utilizado para definir o nome do atributo utilizado na atualização de dados do SICLI. Esse atributo deve ser utilizado em conjunto com o atributo nome_objeto_sicli e tipo_atributo_sicli que determina o agrupamento do atributo a ser enviado na mensagem de atualização.")
    private String nomeAtributoSICLI;
    
    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_SICOD)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_SICOD, required = false, value = "Atributo utilizado para definir o nome do atributo no Sicod")
    private String nomeSicod;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATIVO, required = true, value = "Atributo utilizado para definir se o atributo deve ser procurado para captura no objeto de retorno dos dados extaidos do documento.")
    private boolean ativo;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_CALCULO_DATA_VALIDADE)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_CALCULO_DATA_VALIDADE, required = true, value = "Atributo utilizado para indicar o uso do calculo de validade")
    private boolean indicadorCalculoValidade;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_OBRIGATORIO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_OBRIGATORIO, required = true, value = "Atributo utilizado para indicar se esta informação é de captura obrigatorio para o tipo de documento associado")
    private boolean obrigatorio;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_OBRIGATORIO_SIECM)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_OBRIGATORIO_SIECM, required = false, value = "Atributo utilizado para indicar se esta informação junto ao GED tem cunho obrigatorio.")
    private boolean obrigatorioSiecm;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_IDENTIFICADOR_PESSOA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_IDENTIFICADOR_PESSOA, required = true, value = "Atributo utilizado para indicar se o atributo extraido do documento deve ser utilizado como identificador de CPF/CNPJ para associação do documento a um determinado dossiê de cliente")
    private boolean identificadorPessoa;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_CAMPO_COMPARACAO_RECEITA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_CAMPO_COMPARACAO_RECEITA, required = false, value = "Atributo utilizado para indicar qual campo de retorno o SICPF deve ser utilizado para uma possivel comparação.")
    private SICPFCampoEnum sicpfCampoEnum;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_MODO_COMPARACAO_RECEITA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_MODO_COMPARACAO_RECEITA, required = false, value = "Atributo utilizado para indicar a forma de comparação com o campo de retorno o SICPF deve ser utilizado para uma possivel comparação sendo que [E] indica que nome_dccumento deve ter o valor EXATO (igual) ao valor retornado no SICPF e o [P] indica que o valor deve estar contido na informação retornada.")
    private SICPFModoEnum sicpfModoEnum;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.TIPO_CAMPO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.TIPO_CAMPO, required = false, value = "Atributo utilizado para indicar o tipo de campo que deverá ser utilizado pela interface para os casos de captura da informação quando inserida de forma manual")
    private TipoCampoEnum tipoCampoEnum;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_GERAL)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_GERAL, required = false, value = "Atributo utilizado para indicar qual o tipo de atributo definido para ações internas do SIMTR como geração de minutas, ")
    private TipoAtributoEnum tipoAtributoGeral;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SIECM)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SIECM, required = false, value = "Atributo utilizado para indicar qual o tipo de atributo definido junto a classe GED. ")
    private TipoAtributoEnum tipoAtributoSiecm;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SICLI)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SICLI, required = false, value = "Atributo utilizado para indicar como (tipo de dado) este registro deverá ser definido para envio ao SICLI na rotina de atualização de dados.")
    private TipoAtributoEnum tipoAtributoSICLI;
    
    @JsonProperty(value = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SICOD)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO_SICOD, required = false, value = "Atributo utilizado para indicar tipo do atributo a ser utilizado pelo SICOD.")
    private TipoAtributoEnum tipoAtributoSICOD;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.IDENTIFICADOR_ATRIBUTO_PARTILHA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.IDENTIFICADOR_ATRIBUTO_PARTILHA, required = false, value = "Representa a relação com o atributo que deve ser utilizado na partilha da informação quando comparado ao nome da mãe do SICPF na forma indicada no atributo modo_partilha")
    private Integer identificadorAtributoPartilha;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_MODO_PARTILHA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_MODO_PARTILHA, required = false, value = "Atributo utilizado para indicar a forma de partilha da informação de um atributo indicando qual parte da informação deve ser enviada para outro atributo, sendo [S] para a SOBRA , ou seja tudo al´m do que foi localizado [L] para enviar ao novo campo o valor localizado")
    private ModoPartilhaEnum modoPartilhaEnum;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.IDENTIFICADOR_ESTRATEGIA_PARTILHA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.IDENTIFICADOR_ESTRATEGIA_PARTILHA, required = false, value = "Atributo utilizado para indicar como a partilha do campo deverá ser feita, quando for o caso. Ex: RECEITA_MAE signfica que a informalção do campo deve ser analisada baseado no nome da mãe obtido junto ao cadastro da receita federal.")
    private EstrategiaPartilhaEnum estrategiaPartilhaEnum;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_PRESENTE_DOCUMENTO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_PRESENTE_DOCUMENTO, required = true, value = "Atributo utilizado para indicar se a informação pode ser encontrada no documento ou trata-se de um metadado necessario para disponibilização da informação em algum sistema de integração.")
    private Boolean presenteDocumento;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.VALOR_PADRAO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.VALOR_PADRAO, required = false, value = "Atributo utilizado para manter opcionalmente um valor padrão a ser utilizado no envio das informação para as integrações quando não informado ou não capturado no documento.")
    private String valorPadrao;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.GRUPO_ATRIBUTO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.GRUPO_ATRIBUTO, required = false, value = "Indica o grupo ao qual o atributo faz parte. Caso este atributo faça parte de um grupo, todos os demais atributos do grupo deverão ser informados, caso ao menos um deles seja informado.")
    private Integer grupoAtributo;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.ORDEM_APRESENTACAO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ORDEM_APRESENTACAO, required = true, value = "Indica a sequencia de apresentação deste campo perante o formulario.")
    private Integer ordemApresentacao;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.EXPRESSAO_INTERFACE)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.EXPRESSAO_INTERFACE, required = false, value = "Determina uma expressão a ser utilizada pela interface para definir a condição para o campo ser apresentado ou ocultado no formulario.")
    private String expressaoInterface;
    
    @JsonProperty(value = ConstantesCadastroTipoDocumento.ORIENTACAO_PREENCHIMENTO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ORIENTACAO_PREENCHIMENTO, required = false, value = "Determina uma expressão a ser utilizada pela interface para definir a condição para o campo ser apresentado ou ocultado no formulario.")
    private String orientacaoPreenchimento;
    
    @JsonProperty(value = ConstantesCadastroTipoDocumento.OPCOES_ATRIBUTO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.OPCOES_ATRIBUTO, required = false, value = "Lista de atributos previstos (extração), associadas ao tipo de documento")
    private List<OpcaoAtributoDTO> opcoesAtributoDTO;

    public AtributoExtracaoDTO() {
        super();
    }

    public AtributoExtracaoDTO(AtributoExtracao atributoExtracao) {
        this();
        if (Objects.nonNull(atributoExtracao)) {
            this.id = atributoExtracao.getId();
            this.nomeNegocial = atributoExtracao.getNomeNegocial();
            this.nomeRetornoExtracao = atributoExtracao.getNomeAtributoRetorno();
            this.nomeSiecm = atributoExtracao.getNomeAtributoSIECM();
            this.nomeDocumento = atributoExtracao.getNomeAtributoDocumento();
            this.nomeObjetoSICLI = atributoExtracao.getNomeObjetoSICLI();
            this.nomeAtributoSICLI = atributoExtracao.getNomeAtributoSICLI();
            this.ativo = Objects.isNull(atributoExtracao.getAtivo()) ? Boolean.FALSE : atributoExtracao.getAtivo();
            this.indicadorCalculoValidade = Objects.isNull(atributoExtracao.getUtilizadoCalculoValidade()) ? Boolean.FALSE : atributoExtracao.getUtilizadoCalculoValidade();
            this.obrigatorio = Objects.isNull(atributoExtracao.getObrigatorio()) ? Boolean.FALSE : atributoExtracao.getObrigatorio();
            this.obrigatorioSiecm = Objects.isNull(atributoExtracao.getObrigatorioSIECM()) ? Boolean.FALSE : atributoExtracao.getObrigatorioSIECM();
            this.identificadorPessoa = Objects.isNull(atributoExtracao.getUtilizadoIdentificadorPessoa()) ? Boolean.FALSE : atributoExtracao.getUtilizadoIdentificadorPessoa();
            this.estrategiaPartilhaEnum = atributoExtracao.getEstrategiaPartilhaEnum();
            this.presenteDocumento = atributoExtracao.getPresenteDocumento();
            this.valorPadrao = atributoExtracao.getValorPadrao();
            this.grupoAtributo = atributoExtracao.getGrupoAtributo();
            this.ordemApresentacao = atributoExtracao.getOrdemApresentacao();
            this.expressaoInterface = atributoExtracao.getExpressaoInterface();
            this.nomeSicod = atributoExtracao.getNomeAtributoSICOD();
            this.tipoAtributoSICOD = atributoExtracao.getTipoAtributoSicodEnum();
            this.orientacaoPreenchimento = atributoExtracao.getOrientacaoPreenchimento();
            
            this.opcoesAtributoDTO = new ArrayList<>();

            if (Objects.nonNull(atributoExtracao.getAtributoPartilha())) {
                this.identificadorAtributoPartilha = atributoExtracao.getAtributoPartilha().getId();
                this.modoPartilhaEnum = atributoExtracao.getModoPartilhaEnum();
            }
            this.sicpfCampoEnum = atributoExtracao.getSicpfCampoEnum();
            this.sicpfModoEnum = atributoExtracao.getSicpfModoEnum();
            this.tipoCampoEnum = atributoExtracao.getTipoCampoEnum();

            this.tipoAtributoGeral = atributoExtracao.getTipoAtributoGeralEnum();
            this.tipoAtributoSiecm = atributoExtracao.getTipoAtributoSiecmEnum();
            this.tipoAtributoSICLI = atributoExtracao.getTipoAtributoSicliEnum();
            
            try {
                atributoExtracao.getOpcoesAtributo().stream()
                        .forEach(op -> this.opcoesAtributoDTO.add(new OpcaoAtributoDTO(op.getId(), op.getValorOpcao(), op.getDescricaoOpcao())));
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.opcoesAtributoDTO = null;
            }
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

    public String getNomeRetornoExtracao() {
        return nomeRetornoExtracao;
    }

    public void setNomeRetornoExtracao(String nomeRetornoExtracao) {
        this.nomeRetornoExtracao = nomeRetornoExtracao;
    }

    public String getNomeDocumento() {
        return nomeDocumento;
    }

    public void setNomeDocumento(String nomeDocumento) {
        this.nomeDocumento = nomeDocumento;
    }

    public String getNomeObjetoSICLI() {
        return nomeObjetoSICLI;
    }

    public void setNomeObjetoSICLI(String nomeObjetoSICLI) {
        this.nomeObjetoSICLI = nomeObjetoSICLI;
    }

    public String getNomeAtributoSICLI() {
        return nomeAtributoSICLI;
    }

    public void setNomeAtributoSICLI(String nomeAtributoSICLI) {
        this.nomeAtributoSICLI = nomeAtributoSICLI;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public boolean isIdentificadorPessoa() {
        return identificadorPessoa;
    }

    public void setIdentificadorPessoa(boolean identificadorPessoa) {
        this.identificadorPessoa = identificadorPessoa;
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

    public TipoAtributoEnum getTipoAtributoSICLI() {
        return tipoAtributoSICLI;
    }

    public void setTipoAtributoSICLI(TipoAtributoEnum tipoAtributoSICLI) {
        this.tipoAtributoSICLI = tipoAtributoSICLI;
    }

    public Integer getIdentificadorAtributoPartilha() {
        return identificadorAtributoPartilha;
    }

    public void setIdentificadorAtributoPartilha(Integer identificadorAtributoPartilha) {
        this.identificadorAtributoPartilha = identificadorAtributoPartilha;
    }

    public ModoPartilhaEnum getModoPartilhaEnum() {
        return modoPartilhaEnum;
    }

    public void setModoPartilhaEnum(ModoPartilhaEnum modoPartilhaEnum) {
        this.modoPartilhaEnum = modoPartilhaEnum;
    }

    public EstrategiaPartilhaEnum getEstrategiaPartilhaEnum() {
        return estrategiaPartilhaEnum;
    }

    public void setEstrategiaPartilhaEnum(EstrategiaPartilhaEnum estrategiaPartilhaEnum) {
        this.estrategiaPartilhaEnum = estrategiaPartilhaEnum;
    }

    public Boolean getPresenteDocumento() {
        return presenteDocumento;
    }

    public void setPresenteDocumento(Boolean presenteDocumento) {
        this.presenteDocumento = presenteDocumento;
    }

    public String getValorPadrao() {
        return valorPadrao;
    }

    public void setValorPadrao(String valorPadrao) {
        this.valorPadrao = valorPadrao;
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

    public String getNomeSiecm() {
            return nomeSiecm;
    }

    public void setNomeSiecm(String nomeSiecm) {
            this.nomeSiecm = nomeSiecm;
    }

    public String getNomeSicod() {
            return nomeSicod;
    }

    public void setNomeSicod(String nomeSicod) {
            this.nomeSicod = nomeSicod;
    }

    public boolean isIndicadorCalculoValidade() {
            return indicadorCalculoValidade;
    }

    public void setIndicadorCalculoValidade(boolean indicadorCalculoValidade) {
            this.indicadorCalculoValidade = indicadorCalculoValidade;
    }

    public boolean isObrigatorioSiecm() {
            return obrigatorioSiecm;
    }

    public void setObrigatorioSiecm(boolean obrigatorioSiecm) {
            this.obrigatorioSiecm = obrigatorioSiecm;
    }

    public TipoAtributoEnum getTipoAtributoSiecm() {
            return tipoAtributoSiecm;
    }

    public void setTipoAtributoSiecm(TipoAtributoEnum tipoAtributoSiecm) {
            this.tipoAtributoSiecm = tipoAtributoSiecm;
    }

    public TipoAtributoEnum getTipoAtributoSICOD() {
            return tipoAtributoSICOD;
    }

    public void setTipoAtributoSICOD(TipoAtributoEnum tipoAtributoSICOD) {
            this.tipoAtributoSICOD = tipoAtributoSICOD;
    }

    public String getOrientacaoPreenchimento() {
            return orientacaoPreenchimento;
    }

    public void setOrientacaoPreenchimento(String orientacaoPreenchimento) {
            this.orientacaoPreenchimento = orientacaoPreenchimento;
    }

    public List<OpcaoAtributoDTO> getOpcoesAtributoDTO() {
            return opcoesAtributoDTO;
    }

    public void setOpcoesAtributoDTO(List<OpcaoAtributoDTO> opcoesAtributoDTO) {
            this.opcoesAtributoDTO = opcoesAtributoDTO;
    }
}
