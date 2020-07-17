package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente;

import java.util.Calendar;
import java.util.List;

import br.gov.caixa.simtr.modelo.enumerator.PortePessoaJuridicaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieCliente;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.manutencao.DossieClienteInclusaoPFDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.manutencao.DossieClienteInclusaoPJDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = ConstantesNegocioDossieCliente.API_MODEL_V1_DOSSIE_CLIENTE_DOC,
          description = "Objeto utilizado para representar o dossiê cliente consultado perante a documentação do Swagger.", subTypes =
          {
              DossieClienteInclusaoPFDTO.class,
              DossieClienteInclusaoPJDTO.class
          }, discriminator = ConstantesNegocioDossieCliente.TIPO_PESSOA)
public final class DossieClienteDOC {

    private DossieClienteDOC() {
    }

    @ApiModelProperty(name = ConstantesNegocioDossieCliente.ID, value = "Identificador do dossiê do cliente consultado", required = true)
    public Long id;

    @ApiModelProperty(name = ConstantesNegocioDossieCliente.CPF, value = "Numero do CPF a ser associado ao dossiê cliente PF (Presente apenas se o tipo de Cliente for PF)",
                      required = false)
    public Long cpf;

    @ApiModelProperty(name = ConstantesNegocioDossieCliente.CNPJ, value = "Número do CNPJ a ser associado ao dossiê cliente PJ (Presente apenas se o tipo de Cliente for PJ)",
                      required = false)
    public Long cnpj;

    @ApiModelProperty(name = ConstantesNegocioDossieCliente.NOME, value = "Nome de identificação do cliente para PF ou nome fantasia para clientes PJ", required = true)
    public String nome;

    @ApiModelProperty(name = ConstantesNegocioDossieCliente.DATA_NASCIMENTO, value = "Data de nascimento do cliente PF (Presente apenas se o tipo de Cliente for PF)",
                      required = false, example = "dd/MM/yyyy")
    public Calendar dataNascimento;

    @ApiModelProperty(name = ConstantesNegocioDossieCliente.NOME_MAE, value = "Nome da mãe do cliente PF (Presente apenas se o tipo de Cliente for PF)", required = false)
    public String nomeMae;

    @ApiModelProperty(name = ConstantesNegocioDossieCliente.RAZAO_SOCIAL, value = "Nome da razão social do cliente PJ (Presente apenas se o tipo de Cliente for PJ)",
                      required = false)
    public String razaoSocial;

    @ApiModelProperty(name = ConstantesNegocioDossieCliente.DATA_FUNDACAO, value = "Data de fundação da empresa cliente (Presente apenas se o tipo de Cliente for PJ)",
                      required = false, example = "dd/MM/yyyy HH:mm:ss")
    public Calendar dataFundacao;

    @ApiModelProperty(name = ConstantesNegocioDossieCliente.SIGLA_PORTE,
                      value = "Sigla do porte da empresa perante a Receita Federal (Presente apenas se o tipo de Cliente for PJ)", required = false)
    public PortePessoaJuridicaEnum porteSiglaEnum;

    @ApiModelProperty(name = ConstantesNegocioDossieCliente.CONGLOMERADO,
                      value = "Utilizado para indicar se a empresa integra um conglomerado (Presente apenas se o tipo de Cliente for PJ)", required = false)
    public Boolean conglomerado;

    @ApiModelProperty(name = ConstantesNegocioDossieCliente.EMAIL, value = "Email de contato do cliente utilizado nas rotinas de comunicação com o SSO", required = false)
    public String email;

    @ApiModelProperty(name = ConstantesNegocioDossieCliente.DATA_HORA_APURACAO_NIVEL, required = false, value = "Data e hora de apuração do nivel documental",
                      example = "dd/MM/yyyy hh:mm:ss")
    private Calendar dataApuracaoNivel;

    // *****************************************
    @ApiModelProperty(name = ConstantesNegocioDossieCliente.DOCUMENTOS, required = false, value = "Lista de documentos vinculados ao cliente")
    public List<DocumentoDTO> documentosDTO;

    @ApiModelProperty(name = ConstantesNegocioDossieCliente.PRODUTOS_HABILITADOS, required = false,
                      value = "Lista de produtos habilitados para o cliente sob as regras do dossiê digital")
    public List<ProdutoHabilitadoDTO> produtosHabilitadosDTO;

    @ApiModelProperty(name = ConstantesNegocioDossieCliente.DOSSIES_PRODUTO, required = false, value = "Lista de dossiês de produto vinculados ao cliente")
    public List<DossieProdutoDTO> dossiesProdutoDTO;
}
