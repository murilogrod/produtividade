package br.gov.caixa.simtr.controle.servico;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.controle.excecao.SimtrCadastroException;
import br.gov.caixa.simtr.controle.servico.helper.CadastroHelper;
import br.gov.caixa.simtr.controle.vo.excecao.PendenciasVO;
import br.gov.caixa.simtr.modelo.entidade.CampoEntrada;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroDefinicaoCampoFormulario;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario.AlteracaoCadastroDefinicaoCampoFormularioDTO;
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
public class CampoEntradaServico extends AbstractService<CampoEntrada, Long> {
    
    @Inject
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }
    
    @EJB
    private CadastroHelper cadastroHelper;
    
    /**
     * Valida se os valores do campo de entrada estão conforme as regras estabelecidas para inclusão.
     * @param campoEntrada
     */
    public void validaInclusaoCampoEntrada(CampoEntrada campoEntrada) {
        Map<String, List<String>> mapaPendencias = new HashMap<>();
        
        if(Objects.isNull(campoEntrada.getTipo())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.TIPO_CAMPO, "O tipo de campo deve ser informado.");
        }
        
        if(Objects.isNull(campoEntrada.getChave())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.CHAVE, "Indicador de campo chave deve ser informado.");
        }
        
        if(Objects.isNull(campoEntrada.getLabel())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.LABEL, "O nome do label deve ser informado.");
        }else if(campoEntrada.getLabel().trim().length() > 50 ) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.LABEL, "O nome do label não deve ser superior a 50 caracteres.");
        }
        
        if(Objects.nonNull(campoEntrada.getMascara()) && campoEntrada.getMascara().trim().length() > 100) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.MASCARA, "A definição da máscara não deve ser superior a 100 caracteres.");
        }
        
        if(Objects.nonNull(campoEntrada.getPlaceholder()) && campoEntrada.getPlaceholder().trim().length() > 100) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.PLACEHOLDER, "A definição do placeholder não deve ser superior a 100 caracteres.");
        }
        
        if(Objects.nonNull(campoEntrada.getTamanhoMinimo()) && campoEntrada.getTamanhoMinimo() < 1) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.TAMANHO_MINIMO, "O valor informado para o tamanho mínimo do campo deve ser um valor inteiro positivo.");
        }
        
        if(Objects.nonNull(campoEntrada.getTamanhoMaximo()) && campoEntrada.getTamanhoMaximo() < 1) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.TAMANHO_MAXIMO, "O valor informado para o tamanho máximo do campo deve ser um valor inteiro positivo.");
        }
        
        if(Objects.nonNull(campoEntrada.getTamanhoMinimo()) 
                && Objects.isNull(campoEntrada.getTamanhoMaximo()) 
                || (Objects.nonNull(campoEntrada.getTamanhoMinimo())
                && Objects.nonNull(campoEntrada.getTamanhoMaximo())
                && campoEntrada.getTamanhoMinimo() >= campoEntrada.getTamanhoMaximo())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.TAMANHO_MINIMO, "O valor informado para o tamanho mínimo do campo não pode ser igual e nem superior ao valor informado para o tamanho máximo.");
        }
        
        List<PendenciasVO> listaPendencias = mapaPendencias.entrySet().stream().map(registro -> new PendenciasVO(registro.getKey(), registro.getValue()))
                .collect(Collectors.toList());
        
        if (!listaPendencias.isEmpty()) {
            throw new SimtrCadastroException("CES.vICE.001 - Problemas identificados na execução da inclusão de um campo de entrada.", listaPendencias);
        }
    }
    
    /**
     * Valida se os valores do campo de entrada estão conforme as regras estabelecidas para alteração.
     * @param campoEntrada
     */
    public void validaAlteracaoCampoEntrada(CampoEntrada campoEntrada, AlteracaoCadastroDefinicaoCampoFormularioDTO alteracaoCadasdroCampoFormularioDTO) {
        Map<String, List<String>> mapaPendencias = new HashMap<>();
        
        if(Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getTipoCampo())) {
            campoEntrada.setTipo(alteracaoCadasdroCampoFormularioDTO.getTipoCampo());
        }
        
        if(Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getChave())) {
            campoEntrada.setChave(alteracaoCadasdroCampoFormularioDTO.getChave());
        }
        
        if(Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getLabel()) && campoEntrada.getLabel().trim().length() > 50) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.LABEL, "O nome do label não deve ser superior a 50 caracteres.");
        }else if(Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getLabel()) && campoEntrada.getLabel().trim().length() <= 50) {
            campoEntrada.setLabel(alteracaoCadasdroCampoFormularioDTO.getLabel());
        }
        
        if(Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getMascara()) && alteracaoCadasdroCampoFormularioDTO.getMascara().trim().length() > 100) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.MASCARA, "A definição da máscara não deve ser superior a 100 caracteres.");
        } else if(Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getMascara()) && alteracaoCadasdroCampoFormularioDTO.getMascara().trim().length() <= 100 && !alteracaoCadasdroCampoFormularioDTO.getMascara().isEmpty()) {
            campoEntrada.setMascara(alteracaoCadasdroCampoFormularioDTO.getMascara());
        } else if(Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getMascara()) && alteracaoCadasdroCampoFormularioDTO.getMascara().isEmpty()) {
            campoEntrada.setMascara(null);
        }
        
        if(Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getPlaceholder()) && alteracaoCadasdroCampoFormularioDTO.getPlaceholder().trim().length() > 100) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.PLACEHOLDER, "A definição do placeholder não deve ser superior a 100 caracteres.");
        } else if(Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getPlaceholder()) && alteracaoCadasdroCampoFormularioDTO.getPlaceholder().trim().length() <= 100 && !alteracaoCadasdroCampoFormularioDTO.getPlaceholder().isEmpty()) {
            campoEntrada.setPlaceholder(alteracaoCadasdroCampoFormularioDTO.getPlaceholder());
        } else if(Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getPlaceholder()) && alteracaoCadasdroCampoFormularioDTO.getPlaceholder().isEmpty()) {
            campoEntrada.setPlaceholder(null);
        }
        
        if(Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getTamanhoMinimo()) && alteracaoCadasdroCampoFormularioDTO.getTamanhoMinimo() < 1) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.TAMANHO_MINIMO, "O valor informado para o tamanho mínimo do campo deve ser um valor inteiro positivo.");
        } else if(Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getTamanhoMinimo()) && alteracaoCadasdroCampoFormularioDTO.getTamanhoMinimo() >= 1) {
            campoEntrada.setTamanhoMinimo(alteracaoCadasdroCampoFormularioDTO.getTamanhoMinimo());
        }
        
        if(Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getTamanhoMaximo()) && alteracaoCadasdroCampoFormularioDTO.getTamanhoMaximo() < 1) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.TAMANHO_MAXIMO, "O valor informado para o tamanho máximo do campo deve ser um valor inteiro positivo.");
        } else if(Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getTamanhoMaximo()) && alteracaoCadasdroCampoFormularioDTO.getTamanhoMaximo() >= 1) {
            campoEntrada.setTamanhoMaximo(alteracaoCadasdroCampoFormularioDTO.getTamanhoMaximo());
        }
        
        if(Objects.nonNull(campoEntrada.getTamanhoMinimo()) 
                && Objects.isNull(campoEntrada.getTamanhoMaximo()) 
                || (Objects.nonNull(campoEntrada.getTamanhoMinimo())
                && Objects.nonNull(campoEntrada.getTamanhoMaximo())
                && campoEntrada.getTamanhoMinimo() >= campoEntrada.getTamanhoMaximo())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroDefinicaoCampoFormulario.TAMANHO_MINIMO, "O valor informado para o tamanho mínimo do campo não pode ser igual e nem superior ao valor informado para o tamanho máximo.");
        }
        
        List<PendenciasVO> listaPendencias = mapaPendencias.entrySet().stream().map(registro -> new PendenciasVO(registro.getKey(), registro.getValue()))
                .collect(Collectors.toList());
        
        if (!listaPendencias.isEmpty()) {
            throw new SimtrCadastroException("CES.vACE.001 - Problemas identificados na execução da alteração de um campo de entrada.", listaPendencias);
        }
    }
}