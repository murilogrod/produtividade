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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;

import com.google.common.base.Strings;

import br.gov.caixa.simtr.controle.excecao.SimtrCadastroException;
import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.helper.CadastroHelper;
import br.gov.caixa.simtr.controle.vo.ChecklistVO;
import br.gov.caixa.simtr.controle.vo.excecao.PendenciasVO;
import br.gov.caixa.simtr.modelo.entidade.Apontamento;
import br.gov.caixa.simtr.modelo.entidade.Checklist;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroChecklist;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist.AlteracaoApontamentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist.CadastroChecklistDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRTEC,
    ConstantesUtil.PERFIL_MTRAUD,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE,
    ConstantesUtil.PERFIL_MTRSDNTTG,
    ConstantesUtil.PERFIL_MTRSDNTTO,
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class ChecklistServico extends AbstractService<Checklist, Integer> {
    
    @EJB
    private ChecklistServico self;
    
    @EJB
    private ApontamentoServico apontamentoServico;
    
    @EJB
    private VinculacaoChecklistServico vinculacaoChecklistServico;
    
    @Inject
    private EntityManager entityManager;
    
    @EJB
    private CadastroHelper cadastroHelper;

    private static final Logger LOGGER = Logger.getLogger(ChecklistServico.class.getName());

    private Calendar dataHoraUltimaAlteracao;
    private final Map<Integer, Checklist> mapaChecklists = new HashMap<>();
    
    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }
    
    @PermitAll
    public Calendar getDataHoraUltimaAlteracao() {
        return this.dataHoraUltimaAlteracao;
    }
    
    @PostConstruct
    @PermitAll
    private void carregarMapas() {
        String jpqlUltimaAlteracao = "SELECT MAX(c.dataHoraUltimaAlteracao) FROM Checklist c";
        TypedQuery<Calendar> queryUltimaAlteracao = this.entityManager.createQuery(jpqlUltimaAlteracao, Calendar.class);
        Calendar ultimaAlteracao = queryUltimaAlteracao.getSingleResult();

        if (Objects.isNull(this.dataHoraUltimaAlteracao) || dataHoraUltimaAlteracao.before(ultimaAlteracao)) {
            this.mapaChecklists.clear();
            StringBuilder jpql = new StringBuilder();
            jpql.append("SELECT DISTINCT c FROM Checklist c "); 
            jpql.append("LEFT JOIN FETCH c.apontamentos a "); 
            jpql.append("LEFT JOIN FETCH c.vinculacoesChecklists vc "); 
            jpql.append("LEFT JOIN FETCH vc.processoDossie pd "); 
            jpql.append("LEFT JOIN FETCH vc.processoFase pf "); 
            jpql.append("LEFT JOIN FETCH vc.tipoDocumento td "); 
            jpql.append("LEFT JOIN FETCH vc.funcaoDocumental fd");
            
            TypedQuery<Checklist> query = this.entityManager.createQuery(jpql.toString(), Checklist.class);
            List<Checklist> checklists = null;
            try {
                checklists = query.getResultList();
            } catch (NoResultException nre) {
                LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            }
            
            for(Checklist checklist : checklists) {
                this.mapaChecklists.put(checklist.getId(), checklist);
            }
            
            this.dataHoraUltimaAlteracao = ultimaAlteracao;
        }
    }

    private String getQuery(boolean vinculacoesAssociados, boolean vinculacoesProcessoDocumento) {
        boolean carregaBase = !vinculacoesAssociados && !vinculacoesProcessoDocumento;

        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT c FROM Checklist c ");

        if (carregaBase) {
            jpql.append(" LEFT JOIN FETCH c.apontamentos a ");
        }

        if (vinculacoesProcessoDocumento) {
            jpql.append(" LEFT JOIN FETCH c.vinculacoesChecklists vc ");
            jpql.append(" LEFT JOIN FETCH vc.processoDossie pd ");
            jpql.append(" LEFT JOIN FETCH vc.processoFase pf ");
            jpql.append(" LEFT JOIN FETCH vc.tipoDocumento td ");
            jpql.append(" LEFT JOIN FETCH vc.funcaoDocumental fd ");
            jpql.append(" LEFT JOIN FETCH fd.tiposDocumento tdfd ");
        }

        if (vinculacoesAssociados) {
            // Vinculos de Checklists Associados
            jpql.append(" LEFT JOIN FETCH c.checklistsAssociados ca ");
            jpql.append(" LEFT JOIN FETCH ca.verificacoes v ");
            jpql.append(" LEFT JOIN FETCH v.pareceres p ");
        }
        return jpql.toString();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Checklist getById(Integer id, boolean vinculacoesAssociados, boolean vinculacoesProcessoDocumento) {

        if (Objects.isNull(id)) {
            throw new SimtrRecursoDesconhecidoException("CS.gBI.001 - Não é possivel localizar Checklist com ID nulo");
        }

        Checklist checklistRetorno;
        Checklist checklistTemp;

        String jpqlBase = this.getQuery(Boolean.FALSE, Boolean.FALSE);
        jpqlBase = jpqlBase.concat(" WHERE c.id = :id ");
        TypedQuery<Checklist> queryChecklist = this.entityManager.createQuery(jpqlBase, Checklist.class);
        queryChecklist.setParameter("id", id);
        try {
            checklistRetorno = queryChecklist.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }

        if (vinculacoesAssociados) {
            String jpql = this.getQuery(Boolean.TRUE, Boolean.FALSE);
            jpql = jpql.concat(" WHERE dp.id = :id ");
            TypedQuery<Checklist> query = this.entityManager.createQuery(jpql, Checklist.class);
            query.setParameter("id", id);

            checklistTemp = query.getSingleResult();
            checklistRetorno.setChecklistsAssociados(checklistTemp.getChecklistsAssociados());
        }

        if (vinculacoesProcessoDocumento) {
            String jpql = this.getQuery(Boolean.FALSE, Boolean.TRUE);
            jpql = jpql.concat(" WHERE dp.id = :id ");
            TypedQuery<Checklist> query = this.entityManager.createQuery(jpql, Checklist.class);
            query.setParameter("id", id);

            checklistTemp = query.getSingleResult();
            checklistRetorno.setChecklistsAssociados(checklistTemp.getChecklistsAssociados());
        }

        return checklistRetorno;
    }
    
    /**
     * Cadastra um checklist novo.
     * @param checklist
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void criaNovoChecklist(Checklist checklist) {
        this.validaInclusaoChecklist(checklist);
        this.save(checklist);
    }
    
    /**
     * Valida os campos obrigatórios da entidade Checklist antes de salvá-la.
     * @param checklist
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    private void validaInclusaoChecklist(Checklist checklist) {
        Map<String, List<String>> mapaPendencias = new HashMap<>();

        if (Strings.isNullOrEmpty(checklist.getNome())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroChecklist.NOME, "Nome do checklist é obrigatório.");
        }
        
        if (Objects.isNull(checklist.getUnidade())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroChecklist.UNIDADE_RESPONSAVEL, "Unidade responsável é obrigatória.");
        }
        
        if (Objects.isNull(checklist.getIndicacaoVerificacaoPrevia())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroChecklist.VERIFICACAO_PREVIA, "Indicação de verificação prévia é obrigatório.");
        }
        
        List<PendenciasVO> listaPendencias = mapaPendencias.entrySet().stream().map(registro -> new PendenciasVO(registro.getKey(), registro.getValue()))
                .collect(Collectors.toList());
        
        if (!listaPendencias.isEmpty()) {
            throw new SimtrCadastroException("CLS.vIC.001 - Problemas identificados na execução da inclusão de um checklist.", listaPendencias);
        }
    }
    
    /**
     * Altera um checklist existente.
     * @param checklist
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void alteraChecklist(Integer idChecklist, CadastroChecklistDTO checklistDTO) {
        this.carregarMapas();
        this.update(this.validaAlteracaoChecklist(checklistDTO, idChecklist));
    }
    
    /**
     * Valida os campos obrigatórios da entidade Checklist antes de atualizá-la.
     * @param checklistDTO
     * @param checklist
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    private Checklist validaAlteracaoChecklist(CadastroChecklistDTO checklistDTO, Integer idChecklist) {
        ChecklistVO checklistVO =  this.verificaIntegridadeChecklist(idChecklist);
        
        if (!Strings.isNullOrEmpty(checklistDTO.getNomeChecklist())) {
            checklistVO.getChecklist().setNome(checklistDTO.getNomeChecklist());
        }
        
        if (Objects.nonNull(checklistDTO.getUnidadeResponsavel())) {
            checklistVO.getChecklist().setUnidade(checklistDTO.getUnidadeResponsavel());
        }
        
        if (Objects.nonNull(checklistDTO.getVerificacaoPrevia())) {
            checklistVO.getChecklist().setIndicacaoVerificacaoPrevia(checklistDTO.getVerificacaoPrevia());
        }
        
        if ("".equals(checklistDTO.getOrientacaoOperador())) {
            checklistVO.getChecklist().setOrientacaoOperador(null);
        }else if(Objects.nonNull(checklistDTO.getOrientacaoOperador())) {
            checklistVO.getChecklist().setOrientacaoOperador(checklistDTO.getOrientacaoOperador());
        }
        
        checklistVO.getChecklist().setDataHoraUltimaAlteracao(Calendar.getInstance());
        
        return checklistVO.getChecklist();
    }
    
    /**
     * <p>Carrega lista de checklists cadastrados junatamente com suas vinculações cadastradas.</p>
     * @param idProcesso Identificador do processo.
     * @return Lista de checklists cadastrados.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
    })
    public List<ChecklistVO> carregaChecklists(){
        this.carregarMapas();
        return this.buscaContagemAssociacoesChecklist();
    }
    
    /**
     * <p>Carrega checklist cadastrado pelo seu identificador.</p>
     * @param idChecklist Identificador do checklist a ser carregado.
     * @return Checklist cadastrado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
    })
    public ChecklistVO carregaChecklistById(Integer idChecklist) {
        this.carregarMapas();
        return this.buscaContagemAssociacoesChecklist(idChecklist);
    }
    
    /**
     * <p>Retorna uma contagem de associacões de uma lista de checklists.</>
     * @return List<Checklist> Lista de checklists associados
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
    })
    private List<ChecklistVO> buscaContagemAssociacoesChecklist(){
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT c.id, COUNT(ca) FROM Checklist c "); 
        jpql.append("LEFT JOIN c.checklistsAssociados ca ");
        jpql.append("GROUP BY c.id ORDER BY c.id");
        Query query = this.entityManager.createQuery(jpql.toString());
        List<Object[]> listaContagemAssociados = null;
        try {
            listaContagemAssociados = query.getResultList();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
        List<ChecklistVO> listaVOs = new ArrayList<>();
        for(Object[] arrayContagem : listaContagemAssociados) {
            Integer idChecklist = (Integer) arrayContagem[0];
            Long quantidade = (Long) arrayContagem[1];
            Checklist checklistTransacioanal = this.mapaChecklists.get(idChecklist);
            ChecklistVO checklistVO = new ChecklistVO(checklistTransacioanal, quantidade);
            listaVOs.add(checklistVO);
        }
        return listaVOs;
    }
    
    /**
     * <p>Retorna uma contagem de associacões de um checklist .</>
     * @return List<Checklist> Lista de checklists associados
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
    })
    public ChecklistVO buscaContagemAssociacoesChecklist(Integer idChecklist){
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT c.id, COUNT(ca) FROM Checklist c "); 
        jpql.append("LEFT JOIN c.checklistsAssociados ca ");
        jpql.append("WHERE c.id = :idChecklist ");
        jpql.append("GROUP BY c.id");
        
        Query typedQuery = this.entityManager.createQuery(jpql.toString());
        typedQuery.setParameter("idChecklist", idChecklist);
        
        Object[] contagemAssociacoesChecklist = null;
        try {
            contagemAssociacoesChecklist = (Object[]) typedQuery.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
        Long quantidade = (Long) contagemAssociacoesChecklist[1];
        Checklist checklistTransacioanal = this.mapaChecklists.get(idChecklist);
        return new ChecklistVO(checklistTransacioanal, quantidade);
    }
    
    /**
     * <p>Exclui cheklist com seus apontamentos e vinculações.</p>
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
    })
    public void excluiChecklist(Integer idChecklist) {
        this.carregarMapas();
        ChecklistVO checklistVO = this.buscaContagemAssociacoesChecklist(idChecklist);
        if(checklistVO.getQuantidadeAssociacoes() > 0) {
           this.vinculacaoChecklistServico.excluiVinculacoesChecklistLogicamente(checklistVO.getChecklist().getVinculacoesChecklists());
           checklistVO.getChecklist().setDataHoraInativacao(Calendar.getInstance());
           checklistVO.getChecklist().setDataHoraUltimaAlteracao(Calendar.getInstance());
           this.update(checklistVO.getChecklist());
        }else {
           this.vinculacaoChecklistServico.excluiVinculacoesChecklistFisicamente(checklistVO.getChecklist().getVinculacoesChecklists());
           this.apontamentoServico.excluiApontamentos(checklistVO.getChecklist().getApontamentos());
           this.delete(this.getEntityManager().merge(checklistVO.getChecklist()));
           this.mapaChecklists.remove(idChecklist);
        }
    }
    
    /**
     * Cadastra um apontamento relacionado a um checklist.
     * @param checklist
     * @param apontamento
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void cadastraApontamentoChecklistAtual(Integer idChecklist, Apontamento apontamento) {
        this.carregarMapas();
        ChecklistVO checklistVO = this.verificaIntegridadeChecklist(idChecklist);
        apontamento.setChecklist(checklistVO.getChecklist());
        this.apontamentoServico.cadastraApontamento(apontamento);
    }
    
    /**
     * Altera um apontamento relacionado a um checklist.
     * @param checklist
     * @param idApontamento
     * @param apontamentoDTO
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void alteraApontamentoChecklistAtual(Integer idChecklist, Long idApontamento, AlteracaoApontamentoDTO apontamentoDTO) {
        this.carregarMapas();
        ChecklistVO checklistVO = this.verificaIntegridadeChecklist(idChecklist);
        this.apontamentoServico.alteraApontamento(checklistVO.getChecklist().getApontamentos(), idApontamento, apontamentoDTO);
    }
    
    /**
     * Deleta um apontamento relacionado a um checklist.
     * @param checklist
     * @param idApontamento
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void deletaApontamentoChecklistAtual(Integer idChecklist, Long idApontamento) {
        this.carregarMapas();
        ChecklistVO checklistVO = this.verificaIntegridadeChecklist(idChecklist);
        this.apontamentoServico.excluiApontamento(checklistVO.getChecklist().getApontamentos(), idApontamento);
    }
    
    /**
     * Verifica se o checklist existe e está apto para ser manipulado.
     * @param idChecklist
     */
    private ChecklistVO verificaIntegridadeChecklist(Integer idChecklist) {
        ChecklistVO checklistVO = this.buscaContagemAssociacoesChecklist(idChecklist);
        if(Objects.isNull(checklistVO)) {
            String mensagem = MessageFormat.format("CLS.vIC.001 - Não é possivel localizar Checklist com ID {0}", String.valueOf(idChecklist));
            throw new SimtrRequisicaoException(mensagem);
        }
        if(checklistVO.getQuantidadeAssociacoes() > 0) {
            String mensagem = MessageFormat.format("CLS.vIC.002 - O Checklist {0} - {1} já está em uso no tratamento de documentos. Quantidade de associações já feitas: {2}", checklistVO.getChecklist().getId(), checklistVO.getChecklist().getNome(), checklistVO.getQuantidadeAssociacoes());
            throw new SimtrEstadoImpeditivoException(mensagem);
        }
        return checklistVO;
    }
}
