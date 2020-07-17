package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tipologia;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.Dominio;
import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTipologia;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioTipologia.XML_ROOT_ELEMENT_DOMINIO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTipologia.API_MODEL_V1_DOMINIO,
        description = "Objeto utilizado para representar a dominio sob a ótica Apoio ao Negocio."
)
public class DominioDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = ConstantesNegocioTipologia.DOMINIO)
    @ApiModelProperty(name = ConstantesNegocioTipologia.DOMINIO, value = "Atributo utilizado para representar o nome do dominio utilizado para agrupar os valores das opções")
	private String dominio;
	
	@XmlElement(name = ConstantesNegocioTipologia.TIPO_CAMPO)
    @ApiModelProperty(name = ConstantesNegocioTipologia.TIPO_CAMPO, value = "Atributo utilizado para armazenar o tipo de campo de formulario que será gerado")
	private TipoCampoEnum tipoCampo;
	
	@XmlElement(name = ConstantesNegocioTipologia.MULTIPLOS)
    @ApiModelProperty(name = ConstantesNegocioTipologia.MULTIPLOS, value = "Atributo utilizado para indicar se o dominio permite seleção multipla caso a estrutura do campo comporte")
	private Boolean multiplos;
	
	@XmlElement(name = ConstantesNegocioTipologia.OPCOES)
    @ApiModelProperty(name = ConstantesNegocioTipologia.OPCOES, value = "Atributo de opções de dominios utilizados em campos de formulário e de extração de dados de documentos")
	private Set<OpcaoDominioDTO> opcoes;
	
	public DominioDTO() {
		super();
		this.opcoes = new HashSet<>();
	}
	
	public DominioDTO(Dominio entidade) {
		this();
		this.dominio = entidade.getDominio();
		this.tipoCampo = entidade.getTipo();
		this.multiplos = entidade.getMultiplos();
		
		if(entidade.getOpcoesDominio() != null && !entidade.getOpcoesDominio().isEmpty()) {
			entidade.getOpcoesDominio().forEach(opcao -> this.opcoes.add(new OpcaoDominioDTO(opcao.getValue(), opcao.getLabel())));
		}
	}
	
	public String getDominio() {
		return dominio;
	}

	public void setDominio(String dominio) {
		this.dominio = dominio;
	}

	public TipoCampoEnum getTipoCampo() {
		return tipoCampo;
	}

	public void setTipoCampo(TipoCampoEnum tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	public Boolean getMultiplos() {
		return multiplos;
	}

	public void setMultiplos(Boolean multiplos) {
		this.multiplos = multiplos;
	}

	public Set<OpcaoDominioDTO> getOpcoes() {
		return opcoes;
	}

	public void setOpcoes(Set<OpcaoDominioDTO> opcoes) {
		this.opcoes = opcoes;
	}
}
