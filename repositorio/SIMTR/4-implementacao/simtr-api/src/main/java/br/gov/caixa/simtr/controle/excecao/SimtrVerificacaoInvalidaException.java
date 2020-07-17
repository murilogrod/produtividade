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

import br.gov.caixa.simtr.controle.vo.checklist.ChecklistPendenteVO;
import br.gov.caixa.simtr.modelo.entidade.Checklist;
import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SimtrVerificacaoInvalidaException extends RuntimeException {

    /**
     * Atributo serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private final List<Long> listaIdentificadoresInstanciasDocumentoInvalidos;
    private final List<Checklist> listaChecklistsFasePendentes;
    private final Map<Integer, Long> mapaVerificacoesInesperadasChecklistInstancia;
    private final Map<Checklist, Integer> mapaVerificacoesChecklistsReplicados;
    private final Map<InstanciaDocumento, Checklist> mapaInstanciasChecklistsPendentes;
    private final Map<InstanciaDocumento, Integer> mapaVerificacoesInstanciasReplicados;
    private final List<ChecklistPendenteVO> listaChecklistsApontamentosPendentes;

    public SimtrVerificacaoInvalidaException(String message,
            List<Long> listaIdentificadoresInstanciasDocumentoInvalidos,
            Map<Integer, Long> mapaVerificacoesInesperadasChecklistInstancia,
            List<Checklist> listaChecklistsFasePendentes,
            Map<Checklist, Integer> mapaVerificacoesChecklistsDuplicados,
            Map<InstanciaDocumento, Checklist> mapaInstanciasChecklistsPendentes,
            Map<InstanciaDocumento, Integer> mapaVerificacoesInstanciasDuplicados,
            List<ChecklistPendenteVO> listaChecklistsApontamentosPendentes) {
        super(message);
        this.listaIdentificadoresInstanciasDocumentoInvalidos = Objects.nonNull(listaIdentificadoresInstanciasDocumentoInvalidos) ? listaIdentificadoresInstanciasDocumentoInvalidos : new ArrayList<>();
        this.listaChecklistsFasePendentes = Objects.nonNull(listaChecklistsFasePendentes) ? listaChecklistsFasePendentes : new ArrayList<>();
        this.mapaVerificacoesInesperadasChecklistInstancia = Objects.nonNull(mapaVerificacoesInesperadasChecklistInstancia) ? mapaVerificacoesInesperadasChecklistInstancia : new HashMap<>();
        this.mapaInstanciasChecklistsPendentes = Objects.nonNull(mapaInstanciasChecklistsPendentes) ? mapaInstanciasChecklistsPendentes : new HashMap<>();
        this.mapaVerificacoesChecklistsReplicados = Objects.nonNull(mapaVerificacoesChecklistsDuplicados) ? mapaVerificacoesChecklistsDuplicados : new HashMap<>();
        this.mapaVerificacoesInstanciasReplicados = Objects.nonNull(mapaVerificacoesInstanciasDuplicados) ? mapaVerificacoesInstanciasDuplicados : new HashMap<>();
        this.listaChecklistsApontamentosPendentes = Objects.nonNull(listaChecklistsApontamentosPendentes) ? listaChecklistsApontamentosPendentes : new ArrayList<>();
    }

    public SimtrVerificacaoInvalidaException(String message, Throwable cause,
            List<Long> listaIdentificadoresInstanciasDocumentoInvalidos,
            Map<Integer, Long> mapaVerificacoesInesperadasChecklistInstancia,
            List<Checklist> listaChecklistsFasePendentes,
            Map<Checklist, Integer> mapaVerificacoesChecklistsDuplicados,
            Map<InstanciaDocumento, Checklist> mapaInstanciasChecklistsPendentes,
            Map<InstanciaDocumento, Integer> mapaVerificacoesInstanciasDuplicados,
            List<ChecklistPendenteVO> listaChecklistsApontamentosPendentes) {
        super(message, cause);
        this.listaIdentificadoresInstanciasDocumentoInvalidos = Objects.nonNull(listaIdentificadoresInstanciasDocumentoInvalidos) ? listaIdentificadoresInstanciasDocumentoInvalidos : new ArrayList<>();
        this.listaChecklistsFasePendentes = Objects.nonNull(listaChecklistsFasePendentes) ? listaChecklistsFasePendentes : new ArrayList<>();
        this.mapaVerificacoesInesperadasChecklistInstancia = Objects.nonNull(mapaVerificacoesInesperadasChecklistInstancia) ? mapaVerificacoesInesperadasChecklistInstancia : new HashMap<>();
        this.mapaInstanciasChecklistsPendentes = Objects.nonNull(mapaInstanciasChecklistsPendentes) ? mapaInstanciasChecklistsPendentes : new HashMap<>();
        this.mapaVerificacoesChecklistsReplicados = Objects.nonNull(mapaVerificacoesChecklistsDuplicados) ? mapaVerificacoesChecklistsDuplicados : new HashMap<>();
        this.mapaVerificacoesInstanciasReplicados = Objects.nonNull(mapaVerificacoesInstanciasDuplicados) ? mapaVerificacoesInstanciasDuplicados : new HashMap<>();
        this.listaChecklistsApontamentosPendentes = Objects.nonNull(listaChecklistsApontamentosPendentes) ? listaChecklistsApontamentosPendentes : new ArrayList<>();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<Long> getListaIdentificadoresInstanciasDocumentoInvalidos() {
        return listaIdentificadoresInstanciasDocumentoInvalidos;
    }

    public List<Checklist> getListaChecklistsFasePendentes() {
        return listaChecklistsFasePendentes;
    }

    public Map<Integer, Long> getMapaVerificacoesInesperadasChecklistInstancia() {
        return mapaVerificacoesInesperadasChecklistInstancia;
    }

    public Map<Checklist, Integer> getMapaVerificacoesChecklistsReplicados() {
        return mapaVerificacoesChecklistsReplicados;
    }

    public Map<InstanciaDocumento, Checklist> getMapaInstanciasChecklistsPendentes() {
        return mapaInstanciasChecklistsPendentes;
    }

    public Map<InstanciaDocumento, Integer> getMapaVerificacoesInstanciasReplicados() {
        return mapaVerificacoesInstanciasReplicados;
    }

    public List<ChecklistPendenteVO> getListaChecklistsApontamentosPendentes() {
        return listaChecklistsApontamentosPendentes;
    }

}
