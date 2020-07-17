package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProduto.XML_ROOT_ELEMENT_TIPO_RELACIONAMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProduto.API_MODEL_V1_TIPO_RELACIONAMENTO,
        description = "Objeto utilizado para o tipo de relacionamento possível de ser definido na vinculação de um dossiê de cliente ao dossiê de produto e os parametros que definem o comportamento esperado nessa inclusão."
)
public class TipoRelacionamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProduto.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ID, required = true, value = "Identificador único do tipo de relacionamento definido para o vinculo.")
    private Integer id;

    @XmlElement(name = ConstantesNegocioDossieProduto.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.NOME, required = true, value = "Identifica o nome do tipo de relacionamento definido para o vinculo.")
    private String nomeTipoRelacionamento;

    @XmlElement(name = ConstantesNegocioDossieProduto.PRINCIPAL, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.PRINCIPAL, required = true, value = "Indica se o tipo de relacionamento possui caracteristica de relacionamento principal.")
    private boolean definicaoPrincipal;

    @XmlElement(name = ConstantesNegocioDossieProduto.INDICA_RELACIONADO, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.INDICA_RELACIONADO, required = true, value = "Indica se o tipo de relacionamento exige a identificação de um dossiê de cliente relacionado.")
    private boolean definicaoRelacionado;

    @XmlElement(name = ConstantesNegocioDossieProduto.INDICA_SEQUENCIA, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.INDICA_SEQUENCIA, required = true, value = "Indica se o tipo de relacionamento exige a identificação de sequenciamento do mesmo tipo de vinculo.")
    private boolean definicaoSequencia;

    public TipoRelacionamentoDTO() {
        super();
    }

    public TipoRelacionamentoDTO(TipoRelacionamento tipoRelacionamento) {
        this();
        if (tipoRelacionamento != null) {
            this.id = tipoRelacionamento.getId();
            this.nomeTipoRelacionamento = tipoRelacionamento.getNome();
            this.definicaoPrincipal = tipoRelacionamento.getIndicadorPrincipal();
            this.definicaoRelacionado = tipoRelacionamento.getIndicadorRelacionado();
            this.definicaoSequencia = tipoRelacionamento.getIndicadorSequencia();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeTipoRelacionamento() {
        return nomeTipoRelacionamento;
    }

    public void setNomeTipoRelacionamento(String nomeTipoRelacionamento) {
        this.nomeTipoRelacionamento = nomeTipoRelacionamento;
    }

    public boolean isDefinicaoPrincipal() {
        return definicaoPrincipal;
    }

    public void setDefinicaoPrincipal(boolean definicaoPrincipal) {
        this.definicaoPrincipal = definicaoPrincipal;
    }

    public boolean isDefinicaoRelacionado() {
        return definicaoRelacionado;
    }

    public void setDefinicaoRelacionado(boolean definicaoRelacionado) {
        this.definicaoRelacionado = definicaoRelacionado;
    }

    public boolean isDefinicaoSequencia() {
        return definicaoSequencia;
    }

    public void setDefinicaoSequencia(boolean definicaoSequencia) {
        this.definicaoSequencia = definicaoSequencia;
    }
}
