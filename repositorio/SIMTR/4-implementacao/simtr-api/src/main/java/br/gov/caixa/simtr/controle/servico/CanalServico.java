/*
 * Copyright (c) 2018 Caixa Econômica Federal. Todos os direitos
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
package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.postgresql.util.PSQLException;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.pedesgo.arquitetura.util.UtilRest;
import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.enumerator.JanelaTemporalExtracaoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.canal.CanalManutencaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.retaguarda.outsourcing.RetornoAvaliacaoExtracaoDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.util.KeycloakUtil;
import java.util.logging.Level;

@Stateless
@PermitAll
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class CanalServico extends AbstractService<Canal, Integer> {

    @EJB
    private KeycloakUtil keycloakUtil;

    @Inject
    private EntityManager entityManager;

    private final Map<String, Canal> mapaClienteSSO = new HashMap<>();
    private Calendar dataHoraUltimaAlteracao;

    private final static Logger LOGGER = Logger.getLogger(CanalServico.class.getName());

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Aplica as alterações encaminhadas no canal de comunicação indicada.
     *
     * @param id Identificador do canal a ser alterada.
     * @param canalManutencaoDTO Objeto contendo os atributos a serem alterados na aplicação do patch. Caso os atributos sejam nulos nenhuma alteração será realizada no mesmo.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void aplicaPatch(Integer id, CanalManutencaoDTO canalManutencaoDTO) {
        try {
            Canal canal = this.getById(id);

            this.validaRecursoLocalizado(canal, "CS.aP.001 - Canal de comunicação não localizado para o identificador informado");

            if (Objects.nonNull(canalManutencaoDTO.getSiglaCanal())) {
                canal.setSigla(canalManutencaoDTO.getSiglaCanal());
            }

            if (Objects.nonNull(canalManutencaoDTO.getDescricaoCanal())) {
                canal.setDescricao(canalManutencaoDTO.getDescricaoCanal());
            }

            if (Objects.nonNull(canalManutencaoDTO.getCanalCaixaEnum())) {
                canal.setCanalCaixa(canalManutencaoDTO.getCanalCaixaEnum());
            }

            if (Objects.nonNull(canalManutencaoDTO.getNomeClienteSSO())) {
                canal.setClienteSSO(canalManutencaoDTO.getNomeClienteSSO());
            }

            if (Objects.nonNull(canalManutencaoDTO.getIndicadorJanelaM0())) {
                canal.setJanelaExtracaoM0(canalManutencaoDTO.getIndicadorJanelaM0());
            }

            if (Objects.nonNull(canalManutencaoDTO.getIndicadorJanelaM30())) {
                canal.setJanelaExtracaoM30(canalManutencaoDTO.getIndicadorJanelaM30());
            }

            if (Objects.nonNull(canalManutencaoDTO.getIndicadorJanelaM60())) {
                canal.setJanelaExtracaoM60(canalManutencaoDTO.getIndicadorJanelaM60());
            }

            if (Objects.nonNull(canalManutencaoDTO.getIndicadorAvaliacaoAutenticidade())) {
                canal.setIndicadorAvaliacaoAutenticidade(canalManutencaoDTO.getIndicadorAvaliacaoAutenticidade());
            }

            if (Objects.nonNull(canalManutencaoDTO.getIndicadorAtualizacaoDocumental())) {
                canal.setIndicadorAtualizacaoDocumento(canalManutencaoDTO.getIndicadorAtualizacaoDocumental());
            }

            if (Objects.nonNull(canalManutencaoDTO.getIndicadorOutorgaCadastroReceita())) {
                canal.setIndicadorOutorgaReceita(canalManutencaoDTO.getIndicadorOutorgaCadastroReceita());
            }

            if (Objects.nonNull(canalManutencaoDTO.getIndicadorOutorgaCadastroCaixa())) {
                canal.setIndicadorOutorgaCadastroCaixa(canalManutencaoDTO.getIndicadorOutorgaCadastroCaixa());
            }

            if (Objects.nonNull(canalManutencaoDTO.getIndicadorOutorgaApimanager())) {
                canal.setIndicadorOutorgaApimanager(canalManutencaoDTO.getIndicadorOutorgaApimanager());
            }

            if (Objects.nonNull(canalManutencaoDTO.getIndicadorOutorgaSiric())) {
                canal.setIndicadorOutorgaSiric(canalManutencaoDTO.getIndicadorOutorgaSiric());
            }

            if (Objects.nonNull(canalManutencaoDTO.getUrlCallbackDocumento())) {
                canal.setUrlCallbackDocumento(canalManutencaoDTO.getUrlCallbackDocumento());
            }

            if (Objects.nonNull(canalManutencaoDTO.getUrlCallbackDossie())) {
                canal.setUrlCallbackDossie(canalManutencaoDTO.getUrlCallbackDossie());
            }

            if (Objects.nonNull(canalManutencaoDTO.getUnidadeCallbackDossie())) {
                canal.setUnidadeCallbackDossie(canalManutencaoDTO.getUnidadeCallbackDossie());
            }
            if(Objects.nonNull(canalManutencaoDTO.getToken())) {
                canal.setToken(canalManutencaoDTO.getToken());
            }
            canal.setDataHoraUltimaAlteracao(Calendar.getInstance());
            this.update(canal);
        } catch (PersistenceException pe) {
            Throwable problema = pe.getCause();
            while ((Objects.nonNull(problema.getCause())) && !(PSQLException.class.equals(problema.getClass()))) {
                problema = problema.getCause();
            }
            throw new SimtrEstadoImpeditivoException("CS.aP.002 - ".concat(problema.getLocalizedMessage()), pe);
        }
    }

    /**
     * Realiza a exclusão de um canal de comunicação
     *
     * @param id Identificador do canal a ser excluido
     * @throws SimtrRecursoDesconhecidoException Lançada caso o canal não seja localizado sob o identificador informado.
     * @throws SimtrEstadoImpeditivoException Lançada caso identificado vinculos com registros de outras entidades existentes com o canal que impedem a sua exclusão
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void deleteCanalByID(Integer id) {
        try {
            Canal canal = this.getById(id);
            if (Objects.nonNull(canal)) {
                this.delete(canal);
            } else {
                String mensagem = MessageFormat.format("CS.dCBI.001 - Canal não localizado sob identificador informado. ID = {0}", id);
                throw new SimtrRecursoDesconhecidoException(mensagem);
            }
        } catch (PersistenceException pe) {
            Throwable problema = pe.getCause();
            while ((Objects.nonNull(problema.getCause())) && !(PSQLException.class.equals(problema.getClass()))) {
                problema = problema.getCause();
            }
            throw new SimtrEstadoImpeditivoException("CS.dCBI.002 - ".concat(problema.getLocalizedMessage()), pe);
        }
    }

    /**
     * Retorna o objeto que representa o canal baseado em seu cliente do SSO
     *
     * @param clientID Nome do cliente SSO de identificação do Canal
     * @return Canal identificado ou nulo caso nenhum canal seja identificado pela sigla encaminhada.
     */
    public Canal getByClienteSSO(final String clientID) {
        this.carregarMapas();
        return mapaClienteSSO.get(clientID);
    }

    /**
     * Retorna o objeto que representa o canal baseado no cliente do SSO vinculado da thread em curso
     *
     * @return Canal identificado ou nulo caso nenhum canal seja identificado pela sigla encaminhada.
     */
    public Canal getByClienteSSO() {
        this.carregarMapas();
        String clientId = this.keycloakUtil.getClientId();
        return mapaClienteSSO.get(clientId);
    }

    /**
     * Realiza a validação do canal quanto a permissão de consumo do serviço de extração de dados junto a janela temporal indicada.
     *
     * @param canal Objeto que representa o canal a ser validado
     * @param janelaTemporalExtracaoEnum Janela temporal indicada para verificação se o canal possui permissão para utilização
     * @param mensagemErro Mensagem de erro a ser incluida na exceção lançada caso a janela indicada não seja validada na configuração do canal.
     *
     * @throws SimtrPermissaoException Lançada caso a janela indicada não tenha sinalização positiva nas configurações do canal.
     */
    public void validaJanelaExtracaoPermitida(Canal canal, JanelaTemporalExtracaoEnum janelaTemporalExtracaoEnum, String mensagemErro) {
        if ((JanelaTemporalExtracaoEnum.M0.equals(janelaTemporalExtracaoEnum)) && canal.getJanelaExtracaoM0()) {
            return;
        } else if ((JanelaTemporalExtracaoEnum.M30.equals(janelaTemporalExtracaoEnum)) && canal.getJanelaExtracaoM30()) {
            return;
        } else if ((JanelaTemporalExtracaoEnum.M60.equals(janelaTemporalExtracaoEnum)) && canal.getJanelaExtracaoM60()) {
            return;
        }
        throw new SimtrPermissaoException(mensagemErro);
    }

    /**
     * Valida se o canal encaminhado possui as caracteristicas de outorga indicadas.
     *
     * @param canal Objeto que representa o canal a ser verificado.
     * @param outorgaReceita Indicador se deve ser verificado a outorga para consumo aos serviços de consulta junto a Receita Federal
     * @param outorgaCadastroCaixa Indicador se deve ser verificado a outorga para consumo aos serviços do SICLI
     * @param outorgaDossieDigital Indicador se deve ser verificado a outorga para consumo aos serviços do Dossiê Digital
     * @param outorgaApimanager Indicador se deve ser verificado a outorga para consumo aos serviços do API Manager usando o SIMTR como ponte
     * @param outorgaSiric Indicador se deve ser verificado a outorga para consumo aos serviços do SIRIC expostos pelo SIMTR
     * @param mensagemErro Mensagem de erro a ser incluida na exceção lançada caso a janela indicada não seja validada na configuração do canal.
     * @throws SimtrRecursoDesconhecidoException Lançada caso o objeto encaminhado para validação seja nulo.
     */
    public void validaOutorgaCanal(Canal canal, boolean outorgaReceita, boolean outorgaCadastroCaixa, boolean outorgaDossieDigital, boolean outorgaApimanager, boolean outorgaSiric, String mensagemErro) {
        if (outorgaReceita && !canal.getIndicadorOutorgaReceita()) {
            throw new SimtrPermissaoException(mensagemErro);
        }

        if (outorgaCadastroCaixa && !canal.getIndicadorOutorgaCadastroCaixa()) {
            throw new SimtrPermissaoException(mensagemErro);
        }

        if (outorgaDossieDigital && !canal.getIndicadorOutorgaDossieDigital()) {
            throw new SimtrPermissaoException(mensagemErro);
        }

        if (outorgaApimanager && !canal.getIndicadorOutorgaApimanager()) {
            throw new SimtrPermissaoException(mensagemErro);
        }

        if (outorgaSiric && !canal.getIndicadorOutorgaSiric()) {
            throw new SimtrPermissaoException(mensagemErro);
        }
    }

    /**
     * Verifica se o o canal encaminhado trata-se de uma instancia valida. Caso seja uma referência nula lança uma exceção do tipo SimtrPermissaoException com a mensagem de erro encaminhada inclusa
     * como causa do problema. O método foi sobrescrito para forçar um lançmento da exceção SimtrPermissaoException uma vez que o método comum lança uma exceção do tipo
     * SimtrRecursoDesconhecidoException
     *
     * @param canal Instancia de canal a ser verificada
     * @param mensagemErro Mensagem de erro a ser incluida como causa da exceção
     * @throws SimtrPermissaoException Lançada caso o objeto seja nulo indicando que o canal não foi localizado.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTO,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNSEG,
        ConstantesUtil.PERFIL_MTRSDNEXT,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRPAEOPE,
        ConstantesUtil.PERFIL_MTRPAEMTZ
    })
    @Override
    public void validaRecursoLocalizado(Canal canal, String mensagemErro) {
        if (Objects.isNull(canal)) {
            throw new SimtrPermissaoException(mensagemErro);
        }
    }

    /**
     * Sinaliza o canal submissor do documento indicando que houve uma atualização no documento
     * 
     * @param documento Documento que sofreu a atualização e que esta vinculado ao canal que deve ser notificado.
     */
    public void notificaCanalSubmissor(Documento documento) {
        Canal canal = documento.getCanalCaptura();

        // Verifica se o canal possui URL de callback definida
        if (canal.getUrlCallbackDocumento() != null && !canal.getUrlCallbackDocumento().trim().isEmpty()) {

            String url = canal.getUrlCallbackDocumento();
            boolean usoPathParam = (url.contains("{id}"));

            // Substitui o termo coringa pelo identificador do documento no caso de uso sob Path Param
            if (usoPathParam) {
                url = canal.getUrlCallbackDocumento().replaceAll("\\{id\\}", documento.getId().toString());
            }

            RetornoAvaliacaoExtracaoDTO retornoAvaliacaoExtracaoDTO = new RetornoAvaliacaoExtracaoDTO(documento);
            String body;
            try {
                body = UtilJson.converterParaJson(retornoAvaliacaoExtracaoDTO);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "CS.nCS.001 - Falha ao converter objeto de retorno da avaliação de extração para envio de callback", ex);
                body = null;
            }

            Response response;
            
            try{
                if(canal.getToken() == null){
                    String accessToken =  this.keycloakUtil.getTokenServico();
                    response = UtilRest.consumirServicoOAuth2JSON(url, EnumMetodoHTTP.POST, new HashMap<>(), new HashMap<>(), body, accessToken);
                } else {
                    String accessToken = canal.getToken();
                    Map<String, String> headers = new HashMap<>();
                    headers.put(ConstantesUtil.HEADER_AUTHORIZATION, accessToken);
                    response = UtilRest.consumirServicoNoAuthJSON(url, EnumMetodoHTTP.POST, headers, new HashMap<>(), body);
                }

                if (!response.getStatusInfo().getFamily().equals(Status.Family.SUCCESSFUL)) {
                    String mensagem = MessageFormat.format("CS.nCS.001 - Falha ao notificar o canal submissor do documento. ID: {0} | Canal: {1} | URL: {2} | HTTP Status: {3} | Retorno: {4}", documento.getId(), canal.getSigla(), url, response.getStatus(), response.getEntity());
                    LOGGER.warning(mensagem);
                }
            } catch(Exception e){
                String mensagem = MessageFormat.format("CS.nCS.002 - Falha ao notificar o canal submissor do documento. ID = {0} | Canal: {1} | URL: {2} |  Excecao: {3}", documento.getId(), canal.getSigla(), url, e.getLocalizedMessage());
                LOGGER.severe(mensagem);
                throw e;
            }
        }
    }

    // *********** Métodos Privados ***********
    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    private void carregarMapas() {
        String jpqlUltimaAlteracao = "SELECT MAX(c.dataHoraUltimaAlteracao) FROM Canal c";
        TypedQuery<Calendar> queryUltimaAlteracao = this.entityManager.createQuery(jpqlUltimaAlteracao, Calendar.class);
        Calendar ultimaAlteracao = queryUltimaAlteracao.getSingleResult();

        if (Objects.isNull(this.dataHoraUltimaAlteracao) || dataHoraUltimaAlteracao.before(ultimaAlteracao)) {

            List<Canal> canais = this.list();

            mapaClienteSSO.clear();

            canais.forEach(canal -> {
                mapaClienteSSO.put(canal.getClienteSSO(), canal);
            });

            this.dataHoraUltimaAlteracao = ultimaAlteracao;
        }
    }
}
