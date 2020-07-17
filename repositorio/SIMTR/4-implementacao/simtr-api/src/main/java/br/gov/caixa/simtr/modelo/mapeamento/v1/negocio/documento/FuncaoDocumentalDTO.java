package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.documento;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDocumento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDocumento.XML_ROOT_ELEMENT_FUNCAO_DOCUMENTAL)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDocumento.API_MODEL_V1_FUNCAO_DOCUMENTAL,
        description = "Objeto utilizado para representar a Função Documental no contexto do Tipo de Documento sob a visão da sustentação ao negócio."
)
public class FuncaoDocumentalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDocumento.ID)
    @ApiModelProperty(name = ConstantesNegocioDocumento.ID, value = "Identificador da função documental.", required = true)
    private Integer id;

    @XmlElement(name = ConstantesNegocioDocumento.NOME)
    @ApiModelProperty(name = ConstantesNegocioDocumento.NOME, value = "Nome negocial da função documental.", required = true)
    private String nome;

    public FuncaoDocumentalDTO() {
        super();
    }

    public FuncaoDocumentalDTO(FuncaoDocumental funcaoDocumental) {
        this();
        this.id = funcaoDocumental.getId();
        this.nome = funcaoDocumental.getNome();
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

}
