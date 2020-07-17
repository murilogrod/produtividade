package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.manutencao;

import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePF;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CPFAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarSimpleBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieClienteManutencao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieClienteManutencao.XML_ROOT_ELEMENT_DOSSIE_CLIENTE_INCLUSAO_PF)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieClienteManutencao.API_MODEL_V1_DOSSIE_CLIENTE_INCLUSAO_PF,
        description = "Objeto utilizado para representar o Dossiê Cliente a ser cadastrado especializado na PF (Pessoa Fisica).",
        parent = DossieClienteInclusaoDTO.class
)
public class DossieClienteInclusaoPFDTO extends DossieClienteInclusaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlJavaTypeAdapter(value = CPFAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.CPF)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.CPF, required = true, value = "Numero do CPF dossiê de cliente PF. Essa informação não pode ser alterada")
    private Long cpf;

    @XmlJavaTypeAdapter(value = CalendarSimpleBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.DATA_NASCIMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.NOME, required = true, value = "Data de nascimento do cliente vinculado ao dossiê de cliente", example = "dd/MM/yyyy")
    private Calendar dataNascimento;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.NOME_MAE)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.NOME_MAE, required = true, value = "Nome da mãe do cliente vinculado ao dossiê de cliente")
    private String nomeMae;

    // *****************************************
    public DossieClienteInclusaoPFDTO() {
        super();
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

    @Override
    public DossieCliente prototype() {
        DossieClientePF dossieClientePF = new DossieClientePF();
        dossieClientePF.setNome(this.getNome());
        dossieClientePF.setEmail(this.getEmail());
        dossieClientePF.setCpfCnpj(this.getCpf());
        dossieClientePF.setDataNascimento(this.getDataNascimento());
        dossieClientePF.setNomeMae(this.getNomeMae());
        return dossieClientePF;
    }
}
