package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.complementacao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.RelacaoProcesso;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProdutoManutencao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProdutoManutencao.XML_ROOT_ELEMENT_PROCESSO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProdutoManutencao.API_MODEL_V1_PROCESSO,
        description = "Objeto utilizado para representar o Processo de vinculação e das fases de dossiê de produto no retorno as consultas realizadas para a complementação de dados."
)
public class ProcessoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.ID)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.ID, required = true, value = "Codigo de identificação do processo.")
    private Integer id;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.NOME)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.NOME, required = true, value = "Nome de identificação do processo.")
    private String nome;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.AVATAR)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.AVATAR, required = true, value = "Identificação do nome da icone que representa o processo.")
    private String avatar;

    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.QUANTIDADE_DOSSIES)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.QUANTIDADE_DOSSIES, required = true, value = "Indica a quantiadade de dossi~es de produto na situação \"Aguardando Tratamento\" na realização da consulta.")
    private Integer quantidadeDossies;

    // *********************************************
    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.UNIDADES_AUTORIZADAS)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.UNIDADE_AUTORIZADA)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.UNIDADES_AUTORIZADAS)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.UNIDADES_AUTORIZADAS, required = false, value = "Lista de objetos que representam as autorizações de ação neste processo.")
    private List<Integer> unidadesAutorizadas;

    @XmlElementWrapper(name = ConstantesNegocioDossieProdutoManutencao.PROCESSOS_FILHO)
    @XmlElement(name = ConstantesNegocioDossieProdutoManutencao.PROCESSO_FILHO)
    @JsonProperty(value = ConstantesNegocioDossieProdutoManutencao.PROCESSOS_FILHO)
    @ApiModelProperty(name = ConstantesNegocioDossieProdutoManutencao.PROCESSOS_FILHO, required = false, value = "Lista de processos filho vinculados neste processo.", dataType = "Void.class")
    private List<ProcessoDTO> processosFilhoDTO;

    public ProcessoDTO() {
        super();
        this.quantidadeDossies = 0;
        this.unidadesAutorizadas = new ArrayList<>();
        this.processosFilhoDTO = new ArrayList<>();
    }

    public ProcessoDTO(Processo processo) {
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

            if (processo.getUnidadesAutorizadas() != null) {
                processo.getUnidadesAutorizadas()
                        .forEach(unidadeAutorizada -> this.unidadesAutorizadas.add(unidadeAutorizada.getUnidade()));
            }

            if (processo.getRelacoesProcessoVinculoPai() != null) {
                List<RelacaoProcesso> relacoes = Arrays.asList(processo.getRelacoesProcessoVinculoPai().toArray(new RelacaoProcesso[processo.getRelacoesProcessoVinculoPai().size()]));
                relacoes.sort(Comparator.nullsFirst(Comparator.comparing(RelacaoProcesso::getOrdem, Comparator.nullsFirst(Comparator.naturalOrder()))));
                relacoes.forEach(relacaoProcesso -> {
                    if (relacaoProcesso.getProcessoFilho() != null) {
                        this.processosFilhoDTO.add(new ProcessoDTO(relacaoProcesso.getProcessoFilho()));
                    }
                });
            }

            if (this.quantidadeDossies == 0) {
                this.quantidadeDossies = this.processosFilhoDTO.stream().mapToInt(p -> p.getQuantidadeDossies()).sum();
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

    public List<Integer> getUnidadesAutorizadas() {
        return unidadesAutorizadas;
    }

    public void setUnidadesAutorizadas(List<Integer> unidadesAutorizadas) {
        this.unidadesAutorizadas = unidadesAutorizadas;
    }

    public Integer getQuantidadeDossies() {
        return quantidadeDossies;
    }

    public void setQuantidadeDossies(Integer quantidadeDossies) {
        this.quantidadeDossies = quantidadeDossies;
    }

    public List<ProcessoDTO> getProcessosFilhoDTO() {
        return processosFilhoDTO;
    }

    public void setProcessosFilhoDTO(List<ProcessoDTO> processosFilhoDTO) {
        this.processosFilhoDTO = processosFilhoDTO;
    }
}
