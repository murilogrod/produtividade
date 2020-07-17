package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.canal;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.enumerator.CanalCaixaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroCanal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = ConstantesCadastroCanal.API_MODEL_CANAL_MANUTENCAO,
        description = "Objeto utilizado para representar um canal de comunicação no envio de inclusão ou atualização sob a ótica dos cadastros."
)
public class CanalManutencaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesCadastroCanal.SIGLA_CANAL)
    @ApiModelProperty(name = ConstantesCadastroCanal.SIGLA_CANAL, required = true, value = "Atributo utilizado para identificar a sigla do canal de captura")
    private String siglaCanal;

    @JsonProperty(value = ConstantesCadastroCanal.DESCRICAO_CANAL)
    @ApiModelProperty(name = ConstantesCadastroCanal.DESCRICAO_CANAL, required = true, value = "Atributo utilizado para descrever o canal de captura")
    private String descricaoCanal;

    @JsonProperty(value = ConstantesCadastroCanal.CANAL_CAIXA)
    @ApiModelProperty(name = ConstantesCadastroCanal.CANAL_CAIXA, required = true, value = "Atributo utilizado para indicar o canal CAIXA utilizado para submeter o dossiê")
    private CanalCaixaEnum canalCaixaEnum;

    @JsonProperty(value = ConstantesCadastroCanal.NOME_CLIENTE_SSO)
    @ApiModelProperty(name = ConstantesCadastroCanal.NOME_CLIENTE_SSO, required = true, value = "Atributo utilizado para identificar o nome do identificador de cliente junto a solução de SSO")
    private String nomeClienteSSO;

    @JsonProperty(value = ConstantesCadastroCanal.INDICADOR_EXTRACAO_M0, required = true)
    @ApiModelProperty(name = ConstantesCadastroCanal.INDICADOR_EXTRACAO_M0, required = true, value = "Atributo utilizado para indicar se o canal possui permisão ou não de consumo do serviço de extração na janela M+0")
    private Boolean indicadorJanelaM0;

    @JsonProperty(value = ConstantesCadastroCanal.INDICADOR_EXTRACAO_M30, required = true)
    @ApiModelProperty(name = ConstantesCadastroCanal.INDICADOR_EXTRACAO_M30, required = true, value = "Atributo utilizado para indicar se o canal possui permisão ou não de consumo do serviço de extração na janela M+30")
    private Boolean indicadorJanelaM30;

    @JsonProperty(value = ConstantesCadastroCanal.INDICADOR_EXTRACAO_M60, required = true)
    @ApiModelProperty(name = ConstantesCadastroCanal.INDICADOR_EXTRACAO_M60, required = true, value = "Atributo utilizado para indicar se o canal possui permisão ou não de consumo do serviço de extração na janela M+60")
    private Boolean indicadorJanelaM60;

    @JsonProperty(value = ConstantesCadastroCanal.INDICADOR_AVALIACAO_AUTENTICIDADE, required = true)
    @ApiModelProperty(name = ConstantesCadastroCanal.INDICADOR_AVALIACAO_AUTENTICIDADE, required = true, value = "Atributo utilizado para indicar se o canal possui permisão ou não de consumo do serviço de avaliação de autenticidade documental")
    private Boolean indicadorAvaliacaoAutenticidade;

    @JsonProperty(value = ConstantesCadastroCanal.INDICADOR_OUTORGA_CADASTRO_RECEITA, required = true)
    @ApiModelProperty(name = ConstantesCadastroCanal.INDICADOR_OUTORGA_CADASTRO_RECEITA, required = true, value = "Atrbuto utilizado para indicar que o canal pode consumir os end points relacionados ao cadastro da receita federal")
    private Boolean indicadorOutorgaCadastroReceita;

    @JsonProperty(value = ConstantesCadastroCanal.INDICADOR_OUTORGA_CADASTRO_CAIXA, required = true)
    @ApiModelProperty(name = ConstantesCadastroCanal.INDICADOR_OUTORGA_CADASTRO_CAIXA, required = true, value = "Atrbuto utilizado para indicar que o canal pode consumir os end points relacionados ao cadastro da CAIXA (SICLI)")
    private Boolean indicadorOutorgaCadastroCaixa;

    @JsonProperty(value = ConstantesCadastroCanal.INDICADOR_ATUALIZACAO_DOCUMENTAL, required = true)
    @ApiModelProperty(name = ConstantesCadastroCanal.INDICADOR_ATUALIZACAO_DOCUMENTAL, required = true, value = "Atrbuto utilizado para indicar que o canal pode atualizar as informações de atributos, classificação e avaliação de autenticidade")
    private Boolean indicadorAtualizacaoDocumental;

    @JsonProperty(value = ConstantesCadastroCanal.INDICADOR_OUTORGA_APIMANAGER, required = true)
    @ApiModelProperty(name = ConstantesCadastroCanal.INDICADOR_OUTORGA_APIMANAGER, required = true, value = "Atributo utilizado para indicar se o canal possui permisão ou não de consumo dos serviços do API Manager expostos diretamente na API do SIMTR em um endpoint de ponte.")
    private Boolean indicadorOutorgaApimanager;
    
    @JsonProperty(value = ConstantesCadastroCanal.INDICADOR_OUTORGA_SIRIC, required = true)
    @ApiModelProperty(name = ConstantesCadastroCanal.INDICADOR_OUTORGA_SIRIC, required = true, value = "Atributo utilizado para indicar se o canal possui permisão ou não de consumo dos serviços de consulta junto ao SIRIC.")
    private Boolean indicadorOutorgaSiric;
    
    @JsonProperty(value = ConstantesCadastroCanal.URL_CALLBACK_DOCUMENTO, required = false)
    @ApiModelProperty(name = ConstantesCadastroCanal.URL_CALLBACK_DOCUMENTO, required = false, value = "Atributo utilizado para armazenar a url de calback do Documento consultado por usuários externos.")
    private String urlCallbackDocumento;
    
    @JsonProperty(value = ConstantesCadastroCanal.URL_CALLBACK_DOSSIE, required = false)
    @ApiModelProperty(name = ConstantesCadastroCanal.URL_CALLBACK_DOSSIE, required = false, value = "Atributo utilizado para armazenar a url de calback do Dossie consultado por usuários externos.")
    private String urlCallbackDossie;
    
    @JsonProperty(value = ConstantesCadastroCanal.UNIDADE_CALLBACK_DOSSIE, required = false)
    @ApiModelProperty(name = ConstantesCadastroCanal.UNIDADE_CALLBACK_DOSSIE, required = false, value = "Atributo utilizado para armazenar a url de calback da unidade consultada por usuários externos.")
    private Integer unidadeCallbackDossie;
    
    @JsonProperty(value= ConstantesCadastroCanal.TOKEN, required = false)
    @ApiModelProperty(name = ConstantesCadastroCanal.TOKEN, required = false, value = "Atributo utilizado para armazenar o token de longa duração que permite o acesso ao recurso de criação de canal.")
    private String token;
    
    // *********************************************
    public CanalManutencaoDTO() {
        super();
    }

    public String getSiglaCanal() {
        return siglaCanal;
    }

    public void setSiglaCanal(String siglaCanal) {
        this.siglaCanal = siglaCanal;
    }

    public String getDescricaoCanal() {
        return descricaoCanal;
    }

    public void setDescricaoCanal(String descricaoCanal) {
        this.descricaoCanal = descricaoCanal;
    }

    public CanalCaixaEnum getCanalCaixaEnum() {
        return canalCaixaEnum;
    }

    public void setCanalCaixaEnum(CanalCaixaEnum canalCaixaEnum) {
        this.canalCaixaEnum = canalCaixaEnum;
    }

    public String getNomeClienteSSO() {
        return nomeClienteSSO;
    }

    public void setNomeClienteSSO(String nomeClienteSSO) {
        this.nomeClienteSSO = nomeClienteSSO;
    }

    public Boolean getIndicadorJanelaM0() {
        return indicadorJanelaM0;
    }

    public void setIndicadorJanelaM0(Boolean indicadorJanelaM0) {
        this.indicadorJanelaM0 = indicadorJanelaM0;
    }

    public Boolean getIndicadorJanelaM30() {
        return indicadorJanelaM30;
    }

    public void setIndicadorJanelaM30(Boolean indicadorJanelaM30) {
        this.indicadorJanelaM30 = indicadorJanelaM30;
    }

    public Boolean getIndicadorJanelaM60() {
        return indicadorJanelaM60;
    }

    public void setIndicadorJanelaM60(Boolean indicadorJanelaM60) {
        this.indicadorJanelaM60 = indicadorJanelaM60;
    }

    public Boolean getIndicadorAvaliacaoAutenticidade() {
        return indicadorAvaliacaoAutenticidade;
    }

    public void setIndicadorAvaliacaoAutenticidade(Boolean indicadorAvaliacaoAutenticidade) {
        this.indicadorAvaliacaoAutenticidade = indicadorAvaliacaoAutenticidade;
    }

    public Boolean getIndicadorOutorgaCadastroReceita() {
        return indicadorOutorgaCadastroReceita;
    }

    public void setIndicadorOutorgaCadastroReceita(Boolean indicadorOutorgaCadastroReceita) {
        this.indicadorOutorgaCadastroReceita = indicadorOutorgaCadastroReceita;
    }

    public Boolean getIndicadorOutorgaCadastroCaixa() {
        return indicadorOutorgaCadastroCaixa;
    }

    public void setIndicadorOutorgaCadastroCaixa(Boolean indicadorOutorgaCadastroCaixa) {
        this.indicadorOutorgaCadastroCaixa = indicadorOutorgaCadastroCaixa;
    }

    public Boolean getIndicadorOutorgaApimanager() {
        return indicadorOutorgaApimanager;
    }

    public void setIndicadorOutorgaApimanager(Boolean indicadorOutorgaApimanager) {
        this.indicadorOutorgaApimanager = indicadorOutorgaApimanager;
    }

    public Boolean getIndicadorAtualizacaoDocumental() {
        return indicadorAtualizacaoDocumental;
    }

    public void setIndicadorAtualizacaoDocumental(Boolean indicadorAtualizacaoDocumental) {
        this.indicadorAtualizacaoDocumental = indicadorAtualizacaoDocumental;
    }

    public Boolean getIndicadorOutorgaSiric() {
	return indicadorOutorgaSiric;
    }

    public void setIndicadorOutorgaSiric(Boolean indicadorOutorgaSiric) {
	this.indicadorOutorgaSiric = indicadorOutorgaSiric;
    }

    public String getUrlCallbackDocumento() {
        return urlCallbackDocumento;
    }

    public void setUrlCallbackDocumento(String urlCallbackDocumento) {
        this.urlCallbackDocumento = urlCallbackDocumento;
    }

    public String getUrlCallbackDossie() {
        return urlCallbackDossie;
    }

    public void setUrlCallbackDossie(String urlCallbackDossie) {
        this.urlCallbackDossie = urlCallbackDossie;
    }

    public Integer getUnidadeCallbackDossie() {
        return unidadeCallbackDossie;
    }

    public void setUnidadeCallbackDossie(Integer unidadeCallbackDossie) {
        this.unidadeCallbackDossie = unidadeCallbackDossie;
    }
    
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
