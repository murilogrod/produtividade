package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.processo;

import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroProcesso;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

@ApiModel(
        value = ConstantesCadastroProcesso.API_MODEL_PROCESSO,
        description = "Objeto utilizado para representar um processo nas consultas realizadas sob a ótica dos cadastros."
)
public class ProcessoNovoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesCadastroProcesso.NOME_PROCESSO)
    @ApiModelProperty(name = ConstantesCadastroProcesso.NOME_PROCESSO, required = true, value = "Atributo utilizado para armazenar o nome de identificação negocial do processo.")
    protected String nomeProcesso;

    @JsonProperty(value = ConstantesCadastroProcesso.NOME_PROCESSO_BPM)
    @ApiModelProperty(name = ConstantesCadastroProcesso.NOME_PROCESSO_BPM, required = false, value = "Atributo que armazena o valor de identificação do processo orignador junto a solução de BPM.")
    protected String nomeProcessoBpm;
    
    @JsonProperty(value = ConstantesCadastroProcesso.NOME_CONTAINER_BPM)
    @ApiModelProperty(name = ConstantesCadastroProcesso.NOME_CONTAINER_BPM, required = false, value = "Atributo que armazena o valor de identificação do container utilizado no agrupamento dos processos junto a solução de BPM que possui o processo originador vinculado.")
    protected String nomeContainerBpm;
    
    @JsonProperty(value = ConstantesCadastroProcesso.NOME_AVATAR)
    @ApiModelProperty(name = ConstantesCadastroProcesso.NOME_AVATAR, required = false, value = "Atributo utilizado para armazenar o nome do avatar que será disponibilizado no pacote da ineterface grafica para montagem e apresentação das filas de captura o uinformações do processo.")
    protected String nomeAvatar;
    
    @JsonProperty(value = ConstantesCadastroProcesso.INDICADOR_ATIVO)
    @ApiModelProperty(name = ConstantesCadastroProcesso.INDICADOR_ATIVO, required = true, value = "Atributo que indica que se o processo esta ativo ou não para utilização pelo sistema.")
    protected Boolean indicadorAtivo;
    
    @JsonProperty(value = ConstantesCadastroProcesso.INDICADOR_DOSSIE_DIGITAL)
    @ApiModelProperty(name = ConstantesCadastroProcesso.INDICADOR_DOSSIE_DIGITAL, required = true, value = "Atributo utilizado para identificar os processos que podem ter vinculação de dossiês de produto. Ao navegar na árvore de processo, ao chegar a um processo que possa ter vinculação de dossiê, o sistema apresenta as opções de inclusão de dossiê para o processo, considerando a parametrização do processo.")
    protected Boolean dossieDigital;
    
    @JsonProperty(value = ConstantesCadastroProcesso.INDICADOR_CONTROLA_VALIDADE_DOCUMENTO)
    @ApiModelProperty(name = ConstantesCadastroProcesso.INDICADOR_CONTROLA_VALIDADE_DOCUMENTO, required = true, value = "Atributo utilizado para identificar se os dossiês vinculados a este tipo de processo deverão ter os registros de documentos com a validade controlada.")
    protected Boolean controlaValidadeDocumento;
 
    @JsonProperty(value = ConstantesCadastroProcesso.INDICADOR_TIPO_PESSOA)
    @ApiModelProperty(name = ConstantesCadastroProcesso.INDICADOR_TIPO_PESSOA, required = true, value = "Atributo que determina qual tipo de pessoa pode ter o processo atribuído.")
    protected TipoPessoaEnum tipoPessoa;    
    
    // *********************************************
    public ProcessoNovoDTO() {
        super();
    }

    public ProcessoNovoDTO(Processo processo) {
        this();
        if (Objects.nonNull(processo)) {
            this.nomeProcesso = processo.getNome();
            this.controlaValidadeDocumento = processo.getControlaValidade();
            this.dossieDigital = processo.getIndicadorGeracaoDossie();
            this.nomeAvatar = processo.getAvatar();
            this.nomeContainerBpm = processo.getNomeContainerBPM();
            this.nomeProcessoBpm = processo.getNomeProcessoBPM();
            this.tipoPessoa = processo.getTipoPessoa();
            this.indicadorAtivo = processo.getAtivo();
        }
    }

    public String getNomeProcesso() {
        return nomeProcesso;
    }

    public void setNomeProcesso(String nomeProcesso) {
        this.nomeProcesso = nomeProcesso;
    }

    public String getNomeProcessoBpm() {
        return nomeProcessoBpm;
    }

    public void setNomeProcessoBpm(String nomeProcessoBpm) {
        this.nomeProcessoBpm = nomeProcessoBpm;
    }

    public String getNomeContainerBpm() {
        return nomeContainerBpm;
    }

    public void setNomeContainerBpm(String nomeContainerBpm) {
        this.nomeContainerBpm = nomeContainerBpm;
    }

    public String getNomeAvatar() {
        return nomeAvatar;
    }

    public void setNomeAvatar(String nomeAvatar) {
        this.nomeAvatar = nomeAvatar;
    }

    public Boolean getIndicadorAtivo() {
        return indicadorAtivo;
    }

    public void setIndicadorAtivo(Boolean indicadorAtivo) {
        this.indicadorAtivo = indicadorAtivo;
    }

    public Boolean getDossieDigital() {
        return dossieDigital;
    }

    public void setDossieDigital(Boolean dossieDigital) {
        this.dossieDigital = dossieDigital;
    }

    public Boolean getControlaValidadeDocumento() {
        return controlaValidadeDocumento;
    }

    public void setControlaValidadeDocumento(Boolean controlaValidadeDocumento) {
        this.controlaValidadeDocumento = controlaValidadeDocumento;
    }

    public TipoPessoaEnum getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoaEnum tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

   

    public Processo prototype() {
        Processo processo = new Processo();
        processo.setNome(this.nomeProcesso);
        processo.setAtivo(this.indicadorAtivo);
        processo.setAvatar(this.nomeAvatar);
        processo.setNome(this.nomeProcesso);
        processo.setNomeContainerBPM(this.nomeContainerBpm);
        processo.setNomeProcessoBPM(this.nomeProcessoBpm);
        processo.setTipoPessoa(this.tipoPessoa);
        processo.setControlaValidade(this.controlaValidadeDocumento);
        return processo;
    }
    
}
