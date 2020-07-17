package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalDossieCliente.XML_ROOT_ELEMENT_FUNCAO_DOCUMENTAL)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
          value = ConstantesDossieDigitalDossieCliente.API_MODEL_V1_FUNCAO_DOCUMENTAL,
          description = "Objeto utilizado para representar a função documental no retorno as consultas realizadas a partir do Dossiê do Cliente no contexto do dossiê digital")
public class FuncaoDocumentalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.ID)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.ID, required = true, value = "Valor que identifica a função documental")
    private Integer id;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.NOME)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.NOME, required = true, value = "Valor que reprensenta o nome da função documental")
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
