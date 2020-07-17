/*
 * Copyright (c) 2017 Caixa Econômica Federal. Todos os direitos
 * reservados.
 *
 * Caixa Econômica Federal - simtr-api
 *
 * Este software foi desenvolvido sob demanda da CAIXA e está
 * protegido por leis de direitos autorais e tratados internacionais. As
 * condições de cópia e utilização da totalidade ou partes dependem de autorização da
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
package br.gov.caixa.simtr.controle.excecao;

import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao.PendenciaGarantiaInformadaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao.PendenciaProcessoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao.PendenciaVinculoPessoaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao.PendenciaVinculoProdutoDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimtrDossieProdutoException extends RuntimeException {

    /**
     * Atributo serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private final List<PendenciaVinculoPessoaDTO> pendenciasVinculosPessoasDTO;
    private final List<PendenciaVinculoProdutoDTO> pendenciasVinculosProdutosDTO;
    private final List<PendenciaProcessoDTO> pendenciasProcessoDossieDTO;
    private final List<PendenciaProcessoDTO> pendenciasProcessoFaseDTO;
    List<PendenciaGarantiaInformadaDTO> pendenciasGarantiasDTO;
//    private final List<Checklist> listaChecklistsFasePendentes;
//    private final Map<Integer, Long> mapaVerificacoesInesperadasChecklistInstancia;
//    private final Map<Checklist, Integer> mapaVerificacoesChecklistsReplicados;
//    private final Map<InstanciaDocumento, Checklist> mapaInstanciasChecklistsPendentes;
//    private final Map<InstanciaDocumento, Integer> mapaVerificacoesInstanciasReplicados;
//    private final List<ChecklistPendenteVO> listaChecklistsApontamentosPendentes;

    public SimtrDossieProdutoException(String message,
            List<PendenciaVinculoPessoaDTO> pendenciasVinculosPessoas,
            List<PendenciaVinculoProdutoDTO> pendenciasVinculosProdutos,
            List<PendenciaProcessoDTO> pendenciasProcessoDossie,
            List<PendenciaProcessoDTO> pendenciasProcessoFase,
            List<PendenciaGarantiaInformadaDTO> pendenciasGarantias) {
        super(message);
        this.pendenciasVinculosPessoasDTO = Objects.nonNull(pendenciasVinculosPessoas) ? pendenciasVinculosPessoas : new ArrayList<>();
        this.pendenciasVinculosProdutosDTO = Objects.nonNull(pendenciasVinculosProdutos) ? pendenciasVinculosProdutos : new ArrayList<>();
        this.pendenciasProcessoDossieDTO = Objects.nonNull(pendenciasProcessoDossie) ? pendenciasProcessoDossie : new ArrayList<>();
        this.pendenciasProcessoFaseDTO = Objects.nonNull(pendenciasProcessoFase) ? pendenciasProcessoFase : new ArrayList<>();
        this.pendenciasGarantiasDTO = Objects.nonNull(pendenciasGarantias) ? pendenciasGarantias : new ArrayList<>();
    }

    public SimtrDossieProdutoException(String message, Throwable cause,
            List<PendenciaVinculoPessoaDTO> pendenciasVinculosPessoas,
            List<PendenciaVinculoProdutoDTO> pendenciasVinculosProdutos,
            List<PendenciaProcessoDTO> pendenciasProcessoDossie,
            List<PendenciaProcessoDTO> pendenciasProcessoFase,
            List<PendenciaGarantiaInformadaDTO> pendenciasGarantias) {
        super(message, cause);
        this.pendenciasVinculosPessoasDTO = Objects.nonNull(pendenciasVinculosPessoas) ? pendenciasVinculosPessoas : new ArrayList<>();
        this.pendenciasVinculosProdutosDTO = Objects.nonNull(pendenciasVinculosProdutos) ? pendenciasVinculosProdutos : new ArrayList<>();
        this.pendenciasProcessoDossieDTO = Objects.nonNull(pendenciasProcessoDossie) ? pendenciasProcessoDossie : new ArrayList<>();
        this.pendenciasProcessoFaseDTO = Objects.nonNull(pendenciasProcessoFase) ? pendenciasProcessoFase : new ArrayList<>();
        this.pendenciasGarantiasDTO = Objects.nonNull(pendenciasGarantias) ? pendenciasGarantias : new ArrayList<>();
    }

    public List<PendenciaVinculoPessoaDTO> getPendenciasVinculosPessoasDTO() {
        return pendenciasVinculosPessoasDTO;
    }

    public List<PendenciaVinculoProdutoDTO> getPendenciasVinculosProdutosDTO() {
        return pendenciasVinculosProdutosDTO;
    }

    public List<PendenciaProcessoDTO> getPendenciasProcessoDossieDTO() {
        return pendenciasProcessoDossieDTO;
    }

    public List<PendenciaProcessoDTO> getPendenciasProcessoFaseDTO() {
        return pendenciasProcessoFaseDTO;
    }

    public List<PendenciaGarantiaInformadaDTO> getPendenciasGarantiasDTO() {
        return pendenciasGarantiasDTO;
    }
}
