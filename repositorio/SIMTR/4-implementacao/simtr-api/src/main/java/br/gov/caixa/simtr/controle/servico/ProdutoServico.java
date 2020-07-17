package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.postgresql.util.PSQLException;

import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.produto.ProdutoManutencaoDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRAUD,
    ConstantesUtil.PERFIL_MTRTEC,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class ProdutoServico extends AbstractService<Produto, Integer> {

    @Inject
    private EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(ProdutoServico.class.getName());

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Realizar a captura de um Produto baseado no identificador informado.
     *
     * @param id Identificador do produto desejado.
     * @param vinculacoesComportamentoPesquisa Indica se as vinculações relativas ao comportamento esperado após pesquisas cadastrais devem ser carregadas
     * @param vinculacoesComposicaoDocumento Indica se as vinculações relativas as composições documentais do dossiê digital devem devem ser carregadas
     * @param vinculacoesDossieProduto Indica se as vinculações relativas aos dossiês de produto com envolvimento neste produto devem ser carregados. Vale ressaltar
     *        que as derivações do dossiês não serão carregadas.
     * @param vinculacoesProcesso Indica se as vinculações relativas aos processos associados com o produtodevem ser carregadas
     * @param vinculacoesGarantias Indica se as vinculações relativas as garantias associadas ao produto devem ser carregadas
     * @return Produto localizado com as informações paramterizadas carregadas ou null caso o produto não seja localizado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTO,
        ConstantesUtil.PERFIL_MTRSDNTTG
    })
    public Produto getById(final Integer id, final boolean vinculacoesComportamentoPesquisa, final boolean vinculacoesComposicaoDocumento, final boolean vinculacoesDossieProduto, final boolean vinculacoesProcesso, final boolean vinculacoesGarantias, final boolean vinculacoesElementosConteudo) {
        if (id == null) {
            String mensagem = MessageFormat.format("Necessario informar o identificador para realizar a consulta do produto. ID: {0}", id);
            throw new SimtrRequisicaoException(mensagem);
        }
        return this.getByIdOrOperacaoModalidade(id, null, null, vinculacoesComportamentoPesquisa, vinculacoesComposicaoDocumento, vinculacoesDossieProduto, vinculacoesProcesso, vinculacoesGarantias, vinculacoesElementosConteudo);
    }

    /**
     * Realizar a captura de um Produto baseado no codigo de operação e codigo de modalidade informados.
     *
     * @param operacao Codigo de operação do produto desejado.
     * @param modalidade Codigo de modalidade do produto desejado.
     * @param vinculacoesComportamentoPesquisa Indica se as vinculações relativas ao comportamento esperado após pesquisas cadastrais devem ser carregadas
     * @param vinculacoesComposicaoDocumento Indica se as vinculações relativas as composições documentais do dossiê digital devem devem ser carregadas
     * @param vinculacoesDossieProduto Indica se as vinculações relativas aos dossiês de produto com envolvimento neste produto devem ser carregados. Vale ressaltar
     *        que as derivações do dossiês não serão carregadas.
     * @param vinculacoesProcesso Indica se as vinculações relativas aos processos associados com o produtodevem ser carregadas
     * @param vinculacoesGarantias Indica se as vinculações relativas as garantias associadas ao produto devem ser carregadas
     * @return Produto localizado com as informações paramterizadas carregadas ou null caso o produto não seja localizado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTO,
        ConstantesUtil.PERFIL_MTRSDNTTG
    })
    public Produto getByOperacaoModalidade(final Integer operacao, final Integer modalidade, final boolean vinculacoesComportamentoPesquisa, final boolean vinculacoesComposicaoDocumento, final boolean vinculacoesDossieProduto, final boolean vinculacoesProcesso, final boolean vinculacoesGarantias, final boolean vinculacoesElementosConteudo) {
        if ((operacao == null) || (modalidade == null)) {
            String mensagem = MessageFormat.format("Necessario informar operação e modalidade para realizar a consulta do produto. Operação: {0} | Modalidade {1}", operacao, modalidade);
            throw new SimtrRequisicaoException(mensagem);
        }
        return this.getByIdOrOperacaoModalidade(null, operacao, modalidade, vinculacoesComportamentoPesquisa, vinculacoesComposicaoDocumento, vinculacoesDossieProduto, vinculacoesProcesso, vinculacoesGarantias, vinculacoesElementosConteudo);
    }
    
    /**
     * Consulta um produto pelo seu código correspondente no portal do empreendedor.
     * @param codigoProdutoPortalEmpreendedor
     * @return
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT
    })
    public Produto getByCodigoPortalEmpreendedor(Integer codigoProdutoPortalEmpreendedor) {
        if (Objects.isNull(codigoProdutoPortalEmpreendedor)) {
            throw new SimtrRequisicaoException("Código do produto no portal do empreendedor é obrigatório.");
        }
        final StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT p FROM Produto p ");
        jpql.append("WHERE p.codigoProdutoPortalEmpreendedor = :codigoProduto");
        TypedQuery<Produto> query = this.entityManager.createQuery(jpql.toString(), Produto.class);
        query.setParameter("codigoProduto", codigoProdutoPortalEmpreendedor);
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }

    /**
     * Aplica as alterações encaminhadas no produto indicado.
     *
     * @param id Identificador do produto ser alterada.
     * @param produtoManutencaoDTO Objeto contendo os atributos a serem alterados na aplicação do patch. Caso os atributos sejam nulos nenhuma alteração será
     *        realizada no mesmo.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void aplicaPatch(Integer id, ProdutoManutencaoDTO produtoManutencaoDTO) {
        try {
            Produto produto = this.getById(id);

            this.validaProdutoLocalizado(produto, "PS.aP.001 - Produto não localizado para o identificador informado");

            if (Objects.nonNull(produtoManutencaoDTO.getContratacaoConjunta())) {
                produto.setContratacaoConjunta(produtoManutencaoDTO.getContratacaoConjunta());
            }

            if (Objects.nonNull(produtoManutencaoDTO.getDossieDigital())) {
                produto.setDossieDigital(produtoManutencaoDTO.getDossieDigital());
            }

            if (Objects.nonNull(produtoManutencaoDTO.getModalidadeProduto())) {
                produto.setModalidade(produtoManutencaoDTO.getModalidadeProduto());
            }

            if (Objects.nonNull(produtoManutencaoDTO.getNomeProduto())) {
                produto.setNome(produtoManutencaoDTO.getNomeProduto());
            }

            if (Objects.nonNull(produtoManutencaoDTO.getOperacaoProduto())) {
                produto.setOperacao(produtoManutencaoDTO.getOperacaoProduto());
            }

            if (Objects.nonNull(produtoManutencaoDTO.getPesquisaCCF())) {
                produto.setPesquisaCcf(produtoManutencaoDTO.getPesquisaCCF());
            }

            if (Objects.nonNull(produtoManutencaoDTO.getPesquisaCadin())) {
                produto.setPesquisaCadin(produtoManutencaoDTO.getPesquisaCadin());
            }

            if (Objects.nonNull(produtoManutencaoDTO.getPesquisaReceita())) {
                produto.setPesquisaReceita(produtoManutencaoDTO.getPesquisaReceita());
            }

            if (Objects.nonNull(produtoManutencaoDTO.getPesquisaSCPC())) {
                produto.setPesquisaScpc(produtoManutencaoDTO.getPesquisaSCPC());
            }

            if (Objects.nonNull(produtoManutencaoDTO.getPesquisaSerasa())) {
                produto.setPesquisaSerasa(produtoManutencaoDTO.getPesquisaSerasa());
            }

            if (Objects.nonNull(produtoManutencaoDTO.getPesquisaSicow())) {
                produto.setPesquisaSicow(produtoManutencaoDTO.getPesquisaSicow());
            }

            if (Objects.nonNull(produtoManutencaoDTO.getPesquisaSinad())) {
                produto.setPesquisaSinad(produtoManutencaoDTO.getPesquisaSinad());
            }

            if (Objects.nonNull(produtoManutencaoDTO.getTipoPessoa())) {
                produto.setTipoPessoa(produtoManutencaoDTO.getTipoPessoa());
            }
            
            if (Objects.nonNull(produtoManutencaoDTO.getCodigoProdutoPortalEmpreendedor())) {
                produto.setCodigoProdutoPortalEmpreendedor(produtoManutencaoDTO.getCodigoProdutoPortalEmpreendedor());
            }

            this.update(produto);
        } catch (PersistenceException pe) {
            Throwable problema = pe.getCause();
            while ((Objects.nonNull(problema.getCause())) && !(PSQLException.class.equals(problema.getClass()))) {
                problema = problema.getCause();
            }
            throw new SimtrEstadoImpeditivoException("CS.aP.002 - ".concat(problema.getLocalizedMessage()), pe);
        }
    }

    /**
     * Realiza a exclusão de um produto
     *
     * @param id Identificador do canal a ser excluido
     * @throws SimtrRecursoDesconhecidoException Lançada caso o canal não seja localizado sob o identificador informado.
     * @throws SimtrEstadoImpeditivoException Lançada caso identificado vinculos com registros de outras entidades existentes com o canal que impedem a sua exclusão
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void deleteByID(Integer id) {

        try {
            Produto produto = this.getById(id);
            if (Objects.nonNull(produto)) {
                this.delete(produto);
            } else {
                String mensagem = MessageFormat.format("CS.dCBI.001 - Produto não localizado sob identificador informado. ID = {0}", id);
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
     * Valida se o canal encaminhado é diferente de nulo.
     *
     * @param produto Objeto que representa o produto a ser verificado.
     * @param mensagemErro Mensagem de erro a ser incluida na exceção lançada caso a janela indicada não seja validada na configuração do canal.
     * @throws SimtrRecursoDesconhecidoException Lançada caso o objeto encaminhado para validação seja nulo.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void validaProdutoLocalizado(Produto produto, String mensagemErro) {
        if (Objects.isNull(produto)) {
            throw new SimtrRecursoDesconhecidoException(mensagemErro);
        }
    }

    // *********** Métodos Privados ***********
    /**
     * Realizar a captura de um Produto baseado no identificador do produto ou no codigo de operação e codigo de modalidade informados.
     *
     * @param id Identificador do produto desejado. Caso informado, a consulta será realizada pelo id
     * @param operacao Codigo de operação do produto desejado.
     * @param modalidade Codigo de modalidade do produto desejado.
     * @param vinculacoesComportamentoPesquisa Indica se as vinculações relativas ao comportamento esperado após pesquisas cadastrais devem ser carregadas
     * @param vinculacoesComposicaoDocumento Indica se as vinculações relativas as composições documentais do dossiê digital devem devem ser carregadas
     * @param vinculacoesDossieProduto Indica se as vinculações relativas aos dossiês de produto com envolvimento neste produto devem ser carregados. Vale ressaltar
     *        que as derivações do dossiês não serão carregadas.
     * @param vinculacoesProcesso Indica se as vinculações relativas aos processos associados com o produtodevem ser carregadas
     * @param vinculacoesGarantias Indica se as vinculações relativas as garantias associadas ao produto devem ser carregadas
     * @return Produto localizado com as informações paramterizadas carregadas ou null caso o produto não seja localizado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    private Produto getByIdOrOperacaoModalidade(final Integer id, final Integer operacao, final Integer modalidade, final boolean vinculacoesComportamentoPesquisa, final boolean vinculacoesComposicaoDocumento, final boolean vinculacoesDossieProduto, final boolean vinculacoesProcesso, final boolean vinculacoesGarantias, final boolean vinculacoesElementosConteudo) {
        final StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT p FROM Produto p ");
        if (vinculacoesComportamentoPesquisa) {
            jpql.append(" LEFT JOIN FETCH p.comportamentosPesquisas cp ");
        }
        if (vinculacoesComposicaoDocumento) {
            jpql.append(" LEFT JOIN FETCH p.composicoesDocumentais cd ");
            jpql.append(" LEFT JOIN FETCH cd.regrasDocumentais rd ");
            jpql.append(" LEFT JOIN FETCH rd.tipoDocumento td ");
            jpql.append(" LEFT JOIN FETCH rd.funcaoDocumental fd ");
            jpql.append(" LEFT JOIN FETCH fd.tiposDocumento tdfd ");
        }
        if (vinculacoesDossieProduto) {
            jpql.append(" LEFT JOIN FETCH p.produtosDossie pd ");
            jpql.append(" LEFT JOIN FETCH pd.dossieProduto dp ");
        }
        if (vinculacoesProcesso) {
            jpql.append(" LEFT JOIN FETCH p.processos proc ");
        }
        if (vinculacoesGarantias) {
            jpql.append(" LEFT JOIN FETCH p.garantias g ");
        }
        if (vinculacoesElementosConteudo) {
            jpql.append(" LEFT JOIN FETCH p.elementosConteudo ec ");
            jpql.append(" LEFT JOIN FETCH ec.tipoDocumento tdec ");
        }
        if (id != null) {
            jpql.append(" WHERE p.id = :id ");
        } else {
            jpql.append(" WHERE p.operacao = :operacao AND p.modalidade = :modalidade ");
        }
        TypedQuery<Produto> query = this.entityManager.createQuery(jpql.toString(), Produto.class);
        if (id != null) {
            query.setParameter("id", id);
        } else {
            query.setParameter("operacao", operacao);
            query.setParameter("modalidade", modalidade);
        }
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }
}
