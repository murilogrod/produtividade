package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto;

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
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieProduto.XML_ROOT_ELEMENT_DOSSIE_CLIENTE_PF)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieProduto.API_MODEL_V1_DOSSIE_CLIENTE_PF,
        description = "Objeto utilizado para representar o Dossiê Cliente especializado na PF (Pessoa Fisica) no retorno as consultas realizadas sob a ótica Apoio ao Negocio a partir do Dossiê de Produto.",
        parent = DossieClienteDTO.class
)
public class DossieClientePFDTO extends DossieClienteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProduto.CPF)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.CPF, value = "Numero do CPF dossiê de cliente PF.", required = true)
    @XmlJavaTypeAdapter(value = CPFAdapter.class)
    private Long cpf;

    @XmlJavaTypeAdapter(value = CalendarSimpleBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieProduto.DATA_NASCIMENTO, nillable = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.DATA_NASCIMENTO, value = "Data de nascimento do cliente vinculado ao dossiê de cliente.", required = true, example = "dd/MM/yyyy HH:mm:ss")
    private Calendar dataNascimento;

    @XmlElement(name = ConstantesNegocioDossieProduto.NOME_MAE, nillable = true)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.NOME_MAE, value = "Nome da mãe do cliente vinculado ao dossiê de cliente.", required = true)
    private String nomeMae;

    public DossieClientePFDTO() {
        super();
    }

    public DossieClientePFDTO(DossieClientePF dossieClientePF) {
        super(dossieClientePF);
        this.tipoPessoa = TipoPessoaEnum.F;
        if (dossieClientePF != null) {
            this.cpf = dossieClientePF.getCpfCnpj();
            this.dataNascimento = dossieClientePF.getDataNascimento();
            this.nomeMae = dossieClientePF.getNomeMae();
        }
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
