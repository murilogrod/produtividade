package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastro;
import br.gov.caixa.simtr.controle.vo.excecao.PendenciasVO;
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.Objects;

@XmlRootElement(name = ConstantesCadastro.XML_ROOT_PENDENCIA_CADASTRO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesCadastro.API_MODEL_PENDENCIA,
        description = "Objeto utilizado para representar um retorno indicando os problemas apresentados no envio das verificações realizadas no ato da apuração do tratamento encaminhado a um dossiê de produto sob a ótica Apoio ao Negocio."
)
public class PendenciaCadastroDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesCadastro.CAMPO)
    @JsonProperty(value = ConstantesCadastro.CAMPO)
    @ApiModelProperty(name = ConstantesCadastro.CAMPO, required = false, value = "Identificador do campo/atributo que esta relacionado com o problema. Trata-se do mesmo nome de atributo encaminhado no corpo da mensagem de envio da mensagem")
    private String campo;

    @XmlElementWrapper(name = ConstantesCadastro.APONTAMENTOS)
    @XmlElement(name = ConstantesCadastro.APONTAMENTOS_ELEMENTO)
    @JsonProperty(value = ConstantesCadastro.APONTAMENTOS)
    @ApiModelProperty(name = ConstantesCadastro.APONTAMENTOS, required = false, value = "Lista de apontamentos identificados para o campo em referência")
    private List<String> apontamentos;

    public PendenciaCadastroDTO() {
        super();
        this.apontamentos = new ArrayList<>();
    }

    public PendenciaCadastroDTO(PendenciasVO pendencia) {
        this();

        if (Objects.nonNull(pendencia)) {
            this.campo = pendencia.getCampo();
            this.apontamentos = pendencia.getApontamentos();
        }
    }

    public PendenciaCadastroDTO(String campo, List<String> apontamentos) {
        this();

        if (Objects.nonNull(campo)) {
            this.campo = campo;
        }
        if (Objects.nonNull(apontamentos)) {
            this.apontamentos = apontamentos;
        }
    }

    public String getCampo() {
        return campo;
    }

    public List<String> getApontamentos() {
        return apontamentos;
    }
}
