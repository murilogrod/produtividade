/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.autorizacao;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalAutorizacao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * @author c090347
 */
@XmlRootElement(name = ConstantesDossieDigitalAutorizacao.XML_ROOT_ELEMENT_ANALISE_REGRA)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
          value = ConstantesDossieDigitalAutorizacao.API_MODEL_V1_ANALISE_REGRA,
          description = "Objeto utilizado para representar as analise das regras documentais sob o contexto do fluxo de autorização do Dossiê Digital.")
public class AnaliseRegraDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalAutorizacao.TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.TIPO_DOCUMENTO, required = true, value = "Nome do tipo de documento associado a regra analisada")
    private String tipoDocumento;
    
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.FUNCAO_DOCUMENTAL)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.FUNCAO_DOCUMENTAL, required = true, value = "Nome da função documental associada a regra analisada")
    private String funcaoDocumental;
    
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.IDENTIFICADOR_COMPOSICAO)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.IDENTIFICADOR_COMPOSICAO, required = true, value = "Identificador da compopsição em que a regra esta vinculada. Para atender a uma composição todas as regras vinculadas devem ser atendidas.")
    private Long identificadorComposicao;
    
    @XmlElement(name = ConstantesDossieDigitalAutorizacao.DOSSIE_DIGITAL)
    @ApiModelProperty(name = ConstantesDossieDigitalAutorizacao.DOSSIE_DIGITAL, required = true, value = "Indicador se existe um documento valido com a marca dossiê digital  que atenda a regra vinculado ao dossiê do cliente")
    private boolean dossieDigital;

    public AnaliseRegraDTO() {
        super();
    }

    public AnaliseRegraDTO(String tipoDocumento, String funcaoDocumental, Long identificadorComposicao, boolean presenteDossie) {
        this.tipoDocumento = tipoDocumento;
        this.funcaoDocumental = funcaoDocumental;
        this.identificadorComposicao = identificadorComposicao;
        this.dossieDigital = presenteDossie;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getFuncaoDocumental() {
        return funcaoDocumental;
    }

    public void setFuncaoDocumental(String funcaoDocumental) {
        this.funcaoDocumental = funcaoDocumental;
    }

    public Long getIdentificadorComposicao() {
        return identificadorComposicao;
    }

    public void setIdentificadorComposicao(Long identificadorComposicao) {
        this.identificadorComposicao = identificadorComposicao;
    }

    public boolean isDossieDigital() {
        return dossieDigital;
    }

    public void setDossieDigital(boolean dossieDigital) {
        this.dossieDigital = dossieDigital;
    }

}
