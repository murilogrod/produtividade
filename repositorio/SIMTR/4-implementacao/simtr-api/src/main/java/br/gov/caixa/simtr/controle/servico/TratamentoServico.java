package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoBloqueadoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrVerificacaoInvalidaException;
import br.gov.caixa.simtr.controle.vo.checklist.ChecklistPendenteVO;
import br.gov.caixa.simtr.controle.vo.checklist.ParecerApontamentoVO;
import br.gov.caixa.simtr.controle.vo.checklist.VerificacaoVO;
import br.gov.caixa.simtr.modelo.entidade.Apontamento;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.Checklist;
import br.gov.caixa.simtr.modelo.entidade.ChecklistAssociado;
import br.gov.caixa.simtr.modelo.entidade.DossieClienteProduto;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.Parecer;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.ProcessoDocumento;
import br.gov.caixa.simtr.modelo.entidade.ProcessoFaseDossie;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDocumento;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDossie;
import br.gov.caixa.simtr.modelo.entidade.SituacaoInstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.entidade.TipoSituacaoDossie;
import br.gov.caixa.simtr.modelo.entidade.Verificacao;
import br.gov.caixa.simtr.modelo.entidade.VinculacaoChecklist;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.visao.SimtrExceptionDTO;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNTTG,
    ConstantesUtil.PERFIL_MTRSDNTTO
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class TratamentoServico {

    @EJB
    private CanalServico canalServico;

    @EJB
    private ChecklistServico checklistServico;

    @EJB
    private DossieProdutoServico dossieProdutoServico;

    @EJB
    private KeycloakService keycloakService;

    @EJB
    private ProcessoServico processoServico;

    @EJB
    private SituacaoDossieServico situacaoDossieServico;

    @EJB
    private SituacaoDocumentoServico situacaoDocumentoServico;

    @Inject
    private EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(TratamentoServico.class.getName());

    /**
     * Captura o proximo dossiê disponivel para tratamento vinculado ao processo indicado. Localizado dentre os processos vinculado em toda a hierarquia associada ao processo solicitado.
     *
     * @param idProcesso Identificador do processo desejado
     * @return Dossie de Produto localizado para tratamento ou null caso não tenha nenhum dossi~e nesta sityuação
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    public DossieProduto capturaDossieTratamento(final Integer idProcesso) {
        Processo processo = this.processoServico.getById(idProcesso);
        if (processo == null) {
            SimtrExceptionDTO simtrExceptionDTO = new SimtrExceptionDTO();
            simtrExceptionDTO.setFalhaRequisicao(Boolean.TRUE);
            String mensagem = "Identificador do processo informado invalido.";
            LOGGER.log(Level.WARNING, "TS.cDT.001 - {0}", new Object[] {
                mensagem
            });
            throw new SimtrRequisicaoException(mensagem);
        }

        TipoSituacaoDossie tipoSituacaoDosieAguardandoTratamento = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.AGUARDANDO_TRATAMENTO);

        DossieProduto dossieProduto = this.dossieProdutoServico.localizaMaisAntigoByProcessoAndTipoSituacao(processo, tipoSituacaoDosieAguardandoTratamento);

        if (dossieProduto != null) {
            TipoSituacaoDossie tipoSituacaoDossieEmTratamento = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.EM_TRATAMENTO);
            this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoDossieEmTratamento, null, null);

            dossieProduto = this.dossieProdutoServico.getById(dossieProduto.getId(), Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);

            dossieProduto.getInstanciasDocumento().removeIf(id -> {
                return id.getSituacoesInstanciaDocumento().stream()
                         .max(Comparator.comparing(SituacaoInstanciaDocumento::getId)).get().getSituacaoDocumento()
                         .getSituacaoFinal();
            });
        }

        return dossieProduto;
    }

    /**
     * Devolve o dossiê indicado pelo identificador a fila de tratamento inserindo uma nova situação \"Aguardando Tratamento\" vinculada ao mesmo.
     *
     * @param id Identificador do dossi~e que deseja retornar a fila de tratamento
     * @throws SimtrRequisicaoException Lançada caso o Dossiê não seja localizado pelo identificador informado, ou se o mesmo não esteja em situação passivel de retorno a fila de tratamento
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    public void retornaFilaTratamento(final Long id) {
        DossieProduto dossieProduto = this.dossieProdutoServico.getById(id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (dossieProduto == null) {
            SimtrExceptionDTO simtrExceptionDTO = new SimtrExceptionDTO();
            simtrExceptionDTO.setFalhaRequisicao(Boolean.TRUE);
            String mensagem = MessageFormat.format("Dossiê de Produto não localizado sob identificador informado. ID = {0}", id);
            LOGGER.log(Level.WARNING, "DPS.rFT.001 - {0}", new Object[] {
                mensagem
            });
            throw new SimtrRequisicaoException(mensagem);
        }

        TipoSituacaoDossie tipoSituacaoDossieEmTratamento = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.EM_TRATAMENTO);

        SituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream()
                                                          .max(Comparator.comparing(SituacaoDossie::getId))
                                                          .get();

        if (!tipoSituacaoDossieEmTratamento.equals(situacaoAtualDossie.getTipoSituacaoDossie())) {
            SimtrExceptionDTO simtrExceptionDTO = new SimtrExceptionDTO();
            simtrExceptionDTO.setFalhaRequisicao(Boolean.TRUE);
            String nomeSituacaoAtual = situacaoAtualDossie.getTipoSituacaoDossie().getNome();
            String mensagem = MessageFormat.format("DPS.rFT.001 - Dossiê de Produto informado não se encontra em situação de tratamento. ID = {0} | Situação Atual = {1}", id, nomeSituacaoAtual);
            LOGGER.log(Level.WARNING, mensagem);
            throw new SimtrRequisicaoException(mensagem);
        }

        TipoSituacaoDossie tipoSituacaoDossieAguardandoTratamento = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.AGUARDANDO_TRATAMENTO);

        this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoDossieAguardandoTratamento, "DESISTÊNCIA DA OPERAÇÃO DE TRATAMENTO", null);

        // Força captura do dossiê em contexto transacional para permitir a definição da prioriação do dossiê
        dossieProduto = this.dossieProdutoServico.getById(id);

        // Caso o dossiê não esteja priorizado, definine o valor de priorização para unidade padrão de forma que o dossiê retorne para a fila.
        // Este valor faz com que o dossiê esteja em posição privilegiada aos demais considerando já ter chegado em seu momento de tratamento
        if ((dossieProduto.getUnidadePriorizado() == null) && (dossieProduto.getMatriculaPriorizado() == null)) {
            dossieProduto.setUnidadePriorizado(ConstantesUtil.TRATAMENTO_UNIDADE_PRIORIZACAO_RETORNO_FILA);
        }

        this.dossieProdutoServico.update(dossieProduto);
    }

    /**
     * <p>
     * Inclui o registro de verificações realizada para cada checklist analisado junto ao dossiê de produto identificado.
     * </p>
     *
     * <p>
     * Caso a verificação aponte para um elemento (instancia de documento ou para a fase do dossiê) e este elemento ainda não tenha sido verificado, será realizada uma associação do checklist indicado
     * ao elemento definido de forma a manter o mesmo checklist aplicado até o final do ciclo de vida do elemento analisada mesmo que o checklist vinculado já esteja revogado.
     * </p>
     *
     * @param id Identificador do dossiê de produto a ser vinculado os registros de verificações realizadas aos checklists
     * @param verificacoesRealizadas Lista de objetos de valor que representam a verificação a um determinado checklist
     * @throws SimtrRecursoDesconhecidoException Lançada caso algum recurso indicado (Canal, Dossiê, Checklist, etc) não seja localizado pelo identificador informado.
     * @return Lista de verificações desconsideradas a serem informadas no retorno do serviço
     * @throws SimtrRecursoDesconhecidoException Lançada caso algum recurso indicado na solicitação não seja localizado. Ex: Canal, Dossiê de Produto
     * @throws SimtrEstadoImpeditivoException Lançada caso o dossiê de produto indicado esteja em uma situação diferente de "Em Tratamento" o que impede a execução do tratamento pela definição do seu
     *         ciclo de vida
     * @throws SimtrRecursoBloqueadoException Lançada caso o usuário responsável pelo envio do retorno de tratamento seja diferente do usuário que realizou a captura do dossiê que resultou na inclusão
     *         da situação definida no seu cilco de vida como "Em Tratamento" * @throws SimtrVerificacaoInvalidaException Lançada caso seja identificada alguma verificação invalida que impede a
     *         aplicação da execução do tratamento. Analisado:
     *         <ul>
     *         <li>Ausencia do envio de um checklist documental esperado na fase atual do processo.</li>
     *         <li>Ausencia do envio de um checklist não documental esperado na fase atual do processo.</li>
     *         <li>Envio de checklist com ausencia de parecer para um ou mais apontamentos definidos no mesmo.</li>
     *         <li>Envio de mais de uma verificação para o mesmo checklist não documental</li>
     *         <li>Envio de mais de uma verificação para a mesma instancia de documento</li>
     *         <li>Indicação de instancia de documento não asociada ao dossiê.</li>
     *         </ul>
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    public List<VerificacaoVO> executaTratamento(final Long id, List<VerificacaoVO> verificacoesRealizadas) {

        // Captura data e hora a ser utilizada no registro das verificações
        final Calendar dataHoraRegistro = Calendar.getInstance();

        // Captura matricula do operador responsavel pelo registro da operacao
        final String matriculaOperador = this.keycloakService.getMatricula();

        // Valida o canal de comunicação informado pelo codigo de integração
        Canal canal = this.canalServico.getByClienteSSO();
        if (Objects.isNull(canal)) {
            String mensagem = "DPS.eT.001 - Canal de comunicação não localizado";
            throw new SimtrRecursoDesconhecidoException(mensagem);
        }

        // Localiza o dossiê de produto informado pelo identificador
        DossieProduto dossieProduto = this.dossieProdutoServico.getById(id, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
        if (Objects.isNull(dossieProduto)) {
            String mensagem = MessageFormat
                                           .format("DPS.eT.002 - Dossiê de Produto não localizado sob identificador informado. ID = {0}", id);
            throw new SimtrRecursoDesconhecidoException(mensagem);
        }

        // Valida a situação atual do dossiê de produto para verificar se o mesmo encontra-se em estado que permite executar o tratamento
        TipoSituacaoDossie tipoSituacaoDossieEmTratamento = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.EM_TRATAMENTO);

        SituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream()
                                                          .max(Comparator.comparing(SituacaoDossie::getId))
                                                          .orElse(new SituacaoDossie());

        if (!tipoSituacaoDossieEmTratamento.equals(situacaoAtualDossie.getTipoSituacaoDossie())) {
            String nomeSituacaoAtual = situacaoAtualDossie.getTipoSituacaoDossie().getNome();
            String mensagem = MessageFormat.format("DPS.eT.003 - Dossiê de Produto informado não se encontra em situação de tratamento. ID = {0} | Situação Atual = {1}", id, nomeSituacaoAtual);
            throw new SimtrEstadoImpeditivoException(mensagem);
        }

        // Valida se o usuário responsavel por encaminhar o tratamento é o mesmo que
        // capturou o dossiê para tratamento
        if (!situacaoAtualDossie.getResponsavel().equalsIgnoreCase(matriculaOperador)) {
            String mensagem = MessageFormat.format("DPS.eT.004 - Usuário não autorizado a executar o tratamento no momento. Dossiê = {0} | Solicitante = {1} | Utilizador = {2}", id, matriculaOperador, situacaoAtualDossie.getResponsavel());
            throw new SimtrRecursoBloqueadoException(mensagem, situacaoAtualDossie.getResponsavel());
        }

        // Realiza a validação das verificações encaminhadas e captura as verificações a serem desconsideradas por já possuir registro anterior aprovado.
        // Caso o registro possua uma caracteristica de reprovação, será adicionado um novo registro de verificação.
        List<VerificacaoVO> verificacoesDesconsideradas = this.validaVerificacoesEsperadasEncaminhadas(dossieProduto, verificacoesRealizadas);

        // Filtra as verificacoes que devem ser consideradas para inclusão de novos registros
        List<VerificacaoVO> verificacoesConsideradas = verificacoesRealizadas.stream()
                                                                             .filter(v -> !verificacoesDesconsideradas.contains(v))
                                                                             .collect(Collectors.toList());

        // Variavel utilizada para armazenar a identificação do tipo de situação que deverá ser encaminhado o dossiê após os registros das verificações realizadas
        // Pressupõe-se que o processo estará CONFORME e caso algum apontamento esteja negativado o retorno indicará modificação neste objeto.
        SituacaoDossieEnum situacaoFinalDossieEnum = SituacaoDossieEnum.CONFORME;

        // Mapa utilizado para armazenar a situação que deverá ser atribuida a cada instancia de documento analisada ao final do ciclo de criação dos registros de
        // verificação
        Map<InstanciaDocumento, SituacaoDocumentoEnum> mapaSituacoesDefinidas = new HashMap<>();

        // Percorre todos as verificações consideradas inserindo as informações necessarias ao registro da verificação
        Map<Integer, Checklist> mapaChecklistsTransacionais = new HashMap<>();
        for (VerificacaoVO verificacaoVO : verificacoesConsideradas) {
            // Verifica se o checklist informado já foi carregado da base de dados no mapaSituacoesDefinidas
            Checklist checklist = mapaChecklistsTransacionais.get(verificacaoVO.getIdentificadorChecklist());

            // Caso ainda não tenha sido carregado, realiza a carga e armazena o mesmo no mapa contendo os objetos com contexto transacional
            if (Objects.isNull(checklist)) {
                checklist = this.checklistServico.getById(verificacaoVO.getIdentificadorChecklist(), Boolean.FALSE, Boolean.FALSE);
                mapaChecklistsTransacionais.put(checklist.getId(), checklist);
            }

            // Registra a verificação e alimenta o mapa de situação definida para as instancias de documento conforme definição dos apontamentos.
            SituacaoDossieEnum situacaoDossieEnum = this.registraVerificacaoRealizada(dossieProduto, checklist, verificacaoVO, dataHoraRegistro, mapaSituacoesDefinidas);

            // Substitui a situação final do dossiê caso ainda não esteja definido como PENDENTE_SEGURANCA (prioridade) e a situação da verificação atual seja diferente de
            // CONFORME
            if ((!SituacaoDossieEnum.PENDENTE_SEGURANCA.equals(situacaoFinalDossieEnum)) && (!SituacaoDossieEnum.CONFORME.equals(situacaoDossieEnum))) {
                situacaoFinalDossieEnum = situacaoDossieEnum;
            }
        }

        // Atribui as situações definidas a cada instancia de documento após a inclusão do registro de verificação
        mapaSituacoesDefinidas.entrySet().forEach(registro -> {
            InstanciaDocumento instanciaDocumento = registro.getKey();
            SituacaoDocumentoEnum situacaoDocumentoEnum = registro.getValue();

            this.situacaoDocumentoServico.registraNovaSituacaoInstanciaDocumento(instanciaDocumento, situacaoDocumentoEnum);
        });

        TipoSituacaoDossie tipoSituacaoFinalDossie = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(situacaoFinalDossieEnum);
        this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoFinalDossie, null, null);

        return verificacoesDesconsideradas;
    }

    /**
     * Realiza a validação das verificações encaminhadas quanto a sua pertinencia ao momento do ciclo de vida do dossiê de produto. Ao final retorna as verificações que devem ser desconsideradas pois
     * já existem verificações anteriores aprovadas para o mesmo elemento/checklist ou foi encaminhado verificações para checklists sem característica de verificação prévia juntamente com verificações
     * realizadas em checklists desta característica possuindo apontamento não acatado.
     *
     * @param dossieProduto Referência do dossiê de produto localizado ao qual foi solicitado a execução do tratamento.
     * @param verificacoesVO Lista de verificações encaminhadas para o serviço de execução do tratamento a ser validado quanto a pertinencia ao momento do ciclo de vida do dossiê.
     * @return Lista de objetos que representam as verificações encaminhadas e que devem ser desconsiderados por já existir verificação aprovada para o item analisado.
     * @throws SimtrVerificacaoInvalidaException Lançada caso seja identificada alguma verificação invalida que impede a aplicação da execução do tratamento. Analisado:
     *         <ul>
     *         <li>Ausencia do envio de um checklist documental esperado na fase atual do processo.</li>
     *         <li>Ausencia do envio de um checklist não documental esperado na fase atual do processo.</li>
     *         <li>Envio de checklist com ausencia de parecer para um ou mais apontamentos definidos no mesmo.</li>
     *         <li>Envio de mais de uma verificação para o mesmo checklist não documental</li>
     *         <li>Envio de mais de uma verificação para a mesma instancia de documento</li>
     *         <li>Indicação de instancia de documento não asociada ao dossiê.</li>
     *         </ul>
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    private List<VerificacaoVO> validaVerificacoesEsperadasEncaminhadas(DossieProduto dossieProduto, List<VerificacaoVO> verificacoesVO) {

        // Adiciona os objetos analisados durante a validação os elementos.
        // Ao final verifica se a lista de verificações encaminhadas possui mais elementos do que essa lista o que significa envio de elementos não esperados.
        List<VerificacaoVO> listaVerificacoesAnalisadas = new ArrayList<>();

        // Armazena a lista de verificações encaminhadas que serão desconsiderados por já existirem verificações aprovadas ocorrência anterior.
        List<VerificacaoVO> listaVerificacoesDesconsideradas = new ArrayList<>();

        // Utilizado para armazenar as instâncias de documentos que tiveram os checklists documentais enviados e identificados
        Map<InstanciaDocumento, Checklist> mapaInstanciasChecklistIdentificado = new HashMap<>();

        // ************* CONTROLE DE PENDENCIAS *********************//
        // Utilizado para armazenar as verificações identificadas com replicação no envio para a mesma instância de documento
        Map<InstanciaDocumento, List<VerificacaoVO>> mapaPendenciaInstanciasVerificacaoReplicada = new HashMap<>();

        // Utilizado para armazenar as verificações identificadas com replicação no envio para o mesmo checklist não documental
        Map<Checklist, List<VerificacaoVO>> mapaPendenciaChecklistFaseVerificacaoReplicada = new HashMap<>();

        // Utilizado para armazenar as instâncias de documento que deveriam ter checklists documentais enviados mas não foram identificados
        Map<InstanciaDocumento, Checklist> mapaPendenciaInstanciaChecklistAusente = new HashMap<>();

        // Utilizado para armazenar as verificações encamihadas que tiveram os checklists com ausencia de envio de resultado para algum apontamento
        List<ChecklistPendenteVO> listaPendenciaChecklistApontamentoAusente = new ArrayList<>();

        // Utilizado para armazenar os identificadores de instancias de documentos encaminhados, mas não associados ao dossiê de produto indicado.
        List<Long> listaPendenciaInstanciaAusenteDossie = new ArrayList<>();

        // Utilizado para armazenar os checklists não documentais que deveriam ter sido enviados nesta fase mas não foram identificados
        List<Checklist> listaPendenciaChecklistProcessoFaseAusente = new ArrayList<>();

        // ************* CONTROLE DE PENDENCIAS *********************//

        // Percorre a lista de verificações enviadas e checa se existe alguma registro que indique instancia de documento não associado ao dossiê de produto.
        verificacoesVO.forEach(vr -> {
            boolean instanciaLocalizadaDossie = dossieProduto.getInstanciasDocumento()
                                                             .stream()
                                                             .anyMatch(id -> id.getId().equals(vr.getIdentificadorInstanciaDocumento()));

            if ((Objects.nonNull(vr.getIdentificadorInstanciaDocumento())) && (!instanciaLocalizadaDossie)) {
                // Caso seja localizado adiciona o identificador da instancia na lista de pendencias e remove a verificação da lista de registros a serem verificados.
                listaPendenciaInstanciaAusenteDossie.add(vr.getIdentificadorInstanciaDocumento());
                listaVerificacoesAnalisadas.add(vr);
            }
        });

        // Realiza a validação dos checklists prévios pelo processo fase associados ao dossiê no momento da verificação
        boolean aprovadoChecklistPrevio = this.avaliaVerificacoesChecklistPrevio(dossieProduto, verificacoesVO, listaVerificacoesAnalisadas, listaVerificacoesDesconsideradas, listaPendenciaChecklistApontamentoAusente, listaPendenciaChecklistProcessoFaseAusente, mapaPendenciaChecklistFaseVerificacaoReplicada);

        // Caso os checklists prévios sejam aprovados, todas as demais verificações devem ser realizadas
        if (aprovadoChecklistPrevio) {

            // Realiza a validação das instancias de documento definidas pelo vinculo de pessoas associados ao dossiê
            this.avaliaVerificacoesVinculoPessoas(dossieProduto, verificacoesVO, listaVerificacoesAnalisadas, listaVerificacoesDesconsideradas, listaPendenciaChecklistApontamentoAusente, mapaPendenciaInstanciaChecklistAusente, mapaInstanciasChecklistIdentificado, mapaPendenciaInstanciasVerificacaoReplicada);

            // Realiza a validação das instancias de documento definidas pelo vinculo de produtos associados ao dossiê
            this.avaliaVerificacoesVinculoProduto(dossieProduto, verificacoesVO, listaVerificacoesAnalisadas, listaVerificacoesDesconsideradas, listaPendenciaChecklistApontamentoAusente, mapaPendenciaInstanciaChecklistAusente, mapaInstanciasChecklistIdentificado, mapaPendenciaInstanciasVerificacaoReplicada);

            // Realiza a validação das instancias de documento definidas pelo vinculo de garantias informadas associadas ao dossiê
            this.avaliaVerificacoesVinculoGarantias(dossieProduto, verificacoesVO, listaVerificacoesAnalisadas, listaVerificacoesDesconsideradas, listaPendenciaChecklistApontamentoAusente, mapaPendenciaInstanciaChecklistAusente, mapaInstanciasChecklistIdentificado, mapaPendenciaInstanciasVerificacaoReplicada);

            // Realiza a validação das instancias de documento definidas pela estrutura de pessoas associados ao dossiê
            this.avaliaVerificacoesProcessosDocumentais(dossieProduto, verificacoesVO, listaVerificacoesAnalisadas, listaVerificacoesDesconsideradas, listaPendenciaChecklistApontamentoAusente, mapaPendenciaInstanciaChecklistAusente, mapaInstanciasChecklistIdentificado, mapaPendenciaInstanciasVerificacaoReplicada);

            // Realiza a validação dos checklists não documentais pelo processo fase associados ao dossiê no momento da verificação
            this.avaliaVerificacoesProcessosNaoDocumentais(dossieProduto, verificacoesVO, listaVerificacoesAnalisadas, listaVerificacoesDesconsideradas, listaPendenciaChecklistApontamentoAusente, listaPendenciaChecklistProcessoFaseAusente, mapaPendenciaChecklistFaseVerificacaoReplicada);
        }

        // Caso a verificação prévia não seja aprovada, considera como inesperados apenas as verificações por ventura não analisadas e não desconsideradas,
        // Caso contrário apenas as não analisadas
        List<VerificacaoVO> verificacoesInesperadas = verificacoesVO.stream()
                                                                    .filter(v -> !listaVerificacoesAnalisadas.contains(v))
                                                                    .collect(Collectors.toList());

        if (!aprovadoChecklistPrevio) {
            verificacoesInesperadas = verificacoesVO.stream()
                                                    .filter(v -> !listaVerificacoesAnalisadas.contains(v) && !listaVerificacoesDesconsideradas.contains(v))
                                                    .collect(Collectors.toList());
        }

        //@formatter:off
        // Realiza checagem sobre as listas de pendencias e lança uma exceção se:
        // - Checa se a lista de verificações realizadas ainda possui elementos, significando que foram encaminhados elementos não esperados
        // - Checa se a lista de instâncias de documentos inválida foi alimentada, significando que foi encaminhada verificações para instancias não associadas ao dossiê
        // - Checa se a lista de checklists pendentes para o processo fase foi alimentada, significando que checklists de fase não foram encaminhados e estava pendente
        // - Checa se a lista de verificações com apontamentos ausentes foi alimentada, significando que foram encaminhada verificações com a quantiade de apontamentos incompleta
        // - Checa se o mapa de checklists fase com verificação replicada foi alimentada, significando que foi encaminhada mais de uma verificação para o mesmo elemento
        // - Checa se o mapa de instâncias de documentos com checklists ausentes foi alimetando, significando que não foram verificações esperadas para alguma instância de documento
        // - Checa se o mapa de instâncias de documentos com verificação replicada foi alimentada, significando que foi encaminhada mais de uma verificação para o mesmo elemento
        //@formatter:on

        if ((!verificacoesInesperadas.isEmpty()) || (!listaPendenciaInstanciaAusenteDossie.isEmpty())
                || (!listaPendenciaChecklistProcessoFaseAusente.isEmpty())
                || (!listaPendenciaChecklistApontamentoAusente.isEmpty())
                || (!mapaPendenciaChecklistFaseVerificacaoReplicada.isEmpty())
                || (!mapaPendenciaInstanciaChecklistAusente.isEmpty())
                || (!mapaPendenciaInstanciasVerificacaoReplicada.isEmpty())) {

            // Lançar exceção devido a identificação de checklists não esperados, duplicados ou pendentes
            String mensagem = "Existem pendencias que impedem a aplicação das verificações encaminhadas.";

            // Alimenta os mapas com os quantitativos que serão encaminhados no construtor da exceção
            Map<Checklist, Integer> mapaChecklistsFaseDuplicados = mapaPendenciaChecklistFaseVerificacaoReplicada.entrySet().stream()
                                                                                                                 .collect(Collectors.toMap(Map.Entry::getKey, registro -> registro.getValue()
                                                                                                                                                                                  .size()));

            Map<InstanciaDocumento, Integer> mapaInstanciasDuplicadas = mapaPendenciaInstanciasVerificacaoReplicada.entrySet().stream()
                                                                                                                   .collect(Collectors.toMap(Map.Entry::getKey, registro -> registro.getValue()
                                                                                                                                                                                    .size()));

            Map<Integer, Long> mapaVerificacoesInesperadas = new HashMap<>();
            verificacoesInesperadas.forEach(registro -> mapaVerificacoesInesperadas.put(registro.getIdentificadorChecklist(), registro.getIdentificadorInstanciaDocumento()));

            throw new SimtrVerificacaoInvalidaException(mensagem, listaPendenciaInstanciaAusenteDossie, mapaVerificacoesInesperadas, listaPendenciaChecklistProcessoFaseAusente, mapaChecklistsFaseDuplicados, mapaPendenciaInstanciaChecklistAusente, mapaInstanciasDuplicadas, listaPendenciaChecklistApontamentoAusente);
        }

        return listaVerificacoesDesconsideradas;
    }

    /**
     * Realiza a análise das verificações encaminhadas com relação a expectativa de checklists previos. Nessa analise, caso seja identificado algum checklist previo esperado com apontamento rejeitado,
     * outros checklists que não tenham a caracteristica de checklist previo, seram incluidos na lista de verificações desconsideradas.
     *
     * @param dossieProduto Dossiê de produto relacionado com a apuração do tratamento em execução
     * @param verificacoesVO Lista de verificações encaminhadas para armazenamento
     * @param listaVerificacoesAnalisadas Lista de controle utilizada para armazenar a referência das verificações que já foram analisadas no processo de execução do tratamento.
     * @param listaVerificacoesDesconsideradas Lista de controle utilizada para armazenar a referência das verificações que foram desconsideradas no processo de execução do tratamento e serão
     *        informadas no retorno.
     * @param listaPendenciaChecklistApontamentoAusente Lista de controle utilizada para armazenar a referência dos checklists esperados que foram reconhecidos com apontamentos esperados e não
     *        encaminhados na verificação.
     * @param listaPendenciaChecklistProcessoFaseAusente Lista de controle utilizada para armazenar a referência dos checklists esperados e que não tiveram verificações relacionadas encaminhadas. no
     *        processo de execução do tratamento.
     * @param mapaPendenciaChecklistFaseVerificacaoReplicada Mapa de controle utilizado para armazenar a as verificações encaminhadas em mais de um registro para o mesmo checklist, onde a chave é a
     *        referência ao checklist e o valor é a lista de verificações encaminhadas.
     * @return true caso a avaliação de checklists previos seja atendida indicando que os demais checklists devem ser avaliados normalmente ou false caso algum checklist previo esperado tenha sido
     *         encaminhado com indicação de apontamento não acatado, permitindo assim pular as demais verificações.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    private boolean avaliaVerificacoesChecklistPrevio(DossieProduto dossieProduto, List<VerificacaoVO> verificacoesVO, List<VerificacaoVO> listaVerificacoesAnalisadas, List<VerificacaoVO> listaVerificacoesDesconsideradas, List<ChecklistPendenteVO> listaPendenciaChecklistApontamentoAusente, List<Checklist> listaPendenciaChecklistProcessoFaseAusente, Map<Checklist, List<VerificacaoVO>> mapaPendenciaChecklistFaseVerificacaoReplicada) {

        // Variavél utilizada para controlar se as verificações dos checklists prévios foram bem sucedidas
        boolean verificacaoPreviaAtentida = true;

        // Captura a estrutura do processo gerador do dossiê para varrer seus elementos verificando a caracteristica de obrigatoriedade de seus os objetos.
        Processo processoDossie = this.processoServico.getById(dossieProduto.getProcesso().getId());

        // Captura a estrutura do processo fase que o dossiê para varrer seus elementos verificando a caracteristica de obrigatoriedade de seus os objetos.
        ProcessoFaseDossie processoFaseAtual = dossieProduto.getProcessosFaseDossie().stream()
                .max(Comparator.comparing(ProcessoFaseDossie::getId)).get();
        Processo processoFase = this.processoServico.getById(processoFaseAtual.getProcessoFase().getId());

        // No caso de checklists não documentais da fase, o dossiê já possua checklists associados para a fase, apenas estes serão analisados.
        // Somente serão verificados na parametrização de vinculação, caso nenhum checklist não documental esteja associado a fase atual do dossiê.
        // Verifica os checklists prévios por ventura já associados ao dossiê de produto na fase atual.
        List<Checklist> checklistsPrevios = dossieProduto.getChecklistsAssociados().stream()
                                                         .filter(ca -> ca.getChecklist().getIndicacaoVerificacaoPrevia() && processoFase.equals(ca.getProcessoFase()))
                                                         .map(ChecklistAssociado::getChecklist)
                                                         .collect(Collectors.toList());

        // Caso não sejam identificados checklists prévios associados ao dossiê de produto na fase atual, captura os checklists que estejam vigentes na parametrização
        // do processo
        if (Objects.isNull(checklistsPrevios) || checklistsPrevios.isEmpty()) {
            checklistsPrevios = processoFase.getVinculacoesChecklistsFase().stream()
                                            .filter(vc -> Objects.isNull(vc.getTipoDocumento()) && Objects.isNull(vc.getFuncaoDocumental())
                                                    && processoDossie.equals(vc.getProcessoDossie())
                                                    && vc.getDataRevogacao().after(Calendar.getInstance())
                                                    && vc.getChecklist().getIndicacaoVerificacaoPrevia())
                                            .sorted(Comparator.comparing(VinculacaoChecklist::getDataRevogacao))
                                            .map(VinculacaoChecklist::getChecklist)
                                            .collect(Collectors.toList());
        }

        for (Checklist checklistPrevisto : checklistsPrevios) {
            // 1 - Obtem a lista de verificações que ainda não foram e devem ser analisadas sobre as verificações enviadas.
            List<VerificacaoVO> verificacoesPendentesAnalise = verificacoesVO.stream()
                                                                             .filter(verificacao -> ((!listaVerificacoesAnalisadas.contains(verificacao))
                                                                                     && (!listaVerificacoesDesconsideradas.contains(verificacao))))
                                                                             .collect(Collectors.toList());

            // 2 - Identifica o checklist esperado para o processo fase analisado considerando a possibilidade de já existir uma associação previa.
            ChecklistAssociado checklistAssociado = dossieProduto.getChecklistsAssociados().stream()
                                                                 .filter(ca -> checklistPrevisto.equals(ca.getChecklist()) && processoFase.equals(ca.getProcessoFase()))
                                                                 .findFirst().orElse(null);

            Checklist checklistEsperado = Objects.nonNull(checklistAssociado) ? checklistAssociado.getChecklist() : checklistPrevisto;

            final Checklist checklistIdentificado = checklistEsperado;

            // 4 - Checa se existe mais algum registro de verificação encaminhado para o mesmo checklist prévio.
            List<VerificacaoVO> listaVerificacoesReplicadas = verificacoesPendentesAnalise.stream()
                                                                                          .filter(vr -> Objects.isNull(vr.getIdentificadorInstanciaDocumento())
                                                                                                  && (Objects.nonNull(vr.getIdentificadorChecklist())
                                                                                                          && vr.getIdentificadorChecklist()
                                                                                                               .equals(checklistIdentificado.getId())))
                                                                                          .collect(Collectors.toList());

            // 5 - Caso seja identificada mais de uma verificação para o mesmo checklist, adiciona no mapa de verificações duplicadas e remove da lista de analise.
            if (listaVerificacoesReplicadas.size() > 1) {
                mapaPendenciaChecklistFaseVerificacaoReplicada.put(checklistEsperado, listaVerificacoesReplicadas);
                listaVerificacoesAnalisadas.addAll(listaVerificacoesReplicadas);
                verificacaoPreviaAtentida = false;
                // Passa para o proximo checklist a ser analisado em face a identificação de multiplicidade.
                continue;
            }

            // 6 - Verifica se foi encaminhada verificação para o checklist em analise
            VerificacaoVO verificacaoIdentificada = null;
            for (VerificacaoVO verificacaoAnalise : verificacoesPendentesAnalise) {

                // Entra na análise do bloco se o identificador indicado na verificação é o mesmo do checklist esperado
                if ((Objects.isNull(verificacaoAnalise.getIdentificadorInstanciaDocumento()))
                        && (checklistEsperado.getId().equals(verificacaoAnalise.getIdentificadorChecklist()))) {

                    // Caso seja localizado remove a verificação da lista de registros a serem avaliados na verificação.
                    verificacaoIdentificada = verificacaoAnalise;
                    listaVerificacoesAnalisadas.add(verificacaoAnalise);

                    // Valida se a verificação enviada atende a todos os apontamentos definidos no checklist
                    List<Apontamento> apontamentosAusentes = new ArrayList<>();
                    // OBS: NÃO USAR OPERAÇÃO FUNCIONAL VISTO QUE DENTRO DO FOR EXISTE UMA OPERAÇÃO
                    // E PODE OCORRER ERRO DE CONCORRENCIA PELO ANINHAMENTO DE OPERAÇÕES FUNCIONAIS
                    for (Apontamento apontamento : checklistEsperado.getApontamentos()) {
                        if (verificacaoAnalise.getParecerApontamentosVO().stream()
                                              .noneMatch(parecerOperador -> parecerOperador.getIdentificadorApontamento()
                                                                                           .equals(apontamento.getId()))) {
                            apontamentosAusentes.add(apontamento);
                        }
                    }

                    // Caso seja identificado algum apontamento pendente, inclui o checklist na lista de pendencias indicando os apontamentos não localizados.
                    if (!apontamentosAusentes.isEmpty()) {
                        ChecklistPendenteVO checklistPendenteVO = new ChecklistPendenteVO();
                        checklistPendenteVO.setChecklist(checklistEsperado);
                        checklistPendenteVO.setIdentificadorInstanciaDocumento(verificacaoAnalise.getIdentificadorInstanciaDocumento());
                        checklistPendenteVO.setApontamentosAusentes(apontamentosAusentes);

                        listaPendenciaChecklistApontamentoAusente.add(checklistPendenteVO);
                        verificacaoPreviaAtentida = false;
                    } else {
                        // Caso algum apontamento não tenha sido acatado para a verificação, indica que a verificação prévia não foi atendida.
                        boolean apontamentoNaoAcatado = verificacaoAnalise.getParecerApontamentosVO().stream()
                                                                          .anyMatch(apontamento -> !apontamento.isAprovado());
                        if (apontamentoNaoAcatado) {
                            verificacaoPreviaAtentida = false;
                        }
                    }
                    break;
                }
            }

            // 7 - Verifica se já houve alguma verificação no checklist com indicação de aprovação.
            boolean verificacaoAprovadaPreviamente = Boolean.FALSE;
            List<ChecklistAssociado> checklistsAssociados = dossieProduto.getChecklistsAssociados().stream()
                                                                         .filter(ca -> checklistIdentificado.equals(ca.getChecklist()) && processoFase.equals(ca.getProcessoFase()))
                                                                         .collect(Collectors.toList());
            for (ChecklistAssociado ca : checklistsAssociados) {
                // Caso encontre uma verificação vinculada ao checklist já aprovado,
                // marca o identificador da verificação do checklist aprovado para realizar a inibição da pendência por falta de envio da verificação.
                if (ca.getVerificacoes().stream().anyMatch(v -> v.getIndicacaoVerificacao()
                        || (Objects.nonNull(v.getIndicacaoRevisao()) && v.getIndicacaoRevisao()))) {
                    verificacaoAprovadaPreviamente = Boolean.TRUE;
                    break;
                }
            }

            // Caso seja localizada uma verificação relacionada com o checklist esperado e já possua verificação anterior com indicação de aprovação:
            // DESCONSIDERADO = Adiciona a verificação na lista de verificações desconsideradas.
            if ((Objects.nonNull(verificacaoIdentificada)) && (verificacaoAprovadaPreviamente)) {
                listaVerificacoesDesconsideradas.add(verificacaoIdentificada);
                continue;
            }

            // Caso não seja localizada uma verificação relacionada com o checklist esperado e ainda não tenha nenhuma verificação anterior de aprovação:
            // PENDENCIA = Adiciona o checklist na lista de pendencias de indicando que o checklist é esperado.
            if ((Objects.isNull(verificacaoIdentificada)) && (!verificacaoAprovadaPreviamente)) {
                listaPendenciaChecklistProcessoFaseAusente.add(checklistEsperado);
                continue;
            }

            // Caso seja localizada uma verificação com o checklist esperado, sem indicador de realização da apuração e sem aprovação anterior
            // PENDENCIA = Adiciona o checklist na lista de pendencias de indicando que o checklist é esperado.
            if ((Objects.nonNull(verificacaoIdentificada)) && (!verificacaoAprovadaPreviamente)
                    && (!verificacaoIdentificada.isAnaliseRealizada())) {
                listaPendenciaChecklistProcessoFaseAusente.add(checklistEsperado);
            }
        }

        // Caso a verificação prévia não seja atendida, desconsidera todas as demais verificações ainda não analisadas e encaminhadas na mesma solicitação
        if (!verificacaoPreviaAtentida) {
            List<VerificacaoVO> listaVerificacoesNaoAnalisadas = verificacoesVO.stream()
                                                                               .filter(verificacao -> (!listaVerificacoesAnalisadas.contains(verificacao))
                                                                                       && (!listaVerificacoesDesconsideradas.contains(verificacao)))
                                                                               .collect(Collectors.toList());
            listaVerificacoesDesconsideradas.addAll(listaVerificacoesNaoAnalisadas);
        }

        return verificacaoPreviaAtentida;
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    private void avaliaVerificacoesVinculoPessoas(DossieProduto dossieProduto, List<VerificacaoVO> verificacoesVO, List<VerificacaoVO> listaVerificacoesAnalisadas, List<VerificacaoVO> listaVerificacoesDesconsideradas, List<ChecklistPendenteVO> listaPendenciaChecklistApontamentoAusente, Map<InstanciaDocumento, Checklist> mapaPendenciaInstanciaChecklistAusente, Map<InstanciaDocumento, Checklist> mapaInstanciasChecklistIdentificado, Map<InstanciaDocumento, List<VerificacaoVO>> mapaPendenciaInstanciasVerificacaoReplicada) {

        // Captura a estrutura do processo gerador do dossiê para varrer seus elementos verificando a caracteristica de obrigatoriedade de seus os objetos.
        Processo processoDossie = this.processoServico.getById(dossieProduto.getProcesso().getId());

        // Captura situação do documento conforme para utilizar nos filtros das instancias.
        SituacaoDocumento situacaoDocumentoConforme = this.situacaoDocumentoServico.getBySituacaoDocumentoEnum(SituacaoDocumentoEnum.CONFORME);

        // Percorre a lista de documentos posveis por tipo de vinculo para o processo formador do dossiê de produto
        for (ProcessoDocumento pd : processoDossie.getProcessoDocumentos()) {
            TipoRelacionamento tipoRelacionamentoDossieEnum = pd.getTipoRelacionamento();

            // Identifica todos os dossiês de cliente vinculados ao dossiê de produto associados pelo tipo de vinculo em análise
            List<DossieClienteProduto> vinculosPessoas = dossieProduto.getDossiesClienteProduto().stream()
                                                                      .filter(dcp -> tipoRelacionamentoDossieEnum.equals(dcp.getTipoRelacionamento()))
                                                                      .collect(Collectors.toList());

            // Percorre todos os vinculos de pessoas identificados para o tipo de relacionamento previsto
            for (DossieClienteProduto vinculoPessoa : vinculosPessoas) {

                // Captura as instancias de documento que atendem aos filtros:
                // Filtro 1 - Relacionadas com o vinculo de pessoas (dossiê cliente x dossiê de produto) em analise
                // Filtro 2 - Que seja vinculada a um documento do tipo de documento especifico ou componha uma função documental definida na parametrização do processo para o
                // tipo de vinculo analisado
                // Filtro 3 - Ultima situação da instancia não possua indicação de situação final
                // Filtro 4 - Não possua registro de situação conforme
                // Filtro 5 - Não esteja presente nos mapas de instancias já identificadas com checklists ausentes e instancias com checklists já identificados
                List<InstanciaDocumento> instanciasDocumento = dossieProduto.getInstanciasDocumento().stream()
                                                                            .filter(id -> ((Objects.nonNull(id.getDossieClienteProduto()))
                                                                                    && (id.getDossieClienteProduto().equals(vinculoPessoa))))
                                                                            .filter(id -> {
                                                                                TipoDocumento tipoInstancia = id.getDocumento().getTipoDocumento();
                                                                                Set<FuncaoDocumental> funcoesInstancia = new HashSet<>();
                                                                                if (Objects.nonNull(tipoInstancia.getFuncoesDocumentais())
                                                                                        && (!tipoInstancia.getFuncoesDocumentais().isEmpty())) {
                                                                                    funcoesInstancia = tipoInstancia.getFuncoesDocumentais();
                                                                                }
                                                                                return ((Objects.nonNull(pd.getTipoDocumento())
                                                                                        && tipoInstancia.equals(pd.getTipoDocumento()))
                                                                                        || (Objects.nonNull(pd.getFuncaoDocumental())
                                                                                                && funcoesInstancia.contains(pd.getFuncaoDocumental())));
                                                                            }).filter(id -> {
                                                                                SituacaoDocumento situacaoAtualFinal = id.getSituacoesInstanciaDocumento().stream()
                                                                                                                         .max(Comparator.comparing(SituacaoInstanciaDocumento::getId))
                                                                                                                         .get()
                                                                                                                         .getSituacaoDocumento();

                                                                                return (!situacaoAtualFinal.getSituacaoFinal());
                                                                            })
                                                                            .filter(id -> id.getSituacoesInstanciaDocumento().stream()
                                                                                            .noneMatch(sid -> sid.getSituacaoDocumento().equals(situacaoDocumentoConforme)))
                                                                            .filter(id -> (!mapaPendenciaInstanciaChecklistAusente.keySet().contains(id))
                                                                                    && (!mapaInstanciasChecklistIdentificado.keySet().contains(id)))
                                                                            .collect(Collectors.toList());

                // Para cada instancias de documento identificada, verifica se estão associadas ao tipo de documento/função documental definido na parametrização do processo
                instanciasDocumento.forEach((instancia) -> {
                    // Identifica se o elemento de conteudo vinculado a instancia de documento possui configuração de obrigatoriedade
                    boolean obrigatorio = pd.getObrigatorio();

                    // Realiza a avaliação da instancia de documento alimentando os mapas e listas de controle
                    this.avaliaInstanciasDocumento(dossieProduto, instancia, obrigatorio, verificacoesVO, listaVerificacoesAnalisadas, listaVerificacoesDesconsideradas, listaPendenciaChecklistApontamentoAusente, mapaPendenciaInstanciaChecklistAusente, mapaInstanciasChecklistIdentificado, mapaPendenciaInstanciasVerificacaoReplicada);
                });
            }
        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    private void avaliaVerificacoesVinculoProduto(DossieProduto dossieProduto, List<VerificacaoVO> verificacoesVO, List<VerificacaoVO> listaVerificacoesAnalisadas, List<VerificacaoVO> listaVerificacoesDesconsideradas, List<ChecklistPendenteVO> listaPendenciaChecklistApontamentoAusente, Map<InstanciaDocumento, Checklist> mapaPendenciaInstanciaChecklistAusente, Map<InstanciaDocumento, Checklist> mapaInstanciasChecklistIdentificado, Map<InstanciaDocumento, List<VerificacaoVO>> mapaPendenciaInstanciasVerificacaoReplicada) {

        // Captura a estrutura do processo gerador do dossiê para varrer produtos associados verificando quais foram contratados no dossiê.
        Processo processoDossie = this.processoServico.getById(dossieProduto.getProcesso().getId());

        // Captura situação do documento conforme para utilizar nos filtros das instancias.
        SituacaoDocumento situacaoDocumentoConforme = this.situacaoDocumentoServico.getBySituacaoDocumentoEnum(SituacaoDocumentoEnum.CONFORME);

        // Percorre a lista de produtos possiveis para o processo formador do dossiê de produto
        for (Produto p : processoDossie.getProdutos()) {

            // Verifica o produto em analise possui associação com o dossiê e em caso negativo já avança para o proximo produto.
            if (!dossieProduto.getProdutosDossie().stream().anyMatch(pd -> p.equals(pd.getProduto()))) {
                continue;
            }

            // Captura as instancias de documento que atendem aos filtros:
            // Filtro 1 - Relacionadas com o elemento de conteudo vinculado ao produto em analise
            // Filtro 2 - Ultima situação da instancia não possua indicação de situação final
            // Filtro 3 - Não possua registro de situação conforme
            // Filtro 4 - Não esteja presente nos mapas de instancias já identificadas com checklists ausentes e instancias com checklists já identificados
            List<InstanciaDocumento> instanciasDocumento = dossieProduto.getInstanciasDocumento().stream()
                                                                        .filter(id -> ((Objects.nonNull(id.getElementoConteudo()))
                                                                                && (p.equals(id.getElementoConteudo().getProduto()))))
                                                                        .filter(id -> {
                                                                            SituacaoDocumento situacaoAtualFinal = id.getSituacoesInstanciaDocumento().stream()
                                                                                                                     .max(Comparator.comparing(SituacaoInstanciaDocumento::getId))
                                                                                                                     .get()
                                                                                                                     .getSituacaoDocumento();

                                                                            return (!situacaoAtualFinal.getSituacaoFinal());
                                                                        })
                                                                        .filter(id -> id.getSituacoesInstanciaDocumento().stream()
                                                                                        .noneMatch(sid -> sid.getSituacaoDocumento().equals(situacaoDocumentoConforme)))
                                                                        .filter(id -> (!mapaPendenciaInstanciaChecklistAusente.keySet().contains(id))
                                                                                && (!mapaInstanciasChecklistIdentificado.keySet().contains(id)))
                                                                        .collect(Collectors.toList());

            // Realiza a avaliação de cada instancia identificada
            instanciasDocumento.forEach((instancia) -> {
                // Identifica se o elemento de conteudo vinculado a instancia de documento possui configuração de obrigatoriedade
                boolean obrigatorio = instancia.getElementoConteudo().getObrigatorio();

                // Realiza a avaiação da instancia de documento conforme tipo de documento/função documental definida na parametrização do processo, alimentando os mapas e
                // listas de controle
                this.avaliaInstanciasDocumento(dossieProduto, instancia, obrigatorio, verificacoesVO, listaVerificacoesAnalisadas, listaVerificacoesDesconsideradas, listaPendenciaChecklistApontamentoAusente, mapaPendenciaInstanciaChecklistAusente, mapaInstanciasChecklistIdentificado, mapaPendenciaInstanciasVerificacaoReplicada);
            });
        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    private void avaliaVerificacoesVinculoGarantias(DossieProduto dossieProduto, List<VerificacaoVO> verificacoesVO, List<VerificacaoVO> listaVerificacoesAnalisadas, List<VerificacaoVO> listaVerificacoesDesconsideradas, List<ChecklistPendenteVO> listaPendenciaChecklistApontamentoAusente, Map<InstanciaDocumento, Checklist> mapaPendenciaInstanciaChecklistAusente, Map<InstanciaDocumento, Checklist> mapaInstanciasChecklistIdentificado, Map<InstanciaDocumento, List<VerificacaoVO>> mapaPendenciaInstanciasVerificacaoReplicada) {

        // Captura situação do documento conforme para utilizar nos filtros das instancias.
        SituacaoDocumento situacaoDocumentoConforme = this.situacaoDocumentoServico.getBySituacaoDocumentoEnum(SituacaoDocumentoEnum.CONFORME);

        // Captura as instancias de documento que atendem aos filtros:
        // Filtro 1 - Relacionadas com o elemento de conteudo vinculado a garantia em analise
        // Filtro 2 - Ultima situação da instancia não possua indicação de situação final
        // Filtro 3 - Não possua registro de situação conforme
        // Filtro 4 - Não esteja presente nos mapas de instancias já identificadas com checklists ausentes e instancias com checklists já identificados
        List<InstanciaDocumento> instanciasDocumento = dossieProduto.getInstanciasDocumento().stream()
                                                                    .filter(id -> ((Objects.nonNull(id.getGarantiaInformada()))
                                                                            && (Objects.nonNull(id.getGarantiaInformada().getGarantia()))))
                                                                    .filter(id -> {
                                                                        SituacaoDocumento situacaoAtualFinal = id.getSituacoesInstanciaDocumento().stream()
                                                                                                                 .max(Comparator.comparing(SituacaoInstanciaDocumento::getId)).get()
                                                                                                                 .getSituacaoDocumento();

                                                                        return (!situacaoAtualFinal.getSituacaoFinal());
                                                                    })
                                                                    .filter(id -> id.getSituacoesInstanciaDocumento().stream()
                                                                                    .noneMatch(sid -> sid.getSituacaoDocumento().equals(situacaoDocumentoConforme)))
                                                                    .filter(id -> (!mapaPendenciaInstanciaChecklistAusente.keySet().contains(id))
                                                                            && (!mapaInstanciasChecklistIdentificado.keySet().contains(id)))
                                                                    .collect(Collectors.toList());

        // Realiza a avaliação de cada instancia identificada
        instanciasDocumento.forEach((instancia) -> {
            // Realiza a avaiação da instancia de documento conforme tipo de documento/função documental definida na parametrização do processo, alimentando os mapas e
            // listas de controle
            this.avaliaInstanciasDocumento(dossieProduto, instancia, Boolean.TRUE, verificacoesVO, listaVerificacoesAnalisadas, listaVerificacoesDesconsideradas, listaPendenciaChecklistApontamentoAusente, mapaPendenciaInstanciaChecklistAusente, mapaInstanciasChecklistIdentificado, mapaPendenciaInstanciasVerificacaoReplicada);
        });

    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    private void avaliaVerificacoesProcessosDocumentais(DossieProduto dossieProduto, List<VerificacaoVO> verificacoesVO, List<VerificacaoVO> listaVerificacoesAnalisadas, List<VerificacaoVO> listaVerificacoesDesconsideradas, List<ChecklistPendenteVO> listaPendenciaChecklistApontamentoAusente, Map<InstanciaDocumento, Checklist> mapaPendenciaInstanciaChecklistAusente, Map<InstanciaDocumento, Checklist> mapaInstanciasChecklistIdentificado, Map<InstanciaDocumento, List<VerificacaoVO>> mapaPendenciaInstanciasVerificacaoReplicada) {

        // Captura a estrutura do processo gerador do dossiê para varrer produtos associados verificando quais foram contratados no dossiê.
        Processo processoDossie = this.processoServico.getById(dossieProduto.getProcesso().getId());

        // Captura a estrutura do processo fase que o dossiê para varrer seus elementos verificando a caracteristica de obrigatoriedade de seus os objetos.
        ProcessoFaseDossie processoFaseAtual = dossieProduto.getProcessosFaseDossie().stream()
                .max(Comparator.comparing(ProcessoFaseDossie::getId)).get();
        Processo processoFase = this.processoServico.getById(processoFaseAtual.getProcessoFase().getId());

        // Captura situação do documento conforme para utilizar nos filtros das instancias.
        SituacaoDocumento situacaoDocumentoConforme = this.situacaoDocumentoServico.getBySituacaoDocumentoEnum(SituacaoDocumentoEnum.CONFORME);

        // Captura as instancias de documento que atendem aos filtros:
        // Filtro 1 - Relacionadas com o elemento de conteudo vinculado ao processo analisado (Gerador de dossiê ou fase)
        // Filtro 2 - Ultima situação da instancia não possua indicação de situação final
        // Filtro 3 - Não possua registro de situação conforme
        // Filtro 4 - Não esteja presente nos mapas de instancias já identificadas com checklists ausentes e instancias com checklists já identificados
        List<InstanciaDocumento> instanciasDocumento = dossieProduto.getInstanciasDocumento().stream().filter(id -> {
            return (Objects.nonNull(id.getElementoConteudo())
                    && ((processoDossie.equals(id.getElementoConteudo().getProcesso()))
                            || (processoFase.equals(id.getElementoConteudo().getProcesso()))));
        }).filter(id -> {
            SituacaoDocumento situacaoAtualFinal = id.getSituacoesInstanciaDocumento().stream()
                                                     .max(Comparator.comparing(SituacaoInstanciaDocumento::getId)).get().getSituacaoDocumento();

            return (!situacaoAtualFinal.getSituacaoFinal());
        }).filter(id -> id.getSituacoesInstanciaDocumento().stream()
                          .noneMatch(sid -> sid.getSituacaoDocumento().equals(situacaoDocumentoConforme)))
                                                                    .filter(id -> (!mapaPendenciaInstanciaChecklistAusente.keySet().contains(id))
                                                                            && (!mapaInstanciasChecklistIdentificado.keySet().contains(id)))
                                                                    .collect(Collectors.toList());

        // Realiza a avaliação de cada instancia identificada
        instanciasDocumento.forEach((instancia) -> {
            // Identifica se o elemento de conteudo vinculado a instancia de documento possui configuração de obrigatoriedade
            boolean obrigatorio = instancia.getElementoConteudo().getObrigatorio();

            // Realiza a avaiação da instancia de documento conforme tipo de documento/função documental definida na parametrização do processo, alimentando os mapas e
            // listas de controle
            this.avaliaInstanciasDocumento(dossieProduto, instancia, obrigatorio, verificacoesVO, listaVerificacoesAnalisadas, listaVerificacoesDesconsideradas, listaPendenciaChecklistApontamentoAusente, mapaPendenciaInstanciaChecklistAusente, mapaInstanciasChecklistIdentificado, mapaPendenciaInstanciasVerificacaoReplicada);
        });
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    private void avaliaInstanciasDocumento(DossieProduto dossieProduto, InstanciaDocumento instancia, boolean elementoObrigatorio, List<VerificacaoVO> verificacoesVO, List<VerificacaoVO> listaVerificacoesAnalisadas, List<VerificacaoVO> listaVerificacoesDesconsideradas, List<ChecklistPendenteVO> listaPendenciaChecklistApontamentoAusente, Map<InstanciaDocumento, Checklist> mapaPendenciaInstanciaChecklistAusente, Map<InstanciaDocumento, Checklist> mapaInstanciasChecklistIdentificado, Map<InstanciaDocumento, List<VerificacaoVO>> mapaPendenciaInstanciasVerificacaoReplicada) {

        // Captura a estrutura do processo gerador do dossiê para varrer seus elementos verificando a caracteristica de obrigatoriedade de seus os objetos.
        Processo processoDossie = this.processoServico.getById(dossieProduto.getProcesso().getId());

        // Captura a estrutura do processo fase que o dossiê para varrer seus elementos verificando a caracteristica de obrigatoriedade de seus os objetos.
        ProcessoFaseDossie processoFaseAtual = dossieProduto.getProcessosFaseDossie().stream()
                .max(Comparator.comparing(ProcessoFaseDossie::getId)).get();
        Processo processoFase = this.processoServico.getById(processoFaseAtual.getProcessoFase().getId());

        // Identifica o tipo de documento associado a instancia em analise
        TipoDocumento tipoDocumento = instancia.getDocumento().getTipoDocumento();

        // 1 - Obtem a lista de que ainda não foram e devem ser analisadas sobre as verificações enviadas.
        List<VerificacaoVO> verificacoesPendentesAnalise = verificacoesVO.stream().filter(v -> {
            return ((!listaVerificacoesAnalisadas.contains(v)) && (!listaVerificacoesDesconsideradas.contains(v)));
        }).collect(Collectors.toList());

        // 2 - Identifica o checklist esperado para o tipo de documento analisado considerando a possibilidade de já existir uma associação previa.
        ChecklistAssociado checklistAssociado = dossieProduto.getChecklistsAssociados().stream()
                                                             .filter(ca -> instancia.equals(ca.getInstanciaDocumento()) && processoFase.equals(ca.getProcessoFase()))
                                                             .findFirst().orElse(null);

        Checklist checklistEsperado = Objects.nonNull(checklistAssociado) ? checklistAssociado.getChecklist() : null;

        // 3 - Caso não exista nenhum checklist associado a instância de documento, pecorre a lista de vinculações para identificar o qual o checklist esperado.
        if (checklistEsperado == null) {
            // Checa se existe algum checklist definido para o tipo de documento da instancia na fase atual, caso não exista a vinculação será nula.
            VinculacaoChecklist vinculacaoChecklist = processoFase.getVinculacoesChecklistsFase().stream()
                                                                  .filter(vc -> {
                                                                      return (tipoDocumento.equals(vc.getTipoDocumento())
                                                                              && processoDossie.equals(vc.getProcessoDossie())
                                                                              && processoFase.equals(vc.getProcessoFase())
                                                                              && Calendar.getInstance().before(vc.getDataRevogacao()));
                                                                  }).sorted(Comparator.comparing(VinculacaoChecklist::getDataRevogacao)).findFirst().orElse(null);

            // Caso não exista checklist vinculado ao tipo, verifica se existe para alguma das funções exercidas
            if (Objects.isNull(vinculacaoChecklist)) {
                vinculacaoChecklist = processoFase.getVinculacoesChecklistsFase().stream().filter(vc -> {
                    boolean funcaoIdentificada = Boolean.FALSE;
                    for (FuncaoDocumental funcaoDocumental : tipoDocumento.getFuncoesDocumentais()) {
                        if ((funcaoDocumental.equals(vc.getFuncaoDocumental())
                                && processoDossie.equals(vc.getProcessoDossie())
                                && processoFase.equals(vc.getProcessoFase())
                                && Calendar.getInstance().before(vc.getDataRevogacao()))) {
                            funcaoIdentificada = Boolean.TRUE;
                            break;
                        }
                    }
                    return funcaoIdentificada;
                }).sorted(Comparator.comparing(VinculacaoChecklist::getDataRevogacao)).findFirst().orElse(null);
            }

            // Atribui o checklist associado a vinculação identificada
            if (vinculacaoChecklist != null) {
                checklistEsperado = vinculacaoChecklist.getChecklist();
            } else {
                // Caso não exista checklist definido finaliza o fluxo de forma que a instância não deva ser analisada
                return;
            }
        }

        // 4 - Checa se existe mais algum registro de verificação encaminhado para a mesma instancia de documento.
        List<VerificacaoVO> listaVerificacoesInstancia = verificacoesPendentesAnalise.stream().filter(vr -> {
            return Objects.nonNull(vr.getIdentificadorInstanciaDocumento())
                    && vr.getIdentificadorInstanciaDocumento().equals(instancia.getId());
        }).collect(Collectors.toList());

        // 5 - Caso seja identificada mais de uma verificação para a mesma instancia de documento, adiciona no mapa de verificações duplicadas e remove da lista de
        // analise.
        if (listaVerificacoesInstancia.size() > 1) {
            mapaPendenciaInstanciasVerificacaoReplicada.put(instancia, listaVerificacoesInstancia);
            listaVerificacoesAnalisadas.addAll(listaVerificacoesInstancia);
            // Passa para a proxima instancia de documento a ser analisado em face a identificação de multiplicidade.
            return;
        }

        // 6 - Verifica se foi encaminhada verificação para a instancia em analise
        VerificacaoVO verificacaoIdentificada = null;
        for (VerificacaoVO vr : verificacoesPendentesAnalise) {
            if ((Objects.nonNull(vr.getIdentificadorInstanciaDocumento()))
                    && (instancia.getId().equals(vr.getIdentificadorInstanciaDocumento()))
                    && (checklistEsperado.getId().equals(vr.getIdentificadorChecklist()))) {

                // Caso seja localizado remove a verificação da lista de registros a serem avaliados na verificação.
                verificacaoIdentificada = vr;
                listaVerificacoesAnalisadas.add(vr);

                if (elementoObrigatorio || vr.isAnaliseRealizada()) {
                    // Valida se a verificação enviada atende a todos os apontamentos definidos no checklist
                    List<Apontamento> apontamentosAusentes = new ArrayList<>();
                    for (Apontamento apontamento : checklistEsperado.getApontamentos()) {
                        if (!vr.getParecerApontamentosVO().stream()
                               .filter(pa -> pa.getIdentificadorApontamento().equals(apontamento.getId())).findAny()
                               .isPresent()) {
                            apontamentosAusentes.add(apontamento);
                        }
                    }

                    // Caso seja identificado algum apontamento pendente, inclui o checklist na lista de pendencias indicando os apontamentos não localizados.
                    if (!apontamentosAusentes.isEmpty()) {
                        ChecklistPendenteVO checklistPendenteVO = new ChecklistPendenteVO();
                        checklistPendenteVO.setChecklist(checklistEsperado);
                        checklistPendenteVO.setIdentificadorInstanciaDocumento(vr.getIdentificadorInstanciaDocumento());
                        checklistPendenteVO.setApontamentosAusentes(apontamentosAusentes);

                        listaPendenciaChecklistApontamentoAusente.add(checklistPendenteVO);
                    }

                    break;
                }
            }
        }

        // 7 - Verifica se já houve alguma verificação na instancia com indicação de aprovação ou marcada como desabilitada.
        boolean verificacaoAprovadaPreviamente = Boolean.FALSE;
        List<ChecklistAssociado> checklistsAssociadosInstancia = dossieProduto.getChecklistsAssociados().stream()
                                                                              .filter(ca -> {
                                                                                  return instancia.equals(ca.getInstanciaDocumento()) && processoFase.equals(ca.getProcessoFase());
                                                                              }).collect(Collectors.toList());
        for (ChecklistAssociado ca : checklistsAssociadosInstancia) {
            // Caso encontre uma verificação vinculada a instancia já aprovada, ou desabilitada (não executada),
            // marca o identificador de instancia aprovada para realizar a inibição da pendência por falta de envio da verificação.
            if (ca.getVerificacoes().stream().filter(v -> {
                return v.getIndicacaoRealizacao()
                        && ((Objects.nonNull(v.getIndicacaoVerificacao()) && v.getIndicacaoVerificacao()) || (Objects.nonNull(v.getIndicacaoRevisao()) && v.getIndicacaoRevisao()));
            }).findFirst().isPresent()) {
                verificacaoAprovadaPreviamente = Boolean.TRUE;
                break;
            }
        }

        // Caso seja localizada uma verificação relacionada a instância do documento com o checklist esperado,
        // mas a instancia já possua verificação anterior com indicação de aprovação:
        // DESCONSIDERADO = Adiciona a verificação na lista de verificações desconsideradas.
        if ((verificacaoIdentificada != null) && (verificacaoAprovadaPreviamente)) {
            listaVerificacoesDesconsideradas.add(verificacaoIdentificada);
            return;
        }

        // Caso não seja localizada uma verificação relacionada a instância do documento com o checklist esperado
        // e a instancia ainda não tenha nenhuma verificação anterior de aprovação:
        // PENDENCIA = Adiciona a instancia de documento no mapa de pendencias de instancias indicando o checklist esperado.
        if ((verificacaoIdentificada == null) && (!verificacaoAprovadaPreviamente)) {
            mapaPendenciaInstanciaChecklistAusente.put(instancia, checklistEsperado);
            return;
        }

        // Caso não seja localizada uma verificação relacionada a instância do documento com o checklist esperado, sem indicador de realização da apuração,
        // mas a configuração do processo indique o tipo de documento relacionado a instancia como obrigatorio:
        // PENDENCIA = Adiciona a verificação no mapa de pendencias de instancias indicando o checklist esperado.
        if ((verificacaoIdentificada != null) && (!verificacaoAprovadaPreviamente) && (!verificacaoIdentificada.isAnaliseRealizada()) && (elementoObrigatorio)) {
            mapaPendenciaInstanciaChecklistAusente.put(instancia, checklistEsperado);
            return;
        }

        // Caso não seja localizada uma verificação relacionada a instância do documento com o checklist esperado, com indicador de realização da apuração:
        // VALIDO = Adiciona a instância no mapa de registros com checklists identificados.
        if ((verificacaoIdentificada != null) && (!verificacaoAprovadaPreviamente) && (verificacaoIdentificada.isAnaliseRealizada())) {
            mapaInstanciasChecklistIdentificado.put(instancia, checklistEsperado);
        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    private void avaliaVerificacoesProcessosNaoDocumentais(DossieProduto dossieProduto, List<VerificacaoVO> verificacoesVO, List<VerificacaoVO> listaVerificacoesAnalisadas, List<VerificacaoVO> listaVerificacoesDesconsideradas, List<ChecklistPendenteVO> listaPendenciaChecklistApontamentoAusente, List<Checklist> listaPendenciaChecklistProcessoFaseAusente, Map<Checklist, List<VerificacaoVO>> mapaPendenciaChecklistFaseVerificacaoReplicada) {

        // Captura a estrutura do processo gerador do dossiê para varrer seus elementos verificando a caracteristica de obrigatoriedade de seus os objetos.
        Processo processoDossie = this.processoServico.getById(dossieProduto.getProcesso().getId());

        // Captura a estrutura do processo fase que o dossiê para varrer seus elementos verificando a caracteristica de obrigatoriedade de seus os objetos.
        ProcessoFaseDossie processoFaseAtual = dossieProduto.getProcessosFaseDossie().stream()
                .max(Comparator.comparing(ProcessoFaseDossie::getId)).get();
        Processo processoFase = this.processoServico.getById(processoFaseAtual.getProcessoFase().getId());

        // No caso de checklists não documentais da fase, o dossiê já possua checklists associados para a fase, apenas estes serão analisados.
        // Somente serão verificados na parametrização de vinculação, caso nenhum checklist não documental esteja associado a fase atual do dossiê.

        // Verifica os checklists não documentais por ventura já associados ao dossiê de produto na fase atual.
        List<Checklist> checklistsFase = dossieProduto.getChecklistsAssociados().stream()
                                                      .filter(ca -> !ca.getChecklist().getIndicacaoVerificacaoPrevia() && Objects.isNull(ca.getInstanciaDocumento())
                                                              && processoFase.equals(ca.getProcessoFase()))
                                                      .map(ca -> ca.getChecklist()).collect(Collectors.toList());

        // Caso não sejam identificados checklists associados ao dossiê de produto na fase atual,
        // captura os checklists que estejam vigentes na parametrização do processo
        if (Objects.isNull(checklistsFase) || checklistsFase.isEmpty()) {
            checklistsFase = processoFase.getVinculacoesChecklistsFase().stream()
                                         .filter(vc -> Objects.isNull(vc.getTipoDocumento()) && Objects.isNull(vc.getFuncaoDocumental())
                                                 && !vc.getChecklist().getIndicacaoVerificacaoPrevia()
                                                 && processoDossie.equals(vc.getProcessoDossie())
                                                 && vc.getDataRevogacao().after(Calendar.getInstance()))
                                         .sorted(Comparator.comparing(VinculacaoChecklist::getDataRevogacao)).map(vc -> vc.getChecklist())
                                         .collect(Collectors.toList());
        }

        for (Checklist checklistPrevisto : checklistsFase) {
            // 1 - Obtem a lista de verificações que ainda não foram e devem ser analisadas sobre as verificações enviadas.
            List<VerificacaoVO> verificacoesPendentesAnalise = verificacoesVO.stream().filter(v -> {
                return ((!listaVerificacoesAnalisadas.contains(v)) && (!listaVerificacoesDesconsideradas.contains(v)));
            }).collect(Collectors.toList());

            // 2 - Identifica o checklist esperado para o tipo de documento analisado considerando a possibilidade de já existir uma associação previa.
            ChecklistAssociado checklistAssociado = dossieProduto.getChecklistsAssociados().stream()
                                                                 .filter(ca -> checklistPrevisto.equals(ca.getChecklist()) && processoFase.equals(ca.getProcessoFase()))
                                                                 .findFirst().orElse(null);
            Checklist checklistEsperado = Objects.nonNull(checklistAssociado) ? checklistAssociado.getChecklist() : checklistPrevisto;

            final Checklist checklistIdentificado = checklistEsperado;

            // 4 - Checa se existe mais algum registro de verificação encaminhado para o mesmo checklist não documental.
            List<VerificacaoVO> listaVerificacoesInstancia = verificacoesPendentesAnalise.stream().filter(vr -> {
                return Objects.isNull(vr.getIdentificadorInstanciaDocumento())
                        && vr.getIdentificadorChecklist().equals(checklistIdentificado.getId());
            }).collect(Collectors.toList());

            // 5 - Caso seja identificada mais de uma verificação para o mesmo checklist, adiciona no mapa de verificações duplicadas e remove da lista de analise.
            if (listaVerificacoesInstancia.size() > 1) {
                mapaPendenciaChecklistFaseVerificacaoReplicada.put(checklistEsperado, listaVerificacoesInstancia);
                listaVerificacoesAnalisadas.addAll(listaVerificacoesInstancia);
                // Passa para o proximo checklist a ser analisado em face a identificação de multiplicidade.
                continue;
            }

            // 6 - Verifica se foi encaminhada verificação para o checklist em analise
            VerificacaoVO verificacaoIdentificada = null;
            for (VerificacaoVO vr : verificacoesPendentesAnalise) {
                if ((Objects.isNull(vr.getIdentificadorInstanciaDocumento())) && (checklistEsperado.getId().equals(vr.getIdentificadorChecklist()))) {

                    // Caso seja localizado remove a verificação da lista de registros a serem avaliados na verificação.
                    verificacaoIdentificada = vr;
                    listaVerificacoesAnalisadas.add(vr);

                    if (vr.isAnaliseRealizada()) {
                        // Valida se a verificação envaida atende a todos os apontamentos definidos no checklist
                        List<Apontamento> apontamentosAusentes = new ArrayList<>();
                        // OBS: NÃO USAR OPERAÇÃO FUNCIONAL VISTO QUE DENTRO DO FOR EXISTE UMA OPERAÇÃO
                        // E PODE OCORRER ERRO DE CONCORRENCIA PELO ANINHAMENTO DE OPERAÇÕES FUNCIONAIS
                        for (Apontamento apontamento : checklistEsperado.getApontamentos()) {
                            if (!vr.getParecerApontamentosVO().stream()
                                   .filter(pa -> pa.getIdentificadorApontamento().equals(apontamento.getId()))
                                   .findAny().isPresent()) {
                                apontamentosAusentes.add(apontamento);
                            }
                        }

                        // Caso seja identificado algum apontamento pendente, inclui o checklist na lista de pendencias indicando os apontamentos não localizados.
                        if (!apontamentosAusentes.isEmpty()) {
                            ChecklistPendenteVO checklistPendenteVO = new ChecklistPendenteVO();
                            checklistPendenteVO.setChecklist(checklistEsperado);
                            checklistPendenteVO.setIdentificadorInstanciaDocumento(vr.getIdentificadorInstanciaDocumento());
                            checklistPendenteVO.setApontamentosAusentes(apontamentosAusentes);

                            listaPendenciaChecklistApontamentoAusente.add(checklistPendenteVO);
                        }
                    }

                    break;
                }
            }

            // 7 - Verifica se já houve alguma verificação no checklist com indicação de aprovação.
            boolean verificacaoAprovadaPreviamente = Boolean.FALSE;
            List<ChecklistAssociado> checklistsAssociados = dossieProduto.getChecklistsAssociados().stream()
                                                                         .filter(ca -> {
                                                                             return checklistIdentificado.equals(ca.getChecklist())
                                                                                     && processoFase.equals(ca.getProcessoFase());
                                                                         }).collect(Collectors.toList());
            for (ChecklistAssociado ca : checklistsAssociados) {
                // Caso encontre uma verificação vinculada ao checklist já aprovado,
                // marca o identificador da verificação do checklist aprovado para realizar a inibição da pendência por falta de envio da verificação.
                if (ca.getVerificacoes().stream().filter(v -> {
                    return v.getIndicacaoVerificacao()
                            || (Objects.nonNull(v.getIndicacaoRevisao()) && v.getIndicacaoRevisao());
                }).findAny().isPresent()) {
                    verificacaoAprovadaPreviamente = Boolean.TRUE;
                    break;
                }
            }

            // Caso seja localizada uma verificação relacionada a instância do documento com o checklist esperado e
            // já possua verificação anterior com indicação de aprovação:
            // DESCONSIDERADO = Adiciona a verificação na lista de verificações desconsideradas.
            if ((verificacaoIdentificada != null) && (verificacaoAprovadaPreviamente)) {
                listaVerificacoesDesconsideradas.add(verificacaoIdentificada);
                continue;
            }

            // Caso não seja localizada uma verificação relacionada a instância do documento com o checklist esperado
            // e ainda não tenha nenhuma verificação anterior de aprovação:
            // PENDENCIA = Adiciona o checklist na lista de pendencias de indicando que o checklist é esperado.
            if ((verificacaoIdentificada == null) && (!verificacaoAprovadaPreviamente)) {
                listaPendenciaChecklistProcessoFaseAusente.add(checklistEsperado);
            }
        }
    }

    /**
     * <p>
     * Realiza a inclusão de um registro de verificação incluindo a associação do checklist ao dossiê de produto e os registros de parecer.
     * </p>
     * <p>
     * Os objetos passados em parametros que representam o mapa de situações definidas para as instancias de documento e o indicativo de encaminhamento do dossiê para sinalização de pendência de
     * informação são referencia de objetos a serem alimentados internamente ao percorrer os apontamentos e identificar o comportamento esperado realizado na configuração do checklist
     * </p>
     *
     * @param dossieProduto Referencia do Dossiê de Produto sob contexto transacional, utilizado na associação do registro de associação do checklist a ser criado.
     * @param checklist Referência do Checklist sob contexto transacional, utilizado na associação do registro de associação com o Dossiê de Produto a ser criado
     * @param verificacaoVO Objeto que representa as informações encaminhadas pelo operados para os apontamentos associados ao checklist, incluindo observação realizada para cada analise de
     *        apontamento.
     * @param dataHoraRegistro Referencia de objeto Calendar a ser utilizado na vinculação da data hora do registro de forma a manter homogeneidade de todas as verificações realizadas na mesam
     *        requisição, mantendo a data e hora de inicio da execução do serviço.
     * @param mapaSituacoesDefinidas Objeto passado como referência de memoria para ser modificado internamento indicando a situação a ser alocada as Instancias de Documento após a verificação do
     *        comportamento esperado caso haja rejeição de algum apontamento.
     * @param indicativoPendenciaInformacaoDossie Objeto passado como referência de memoria para ser modificado internamento indicando se o fluxo deverá sinalizar o encaminhamento do Dossiê de Produto
     *        para situação "PENDENTE DE INFORMACAO" após a verificação do comportamento esperado caso haja rejeição de algum apontamento que determine este fim.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    private SituacaoDossieEnum registraVerificacaoRealizada(DossieProduto dossieProduto, Checklist checklist, VerificacaoVO verificacaoVO, Calendar dataHoraRegistro, Map<InstanciaDocumento, SituacaoDocumentoEnum> mapaSituacoesDefinidas) {

        // Cria o registro de associação do checklist ao dossiê produto, processo, fase e instancia de documento quando for o caso
        ChecklistAssociado checklistAssociado;

        checklistAssociado = dossieProduto.getChecklistsAssociados().stream().filter(ca -> {

            ProcessoFaseDossie processoFaseAtual = dossieProduto.getProcessosFaseDossie().stream()
                    .max(Comparator.comparing(ProcessoFaseDossie::getDataHoraInclusao)).get();
            boolean mesmoProcessoFase = processoFaseAtual.getProcessoFase().equals(ca.getProcessoFase());
            boolean mesmoChecklist = checklist.equals(ca.getChecklist());
            boolean verificacaoNaoDocumental = Objects.isNull(verificacaoVO.getIdentificadorInstanciaDocumento()) && Objects.isNull(ca.getInstanciaDocumento());
            boolean mesmaInstanciaVerificada = Objects.nonNull(verificacaoVO.getIdentificadorInstanciaDocumento()) && Objects.nonNull(ca.getInstanciaDocumento())
                    && verificacaoVO.getIdentificadorInstanciaDocumento().equals(ca.getInstanciaDocumento().getId());

            return (mesmoProcessoFase && mesmoChecklist && (verificacaoNaoDocumental || mesmaInstanciaVerificada));
        }).findFirst().orElse(null);

        // Caso não exista checklista associado ao cenario, cria um novo vinculando os
        if (Objects.isNull(checklistAssociado)) {
            checklistAssociado = new ChecklistAssociado();
            checklistAssociado.setChecklist(checklist);
            checklistAssociado.setDossieProduto(dossieProduto);
            ProcessoFaseDossie processoFaseAtual = dossieProduto.getProcessosFaseDossie().stream()
                    .max(Comparator.comparing(ProcessoFaseDossie::getId)).get();
            checklistAssociado.setProcessoFase(processoFaseAtual.getProcessoFase());
            if (Objects.nonNull(verificacaoVO.getIdentificadorInstanciaDocumento())) {
                InstanciaDocumento instanciaDocumento = dossieProduto.getInstanciasDocumento().stream()
                                                                     .filter(instancia -> instancia.getId().equals(verificacaoVO.getIdentificadorInstanciaDocumento()))
                                                                     .findFirst().get();
                checklistAssociado.setInstanciaDocumento(instanciaDocumento);
            }

            // Realiza a persistência do registro de associação do checklist
            this.entityManager.persist(checklistAssociado);
        }

        // Captura matricula do operador responsavel pelo registro da operacao
        final String matriculaOperador = this.keycloakService.getMatricula();

        // Captura unidade de vinculação do operador responsavel pelo registro da operacao
        final Integer unidadeOperador = this.keycloakService.getLotacaoAdministrativa();

        // Cria o registro de verificação armazenando a data e hora de execução, matricula unidade do operador e checklist
        Verificacao verificacao = new Verificacao();
        verificacao.setChecklistAssociado(checklistAssociado);
        verificacao.setDataHoraVerificacao(dataHoraRegistro);
        verificacao.setMatriculaOperador(matriculaOperador);
        verificacao.setUnidadeOperador(unidadeOperador);
        verificacao.setIndicacaoRealizacao(verificacaoVO.isAnaliseRealizada());

        // Inicializa lista que armazenará os registros de parecer criados para serem persistidos após a realização de persistência do registro de verificação.
        List<Parecer> listaPareceresParaPersistir = new ArrayList<>();

        // Declara variavel utilizada para armazenar a indicação de retorno sobre a situação a ser definida para o dossiê, podendo ser aplicados:
        // - CONFORME;
        // - PENDENTE DE INFORMAÇÃO
        // - PENDENTE DE SEGURANÇA
        SituacaoDossieEnum situacaoDossieEnum = SituacaoDossieEnum.CONFORME;

        // Caso a analise tenha sido realizada adiciona os registros que representam o parecer para cada apontamento e vincula estes ao registro de verificação
        if (verificacaoVO.isAnaliseRealizada()) {
            // Adiciona todos as analises de apontamento recebidas em um mapa cuja chave é o identificador do parecer para facilitar a captura.
            Map<Long, ParecerApontamentoVO> mapaApontamentosVO = verificacaoVO.getParecerApontamentosVO().stream()
                                                                              .collect(Collectors.toMap(p -> p.getIdentificadorApontamento(), p -> p));

            boolean verificacaoAprovada = Boolean.TRUE;

            // Percorre todos os apontamentos vinculados ao checklist e adiciona o registro de parecer para cada um associado ao registro da verificação
            for (Apontamento apontamento : checklist.getApontamentos()) {
                ParecerApontamentoVO parecerVO = mapaApontamentosVO.get(apontamento.getId());

                // Cria o registro que representa o parecer apresentado para o apontamento
                Parecer parecer = new Parecer();
                parecer.setApontamento(apontamento);
                parecer.setComentarioTratamento(parecerVO.getComentario());
                parecer.setIndicacaoAprovado(parecerVO.isAprovado());
                parecer.setVerificacao(verificacao);

                // Adiciona o parecer recem criado na coleção de pareceres da verificação
                verificacao.addPareceres(parecer);

                // Caso o parecer não seja de aprovação, indica a negativa de aprovação para a verificação e
                // identifica o comportamento esperado com o dossiê ou instancia de documento.
                if (!parecerVO.isAprovado()) {
                    verificacaoAprovada = Boolean.FALSE;

                    if (apontamento.getIndicativoRejeicao()) {

                        // Caso o checklist seja vinculado a um documento instancia de documento, a instancia ganha a situação de rejeitado
                        if ((Objects.nonNull(checklistAssociado.getInstanciaDocumento()))) {
                            mapaSituacoesDefinidas.put(checklistAssociado.getInstanciaDocumento(), SituacaoDocumentoEnum.REJEITADO);
                        }

                        // Caso a situação já tenha sido definida como PENDENTE_SEGURANCA terá "preferência" no retorno sobre a PENDENCIA_INFORMACAO
                        if (!SituacaoDossieEnum.PENDENTE_SEGURANCA.equals(situacaoDossieEnum)) {
                            situacaoDossieEnum = SituacaoDossieEnum.PENDENTE_INFORMACAO;
                        }

                    } else if ((apontamento.getIndicativoSeguranca()) && (Objects.nonNull(checklistAssociado.getInstanciaDocumento()))) {
                        mapaSituacoesDefinidas.put(checklistAssociado.getInstanciaDocumento(), SituacaoDocumentoEnum.QUARENTENA);
                        situacaoDossieEnum = SituacaoDossieEnum.PENDENTE_SEGURANCA;
                    } else if (apontamento.getIndicativoInformacao()) {
                        // Caso a situação já tenha sido definida como PENDENTE_SEGURANCA terá "preferência" no retorno sobre a PENDENCIA_INFORMACAO
                        if (!SituacaoDossieEnum.PENDENTE_SEGURANCA.equals(situacaoDossieEnum)) {
                            situacaoDossieEnum = SituacaoDossieEnum.PENDENTE_INFORMACAO;
                        }
                    }
                }

                listaPareceresParaPersistir.add(parecer);
            }

            // Após percorrer todos os apontamentos do checklist atribui o resultado de aprovação a verificação que será positivo apenas se todos os apontamentos
            verificacao.setIndicacaoVerificacao(verificacaoAprovada);

            // Caso o checklist seja associado a uma instancia de documento e não haja nenhum indicativo de rejeição do documento:
            // Deverá ter atribuido situação "CONFORME"
            if (Objects.nonNull(checklistAssociado.getInstanciaDocumento())) {
                // IMPORTANTE: O uso do "putIfAbsent" garante que caso já exista uma valor definido para a chave no mapa, o valor não será sobreposto.
                mapaSituacoesDefinidas.putIfAbsent(checklistAssociado.getInstanciaDocumento(), SituacaoDocumentoEnum.CONFORME);
            }

        } else {
            // Caso a análise não seja realizada, a verificação é marcada como não aprovada
            verificacao.setIndicacaoVerificacao(Boolean.FALSE);

            // Indica a atribuição da situação IGNORADA para a instancia de documento não avaliada pelo operador.
            if (checklistAssociado.getInstanciaDocumento() != null) {
                mapaSituacoesDefinidas.putIfAbsent(checklistAssociado.getInstanciaDocumento(), SituacaoDocumentoEnum.IGNORADO);
            }
        }
        // Realiza a persistência do registro de verificação recem criado.
        this.entityManager.persist(verificacao);

        // Realiza a persistencia da lista de pareceres recem criados
        listaPareceresParaPersistir.forEach(p -> this.entityManager.persist(p));

        return situacaoDossieEnum;
    }

    /**
     * Retorna dossiê de produto para tratamento indicado.
     * 
     * @param id Identificador do dossiê de produto.
     * @return Dossie de Produto localizado para tratamento.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNTTO,
        ConstantesUtil.PERFIL_MTRSDNTTG
    })
    public DossieProduto selecionaDossieProduto(Long id) {
        DossieProduto dossieProduto = this.dossieProdutoServico.getById(id);

        if (dossieProduto == null) {
            throw new SimtrRequisicaoException("TS.sDP.001 - Dossiê de Produto não localizado sob identificador informado");
        }

        Integer lotacaoAdministrativa = this.keycloakService.getLotacaoAdministrativa();
        Integer lotacaoFisica = this.keycloakService.getLotacaoFisica();

        // Verifica se a lotação do usuário está autorizada a manipular o dossiê no momento.
        if (!dossieProduto.getUnidadesTratamento().stream()
                          .filter(ut -> (ut.getUnidade().equals(lotacaoAdministrativa) || (ut.getUnidade().equals(lotacaoFisica))))
                          .findAny()
                          .isPresent()) {
            if (!(dossieProduto.getUnidadeCriacao().equals(lotacaoAdministrativa)) && !(dossieProduto.getUnidadeCriacao().equals(lotacaoFisica))) {
                throw new SimtrPermissaoException("TS.sDP.002 - A unidade de lotação do usuário não esta apta a realizar tratamento neste dossiê.");
            }
        }

        if (!dossieProduto.getProcesso().getTratamentoSeletivo()) {
            throw new SimtrPermissaoException("TS.sDP.003 - Este processo não permite realizar o tratamento seletivo. Utilize a opção de captura de dossiês para tratamento por fila.");
        }

        SituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get();
        TipoSituacaoDossie tipoSituacaoAguardandoTratamento = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.AGUARDANDO_TRATAMENTO);
        
        if (situacaoAtualDossie.getTipoSituacaoDossie().equals(tipoSituacaoAguardandoTratamento)) {

            TipoSituacaoDossie tipoSituacaoDossieEmTratamento = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.EM_TRATAMENTO);

            this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoDossieEmTratamento, null, null);

        } else {
            String nomeSituacaoAtual = situacaoAtualDossie.getTipoSituacaoDossie().getNome();
            String mensagem = MessageFormat.format("TS.sDP.004 - A situação atual deste dossiê de produto não permite realizar o tratamento. Situação Atual: {0}", nomeSituacaoAtual);
            throw new SimtrRequisicaoException(mensagem);
        }

        return dossieProduto;
    }
    
    /**
     * Adiciona uma nova situação \"Em Tratamento\" no dossiê de produto para renovar o tempo para o tratamento.
     * 
     * @param id Identificador do dossiê de produto.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNTTO,
        ConstantesUtil.PERFIL_MTRSDNTTG
    })
    public void renovarTempoTratamento(Long id) {
        DossieProduto dossieProduto = this.dossieProdutoServico.getById(id);

        if (dossieProduto == null) {
            throw new SimtrRequisicaoException("TS.rTT.001 - Dossiê de Produto não localizado sob identificador informado");
        }
        
        SituacaoDossie situacaoAtualDossie = dossieProduto.getSituacoesDossie().stream().max(Comparator.comparing(SituacaoDossie::getId)).get();
        TipoSituacaoDossie tipoSituacaoAguardandoTratamento = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.EM_TRATAMENTO);
        
        if(!this.keycloakService.getMatricula().equals(situacaoAtualDossie.getResponsavel())) {
            String responsavel = situacaoAtualDossie.getResponsavel();
            String mensagem = MessageFormat.format("TS.sDP.002 - Usuário não autorizado a realizar renovação de tempo de sessão. Dossiê de produto em tratamento pelo usuário: {0}", responsavel);
            throw new SimtrPermissaoException(mensagem);
        }

        if (situacaoAtualDossie.getTipoSituacaoDossie().equals(tipoSituacaoAguardandoTratamento)) {

            TipoSituacaoDossie tipoSituacaoDossieEmTratamento = this.situacaoDossieServico.getByTipoSituacaoDossieEnum(SituacaoDossieEnum.EM_TRATAMENTO);
            String observacao = "TEMPO DE TRATAMENTO RENOVADO PELO OPERADOR";
            
            this.situacaoDossieServico.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoDossieEmTratamento, observacao, null);

        } else {
            throw new SimtrRequisicaoException("TS.sDP.003 - Este dossiê não esta em situação de execução do tratamento e não pode ter o tempo de sessão renovado.");
        }
    }
}
