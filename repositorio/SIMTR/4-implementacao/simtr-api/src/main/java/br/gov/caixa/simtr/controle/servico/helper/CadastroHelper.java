package br.gov.caixa.simtr.controle.servico.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CadastroHelper {

    /**
     * Adiciona no mapa uma nova pendência
     *
     * @param mapa de pendência
     * @param nome do campo
     * @param descrição da pendência
     */
    public void incluiPendenciaMapa(Map<String, List<String>> mapaPendencias, String campo, String pendencia) {
        // Captura a lista de pendencias pré-existentes no mapa vinculada ao campo, mas caso não exista, cria uma nova
        List<String> pendencias = mapaPendencias.getOrDefault(campo, new ArrayList<>());

        // Adiciona a pendência na lista
        pendencias.add(pendencia);

        // Inclui ou sobrepôe a lista de pendencias associada ao campo
        mapaPendencias.put(campo, pendencias);
    }

}
