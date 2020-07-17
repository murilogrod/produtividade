package br.gov.caixa.simtr.controle.servico;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
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
public class ApensoAdministrativoServico extends AbstractService<ApensoAdministrativo, Long> {

    private static final Logger LOGGER = Logger.getLogger(ApensoAdministrativoServico.class.getName());

    @EJB
    private CanalServico canalServico;

    @EJB
    private RelatorioServico relatorioServico;

    @EJB
    private ContratoAdministrativoServico contratoAdministrativoServico;

    @EJB
    private TipoDocumentoServico tipoDocumentoServico;

    @EJB
    private DocumentoAdministrativoServico documentoAdministrativoServico;

    @EJB
    private DocumentoServico documentoServico;

    @EJB
    private KeycloakService keycloakService;

    @EJB
    private ProcessoAdministrativoServico processoAdministrativoServico;

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
    public void save(ApensoAdministrativo apensoAdministrativo) {
        if (!this.validaProtocoloSICLG(apensoAdministrativo)) {
            throw new SimtrRequisicaoException("AAS.s.001 - O tamanho mínimo do protocolo SICLG deve ser de 5 caracteres ou é obrigatório.");
        }

        if (apensoAdministrativo.getContratoAdministrativo() != null && apensoAdministrativo.getProcessoAdministrativo() != null) {
            throw new SimtrRequisicaoException("AAS.s.002 - Parâmetros inválido para o apenso, informar somente o número do processo ou do contrato.");
        }

        if (TipoApensoEnum.AX.equals(apensoAdministrativo.getTipoApenso())
            && ((Objects.isNull(apensoAdministrativo.getTitulo())) || (apensoAdministrativo.getTitulo().length() > 20))) {
            throw new SimtrRequisicaoException("AAS.s.003 - Parâmetro inválido para o apenso, informar titulo do apenso sem caracteres especiais e com no máximo 20 caracteres.");
        }

        apensoAdministrativo.setMatriculaInclusao(this.keycloakService.getMatricula());
        super.save(apensoAdministrativo);
    }

    public ApensoAdministrativo getById(Long idApenso, boolean vinculacaoProcesso, boolean vinculacaoContrato) {
        ApensoAdministrativo apensoAdministrativoRetorno;
        ApensoAdministrativo apensoAdministrativoTemp;

        String jpqlBase = this.getQuery(Boolean.FALSE, Boolean.FALSE);
        jpqlBase = jpqlBase.concat(" WHERE aa.id = :id ");
        TypedQuery<ApensoAdministrativo> queryApenso = this.entityManager.createQuery(jpqlBase, ApensoAdministrativo.class);
        queryApenso.setParameter("id", idApenso);
        try {
            apensoAdministrativoRetorno = queryApenso.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }

        if (vinculacaoProcesso) {
            String jpql = this.getQuery(Boolean.TRUE, Boolean.FALSE);
            jpql = jpql.concat(" WHERE aa.id = :id ");
            TypedQuery<ApensoAdministrativo> query = this.entityManager.createQuery(jpql, ApensoAdministrativo.class);
            query.setParameter("id", idApenso);

            apensoAdministrativoTemp = query.getSingleResult();
            apensoAdministrativoRetorno.setProcessoAdministrativo(apensoAdministrativoTemp.getProcessoAdministrativo());
        }

        if (vinculacaoContrato) {
            String jpql = this.getQuery(Boolean.FALSE, Boolean.TRUE);
            jpql = jpql.concat(" WHERE aa.id = :id ");
            TypedQuery<ApensoAdministrativo> query = this.entityManager.createQuery(jpql, ApensoAdministrativo.class);
            query.setParameter("id", idApenso);

            apensoAdministrativoTemp = query.getSingleResult();
            apensoAdministrativoRetorno.setContratoAdministrativo(apensoAdministrativoTemp.getContratoAdministrativo());
        }

        return apensoAdministrativoRetorno;
    }

