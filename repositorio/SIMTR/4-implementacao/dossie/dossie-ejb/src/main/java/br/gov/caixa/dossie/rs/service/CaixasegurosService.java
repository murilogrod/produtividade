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

import br.gov.caixa.dossie.rs.entity.CaixasegurosEntity;
import br.gov.caixa.dossie.rs.requisicao.CaixasegurosRequisicao;
import br.gov.caixa.dossie.rs.retorno.CaixasegurosRetorno;
import br.gov.caixa.dossie.util.DossieConstantes;
/**
 * @author SIOGP
 */
@Stateless
@LocalBean
@Named
public class CaixasegurosService {
	private static final Logger LOGGER = Logger.getLogger(CaixasegurosService.class);
	/* colecao da tela Caixaseguros*/
	private Map<Long, Object> elementosCaixaseguros;
	/**
	 * Construtor Padrão
	 */
	public CaixasegurosService() {
		super();
		/* As variaveis elementosXXXX armazenam em memoria uma coleção de objetos a serem exibidos em tela.
		 *	Faz-se útil em caso de tabelas	
		 */
		/* inicialização da coleção da tela Caixaseguros*/
		elementosCaixaseguros = new HashMap<Long, Object>();
		/* A inicialização dos elementos abaixo somente é necessario para a demonstracao da carga inicial dos elementos em tela.
		 *	Deve ser removido quando não for mais necessário
		 */
		CaixasegurosEntity caixaseguros = new CaixasegurosEntity();
		elementosCaixaseguros.put(caixaseguros.getId(), caixaseguros);
	}
	/**
	 * @Objetivo O metodo createCaixaseguros é utilizado para incluir um
	 *           CaixasegurosEntity na colecao de elementos.
	 * @param requisicao
	 * @return retorno
	 */
	public  CaixasegurosRetorno createCaixaseguros(CaixasegurosRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_CREATE);
		CaixasegurosEntity caixaseguros = new CaixasegurosEntity();
		caixaseguros.setId(System.currentTimeMillis());
		elementosCaixaseguros.put(caixaseguros.getId(), caixaseguros);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_INCLUSAO_SUCESSO);
		CaixasegurosRetorno retorno = new CaixasegurosRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(caixaseguros);
		return retorno;
	}
	/**
	 * @Objetivo O metodo readAllCaixaseguros é utilizado para obter todos os
	 *           {GroupId + 'Entity'} existentes na colecao de elementos.
	 * @return retorno
	 */
	public CaixasegurosRetorno readAllCaixaseguros() {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ_ALL);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_SUCESSO);
		CaixasegurosRetorno retorno = new CaixasegurosRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		for (Object caixaseguros: elementosCaixaseguros.values()) {
			retorno.getData().add((CaixasegurosEntity) caixaseguros);
		}
		return retorno;
	}
	/**
	 * @Objetivo O metodo readCaixaseguros(Long id) é utilizado para obter um elemento
	 *           CaixasegurosEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public CaixasegurosRetorno readCaixaseguros(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ + id);
		CaixasegurosEntity caixaseguros = (CaixasegurosEntity) elementosCaixaseguros.get(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_ID_SUCESSO);
		CaixasegurosRetorno retorno = new CaixasegurosRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(caixaseguros);
		return retorno;
	}
	/**
	 * @Objetivo O metodo updateCaixaseguros é utilizado para atualizar um elemento
	 *           CaixasegurosEntity existente na colecao de elementos.
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	public CaixasegurosRetorno updateCaixaseguros(Long id, CaixasegurosRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_UPDATE + id + "," + requisicao);
		CaixasegurosEntity caixaseguros = (CaixasegurosEntity) elementosCaixaseguros.get(id);
		elementosCaixaseguros.remove(id);
		elementosCaixaseguros.put(id, caixaseguros);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_UPDATE_SUCESSO);
		CaixasegurosRetorno retorno = new CaixasegurosRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(caixaseguros);
		return retorno;
	}
	/**
	 * @Objetivo O metodo deleteCaixaseguros é utilizado para remover um elemento
	 *           CaixasegurosEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public CaixasegurosRetorno deleteCaixaseguros(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_DELETE + id);
		elementosCaixaseguros.remove(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_EXCLUSAO_SUCESSO);
		CaixasegurosRetorno retorno = new CaixasegurosRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		return retorno;
	}
}

