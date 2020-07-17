package br.gov.caixa.simtr.controle.servico;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.modelo.entidade.Dominio;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class DominioServico extends AbstractService<Dominio, Integer>{

	@Inject
	private EntityManager entityManager;
	
	private Calendar dataHoraUltimaAlteracao;
	
	private final Map<Integer, Dominio> mapaById = new HashMap<>();
	
	@Override
	protected EntityManager getEntityManager() {
		return this.entityManager;
	}
	
	@PermitAll
    public Calendar getDataHoraUltimaAlteracao() {
        return dataHoraUltimaAlteracao;
    }
	
	@PostConstruct
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void carregarMapas() {
        // Captura a data de ultima atualização realizada com qualquer registro da tabela de tipo de documento
        String jpqlUltimaAlteracao = " SELECT MAX(d.dataHoraUltimaAlteracao) FROM Dominio d ";
        TypedQuery<Calendar> queryUltimaAlteracao = this.entityManager.createQuery(jpqlUltimaAlteracao, Calendar.class);
        Calendar ultimaAlteracao = queryUltimaAlteracao.getSingleResult();

        // Verifica se a data armazenada no EJB como base de ultima atualização é nula ou menor do que a retornada na consulta
        if (Objects.isNull(this.dataHoraUltimaAlteracao) || dataHoraUltimaAlteracao.before(ultimaAlteracao)) {

            StringBuilder jpqlDominio = new StringBuilder();
            jpqlDominio.append(" SELECT DISTINCT d FROM Dominio d ");
            jpqlDominio.append(" LEFT JOIN FETCH d.opcoesDominio op ");

            List<Dominio> dominios = this.entityManager.createQuery(jpqlDominio.toString(), Dominio.class).getResultList();

            // Limpa os mapas de armazenamento da tipologia
            this.mapaById.clear();

            // Recarrega os mapas pelas diferentes visões possiveis
            dominios.forEach(dominio -> {
                this.mapaById.put(dominio.getId(), dominio);
            });

            // Atualiza a data armazenada no EJB como base para comparação da ultima alteração
            this.dataHoraUltimaAlteracao = ultimaAlteracao;
        }
    }
	
	@Override
    @PermitAll
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Dominio> list() {
        this.carregarMapas();
        List<Dominio> lista = this.mapaById.values().stream().collect(Collectors.toList());
        return new ArrayList<>(lista);
    }
}
