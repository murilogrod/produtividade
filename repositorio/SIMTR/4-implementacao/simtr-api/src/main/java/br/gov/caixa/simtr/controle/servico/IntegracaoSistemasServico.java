package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.pedesgo.arquitetura.util.UtilParametro;
import br.gov.caixa.pedesgo.arquitetura.util.UtilRest;
import br.gov.caixa.simtr.controle.excecao.SicliException;
import br.gov.caixa.simtr.controle.excecao.SimtrAtributoIntegracaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.AtributoIntegracao;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieClienteProduto;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.ObjetoIntegracao;
import br.gov.caixa.simtr.modelo.entidade.OpcaoCampo;
import br.gov.caixa.simtr.modelo.entidade.PriorizacaoObjetoIntegracao;
import br.gov.caixa.simtr.modelo.entidade.RespostaDossie;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.AcaoComparacaoEnum;
import br.gov.caixa.simtr.modelo.enumerator.ObjetoSemTpOperacaoSICLIEnum;
import br.gov.caixa.simtr.modelo.enumerator.OperadorComparacaoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SICLITipoFonteEnum;
import br.gov.caixa.simtr.modelo.enumerator.SistemaIntegracaoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.util.KeycloakUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRAUD,
    ConstantesUtil.PERFIL_MTRTEC,
    ConstantesUtil.PERFIL_MTRDOSINT,
    ConstantesUtil.PERFIL_MTRDOSMTZ,
    ConstantesUtil.PERFIL_MTRDOSOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class IntegracaoSistemasServico {

    /**
     * Classe criada para realizar integração entre sistemas.
     *
     * @author @f559852
     */
    
    @Inject
    private EntityManager entityManager;
    
    @EJB
    private AvaliacaoExtracaoServico avaliacaoExtracaoServico;

    @EJB
    private CadastroCaixaServico cadastroCaixaServico;

    @EJB
    private CanalServico canalServico;

    @EJB
    private DocumentoServico documentoServico;
    
    @EJB
    private DossieProdutoServico dossieProdutoServico;
    
    @EJB
    private KeycloakUtil keycloakUtil;
    
    @EJB
    private IntegracaoSistemasServicoInsercao integracaoSistemasServicoInsercao;

    private static final Logger LOGGER = Logger.getLogger(IntegracaoSistemasServico.class.getName());

    private static final String MENSAGEM_CANAL_NAO_LOCALIZADO = "Canal de comunicação não localizado para client ID vinculado ao token de autenticação.";
    private static final String MENSAGEM_CANAL_NAO_AUTORIZADO = "Canal de comunicação não autorizado para consumo dos serviços do dossiê digital.";
    private static final String ATRIBUTO_CODIGO_SITUACAO = "codSituacao";
    private static final String ATRIBUTO_RETORNO = "retorno";
    private static final String ATRIBUTO_TIPO_OPERACAO = "tpOperacao";
    private static final String PARAMETRO_SICLI = "url.servico.atualiza.cliente.sicli";
    private static final String DADOSGERAIS = "dadosgerais";
    private static final String CNPJ = "cnpj";
    private static final String CPF = "cpf";
    private static final String APPLICATION_JSON = "application/json";
    private String urlWebService;
    private String apiKey;
    private final Map<String, String> cabecalhos = new HashMap<>();
    private static final String NU_SEQUENCIAL = "nuSequencial";
    private static final TipoCampoEnum[] CAMPOS_RESPOSTA_NAO_ABERTA = { TipoCampoEnum.SELECT, TipoCampoEnum.RADIO, TipoCampoEnum.CHECKBOX };
    private static final String SEQUENCIAL = "sequencial";
    private static final String CLASSE = "classe";
    

    /**
     * 
     * @param idDossieProduto
     */
    public void atualizaCadastroCaixaDossieProduto(Long idDossieProduto, Integer classe) {

        // Captura o canal de comunicação para utilizar na vinculação do documento
        Canal canal = this.canalServico.getByClienteSSO();
        this.canalServico.validaRecursoLocalizado(canal, "DDS.aCCDP.001 - ".concat(MENSAGEM_CANAL_NAO_LOCALIZADO));
        this.canalServico.validaOutorgaCanal(canal, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, "DDS.aCC.002 - ".concat(MENSAGEM_CANAL_NAO_AUTORIZADO));

        DossieProduto dossieProduto = this.dossieProdutoServico.getById(idDossieProduto, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
        this.dossieProdutoServico.validaRecursoLocalizado(dossieProduto, "DDS.aCCDP.002 - Dossiê de produto não localizado pelo identificador informado");
        
        DossieClienteProduto dossieClientePrincipal = dossieProduto.getDossiesClienteProduto().stream().filter(dossieClienteProduto -> dossieClienteProduto.getTipoRelacionamento().getIndicadorPrincipal()).findFirst().get();
        
        Long cpfCnpj = dossieClientePrincipal.getDossieCliente().getCpfCnpj();
        
        Map<String, Object> mapaRetornoSICLI = this.verificaCadastroExistente(cpfCnpj, dossieClientePrincipal.getDossieCliente().getTipoPessoa(), classe.toString());
        
        Map<String, Object> mapaCorpoRequisicaoSICLI = new HashMap<>();
        List<String> pendencias = new ArrayList<>();
        
        System.out.println(mapaRetornoSICLI);
        
//        String str = "";
//        
//        try {
//            mapaRetornoSICLI = (Map<String, Object>) UtilJson.converterDeJson(str, Map.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        
        if(Objects.isNull(mapaRetornoSICLI)) {
            mapaCorpoRequisicaoSICLI = this.integracaoSistemasServicoInsercao.montaObjetoIntegracaoParaInserir(dossieProduto, dossieClientePrincipal, classe, SistemaIntegracaoEnum.SICLI);
        }else {
            
            
            
            List<ObjetoIntegracao> objetoIntegracaos = this.consultaObjetoIntegracaoPeloProcesso(dossieProduto.getProcesso().getId());
            List<ObjetoIntegracao> ordenadosObjetosIntegracaos = this.retornaListaOrdenada(objetoIntegracaos);
            
            //PUT precisa implementar o restantes das comparações >= != <, so esta implementada a =. Apos realizar a comparação precisa implemtar a ação inserir, excluir ou alterar
            for(ObjetoIntegracao objetoIntegracao : ordenadosObjetosIntegracaos) {
                if(objetoIntegracao.getSistemaIntegracao().equals(SistemaIntegracaoEnum.SICLI)){
                     this.montarObjetosIntegracaoPelaListaPriorizacao(objetoIntegracao, dossieProduto, dossieClientePrincipal, mapaCorpoRequisicaoSICLI, mapaRetornoSICLI, pendencias);
                }
            }
            
            if(!mapaCorpoRequisicaoSICLI.isEmpty()) {
                mapaCorpoRequisicaoSICLI.put(CLASSE, classe);
            }
        }
        
        if(pendencias.isEmpty() && !mapaCorpoRequisicaoSICLI.isEmpty()) {
            this.enviaRequisicaoProSicli(mapaCorpoRequisicaoSICLI, Objects.isNull(mapaRetornoSICLI) ? EnumMetodoHTTP.POST :EnumMetodoHTTP.PUT, classe.toString(), cpfCnpj, dossieClientePrincipal.getDossieCliente().getTipoPessoa());
            
        }else {
            throw new SimtrAtributoIntegracaoException("DDS.aCCDP.003 - Problemas identificados na montagem do json para SICLI.", pendencias);
        }
    }
    
    private void montarObjetosIntegracaoPelaListaPriorizacao(ObjetoIntegracao objetoIntegracao, DossieProduto dossieProduto, DossieClienteProduto dossieClientePrincipal, Map<String, Object> mapaCorpoRequisicaoSICLI,  Map<String, Object> mapaRetornoSICLI, List<String> pendencias) {
        
        List<PriorizacaoObjetoIntegracao> ordenados = objetoIntegracao.getPriorizacaoObjetoIntegracaos()
                .stream().sorted((primeiro, segundo) -> 
                primeiro.getOrdemPrioridade().compareTo(segundo.getOrdemPrioridade()))
                .collect(Collectors.toList());
        
        PriorizacaoObjetoIntegracao priorizacaoMaior = ordenados.get(0);
        
        if(SICLITipoFonteEnum.D.equals(priorizacaoMaior.getFonte())){
            this.montarAtributosIntegracaoPorDocumento(dossieProduto, priorizacaoMaior.getObjetoIntegracao(), priorizacaoMaior.getTipoDocumento(), dossieClientePrincipal, mapaCorpoRequisicaoSICLI,  mapaRetornoSICLI, pendencias);
        } else if(SICLITipoFonteEnum.F.equals(priorizacaoMaior.getFonte())){
            this.montarAtributosIntegracaoPorFormulario(priorizacaoMaior.getObjetoIntegracao(), dossieProduto, mapaCorpoRequisicaoSICLI, mapaRetornoSICLI);
        }
    }
    
    private void montarAtributosIntegracaoPorFormulario(ObjetoIntegracao objetoIntegracao, DossieProduto dossieProduto, Map<String, Object> mapaCorpoRequisicaoSICLI, Map<String, Object> mapaRetornoSICLI) {
        DossieClienteProduto dossieClientePrincipal = dossieProduto.getDossiesClienteProduto().stream().filter(dossieClienteProduto -> dossieClienteProduto.getTipoRelacionamento().getIndicadorPrincipal()).findFirst().get();
        String cpfCnpjFormatado = StringUtils.leftPad(String.valueOf(dossieClientePrincipal.getDossieCliente().getCpfCnpj()), TipoPessoaEnum.F.equals(dossieClientePrincipal.getDossieCliente().getTipoPessoa()) ? 11 : 14, '0');
        Boolean todosValoresComparadosSucesso = Boolean.TRUE;
        String objetoParametrizacao = Objects.nonNull(objetoIntegracao.getObjetoConsulta()) ? objetoIntegracao.getObjetoConsulta() : objetoIntegracao.getObjetoIntegracao();
        Map<String, Object> mapaObjectRaiz = new HashMap<>();
        mapaObjectRaiz.put(cpfCnpjFormatado.length() == 11 ? CPF : CNPJ, cpfCnpjFormatado);
        String objetoRaiz = objetoParametrizacao.split("/")[0];
        Map<String, Object> mapaObject = new HashMap<>();
        LinkedHashMap<String, Object> listaMapaRetornoSICLI = (LinkedHashMap<String, Object>)  mapaRetornoSICLI;
        for(AtributoIntegracao atributoIntegracao : objetoIntegracao.getAtributosIntegracao()) {
            String valorAtributoRetornoSICLI = null;
            Object repostaCampoFormulario = this.pesquisaPorFormulario(dossieProduto, atributoIntegracao);
            if(Objects.nonNull(repostaCampoFormulario)) {
                if(Objects.isNull(objetoIntegracao.getObjetoIntegracao())) {
                    Object atributoRetornoSICLI = listaMapaRetornoSICLI.get(atributoIntegracao.getAtributo());
                    valorAtributoRetornoSICLI = atributoRetornoSICLI.toString();
                    this.realizarComparacaoObjetosRespostaSICLICampoFormulario(objetoParametrizacao, repostaCampoFormulario.toString(), objetoIntegracao, atributoIntegracao, valorAtributoRetornoSICLI, mapaObject, todosValoresComparadosSucesso);
                } else {
                    objetoParametrizacao = objetoParametrizacao.substring(objetoParametrizacao.lastIndexOf("/") + 1);
                    Object objetoRetornoSICLI = listaMapaRetornoSICLI.get(objetoParametrizacao);
                    LinkedHashMap<String, Object> objetoChaveValor = (LinkedHashMap<String, Object>) objetoRetornoSICLI;
                    Object atributoRetornoSICLI = objetoChaveValor.get(atributoIntegracao.getAtributo());
                    valorAtributoRetornoSICLI = atributoRetornoSICLI.toString();
                    this.realizarComparacaoObjetosRespostaSICLICampoFormulario(objetoParametrizacao, repostaCampoFormulario.toString(), objetoIntegracao, atributoIntegracao, valorAtributoRetornoSICLI, mapaObject, todosValoresComparadosSucesso);
                    
                }
            } else {
                throw new SimtrAtributoIntegracaoException(
                        "Sem atributo integração parametrizado na tabela 19 para o campo de formulário.");
            }
        }
        if(todosValoresComparadosSucesso && !objetoParametrizacao.equals(ObjetoSemTpOperacaoSICLIEnum.DADOS_BASICOS.getDescricao())) {
            if(!mapaObject.isEmpty() && !mapaObject.containsKey(ATRIBUTO_TIPO_OPERACAO)) {
                mapaObject.put(ATRIBUTO_TIPO_OPERACAO, objetoIntegracao.getAcaoComparacao());
            } 
        }
        mapaObjectRaiz.put(objetoParametrizacao, mapaObject);
        mapaCorpoRequisicaoSICLI.put(objetoRaiz, mapaObjectRaiz);
    }
    
    private boolean isCampoOpcao(final TipoCampoEnum tipoCampoEnum) {
        for (TipoCampoEnum item : CAMPOS_RESPOSTA_NAO_ABERTA) {
            if (item.equals(tipoCampoEnum)) {
                return true;
            }
        }
        return false;
    }
    private void realizarComparacaoObjetosRespostaSICLICampoFormulario(String objetoParametrizacao, String respostaFormularioAtributoIntegracao, ObjetoIntegracao objetoIntegracao, AtributoIntegracao atributoIntegracao, String valorAtributoRetornoSICLI, Map<String, Object> mapaObject, Boolean todosValoresComparadosSucesso) {
        if(atributoIntegracao.getChaveComparacao() && atributoIntegracao.getOperadorComparacao() != null){
            if(OperadorComparacaoEnum.IGUAL.equals(atributoIntegracao.getOperadorComparacao())){
                if(respostaFormularioAtributoIntegracao.equalsIgnoreCase(valorAtributoRetornoSICLI)) {
                    mapaObject.put(atributoIntegracao.getAtributo(), respostaFormularioAtributoIntegracao);
                }
            }else if(OperadorComparacaoEnum.DIFERENTE.equals(atributoIntegracao.getOperadorComparacao())) {
                if(!respostaFormularioAtributoIntegracao.equalsIgnoreCase(valorAtributoRetornoSICLI)) {
                    todosValoresComparadosSucesso = Boolean.FALSE;
                    mapaObject.put(atributoIntegracao.getAtributo(), respostaFormularioAtributoIntegracao);
                    if(!mapaObject.isEmpty() && !mapaObject.containsKey(ATRIBUTO_TIPO_OPERACAO) && !objetoParametrizacao.equals(ObjetoSemTpOperacaoSICLIEnum.DADOS_BASICOS.getDescricao())) {
                        mapaObject.put(ATRIBUTO_TIPO_OPERACAO, objetoIntegracao.getAcao());
                    }
                }
            }
        } else {
            mapaObject.put(atributoIntegracao.getAtributo(), respostaFormularioAtributoIntegracao);
            if(!mapaObject.isEmpty() && !mapaObject.containsKey(ATRIBUTO_TIPO_OPERACAO) && !objetoParametrizacao.equals(ObjetoSemTpOperacaoSICLIEnum.DADOS_BASICOS.getDescricao())) {
                mapaObject.put(ATRIBUTO_TIPO_OPERACAO, objetoIntegracao.getAcao());
            }
        }                    
    }
    
    private void montarAtributosIntegracaoPorDocumento(DossieProduto dossieProduto, ObjetoIntegracao objetoIntegracao, TipoDocumento tipoDocumento, DossieClienteProduto dossieClientePrincipal, Map<String, Object> mapaCorpoRequisicaoSICLI, Map<String, Object> mapaRetornoSICLI, List<String> pendencias) {
        List<InstanciaDocumento> instanciasDocumentoDossieClientePrincipal = new ArrayList<>();
        dossieProduto.getInstanciasDocumento().forEach(instanciaDocumento -> {
            if ((instanciaDocumento.getDossieClienteProduto() != null) && (instanciaDocumento.getDossieClienteProduto().equals(dossieClientePrincipal))) {
                instanciasDocumentoDossieClientePrincipal.add(instanciaDocumento);
            }
        });
        Documento documentoLocalizado = null;
        Optional<Documento> documento = instanciasDocumentoDossieClientePrincipal.stream()
                .map(instanciaDoc -> instanciaDoc.getDocumento())
                .filter(doc -> tipoDocumento.getId().equals(doc.getTipoDocumento().getId()))
                .filter(doc -> !doc.getAtributosDocumento().isEmpty())
                .filter(doc -> TemporalidadeDocumentoEnum.TEMPORARIO_ANTIFRAUDE.equals(doc.getSituacaoTemporalidade()) ||
                             TemporalidadeDocumentoEnum.VALIDO.equals(doc.getSituacaoTemporalidade()))
                .max(Comparator.comparing(Documento::getDataHoraCaptura));
        
        if (documento.isPresent()) {
            documentoLocalizado = documento.get();
        }
        
        String objetoConsulta = Objects.nonNull(objetoIntegracao.getObjetoConsulta()) ? objetoIntegracao.getObjetoConsulta() : objetoIntegracao.getObjetoIntegracao();
        if(documentoLocalizado == null) {
            return;
        }
        
        if(Objects.nonNull(objetoConsulta)) {
            
            if(objetoConsulta.endsWith("[]") || objetoConsulta.contains("/")) {
                String[] arrayCamposPesquisarDocumento = this.retornaArrayDeChaves(objetoConsulta);
                Boolean encontrouObjejetoIntegracaoRetornoSicli = Boolean.TRUE;
                
                for(int i = 0; i < arrayCamposPesquisarDocumento.length; i++) {
                    if(Objects.isNull(mapaRetornoSICLI.get(arrayCamposPesquisarDocumento[i]))){
                        if(arrayCamposPesquisarDocumento[i].equals(arrayCamposPesquisarDocumento[arrayCamposPesquisarDocumento.length - 1])){
                            encontrouObjejetoIntegracaoRetornoSicli = Boolean.FALSE;
                        } else {
                            throw new SimtrAtributoIntegracaoException(
                                    "Cadastro retornado pelo SICLI não possui hierarquia antecedente definida na parameritiração: "+ arrayCamposPesquisarDocumento[i]);
                        }
                    }
                }
                
                this.executarAcoesPartindoParametrizacaoSistema(mapaCorpoRequisicaoSICLI, encontrouObjejetoIntegracaoRetornoSicli, objetoIntegracao, documentoLocalizado, mapaRetornoSICLI, arrayCamposPesquisarDocumento, pendencias);
            }else {
                //objeto simples na raiz
                this.executarAcoesPartindoParametrizacaoSistema(mapaCorpoRequisicaoSICLI, Boolean.TRUE, objetoIntegracao, documentoLocalizado, mapaRetornoSICLI, null, pendencias);
            }
            
        }else {
            // raiz objeto
            Map<String, Object> mapaObject = new HashMap<String, Object>();
            this.realizarComparacaoAtributosSimples(objetoIntegracao, documentoLocalizado, mapaRetornoSICLI, mapaObject, Boolean.FALSE, pendencias);
            if(!mapaObject.isEmpty()) {
                for(String chave: mapaObject.keySet()) {
                    Object atributoRaiz = mapaObject.get(chave);
                    mapaCorpoRequisicaoSICLI.put(chave, atributoRaiz);
                }
            }
        }
    }
    
    private void executarAcoesPartindoParametrizacaoSistema(Map<String, Object> mapaCorpoRequisicaoSICLI, Boolean encontrouObjejetoIntegracaoRetornoSicli, ObjetoIntegracao objetoIntegracao, Documento documentoLocalizado, Map<String, Object> mapaRetornoSICLI, String[] arrayCamposPesquisarDocumento, List<String> pendencias){
        if(encontrouObjejetoIntegracaoRetornoSicli && objetoIntegracao.getComparacaoCompleta()) {
            // TODO:OPERAÇÃO COMPLETA 
            
        } else if(encontrouObjejetoIntegracaoRetornoSicli && !objetoIntegracao.getComparacaoCompleta()) {
            this.definirAcoesObjetosSimplesOuCompostosPelaParametrizacaoSistema(mapaCorpoRequisicaoSICLI, encontrouObjejetoIntegracaoRetornoSicli, objetoIntegracao, documentoLocalizado, mapaRetornoSICLI, arrayCamposPesquisarDocumento, pendencias);
        } else if(!encontrouObjejetoIntegracaoRetornoSicli && !objetoIntegracao.getComparacaoCompleta()) {
            this.definirAcoesObjetosSimplesOuCompostosPelaParametrizacaoSistema(mapaCorpoRequisicaoSICLI, encontrouObjejetoIntegracaoRetornoSicli, objetoIntegracao, documentoLocalizado, mapaRetornoSICLI, arrayCamposPesquisarDocumento, pendencias);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void definirAcoesObjetosSimplesOuCompostosPelaParametrizacaoSistema(Map<String, Object> mapaCorpoRequisicaoSICLI, Boolean encontrouObjejetoIntegracaoRetornoSicli, ObjetoIntegracao objetoIntegracao, Documento documentoLocalizado, Map<String, Object> mapaRetornoSICLI, String[] arrayCamposPesquisarDocumento, List<String> pendencias) {
        Object objectoSicli = null;
        String key = null;
        
        String objetoConsulta = Objects.nonNull(objetoIntegracao.getObjetoConsulta()) ? objetoIntegracao.getObjetoConsulta() : objetoIntegracao.getObjetoIntegracao();
        
        if(Objects.nonNull(arrayCamposPesquisarDocumento) && objetoConsulta.endsWith("[]")) {
            objectoSicli = mapaRetornoSICLI.get(arrayCamposPesquisarDocumento[arrayCamposPesquisarDocumento.length - 1]);
            key = arrayCamposPesquisarDocumento[arrayCamposPesquisarDocumento.length - 1];
            
        }else if(Objects.nonNull(arrayCamposPesquisarDocumento) && objetoConsulta.contains("/")) {
            objectoSicli = mapaRetornoSICLI.get(arrayCamposPesquisarDocumento[arrayCamposPesquisarDocumento.length - 1]);
            key = arrayCamposPesquisarDocumento[arrayCamposPesquisarDocumento.length - 1];
            
        } else {
            objectoSicli = mapaRetornoSICLI.get(objetoConsulta);
            key = objetoConsulta;
        }
        
        // OBJECT ARRAY
        List<Map<String, Object>> listMapObject = new ArrayList<>();
        
        // OBJETO QUE NÃO EXISTE NO RETORNO DO SICLI MAS QUE PODE SER ENVIADO
        if(Objects.isNull(objectoSicli)) {
            
            // inserir lista
            if(this.isUltimoNivelArray(objetoIntegracao.getObjetoIntegracao())) {
                
                if(isExisteNiveisNoObjetoIntegracao(objetoIntegracao.getObjetoIntegracao())) {
                    String chavePai = retornaChaveDoPai(objetoIntegracao.getObjetoIntegracao());
                    
                    /// recupera o pai
                    HashMap<String, Object> mapaObjectPai = (HashMap<String, Object>) mapaCorpoRequisicaoSICLI.get(chavePai);
                    
                    if(this.realizarInsercaoObjetoListaIntegracao(objetoIntegracao, documentoLocalizado, listMapObject, pendencias)) {
                        
                        // adiciona o filho no pai
                        mapaObjectPai.put(key, listMapObject);
                     
                        // adiciona o pai no map geral
                        mapaCorpoRequisicaoSICLI.put(chavePai, mapaObjectPai);
                    }
                    
                }else {
                    // não tem pai, lista fica na raiz
                    if(this.realizarInsercaoObjetoListaIntegracao(objetoIntegracao, documentoLocalizado, listMapObject, pendencias)) {
                        // adiciona o objeto raiz no map geral
                        mapaCorpoRequisicaoSICLI.put(key, listMapObject);
                    }
                }
                
            }else {// inseir objeto simples
                
                // objeto lista dentro de alguém
                if(isExisteNiveisNoObjetoIntegracao(objetoIntegracao.getObjetoIntegracao())) {
                    String chavePai = retornaChaveDoPai(objetoIntegracao.getObjetoIntegracao());
                    
                    // recupera o pai
                    HashMap<String, Object> mapaObjectPai = (HashMap<String, Object>) mapaCorpoRequisicaoSICLI.get(chavePai);
                    
                    Map<String, Object> mapaObjectFilho = new HashMap<String, Object>();
                    if(this.realizarInsercaoObjetoSimplesIntegracao(objetoIntegracao, documentoLocalizado, mapaObjectFilho, pendencias)) {
                        
                        mapaObjectPai.put(key, mapaObjectFilho);
                        
                        mapaCorpoRequisicaoSICLI.put(chavePai, mapaObjectPai);
                    }
                    
                }else {
                    // não tem pai, objeto simples na raiz
                    Map<String, Object> mapaObjectFilho = new HashMap<String, Object>();
                    if(this.realizarInsercaoObjetoSimplesIntegracao(objetoIntegracao, documentoLocalizado, mapaObjectFilho, pendencias)) {
                        mapaCorpoRequisicaoSICLI.put(key, mapaObjectFilho);
                    }
                }
            }
        
        // OBJETO QUE EXISTE NO RETORNO DO SICLI É UMA LISTA
        } else if(objectoSicli instanceof List) {
          
          String nomeCampo = arrayCamposPesquisarDocumento[arrayCamposPesquisarDocumento.length - 1];
          nomeCampo += "[]";
          
          // objeto lista dentro de alguém
          if(isExisteNiveisNoObjetoIntegracao(objetoIntegracao.getObjetoIntegracao())) {
              String chavePai = retornaChaveDoPai(objetoIntegracao.getObjetoIntegracao());
              
              // recupera o pai
              HashMap<String, Object> mapaObjectPai = (HashMap<String, Object>) mapaCorpoRequisicaoSICLI.get(chavePai);
              
              if(mapaObjectPai == null) {
                  mapaObjectPai = new HashMap<String, Object>();
              }
              
              if(this.realizarComparacaoAtributosList(objetoIntegracao, documentoLocalizado, nomeCampo, objectoSicli, mapaCorpoRequisicaoSICLI, listMapObject, pendencias)) {
                  // adiciona o filho no pai
                  mapaObjectPai.put(key, listMapObject);
               
                  // adiciona o pai no map geral
                  mapaCorpoRequisicaoSICLI.put(chavePai, mapaObjectPai);
              }
              
          }else {
              
              // não tem pai, lista fica na raiz
              if(this.realizarComparacaoAtributosList(objetoIntegracao, documentoLocalizado, nomeCampo, objectoSicli, mapaCorpoRequisicaoSICLI, listMapObject, pendencias)) {
                  // adiciona o objeto raiz no map geral
                  mapaCorpoRequisicaoSICLI.put(key, listMapObject);
              }
          }
         
          // OBJETO SIMPLES QUE EXISTE NO SICLI 
        }else {
            
            // objeto simples dentro de alguém
            if(isExisteNiveisNoObjetoIntegracao(objetoIntegracao.getObjetoIntegracao())) {
                String chavePai = retornaChaveDoPai(objetoIntegracao.getObjetoIntegracao());
                
                // recupera o pai
                HashMap<String, Object> mapaObjectPai = (HashMap<String, Object>) mapaCorpoRequisicaoSICLI.get(chavePai);
                
                // OBJETO SIMPLES
                Map<String, Object> mapaObjectFilho = new HashMap<String, Object>();
                if(Objects.nonNull(key) && this.realizarComparacaoAtributosSimples(objetoIntegracao, documentoLocalizado, objectoSicli, mapaObjectFilho, Boolean.TRUE, pendencias)) {
                    
                    // adiciona o filho no pai
                    mapaObjectPai.put(key, mapaObjectFilho);
                    
                    // adiciona o pai no map geral
                    mapaCorpoRequisicaoSICLI.put(chavePai, mapaObjectPai);
                }
            }else {
                
                // não tem pai, objeto simples na raiz
                Map<String, Object> mapaObjectFilho = new HashMap<String, Object>();
                if(Objects.nonNull(key) && this.realizarComparacaoAtributosSimples(objetoIntegracao, documentoLocalizado, objectoSicli, mapaObjectFilho, Boolean.TRUE, pendencias)) {
                    
                    // adiciona o objeto raiz no map geral
                    mapaCorpoRequisicaoSICLI.put(key, mapaObjectFilho);
                }
            }
        }
     
    }
    
    private boolean isUltimoNivelArray(String objetoIntegracao) {
        String igualaSeparador = objetoIntegracao.replaceAll("\\/", "/@");
        igualaSeparador = igualaSeparador.replaceAll("\\[\\]", "[]@");
        
        if(igualaSeparador.contains("@")) {
            
            String arrayKeys [] = igualaSeparador.split("@");
            
            String ultimoNivel = arrayKeys.length >= 2 ? arrayKeys[arrayKeys.length - 1] : arrayKeys[0];
            
            return ultimoNivel.contains("[");
        }else {
            return Boolean.FALSE;
        }
    }
    
    private String retornaUltimoNivel(String objetoIntegracao) {
        String igualaSeparador = objetoIntegracao.replaceAll("\\/", "/@");
        igualaSeparador = igualaSeparador.replaceAll("\\[\\]", "[]@");
        
        if(igualaSeparador.contains("@")) {
            String arrayKeys [] = igualaSeparador.split("@");
            
            String ultimoNivel = arrayKeys.length >= 2 ? arrayKeys[arrayKeys.length - 1] : arrayKeys[0];
            
            return ultimoNivel;
        }else {
            return igualaSeparador;
        }
    }
    
    private String retornaChaveDoPai(String objetoIntegracao) {
        String chavePai = null;
        
        String igualaSeparador = objetoIntegracao.replaceAll("\\/", "/@");
        igualaSeparador = igualaSeparador.replaceAll("\\[\\]", "[]@");
        
        String arrayKeys [] = igualaSeparador.split("@");
        
        if(arrayKeys.length >= 2) {
            chavePai = arrayKeys[arrayKeys.length - 2];
        }else {
            chavePai = arrayKeys[0];
        }
        
        chavePai = chavePai.replaceAll("\\/", "");
        chavePai = chavePai.replaceAll("\\[\\]", "");
        
        return chavePai;
    }
    
    private String [] retornaArrayDeChaves(String objetoIntegracao) {
        String igualaSeparador = objetoIntegracao.replaceAll("\\/", "/@");
        igualaSeparador = igualaSeparador.replaceAll("\\[\\]", "[]@");
        
        igualaSeparador = igualaSeparador.replaceAll("\\/", "");
        igualaSeparador = igualaSeparador.replaceAll("\\[\\]", "");
        
        String arrayKeys [] = igualaSeparador.split("@");
        
        return arrayKeys;
    }
    
    private Boolean isExisteNiveisNoObjetoIntegracao(String objetoIntegracao) {
        return objetoIntegracao.contains("/") || objetoIntegracao.contains("[]");
    }
    
    private Map<String, String> montaParametrosRequisicao(String classe, Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum) {
        Map<String, String> parametrosRequisicao = new HashMap<>();
        
        String cpfCnpjFormatado = StringUtils.leftPad(String.valueOf(cpfCnpj), TipoPessoaEnum.F.equals(tipoPessoaEnum) ? 11 : 14, '0');
        
        parametrosRequisicao.put("cpfcnpj", cpfCnpjFormatado);
        parametrosRequisicao.put("classe", classe);
        return parametrosRequisicao;
    }
    
    public void enviaRequisicaoProSicli(Map<String, Object> mapaDadosEnvioSICLI, EnumMetodoHTTP verbo, String classe, Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum) {
        // Executa a montagem do objeto final baseado na alimentação realizada com o mapa de dados finais.
        // Essa chamada possui carater recursivo devido a necessidade de percorrer toda a estrutura de mapas sobre mapas ou listas.
        // this.substituiMapasValoresFinais(mapaDadosEnvioSICLI, mapaDadosFinaisSICLI);
        
        Map<String, String> parametrosRequisicao = montaParametrosRequisicao(classe, cpfCnpj, tipoPessoaEnum);

        String corpoRequisicao;
        try {
            corpoRequisicao = UtilJson.converterParaJson(mapaDadosEnvioSICLI);
        } catch (Exception e) {
            String mensagem = "Falha ao converter o objeto que representa o corpo da mensagem de envio ao SICLI para Json.";
            LOGGER.log(Level.ALL, mensagem, e);
            throw new SimtrConfiguracaoException(mensagem, e);
        }

        LOGGER.info(MessageFormat.format("Chamando atualização SICLI: {0} {1} | parametros = {2} | corpo = {3}", verbo.name(), urlWebService, StringUtils.join(parametrosRequisicao), corpoRequisicao));
        Response responseAtualizacao = UtilRest.consumirServicoOAuth2JSON(urlWebService, verbo, cabecalhos, parametrosRequisicao, corpoRequisicao, this.keycloakUtil.getTokenServico());

        switch (responseAtualizacao.getStatusInfo().getFamily()) {
            case SUCCESSFUL:
                return;
            case SERVER_ERROR:
                throw new SicliException(responseAtualizacao.readEntity(String.class), Boolean.FALSE);
            case CLIENT_ERROR:
            default:
                throw new SicliException(responseAtualizacao.readEntity(String.class), Boolean.TRUE);
        }
    }
    
 // *************Métodos Privados ****************//
    /*
    @SuppressWarnings("rawtypes")
    private Boolean preencherComposicaoSocietaria(ObjetoIntegracao objetoIntegracao, Documento documentoLocalizado , String nomeCampoRetornoSICLI, Object objetoRetornoSICLI, Map<String, Object> mapaCorpoRequisicaoSICLI, List<Map<String, Object>> listMapObject, List<String> pendencias) {
        
        for(AtributoIntegracao atributoIntegracao : objetoIntegracao.getAtributosIntegracao()) {
            
               Optional<AtributoExtracao> atributoExtracao = atributoIntegracao.getAtributoExtracoes().stream()
                                                   .filter(atrExtracao -> 
                                                   atrExtracao.getTipoDocumento().getId().equals(documentoLocalizado.getTipoDocumento().getId())
                                                   && atrExtracao.getNomeAtributoDocumento().equals(atributoDocumentoResposta.getDescricao())).findFirst();
                if(atributoExtracao.isPresent()) {
                    break;
                }
             } 
            
            // adiciona resposta do documento no mapObject
            if(atributoExtracao.isPresent()) {
                mapaObject.put(atributoExtracao.get().getAtributoIntegracao().getAtributo(), atributoDocumentoResposta.getConteudo());
            }else {
                String pendencia = MessageFormat.format("Não foi localizado atributo integracao = {0}, parametrizado ao tipo Documento = {1}.", atributoIntegracaoAtual.getAtributo(), documentoLocalizado.getTipoDocumento().getNome());
                pendencias.add(pendencia);
            }
        
        }
        
        Map<String, List<AtributoDocumento>> mapDocumentoAgrupado = this.retornaMapAtributosDocumetoAgrupadoPeloArrayDocumento(objetoIntegracao, documentoLocalizado);
        
        for(String dominioAgrupador : mapDocumentoAgrupado.keySet()) {
            List<AtributoDocumento> atributosDocumentos = mapDocumentoAgrupado.get(dominioAgrupador);
            Map<String, Object> mapaObject = new HashMap<String, Object>();
            
            AtomicReference<String> nomeSenquencialCorreto = new AtomicReference<>();
            AtomicInteger valorSequencial =  new AtomicInteger();
            
            Boolean isTodosAtributosChaveOK = this.comparaTodosAtributoChave(objetoRetornoSICLI, objetoIntegracao, atributosDocumentos, documentoLocalizado.getTipoDocumento(), pendencias, nomeSenquencialCorreto, valorSequencial);
            
            
            }
            
            if(isTodosAtributosChaveOK) {// faz alteração 
                mapaObject.put(ATRIBUTO_TIPO_OPERACAO, objetoIntegracao.getAcaoComparacao());
                mapaObject.put(nomeSenquencialCorreto.get(), String.format("%04d", valorSequencial.get()));
            }else {
                // novo registro
                mapaObject.put(ATRIBUTO_TIPO_OPERACAO, objetoIntegracao.getAcao());
                mapaObject.put(nomeSenquencialCorreto.get(), String.format("%04d", 0));
            }
            
            
            listMapObject.add(mapaObject);
        }
        
        
        return listMapObject.isEmpty() ? false : true;
    }
    */
    
    // *************Métodos Privados ****************//
    @SuppressWarnings("rawtypes")
    private Boolean realizarComparacaoAtributosList(ObjetoIntegracao objetoIntegracao, Documento documentoLocalizado , String nomeCampoRetornoSICLI, Object objetoRetornoSICLI, Map<String, Object> mapaCorpoRequisicaoSICLI, List<Map<String, Object>> listMapObject, List<String> pendencias) {
        
        Map<String, List<AtributoDocumento>> mapDocumentoAgrupado = this.retornaMapAtributosDocumetoAgrupadoPeloArrayDocumento(objetoIntegracao, documentoLocalizado);
        
        for(String dominioAgrupador : mapDocumentoAgrupado.keySet()) {
            List<AtributoDocumento> atributosDocumentos = mapDocumentoAgrupado.get(dominioAgrupador);
            Map<String, Object> mapaObject = new HashMap<String, Object>();
            
            AtomicReference<String> nomeSenquencialCorreto = new AtomicReference<>();
            AtomicInteger valorSequencial =  new AtomicInteger();
            
            Boolean isTodosAtributosChaveOK = this.comparaTodosAtributoChave(objetoRetornoSICLI, objetoIntegracao, atributosDocumentos, documentoLocalizado.getTipoDocumento(), pendencias, nomeSenquencialCorreto, valorSequencial);
            
            for(AtributoDocumento atributoDocumentoResposta : atributosDocumentos) {
                
                Optional<AtributoExtracao> atributoExtracao = null;
                AtributoIntegracao atributoIntegracaoAtual = null;
                
                for(AtributoIntegracao atributoIntegracao : objetoIntegracao.getAtributosIntegracao()) {
                    atributoIntegracaoAtual = atributoIntegracao;
                    
                    atributoExtracao = atributoIntegracao.getAtributoExtracoes().stream()
                                                       .filter(atrExtracao -> 
                                                       atrExtracao.getTipoDocumento().getId().equals(documentoLocalizado.getTipoDocumento().getId())
                                                       && atrExtracao.getNomeAtributoDocumento().equals(atributoDocumentoResposta.getDescricao())).findFirst();
                    if(atributoExtracao.isPresent()) {
                        break;
                    }
                 } 
                
                // adiciona resposta do documento no mapObject
                if(atributoExtracao.isPresent()) {
                    mapaObject.put(atributoExtracao.get().getAtributoIntegracao().getAtributo(), atributoDocumentoResposta.getConteudo());
                }else {
                    String pendencia = MessageFormat.format("Não foi localizado atributo integracao = {0}, parametrizado ao tipo Documento = {1}.", atributoIntegracaoAtual.getAtributo(), documentoLocalizado.getTipoDocumento().getNome());
                    pendencias.add(pendencia);
                }
            }
            
            if(isTodosAtributosChaveOK) {// faz alteração 
                mapaObject.put(ATRIBUTO_TIPO_OPERACAO, objetoIntegracao.getAcaoComparacao());
                mapaObject.put(nomeSenquencialCorreto.get(), valorSequencial.get());
            }else {
                // novo registro
                mapaObject.put(ATRIBUTO_TIPO_OPERACAO, objetoIntegracao.getAcao());
                mapaObject.put(nomeSenquencialCorreto.get(),  0);
            }
            
            
            listMapObject.add(mapaObject);
        }
        
        return listMapObject.isEmpty() ? false : true;
    }
    
    public Map<String, List<AtributoDocumento>> retornaMapAtributosDocumetoAgrupadoPeloArrayDocumento(ObjetoIntegracao objetoIntegrcao, Documento documentoLocalizado){
        AtributoExtracao atributoExtracaoArrayDocumento = null;
        
        for(AtributoIntegracao atr : objetoIntegrcao.getAtributosIntegracao()) {
            
            Optional<AtributoExtracao> atributoExtracaoOptional = atr.getAtributoExtracoes().stream().filter(atrExtracao -> 
                                        Objects.nonNull(atrExtracao.getObjetoDocumento()) 
                                        && atrExtracao.getObjetoDocumento().contains("[]")
                                        && atrExtracao.getTipoDocumento().getId().equals(documentoLocalizado.getTipoDocumento().getId())).findFirst();
            
            if(atributoExtracaoOptional.isPresent()) {
                atributoExtracaoArrayDocumento = atributoExtracaoOptional.get();
                break;
            }
        }
        
        Map<String, List<AtributoDocumento>> hasMapAgrupadoPorDocumento = new HashMap<String, List<AtributoDocumento>>();
        
        if(Objects.nonNull(atributoExtracaoArrayDocumento)) {           
           String nomeAgrupador = this.retornaUltimoAgarupadoArrayDocumento(atributoExtracaoArrayDocumento.getObjetoDocumento());
            
           hasMapAgrupadoPorDocumento = documentoLocalizado.getAtributosDocumento().stream()        
                    .filter(atr ->  Objects.nonNull(atr.getDeObjeto()) 
                            && atr.getDeObjeto().contains(nomeAgrupador)
                            && atr.getDeObjeto().contains("[")
                            && atr.getDeObjeto().contains("]"))
                    .collect(Collectors.groupingBy(AtributoDocumento::getDeObjeto));
        }
        
        return hasMapAgrupadoPorDocumento;
    }
    
    String retornaUltimoAgarupadoArrayDocumento(String arrayDocumento) {
        String arrayCahves [] = this.retornaArrayDeChaves(arrayDocumento);
        
        String ultimaChave = arrayCahves[arrayCahves.length - 1];
        
        String nomeArray [] = ultimaChave.split("\\[");
        
        return nomeArray[0];
    }
    
    @SuppressWarnings({
        "rawtypes",
        "unchecked"
    })
    public Boolean comparaTodosAtributoChave(Object objetoRetornoSICLI, ObjetoIntegracao objetoIntegracao, List<AtributoDocumento> respostaAtributosDocumento, TipoDocumento tipoDocumento, List<String> listaPendencias, AtomicReference<String> nomeSenquencialCorreto, AtomicInteger valorSequencial) {
        boolean isTodosIguais = false;
        List list = (List) objetoRetornoSICLI;
        
        OperadorComparacaoEnum acaoComparacaoCompleta = null;
        List<AtributoIntegracao> listAtributosChaves = new  ArrayList<>();
        
        // comparação completa
        if(objetoIntegracao.getComparacaoCompleta() && objetoIntegracao.getOperadorComparacao() != null) {
            acaoComparacaoCompleta = objetoIntegracao.getOperadorComparacao();
            listAtributosChaves.addAll(objetoIntegracao.getAtributosIntegracao());
            
        }else {
            // identifica atributos chaves
            listAtributosChaves = objetoIntegracao.getAtributosIntegracao().stream()
                        .filter(atr -> atr.getChaveComparacao() && Objects.nonNull(atr.getOperadorComparacao()))
                        .collect(Collectors.toList());
        }
        
        Optional<AtributoDocumento> atributoRespostaDocumento = respostaAtributosDocumento.stream().filter(resposta ->  Objects.nonNull(resposta.getDeObjeto()) 
                                    && resposta.getDeObjeto().contains("[") && resposta.getDeObjeto().contains("]")).findFirst();
        
        if(!listAtributosChaves.isEmpty() && atributoRespostaDocumento.isPresent()) {
            
            for(Object obj : list) {
                
                LinkedHashMap<String, Object> objMap = (LinkedHashMap<String, Object>)  obj;
                
                Object objSequencial = this.retornaFieldSequencial(objMap);
                valorSequencial.set(Objects.nonNull(objSequencial) ? Integer.parseInt(objSequencial.toString()) : 0);
                nomeSenquencialCorreto.set(this.retornaNomeDoSequencialCorreto(objMap));
                
                List<Boolean> isChavesIguais = new ArrayList<>();
                
                for(AtributoIntegracao atributoIntegracao : listAtributosChaves) {
                    Object atributoRetornoSICLI = objMap.get(atributoIntegracao.getAtributo());
                  
                    // verificar se existe atributo chave no retorno do sicli
                    if(Objects.nonNull(atributoRetornoSICLI)) {
                        String valorSicli  = atributoRetornoSICLI.toString();
                        
                        Optional<AtributoExtracao> atributoExtracaoOptional = atributoIntegracao.getAtributoExtracoes()
                                                    .stream().filter(atr -> atr.getTipoDocumento().getId().equals(tipoDocumento.getId())).findFirst();
                        
                        // atributoExtracao identificado
                        if(atributoExtracaoOptional.isPresent()) {
                            AtributoExtracao atributoExtracao = atributoExtracaoOptional.get();
                            
                            // resposta identifcada
                            Optional<AtributoDocumento> respostaAtributo = respostaAtributosDocumento.stream()
                                    .filter(atrResposta -> atrResposta.getDescricao().equals(atributoExtracao.getNomeAtributoDocumento())).findFirst();
                            
                            if(respostaAtributo.isPresent()) {
                                if(this.comparacao(atributoIntegracao.getOperadorComparacao(), atributoRetornoSICLI, valorSicli, respostaAtributo.get(), acaoComparacaoCompleta)) {
                                    isChavesIguais.add(Boolean.TRUE);
                                }else {
                                    isChavesIguais.add(Boolean.FALSE);
                                } 
                            }else {
                                String pendencia = MessageFormat.format("Sem resposta para tipo atributo integracao = {0}, do tipo Documento = {1}", atributoIntegracao.getAtributo(), tipoDocumento.getNome());
                                listaPendencias.add(pendencia);
                            }
                            
                        }else {
                            String pendencia = MessageFormat.format("Sem atributo extração parametrizado para tipo atributo integração = {0}, do tipo Documento = {1}", atributoIntegracao.getAtributo(), tipoDocumento.getNome());
                            listaPendencias.add(pendencia);
                        }
                    } else {
                        String pendencia = MessageFormat.format("No retorno do Sicli não foi localizdo esse atribuo integração chave = {0}, do tipo Documento = {1}", atributoIntegracao.getAtributo(), tipoDocumento.getNome());
                        listaPendencias.add(pendencia);
                    }
                }
                
                int quantidadesChaves = listAtributosChaves.size();
                Long quantidadesChavesOk = isChavesIguais.stream().filter(isChaves -> isChaves).count();
                
                if(quantidadesChavesOk.intValue() == quantidadesChaves) {
                    isTodosIguais = Boolean.TRUE;
                    break;
                }
            } 
            
        }
        
        return isTodosIguais;
    }
    
    private Boolean realizarInsercaoObjetoListaIntegracao(ObjetoIntegracao objetoIntegracao, Documento documentoLocalizado , List<Map<String, Object>> listMapObject, List<String> pendencias) {
        Map<String, List<AtributoDocumento>> mapDocumentoAgrupado = this.retornaMapAtributosDocumetoAgrupadoPeloArrayDocumento(objetoIntegracao, documentoLocalizado);
        
        Integer valorSequencial =  0;
        
        for(String dominioAgrupador : mapDocumentoAgrupado.keySet()) {
            List<AtributoDocumento> atributosDocumentos = mapDocumentoAgrupado.get(dominioAgrupador);
            Map<String, Object> mapaObject = new HashMap<String, Object>();
            
            for(AtributoDocumento atributoDocumentoResposta : atributosDocumentos) {
                
                Optional<AtributoExtracao> atributoExtracao = null;
                AtributoIntegracao atributoIntegracaoAtual = null;
                
                for(AtributoIntegracao atributoIntegracao : objetoIntegracao.getAtributosIntegracao()) {
                    atributoIntegracaoAtual = atributoIntegracao;
                    
                    atributoExtracao = atributoIntegracao.getAtributoExtracoes().stream()
                                                       .filter(atrExtracao -> 
                                                       atrExtracao.getTipoDocumento().getId().equals(documentoLocalizado.getTipoDocumento().getId())
                                                       && atrExtracao.getNomeAtributoDocumento().equals(atributoDocumentoResposta.getDescricao())).findFirst();
                    if(atributoExtracao.isPresent()) {
                        break;
                    }
                 } 
                
                // adiciona resposta do documento no mapObject
                if(atributoExtracao.isPresent()) {
                    mapaObject.put(atributoExtracao.get().getAtributoIntegracao().getAtributo(), atributoDocumentoResposta.getConteudo());
                }else {
                    String pendencia = MessageFormat.format("Não foi localizado atributo integracao = {0}, parametrizado ao tipo Documento = {1}.", atributoIntegracaoAtual.getAtributo(), documentoLocalizado.getTipoDocumento().getNome());
                    pendencias.add(pendencia);
                }
            }
            
            // novo registro
            mapaObject.put(ATRIBUTO_TIPO_OPERACAO, objetoIntegracao.getAcao());
            mapaObject.put(NU_SEQUENCIAL, String.format("%04d", valorSequencial));
            
            listMapObject.add(mapaObject);
        }
        
        return listMapObject.isEmpty() ? false : true;
    }
    
    private Boolean realizarInsercaoObjetoSimplesIntegracao(ObjetoIntegracao objetoIntegracao, Documento documentoLocalizado, Map<String, Object> mapaObject, List<String> pendencias) {
        
        for(AtributoIntegracao atributoIntegracao : objetoIntegracao.getAtributosIntegracao()) {
            
            Optional<AtributoExtracao> atributoExtracao = atributoIntegracao.getAtributoExtracoes().stream()
                    .filter(ae -> ae.getTipoDocumento().equals(documentoLocalizado.getTipoDocumento())).findFirst();
            
            if (!atributoExtracao.isPresent()) {
                if(atributoIntegracao.getObrigatorio()) {
                    String pendencia = MessageFormat.format("Sem atributo extração parametrizado para tipo de documento = {0}", documentoLocalizado.getTipoDocumento().getNome());
                    pendencias.add(pendencia);
                    continue;
                }else {
                    continue;
                }
            }

            Optional<AtributoDocumento> atributoDocumentoOptional = documentoLocalizado.getAtributosDocumento().stream()
                    .filter(atributoDocumento -> atributoDocumento.getDescricao().equals(atributoExtracao.get().getNomeAtributoDocumento())).findFirst();
            
            if (atributoDocumentoOptional.isPresent()) {
                AtributoDocumento atributoDocumento = atributoDocumentoOptional.get();
                mapaObject.put(atributoIntegracao.getAtributo(), atributoDocumento.getConteudo());
            } else {
                if(atributoIntegracao.getObrigatorio()) {
                    String pendencia = MessageFormat.format("Não foi localizado Resposta para o Atributo Integracao = {0}, do tipo do Documento {1}", atributoIntegracao.getAtributo(), documentoLocalizado.getTipoDocumento().getNome());
                    pendencias.add(pendencia);
                    continue;
                }
            }
        } 
        
        if(!mapaObject.isEmpty() && !mapaObject.containsKey(ATRIBUTO_TIPO_OPERACAO) 
                && !ObjetoSemTpOperacaoSICLIEnum.PESSOA_JURIDICA.getDescricao().equalsIgnoreCase(this.retornaUltimoNivel(objetoIntegracao.getObjetoIntegracao()))
                && !ObjetoSemTpOperacaoSICLIEnum.DADOS_BASICOS.getDescricao().equalsIgnoreCase(this.retornaUltimoNivel(objetoIntegracao.getObjetoIntegracao()))) {
            
            mapaObject.put(ATRIBUTO_TIPO_OPERACAO, AcaoComparacaoEnum.I.toString());
            
        }
        
        return mapaObject.isEmpty() ? false : true;
    }

    
    private Object retornaFieldSequencial(LinkedHashMap<String, Object> objMap) {
        for(String key : objMap.keySet()) {
            if(key.equals(NU_SEQUENCIAL) || key.equals(SEQUENCIAL)) {
                return objMap.get(key);
            }
        }
        return null;
    }
    
    private String retornaNomeDoSequencialCorreto(LinkedHashMap<String, Object> objMap) {
        for(String key : objMap.keySet()) {
            if(key.equals(NU_SEQUENCIAL) || key.equals(SEQUENCIAL)) {
                return key;
            }
        }
        return null;
    }
    
    // *************Métodos Privados ****************//
    @SuppressWarnings("unchecked")
    private Boolean realizarComparacaoAtributosSimples(ObjetoIntegracao objetoIntegracao, Documento documentoLocalizado, Object objetoRetornoSICLI, Map<String, Object> mapaObject, Boolean isNaoEObjetoRaiz, List<String> pendencias) {
        LinkedHashMap<String, Object> hasMap = (LinkedHashMap<String, Object>)  objetoRetornoSICLI;
        
        // existe na consulta do sicli
        boolean existeObjetoNoSicli = Objects.nonNull(hasMap);
        
        for(AtributoIntegracao atributoIntegracao : objetoIntegracao.getAtributosIntegracao()) {
            
            if(existeObjetoNoSicli) {
                
                Object atributoRetornoSICLI = hasMap.get(atributoIntegracao.getAtributo());
                
                if(atributoRetornoSICLI == null) {
                    if(atributoIntegracao.getObrigatorio()) {
                        String pendencia = MessageFormat.format("Atributo de Integração não localizado no SILCI = {0}, do tipo Objeto Integracao {1}", atributoIntegracao.getAtributo(), objetoIntegracao.getObjetoIntegracao());
                        pendencias.add(pendencia);
                        continue;
                    }else {
                        continue;
                    }
                }
                
                String valorAtributoRetornoSICLI = atributoRetornoSICLI.toString();
                
                Optional<AtributoExtracao> atributoExtracao = atributoIntegracao.getAtributoExtracoes().stream()
                        .filter(ae -> ae.getTipoDocumento().equals(documentoLocalizado.getTipoDocumento())).findFirst();
                
                if (!atributoExtracao.isPresent()) {
                    if(atributoIntegracao.getObrigatorio()) {
                        String pendencia = MessageFormat.format("Sem atributo extração parametrizado para tipo de documento = {0}", documentoLocalizado.getTipoDocumento().getNome());
                        pendencias.add(pendencia);
                        continue;
                    }else {
                        continue;
                    }
                }

                Optional<AtributoDocumento> streamAtributoDocumento = null;
                
                if(isNaoEObjetoRaiz) {
                    streamAtributoDocumento = documentoLocalizado.getAtributosDocumento().stream()
                    .filter(atributoDocumento -> atributoDocumento.getDescricao().equals(atributoExtracao.get().getNomeAtributoDocumento()) 
                            && atributoDocumento.getDeObjeto().equals(atributoExtracao.get().getObjetoDocumento())).findFirst();
                }else {
                    streamAtributoDocumento = documentoLocalizado.getAtributosDocumento().stream()
                            .filter(atributoDocumento -> atributoDocumento.getDescricao().equals(atributoExtracao.get().getNomeAtributoDocumento())).findFirst();
                }
                
                AtributoDocumento atributoDocumento = null;
                if (streamAtributoDocumento.isPresent()) {
                    atributoDocumento = streamAtributoDocumento.get();
                }
                
                if(atributoIntegracao.getChaveComparacao() && atributoIntegracao.getOperadorComparacao() != null){
                    if(atributoDocumento != null && comparacao(atributoIntegracao.getOperadorComparacao(), atributoRetornoSICLI, valorAtributoRetornoSICLI, atributoDocumento, null)){
                        mapaObject.put(atributoIntegracao.getAtributo(), atributoDocumento.getConteudo());
                    }
                }else if(atributoDocumento != null) {
                    mapaObject.put(atributoIntegracao.getAtributo(), atributoDocumento.getConteudo());
                }
                
            }else {
                // objeto não existe no retorno do sicli exe: pessoaJuridica
                // obrigado a preencher objeto
                
                Optional<AtributoExtracao> atributoExtracao = atributoIntegracao.getAtributoExtracoes().stream()
                        .filter(ae -> ae.getTipoDocumento().equals(documentoLocalizado.getTipoDocumento())).findFirst();
                
                if (!atributoExtracao.isPresent()) {
                    if(atributoIntegracao.getObrigatorio()) {
                        String pendencia = MessageFormat.format("Sem atributo extração parametrizado para tipo de documento = {0}", documentoLocalizado.getTipoDocumento().getNome());
                        pendencias.add(pendencia);
                        continue;
                    }else {
                        continue;
                    }
                }

                Optional<AtributoDocumento> streamAtributoDocumento = null;
                
                if(isNaoEObjetoRaiz) {
                    streamAtributoDocumento = documentoLocalizado.getAtributosDocumento().stream()
                    .filter(atributoDocumento -> atributoDocumento.getDescricao().equals(atributoExtracao.get().getNomeAtributoDocumento()) 
                            && atributoDocumento.getDeObjeto().equals(atributoExtracao.get().getObjetoDocumento())).findFirst();
                }else {
                    streamAtributoDocumento = documentoLocalizado.getAtributosDocumento().stream()
                            .filter(atributoDocumento -> atributoDocumento.getDescricao().equals(atributoExtracao.get().getNomeAtributoDocumento())).findFirst();
                }
                
                AtributoDocumento atributoDocumento = null;
                if (streamAtributoDocumento.isPresent()) {
                    atributoDocumento = streamAtributoDocumento.get();
                }
                
                if(atributoDocumento != null) {
                    mapaObject.put(atributoIntegracao.getAtributo(), atributoDocumento.getConteudo());
                }
            }
        }
        
        if(!mapaObject.isEmpty() && !mapaObject.containsKey(ATRIBUTO_TIPO_OPERACAO) && isNaoEObjetoRaiz 
                && !ObjetoSemTpOperacaoSICLIEnum.PESSOA_JURIDICA.getDescricao().equalsIgnoreCase(this.retornaUltimoNivel(objetoIntegracao.getObjetoIntegracao()))
                && !ObjetoSemTpOperacaoSICLIEnum.DADOS_BASICOS.getDescricao().equalsIgnoreCase(this.retornaUltimoNivel(objetoIntegracao.getObjetoIntegracao()))) {
            
            mapaObject.put(ATRIBUTO_TIPO_OPERACAO, Objects.isNull(objetoIntegracao.getAcaoComparacao()) ? objetoIntegracao.getAcao() : objetoIntegracao.getAcaoComparacao());
        }
        
        return mapaObject.isEmpty() ? false : true;
    }
    
    private boolean comparacao(OperadorComparacaoEnum operacao, Object atributoRetornoSICLI, String valorAtributoRetornoSICLI, AtributoDocumento atributoDocumento, OperadorComparacaoEnum acaoComparacaoCompleta){
        // comparacao completa
        if(Objects.nonNull(acaoComparacaoCompleta)) {
            
            if(OperadorComparacaoEnum.IGUAL.equals(acaoComparacaoCompleta)){
                return atributoDocumento.getConteudo().equalsIgnoreCase(valorAtributoRetornoSICLI);
                
            }else if(OperadorComparacaoEnum.DIFERENTE.equals(acaoComparacaoCompleta)) {
                    return !atributoDocumento.getConteudo().equalsIgnoreCase(valorAtributoRetornoSICLI);
            } 
            
        }else {// comparação por atributo integracao
            if(OperadorComparacaoEnum.IGUAL.equals(operacao)){
                return atributoDocumento.getConteudo().equalsIgnoreCase(valorAtributoRetornoSICLI);
                
            }else if(OperadorComparacaoEnum.DIFERENTE.equals(operacao)) {
                    return !atributoDocumento.getConteudo().equalsIgnoreCase(valorAtributoRetornoSICLI);
            } 
        }
        return false;
                
        /*TODO
        else if(OperadorComparacaoEnum.MAIOR.equals(operacao)) {
                
        //      if(atributoDocumento != null && valorAtributoRetornoSICLI > atributoDocumento.getConteudo()) {
        //        //GUARDAR O SEQUENCIAL PARA USO NO ENVIO 
        //    }
                
        }else if(OperadorComparacaoEnum.MAIOR_IGUAL.equals(operacao)) {
                
        //      if(atributoDocumento != null && valorAtributoRetornoSICLI.equalsIgnoreCase(atributoDocumento.getConteudo())) {
        //        //GUARDAR O SEQUENCIAL PARA USO NO ENVIO 
        //    }
                
        }else if(OperadorComparacaoEnum.MENOR.equals(operacao)) {
                
        //      if(atributoDocumento != null && valorAtributoRetornoSICLI.equalsIgnoreCase(atributoDocumento.getConteudo())) {
        //        //GUARDAR O SEQUENCIAL PARA USO NO ENVIO 
        //    }
                
        }else if(OperadorComparacaoEnum.MENOR_IGUAL.equals(operacao)) {
                
        //      if(atributoDocumento != null && valorAtributoRetornoSICLI.equalsIgnoreCase(atributoDocumento.getConteudo())) {
        //        //GUARDAR O SEQUENCIAL PARA USO NO ENVIO 
        //    }
        }
        */
    }
    
    
    /**
     * 
     * @param cpfCnpj
     * @param tipoPessoaEnum
     * @param classe
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> verificaCadastroExistente(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, String classe) {
        this.carregaDadosComunicacao();
        

        Map<String, String> parametrosRequisicao = montaParametrosRequisicao(classe, cpfCnpj, tipoPessoaEnum);
        parametrosRequisicao.put("campos", DADOSGERAIS);

        // Realiza consulta ao SICLI para verificar se o CPF/CNPJ já possui cadastro
        LOGGER.info(MessageFormat.format("Chamando consulta SICLI: GET {0} | parametros = {1}", urlWebService, StringUtils.join(parametrosRequisicao)));
        Response responseConsulta = UtilRest.consumirServicoOAuth2(this.urlWebService, EnumMetodoHTTP.GET, cabecalhos, parametrosRequisicao, null, this.keycloakUtil.getTokenServico(), null, APPLICATION_JSON);

        // Captura o corpo e o codigo de retorno da consulta reaizada
        int httpStatusCode = responseConsulta.getStatus();
        String retornoSICLI = responseConsulta.readEntity(String.class);

        if (Response.Status.Family.SERVER_ERROR.equals(responseConsulta.getStatusInfo().getFamily())) {
            throw new SicliException("Falha ao consultar cliente no SICLI", Boolean.FALSE);
        }

        if (Response.Status.NOT_FOUND.getStatusCode() == httpStatusCode) {
            return null;
        }

        if (Response.Status.BAD_REQUEST.getStatusCode() == httpStatusCode) {
            String objRetorno = UtilJson.consultarAtributo(retornoSICLI, ATRIBUTO_RETORNO);
            try {
                Map<String, Object> mapaRetorno = (Map<String, Object>) UtilJson.converterDeJson(objRetorno, Map.class);
                int situacao = Integer.parseInt(mapaRetorno.get(ATRIBUTO_CODIGO_SITUACAO).toString());
                if (situacao == 9) {
                    return null;
                }
            } catch (Exception ex) {
                throw new SicliException("Falha ao converter o retorno da consulta ao Cadastro CAIXA", ex, Boolean.TRUE);
            }
        }
        Map<String, Object> mapaRetorno = null;
        try {
            mapaRetorno = (Map<String, Object>) UtilJson.converterDeJson(retornoSICLI, Map.class);
        }catch(Exception ex) {
            throw new SicliException("Falha ao converter o retorno da consulta ao Cadastro CAIXA", ex, Boolean.TRUE);
        }
        return mapaRetorno;
    }
    
    private void carregaDadosComunicacao() {
        if (urlWebService == null) {
            urlWebService = UtilParametro.getParametro(PARAMETRO_SICLI, true);
            apiKey = UtilParametro.getParametro(ConstantesUtil.API_KEY_SIMTR, Boolean.TRUE);

            cabecalhos.put("apikey", this.apiKey);
            cabecalhos.put("Content-Type", APPLICATION_JSON);
        }
    }

    /**
     * 
     * @param idProcessoDossie
     * @return
     */
    private List<PriorizacaoObjetoIntegracao> consultaPriorizacoesoObjetoIntegracaoPeloProcesso(Integer idProcessoDossie){
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT poi FROM PriorizacaoObjetoIntegracao poi ");
        jpql.append(" LEFT JOIN FETCH poi.objetoIntegracao objInt ");
        jpql.append(" LEFT JOIN FETCH objInt.atributosIntegracao atbsInt ");
        jpql.append(" LEFT JOIN FETCH atbsInt.atributoExtracoes ");
        jpql.append(" WHERE poi.processoDossie.id = :idProcessoDossie ");
        jpql.append(" ORDER BY poi.ordemPrioridade ASC");
        
        TypedQuery<PriorizacaoObjetoIntegracao> queryListPriorizacaoObjetoIntegracao = this.entityManager.createQuery(jpql.toString(), PriorizacaoObjetoIntegracao.class);
        queryListPriorizacaoObjetoIntegracao.setParameter("idProcessoDossie", idProcessoDossie);
        
        return queryListPriorizacaoObjetoIntegracao.getResultList();

    }
    
    //Utility function
    @SuppressWarnings("unused")
    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) 
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public void inserirObjetoIntegracao(final DossieProduto dossieProduto, final Integer classe) {
        final Map<String, Object> mapaSicli = new HashMap<>();
        mapaSicli.put("classe", classe.toString());
        Optional<DossieClienteProduto> dossieClientePrincipalOP = dossieProduto.getDossiesClienteProduto().stream()
                .filter(dossieClienteProduto -> dossieClienteProduto.getTipoRelacionamento().getIndicadorPrincipal()).findFirst();
        if (!dossieClientePrincipalOP.isPresent()) {
            throw new SimtrAtributoIntegracaoException("Dossie cliente principal não encontrado para dossie produto: " + dossieProduto.getId());
        }
        DossieClienteProduto dossieClientePrincipal = dossieClientePrincipalOP.get();
        List<ObjetoIntegracao> objetoIntegracaos = this.consultaObjetoIntegracaoPeloProcesso(dossieProduto.getProcesso().getId());
        for (ObjetoIntegracao objetoIntegracao : objetoIntegracaos) {
            if (Objects.nonNull(objetoIntegracao.getObjetoIntegracao())) {
                String[] arrayCamposPesquisarDocumento = objetoIntegracao.getObjetoIntegracao().split("/");
                // TODO fiz somente para quando tiver objeto (pode ser raiz tambem) e array Ex:
                // dadosBasico, enderecos[]
                // Fazer para quanto tiver composicaoSocietario[]/socios[]
                if (arrayCamposPesquisarDocumento.length == 1) {
                    final String campo = arrayCamposPesquisarDocumento[0];
                    this.pesquisaAtributosIntegracaoPelaListaPriorizacao(objetoIntegracao, dossieProduto, dossieClientePrincipal, campo, mapaSicli);
                } else {

                }
            } else {
                this.pesquisaAtributosIntegracaoPelaListaPriorizacao(objetoIntegracao, dossieProduto, dossieClientePrincipal, null, mapaSicli);
            }
        }

    }

    @SuppressWarnings("unchecked")
    private void pesquisaAtributosIntegracaoPelaListaPriorizacao(final ObjetoIntegracao objetoIntegracao, DossieProduto dossieProduto,
            DossieClienteProduto dossieClientePrincipal, final String campo, final Map<String, Object> result) {

        Integer tamanhoLista = null;
        for (final AtributoIntegracao atributoIntegracao : objetoIntegracao.getAtributosIntegracao()) {
            Object valor = null;
            for (final PriorizacaoObjetoIntegracao priorizacaoObjetoIntegracao : objetoIntegracao.getPriorizacaoObjetoIntegracaos()) {
                if (SICLITipoFonteEnum.D.equals(priorizacaoObjetoIntegracao.getFonte())) {
                    valor = this.pesquisaPorDocumento(dossieProduto, priorizacaoObjetoIntegracao, dossieClientePrincipal, atributoIntegracao, campo);
                } else if (SICLITipoFonteEnum.F.equals(priorizacaoObjetoIntegracao.getFonte())) {
                    valor = this.pesquisaPorFormulario(dossieProduto, atributoIntegracao);
                }
                if (valor != null) {
                    break;
                }
            }

            if (valor == null && atributoIntegracao.getObrigatorio()) {
                throw new SimtrAtributoIntegracaoException(
                        MessageFormat.format("Não encontrado valor para o atributo {0} ", atributoIntegracao.getAtributo()));
            } else if (valor != null) {
                if (valor instanceof List) {
                    String nomeCampoEntrada = campo.replaceAll("\\[\\]", "");
                    List<AtributoDocumento> listaAD = (List<AtributoDocumento>) valor;

                    // Guarda o tamanho da lista porque se vier algo diferente
                    // nos próximo é erro
                    if (tamanhoLista == null) {
                        tamanhoLista = listaAD.size();
                    } else if (!tamanhoLista.equals(listaAD.size()) && atributoIntegracao.getObrigatorio()) {
                        throw new SimtrAtributoIntegracaoException(
                                MessageFormat.format("Não encontrado valor para o atributo {0} dentro da lista {1}  ",
                                        atributoIntegracao.getAtributo(), nomeCampoEntrada));
                    }

                    Object list = result.get(nomeCampoEntrada);
                    List<Map<String, Object>> listConvertida = null;
                    if (list == null) {
                        listConvertida = new ArrayList<>();
                        result.put(nomeCampoEntrada, listConvertida);
                    } else {
                        listConvertida = (List<Map<String, Object>>) list;
                    }

                    for (int i = 0; i < listaAD.size(); i++) {
                        Map<String, Object> mapInterno = listConvertida.get(i);
                        if (mapInterno == null) {
                            mapInterno = new HashMap<>();
                        }
                        mapInterno.put(listaAD.get(i).getDescricao(), listaAD.get(i).getConteudo());
                    }
                } else {
                    result.put(atributoIntegracao.getAtributo(), valor);
                }
            }
        }
    }

    private Object pesquisaPorFormulario(DossieProduto dossieProduto, AtributoIntegracao atributoIntegracao) {
        Optional<RespostaDossie> optional = dossieProduto.getRespostasDossie().stream()
                .filter(resp -> resp.getCampoFormulario().getAtributoIntegracao() != null
                        && resp.getCampoFormulario().getAtributoIntegracao().getId().equals(atributoIntegracao.getId()))
                .findFirst();
        if (optional.isPresent()) {
            RespostaDossie respostaDossie = optional.get();
            if (this.isCampoOpcao(respostaDossie.getCampoFormulario().getCampoEntrada().getTipo())) {
                Iterator<OpcaoCampo> iterator = respostaDossie.getOpcoesCampo().iterator();
                if (iterator.hasNext()) {
                    return iterator.next().getValue();
                }
                return null;
            }
            return respostaDossie.getRespostaAberta();
        }

        return null;
    }

    private Object pesquisaPorDocumento(DossieProduto dossieProduto, PriorizacaoObjetoIntegracao priorizacaoObjetoIntegracao,
            DossieClienteProduto dossieClientePrincipal, final AtributoIntegracao atributoIntegracao, final String campo) {
        List<InstanciaDocumento> instanciasDocumentoDossieClientePrincipal = new ArrayList<>();
        dossieProduto.getInstanciasDocumento().forEach(instanciaDocumento -> {
            if ((instanciaDocumento.getDossieClienteProduto() != null)
                    && (instanciaDocumento.getDossieClienteProduto().equals(dossieClientePrincipal))) {
                instanciasDocumentoDossieClientePrincipal.add(instanciaDocumento);
            }
        });
        Documento documentoLocalizado = null;
        Optional<Documento> documento = instanciasDocumentoDossieClientePrincipal.stream().map(instanciaDoc -> instanciaDoc.getDocumento())
                .filter(doc -> priorizacaoObjetoIntegracao.getTipoDocumento().equals(doc.getTipoDocumento()))
                .filter(doc -> !doc.getAtributosDocumento().isEmpty())
                .filter(doc -> TemporalidadeDocumentoEnum.TEMPORARIO_ANTIFRAUDE.equals(doc.getSituacaoTemporalidade())
                        || TemporalidadeDocumentoEnum.VALIDO.equals(doc.getSituacaoTemporalidade()))
                .max(Comparator.comparing(Documento::getDataHoraCaptura));

        if (documento.isPresent()) {
            documentoLocalizado = documento.get();
            Stream<AtributoDocumento> streamAtributoDocumento = documentoLocalizado.getAtributosDocumento().stream()
                    .filter(atributoDocumento -> atributoDocumento.getDescricao().equals(atributoIntegracao.getAtributo()));
            if (this.isArray(campo)) {
                Optional<AtributoExtracao> atributoExtracao = atributoIntegracao.getAtributoExtracoes().stream()
                        .filter(ae -> ae.getTipoDocumento().equals(priorizacaoObjetoIntegracao.getTipoDocumento())).findFirst();
                if (!atributoExtracao.isPresent()) {
                    throw new SimtrAtributoIntegracaoException("Sem atributo extração parametrizado para tipo de documento: "
                            + priorizacaoObjetoIntegracao.getTipoDocumento().getNome());
                }
                String nomeCampoEntrada = atributoExtracao.get().getObjetoDocumento().replaceAll("\\[\\]", "");
                return streamAtributoDocumento.filter(at -> at.getDeObjeto().contains(nomeCampoEntrada)).sorted((first, second) -> {
                    String firstP = first.getDeObjeto().replaceAll("[^0-9]", "");
                    String secondP = second.getDeObjeto().replaceAll("[^0-9]", "");
                    return Integer.compare(Integer.parseInt(firstP), Integer.parseInt(secondP));
                }).collect(Collectors.toList());
            } else {
                Optional<AtributoDocumento> opcao = streamAtributoDocumento.findFirst();
                if (opcao.isPresent()) {
                    return opcao.get().getConteudo();
                }
            }
        }

        return null;
    }

    /**
     * 
     * @param idProcessoDossie
     * @return
     */
    private List<ObjetoIntegracao> consultaObjetoIntegracaoPeloProcesso(Integer idProcessoDossie) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT DISTINCT OI FROM ObjetoIntegracao OI ");
        jpql.append(" LEFT JOIN FETCH OI.atributosIntegracao AI ");
        jpql.append(" LEFT JOIN FETCH AI.atributoExtracoes AE LEFT JOIN FETCH AE.tipoDocumento TP");
        jpql.append(" LEFT JOIN FETCH OI.priorizacaoObjetoIntegracaos POI ");
        jpql.append(" WHERE POI.processoDossie.id = :idProcessoDossie ");
        jpql.append(" ORDER BY POI.ordemPrioridade ASC");

        TypedQuery<ObjetoIntegracao> queryListPriorizacaoObjetoIntegracao = this.entityManager.createQuery(jpql.toString(), ObjetoIntegracao.class);
        queryListPriorizacaoObjetoIntegracao.setParameter("idProcessoDossie", idProcessoDossie);

        return queryListPriorizacaoObjetoIntegracao.getResultList();

    }

    private boolean isArray(final String campo) {
        return campo != null && campo.contains("[]");
    }
    
    private List<ObjetoIntegracao> retornaListaOrdenada(List<ObjetoIntegracao> lista){
        return lista.stream().sorted((primeiro, segundo) -> {
          
            String objDes1 = primeiro.getObjetoIntegracao();
            String objDes2 = segundo.getObjetoIntegracao();
            
            if (objDes1 == null && objDes2 == null) {
                return 0;
            }
            if (objDes1 != null && objDes2 == null) {
                return 1;
            }
            if (objDes1 == null && objDes2 != null) {
                return -1;
            }
            
            // descobrir quantidades de niveis
            Integer obj1 = objDes1.split("/").length + objDes1.split("\\[\\]").length;
            Integer obj2 = objDes2.split("/").length + objDes2.split("\\[\\]").length;
            
            return obj1.compareTo(obj2);
                
        }).collect(Collectors.toList());
    }
}
