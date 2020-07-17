package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.util.UtilData;
import br.gov.caixa.simtr.controle.excecao.SimtrCadastroException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.helper.CadastroHelper;
import br.gov.caixa.simtr.controle.servico.helper.VinculacaoChecklistHelper;
import br.gov.caixa.simtr.controle.vo.ChecklistVO;
import br.gov.caixa.simtr.controle.vo.excecao.PendenciasVO;
import br.gov.caixa.simtr.controle.vo.vinculacaochecklist.VinculacaoChecklistValidadaVO;
import br.gov.caixa.simtr.modelo.entidade.Checklist;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.entidade.VinculacaoChecklist;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesCadastroVinculacaoChecklist;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.checklist.CadastroVinculacaoChecklistDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class VinculacaoChecklistServico extends AbstractService<VinculacaoChecklist, Long> {

    @Inject
    private EntityManager entityManager;

    @EJB
    private ProcessoServico processoServico;
    
    @EJB
    private TipoDocumentoServico tipoDocumentoServico;
    
    @EJB
    private FuncaoDocumentalServico funcaoDocumentalServico;

    @EJB
    private ChecklistServico checkListServico;
    
    @EJB
    private VinculacaoChecklistHelper vinculacaoChecklistHelper;
    
    @EJB
    private CadastroHelper cadastroHelper;
    
    private static final Logger LOGGER = Logger.getLogger(VinculacaoChecklistServico.class.getName());

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * 
     * @param vinculacaoChecklist
     * @param cadastroVinculacaoChecklistDTO
     */
    public VinculacaoChecklist montaNovaVinculacaoChecklist(CadastroVinculacaoChecklistDTO cadastroVinculacaoChecklistDTO) {
        VinculacaoChecklistValidadaVO vinculacaoChecklistValidadaVO = this.validaInclusaoVinculacaoChecklist(cadastroVinculacaoChecklistDTO);
        
        if(Objects.nonNull(vinculacaoChecklistValidadaVO.getVinculacaoChecklistAnterior()) 
                && Objects.nonNull(cadastroVinculacaoChecklistDTO.getDataAtivacaoAtual())){
            vinculacaoChecklistValidadaVO.getVinculacaoChecklistAnterior().setDataRevogacao(cadastroVinculacaoChecklistDTO.getDataAtivacaoAtual());
            this.update(vinculacaoChecklistValidadaVO.getVinculacaoChecklistAnterior());
        }
        
        VinculacaoChecklist novaVinculacaoChecklist = new VinculacaoChecklist();
        novaVinculacaoChecklist.setProcessoDossie(vinculacaoChecklistValidadaVO.getProcessoDossie());
        novaVinculacaoChecklist.setProcessoFase(vinculacaoChecklistValidadaVO.getProcessoFase());
        novaVinculacaoChecklist.setTipoDocumento(vinculacaoChecklistValidadaVO.getTipoDocumento());
        novaVinculacaoChecklist.setFuncaoDocumental(vinculacaoChecklistValidadaVO.getFuncaoDocumental());
        novaVinculacaoChecklist.setChecklist(vinculacaoChecklistValidadaVO.getChecklist());
        Calendar dataFutura = Calendar.getInstance();
        dataFutura.setTime(UtilData.gerarData(1, 0, 9999).getTime());
        novaVinculacaoChecklist.setDataRevogacao(dataFutura);
        this.save(novaVinculacaoChecklist);
        return novaVinculacaoChecklist;
    }

    /**
     * 
     * @param cadastroVinculacaoChecklistDTO
     */
    private VinculacaoChecklistValidadaVO validaInclusaoVinculacaoChecklist(CadastroVinculacaoChecklistDTO cadastroVinculacaoChecklistDTO) {
        Map<String, List<String>> mapaPendencias = new HashMap<>();
        
        Processo processoDossie = this.processoServico.getById(cadastroVinculacaoChecklistDTO.getIdentificadorProcessoDossie());
        if (Objects.isNull(processoDossie)) {
            String mensagemCampo = "VCS.vIVC.001 Processo Dossiê não localizado pelo identificador informado.";
            this.vinculacaoChecklistHelper.montaExcecaoRequisicaoVinculacaoChecklist(mensagemCampo, cadastroVinculacaoChecklistDTO);
        }
        
        Processo processoFase = this.processoServico.getById(cadastroVinculacaoChecklistDTO.getIdentificadorProcessoFase());
        if (Objects.isNull(processoFase)) {
            String mensagemCampo = "VCS.vIVC.002 Processo Fase não localizado pelo identificador informado.";
            this.vinculacaoChecklistHelper.montaExcecaoRequisicaoVinculacaoChecklist(mensagemCampo, cadastroVinculacaoChecklistDTO);
        }
        
        if(Objects.nonNull(cadastroVinculacaoChecklistDTO.getIdentificadorTipoDocumento()) && 
                Objects.nonNull(cadastroVinculacaoChecklistDTO.getIdentificadorFuncaoDocumental())) {
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroVinculacaoChecklist.ID_TIPO_DOCUMENTO, this.vinculacaoChecklistHelper.montaMensagemExcecaoSimtrCadastroVinculacaoChecklist("Para vinculações documentais apenas o tipo ou função documental devem ser informadas.", cadastroVinculacaoChecklistDTO));
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroVinculacaoChecklist.ID_FUNCAO_DOCUMENTAO, this.vinculacaoChecklistHelper.montaMensagemExcecaoSimtrCadastroVinculacaoChecklist("Para vinculações documentais apenas o tipo ou função documental devem ser informadas.", cadastroVinculacaoChecklistDTO));
        }
        
        TipoDocumento tipoDocumento = null;
        if(Objects.nonNull(cadastroVinculacaoChecklistDTO.getIdentificadorTipoDocumento())){
            tipoDocumento = this.tipoDocumentoServico.getById(cadastroVinculacaoChecklistDTO.getIdentificadorTipoDocumento());
            if (Objects.isNull(tipoDocumento)) {
                String mensagemCampo = "VCS.vIVC.003 Tipo de Documento não localizado pelo identificador informado.";
                this.vinculacaoChecklistHelper.montaExcecaoRequisicaoVinculacaoChecklist(mensagemCampo, cadastroVinculacaoChecklistDTO);
            }
        }
        
        FuncaoDocumental funcaoDocumental = null;
        if(Objects.nonNull(cadastroVinculacaoChecklistDTO.getIdentificadorFuncaoDocumental())) {
            funcaoDocumental = this.funcaoDocumentalServico.getById(cadastroVinculacaoChecklistDTO.getIdentificadorFuncaoDocumental());
            if (Objects.isNull(funcaoDocumental)) {
                String mensagemCampo = "VCS.vIVC.004 Função Documental não localizada pelo identificador informado.";
                this.vinculacaoChecklistHelper.montaExcecaoRequisicaoVinculacaoChecklist(mensagemCampo, cadastroVinculacaoChecklistDTO);
            }
        }
        
        Checklist checklist = this.checkListServico.getById(cadastroVinculacaoChecklistDTO.getIdentificadorChecklist());
        if (Objects.isNull(checklist)) {
            String mensagemCampo = "VCS.vIVC.005 Checklist não localizado pelo identificador informado";
            this.vinculacaoChecklistHelper.montaExcecaoRequisicaoVinculacaoChecklist(mensagemCampo, cadastroVinculacaoChecklistDTO);
        }
        
        if(Objects.nonNull(checklist.getDataHoraInativacao())){
            this.vinculacaoChecklistHelper.montaExcecaoEstadoImpeditivoVinculacaoChecklist(cadastroVinculacaoChecklistDTO);
        }
        
        if(Objects.nonNull(cadastroVinculacaoChecklistDTO.getDataAtivacaoAtual()) 
                && cadastroVinculacaoChecklistDTO.getDataAtivacaoAtual().before(Calendar.getInstance())) {
            String mensagemCampo = "VCS.vIVC.006 - A data de ativação do registro a ser inserido, não pode ser inferior a data atual.";
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroVinculacaoChecklist.DATA_ATIVACAO, 
                    this.vinculacaoChecklistHelper.montaMensagemExcecaoSimtrCadastroVinculacaoChecklist(mensagemCampo, cadastroVinculacaoChecklistDTO));
        }
        
        VinculacaoChecklist vinculacaoChecklistAnterior = this.consultaVinculacaoChecklistAnterior(cadastroVinculacaoChecklistDTO);
        
        if(Objects.nonNull(cadastroVinculacaoChecklistDTO.getDataAtivacaoAtual()) 
                && Objects.isNull((vinculacaoChecklistAnterior))) {
            String mensagemCampo = "VCS.vIVC.007 - Somente deverá ser fornecida data de ativação quando houver vinculação de checklist anterior para o mesmo processo, fase e tipo documental/função documental.";
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroVinculacaoChecklist.DATA_ATIVACAO,
                    this.vinculacaoChecklistHelper.montaMensagemExcecaoSimtrCadastroVinculacaoChecklist(mensagemCampo, cadastroVinculacaoChecklistDTO));
        }
        
        if(Objects.nonNull(vinculacaoChecklistAnterior) 
                && Objects.isNull(cadastroVinculacaoChecklistDTO.getDataAtivacaoAtual())) {
            String mensagemCampo = "VCS.vIVC.008 - Existe vinculação equivalente anterior, então é obrigatório definir a data de ativação desta nova vinculação."; 
            this.cadastroHelper.incluiPendenciaMapa(mapaPendencias, ConstantesCadastroVinculacaoChecklist.DATA_ATIVACAO, 
                    this.vinculacaoChecklistHelper.montaMensagemExcecaoSimtrCadastroVinculacaoChecklist(mensagemCampo, cadastroVinculacaoChecklistDTO));
        }
        
        List<PendenciasVO> listaPendencias = mapaPendencias.entrySet().stream().map(registro -> new PendenciasVO(registro.getKey(), registro.getValue()))
                .collect(Collectors.toList());
        
        if (!listaPendencias.isEmpty()) {
            throw new SimtrCadastroException("VCS.vIVC.001 - Problemas identificados na execução da inclusão de uma vinculação de checklist.", listaPendencias);
        }
        return new VinculacaoChecklistValidadaVO(processoDossie, processoFase, tipoDocumento, funcaoDocumental, checklist, vinculacaoChecklistAnterior);
    }
    
    /**
     * 
     * @param cadastroVinculacaoChecklistDTO
     * @return
     */
    private VinculacaoChecklist consultaVinculacaoChecklistAnterior(CadastroVinculacaoChecklistDTO cadastroVinculacaoChecklistDTO) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT vc FROM VinculacaoChecklist vc");
        jpql.append(" WHERE vc.id = (SELECT MAX(vcsub.id) FROM VinculacaoChecklist vcsub");
        jpql.append("               WHERE vcsub.processoDossie.id = :idProcessoDossie");
        jpql.append("               AND vcsub.processoFase.id = :idProcessoFase");
        jpql.append("               AND vcsub.checklist.id != :identificadorChecklist");
        if(Objects.nonNull(cadastroVinculacaoChecklistDTO.getIdentificadorTipoDocumento())) {
            jpql.append("           AND vcsub.tipoDocumento.id = :idTipoDoc)");
        } else {
            jpql.append("           AND vcsub.funcaoDocumental.id = :idFuncaoDoc)");
        }
        
        TypedQuery<VinculacaoChecklist> query = this.entityManager.createQuery(jpql.toString(), VinculacaoChecklist.class);
        query.setParameter("idProcessoDossie", cadastroVinculacaoChecklistDTO.getIdentificadorProcessoDossie());
        query.setParameter("idProcessoFase", cadastroVinculacaoChecklistDTO.getIdentificadorProcessoFase());
        query.setParameter("identificadorChecklist", cadastroVinculacaoChecklistDTO.getIdentificadorChecklist());
        if(Objects.nonNull(cadastroVinculacaoChecklistDTO.getIdentificadorTipoDocumento())) {
            query.setParameter("idTipoDoc", cadastroVinculacaoChecklistDTO.getIdentificadorTipoDocumento());
        } else {
            query.setParameter("idFuncaoDoc", cadastroVinculacaoChecklistDTO.getIdentificadorFuncaoDocumental());
        }
        
        try {
            return query.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }
    }

    /**
     * Percorre a lista de vinculações e define a data atual do sistema como forma de exclusão para cada vinculação de checklist cadastrada.
     * 
     * @param vinculacoes
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void excluiVinculacoesChecklistLogicamente(Set<VinculacaoChecklist> vinculacoes) {
        if (Objects.nonNull(vinculacoes)) {
            vinculacoes.forEach(vinculacao -> {
                vinculacao.setDataRevogacao(Calendar.getInstance());
                this.update(vinculacao);
            });
        }
    }

    /**
     * Percorre a lista de vinculações e exclui fisicamente todas as vinculações relacionadas ao checklist.
     * 
     * @param vinculacoes
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void excluiVinculacoesChecklistFisicamente(Set<VinculacaoChecklist> vinculacoes) {
        if (Objects.nonNull(vinculacoes)) {
            for (VinculacaoChecklist vinculacao : vinculacoes) {
                this.getEntityManager().remove(this.getEntityManager().merge(vinculacao));
            }
        }
    }

    /**
     * Exclui vinculações checklist fisicamente ou logicamente.
     * 
     * @param vinculacoes
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM
    })
    public void excluirVinculacaoCheckList(Long id) {
        VinculacaoChecklist vinculacaoCheckList = this.getById(id);

        if (Objects.isNull(vinculacaoCheckList)) {
            String mensagem = MessageFormat.format("VCS.eVC.001 - Vinculação de Checklist não localizada sob o identificador informado: {0}", id);
            throw new SimtrRequisicaoException(mensagem);
        }

        ChecklistVO checkListVO = this.checkListServico.buscaContagemAssociacoesChecklist(vinculacaoCheckList.getChecklist().getId());

        if (Objects.isNull(checkListVO)) {
            String mensagem = MessageFormat.format("VCS.eVC.001 - Não é possivel localizar Checklist com ID {0}", String.valueOf(vinculacaoCheckList.getChecklist().getId()));
            throw new SimtrRequisicaoException(mensagem);
        }

        Set<VinculacaoChecklist> vinculacoesCheckList = new HashSet<>();
        vinculacoesCheckList.add(vinculacaoCheckList);

        if (checkListVO.getQuantidadeAssociacoes() > 0) {
            this.excluiVinculacoesChecklistLogicamente(vinculacoesCheckList);
        } else {
            this.delete(vinculacaoCheckList);
        }
    }
}