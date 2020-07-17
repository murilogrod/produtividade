package br.gov.caixa.simtr.modelo.mapeamento.v1.pae.apenso;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.gov.caixa.simtr.modelo.entidade.ApensoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.ContratoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.ProcessoAdministrativo;
import br.gov.caixa.simtr.modelo.enumerator.TipoApensoEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CNPJAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CPFAdapter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesPAE;

@XmlRootElement(name = ConstantesPAE.XML_ROOT_ELEMENT_APENSO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesPAE.VISAO_APENSO_API_MODEL_APENSO_ADMINISTRATIVO_MANUTENCAO,
        description = "Objeto utilizado para aplicar ajustes em um Apenso Administrativo modificando as definições iniciais dos atributos do registro."
)
public class ApensoAdministrativoManutencaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlElement(name = ConstantesPAE.ID_PROCESSO_VINCULADO)
    @ApiModelProperty(name = ConstantesPAE.ID_PROCESSO_VINCULADO, required = false, value = "Valor que representa o identificador do Processo Adminstrativo. Utilizado para alterar a vinculação. Caso este atributo seja preenchido o id_processo_vinculado não deverá ser enviado.")
    private Long idProcessoAdministrativo;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlElement(name = ConstantesPAE.ID_CONTRATO_VINCULADO)
    @ApiModelProperty(name = ConstantesPAE.ID_CONTRATO_VINCULADO, required = false, value = "Valor que representa o identificador do Contrato Adminstrativo. Utilizado alterar a vinculação. Caso este atributo seja preenchido o id_processo_vinculado não deverá ser enviado.")
    private Long idContratoAdministrativo;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CPFAdapter.class)
    @XmlElement(name = ConstantesPAE.CPF_FORNECEDOR)
    @ApiModelProperty(name = ConstantesPAE.CPF_FORNECEDOR, required = false, value = "CPF do fornecedor caso o mesmo seja pessoa fisica.", example = "999.999.999-99")
    private String cpfFornecedor;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CNPJAdapter.class)
    @XmlElement(name = ConstantesPAE.CNPJ_FORNECEDOR)
    @ApiModelProperty(name = ConstantesPAE.CNPJ_FORNECEDOR, required = false, value = "CNPJ do fornecedor caso o mesmo seja pessoa fisica.", example = "99.999.999/9999-99")
    private String cnpjFornecedor;

    @XmlElement(name = ConstantesPAE.TIPO_APENSO)
    @ApiModelProperty(name = ConstantesPAE.TIPO_APENSO, required = true, value = "Indicador do tipo Apenso Administrativo.")
    private TipoApensoEnum tipoApenso;

    @XmlElement(name = ConstantesPAE.DESCRICAO_APENSO)
    @ApiModelProperty(name = ConstantesPAE.DESCRICAO_APENSO, required = false, value = "Descrição do livre vinculada ao Apenso Administrativo.")
    private String descricaoApenso;

    @XmlElement(name = ConstantesPAE.TITULO_APENSO)
    @ApiModelProperty(name = ConstantesPAE.TITULO_APENSO, required = false, value = "Descrição do livre vinculada ao titulo de um Apenso Administrativo.")
    private String tituloApenso;

    @XmlElement(name = ConstantesPAE.PROTOCOLO_SICLG)
    @ApiModelProperty(name = ConstantesPAE.PROTOCOLO_SICLG, required = true, value = "Codigo de identificação da demanda inicial junto ao SICLG. Dado informativo, não há integração entre os sistemas.")
    private String protocoloSICLG;

    public ApensoAdministrativoManutencaoDTO() {
        super();
    }

    public ApensoAdministrativoManutencaoDTO(ApensoAdministrativo apenso) {
        this();

        if (ConstantesUtil.TAMANHO_CPF.equals(apenso.getCpfCnpjFornecedor().length())) {
            this.cpfFornecedor = apenso.getCpfCnpjFornecedor();
        } else if (ConstantesUtil.TAMANHO_CNPJ.equals(apenso.getCpfCnpjFornecedor().length())) {
            this.cnpjFornecedor = apenso.getCpfCnpjFornecedor();
        }
        this.tipoApenso = apenso.getTipoApenso();
        this.descricaoApenso = apenso.getDescricaoApenso();
        this.tituloApenso = apenso.getTitulo();
        this.protocoloSICLG = apenso.getProtocoloSICLG();

    }

    public Long getIdProcessoAdministrativo() {
        return idProcessoAdministrativo;
    }

    public void setIdProcessoAdministrativo(Long idProcessoAdministrativo) {
        this.idProcessoAdministrativo = idProcessoAdministrativo;
    }

    public Long getIdContratoAdministrativo() {
        return idContratoAdministrativo;
    }

    public void setIdContratoAdministrativo(Long idContratoAdministrativo) {
        this.idContratoAdministrativo = idContratoAdministrativo;
    }

    public String getCpfFornecedor() {
        return cpfFornecedor;
    }

    public void setCpfFornecedor(String cpfFornecedor) {
        this.cpfFornecedor = cpfFornecedor;
    }

    public String getCnpjFornecedor() {
        return cnpjFornecedor;
    }

    public void setCnpjFornecedor(String cnpjFornecedor) {
        this.cnpjFornecedor = cnpjFornecedor;
    }

    public TipoApensoEnum getTipoApenso() {
        return tipoApenso;
    }

    public void setTipoApenso(TipoApensoEnum tipoApenso) {
        this.tipoApenso = tipoApenso;
    }

    public String getDescricaoApenso() {
        return descricaoApenso;
    }

    public void setDescricaoApenso(String descricaoApenso) {
        this.descricaoApenso = descricaoApenso;
    }

    public String getTituloApenso() {
        return tituloApenso;
    }

    public void setTituloApenso(String tituloApenso) {
        this.tituloApenso = tituloApenso;
    }

    public String getProtocoloSICLG() {
        return protocoloSICLG;
    }

    public void setProtocoloSICLG(String protocoloSICLG) {
        this.protocoloSICLG = protocoloSICLG;
    }

    public ApensoAdministrativo prototype() {
        ApensoAdministrativo apensoAdministrativo = new ApensoAdministrativo();

        if (this.idProcessoAdministrativo != null) {
            ProcessoAdministrativo processoAdministrativo = new ProcessoAdministrativo();
            processoAdministrativo.setId(this.idProcessoAdministrativo);

            apensoAdministrativo.setProcessoAdministrativo(processoAdministrativo);
        }

        if (this.idContratoAdministrativo != null) {
            ContratoAdministrativo contrato = new ContratoAdministrativo();
            contrato.setId(this.idContratoAdministrativo);

            apensoAdministrativo.setContratoAdministrativo(contrato);
        }

        apensoAdministrativo.setCpfCnpjFornecedor(this.cpfFornecedor != null ? this.cpfFornecedor : this.cnpjFornecedor);
        apensoAdministrativo.setTipoApenso(this.tipoApenso);
        apensoAdministrativo.setDescricaoApenso(this.descricaoApenso);
        apensoAdministrativo.setTitulo(this.tituloApenso);
        apensoAdministrativo.setProtocoloSICLG(this.protocoloSICLG);

        return apensoAdministrativo;
    }
}