    public ApensoAdministrativo getByProtocoloSICLG(String protocoloSiclg, boolean vinculacaoProcesso, boolean vinculacaoContrato) {
        ApensoAdministrativo apensoAdministrativoRetorno;
        ApensoAdministrativo apensoAdministrativoTemp;

        String jpqlBase = this.getQuery(Boolean.FALSE, Boolean.FALSE);
        jpqlBase = jpqlBase.concat(" WHERE aa.protocoloSICLG = :protocoloSiclg ");
        TypedQuery<ApensoAdministrativo> queryApenso = this.entityManager.createQuery(jpqlBase, ApensoAdministrativo.class);
        queryApenso.setParameter("protocoloSiclg", protocoloSiclg);
        try {
            apensoAdministrativoRetorno = queryApenso.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }

        if (vinculacaoProcesso) {
            String jpql = this.getQuery(Boolean.TRUE, Boolean.FALSE);
            jpql = jpql.concat(" WHERE aa.protocoloSICLG = :protocoloSiclg ");
            TypedQuery<ApensoAdministrativo> query = this.entityManager.createQuery(jpql, ApensoAdministrativo.class);
            query.setParameter("protocoloSiclg", protocoloSiclg);

            apensoAdministrativoTemp = query.getSingleResult();
            apensoAdministrativoRetorno.setProcessoAdministrativo(apensoAdministrativoTemp.getProcessoAdministrativo());
        }

        if (vinculacaoContrato) {
            String jpql = this.getQuery(Boolean.FALSE, Boolean.TRUE);
            jpql = jpql.concat(" WHERE aa.protocoloSICLG = :protocoloSiclg ");
            TypedQuery<ApensoAdministrativo> query = this.entityManager.createQuery(jpql, ApensoAdministrativo.class);
            query.setParameter("protocoloSiclg", protocoloSiclg);

            apensoAdministrativoTemp = query.getSingleResult();
            apensoAdministrativoRetorno.setContratoAdministrativo(apensoAdministrativoTemp.getContratoAdministrativo());
        }

        return apensoAdministrativoRetorno;
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE
    })
    public void aplicaPatch(Long idApenso, ApensoAdministrativo apensoPatch) {

        ApensoAdministrativo apensoAdministrativo = this.getById(idApenso, Boolean.FALSE, Boolean.FALSE);

        if (apensoPatch.getProcessoAdministrativo() != null) {
            Long id = apensoPatch.getProcessoAdministrativo().getId();
            ProcessoAdministrativo processoAdministrativo = processoAdministrativoServico.getById(id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            apensoAdministrativo.setProcessoAdministrativo(processoAdministrativo);
        }

        if (apensoPatch.getContratoAdministrativo() != null) {
            Long id = apensoPatch.getContratoAdministrativo().getId();
            ContratoAdministrativo contratoAdministrativo = contratoAdministrativoServico.getById(id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            apensoAdministrativo.setContratoAdministrativo(contratoAdministrativo);
        }

        if (apensoPatch.getCpfCnpjFornecedor() != null) {
            apensoAdministrativo.setCpfCnpjFornecedor(apensoPatch.getCpfCnpjFornecedor());
        }

        if (apensoPatch.getTipoApenso() != null) {
            apensoAdministrativo.setTipoApenso(apensoPatch.getTipoApenso());
        }

        if (apensoPatch.getDescricaoApenso() != null) {
            apensoAdministrativo.setDescricaoApenso(apensoPatch.getDescricaoApenso());
        }

        if (apensoPatch.getDataHoraInclusao() != null) {
            apensoAdministrativo.setDataHoraInclusao(apensoPatch.getDataHoraInclusao());
        }

        if (apensoPatch.getMatriculaInclusao() != null) {
            apensoAdministrativo.setMatriculaInclusao(apensoPatch.getMatriculaInclusao());
        }

        if (validaProtocoloSICLG(apensoPatch)) {
            apensoAdministrativo.setProtocoloSICLG(apensoPatch.getProtocoloSICLG());
        } else {
            throw new SimtrRequisicaoException("AAS.aP.001 - O protocolo do SICLG deve ser informado com minimo de 5 caracteres.");
        }

        if (apensoPatch.getTitulo() != null) {
            apensoAdministrativo.setTitulo(apensoPatch.getTitulo());
        }

        this.update(apensoAdministrativo);
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE
    })
    public void criaDocumentoApensoAdministrativo(Long idApensoAdminstrativo, Integer idTipoDocumento, Long idDocumentoSubstituicao, String justificativaSubstituicao, OrigemDocumentoEnum origemDocumentoEnum, boolean confidencial, boolean valido, String descricao, String mimeType, List<AtributoDocumento> atributosDocumento, String binario) {
        // Captura do canal de comunicação
        Canal canal = this.canalServico.getByClienteSSO();
        if (canal == null) {
            throw new SimtrRequisicaoException("AAS.cDAA.001 - Canal de comunicação não localizado para codigo de integração fornecido.");
        }

        TipoDocumento tipoDocumento = this.tipoDocumentoServico.getById(idTipoDocumento);
        if (tipoDocumento == null) {
            throw new SimtrRequisicaoException("AAS.cDAA.002 - Tipo de Documento não localizado para codigo fornecido.");
        }

        // Realiza captura do dossiê do cliente com base no ID informado.
        ApensoAdministrativo apensoAdministrativo = this.getById(idApensoAdminstrativo, Boolean.FALSE, Boolean.FALSE);
        if (apensoAdministrativo == null) {
            throw new SimtrRequisicaoException("AAS.cDAA.003 - Contrato Administrativo não localizado sob identificador informado para vinculação do documento.");
        }

        FormatoConteudoEnum formatoConteudoEnum = null;
        if (mimeType != null) {
            try {
                formatoConteudoEnum = FormatoConteudoEnum.getByMimeType(mimeType);
                if (!this.documentoAdministrativoServico.validaMimetypeConteudo(formatoConteudoEnum, binario)) {
                    String mensagem = MessageFormat.format("AAS.cDAA.004 - Conteudo não compatível como o mimeType fornecido. mimeType: {0}.", mimeType);
                    throw new SimtrRequisicaoException(mensagem);
                }
            } catch (IllegalArgumentException iae) {
                String mensagem = MessageFormat.format("AAS.cDAA.005 - Formato de Conteudo não localizado pelo mimeType fornecido. mimeType: {0}.", mimeType);
                LOGGER.log(Level.ALL, mensagem, iae);
                throw new SimtrRequisicaoException(mensagem);
            }
        }

        if (idDocumentoSubstituicao != null && (justificativaSubstituicao == null || justificativaSubstituicao.isEmpty())) {
            throw new SimtrRequisicaoException("APS.cDAA.006 - Justificativa obrigatória para Documento Administrativo substituído.");
        }

        Documento documento = this.documentoServico.prototype(canal, Boolean.FALSE, tipoDocumento, TemporalidadeDocumentoEnum.VALIDO, origemDocumentoEnum, formatoConteudoEnum, atributosDocumento, binario);
        this.documentoAdministrativoServico.insereDocumentoApensoAdministrativo(idApensoAdminstrativo, documento, confidencial, valido, descricao, idDocumentoSubstituicao, justificativaSubstituicao);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public byte[] criaRelatorioPAE(Long idApenso, boolean completo, boolean incluirSemConteudo) throws IOException {
        ApensoAdministrativo apensoAdministrativo = this.getById(idApenso, completo, completo);

        if (apensoAdministrativo == null) {
            String mensagem = MessageFormat.format("AAS.cRP.001  - Apenso Administrativo não localizado sob identificador informado para vinculação do documento. Identificador: {0}", idApenso);
            throw new SimtrRequisicaoException(mensagem);
        }
        String processoNumeroAno;

        if (apensoAdministrativo.getProcessoAdministrativo() != null) {
            processoNumeroAno = apensoAdministrativo.getProcessoAdministrativo().getNumeroProcesso() + "/" + apensoAdministrativo.getProcessoAdministrativo().getAnoProcesso();
        } else {
            processoNumeroAno = apensoAdministrativo.getContratoAdministrativo().getProcessoAdministrativo().getNumeroProcesso() + "/"
                                + apensoAdministrativo.getContratoAdministrativo().getProcessoAdministrativo().getAnoProcesso();
        }

        @SuppressWarnings("resource")
        StreamingOutput streamingOutput = outputStream -> {
            try {
                ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(outputStream));
                byte[] dados = null;

                if (completo && apensoAdministrativo.getDocumentosAdministrativos() != null) {
                    String nomeApenso = "APENSO_" + apensoAdministrativo.getId() + "_" + apensoAdministrativo.getTipoApenso().getDescricao().replaceAll(" ", "_");
                    if ("AX".equals(apensoAdministrativo.getTipoApenso().name())) {
                        nomeApenso = "APENSO_" + apensoAdministrativo.getTitulo();
                    }

                    dados = this.relatorioServico.getRelatorioPAE(apensoAdministrativo.getDocumentosAdministrativos(), nomeApenso + ".pdf", processoNumeroAno);
                    zipOut = this.relatorioServico.adicionaArquivoZIP(dados, zipOut, nomeApenso + ".pdf", incluirSemConteudo, apensoAdministrativo.getMatriculaInclusao());
                }
                if (completo) {
                    // TODO: definir o que incluir no completo
                }
                zipOut.flush();
                zipOut.close();
                outputStream.flush();
                outputStream.close();

            } catch (SiecmException ex) {
                throw ex;
            } catch (Exception ex) {
                String mensagem = MessageFormat.format("AAS.cRP.002  - Falha ao exportar o Apenso Administrativo. ID {0}", idApenso);
                throw new SimtrConfiguracaoException(mensagem, ex);
            }
        };

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        streamingOutput.write(baos);
        return baos.toByteArray();

    }

    // *********** Métodos Privados ***********
    private String getQuery(boolean vinculacaoProcesso, boolean vinculacaoContrato) {

        boolean carregaBase = !vinculacaoProcesso && !vinculacaoContrato;

        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT aa FROM ApensoAdministrativo aa ");

        // Dados comuns ao contrato administrativo
        if (carregaBase) {
            jpql.append(" LEFT JOIN FETCH aa.processoAdministrativo pa ");
            jpql.append(" LEFT JOIN FETCH aa.contratoAdministrativo ca ");
            jpql.append(" LEFT JOIN FETCH ca.processoAdministrativo paca ");
            jpql.append(" LEFT JOIN FETCH aa.documentosAdministrativos daa ");
            jpql.append(" LEFT JOIN FETCH daa.documento da ");
            jpql.append(" LEFT JOIN FETCH da.tipoDocumento tda ");
            jpql.append(" LEFT JOIN FETCH da.atributosDocumento ada ");
        }

        if (vinculacaoProcesso) {
            // Processos Administrativos
            jpql.append(" LEFT JOIN FETCH aa.processoAdministrativo pa ");
            jpql.append(" LEFT JOIN FETCH pa.documentosAdministrativos dap ");
            jpql.append(" LEFT JOIN FETCH dap.documento dp ");
            jpql.append(" LEFT JOIN FETCH dp.tipoDocumento tdp ");
            jpql.append(" LEFT JOIN FETCH dp.atributosDocumento adp ");
        }

        if (vinculacaoContrato) {
            // Contratos Administrativos vinculados ao processo administrativo
            jpql.append(" LEFT JOIN FETCH aa.contratoAdministrativo ca ");
            jpql.append(" LEFT JOIN FETCH ca.processoAdministrativo paca ");
            jpql.append(" LEFT JOIN FETCH ca.documentosAdministrativos dac ");
            jpql.append(" LEFT JOIN FETCH dac.documento dc ");
            jpql.append(" LEFT JOIN FETCH dc.tipoDocumento tdc ");
            jpql.append(" LEFT JOIN FETCH dc.atributosDocumento adc ");
        }

        return jpql.toString();
    }

    private boolean validaProtocoloSICLG(ApensoAdministrativo apensoAdm) {

        if (apensoAdm.getTipoApenso().isSiclgObrigatorio() && (apensoAdm.getProtocoloSICLG() == null || apensoAdm.getProtocoloSICLG().replaceAll(" ", "").trim().length() < 5)) {
            return Boolean.FALSE;
        }

        if ((apensoAdm.getTipoApenso().isSiclgProibido()) && (apensoAdm.getProtocoloSICLG() != null)) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
