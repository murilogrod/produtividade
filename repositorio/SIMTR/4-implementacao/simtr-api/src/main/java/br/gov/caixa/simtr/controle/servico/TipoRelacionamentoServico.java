package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.postgresql.util.PSQLException;

import com.google.common.base.Strings;

import br.gov.caixa.simtr.controle.excecao.SimtrCadastroException;
import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.helper.CadastroHelper;
import br.gov.caixa.simtr.controle.vo.excecao.PendenciasVO;
import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroTipoRelacionamento;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.tiporelacionamento.TipoRelacionamentoAlteracaoDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class TipoRelacionamentoServico extends AbstractService<TipoRelacionamento, Integer> {

    @Inject
    private EntityManager entityManager;
    
    @EJB
    private CadastroHelper cadastroHelper;

    private static final Logger LOGGER = Logger.getLogger(TipoRelacionamentoServico.class.getName());

    private Calendar dataHoraUltimaAlteracao;
    private final Map<Integer, TipoRelacionamento> mapaById = new HashMap<>();
    private final Map<String, TipoRelacionamento> mapaByNome = new HashMap<>();

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    public Calendar getDataHoraUltimaAlteracao() {
        return dataHoraUltimaAlteracao;
    }

    @PostConstruct
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void carregarMapas() {
        // Captura a data de ultima atualização realizada com qualquer registro da tabela de tipo de documento
        String jpqlUltimaAlteracao = " SELECT MAX(tr.dataHoraUltimaAlteracao) FROM TipoRelacionamento tr ";
        TypedQuery<Calendar> queryUltimaAlteracao = this.entityManager.createQuery(jpqlUltimaAlteracao, Calendar.class);
        Calendar ultimaAlteracao = queryUltimaAlteracao.getSingleResult();

        // Verifica se a data armazenada no EJB como base de ultima atualização é nula ou menor do que a retornada na consulta
        if (Objects.isNull(this.dataHoraUltimaAlteracao) || dataHoraUltimaAlteracao.before(ultimaAlteracao)) {

            StringBuilder jpqlTipoRelacionamento = new StringBuilder();
            jpqlTipoRelacionamento.append(" SELECT DISTINCT tr FROM TipoRelacionamento tr ");

            List<TipoRelacionamento> tiposRelacionamento = this.entityManager.createQuery(jpqlTipoRelacionamento.toString(), TipoRelacionamento.class).getResultList();

            // Limpa os mapas de armazenamento dos tipos de relacionamento
            this.mapaById.clear();
            this.mapaByNome.clear();

            // Recarrega os mapas pelas diferentes visões possiveis
            tiposRelacionamento.forEach(tipoRelacionamento -> {
                this.mapaById.put(tipoRelacionamento.getId(), tipoRelacionamento);
                this.mapaByNome.put(tipoRelacionamento.getNome(), tipoRelacionamento);
            });

            // Atualiza a data armazenada no EJB como base para comparação da ultima alteração
            this.dataHoraUltimaAlteracao = ultimaAlteracao;
        }
    }

    @PermitAll
    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public TipoRelacionamento getById(final Integer id) {
        this.carregarMapas();
        return mapaById.get(id);
    }

    @PermitAll
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public TipoRelacionamento getByNome(final String nome) {
        return this.getByNome(nome, Boolean.FALSE);
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public TipoRelacionamento getByNome(final String nome, final boolean validaExistencia) {
        this.carregarMapas();
        TipoRelacionamento tipoRelacionamento = mapaByNome.get(nome);
        if (tipoRelacionamento == null) {
            throw new SimtrRequisicaoException(MessageFormat.format("Tipo de Documento {0} não foi encontrado na base de dados", nome));
        }
        return tipoRelacionamento;
    }
    
    /**
     * Retorna a lista de objetos com todos os tipos relacionamento até momento da consulta. 
     *
     * @return Lista de Tipos de Relacionamento localizados
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public List<TipoRelacionamento> listTodos() {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT tr FROM TipoRelacionamento tr ");

        TypedQuery<TipoRelacionamento> query = this.entityManager.createQuery(jpql.toString(), TipoRelacionamento.class);

        return query.getResultList();
    }
    
    /**
     * Verifica se existe algum Tipo de Relacionamento com o nome. 
     *
     * @return true|false
     */
    public boolean existeTipoRelacionamentoComNome(TipoRelacionamento tipoRelacionamento) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT COUNT(tr) FROM TipoRelacionamento tr WHERE tr.id != :id AND UPPER(tr.nome) LIKE :nome ");

        TypedQuery<Long> query = this.entityManager.createQuery(jpql.toString(), Long.class);
        query.setParameter("id", tipoRelacionamento.getId());
        query.setParameter("nome", tipoRelacionamento.getNome().trim().toUpperCase());

        return query.getSingleResult().intValue() > 0;
    }
    
    /**
     * @return Tipos de Relacionamento localizados pelo identificador informado.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public TipoRelacionamento findById(Integer id) {
        
        TipoRelacionamento tipoRelacionamento = this.getById(id);
        
        if(tipoRelacionamento == null) {
            String mensagem = MessageFormat.format("Tipo Relacionamento não localizado para o identificador informado. ID = {0}", id);
            throw new SimtrRequisicaoException(mensagem);
        }
       
        return tipoRelacionamento;
    }
    
    /**
     * Cadastra um Tipo de Relacionamento novo.
     * @param tipoRelacionamento
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void criaNovoTipoRelacionamento(TipoRelacionamento tipoRelacionamento) {
        tipoRelacionamento.setDataHoraUltimaAlteracao(Calendar.getInstance());
        this.validaInclusaoTipoRelacionamento(tipoRelacionamento);
        this.save(tipoRelacionamento);
    }
    
    /**
     * Valida os campos obrigatórios da entidade Tipo de Relacionamento antes de salvá-la.
     * @param tipoRelacionamento
     */
    private void validaInclusaoTipoRelacionamento(TipoRelacionamento tipoRelacionamento) {
        Map<String, List<String>> mapaPendencias = new HashMap<>();

        if (Strings.isNullOrEmpty(tipoRelacionamento.getNome())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoRelacionamento.NOME, "Nome do tipo de relacionamento não enviado, ou vazio.");
        }else {
            
            int tamanhoNome = tipoRelacionamento.getNome().trim().length();
            boolean existeComOutroNome = this.existeTipoRelacionamentoComNome(tipoRelacionamento);
            
            if(tamanhoNome > 50 || existeComOutroNome) {
                this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoRelacionamento.NOME, "Nome do tipo de relacionamento não pode ser vazio, deve ter pelo menos 6 caracteres e não pode ser igual a outro pré-existente.");
            }
        }
        
        if (Objects.isNull(tipoRelacionamento.getTipoPessoaEnum())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoRelacionamento.TIPO_PESSOA, "Indicação do tipo de pessoa associado ao tipo de relacionamento é obrigatório.");
        }
        
        if (Objects.isNull(tipoRelacionamento.getIndicadorPrincipal())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoRelacionamento.INDICADOR_PRINCIPAL, "Indicação do registro do tipo principal é obrigatório.");
        }
        
        if (Objects.isNull(tipoRelacionamento.getIndicadorRelacionado())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoRelacionamento.INDICADOR_RELACIONADO, "Indicação do registro do tipo relacionado é obrigatório.");
        }
        
        if (Objects.isNull(tipoRelacionamento.getIndicadorSequencia())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoRelacionamento.INDICADOR_SEQUENCIA, "Indicação do registro do tipo sequencia é obrigatório.");
        }
        
        if (Objects.isNull(tipoRelacionamento.getIndicadorReceitaPessoaFisica())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoRelacionamento.INDICADOR_RECEITA_PF, "Indicação de criação de vinculo para retorno de sócios pessoas físicas perante a receita federal é obrigatório.");
        }
        
        if (Objects.isNull(tipoRelacionamento.getIndicadorReceitaPessoaJuridica())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoRelacionamento.INDICADOR_RECEITA_PJ, "Indicação de criação de vinculo para retorno de sócios pessoas jurídica perante a receita federal é obrigatório.");
        }
        
        List<PendenciasVO> listaPendencias = mapaPendencias.entrySet().stream().map(registro -> new PendenciasVO(registro.getKey(), registro.getValue()))
                .collect(Collectors.toList());
        
        if (!listaPendencias.isEmpty()) {
            throw new SimtrCadastroException("TRS.vITR.001 - Problemas identificados na execução da inclusão de um Tipo de Relacionamento.", listaPendencias);
        }
    }
    
    /**
     * Atualiza um Tipo de Relacionamento novo.
     * @param idTipoRelacionamento
     * @param tipoRelacionamentoAlteracaoDTO
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void alteraTipoRelacionamento(Integer idTipoRelacionamento, TipoRelacionamentoAlteracaoDTO tipoRelacionamentoAlteracaoDTO) {
        TipoRelacionamento tipoRelacionamento = this.findById(idTipoRelacionamento);
        this.validaAlteracaoTipoRelacionamento(tipoRelacionamento, tipoRelacionamentoAlteracaoDTO);
        tipoRelacionamento.setDataHoraUltimaAlteracao(Calendar.getInstance());
        this.update(tipoRelacionamento);
    }
    
    /**
     * Valida os campos da entidade Tipo de Relacionamento antes de alterá-la.
     * @param tipoRelacionamento
     * @param tipoRelacionamentoAlteracaoDTO
     */
    private void validaAlteracaoTipoRelacionamento(TipoRelacionamento tipoRelacionamento, TipoRelacionamentoAlteracaoDTO tipoRelacionamentoAlteracaoDTO) {
        Map<String, List<String>> mapaPendencias = new HashMap<>();

        if (!Strings.isNullOrEmpty(tipoRelacionamentoAlteracaoDTO.getNome())) {
            int tamanhoNome = tipoRelacionamento.getNome().trim().length();
            boolean existeComOutroNome = this.existeTipoRelacionamentoComNome(tipoRelacionamento);
            
            if(tamanhoNome > 50 || existeComOutroNome) {
                this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroTipoRelacionamento.NOME, "Nome do tipo de relacionamento não pode ser vazio e não pode ser igual a outro pré-existente.");
            }else {
                tipoRelacionamento.setNome(tipoRelacionamentoAlteracaoDTO.getNome());
            }
        }
        
        if (Objects.nonNull(tipoRelacionamentoAlteracaoDTO.getTipoPessoa())) {
           tipoRelacionamento.setTipoPessoaEnum(tipoRelacionamentoAlteracaoDTO.getTipoPessoa());
        }
        
        if (Objects.nonNull(tipoRelacionamentoAlteracaoDTO.getIndicadorPrincipal())) {
            tipoRelacionamento.setIndicadorPrincipal(tipoRelacionamentoAlteracaoDTO.getIndicadorPrincipal());
        }
        
        if (Objects.nonNull(tipoRelacionamentoAlteracaoDTO.getIndicadorRelacionado())) {
            tipoRelacionamento.setIndicadorRelacionado(tipoRelacionamentoAlteracaoDTO.getIndicadorRelacionado());
        }
        
        if (Objects.nonNull(tipoRelacionamentoAlteracaoDTO.getIndicadorSequencia())) {
            tipoRelacionamento.setIndicadorSequencia(tipoRelacionamentoAlteracaoDTO.getIndicadorSequencia());
        }
        
        if (Objects.nonNull(tipoRelacionamentoAlteracaoDTO.getIndicadorReceitaPF())) {
            tipoRelacionamento.setIndicadorReceitaPessoaFisica(tipoRelacionamentoAlteracaoDTO.getIndicadorReceitaPF());
        }
        
        if (Objects.nonNull(tipoRelacionamentoAlteracaoDTO.getIndicadorReceitaPJ())) {
            tipoRelacionamento.setIndicadorReceitaPessoaJuridica(tipoRelacionamentoAlteracaoDTO.getIndicadorReceitaPJ());
        }
        
        List<PendenciasVO> listaPendencias = mapaPendencias.entrySet().stream().map(registro -> new PendenciasVO(registro.getKey(), registro.getValue()))
                .collect(Collectors.toList());
        
        if (!listaPendencias.isEmpty()) {
            throw new SimtrCadastroException("TRS.vATR.001 - Problemas identificados na execução da alteracao de um Tipo de Relacionamento.", listaPendencias);
        }
    }
    
    /**
     * Deleta um Tipo de Relacionamento.
     * @param idTipoRelacionamento
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void excluiTipoRelacionamento(Integer idTipoRelacionamento) {
        TipoRelacionamento tipoRelacionamento = super.getById(idTipoRelacionamento);
        if(tipoRelacionamento == null) {
            String mensagem = MessageFormat.format("Tipo Relacionamento não localizado para o identificador informado. ID = {0}", idTipoRelacionamento);
            throw new SimtrRequisicaoException(mensagem);
        }
        
        try{
            this.delete(tipoRelacionamento);
        } catch (PersistenceException pe) {
           Throwable problema = pe.getCause();
           while ((Objects.nonNull(problema.getCause())) && !(PSQLException.class.equals(problema.getClass()))) {
               problema = problema.getCause();
           }
           throw new SimtrEstadoImpeditivoException("TRS.eTR.001 - ".concat(problema.getLocalizedMessage()), pe);
        }
    }
}
