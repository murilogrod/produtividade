package br.gov.caixa.simtr.controle.servico;

import java.util.Objects;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.modelo.entidade.CampoApresentacao;
import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario.AlteracaoCadastroDefinicaoCampoFormularioDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario.FormaApresentacaoDTO;
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
public class CampoApresentacaoServico extends AbstractService<CampoApresentacao, Long> {

    @Inject
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * 
     * @param campoForumulario
     */
    public void cadastraCamposApresentacao(CampoFormulario campoForumulario) {
        if(Objects.nonNull(campoForumulario.getCamposApresentacao()) && !campoForumulario.getCamposApresentacao().isEmpty()) {
            campoForumulario.getCamposApresentacao().forEach(campoApresentacao -> {
                campoApresentacao.setCampoFormulario(campoForumulario);
                this.save(campoApresentacao);
            });
        }
    }
    
    /**
     * Atualiza campos de apresentação existentes e salva os inexistentes.
     * @param campoFormulario
     * @param alteracaoCadastroCampoFormularioDTO
     */
    public void atualizaCamposApresentacao(CampoFormulario campoFormulario, AlteracaoCadastroDefinicaoCampoFormularioDTO alteracaoCadastroCampoFormularioDTO) {
        for(FormaApresentacaoDTO formaApresentacao : alteracaoCadastroCampoFormularioDTO.getFormasApresentacao()) {
            Boolean encontrouCompativel = false;
            for(CampoApresentacao campoApresentacao : campoFormulario.getCamposApresentacao()) {
                if(campoApresentacao.getTipoDispositivoEnum().equals(formaApresentacao.getTipoDispositivo())){
                    encontrouCompativel = true;
                    campoApresentacao.setLargura(formaApresentacao.getLargura());
                    this.update(campoApresentacao);
                }
            }
            if (!encontrouCompativel) {
                CampoApresentacao campoApresentacao = new CampoApresentacao();
                campoApresentacao.setCampoFormulario(campoFormulario);
                campoApresentacao.setTipoDispositivoEnum(formaApresentacao.getTipoDispositivo());
                campoApresentacao.setLargura(formaApresentacao.getLargura());
                this.save(campoApresentacao);
            }
        }
    }
    
    /**
     * Deleta os campos de apresentação associados ao campo de formulário
     * @param camposApresentacao
     */
    public void deletaCamposApresentacao(Set<CampoApresentacao> camposApresentacao) {
        if(Objects.nonNull(camposApresentacao)) {
            for(CampoApresentacao campoApresentacao : camposApresentacao) {
                this.delete(campoApresentacao);
            }
        }
    }
}
