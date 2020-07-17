package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto;

import br.gov.caixa.simtr.modelo.entidade.Parecer;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

@XmlRootElement(name = ConstantesNegocioDossieProduto.XML_ROOT_ELEMENT_PARECER)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProduto.API_MODEL_V1_PARECER,
        description = "Objeto utilizado para representar o parecer de um apontamento relacionado a uma verificação sob a ótica Apoio ao Negocio."
)
public class ParecerDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProduto.IDENTIFICADOR_PARECER)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.IDENTIFICADOR_PARECER, required = true, value = "Identificador do parecer.")
    private Long idParecer;

    @XmlElement(name = ConstantesNegocioDossieProduto.IDENTIFICADOR_APONTAMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.IDENTIFICADOR_APONTAMENTO, required = true, value = "Identificador do apontamento vinculado ao parecer.")
    private Long idApontamento;

    @XmlElement(name = ConstantesNegocioDossieProduto.TITULO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.TITULO, required = true, value = "Valor que representa o titulo do apontamento apresentado ao operador.")
    private String titulo;

    @XmlElement(name = ConstantesNegocioDossieProduto.APROVADO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.APROVADO, required = true, value = "Valor que representa a indicação do operador de aprovação ou não para o apontamento analisado. Trata-se do resultado da analise para o apontamento.")
    private boolean aprovado;

    @XmlElement(name = ConstantesNegocioDossieProduto.ORIENTACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ORIENTACAO, required = false, value = "Valor que representa a orientação parametrizada a ser apresentada para o usuário para ajustar o apontamento rejeitado. Caso o parecer tenha indicado aprovação para o apontamento, essa informação não será enviada.")
    private String orientacao;

    @XmlElement(name = ConstantesNegocioDossieProduto.COMENTARIO_ANALISTA)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.COMENTARIO_ANALISTA, required = false, value = "Valor que representa o comentario encaminhado pelo analista no ato da execução do tratamento.")
    private String comentarioTratamento;

    public ParecerDTO() {
        super();
    }

    public ParecerDTO(Parecer parecer) {
        this();
        if (Objects.nonNull(parecer)) {
            this.idParecer = parecer.getId();
            this.aprovado = parecer.getIndicacaoAprovado();
            this.comentarioTratamento = parecer.getComentarioTratamento();
            this.orientacao = null;

            if (Objects.nonNull(parecer.getApontamento())) {
                this.idApontamento = parecer.getApontamento().getId();
                this.titulo = parecer.getApontamento().getNome();
                if (!aprovado) {
                    this.orientacao = parecer.getApontamento().getOrientacao();
                }
            }
        }
    }

    public Long getIdParecer() {
        return idParecer;
    }

    public void setIdParecer(Long idParecer) {
        this.idParecer = idParecer;
    }

    public Long getIdApontamento() {
        return idApontamento;
    }

    public void setIdApontamento(Long idApontamento) {
        this.idApontamento = idApontamento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }

    public String getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(String orientacao) {
        this.orientacao = orientacao;
    }

    public String getComentarioTratamento() {
        return comentarioTratamento;
    }

    public void setComentarioTratamento(String comentarioTratamento) {
        this.comentarioTratamento = comentarioTratamento;
    }
}
