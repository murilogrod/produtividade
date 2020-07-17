package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalDossieCliente.XML_ROOT_ELEMENT_PENDENCIA_CADASTRO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesDossieDigitalDossieCliente.API_MODEL_V1_PENDENCIA_CADASTRO,
        description = "Objeto utilizado para representar uma pendência necessaria ao cadastro basico do cliente junto ao SICLI no retorno as consultas realizadas a partir do Dossiê do Cliente no contexto do dossiê digital")
public class PendenciaCadastroDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonInclude(value = Include.NON_NULL)
    @XmlElement(name = ConstantesDossieDigitalDossieCliente.TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.TIPO_DOCUMENTO, required = false,
            value = "Valor que reprensenta um tipo de documento. Caso prrenchido a pendência não deve indicar função documental")
    private TipoDocumentoDTO tipoDocumento;

    @JsonInclude(value = Include.NON_NULL)
    @XmlElement(name = ConstantesDossieDigitalDossieCliente.FUNCAO_DOCUMENTAL)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.FUNCAO_DOCUMENTAL, required = false,
            value = "Valor que reprensenta uma função documental. Caso prrenchido a pendência não deve indicar tipo de documento")
    private FuncaoDocumentalDTO funcaoDocumental;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.IDENTIFICADOR_COMPOSICAO_DOCUMENTAL)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.IDENTIFICADOR_COMPOSICAO_DOCUMENTAL, required = false,
            value = "Valor que reprensenta o identificador da composiçãop documental relacioonada com a pendencia. Para resolver a pendência é necessário atender todas as indicações de pendências de uma mesma composição.")
    private Long identificadorComposicaoDocumental;

    public PendenciaCadastroDTO() {
        super();
    }

    public PendenciaCadastroDTO(TipoDocumento tipoDocumento, Long identificadorComposicao) {
        this();
        this.identificadorComposicaoDocumental = identificadorComposicao;
        this.tipoDocumento = new TipoDocumentoDTO(tipoDocumento);
        this.funcaoDocumental = null;
    }

    public PendenciaCadastroDTO(FuncaoDocumental funcaoDocumental, Long identificadorComposicao) {
        this();
        this.identificadorComposicaoDocumental = identificadorComposicao;
        this.tipoDocumento = null;
        this.funcaoDocumental = new FuncaoDocumentalDTO(funcaoDocumental);
    }

    public TipoDocumentoDTO getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumentoDTO tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public FuncaoDocumentalDTO getFuncaoDocumental() {
        return funcaoDocumental;
    }

    public void setFuncaoDocumental(FuncaoDocumentalDTO funcaoDocumental) {
        this.funcaoDocumental = funcaoDocumental;
    }

    public Long getIdentificadorComposicaoDocumental() {
        return identificadorComposicaoDocumental;
    }

    public void setIdentificadorComposicaoDocumental(Long identificadorComposicaoDocumental) {
        this.identificadorComposicaoDocumental = identificadorComposicaoDocumental;
    }
}
