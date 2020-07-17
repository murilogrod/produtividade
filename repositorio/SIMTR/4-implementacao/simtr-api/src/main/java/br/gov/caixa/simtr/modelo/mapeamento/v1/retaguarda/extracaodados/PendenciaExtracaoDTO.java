package br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.extracaodados;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesRetaguardaExtracaoDados;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesRetaguardaExtracaoDados.XML_ROOT_ELEMENT_PENDENCIA_EXTRACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesRetaguardaExtracaoDados.API_MODEL_PENDENCIA_EXTRACAO,
        description = "Objeto utilizado para representar a quantidade de documentos pendentes de extracao de dados por tipo de documento sob a ótica Apoio ao Negocio."
)
public class PendenciaExtracaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesRetaguardaExtracaoDados.ID_TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.ID_TIPO_DOCUMENTO, required = true, value = "Valor que representa o identificador do Tipo de Documento.")
    private Integer id;

    @XmlElement(name = ConstantesRetaguardaExtracaoDados.NOME_TIPO_DOCUMENTO)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.NOME_TIPO_DOCUMENTO, required = true, value = "Valor que reprensenta o nome do Tipo de Documento.")
    private String nome;

    @XmlElement(name = ConstantesRetaguardaExtracaoDados.QUANTIDADE)
    @ApiModelProperty(name = ConstantesRetaguardaExtracaoDados.QUANTIDADE, required = true, value = "Valor que representa a quantidade de documents pendentes de extração de dados.")
    private Integer quantidade;

    public PendenciaExtracaoDTO() {
        super();
    }

    public PendenciaExtracaoDTO(TipoDocumento tipoDocumento, Integer quantidade) {
        this();
        this.id = tipoDocumento.getId();
        this.nome = tipoDocumento.getNome();
        this.quantidade = quantidade != null ? quantidade : 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

}
