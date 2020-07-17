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

import br.gov.caixa.dossie.rs.entity.PortfolioacessosEntity;
import br.gov.caixa.dossie.rs.requisicao.PortfolioacessosRequisicao;
import br.gov.caixa.dossie.rs.retorno.PortfolioacessosRetorno;
import br.gov.caixa.dossie.util.DossieConstantes;
/**
 * @author SIOGP
 */
@Stateless
@LocalBean
@Named
public class PortfolioacessosService {
	private static final Logger LOGGER = Logger.getLogger(PortfolioacessosService.class);
	/* colecao da tela Portfolioacessos*/
	private Map<Long, Object> elementosPortfolioacessos;
	/**
	 * Construtor Padrão
	 */
	public PortfolioacessosService() {
		super();
		/* As variaveis elementosXXXX armazenam em memoria uma coleção de objetos a serem exibidos em tela.
		 *	Faz-se útil em caso de tabelas	
		 */
		/* inicialização da coleção da tela Portfolioacessos*/
		elementosPortfolioacessos = new HashMap<Long, Object>();
		/* A inicialização dos elementos abaixo somente é necessario para a demonstracao da carga inicial dos elementos em tela.
		 *	Deve ser removido quando não for mais necessário
		 */
		PortfolioacessosEntity portfolioacessos = new PortfolioacessosEntity();
		elementosPortfolioacessos.put(portfolioacessos.getId(), portfolioacessos);
	}
	/**
	 * @Objetivo O metodo createPortfolioacessos é utilizado para incluir um
	 *           PortfolioacessosEntity na colecao de elementos.
	 * @param requisicao
	 * @return retorno
	 */
	public  PortfolioacessosRetorno createPortfolioacessos(PortfolioacessosRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_CREATE);
		PortfolioacessosEntity portfolioacessos = new PortfolioacessosEntity();
		portfolioacessos.setId(System.currentTimeMillis());
		elementosPortfolioacessos.put(portfolioacessos.getId(), portfolioacessos);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_INCLUSAO_SUCESSO);
		PortfolioacessosRetorno retorno = new PortfolioacessosRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(portfolioacessos);
		return retorno;
	}
	/**
	 * @Objetivo O metodo readAllPortfolioacessos é utilizado para obter todos os
	 *           {GroupId + 'Entity'} existentes na colecao de elementos.
	 * @return retorno
	 */
	public PortfolioacessosRetorno readAllPortfolioacessos() {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ_ALL);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_SUCESSO);
		PortfolioacessosRetorno retorno = new PortfolioacessosRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		for (Object portfolioacessos: elementosPortfolioacessos.values()) {
			retorno.getData().add((PortfolioacessosEntity) portfolioacessos);
		}
		return retorno;
	}
	/**
	 * @Objetivo O metodo readPortfolioacessos(Long id) é utilizado para obter um elemento
	 *           PortfolioacessosEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public PortfolioacessosRetorno readPortfolioacessos(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ + id);
		PortfolioacessosEntity portfolioacessos = (PortfolioacessosEntity) elementosPortfolioacessos.get(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_ID_SUCESSO);
		PortfolioacessosRetorno retorno = new PortfolioacessosRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(portfolioacessos);
		return retorno;
	}
	/**
	 * @Objetivo O metodo updatePortfolioacessos é utilizado para atualizar um elemento
	 *           PortfolioacessosEntity existente na colecao de elementos.
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	public PortfolioacessosRetorno updatePortfolioacessos(Long id, PortfolioacessosRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_UPDATE + id + "," + requisicao);
		PortfolioacessosEntity portfolioacessos = (PortfolioacessosEntity) elementosPortfolioacessos.get(id);
		elementosPortfolioacessos.remove(id);
		elementosPortfolioacessos.put(id, portfolioacessos);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_UPDATE_SUCESSO);
		PortfolioacessosRetorno retorno = new PortfolioacessosRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(portfolioacessos);
		return retorno;
	}
	/**
	 * @Objetivo O metodo deletePortfolioacessos é utilizado para remover um elemento
	 *           PortfolioacessosEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public PortfolioacessosRetorno deletePortfolioacessos(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_DELETE + id);
		elementosPortfolioacessos.remove(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_EXCLUSAO_SUCESSO);
		PortfolioacessosRetorno retorno = new PortfolioacessosRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		return retorno;
	}
}

