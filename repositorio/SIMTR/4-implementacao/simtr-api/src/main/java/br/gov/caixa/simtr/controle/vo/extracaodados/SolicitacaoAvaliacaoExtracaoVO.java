package br.gov.caixa.simtr.controle.vo.extracaodados;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.enumerator.JanelaTemporalExtracaoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesRetaguardaAvaliacaoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.outsourcing.SolicitacaoAvaliacaoExtracaoDTO;

@XmlRootElement(name = "solicitacao-avaliacao-extracao")
@XmlAccessorType(XmlAccessType.FIELD)
public class SolicitacaoAvaliacaoExtracaoVO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private static final String PROCESSO_PADRAO = "PADRAO";
    
    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.TIPO_DOCUMENTO, required = false)
    @JsonProperty(ConstantesRetaguardaAvaliacaoDocumento.TIPO_DOCUMENTO)
    private String tipoDocumento;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.EXECUTA_EXTRACAO, required = true)
    @JsonProperty(ConstantesRetaguardaAvaliacaoDocumento.EXECUTA_EXTRACAO)
    private boolean executaExtracao;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.JANELA_TEMPORAL, required = false)
    @JsonProperty(ConstantesRetaguardaAvaliacaoDocumento.JANELA_TEMPORAL)
    private JanelaTemporalExtracaoEnum janelaTemporalExtracaoEnum;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.EXECUTA_AUTENTICIDADE, required = true)
    @JsonProperty(ConstantesRetaguardaAvaliacaoDocumento.EXECUTA_AUTENTICIDADE)
    private boolean executaAutenticidade;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.PROCESSO, required = true)
    @JsonProperty(ConstantesRetaguardaAvaliacaoDocumento.PROCESSO)
    private String processo;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.SELFIES, required = true)
    @JsonProperty(ConstantesRetaguardaAvaliacaoDocumento.SELFIES)
    private List<String> selfies;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.ATRIBUTOS, required = true)
    @JsonProperty(ConstantesRetaguardaAvaliacaoDocumento.ATRIBUTOS)
    private List<AtributoDocumentoVO> atributos;
    
    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.BINARIOS, required = true)
    @JsonProperty(ConstantesRetaguardaAvaliacaoDocumento.BINARIOS)
    private List<String> binarios;

    public SolicitacaoAvaliacaoExtracaoVO() {
        super();
        this.processo = PROCESSO_PADRAO;
        this.selfies = new ArrayList<>();
        this.atributos = new ArrayList<>();
        this.binarios = new ArrayList<>();
    }
    
    public SolicitacaoAvaliacaoExtracaoVO(SolicitacaoAvaliacaoExtracaoDTO solicitacaoAvaliacaoExtracaoDTO) {
        this();
        this.tipoDocumento = solicitacaoAvaliacaoExtracaoDTO.getTipoDocumento();
        this.executaExtracao = solicitacaoAvaliacaoExtracaoDTO.isExecutaExtracao();
        this.janelaTemporalExtracaoEnum = solicitacaoAvaliacaoExtracaoDTO.getJanelaTemporalExtracaoEnum();
        this.executaAutenticidade = solicitacaoAvaliacaoExtracaoDTO.isExecutaAutenticidade();
        if(solicitacaoAvaliacaoExtracaoDTO.getDados() != null) {
            this.atributos = solicitacaoAvaliacaoExtracaoDTO.getDados().stream().map(db -> new AtributoDocumentoVO(db.getChave(), db.getValor())).collect(Collectors.toList());
        }
        if(solicitacaoAvaliacaoExtracaoDTO.getBinario() != null) {
            this.binarios.add(solicitacaoAvaliacaoExtracaoDTO.getBinario());
        }
        if(solicitacaoAvaliacaoExtracaoDTO.getBinariosComplementares() != null) {
            this.binarios.addAll(solicitacaoAvaliacaoExtracaoDTO.getBinariosComplementares());
        }
    }
    
    public SolicitacaoAvaliacaoExtracaoVO(SolicitacaoAvaliacaoExtracaoDTO solicitacaoAvaliacaoExtracaoDTO, String processo) {
        this(solicitacaoAvaliacaoExtracaoDTO);
        this.processo = processo;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
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

    public String getProcesso() {
        return processo;
    }

    public void setProcesso(String processo) {
        this.processo = processo;
    }

    public List<String> getSelfies() {
        return selfies;
    }

    public void setSelfies(List<String> selfies) {
        this.selfies = selfies;
    }

    public List<String> getBinarios() {
        return binarios;
    }

    public void setBinarios(List<String> binarios) {
        this.binarios = binarios;
    }

    public List<AtributoDocumentoVO> getAtributos() {
        return atributos;
    }

    public void setAtributos(List<AtributoDocumentoVO> atributos) {
        this.atributos = atributos;
    }

}
