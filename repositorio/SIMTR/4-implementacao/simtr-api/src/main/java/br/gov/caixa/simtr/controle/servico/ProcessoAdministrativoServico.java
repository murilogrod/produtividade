package br.gov.caixa.simtr.controle.servico;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
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
public class ProcessoAdministrativoServico extends AbstractService<ProcessoAdministrativo, Long> {

    private static final Logger LOGGER = Logger.getLogger(ProcessoAdministrativoServico.class.getName());

    @EJB
    private CanalServico canalServico;

    @EJB
    private DocumentoServico documentoServico;

    @EJB
    private DocumentoAdministrativoServico documentoAdministrativoServico;

    @EJB
    private KeycloakService keycloakService;

    @EJB
    private TipoDocumentoServico tipoDocumentoServico;

    @Inject
    private RelatorioServico relatorioServico;

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
    public void save(ProcessoAdministrativo processoAdministrativo) {
        if (!validaProtocoloSICLG(processoAdministrativo.getProtocoloSICLG())) {
            throw new SimtrRequisicaoException("PAS.s.001 - O tamanho mínimo do protocolo SICLG dever ser de 5 caracteres.");
        }
        processoAdministrativo.setMatriculaInclusao(this.keycloakService.getMatricula());
        super.save(processoAdministrativo);

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ProcessoAdministrativo getById(Long idProcesso, boolean vinculacaoContratos, boolean vinculacaoApensosContrato, boolean vinculacaoApensosDiretos) {
        ProcessoAdministrativo processoAdministrativoRetorno;
        ProcessoAdministrativo processoAdministrativoTemp;

        String jpqlBase = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        jpqlBase = jpqlBase.concat(" WHERE pa.id = :id ");
        TypedQuery<ProcessoAdministrativo> queryProcesso = this.entityManager.createQuery(jpqlBase, ProcessoAdministrativo.class);
        queryProcesso.setParameter("id", idProcesso);
        try {
            processoAdministrativoRetorno = queryProcesso.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }

        if (vinculacaoApensosDiretos) {
            String jpql = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
            jpql = jpql.concat(" WHERE pa.id = :id ");
            TypedQuery<ProcessoAdministrativo> query = this.entityManager.createQuery(jpql, ProcessoAdministrativo.class);
            query.setParameter("id", idProcesso);

            processoAdministrativoTemp = query.getSingleResult();
            processoAdministrativoRetorno.setApensosAdministrativos(processoAdministrativoTemp.getApensosAdministrativos());
        }

        if (vinculacaoContratos) {
            String jpql = this.getQuery(Boolean.TRUE, vinculacaoApensosContrato, Boolean.FALSE);
            jpql = jpql.concat(" WHERE pa.id = :id ");
            TypedQuery<ProcessoAdministrativo> query = this.entityManager.createQuery(jpql, ProcessoAdministrativo.class);
            query.setParameter("id", idProcesso);

            processoAdministrativoTemp = query.getSingleResult();
            processoAdministrativoRetorno.setContratosAdministrativos(processoAdministrativoTemp.getContratosAdministrativos());
        }

        return processoAdministrativoRetorno;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ProcessoAdministrativo getByNumeroAno(Integer numero, Integer ano, boolean vinculacaoContratos, boolean vinculacaoApensosContrato, boolean vinculacaoApensosDiretos) {
        ProcessoAdministrativo processoAdministrativoRetorno;
        ProcessoAdministrativo processoAdministrativoTemp;

        String jpqlBase = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        jpqlBase = jpqlBase.concat(" WHERE pa.numeroProcesso = :numero AND pa.anoProcesso = :ano ");
        TypedQuery<ProcessoAdministrativo> queryProcesso = this.entityManager.createQuery(jpqlBase, ProcessoAdministrativo.class);
        queryProcesso.setParameter("numero", numero);
        queryProcesso.setParameter("ano", ano);
        try {
            processoAdministrativoRetorno = queryProcesso.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }

        if (vinculacaoApensosDiretos) {
            String jpql = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
            jpql = jpql.concat(" WHERE pa.numeroProcesso = :numero AND pa.anoProcesso = :ano ");
            TypedQuery<ProcessoAdministrativo> query = this.entityManager.createQuery(jpql, ProcessoAdministrativo.class);
            query.setParameter("numero", numero);
            query.setParameter("ano", ano);

            processoAdministrativoTemp = query.getSingleResult();
            processoAdministrativoRetorno.setApensosAdministrativos(processoAdministrativoTemp.getApensosAdministrativos());
        }

        if (vinculacaoContratos) {
            String jpql = this.getQuery(Boolean.TRUE, vinculacaoApensosContrato, Boolean.FALSE);
            jpql = jpql.concat(" WHERE pa.numeroProcesso = :numero AND pa.anoProcesso = :ano ");
            TypedQuery<ProcessoAdministrativo> query = this.entityManager.createQuery(jpql, ProcessoAdministrativo.class);
            query.setParameter("numero", numero);
            query.setParameter("ano", ano);

            processoAdministrativoTemp = query.getSingleResult();
            processoAdministrativoRetorno.setContratosAdministrativos(processoAdministrativoTemp.getContratosAdministrativos());
        }

        return processoAdministrativoRetorno;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<ProcessoAdministrativo> listByDescricao(String objetoContratacao, boolean vinculacaoContratos, boolean vinculacaoApensosContrato, boolean vinculacaoApensosDiretos) {

        List<ProcessoAdministrativo> processoAdministrativoRetorno;
        List<ProcessoAdministrativo> processoAdministrativoTemp;

        String jpqlBase = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        jpqlBase = jpqlBase.concat(" WHERE UPPER(pa.objetoContratacao) LIKE CONCAT( '%', :objetoContratacao, '%' ) ");
        TypedQuery<ProcessoAdministrativo> queryProcesso = this.entityManager.createQuery(jpqlBase, ProcessoAdministrativo.class);
        queryProcesso.setParameter("objetoContratacao", objetoContratacao.toUpperCase());
        try {
            processoAdministrativoRetorno = queryProcesso.getResultList();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }

        if (vinculacaoApensosDiretos) {
            String jpql = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
            jpql = jpql.concat(" WHERE UPPER(pa.objetoContratacao) LIKE CONCAT( '%', :objetoContratacao, '%' ) ");
            TypedQuery<ProcessoAdministrativo> query = this.entityManager.createQuery(jpql, ProcessoAdministrativo.class);
            query.setParameter("objetoContratacao", objetoContratacao.toUpperCase());

            processoAdministrativoTemp = query.getResultList();
            for (ProcessoAdministrativo processoTemp : processoAdministrativoTemp) {
                processoAdministrativoRetorno.stream()
                                             .filter(pa -> pa.getId().equals(processoTemp.getId()))
                                             .forEach(pa -> pa.setApensosAdministrativos(processoTemp.getApensosAdministrativos()));
            }
        }

        if (vinculacaoContratos) {
            String jpql = this.getQuery(Boolean.TRUE, vinculacaoApensosContrato, Boolean.FALSE);
            jpql = jpql.concat(" WHERE UPPER(pa.objetoContratacao) LIKE CONCAT( '%', :objetoContratacao, '%' ) ");
            TypedQuery<ProcessoAdministrativo> query = this.entityManager.createQuery(jpql, ProcessoAdministrativo.class);
            query.setParameter("objetoContratacao", objetoContratacao.toUpperCase());

            processoAdministrativoTemp = query.getResultList();
            for (ProcessoAdministrativo processoTemp : processoAdministrativoTemp) {
                processoAdministrativoRetorno.stream()
                                             .filter(pa -> pa.getId().equals(processoTemp.getId()))
                                             .forEach(pa -> pa.setContratosAdministrativos(processoTemp.getContratosAdministrativos()));
            }
        }

        return processoAdministrativoRetorno;
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE
    })
    public void aplicaPatch(Long idProcesso, ProcessoAdministrativo processoPatch) {

        ProcessoAdministrativo processoAdministrativo = this.getById(idProcesso, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);

        if (processoPatch.getNumeroPregao() != null) {
            processoAdministrativo.setNumeroPregao(processoPatch.getNumeroPregao());
        }

        if (processoPatch.getUnidadeContratacao() != null) {
            processoAdministrativo.setUnidadeContratacao(processoPatch.getUnidadeContratacao());
        }

        if (processoPatch.getAnoPregao() != null) {
            processoAdministrativo.setAnoPregao(processoPatch.getAnoPregao());
        }

        if (processoPatch.getObjetoContratacao() != null) {
            processoAdministrativo.setObjetoContratacao(processoPatch.getObjetoContratacao());
        }

        if (processoPatch.getDataHoraInclusao() != null) {
            processoAdministrativo.setDataHoraInclusao(processoPatch.getDataHoraInclusao());
        }

        if (processoPatch.getMatriculaInclusao() != null) {
            processoAdministrativo.setMatriculaInclusao(processoPatch.getMatriculaInclusao());
        }

        if (processoPatch.getDataHoraFinalizacao() != null) {
            processoAdministrativo.setDataHoraFinalizacao(processoPatch.getDataHoraFinalizacao());
        }

        if (processoPatch.getMatriculaFinalizacao() != null) {
            processoAdministrativo.setMatriculaFinalizacao(processoPatch.getMatriculaFinalizacao());
        }

        if (processoPatch.getUnidadeDemandante() != null) {
            processoAdministrativo.setUnidadeDemandante(processoPatch.getUnidadeDemandante());
        }

        if (validaProtocoloSICLG(processoPatch.getProtocoloSICLG())) {
            processoAdministrativo.setProtocoloSICLG(processoPatch.getProtocoloSICLG());

        } else {
            throw new SimtrRequisicaoException("PAS.aP.001 - O tamanho mínimo do protocolo SICLG dever ser de 5 caracteres.");
        }

        this.update(processoAdministrativo);
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE
    })
    public void criaDocumentoProcessoAdministrativo(Long idProcessoAdminstrativo, Integer idTipoDocumento, Long idDocumentoSubstituicao, String justificativaSubstituicao, OrigemDocumentoEnum origemDocumentoEnum, boolean confidencial, boolean valido, String descricao, String mimeType, List<AtributoDocumento> atributosDocumento, String binario) {
        // Captura do canal de comunicação
        Canal canal = this.canalServico.getByClienteSSO();
        if (canal == null) {
            throw new SimtrRequisicaoException("PAS.cDPA.001 - Canal de comunicação não localizado para codigo de integração fornecido.");
        }

        TipoDocumento tipoDocumento = this.tipoDocumentoServico.getById(idTipoDocumento);
        if (tipoDocumento == null) {
            throw new SimtrRequisicaoException("PAS.cDPA.002 - Tipo de Documento não localizado para codigo fornecido.");
        }

        // Realiza captura do dossiê do cliente com base no ID informado.
        ProcessoAdministrativo processoAdministrativo = this.getById(idProcessoAdminstrativo, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (processoAdministrativo == null) {
            throw new SimtrRequisicaoException("PAS.cDPA.003 - Processo Administrativo não localizado sob identificador informado para vinculação do documento.");
        }

        FormatoConteudoEnum formatoConteudoEnum = null;
        if (mimeType != null) {
            try {
                formatoConteudoEnum = FormatoConteudoEnum.getByMimeType(mimeType);
                if (!this.documentoAdministrativoServico.validaMimetypeConteudo(formatoConteudoEnum, binario)) {
                    String mensagem = MessageFormat.format("PAS.cDPA.004 - Conteudo não compatível como o mimeType fornecido. mimeType: {0}.", mimeType);
                    throw new SimtrRequisicaoException(mensagem);
                }

            } catch (IllegalArgumentException iae) {
                String mensagem = MessageFormat.format("PAS.cDPA.005 - Formato de Conteudo não localizado pelo mimeType fornecido. mimeType: {0}.", mimeType);
                throw new SimtrRequisicaoException(mensagem, iae);
            }
        }

        if (idDocumentoSubstituicao != null && (justificativaSubstituicao == null || justificativaSubstituicao.isEmpty())) {
            String mensagem = "PAS.cDPA.006 - Justificativa obrigatória para Documento Administrativo substituído.";
            throw new SimtrRequisicaoException(mensagem);
        }

        Documento documento = this.documentoServico.prototype(canal, Boolean.FALSE, tipoDocumento, TemporalidadeDocumentoEnum.VALIDO, origemDocumentoEnum, formatoConteudoEnum, atributosDocumento, binario);
        this.documentoAdministrativoServico.insereDocumentoProcessoAdministrativo(idProcessoAdminstrativo, documento, idDocumentoSubstituicao, justificativaSubstituicao, confidencial, valido, descricao);

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public byte[] criaRelatorioPAE(Long idProcesso, boolean completo, boolean incluirSemConteudo) throws IOException {
        ProcessoAdministrativo processoAdministrativo = this.getById(idProcesso, completo, completo, completo);

        if (processoAdministrativo == null) {
            throw new SimtrRequisicaoException("PAS.cRP.001 - Processo Administrativo não localizado sob identificador informado para vinculação do documento.");
        }
        String processoNumeroAno = processoAdministrativo.getNumeroProcesso() + "/" + processoAdministrativo.getAnoProcesso();

        StreamingOutput streamingOutput = outputStream -> {
            try {
                String nomeProcesso = null;
                String nomeContrato = null;
                String nomeApenso = null;
                ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(outputStream));
                byte[] dados = null;

                if (processoAdministrativo.getDocumentosAdministrativos() != null) {
                    nomeProcesso = "PROCESSO_" + processoAdministrativo.getNumeroProcesso() + "_" + processoAdministrativo.getAnoProcesso();

                    dados = this.relatorioServico.getRelatorioPAE(processoAdministrativo.getDocumentosAdministrativos(), nomeProcesso + ".pdf", processoNumeroAno);

                    zipOut = this.relatorioServico.adicionaArquivoZIP(dados, zipOut, nomeProcesso + ".pdf", incluirSemConteudo, processoAdministrativo.getMatriculaInclusao());
                }
                if (completo) {
                    if (processoAdministrativo.getContratosAdministrativos() != null) {
                        for (ContratoAdministrativo contratoAdministrativo : processoAdministrativo.getContratosAdministrativos()) {
                            nomeContrato = "CONTRATO_" + contratoAdministrativo.getNumeroContrato() + "_" + contratoAdministrativo.getAnoContrato();

                            if (contratoAdministrativo.getApensosAdministrativos() != null) {
                                for (ApensoAdministrativo apensoAdministrativo : contratoAdministrativo.getApensosAdministrativos()) {
                                    nomeApenso = nomeContrato + "_APENSO_" + apensoAdministrativo.getId() + "_"
                                                 + apensoAdministrativo.getTipoApenso().getDescricao().replaceAll(" ", "_");
                                    if (TipoApensoEnum.AX.equals(apensoAdministrativo.getTipoApenso())) {
                                        nomeApenso = nomeContrato + "_APENSO_" + apensoAdministrativo.getTitulo();
                                    }

                                    dados = this.relatorioServico.getRelatorioPAE(apensoAdministrativo.getDocumentosAdministrativos(), nomeApenso + ".pdf", processoNumeroAno);

                                    zipOut = this.relatorioServico.adicionaArquivoZIP(dados, zipOut, nomeApenso
                                                                                                     + ".pdf", incluirSemConteudo, apensoAdministrativo.getMatriculaInclusao());
                                }
                            }
                        }
                    }

                    if (processoAdministrativo.getApensosAdministrativos() != null) {
                        for (ApensoAdministrativo apensoAdministrativo : processoAdministrativo.getApensosAdministrativos()) {
                            nomeApenso = nomeProcesso + "_APENSO_" + apensoAdministrativo.getId() + "_" + apensoAdministrativo.getTipoApenso().getDescricao().replaceAll(" ", "_");
                            if (TipoApensoEnum.AX.equals(apensoAdministrativo.getTipoApenso())) {
                                nomeApenso = nomeProcesso + "_APENSO_" + apensoAdministrativo.getTitulo();
                            }

                            dados = this.relatorioServico.getRelatorioPAE(apensoAdministrativo.getDocumentosAdministrativos(), nomeApenso + ".pdf", processoNumeroAno);

                            zipOut = this.relatorioServico.adicionaArquivoZIP(dados, zipOut, nomeApenso + ".pdf", incluirSemConteudo, apensoAdministrativo.getMatriculaInclusao());
                        }
                    }
                }

                zipOut.flush();
                zipOut.close();
                outputStream.flush();
                outputStream.close();

            } catch (SiecmException ex) {
                throw ex;
            } catch (Exception ex) {
                String mensagem = MessageFormat.format("PAS.cRP.002 - Falha ao exportar o Processo Administrativo. ID {0}", idProcesso);
                throw new SimtrConfiguracaoException(mensagem, ex);
            }
        };

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        streamingOutput.write(baos);
        return baos.toByteArray();

    }

    // *********** Métodos Privados ***********
    private String getQuery(boolean vinculacaoContratos, boolean vinculacaoApensosContrato, boolean vinculacaoApensosDiretos) {
        boolean carregaBase = !vinculacaoContratos && !vinculacaoApensosContrato && !vinculacaoApensosDiretos;

        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT pa FROM ProcessoAdministrativo pa ");

        if (carregaBase) {
            jpql.append(" LEFT JOIN FETCH pa.documentosAdministrativos dap ");
            jpql.append(" LEFT JOIN FETCH dap.documento dp ");
            jpql.append(" LEFT JOIN FETCH dp.tipoDocumento tdp ");
            jpql.append(" LEFT JOIN FETCH dp.atributosDocumento adp ");
        }

        if (vinculacaoContratos) {
            // Contratos Administrativos vinculados ao processo administrativo
            jpql.append(" LEFT JOIN FETCH pa.contratosAdministrativos ca ");
            jpql.append(" LEFT JOIN FETCH ca.documentosAdministrativos dac ");
            jpql.append(" LEFT JOIN FETCH dac.documento dc ");
            jpql.append(" LEFT JOIN FETCH dc.tipoDocumento tdc ");
            jpql.append(" LEFT JOIN FETCH dc.atributosDocumento adc ");

            if (vinculacaoApensosContrato) {
                // Apensos vinculados ao contrato administrativo
                jpql.append(" LEFT JOIN FETCH ca.apensosAdministrativos aacp ");
                jpql.append(" LEFT JOIN FETCH aacp.documentosAdministrativos dacp ");
                jpql.append(" LEFT JOIN FETCH dacp.documento dcp ");
                jpql.append(" LEFT JOIN FETCH dcp.tipoDocumento tdcp ");
                jpql.append(" LEFT JOIN FETCH dcp.atributosDocumento adcp ");
            }
        }
        if (vinculacaoApensosDiretos) {
            // Apensos Administrativos vinculados ao processo administrativo
            jpql.append(" LEFT JOIN FETCH pa.apensosAdministrativos aa ");
            jpql.append(" LEFT JOIN FETCH aa.documentosAdministrativos daa ");
            jpql.append(" LEFT JOIN FETCH daa.documento dap ");
            jpql.append(" LEFT JOIN FETCH dap.tipoDocumento tdap ");
            jpql.append(" LEFT JOIN FETCH dap.atributosDocumento adap ");
        }

        return jpql.toString();
    }

    private boolean validaProtocoloSICLG(String codigoSICLG) {
        boolean protocoloValido = true;

        if (codigoSICLG != null) {
            if (codigoSICLG.replaceAll(" ", "").trim().length() < 5) { // Tamanho definido para o protocolo
                protocoloValido = false;
            }

        }
        return protocoloValido;

    }

}
