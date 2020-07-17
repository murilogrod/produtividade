package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tipologia.tipo;

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
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTipologia;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tipologia.AtributoExtracaoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioTipologia.XML_ROOT_ELEMENT_TIPO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTipologia.VISAO_TIPO_API_MODEL_V1_TIPO_DOCUMENTO,
        description = "Objeto utilizado para representar o Tipo de Documento no contexto do Tipo de Documento sob a visão da sustentação ao negócio."
)
public class TipoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(TipoDocumentoDTO.class.getName());

    @XmlElement(name = ConstantesNegocioTipologia.ID)
    @ApiModelProperty(name = ConstantesNegocioTipologia.ID, required = true, value = "Valor que identifica o Tipo de Documento.")
    private Integer id;

    @XmlElement(name = ConstantesNegocioTipologia.NOME)
    @ApiModelProperty(name = ConstantesNegocioTipologia.NOME, required = true, value = "Valor que reprensenta o nome do Tipo de Documento.")
    private String nome;

    @XmlElement(name = ConstantesNegocioTipologia.TIPO_PESSOA)
    @ApiModelProperty(name = ConstantesNegocioTipologia.TIPO_PESSOA, required = false, value = "Indicador do tipo de pessoa que pode ter o documento vinculado. Em caso de documentos de produto/serviço este atributo será nulo.")
    private TipoPessoaEnum tipoPessoaEnum;

    @XmlElement(name = ConstantesNegocioTipologia.CODIGO_TIPOLOGIA)
    @ApiModelProperty(name = ConstantesNegocioTipologia.CODIGO_TIPOLOGIA, required = false, value = "Valor que identifica o codigo de Tipologia para o Tipo de Documento.")
    private String codigoTipologia;

    @XmlElement(name = ConstantesNegocioTipologia.PERMITE_REUSO)
    @ApiModelProperty(name = ConstantesNegocioTipologia.PERMITE_REUSO, required = true, value = "Valor que indica se o tipo de documento possibilita o reuso.")
    private Boolean permiteReuso;
    
    @XmlElement(name = ConstantesNegocioTipologia.PERMITE_EXTRACAO_EXTERNA)
    @ApiModelProperty(name = ConstantesNegocioTipologia.PERMITE_EXTRACAO_EXTERNA, required = true, value = "Valor que indica se o tipo de documento possibilita a extração de dados por serviço externo.")
    private Boolean permiteExtracaoExterna;
    
    @XmlElement(name = ConstantesNegocioTipologia.PERMITE_AVALIACAO_AUTENTICIDADE)
    @ApiModelProperty(name = ConstantesNegocioTipologia.PERMITE_AVALIACAO_AUTENTICIDADE, required = true, value = "Valor que indica se o tipo de documento possibilita a avaliação de autenticidade por serviço externo.")
    private Boolean permiteAvaliacaoAutenticidade;

    @JsonProperty(value = ConstantesNegocioTipologia.AVATAR)
    @ApiModelProperty(name = ConstantesNegocioTipologia.AVATAR, required = true, value = "Atributo utilizado para armazenar a referência do icone utilizado como avatar do tipo de documento nas caixas da fila de extração de dados.", example = "glyphicon glyphicon-picture")
    private String avatar;

    @JsonProperty(value = ConstantesNegocioTipologia.COR_FUNDO)
    @ApiModelProperty(name = ConstantesNegocioTipologia.COR_FUNDO, required = true, value = "Atributo utilizado para armazenar a cor RGB em padrão hexadecimal utilizado pelo tipo de documento nas caixas da fila de extração de dados.", example = "#FFCC00")
    private String corRGB;
    
    @JsonProperty(value = ConstantesCadastroTipoDocumento.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATIVO, value = "Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema.")    
    private Boolean ativo;        

    //*************************************
    @XmlElement(name = ConstantesNegocioTipologia.FUNCAO_DOCUMENTAL)
    @XmlElementWrapper(name = ConstantesNegocioTipologia.FUNCOES_DOCUMENTAIS)
    @JsonProperty(value = ConstantesNegocioTipologia.FUNCOES_DOCUMENTAIS)
    @ApiModelProperty(name = ConstantesNegocioTipologia.FUNCOES_DOCUMENTAIS, value = "Lista das funções documentais que o documento pode assumir.")
    private List<FuncaoDocumentalDTO> funcoesDocumentoDTO;

    @XmlElement(name = ConstantesNegocioTipologia.ATRIBUTO_DOCUMENTO)
    @XmlElementWrapper(name = ConstantesNegocioTipologia.ATRIBUTOS_DOCUMENTO)
    @JsonProperty(value = ConstantesNegocioTipologia.ATRIBUTOS_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioTipologia.ATRIBUTOS_DOCUMENTO, value = "Lista dos atributos mapeados para o documento.")
    private List<AtributoExtracaoDTO> atributosExtracaoDTO;

    public TipoDocumentoDTO() {
        super();
        this.funcoesDocumentoDTO = new ArrayList<>();
        this.atributosExtracaoDTO = new ArrayList<>();
    }

    public TipoDocumentoDTO(TipoDocumento tipoDocumento) {
        this();
        this.id = tipoDocumento.getId();
        this.nome = tipoDocumento.getNome();
        this.tipoPessoaEnum = tipoDocumento.getTipoPessoaEnum();
        this.codigoTipologia = tipoDocumento.getCodigoTipologia();
        this.permiteReuso = tipoDocumento.getReuso();
        this.permiteExtracaoExterna = tipoDocumento.getEnviaExtracaoExterna();
        this.permiteAvaliacaoAutenticidade = tipoDocumento.getEnviaAvaliacaoDocumental();
        this.avatar = tipoDocumento.getAvatar();
        this.corRGB = tipoDocumento.getCorRGB();
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

        try {
            if (tipoDocumento.getAtributosExtracao() != null) {
                tipoDocumento.getAtributosExtracao()
                        .forEach(atributoExtracao -> this.atributosExtracaoDTO.add(new AtributoExtracaoDTO(atributoExtracao)));
            }
        } catch (RuntimeException re) {
            //Lazy Exception ou atributos não carregados
            LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
            this.atributosExtracaoDTO = null;
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

    public Boolean getPermiteReuso() {
        return permiteReuso;
    }

    public void setPermiteReuso(Boolean permiteReuso) {
        this.permiteReuso = permiteReuso;
    }

    public Boolean getPermiteExtracaoExterna() {
        return permiteExtracaoExterna;
    }

    public void setPermiteExtracaoExterna(Boolean permiteExtracaoExterna) {
        this.permiteExtracaoExterna = permiteExtracaoExterna;
    }

    public Boolean getPermiteAvaliacaoAutenticidade() {
        return permiteAvaliacaoAutenticidade;
    }

    public void setPermiteAvaliacaoAutenticidade(Boolean permiteAvaliacaoAutenticidade) {
        this.permiteAvaliacaoAutenticidade = permiteAvaliacaoAutenticidade;
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

    public List<AtributoExtracaoDTO> getAtributosExtracaoDTO() {
        return atributosExtracaoDTO;
    }

    public void setAtributosExtracaoDTO(List<AtributoExtracaoDTO> atributosExtracaoDTO) {
        this.atributosExtracaoDTO = atributosExtracaoDTO;
    }
}
