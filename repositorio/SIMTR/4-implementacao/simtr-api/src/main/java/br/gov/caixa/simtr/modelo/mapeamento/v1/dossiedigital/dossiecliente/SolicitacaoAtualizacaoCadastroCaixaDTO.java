package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalDossieCliente;
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

@XmlRootElement(name = ConstantesDossieDigitalDossieCliente.XML_ROOT_ELEMENT_SOLICITACAO_ATUALIZACAO_CADASTRO_CAIXA)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesDossieDigitalDossieCliente.API_MODEL_V1_SOLICITACAO_ATUALIZACAO_CADASTRO_CAIXA,
        description = "Objeto utilizado para representar a solicitação de atualização dos dados cadastrais no cadastro CAIXA e com a indicação dos documentos a serem utilizados."
)
public class SolicitacaoAtualizacaoCadastroCaixaDTO implements Serializable {

    /**
     * Atributo serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.DOCUMENTO_SIMTR)
    @XmlElementWrapper(name = ConstantesDossieDigitalDossieCliente.DOCUMENTOS_SIMTR)
    @JsonProperty(value = ConstantesDossieDigitalDossieCliente.DOCUMENTOS_SIMTR)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.DOCUMENTOS_SIMTR, required = false, value = "Lista de identificadores do SIMTR para os documentos que serão utilizados na atualização do cadastro")
    private List<Long> identificadoresDocumentosSIMTR;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.DOCUMENTO_SIECM)
    @XmlElementWrapper(name = ConstantesDossieDigitalDossieCliente.DOCUMENTOS_SIECM)
    @JsonProperty(value = ConstantesDossieDigitalDossieCliente.DOCUMENTOS_SIECM)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.DOCUMENTOS_SIECM, required = false, value = "Lista de identificadores do SIECM para os documentos que serão utilizados na atualização do cadastro")
    private List<String> identificadoresDocumentosSIECM;

    public SolicitacaoAtualizacaoCadastroCaixaDTO() {
        this.identificadoresDocumentosSIMTR = new ArrayList<>();
        this.identificadoresDocumentosSIECM = new ArrayList<>();
    }
    
    public List<Long> getIdentificadoresDocumentosSIMTR() {
        return identificadoresDocumentosSIMTR;
    }

    public void setIdentificadoresDocumentosSIMTR(List<Long> identificadoresDocumentosSIMTR) {
        this.identificadoresDocumentosSIMTR = identificadoresDocumentosSIMTR;
    }

    public List<String> getIdentificadoresDocumentosSIECM() {
        return identificadoresDocumentosSIECM;
    }

    public void setIdentificadoresDocumentosSIECM(List<String> identificadoresDocumentosSIECM) {
        this.identificadoresDocumentosSIECM = identificadoresDocumentosSIECM;
    }

}
