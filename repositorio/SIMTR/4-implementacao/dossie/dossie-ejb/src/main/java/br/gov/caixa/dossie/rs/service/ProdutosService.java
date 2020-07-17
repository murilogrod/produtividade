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

import br.gov.caixa.dossie.rs.entity.ProdutosEntity;
import br.gov.caixa.dossie.rs.requisicao.ProdutosRequisicao;
import br.gov.caixa.dossie.rs.retorno.ProdutosRetorno;
import br.gov.caixa.dossie.util.DossieConstantes;
/**
 * @author SIOGP
 */
@Stateless
@LocalBean
@Named
public class ProdutosService {
	private static final Logger LOGGER = Logger.getLogger(ProdutosService.class);
	/* colecao da tela Produtos*/
	private Map<Long, Object> elementosProdutos;
	/**
	 * Construtor Padrão
	 */
	public ProdutosService() {
		super();
		/* As variaveis elementosXXXX armazenam em memoria uma coleção de objetos a serem exibidos em tela.
		 *	Faz-se útil em caso de tabelas	
		 */
		/* inicialização da coleção da tela Produtos*/
		elementosProdutos = new HashMap<Long, Object>();
		/* A inicialização dos elementos abaixo somente é necessario para a demonstracao da carga inicial dos elementos em tela.
		 *	Deve ser removido quando não for mais necessário
		 */
		ProdutosEntity produtos = new ProdutosEntity();
		elementosProdutos.put(produtos.getId(), produtos);
	}
	/**
	 * @Objetivo O metodo createProdutos é utilizado para incluir um
	 *           ProdutosEntity na colecao de elementos.
	 * @param requisicao
	 * @return retorno
	 */
	public  ProdutosRetorno createProdutos(ProdutosRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_CREATE);
		ProdutosEntity produtos = new ProdutosEntity();
		produtos.setId(System.currentTimeMillis());
		elementosProdutos.put(produtos.getId(), produtos);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_INCLUSAO_SUCESSO);
		ProdutosRetorno retorno = new ProdutosRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(produtos);
		return retorno;
	}
	/**
	 * @Objetivo O metodo readAllProdutos é utilizado para obter todos os
	 *           {GroupId + 'Entity'} existentes na colecao de elementos.
	 * @return retorno
	 */
	public ProdutosRetorno readAllProdutos() {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ_ALL);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_SUCESSO);
		ProdutosRetorno retorno = new ProdutosRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		for (Object produtos: elementosProdutos.values()) {
			retorno.getData().add((ProdutosEntity) produtos);
		}
		return retorno;
	}
	/**
	 * @Objetivo O metodo readProdutos(Long id) é utilizado para obter um elemento
	 *           ProdutosEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public ProdutosRetorno readProdutos(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ + id);
		ProdutosEntity produtos = (ProdutosEntity) elementosProdutos.get(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_ID_SUCESSO);
		ProdutosRetorno retorno = new ProdutosRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(produtos);
		return retorno;
	}
	/**
	 * @Objetivo O metodo updateProdutos é utilizado para atualizar um elemento
	 *           ProdutosEntity existente na colecao de elementos.
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	public ProdutosRetorno updateProdutos(Long id, ProdutosRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_UPDATE + id + "," + requisicao);
		ProdutosEntity produtos = (ProdutosEntity) elementosProdutos.get(id);
		elementosProdutos.remove(id);
		elementosProdutos.put(id, produtos);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_UPDATE_SUCESSO);
		ProdutosRetorno retorno = new ProdutosRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(produtos);
		return retorno;
	}
	/**
	 * @Objetivo O metodo deleteProdutos é utilizado para remover um elemento
	 *           ProdutosEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public ProdutosRetorno deleteProdutos(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_DELETE + id);
		elementosProdutos.remove(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_EXCLUSAO_SUCESSO);
		ProdutosRetorno retorno = new ProdutosRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		return retorno;
	}
}

