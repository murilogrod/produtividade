package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto;

import br.gov.caixa.pedesgo.arquitetura.adapters.CalendarFullBRAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.SituacaoInstanciaDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Calendar;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = ConstantesNegocioDossieProduto.XML_ROOT_ELEMENT_INSTANCIA_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProduto.API_MODEL_V1_INSTANCIA_DOCUMENTO,
        description = "Objeto utilizado para representar a Instancia de Documento no retorno as consultas realizadas ao dossiê de produto sob a ótica Apoio ao Negocio."
)
public class InstanciaDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProduto.ID, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ID, required = true, value = "Identificador único do instancia do documento.")
    private Long id;

    @XmlElement(name = ConstantesNegocioDossieProduto.ID_ELEMENTO_CONTEUDO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ID_ELEMENTO_CONTEUDO, required = false, value = "Identificador único do elemento de conteudo a qual a instancia de documento foi vinculada.")
    private Long identificadorElementoConteudo;

    @XmlElement(name = ConstantesNegocioDossieProduto.ID_GARANTIA_INFORMADA)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ID_GARANTIA_INFORMADA, required = false, value = "Identificador único da garantia informada a qual a instancia de documento foi vinculada.")
    private Long identificadorGarantiaInformada;

    @XmlElement(name = ConstantesNegocioDossieProduto.ID_DOSSIE_CLIENTE_PRODUTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ID_DOSSIE_CLIENTE_PRODUTO, required = false, value = "Identificador único da relação entre o dossiê cliente e o dossiê de produto a qual a instancia de documento foi vinculada.")
    private Long identificadorDossieClienteProduto;

    @XmlElement(name = ConstantesNegocioDossieProduto.DOCUMENTO, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.DOCUMENTO, required = true, value = "Objeto que representa o registro do documento relacionado com a instancia.")
    private DocumentoDTO documentoDTO;

    //*******************************************
    @XmlElement(name = ConstantesNegocioDossieProduto.SITUACAO_ATUAL)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.SITUACAO_ATUAL, value = "Identificação da situação atual em que se encontra a instância do documento")
    private String situacaoAtual;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProduto.DATA_HORA_SITUACAO_ATUAL)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.DATA_HORA_SITUACAO_ATUAL, value = "Data/Hora de atribuição da situação atual em que se encontra a instância do documento", required = true, example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraSituacaoAtual;
    
    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProduto.DATA_HORA_VINCULACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.DATA_HORA_VINCULACAO, value = "Data/Hora de vinculação do documento ao dossiê de produto. Data e Hora da situação \"CRIADO\"", required = true, example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraVinculacao;


    @XmlElement(name = ConstantesNegocioDossieProduto.SITUACAO_INSTANCIA)
    @XmlElementWrapper(name = ConstantesNegocioDossieProduto.HISTORICO_SITUACOES)
    @JsonProperty(value = ConstantesNegocioDossieProduto.HISTORICO_SITUACOES)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.HISTORICO_SITUACOES, required = true, value = "Lista de situações que compõem o hitorico relacionado a instancia do documento. A lista é ordenada em ordem cronologica pela data de inclusão da situação.")
    private List<SituacaoInstanciaDTO> situacoesInstanciaDTO;

    public InstanciaDocumentoDTO() {
        super();
        this.situacoesInstanciaDTO = new ArrayList<>();
    }

    public InstanciaDocumentoDTO(InstanciaDocumento instanciaDocumento, boolean incluirHistoricoSituacoes) {
        this();
        if (instanciaDocumento != null) {
            this.id = instanciaDocumento.getId();
            if (instanciaDocumento.getDocumento() != null) {
                this.documentoDTO = new DocumentoDTO(instanciaDocumento.getDocumento());
            }
            if (instanciaDocumento.getElementoConteudo() != null) {
                this.identificadorElementoConteudo = instanciaDocumento.getElementoConteudo().getId();
            }
            if (instanciaDocumento.getGarantiaInformada() != null) {
                this.identificadorGarantiaInformada = instanciaDocumento.getGarantiaInformada().getId();
            }
            if (instanciaDocumento.getDossieClienteProduto() != null) {
                this.identificadorDossieClienteProduto = instanciaDocumento.getDossieClienteProduto().getId();
            }
            if (!incluirHistoricoSituacoes) {
                this.situacoesInstanciaDTO = null;
            } else if (instanciaDocumento.getSituacoesInstanciaDocumento() != null) {
                //Captura a primeira situação categorizada como tipo inicial e ordena as mesmas pelo ID
                SituacaoInstanciaDocumento situacaoInicial = instanciaDocumento.getSituacoesInstanciaDocumento().stream()
                        .filter(sid -> sid.getSituacaoDocumento().getSituacaoInicial())
                        .sorted(Comparator.comparing(SituacaoInstanciaDocumento::getId))
                        .findFirst().orElse(null);
                if (situacaoInicial != null) {
                    this.dataHoraVinculacao = situacaoInicial.getDataHoraInclusao();
                }

                SituacaoInstanciaDocumento situacaoAtualInstancia = instanciaDocumento.getSituacoesInstanciaDocumento().stream().max(Comparator.comparing(SituacaoInstanciaDocumento::getId)).get();
                this.situacaoAtual = situacaoAtualInstancia.getSituacaoDocumento().getNome();
                this.dataHoraSituacaoAtual = situacaoAtualInstancia.getDataHoraInclusao();

                instanciaDocumento.getSituacoesInstanciaDocumento().stream().sorted(Comparator.comparing(SituacaoInstanciaDocumento::getId))
                        .forEachOrdered(situacaoInstancia -> this.situacoesInstanciaDTO.add(new SituacaoInstanciaDTO(situacaoInstancia)));
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdentificadorElementoConteudo() {
        return identificadorElementoConteudo;
    }

    public void setIdentificadorElementoConteudo(Long identificadorElementoConteudo) {
        this.identificadorElementoConteudo = identificadorElementoConteudo;
    }

    public Long getIdentificadorGarantiaInformada() {
        return identificadorGarantiaInformada;
    }

    public void setIdentificadorGarantiaInformada(Long identificadorGarantiaInformada) {
        this.identificadorGarantiaInformada = identificadorGarantiaInformada;
    }

    public Long getIdentificadorDossieClienteProduto() {
        return identificadorDossieClienteProduto;
    }

    public void setIdentificadorDossieClienteProduto(Long identificadorDossieClienteProduto) {
        this.identificadorDossieClienteProduto = identificadorDossieClienteProduto;
    }

    public DocumentoDTO getDocumentoDTO() {
        return documentoDTO;
    }

    public void setDocumentoDTO(DocumentoDTO documentoDTO) {
        this.documentoDTO = documentoDTO;
    }

    public String getSituacaoAtual() {
        return situacaoAtual;
    }

    public void setSituacaoAtual(String situacaoAtual) {
        this.situacaoAtual = situacaoAtual;
    }

    public Calendar getDataHoraSituacaoAtual() {
        return dataHoraSituacaoAtual;
    }

    public void setDataHoraSituacaoAtual(Calendar dataHoraSituacaoAtual) {
        this.dataHoraSituacaoAtual = dataHoraSituacaoAtual;
    }

    public Calendar getDataHoraVinculacao() {
        return dataHoraVinculacao;
    }

    public void setDataHoraVinculacao(Calendar dataHoraVinculacao) {
        this.dataHoraVinculacao = dataHoraVinculacao;
    }

    public List<SituacaoInstanciaDTO> getSituacoesInstanciaDTO() {
        return situacoesInstanciaDTO;
    }

    public void setSituacoesInstanciaDTO(List<SituacaoInstanciaDTO> situacoesInstanciaDTO) {
        this.situacoesInstanciaDTO = situacoesInstanciaDTO;
    }

}
