/**
 * Copyright (c) 2020 Caixa Econômica Federal. Todos os direitos
 * reservados.
 *
 * Caixa Econômica Federal - simtr-api - Sistema de Crédito Rural
 *
 * Este software foi desenvolvido sob demanda da CAIXA e está
 * protegido por leis de direitos autorais e tratados internacionais. As
 * condições de cópia e utilização do total ou partes dependem de autorização da
 * empresa. Cópias não são permitidas sem expressa autorização. Não pode ser
 * comercializado ou utilizado para propósitos particulares.
 *
 * Uso exclusivo da Caixa Econômica Federal. A reprodução ou distribuição não
 * autorizada deste programa ou de parte dele, resultará em punições civis e
 * criminais e os infratores incorrem em sanções previstas na legislação em
 * vigor.
 *
 * Histórico do TFS:
 *
 * LastChangedRevision: $Revision$
 * LastChangedBy: $Author$
 * LastChangedDate: $Date$
 *
 * HeadURL: $HeadURL$
 *
 */
package br.gov.caixa.simtr.controle.servico;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.controle.excecao.SimtrAtributoIntegracaoException;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.AtributoIntegracao;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieClienteProduto;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.ObjetoIntegracao;
import br.gov.caixa.simtr.modelo.entidade.OpcaoCampo;
import br.gov.caixa.simtr.modelo.entidade.PriorizacaoObjetoIntegracao;
import br.gov.caixa.simtr.modelo.entidade.RespostaDossie;
import br.gov.caixa.simtr.modelo.enumerator.SICLITipoFonteEnum;
import br.gov.caixa.simtr.modelo.enumerator.SistemaIntegracaoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TemporalidadeDocumentoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.util.ConstantesUtil;

/**
 * <p>
 * IntegracaoSistemaServicoInsercao
 * </p>
 *
 * <p>
 * Descrição: Descrição do tipo
 * </p>
 *
 * <br>
 * <b>Empresa:</b> Cef - Caixa Econômica Federal
 *
 *
 * @author f725905
 *
 * @version 1.0
 */
