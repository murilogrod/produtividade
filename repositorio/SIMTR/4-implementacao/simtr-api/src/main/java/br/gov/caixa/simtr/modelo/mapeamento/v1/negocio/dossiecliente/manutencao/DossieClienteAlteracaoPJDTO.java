package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.manutencao;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePJ;
import br.gov.caixa.simtr.modelo.enumerator.PortePessoaJuridicaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarSimpleBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieClienteManutencao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieClienteManutencao.XML_ROOT_ELEMENT_DOSSIE_CLIENTE_ALTERACAO_PJ)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieClienteManutencao.API_MODEL_V1_DOSSIE_CLIENTE_ALTERACAO_PJ,
        description = "Objeto utilizado para representar o Dossiê Cliente especializado na PJ (Pessoa Juridica)",
        parent = DossieClienteAlteracaoDTO.class
)
public class DossieClienteAlteracaoPJDTO extends DossieClienteAlteracaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.RAZAO_SOCIAL)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.RAZAO_SOCIAL, value = "Nome da razão social do cliente vinculado ao dossiê de cliente.", required = false)
    private String razaoSocial;

    @XmlJavaTypeAdapter(value = CalendarSimpleBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.DATA_FUNDACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.DATA_FUNDACAO, value = "Data de fundação da empresa cliente vinculada ao dossiê de cliente.", required = false, example = "dd/MM/yyyy")
    private Calendar dataFundacao;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.SIGLA_PORTE)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.SIGLA_PORTE, value = "Sigla do porte da empresa perante a Receita Federal.", required = false)
    private PortePessoaJuridicaEnum porteSiglaEnum;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.CONGLOMERADO)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.CONGLOMERADO, value = "Utilizado para indicar se a empresa integra um conglomerado.", required = false)
    private Boolean conglomerado;

    public DossieClienteAlteracaoPJDTO() {
        super();
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public Calendar getDataFundacao() {
        return dataFundacao;
    }

    public void setDataFundacao(Calendar dataFundacao) {
        this.dataFundacao = dataFundacao;
    }

    public PortePessoaJuridicaEnum getPorteSiglaEnum() {
        return porteSiglaEnum;
    }

    public void setPorteSiglaEnum(PortePessoaJuridicaEnum porteSiglaEnum) {
        this.porteSiglaEnum = porteSiglaEnum;
    }

    public Boolean isConglomerado() {
        return conglomerado;
    }

    public void setConglomerado(Boolean conglomerado) {
        this.conglomerado = conglomerado;
    }

    @Override
    public DossieCliente prototype() {
        DossieClientePJ dossieClientePJ = new DossieClientePJ();
        dossieClientePJ.setEmail(this.email);
        dossieClientePJ.setRazaoSocial(this.razaoSocial);
        dossieClientePJ.setDataFundacao(this.dataFundacao);
        dossieClientePJ.setConglomerado(this.conglomerado);
        dossieClientePJ.setPorte(this.porteSiglaEnum);
        return dossieClientePJ;
    }
}
