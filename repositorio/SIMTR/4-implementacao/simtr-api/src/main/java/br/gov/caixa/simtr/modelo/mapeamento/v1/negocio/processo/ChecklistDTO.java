package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo;

import br.gov.caixa.simtr.modelo.entidade.Checklist;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.VinculacaoChecklist;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioProcesso;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarSimpleBRAdapter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = ConstantesNegocioProcesso.XML_ROOT_ELEMENT_CHECKLIST)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioProcesso.API_MODEL_V1_CHECKLIST,
        description = "Objeto utilizado para representar o checklist vinculado ao processo fase e relacionado ou não a um tipo de documento /função documental"
)
public class ChecklistDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ChecklistDTO.class.getName());

    @XmlElement(name = ConstantesNegocioProcesso.ID)
    @ApiModelProperty(name = ConstantesNegocioProcesso.ID, required = true, value = "Identificador único do checklist vinculado ao processo fase")
    private Integer id;

    @XmlJavaTypeAdapter(value = CalendarSimpleBRAdapter.class)
    @XmlElement(name = ConstantesNegocioProcesso.DATA_REVOGACAO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.DATA_REVOGACAO, required = true, value = "Data limite para apresentação do checklist para um dossiê que ainda não tenha sido vinculado a nenhum checklist definido para a fase. Caso o checklist já esteja associado ao dossiê, o mesmo deverá ser apresentado independente da sua data de revogação", example = "dd/MM/yyyy")
    private Calendar dataRevogacao;

    @XmlElement(name = ConstantesNegocioProcesso.NOME)
    @ApiModelProperty(name = ConstantesNegocioProcesso.NOME, required = true, value = "Nome utilizado para fins de identificação do checklist a apresentação do mesmo como label")
    private String nome;

    @XmlElement(name = ConstantesNegocioProcesso.VERIFICACAO_PREVIA)
    @ApiModelProperty(name = ConstantesNegocioProcesso.VERIFICACAO_PREVIA, required = true, value = "Indica que o checklista trata-se de uma verificação previa e deve ser analisado antes dos demais previstos para a etapa")
    private Boolean verificacaoPrevia;

    @XmlElement(name = ConstantesNegocioProcesso.ORIENTACAO_OPERADOR)
    @ApiModelProperty(name = ConstantesNegocioProcesso.ORIENTACAO_OPERADOR, required = false, value = "Apresenta uma orientação para o operador indicando o que deve ser observado na avaliação do checklist")
    private String orientacaoOperador;

    @XmlElement(name = ConstantesNegocioProcesso.TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.TIPO_DOCUMENTO, required = false, value = "Objeto que representa o tipo de documento associado ao checklist. A presença deste objeto determina que a instância de documento vinculada ao dossiê que ainda não foi avaliada e possui uma tipologia identificada por este objeto, deverá ser avaliada nesta fase baseado na comparação da data atual com a data de revogação")
    private TipoDocumentoDTO tipoDocumentoDTO;

    @XmlElement(name = ConstantesNegocioProcesso.FUNCAO_DOCUMENTAL)
    @ApiModelProperty(name = ConstantesNegocioProcesso.FUNCAO_DOCUMENTAL, required = false, value = "Objeto que representa a função documental associada ao checklist. A presença deste objeto determina que a instância de documento vinculada ao dossiê que ainda não foi avaliada e possui uma tipologia associada a função identificada por este objeto, deverá ser avaliada nesta fase baseado na comparação da data atual com a data de revogação")
    private FuncaoDocumentalSimplesDTO funcaoDocumentalDTO;

    @XmlElementWrapper(name = ConstantesNegocioProcesso.APONTAMENTOS)
    @XmlElement(name = ConstantesNegocioProcesso.APONTAMENTO)
    @JsonProperty(value = ConstantesNegocioProcesso.APONTAMENTOS)
    @ApiModelProperty(name = ConstantesNegocioProcesso.APONTAMENTOS, required = false, value = "Lista de apontamentos a serem verificados pelo operador no tratamento do checklist")
    private List<ApontamentoDTO> apontamentosDTO;

    public ChecklistDTO() {
        super();
        this.apontamentosDTO = new ArrayList<>();
    }

    public ChecklistDTO(VinculacaoChecklist vinculacaoChecklist) {
        this();
        if (Objects.nonNull(vinculacaoChecklist)) {
            this.dataRevogacao = vinculacaoChecklist.getDataRevogacao();
            Checklist checklist = vinculacaoChecklist.getChecklist();
            this.id = checklist.getId();
            this.nome = checklist.getNome();
            this.verificacaoPrevia = checklist.getIndicacaoVerificacaoPrevia();
            this.orientacaoOperador = checklist.getOrientacaoOperador();
            if (Objects.nonNull(vinculacaoChecklist.getTipoDocumento())) {
                this.tipoDocumentoDTO = new TipoDocumentoDTO(vinculacaoChecklist.getTipoDocumento());
            } else if (Objects.nonNull(vinculacaoChecklist.getFuncaoDocumental())) {
                this.funcaoDocumentalDTO = new FuncaoDocumentalSimplesDTO(vinculacaoChecklist.getFuncaoDocumental());
            }
            try {
                checklist.getApontamentos().forEach(a -> this.apontamentosDTO.add(new ApontamentoDTO(a)));
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.apontamentosDTO = null;
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Calendar getDataRevogacao() {
        return dataRevogacao;
    }

    public void setDataRevogacao(Calendar dataRevogacao) {
        this.dataRevogacao = dataRevogacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getVerificacaoPrevia() {
        return verificacaoPrevia;
    }

    public void setVerificacaoPrevia(Boolean verificacaoPrevia) {
        this.verificacaoPrevia = verificacaoPrevia;
    }

    public String getOrientacaoOperador() {
        return orientacaoOperador;
    }

    public void setOrientacaoOperador(String orientacaoOperador) {
        this.orientacaoOperador = orientacaoOperador;
    }

    public TipoDocumentoDTO getTipoDocumentoDTO() {
        return tipoDocumentoDTO;
    }

    public void setTipoDocumentoDTO(TipoDocumentoDTO tipoDocumentoDTO) {
        this.tipoDocumentoDTO = tipoDocumentoDTO;
    }

    public FuncaoDocumentalSimplesDTO getFuncaoDocumentalDTO() {
        return funcaoDocumentalDTO;
    }

    public void setFuncaoDocumentalDTO(FuncaoDocumentalSimplesDTO funcaoDocumentalDTO) {
        this.funcaoDocumentalDTO = funcaoDocumentalDTO;
    }

    public List<ApontamentoDTO> getApontamentosDTO() {
        return apontamentosDTO;
    }

    public void setApontamentosDTO(List<ApontamentoDTO> apontamentosDTO) {
        this.apontamentosDTO = apontamentosDTO;
    }

}
