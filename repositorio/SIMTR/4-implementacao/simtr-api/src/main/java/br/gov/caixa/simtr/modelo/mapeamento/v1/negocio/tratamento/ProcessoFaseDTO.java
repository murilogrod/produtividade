package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tratamento;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.RelacaoProcesso;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTratamento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioTratamento.XML_ROOT_ELEMENT_PROCESSO_FASE)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTratamento.API_MODEL_V1_PROCESSO_FASE,
        description = "Objeto utilizado para representar o processo fase no retorno as consultas realizadas para o tratamento"
)
public class ProcessoFaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioTratamento.ID)
    @ApiModelProperty(name = ConstantesNegocioTratamento.ID, required = true, value = "Codigo de identificação do processo")
    private Integer id;

    @XmlElement(name = ConstantesNegocioTratamento.NOME)
    @ApiModelProperty(name = ConstantesNegocioTratamento.NOME, required = true, value = "Nome de identificação do processo")
    private String nome;

    @XmlElement(name = ConstantesNegocioTratamento.AVATAR)
    @ApiModelProperty(name = ConstantesNegocioTratamento.AVATAR, required = false, value = "Identificação do nome da icone que representa o processo")
    private String avatar;

    @XmlElement(name = ConstantesNegocioTratamento.PRIORIZADO)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(name = ConstantesNegocioTratamento.PRIORIZADO, required = false, value = "Indica que a estrutura de processos esta priorizada e por isso a visão da lista apresetada na captura de dossiês para tratamento será diferenciada")
    private Boolean priorizado;

    @XmlElement(name = ConstantesNegocioTratamento.QUANTIDADE_DOSSIES)
    @ApiModelProperty(name = ConstantesNegocioTratamento.QUANTIDADE_DOSSIES, required = false, value = "Indica a quantiadade de dossi~es de produto na situação \"Aguardando Tratamento\" na realização da consulta.")
    private Integer quantidadeDossies;

    public ProcessoFaseDTO() {
        super();
        this.quantidadeDossies = 0;
    }

    public ProcessoFaseDTO(Processo processo) {
        this();
        if (processo != null) {
            this.id = processo.getId();
            this.nome = processo.getNome();
            this.avatar = processo.getAvatar();

            if (processo.getDossiesProduto() != null) {
                this.quantidadeDossies += processo.getDossiesProduto().size();
            }

            if (processo.getProcessosFaseDossie() != null) {
                this.quantidadeDossies += (int) processo.getProcessosFaseDossie().stream().filter(pfd -> pfd.getDataHoraSaida() == null).count();
            }
            
            if (processo.getRelacoesProcessoVinculoPai() != null) {
                List<RelacaoProcesso> relacoes = Arrays.asList(processo.getRelacoesProcessoVinculoPai().toArray(new RelacaoProcesso[processo.getRelacoesProcessoVinculoPai().size()]));
                relacoes.sort(Comparator.nullsFirst(Comparator.comparing(RelacaoProcesso::getOrdem, Comparator.nullsFirst(Comparator.naturalOrder()))));
                relacoes.forEach(relacaoProcesso -> {
                    if (relacaoProcesso.getProcessoFilho() != null) {
                        this.priorizado = relacaoProcesso.getPrioridade() != null;
                    }
                });
            }
        }
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getPriorizado() {
        return priorizado;
    }

    public void setPriorizado(Boolean priorizado) {
        this.priorizado = priorizado;
    }

    public Integer getQuantidadeDossies() {
        return quantidadeDossies;
    }

    public void setQuantidadeDossies(Integer quantidadeDossies) {
        this.quantidadeDossies = quantidadeDossies;
    }
}