package br.gov.caixa.simtr.controle.servico.helper;

import java.text.MessageFormat;
import java.util.Objects;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.util.UtilData;
import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist.CadastroVinculacaoChecklistDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class VinculacaoChecklistHelper {

   /**
    * 
    * @param mensagemCampo
    * @param cadastroVinculacaoChecklistDTO
    */
    public void montaExcecaoRequisicaoVinculacaoChecklist(String mensagemCampo, CadastroVinculacaoChecklistDTO cadastroVinculacaoChecklistDTO) {
        String mensagem = MessageFormat.format(mensagemCampo + "Registro de vinculação inválido com elemento não localizado sob o identificador informado. "
                + "Processo: {0} | Fase: {1} | Tipo de Documento: {2} | Função Documental: {3} | Checklist: {4} | Data Ativação: {5}",
                cadastroVinculacaoChecklistDTO.getIdentificadorProcessoDossie().toString(), 
                cadastroVinculacaoChecklistDTO.getIdentificadorProcessoFase().toString(), 
                Objects.nonNull(cadastroVinculacaoChecklistDTO.getIdentificadorTipoDocumento()) ? cadastroVinculacaoChecklistDTO.getIdentificadorTipoDocumento().toString() : null, 
                Objects.nonNull(cadastroVinculacaoChecklistDTO.getIdentificadorFuncaoDocumental()) ? cadastroVinculacaoChecklistDTO.getIdentificadorFuncaoDocumental().toString() : null,
                cadastroVinculacaoChecklistDTO.getIdentificadorChecklist().toString(),
                Objects.nonNull(cadastroVinculacaoChecklistDTO.getDataAtivacaoAtual()) ? UtilData.converterDateParaStringAnoMesDia(cadastroVinculacaoChecklistDTO.getDataAtivacaoAtual().getTime()) : null);
        throw new SimtrRequisicaoException(mensagem);
    }
    
    /**
     * 
     * @param cadastroVinculacaoChecklistDTO
     */
    public void montaExcecaoEstadoImpeditivoVinculacaoChecklist(CadastroVinculacaoChecklistDTO cadastroVinculacaoChecklistDTO) {
        String mensagem = MessageFormat.format("VCH.mEEIVC.001 - Checklist informado já foi inativado e não pode mais ser vinculado a nenhuma operação. "
                + "Processo: {0} | Fase: {1} | Tipo de Documento: {2} | Função Documental: {3} | Checklist: {4} | Data Ativação: {5}",
                cadastroVinculacaoChecklistDTO.getIdentificadorProcessoDossie().toString(), 
                cadastroVinculacaoChecklistDTO.getIdentificadorProcessoFase().toString(), 
                Objects.nonNull(cadastroVinculacaoChecklistDTO.getIdentificadorTipoDocumento()) ? cadastroVinculacaoChecklistDTO.getIdentificadorTipoDocumento().toString() : null, 
                Objects.nonNull(cadastroVinculacaoChecklistDTO.getIdentificadorFuncaoDocumental()) ? cadastroVinculacaoChecklistDTO.getIdentificadorFuncaoDocumental().toString() : null,
                cadastroVinculacaoChecklistDTO.getIdentificadorChecklist().toString(),
                Objects.nonNull(cadastroVinculacaoChecklistDTO.getDataAtivacaoAtual()) ? UtilData.converterDateParaStringAnoMesDia(cadastroVinculacaoChecklistDTO.getDataAtivacaoAtual().getTime()) : null);
        throw new SimtrEstadoImpeditivoException(mensagem);
    }
    
     /**
      * 
      * @param mensagemCampo
      * @param cadastroVinculacaoChecklistDTO
      * @return
      */
     public String montaMensagemExcecaoSimtrCadastroVinculacaoChecklist(String mensagemCampo, CadastroVinculacaoChecklistDTO cadastroVinculacaoChecklistDTO) {
         return MessageFormat.format(mensagemCampo 
                 + " Processo: {0} | Fase: {1} | Tipo de Documento: {2} | Função Documental: {3} | Checklist: {4} | Data Ativação: {5}",
                 cadastroVinculacaoChecklistDTO.getIdentificadorProcessoDossie().toString(), 
                 cadastroVinculacaoChecklistDTO.getIdentificadorProcessoFase().toString(), 
                 Objects.nonNull(cadastroVinculacaoChecklistDTO.getIdentificadorTipoDocumento()) ? cadastroVinculacaoChecklistDTO.getIdentificadorTipoDocumento().toString() : null, 
                 Objects.nonNull(cadastroVinculacaoChecklistDTO.getIdentificadorFuncaoDocumental()) ? cadastroVinculacaoChecklistDTO.getIdentificadorFuncaoDocumental().toString() : null,
                 cadastroVinculacaoChecklistDTO.getIdentificadorChecklist().toString(),
                 Objects.nonNull(cadastroVinculacaoChecklistDTO.getDataAtivacaoAtual()) ? UtilData.converterDateParaStringAnoMesDia(cadastroVinculacaoChecklistDTO.getDataAtivacaoAtual().getTime()) : null);
     }
}
