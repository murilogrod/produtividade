package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.gov.caixa.simtr.modelo.entidade.DossieClientePF;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CPFAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarSimpleBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieCliente;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieCliente.XML_ROOT_ELEMENT_DOSSIE_CLIENTE_PF)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieCliente.API_MODEL_V1_DOSSIE_CLIENTE_PF,
        description = "Objeto utilizado para representar o dossiê cliente especializado na PF (Pessoa Fisica)no retorno as consultas realizadas a partir do Dossiê do Cliente",
        parent = DossieClienteDTO.class
)
public class DossieClientePFDTO extends DossieClienteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlJavaTypeAdapter(value = CPFAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieCliente.CPF)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.CPF, required = true, value = "Numero do CPF dossiê de cliente PF. Essa informação não pode ser alterada")
    private Long cpf;

    @XmlJavaTypeAdapter(value = CalendarSimpleBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieCliente.DATA_NASCIMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.DATA_NASCIMENTO, required = false, value = "Data de nascimento do cliente vinculado ao dossiê de cliente", example = "dd/MM/yyyy")
    private Calendar dataNascimento;

    @XmlElement(name = ConstantesNegocioDossieCliente.NOME_MAE)
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.NOME_MAE, required = false, value = "Nome da mãe do cliente vinculado ao dossiê de cliente")
    private String nomeMae;

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
}
