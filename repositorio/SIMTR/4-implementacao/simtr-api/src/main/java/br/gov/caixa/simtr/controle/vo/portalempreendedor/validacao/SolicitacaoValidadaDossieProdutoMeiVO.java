package br.gov.caixa.simtr.controle.vo.portalempreendedor.validacao;

import java.io.Serializable;
import java.util.List;

import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.Produto;

public class SolicitacaoValidadaDossieProdutoMeiVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private DossiesClienteValidadoVO dossiesClienteValidadoVO;
    private String agenciaContaFormatada;
    private String enderecoPJFormatado;
    private List<Produto> listaProdutosMEI;
    private StringBuilder necessidadesNaoEncontradas;
    private Processo processoGeraDossie;
    private TiposDocumentoValidadoVO tiposDocumentoValidadoVO;
    private TiposRelacionamentoValidadoVO tiposRelacionamentoValidadoVO;
    
    public SolicitacaoValidadaDossieProdutoMeiVO(DossiesClienteValidadoVO dossiesClienteValidadoVO,
                  String agenciaContaFormatada, String enderecoPJFormatado, List<Produto> listaProdutosMEI, 
                  StringBuilder necessidadesNaoEncontradas, Processo processoGeraDossie, 
                  TiposDocumentoValidadoVO tiposDocumentoValidadoVO, TiposRelacionamentoValidadoVO tiposRelacionamentoValidadoVO) {
        super();
        this.dossiesClienteValidadoVO = dossiesClienteValidadoVO;
        this.agenciaContaFormatada = agenciaContaFormatada;
        this.enderecoPJFormatado = enderecoPJFormatado;
        this.listaProdutosMEI = listaProdutosMEI;
        this.necessidadesNaoEncontradas = necessidadesNaoEncontradas;
        this.processoGeraDossie = processoGeraDossie;
        this.tiposDocumentoValidadoVO = tiposDocumentoValidadoVO;
        this.tiposRelacionamentoValidadoVO = tiposRelacionamentoValidadoVO;
    }

    public DossiesClienteValidadoVO getDossiesClienteValidadoVO() {
        return dossiesClienteValidadoVO;
    }



    public void setDossiesClienteValidadoVO(DossiesClienteValidadoVO dossiesClienteValidadoVO) {
        this.dossiesClienteValidadoVO = dossiesClienteValidadoVO;
    }



    public String getAgenciaContaFormatada() {
        return agenciaContaFormatada;
    }

    public void setAgenciaContaFormatada(String agenciaConta) {
        this.agenciaContaFormatada = agenciaConta;
    }

    public String getEnderecoPJFormatado() {
        return enderecoPJFormatado;
    }

    public void setEnderecoPJFormatado(String enderecoPJFormatado) {
        this.enderecoPJFormatado = enderecoPJFormatado;
    }

    public List<Produto> getListaProdutosMEI() {
        return listaProdutosMEI;
    }

    public void setListaProdutosMEI(List<Produto> listaProdutosMEI) {
        this.listaProdutosMEI = listaProdutosMEI;
    }

    public StringBuilder getNecessidadesNaoEncontradas() {
        return necessidadesNaoEncontradas;
    }

    public void setNecessidadesNaoEncontradas(StringBuilder necessidadesNaoEncontradas) {
        this.necessidadesNaoEncontradas = necessidadesNaoEncontradas;
    }

    public Processo getProcessoGeraDossie() {
        return processoGeraDossie;
    }

    public void setProcessoGeraDossie(Processo processoGeraDossie) {
        this.processoGeraDossie = processoGeraDossie;
    }

    public TiposDocumentoValidadoVO getTiposDocumentoValidadoVO() {
        return tiposDocumentoValidadoVO;
    }

    public void setTiposDocumentoValidadoVO(TiposDocumentoValidadoVO tiposDocumentoValidadoVO) {
        this.tiposDocumentoValidadoVO = tiposDocumentoValidadoVO;
    }

    public TiposRelacionamentoValidadoVO getTiposRelacionamentoValidadoVO() {
        return tiposRelacionamentoValidadoVO;
    }

    public void setTiposRelacionamentoValidadoVO(TiposRelacionamentoValidadoVO tiposRelacionamentoValidadoVO) {
        this.tiposRelacionamentoValidadoVO = tiposRelacionamentoValidadoVO;
    }
}
