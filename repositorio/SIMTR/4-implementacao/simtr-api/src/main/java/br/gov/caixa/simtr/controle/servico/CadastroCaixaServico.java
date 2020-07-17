package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.sicli.dto.RetornoClasse1DTO;
import br.gov.caixa.pedesgo.arquitetura.sicli.dto.inclusao.pf.SicliPFRequestDTO;
import br.gov.caixa.pedesgo.arquitetura.sicli.dto.inclusao.pj.SicliPJRequestDTO;
import br.gov.caixa.pedesgo.arquitetura.sicli.servico.ISicliServico;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.pedesgo.arquitetura.util.UtilParametro;
import br.gov.caixa.pedesgo.arquitetura.util.UtilRest;
import br.gov.caixa.pedesgo.arquitetura.util.UtilString;
import br.gov.caixa.pedesgo.arquitetura_common.entities.ComunicacaoWSDTO;
import br.gov.caixa.simtr.controle.excecao.SicliException;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.RespostaDossie;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.util.KeycloakUtil;

@Stateless
@PermitAll
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class CadastroCaixaServico {

    ////////////// CONTANTES UTILIZADAS NA ATUALIZACAO DE DADOS DO SICLI
    private static final String ATRIBUTO_CODIGO_SITUACAO = "codSituacao";
    private static final String ATRIBUTO_RETORNO = "retorno";
    private static final String ATRIBUTO_SEQUENCIAL = "sequencial";
    private static final String ATRIBUTO_TIPO_OPERACAO = "tpOperacao";
    private static final String PARAMETRO_SICLI = "url.servico.atualiza.cliente.sicli";
    private static final String CLASSE1 = "1";
    private static final String CLASSE5 = "5";
    private static final String DADOSBASICOS = "dadosbasicos";
    private static final String DADOSGERAIS = "dadosgerais";
    private static final String APPLICATION_JSON = "application/json";

    @EJB
    private CalendarUtil calendarUtil;

    @EJB
    private CanalServico canalServico;

    @EJB
    private KeycloakUtil keycloakUtil;

    @EJB
    private ISicliServico sicliServico;

    private String urlWebService;
    private String apiKey;
    private final Map<String, String> cabecalhos = new HashMap<>();
    private static final Logger LOGGER = Logger.getLogger(CadastroCaixaServico.class.getName());

    /**
     * Rotina de atualização de dados no SICLI baseado em um documento utilizando serviço REST diponivel com ação sob a classe documental do dossiê digital.
     * Parametro utilizado na chamada: campos=precargadd
     *
     * @param cpfCnpj CPF/CNPJ do cliente a ter os dados atualizados
     * @param tipoPessoaEnum Indicador se Pessoa Fisica ou Pessoa Juridica
     * @param documentos Lista de documento utilizado para atualização de dados
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public void atualizaDadosDossieDigital(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, List<RespostaDossie> respostasDossieCamposFormularioSICLI, List<Documento> documentos) {

        /* @formatter:off
        
        Para a montagem da mensagem do SICLI é necessario montar uma estrutura de dados padronizada de forma a atender todas as possibilidades de corpo incluindo 
        elementos definidos em uma lista (array).
        
        De forma a atender essa necessidade, foi definido um padão na parametrização em que o elementos que representam a estrutura do bloco em que os elementos finais comportam-se:
        * Elementos finais tipo lista:
            - São configuradas COM indicação de colchetes [] e agrupam os dados baseado em um numeral que passa a ser utilizado como chave no mapa de dados
            - Esse numero não significa sequencia de inclusão, apenas será utilizado como chave para agrupar os valores no mapa de dados
        
        * Elementos finais simples:
            - São configuradas SEM indicação de colchetes [] e agrupam os dados baseado utilizando o valor 0 (zero) como chave no mapa de dados
        
        Exemplos:
        
        Informação                      | Estrutura (Objeto)            | Atributo
        ------------------------------------------------------------------------------------
        Rua do Endereço Residencial     | pessoaFisica/enderecos[10]    | logradouro
        Bairro do Endereço Residencial  | pessoaFisica/enderecos[10]    | bairro
        Rua do Endereço Comercial       | pessoaFisica/enderecos[20]    | logradouro
        Bairro do Endereço Comercial    | pessoaFisica/enderecos[20]    | bairro
        Rua do Endereço Declarado       | pessoaFisica/enderecos[15]    | logradouro
        Bairro do Endereço Declarado    | pessoaFisica/enderecos[15]    | bairro
        Nome Social                     | pessoaFisica/dadosBasicos     | nomeSocial
                
        Considerando este fato serão utilizados dois mapas para representar os estagios necessarios para criar a mensagem final a ser enviada.
        - MapaDadosEnvioSICLI
        - MapaDadosFinaisSICLI
                
        Primeiro é montado o mapaDadosEnvio criando uma situação intermediaria que padroniza a estrutura de dados independente do elemento da ponta ser uma lista ou não.
        
        Ao final, essa estrutura é transcrita para o mapa final transformanso os mapas que possuem não possuem a chave 0 em Listas de Mapas
        criar então listas de objetos na representação do JSON a ser encaminhado.
        
        Segue abaixo a estrutura que se obtem ao concluir a montagem de dados de envio ao SICLI:
        
        Dados Finais:
        -------------

        Chave			| Valor -> DADOS FINAIS
        -------------------------------------------------------
        endereco		| Chave	| Valor -> isArray = true
				| ---------------------------------
				|   1	| Chave		| Valor
				| 	| ---------------------------------
				|	| sequencial	| 0
				|	| logradouro	| XXXXXXXXXXXXXX
				|	| cep		| 12345000
				|	| dtApuracao	| 99/99/9999
				| ---------------------------------
				|   2	| Chave		| Valor
				| 	| ---------------------------------
				|	| sequencial	| 0
				|	| logradouro	| YYYYYYYYYYYYYY
				|	| cep		| 987654000
				|	| dtApuracao	| 99/99/9999 
        -------------------------------------------------------
        dadosPessoais           | Chave	| Valor -> isArray = false
				| ---------------------------------
				|   0	| Chave		| Valor
				| 	|---------------------------------
				| 	| logradouro	| XXXXXXXXXXXXXX
				| 	| cep		| 12345000
				| 	| dtApuracao	| 99/99/9999
				|---------------------------------------------
				|
        @formatter:on 
        */

        String cpfCnpjFormatado = StringUtils.leftPad(String.valueOf(cpfCnpj), TipoPessoaEnum.F.equals(tipoPessoaEnum) ? 11 : 14, '0');

        Map<String, String> parametrosRequisicao = new HashMap<>();
        parametrosRequisicao.put("cpfcnpj", cpfCnpjFormatado);
        parametrosRequisicao.put("classe", CLASSE5);

        // Inicializa a o hash map que será utilizado para constução do JSON de envio ao SICLI
        Map<String, Object> mapaDadosEnvioSICLI = new HashMap<>();
        // Inicializa a o hash map que será utilizado para montagem dos objetos que serão utilizados na alimentação final da mntagem ao SICLI
        Map<String, Object> mapaDadosFinaisSICLI = new HashMap<>();

        // Analisa o retorno para concluir se já existe cadastro
        boolean cadastroExistente = this.verificaCadastroExistente(cpfCnpj, tipoPessoaEnum, CLASSE5);

        // Captura data/hora para definir no documento o momento de atualização do cadastro
        Calendar dataHoraAtualizacaoCadastro = Calendar.getInstance();

        for(RespostaDossie respostaDossie : respostasDossieCamposFormularioSICLI) {
            // Remove os acentos por ventura existentes na palavra ao enviar para o SICLI
            String valor = UtilString.removerAcentos(respostaDossie.getRespostaAberta());
            // Formata o valor removendo tabualações, quebra de linha
            valor = valor.replaceAll("\t", " ").replaceAll("\n", " ");
            // Formata o valor removendo espaços adicionais antes e depois da palavra, transformando toda a palavra em maiusculo
            valor = valor.trim().toUpperCase();    
            
            // Transforma o dado conforme o tipo esperado pelo SICLI
            String[] estruturaBloco = respostaDossie.getCampoFormulario().getNomeObjetoSICLI().split("/");
            try {
                Object conteudo = this.converteValorAtributoDocumento(valor, respostaDossie.getCampoFormulario().getTipoAtributoSicliEnum());

                // Submete atributo a inclusão no hash map para obedecer niveis parametrizados pelo objetoSICLI
                this.incluiValorAributo(mapaDadosEnvioSICLI, mapaDadosFinaisSICLI, conteudo, respostaDossie.getCampoFormulario().getNomeAtributoSICLI(), estruturaBloco);
            } catch (RuntimeException | ParseException ex) {
                this.incluiValorAributo(mapaDadosEnvioSICLI, mapaDadosFinaisSICLI, valor, respostaDossie.getCampoFormulario().getNomeAtributoSICLI(), estruturaBloco);
                String mensagem = MessageFormat.format("CSS.aDDD.001 - Falha ao converter atributo para o SICLI. Valor = {0} | Causa = {1}", valor, ex.getLocalizedMessage());
                LOGGER.log(Level.WARNING, mensagem);
                // Incluido registro de log da exceção para não gerar apontamento do SONAR.
                LOGGER.log(Level.ALL, mensagem, ex);
            }

        }
        
        // Percorre a lista de documento realizando a inserção dos atributos no JSON conforme parametrização na tabela de Atributos Extração
        for (Documento documento : documentos) {
            documento.setDataHoraCadastroCliente(dataHoraAtualizacaoCadastro);

            // Mapiea os registros do atributo extyração utilizando como chave o valor do campo nomeAtributoDocumento que também é utilizado coo chave no armazenamento dos
            // atributos do documento
            Map<String, AtributoExtracao> mapaAtributosSICLI = documento.getTipoDocumento().getAtributosExtracao().stream()
                                                                        .filter(ae -> Objects.nonNull(ae.getNomeAtributoSICLI()) && Objects.nonNull(ae.getNomeObjetoSICLI()))
                                                                        .collect(Collectors.toMap(AtributoExtracao::getNomeAtributoDocumento, ae -> ae));

            Map<String, String> atributosDocumento = documento.getAtributosDocumento().stream()
                                                              .filter(ad -> ad.getConteudo() != null || (ad.getOpcoesSelecionadas() != null && !ad.getOpcoesSelecionadas().isEmpty()))
                                                              .collect(Collectors.toMap(AtributoDocumento::getDescricao, ad -> {
                                                                  if(ad.getConteudo() != null){
                                                                      return ad.getConteudo();
                                                                  }
                                                                  
                                                                  int qtdeSelecionados = ad.getOpcoesSelecionadas().size();
                                                                  StringBuilder retorno = new StringBuilder();
                                                                  if(qtdeSelecionados > 1){
                                                                    retorno.append("[");
                                                                    ad.getOpcoesSelecionadas().forEach(ae -> retorno.append(ae.getValorOpcao()).append(","));
                                                                    retorno.deleteCharAt(retorno.length());
                                                                    retorno.append("]");
                                                                  } else {
                                                                    ad.getOpcoesSelecionadas().forEach(ae -> retorno.append(ae.getValorOpcao()));
                                                                  }
                                                                  return retorno.toString();
                                                              }));            
            // Percorre cada um dos atributos do definido na tipologia documental e inclui os valores localizados no documento capturado do GED
            // no HashMap base de envio ao SICLI conforme regras de cada Atributo Extração parametrizadoem banco.
            mapaAtributosSICLI.entrySet().forEach(registro -> {
                AtributoExtracao atributoExtracao = registro.getValue();
                String valor = atributosDocumento.get(atributoExtracao.getNomeAtributoDocumento());

                if (valor == null) {
                    valor = atributoExtracao.getValorPadrao();
                }

                this.incluiConteudoMapasValoresFinais(valor, atributoExtracao, tipoPessoaEnum, mapaDadosEnvioSICLI, mapaDadosFinaisSICLI);
            });
        }

        
        // Executa a montagem do objeto final baseado na alimentação realizada com o mapa de dados finais.
        // Essa chamada possui carater recursivo devido a necessidade de percorrer toda a estrutura de mapas sobre mapas ou listas.
        this.substituiMapasValoresFinais(mapaDadosEnvioSICLI, mapaDadosFinaisSICLI);

        String corpoRequisicao;
        try {
            corpoRequisicao = UtilJson.converterParaJson(mapaDadosEnvioSICLI);
        } catch (Exception e) {
            String mensagem = "Falha ao converter o objeto que representa o corpo da mensagem de envio ao SICLI para Json.";
            LOGGER.log(Level.ALL, mensagem, e);
            throw new SimtrConfiguracaoException(mensagem, e);
        }

        EnumMetodoHTTP verbo = cadastroExistente ? EnumMetodoHTTP.PUT : EnumMetodoHTTP.POST;
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

    @PermitAll
    public RetornoClasse1DTO consultaCadastroClienteClaseHomologada(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum) {
        this.validaPermissaoCanalComunicacao();
        String cpfCnpjFormatado = StringUtils.leftPad(String.valueOf(cpfCnpj), TipoPessoaEnum.F.equals(tipoPessoaEnum) ? 11 : 14, '0');
        try {
            return this.sicliServico.consultarSicli(cpfCnpjFormatado, DADOSGERAIS);
        } catch (Exception exception) {
            String mensagem = MessageFormat.format("SS.cCC.001 - Falha na comunicação com o SICLI para consulta de cliente. Tipo Cliente: {0} | CPF/CNPJ:{1}", tipoPessoaEnum, cpfCnpjFormatado);
            throw new SicliException(mensagem, exception, Boolean.TRUE);
        }
    }

    @PermitAll
    public ComunicacaoWSDTO atualizaCadastroClientePessoaFisica(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, SicliPFRequestDTO sicliPFRequestDTO) {
        return this.atualizaCadastroClientePessoaFisica(cpfCnpj, tipoPessoaEnum, CLASSE1, sicliPFRequestDTO);
    }

    @PermitAll
    public ComunicacaoWSDTO atualizaCadastroClientePessoaFisica(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, String classe, SicliPFRequestDTO sicliPFRequestDTO) {
        this.validaPermissaoCanalComunicacao();
        try {
            boolean cadastroExistente = this.verificaCadastroExistente(cpfCnpj, tipoPessoaEnum, classe);
            return this.sicliServico.insertOrUpdateSicli(sicliPFRequestDTO, !cadastroExistente);
        } catch (Exception exception) {
            throw new SicliException("SS.aCCPF.001 - Falha na comunicação com o SICLI para atualização da Pessoa Física", exception, Boolean.TRUE);
        }
    }

    @PermitAll
    public ComunicacaoWSDTO atualizaCadastroClientePessoaJuridica(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, SicliPJRequestDTO sicliPJRequestDTO) {
        return this.atualizaCadastroClientePessoaJuridica(cpfCnpj, tipoPessoaEnum, CLASSE1, sicliPJRequestDTO);
    }

    @PermitAll
    public ComunicacaoWSDTO atualizaCadastroClientePessoaJuridica(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, String classe, SicliPJRequestDTO sicliPJRequestDTO) {
        this.validaPermissaoCanalComunicacao();
        try {
            boolean cadastroExistente = this.verificaCadastroExistente(cpfCnpj, tipoPessoaEnum, classe);
            return this.sicliServico.insertOrUpdateSicli(sicliPJRequestDTO, !cadastroExistente);
        } catch (Exception exception) {
            throw new SicliException("SS.aCCPF.001 - Falha na comunicação com o SICLI para atualização da Pessoa Jurídica", exception, Boolean.TRUE);
        }
    }

    // *************Métodos Privados ****************//
    private void carregaDadosComunicacao() {
        if (urlWebService == null) {
            urlWebService = UtilParametro.getParametro(PARAMETRO_SICLI, true);
            apiKey = UtilParametro.getParametro(ConstantesUtil.API_KEY_SIMTR, Boolean.TRUE);

            cabecalhos.put("apikey", this.apiKey);
            cabecalhos.put("Content-Type", APPLICATION_JSON);
        }
    }

    private void validaPermissaoCanalComunicacao() {
        String clienteSSO = this.keycloakUtil.getClientId();
        Canal canal = this.canalServico.getByClienteSSO(clienteSSO);
        if (!canal.getIndicadorOutorgaCadastroCaixa()) {
            String mensagem = MessageFormat.format("Canal de comunicação não autorizado realizar atualização do Cadastro CAIXA através do SIMTR. Client ID: {0}", clienteSSO);
            throw new SimtrPermissaoException(mensagem);
        }
    }

    /**
     * 
     * @param cpfCnpj
     * @param tipoPessoaEnum
     * @param classe
     * @return
     */
    public boolean verificaCadastroExistente(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, String classe) {
        this.carregaDadosComunicacao();
        String cpfCnpjFormatado = StringUtils.leftPad(String.valueOf(cpfCnpj), TipoPessoaEnum.F.equals(tipoPessoaEnum) ? 11 : 14, '0');

        Map<String, String> parametrosRequisicao = new HashMap<>();
        parametrosRequisicao.put("cpfcnpj", cpfCnpjFormatado);
        parametrosRequisicao.put("classe", classe);
        parametrosRequisicao.put("campos", DADOSBASICOS);

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
            return Boolean.FALSE;
        }

        if (Response.Status.BAD_REQUEST.getStatusCode() == httpStatusCode) {
            String objRetorno = UtilJson.consultarAtributo(retornoSICLI, ATRIBUTO_RETORNO);
            try {
                Map<String, Object> mapaRetorno = (Map<String, Object>) UtilJson.converterDeJson(objRetorno, Map.class);
                int situacao = Integer.parseInt(mapaRetorno.get(ATRIBUTO_CODIGO_SITUACAO).toString());

                if (situacao == 9) {
                    return Boolean.FALSE;
                }
            } catch (Exception ex) {
                throw new SicliException("Falha ao converter o retorno da consulta ao Cadastro CAIXA", ex, Boolean.TRUE);
            }
        }

        return Boolean.TRUE;
    }

    private void incluiConteudoMapasValoresFinais(String valor, AtributoExtracao atributoExtracao, TipoPessoaEnum tipoPessoaEnum, Map<String, Object> mapaDadosEnvioSICLI, Map<String, Object> mapaDadosFinaisSICLI) {
        // Captura os parametros de configuração do JSON para envio ao SICLI
        String objetoSICLI = atributoExtracao.getNomeObjetoSICLI();
        String atributoSICLI = atributoExtracao.getNomeAtributoSICLI();

        if (Objects.isNull(objetoSICLI) || Objects.isNull(atributoSICLI)) {
            return;
        }

        // Remove os acentos por ventura existentes na palavra ao enviar para o SICLI
        valor = UtilString.removerAcentos(valor);
        // Formata o valor removendo tabualações, quebra de linha
        valor = valor.replaceAll("\t", " ").replaceAll("\n", " ");
        // Formata o valor removendo espaços adicionais antes e depois da palavra, transformando toda a palavra em maiusculo
        valor = valor.trim().toUpperCase();

        // Caso o atributo seja um identificador de pessoa, formata o mesmo como um CPF/CNPJ
        if (atributoExtracao.getUtilizadoIdentificadorPessoa()) {
            valor = valor.replaceAll("[^0-9]", "");
            valor = StringUtils.leftPad(valor, TipoPessoaEnum.F.equals(tipoPessoaEnum) ? 11 : 14, '0');
        }

        // Transforma o dado conforme o tipo esperado pelo SICLI
        String[] estruturaBloco = objetoSICLI.split("/");

        try {
            Object conteudo = this.converteValorAtributoDocumento(valor, atributoExtracao.getTipoAtributoSicliEnum());

            // Submete atributo a inclusão no hash map para obedecer niveis parametrizados pelo objetoSICLI
            this.incluiValorAributo(mapaDadosEnvioSICLI, mapaDadosFinaisSICLI, conteudo, atributoSICLI, estruturaBloco);
        } catch (RuntimeException | ParseException ex) {
            this.incluiValorAributo(mapaDadosEnvioSICLI, mapaDadosFinaisSICLI, valor, atributoSICLI, estruturaBloco);
            String mensagem = MessageFormat.format("CSS.aDDD.001 - Falha ao converter atributo para o SICLI. Valor = {0} | Causa = {1}", valor, ex.getLocalizedMessage());
            LOGGER.log(Level.WARNING, mensagem);
            // Incluido registro de log da exceção para não gerar apontamento do SONAR.
            LOGGER.log(Level.ALL, mensagem, ex);
        }
    }

    private Object converteValorAtributoDocumento(String valor, TipoAtributoEnum tipoAtributoSicliEnum) throws ParseException {
        switch (tipoAtributoSicliEnum) {
            case BOOLEAN:
                return Boolean.valueOf(valor);
            case DATE:
                Calendar c = this.calendarUtil.toCalendar(valor, Boolean.FALSE);
                return this.calendarUtil.toString(c, "dd/MM/yyyy");
            case DECIMAL:
                valor = valor.replaceAll("[^0-9,]", "");
                valor = valor.replaceAll(",", ".");
                return Double.valueOf(valor);
            case LONG:
                return Long.valueOf(valor);
            case STRING:
            default:
                return valor;
        }
    }

    /**
     * Inclui um objeto no HashMap que será utilizado para montagem do JSON de envio ao SICLI baseado na estrutura de niveis indicadas e no atributo defnido para
     * identificação do elemento
     *
     * @param mapaDadosSICLI Refeência do map principal uilizado para inserir o atributo
     * @param valorAtributo Valor do campo a ser encaminhado para o SICLI
     * @param nomeAtributo Nome do atributo do SICLI esperado como chave para identificação do campo a ser consumido pelo SICLI
     * @param estruturaBloco Lista de strings que definem os niveis e subniveis aos quais o atributo deve ser incluido
     */
    @SuppressWarnings("unchecked")
    private void incluiValorAributo(Map<String, Object> mapaDadosSICLI, Map<String, Object> mapaDadosFinais, Object valorAtributo, String nomeAtributo, String... estruturaBloco) {
        // Cria um apontador para a referencia principal, o qual será ajustado para apontar para os mapas filhos que representaram sub objetos
        Map<String, Object> elementoAnalisado = mapaDadosSICLI;
        if (Objects.isNull(valorAtributo) || (valorAtributo.toString().isEmpty())) {
            return;
        }

        // Percorre a lista de elementos que representam a estrutura de blocos para incluir o atributo
        for (int i = 1; i <= estruturaBloco.length; i++) {

            // Verifica se o elemento analisado com relação a estrutura do bloco trata-se de uma representação de array
            String nomeElemento = estruturaBloco[i - 1];
            int posicaoInicioArray = nomeElemento.indexOf('[');
            int posicaoFinalArray = nomeElemento.indexOf(']');
            int indiceLista = 0;
            boolean isArray = (posicaoInicioArray > 0);
            if (isArray) {
                indiceLista = Integer.valueOf(nomeElemento.substring(posicaoInicioArray + 1, posicaoFinalArray));
                nomeElemento = nomeElemento.substring(0, posicaoInicioArray);
            }

            // Verifica se já existe elemento definido como estrutura do bloco
            Object elemento = elementoAnalisado.get(nomeElemento);

            if (elemento != null) {
                // Caso já exista altera o apontador do elemento analisado para este objeto
                elementoAnalisado = (Map<String, Object>) elemento;
            } else {
                // Caso não exista cria um novo bloco e aponta o elemento analisado para esta referencia
                Map<String, Object> novoBloco = new HashMap<>();
                elementoAnalisado.put(nomeElemento, novoBloco);
                elementoAnalisado = novoBloco;
            }

            // Caso já tenha percorrido todos os elementos do bloco, alimenta o mapa de dados finais.
            if (i == estruturaBloco.length) {

                // Verifica no mapa de dados finais se o ultimo elemento definido na hierarquia da representação do objeto já esta criado.
                Object objetoPonta = mapaDadosFinais.get(nomeElemento);
                if (Objects.isNull(objetoPonta)) {
                    // Cria o objeto que presenta o ultimo elemento da estrutura da arvore JSON a ser enviado ao SICLI
                    Map<Integer, Object> mapaPonta = new HashMap<>();

                    // Adiciona um novo objeto que presenta a folha da estrutura da arvore JSON a ser enviado ao SICLI
                    Map<String, Object> novoMapaFolha = new HashMap<>();
                    novoMapaFolha.put(nomeAtributo, valorAtributo);
                    if (isArray) {
                        // Caso o elemento seja um array inclui o atributo denominado sequencial para o SICLI
                        novoMapaFolha.put(ATRIBUTO_SEQUENCIAL, 0);
                        novoMapaFolha.put(ATRIBUTO_TIPO_OPERACAO, "I");
                    }
                    // Inclui o mapa criado como elemento de ponta utilizando o indice de agruamento como chave no mapa de dados finais
                    // IMPORTANTE: Caso o elemento não seja uma representacao de array, o indice será mantido com o valor 0
                    mapaPonta.put(indiceLista, novoMapaFolha);

                    // Inclui o mapa criado para representar o ultimo elemento definido na hierarquia da JSON no mapa de dados finais.
                    mapaDadosFinais.put(nomeElemento, mapaPonta);
                } else {
                    // Converte o objeto já criado como mapa para inclusão das novas informações
                    Map<Integer, Object> mapaPonta = (Map<Integer, Object>) objetoPonta;
                    if (isArray) {
                        // Verifica se o elemento que representa o objeto agrupador no caso de listas já esta definido
                        Object objetoFolha = mapaPonta.get(indiceLista);
                        Map<String, Object> mapaFolha = new HashMap<>();
                        if (Objects.nonNull(objetoFolha)) {
                            // Caso o objeto já exista altera o apontador do mapa para o objeto já existente
                            mapaFolha = (Map<String, Object>) objetoFolha;
                        }

                        // Inclui os atributos indicando o sequencial e o tipo de operação a ser realizado.
                        mapaFolha.putIfAbsent(ATRIBUTO_SEQUENCIAL, 0);
                        mapaFolha.putIfAbsent(ATRIBUTO_TIPO_OPERACAO, "I");

                        // Inclui o novo atributo no mapa
                        mapaFolha.put(nomeAtributo, valorAtributo);

                        // Inclui ou substitui o mapa que representa o objeto folha no mapa que repsenta o elemento final da estrutura,
                        // Este elemento fica armazenado no mapa que representa os elementos finais e será utilizado na inclusão mapa que representa o corpo de envio ao SICLI
                        mapaPonta.put(indiceLista, mapaFolha);
                    } else {
                        // Caso o elemento a ser inserido não seja uma representação de array, captura o mapa representado pelo indiceLista (Que terá o valor 0)
                        // e apenas inclui o atributo com seu valor definido no mapa da folha.
                        Map<String, Object> mapaFolha = (Map<String, Object>) mapaPonta.get(indiceLista);
                        mapaFolha.put(nomeAtributo, valorAtributo);
                    }
                }
            }
        }
    }

    /**
     * Metodo utilizado em carater recursivo para substituir todos os mapas alimentados previamente no objeto que representa a estrutura de envio ao SICLI que será
     * convertida no JSON de corpo da mensagem de envio. Essa implementação permite que casos que tenham mais um registro agrupador (Ex: "enderecos") possam ser
     * incluidos na lista apenas os existentes.
     *
     * @param mapaAnalise O mapa em analise a ser percorrido as chaves e ter ser valor substituido pelos elementos definidos no mapa de dados finais.
     * @param mapaDadosFinais O mapa que contem os dados finais previamente alimentados para montagem do objeto que representa o corpo da mensagem JSON a ser
     *        enviada ao SICLI. Para os casos de elementos vinculados a chave de busca com presença de indice 0 entende-se como objeto simples. Para os demais casos
     *        entende que essa chave trata-se do agrupador do array e inclui dos os valores deste mapa dentro de uma lista para inclusão no mapa em analise.
     */
    private void substituiMapasValoresFinais(Map mapaAnalise, Map<String, Object> mapaDadosFinais) {
        // Devido as chamadas recursivas, verifica se o mapa que contem dados finais ainda possui algum valor.
        // Caso não possua mais nenhum já sai do metod retornando o fluxo para continuação desempilhada pós recursividade.
        if (mapaDadosFinais.isEmpty()) {
            return;
        }

        // IMPORTANTE: Não é possivel usar operação funcional neste ponto pois existe outro ciclo de repetição interno que causa ConcurrentModificationException
        // Percorre todas as chaves do mapa mapa em analise para verificar a existência de elementos mapeados na estrutura de dados finais.
        for (Object chave : mapaAnalise.keySet()) {
            // Verifica se o mapa que contem os dados finais possui um objeto que representa o(s) registro(s) alimentado para o mapa.
            Object registroFinal = mapaDadosFinais.remove(chave.toString());

            // Caso seja localizado um registro para a chave captura o valor para inclusão no mapa final
            if ((Objects.nonNull(registroFinal)) && (registroFinal instanceof Map)) {
                // Transforma o registro identificado num objeto de Map para manipulação dos dados
                // IMPORTANTE: Necessario manter em carater generico, pois em alguns casos o chave é uma String e em outros um Integer.
                Map registroFinalAsMap = (Map) registroFinal;
                // Varifica se o mapa definido nos dados finais refere-se a um objeto simples (Chave com valor 0)
                // ou se trata de lista objetos (identificados pela (Ausência da chave com o valor 0)
                if (registroFinalAsMap.containsKey(0)) {
                    // Retira o objeto do mapa de objetos finais convertendo para um mapa.
                    // Essa remoção permite a verificação de registros ainda existentes no inicio do metodo
                    Map mapa = (Map) registroFinalAsMap.remove(0);
                    Map mapaInclusao = (Map) mapaAnalise.get(chave);
                    // Inclui todos os elementos em carater adicional (sem substituir) no mapa existente como valor da chave do mapa em analise.
                    mapaInclusao.putAll(mapa);
                } else {
                    // Caso o mapa não possua a chave com o valor 0, inclui a lista de todos os valores mapeados em uma lista
                    // e substitui o valor definido no mapa analisado vinculado a chave corrente.
                    mapaAnalise.put(chave, new ArrayList(registroFinalAsMap.values()));
                }
            }

            // Após a substituição do valor do do objeto representado pela chave, captura o valor agora definido como final
            // e chama o metodo recurvivamente para o valor definido na rotina acima obrigando a realizar a substituição por toda a estrutura.
            Object registroAnalise = mapaAnalise.get(chave);
            // Checa se o objeto capturado trata-se de um Mapa, Lista, ou Objeto comum.
            if (registroAnalise instanceof Map) {
                // Caso seja um mapa, converte diretamente o objeto e realiza a chamada recursiva.
                Map mapa = (Map) registroAnalise;
                this.substituiMapasValoresFinais(mapa, mapaDadosFinais);
            } else if (registroAnalise instanceof List) {
                // Caso seja uma lista, converte na lista para percorrer todos os elementos chamando a substituição recursivamente.
                List lista = (List) registroAnalise;
                for (Object reg : lista) {
                    // Para cada objeto identificado na lista, converte para um mapa e realiza a chamada recursiva.
                    Map mapa = (Map) reg;
                    this.substituiMapasValoresFinais(mapa, mapaDadosFinais);
                }
            }
        }
    }
}
