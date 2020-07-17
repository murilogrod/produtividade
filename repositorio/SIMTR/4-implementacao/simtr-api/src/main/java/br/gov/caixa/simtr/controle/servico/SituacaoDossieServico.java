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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDossie;
import br.gov.caixa.simtr.modelo.entidade.TipoSituacaoDossie;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class SituacaoDossieServico extends AbstractService<SituacaoDossie, Long> {

    @EJB
    private KeycloakService keycloakService;
    
    @EJB
    private CanalServico canalServico;

    @Inject
    private EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(SituacaoDossieServico.class.getName());

    private final Map<SituacaoDossieEnum, TipoSituacaoDossie> mapaTiposSituacao = new HashMap<>();
    private Calendar dataHoraUltimaAlteracao = null;

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Captura o tipo de situação pela representação do Enumerador. Caso não seja localizado realiza uma nova carga no mapa e tenta localizar o registro novamente,
     * se a ausencia ainda persistir retorna null.
     *
     * @param situacaoDossieEnum Registro do enumerador que representa o tipo de situação a ser localizado
     * @return Tipo de situação localizado ou null caso o mesmo n~~ao seja localizado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public TipoSituacaoDossie getByTipoSituacaoDossieEnum(final SituacaoDossieEnum situacaoDossieEnum) {
        this.carregarMapas();
        return mapaTiposSituacao.get(situacaoDossieEnum);
    }

    /**
     * Registra uma nova situação do tipo indicado vinculada ao dossiê de produto informado. Caso seja encaminhado um valor na observação, a mensagem será definido
     * no registro da situação.
     *
     * @param dossieProduto Dossie de produto a ter a situação associada.
     * @param tipoSituacaoDossie Tipo de situação a ser utilizada na vinculação
     * @param observacaoAtual Valor a ser definido na observação da situação da situação atual
     * @param observacaoNova Valor a ser definido como observação do novo registro de situação a ser criado.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTO,
        ConstantesUtil.PERFIL_MTRSDNTTG 
    })
    public void registraNovaSituacaoDossie(DossieProduto dossieProduto, TipoSituacaoDossie tipoSituacaoDossie, String observacaoAtual, String observacaoNova) {
        
        Calendar dataHoraRegistro = Calendar.getInstance();

        // Captura o canal de comunicação baseado no client ID para usar a sigla caso a matricula seja superior a 20 caracteres
        Canal canal = this.canalServico.getByClienteSSO();
        
        // Monta consulta para atualizar o registro da ultima situação do dossiê baseado na data/hora de inclusão da mesma
        StringBuilder jpqlSituacao = new StringBuilder();
        jpqlSituacao.append(" UPDATE SituacaoDossie ");
        jpqlSituacao.append(" SET dataHoraSaida = :dataHoraSaida ");
        if (observacaoAtual != null) {
            jpqlSituacao.append(" , observacao = :observacao ");
        }
        jpqlSituacao.append(" WHERE id = ( ");
        jpqlSituacao.append("   SELECT sd.id FROM SituacaoDossie sd WHERE sd.dossieProduto = :dossieProduto AND sd.dataHoraInclusao = ( ");
        jpqlSituacao.append("       SELECT MAX(sd2.dataHoraInclusao) FROM SituacaoDossie sd2 WHERE sd2.dossieProduto = :dossieProduto ");
        jpqlSituacao.append("   ) ");
        jpqlSituacao.append(" ) ");

        Query query = this.entityManager.createQuery(jpqlSituacao.toString());
        query.setParameter(ConstantesUtil.KEY_DOSSIE_PRODUTO, dossieProduto);
        query.setParameter("dataHoraSaida", dataHoraRegistro);
        if (observacaoAtual != null) {
            query.setParameter("observacao", observacaoAtual);
        }

        // Executa a atualização do registro da situação
        query.executeUpdate();

        // Cria o registro da nova situação do dossiê de produto
        SituacaoDossie situacaoDossie = new SituacaoDossie();
        situacaoDossie.setDataHoraInclusao(Calendar.getInstance());
        String responsavel = this.keycloakService.getMatricula().length() < 20 ? this.keycloakService.getMatricula() : canal.getSigla();
        situacaoDossie.setResponsavel(responsavel);
        situacaoDossie.setObservacao(observacaoNova);
        situacaoDossie.setTipoSituacaoDossie(tipoSituacaoDossie);
        situacaoDossie.setUnidade(this.keycloakService.getLotacaoAdministrativa());
        situacaoDossie.setDossieProduto(dossieProduto);
        dossieProduto.addSituacoesDossie(situacaoDossie);

        // Salva o registro da nova situação criada.
        this.save(situacaoDossie);

        // Caso a situação seja do tipo final, armazena a data/hora de finalização do dossiê
        if (tipoSituacaoDossie.getTipoFinal()) {
            String jpqlFinalizacao = " UPDATE DossieProduto SET dataHoraFalhaBPM = NULL, dataHoraFinalizacao = :dataHoraRegistro WHERE id = :id ";

            Query queryFinalizacao = this.entityManager.createQuery(jpqlFinalizacao);
            queryFinalizacao.setParameter("id", dossieProduto.getId());
            queryFinalizacao.setParameter("dataHoraRegistro", dataHoraRegistro);

            queryFinalizacao.executeUpdate();
        }

    }

    /**
     * Registra uma nova situação do tipo indicado vinculada ao dossiê de produto informado. Caso seja encaminhado um valor na observação, a mensagem será definido
     * no registro da situação.
     *
     * @param dossieProduto Dossie de produto a ter a situação associada.
     * @param situacaoDossieEnum Enumerador que representa o tipo de situação do ddossiê a ser utilizada na vinculação do novo registro
     * @param observacaoAtual Valor a ser definido na observação da situação da situação atual
     * @param observacaoNova Valor a ser definido como observação do novo registro de situação a ser criado.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    public void registraNovaSituacaoDossie(DossieProduto dossieProduto, SituacaoDossieEnum situacaoDossieEnum, String observacaoAtual, String observacaoNova) {
        TipoSituacaoDossie tipoSituacaoDossie = this.getByTipoSituacaoDossieEnum(situacaoDossieEnum);
        this.registraNovaSituacaoDossie(dossieProduto, tipoSituacaoDossie, observacaoAtual, observacaoNova);
    }

    /**
     * Remove a ultima situação vinculada ao dossiê de produto informado. Inclui uma observação encaminhada na situação remanescente.
     *
     * @param dossieProduto Dossie de produto a ter a ultima situação removida.
     * @param observacao Valor a ser definido na observação da situação retomada caso não exista.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTO,
        ConstantesUtil.PERFIL_MTRSDNTTG
    })
    public void removeUltimaSituacaoDossie(DossieProduto dossieProduto, String observacao) {
        // Monta consulta para atualizar o registro da ultima situação do dossiê baseado na data/hora de inclusão da mesma
        StringBuilder jpql = new StringBuilder();
        jpql.append(" DELETE FROM SituacaoDossie ");
        jpql.append(" WHERE id = (");
        jpql.append("    SELECT sd.id FROM SituacaoDossie sd WHERE sd.dossieProduto = :dossieProduto AND sd.dataHoraInclusao = ( ");
        jpql.append("        SELECT MAX(sd2.dataHoraInclusao) FROM SituacaoDossie sd2 WHERE sd2.dossieProduto = :dossieProduto ");
        jpql.append("  ) ");
        jpql.append(" )  ");

        Query query = this.entityManager.createQuery(jpql.toString());
        query.setParameter(ConstantesUtil.KEY_DOSSIE_PRODUTO, dossieProduto);

        // Executa a atualização do registro da situação
        query.executeUpdate();

        // Cria o registro da nova situação do dossiê de produto
        jpql = new StringBuilder();
        jpql.append(" UPDATE SituacaoDossie ");
        jpql.append(" SET dataHoraSaida = null ");
        jpql.append(" , observacao = :observacao ");
        jpql.append(" WHERE id = ( ");
        jpql.append("     SELECT sd.id FROM SituacaoDossie sd WHERE sd.dossieProduto = :dossieProduto AND sd.dataHoraInclusao = ( ");
        jpql.append("         SELECT MAX(sd2.dataHoraInclusao) FROM SituacaoDossie sd2 WHERE sd2.dossieProduto = :dossieProduto ");
        jpql.append(" )   ");
        jpql.append(")");

        query = this.entityManager.createQuery(jpql.toString());
        query.setParameter(ConstantesUtil.KEY_DOSSIE_PRODUTO, dossieProduto);
        query.setParameter("observacao", observacao);

        // Executa a atualização do registro da situação
        query.executeUpdate();

    }

    // *********** Métodos privados auxiliares ***************
    /**
     * Realiza a carga dos Tipos de Situação configurados no banco e mantem preenchido em um map ára evitar consultas excessivas no banco de dados com informações
     * pouco alteradas
     */
    @PostConstruct
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    private void carregarMapas() {
        String jpqlUltimaAlteracao = " SELECT MAX(tsd.dataHoraUltimaAlteracao) FROM TipoSituacaoDossie tsd ";
        TypedQuery<Calendar> queryUltimaAlteracao = this.entityManager.createQuery(jpqlUltimaAlteracao, Calendar.class);
        Calendar ultimaAlteracao = queryUltimaAlteracao.getSingleResult();

        if (Objects.isNull(this.dataHoraUltimaAlteracao) || dataHoraUltimaAlteracao.before(ultimaAlteracao)) {

            StringBuilder jpql = new StringBuilder();
            jpql.append(" SELECT tsd FROM TipoSituacaoDossie tsd ");

            List<TipoSituacaoDossie> tiposSituacao = this.entityManager.createQuery(jpql.toString(), TipoSituacaoDossie.class).getResultList();

            mapaTiposSituacao.clear();

            tiposSituacao.forEach(tipoSituacao -> {
                try {
                    SituacaoDossieEnum situacaoDossieEnum = SituacaoDossieEnum.getByDescricao(tipoSituacao.getNome());
                    mapaTiposSituacao.put(situacaoDossieEnum, tipoSituacao);
                } catch (IllegalArgumentException iae) {
                    String mensagem = MessageFormat.format("Falha ao localizar enumeração para o tipo de situação dossiê no carregamento do mapa. Tipo Situação Dossiê = {0}", tipoSituacao.getNome());
                    LOGGER.log(Level.SEVERE, "SDS.cM.001 - {0}", mensagem);
                    throw new SimtrRecursoDesconhecidoException(mensagem, iae);
                }
            });

            this.dataHoraUltimaAlteracao = ultimaAlteracao;
        }
    }
}
