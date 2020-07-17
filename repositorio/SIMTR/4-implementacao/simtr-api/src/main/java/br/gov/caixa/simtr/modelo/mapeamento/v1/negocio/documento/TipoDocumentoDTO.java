package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.documento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDocumento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDocumento.XML_ROOT_ELEMENT_TIPO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDocumento.API_MODEL_V1_TIPO_DOCUMENTO,
        description = "Objeto utilizado para representar o Tipo de Documento no contexto do Tipo de Documento sob a visão da sustentação ao negócio."
)
public class TipoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(TipoDocumentoDTO.class.getName());

    @XmlElement(name = ConstantesNegocioDocumento.ID)
    @ApiModelProperty(name = ConstantesNegocioDocumento.ID, required = true, value = "Valor que identifica o Tipo de Documento.")
    private Integer id;

    @XmlElement(name = ConstantesNegocioDocumento.NOME)
    @ApiModelProperty(name = ConstantesNegocioDocumento.NOME, required = true, value = "Valor que reprensenta o nome do Tipo de Documento.")
    private String nome;

    @XmlElement(name = ConstantesNegocioDocumento.TIPO_PESSOA)
    @ApiModelProperty(name = ConstantesNegocioDocumento.TIPO_PESSOA, required = false, value = "Indicador do tipo de pessoa que pode ter o documento vinculado. Em caso de documentos de produto/serviço este atributo será nulo.")
    private TipoPessoaEnum tipoPessoaEnum;

    @XmlElement(name = ConstantesNegocioDocumento.CODIGO_TIPOLOGIA)
    @ApiModelProperty(name = ConstantesNegocioDocumento.CODIGO_TIPOLOGIA, required = false, value = "Valor que identifica o codigo de Tipologia para o Tipo de Documento.")
    private String codigoTipologia;

    @XmlElement(name = ConstantesNegocioDocumento.PERMITE_REUSO)
    @ApiModelProperty(name = ConstantesNegocioDocumento.PERMITE_REUSO, required = true, value = "Valor que indica se o tipo de documento possibilita o reuso.")
    private Boolean indicacaoReuso;
    
    @XmlElement(name = ConstantesCadastroTipoDocumento.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATIVO, value = "Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema.")    
    private Boolean ativo;  

    // ***********************************
    @XmlElement(name = ConstantesNegocioDocumento.FUNCAO_DOCUMENTAL)
    @XmlElementWrapper(name = ConstantesNegocioDocumento.FUNCOES_DOCUMENTAIS)
    @JsonProperty(value = ConstantesNegocioDocumento.FUNCOES_DOCUMENTAIS)
    @ApiModelProperty(name = ConstantesNegocioDocumento.FUNCOES_DOCUMENTAIS, value = "Lista das funções documentais que o documento pode assumir.")
    private List<FuncaoDocumentalDTO> funcoesDocumentoDTO;
    
    public TipoDocumentoDTO() {
        super();
        this.funcoesDocumentoDTO = new ArrayList<>();
    }

    public TipoDocumentoDTO(TipoDocumento tipoDocumento) {
        this();
        this.id = tipoDocumento.getId();
        this.nome = tipoDocumento.getNome();
        this.tipoPessoaEnum = tipoDocumento.getTipoPessoaEnum();
        this.codigoTipologia = tipoDocumento.getCodigoTipologia();
        this.indicacaoReuso = tipoDocumento.getReuso();
        this.ativo = tipoDocumento.getAtivo();

        try {
            if (tipoDocumento.getFuncoesDocumentais() != null) {
                tipoDocumento.getFuncoesDocumentais()
                        .forEach(funcaoDocumental -> this.funcoesDocumentoDTO.add(new FuncaoDocumentalDTO(funcaoDocumental)));
            }
        } catch (RuntimeException re) {
            //Lazy Exception ou atributos não carregados
            LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
            this.funcoesDocumentoDTO = null;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoPessoaEnum getTipoPessoaEnum() {
        return tipoPessoaEnum;
    }

    public void setTipoPessoaEnum(TipoPessoaEnum tipoPessoaEnum) {
        this.tipoPessoaEnum = tipoPessoaEnum;
    }

    public String getCodigoTipologia() {
        return codigoTipologia;
    }

    public void setCodigoTipologia(String codigoTipologia) {
        this.codigoTipologia = codigoTipologia;
    }

    public Boolean getIndicacaoReuso() {
        return indicacaoReuso;
    }

    public void setIndicacaoReuso(Boolean indicacaoReuso) {
        this.indicacaoReuso = indicacaoReuso;
    }
    
    public Boolean getAtivo() {
		return ativo;
	}
    
    public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

    public List<FuncaoDocumentalDTO> getFuncoesDocumentoDTO() {
        return funcoesDocumentoDTO;
    }

    public void setFuncoesDocumentoDTO(List<FuncaoDocumentalDTO> funcoesDocumentoDTO) {
        this.funcoesDocumentoDTO = funcoesDocumentoDTO;
    }
}
