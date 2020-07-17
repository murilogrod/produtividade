	package br.gov.caixa.dossie.rs.service;
/*
	Nesta classe é definido o EJB do seu projeto. 	
	Para este template cada tela possui seu proprio EJB
*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

import org.apache.log4j.Logger;

import br.gov.caixa.dossie.rs.entity.PrincipalEntity;
import br.gov.caixa.dossie.rs.requisicao.PrincipalRequisicao;
import br.gov.caixa.dossie.rs.retorno.PrincipalRetorno;
import br.gov.caixa.dossie.util.DossieConstantes;
/**
 * @author SIOGP
 */
@Stateless
@LocalBean
@Named
public class PrincipalService {
	private static final Logger LOGGER = Logger.getLogger(PrincipalService.class);
	/* colecao da tela Principal*/
	private Map<Long, Object> elementosPrincipal;
	/**
	 * Construtor Padrão
	 */
	public PrincipalService() {
		super();
		/* As variaveis elementosXXXX armazenam em memoria uma coleção de objetos a serem exibidos em tela.
		 *	Faz-se útil em caso de tabelas	
		 */
		/* inicialização da coleção da tela Principal*/
		elementosPrincipal = new HashMap<Long, Object>();
		/* A inicialização dos elementos abaixo somente é necessario para a demonstracao da carga inicial dos elementos em tela.
		 *	Deve ser removido quando não for mais necessário
		 */
		PrincipalEntity principal = new PrincipalEntity();
		elementosPrincipal.put(principal.getId(), principal);
	}
	/**
	 * @Objetivo O metodo createPrincipal é utilizado para incluir um
	 *           PrincipalEntity na colecao de elementos.
	 * @param requisicao
	 * @return retorno
	 */
	public  PrincipalRetorno createPrincipal(PrincipalRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_CREATE);
		PrincipalEntity principal = new PrincipalEntity();
		principal.setId(System.currentTimeMillis());
	    principal.setImgdossielogopng(requisicao.getImgdossielogopng());
	    principal.setNumcpf2(requisicao.getNumcpf2());
		elementosPrincipal.put(principal.getId(), principal);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_INCLUSAO_SUCESSO);
		PrincipalRetorno retorno = new PrincipalRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(principal);
		return retorno;
	}
	/**
	 * @Objetivo O metodo readAllPrincipal é utilizado para obter todos os
	 *           {GroupId + 'Entity'} existentes na colecao de elementos.
	 * @return retorno
	 */
	public PrincipalRetorno readAllPrincipal() {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ_ALL);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_SUCESSO);
		PrincipalRetorno retorno = new PrincipalRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		for (Object principal: elementosPrincipal.values()) {
			retorno.getData().add((PrincipalEntity) principal);
		}
		return retorno;
	}
	/**
	 * @Objetivo O metodo readPrincipal(Long id) é utilizado para obter um elemento
	 *           PrincipalEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public PrincipalRetorno readPrincipal(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ + id);
		PrincipalEntity principal = (PrincipalEntity) elementosPrincipal.get(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_ID_SUCESSO);
		PrincipalRetorno retorno = new PrincipalRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(principal);
		return retorno;
	}
	/**
	 * @Objetivo O metodo updatePrincipal é utilizado para atualizar um elemento
	 *           PrincipalEntity existente na colecao de elementos.
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	public PrincipalRetorno updatePrincipal(Long id, PrincipalRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_UPDATE + id + "," + requisicao);
		PrincipalEntity principal = (PrincipalEntity) elementosPrincipal.get(id);
		principal.setImgdossielogopng(requisicao.getImgdossielogopng());
		principal.setNumcpf2(requisicao.getNumcpf2());
		elementosPrincipal.remove(id);
		elementosPrincipal.put(id, principal);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_UPDATE_SUCESSO);
		PrincipalRetorno retorno = new PrincipalRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(principal);
		return retorno;
	}
	/**
	 * @Objetivo O metodo deletePrincipal é utilizado para remover um elemento
	 *           PrincipalEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public PrincipalRetorno deletePrincipal(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_DELETE + id);
		elementosPrincipal.remove(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_EXCLUSAO_SUCESSO);
		PrincipalRetorno retorno = new PrincipalRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		return retorno;
	}
}

