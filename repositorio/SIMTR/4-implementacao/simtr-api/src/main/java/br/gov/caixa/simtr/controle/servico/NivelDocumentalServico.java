package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.helper.NivelDocumentalComposicaoHelper;
import br.gov.caixa.simtr.modelo.entidade.ComposicaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.RegraDocumental;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRTEC,
    ConstantesUtil.PERFIL_MTRAUD,
    ConstantesUtil.PERFIL_MTRDOSINT,
    ConstantesUtil.PERFIL_MTRDOSMTZ,
    ConstantesUtil.PERFIL_MTRDOSOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class NivelDocumentalServico {

    @Inject
    private EntityManager entityManager;

    @EJB
    private ComposicaoDocumentalServico composicaoDocumentalServico;
    
    @EJB
    private DocumentoServico documentoServico;

    @EJB
    private DossieClienteServico dossieClienteServico;
    
    @EJB
    private NivelDocumentalComposicaoHelper nivelDocumentalComposicaoHelper;

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public DossieCliente atualizaNiveisDocumentaisCliente(Long id) {
        DossieCliente dossieCliente = this.dossieClienteServico.getById(id, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
        if (dossieCliente == null) {
            throw new SimtrRequisicaoException(MessageFormat.format("NDS.aNDC.001 - Dossiê de Cliente não localizado sob o identificador informado. ID = {0}", id));
        }

        Map<Documento, String> documentosDefinitivos = this.documentoServico.listDocumentosDefinitivosDossieDigital(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa());

        return this.atualizaNiveisDocumentaisCliente(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), new ArrayList<>(documentosDefinitivos.keySet()));
    }
    
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public DossieCliente atualizaNiveisDocumentaisCliente(final Long cpfCnpj, final TipoPessoaEnum tipoPessoaEnum) {
        DossieCliente dossieCliente = this.dossieClienteServico.getByCpfCnpj(cpfCnpj, tipoPessoaEnum, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
        if (dossieCliente == null) {
            throw new SimtrRequisicaoException(MessageFormat.format("NDS.aNDC.001 - Dossiê de Cliente não localizado sob o identificador informado. CPF/CNPJ = {0} | Tipo Pessoa = {1}", String.valueOf(cpfCnpj), tipoPessoaEnum.name()));
        }

        Map<Documento, String> documentosDefinitivos = this.documentoServico.listDocumentosDefinitivosDossieDigital(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa());

        return this.atualizaNiveisDocumentaisCliente(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), new ArrayList<>(documentosDefinitivos.keySet()));
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public DossieCliente atualizaNiveisDocumentaisCliente(final Long cpfCnpj, final TipoPessoaEnum tipoPessoaEnum, List<Documento> documentosCliente) {

        List<ComposicaoDocumental> composicoesDocumentais = this.composicaoDocumentalServico.list(Boolean.FALSE, Boolean.FALSE, tipoPessoaEnum);

        List<ComposicaoDocumental> composicoesAtendidas = new ArrayList<>();

        // Percorre todas as composições localizadas para o produto solicitado
        composicoesDocumentais.forEach(composicaoDocumental -> {
            List<RegraDocumental> regrasNaoAtendidas = this.nivelDocumentalComposicaoHelper.analisaComposicaoAtendida(composicaoDocumental, documentosCliente);
            if (regrasNaoAtendidas.isEmpty()) {
                composicoesAtendidas.add(composicaoDocumental);
            }
        });

        DossieCliente dossieCliente = this.getDossieClienteComComposicoesDocumentais(cpfCnpj, tipoPessoaEnum);
        dossieCliente.getComposicoesDocumentais().clear();
        dossieCliente.addComposicoesDocumentais(composicoesAtendidas.toArray(new ComposicaoDocumental[composicoesAtendidas.size()]));
        dossieCliente.setDataHoraApuracaoNivel(Calendar.getInstance());
        return dossieCliente;
    }
    
    

    /**
     * Busca Dossie Cliente com suas composições documentais sob o contexto transacional para poder ser alterado pelo JPA
     *
     * @param cpfCnpj - CPF/CNPJ de identificação do dossiê de cliente
     * @return Dossiê de cliente carregado com as composções vinculadas
     */
    private DossieCliente getDossieClienteComComposicoesDocumentais(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT dc FROM DossieCliente dc ");
        jpql.append(" LEFT JOIN FETCH dc.composicoesDocumentais cd ");
        jpql.append(" WHERE dc.cpfCnpj = :cpfCnpj ");
        jpql.append(" AND dc.tipoPessoa = :tipoPessoa ");

        TypedQuery<DossieCliente> query = this.entityManager.createQuery(jpql.toString(), DossieCliente.class);
        query.setParameter("cpfCnpj", cpfCnpj);
        query.setParameter("tipoPessoa", tipoPessoaEnum);
        return query.getSingleResult();
    }
}
