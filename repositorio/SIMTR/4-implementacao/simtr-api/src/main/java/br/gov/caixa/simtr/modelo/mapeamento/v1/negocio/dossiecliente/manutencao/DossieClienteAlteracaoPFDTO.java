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
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarSimpleBRAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieClienteManutencao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioDossieClienteManutencao.XML_ROOT_ELEMENT_DOSSIE_CLIENTE_ALTERACAO_PF)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioDossieClienteManutencao.API_MODEL_V1_DOSSIE_CLIENTE_ALTERACAO_PF,
        description = "Objeto utilizado para representar uma alteração no Dossiê Cliente especializado na PF (Pessoa Fisica)",
        parent = DossieClienteAlteracaoDTO.class
)
public class DossieClienteAlteracaoPFDTO extends DossieClienteAlteracaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlJavaTypeAdapter(value = CalendarSimpleBRAdapter.class)
    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.DATA_NASCIMENTO)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.DATA_NASCIMENTO, required = false, value = "Data de nascimento do cliente vinculado ao dossiê de cliente.", example = "dd/MM/yyyy")
    private Calendar dataNascimento;

    @XmlElement(name = ConstantesNegocioDossieClienteManutencao.NOME_MAE)
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.NOME_MAE, required = false, value = "Nome da mãe do cliente vinculado ao dossiê de cliente.")
    private String nomeMae;

    // *****************************************
    public DossieClienteAlteracaoPFDTO() {
        super();
    }

    public DossieClienteAlteracaoPFDTO(DossieClientePF dossieClientePF) {
        super(dossieClientePF);
        this.tipoPessoa = TipoPessoaEnum.F;
        this.dataNascimento = dossieClientePF.getDataNascimento();
        this.nomeMae = dossieClientePF.getNomeMae();
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
        dossieClientePF.setEmail(this.email);
        dossieClientePF.setDataNascimento(this.getDataNascimento());
        dossieClientePF.setNomeMae(this.getNomeMae());
        return dossieClientePF;
    }
}
