package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento;

import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import java.io.Serializable;

import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiModel(
        value = ConstantesCadastroTipoDocumento.API_MODEL_TIPO_DOCUMENTO_NOVO,
        description = "Objeto utilizado para representar um novo tipo de documento no envio de inclusão sob a ótica dos cadastros"
)
public class TipoDocumentoNovoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME, required = true, value = "Atributo que identifica o tipo de documento vinculado")
    private String nome;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_TIPO_PESSOA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_TIPO_PESSOA, required = false, value = "Atributo que determina qual tipo de pessoa pode ter o documento atribuido. Quando atributo possui o valor nulo indica que trata-se de um documento de produto/serviço, não relacionado a uma pessoa")
    private TipoPessoaEnum tipoPessoa;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_VALIDADE_AUTOCONTIDA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_VALIDADE_AUTOCONTIDA, required = true, value = "Atributo determina se a validade do documento esta definida no proprio documento ou não como por exemplo no caso de certidões que possuem a validade determinada em seu corpo. Caso o valor deste atributo seja falso, o prazo de validade deve ser calculado conforme definido no atributo de prazo de validade")
    private boolean validadeAutoContida;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.PRAZO_VALIDADE_DIAS)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.PRAZO_VALIDADE_DIAS, required = false, value = "Atributo que indica a quantidade de dias para atribuição da validade do documento a partir da sua emissão. Caso o valor deste atributo não esteja definido, significa que o documento possui um prazo de validade indeterminado")
    private Integer prazoValidadeDias;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.CODIGO_TIPOLOGIA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.CODIGO_TIPOLOGIA, required = false, value = "Atributo utilizado para armazenar o codigo da tipologia documental corporativa")
    private String codigoTipologia;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.CLASSE_SIECM)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.CLASSE_SIECM, required = false, value = "Atributo utilizado para armazenar o nome da classe no GED que deve ser utilizada para armazenar o documento junto ao SIECM")
    private String classeSiecm;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_REUSO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_REUSO, required = true, value = "Atributo utilizado para identificar se deve ser aplicado reuso ou não na carga do documento")
    private boolean indicadorReuso;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_APOIO_NEGOCIO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_APOIO_NEGOCIO, required = true, value = "Atributo utilizado para identificar se o tipo de documento faz utilização perante o Apoio ao Negocio")
    private boolean indicadorUsoApoioNegocio;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_DOSSIE_DIGITAL)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_DOSSIE_DIGITAL, required = true, value = "Atributo utilizado para identificar se o tipo de documento faz utilização perante o Dossiê Digital")
    private boolean indicadorUsoDossieDigital;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_PROCESSO_ADMINISTRATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_PROCESSO_ADMINISTRATIVO, required = true, value = "Atributo utilizado para identificar se o tipo de documento faz utilização perante o Processo Administrativo Eletronico (PAE)")
    private boolean indicadorUsoProcessoAdministrativo;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.ARQUIVO_MINUTA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ARQUIVO_MINUTA, required = false, value = "Atributo utilizado para indicar o nome do arquivo utilizado pelo gerador de relatorio na emissão da minuta do documento")
    private String nomeArquivoMinuta;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_CADASTRAL)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_CADASTRAL, required = true, value = "Indica se o documento pode ser enviado a avaliação de validade cadastral. Dados do documento")
    private boolean indicadorValidacaoCadastral;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_DOCUMENTAL)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_DOCUMENTAL, required = true, value = "Indica se o documento pode ser enviado a avaliação de validade documental. Estrutura do documento")
    private boolean indicadorValidacaoDocumental;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_SICOD)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_SICOD, required = true, value = "Indica se o documento pode ser enviado a avaliação de validade documental com o SICOD.")
    private boolean indicadorValidacaoSicod;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_EXTRACAO_EXTERNA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_EXTRACAO_EXTERNA, required = true, value = "Indica se o documento deve ser encaminhado para extração de dados junto a empresa de prestação de serviço externa")
    private boolean indicadorExtracaoExterna;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_EXTRACAO_M0)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_EXTRACAO_M0, required = true, value = "Indica se o documento pode ser encaminhado para extração em janela on-line (M0)")
    private boolean indicadorExtracaoM0;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_MULTIPLOS)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_MULTIPLOS, required = true, value = "Indica se o documento pode ser possuir multiplos registros validos para utilização em novos negócios")
    private boolean indicadorMultiplos;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.AVATAR)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.AVATAR, required = true, value = "Atributo utilizado para armazenar a referência do icone utilizado como avatar do tipo de documento nas caixas da fila de extração de dados.", example = "glyphicon glyphicon-picture")
    private String avatar;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.COR_BOX)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.COR_BOX, required = true, value = "Atributo utilizado para armazenar a cor RGB em padrão hexadecimal utilizado pelo tipo de documento nas caixas da fila de extração de dados.", example = "#FFCC00")
    private String corRGB;
    
    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_GUARDA_BINARIO_OUTSOURCING)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_GUARDA_BINARIO_OUTSOURCING, required = true, value = "Atributo determina se o binário do documento encaminhado para atendimento ao serviço outsourcing documental deve ser armazenado no SIECM.")
    private Boolean indicadorGuardaBinarioOutsourcing;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATIVO, value = "Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema.")    
    private Boolean ativo;
    
