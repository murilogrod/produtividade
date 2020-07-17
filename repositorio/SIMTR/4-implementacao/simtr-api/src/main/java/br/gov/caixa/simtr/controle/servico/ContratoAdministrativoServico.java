package br.gov.caixa.simtr.controle.servico;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.StreamingOutput;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.simtr.controle.excecao.SiecmException;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.ApensoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.ContratoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.ProcessoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoApensoEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRAUD,
    ConstantesUtil.PERFIL_MTRTEC,
    ConstantesUtil.PERFIL_MTRPAEMTZ,
    ConstantesUtil.PERFIL_MTRPAEOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class ContratoAdministrativoServico extends AbstractService<ContratoAdministrativo, Long> {

    private static final Logger LOGGER = Logger.getLogger(ContratoAdministrativoServico.class.getName());

    @EJB
    private CanalServico canalServico;

    @EJB
    private DocumentoServico documentoServico;

    @EJB
    private DocumentoAdministrativoServico documentoAdministrativoServico;

    @EJB
    private KeycloakService keycloakService;

    @EJB
    private ProcessoAdministrativoServico processoAdministrativoServico;

    @Inject
    private RelatorioServico relatorioServico;

    @EJB
    private TipoDocumentoServico tipoDocumentoServico;

    @Inject
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE
    })
    @Override
    public void save(ContratoAdministrativo contratoAdministrativo) {
        contratoAdministrativo.setMatriculaInclusao(this.keycloakService.getMatricula());
        super.save(contratoAdministrativo);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ContratoAdministrativo getById(Long idContrato, boolean vinculacaoProcesso, boolean vinculacaoApensosProcesso, boolean vinculacaoApensosDiretos) {
        ContratoAdministrativo contratoAdministrativoRetorno;
        ContratoAdministrativo contratoAdministrativoTemp;

        String jpqlBase = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        jpqlBase = jpqlBase.concat(" WHERE ca.id = :id ");
        TypedQuery<ContratoAdministrativo> queryContrato = this.entityManager.createQuery(jpqlBase, ContratoAdministrativo.class);
        queryContrato.setParameter("id", idContrato);
        try {
            contratoAdministrativoRetorno = queryContrato.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }

        if (vinculacaoProcesso) {
            String jpql = this.getQuery(Boolean.TRUE, vinculacaoApensosProcesso, Boolean.FALSE);
            jpql = jpql.concat(" WHERE ca.id = :id ");
            TypedQuery<ContratoAdministrativo> query = this.entityManager.createQuery(jpql, ContratoAdministrativo.class);
            query.setParameter("id", idContrato);

            contratoAdministrativoTemp = query.getSingleResult();
            contratoAdministrativoRetorno.setProcessoAdministrativo(contratoAdministrativoTemp.getProcessoAdministrativo());
        }

        if (vinculacaoApensosDiretos) {
            String jpql = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
            jpql = jpql.concat(" WHERE ca.id = :id ");
            TypedQuery<ContratoAdministrativo> query = this.entityManager.createQuery(jpql, ContratoAdministrativo.class);
            query.setParameter("id", idContrato);

            contratoAdministrativoTemp = query.getSingleResult();
            contratoAdministrativoRetorno.setApensosAdministrativos(contratoAdministrativoTemp.getApensosAdministrativos());
        }

        return contratoAdministrativoRetorno;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ContratoAdministrativo getByNumeroAno(Integer numeroContrato, Integer anoContrato, boolean vinculacaoProcesso, boolean vinculacaoApensosProcesso, boolean vinculacaoApensosDiretos) {
        ContratoAdministrativo contratoAdministrativoRetorno;
        ContratoAdministrativo contratoAdministrativoTemp;

        String jpqlBase = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        jpqlBase = jpqlBase.concat(" WHERE ca.numeroContrato = :numeroContrato AND ca.anoContrato = :anoContrato ");
        TypedQuery<ContratoAdministrativo> queryContrato = this.entityManager.createQuery(jpqlBase, ContratoAdministrativo.class);
        queryContrato.setParameter("numeroContrato", numeroContrato);
        queryContrato.setParameter("anoContrato", anoContrato);
        try {
            contratoAdministrativoRetorno = queryContrato.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }

        if (vinculacaoProcesso) {
            String jpql = this.getQuery(Boolean.TRUE, vinculacaoApensosProcesso, Boolean.FALSE);
            jpql = jpql.concat(" WHERE ca.numeroContrato = :numeroContrato AND ca.anoContrato = :anoContrato ");
            TypedQuery<ContratoAdministrativo> query = this.entityManager.createQuery(jpql, ContratoAdministrativo.class);
            query.setParameter("numeroContrato", numeroContrato);
            query.setParameter("anoContrato", anoContrato);

            contratoAdministrativoTemp = query.getSingleResult();
            contratoAdministrativoRetorno.setProcessoAdministrativo(contratoAdministrativoTemp.getProcessoAdministrativo());
        }

        if (vinculacaoApensosDiretos) {
            String jpql = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
            jpql = jpql.concat(" WHERE ca.numeroContrato = :numeroContrato AND ca.anoContrato = :anoContrato ");
            TypedQuery<ContratoAdministrativo> query = this.entityManager.createQuery(jpql, ContratoAdministrativo.class);
            query.setParameter("numeroContrato", numeroContrato);
            query.setParameter("anoContrato", anoContrato);

            contratoAdministrativoTemp = query.getSingleResult();
            contratoAdministrativoRetorno.setApensosAdministrativos(contratoAdministrativoTemp.getApensosAdministrativos());
        }

        return contratoAdministrativoRetorno;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<ContratoAdministrativo> listByDescricao(String descricaoContrato, boolean vinculacaoProcesso, boolean vinculacaoApensosProcesso, boolean vinculacaoApensosDiretos) {
        List<ContratoAdministrativo> contratoAdministrativoRetorno;
        List<ContratoAdministrativo> contratoAdministrativoTemp;

        String jpqlBase = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        jpqlBase = jpqlBase.concat(" WHERE UPPER( ca.descricaoContrato ) LIKE CONCAT( '%', :descricaoContrato, '%' ) ");
        TypedQuery<ContratoAdministrativo> queryContrato = this.entityManager.createQuery(jpqlBase, ContratoAdministrativo.class);
        queryContrato.setParameter("descricaoContrato", descricaoContrato.toUpperCase());
        try {
            contratoAdministrativoRetorno = queryContrato.getResultList();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return new ArrayList<>();
        }

        if (vinculacaoProcesso) {
            String jpql = this.getQuery(Boolean.TRUE, vinculacaoApensosProcesso, Boolean.FALSE);
            jpql = jpql.concat(" WHERE UPPER( ca.descricaoContrato ) LIKE CONCAT( '%', :descricaoContrato, '%' ) ");
            TypedQuery<ContratoAdministrativo> query = this.entityManager.createQuery(jpql, ContratoAdministrativo.class);
            query.setParameter("descricaoContrato", descricaoContrato.toUpperCase());

            contratoAdministrativoTemp = query.getResultList();
            for (ContratoAdministrativo contratoTemp : contratoAdministrativoTemp) {
                contratoAdministrativoRetorno.stream()
                                             .filter(ca -> ca.getId().equals(contratoTemp.getId()))
                                             .forEach(ca -> ca.setProcessoAdministrativo(contratoTemp.getProcessoAdministrativo()));
            }
        }

        if (vinculacaoApensosDiretos) {
            String jpql = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
            jpql = jpql.concat(" WHERE UPPER( ca.descricaoContrato ) LIKE CONCAT( '%', :descricaoContrato, '%' ) ");
            TypedQuery<ContratoAdministrativo> query = this.entityManager.createQuery(jpql, ContratoAdministrativo.class);
            query.setParameter("descricaoContrato", descricaoContrato.toUpperCase());

            contratoAdministrativoTemp = query.getResultList();
            for (ContratoAdministrativo contratoTemp : contratoAdministrativoTemp) {
                contratoAdministrativoRetorno.stream()
                                             .filter(ca -> ca.getId().equals(contratoTemp.getId()))
                                             .forEach(ca -> ca.setApensosAdministrativos(contratoTemp.getApensosAdministrativos()));
            }
        }

        return contratoAdministrativoRetorno;

    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE
    })
    public void aplicaPatch(Long idContrato, ContratoAdministrativo contratoPatch) {

        ContratoAdministrativo contratoAdministrativo = this.getById(idContrato, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);

        if (contratoPatch.getProcessoAdministrativo() != null) {
            Long id = contratoPatch.getProcessoAdministrativo().getId();
            ProcessoAdministrativo processoAdministrativo = processoAdministrativoServico.getById(id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            contratoAdministrativo.setProcessoAdministrativo(processoAdministrativo);
        }

        if (contratoPatch.getNumeroContrato() != null) {
            contratoAdministrativo.setNumeroContrato(contratoPatch.getNumeroContrato());
        }

        if (contratoPatch.getAnoContrato() != null) {
            contratoAdministrativo.setAnoContrato(contratoPatch.getAnoContrato());
        }

        if (contratoPatch.getDescricaoContrato() != null) {
            contratoAdministrativo.setDescricaoContrato(contratoPatch.getDescricaoContrato());
        }

        if (contratoPatch.getDataHoraInclusao() != null) {
            contratoAdministrativo.setDataHoraInclusao(contratoPatch.getDataHoraInclusao());
        }

        if (contratoPatch.getMatriculaInclusao() != null) {
            contratoAdministrativo.setMatriculaInclusao(contratoPatch.getMatriculaInclusao());
        }

        if (contratoPatch.getCpfCnpjFornecedor() != null) {
            contratoAdministrativo.setCpfCnpjFornecedor(contratoPatch.getCpfCnpjFornecedor());
        }

        if (contratoPatch.getUnidadeOperacional() != null) {
            contratoAdministrativo.setUnidadeOperacional(contratoPatch.getUnidadeOperacional());
        }

        this.update(contratoAdministrativo);
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE
    })
    public void criaDocumentoContratoAdministrativo(Long idContratoAdminstrativo, Integer idTipoDocumento, Long idDocumentoSubstituicao, String justificativaSubstituicao, OrigemDocumentoEnum origemDocumentoEnum, boolean confidencial, boolean valido, String descricao, String mimeType, List<AtributoDocumento> atributosDocumento, String binario) {
        // Captura do canal de comunicação
        Canal canal = this.canalServico.getByClienteSSO();
        if (canal == null) {
            throw new SimtrRequisicaoException("CAS.cDCA.001 - Canal de comunicação não localizado para codigo de integração fornecido.");
        }

        TipoDocumento tipoDocumento = this.tipoDocumentoServico.getById(idTipoDocumento);
        if (tipoDocumento == null) {
            throw new SimtrRequisicaoException("CAS.cDCA.002 - Tipo de Documento não localizado para codigo fornecido.");
        }

        ContratoAdministrativo contratoAdministrativo = this.getById(idContratoAdminstrativo, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (contratoAdministrativo == null) {
            throw new SimtrRequisicaoException("CAS.cDCA.003 - Contrato Administrativo não localizado sob identificador informado para vinculação do documento.");
        }

        FormatoConteudoEnum formatoConteudoEnum = null;
        if (mimeType != null) {
            try {
                formatoConteudoEnum = FormatoConteudoEnum.getByMimeType(mimeType);
                if (!this.documentoAdministrativoServico.validaMimetypeConteudo(formatoConteudoEnum, binario)) {
                    String mensagem = MessageFormat.format("CAS.cDCA.004 - Conteudo não compatível como o mimeType fornecido. mimeType: {0}.", mimeType);
                    throw new SimtrRequisicaoException(mensagem);
                }
            } catch (IllegalArgumentException iae) {
                String mensagem = MessageFormat.format("CAS.cDCA.005 - Formato de Conteudo não localizado pelo mimeType fornecido. mimeType: {0}.", mimeType);
                throw new SimtrRequisicaoException(mensagem, iae);
            }
        }

        if (idDocumentoSubstituicao != null && (justificativaSubstituicao == null || justificativaSubstituicao.isEmpty())) {
            throw new SimtrRequisicaoException("PAS.cDCA.006 - Justificativa obrigatória para Documento Administrativo substituído.");
        }

        Documento documento = this.documentoServico.prototype(canal, Boolean.FALSE, tipoDocumento, TemporalidadeDocumentoEnum.VALIDO, origemDocumentoEnum, formatoConteudoEnum, atributosDocumento, binario);
        this.documentoAdministrativoServico.insereDocumentoContratoAdministrativo(idContratoAdminstrativo, documento, idDocumentoSubstituicao, confidencial, valido, descricao);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public byte[] criaRelatorioPAE(Long idContrato, boolean completo, boolean incluirSemConteudo) throws IOException {
        ContratoAdministrativo contratoAdministrativo = this.getById(idContrato, completo, completo, completo);
        if (contratoAdministrativo == null) {
            throw new SimtrRequisicaoException("PAS.cRP.001 - Contrato Administrativo não localizado sob identificador informado para vinculação do documento.");
        }
        String processoNumeroAno = contratoAdministrativo.getProcessoAdministrativo().getNumeroProcesso() + "/"
                                   + contratoAdministrativo.getProcessoAdministrativo().getAnoProcesso();
        StreamingOutput streamingOutput = outputStream -> {
            try {
                String nomeContrato = "CONTRATO_" + contratoAdministrativo.getNumeroContrato() + "_" + contratoAdministrativo.getAnoContrato();
                String nomeApenso = null;
                ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(outputStream));
                byte[] dados = null;

                if (completo && contratoAdministrativo.getApensosAdministrativos() != null) {
                    for (ApensoAdministrativo apensoAdministrativo : contratoAdministrativo.getApensosAdministrativos()) {
                        nomeApenso = nomeContrato + "_APENSO_" + apensoAdministrativo.getId() + "_" + apensoAdministrativo.getTipoApenso().getDescricao().replaceAll(" ", "_");
                        if (TipoApensoEnum.AX.equals(apensoAdministrativo.getTipoApenso())) {
                            nomeApenso = nomeContrato + "_APENSO_" + apensoAdministrativo.getTitulo();
                        }

                        dados = this.relatorioServico.getRelatorioPAE(apensoAdministrativo.getDocumentosAdministrativos(), nomeApenso + ".pdf", processoNumeroAno);
                        zipOut = this.relatorioServico.adicionaArquivoZIP(dados, zipOut, nomeApenso + ".pdf", incluirSemConteudo, apensoAdministrativo.getMatriculaInclusao());
                    }
                }
                zipOut.flush();
                zipOut.close();
                outputStream.flush();
                outputStream.close();
            } catch (SiecmException ex) {
                throw ex;
            } catch (Exception ex) {
                String mensagem = MessageFormat.format("CAS.cRP.002 - Falha ao exportar o Contrato Administrativo. ID {0}", idContrato);
                throw new SimtrConfiguracaoException(mensagem, ex);
            }
        };

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        streamingOutput.write(baos);
        return baos.toByteArray();

    }

    // *********** Métodos Privados ***********
    private String getQuery(boolean vinculacaoProcesso, boolean vinculacaoApensosProcesso, boolean vinculacaoApensosDiretos) {

        boolean carregaBase = !vinculacaoProcesso && !vinculacaoApensosProcesso && !vinculacaoApensosDiretos;

        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT ca FROM ContratoAdministrativo ca ");

        // Dados comuns ao contrato administrativo
        if (carregaBase) {
            jpql.append(" LEFT JOIN FETCH ca.processoAdministrativo pa ");
            jpql.append(" LEFT JOIN FETCH ca.documentosAdministrativos dac ");
            jpql.append(" LEFT JOIN FETCH dac.documento dc ");
            jpql.append(" LEFT JOIN FETCH dc.tipoDocumento tdc ");
            jpql.append(" LEFT JOIN FETCH dc.atributosDocumento adc ");
        }

        if (vinculacaoProcesso) {
            // Processos Administrativos
            jpql.append(" LEFT JOIN FETCH ca.processoAdministrativo pa ");
            jpql.append(" LEFT JOIN FETCH pa.documentosAdministrativos dap ");
            jpql.append(" LEFT JOIN FETCH dap.documento dp ");
            jpql.append(" LEFT JOIN FETCH dp.tipoDocumento tdp ");
            jpql.append(" LEFT JOIN FETCH dp.atributosDocumento adp ");

            if (vinculacaoApensosProcesso) {
                // Apensos vinculados ao contrato administrativo
                jpql.append(" LEFT JOIN FETCH pa.apensosAdministrativos aapc ");
                jpql.append(" LEFT JOIN FETCH aapc.documentosAdministrativos dapc ");
                jpql.append(" LEFT JOIN FETCH dapc.documento dapc ");
                jpql.append(" LEFT JOIN FETCH dapc.tipoDocumento tdapc ");
                jpql.append(" LEFT JOIN FETCH dapc.atributosDocumento adpc ");
            }
        }

        if (vinculacaoApensosDiretos) {
            // Apensos Administrativos vinculados ao processo administrativo
            jpql.append(" LEFT JOIN FETCH ca.apensosAdministrativos aa ");
            jpql.append(" LEFT JOIN FETCH aa.documentosAdministrativos daa ");
            jpql.append(" LEFT JOIN FETCH daa.documento dac ");
            jpql.append(" LEFT JOIN FETCH dac.tipoDocumento tdac ");
            jpql.append(" LEFT JOIN FETCH dac.atributosDocumento adac ");
        }

        return jpql.toString();
    }
}
