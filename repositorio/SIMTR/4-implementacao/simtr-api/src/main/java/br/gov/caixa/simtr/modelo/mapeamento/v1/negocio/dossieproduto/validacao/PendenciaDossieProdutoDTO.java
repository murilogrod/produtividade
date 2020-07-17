package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoValidacao;
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

@XmlRootElement(name = ConstantesNegocioDossieProdutoValidacao.XML_ROOT_ELEMENT_PENDENCIA_DOSSIE_PRODUTO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoValidacao.API_MODEL_V1_PENDENCIA_DOSSIE_PRODUTO,
        description = "Objeto utilizado para representar um retorno indicando os problemas apresentados na validação realizada em um dossiê de produto a ser incluido ou alterado."
)
public class PendenciaDossieProdutoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.PENDENCIA_VINCULO_PESSOA)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoValidacao.PENDENCIAS_VINCULO_PESSOA)
    @JsonProperty(value = ConstantesNegocioDossieProdutoValidacao.PENDENCIAS_VINCULO_PESSOA)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.PENDENCIAS_VINCULO_PESSOA, required = false, value = "Lista de pendências documentais para os vinculos de pessoas informados que foram identificadas no ato de inclusão ou atualização do dossiê de produto analisado")
    private List<PendenciaVinculoPessoaDTO> pendenciasVinculoPessoaDTO;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.PENDENCIA_VINCULO_PRODUTO)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoValidacao.PENDENCIAS_VINCULO_PRODUTO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoValidacao.PENDENCIAS_VINCULO_PRODUTO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.PENDENCIAS_VINCULO_PRODUTO, required = false, value = "Lista de pendências documentais para os vinculos de produtos informados que foram identificadas no ato de inclusão ou atualização do dossiê de produto analisado")
    private List<PendenciaVinculoProdutoDTO> pendenciasVinculoProdutoDTO;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.PENDENCIA_PROCESSO_DOSSIE)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoValidacao.PENDENCIAS_PROCESSO_DOSSIE)
    @JsonProperty(value = ConstantesNegocioDossieProdutoValidacao.PENDENCIAS_PROCESSO_DOSSIE)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.PENDENCIAS_PROCESSO_DOSSIE, required = false, value = "Lista de pendências documentais para os elementos definidos no processo gerador do dossiê que foram identificadas no ato de inclusão ou atualização do dossiê de produto analisado")
    private List<PendenciaProcessoDTO> pendenciasProcessoDossieDTO;

    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.PENDENCIA_PROCESSO_FASE)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoValidacao.PENDENCIAS_PROCESSO_FASE)
    @JsonProperty(value = ConstantesNegocioDossieProdutoValidacao.PENDENCIAS_PROCESSO_FASE)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.PENDENCIAS_PROCESSO_FASE, required = false, value = "Lista de pendências documentais para os elementos definidos no processo fase que foram identificadas no ato de inclusão ou atualização do dossiê de produto analisado")
    private List<PendenciaProcessoDTO> pendenciasProcessoFaseDTO;
    
    @XmlElement(name = ConstantesNegocioDossieProdutoValidacao.PENDENCIAS_GARANTIA)
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoValidacao.PENDENCIAS_GARANTIA)
    @JsonProperty(value = ConstantesNegocioDossieProdutoValidacao.PENDENCIAS_GARANTIA)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoValidacao.PENDENCIAS_GARANTIA, required = false, value = "Lista de pendências para os vinculos de garantia que foram identificadas no ato de inclusão ou atualização do dossiê de produto analisado")
    List<PendenciaGarantiaInformadaDTO> pendenciasGarantiasDTO;

    private PendenciaDossieProdutoDTO() {
        super();
        this.pendenciasVinculoPessoaDTO = new ArrayList<>();
        this.pendenciasVinculoProdutoDTO = new ArrayList<>();
        this.pendenciasProcessoDossieDTO = new ArrayList<>();
        this.pendenciasProcessoFaseDTO = new ArrayList<>();
        this.pendenciasGarantiasDTO = new ArrayList<>(); 
    }

    public PendenciaDossieProdutoDTO(
            List<PendenciaVinculoPessoaDTO> pendenciasVinculoPessoaDTO,
            List<PendenciaVinculoProdutoDTO> pendenciasVinculoProdutoDTO,
            List<PendenciaProcessoDTO> pendenciasProcessoDossieDTO,
            List<PendenciaProcessoDTO> pendenciasProcessoFaseDTO,
            List<PendenciaGarantiaInformadaDTO> pendenciasGarantiasDTO) {
        this();
        if (pendenciasVinculoPessoaDTO != null) {
            this.pendenciasVinculoPessoaDTO = pendenciasVinculoPessoaDTO;
        }
        if (pendenciasVinculoProdutoDTO != null) {
            this.pendenciasVinculoProdutoDTO = pendenciasVinculoProdutoDTO;
        }
        if (pendenciasProcessoDossieDTO != null) {
            this.pendenciasProcessoDossieDTO = pendenciasProcessoDossieDTO;
        }
        if (pendenciasProcessoFaseDTO != null) {
            this.pendenciasProcessoFaseDTO = pendenciasProcessoFaseDTO;
        }
        if(pendenciasGarantiasDTO  != null) {
            this.pendenciasGarantiasDTO = pendenciasGarantiasDTO;
        }
    }

    public List<PendenciaVinculoPessoaDTO> getPendenciasVinculoPessoaDTO() {
        return pendenciasVinculoPessoaDTO;
    }

    public void setPendenciasVinculoPessoaDTO(List<PendenciaVinculoPessoaDTO> pendenciasVinculoPessoaDTO) {
        this.pendenciasVinculoPessoaDTO = pendenciasVinculoPessoaDTO;
    }

    public List<PendenciaVinculoProdutoDTO> getPendenciasVinculoProdutoDTO() {
        return pendenciasVinculoProdutoDTO;
    }

    public void setPendenciasVinculoProdutoDTO(List<PendenciaVinculoProdutoDTO> pendenciasVinculoProdutoDTO) {
        this.pendenciasVinculoProdutoDTO = pendenciasVinculoProdutoDTO;
    }

    public List<PendenciaProcessoDTO> getPendenciasProcessoDossieDTO() {
        return pendenciasProcessoDossieDTO;
    }

    public void setPendenciasProcessoDossieDTO(List<PendenciaProcessoDTO> pendenciasProcessoDossieDTO) {
        this.pendenciasProcessoDossieDTO = pendenciasProcessoDossieDTO;
    }

    public List<PendenciaProcessoDTO> getPendenciasProcessoFaseDTO() {
        return pendenciasProcessoFaseDTO;
    }

    public void setPendenciasProcessoFaseDTO(List<PendenciaProcessoDTO> pendenciasProcessoFaseDTO) {
        this.pendenciasProcessoFaseDTO = pendenciasProcessoFaseDTO;
    }

    public void setPendenciasGarantiasDTO(List<PendenciaGarantiaInformadaDTO> pendenciasGarantiasDTO) {
        this.pendenciasGarantiasDTO = pendenciasGarantiasDTO;
    }
}
