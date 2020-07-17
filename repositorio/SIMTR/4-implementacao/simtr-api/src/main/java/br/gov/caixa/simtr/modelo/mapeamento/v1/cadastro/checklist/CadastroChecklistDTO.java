package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.Checklist;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroChecklist;
import br.gov.caixa.simtr.visao.PrototypeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesCadastroChecklist.XML_ROOT_ELEMENT_CHECKLIST)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesCadastroChecklist.API_MODEL_V1_CHECKLIST,
        description = "Objeto utilizado para representar um checklist a ser cadastrado."
)
public class CadastroChecklistDTO implements Serializable, PrototypeDTO<Checklist>{
    private static final long serialVersionUID = 1L;
    
    @JsonProperty(value = ConstantesCadastroChecklist.NOME)
    @ApiModelProperty(name = ConstantesCadastroChecklist.NOME, value = "Atributo que representa o nome dado ao checklist.")
    private String nomeChecklist;
    
    @JsonProperty(value = ConstantesCadastroChecklist.UNIDADE_RESPONSAVEL)
    @ApiModelProperty(name = ConstantesCadastroChecklist.UNIDADE_RESPONSAVEL, value = "Atributo que representa qual unidade o checklist pertence.")
    private Integer unidadeResponsavel;
    
    @JsonProperty(value = ConstantesCadastroChecklist.VERIFICACAO_PREVIA)
    @ApiModelProperty(name = ConstantesCadastroChecklist.VERIFICACAO_PREVIA, value = "Atributo que define se o checklist é do tipo prévio.")
    private Boolean verificacaoPrevia;
    
    @JsonProperty(value = ConstantesCadastroChecklist.ORIENTACAO_OPERADOR)
    @ApiModelProperty(name = ConstantesCadastroChecklist.ORIENTACAO_OPERADOR, value = "Atributo que define uma mensagem de orientação ao usuário.")
    private String orientacaoOperador;
    
    public String getNomeChecklist() {
        return nomeChecklist;
    }
    
    public void setNomeChecklist(String nomeChecklist) {
        this.nomeChecklist = nomeChecklist;
    }
    
    public Integer getUnidadeResponsavel() {
        return unidadeResponsavel;
    }
    
    public void setUnidadeResponsavel(Integer unidadeResponsavel) {
        this.unidadeResponsavel = unidadeResponsavel;
    }
    
    public Boolean getVerificacaoPrevia() {
        return verificacaoPrevia;
    }
    
    public void setVerificacaoPrevia(Boolean verificacaoPrevia) {
        this.verificacaoPrevia = verificacaoPrevia;
    }
    
    public String getOrientacaoOperador() {
        return orientacaoOperador;
    }
    
    public void setOrientacaoOperador(String orientacaoOperador) {
        this.orientacaoOperador = orientacaoOperador;
    }

    @Override
    public Checklist prototype() {
        Checklist checklist = new Checklist();
        checklist.setNome(this.getNomeChecklist());
        checklist.setUnidade(this.getUnidadeResponsavel());
        checklist.setIndicacaoVerificacaoPrevia(this.getVerificacaoPrevia());
        checklist.setOrientacaoOperador(this.getOrientacaoOperador());
        checklist.setDataHoraCriacao(Calendar.getInstance());
        checklist.setDataHoraUltimaAlteracao(Calendar.getInstance());
        return checklist;
    }
}
