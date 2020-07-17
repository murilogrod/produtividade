package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoManutencao;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoManutencao.XML_ROOT_ELEMENT_ELEMENTO_CONTEUDO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoManutencao.API_MODEL_V1_ELEMENTO_CONTEUDO,
        description = "Objeto utilizado para para representar um elemento de conteudo no momento de cadastro do dossiê de produto na ótica do Apoio ao Negócio."
)
public class ElementoConteudoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.IDENTIFICADOR_ELEMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.IDENTIFICADOR_ELEMENTO, required = true, value = "Identificador do elemento a ser vinculado o documento gerado com o conteudo carregado")
    private Long idElemento;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.DOCUMENTO, required = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.DOCUMENTO, required = true, value = "Documento encaminhado como para atendimento ao elemento de conteudo solicitado")
    private DocumentoDTO documentoDTO;

    public ElementoConteudoDTO() {
        super();
    }

    public Long getIdElemento() {
        return idElemento;
    }

    public void setIdElemento(Long idElemento) {
        this.idElemento = idElemento;
    }

    public DocumentoDTO getDocumentoDTO() {
        return documentoDTO;
    }

    public void setDocumentoDTO(DocumentoDTO documentoDTO) {
        this.documentoDTO = documentoDTO;
    }
}
