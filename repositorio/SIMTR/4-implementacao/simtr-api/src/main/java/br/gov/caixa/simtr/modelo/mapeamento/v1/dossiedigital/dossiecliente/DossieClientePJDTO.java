package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

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
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalDossieCliente.XML_ROOT_ELEMENT_DOSSIE_CLIENTE_PJ)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
          value = ConstantesDossieDigitalDossieCliente.API_MODEL_V1_DOSSIE_CLIENTE_PJ,
          description = "Objeto utilizado para representar o dossiê cliente especializado na PJ (Pessoa Jurídica) no retorno as consultas realizadas a partir do Dossiê do Cliente",
          parent = DossieClienteDTO.class)
public class DossieClientePJDTO extends DossieClienteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlJavaTypeAdapter(value = CNPJAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalDossieCliente.CNPJ)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.CNPJ, required = true, value = "Numero do CNPJ dossiê de cliente PF. Essa informação não pode ser alterada")
    private Long cnpj;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.RAZAO_SOCIAL)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.RAZAO_SOCIAL, required = false, value = "Nome da razão social do cliente vinculado ao dossiê de cliente")
    private String razaoSocial;

    @XmlJavaTypeAdapter(value = CalendarSimpleBRAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalDossieCliente.DATA_FUNDACAO)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.DATA_FUNDACAO, required = false, value = "Data de fundação da empresa cliente vinculada ao dossiê de cliente", example = "dd/MM/yyyy")
    private Calendar dataFundacao;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.SIGLA_PORTE)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.SIGLA_PORTE, required = false, value = "Sigla do porte da empresa perante a Receita Federal")
    private PortePessoaJuridicaEnum porteSiglaEnum;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.CONGLOMERADO)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.CONGLOMERADO, required = false, value = "Utilizado para indicar se a empresa integra um conglomerado")
    private Boolean conglomerado;

    // *****************************************
    public DossieClientePJDTO() {
        super();
    }

    public DossieClientePJDTO(DossieClientePJ dossieClientePJ) {
        super(dossieClientePJ);
        this.tipoPessoa = TipoPessoaEnum.J;
        this.cnpj = dossieClientePJ.getCpfCnpj();
        this.razaoSocial = dossieClientePJ.getRazaoSocial();
        this.dataFundacao = dossieClientePJ.getDataFundacao();
        this.porteSiglaEnum = dossieClientePJ.getPorte();
        this.conglomerado = dossieClientePJ.getConglomerado();
    }

    public DossieClientePJDTO(DossieClientePJ dossieClientePJ, List<PendenciaCadastroDTO> pendenciasCadastroCaixa) {
        this(dossieClientePJ);
        this.setPendenciasCadastroDTO(pendenciasCadastroCaixa);
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

    public Boolean getConglomerado() {
        return conglomerado;
    }

    public void setConglomerado(Boolean conglomerado) {
        this.conglomerado = conglomerado;
    }
}
