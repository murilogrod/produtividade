package br.gov.caixa.simtr.modelo.mapeamento.v1.pae.apenso;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.gov.caixa.simtr.modelo.entidade.ApensoAdministrativo;
import br.gov.caixa.simtr.modelo.enumerator.TipoApensoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CNPJAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CPFAdapter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesPAE;

@XmlRootElement(name = ConstantesPAE.XML_ROOT_ELEMENT_APENSO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesPAE.VISAO_APENSO_API_MODEL_APENSO_ADMINISTRATIVO_NOVO,
        description = "Objeto utilizado para realizar a inclusão de um Apenso Administrativo inicial limitando as possibilidades de definição dos atributos."
)
public class ApensoAdministrativoNovoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CPFAdapter.class)
    @XmlElement(name = ConstantesPAE.CPF_FORNECEDOR)
    @ApiModelProperty(name = ConstantesPAE.CPF_FORNECEDOR, required = false, value = "CPF do fornecedor caso o mesmo seja pessoa fisica.", example = "999.999.999-99")
    private String cpfFornecedor;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @XmlJavaTypeAdapter(value = CNPJAdapter.class)
    @XmlElement(name = ConstantesPAE.CNPJ_FORNECEDOR)
    @ApiModelProperty(name = ConstantesPAE.CNPJ_FORNECEDOR, required = false, value = "CNPJ do fornecedor caso o mesmo seja pessoa juridica.", example = "99.999.999/9999-99")
    private String cnpjFornecedor;

    @XmlElement(name = ConstantesPAE.TIPO_APENSO)
    @ApiModelProperty(name = ConstantesPAE.TIPO_APENSO, required = true, value = "Indicador do tipo Apenso Administrativo.")
    private TipoApensoEnum tipoApenso;

    @XmlElement(name = ConstantesPAE.DESCRICAO_APENSO)
    @ApiModelProperty(name = ConstantesPAE.DESCRICAO_APENSO, required = false, value = "Descrição livre vinculada ao Apenso Administrativo.")
    private String descricaoApenso;

    @XmlElement(name = ConstantesPAE.PROTOCOLO_SICLG)
    @ApiModelProperty(name = ConstantesPAE.PROTOCOLO_SICLG, required = true, value = "Codigo de identificação da demanda inicial junto ao SICLG. Dado informativo, não há integração entre os sistemas.")
    private String protocoloSICLG;

    @XmlElement(name = ConstantesPAE.TITULO_APENSO)
    @ApiModelProperty(name = ConstantesPAE.TITULO_APENSO, required = false, value = "Descrição do livre vinculada ao titulo de um Apenso Administrativo.")
    private String tituloApenso;

    public ApensoAdministrativoNovoDTO() {
        super();
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

    public String getProtocoloSICLG() {
        return protocoloSICLG;
    }

    public void setProtocoloSICLG(String protocoloSICLG) {
        this.protocoloSICLG = protocoloSICLG;
    }

    public String getTituloApenso() {
        return tituloApenso;
    }

    public void setTituloApenso(String tituloApenso) {
        this.tituloApenso = tituloApenso;
    }

    public ApensoAdministrativo prototype() {
        ApensoAdministrativo apensoAdministrativo = new ApensoAdministrativo();
        apensoAdministrativo.setCpfCnpjFornecedor(this.cpfFornecedor != null ? this.cpfFornecedor : this.cnpjFornecedor);
        apensoAdministrativo.setTipoApenso(this.tipoApenso);
        apensoAdministrativo.setDescricaoApenso(this.descricaoApenso);
        apensoAdministrativo.setDataHoraInclusao(Calendar.getInstance());
        apensoAdministrativo.setProtocoloSICLG(this.protocoloSICLG);
        apensoAdministrativo.setTitulo(this.tituloApenso);

        return apensoAdministrativo;
    }
}
