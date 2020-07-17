package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.dossiedigital;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import java.io.Serializable;

import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = ConstantesCadastroTipoDocumento.XML_ROOT_ELEMENT_TIPO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesCadastroTipoDocumento.API_MODEL_TIPO_DOCUMENTO__DOSSIEDIGITAL,
        description = "Objeto utilizado para representar um tipo de documento nas consultas realizadas por sistemas terceiros que não compoem a plataforma."
)
public class TipoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesCadastroTipoDocumento.NOME)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME, required = true, value = "Atributo que identifica o tipo de documento vinculado", example = "CNH")
    private String nome;
    
    @XmlElement(name = ConstantesCadastroTipoDocumento.CODIGO_TIPOLOGIA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.CODIGO_TIPOLOGIA, required = true, value = "Atributo que identifica o código de tipologia associado ao tipo de documento", example = "0001000100020007")
    private String codigoTipologia;

    @XmlElement(name = ConstantesCadastroTipoDocumento.INDICADOR_TIPO_PESSOA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_TIPO_PESSOA, required = false, value = "Atributo que determina qual tipo de pessoa pode ter o documento atribuido. Quando atributo possui o valor nulo indica que trata-se de um documento de produto/serviço, não relacionado a uma pessoa", example = "F")
    private TipoPessoaEnum tipoPessoa;

    @XmlElement(name = ConstantesCadastroTipoDocumento.INDICADOR_EMISSAO_MINUTA)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_EMISSAO_MINUTA, required = false, value = "Atributo que determina se o tipo de documento referenciado esta apto para o serviço de emisão de minuta na pataforma.", example = "false")
    private Boolean indicadorMinuta;
    
    @JsonProperty(value = ConstantesCadastroTipoDocumento.ATIVO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATIVO, value = "Atributo que indica que se o tipo documento esta ativo ou não para utilização pelo sistema.")    
    private Boolean ativo;    

    //****************************************************
    @JsonInclude(Include.NON_NULL)
    @JsonProperty(ConstantesCadastroTipoDocumento.ATRIBUTOS_EXTRACAO)
    @XmlElement(name = ConstantesCadastroTipoDocumento.ATRIBUTO_EXTRACAO)
    @XmlElementWrapper(name = ConstantesCadastroTipoDocumento.ATRIBUTOS_EXTRACAO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.ATRIBUTOS_EXTRACAO, required = false, value = "Lista de atributos previstos (extração), associadas ao tipo de documento")
    private List<AtributoExtracaoDTO> atributosExtracaoDTO;

    public TipoDocumentoDTO() {
        super();
        this.atributosExtracaoDTO = new ArrayList<>();
    }

    public TipoDocumentoDTO(TipoDocumento tipoDocumento) {
        this();
        if (tipoDocumento != null) {
            this.nome = tipoDocumento.getNome();
            this.tipoPessoa = tipoDocumento.getTipoPessoaEnum();
            this.codigoTipologia = tipoDocumento.getCodigoTipologia();

            this.indicadorMinuta = (tipoDocumento.getNomeArquivoMinuta() != null) && (tipoDocumento.getNomeArquivoMinuta().isEmpty());
            this.ativo = tipoDocumento.getAtivo();

            if (Objects.nonNull(tipoDocumento.getAtributosExtracao())) {
                tipoDocumento.getAtributosExtracao().stream()
                        .forEach(atributo -> this.atributosExtracaoDTO.add(new AtributoExtracaoDTO(atributo)));
            }
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCodigoTipologia() {
        return codigoTipologia;
    }

    public void setCodigoTipologia(String codigoTipologia) {
        this.codigoTipologia = codigoTipologia;
    }

    public TipoPessoaEnum getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoaEnum tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public Boolean getIndicadorMinuta() {
        return indicadorMinuta;
    }

    public void setIndicadorMinuta(Boolean indicadorMinuta) {
        this.indicadorMinuta = indicadorMinuta;
    }
    
    public Boolean getAtivo() {
		return ativo;
	}
    
    public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

    public List<AtributoExtracaoDTO> getAtributosExtracaoDTO() {
        return atributosExtracaoDTO;
    }

    public void setAtributosExtracaoDTO(List<AtributoExtracaoDTO> atributosExtracaoDTO) {
        this.atributosExtracaoDTO = atributosExtracaoDTO;
    }
}
