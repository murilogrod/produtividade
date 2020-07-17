/**
 * Copyright (c) 2020 Caixa Econômica Federal. Todos os direitos
 * reservados.
 *
 * Caixa Econômica Federal - simtr-api - Sistema de Crédito Rural
 *
 * Este software foi desenvolvido sob demanda da CAIXA e está
 * protegido por leis de direitos autorais e tratados internacionais. As
 * condições de cópia e utilização do total ou partes dependem de autorização da
 * empresa. Cópias não são permitidas sem expressa autorização. Não pode ser
 * comercializado ou utilizado para propósitos particulares.
 *
 * Uso exclusivo da Caixa Econômica Federal. A reprodução ou distribuição não
 * autorizada deste programa ou de parte dele, resultará em punições civis e
 * criminais e os infratores incorrem em sanções previstas na legislação em
 * vigor.
 *
 * Histórico do TFS:
 *
 * LastChangedRevision: $Revision$
 * LastChangedBy: $Author$
 * LastChangedDate: $Date$
 *
 * HeadURL: $HeadURL$
 *
 */
package br.gov.caixa.simtr.controle.vo.outsourcing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.gov.caixa.simtr.modelo.entidade.ControleDocumento;
import br.gov.caixa.simtr.modelo.enumerator.JanelaTemporalExtracaoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.outsourcing.DadoBaseDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.outsourcing.SolicitacaoAvaliacaoExtracaoDTO;

public class SubmissaoDocumentoVO implements Serializable {

    /** Atributo serialVersionUID. */
    private static final long serialVersionUID = 1L;

    private static final String PROCESSO_PADRAO = "PADRAO";

    private String tipoDocumento;
    private String processo;
    private boolean executaExtracao;
    private JanelaTemporalExtracaoEnum janelaTemporalExtracaoEnum;
    private boolean executaAutenticidade;
    private List<String> binarios;
    private List<String> selfies;
    private ControleDocumento controleDocumento;
    private List<DadoBaseDTO> dados;

    public SubmissaoDocumentoVO() {
	super();
	this.processo = PROCESSO_PADRAO;
	this.binarios = new ArrayList<>();
	this.selfies = new ArrayList<>();
	this.dados = new ArrayList<>();
    }

    public SubmissaoDocumentoVO(SolicitacaoAvaliacaoExtracaoDTO solicitacaoAvaliacaoExtracaoDTO) {
        this();
	this.tipoDocumento = solicitacaoAvaliacaoExtracaoDTO.getTipoDocumento();
	this.executaExtracao = solicitacaoAvaliacaoExtracaoDTO.isExecutaExtracao();
	this.janelaTemporalExtracaoEnum = solicitacaoAvaliacaoExtracaoDTO.getJanelaTemporalExtracaoEnum();
	this.executaAutenticidade = solicitacaoAvaliacaoExtracaoDTO.isExecutaAutenticidade();
	if (solicitacaoAvaliacaoExtracaoDTO.getDados() != null) {
	    this.dados = solicitacaoAvaliacaoExtracaoDTO.getDados().stream().map(db -> new DadoBaseDTO(db.getChave(), db.getValor()))
		    .collect(Collectors.toList());
	}
	if (solicitacaoAvaliacaoExtracaoDTO.getBinario() != null) {
	    this.binarios.add(solicitacaoAvaliacaoExtracaoDTO.getBinario());
	}
	if (solicitacaoAvaliacaoExtracaoDTO.getBinariosComplementares() != null) {
	    this.binarios.addAll(solicitacaoAvaliacaoExtracaoDTO.getBinariosComplementares());
	}
	if (solicitacaoAvaliacaoExtracaoDTO.getSelfies() != null) {
            this.selfies.addAll(solicitacaoAvaliacaoExtracaoDTO.getSelfies());
        }
    }

    /**
     * Responsável pela criação de novas instâncias desta classe.
     *
     * @param tipoDocumento
     * @param processo
     * @param enviaExtracaoExterna
     * @param executaExtracao
     * @param janelaTemporalExtracaoEnum
     * @param executaAutenticidade
     * @param controleDocumento
     *
     */
    public SubmissaoDocumentoVO(String tipoDocumento, String processo, boolean executaExtracao, JanelaTemporalExtracaoEnum janelaTemporalExtracaoEnum, boolean executaAutenticidade, String binario, ControleDocumento controleDocumento) {
	this();
	this.tipoDocumento = tipoDocumento;
	this.processo = processo;
	this.executaExtracao = executaExtracao;
	this.janelaTemporalExtracaoEnum = janelaTemporalExtracaoEnum;
	this.executaAutenticidade = executaAutenticidade;
	this.binarios.add(binario);
	this.controleDocumento = controleDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getProcesso() {
        return processo;
    }

    public void setProcesso(String processo) {
        this.processo = processo;
    }

    public boolean isExecutaExtracao() {
        return executaExtracao;
    }

    public void setExecutaExtracao(boolean executaExtracao) {
        this.executaExtracao = executaExtracao;
    }

    public JanelaTemporalExtracaoEnum getJanelaTemporalExtracaoEnum() {
        return janelaTemporalExtracaoEnum;
    }

    public void setJanelaTemporalExtracaoEnum(JanelaTemporalExtracaoEnum janelaTemporalExtracaoEnum) {
        this.janelaTemporalExtracaoEnum = janelaTemporalExtracaoEnum;
    }

    public boolean isExecutaAutenticidade() {
        return executaAutenticidade;
    }

    public void setExecutaAutenticidade(boolean executaAutenticidade) {
        this.executaAutenticidade = executaAutenticidade;
    }

    public List<String> getBinarios() {
        return binarios;
    }

    public void setBinarios(List<String> binarios) {
        this.binarios = binarios;
    }
    
    public List<String> getSelfies() {
        return selfies;
    }

    public void setSelfies(List<String> selfies) {
        this.selfies = selfies;
    }

    public ControleDocumento getControleDocumento() {
        return controleDocumento;
    }

    public void setControleDocumento(ControleDocumento controleDocumento) {
        this.controleDocumento = controleDocumento;
    }

    public List<DadoBaseDTO> getDados() {
        return dados;
    }

    public void setDados(List<DadoBaseDTO> dados) {
        this.dados = dados;
    }

    public static String getProcessoPadrao() {
        return PROCESSO_PADRAO;
    }
}
