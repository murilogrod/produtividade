package br.gov.caixa.simtr.controle.servico.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.modelo.entidade.ComposicaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.RegraDocumental;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE,
    ConstantesUtil.PERFIL_MTRDOSINT,
    ConstantesUtil.PERFIL_MTRDOSMTZ,
    ConstantesUtil.PERFIL_MTRDOSOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class NivelDocumentalComposicaoHelper {

    public List<RegraDocumental> analisaComposicaoAtendida(ComposicaoDocumental composicaoDocumental, List<Documento> documentos){
        Set<RegraDocumental> regrasDocumentais = composicaoDocumental.getRegrasDocumentais();
        List<RegraDocumental> regrasNaoAtendidas = new ArrayList<>();
        // Percorre todas as regras localizadas para cada composição
        regrasDocumentais.forEach(regraDocumental -> {
            boolean atendida = this.analisaRegraDocumental(regraDocumental, documentos);
            if (!atendida) {
                regrasNaoAtendidas.add(regraDocumental);
            }
        });
        return regrasNaoAtendidas;
    }

    
    
    // ************ METODOS PRIVADOS **************//

    /**
     * Realiza a analise de atendimento a uma determinada regra documental.
     *
     * @param regraDocumental Regra Documentasl a ser analisada
     * @param documentosCliente Lista de documentos do cliente utilizados como base na identificação do atendimento a regra
     * @return true se a regra atendida ou false caso contrario.
     */
    private boolean analisaRegraDocumental(RegraDocumental regraDocumental, List<Documento> documentosCliente) {
        boolean documentoLocalizado = Boolean.FALSE;

        // Regra atende por função documental ou tipo especifico?
        if (regraDocumental.getFuncaoDocumental() != null) {

            FuncaoDocumental funcaoDocumental = regraDocumental.getFuncaoDocumental();

            // Percorre a lista de tipos vinculada a função documental para verificar se algum documento do cliente atende a necessidade definida.
            Set<TipoDocumento> tiposDocumento = funcaoDocumental.getTiposDocumento();
            for (TipoDocumento tipoDocumento : tiposDocumento) {
                // Percorre documentos do cliente em busca de um que atenda a função definida na regra
                if (documentosCliente.stream().filter(dc -> tipoDocumento.equals(dc.getTipoDocumento())).findAny().isPresent()) {
                    documentoLocalizado = Boolean.TRUE;
                    break;
                }
            }
        } else {
            TipoDocumento tipoDocumento = regraDocumental.getTipoDocumento();
            if (documentosCliente.stream().filter(dc -> tipoDocumento.equals(dc.getTipoDocumento())).findAny().isPresent()) {
                documentoLocalizado = Boolean.TRUE;
            }
        }
        return documentoLocalizado;
    }
}
