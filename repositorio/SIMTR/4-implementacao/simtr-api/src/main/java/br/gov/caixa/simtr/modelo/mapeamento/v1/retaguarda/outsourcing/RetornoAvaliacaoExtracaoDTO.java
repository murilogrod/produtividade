package br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.outsourcing;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.ControleDocumento;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesRetaguardaAvaliacaoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullBRAdapter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Comparator;

@XmlRootElement(name = ConstantesRetaguardaAvaliacaoDocumento.XML_ROOT_ELEMENT_RETORNO_AVALIACAO_EXTRACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesRetaguardaAvaliacaoDocumento.API_MODEL_V1_RETORNO_AVALIACAO_EXTRACAO,
        description = "Objeto utilizado para representar o resultado de um Documento submetido a classificação, extração de dados e avaliação avaliação documental sob a otica dos serviços públicos."
)
public class RetornoAvaliacaoExtracaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.IDENTIFICADOR_DOCUMENTO, required = true)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.IDENTIFICADOR_DOCUMENTO, required = true, value = "Identificador único do documento recebido no ato da solicitação do serviço.")
    private Long id;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.TIPO_DOCUMENTO, required = true)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.TIPO_DOCUMENTO, required = false, value = "Nome do tipo de documento categorizado.")
    private String codigoTipologia;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.DATA_HORA_CAPTURA)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.DATA_HORA_CAPTURA, required = true, value = "Data/Hora de captura do documento.", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraCaptura;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.MATRICULA_CAPTURA)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.MATRICULA_CAPTURA, required = true, value = "Matricula do usuário responsável pela captura do documento.", example = "c999999")
    private String matricula;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.ORIGEM_DOCUMENTO)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.ORIGEM_DOCUMENTO, required = true, value = "Identificador do tipo de midia origem do documento digitalizado.")
    private OrigemDocumentoEnum origemDocumentoEnum;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.DATA_HORA_ENVIO_EXTRACAO)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.DATA_HORA_ENVIO_EXTRACAO, required = false, value = "Data/Hora de envio do documento para execução do serviço de extração de dados.", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraEnvioExtracao;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.DATA_HORA_RETORNO_EXTRACAO)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.DATA_HORA_RETORNO_EXTRACAO, required = false, value = "Data/Hora de retorno do resultado para execução do serviço de extração de dados.", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraRetornoExtracao;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.DATA_HORA_ENVIO_AUTENTICIDADE)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.DATA_HORA_ENVIO_AUTENTICIDADE, required = false, value = "Data/Hora de envio do documento para execução do serviço de avaliação de autenticidade.", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraEnvioAutenticidade;

    @XmlJavaTypeAdapter(value = CalendarFullBRAdapter.class)
    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.DATA_HORA_RETORNO_AUTENTICIDADE)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.DATA_HORA_RETORNO_AUTENTICIDADE, required = false, value = "Data/Hora de retorno do resultado para execução do serviço de avaliação de autenticidade.", example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataHoraRetornoAutenticidade;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.INDICE_AVALIACAO_AUTENTICIDADE)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.INDICE_AVALIACAO_AUTENTICIDADE, required = false, value = "Indice retornado pelo serviço de avaliação de autenticidade documental.")
    private BigDecimal indiceAvaliacaoAutenticidade;

    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.CODIGO_REJEICAO)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.CODIGO_REJEICAO, required = false, value = "Código de identificação do problema apresentado no documento que impediu a execução do serviço.")
    private String codigoRejeicao;

    // *********************************************
    @XmlElement(name = ConstantesRetaguardaAvaliacaoDocumento.ATRIBUTO)
    @XmlElementWrapper(name = ConstantesRetaguardaAvaliacaoDocumento.ATRIBUTOS)
    @JsonProperty(value = ConstantesRetaguardaAvaliacaoDocumento.ATRIBUTOS)
    @ApiModelProperty(name = ConstantesRetaguardaAvaliacaoDocumento.ATRIBUTOS, required = false, value = "Lista dos atributos extraidos do documento.")
    private List<AtributoDocumentoDTO> atributosDocumentoDTO;

    public RetornoAvaliacaoExtracaoDTO() {
        super();
        this.atributosDocumentoDTO = new ArrayList<>();
    }

    public RetornoAvaliacaoExtracaoDTO(Documento documento) {
        this();
        if (documento != null) {
            this.id = documento.getId();
            if (documento.getTipoDocumento() != null) {
                this.codigoTipologia = documento.getTipoDocumento().getCodigoTipologia().toUpperCase();
            }
            this.dataHoraCaptura = documento.getDataHoraCaptura();
            this.matricula = documento.getResponsavel();
            this.origemDocumentoEnum = documento.getOrigemDocumento();
            if ((documento.getControlesDocumento() != null) && (!documento.getControlesDocumento().isEmpty())) {

                //Verifica se a ultima tentativa de execução do serviço foi rejeitada
                documento.getControlesDocumento().stream()
                        .max(Comparator.comparing(ControleDocumento::getDataHoraEnvio))
                        .ifPresent(cd -> {
                            if (cd.getCodigoRejeicao() != null) {
                                this.codigoRejeicao = cd.getCodigoRejeicao();
                            }
                        });

                //Caso a ultima tentativa de execução não tenha rejeitado o serviço, captura as datas de execução das atividades
                if (this.codigoRejeicao == null) {
                    documento.getControlesDocumento().stream()
                            .filter(cd -> cd.getIndicativoExtracao())
                            .max(Comparator.comparing(ControleDocumento::getDataHoraEnvio))
                            .ifPresent(cd -> {
                                this.dataHoraEnvioExtracao = cd.getDataHoraEnvio();
                                this.dataHoraRetornoExtracao = cd.getDataHoraRetornoExtracao();
                            });

                    documento.getControlesDocumento().stream()
                            .filter(cd -> cd.getIndicativoAvaliacaoAutenticidade())
                            .max(Comparator.comparing(ControleDocumento::getDataHoraEnvio))
                            .ifPresent(cd -> {
                                this.dataHoraEnvioAutenticidade = cd.getDataHoraEnvio();
                                this.dataHoraRetornoAutenticidade = cd.getDataHoraRetornoAvaliacaoAutenticidade();
                                this.indiceAvaliacaoAutenticidade = documento.getIndiceAntifraude();
                            });
                }
            }

            if ((documento.getAtributosDocumento() != null) && (!documento.getAtributosDocumento().isEmpty())) {
                Map<String, AtributoDocumento> atributosDocumento = documento.getAtributosDocumento().stream().collect(Collectors.toMap(ad -> ad.getDescricao(), ad -> ad));
                documento.getTipoDocumento().getAtributosExtracao().forEach(ae -> {
                    AtributoDocumento atributoDocumento = atributosDocumento.get(ae.getNomeAtributoDocumento());
                    if (atributoDocumento != null) {
                        this.atributosDocumentoDTO.add(new AtributoDocumentoDTO(atributoDocumento));
                    }
                });
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoTipologia() {
        return codigoTipologia;
    }

    public void setCodigoTipologia(String codigoTipologia) {
        this.codigoTipologia = codigoTipologia;
    }

    public Calendar getDataHoraCaptura() {
        return dataHoraCaptura;
    }

    public void setDataHoraCaptura(Calendar dataHoraCaptura) {
        this.dataHoraCaptura = dataHoraCaptura;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public OrigemDocumentoEnum getOrigemDocumentoEnum() {
        return origemDocumentoEnum;
    }

    public void setOrigemDocumentoEnum(OrigemDocumentoEnum origemDocumentoEnum) {
        this.origemDocumentoEnum = origemDocumentoEnum;
    }

    public Calendar getDataHoraEnvioExtracao() {
        return dataHoraEnvioExtracao;
    }

    public void setDataHoraEnvioExtracao(Calendar dataHoraEnvioExtracao) {
        this.dataHoraEnvioExtracao = dataHoraEnvioExtracao;
    }

    public Calendar getDataHoraRetornoExtracao() {
        return dataHoraRetornoExtracao;
    }

    public void setDataHoraRetornoExtracao(Calendar dataHoraRetornoExtracao) {
        this.dataHoraRetornoExtracao = dataHoraRetornoExtracao;
    }

    public Calendar getDataHoraEnvioAutenticidade() {
        return dataHoraEnvioAutenticidade;
    }

    public void setDataHoraEnvioAutenticidade(Calendar dataHoraEnvioAutenticidade) {
        this.dataHoraEnvioAutenticidade = dataHoraEnvioAutenticidade;
    }

    public Calendar getDataHoraRetornoAutenticidade() {
        return dataHoraRetornoAutenticidade;
    }

    public void setDataHoraRetornoAutenticidade(Calendar dataHoraRetornoAutenticidade) {
        this.dataHoraRetornoAutenticidade = dataHoraRetornoAutenticidade;
    }

    public BigDecimal getIndiceAvaliacaoAutenticidade() {
        return indiceAvaliacaoAutenticidade;
    }

    public void setIndiceAvaliacaoAutenticidade(BigDecimal indiceAvaliacaoAutenticidade) {
        this.indiceAvaliacaoAutenticidade = indiceAvaliacaoAutenticidade;
    }

    public String getCodigoRejeicao() {
        return codigoRejeicao;
    }

    public void setCodigoRejeicao(String codigoRejeicao) {
        this.codigoRejeicao = codigoRejeicao;
    }

    public List<AtributoDocumentoDTO> getAtributosDocumentoDTO() {
        return atributosDocumentoDTO;
    }

    public void setAtributosDocumentoDTO(List<AtributoDocumentoDTO> atributosDocumentoDTO) {
        this.atributosDocumentoDTO = atributosDocumentoDTO;
    }
}
