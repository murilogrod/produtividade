package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoManutencao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoManutencao.XML_ROOT_ELEMENT_DOSSIE_PRODUTO_INCLUSAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = ConstantesNegocioDossieProdutoManutencao.API_MODEL_V1_DOSSIE_PRODUTO_INCLUSAO, description = "Objeto utilizado para realizar a inclusão de um Dossiê de Produto inicial limitando as possibilidades de definição dos atributos.")
public class DossieProdutoInclusaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.RASCUNHO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.RASCUNHO, required = true, value = "Indica se o processo deve ser armazenado como rascunho.")
    private boolean rascunho;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.PROCESSO_ORIGEM)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.PROCESSO_ORIGEM, required = true, value = "Indica o identificador do processo originador do dossiê.")
    private Integer processoOrigem;

    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.ELEMENTOS_CONTEUDO)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.ELEMENTO_CONTEUDO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.ELEMENTOS_CONTEUDO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.ELEMENTOS_CONTEUDO, required = false, value = "Lista de elementos de conteudo carregados para atender definições do processo")
    private List<ElementoConteudoDTO> elementosConteudoDTO;

    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.PRODUTOS_CONTRATADOS)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.PRODUTO_CONTRATADO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.PRODUTOS_CONTRATADOS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.PRODUTOS_CONTRATADOS, required = false, value = "Lista de produtos contratados vinculados ao dossiê de produto")
    private List<ProdutoContratadoDTO> produtosContratadosDTO;

    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.VINCULOS_PESSOAS)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.VINCULO_PESSOA)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.VINCULOS_PESSOAS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.VINCULOS_PESSOAS, required = false, value = "Lista de dossiês de cliente vinculados ao dossiê de produto com a respectiva identificação do tipo de vinculo")
    private List<VinculoPessoaDTO> vinculosDossieClienteDTO;

    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.GARANTIAS_INFORMADAS)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.GARANTIA_INFORMADA)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.GARANTIAS_INFORMADAS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.GARANTIAS_INFORMADAS, required = false, value = "Lista de garantias informadas para a contratação dos produtos")
    private List<GarantiaInformadaDTO> garantiasInformadasDTO;

    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.RESPOSTAS_FORMULARIO)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.RESPOSTA_FORMULARIO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.RESPOSTAS_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.RESPOSTAS_FORMULARIO, required = false, value = "Lista de respostas de formulario submetidas ao dossiê de produto")
    private List<RespostaFormularioDTO> respostasFormularioDTO;

    public DossieProdutoInclusaoDTO() {
	super();
	this.elementosConteudoDTO = new ArrayList<>();
	this.produtosContratadosDTO = new ArrayList<>();
	this.vinculosDossieClienteDTO = new ArrayList<>();
	this.garantiasInformadasDTO = new ArrayList<>();
	this.respostasFormularioDTO = new ArrayList<>();
    }

    public boolean isRascunho() {
	return rascunho;
    }

    public void setRascunho(boolean rascunho) {
	this.rascunho = rascunho;
    }

    public Integer getProcessoOrigem() {
	return processoOrigem;
    }

    public void setProcessoOrigem(Integer processoOrigem) {
	this.processoOrigem = processoOrigem;
    }

    public List<ElementoConteudoDTO> getElementosConteudoDTO() {
	return elementosConteudoDTO;
    }

    public void setElementosConteudoDTO(List<ElementoConteudoDTO> elementosConteudoDTO) {
	this.elementosConteudoDTO = elementosConteudoDTO;
    }

    public List<ProdutoContratadoDTO> getProdutosContratadosDTO() {
	return produtosContratadosDTO;
    }

    public void setProdutosContratadosDTO(List<ProdutoContratadoDTO> produtosContratadosDTO) {
	this.produtosContratadosDTO = produtosContratadosDTO;
    }

    public List<VinculoPessoaDTO> getVinculosDossieClienteDTO() {
	return vinculosDossieClienteDTO;
    }

    public void setVinculosDossieClienteDTO(List<VinculoPessoaDTO> vinculosDossieClienteDTO) {
	this.vinculosDossieClienteDTO = vinculosDossieClienteDTO;
    }

    public List<GarantiaInformadaDTO> getGarantiasInformadasDTO() {
	return garantiasInformadasDTO;
    }

    public void setGarantiasInformadasDTO(List<GarantiaInformadaDTO> garantiasInformadasDTO) {
	this.garantiasInformadasDTO = garantiasInformadasDTO;
    }

    public List<RespostaFormularioDTO> getRespostasFormularioDTO() {
	return respostasFormularioDTO;
    }

    public void setRespostasFormularioDTO(List<RespostaFormularioDTO> respostasFormularioDTO) {
	this.respostasFormularioDTO = respostasFormularioDTO;
    }

}
