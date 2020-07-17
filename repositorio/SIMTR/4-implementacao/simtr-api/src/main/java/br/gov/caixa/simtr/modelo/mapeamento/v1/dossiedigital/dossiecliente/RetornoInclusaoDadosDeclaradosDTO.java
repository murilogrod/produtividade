package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalDossieCliente;
import br.gov.caixa.simtr.util.ConstantesUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalDossieCliente.XML_ROOT_ELEMENT_RETORNO_INCLUSAO_DADOS_DECLARADOS)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
          value = ConstantesDossieDigitalDossieCliente.API_MODEL_V1_RETORNO_INCLUSAO_DADOS_DECLARADOS,
          description = "Objeto utilizado para representar a resposta a uma solicitação de atualização de documento de dados declarados perante o fluxo do Dossiê Digital.")
public class RetornoInclusaoDadosDeclaradosDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.IDENTIFICADOR_SIMTR)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.IDENTIFICADOR_SIMTR, required = true, value = "Representa o codigo identificador do documento junto ao GED.",
                      example = "2134")
    private Long identificadorSIMTR;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.IDENTIFICADOR_SIECM)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.IDENTIFICADOR_SIECM, required = true, value = "Representa o codigo identificador do documento junto ao GED.",
                      example = "1073F866-0000-C31A-8B12-2CBDA9B41434")
    private String identificadorSIECM;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.OBJECT_STORE)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.OBJECT_STORE, required = true, value = "Indica o Object Store de armazenamento do documento.", example = "OS_CAIXA")
    private String objectStore;

    public RetornoInclusaoDadosDeclaradosDTO(Documento documento) {
        super();
        if (documento != null) {
            this.identificadorSIMTR = documento.getId();
            if (documento.getCodigoSiecmReuso() != null) {
                this.identificadorSIECM = documento.getCodigoSiecmReuso();
                this.objectStore = ConstantesUtil.SIECM_OS_REUSO;
            } else if (documento.getCodigoSiecmTratado() != null) {
                this.identificadorSIECM = documento.getCodigoSiecmTratado();
                this.objectStore = ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL;
            } else if (documento.getCodigoSiecmCaixa() != null) {
                this.identificadorSIECM = documento.getCodigoSiecmCaixa();
                this.objectStore = ConstantesUtil.SIECM_OS_CAIXA;
            } else {
                this.identificadorSIECM = documento.getCodigoGED();
                this.objectStore = ConstantesUtil.SIECM_OS_DOSSIE_DIGITAL;
            }
        }
    }

    public Long getIdentificadorSIMTR() {
        return identificadorSIMTR;
    }

    public void setIdentificadorSIMTR(Long identificadorSIMTR) {
        this.identificadorSIMTR = identificadorSIMTR;
    }

    public String getIdentificadorSIECM() {
        return identificadorSIECM;
    }

    public void setIdentificadorSIECM(String identificadorSIECM) {
        this.identificadorSIECM = identificadorSIECM;
    }

    public String getObjectStore() {
        return objectStore;
    }

    public void setObjectStore(String objectStore) {
        this.objectStore = objectStore;
    }
}
