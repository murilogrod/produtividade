package br.gov.caixa.simtr.controle.servico.helper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.DocumentoServico;
import br.gov.caixa.simtr.controle.servico.DossieClienteServico;
import br.gov.caixa.simtr.controle.servico.FuncaoDocumentalServico;
import br.gov.caixa.simtr.controle.servico.GarantiaServico;
import br.gov.caixa.simtr.controle.servico.ProcessoServico;
import br.gov.caixa.simtr.controle.servico.ProdutoServico;
import br.gov.caixa.simtr.controle.servico.SituacaoDocumentoServico;
import br.gov.caixa.simtr.controle.servico.TipoDocumentoServico;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.DossieClienteProduto;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.ElementoConteudo;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.Garantia;
import br.gov.caixa.simtr.modelo.entidade.GarantiaInformada;
import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.OpcaoCampo;
import br.gov.caixa.simtr.modelo.entidade.OpcaoSelecionada;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.ProcessoFaseDossie;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.entidade.ProdutoDossie;
import br.gov.caixa.simtr.modelo.entidade.RespostaDossie;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDocumento;
import br.gov.caixa.simtr.modelo.entidade.SituacaoInstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.AtributoDocumentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.DocumentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.ElementoConteudoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.GarantiaInformadaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.ProdutoContratadoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.VinculoPessoaDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE,
    ConstantesUtil.PERFIL_MTRSDNTTO,
    ConstantesUtil.PERFIL_MTRSDNTTG
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class DossieProdutoVinculosHelper {

    @EJB
    private DocumentoServico documentoServico;

    @EJB
    private DossieClienteServico dossieClienteServico;

    @EJB
    private FuncaoDocumentalServico funcaoDocumentalServico;

    @EJB
    private GarantiaServico garantiaServico;

    @EJB
    private ProcessoServico processoServico;

    @EJB
    private ProdutoServico produtoServico;

    @EJB
    private SituacaoDocumentoServico situacaoDocumentoServico;

    @EJB
    private TipoDocumentoServico tipoDocumentoServico;

    @Inject
    private EntityManager entityManager;

    private static final Logger LOGGER = Logger.getLogger(DossieProdutoVinculosHelper.class.getName());

    // ************** VINCULO DE PESSOAS ***************//

    /**
     * Cria o vinculo entre o dossiê de produto e o dossiê de cliente indicados, conforme o tipo de relacionamento e a sequencia indicados
     *
     * @param dossieCliente Dossie de Cliente definido para o vinculo considerado principal na relação.
     * @param dossieClienteRelacionado Dossiê do cliente relacionado no vinculo definido com o dossiê de cliente principal da relação.
     * @param dossieProduto Dossie de Produto definido para o vinculo
     * @param tipoRelacionamento Tipo de relacionamento especificado para o vinculo.
     * @param sequencia Sequencia utilizada no tipo de vinculo
     * @return Referencia da relação criada entre o dossiê de cliente com o dossiê de produto
     */
    public DossieClienteProduto criaVinculoDossieCliente(DossieCliente dossieCliente, DossieCliente dossieClienteRelacionado, DossieProduto dossieProduto, TipoRelacionamento tipoRelacionamento, Integer sequencia) {
        // Cria o vinculo entre dossie de cliente e dossie de produto
        DossieClienteProduto dossieClienteProduto = new DossieClienteProduto();
        dossieClienteProduto.setDossieCliente(dossieCliente);
        dossieClienteProduto.setDossieClienteRelacionado(dossieClienteRelacionado);
        dossieClienteProduto.setDossieProduto(dossieProduto);
        dossieClienteProduto.setSequenciaTitularidade(sequencia);
        dossieClienteProduto.setTipoRelacionamento(tipoRelacionamento);

        this.entityManager.persist(dossieClienteProduto);

        return dossieClienteProduto;
    }

    public void removeVinculoDossieCliente(DossieClienteProduto dossieClienteProduto) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" DELETE FROM InstanciaDocumento instDoc ");
        jpql.append(" WHERE instDoc.dossieProduto.id = :idDossieProduto ");
        jpql.append(" AND instDoc.dossieClienteProduto.id = :idClienteProduto ");

        Query queryInstancias = this.entityManager.createQuery(jpql.toString());
        queryInstancias.setParameter(ConstantesUtil.KEY_ID_DOSSIE_PRODUTO, dossieClienteProduto.getDossieProduto().getId());
        queryInstancias.setParameter("idClienteProduto", dossieClienteProduto.getId());

        queryInstancias.executeUpdate();

        jpql = new StringBuilder();
        jpql.append(" DELETE FROM RespostaDossie rd ");
        jpql.append(" WHERE rd.dossieProduto.id = :idDossieProduto ");
        jpql.append(" AND rd.dossieClienteProduto.id = :idClienteProduto ");

        Query queryRespostas = this.entityManager.createQuery(jpql.toString());
        queryRespostas.setParameter(ConstantesUtil.KEY_ID_DOSSIE_PRODUTO, dossieClienteProduto.getDossieProduto().getId());
        queryRespostas.setParameter("idClienteProduto", dossieClienteProduto.getId());

        queryRespostas.executeUpdate();

        jpql = new StringBuilder();
        jpql.append(" DELETE FROM DossieClienteProduto ");
        jpql.append(" WHERE id = :idDossieClienteProduto ");

        Query queryProdutoDossie = this.entityManager.createQuery(jpql.toString());
        queryProdutoDossie.setParameter("idDossieClienteProduto", dossieClienteProduto.getId());

        queryProdutoDossie.executeUpdate();
    }

    public void atualizaVinculoDossieClienteRelacionado(DossieClienteProduto dossieClienteProduto, DossieCliente dossieClienteRelacionado) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" UPDATE DossieClienteProduto SET dossieClienteRelacionado = :dossieClienteRelacionado ");
        jpql.append(" WHERE id = :id ");

        Query query = this.entityManager.createQuery(jpql.toString());
        query.setParameter("dossieClienteRelacionado", dossieClienteRelacionado);
        query.setParameter("id", dossieClienteProduto.getId());

        query.executeUpdate();
    }

    public void atualizaVinculoDossieClienteSequencial(DossieClienteProduto dossieClienteProduto, Integer sequenciaTitularidade) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" UPDATE DossieClienteProduto SET sequenciaTitularidade = :seqTitularidade ");
        jpql.append(" WHERE id = :id ");

        Query query = this.entityManager.createQuery(jpql.toString());
        query.setParameter("seqTitularidade", sequenciaTitularidade);
        query.setParameter("id", dossieClienteProduto.getId());

        query.executeUpdate();
    }

    // ************** VINCULO DE GARANTIAS ***************//

    public GarantiaInformada criaVinculoGarantiaInformada(DossieProduto dossieProduto, Garantia garantia, Produto produtoRelacionado, GarantiaInformadaDTO garantiaInformadaDTO) {

        GarantiaInformada garantiaInformada = new GarantiaInformada();
        garantiaInformada.setDossieProduto(dossieProduto);
        garantiaInformada.setGarantia(garantia);
        garantiaInformada.setValorGarantia(garantiaInformadaDTO.getValor());
        garantiaInformada.setProduto(produtoRelacionado);

        if ((garantia.getFidejussoria()) && (garantiaInformadaDTO.getIdentificadoresClientesAvalistas() != null)) {

            List<DossieCliente> avalistas = new ArrayList<>();
            garantiaInformadaDTO.getIdentificadoresClientesAvalistas().forEach(idDossieCliente -> {
                DossieCliente dossieCliente = this.dossieClienteServico.getById(idDossieCliente, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
                if ((dossieCliente == null)) {
                    throw new SimtrRequisicaoException(MessageFormat.format("DPS.vGIDP.001 - Dossiê de Cliente não localizado para identificador fornecido. ID = {0}", idDossieCliente));
                }
                dossieCliente.addGarantiasInformadas(garantiaInformada);
                avalistas.add(dossieCliente);
            });

            garantiaInformada.addDossiesCliente(avalistas.toArray(new DossieCliente[avalistas.size()]));
        }
        // Registra produto dossiê associada ao dossiê de produto
        this.entityManager.persist(garantiaInformada);

        return garantiaInformada;
    }

    /**
     * Exclui os registros das instancias de documento vinculadas ao dossiê de produto informado, relacionaas aos registros de denominaddos de garantias informadas que por sua vez estão vinculados a
     * registros de garantia sob codigo de identificsação informadoinformada, em seguida remove a relação entre a garantia e o dossiê de produto
     *
     * @param idDossieProduto - Identificador do Dossiê de Produto
     * @param idGarantia - Identificador da garantia
     * @param idProduto - Identificador do Produto, caso não informado serão consideradas garantias informadas para o dossiê de forma geral
     */
    public void removeVinculoGarantiaInformada(Long idDossieProduto, Integer idGarantia, Integer idProduto) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" DELETE FROM InstanciaDocumento instDoc2 ");
        jpql.append(" WHERE instDoc2.dossieProduto.id = :idDossieProduto ");
        jpql.append(" AND instDoc2.garantiaInformada IN ( ");
        jpql.append("   SELECT gi FROM GarantiaInformada gi ");
        jpql.append("   WHERE gi.dossieProduto.id = :idDossieProduto ");
        jpql.append("   AND gi.garantia.id = :idGarantia ");
        if (idProduto != null) {
            jpql.append("   AND gi.produto.id = :idProduto ");
        }
        jpql.append(" )");

        Query queryInstancias = this.entityManager.createQuery(jpql.toString());
        queryInstancias.setParameter(ConstantesUtil.KEY_ID_DOSSIE_PRODUTO, idDossieProduto);
        queryInstancias.setParameter("idGarantia", idGarantia);
        if (idProduto != null) {
            queryInstancias.setParameter(ConstantesUtil.KEY_ID_PRODUTO, idProduto);
        }

        queryInstancias.executeUpdate();

        jpql = new StringBuilder();
        jpql.append(" DELETE FROM RespostaDossie rd ");
        jpql.append(" WHERE rd.dossieProduto.id = :idDossieProduto ");
        jpql.append(" AND rd.garantiaInformada IN ( ");
        jpql.append("   SELECT gi FROM GarantiaInformada gi ");
        jpql.append("   WHERE gi.dossieProduto.id = :idDossieProduto ");
        jpql.append("   AND gi.garantia.id = :idGarantia ");
        if (idProduto != null) {
            jpql.append("   AND gi.produto.id = :idProduto ");
        }
        jpql.append(" )");

        Query queryRespostas = this.entityManager.createQuery(jpql.toString());
        queryRespostas.setParameter(ConstantesUtil.KEY_ID_DOSSIE_PRODUTO, idDossieProduto);
        queryRespostas.setParameter("idGarantia", idGarantia);
        if (idProduto != null) {
            queryRespostas.setParameter(ConstantesUtil.KEY_ID_PRODUTO, idProduto);
        }

        queryRespostas.executeUpdate();

        jpql = new StringBuilder();
        jpql.append(" DELETE FROM GarantiaInformada gi ");
        jpql.append(" WHERE gi.dossieProduto.id = :idDossieProduto ");
        jpql.append(" AND gi.garantia.id = :idGarantia ");
        if (idProduto != null) {
            jpql.append(" AND gi.produto.id = :idProduto ");
        }

        Query queryGarantiaInformada = this.entityManager.createQuery(jpql.toString());
        queryGarantiaInformada.setParameter(ConstantesUtil.KEY_ID_DOSSIE_PRODUTO, idDossieProduto);
        queryGarantiaInformada.setParameter("idGarantia", idGarantia);
        if (idProduto != null) {
            queryGarantiaInformada.setParameter(ConstantesUtil.KEY_ID_PRODUTO, idProduto);
        }

        queryGarantiaInformada.executeUpdate();
    }

    // ************** VINCULO DE PRODUTOS ***************//

    public ProdutoDossie criaVinculoProduto(DossieProduto dossieProduto, Produto produto) {
        ProdutoDossie produtoDossie = new ProdutoDossie();
        produtoDossie.setProduto(produto);
        produtoDossie.setDossieProduto(dossieProduto);

        // Registra produto dossiê associada ao dossiê de produto
        this.entityManager.persist(produtoDossie);

        return produtoDossie;
    }

    /**
     * Exclui os registros das instancias de documento vinculadas ao dossiê de produto informado, relacionaas aos elementos de conteudo que estão vinculados ao produto informado, em seguida remove a
     * relação entre o produto e o dossiê de produto
     *
     * @param idDossieProduto - Identificador do Dossiê de Produto
     * @param idProduto - Identificador do Produto
     */
    public void removeVinculoProduto(Long idDossieProduto, Integer idProduto) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" DELETE FROM InstanciaDocumento instDoc1 ");
        jpql.append(" WHERE instDoc1.dossieProduto.id = :idDossieProduto ");
        jpql.append(" AND elementoConteudo IN ( ");
        jpql.append("   SELECT ec FROM ElementoConteudo ec WHERE ec.produto.id = :idProduto");
        jpql.append(" )");

        Query queryInstancias = this.entityManager.createQuery(jpql.toString());
        queryInstancias.setParameter(ConstantesUtil.KEY_ID_DOSSIE_PRODUTO, idDossieProduto);
        queryInstancias.setParameter(ConstantesUtil.KEY_ID_PRODUTO, idProduto);

        queryInstancias.executeUpdate();

        jpql = new StringBuilder();
        jpql.append(" DELETE FROM RespostaDossie rd ");
        jpql.append(" WHERE rd.dossieProduto.id = :idDossieProduto ");
        jpql.append(" AND produtoDossie IN ( ");
        jpql.append("   SELECT pd FROM ProdutoDossie pd WHERE pd.produto.id = :idProduto");
        jpql.append(" )");

        Query queryRespostas = this.entityManager.createQuery(jpql.toString());
        queryRespostas.setParameter(ConstantesUtil.KEY_ID_DOSSIE_PRODUTO, idDossieProduto);
        queryRespostas.setParameter(ConstantesUtil.KEY_ID_PRODUTO, idProduto);

        queryRespostas.executeUpdate();

        jpql = new StringBuilder();
        jpql.append(" DELETE FROM ProdutoDossie pd ");
        jpql.append(" WHERE pd.dossieProduto.id = :idDossieProduto ");
        jpql.append(" AND pd.produto.id = :idProduto ");

        Query queryProdutoDossie = this.entityManager.createQuery(jpql.toString());
        queryProdutoDossie.setParameter(ConstantesUtil.KEY_ID_DOSSIE_PRODUTO, idDossieProduto);
        queryProdutoDossie.setParameter(ConstantesUtil.KEY_ID_PRODUTO, idProduto);

        queryProdutoDossie.executeUpdate();
    }

    // ************** INSTANCIAS DE DOCUMENTOS ***************//
    // As instâncias associadas a elementos de conteudo, podem ser relativas a um produto, ao processo gerador de dossiê ou ao processo fase

    public void insereDocumentosElementoConteudoProcesso(Canal canal, DossieProduto dossieProduto, List<ElementoConteudoDTO> elementosConteudoDTO) {
        Processo processoOrigem = this.processoServico.getById(dossieProduto.getProcesso().getId());
        ProcessoFaseDossie processoFaseAtual = dossieProduto.getProcessosFaseDossie().stream()
                .max(Comparator.comparing(ProcessoFaseDossie::getId)).get();
        Processo processoFase = this.processoServico.getById(processoFaseAtual.getProcessoFase().getId());

        // Cria registro do documento enviado para o elemento de conteudo do processo e vincula instancia do documento junto ao dossiê do produto
        if (elementosConteudoDTO != null) {
            elementosConteudoDTO.forEach(ecp -> {
                OrigemDocumentoEnum origemDocumentoEnum = ecp.getDocumentoDTO().getOrigemDocumentoEnum();
                List<AtributoDocumento> atributosDocumento = ecp.getDocumentoDTO().getAtributosDocumento().stream().map(ad -> {
                    AtributoDocumento atributoDocumento = new AtributoDocumento();
                    atributoDocumento.setDescricao(ad.getChave());
                    atributoDocumento.setConteudo(ad.getValor());
                    atributoDocumento.setAcertoManual(Boolean.TRUE);
                    return atributoDocumento;
                }).collect(Collectors.toList());

                // Verifica se o elemento de conteudo esta vinculado ao processo fase definido no dossiê de produto
                ElementoConteudo elementoConteudo = processoFase.getElementosConteudo().stream()
                                                                .filter(ec -> ec.getId().equals(ecp.getIdElemento()))
                                                                .findFirst().orElseGet(() ->
                // Caso não encontre no processo fase, verifica se o elemento de conteudo esta
                // vinculado ao processo origem definido no dossiê de produto
                processoOrigem.getElementosConteudo().stream()
                              .filter(ec -> ec.getId().equals(ecp.getIdElemento()))
                              .findFirst().orElseThrow(() -> {
                                  // Caso não encontre em nenhum dos dois lança uma exceção para o elemento não localizado.
                                  String mensagem = MessageFormat.format("DPVH.iDECP.001 - Elemento de conteudo vinculado ao processo não localizado para identificador fornecido. ID Processo Dossie = {0} | ID Processo Fase = {1} | ID Elemento Conteudo = {2}", processoOrigem.getId(), processoFase.getId(), ecp.getIdElemento());
                                  return new SimtrRequisicaoException(mensagem);
                              }));

                if (elementoConteudo.getTipoDocumento() == null) {
                    String mensagem = MessageFormat.format("DPVH.iDECP.002 - O Elemento de Conteudo indicado para o processo não possui caracteristica de vinculação de documentos. Elemento de Conteudo = {0}", ecp.getIdElemento());
                    throw new SimtrRequisicaoException(mensagem);
                }

                if (!elementoConteudo.getTipoDocumento().getId().equals(ecp.getDocumentoDTO().getCodigoTipoDocumento())) {
                    Integer identificadorTipoEnviado = ecp.getDocumentoDTO().getCodigoTipoDocumento();
                    Integer identificadorTipoEsperado = elementoConteudo.getTipoDocumento().getId();
                    String mensagem = MessageFormat.format("DPVH.iDECP.003 - Tipo de Documento enviado é divergente do tipo de documento definido no Elemento de Conteudo do processo. Elemento de Conteudo = {0} | Tipo Enviado = {1} | Tipo Esperado = {2}", ecp.getIdElemento(), identificadorTipoEnviado, identificadorTipoEsperado);
                    throw new SimtrRequisicaoException(mensagem);
                }

                // Captura o tipo de documento pre carregado para evitar problema de LazyException
                TipoDocumento tipoDocumento = this.tipoDocumentoServico.getById(ecp.getDocumentoDTO().getCodigoTipoDocumento());

                // Caso o binario não tenha sido encaminhado e o documento não gera minuta, levanta uma exceção indicando o problema
                if(ecp.getDocumentoDTO().getBinario() == null && tipoDocumento.getNomeArquivoMinuta() == null) {
                    String mensagem = MessageFormat.format("DPVH.iDECP.004 - A tipologia documental informada para o processo não define geração de minuta e o binario do documento não foi encaminhado. ID Processo Dossie = {0} | ID Processo Fase = {1} | ID Elemento Conteudo = {2} | ID Tipo Documento: {3} | Tipologia: {4} | Nome: {5}", processoOrigem.getId(), processoFase.getId(), ecp.getIdElemento(), tipoDocumento.getId(), tipoDocumento.getCodigoTipologia(), tipoDocumento.getNome());
                    throw new SimtrRequisicaoException(mensagem);
                }
                
                // Captura o formato de conteudo do documento
                FormatoConteudoEnum formatoConteudoEnum = FormatoConteudoEnum.getByMimeType(ecp.getDocumentoDTO().getMimeType());

                // Prepara o documento para ser persistido
                Documento documentoProcesso = this.documentoServico.prototype(canal, Boolean.FALSE, tipoDocumento, TemporalidadeDocumentoEnum.VALIDO, origemDocumentoEnum, formatoConteudoEnum, atributosDocumento, ecp.getDocumentoDTO()
                                                                                                                                                                                                                       .getBinario());

                // Persiste o documento e armazena o conteudo no GED
                this.documentoServico.insereDocumentoOperacaoNegocio(dossieProduto.getId(), documentoProcesso);

                // Vincula o documento com o dossiê de produto criado com o elemento de conteudo através de uma instancia do documento e cria a situação inicial da instância.
                this.registraInstanciaDocumentoElementoConteudo(dossieProduto, documentoProcesso, elementoConteudo, null);
            });
        }
    }

    public void insereDocumentosElementoConteudoProduto(Canal canal, DossieProduto dossieProduto, ProdutoContratadoDTO produtoContratadoDTO) {
        Produto produto;
        if (produtoContratadoDTO.getIdProduto() != null) {
            produto = this.produtoServico.getById(produtoContratadoDTO.getIdProduto(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
        } else {
            produto = this.produtoServico.getByOperacaoModalidade(produtoContratadoDTO.getOperacao(), produtoContratadoDTO.getModalidade(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
        }

        // Cria registro do documento enviado para o elemento de conteudo do produto contratado e vincula instancia do documento junto ao dossiê do produto
        if (produtoContratadoDTO.getElementosConteudoDTO() != null) {
            produtoContratadoDTO.getElementosConteudoDTO().forEach(ecpc -> {
                // Captura o tipo de documento pre carregado para evitar problema de LazyException
                TipoDocumento tipoDocumento = this.tipoDocumentoServico.getById(ecpc.getDocumentoDTO().getCodigoTipoDocumento());

                OrigemDocumentoEnum origemDocumentoEnum = ecpc.getDocumentoDTO().getOrigemDocumentoEnum();
                List<AtributoDocumento> atributosDocumento = ecpc.getDocumentoDTO().getAtributosDocumento().stream().map(ad -> {
                    return preencheAtributosDocumento(ad, tipoDocumento);
                }).collect(Collectors.toList());

                ElementoConteudo elementoConteudo = produto.getElementosConteudo().stream()
                                                           .filter(ec -> ec.getId().equals(ecpc.getIdElemento()))
                                                           .findFirst().orElseThrow(() -> {
                                                               String mensagem = MessageFormat.format("DPVH.iDECP.001 - Elemento de conteudo vinculado ao produto não localizado para identificador fornecido. ID Produto = {0} | ID Elemento Conteudo = {1}", produtoContratadoDTO.getIdProduto(), ecpc.getIdElemento());
                                                               return new SimtrRequisicaoException(mensagem);
                                                           });

                if (!elementoConteudo.getTipoDocumento().getId().equals(ecpc.getDocumentoDTO().getCodigoTipoDocumento())) {
                    Integer identificadorTipoEnviado = ecpc.getDocumentoDTO().getCodigoTipoDocumento();
                    Integer identificadorTipoEsperado = elementoConteudo.getTipoDocumento().getId();
                    String mensagem = MessageFormat.format("DPVH.iDECP.002 - Tipo de Documento enviado é divergente do tipo de documento definido no Elemento de Conteudo do produto. Elemento de Conteudo = {0} | Tipo Enviado = {1} | Tipo Esperado = {2}", ecpc.getIdElemento(), identificadorTipoEnviado, identificadorTipoEsperado);
                    throw new SimtrRequisicaoException(mensagem);
                }

                // Caso o binario não tenha sido encaminhado e o documento não gera minuta, levanta uma exceção indicando o problema
                if(ecpc.getDocumentoDTO().getBinario() == null && tipoDocumento.getNomeArquivoMinuta() == null) {
                    String mensagem = MessageFormat.format("DPVH.iDECP.003 - A tipologia documental informada para o produto não define geração de minuta e o binario do documento não foi encaminhado. ID Produto = {0} | ID Elemento Conteudo = {1} | ID Tipo Documento: {2} | Tipologia: {3} | Nome: {4}", produtoContratadoDTO.getIdProduto(), ecpc.getIdElemento(), tipoDocumento.getId(), tipoDocumento.getCodigoTipologia(), tipoDocumento.getNome());
                    throw new SimtrRequisicaoException(mensagem);
                }
                
                // Captura o formato de conteudo do documento
                FormatoConteudoEnum formatoConteudoEnum = ecpc.getDocumentoDTO().getMimeType() == null ? null : FormatoConteudoEnum.getByMimeType(ecpc.getDocumentoDTO().getMimeType());

                // Prepara o documento para ser persistido
                String binario = ecpc.getDocumentoDTO().getBinario();
                Documento documentoProduto = this.documentoServico.prototype(canal, Boolean.FALSE, tipoDocumento, TemporalidadeDocumentoEnum.VALIDO, origemDocumentoEnum, formatoConteudoEnum, atributosDocumento, binario);

                // Persiste o documento e armazena o conteudo no GED
                this.documentoServico.insereDocumentoOperacaoNegocio(dossieProduto.getId(), documentoProduto);

                // Vincula o documento com o dossiê de produto criado e com o elemento de conteudo informado através de uma instancia do documento e cria a situação inicial da
                // instância.
                this.registraInstanciaDocumentoElementoConteudo(dossieProduto, documentoProduto, elementoConteudo, null);
            });
        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public void insereDocumentosGarantiaInformada(Canal canal, DossieProduto dossieProduto, GarantiaInformada garantiaInformada, GarantiaInformadaDTO garantiaInformadaDTO) {
        Garantia garantia = this.garantiaServico.getById(garantiaInformadaDTO.getIdentificadorGarantia(), Boolean.FALSE, Boolean.TRUE, Boolean.TRUE);

        // Cria registro do documento enviado para o elemento de conteudo do produto contratado e vincula instancia do documento reutilizado
        if (garantiaInformadaDTO.getDocumentosUtilizados() != null && garantiaInformadaDTO.getDocumentosUtilizados().size() > 0) {
            garantiaInformadaDTO.getDocumentosUtilizados().forEach(du -> {
                Documento documentoGarantia = this.documentoServico.getById(du, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);

                garantia.getDocumentosGarantia().stream().filter(dg -> {
                    // Verifica se o registro de documento de garantia define um tipo igual ao tipo informado na indicação do documento
                    boolean criterioTipoDocumento = dg.getTipoDocumento() != null && dg.getTipoDocumento().getId().equals(documentoGarantia.getTipoDocumento().getId());
                    // Verifica se o registro de documento de garantia define uma função documento que contem um tipo de documento igual ao informado na indicação do documento
                    boolean criterioFuncaoDocumental = dg.getFuncaoDocumental() != null && dg.getFuncaoDocumental().getTiposDocumento().stream()
                                                                                             .filter(td -> td.getId().equals(documentoGarantia.getTipoDocumento().getId()))
                                                                                             .findAny().isPresent();
                    return (criterioTipoDocumento || criterioFuncaoDocumental);
                }).findAny().orElseThrow(() -> {
                    Integer identificadorTipoGarantia = documentoGarantia.getTipoDocumento().getId();
                    String mensagem = MessageFormat.format("DPVH.iDGI.001 - Tipo de Documento vinculado a comprovação da garantia não é valido. ID Garantia = {0} | ID Tipo Documento = {1}", garantiaInformadaDTO.getIdentificadorGarantia(), identificadorTipoGarantia);
                    return new SimtrRequisicaoException(mensagem);
                });

                SituacaoDocumento situacaoDocumentoFinal = null;

                if (!dossieProduto.getProcesso().getExigenciaValidacao()) {
                    boolean aplicaReuso = aplicaReusoSituacaoConforme(documentoGarantia, dossieProduto.getProcesso());

                    if (aplicaReuso) {
                        situacaoDocumentoFinal = this.situacaoDocumentoServico.getBySituacaoDocumentoEnum(SituacaoDocumentoEnum.CONFORME);
                    }
                }

                this.registraInstanciaDocumentoGarantiaInformada(dossieProduto, documentoGarantia, garantiaInformada, situacaoDocumentoFinal);
            });
        }

        // Cria registro do documento enviado para o elemento de conteudo do produto contratado e vincula instancia do documento junto ao dossiê do produto
        if (garantiaInformadaDTO.getDocumentosNovosDTO() != null) {
            for (DocumentoDTO documentoDTO : garantiaInformadaDTO.getDocumentosNovosDTO()) {

                // Captura o tipo de documento pre carregado para evitar problema de LazyException
                TipoDocumento tipoDocumento = this.tipoDocumentoServico.getById(documentoDTO.getCodigoTipoDocumento());

                OrigemDocumentoEnum origemDocumentoEnum = documentoDTO.getOrigemDocumentoEnum();
                List<AtributoDocumento> atributosDocumento = documentoDTO.getAtributosDocumento().stream().map(ad -> {
                    return preencheAtributosDocumento(ad, tipoDocumento);
                }).collect(Collectors.toList());

                garantia.getDocumentosGarantia().stream().filter(dg -> {
                    // Verifica se o registro de documento de garantia define um tipo igual ao informado na indicação do documento
                    boolean criterioTipoDocumento = dg.getTipoDocumento() != null && dg.getTipoDocumento().getId().equals(documentoDTO.getCodigoTipoDocumento());
                    // Verifica se o registro de documento de garantia define uma função documento que contem um tipo de documento igual ao informado na indicação do documento
                    boolean criterioFuncaoDocumental = dg.getFuncaoDocumental() != null && dg.getFuncaoDocumental().getTiposDocumento().stream()
                                                                                             .filter(td -> td.getId().equals(documentoDTO.getCodigoTipoDocumento()))
                                                                                             .findAny().isPresent();
                    return (criterioTipoDocumento || criterioFuncaoDocumental);

                }).findAny().orElseThrow(() -> {
                    String mensagem = MessageFormat.format("DPVH.iDGI.002 - Tipo de Documento vinculado a comprovação da garantia não é valido. ID Garantia = {0} | ID Tipo Documento = {1}", garantiaInformadaDTO.getIdentificadorGarantia(), documentoDTO.getCodigoTipoDocumento());
                    return new SimtrRequisicaoException(mensagem);
                });
                
                // Caso o binario não tenha sido encaminhado e o documento não gera minuta, levanta uma exceção indicando o problema
                if(documentoDTO.getBinario() == null && tipoDocumento.getNomeArquivoMinuta() == null) {
                    String mensagem = MessageFormat.format("DPVH.iDGI.003 - A tipologia documental informada para a garantia não define geração de minuta e o binario do documento não foi encaminhado. ID Garantia = {0} | ID Tipo Documento: {1} | Tipologia: {2} | Nome: {3}", garantiaInformadaDTO.getIdentificadorGarantia(), tipoDocumento.getId(), tipoDocumento.getCodigoTipologia(), tipoDocumento.getNome());
                    throw new SimtrRequisicaoException(mensagem);
                }

                // Captura o formato de conteudo do documento
                FormatoConteudoEnum formatoConteudoEnum = documentoDTO.getMimeType() == null ? null : FormatoConteudoEnum.getByMimeType(documentoDTO.getMimeType());

                // Prepara o documento para ser persistido
                Documento documentoProduto = this.documentoServico.prototype(canal, Boolean.FALSE, tipoDocumento, TemporalidadeDocumentoEnum.VALIDO, origemDocumentoEnum, formatoConteudoEnum, atributosDocumento, documentoDTO.getBinario());

                // Persiste o documento e armazena o conteudo no GED
                this.documentoServico.insereDocumentoOperacaoNegocio(dossieProduto.getId(), documentoProduto);

                // Vincula o documento com o dossiê de produto criado e com a garantia informada através de uma instancia do documento e cria a situação inicial da instância.
                this.registraInstanciaDocumentoGarantiaInformada(dossieProduto, documentoProduto, garantiaInformada, null);
            }
        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public void insereDocumentosVinculoDossieCliente(Canal canal, DossieProduto dossieProduto, DossieClienteProduto dossieClienteProduto, VinculoPessoaDTO vinculoPessoaDTO) {
        Processo processoOrigem = this.processoServico.getById(dossieProduto.getProcesso().getId());
        DossieCliente dossieCliente = this.dossieClienteServico.getById(dossieClienteProduto.getDossieCliente().getId(), Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
        // Cria registro do documento enviado para o elemento de conteudo do produto contratado e vincula instancia do documento junto ao dossiê do produto
        if (vinculoPessoaDTO.getDocumentosUtilizados() != null) {
            vinculoPessoaDTO.getDocumentosUtilizados().forEach(du -> {

                // Captura o documento para ser vinculado ao dossiê de produto
                Documento documentoVinculo = this.documentoServico.getById(du, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);

                processoOrigem.getProcessoDocumentos().stream().filter(pd -> {
                    // Capturado objetos para evitar problemas de LazyException
                    FuncaoDocumental funcaoDocumental = pd.getFuncaoDocumental() == null ? null : this.funcaoDocumentalServico.getById(pd.getFuncaoDocumental().getId());
                    TipoDocumento tipoDocumento = pd.getTipoDocumento() == null ? null : this.tipoDocumentoServico.getById(pd.getTipoDocumento().getId());

                    // Verifica se o registro de documento de garantia define um tipo igual ao tipo informado na indicação do documento
                    return ((tipoDocumento != null && tipoDocumento.getId().equals(documentoVinculo.getTipoDocumento().getId()))
                            // Verifica se o registro de documento de garantia define uma função documento que contem um tipo de documento tipo igual ao tipo informado na indicação do
                            // documento
                            || (funcaoDocumental != null && funcaoDocumental.getTiposDocumento().stream()
                                                                            .filter(td -> td.getId().equals(documentoVinculo.getTipoDocumento().getId()))
                                                                            .findAny().isPresent()));
                }).findAny().orElseThrow(() -> {
                    Object[] parametros = new Object[] {
                        vinculoPessoaDTO.getTipoRelacionamento(),
                        documentoVinculo.getId(),
                        documentoVinculo.getTipoDocumento().getNome()
                    };
                    String mensagem = MessageFormat.format("DPVH.iDVDC.001 - Tipo de Documento associado ao documento informado para o tipo de relacionamento definido não é valido. Tipo Relacionamento = {0} | ID Documento Utilizado = {1} | Tipo Documento = {2}", parametros);
                    return new SimtrRequisicaoException(mensagem);
                });

                SituacaoDocumento situacaoDocumentoFinal = null;

                if (!dossieProduto.getProcesso().getExigenciaValidacao()) {
                    boolean aplicaReuso = aplicaReusoSituacaoConforme(documentoVinculo, dossieProduto.getProcesso());

                    if (aplicaReuso) {
                        situacaoDocumentoFinal = this.situacaoDocumentoServico.getBySituacaoDocumentoEnum(SituacaoDocumentoEnum.CONFORME);
                    }
                }

                // Vincula o documento com o dossiê de produto criado e com o elemento de conteudo informado através de uma instancia do documento e cria a situação inicial da
                // instância.
                this.registraInstanciaDocumentoCliente(dossieProduto, documentoVinculo, dossieClienteProduto, situacaoDocumentoFinal);
            });
        }

        // Cria registro do documento novo enviado para vinculo com a associação entre o dossiê de cliente e dossiê de produto Vincula a instancia do documento junto ao
        // dossiê do produto
        if (vinculoPessoaDTO.getDocumentosNovosDTO() != null) {
            vinculoPessoaDTO.getDocumentosNovosDTO().forEach(dn -> {
                // Captura o tipo de documento pre carregado para evitar problema de LazyException
                TipoDocumento tipoDocumentoNovo = this.tipoDocumentoServico.getById(dn.getCodigoTipoDocumento());

                List<AtributoDocumento> atributosDocumento = dn.getAtributosDocumento().stream().map(ad -> {
                    return preencheAtributosDocumento(ad, tipoDocumentoNovo);
                }).collect(Collectors.toList());

                processoOrigem.getProcessoDocumentos().stream().filter(pd -> {
                    // Capturado objetos para evitar problemas de LazyException
                    FuncaoDocumental funcaoDocumental = pd.getFuncaoDocumental() == null ? null : this.funcaoDocumentalServico.getById(pd.getFuncaoDocumental().getId());
                    TipoDocumento tipoDocumento = pd.getTipoDocumento() == null ? null : this.tipoDocumentoServico.getById(pd.getTipoDocumento().getId());

                    // Verifica se o registro de documento de garantia define um tipo igual ao tipo informado na indicação do documento
                    boolean criterioTipoDocumento = tipoDocumento != null && tipoDocumento.getId().equals(tipoDocumentoNovo.getId());
                    // Verifica se o registro de documento de garantia define uma função documento que contem um tipo de documento igual ao informado na indicação do documento
                    boolean criterioFuncaoDocumental = funcaoDocumental != null && funcaoDocumental.getTiposDocumento().stream()
                                                                                                   .filter(td -> td.getId().equals(tipoDocumentoNovo.getId()))
                                                                                                   .findAny().isPresent();
                    return (criterioTipoDocumento || criterioFuncaoDocumental);
                }).findAny().orElseThrow(() -> {
                    Object[] parametros = new Object[] {
                        vinculoPessoaDTO.getTipoRelacionamento(),
                        dn.getCodigoTipoDocumento()
                    };
                    String mensagem = MessageFormat.format("DPVH.iDVDC.002 - Tipo de Documento associado ao tipo de Relacionamento definido não é valido. Tipo Relacionamento = {0} | ID Tipo Documento = {1}", parametros);
                    return new SimtrRequisicaoException(mensagem);
                });

                //Caso o binario não tenha sido encaminhado e o documento não gera minuta, levanta uma exceção indicando o problema
                if(dn.getBinario() == null && tipoDocumentoNovo.getNomeArquivoMinuta() == null) {
                    String mensagem = MessageFormat.format("DPVH.iDVDC.003 - A tipologia documental informada para o vinculo de pessoa não define geração de minuta e o binario do documento não foi encaminhado. Tipo Relacionamento = {0} | ID Tipo Documento = {1} | Tipologia: {2} | Nome: {3}", vinculoPessoaDTO.getTipoRelacionamento(), tipoDocumentoNovo.getId(), tipoDocumentoNovo.getCodigoTipologia(), tipoDocumentoNovo.getNome());
                    throw new SimtrRequisicaoException(mensagem);
                }
                
                // Captura o formato de conteudo do documento
                FormatoConteudoEnum formatoConteudoEnum = dn.getMimeType() == null ? null : FormatoConteudoEnum.getByMimeType(dn.getMimeType());

                // Prepara o documento para ser persistido
                Documento documentoVinculo = this.documentoServico.prototype(canal, Boolean.FALSE, tipoDocumentoNovo, TemporalidadeDocumentoEnum.VALIDO, dn.getOrigemDocumentoEnum(), formatoConteudoEnum, atributosDocumento, dn.getBinario());

                // Persiste o documento e armazena o conteudo no GED
                this.documentoServico.insereDocumentoClienteNegocio(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), documentoVinculo);

                // Vincula o documento com o dossiê de cliente criado.
                dossieCliente.addDocumentos(documentoVinculo);
                this.dossieClienteServico.update(dossieCliente);

                // Vincula o documento com o dossiê de produto criado e com o elemento de conteudo informado através de uma instancia do documento e cria a situação inicial da
                // instância.
                this.registraInstanciaDocumentoCliente(dossieProduto, documentoVinculo, dossieClienteProduto, null);
            });
        }
    }

    // ************** FORMULARIOS ***************//

    public void vinculaRespostaFormularioDossieProduto(DossieProduto dossieProduto, CampoFormulario campoFormulario, String respostaAberta, List<String> respostasObjetivas, DossieClienteProduto dossieClienteProduto, ProdutoDossie produtoDossie, GarantiaInformada garantiaInformada) {

        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT rd FROM RespostaDossie rd ");
        jpql.append(" LEFT JOIN FETCH rd.opcoesCampo oc ");
        jpql.append(" LEFT JOIN FETCH rd.campoFormulario cf ");
        jpql.append(" WHERE rd.dossieProduto = :dossieProduto ");
        jpql.append(" AND cf = :campoFormulario ");

        TypedQuery<RespostaDossie> query = this.entityManager.createQuery(jpql.toString(), RespostaDossie.class);
        query.setParameter("dossieProduto", dossieProduto);
        query.setParameter("campoFormulario", campoFormulario);

        RespostaDossie respostaDossie;
        try {
            respostaDossie = query.getSingleResult();
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            respostaDossie = new RespostaDossie();
            respostaDossie.setDossieProduto(dossieProduto);
            respostaDossie.setCampoFormulario(campoFormulario);
            if (dossieClienteProduto != null) {
                respostaDossie.setDossieClienteProduto(dossieClienteProduto);
            } else if (produtoDossie != null) {
                respostaDossie.setProdutoDossie(produtoDossie);
            } else if (garantiaInformada != null) {
                respostaDossie.setGarantiaInformada(garantiaInformada);
            } else {
                ProcessoFaseDossie processoFaseAtual = dossieProduto.getProcessosFaseDossie().stream()
                        .max(Comparator.comparing(ProcessoFaseDossie::getId)).get();
                respostaDossie.setProcessoFase(processoFaseAtual.getProcessoFase());
            }
        }
        if ((Objects.isNull(campoFormulario.getCampoEntrada().getOpcoesCampo())) || (campoFormulario.getCampoEntrada().getOpcoesCampo().isEmpty())) {
            respostaDossie.setRespostaAberta(respostaAberta);
        } else {
            respostaDossie.getOpcoesCampo().clear();
            Set<OpcaoCampo> opcoesSelecionadas = new HashSet<>();
            if (respostasObjetivas != null) {
                respostasObjetivas.forEach(valorCampo -> campoFormulario.getCampoEntrada().getOpcoesCampo().stream()
                                                                        .filter(oc -> oc.getValue().equals(valorCampo))
                                                                        .findFirst()
                                                                        .ifPresent(opcaoCampo -> opcoesSelecionadas.add(opcaoCampo)));
                respostaDossie.setOpcoesCampo(opcoesSelecionadas);
            }
        }

        // Registra a resposta do formulario associada ao dossiê de produto
        this.entityManager.persist(respostaDossie);

    }

    // *************** METODOS PRIVADOS **************//

    private void registraInstanciaDocumentoCliente(DossieProduto dossieProduto, Documento documento, DossieClienteProduto dossieClienteProduto, SituacaoDocumento situacaoFinal) {
        InstanciaDocumento instanciaDocumento = new InstanciaDocumento();
        instanciaDocumento.setDocumento(documento);
        instanciaDocumento.setDossieProduto(dossieProduto);
        instanciaDocumento.setDossieClienteProduto(dossieClienteProduto);

        // Registra a instancia do documento associada ao dossiê de produto
        this.entityManager.persist(instanciaDocumento);

        // Cria a situação inicial da instancia do documento
        SituacaoDocumento situacaoDocumentoCriado = this.situacaoDocumentoServico.getBySituacaoDocumentoEnum(SituacaoDocumentoEnum.CRIADO);
        this.situacaoDocumentoServico.registraNovaSituacaoInstanciaDocumento(instanciaDocumento, situacaoDocumentoCriado);

        // Caso seja enviada uma situação a ser definida como situação final, registra a nova situação a instancia do documento.
        if (situacaoFinal != null) {
            this.situacaoDocumentoServico.registraNovaSituacaoInstanciaDocumento(instanciaDocumento, situacaoFinal);
        }
    }

    private void registraInstanciaDocumentoGarantiaInformada(DossieProduto dossieProduto, Documento documento, GarantiaInformada garantiaInformada, SituacaoDocumento situacaoFinal) {
        SituacaoDocumento situacaoDocumentoCriado = this.situacaoDocumentoServico.getBySituacaoDocumentoEnum(SituacaoDocumentoEnum.CRIADO);
        SituacaoDocumento situacaoDocumentoSubstituido = this.situacaoDocumentoServico.getBySituacaoDocumentoEnum(SituacaoDocumentoEnum.SUBSTITUIDO);

        // 1 - Captura todas as instancias de documento do dossiê
        // 2 - Filtra apenas aquelas que são do elemento conteudo de vinculação
        // 3 - Filtra aquelas que não possuem nenhuma situação tipo final (Conforme, Recusado, Rejeitado, Reprovado, Substituido);
        // 4 - Para cada uma das instancias localizadas, insere uma situação de substituição para identificar os documentos alterados.
        // OBS - A tendência é que só exista uma instancia nessa situação, mas caso haja mais de uma o sistema corrije a situação.
        dossieProduto.getInstanciasDocumento().stream()
                     .filter(id -> (id.getGarantiaInformada() != null) && (id.getGarantiaInformada().equals(garantiaInformada)))
                     .filter(id -> !id.getSituacoesInstanciaDocumento().stream()
                                      .filter(sid -> sid.getSituacaoDocumento().getSituacaoFinal())
                                      .findAny().isPresent())
                     .forEach(instanciaSubstituida -> this.situacaoDocumentoServico.registraNovaSituacaoInstanciaDocumento(instanciaSubstituida, situacaoDocumentoSubstituido));

        InstanciaDocumento instanciaDocumento = new InstanciaDocumento();
        instanciaDocumento.setDocumento(documento);
        instanciaDocumento.setDossieProduto(dossieProduto);
        instanciaDocumento.setGarantiaInformada(garantiaInformada);

        // Registra a instancia do documento associada ao dossiê de produto
        this.entityManager.persist(instanciaDocumento);

        // Cria a situação inicial da instancia do documento
        this.situacaoDocumentoServico.registraNovaSituacaoInstanciaDocumento(instanciaDocumento, situacaoDocumentoCriado);

        // Caso seja enviada uma situação a ser definida como situação final, registra a nova situação a instancia do documento.
        if (situacaoFinal != null) {
            this.situacaoDocumentoServico.registraNovaSituacaoInstanciaDocumento(instanciaDocumento, situacaoFinal);
        }
    }

    private void registraInstanciaDocumentoElementoConteudo(DossieProduto dossieProduto, Documento documento, ElementoConteudo elementoConteudo, SituacaoDocumento situacaoFinal) {
        SituacaoDocumento situacaoDocumentoCriado = this.situacaoDocumentoServico.getBySituacaoDocumentoEnum(SituacaoDocumentoEnum.CRIADO);
        SituacaoDocumento situacaoDocumentoSubstituido = this.situacaoDocumentoServico.getBySituacaoDocumentoEnum(SituacaoDocumentoEnum.SUBSTITUIDO);

        // 1 - Captura todas as instancias de documento do dossiê;
        // 2 - Filtra aquelas que não possuem nenhuma situação do tipo final (Conforme, Recusado, Rejeitado, Reprovado, Substituido);
        // 3 - Filtra apenas aquelas que são do elemento conteudo de vinculação;
        // 4 - Para cada uma das instancias localizadas, insere uma situação de substituição para identificar os documentos alterados.
        // OBS - A tendência é que só exista uma instancia nessa situação, mas caso haja mais de uma o sistema corrije a situação.
        dossieProduto.getInstanciasDocumento().stream()
                     .filter(id -> ((id.getElementoConteudo() != null) && (id.getElementoConteudo().equals(elementoConteudo))))
                     .filter(id -> !id.getSituacoesInstanciaDocumento().stream()
                                      .filter(sid -> sid.getSituacaoDocumento().getSituacaoFinal())
                                      .findAny().isPresent())
                     .forEach(instanciaSubstituida -> this.situacaoDocumentoServico.registraNovaSituacaoInstanciaDocumento(instanciaSubstituida, situacaoDocumentoSubstituido));

        InstanciaDocumento instanciaDocumento = new InstanciaDocumento();
        instanciaDocumento.setDocumento(documento);
        instanciaDocumento.setDossieProduto(dossieProduto);
        instanciaDocumento.setElementoConteudo(elementoConteudo);

        // Registra a instancia do documento associada ao dossiê de produto
        this.entityManager.persist(instanciaDocumento);

        // Cria a situação inicial da instancia do documento
        this.situacaoDocumentoServico.registraNovaSituacaoInstanciaDocumento(instanciaDocumento, situacaoDocumentoCriado);

        // Caso seja enviada uma situação a ser definida como situação final, registra a nova situação a instancia do documento.
        if (situacaoFinal != null) {
            this.situacaoDocumentoServico.registraNovaSituacaoInstanciaDocumento(instanciaDocumento, situacaoFinal);
        }
    }

    /**
     * Método responsável por preencher os atributos do Documento. Verifica se o atributo do documento é um atributo objetivo, ou seja, contem valores multi valorados, preenchidos através das opções
     * selecionadas pelo cliente. Caso exista valores preenchidos o campo Conteudo do atributo não é preenchido, sendo assim, ficando uma lista de valores preenchidos em opções selecionadas. Caso
     * contrário o atributo sendo subjetivo, o campo conteudo é preenchido. Todo atributo documento deve ser esperado para extração, ou seja, mapeado na tabela de AtributosExtracao, sendo assim, para
     * cada atributo do documento é feito uma validação para confirmar se o Atributo é experado, caso não seja, uma exception é lançada.
     * 
     * 
     * @param atributoDocumentoDTO
     * @param Atributo extracao do tipo Documento, utilizado para validar todos se todos os atributos são validos
     * @return Atributo documento preenchido
     */
    private AtributoDocumento preencheAtributosDocumento(AtributoDocumentoDTO atributoDocumentoDTO, TipoDocumento tipoDocumento) {
        AtributoDocumento atributoDocumento = new AtributoDocumento();
        atributoDocumento.setDescricao(atributoDocumentoDTO.getChave());
        atributoDocumento.setAcertoManual(Boolean.TRUE);

        // Verifica se o AtributoDocumento é objetivo, caso positivo preenche as opções selecionadas
        List<String> atributosInvalidos = new ArrayList<String>();
        if (Objects.nonNull(atributoDocumentoDTO.getOpcoesSelecionadas()) && !atributoDocumentoDTO.getOpcoesSelecionadas().isEmpty()) {
            atributoDocumento.setOpcoesSelecionadas(atributoDocumentoDTO.getOpcoesSelecionadas().stream().map(valorOpcao -> {
                // Valida se o AtributoDocumento é um atributo experado pelo Documento
                if (!tipoDocumento.getAtributosExtracao().stream().filter(ae -> ae.getNomeAtributoDocumento().equals(atributoDocumentoDTO.getChave())).findAny().isPresent()) {
                    atributosInvalidos.add(atributoDocumentoDTO.getChave());
                }
                OpcaoSelecionada opcao = new OpcaoSelecionada();
                opcao.setAtributoDocumento(atributoDocumento);
                opcao.setDescricaoOpcao(atributoDocumentoDTO.getChave());
                opcao.setValorOpcao(valorOpcao);
                return opcao;
            }).collect(Collectors.toSet()));
        } else {
            // Caso não exista opções selecionadas significa que o campo é subjetivo e deve ser salvo no conteudo do Atributo Documento.
            atributoDocumento.setConteudo(atributoDocumentoDTO.getValor());
        }

        if (Objects.nonNull(atributosInvalidos) && !atributosInvalidos.isEmpty()) {
            String listaAtributos = atributosInvalidos.stream().collect(Collectors.joining(","));
            String mensagem = MessageFormat.format("DPS.pAD.001 - Atributo [{0}] do documento [{1}] não esta definido para este o tipo de documento.", listaAtributos, tipoDocumento.getNome());
            throw new SimtrRequisicaoException(mensagem);
        }

        return atributoDocumento;
    }

    /**
     * Método responsável por verificar se o documento possui alguma instância já validada como conforme e se o mesmo possui validade para novos negócios. Se essas condições foram atendidas, então a
     * nova instancia criada poderá ser criada ja com a situação conforme caso o processo não exija nova validação.
     *
     * @param documento Documento a ser verificado quanto a existência de instancia associada com situacao atual Conforme e data e hora de validade para novos negócio.
     * @return Retorna verdadeiro se o documento atendende a regra de reuso da situação conforme ou falso caso contrário
     */
    private boolean aplicaReusoSituacaoConforme(Documento documento, Processo processo) {

        // Verifica se o processo exige validação das instâncias de documento e caso positivo jé retorna a negativa de reuso da conformidade
        if (processo.getExigenciaValidacao()) {
            return false;
        }

        // Captura o tipo de situação do documento CONFORME
        SituacaoDocumento situacaoDocumentoConforme = this.situacaoDocumentoServico.getBySituacaoDocumentoEnum(SituacaoDocumentoEnum.CONFORME);

        // Verifica se o documento possui instâncias associadas que podem ser analisadas quanto a conformidade e se o documento possui validade para vinculação a novos negócios
        if ((documento.getInstanciasDocumento() != null) && (documento.getDataHoraValidade() == null || documento.getDataHoraValidade().after(Calendar.getInstance()))) {

            // Retorna verdadeiro se alguma das instâncias associadas possui a ultima situação definida como conforme
            // Essa regra funciona pois caso alguma instânia tivesse sido rejeitada, o documento já estaria com a data de validade definida e não entraria nesta condição
            return documento.getInstanciasDocumento().stream()
                            .anyMatch(instancia -> {
                                SituacaoInstanciaDocumento situacaoAtualInstancia = instancia.getSituacoesInstanciaDocumento().stream()
                                                                                             .max(Comparator.comparing(SituacaoInstanciaDocumento::getId))
                                                                                             .get();
                                return situacaoDocumentoConforme.equals(situacaoAtualInstancia.getSituacaoDocumento());
                            });
        }

        return false;
    }

}
