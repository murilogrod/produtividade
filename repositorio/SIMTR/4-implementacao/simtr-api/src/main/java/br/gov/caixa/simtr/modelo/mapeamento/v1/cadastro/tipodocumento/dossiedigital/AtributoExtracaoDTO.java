package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tipodocumento.dossiedigital;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoDocumento;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesCadastroTipoDocumento.XML_ROOT_ELEMENT_ATRIBUTO_EXTRACAO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesCadastroTipoDocumento.API_MODEL_ATRIBUTO_EXTRACAO__DOSSIEDIGITAL,
        description = "Objeto utilizado para representar um atributo previsto (chamado de atributo extração) vinculada a um tipo de documento nas consultas realizadas por sistemas terceiros que não compoem a plataforma."
)
public class AtributoExtracaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME_DOCUMENTO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_DOCUMENTO, required = true, value = "Indica o nome do atributo identificado pela integração com outros sistemas")
    private String nomeDocumento;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_SICLI)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.NOME_ATRIBUTO_SICLI, required = true, value = "Indica o nome do atributo enviado ao SICLI no momento do envio da informação")
    private String nomeAtributoSicli;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.INDICADOR_OBRIGATORIO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.INDICADOR_OBRIGATORIO, required = true, value = "Indica se esta informação é de captura obrigatorio para o tipo de documento associado")
    private boolean obrigatorio;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.TIPO_ATRIBUTO, required = false, value = "Indica o tipo informação que representa o referido atributo perante o documento")
    private TipoAtributoEnum tipoAtributoEnum;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.GRUPO_ATRIBUTO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.GRUPO_ATRIBUTO, required = false, value = "Indica o grupo ao qual o atributo faz parte. Caso este atributo faça parte de um grupo, todos os demais atributos do grupo deverão ser informados, caso ao menos um deles seja informado")
    private Integer grupoAtributo;

    @JsonProperty(value = ConstantesCadastroTipoDocumento.VALOR_PADRAO)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.VALOR_PADRAO, required = false, value = "Indica o o valor padrão a ser atribuido no registro caso o mesmo não seja informado.")
    private String valorPadrao;
    
    @JsonProperty(value = ConstantesCadastroTipoDocumento.OPCOES)
    @XmlElement(name = ConstantesCadastroTipoDocumento.OPCAO)
    @XmlElementWrapper(name = ConstantesCadastroTipoDocumento.OPCOES)
    @ApiModelProperty(name = ConstantesCadastroTipoDocumento.OPCOES, required = false, value = "Lista de opções de valores previstos, associadas ao atributo definido para o tipo de documento")
    private List<OpcaoAtributoDTO> opcoesAtributoDTO;

    public AtributoExtracaoDTO() {
        super();
        this.opcoesAtributoDTO = new ArrayList<>();
    }

    public AtributoExtracaoDTO(AtributoExtracao atributoExtracao) {
        this();
        if (atributoExtracao != null) {
            this.nomeDocumento = atributoExtracao.getNomeAtributoDocumento();
            this.nomeAtributoSicli = atributoExtracao.getNomeAtributoSICLI();
            this.obrigatorio = Objects.isNull(atributoExtracao.getObrigatorio()) ? Boolean.FALSE : atributoExtracao.getObrigatorio();
            this.tipoAtributoEnum = atributoExtracao.getTipoAtributoGeralEnum();
            this.grupoAtributo = atributoExtracao.getGrupoAtributo();
            this.valorPadrao = atributoExtracao.getValorPadrao();
            if(atributoExtracao.getOpcoesAtributo() != null) {
                atributoExtracao.getOpcoesAtributo().forEach(opcao -> {
                    this.opcoesAtributoDTO.add(new OpcaoAtributoDTO(opcao.getDescricaoOpcao(), opcao.getValorOpcao()));                   
                });
            }
        }
    }

    public String getNomeDocumento() {
        return nomeDocumento;
    }

    public void setNomeDocumento(String nomeDocumento) {
        this.nomeDocumento = nomeDocumento;
    }

    public String getNomeAtributoSicli() {
        return nomeAtributoSicli;
    }

    public void setNomeAtributoSicli(String nomeAtributoSicli) {
        this.nomeAtributoSicli = nomeAtributoSicli;
    }

    public boolean isObrigatorio() {
        return obrigatorio;
    }

    public void setObrigatorio(boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public TipoAtributoEnum getTipoAtributoEnum() {
        return tipoAtributoEnum;
    }

    public void setTipoAtributoEnum(TipoAtributoEnum tipoAtributoEnum) {
        this.tipoAtributoEnum = tipoAtributoEnum;
    }

    public Integer getGrupoAtributo() {
        return grupoAtributo;
    }

    public void setGrupoAtributo(Integer grupoAtributo) {
        this.grupoAtributo = grupoAtributo;
    }

    public String getValorPadrao() {
        return valorPadrao;
    }

    public void setValorPadrao(String valorPadrao) {
        this.valorPadrao = valorPadrao;
    }
}
