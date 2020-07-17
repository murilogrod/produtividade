package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dashboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDashboard;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDashboard.XML_ROOT_ELEMENT_RETORNO_PESSOAL_VISAO_UNIDADE)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDashboard.API_MODEL_V1_RETORNO_PESSOAL_VISAO_UNIDADE,
        description = "Objeto utilizado para representar as informações apresentar no dashboar do usuário em sua visão pessoal sob a ótica Apoio ao Negocio"
)
public class RetornoPessoalVisaoUnidadeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElementWrapper(name = ConstantesNegocioDashboard.DOSSIES_PRODUTO)
    @XmlElement(name = ConstantesNegocioDashboard.DOSSIE_PRODUTO)
    @JsonProperty(value = ConstantesNegocioDashboard.DOSSIES_PRODUTO)
    @ApiModelProperty(name = ConstantesNegocioDashboard.DOSSIES_PRODUTO, required = true, value = "Lista de dossiês de produto vinculados a unidade do usuário.")
    private List<DossieProdutoDTO> dossiesProdutoDTO;

    @XmlElement(name = ConstantesNegocioDashboard.RESUMO_SITUACAO)
    @ApiModelProperty(name = ConstantesNegocioDashboard.RESUMO_SITUACAO, required = true, value = "Resumo de Dossiês de produto agrupados por situação.")
    private Map<String, Integer> mapaDossiesSituacao;

    @XmlElement(name = ConstantesNegocioDashboard.MENSAGEM)
    @ApiModelProperty(name = ConstantesNegocioDashboard.MENSAGEM, required = true, value = "Mensagem a ser apresentada ao usuário indicando alguma situação especifica na visualização do dashboard.")
    private String mensagem;

    private final List<String> situacoesBPM = Arrays.asList(
            SituacaoDossieEnum.ALIMENTACAO_FINALIZADA.name(),
            SituacaoDossieEnum.CONFORME.name(),
            SituacaoDossieEnum.CRIADO.name(),
            SituacaoDossieEnum.SEGURANCA_FINALIZADO.name()
    );

    public RetornoPessoalVisaoUnidadeDTO() {
        super();
        this.dossiesProdutoDTO = new ArrayList<>();
        this.mapaDossiesSituacao = new HashMap<>();
        for (SituacaoDossieEnum situacaoDossieEnum : SituacaoDossieEnum.values()) {
            this.mapaDossiesSituacao.put(situacaoDossieEnum.getDescricao(), 0);
        }
    }

    public List<DossieProdutoDTO> getDossiesProdutoDTO() {
        return dossiesProdutoDTO;
    }

    public void setDossiesProdutoDTO(List<DossieProdutoDTO> dossiesProdutoDTO) {
        this.dossiesProdutoDTO = dossiesProdutoDTO;
    }

    public Map<String, Integer> getMapaDossiesSituacao() {
        return mapaDossiesSituacao;
    }

    public void setMapaDossiesSituacao(Map<String, Integer> mapaDossiesSituacao) {
        this.mapaDossiesSituacao = mapaDossiesSituacao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void addDossieProdutoDTO(DossieProdutoDTO... dossiesProdutoDTO) {
        this.dossiesProdutoDTO.addAll(Arrays.asList(dossiesProdutoDTO));
    }

    public void removeDossieProdutoDTO(DossieProdutoDTO... dossiesProdutoDTO) {
        this.dossiesProdutoDTO.removeAll(Arrays.asList(dossiesProdutoDTO));
    }

    public void addBySituacao(String nomeSituacaoDossie) {
        if (situacoesBPM.contains(nomeSituacaoDossie)) {
            this.mensagem = "Existem dossiês com pendência de atuação do BPM. Necessario contactar o suporte da ferramenta.";
        }

        Integer qtde = this.mapaDossiesSituacao.get(nomeSituacaoDossie);
        if (qtde == null) {
            this.mapaDossiesSituacao.put(nomeSituacaoDossie, 1);
        } else {
            this.mapaDossiesSituacao.put(nomeSituacaoDossie, ++qtde);
        }
    }
}
