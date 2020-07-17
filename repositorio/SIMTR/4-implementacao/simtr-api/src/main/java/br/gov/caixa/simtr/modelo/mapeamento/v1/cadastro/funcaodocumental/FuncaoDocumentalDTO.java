package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.funcaodocumental;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroFuncaoDocumental;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiModel(
        value = ConstantesCadastroFuncaoDocumental.API_MODEL_FUNCAO_DOCUMENTAL,
        description = "Objeto utilizado para representar uma função documental nas consultas realizadas sob a ótica dos cadastros."
)
public class FuncaoDocumentalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(ConstantesCadastroFuncaoDocumental.IDENTIFICADOR_FUNCAO_DOCUMENTAL)
    @ApiModelProperty(name = ConstantesCadastroFuncaoDocumental.IDENTIFICADOR_FUNCAO_DOCUMENTAL, required = true, value = "Atributo que representa a chave primaria da entidade.")
    private Integer id;

    @JsonProperty(ConstantesCadastroFuncaoDocumental.NOME_FUNCAO_DOCUMENTAL)
    @ApiModelProperty(name = ConstantesCadastroFuncaoDocumental.NOME_FUNCAO_DOCUMENTAL, value = "Atributo definido para armazenar o nome da função documental.", required = true)
    private String nome;

    @JsonProperty(ConstantesCadastroFuncaoDocumental.INDICADOR_PROCESSO_ADMINISTRATIVO)
    @ApiModelProperty(name = ConstantesCadastroFuncaoDocumental.INDICADOR_PROCESSO_ADMINISTRATIVO, value = "Atributo utilizado para identificar se a função faz utilização perante o Processo Administrativo Eletronico (PAE).")
    private Boolean indicadorProcessoAdministrativo;

    @JsonProperty(ConstantesCadastroFuncaoDocumental.INDICADOR_DOSSIE_DIGITAL)
    @ApiModelProperty(name = ConstantesCadastroFuncaoDocumental.INDICADOR_DOSSIE_DIGITAL, value = "Atributo utilizado para identificar se a função documental faz utilização perante o Dossiê Digital.")
    private Boolean indicadorDossieDigital;

    @JsonProperty(ConstantesCadastroFuncaoDocumental.INDICADOR_APOIO_NEGOCIO)
    @ApiModelProperty(name = ConstantesCadastroFuncaoDocumental.INDICADOR_APOIO_NEGOCIO, value = "Atributo utilizado para identificar se a função documental faz utilização perante o Apoio ao Negocio.")
    private Boolean indicadorApoioNegocio;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(ConstantesCadastroFuncaoDocumental.TIPOS_DOCUMENTO)
    @ApiModelProperty(name = ConstantesCadastroFuncaoDocumental.TIPOS_DOCUMENTO, required = false, value = "Lista de tipos de documento associados a função documental.")
    private List<TipoDocumentoDTO> tiposDocumentoDTO;
    
    @JsonProperty(ConstantesCadastroFuncaoDocumental.DATA_HORA_ULTIMA_ALTERACAO)
    @ApiModelProperty(name = ConstantesCadastroFuncaoDocumental.DATA_HORA_ULTIMA_ALTERACAO, value = "Atributo que carrega a data e hora da última atualização deste registro.")
    private String dataHoraUltimaAlteracao;

    public FuncaoDocumentalDTO() {
        super();
        this.tiposDocumentoDTO = new ArrayList<>();
    }

    public FuncaoDocumentalDTO(FuncaoDocumental funcaoDocumental, boolean incluirTiposDocumento) {
        this();
        this.id = funcaoDocumental.getId();
        this.nome = funcaoDocumental.getNome();
        this.indicadorApoioNegocio = funcaoDocumental.getUsoApoioNegocio();
        this.indicadorDossieDigital = funcaoDocumental.getUsoDossieDigital();
        this.indicadorProcessoAdministrativo = funcaoDocumental.getUsoProcessoAdministrativo();
        this.dataHoraUltimaAlteracao = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(funcaoDocumental.getDataHoraUltimaAlteracao().getTime());

        if (!incluirTiposDocumento) {
            this.tiposDocumentoDTO = null;
        } else if (Objects.nonNull(funcaoDocumental.getTiposDocumento())) {
            funcaoDocumental.getTiposDocumento().stream()
                    .forEach(tipoDocumento -> this.tiposDocumentoDTO.add(new TipoDocumentoDTO(tipoDocumento)));
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

    public Boolean getIndicadorProcessoAdministrativo() {
        return indicadorProcessoAdministrativo;
    }

    public void setIndicadorProcessoAdministrativo(Boolean indicadorProcessoAdministrativo) {
        this.indicadorProcessoAdministrativo = indicadorProcessoAdministrativo;
    }

    public Boolean getIndicadorDossieDigital() {
        return indicadorDossieDigital;
    }

    public void setIndicadorDossieDigital(Boolean indicadorDossieDigital) {
        this.indicadorDossieDigital = indicadorDossieDigital;
    }

    public Boolean getIndicadorApoioNegocio() {
        return indicadorApoioNegocio;
    }

    public void setIndicadorApoioNegocio(Boolean indicadorApoioNegocio) {
        this.indicadorApoioNegocio = indicadorApoioNegocio;
    }

    public List<TipoDocumentoDTO> getTiposDocumentoDTO() {
        return tiposDocumentoDTO;
    }

    public void setTiposDocumentoDTO(List<TipoDocumentoDTO> tiposDocumentoDTO) {
        this.tiposDocumentoDTO = tiposDocumentoDTO;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FuncaoDocumentalDTO other = (FuncaoDocumentalDTO) obj;
        return Objects.equals(this.id, other.id);
    }
}
