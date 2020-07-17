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
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CNPJAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarSimpleBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieClienteManutencao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieClienteManutencao.XML_ROOT_ELEMENT_DOSSIE_CLIENTE_INCLUSAO_PJ)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieClienteManutencao.API_MODEL_V1_DOSSIE_CLIENTE_INCLUSAO_PJ,
        description = "Objeto utilizado para representar o Dossiê Cliente a ser cadastraddo especializado na PJ (Pessoa Juridica)",
        parent = DossieClienteInclusaoDTO.class
)
public class DossieClienteInclusaoPJDTO extends DossieClienteInclusaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlJavaTypeAdapter(value = CNPJAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.CNPJ)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.CNPJ, required = true, value = "Número do CNPJ do cliente vinculado ao dossiê de cliente PJ")
    private Long cnpj;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.RAZAO_SOCIAL)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.RAZAO_SOCIAL, required = true, value = "Nome da razão social do cliente")
    private String razaoSocial;

    @XmlJavaTypeAdapter(value = CalendarSimpleBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.DATA_FUNDACAO)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.DATA_FUNDACAO, required = true, value = "Data de fundação da empresa cliente", example = "dd/MM/yyyy")
    private Calendar dataFundacao;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.SIGLA_PORTE)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.SIGLA_PORTE, value = "Sigla do porte da empresa perante a Receita Federal.", required = false)
    private PortePessoaJuridicaEnum porteSiglaEnum;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.CONGLOMERADO, nillable = true)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.CONGLOMERADO, value = "Utilizado para indicar se a empresa integra um conglomerado.", required = false)
    private Boolean conglomerado;

    public DossieClienteInclusaoPJDTO() {
        super();
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
        dossieClientePJ.setNome(this.nome);
        dossieClientePJ.setCpfCnpj(this.cnpj);
        dossieClientePJ.setEmail(this.getEmail());
        dossieClientePJ.setRazaoSocial(this.razaoSocial);
        dossieClientePJ.setDataFundacao(this.dataFundacao);
        dossieClientePJ.setConglomerado(this.conglomerado);
        dossieClientePJ.setPorte(this.porteSiglaEnum);
        return dossieClientePJ;
    }
}
