package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.servico.localidade.LocalidadeService;
import br.gov.caixa.pedesgo.arquitetura.servico.localidade.dto.LocalidadeResponseDTO;
import br.gov.caixa.simtr.controle.excecao.SimtrException;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class LocalidadeServico {

    @EJB
    private LocalidadeService localidadeService;

    @Inject
    private Logger logger;

    /**
     * Realiza a consulta de uma localidade informado um cep e complemento
     *
     * @param cep; parte inicial
     * @param cepComplemento; complemento final
     * @return Resposta encaminhada pelo localidadeService ou null caso o CEP não seja encontrado
     * @throws Exception Lançada em caso de falha de comunicação com o serviço de Localidade
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public LocalidadeResponseDTO consultaCEP(String cep, String cepComplemento) throws Exception {
        LocalidadeResponseDTO localidadeResponseDTO = null;
        try {
            localidadeResponseDTO = this.localidadeService.callService(cep, cepComplemento);
            if (localidadeResponseDTO == null) {
                throw new SimtrException("CEP não localizado no serviço de Localidade.");
            }
            return localidadeResponseDTO;
        } catch (RuntimeException e) {
            this.logger.log(Level.INFO, MessageFormat.format("SS.cS.002 {0}", e.getLocalizedMessage()), e);
            throw e;
        } catch (Exception e) {
            this.logger.log(Level.SEVERE, "SS.cS.001 Falha na comunicação com o serviço de Localidade", e);
            throw e;
        }
    }
}
