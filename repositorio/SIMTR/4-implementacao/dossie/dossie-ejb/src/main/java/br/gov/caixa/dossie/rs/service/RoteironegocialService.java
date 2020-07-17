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

import br.gov.caixa.dossie.rs.entity.RoteironegocialEntity;
import br.gov.caixa.dossie.rs.requisicao.RoteironegocialRequisicao;
import br.gov.caixa.dossie.rs.retorno.RoteironegocialRetorno;
import br.gov.caixa.dossie.util.DossieConstantes;
/**
 * @author SIOGP
 */
@Stateless
@LocalBean
@Named
public class RoteironegocialService {
	private static final Logger LOGGER = Logger.getLogger(RoteironegocialService.class);
	/* colecao da tela Roteironegocial*/
	private Map<Long, Object> elementosRoteironegocial;
	/**
	 * Construtor Padrão
	 */
	public RoteironegocialService() {
		super();
		/* As variaveis elementosXXXX armazenam em memoria uma coleção de objetos a serem exibidos em tela.
		 *	Faz-se útil em caso de tabelas	
		 */
		/* inicialização da coleção da tela Roteironegocial*/
		elementosRoteironegocial = new HashMap<Long, Object>();
		/* A inicialização dos elementos abaixo somente é necessario para a demonstracao da carga inicial dos elementos em tela.
		 *	Deve ser removido quando não for mais necessário
		 */
		RoteironegocialEntity roteironegocial = new RoteironegocialEntity();
		elementosRoteironegocial.put(roteironegocial.getId(), roteironegocial);
	}
	/**
	 * @Objetivo O metodo createRoteironegocial é utilizado para incluir um
	 *           RoteironegocialEntity na colecao de elementos.
	 * @param requisicao
	 * @return retorno
	 */
	public  RoteironegocialRetorno createRoteironegocial(RoteironegocialRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_CREATE);
		RoteironegocialEntity roteironegocial = new RoteironegocialEntity();
		roteironegocial.setId(System.currentTimeMillis());
		elementosRoteironegocial.put(roteironegocial.getId(), roteironegocial);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_INCLUSAO_SUCESSO);
		RoteironegocialRetorno retorno = new RoteironegocialRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(roteironegocial);
		return retorno;
	}
	/**
	 * @Objetivo O metodo readAllRoteironegocial é utilizado para obter todos os
	 *           {GroupId + 'Entity'} existentes na colecao de elementos.
	 * @return retorno
	 */
	public RoteironegocialRetorno readAllRoteironegocial() {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ_ALL);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_SUCESSO);
		RoteironegocialRetorno retorno = new RoteironegocialRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		for (Object roteironegocial: elementosRoteironegocial.values()) {
			retorno.getData().add((RoteironegocialEntity) roteironegocial);
		}
		return retorno;
	}
	/**
	 * @Objetivo O metodo readRoteironegocial(Long id) é utilizado para obter um elemento
	 *           RoteironegocialEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public RoteironegocialRetorno readRoteironegocial(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ + id);
		RoteironegocialEntity roteironegocial = (RoteironegocialEntity) elementosRoteironegocial.get(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_ID_SUCESSO);
		RoteironegocialRetorno retorno = new RoteironegocialRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(roteironegocial);
		return retorno;
	}
	/**
	 * @Objetivo O metodo updateRoteironegocial é utilizado para atualizar um elemento
	 *           RoteironegocialEntity existente na colecao de elementos.
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	public RoteironegocialRetorno updateRoteironegocial(Long id, RoteironegocialRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_UPDATE + id + "," + requisicao);
		RoteironegocialEntity roteironegocial = (RoteironegocialEntity) elementosRoteironegocial.get(id);
		elementosRoteironegocial.remove(id);
		elementosRoteironegocial.put(id, roteironegocial);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_UPDATE_SUCESSO);
		RoteironegocialRetorno retorno = new RoteironegocialRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(roteironegocial);
		return retorno;
	}
	/**
	 * @Objetivo O metodo deleteRoteironegocial é utilizado para remover um elemento
	 *           RoteironegocialEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public RoteironegocialRetorno deleteRoteironegocial(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_DELETE + id);
		elementosRoteironegocial.remove(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_EXCLUSAO_SUCESSO);
		RoteironegocialRetorno retorno = new RoteironegocialRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		return retorno;
	}
}

