package br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.dossiecliente;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.gov.caixa.simtr.modelo.entidade.DossieClientePF;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CPFAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarSimpleBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesDossieDigitalDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesDossieDigitalDossieCliente.XML_ROOT_ELEMENT_DOSSIE_CLIENTE_PF)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesDossieDigitalDossieCliente.API_MODEL_V1_DOSSIE_CLIENTE_PF,
        description = "Objeto utilizado para representar o dossiê cliente especializado na PF (Pessoa Fisica)no retorno as consultas realizadas a partir do Dossiê do Cliente",
        parent = DossieClienteDTO.class
)
public class DossieClientePFDTO extends DossieClienteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlJavaTypeAdapter(value = CPFAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalDossieCliente.CPF)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.CPF, required = true, value = "Numero do CPF dossiê de cliente PF. Essa informação não pode ser alterada")
    private Long cpf;

    @XmlJavaTypeAdapter(value = CalendarSimpleBRAdapter.class)
    @XmlElement(name = ConstantesDossieDigitalDossieCliente.DATA_NASCIMENTO)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.DATA_NASCIMENTO, required = false, value = "Data de nascimento do cliente vinculado ao dossiê de cliente", example = "dd/MM/yyyy")
    private Calendar dataNascimento;

    @XmlElement(name = ConstantesDossieDigitalDossieCliente.NOME_MAE)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.NOME_MAE, required = false, value = "Nome da mãe do cliente vinculado ao dossiê de cliente")
    private String nomeMae;
    
    @XmlElement(name = ConstantesDossieDigitalDossieCliente.SITUACAO_RECEITA)
    @ApiModelProperty(name = ConstantesDossieDigitalDossieCliente.SITUACAO_RECEITA, required = false, value = "Situação do CPF perante a consulta realizada a Receita Federal", example = "0:REGULAR")
    private String situacaoReceita;

    // *****************************************
    public DossieClientePFDTO() {
        super();
    }

    public DossieClientePFDTO(DossieClientePF dossieClientePF) {
        super(dossieClientePF);
        this.tipoPessoa = TipoPessoaEnum.F;
        this.cpf = dossieClientePF.getCpfCnpj();
        this.dataNascimento = dossieClientePF.getDataNascimento();
        this.nomeMae = dossieClientePF.getNomeMae();
    }
    
    public DossieClientePFDTO(DossieClientePF dossieClientePF, List<PendenciaCadastroDTO> pendenciasCadastroCaixa) {
        this(dossieClientePF);
        this.pendenciasCadastroDTO = pendenciasCadastroCaixa;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public Calendar getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Calendar dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getSituacaoReceita() {
        return situacaoReceita;
    }

    public void setSituacaoReceita(String situacaoReceita) {
        this.situacaoReceita = situacaoReceita;
    }
}