//****************************************************
    @JsonProperty(ConstantesCadastroTipoDocumento.TAGS)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.TAGS, required = false, value = "Atributo utilizado para realizar a vinculação de TAGS pre definidaas ao tipo documental. Essas Tasgs devem ser sepradas por \";\" (ponto e virgula) serão utilizadas para localizar documentos que estejam associados ao tipo que tenha relação com TAG pesquisada")
    private List<String> tags;
    
    @JsonProperty(ConstantesCadastroTipoDocumento.FUNCOES_DOCUMENTO_INCLUSAO_VINCULO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.FUNCOES_DOCUMENTO_INCLUSAO_VINCULO, required = false, value = "Lista de identificadores de funções documentais a serem vinculados ao tipo de documento")
    private List<Integer> funcoesDocumentoInclusaoVinculo;

    public TipoDocumentoNovoDTO() {
        super();
        this.tags = new ArrayList<>();
        this.funcoesDocumentoInclusaoVinculo = new ArrayList<>();
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

    public boolean isValidadeAutoContida() {
        return validadeAutoContida;
    }

    public void setValidadeAutoContida(boolean validadeAutoContida) {
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

    public boolean isIndicadorReuso() {
        return indicadorReuso;
    }

    public void setIndicadorReuso(boolean indicadorReuso) {
        this.indicadorReuso = indicadorReuso;
    }

    public boolean isIndicadorUsoApoioNegocio() {
        return indicadorUsoApoioNegocio;
    }

    public void setIndicadorUsoApoioNegocio(boolean indicadorUsoApoioNegocio) {
        this.indicadorUsoApoioNegocio = indicadorUsoApoioNegocio;
    }

    public boolean isIndicadorUsoDossieDigital() {
        return indicadorUsoDossieDigital;
    }

    public void setIndicadorUsoDossieDigital(boolean indicadorUsoDossieDigital) {
        this.indicadorUsoDossieDigital = indicadorUsoDossieDigital;
    }

    public boolean isIndicadorUsoProcessoAdministrativo() {
        return indicadorUsoProcessoAdministrativo;
    }

    public void setIndicadorUsoProcessoAdministrativo(boolean indicadorUsoProcessoAdministrativo) {
        this.indicadorUsoProcessoAdministrativo = indicadorUsoProcessoAdministrativo;
    }

    public String getNomeArquivoMinuta() {
        return nomeArquivoMinuta;
    }

    public void setNomeArquivoMinuta(String nomeArquivoMinuta) {
        this.nomeArquivoMinuta = nomeArquivoMinuta;
    }

    public boolean isIndicadorValidacaoCadastral() {
        return indicadorValidacaoCadastral;
    }

    public void setIndicadorValidacaoCadastral(boolean indicadorValidacaoCadastral) {
        this.indicadorValidacaoCadastral = indicadorValidacaoCadastral;
    }

    public boolean isIndicadorValidacaoDocumental() {
        return indicadorValidacaoDocumental;
    }

    public void setIndicadorValidacaoDocumental(boolean indicadorValidacaoDocumental) {
        this.indicadorValidacaoDocumental = indicadorValidacaoDocumental;
    }

    public boolean isIndicadorValidacaoSicod() {
        return indicadorValidacaoSicod;
    }

    public void setIndicadorValidacaoSicod(boolean indicadorValidacaoSicod) {
        this.indicadorValidacaoSicod = indicadorValidacaoSicod;
    }

    public boolean isIndicadorExtracaoExterna() {
        return indicadorExtracaoExterna;
    }

    public void setIndicadorExtracaoExterna(boolean indicadorExtracaoExterna) {
        this.indicadorExtracaoExterna = indicadorExtracaoExterna;
    }

    public boolean isIndicadorExtracaoM0() {
        return indicadorExtracaoM0;
    }

    public void setIndicadorExtracaoM0(boolean indicadorExtracaoM0) {
        this.indicadorExtracaoM0 = indicadorExtracaoM0;
    }

    public boolean isIndicadorMultiplos() {
        return indicadorMultiplos;
    }

    public void setIndicadorMultiplos(boolean indicadorMultiplos) {
        this.indicadorMultiplos = indicadorMultiplos;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCorRGB() {
        return corRGB;
    }

    public void setCorRGB(String corRGB) {
        this.corRGB = corRGB;
    }

    public Boolean getIndicadorGuardaBinarioOutsourcing() {
        return indicadorGuardaBinarioOutsourcing;
    }

    public void setIndicadorGuardaBinarioOutsourcing(Boolean guardaBinarioOutsourcing) {
        this.indicadorGuardaBinarioOutsourcing = guardaBinarioOutsourcing;
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

    public List<Integer> getFuncoesDocumentoInclusaoVinculo() {
        return funcoesDocumentoInclusaoVinculo;
    }

    public void setFuncoesDocumentoInclusaoVinculo(List<Integer> funcoesDocumentoInclusaoVinculo) {
        this.funcoesDocumentoInclusaoVinculo = funcoesDocumentoInclusaoVinculo;
    }

    public String getClasseSiecm() {
		return classeSiecm;
	}

	public void setClasseSiecm(String classeSiecm) {
		this.classeSiecm = classeSiecm;
	}

	public TipoDocumento prototype() {
        TipoDocumento tipoDocumento = new TipoDocumento();

        tipoDocumento.setNome(this.nome);
        tipoDocumento.setTipoPessoaEnum(this.tipoPessoa);
        tipoDocumento.setValidadeAutoContida(this.validadeAutoContida);
        tipoDocumento.setPrazoValidade(this.prazoValidadeDias);
        tipoDocumento.setCodigoTipologia(this.codigoTipologia);
        tipoDocumento.setNomeClasseSIECM(this.classeSiecm);
        tipoDocumento.setReuso(this.indicadorReuso);
        tipoDocumento.setUsoApoioNegocio(this.indicadorUsoApoioNegocio);
        tipoDocumento.setUsoDossieDigital(this.indicadorUsoDossieDigital);
        tipoDocumento.setUsoProcessoAdministrativo(this.indicadorUsoProcessoAdministrativo);
        tipoDocumento.setNomeArquivoMinuta(this.nomeArquivoMinuta);
        tipoDocumento.setEnviaAvaliacaoCadastral(this.indicadorValidacaoCadastral);
        tipoDocumento.setEnviaAvaliacaoDocumental(this.indicadorValidacaoDocumental);
        tipoDocumento.setEnviaAvaliacaoSICOD(this.indicadorValidacaoSicod);
        tipoDocumento.setEnviaExtracaoExterna(this.indicadorExtracaoExterna);
        tipoDocumento.setPermiteExtracaoM0(this.indicadorExtracaoM0);
        tipoDocumento.setPermiteMultiplosAtivos(this.indicadorMultiplos);
        tipoDocumento.setAvatar(this.avatar);
        tipoDocumento.setCorRGB(this.corRGB);
        tipoDocumento.setGuardaBinarioOutsourcing(this.indicadorGuardaBinarioOutsourcing);
        tipoDocumento.setAtivo(this.ativo);
        
        if (Objects.nonNull(this.tags) && !this.tags.isEmpty()) {
            StringBuilder conteudoTags = new StringBuilder();
            this.tags.forEach(tag -> conteudoTags.append(tag).append(";"));
            int posicao = conteudoTags.lastIndexOf(";");
            if (posicao > 0) {
                conteudoTags.deleteCharAt(posicao);
            }
            tipoDocumento.setTags(conteudoTags.toString());
        }

        return tipoDocumento;
    }
}
