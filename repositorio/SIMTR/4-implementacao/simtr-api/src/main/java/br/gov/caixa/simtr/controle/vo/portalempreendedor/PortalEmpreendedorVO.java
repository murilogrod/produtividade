package br.gov.caixa.simtr.controle.vo.portalempreendedor;

import java.io.Serializable;
import java.util.List;

public class PortalEmpreendedorVO implements Serializable {
    private static final long serialVersionUID = 1L;
 
    private String data;
    private List<NecessidadeVO> necessidades;
    private DadosComplementaresSolicitanteVO dadosComplementaresSolicitante;
    private PessoaJuridicaVO pessoaJuridica;
    private String protocolo;
    private Boolean microEmpresarioIndividual;
    private ContaVO conta;
    private ComprovanteResidenciaVO comprovanteResidencia;
    private DocumentoIdentidadeVO documentoIdentidade;
    private DeclaracaoFaturamentoVO declaracaoFaturamento;
    private DocumentoConstituicaoEmpresaVO documentoConstituicaoEmpresa;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<NecessidadeVO> getNecessidades() {
        return necessidades;
    }

    public void setNecessidades(List<NecessidadeVO> necessidades) {
        this.necessidades = necessidades;
    }

    public DadosComplementaresSolicitanteVO getDadosComplementaresSolicitante() {
        return dadosComplementaresSolicitante;
    }

    public void setDadosComplementaresSolicitante(DadosComplementaresSolicitanteVO dadosComplementaresSolicitante) {
        this.dadosComplementaresSolicitante = dadosComplementaresSolicitante;
    }

    public PessoaJuridicaVO getPessoaJuridica() {
        return pessoaJuridica;
    }

    public void setPessoaJuridica(PessoaJuridicaVO pessoaJuridica) {
        this.pessoaJuridica = pessoaJuridica;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }
    
    public Boolean isMicroEmpresarioIndividual() {
        return microEmpresarioIndividual;
    }

    public void setMicroEmpresarioIndividual(Boolean microEmpresarioIndividual) {
        this.microEmpresarioIndividual = microEmpresarioIndividual;
    }
    
    public ContaVO getConta() {
        return conta;
    }

    public void setConta(ContaVO conta) {
        this.conta = conta;
    }

    public ComprovanteResidenciaVO getComprovanteResidencia() {
        return comprovanteResidencia;
    }

    public void setComprovanteResidencia(ComprovanteResidenciaVO comprovanteResidencia) {
        this.comprovanteResidencia = comprovanteResidencia;
    }

    public DocumentoIdentidadeVO getDocumentoIdentidade() {
        return documentoIdentidade;
    }

    public void setDocumentoIdentidade(DocumentoIdentidadeVO documentoIdentidade) {
        this.documentoIdentidade = documentoIdentidade;
    }
    
    public DeclaracaoFaturamentoVO getDeclaracaoFaturamento() {
        return declaracaoFaturamento;
    }

    public void setDeclaracaoFaturamento(DeclaracaoFaturamentoVO declaracaoFaturamento) {
        this.declaracaoFaturamento = declaracaoFaturamento;
    }

    public DocumentoConstituicaoEmpresaVO getDocumentoConstituicaoEmpresa() {
        return documentoConstituicaoEmpresa;
    }

    public void setDocumentoConstituicaoEmpresa(DocumentoConstituicaoEmpresaVO documentoConstituicaoEmpresa) {
        this.documentoConstituicaoEmpresa = documentoConstituicaoEmpresa;
    }
}
