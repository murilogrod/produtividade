package br.gov.caixa.simtr.controle.servico;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.util.ConstantesUtil;

/**
 * Classe responsável por prover o <b>EntityManager</b> para as classes por herança e centraliza os métodos de busca mais <b>COMUNS</b> aos dados.
 *
 * @param <T> Tipo de classe que indica a classe utilizada nos métodos de CRUD
 * @param <U> Tipo de classe que indica a classe utilizada com chave primaria da entidade
 * @since 1.0
 */
@DeclareRoles({
    ConstantesUtil.PERFIL_SSOUMA,
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
    ConstantesUtil.PERFIL_MTRBPMANP,
    ConstantesUtil.PERFIL_MTRBPMDEV,
    ConstantesUtil.PERFIL_MTRBPMUSU,
    ConstantesUtil.PERFIL_MTRBPMAPV,
    ConstantesUtil.PERFIL_MTRBPMADM,
    ConstantesUtil.PERFIL_MTRDOSOPE,
    ConstantesUtil.PERFIL_MTRDOSMTZ,
    ConstantesUtil.PERFIL_MTRDOSINT,
    ConstantesUtil.PERFIL_MTRPAEOPE,
    ConstantesUtil.PERFIL_MTRPAESIG,
    ConstantesUtil.PERFIL_MTRPAEMTZ
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public abstract class AbstractService<T, U> {

    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public AbstractService() {
        /*
         * Estrutura utilizada para definir a classe que foi atribuida genericamente e permitir a reflexão para a utilização do list generico
         */
        Class<?> classeDao = this.getClass();
        while (!(classeDao.getGenericSuperclass() instanceof ParameterizedType)) {
            classeDao = classeDao.getSuperclass();
        }
        ParameterizedType paramType = (ParameterizedType) (classeDao.getGenericSuperclass());
        this.entityClass = (Class<T>) (paramType.getActualTypeArguments()[0]);
    }

    protected abstract EntityManager getEntityManager();

    public void refresh(T objeto) {
        EntityManager entityManager = this.getEntityManager();
        entityManager.refresh(objeto);
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
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
    public void save(T objeto) {
        EntityManager entityManager = this.getEntityManager();
        entityManager.persist(objeto);
        entityManager.flush();
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
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
    public void delete(T objeto) {
        EntityManager entityManager = this.getEntityManager();
        entityManager.remove(objeto);
        entityManager.flush();
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
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
    public T update(T objeto) {
        EntityManager entityManager = this.getEntityManager();
        objeto = entityManager.merge(objeto);
        entityManager.flush();

        return objeto;
    }

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
    public T getById(U id) {
        EntityManager entityManager = this.getEntityManager();
        return entityManager.find(this.entityClass, id);
    }

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
    public List<T> list() {
        EntityManager entityManager = this.getEntityManager();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT o FROM ");
        stringBuilder.append(this.entityClass.getSimpleName());
        stringBuilder.append(" o ");
        return entityManager.createQuery(stringBuilder.toString(), this.entityClass).getResultList(); // Retorna a lista obtida.
    }

    /**
     * Verifica se o objeto encaminha trata-se de uma instancia valida. Caso seja uma referência nula lança uma exceção com a mensagem de erro encaminhada inclusa
     * como causa do problema.
     *
     * @param obj Objeto a ser verificado
     * @param mensagemErro Mensagem de erro a ser incluida como causa da exceção
     * @throws SimtrRecursoDesconhecidoException Lançada caso a referência ao recurso a ser verificado seja um valor nulo
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
    public void validaRecursoLocalizado(T obj, String mensagemErro) {
        if (Objects.isNull(obj)) {
            throw new SimtrRecursoDesconhecidoException(mensagemErro);
        }
    }
}
