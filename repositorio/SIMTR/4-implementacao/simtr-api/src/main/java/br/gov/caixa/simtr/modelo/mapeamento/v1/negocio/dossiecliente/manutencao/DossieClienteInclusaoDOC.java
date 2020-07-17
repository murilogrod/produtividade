package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.manutencao;

import java.util.Calendar;

import br.gov.caixa.simtr.modelo.enumerator.PortePessoaJuridicaEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieClienteManutencao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
          value = ConstantesNegocioDossieClienteManutencao.API_MODEL_V1_DOSSIE_CLIENTE_INCLUSAO,
          description = "Objeto utilizado para representar o dossiê cliente para inclusão perante a documentação do Swagger.",
          subTypes =
          {
              DossieClienteInclusaoPFDTO.class,
              DossieClienteInclusaoPJDTO.class
          },
          discriminator = ConstantesNegocioDossieClienteManutencao.TIPO_PESSOA)
public final class DossieClienteInclusaoDOC {

    private DossieClienteInclusaoDOC() {
    }

    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.CPF,
                      value = "Numero do CPF a ser associado ao dossiê cliente PF. (Obrigatório se o tipo de Cliente for PF e não deve ser informado no caso de PJ)",
                      required = false)
    public Long cpf;

    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.CNPJ,
                      value = "Número do CNPJ a ser associado ao dossiê cliente PJ. (Obrigatório se o tipo de Cliente for PJ e não deve ser informado no caso de PF)",
                      required = true)
    public Long cnpj;

    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.NOME, value = "Nome de identificação do cliente para PF ou nome fantasia para clientes PJ.", required = true)
    public String nome;

    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.NOME,
                      value = "Data de nascimento do cliente PF.  (Obrigatório se o tipo de Cliente for PF e não deve ser informado no caso de PJ)", required = false,
                      example = "dd/MM/yyyy")
    public Calendar dataNascimento;

    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.NOME_MAE,
                      value = "Nome da mãe do cliente PF.  (Obrigatório se o tipo de Cliente for PF e não deve ser informado no caso de PJ)", required = false)
    public String nomeMae;

    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.RAZAO_SOCIAL,
                      value = "Nome da razão social do cliente. (Obrigatório se o tipo de Cliente for PJ e não deve ser informado no caso de PF)", required = false)
    public String razaoSocial;

    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.DATA_FUNDACAO,
                      value = "Data de fundação da empresa cliente. (Obrigatório se o tipo de Cliente for PJ e não deve ser informado no caso de PF)", required = false,
                      example = "dd/MM/yyyy HH:mm:ss")
    public Calendar dataFundacao;

    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.SIGLA_PORTE,
                      value = "Sigla do porte da empresa perante a Receita Federal. (Obrigatório se o tipo de Cliente for PJ e não deve ser informado no caso de PF)",
                      required = false)
    public PortePessoaJuridicaEnum porteSiglaEnum;

    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.CONGLOMERADO,
                      value = "Utilizado para indicar se a empresa integra um conglomerado. (Obrigatório se o tipo de Cliente for PJ e não deve ser informado no caso de PF)",
                      required = false)
    public Boolean conglomerado;

    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.EMAIL, value = "Email de contato do cliente utilizado nas rotinas de comunicação com o SSO", required = false)
    public String email;

    // *****************************************
    @ApiModelProperty(name = ConstantesNegocioDossieClienteManutencao.TIPO_PESSOA, value = "Tipo de pessoa encaminhado.", required = true, allowableValues = "F,J")
    public TipoPessoaEnum tipoPessoa;

}
