package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
          value = ConstantesCadastroTipoDocumento.API_MODEL_TIPO_DOCUMENTO_MANUTENCAO,
          description = "Objeto utilizado para representar um tipo de documento no envio de atualização sob a ótica dos cadastros")
public class TipoDocumentoManutencaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME, required = true, value = "Atributo que identifica o tipo de documento vinculado")
    private String nome;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_TIPO_PESSOA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_TIPO_PESSOA, required = false,
                      value = "Atributo que determina qual tipo de pessoa pode ter o documento atribuido. Quando atributo possui o valor nulo indica que trata-se de um documento de produto/serviço, não relacionado a uma pessoa")
    private TipoPessoaEnum tipoPessoa;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.PRAZO_VALIDADE_DIAS)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.PRAZO_VALIDADE_DIAS, required = false,
                      value = "Atributo que indica a quantidade de dias para atribuição da validade do documento a partir da sua emissão. Para remover o vinculo existente deve ser enviado o valor -1.")
    private Integer prazoValidadeDias;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_VALIDADE_AUTOCONTIDA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_VALIDADE_AUTOCONTIDA, required = true,
                      value = "Atributo determina se a validade do documento esta definida no proprio documento ou não como por exemplo no caso de certidões que possuem a validade determinada em seu corpo. Caso o valor deste atributo seja falso, o prazo de validade deve ser calculado conforme definido no atributo de prazo de validade")
    private Boolean validadeAutoContida;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.CODIGO_TIPOLOGIA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.CODIGO_TIPOLOGIA, required = false,
                      value = "Atributo utilizado para armazenar o codigo da tipologia documental corporativa. Caso seja enviado uma string vazia (\"\") o campo será definido como nulo.")
    private String codigoTipologia;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.CLASSE_SIECM)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.CLASSE_SIECM, required = false,
                      value = "Atributo utilizado para armazenar o nome da classe no GED que deve ser utilizada para armazenar o documento junto ao SIECM. Caso seja enviado uma string vazia (\"\") o campo será definido como nulo.")
    private String nomeClasseSIECM;

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

    @JsonProperty(value = ConstantesCadastroTipoDocumento.ARQUIVO_MINUTA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ARQUIVO_MINUTA, required = false,
                      value = "Atributo utilizado para indicar o nome do arquivo utilizado pelo gerador de relatorio na emissão da minuta do documento. Caso seja enviado uma string vazia (\"\") o campo será definido como nulo.")
    private String nomeArquivoMinuta;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_CADASTRAL)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_CADASTRAL, required = true,
                      value = "Indica se o documento pode ser enviado a avaliação de validade cadastral. Dados do documento")
    private Boolean indicadorValidacaoCadastral;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_DOCUMENTAL)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_DOCUMENTAL, required = true,
                      value = "Indica se o documento pode ser enviado a avaliação de validade documental. Estrutura do documento")
    private Boolean indicadorValidacaoDocumental;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_SICOD)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_VALIDACAO_SICOD, required = true,
                      value = "Indica se o documento pode ser enviado a avaliação de validade documental com o SICOD.")
    private Boolean indicadorValidacaoSicod;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_EXTRACAO_EXTERNA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_EXTRACAO_EXTERNA, required = true,
                      value = "Indica se o documento deve ser encaminhado para extração de dados junto a empresa de prestação de serviço externa")
    private Boolean indicadorExtracaoExterna;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_EXTRACAO_M0)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_EXTRACAO_M0, required = true, value = "Indica se o documento pode ser encaminhado para extração em janela on-line (M0)")
    private Boolean indicadorExtracaoM0;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_MULTIPLOS)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_MULTIPLOS, required = true,
                      value = "Indica se o documento pode ser possuir multiplos registros validos para utilização em novos negócios")
    private Boolean indicadorMultiplos;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.AVATAR)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.AVATAR, required = true,
                      value = "Atributo utilizado para armazenar a referência do icone utilizado como avatar do tipo de documento nas caixas da fila de extração de dados.",
                      example = "glyphicon glyphicon-picture")
    private String avatar;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.COR_BOX)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.COR_BOX, required = true,
                      value = "Atributo utilizado para armazenar a cor RGB em padrão hexadecimal utilizado pelo tipo de documento nas caixas da fila de extração de dados.", example = "#FFCC00")
    private String corRGB;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_GUARDA_BINARIO_OUTSOURCING)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_GUARDA_BINARIO_OUTSOURCING, required = true,
                      value = "Atributo determina se o binário do documento encaminhado para atendimento ao serviço outsourcing documental deve ser armazenado no SIECM.")
    private Boolean indicadorGuardaBinarioOutsourcing;
    
    @JsonProperty(value = ConstantesCadastroTipoDocumento.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATIVO, value = "Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema.")    
    private Boolean ativo;    

    // ****************************************************
    @JsonProperty(ConstantesCadastroTipoDocumento.TAGS)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.TAGS, required = false,
                      value = "Atributo utilizado para realizar a vinculação de TAGS pre definidaas ao tipo documental. Essas Tasgs devem ser sepradas por \";\" (ponto e virgula) serão utilizadas para localizar documentos que estejam associados ao tipo que tenha relação com TAG pesquisada")
    private List<String> tags;

    @JsonProperty(ConstantesCadastroTipoDocumento.FUNCOES_DOCUMENTO_INCLUSAO_VINCULO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.FUNCOES_DOCUMENTO_INCLUSAO_VINCULO, required = false,
                      value = "Lista de identificadores de funções documentais a serem vinculados ao tipo de documento")
    private List<Integer> identificadoresFuncaoDocumentalInclusaoVinculo;

    @JsonProperty(ConstantesCadastroTipoDocumento.FUNCOES_DOCUMENTO_EXCLUSAO_VINCULO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.FUNCOES_DOCUMENTO_EXCLUSAO_VINCULO, required = false,
                      value = "Lista de identificadores de funções documentais a serem desvinculados ao tipo de documento.  Identificadores das funções documentais não localizadas na associação com o tipo de documento indicado serão desconsiderados.")
    private List<Integer> identificadoresFuncaoDocumentalExclusaoVinculo;

    public TipoDocumentoManutencaoDTO() {
        super();
        this.tags = new ArrayList<>();
        this.identificadoresFuncaoDocumentalInclusaoVinculo = new ArrayList<>();
        this.identificadoresFuncaoDocumentalExclusaoVinculo = new ArrayList<>();
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

    public Integer getPrazoValidadeDias() {
        return prazoValidadeDias;
    }

    public void setPrazoValidadeDias(Integer prazoValidadeDias) {
        this.prazoValidadeDias = prazoValidadeDias;
    }

    public Boolean getValidadeAutoContida() {
        return validadeAutoContida;
    }

    public void setValidadeAutoContida(Boolean validadeAutoContida) {
        this.validadeAutoContida = validadeAutoContida;
    }

    public String getCodigoTipologia() {
        return codigoTipologia;
    }

    public void setCodigoTipologia(String codigoTipologia) {
        this.codigoTipologia = codigoTipologia;
    }

    public String getNomeClasseSIECM() {
        return nomeClasseSIECM;
    }

    public void setNomeClasseSIECM(String nomeClasseSIECM) {
        this.nomeClasseSIECM = nomeClasseSIECM;
    }

    public Boolean getIndicadorReuso() {
        return indicadorReuso;
    }

    public void setIndicadorReuso(Boolean indicadorReuso) {
        this.indicadorReuso = indicadorReuso;
    }

    public Boolean getIndicadorUsoApoioNegocio() {
        return indicadorUsoApoioNegocio;
    }

    public void setIndicadorUsoApoioNegocio(Boolean indicadorUsoApoioNegocio) {
        this.indicadorUsoApoioNegocio = indicadorUsoApoioNegocio;
    }

    public Boolean getIndicadorUsoDossieDigital() {
        return indicadorUsoDossieDigital;
    }

    public void setIndicadorUsoDossieDigital(Boolean indicadorUsoDossieDigital) {
        this.indicadorUsoDossieDigital = indicadorUsoDossieDigital;
    }

    public Boolean getIndicadorUsoProcessoAdministrativo() {
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

    public Boolean getIndicadorValidacaoCadastral() {
        return indicadorValidacaoCadastral;
    }

    public void setIndicadorValidacaoCadastral(Boolean indicadorValidacaoCadastral) {
        this.indicadorValidacaoCadastral = indicadorValidacaoCadastral;
    }

    public Boolean getIndicadorValidacaoDocumental() {
        return indicadorValidacaoDocumental;
    }

    public void setIndicadorValidacaoDocumental(Boolean indicadorValidacaoDocumental) {
        this.indicadorValidacaoDocumental = indicadorValidacaoDocumental;
    }

    public Boolean getIndicadorValidacaoSicod() {
        return indicadorValidacaoSicod;
    }

    public void setIndicadorValidacaoSicod(Boolean indicadorValidacaoSicod) {
        this.indicadorValidacaoSicod = indicadorValidacaoSicod;
    }

    public Boolean getIndicadorExtracaoExterna() {
        return indicadorExtracaoExterna;
    }

    public void setIndicadorExtracaoExterna(Boolean indicadorExtracaoExterna) {
        this.indicadorExtracaoExterna = indicadorExtracaoExterna;
    }

    public Boolean getIndicadorExtracaoM0() {
        return indicadorExtracaoM0;
    }

    public void setIndicadorExtracaoM0(Boolean indicadorExtracaoM0) {
        this.indicadorExtracaoM0 = indicadorExtracaoM0;
    }

    public Boolean getIndicadorMultiplos() {
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

    public List<Integer> getIdentificadoresFuncaoDocumentalInclusaoVinculo() {
        return identificadoresFuncaoDocumentalInclusaoVinculo;
    }

    public void setIdentificadoresFuncaoDocumentalInclusaoVinculo(List<Integer> identificadoresFuncaoDocumentalInclusaoVinculo) {
        this.identificadoresFuncaoDocumentalInclusaoVinculo = identificadoresFuncaoDocumentalInclusaoVinculo;
    }

    public List<Integer> getIdentificadoresFuncaoDocumentalExclusaoVinculo() {
        return identificadoresFuncaoDocumentalExclusaoVinculo;
    }

    public void setIdentificadoresFuncaoDocumentalExclusaoVinculo(List<Integer> identificadoresFuncaoDocumentalExclusaoVinculo) {
        this.identificadoresFuncaoDocumentalExclusaoVinculo = identificadoresFuncaoDocumentalExclusaoVinculo;
    }

    public String tagsToCSV() {
        if (Objects.nonNull(this.tags)) {
            StringBuilder conteudoTags = new StringBuilder();
            this.tags.forEach(tag -> conteudoTags.append(tag).append(";"));
            int posicao = conteudoTags.lastIndexOf(";");
            if (posicao > 0) {
                conteudoTags.deleteCharAt(posicao);
            }
            return conteudoTags.toString();
        }
        return null;
    }

    public TipoDocumento prototype() {
        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.setNome(this.nome);
        tipoDocumento.setUsoApoioNegocio(this.indicadorUsoApoioNegocio);
        tipoDocumento.setUsoDossieDigital(this.indicadorUsoDossieDigital);
        tipoDocumento.setUsoProcessoAdministrativo(this.indicadorUsoProcessoAdministrativo);
        tipoDocumento.setTipoPessoaEnum(this.tipoPessoa);
        tipoDocumento.setValidadeAutoContida(this.validadeAutoContida);
        tipoDocumento.setPrazoValidade(this.prazoValidadeDias);
        tipoDocumento.setCodigoTipologia(this.codigoTipologia);
        tipoDocumento.setNomeClasseSIECM(this.nomeClasseSIECM);
        tipoDocumento.setReuso(this.indicadorReuso);
        tipoDocumento.setUsoApoioNegocio(this.indicadorUsoApoioNegocio);
        tipoDocumento.setUsoDossieDigital(this.indicadorUsoDossieDigital);
        tipoDocumento.setUsoProcessoAdministrativo(this.indicadorUsoProcessoAdministrativo);
        tipoDocumento.setNomeArquivoMinuta(this.nomeArquivoMinuta);
        tipoDocumento.setAvatar(this.avatar);
        tipoDocumento.setCorRGB(this.corRGB);
        tipoDocumento.setTags(this.tagsToCSV());
        tipoDocumento.setEnviaAvaliacaoCadastral(this.indicadorValidacaoCadastral);
        tipoDocumento.setEnviaAvaliacaoDocumental(this.indicadorValidacaoDocumental);
        tipoDocumento.setEnviaAvaliacaoSICOD(this.indicadorValidacaoSicod);
        tipoDocumento.setEnviaExtracaoExterna(this.indicadorExtracaoExterna);
        tipoDocumento.setPermiteExtracaoM0(this.indicadorExtracaoM0);
        tipoDocumento.setPermiteMultiplosAtivos(this.indicadorMultiplos);

        return tipoDocumento;
    }

}
