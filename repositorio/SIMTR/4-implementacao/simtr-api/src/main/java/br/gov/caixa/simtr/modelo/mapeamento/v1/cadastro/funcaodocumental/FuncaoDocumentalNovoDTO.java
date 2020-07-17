package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.funcaodocumental;

import java.io.Serializable;


import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroFuncaoDocumental;
import br.gov.caixa.simtr.visao.PrototypeDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiModel(
        value = ConstantesCadastroFuncaoDocumental.API_MODEL_FUNCAO_DOCUMENTAL_NOVO,
        description = "Objeto utilizado para representar uma nova função documental no envio de inclusão sob a ótica dos cadastros."
)
public class FuncaoDocumentalNovoDTO implements Serializable, PrototypeDTO<FuncaoDocumental> {

    private static final long serialVersionUID = 1L;

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
    @JsonProperty(ConstantesCadastroFuncaoDocumental.TIPOS_DOCUMENTO_INCLUSAO_VINCULO)
    @ApiModelProperty(name = ConstantesCadastroFuncaoDocumental.TIPOS_DOCUMENTO_INCLUSAO_VINCULO, required = false, value = "Lista de identificadores de tipos de documento a serem vinculados a função documental.")
    private List<Integer> tiposDocumentoInclusaoVinculo;

    public FuncaoDocumentalNovoDTO() {
        super();
        this.tiposDocumentoInclusaoVinculo = new ArrayList<>();
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

    public List<Integer> getTiposDocumentoInclusaoVinculo() {
        return tiposDocumentoInclusaoVinculo;
    }

    public void setTiposDocumentoInclusaoVinculo(List<Integer> tiposDocumentoInclusaoVinculo) {
        this.tiposDocumentoInclusaoVinculo = tiposDocumentoInclusaoVinculo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.nome);
        hash = 41 * hash + Objects.hashCode(this.indicadorProcessoAdministrativo);
        hash = 41 * hash + Objects.hashCode(this.indicadorDossieDigital);
        hash = 41 * hash + Objects.hashCode(this.indicadorApoioNegocio);
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
        final FuncaoDocumentalNovoDTO other = (FuncaoDocumentalNovoDTO) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.indicadorProcessoAdministrativo, other.indicadorProcessoAdministrativo)) {
            return false;
        }
        if (!Objects.equals(this.indicadorDossieDigital, other.indicadorDossieDigital)) {
            return false;
        }
        return Objects.equals(this.indicadorApoioNegocio, other.indicadorApoioNegocio);
    }

    @Override
    public FuncaoDocumental prototype() {
        FuncaoDocumental funcaoDocumental = new FuncaoDocumental();
        funcaoDocumental.setNome(this.nome);
        funcaoDocumental.setUsoApoioNegocio(this.indicadorApoioNegocio);
        funcaoDocumental.setUsoDossieDigital(this.indicadorDossieDigital);
        funcaoDocumental.setUsoProcessoAdministrativo(this.indicadorProcessoAdministrativo);

        return funcaoDocumental;
    }

}