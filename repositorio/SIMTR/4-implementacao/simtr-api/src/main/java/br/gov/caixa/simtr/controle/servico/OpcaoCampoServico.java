package br.gov.caixa.simtr.controle.servico;

import java.util.Objects;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.modelo.entidade.CampoEntrada;
import br.gov.caixa.simtr.modelo.entidade.OpcaoCampo;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRTEC,
    ConstantesUtil.PERFIL_MTRAUD,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class OpcaoCampoServico extends AbstractService<OpcaoCampo, Long> {

    @Inject
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }
   
    /**
     * Cadastra uma lista de opções campo
     * @param campoEntrada
     */
    public void cadastraOpcoesCampo(CampoEntrada campoEntrada) {
        if(Objects.nonNull(campoEntrada.getOpcoesCampo()) && !campoEntrada.getOpcoesCampo().isEmpty()) {
            campoEntrada.getOpcoesCampo().forEach(opcaoCampo -> {
                opcaoCampo.setCampoEntrada(campoEntrada);
                this.save(opcaoCampo);
            });
         }
    }
    
    /**
     * Deleta uma lista de opções campo
     * @param campoEntrada
     */
    public void deletaOpcoesCampo(Set<OpcaoCampo> opcoesCampo) {
        if(Objects.nonNull(opcoesCampo)) {
            for(OpcaoCampo opcaoCampo : opcoesCampo) {
                this.delete(opcaoCampo);
            }
        }
    }
}
