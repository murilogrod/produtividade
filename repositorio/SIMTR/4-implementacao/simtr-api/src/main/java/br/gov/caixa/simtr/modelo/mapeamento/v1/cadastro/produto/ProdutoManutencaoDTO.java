package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.produto;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroProduto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        value = ConstantesCadastroProduto.API_MODEL_PRODUTO,
        description = "Objeto utilizado para representar um produto nas consultas realizadas sob a ótica dos cadastros."
)
public class ProdutoManutencaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty(value = ConstantesCadastroProduto.NOME_PRODUTO)
    @ApiModelProperty(name = ConstantesCadastroProduto.NOME_PRODUTO, required = true, value = "Atributo utilizado para descrever o produto")
    protected String nomeProduto;

    @JsonProperty(value = ConstantesCadastroProduto.OPERACAO_PRODUTO)
    @ApiModelProperty(name = ConstantesCadastroProduto.OPERACAO_PRODUTO, required = true, value = "Atributo que armazena o número de operação corporativa do produto.")
    protected Integer operacaoProduto;
    
    @JsonProperty(value = ConstantesCadastroProduto.MODALIDADE_PRODUTO)
    @ApiModelProperty(name = ConstantesCadastroProduto.MODALIDADE_PRODUTO, required = true, value = "Atributo que armazena o número da modalidade corporativa do produto.")
    protected Integer modalidadeProduto;
    
    @JsonProperty(value = ConstantesCadastroProduto.INDICADOR_CONTRATACAO_CONJUNTA)
    @ApiModelProperty(name = ConstantesCadastroProduto.INDICADOR_CONTRATACAO_CONJUNTA, required = true, value = "Atributo que armazena o número da modalidade corporativa do produto.")
    protected Boolean contratacaoConjunta;
    
    @JsonProperty(value = ConstantesCadastroProduto.INDICADOR_DOSSIE_DIGITAL)
    @ApiModelProperty(name = ConstantesCadastroProduto.INDICADOR_DOSSIE_DIGITAL, required = true, value = " Atributo utilizado para indicar se o produto já esta mapeado para executar ações vinculadas ao modelo do dossiê digital.")
    protected Boolean dossieDigital;
    
    @JsonProperty(value = ConstantesCadastroProduto.INDICADOR_PESQUISA_CADIN)
    @ApiModelProperty(name = ConstantesCadastroProduto.INDICADOR_PESQUISA_CADIN, required = true, value = "Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao CADIN retorne resultado alguma restrição.")
    protected Boolean pesquisaCadin;
     
    @JsonProperty(value = ConstantesCadastroProduto.INDICADOR_PESQUISA_CCF)
    @ApiModelProperty(name = ConstantesCadastroProduto.INDICADOR_PESQUISA_CCF, required = true, value = "Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao SICCF retorne resultado alguma restrição.")
    protected Boolean pesquisaCCF;
    
    @JsonProperty(value = ConstantesCadastroProduto.INDICADOR_PESQUISA_RECEITA)
    @ApiModelProperty(name = ConstantesCadastroProduto.INDICADOR_PESQUISA_RECEITA, required = true, value = "Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto a Receita retorne resultado alguma restrição.")
    protected Boolean pesquisaReceita;

    @JsonProperty(value = ConstantesCadastroProduto.INDICADOR_PESQUISA_SCPC)
    @ApiModelProperty(name = ConstantesCadastroProduto.INDICADOR_PESQUISA_SCPC, required = true, value = " Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao SCPC retorne resultado alguma restrição.")
    protected Boolean pesquisaSCPC;
    
    @JsonProperty(value = ConstantesCadastroProduto.INDICADOR_PESQUISA_SERASA)
    @ApiModelProperty(name = ConstantesCadastroProduto.INDICADOR_PESQUISA_SERASA, required = true, value = "Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao SPC/SERASA retorne resultado alguma restrição.")
    protected Boolean pesquisaSerasa;

    @JsonProperty(value = ConstantesCadastroProduto.INDICADOR_PESQUISA_SICOW)
    @ApiModelProperty(name = ConstantesCadastroProduto.INDICADOR_PESQUISA_SICOW, required = true, value = "Atributo utilizado para identificar se a autorização deve ser negada caso a pesquisa junto ao SICOW retorne resultado alguma restrição.")
    protected Boolean pesquisaSicow;

    @JsonProperty(value = ConstantesCadastroProduto.INDICADOR_PESQUISA_SINAD)
    @ApiModelProperty(name = ConstantesCadastroProduto.INDICADOR_PESQUISA_SINAD, required = true, value = "Atributo utilizado para identificar se a pesquisa junto a Receita deve ser realizada para o produto especificado.")
    protected Boolean pesquisaSinad;

    @JsonProperty(value = ConstantesCadastroProduto.INDICADOR_TIPO_PESSOA)
    @ApiModelProperty(name = ConstantesCadastroProduto.INDICADOR_TIPO_PESSOA, required = true, value = "Atributo que determina qual tipo de pessoa pode contratar o produto.")
    protected TipoPessoaEnum tipoPessoa;
    
    @JsonProperty(value = ConstantesCadastroProduto.CODIGO_PRODDUTO_PORTAL_EMPREENDEDOR)
    @ApiModelProperty(name = ConstantesCadastroProduto.CODIGO_PRODDUTO_PORTAL_EMPREENDEDOR, required = false, value = "Atributo que representa o código do produto no portal do empreendedor.")
    protected Integer codigoProdutoPortalEmpreendedor;    

    // *********************************************
    public ProdutoManutencaoDTO (){
        super();
    }

    public ProdutoManutencaoDTO(Produto produto) {
        this();
        if (Objects.nonNull(produto)) {
            this.nomeProduto = produto.getNome();
            this.contratacaoConjunta = produto.getContratacaoConjunta();
            this.dossieDigital = produto.getDossieDigital();
            this.modalidadeProduto = produto.getModalidade();
            this.operacaoProduto = produto.getOperacao();
            this.pesquisaCCF = produto.getPesquisaCcf();
            this.pesquisaCadin = produto.getPesquisaCadin();
            this.pesquisaReceita = produto.getPesquisaReceita();
            this.pesquisaSCPC = produto.getPesquisaScpc();
            this.pesquisaSerasa = produto.getPesquisaSerasa();
            this.pesquisaSicow = produto.getPesquisaSicow();
            this.pesquisaSinad = produto.getPesquisaSinad();
            this.tipoPessoa = produto.getTipoPessoa();
            this.codigoProdutoPortalEmpreendedor = produto.getCodigoProdutoPortalEmpreendedor();
        }
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Integer getOperacaoProduto() {
        return operacaoProduto;
    }

    public void setOperacaoProduto(Integer operacaoProduto) {
        this.operacaoProduto = operacaoProduto;
    }

    public Integer getModalidadeProduto() {
        return modalidadeProduto;
    }

    public void setModalidadeProduto(Integer modalidadeProduto) {
        this.modalidadeProduto = modalidadeProduto;
    }

    public Boolean getContratacaoConjunta() {
        return contratacaoConjunta;
    }

    public void setContratacaoConjunta(Boolean contratacaoConjunta) {
        this.contratacaoConjunta = contratacaoConjunta;
    }

    public Boolean getDossieDigital() {
        return dossieDigital;
    }

    public void setDossieDigital(Boolean dossieDigital) {
        this.dossieDigital = dossieDigital;
    }

    public Boolean getPesquisaCadin() {
        return pesquisaCadin;
    }

    public void setPesquisaCadin(Boolean pesquisaCadin) {
        this.pesquisaCadin = pesquisaCadin;
    }

    public Boolean getPesquisaCCF() {
        return pesquisaCCF;
    }

    public void setPesquisaCCF(Boolean pesquisaCCF) {
        this.pesquisaCCF = pesquisaCCF;
    }

    public Boolean getPesquisaReceita() {
        return pesquisaReceita;
    }

    public void setPesquisaReceita(Boolean pesquisaReceita) {
        this.pesquisaReceita = pesquisaReceita;
    }

    public Boolean getPesquisaSCPC() {
        return pesquisaSCPC;
    }

    public void setPesquisaSCPC(Boolean pesquisaSCPC) {
        this.pesquisaSCPC = pesquisaSCPC;
    }

    public Boolean getPesquisaSerasa() {
        return pesquisaSerasa;
    }

    public void setPesquisaSerasa(Boolean pesquisaSerasa) {
        this.pesquisaSerasa = pesquisaSerasa;
    }

    public Boolean getPesquisaSicow() {
        return pesquisaSicow;
    }

    public void setPesquisaSicow(Boolean pesquisaSicow) {
        this.pesquisaSicow = pesquisaSicow;
    }

    public Boolean getPesquisaSinad() {
        return pesquisaSinad;
    }

    public void setPesquisaSinad(Boolean pesquisaSinad) {
        this.pesquisaSinad = pesquisaSinad;
    }

    public TipoPessoaEnum getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoaEnum tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public Integer getCodigoProdutoPortalEmpreendedor() {
        return codigoProdutoPortalEmpreendedor;
    }

    public void setCodigoProdutoPortalEmpreendedor(Integer codigoProdutoPortalEmpreendedor) {
        this.codigoProdutoPortalEmpreendedor = codigoProdutoPortalEmpreendedor;
    }

    public Produto prototype() {
        Produto produto = new Produto();
        produto.setNome(this.nomeProduto);
        produto.setModalidade(this.modalidadeProduto);
        produto.setOperacao(this.operacaoProduto);
        produto.setContratacaoConjunta(this.contratacaoConjunta);
        produto.setDossieDigital(this.dossieDigital);
        produto.setPesquisaCcf(this.pesquisaCCF);
        produto.setPesquisaCadin(this.pesquisaCadin);
        produto.setPesquisaReceita(this.pesquisaReceita);
        produto.setPesquisaScpc(this.pesquisaSCPC);
        produto.setPesquisaSerasa(this.pesquisaSerasa);
        produto.setPesquisaSicow(this.pesquisaSicow);
        produto.setPesquisaSinad(this.pesquisaSinad);
        produto.setTipoPessoa(this.tipoPessoa);
        produto.setCodigoProdutoPortalEmpreendedor(this.codigoProdutoPortalEmpreendedor);
        
        return produto;
    }
}
