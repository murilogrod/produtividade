package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.gov.caixa.simtr.modelo.entidade.DossieClientePJ;
import br.gov.caixa.simtr.modelo.enumerator.PortePessoaJuridicaEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CNPJAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarSimpleBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProduto.XML_ROOT_ELEMENT_DOSSIE_CLIENTE_PJ)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProduto.API_MODEL_V1_DOSSIE_CLIENTE_PJ,
        description = "Objeto utilizado para representar o Dossiê Cliente especializado na PJ (Pessoa Juridica) no retorno as consultas realizadas sob a ótica Apoio ao Negocio a partir do Dossiê de Produto.",
        parent = DossieClienteDTO.class
)
public class DossieClientePJDTO extends DossieClienteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProduto.CNPJ)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.CNPJ, value = "Número do CNPJ do cliente vinculado ao dossiê de cliente PJ.", required = true)
    @XmlJavaTypeAdapter(value = CNPJAdapter.class)
    private Long cnpj;

    @XmlElement(name = ConstantesNegocioDossieProduto.RAZAO_SOCIAL)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.RAZAO_SOCIAL, value = "Nome da razão social do cliente vinculado ao dossiê de cliente.", required = true)
    private String razaoSocial;

    @XmlJavaTypeAdapter(value = CalendarSimpleBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProduto.DATA_FUNDACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.DATA_FUNDACAO, value = "Data de fundação da empresa cliente vinculada ao dossiê de cliente.", required = true, example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataFundacao;

    @XmlElement(name = ConstantesNegocioDossieProduto.PORTE)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.PORTE, value = "Indicador do porte em que o CNPJ vinculado ao dossiê de cliente esta enquadrado junto a receita federal.", required = true)
    private PortePessoaJuridicaEnum porte;

    @XmlElement(name = ConstantesNegocioDossieProduto.CONGLOMERADO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.CONGLOMERADO, value = "Valor conglomerado do dossiê de cliente.", required = false)
    private Boolean conglomerado;

    public DossieClientePJDTO() {
        super();
    }

    public DossieClientePJDTO(DossieClientePJ dossieClientePJ) {
        super(dossieClientePJ);
        this.tipoPessoa = TipoPessoaEnum.J;
        if (dossieClientePJ != null) {
            this.cnpj = dossieClientePJ.getCpfCnpj();
            this.razaoSocial = dossieClientePJ.getRazaoSocial();
            this.dataFundacao = dossieClientePJ.getDataFundacao();
            this.porte = dossieClientePJ.getPorte();
            this.conglomerado = dossieClientePJ.getConglomerado();
        }
    }

    public Long getCnpj() {
        return cnpj;
    }

    public void setCnpj(Long cnpj) {
        this.cnpj = cnpj;
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

    public PortePessoaJuridicaEnum getPorte() {
        return porte;
    }

    public void setPorte(PortePessoaJuridicaEnum porte) {
        this.porte = porte;
    }

    public Boolean getConglomerado() {
	return conglomerado;
    }

    public void setConglomerado(Boolean conglomerado) {
	this.conglomerado = conglomerado;
    }
}
