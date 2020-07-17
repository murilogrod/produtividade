package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;

import com.google.common.base.Strings;

import br.gov.caixa.simtr.controle.excecao.SimtrCadastroException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.helper.CadastroHelper;
import br.gov.caixa.simtr.controle.vo.excecao.PendenciasVO;
import br.gov.caixa.simtr.modelo.entidade.Apontamento;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroApontamento;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist.AlteracaoApontamentoDTO;
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
public class ApontamentoServico extends AbstractService<Apontamento, Integer> {

    @EJB
    private CadastroHelper cadastroHelper;
    
    @Inject
    private EntityManager entityManager;
    
    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }
    
    /**
     * Cadastra um apontamento.
     * @param checklist
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void cadastraApontamento(Apontamento apontamento) {
        this.validaInclusaoApontamento(apontamento);
        this.atualizaOrdemApontamentosCadastrados(apontamento);
        this.save(apontamento);
    }

    /**
     * Valida se todos os campos da enteidade estão preenchidos corretamente.
     * @param apontamento
     */
    private void validaInclusaoApontamento(Apontamento apontamento) {
        Map<String, List<String>> mapaPendencias = new HashMap<>();
        
        if (Strings.isNullOrEmpty(apontamento.getNome())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.NOME, "Nome do apontamento é obrigatório.");
        }else {
            int tamanhoNome = apontamento.getNome().trim().length();
            boolean existeApontamentoMesmoNome = this.verificaExisteApontamentoPeloNome(apontamento.getChecklist().getId(), apontamento.getNome());
            
            if(tamanhoNome > 500 || existeApontamentoMesmoNome) {
                this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.NOME, "Nome do apontamento deve conter no máximo 500 caracteres e não pode ter o mesmo nome de um apontamento já cadastrado no checklist.");
            }
        }
        
        if(Strings.isNullOrEmpty(apontamento.getOrientacao())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.ORIENTACAO_RETORNO, "A orientação de ajuste para o apontamento não aprovado é obrigatória.");
        }
        
        if(Objects.isNull(apontamento.getIndicativoInformacao())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.INDICATIVO_INFORMACAO, "A indicação de atribuição do dossiê como pendente de informação é obrigatória.");
        }
        
        if(Objects.isNull(apontamento.getIndicativoRejeicao())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.INDICATIVO_REJEICAO, "A indicação de atribuição do elemento rejeitado é obrigatória.");
        }
        
        if(Objects.isNull(apontamento.getIndicativoSeguranca())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.INDICATIVO_SEGURANCA, "A indicação de atribuição do dossiê como pendente de análise da segurança é obrigatória.");
        }
        
        if(apontamento.getIndicativoInformacao() && apontamento.getIndicativoSeguranca()) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.INDICATIVO_INFORMACAO, "O apontamento não deve indicar pendência de informação e pendência de segurança ao mesmo tempo.");
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.INDICATIVO_SEGURANCA, "O apontamento não deve indicar pendência de informação e pendência de segurança ao mesmo tempo.");
        }
        
        if(Objects.isNull(apontamento.getOrdem())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.ORDEM, "A informação do número de ordem de apresentação do apontamento perante o checklist é obrigatório.");
        }else if(apontamento.getOrdem() <= 0) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.ORDEM, "A informação do número de ordem de apresentação do apontamento deve ser um número inteiro positivo.");
        }
        
        if(!Strings.isNullOrEmpty(apontamento.getTeclaAtalho()) && apontamento.getTeclaAtalho().trim().length() > 30){
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.TECLA_ATALHO, "A informação da tecla de atalho não deve ser superior a 30 caracteres.");
        }
        
        List<PendenciasVO> listaPendencias = mapaPendencias.entrySet().stream().map(registro -> new PendenciasVO(registro.getKey(), registro.getValue()))
                .collect(Collectors.toList());
        
        if (!listaPendencias.isEmpty()) {
            throw new SimtrCadastroException("AS.vIA.001 - Problemas identificados na execução da inclusão de um apontamento.", listaPendencias);
        }
    }

    /**
     * Filtra os apontamentos de ordem igual ou superior a ordem do novo apontamento e 
     * incrementa a ordem em 1(um) valor acima da atual de cada apontamento.
     * @param apontamento
     */
    private void atualizaOrdemApontamentosCadastrados(Apontamento apontamento) {
        if(this.verificaExisteApontamentoMesmaOrdem(apontamento)) {
            List<Apontamento> apontamentosCadastrados = this.consultaApontamentosPelaOrdem(apontamento);
            if(!apontamentosCadastrados.isEmpty()) {
                apontamentosCadastrados.forEach(apontamentoCadastrado -> {
                    apontamentoCadastrado.setOrdem(apontamentoCadastrado.getOrdem() + 1);
                    this.update(apontamentoCadastrado);
                });
            }
        }
    }
    
    /**
     * Verifica se já existe apontamento cadastrado com o mesmo nome em um mesmo checklist.
     * @param nome
     * @return
     */
    private boolean verificaExisteApontamentoPeloNome(Integer idChecklist, String nomeApontamento) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT COUNT(a) FROM Apontamento a WHERE a.checklist.id =:idChecklist AND UPPER(a.nome) LIKE :nome ");

        TypedQuery<Long> query = this.entityManager.createQuery(jpql.toString(), Long.class);
        query.setParameter("idChecklist", idChecklist);
        query.setParameter("nome", nomeApontamento.trim().toUpperCase());

        return query.getSingleResult().intValue() > 0;
    }
    
    /**
     * Consulta apontamento do checklist atual que seja da mesma ordem 
     * @param apontamento
     * @return List<Apontamento>
     */
    private boolean verificaExisteApontamentoMesmaOrdem(Apontamento apontamento) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT COUNT(a) FROM Apontamento a WHERE a.checklist.id =:idChecklist ");
        jpql.append("AND a.ordem = :ordem");

        TypedQuery<Long> query = this.entityManager.createQuery(jpql.toString(), Long.class);
        query.setParameter("idChecklist", apontamento.getChecklist().getId());
        query.setParameter("ordem", apontamento.getOrdem());

        return query.getSingleResult().intValue() > 0;
    }
    
    /**
     * Consulta os apontamentos do checklist atual que estejam na mesma ordem ou 
     * superior a ordem do novo apontamnto a ser cadastrado.
     * @param apontamento
     * @return List<Apontamento>
     */
    private List<Apontamento> consultaApontamentosPelaOrdem(Apontamento apontamento) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT a FROM Apontamento a WHERE a.checklist.id =:idChecklist ");
        jpql.append("AND a.ordem >= :ordem");

        TypedQuery<Apontamento> query = this.entityManager.createQuery(jpql.toString(), Apontamento.class);
        query.setParameter("idChecklist", apontamento.getChecklist().getId());
        query.setParameter("ordem", apontamento.getOrdem());
        return query.getResultList();
    }
    
    /**
     * Atualiza apontamento com as novas informações recebidas.
     * @param idApontamento
     * @param alteracaoApontamentoDTO
     */
    /**
     * Atualiza apontamento com as novas informações recebidas.
     * @param idApontamento
     * @param alteracaoApontamentoDTO
     */
    public void alteraApontamento(Set<Apontamento> apontamentosCadastrados, Long idApontamento, AlteracaoApontamentoDTO alteracaoApontamentoDTO) {
        if(apontamentosCadastrados.stream().noneMatch(apontamento -> Objects.equals(apontamento.getId(), idApontamento))) {
            String mensagem = MessageFormat.format("AS.aA.001 - Não é possivel localizar Apontamento com ID {0}", String.valueOf(idApontamento));
            throw new SimtrRequisicaoException(mensagem);
        }
        
        Optional<Apontamento> optApontamento = apontamentosCadastrados.stream().filter(apontamentoCadastrado -> Objects.equals(apontamentoCadastrado.getId(), idApontamento)).findFirst();
        
        if(optApontamento.isPresent()) {
        	Apontamento apontamento = optApontamento.get();
        	this.validaAlteracaoApontamento(apontamento, alteracaoApontamentoDTO);
        	this.update(apontamento);
        }
        
    }
    
    /**
     * Valida se os campos do apontamento a ser alterado são válidos.
     * @param apontamento
     * @param alteracaoApontamentoDTO
     */
    private void validaAlteracaoApontamento(Apontamento apontamento, AlteracaoApontamentoDTO alteracaoApontamentoDTO) {
        Map<String, List<String>> mapaPendencias = new HashMap<>();
        
        if (!Strings.isNullOrEmpty(alteracaoApontamentoDTO.getNomeApontamento())) {
            int tamanhoNome = alteracaoApontamentoDTO.getNomeApontamento().trim().length();
            boolean existeApontamentoMesmoNome = false;
            if(!apontamento.getNome().trim().equals(alteracaoApontamentoDTO.getNomeApontamento().trim())){
                existeApontamentoMesmoNome = this.verificaExisteApontamentoPeloNome(apontamento.getChecklist().getId(), alteracaoApontamentoDTO.getNomeApontamento());
            }
            if(tamanhoNome > 500 || existeApontamentoMesmoNome) {
                this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.NOME, "Nome do apontamento deve conter no máximo 500 caracteres e não pode ter o mesmo nome de um apontamento já cadastrado no checklist.");
            }
        }else if(Objects.nonNull(alteracaoApontamentoDTO.getNomeApontamento()) && alteracaoApontamentoDTO.getNomeApontamento().isEmpty()) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.NOME, "Nome do apontamento é obrigatório.");
        }
        
        if(Objects.nonNull(alteracaoApontamentoDTO.getNomeApontamento()) && !alteracaoApontamentoDTO.getNomeApontamento().isEmpty()) {
            apontamento.setNome(alteracaoApontamentoDTO.getNomeApontamento());        
        }
        
        if(Objects.nonNull(alteracaoApontamentoDTO.getDescricaoApontamento()) && alteracaoApontamentoDTO.getDescricaoApontamento().isEmpty()) {
            apontamento.setDescricao(null);
        }
        
        if(Objects.nonNull(alteracaoApontamentoDTO.getDescricaoApontamento()) && !alteracaoApontamentoDTO.getDescricaoApontamento().isEmpty()) {
            apontamento.setDescricao(alteracaoApontamentoDTO.getDescricaoApontamento());
        }
        
        if(!Strings.isNullOrEmpty(alteracaoApontamentoDTO.getOrientacaoRetorno())) {
            apontamento.setOrientacao(alteracaoApontamentoDTO.getOrientacaoRetorno());
        }else if(Objects.nonNull(alteracaoApontamentoDTO.getOrientacaoRetorno()) && alteracaoApontamentoDTO.getOrientacaoRetorno().isEmpty()) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.ORIENTACAO_RETORNO, "A orientação de ajuste para o apontamento não aprovado é obrigatória.");
        }
         
        if(Objects.nonNull(alteracaoApontamentoDTO.getIndicativoInformacao()) 
                && Objects.nonNull(alteracaoApontamentoDTO.getIndicativoSeguranca()) 
                && alteracaoApontamentoDTO.getIndicativoInformacao()
                && alteracaoApontamentoDTO.getIndicativoSeguranca()) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.INDICATIVO_INFORMACAO, "O apontamento não deve indicar pendência de informação e pendência de segurança ao mesmo tempo.");
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.INDICATIVO_SEGURANCA, "O apontamento não deve indicar pendência de informação e pendência de segurança ao mesmo tempo.");
        } else if(Objects.nonNull(alteracaoApontamentoDTO.getIndicativoInformacao()) 
                && alteracaoApontamentoDTO.getIndicativoInformacao()
                && alteracaoApontamentoDTO.getIndicativoSeguranca()) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.INDICATIVO_INFORMACAO, "O apontamento não deve indicar pendência de informação e pendência de segurança ao mesmo tempo.");
        } else if(Objects.nonNull(alteracaoApontamentoDTO.getIndicativoSeguranca()) 
                && alteracaoApontamentoDTO.getIndicativoSeguranca()
                && alteracaoApontamentoDTO.getIndicativoInformacao()) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.INDICATIVO_SEGURANCA, "O apontamento não deve indicar pendência de informação e pendência de segurança ao mesmo tempo.");
        } else if(Objects.nonNull(alteracaoApontamentoDTO.getIndicativoInformacao()) 
                && Objects.nonNull(alteracaoApontamentoDTO.getIndicativoSeguranca())){
            apontamento.setIndicativoInformacao(alteracaoApontamentoDTO.getIndicativoInformacao());
            apontamento.setIndicativoSeguranca(alteracaoApontamentoDTO.getIndicativoSeguranca());
        } else if(Objects.nonNull(alteracaoApontamentoDTO.getIndicativoInformacao()) 
                && Objects.isNull(alteracaoApontamentoDTO.getIndicativoSeguranca())){
            apontamento.setIndicativoInformacao(alteracaoApontamentoDTO.getIndicativoInformacao());
        } else if(Objects.isNull(alteracaoApontamentoDTO.getIndicativoInformacao()) 
                && Objects.nonNull(alteracaoApontamentoDTO.getIndicativoSeguranca())){
            apontamento.setIndicativoSeguranca(alteracaoApontamentoDTO.getIndicativoSeguranca());
        }
        
        if(Objects.nonNull(alteracaoApontamentoDTO.getIndicativoRejeicao())) {
            apontamento.setIndicativoRejeicao(alteracaoApontamentoDTO.getIndicativoRejeicao());
        }
        
        if(Objects.isNull((alteracaoApontamentoDTO.getOrdem()))) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.ORDEM, "A informação do número de ordem de apresentação é obrigatória.");
        } else if(alteracaoApontamentoDTO.getOrdem() <= 0) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.ORDEM, "A informação do número de ordem de apresentação do apontamento deve ser um número inteiro positivo.");
        } else {
            apontamento.setOrdem(alteracaoApontamentoDTO.getOrdem());
        }
        
        if(!Strings.isNullOrEmpty(alteracaoApontamentoDTO.getTeclaAtalhoApontamento()) && alteracaoApontamentoDTO.getTeclaAtalhoApontamento().trim().length() > 30){
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroApontamento.TECLA_ATALHO, "A informação da tecla de atalho não deve ser superior a 30 caracteres.");
        } else if(!Strings.isNullOrEmpty(alteracaoApontamentoDTO.getTeclaAtalhoApontamento())) {
            apontamento.setTeclaAtalho(alteracaoApontamentoDTO.getTeclaAtalhoApontamento());
        }
        
        List<PendenciasVO> listaPendencias = mapaPendencias.entrySet().stream().map(registro -> new PendenciasVO(registro.getKey(), registro.getValue()))
                .collect(Collectors.toList());
        
        if (!listaPendencias.isEmpty()) {
            throw new SimtrCadastroException("AS.vAA.001 - Problemas identificados na execução da alteração de um apontamento.", listaPendencias);
        }
    }
    
    /**
     * Exclui apontamento pelo seu identificador.
     * @param apontamentos
     * @param idApontamento
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void excluiApontamento(Set<Apontamento> apontamentosCadastrados, Long idApontamento) {
        if(apontamentosCadastrados.stream().noneMatch(apontamento -> Objects.equals(apontamento.getId(), idApontamento))) {
            String mensagem = MessageFormat.format("AS.eA.001 - Não é possivel localizar Apontamento com ID {0}", String.valueOf(idApontamento));
            throw new SimtrRequisicaoException(mensagem);
        }
        Apontamento apontamento = apontamentosCadastrados.stream().filter(apontamentoCadastrado -> Objects.equals(apontamentoCadastrado.getId(), idApontamento)).findFirst().get();
        Integer ordem = apontamento.getOrdem();
        this.delete(this.getEntityManager().merge(apontamento));
        apontamentosCadastrados.remove(apontamento);
        
        List<Apontamento> apontamentosFiltrados = apontamentosCadastrados.stream().filter(apontamentoCadastrado -> apontamentoCadastrado.getOrdem() > ordem)
                                                                                  .sorted(Comparator.comparingInt(Apontamento::getOrdem))
                                                                                  .collect(Collectors.toList());
        if(!apontamentosFiltrados.isEmpty()) {
            apontamentosFiltrados.forEach(apontamentoFiltrado -> {
                apontamentoFiltrado.setOrdem(apontamentoFiltrado.getOrdem() - 1);
                this.update(apontamentoFiltrado);
            });
        }
    }
    
    /**
     * Percorre a lista de apontamentos e os exclui.
     * @param apontamentos
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void excluiApontamentos(Set<Apontamento> apontamentos) {
        if(Objects.nonNull(apontamentos)) {
           for(Apontamento apontamento : apontamentos) {
               this.getEntityManager().remove(this.getEntityManager().merge(apontamento));
           }
        }
    }
}
