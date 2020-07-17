package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import java.io.Serializable;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@ApiModel(
          value = ConstantesCadastroTipoDocumento.API_MODEL_TIPO_DOCUMENTO,
          description = "Objeto utilizado para representar um tipo de documento nas consultas realizadas sob a ótica dos cadastros")
public class TipoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.ID)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ID, required = true, value = "Atributo que representa a chave primaria da entidade")
    private Integer id;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME, required = true, value = "Atributo que identifica o tipo de documento vinculado")
    private String nome;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_TIPO_PESSOA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_TIPO_PESSOA, required = false,
                      value = "Atributo que determina qual tipo de pessoa pode ter o documento atribuido. Quando atributo possui o valor nulo indica que trata-se de um documento de produto/serviço, não relacionado a uma pessoa")
    private TipoPessoaEnum tipoPessoa;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_VALIDADE_AUTOCONTIDA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_VALIDADE_AUTOCONTIDA, required = true,
                      value = "Atributo determina se a validade do documento esta definida no proprio documento ou não como por exemplo no caso de certidões que possuem a validade determinada em seu corpo. Caso o valor deste atributo seja falso, o prazo de validade deve ser calculado conforme definido no atributo de prazo de validade")
    private Boolean validadeAutoContida;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = ConstantesCadastroTipoDocumento.PRAZO_VALIDADE_DIAS)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.PRAZO_VALIDADE_DIAS, required = false,
                      value = "Atributo que indica a quantidade de dias para atribuição da validade do documento a partir da sua emissão. Caso o valor deste atributo não esteja definido, significa que o documento possui um prazo de validade indeterminado")
    private Integer prazoValidadeDias;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.CODIGO_TIPOLOGIA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.CODIGO_TIPOLOGIA, required = false, value = "Atributo utilizado para armazenar o codigo da tipologia documental corporativa")
    private String codigoTipologia;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = ConstantesCadastroTipoDocumento.CLASSE_SIECM)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.CLASSE_SIECM, required = false,
                      value = "Atributo utilizado para armazenar o nome da classe no GED que deve ser utilizada para armazenar o documento junto ao SIECM")
    private String nomeClasseGED;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_REUSO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_REUSO, required = true, value = "Atributo utilizado para identificar se deve ser aplicado reuso ou não na carga do documento")
    private Boolean indicadorReuso;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_APOIO_NEGOCIO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_APOIO_NEGOCIO, required = true,
                      value = "Atributo utilizado para identificar se o tipo de documento faz utilização perante o Apoio ao Negocio")
    private Boolean indicadorUsoApoioNegocio;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_DOSSIE_DIGITAL)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_DOSSIE_DIGITAL, required = true,
                      value = "Atributo utilizado para identificar se o tipo de documento faz utilização perante o Dossiê Digital")
    private Boolean indicadorUsoDossieDigital;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_PROCESSO_ADMINISTRATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_PROCESSO_ADMINISTRATIVO, required = true,
                      value = "Atributo utilizado para identificar se o tipo de documento faz utilização perante o Processo Administrativo Eletronico (PAE)")
    private Boolean indicadorUsoProcessoAdministrativo;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = ConstantesCadastroTipoDocumento.ARQUIVO_MINUTA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ARQUIVO_MINUTA, required = false,
                      value = "Atributo utilizado para indicar o nome do arquivo utilizado pelo gerador de relatorio na emissão da minuta do documento")
    private String nomeArquivoMinuta;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_CADASTRAL)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_CADASTRAL, required = true,
                      value = "Indica se o documento pode ser enviado a avaliação de validade cadastral. Dados do documento")
    private Boolean indicadorValidacaoCadastral;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_DOCUMENTAL)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_DOCUMENTAL, required = true,
                      value = "Indica se o documento pode ser enviado a avaliação de validade documental. Estrutura do documento")
    private Boolean indicadorValidacaoDocumental;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_SICOD)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_SICOD, required = true,
                      value = "Indica se o documento pode ser enviado a avaliação de validade documental com o SICOD.")
    private Boolean indicadorValidacaoSicod;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_EXTRACAO_EXTERNA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_EXTRACAO_EXTERNA, required = true,
                      value = "Indica se o documento deve ser encaminhado para extração de dados junto a empresa de prestação de serviço externa")
    private Boolean indicadorExtracaoExterna;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_EXTRACAO_M0)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_EXTRACAO_M0, required = true, value = "Indica se o documento pode ser encaminhado para extração em janela on-line (M0)")
    private Boolean indicadorExtracaoM0;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_MULTIPLOS)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_MULTIPLOS, required = true,
                      value = "Indica se o documento pode ser possuir multiplos registros validos para utilização em novos negócios")
    private Boolean indicadorMultiplos;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_GUARDA_BINARIO_OUTSOURCING)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_GUARDA_BINARIO_OUTSOURCING, required = true,
                      value = "Indica se o documento pode ser possuir multiplos registros validos para utilização em novos negócios")
    private Boolean indicadorGuardaOutsourcing;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = ConstantesCadastroTipoDocumento.AVATAR)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.AVATAR, required = true,
                      value = "Atributo utilizado para armazenar a referência do icone utilizado como avatar do tipo de documento nas caixas da fila de extração de dados.",
                      example = "glyphicon glyphicon-picture")
    private String avatar;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(value = ConstantesCadastroTipoDocumento.COR_BOX)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.COR_BOX, required = true,
                      value = "Atributo utilizado para armazenar a cor RGB em padrão hexadecimal utilizado pelo tipo de documento nas caixas da fila de extração de dados.", example = "#FFCC00")
    private String corBox;

    @JsonInclude(Include.NON_NULL)
    @XmlJavaTypeAdapter(CalendarFullBRAdapter.class)
    @JsonProperty(value = ConstantesCadastroTipoDocumento.DATA_HORA_ULTIMA_ALTERACAO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.DATA_HORA_ULTIMA_ALTERACAO, required = true,
                      value = "Atributo utilizado para armazenar a data e hora da última alteração realizada no registro")
    private Calendar dataHoraUltimaAlteracao;
    
    @JsonProperty(value = ConstantesCadastroTipoDocumento.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATIVO, value = "Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema.")    
    private Boolean ativo;    

    // ****************************************************
    @JsonInclude(Include.NON_NULL)
    @JsonProperty(ConstantesCadastroTipoDocumento.TAGS)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.TAGS, required = false,
                      value = "Atributo utilizado para realizar a vinculação de TAGS pre definidaas ao tipo documental. Essas Tasgs devem ser sepradas por \";\" (ponto e virgula) serão utilizadas para localizar documentos que estejam associados ao tipo que tenha relação com TAG pesquisada")
    private List<String> tags;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(ConstantesCadastroTipoDocumento.FUNCOES_DOCUMENTAIS)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.FUNCOES_DOCUMENTAIS, required = false, value = "Lista de funções documentais associadas ao tipo de documento")
    private List<FuncaoDocumentalDTO> funcoesDocumentaisDTO;

    @JsonInclude(Include.NON_NULL)
    @JsonProperty(ConstantesCadastroTipoDocumento.ATRIBUTOS_EXTRACAO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATRIBUTOS_EXTRACAO, required = false, value = "Lista de atributos previstos (extração), associadas ao tipo de documento")
    private List<AtributoExtracaoDTO> atributosExtracaoDTO;

    public TipoDocumentoDTO() {
        super();
        this.tags = new ArrayList<>();
        this.funcoesDocumentaisDTO = new ArrayList<>();
        this.atributosExtracaoDTO = new ArrayList<>();
    }

    public TipoDocumentoDTO(Integer id, String nome, TipoPessoaEnum tipoPessoa, String codigoTipologia, 
                            Boolean indicadorUsoApoioNegocio, Boolean indicadorUsoDossieDigital, 
                            Boolean indicadorUsoProcessoAdministrativo, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.tipoPessoa = tipoPessoa;
        this.codigoTipologia = codigoTipologia;
        this.indicadorUsoApoioNegocio = indicadorUsoApoioNegocio;
        this.indicadorUsoDossieDigital = indicadorUsoDossieDigital;
        this.indicadorUsoProcessoAdministrativo = indicadorUsoProcessoAdministrativo;
        this.ativo = ativo;
    }

    public TipoDocumentoDTO(TipoDocumento tipoDocumento) {
        this();
        if (Objects.nonNull(tipoDocumento)) {
            this.id = tipoDocumento.getId();
            this.nome = tipoDocumento.getNome();
            this.tipoPessoa = tipoDocumento.getTipoPessoaEnum();
            this.validadeAutoContida = tipoDocumento.getValidadeAutoContida();
            this.prazoValidadeDias = tipoDocumento.getPrazoValidade();
            this.codigoTipologia = tipoDocumento.getCodigoTipologia();
            this.nomeClasseGED = tipoDocumento.getNomeClasseSIECM();
            this.indicadorReuso = tipoDocumento.getReuso();
            this.indicadorUsoApoioNegocio = tipoDocumento.getUsoApoioNegocio();
            this.indicadorUsoDossieDigital = tipoDocumento.getUsoDossieDigital();
            this.indicadorUsoProcessoAdministrativo = tipoDocumento.getUsoProcessoAdministrativo();
            this.nomeArquivoMinuta = tipoDocumento.getNomeArquivoMinuta();
            this.indicadorValidacaoCadastral = tipoDocumento.getEnviaAvaliacaoCadastral();
            this.indicadorValidacaoDocumental = tipoDocumento.getEnviaAvaliacaoDocumental();
            this.indicadorValidacaoSicod = tipoDocumento.getEnviaAvaliacaoSICOD();
            this.indicadorExtracaoExterna = tipoDocumento.getEnviaExtracaoExterna();
            this.indicadorExtracaoM0 = tipoDocumento.getPermiteExtracaoM0();
            this.indicadorGuardaOutsourcing = tipoDocumento.getGuardaBinarioOutsourcing();
            this.indicadorMultiplos = tipoDocumento.getPermiteMultiplosAtivos();
            this.avatar = tipoDocumento.getAvatar();
            this.corBox = tipoDocumento.getCorRGB();
            this.dataHoraUltimaAlteracao = tipoDocumento.getDataHoraUltimaAlteracao();
            this.ativo = tipoDocumento.getAtivo();
            
            if (Objects.nonNull(tipoDocumento.getTags()) && !tipoDocumento.getTags().isEmpty()) {
                this.tags.addAll(Arrays.asList(tipoDocumento.getTags().split(";")));
            }

            tipoDocumento.getFuncoesDocumentais().stream()
                         .forEach(funcao -> this.funcoesDocumentaisDTO.add(new FuncaoDocumentalDTO(funcao)));
            tipoDocumento.getAtributosExtracao().stream()
                         .forEach(atributo -> this.atributosExtracaoDTO.add(new AtributoExtracaoDTO(atributo)));

        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoPessoaEnum getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoaEnum tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public Boolean isValidadeAutoContida() {
        return validadeAutoContida;
    }

    public void setValidadeAutoContida(Boolean validadeAutoContida) {
        this.validadeAutoContida = validadeAutoContida;
    }

    public Integer getPrazoValidadeDias() {
        return prazoValidadeDias;
    }

    public void setPrazoValidadeDias(Integer prazoValidadeDias) {
        this.prazoValidadeDias = prazoValidadeDias;
    }

    public String getCodigoTipologia() {
        return codigoTipologia;
    }

    public void setCodigoTipologia(String codigoTipologia) {
        this.codigoTipologia = codigoTipologia;
    }

    public String getNomeClasseGED() {
        return nomeClasseGED;
    }

    public void setNomeClasseGED(String nomeClasseGED) {
        this.nomeClasseGED = nomeClasseGED;
    }

    public Boolean isIndicadorReuso() {
        return indicadorReuso;
    }

    public void setIndicadorReuso(Boolean indicadorReuso) {
        this.indicadorReuso = indicadorReuso;
    }

    public Boolean isIndicadorUsoApoioNegocio() {
        return indicadorUsoApoioNegocio;
    }

    public void setIndicadorUsoApoioNegocio(Boolean indicadorUsoApoioNegocio) {
        this.indicadorUsoApoioNegocio = indicadorUsoApoioNegocio;
    }

    public Boolean isIndicadorUsoDossieDigital() {
        return indicadorUsoDossieDigital;
    }

    public void setIndicadorUsoDossieDigital(Boolean indicadorUsoDossieDigital) {
        this.indicadorUsoDossieDigital = indicadorUsoDossieDigital;
    }

    public Boolean isIndicadorUsoProcessoAdministrativo() {
        return indicadorUsoProcessoAdministrativo;
    }

    public void setIndicadorUsoProcessoAdministrativo(Boolean indicadorUsoProcessoAdministrativo) {
        this.indicadorUsoProcessoAdministrativo = indicadorUsoProcessoAdministrativo;
    }

    public String getNomeArquivoMinuta() {
        return nomeArquivoMinuta;
    }

    public void setNomeArquivoMinuta(String nomeArquivoMinuta) {
        this.nomeArquivoMinuta = nomeArquivoMinuta;
    }

    public Boolean isIndicadorValidacaoCadastral() {
        return indicadorValidacaoCadastral;
    }

    public void setIndicadorValidacaoCadastral(Boolean indicadorValidacaoCadastral) {
        this.indicadorValidacaoCadastral = indicadorValidacaoCadastral;
    }

    public Boolean isIndicadorValidacaoDocumental() {
        return indicadorValidacaoDocumental;
    }

    public void setIndicadorValidacaoDocumental(Boolean indicadorValidacaoDocumental) {
        this.indicadorValidacaoDocumental = indicadorValidacaoDocumental;
    }

    public Boolean isIndicadorValidacaoSicod() {
        return indicadorValidacaoSicod;
    }

    public void setIndicadorValidacaoSicod(Boolean indicadorValidacaoSicod) {
        this.indicadorValidacaoSicod = indicadorValidacaoSicod;
    }

    public Boolean isIndicadorExtracaoExterna() {
        return indicadorExtracaoExterna;
    }

    public void setIndicadorExtracaoExterna(Boolean indicadorExtracaoExterna) {
        this.indicadorExtracaoExterna = indicadorExtracaoExterna;
    }

    public Boolean isIndicadorExtracaoM0() {
        return indicadorExtracaoM0;
    }

    public void setIndicadorExtracaoM0(Boolean indicadorExtracaoM0) {
        this.indicadorExtracaoM0 = indicadorExtracaoM0;
    }

    public Boolean isIndicadorMultiplos() {
        return indicadorMultiplos;
    }

    public void setIndicadorMultiplos(Boolean indicadorMultiplos) {
        this.indicadorMultiplos = indicadorMultiplos;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Calendar getDataHoraUltimaAlteracao() {
        return dataHoraUltimaAlteracao;
    }

    public void setDataHoraUltimaAlteracao(Calendar dataHoraUltimaAlteracao) {
        this.dataHoraUltimaAlteracao = dataHoraUltimaAlteracao;
    }
    
    public Boolean getAtivo() {
		return ativo;
	}
    
    public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCorBox() {
        return corBox;
    }

    public void setCorBox(String corBox) {
        this.corBox = corBox;
    }

    public List<FuncaoDocumentalDTO> getFuncoesDocumentaisDTO() {
        return funcoesDocumentaisDTO;
    }

    public void setFuncoesDocumentaisDTO(List<FuncaoDocumentalDTO> funcoesDocumentaisDTO) {
        this.funcoesDocumentaisDTO = funcoesDocumentaisDTO;
    }

    public List<AtributoExtracaoDTO> getAtributosExtracaoDTO() {
        return atributosExtracaoDTO;
    }

    public void setAtributosExtracaoDTO(List<AtributoExtracaoDTO> atributosExtracaoDTO) {
        this.atributosExtracaoDTO = atributosExtracaoDTO;
    }

    public Boolean isIndicadorGuardaOutsourcing() {
        return indicadorGuardaOutsourcing;
    }

    public void setIndicadorGuardaOutsourcing(Boolean indicadorGuardaOutsourcing) {
        this.indicadorGuardaOutsourcing = indicadorGuardaOutsourcing;
    }
}
