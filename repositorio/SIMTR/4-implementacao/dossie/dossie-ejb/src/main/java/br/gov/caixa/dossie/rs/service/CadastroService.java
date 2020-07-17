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

import br.gov.caixa.dossie.rs.entity.CadastroEntity;
import br.gov.caixa.dossie.rs.requisicao.CadastroRequisicao;
import br.gov.caixa.dossie.rs.retorno.CadastroRetorno;
import br.gov.caixa.dossie.util.DossieConstantes;
/**
 * @author SIOGP
 */
@Stateless
@LocalBean
@Named
public class CadastroService {
	private static final Logger LOGGER = Logger.getLogger(CadastroService.class);
	/* colecao da tela Cadastro*/
	private Map<Long, Object> elementosCadastro;
	/**
	 * Construtor Padrão
	 */
	public CadastroService() {
		super();
		/* As variaveis elementosXXXX armazenam em memoria uma coleção de objetos a serem exibidos em tela.
		 *	Faz-se útil em caso de tabelas	
		 */
		/* inicialização da coleção da tela Cadastro*/
		elementosCadastro = new HashMap<Long, Object>();
		/* A inicialização dos elementos abaixo somente é necessario para a demonstracao da carga inicial dos elementos em tela.
		 *	Deve ser removido quando não for mais necessário
		 */
		CadastroEntity cadastro = new CadastroEntity();
		elementosCadastro.put(cadastro.getId(), cadastro);
	}
	/**
	 * @Objetivo O metodo createCadastro é utilizado para incluir um
	 *           CadastroEntity na colecao de elementos.
	 * @param requisicao
	 * @return retorno
	 */
	public  CadastroRetorno createCadastro(CadastroRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_CREATE);
		CadastroEntity cadastro = new CadastroEntity();
		cadastro.setId(System.currentTimeMillis());
		elementosCadastro.put(cadastro.getId(), cadastro);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_INCLUSAO_SUCESSO);
		CadastroRetorno retorno = new CadastroRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(cadastro);
		return retorno;
	}
	/**
	 * @Objetivo O metodo readAllCadastro é utilizado para obter todos os
	 *           {GroupId + 'Entity'} existentes na colecao de elementos.
	 * @return retorno
	 */
	public CadastroRetorno readAllCadastro() {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ_ALL);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_SUCESSO);
		CadastroRetorno retorno = new CadastroRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		for (Object cadastro: elementosCadastro.values()) {
			retorno.getData().add((CadastroEntity) cadastro);
		}
		return retorno;
	}
	/**
	 * @Objetivo O metodo readCadastro(Long id) é utilizado para obter um elemento
	 *           CadastroEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public CadastroRetorno readCadastro(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ + id);
		CadastroEntity cadastro = (CadastroEntity) elementosCadastro.get(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_ID_SUCESSO);
		CadastroRetorno retorno = new CadastroRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(cadastro);
		return retorno;
	}
	/**
	 * @Objetivo O metodo updateCadastro é utilizado para atualizar um elemento
	 *           CadastroEntity existente na colecao de elementos.
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	public CadastroRetorno updateCadastro(Long id, CadastroRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_UPDATE + id + "," + requisicao);
		CadastroEntity cadastro = (CadastroEntity) elementosCadastro.get(id);
		elementosCadastro.remove(id);
		elementosCadastro.put(id, cadastro);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_UPDATE_SUCESSO);
		CadastroRetorno retorno = new CadastroRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(cadastro);
		return retorno;
	}
	/**
	 * @Objetivo O metodo deleteCadastro é utilizado para remover um elemento
	 *           CadastroEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public CadastroRetorno deleteCadastro(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_DELETE + id);
		elementosCadastro.remove(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_EXCLUSAO_SUCESSO);
		CadastroRetorno retorno = new CadastroRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		return retorno;
	}
}

