package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo;

import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.Garantia;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioProcesso;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CodigoOperacaoAdapter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlRootElement(name = ConstantesNegocioProcesso.XML_ROOT_ELEMENT_GARANTIA)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesNegocioProcesso.API_MODEL_V1_GARANTIA,
        description = "Objeto utilizado para representar a garantia relacionada com o processo/produto"
)
public class GarantiaDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(GarantiaDTO.class.getName());

    @XmlElement(name = ConstantesNegocioProcesso.ID)
    @ApiModelProperty(name = ConstantesNegocioProcesso.ID, required = true, value = "Identificador único da garantia a ser utilizada")
    private Integer id;

    @XmlElement(name = ConstantesNegocioProcesso.CODIGO_BACEN)
    @ApiModelProperty(name = ConstantesNegocioProcesso.CODIGO_BACEN, required = true, value = "Código de identificação da garantia junto ao BACEN, também utilizado como código corporativo da garantia na CAIXA")
    @XmlJavaTypeAdapter(value = CodigoOperacaoAdapter.class)
    private Integer codigoBacen;

    @XmlElement(name = ConstantesNegocioProcesso.NOME_GARANTIA)
    @ApiModelProperty(name = ConstantesNegocioProcesso.NOME_GARANTIA, required = true, value = "Nome da garantia CAIXA")
    private String garantiaNome;

    @XmlElement(name = ConstantesNegocioProcesso.INDICADOR_FIDEJUSSORIA)
    @ApiModelProperty(name = ConstantesNegocioProcesso.INDICADOR_FIDEJUSSORIA, required = true, value = "Indicador de garantia fidejussoria. Em caso positivo será necessario indicar o dossiê de cliente que realiza o papel de fiduciante para essa garantia")
    private Boolean indicadorFidejussoria;

    //***************************
    @XmlElementWrapper(name = ConstantesNegocioProcesso.CAMPOS_FORMULARIO)
    @XmlElement(name = ConstantesNegocioProcesso.CAMPO_FORMULARIO)
    @JsonProperty(value = ConstantesNegocioProcesso.CAMPOS_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.CAMPOS_FORMULARIO, required = false, value = "Lista de objetos que representam os campos de formulario utilizados na fase do processo.")
    private List<CampoFormularioDTO> camposFormularioDTO;

    @XmlElementWrapper(name = ConstantesNegocioProcesso.FUNCOES_DOCUMENTAIS)
    @XmlElement(name = ConstantesNegocioProcesso.FUNCAO_DOCUMENTAL)
    @JsonProperty(value = ConstantesNegocioProcesso.FUNCOES_DOCUMENTAIS)
    @ApiModelProperty(name = ConstantesNegocioProcesso.FUNCOES_DOCUMENTAIS, required = true, value = "Lista de funções documentais definida para o indicar a documentação necessaria a comprovação da garantia indicada. Qualquer tipo de documento que esteja asociado a essa função poderá ser utilizado para comprovação indicada pelo vinculo")
    private List<FuncaoDocumentalDTO> funcoesDocumentaisDTO;

    @XmlElementWrapper(name = ConstantesNegocioProcesso.TIPOS_DOCUMENTO)
    @XmlElement(name = ConstantesNegocioProcesso.TIPO_DOCUMENTO)
    @JsonProperty(value = ConstantesNegocioProcesso.TIPOS_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.TIPOS_DOCUMENTO, required = true, value = "Lista de tipos de documentos especificos definidos para indicar a documentação necessária a a comprovação da garantia informada. Nesta situação, apenas o documento definido poderá ser utilizado para comprovação do vinculo.")
    private List<TipoDocumentoDTO> tiposDocumentoDTO;

    public GarantiaDTO() {
        super();
        this.camposFormularioDTO = new ArrayList<>();
        this.funcoesDocumentaisDTO = new ArrayList<>();
        this.tiposDocumentoDTO = new ArrayList<>();
    }

    private GarantiaDTO(Garantia garantia) {
        this();
        if (garantia != null) {
            this.id = garantia.getId();
            this.codigoBacen = garantia.getGarantiaBacen();
            this.garantiaNome = garantia.getNome();
            this.indicadorFidejussoria = garantia.getFidejussoria();
        }
    }

    public GarantiaDTO(Garantia garantia, TipoDocumento tipoDocumento, FuncaoDocumental funcaoDocumental, Processo processo) {
        this(garantia);
        if (garantia != null) {
            try {
                if (garantia.getCamposFormulario() != null) {
                    garantia.getCamposFormulario().stream()
                            .filter(cf -> processo.equals(cf.getProcesso()))
                            .sorted(Comparator.comparing(CampoFormulario::getOrdemApresentacao))
                            .forEachOrdered(campoFormulario -> this.camposFormularioDTO.add(new CampoFormularioDTO(campoFormulario)));
                }
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.camposFormularioDTO = null;
            }
        }

        if (tipoDocumento != null) {
            this.tiposDocumentoDTO.add(new TipoDocumentoDTO(tipoDocumento));
        }
        if (funcaoDocumental != null) {
            this.funcoesDocumentaisDTO.add(new FuncaoDocumentalDTO(funcaoDocumental));
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCodigoBacen() {
        return codigoBacen;
    }

    public void setCodigoBacen(Integer codigoBacen) {
        this.codigoBacen = codigoBacen;
    }

    public String getGarantiaNome() {
        return garantiaNome;
    }

    public void setGarantiaNome(String garantiaNome) {
        this.garantiaNome = garantiaNome;
    }

    public Boolean getIndicadorFidejussoria() {
        return indicadorFidejussoria;
    }

    public void setIndicadorFidejussoria(Boolean indicadorFidejussoria) {
        this.indicadorFidejussoria = indicadorFidejussoria;
    }

    public List<FuncaoDocumentalDTO> getFuncoesDocumentaisDTO() {
        return funcoesDocumentaisDTO;
    }

    public void setFuncoesDocumentaisDTO(List<FuncaoDocumentalDTO> funcoesDocumentaisDTO) {
        this.funcoesDocumentaisDTO = funcoesDocumentaisDTO;
    }

    public List<TipoDocumentoDTO> getTiposDocumentoDTO() {
        return tiposDocumentoDTO;
    }

    public void setTiposDocumentoDTO(List<TipoDocumentoDTO> tiposDocumentoDTO) {
        this.tiposDocumentoDTO = tiposDocumentoDTO;
    }

    public void addTipoDocumentoDTO(TipoDocumentoDTO tipoDocumentoDTO) {
        this.tiposDocumentoDTO.add(tipoDocumentoDTO);
    }

    public void addFuncaoDocumentalDTO(FuncaoDocumentalDTO funcaoDocumentalDTO) {
        this.funcoesDocumentaisDTO.add(funcaoDocumentalDTO);
    }
}