@Stateless
@RolesAllowed({ ConstantesUtil.PERFIL_MTRADM, ConstantesUtil.PERFIL_MTRAUD, ConstantesUtil.PERFIL_MTRTEC, ConstantesUtil.PERFIL_MTRDOSINT,
	ConstantesUtil.PERFIL_MTRDOSMTZ, ConstantesUtil.PERFIL_MTRDOSOPE })
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class IntegracaoSistemasServicoInsercao {

    private static final String REGEX_ARRAY = "\\[\\]";

    private static final String ERRO_LISTA_TAMANHO_DIFFERENTE = "Não encontrado valor para o atributo {0} dentro da lista {1}. A lista tem tamanho {2} e deveria ter {3} ";

    private static final String[] OBJ_SEM_TP_OPERACAO = { "dadosBasicos", "pessoaJuridica", "pessoaFisica" };

    private static final TipoCampoEnum[] CAMPOS_RESPOSTA_NAO_ABERTA = { TipoCampoEnum.SELECT, TipoCampoEnum.RADIO, TipoCampoEnum.CHECKBOX };

    @Inject
    private EntityManager entityManager;

    @Inject
    private CalendarUtil calendarUtil;

    public Map<String, Object> montaObjetoIntegracaoParaInserir(final DossieProduto dossieProduto, final DossieClienteProduto dossieClientePrincipal,
	    final Integer classe, SistemaIntegracaoEnum sistemaIntegracaoEnum) {
	if (dossieClientePrincipal == null) {
	    throw new SimtrAtributoIntegracaoException("Dossie cliente principal não encontrado para dossie produto: " + dossieProduto.getId());
	}
	if (sistemaIntegracaoEnum == null) {
	    throw new SimtrAtributoIntegracaoException("Enum de sistema de integração vazio");
	}
	List<InstanciaDocumento> instanciasDocumentoDossieClientePrincipal = new ArrayList<>();
	dossieProduto.getInstanciasDocumento().forEach(instanciaDocumento -> {
	    if ((instanciaDocumento.getDossieClienteProduto() != null)
		    && (instanciaDocumento.getDossieClienteProduto().equals(dossieClientePrincipal))) {
		instanciasDocumentoDossieClientePrincipal.add(instanciaDocumento);
	    }
	});
	List<Documento> listDocumentos = instanciasDocumentoDossieClientePrincipal.stream().map(instanciaDoc -> instanciaDoc.getDocumento())
		.filter(doc -> !doc.getAtributosDocumento().isEmpty())
		.filter(doc -> TemporalidadeDocumentoEnum.TEMPORARIO_ANTIFRAUDE.equals(doc.getSituacaoTemporalidade())
			|| TemporalidadeDocumentoEnum.VALIDO.equals(doc.getSituacaoTemporalidade()))
		.collect(Collectors.toList());
	final Map<String, Object> mapaSicli = new HashMap<>();
	if (SistemaIntegracaoEnum.SICLI.equals(sistemaIntegracaoEnum)) {
	    mapaSicli.put("classe", classe.toString());
	}
	List<ObjetoIntegracao> objetoIntegracaos = this.consultaObjetoIntegracaoPeloProcesso(dossieProduto.getProcesso().getId(),
		sistemaIntegracaoEnum);
	objetoIntegracaos.sort((first, second) -> {
	    String firstP = first.getObjetoIntegracao();
	    String secondP = second.getObjetoIntegracao();
	    if (firstP == null && secondP == null) {
		return 0;
	    }
	    if (firstP != null && secondP == null) {
		return 1;
	    }
	    if (firstP == null && secondP != null) {
		return -1;
	    }
	    return firstP.compareTo(secondP);
	});
	for (ObjetoIntegracao objetoIntegracao : objetoIntegracaos) {
	    this.pesquisaAtributosIntegracaoPelaListaPriorizacao(objetoIntegracao, dossieProduto, objetoIntegracao.getObjetoIntegracao(), mapaSicli,
		    sistemaIntegracaoEnum, listDocumentos);
	}

	return mapaSicli;

    }

    @SuppressWarnings("unchecked")
    private void verificaECriaNiveis(Map<String, Object> resultado, String[] caminhos, boolean raiz, SistemaIntegracaoEnum sistemaIntegracaoEnum) {
	if (caminhos == null) {
	    return;
	}
	int i = 0;
	for (final String item : caminhos) {
	    i++;
	    if (raiz && i == caminhos.length) {
		break;
	    }
	    final String nomeItem = item.replaceAll(REGEX_ARRAY, "");
	    Object obj = resultado.get(nomeItem);
	    if (obj == null) {
		obj = this.verificarECriaTpOperacao(item, sistemaIntegracaoEnum);
		resultado.put(nomeItem, obj);
		if (!this.isArray(item)) {
		    this.verificaECriaNiveis((Map<String, Object>) resultado.get(nomeItem), Arrays.copyOfRange(caminhos, i, caminhos.length), false,
			    sistemaIntegracaoEnum);
		}
	    }
	}
    }

    private boolean isCampoOpcao(final TipoCampoEnum tipoCampoEnum) {
	for (TipoCampoEnum item : CAMPOS_RESPOSTA_NAO_ABERTA) {
	    if (item.equals(tipoCampoEnum)) {
		return true;
	    }
	}
	return false;
    }

    private boolean podeInserirTpOperacao(final String campo) {
	for (final String item : OBJ_SEM_TP_OPERACAO) {
	    if (item.equals(campo)) {
		return false;
	    }
	}
	return true;
    }

    @SuppressWarnings("unchecked")
    private Object verificarECriaTpOperacao(final String item, SistemaIntegracaoEnum sistemaIntegracaoEnum) {
	Object obj = this.isArray(item) ? new ArrayList<Map<String, Object>>() : new HashMap<String, Object>();
	if (obj instanceof Map) {
	    this.adicionaValorPadrao(((Map<String, Object>) obj), sistemaIntegracaoEnum, item);
	}
	return obj;
    }

    @SuppressWarnings("unchecked")
    private Object recuperaMultinivel(final Map<String, Object> result, final String[] arrayCaminho, final String campo) {

	int i = 0;
	for (String item : arrayCaminho) {
	    i++;
	    final String nomeItem = item.replaceAll(REGEX_ARRAY, "");
	    Object obj = result.get(nomeItem);
	    if (obj instanceof List) {
		return obj;
	    }
	    if (nomeItem.equals(campo)) {
		return obj;
	    }
	    return recuperaMultinivel((Map<String, Object>) obj, Arrays.copyOfRange(arrayCaminho, i, arrayCaminho.length), campo);
	}

	return result;
    }

    @SuppressWarnings("unchecked")
    private void pesquisaAtributosIntegracaoPelaListaPriorizacao(final ObjetoIntegracao objetoIntegracao, DossieProduto dossieProduto,
	    final String caminho, final Map<String, Object> result, SistemaIntegracaoEnum sistemaIntegracaoEnum, List<Documento> listDocumentos) {

	Integer tamanhoLista = null;
	final String[] arrayCaminho = caminho == null ? null : caminho.split("/");
	boolean multinivel = false;
	boolean multilista = false;
	if (arrayCaminho != null && arrayCaminho.length > 1) {
	    multinivel = true;
	    Object recuperaMultinivel = this.recuperaMultinivel(result, arrayCaminho,
		    arrayCaminho[arrayCaminho.length - 2].replaceAll(REGEX_ARRAY, ""));
	    if (recuperaMultinivel == null) {
		this.verificaECriaNiveis(result, arrayCaminho, true, sistemaIntegracaoEnum);
	    } else {
		final String item = arrayCaminho[arrayCaminho.length - 1];
		if (recuperaMultinivel instanceof List && this.isArray(item)) {
		    multilista = true;
		} else {
		    Map<String, Object> mapInterno = (Map<String, Object>) recuperaMultinivel;
		    mapInterno.put(item.replaceAll(REGEX_ARRAY, ""), this.verificarECriaTpOperacao(item, sistemaIntegracaoEnum));
		}
	    }
	}
	final String campo = arrayCaminho == null ? null : arrayCaminho[arrayCaminho.length - 1];
	for (final AtributoIntegracao atributoIntegracao : objetoIntegracao.getAtributosIntegracao()) {
	    Object valor = null;
	    for (final PriorizacaoObjetoIntegracao priorizacaoObjetoIntegracao : objetoIntegracao.getPriorizacaoObjetoIntegracaos()) {
		if (SICLITipoFonteEnum.D.equals(priorizacaoObjetoIntegracao.getFonte())) {
		    valor = this.pesquisaPorDocumento(priorizacaoObjetoIntegracao, atributoIntegracao, campo, listDocumentos);
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
		    String nomeCampoEntrada = campo.replaceAll(REGEX_ARRAY, "");
		    List<AtributoDocumento> listaAD = (List<AtributoDocumento>) valor;

		    // Guarda o tamanho da lista porque se vier algo diferente
		    // nos próximo é erro
		    if (tamanhoLista == null) {
			tamanhoLista = listaAD.size();
		    } else if (!tamanhoLista.equals(listaAD.size()) && atributoIntegracao.getObrigatorio()) {
			throw new SimtrAtributoIntegracaoException(MessageFormat.format(ERRO_LISTA_TAMANHO_DIFFERENTE,
				atributoIntegracao.getAtributo(), nomeCampoEntrada, listaAD.size(), tamanhoLista));
		    }

		    Object list = null;
		    if (multinivel) {
			list = this.recuperaMultinivel(result, arrayCaminho, nomeCampoEntrada);
		    } else {
			list = result.get(nomeCampoEntrada);
		    }
		    List<Map<String, Object>> listConvertida = null;
		    if (list == null) {
			listConvertida = new ArrayList<>();
			for (int i = 0; i < tamanhoLista; i++) {
			    Map<String, Object> objMap = new HashMap<>();
			    this.adicionaValorPadrao(objMap, sistemaIntegracaoEnum, nomeCampoEntrada);
			    listConvertida.add(objMap);
			}
			result.put(nomeCampoEntrada, listConvertida);
		    } else {
			if (list instanceof Map) {
			    Map<String, Object> mapInterno = (Map<String, Object>) list;
			    Object objNomeCampoEntrada = mapInterno.get(nomeCampoEntrada);
			    listConvertida = (List<Map<String, Object>>) objNomeCampoEntrada;
			    this.trataListaRecebida(listConvertida, listaAD, atributoIntegracao, nomeCampoEntrada, sistemaIntegracaoEnum, multilista);
			} else {
			    listConvertida = (List<Map<String, Object>>) list;
			    this.trataListaRecebida(listConvertida, listaAD, atributoIntegracao, nomeCampoEntrada, sistemaIntegracaoEnum, multilista);
			}
			if (multilista) {
			    Object listInterna = listConvertida.get(0).get(nomeCampoEntrada);
			    // Quer dizer que a lista não tem a outra lista
			    // dentro dela
			    if (listInterna == null) {
				this.criaObjetosNaLista(listConvertida, campo, sistemaIntegracaoEnum);
			    }
			    for (int i = 0; i < listaAD.size(); i++) {
				for (int j = 0; j < listConvertida.size(); j++) {
				    Object objInterno = listConvertida.get(j).get(nomeCampoEntrada);
				    if (objInterno instanceof Map) {
					Map<String, Object> mapInterno = (Map<String, Object>) objInterno;
					mapInterno.put(listaAD.get(i).getDescricao(),
						this.validaEConverteValor(atributoIntegracao, listaAD.get(i).getConteudo()));
				    } else {
					List<Map<String, Object>> listMapInterno = (List<Map<String, Object>>) objInterno;
					this.trataListaRecebida(listMapInterno, listaAD, atributoIntegracao, nomeCampoEntrada, sistemaIntegracaoEnum,
						multilista);
					if (listMapInterno.size() != listaAD.size()) {
					    throw new SimtrAtributoIntegracaoException(MessageFormat.format(ERRO_LISTA_TAMANHO_DIFFERENTE,
						    atributoIntegracao.getAtributo(), nomeCampoEntrada, listaAD.size(), listMapInterno.size()));
					}
					for (int x = 0; x < listMapInterno.size(); x++) {
					    Map<String, Object> mapInterno = listMapInterno.get(x);
					    mapInterno.put(listaAD.get(x).getDescricao(),
						    this.validaEConverteValor(atributoIntegracao, listaAD.get(x).getConteudo()));
					}
				    }
				}
			    }
			} else {
			    for (int i = 0; i < listaAD.size(); i++) {
				Map<String, Object> mapInterno = listConvertida.get(i);
				mapInterno.put(listaAD.get(i).getDescricao(),
					this.validaEConverteValor(atributoIntegracao, listaAD.get(i).getConteudo()));
			    }
			}
		    }

		} else {
		    Object list = null;
		    if (multinivel) {
			list = this.recuperaMultinivel(result, arrayCaminho, campo);
			if (list instanceof List) {
			    List<Map<String,Object>> listConvertida = (List<Map<String,Object>>) list;
			    Map<String, Object> mapInterno = null;
			    if (listConvertida.isEmpty()) {
				mapInterno = new HashMap<>();
				listConvertida.add(mapInterno);
				this.insereValorNaoLista(atributoIntegracao, null, valor, mapInterno, sistemaIntegracaoEnum);
			    } else {
				for (final Map<String, Object> item : listConvertida) {
				    this.insereValorNaoLista(atributoIntegracao, null, valor, item, sistemaIntegracaoEnum);
				}
			    }
			} else {
			    this.insereValorNaoLista(atributoIntegracao, null, valor, (Map<String, Object>) list, sistemaIntegracaoEnum);
			}
		    } else {
			this.insereValorNaoLista(atributoIntegracao, campo, this.validaEConverteValor(atributoIntegracao, valor), result,
				sistemaIntegracaoEnum);
		    }
		}
	    }
	}
    }

    private void criaObjetosNaLista(final List<Map<String, Object>> lista, final String campo, SistemaIntegracaoEnum sistemaIntegracaoEnum) {
	if (lista != null) {
	    lista.forEach(item -> {
		final String chave = campo.replaceAll(REGEX_ARRAY, "");
		item.put(chave, this.verificarECriaTpOperacao(campo, sistemaIntegracaoEnum));
	    });
	}
    }

    private void trataListaRecebida(List<Map<String, Object>> listConvertida, final List<AtributoDocumento> listaAD,
	    final AtributoIntegracao atributoIntegracao, final String nomeCampoEntrada, SistemaIntegracaoEnum sistemaIntegracaoEnum,
	    boolean multilista) {
	if (listConvertida.isEmpty()) {
	    for (int i = 0; i < listaAD.size(); i++) {
		Map<String, Object> mapInterno = new HashMap<>();
		this.adicionaValorPadrao(mapInterno, sistemaIntegracaoEnum, nomeCampoEntrada);
		listConvertida.add(mapInterno);
	    }
	} else if (!multilista && listConvertida.size() != listaAD.size()) {
	    throw new SimtrAtributoIntegracaoException(MessageFormat.format(ERRO_LISTA_TAMANHO_DIFFERENTE, atributoIntegracao.getAtributo(),
		    nomeCampoEntrada, listaAD.size(), listConvertida.size()));
	}
    }

    public Object validaEConverteValor(final AtributoIntegracao atributoIntegracao, Object valor) {

	final TipoAtributoEnum tipoAtributoEnum;
	try {
	    tipoAtributoEnum = TipoAtributoEnum.getByNome(atributoIntegracao.getTipoAtributo());
	} catch (IllegalArgumentException e) {
	    throw new SimtrAtributoIntegracaoException("Tipo de atributo inválido: " + atributoIntegracao.getTipoAtributo());
	}
	if (TipoAtributoEnum.LONG.equals(tipoAtributoEnum)) {
	    try {
		return Long.parseLong(String.valueOf(valor));
	    } catch (NumberFormatException e) {
		throw new SimtrAtributoIntegracaoException(MessageFormat.format("Erro ao converter valor para Long - Valor {0}", valor));
	    }
	} else if (TipoAtributoEnum.BOOLEAN.equals(tipoAtributoEnum)) {
	    if (!"true".equals(valor) && !"false".equals(valor)) {
		throw new SimtrAtributoIntegracaoException(MessageFormat.format("Valor para boolean não reconhecido: Valor {0}", valor));
	    }
	    return Boolean.parseBoolean(String.valueOf(valor));
	} else if (TipoAtributoEnum.DATE.equals(tipoAtributoEnum)) {
	    try {
		Calendar calendar = this.calendarUtil.toCalendar(String.valueOf(valor), true);
		return this.calendarUtil.toString(calendar, "dd/MM/yyyy");
	    } catch (ParseException e) {
		throw new SimtrAtributoIntegracaoException(MessageFormat.format("Erro ao converter o valor em data: Valor {0}", valor));
	    }
	} else if (TipoAtributoEnum.STRING.equals(tipoAtributoEnum)) {
	    return String.valueOf(valor);
	} else if (TipoAtributoEnum.DECIMAL.equals(tipoAtributoEnum)) {
	    try {
		Double doubleValue = Double.parseDouble(String.valueOf(valor));
		return BigDecimal.valueOf(doubleValue);
	    } catch (NumberFormatException | NullPointerException e) {
		throw new SimtrAtributoIntegracaoException(MessageFormat.format("Erro ao converter o valor em Decimal: Valor {0}", valor));
	    }
	}

	return null;
    }

    private void adicionaValorPadrao(final Map<String, Object> resultado, final SistemaIntegracaoEnum sistemaIntegracaoEnum,
	    final String campoAdicionado) {
	if (SistemaIntegracaoEnum.SICLI.equals(sistemaIntegracaoEnum) && this.podeInserirTpOperacao(campoAdicionado)) {
	    resultado.put("tpOperacao", "I");
	}
    }

    @SuppressWarnings("unchecked")
    private void insereValorNaoLista(final AtributoIntegracao atributoIntegracao, final String campo, Object valor, final Map<String, Object> result,
	    SistemaIntegracaoEnum sistemaIntegracaoEnum) {
	if (campo == null) {
	    result.put(atributoIntegracao.getAtributo(), this.validaEConverteValor(atributoIntegracao, valor));
	} else {
	    Object object = result.get(campo);
	    if (object == null) {
		object = new HashMap<String, Object>();
		result.put(campo, object);
		Map<String, Object> objMap = (Map<String, Object>) object;
		this.adicionaValorPadrao(objMap, sistemaIntegracaoEnum, campo);
	    }
	    Map<String, Object> objMap = (Map<String, Object>) object;
	    objMap.put(atributoIntegracao.getAtributo(), this.validaEConverteValor(atributoIntegracao, valor));
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

    private Object pesquisaPorDocumento(PriorizacaoObjetoIntegracao priorizacaoObjetoIntegracao, final AtributoIntegracao atributoIntegracao,
	    final String campo, List<Documento> listDocumentos) {

	Documento documentoLocalizado = null;
	Optional<Documento> documento = listDocumentos.stream()
		.filter(doc -> priorizacaoObjetoIntegracao.getTipoDocumento().equals(doc.getTipoDocumento()))
		.max(Comparator.comparing(Documento::getDataHoraCaptura));

	if (documento.isPresent()) {
	    documentoLocalizado = documento.get();
	    Optional<AtributoExtracao> atributoExtracao = atributoIntegracao.getAtributoExtracoes().stream()
		    .filter(ae -> ae.getTipoDocumento().getId().equals(priorizacaoObjetoIntegracao.getTipoDocumento().getId())).findFirst();
	    if (!atributoExtracao.isPresent()) {
		if (!atributoIntegracao.getObrigatorio()) {
		    return null;
		}
		throw new SimtrAtributoIntegracaoException(
			MessageFormat.format("Sem atributo extração parametrizado para tipo de documento {0} e atributo {1}",
				priorizacaoObjetoIntegracao.getTipoDocumento().getNome(), atributoIntegracao.getAtributo()));
	    }

	    String noArrayDocumento = atributoExtracao.get().getObjetoDocumento();
	    Stream<AtributoDocumento> streamAtributoDocumento = null;
	    streamAtributoDocumento = documentoLocalizado.getAtributosDocumento().stream()
		    .filter(atributoDocumento -> atributoDocumento.getDescricao().equals(atributoExtracao.get().getNomeAtributoDocumento()));
	    if (this.isArray(campo) || noArrayDocumento != null) {
		Stream<AtributoDocumento> streamADFilter = streamAtributoDocumento
			.filter(at -> at.getDeObjeto().replaceAll("[0-9]", "").contains(noArrayDocumento));
		if (this.isArray(campo)) {
		    return streamADFilter.sorted((first, second) -> {
			String firstP = first.getDeObjeto().replaceAll("[^0-9]", "");
			String secondP = second.getDeObjeto().replaceAll("[^0-9]", "");
			return Integer.compare(Integer.parseInt(firstP), Integer.parseInt(secondP));
		    }).collect(Collectors.toList());
		}
		streamAtributoDocumento = streamADFilter;
	    }
	    Optional<AtributoDocumento> opcao = streamAtributoDocumento.findFirst();
	    if (opcao.isPresent()) {
		return opcao.get().getConteudo();
	    }
	}

	return null;
    }

    /**
     * 
     * @param idProcessoDossie
     * @return
     */
    private List<ObjetoIntegracao> consultaObjetoIntegracaoPeloProcesso(Integer idProcessoDossie, SistemaIntegracaoEnum sistemaIntegracaoEnum) {
	StringBuilder jpql = new StringBuilder();
	jpql.append(" SELECT DISTINCT OI FROM ObjetoIntegracao OI ");
	jpql.append(" LEFT JOIN FETCH OI.atributosIntegracao AI ");
	jpql.append(" LEFT JOIN FETCH AI.atributoExtracoes AE LEFT JOIN FETCH AE.tipoDocumento TP");
	jpql.append(" LEFT JOIN FETCH OI.priorizacaoObjetoIntegracaos POI LEFT JOIN FETCH POI.tipoDocumento ");
	jpql.append(" WHERE POI.processoDossie.id = :idProcessoDossie AND OI.sistemaIntegracao = :sistemaIntegracao");
	jpql.append(" AND AI.obrigatorio = true ");
	jpql.append(" ORDER BY OI.objetoIntegracao ASC , POI.ordemPrioridade ASC ");

	TypedQuery<ObjetoIntegracao> queryListPriorizacaoObjetoIntegracao = this.entityManager.createQuery(jpql.toString(), ObjetoIntegracao.class);
	queryListPriorizacaoObjetoIntegracao.setParameter("idProcessoDossie", idProcessoDossie);
	queryListPriorizacaoObjetoIntegracao.setParameter("sistemaIntegracao", sistemaIntegracaoEnum);

	return queryListPriorizacaoObjetoIntegracao.getResultList();

    }

    private boolean isArray(final String campo) {
	return campo != null && campo.contains("[]");
    }
}
