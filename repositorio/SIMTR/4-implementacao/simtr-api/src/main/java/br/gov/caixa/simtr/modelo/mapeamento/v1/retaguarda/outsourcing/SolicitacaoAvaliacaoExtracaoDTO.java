package br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.outsourcing;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.enumerator.JanelaTemporalExtracaoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesRetaguardaAvaliacaoDocumento;
import java.util.ArrayList;

@XmlRootElement(name = ConstantesRetaguardaAvaliacaoDocumento.XML_ROOT_ELEMENT_SOLICITACAO_AVALIACAO_EXTRACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesRetaguardaAvaliacaoDocumento.API_MODEL_V1_SOLICITACAO_AVALIACAO_EXTRACAO,
        description = "Objeto utilizado para representar o Documento submetido a classificação, extração de dados e avaliação avaliação documental sob a otica dos serviços de retaguarda."
)
public class SolicitacaoAvaliacaoExtracaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.TIPO_DOCUMENTO, required = false)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.TIPO_DOCUMENTO, value = "Tipologia do documento submetido. Caso a tipologia não seja informada o serviço será solicitado incluindo a atividade de classificação documental.", required = false, example = "0001000100020007")
    private String tipoDocumento;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.ORIGEM_DOCUMENTO, required = true)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.ORIGEM_DOCUMENTO, value = "Identificador do tipo de midia origem do documento digitalizado.", required = true, example = "O")
    private OrigemDocumentoEnum origemDocumentoEnum;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.EXECUTA_EXTRACAO, required = true)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.EXECUTA_EXTRACAO, value = "Indicador de execução da atividade de extração de dados.", required = true)
    private boolean executaExtracao;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.JANELA_TEMPORAL, required = false)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.JANELA_TEMPORAL, value = "Indicador de janela temporal desejada para execução da atividade de extração de dados. Obrigatorio nos casos de indicativo positivo para execução da extração de dados.", required = false, example = "M30")
    private JanelaTemporalExtracaoEnum janelaTemporalExtracaoEnum;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.EXECUTA_AUTENTICIDADE, required = true)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.EXECUTA_AUTENTICIDADE, value = "Indicador de execução da atividade de avaliação de autenticidade.", required = true)
    private boolean executaAutenticidade;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.DADOS, required = false)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.DADOS, value = "Conteudo binario do documento em formato base64.", required = false)
    private List<DadoBaseDTO> dados;
    
    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.MIMETYPE, required = true)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.MIMETYPE, value = "Mimetype definido para o binario encaminhado.", required = true, example = "image/jpg")
    private String mimetype;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.BINARIO, required = true)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.BINARIO, value = "Conteudo binario do documento em formato base64.", required = true, example = "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==")
    private String binario;
    
    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.BINARIOS_COMPLEMENTARES, required = true)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.BINARIOS_COMPLEMENTARES, value = "Conteudos complementares ao binario do documento em formato base64 que devem ser do mesmo mimetype.", required = false, example = "[iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==]")
    private List<String> binariosComplementares;
    
    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.SELFIES, required = true)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.SELFIES, value = "Conteudos binarios em formato base64 que representam selfies do cliente utilizados em processos específicos.", required = false, example = "[iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==]")
    private List<String> selfies;

    public SolicitacaoAvaliacaoExtracaoDTO() {
        super();
        this.dados = new ArrayList<>();
        this.binariosComplementares = new ArrayList<>();
        this.selfies = new ArrayList<>();
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public OrigemDocumentoEnum getOrigemDocumentoEnum() {
        return origemDocumentoEnum;
    }

    public void setOrigemDocumentoEnum(OrigemDocumentoEnum origemDocumentoEnum) {
        this.origemDocumentoEnum = origemDocumentoEnum;
    }

    public boolean isExecutaExtracao() {
        return executaExtracao;
    }

    public void setExecutaExtracao(boolean executaExtracao) {
        this.executaExtracao = executaExtracao;
    }

    public JanelaTemporalExtracaoEnum getJanelaTemporalExtracaoEnum() {
        return janelaTemporalExtracaoEnum;
    }

    public void setJanelaTemporalExtracaoEnum(JanelaTemporalExtracaoEnum janelaTemporalExtracaoEnum) {
        this.janelaTemporalExtracaoEnum = janelaTemporalExtracaoEnum;
    }

    public boolean isExecutaAutenticidade() {
        return executaAutenticidade;
    }

    public void setExecutaAutenticidade(boolean executaAutenticidade) {
        this.executaAutenticidade = executaAutenticidade;
    }
    
    public List<DadoBaseDTO> getDados() {
        return dados;
    }

    public void setDados(List<DadoBaseDTO> dados) {
        this.dados = dados;
    }

    public String getBinario() {
        return binario;
    }

    public void setBinario(String binario) {
        this.binario = binario;
    }

    public List<String> getBinariosComplementares() {
        return binariosComplementares;
    }

    public void setBinariosComplementares(List<String> binariosComplementares) {
        this.binariosComplementares = binariosComplementares;
    }

    public List<String> getSelfies() {
        return selfies;
    }

    public void setSelfies(List<String> selfies) {
        this.selfies = selfies;
    }
}
