package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.bpm;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.enumerator.CanalCaixaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioBPM;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CGCUnidadeAdapter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioBPM.XML_ROOT_ELEMENT_DOSSIE_PRODUTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioBPM.API_MODEL_V1_DOSSIE_PRODUTO,
        description = "Objeto utilizado para representar o dossiê de produto utilizado na comunicação com o BPM"
)
public class DossieProdutoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesNegocioBPM.ID)
    @ApiModelProperty(name = ConstantesNegocioBPM.ID, required = true, value = "Identificador do dossiê de produto.")
    private String id;

    @JsonProperty(value = ConstantesNegocioBPM.UNIDADE_CRIACAO)
    @ApiModelProperty(name = ConstantesNegocioBPM.UNIDADE_CRIACAO, required = true, value = "CGC de identificação da unidade de criação do dossiê de produto")
    @XmlJavaTypeAdapter(value = CGCUnidadeAdapter.class)
    private String unidadeCriacao;

    @JsonProperty(value = ConstantesNegocioBPM.CANAL_CAIXA)
    @ApiModelProperty(name = ConstantesNegocioBPM.CANAL_CAIXA, required = true, value = "Identificador do canal fisico de origem do dossiê de produto")
    private CanalCaixaEnum canalCaixaEnum;

    @JsonProperty(value = ConstantesNegocioBPM.PROCESSO_NEGOCIO)
    @ApiModelProperty(name = ConstantesNegocioBPM.PROCESSO_NEGOCIO, required = true, value = "Identificador negocial do processo originador do dossiê de produto")
    private Integer processo;

    @JsonProperty(value = ConstantesNegocioBPM.API_KEY)
    @ApiModelProperty(name = ConstantesNegocioBPM.API_KEY, required = true, value = "Chave de comunicação do API Manager que deverá ser utilizado na comunicação com os serviços pelo jBPM")
    private String apiKey;

    @JsonProperty(value = ConstantesNegocioBPM.URL_API)
    @ApiModelProperty(name = ConstantesNegocioBPM.URL_API, required = true, value = "Endereço da API que dispoarou a criação da instancia do processo e que será chamada para consumo de serviços do SIMTR em caso de necessidade do processo")
    private String urlAPI;

    @JsonProperty(value = ConstantesNegocioBPM.URL_SSO)
    @ApiModelProperty(name = ConstantesNegocioBPM.URL_SSO, required = true, value = "Endereço do SSO utilizado pelo ambiente que dispoarou a criação da instancia do processo e que será chamada para gerar tokens necessarios ao consumo de serviços do SIMTR")
    private String urlSSO;

    @JsonProperty(value = ConstantesNegocioBPM.URL_API_MANAGER)
    @ApiModelProperty(name = ConstantesNegocioBPM.URL_API_MANAGER, required = true, value = "Endereço do API Manager utilizado pelo ambiente que dispoarou a criação da instancia do processo e que será chamada para consumir serviços disponibilizados ao SIMTR")
    private String urlAPIManager;

    private DossieProdutoDTO() {
        super();
    }

    public DossieProdutoDTO(DossieProduto dossieProduto, String urlAPI, String urlSSO, String urlAPIManager, String apiKey) {
        this();
        this.urlAPI = urlAPI;
        this.urlSSO = urlSSO;
        this.urlAPIManager = urlAPIManager;
        this.apiKey = apiKey;
        if (dossieProduto != null) {
            this.id = dossieProduto.getId().toString();
            this.unidadeCriacao = dossieProduto.getUnidadeCriacao().toString();
            this.canalCaixaEnum = dossieProduto.getCanal().getCanalCaixa();

            if (dossieProduto.getProcesso() != null) {
                this.processo = dossieProduto.getProcesso().getIdentificadorBPM();
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnidadeCriacao() {
        return unidadeCriacao;
    }

    public void setUnidadeCriacao(String unidadeCriacao) {
        this.unidadeCriacao = unidadeCriacao;
    }

    public CanalCaixaEnum getCanalCaixaEnum() {
        return canalCaixaEnum;
    }

    public void setCanalCaixaEnum(CanalCaixaEnum canalCaixaEnum) {
        this.canalCaixaEnum = canalCaixaEnum;
    }

    public Integer getProcesso() {
        return processo;
    }

    public void setProcesso(Integer processo) {
        this.processo = processo;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getUrlAPIManager() {
        return urlAPIManager;
    }

    public void setUrlAPIManager(String urlAPIManager) {
        this.urlAPIManager = urlAPIManager;
    }

    public String getUrlAPI() {
        return urlAPI;
    }

    public void setUrlAPI(String urlAPI) {
        this.urlAPI = urlAPI;
    }

    public String getUrlSSO() {
        return urlSSO;
    }

    public void setUrlSSO(String urlSSO) {
        this.urlSSO = urlSSO;
    }
}
