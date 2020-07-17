package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.OpcaoSelecionada;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalDossieCliente.XML_ROOT_ELEMENT_ATRIBUTO_DOCUMENTO)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
@ApiModel(
          value = ConstantesDossieDigitalDossieCliente.API_MODEL_V1_ATRIBUTO_DOCUMENTO,
          description = "Objeto utilizado para representar os atributos de um documento ou como retorno ajustados para atualização.")
public class AtributoDocumentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.CHAVE)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.CHAVE, required = true, value = "Nome do atributo do documento", example = "nome")
    private String chave;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.VALOR)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.VALOR, required = true, value = "Valor do atributo do documento no caso de atributos objetivos. Se utilizado o elemento \"opcoes_selecionadas\" não deve ser informado", example = "fulano de tal")
    private String valor;
    
    @JsonInclude(value = Include.NON_EMPTY)
    @XmlElement(name = ConstantesDossieDigitalDossieCliente.OPCAO_SELECIONADA)
    @XmlElementWrapper(name = ConstantesDossieDigitalDossieCliente.OPCOES_SELECIONADAS)
    @JsonProperty(value = ConstantesDossieDigitalDossieCliente.OPCOES_SELECIONADAS)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.OPCOES_SELECIONADAS, required = false, value = "Valor da opção selecionada no caso de atributos objetivos. Se utilizado o elemento \"valor\" não deve ser informado")
    private List<String> opcoesSelecionadas;

    public AtributoDocumentoDTO() {
        super();
        this.opcoesSelecionadas = new ArrayList<>();
    }

    public AtributoDocumentoDTO(String chave, String valor) {
        this();
        this.chave = chave;
        this.valor = valor;
    }
    
    public AtributoDocumentoDTO(String chave, List<String> opcoesSelecionadas) {
        this();
        this.chave = chave;
        if(opcoesSelecionadas != null) {
            opcoesSelecionadas.forEach(os -> this.opcoesSelecionadas.add(os));            
        }
    }

    public AtributoDocumentoDTO(AtributoDocumento atributoDocumento) {
        this();
        if (atributoDocumento != null) {
            this.chave = atributoDocumento.getDescricao();
            this.valor = atributoDocumento.getConteudo();
            if(atributoDocumento.getOpcoesSelecionadas() != null) {
                atributoDocumento.getOpcoesSelecionadas().forEach(os -> this.opcoesSelecionadas.add(os.getValorOpcao()));
            }
        }
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    public List<String> getOpcoesSelecionadas() {
        return opcoesSelecionadas;
    }

    public void setOpcoesSelecionadas(List<String> opcoesSelecionadas) {
        this.opcoesSelecionadas = opcoesSelecionadas;
    }

    public AtributoDocumento prototype() {
        AtributoDocumento atributoDocumento = new AtributoDocumento();
        atributoDocumento.setDescricao(this.chave);
        atributoDocumento.setConteudo(this.valor);
        this.opcoesSelecionadas.forEach(os -> {
            OpcaoSelecionada opcaoSelecionada = new OpcaoSelecionada();
            opcaoSelecionada.setAtributoDocumento(atributoDocumento);
            opcaoSelecionada.setValorOpcao(os);
            atributoDocumento.addOpcaoSelecionada(opcaoSelecionada);
        });
        return atributoDocumento;
    }
}
