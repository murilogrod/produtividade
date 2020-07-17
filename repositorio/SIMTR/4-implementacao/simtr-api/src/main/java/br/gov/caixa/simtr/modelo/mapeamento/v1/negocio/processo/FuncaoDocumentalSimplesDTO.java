package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioProcesso;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioProcesso.XML_ROOT_ELEMENT_FUNCAO_DOCUMENTAL)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioProcesso.API_MODEL_V1_FUNCAO_DOCUMENTAL,
        description = "Objeto utilizado para representar a função documental utilizado na definição do processo não contendo a lista de tipos de documentos associados"
)
public class FuncaoDocumentalSimplesDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioProcesso.ID)
    @ApiModelProperty(name = ConstantesNegocioProcesso.ID, value = "Identificador único da função documental.", required = true)
    private Integer id;

    @XmlElement(name = ConstantesNegocioProcesso.NOME)
    @ApiModelProperty(name = ConstantesNegocioProcesso.NOME, value = "Nome descritivo da função documental.", required = true)
    private String nome;

    public FuncaoDocumentalSimplesDTO() {
        super();
    }

    public FuncaoDocumentalSimplesDTO(FuncaoDocumental funcaoDocumental) {
        this();
        if (funcaoDocumental != null) {
            this.id = funcaoDocumental.getId();
            this.nome = funcaoDocumental.getNome();
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
}
