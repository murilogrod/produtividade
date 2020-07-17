package br.gov.caixa.simtr.controle.excecao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SipesResponseDTO;
import br.gov.caixa.simtr.controle.vo.autorizacao.AutorizacaoDocumentoVO;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.enumerator.SistemaPesquisaEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;

public class DossieAutorizacaoException extends RuntimeException {

    /**
     * Indica que houve um problema na geração da autorização por parte do
     * Dossiê Digital
     */
    private static final long serialVersionUID = 1L;

    private final Long cpfCnpj;
    private final TipoPessoaEnum tipoPessoaEnum;
    private final Integer operacaoSolicitada;
    private final Integer modalidadeSolicitada;
    private final String sistemaSolicitante;
    private final Boolean indicadorProsseguimento;
    private final List<String[]> mensagensOrientacao;
    private final List<String> documentosAusentes;
    private final List<AutorizacaoDocumentoVO> documentosUtilizados;
    private final Produto produto;
    private final SipesResponseDTO resultadoPesquisaSIPES;

    /**
     * Este construtor deve ser utilizado nos casos em que durante a execução da
     * rotina o produto não tiver sido localizado para o fluxo dossiê digital.
     *
     * @param mensagem Mensagem a ser exibida no log de erro
     * @param causa Exceção que deu origem ao impedimento da operação quando for
     * o caso
     * @param sipesResponseDTO Retorno do SIPES a ser incluido no retorno ao
     * solicitante
     * @param cpfCnpj CPF/CNPJ solicitado
     * @param tipoPessoaEnum Tipo de pessoa indicando se Fisica ou Juridica
     * @param operacao Codigo da operação indicada no pedido de autorização
     * @param modalidade Codigo da modalidade indicada no pedido de autorização
     * @param sistemaSolicitante Identificação do canal solicitante da
     * autorização
     * @param indicadorProsseguimento Indicador se a operação deve prosseguir ou
     * não para efetivar captura de documentação
     */
    public DossieAutorizacaoException(String mensagem, Throwable causa, SipesResponseDTO sipesResponseDTO, Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, Integer operacao, Integer modalidade, String sistemaSolicitante, Boolean indicadorProsseguimento) {
        super(mensagem, causa);
        this.cpfCnpj = cpfCnpj;
        this.tipoPessoaEnum = tipoPessoaEnum;
        this.operacaoSolicitada = operacao;
        this.modalidadeSolicitada = modalidade;
        this.indicadorProsseguimento = indicadorProsseguimento;
        this.sistemaSolicitante = sistemaSolicitante;
        this.mensagensOrientacao = new ArrayList<>();
        this.documentosAusentes = new ArrayList<>();
        this.documentosUtilizados = new ArrayList<>();
        this.produto = null;
        this.resultadoPesquisaSIPES = sipesResponseDTO;
    }

    /**
     * Este construtor deve ser utilizado nos casos em que durante a execução da
     * rotina o produto tiver sido localizado para o fluxo dossiê digital.
     *
     * @param mensagem Mensagem a ser exibida no log de erro
     * @param causa Exceção que deu origem ao impedimento da operação quando for
     * o caso
     * @param sipesResponseDTO Retorno do SIPES a ser incluido no retorno ao
     * solicitante
     * @param cpfCnpj CPF/CNPJ solicitado
     * @param tipoPessoaEnum Tipo de pessoa indicando se Fisica ou Juridica
     * @param produto Produto Identificado na execução da rotina
     * @param sistemaSolicitante Identificação do canal solicitante da
     * autorização
     * @param indicadorProsseguimento Indicador se a operação deve prosseguir ou
     * não para efetivar captura de documentação
     */
    public DossieAutorizacaoException(String mensagem, Throwable causa, SipesResponseDTO sipesResponseDTO, Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, Produto produto, String sistemaSolicitante, Boolean indicadorProsseguimento) {
        super(mensagem, causa);
        this.cpfCnpj = cpfCnpj;
        this.tipoPessoaEnum = tipoPessoaEnum;
        this.operacaoSolicitada = produto.getOperacao();
        this.modalidadeSolicitada = produto.getModalidade();
        this.indicadorProsseguimento = indicadorProsseguimento;
        this.sistemaSolicitante = sistemaSolicitante;
        this.mensagensOrientacao = new ArrayList<>();
        this.documentosAusentes = new ArrayList<>();
        this.documentosUtilizados = new ArrayList<>();
        this.produto = produto;
        this.resultadoPesquisaSIPES = sipesResponseDTO;
    }

    public Long getCpfCnpj() {
        return cpfCnpj;
    }

    public TipoPessoaEnum getTipoPessoaEnum() {
        return tipoPessoaEnum;
    }

    public Integer getOperacaoSolicitada() {
        return operacaoSolicitada;
    }

    public Integer getModalidadeSolicitada() {
        return modalidadeSolicitada;
    }

    public String getSistemaSolicitante() {
        return sistemaSolicitante != null ? sistemaSolicitante : "-----";
    }

    public Boolean getIndicadorProsseguimento() {
        return indicadorProsseguimento;
    }

    public List<String[]> getMensagensOrientacao() {
        return mensagensOrientacao;
    }

    public List<String> getDocumentosAusentes() {
        return documentosAusentes;
    }

    public List<AutorizacaoDocumentoVO> getDocumentosUtilizados() {
        return documentosUtilizados;
    }

    public Produto getProduto() {
        return produto;
    }

    public SipesResponseDTO getResultadoPesquisaSIPES() {
        return resultadoPesquisaSIPES;
    }

    //***********************************
    public void addMensagemOrientacao(SistemaPesquisaEnum sistemaPesquisaEnum, String grupoOcorrencia, String mensagem) {
        this.mensagensOrientacao.add(new String[]{sistemaPesquisaEnum.name(), grupoOcorrencia, mensagem});
    }

    public void addDocumentosAusentes(String... documentosAusentes) {
        this.documentosAusentes.addAll(Arrays.asList(documentosAusentes));
    }

    public void addDocumentosUtilizados(AutorizacaoDocumentoVO... documentosUtilizados) {
        this.documentosUtilizados.addAll(Arrays.asList(documentosUtilizados));
    }
}
