package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.Conteudo;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import br.gov.caixa.simtr.visao.PrototypeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProduto.XML_ROOT_ELEMENT_CONTEUDO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProduto.API_MODEL_V1_CONTEUDO,
        description = "Objeto utilizado para representar o Conteudo do Documento no retorno as consultas do Dossiê de Produto realizadas sob a ótica Apoio ao Negocio."
)
public class ConteudoDTO implements Serializable, PrototypeDTO<Conteudo> {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProduto.BINARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.BINARIO, required = true, value = "Codigo de identificação do documento perante a solução de GED. Este atributo esta sendo migrado para o documento e esta em desuso perante o conteudo.")
    private String base64;

    @XmlElement(name = ConstantesNegocioDossieProduto.SEQUENCIA_APRESENTACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.SEQUENCIA_APRESENTACAO, required = true, value = "Valor que indica a ordem de apresentação dos conteudos para um documento com multiplas paginas/conteudos.")
    private Integer ordem;

    // *********************************************
    public ConteudoDTO() {
        super();
    }

    public ConteudoDTO(Conteudo conteudo) {
        this();
        this.base64 = conteudo.getBase64();
        this.ordem = conteudo.getOrdem();
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    @Override
    public Conteudo prototype() {
        Conteudo conteudo = new Conteudo();
        conteudo.setBase64(this.base64);
        conteudo.setOrdem(this.ordem);
        return conteudo;
    }
}
