package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.pedesgo.arquitetura.siiso.dto.RetornoPessoasFisicasDTO;
import br.gov.caixa.pedesgo.arquitetura.siiso.dto.RetornoPessoasJuridicasDTO;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.pedesgo.arquitetura.util.UtilRest;
import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrPreCondicaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.vo.siric.RespostaAvaliacaoSiricVO;
import br.gov.caixa.simtr.controle.vo.siric.RespostaProdutoAvaliacaoSiricVO;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePF;
import br.gov.caixa.simtr.modelo.entidade.DossieClientePJ;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.PortePessoaJuridicaEnum;
import br.gov.caixa.simtr.modelo.enumerator.ResultadoAvaliacaoProdutoSIRICEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.siric.AvaliacaoSiricDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossiecliente.siric.ProdutoAvaliacaoSiricDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRTEC,
    ConstantesUtil.PERFIL_MTRAUD,
    ConstantesUtil.PERFIL_MTRDOSINT,
    ConstantesUtil.PERFIL_MTRDOSMTZ,
    ConstantesUtil.PERFIL_MTRDOSOPE,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE,
    ConstantesUtil.PERFIL_MTRSDNTTO,
    ConstantesUtil.PERFIL_MTRSDNTTG
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class DossieClienteServico extends AbstractService<DossieCliente, Long> {

    @Inject
    private EntityManager entityManager;
    
    @EJB
    private KeycloakService keycloakService;

    @EJB
    private CadastroCaixaServico cadastroCaixaServico;

    @EJB
    private CadastroReceitaServico cadastroReceitaServico;

    @EJB
    private CanalServico canalServico;

    @EJB
    private DocumentoServico documentoServico;

    @EJB
    private TipoDocumentoServico tipoDocumentoServico;

    private static final Logger LOGGER = Logger.getLogger(DossieClienteServico.class.getName());

    private static final String CLAUSULA_WHERE_ID = " WHERE dc.id = :id ";
    private static final String CLAUSULA_WHERE_CPF_CNPJ = " WHERE dc.cpfCnpj = :cpfCnpj AND dc.tipoPessoa = :tipoPessoa ";
    private static final String ID = "id";
    private static final String CPF_CNPJ = "cpfCnpj";
    private static final String TIPO_PESSOA = "tipoPessoa";

    /**
     * Utilizado para realizar a captura de um Dossiê de cliente baseado no ID do Dossiê de Cliente.
     *
     * @param id ID do Dossiê de Cliente quando ativado consulta por este atributo
     * @param vinculacoesNivelDocumental Indicador se as vinculações relativas ao nivel documental devem ser carregadas
     * @param vinculacoesDocumentos Indicador se as vinculações relativas aos documentos de cliente devem ser carregados
     * @param vinculacoesDossiesProduto Indicador se as vinculações relativas aos dossiês de produto com vinculos no dossiê de cliente devem ser carregados
     * @return Dossiê de Cliente carregado conforme parametros indicados ou nulo caso não seja localizado com base no ID informado.
     */
    public DossieCliente getById(Long id, boolean vinculacoesNivelDocumental, boolean vinculacoesDocumentos, boolean vinculacoesDossiesProduto) {
        return this.getByIdOrCpfCnpj(id, null, null, vinculacoesNivelDocumental, vinculacoesDocumentos, vinculacoesDossiesProduto);
    }

    /**
     * Utilizado para realizar a captura de um Dossiê de cliente baseado no CPF/CNPJ do cliente.
     *
     * @param cpfCnpj CPF/CNPJ do cliente quando ativado consulta por este atributo
     * @param tipoPessoaEnum Tipo de pessoa relacionada (F ou J)
     * @param vinculacoesNivelDocumental Indicador se as vinculações relativas ao nivel documental devem ser carregadas
     * @param vinculacoesDocumentos Indicador se as vinculações relativas aos documentos de cliente devem ser carregados
     * @param vinculacoesDossiesProduto Indicador se as vinculações relativas aos dossiês de produto com vinculos no dossiê de cliente devem ser carregados
     * @return Dossiê de Cliente carregado conforme parametros indicados ou nulo caso não seja localizado com base no CPF/CNPJ informados.
     */
    public DossieCliente getByCpfCnpj(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, boolean vinculacoesNivelDocumental, boolean vinculacoesDocumentos, boolean vinculacoesDossiesProduto) {

        DossieCliente dossieCliente = this.getByIdOrCpfCnpj(null, cpfCnpj, tipoPessoaEnum, vinculacoesNivelDocumental, vinculacoesDocumentos, vinculacoesDossiesProduto);

      if (TipoPessoaEnum.F.equals(tipoPessoaEnum)) {
            try {
                RetornoPessoasFisicasDTO sicpfResponseDTO = this.cadastroReceitaServico.consultaCadastroPF(cpfCnpj);
                DossieClientePF dossieClientePF = dossieCliente != null ? (DossieClientePF) dossieCliente : new DossieClientePF();
                dossieClientePF.setCpfCnpj(cpfCnpj);
                dossieClientePF.setNome(sicpfResponseDTO.getNomeContribuinte());
                dossieClientePF.setDataNascimento(sicpfResponseDTO.getDataNascimento());
                dossieClientePF.setNomeMae(sicpfResponseDTO.getNomeMae());

                // Caso o registro não exista realiz a persistência do mesmo,
                // caso contrario o entity manager se encarrega de persistir as atualizações
                if (dossieCliente == null) {
                    this.save(dossieClientePF);
                }
                return dossieClientePF;
            } catch (Exception ex) {
                String mensagem = MessageFormat.format("DCS.gBCC.001 Falha ao comunicar com Cadastro Receita PF. CPF = {0}", String.valueOf(cpfCnpj));
                LOGGER.log(Level.INFO, mensagem, ex);
            }
        } else if (TipoPessoaEnum.J.equals(tipoPessoaEnum)) {
            try {
                RetornoPessoasJuridicasDTO resposta = this.cadastroReceitaServico.consultaCadastroPJ(cpfCnpj);

                String nome = resposta.getEstabelecimento().getRazaoSocial();
                if (resposta.getEstabelecimento().getNomeFantasia().length() > 1) {
                    resposta.getEstabelecimento().getNomeFantasia();
                }

                String tipoPorte = resposta.getEstabelecimento().getTipoPorte();
                PortePessoaJuridicaEnum porte = tipoPorte == null ? PortePessoaJuridicaEnum.ND : PortePessoaJuridicaEnum.getByCodigo(Integer.valueOf(tipoPorte));

                DossieClientePJ dossieClientePJ = dossieCliente != null ? (DossieClientePJ) dossieCliente : new DossieClientePJ();
                dossieClientePJ.setCpfCnpj(cpfCnpj);
                dossieClientePJ.setNome(nome);
                dossieClientePJ.setRazaoSocial(resposta.getEstabelecimento().getRazaoSocial());
                dossieClientePJ.setDataFundacao(resposta.getEstabelecimento().getDataAbertura());
                dossieClientePJ.setPorte(porte);
                // Caso o registro não exista realiza a persistência do mesmo, caso contrario o entity manager se encarrega de persistir as atualizações
                if (dossieCliente == null) {
                    this.save(dossieClientePJ);
                }
                return dossieClientePJ;

            } catch (Exception ex) {
                String mensagem = MessageFormat.format("DCS.gBCC.001 Falha ao comunicar com Cadastro Receita PJ. CNPJ = {0}", String.valueOf(cpfCnpj));
                LOGGER.log(Level.INFO, mensagem, ex);
            }
        }
        return dossieCliente;
    }

    /**
     * Realiza a atualização de informações de Dossiê de Cliente conforme a definição submetida pelo objeto. Caso o mesmo tenha o valor definido aplica o mesmo na
     * atualização.
     *
     * @param id - Identificador do Dossiê que deseja alterar
     * @param dossieClientePatch - Objeto que representa as alterações que se deseja alicar
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public void aplicaPatch(Long id, DossieCliente dossieClientePatch) {
        // Resgata objeto persistente do banco
        DossieCliente dossieCliente = this.getById(id, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);

        // Atualiza informações comuns referente a tabela generica
        if (dossieClientePatch.getNome() != null) {
            dossieCliente.setNome(dossieClientePatch.getNome());
        }
        
        if(dossieClientePatch.getEmail() != null) {
            dossieCliente.setEmail(dossieClientePatch.getEmail());
        }

        // Identifica o tipo de objeto especializado para atualizar as informações corretas
        if (DossieClientePF.class.equals(dossieClientePatch.getClass())) {
            DossieClientePF dossieClientePF = (DossieClientePF) dossieCliente;
            DossieClientePF dossieClientePFPatch = (DossieClientePF) dossieClientePatch;

            aplicaPatchDossieClientePF(dossieClientePFPatch, dossieClientePF);

        } else {
            DossieClientePJ dossieClientePJ = (DossieClientePJ) dossieCliente;
            DossieClientePJ dossieClientePJPatch = (DossieClientePJ) dossieClientePatch;

            aplicaPatchDossieClientePJ(dossieClientePJPatch, dossieClientePJ);
        }
    }

    /**
     * Cria um registro de documento vinculado a um dossiê de cliente de cliente
     *
     * @param idDossieCliente - Identificador do Dossiê de Cliente ao qual o documento deverá ser vinculado
     * @param codigoIntegracao - Código de integração utilizado na ientificação do canal submissor do documento
     * @param idTipoDocumento - Identificador do tipo de documento enviado
     * @param temporalidadeDocumentoEnum - Indicador do status de temporalidade a ser aplicado no registro do documento e utilizado como atributo na submissão de
     *        documento no GED para identificar a situação do documento perante o controle de expurgo.
     * @param origemDocumentoEnum - Indicador do tipo de fonte utilizada para os documentos digitalizados
     * @param mimeType - Referência de mimetype do conteudo do documento.
     * @param atributosDocumento - Lista de atributos informados do documento
     * @param binario Base 64 - String em formato Base64 que representa a imagem ou binario do documento armazenado
     * @param quantidade de conteudo - Integer armazena a quantidade paginas.
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public void criaDocumentoDossieCliente(Long idDossieCliente, Integer idTipoDocumento, TemporalidadeDocumentoEnum temporalidadeDocumentoEnum, OrigemDocumentoEnum origemDocumentoEnum, String mimeType, List<AtributoDocumento> atributosDocumento, String binario) {

        Canal canal = this.canalServico.getByClienteSSO();
        if (canal == null) {
            throw new SimtrRequisicaoException("DCS.cDDC.001 - Canal de comunicação não localizado para codigo de integração fornecido.");
        }

        TipoDocumento tipoDocumento = this.tipoDocumentoServico.getById(idTipoDocumento);
        if (tipoDocumento == null) {
            throw new SimtrRequisicaoException("DCS.cDDC.002 - Tipo de Documento não localizado para codigo fornecido.");
        }

        // Realiza captura do dossiê do cliente com base no ID informado.
        DossieCliente dossieCliente = this.getById(idDossieCliente, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (dossieCliente == null) {
            throw new SimtrRequisicaoException("DCS.cDDC.003 - Identificado do dossiê de cliente encaminhado vinculação do documento é nulo.");
        }
        
        
        //Listas para carregar os Atributos invalidos para o Documento, Opções Invalidas para o Atributo e valores Invalidos para as Opções.
        List<String> atributosInvalidos = new ArrayList<String>();
        List<String> opcoesInvalidos = new ArrayList<String>();
        List<String> valoresInvalidos = new ArrayList<String>();
        atributosDocumento.stream().forEach(ad -> {
            Optional<AtributoExtracao> atributoExtracao = tipoDocumento.getAtributosExtracao().stream().filter(ae ->  ae.getNomeAtributoDocumento().equals(ad.getDescricao())).findAny();
            //Valida se o Atributo informado é um Atributo experado para extração
            if(!atributoExtracao.isPresent()) {
        	atributosInvalidos.add(ad.getDescricao());
            }else {
        	//Valida se  as Opções Selecionadas do AtributoDocumento estão previstas nas Opções do Atributo esperada.
        	ad.getOpcoesSelecionadas().stream().forEach(os -> {
        	    if(!atributoExtracao.get().getOpcoesAtributo().stream().filter(oa -> 
        	    oa.getValorOpcao().equals(os.getValorOpcao())).findAny().isPresent()) {
        		opcoesInvalidos.add(os.getValorOpcao());
        		valoresInvalidos.add(ad.getDescricao());
        	    }
        	});
            }
            
        });
        
        //Caso Exista Atributos invalidos Lança exception detalhando os atributos
        if(!atributosInvalidos.isEmpty()) {
            String mensagem = MessageFormat.format(
        	    "DCS.cDDC.004 - Atributo(s)[{0}] do Tipo de Documento [{1}] não esta definido para este tipo de documento.",
        	    atributosInvalidos.stream().collect(Collectors.joining(",")), tipoDocumento.getNome());
            throw new SimtrRequisicaoException(mensagem);
        }

        //Caso exista Opções invalidas lança exception explicitando as opções que foram selecionadas que não são esperadas
        if(!opcoesInvalidos.isEmpty()) {
            String mensagem = MessageFormat.format(
                    "DCS.cDDC.005 - Opção(ões) [{0}] do(s) Atributo(s) de Documento [{1}] não esta definido para este tipo de documento.",
                    opcoesInvalidos.stream().collect(Collectors.joining(",")), valoresInvalidos.stream().collect(Collectors.joining(",")));
            throw new SimtrRequisicaoException(mensagem);
        }
        
        FormatoConteudoEnum formatoConteudoEnum = null;
        if (mimeType != null) {
            try {
                formatoConteudoEnum = FormatoConteudoEnum.getByMimeType(mimeType);
            } catch (IllegalArgumentException iae) {
                String mensagem = MessageFormat.format("DCS.cDDC.006 - Formato de Conteudo não localizado pelo mimeType fornecido. mimeType: {0}.", mimeType);
                throw new SimtrRequisicaoException(mensagem, iae);
            }
        }
        
        Documento documento = this.documentoServico.prototype(canal, Boolean.FALSE, tipoDocumento, temporalidadeDocumentoEnum, origemDocumentoEnum, formatoConteudoEnum, atributosDocumento, binario);

        this.documentoServico.insereDocumentoClienteNegocio(dossieCliente.getCpfCnpj(), dossieCliente.getTipoPessoa(), documento);

        // Vincula documento ao Dossiê de Cliente
        dossieCliente.addDocumentos(documento);
        this.update(dossieCliente);
    }

    public void atualizaCadastroCaixa(Long id) {

        Canal canal = this.canalServico.getByClienteSSO();

        if (!canal.getIndicadorOutorgaCadastroCaixa()) {
            throw new SimtrPermissaoException("Canal não autorizado a solicitar atualização cadastral do cliente");
        }

        DossieCliente dossieCliente = this.getById(id, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);

        if (dossieCliente == null) {
            throw new SimtrRecursoDesconhecidoException("Dossiê de cliente não localizado pelo identificador informado");
        }

        TipoPessoaEnum tipoPessoaEnum = dossieCliente.getClass().isAssignableFrom(DossieClientePF.class) ? TipoPessoaEnum.F : TipoPessoaEnum.J;

        List<TipoDocumento> tipologiaIdentificada = dossieCliente.getDocumentos().stream()
                                                                 .map(d -> d.getTipoDocumento())
                                                                 .distinct().collect(Collectors.toList());

        List<Documento> documentosAptos = new ArrayList<>();

        tipologiaIdentificada.forEach(td -> {
            Optional<Documento> documento = dossieCliente.getDocumentos().stream()
                                                         .filter(d -> td.equals(d.getTipoDocumento()))
                                                         .filter(d -> !d.getAtributosDocumento().isEmpty())
                                                         .filter(d -> d.getDataHoraCadastroCliente() == null)
                                                         .max(Comparator.comparing(Documento::getDataHoraCaptura));

            if (documento.isPresent()) {
                documentosAptos.add(documento.get());
            }
        });

        if (documentosAptos.isEmpty()) {
            throw new SimtrPreCondicaoException("Não foram identificados documentos aptos para atualização cadastral do cliente solicitado.");
        }

        List<Documento> documentosAtualizacao = new ArrayList<>();
        documentosAptos.forEach(d -> {
            documentosAtualizacao.add(this.documentoServico.getById(d.getId(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE));
        });

        this.cadastroCaixaServico.atualizaDadosDossieDigital(dossieCliente.getCpfCnpj(), tipoPessoaEnum, new ArrayList<>(), documentosAtualizacao);
        
        documentosAtualizacao.forEach(documento -> {
            documento.setDossieDigital(Boolean.TRUE);
            this.documentoServico.update(documento);
        });
    }

    // **** Metodos Privados ****//
    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    private String getQuery(boolean vinculacoesNivelDocumental, boolean vinculacoesDocumentos, boolean vinculacoesDossiesProduto) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT dc FROM DossieCliente dc ");

        if (vinculacoesNivelDocumental) {
            jpql.append(" LEFT JOIN FETCH dc.composicoesDocumentais cd ");
            jpql.append(" LEFT JOIN FETCH cd.produtos p ");
        }

        if (vinculacoesDocumentos) {
            jpql.append(" LEFT JOIN FETCH dc.documentos d ");
            jpql.append(" LEFT JOIN FETCH d.canalCaptura cc ");
            jpql.append(" LEFT JOIN FETCH d.atributosDocumento ad ");
            jpql.append(" LEFT JOIN FETCH d.tipoDocumento tp ");
            jpql.append(" LEFT JOIN FETCH tp.funcoesDocumentais fd ");
        }
        if (vinculacoesDossiesProduto) {
            jpql.append(" LEFT JOIN FETCH dc.dossiesClienteProduto dcp ");
            jpql.append(" LEFT JOIN FETCH dcp.tipoRelacionamento tr ");
            jpql.append(" LEFT JOIN FETCH dcp.dossieProduto dp ");
            jpql.append(" LEFT JOIN FETCH dp.canal ca ");
            jpql.append(" LEFT JOIN FETCH dp.processo pro ");
            jpql.append(" LEFT JOIN FETCH dp.processosFaseDossie pfd ");
            jpql.append(" LEFT JOIN FETCH pfd.processoFase prof ");
            jpql.append(" LEFT JOIN FETCH dp.situacoesDossie sd ");
            jpql.append(" LEFT JOIN FETCH dp.produtosDossie pd ");
            jpql.append(" LEFT JOIN FETCH dp.unidadesTratamento ut ");
            jpql.append(" LEFT JOIN FETCH pd.produto prd ");
            jpql.append(" LEFT JOIN FETCH sd.tipoSituacaoDossie tsd ");
        }

        return jpql.toString();

    }

    /**
     * Utilizado para realizar a captura de um Dossiê de cliente baseado no ID ou no CPF/CNPJ, mas nunca pelos dois ativados conjuntamente. Devido a isso, este
     * metódo é privado e existem dois métodos publicos que habilitam apenas uma opção ou outra.
     *
     * @param id ID do Dossiê de Cliente quando ativado consulta por este atributo
     * @param cpfCnpj CPF/CNPJ do cliente quando ativado consulta por este atributo
     * @param tipoPessoaEnum Tipo de pessoa relacionada (F ou J)
     * @param vinculacoesNivelDocumental Indicador se as vinculações relativas ao nivel documental devem ser carregadas
     * @param vinculacoesDocumentos Indicador se as vinculações relativas aos documentos de cliente devem ser carregados
     * @param vinculacoesDossiesProduto Indicador se as vinculações relativas aos dossiês de produto com vinculos no dossiê de cliente devem ser carregados
     * @return Dossiê de Cliente carregado conforme parametros indicados.
     */
    private DossieCliente getByIdOrCpfCnpj(Long id, Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, boolean vinculacoesNivelDocumental, boolean vinculacoesDocumentos, boolean vinculacoesDossiesProduto) {

        DossieCliente dossieClienteRetorno;
        DossieCliente dossieClienteTemp;

        try {
            String jpqlBase = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            dossieClienteRetorno = this.getQueryResult(jpqlBase, id, cpfCnpj, tipoPessoaEnum);
        } catch (NoResultException nre) {
            LOGGER.log(Level.ALL, nre.getLocalizedMessage(), nre);
            return null;
        }

        if (vinculacoesNivelDocumental) {
            String jpql = this.getQuery(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
            dossieClienteTemp = this.getQueryResult(jpql, id, cpfCnpj, tipoPessoaEnum);
            dossieClienteRetorno.setComposicoesDocumentais(dossieClienteTemp.getComposicoesDocumentais());
        }
        if (vinculacoesDocumentos) {
            String jpql = this.getQuery(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
            dossieClienteTemp = this.getQueryResult(jpql, id, cpfCnpj, tipoPessoaEnum);
            dossieClienteRetorno.setDocumentos(dossieClienteTemp.getDocumentos());
        }
        if (vinculacoesDossiesProduto) {
            String jpql = this.getQuery(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
            dossieClienteTemp = this.getQueryResult(jpql, id, cpfCnpj, tipoPessoaEnum);
            dossieClienteRetorno.setDossiesClienteProduto(dossieClienteTemp.getDossiesClienteProduto());
        }

        return dossieClienteRetorno;
    }

    private DossieCliente getQueryResult(String jpql, Long id, Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum) {
        if (id != null) {
            jpql = jpql.concat(CLAUSULA_WHERE_ID);
        } else {
            jpql = jpql.concat(CLAUSULA_WHERE_CPF_CNPJ);
        }
        TypedQuery<DossieCliente> query = this.entityManager.createQuery(jpql, DossieCliente.class);
        if (id != null) {
            query.setParameter(ID, id);
        } else {
            query.setParameter(CPF_CNPJ, cpfCnpj);
            query.setParameter(TIPO_PESSOA, tipoPessoaEnum);
        }
        return query.getSingleResult();
    }

    private void aplicaPatchDossieClientePJ(DossieClientePJ dossieClientePJPatch, DossieClientePJ dossieClientePJ) {
        if (dossieClientePJPatch.getRazaoSocial() != null) {
            dossieClientePJ.setRazaoSocial(dossieClientePJPatch.getRazaoSocial());
        }

        if (dossieClientePJPatch.getDataFundacao() != null) {
            dossieClientePJ.setDataFundacao(dossieClientePJPatch.getDataFundacao());
        }

        if (dossieClientePJPatch.getPorte() != null) {
            dossieClientePJ.setPorte(dossieClientePJPatch.getPorte());
        }

        if (dossieClientePJPatch.getConglomerado() != null) {
            dossieClientePJ.setConglomerado(dossieClientePJPatch.getConglomerado());
        }
        this.update(dossieClientePJ);
    }

    private void aplicaPatchDossieClientePF(DossieClientePF dossieClientePFPatch, DossieClientePF dossieClientePF) {
        if (dossieClientePFPatch.getDataNascimento() != null) {
            dossieClientePF.setDataNascimento(dossieClientePFPatch.getDataNascimento());
        }

        if (dossieClientePFPatch.getNomeMae() != null) {
            dossieClientePF.setNomeMae(dossieClientePFPatch.getNomeMae());
        }

        this.update(dossieClientePF);
    }

    /**
     * 
     * Valida se o canal de comunicação permite que a consulta da avaliação de risco junto ao siric seja realizada.
     * Consulta e retorna a avaliação de risco do tomador junto ao SIRIC.
     * 
     * 
     * @param cpfCnpj Cpf/Cnpj do tomador
     * @return Resposta do serviço de avaliação de risco do cpf/cnpj.
     * @throws Exception 
     */
   @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTO,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNINT
        
    })
	public List<AvaliacaoSiricDTO> consultaSiric(String cpfCnpj) throws Exception {
		validarCanalComunicacao();
		
		Map<String, String> cabecalhos = new HashMap<>();
		
		//Obtendo a APIKEY e adicionando aos cabeçalhos
        String apikey = System.getProperty("simtr_apikey");
        cabecalhos.put("apikey", apikey);

        String urlApiManager = System.getProperty("url.servico.consulta.tomador.siric");
        urlApiManager = urlApiManager.replaceAll("\\{cpf-cnpj\\}", String.valueOf(cpfCnpj));
                
        
        Response response =  UtilRest.consumirServicoOAuth2JSON(urlApiManager, EnumMetodoHTTP.GET, cabecalhos, new HashMap<>(), null, keycloakService.getAccessTokenString());
        //O usuário logado precisa ter autorização para consulta no SIRIC, caso não tenha, o retorno é com status OK e o corpo vazio.
        if(Objects.nonNull(response) && !response.getEntity().toString().isEmpty()) {
        	List<RespostaAvaliacaoSiricVO> respostaAvaliacoes = Arrays.asList((RespostaAvaliacaoSiricVO[]) UtilJson.converterDeJson(response.getEntity().toString(), RespostaAvaliacaoSiricVO[].class));
        	return respostaAvaliacoes.stream().map(resposta -> this.carregarAvaliacoes(resposta)).collect(Collectors.toList());
        }
        return new ArrayList<>();
        
	}
   
   /**
    * Metodo responsável por carregar a resposta do Serviço de Consulta da avaliação do tomador no SIRIC e mapear em um objeto conhecido e controlado pelo SIMTR.
    * 
    * @param resposta Objeto resposta da consulta do SIRIC
    * 
    * @return Objeto conhecido pelo SIMTR
    */
	 private AvaliacaoSiricDTO carregarAvaliacoes(RespostaAvaliacaoSiricVO resposta) {
		 AvaliacaoSiricDTO avaliacao = new AvaliacaoSiricDTO();
		 avaliacao.setProdutos(resposta.getProdutos().stream().map(produto -> this.carregarProdutosAvaliacao(produto)).collect(Collectors.toList()));
		 avaliacao.setAvaliacaoAprovada("S".equalsIgnoreCase(resposta.getAvaliacaoBloqueadaTomador()) ? Boolean.TRUE : Boolean.FALSE);
		 avaliacao.setCnpj(resposta.getTipoPessoa() == 1 ? null : resposta.getIdentificacaoPessoa());
		 avaliacao.setCpf(resposta.getTipoPessoa() == 1 ? resposta.getIdentificacaoPessoa() : null);
		 avaliacao.setDataFim(resposta.getDataFim());
		 avaliacao.setDataInicio(resposta.getDataInicio());
		 avaliacao.setMotivoReprovacao(resposta.getMotivoReprovacao());
		 avaliacao.setCodigoAvaliacao(resposta.getCodigoAvaliacao());
		 avaliacao.setCodigoProposta(resposta.getCodigoProposta());
		 return avaliacao;
	}

	/**
     * Metodo responsável por carregar o produto da resposta do Serviço de Consulta da avaliação do tomador no SIRIC e mapear em um objeto conhecido e controlado pelo SIMTR.
     *  
     * @param resposta Objeto resposta da consulta do SIRIC
     * 
     * @return Objeto conhecido pelo SIMTR
     */
	 private ProdutoAvaliacaoSiricDTO carregarProdutosAvaliacao(RespostaProdutoAvaliacaoSiricVO produtoResposta) {
		ProdutoAvaliacaoSiricDTO produto = new ProdutoAvaliacaoSiricDTO();
		produto.setCodigoModalidade(produtoResposta.getCodigoModalidade());
		produto.setCodigoOperacao(produtoResposta.getCodigoProduto());
		produto.setPrazo(produtoResposta.getPrazoMeses());
		produto.setRating(produtoResposta.getRating());
		produto.setResultadoAvaliacao(ResultadoAvaliacaoProdutoSIRICEnum.getResultadoAvaliacao(produtoResposta.getResultadoAvaliacao()));
		produto.setValorDisponivel(produtoResposta.getValorDisponivel());
		produto.setValorTotal(produtoResposta.getValorTotal());
		return produto;
	}

	/**
     * 
     * Verifica se o Canal de comunicação do usuário logado no SSO permite a consulta de avaliação de risco junto ao SIRIC através do SIMTR.
     * Caso o canal não seja encontrado retorna exception com mensagem. 
     * Caso o canal seja encontrado e não contenha autorização de consumo junto ao Siric, é lançado uma exception.
     * 
     */
     private void validarCanalComunicacao() {
        Canal canal = this.canalServico.getByClienteSSO();
        if(Objects.isNull(canal)) {
            throw new SimtrPermissaoException("DCS.vCC.001 - Canal de comunicação não cadastrado para o ClientID do SSO utilizado.");
        }
        
        if (!canal.getIndicadorOutorgaSiric()) {
            throw new SimtrPermissaoException("DCS.vCC.002 - \"Canal de comunicação não autorizado a solicitar avaliações junto ao SIRIC utilizando a API do SIMTR.");
        }
    }

     /**
      * 
      * Valida se o canal de comunicação permite que a consulta da avaliação de risco junto ao siric seja realizada.
      * Envia ao Siric as propostas do tomador
      * 
      * 
      * @param cpfCnpj Cpf/Cnpj do tomador
      * @return Resposta do serviço de avaliação de risco do cpf/cnpj.
      * @throws Exception 
      */
     @RolesAllowed({
         ConstantesUtil.PERFIL_MTRADM,
         ConstantesUtil.PERFIL_MTRSDNOPE,
         ConstantesUtil.PERFIL_MTRSDNTTO,
         ConstantesUtil.PERFIL_MTRSDNTTG,
         ConstantesUtil.PERFIL_MTRSDNMTZ,
         ConstantesUtil.PERFIL_MTRSDNINT
     })
	public Response enviaAvaliacaoRiscoSiric(String cpfCnpj) {
		validarCanalComunicacao();
		Map<String, String> cabecalhos = new HashMap<>();
		
		//Obtendo a APIKEY e adicionando aos cabeçalhos
        String apikey = System.getProperty("simtr_apikey");
        cabecalhos.put("apikey", apikey);

        String urlApiManager = System.getProperty("url.servico.executa.tomador.siric");
        urlApiManager = urlApiManager.replaceAll("\\{cpf-cnpj\\}", String.valueOf(cpfCnpj));
                
        
        Response response =  UtilRest.consumirServicoOAuth2JSON(urlApiManager, EnumMetodoHTTP.POST, cabecalhos, new HashMap<>(), null, keycloakService.getAccessTokenString());
        if(response.getStatus() == HttpStatus.SC_NO_CONTENT){
        	return response;
        }else if(response.getStatus() == HttpStatus.SC_BAD_REQUEST){
        	throw new SimtrRequisicaoException("DCS.eARS.001 - Falha ao consumir o serviço junto ao SIRIC.");
        }
		return null;
	}
}
