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

import br.gov.caixa.dossie.rs.entity.LoginEntity;
import br.gov.caixa.dossie.rs.requisicao.LoginRequisicao;
import br.gov.caixa.dossie.rs.retorno.LoginRetorno;
import br.gov.caixa.dossie.util.DossieConstantes;
/**
 * @author SIOGP
 */
@Stateless
@LocalBean
@Named
public class LoginService {
	private static final Logger LOGGER = Logger.getLogger(LoginService.class);
	/* colecao da tela Login*/
	private Map<Long, Object> elementosLogin;
	/**
	 * Construtor Padrão
	 */
	public LoginService() {
		super();
		/* As variaveis elementosXXXX armazenam em memoria uma coleção de objetos a serem exibidos em tela.
		 *	Faz-se útil em caso de tabelas	
		 */
		/* inicialização da coleção da tela Login*/
		elementosLogin = new HashMap<Long, Object>();
		/* A inicialização dos elementos abaixo somente é necessario para a demonstracao da carga inicial dos elementos em tela.
		 *	Deve ser removido quando não for mais necessário
		 */
		LoginEntity login = new LoginEntity();
		elementosLogin.put(login.getId(), login);
	}
	/**
	 * @Objetivo O metodo createLogin é utilizado para incluir um
	 *           LoginEntity na colecao de elementos.
	 * @param requisicao
	 * @return retorno
	 */
	public  LoginRetorno createLogin(LoginRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_CREATE);
		LoginEntity login = new LoginEntity();
		login.setId(System.currentTimeMillis());
	    login.setUsuario(requisicao.getUsuario());
	    login.setSenha(requisicao.getSenha());
	    login.setLogin(requisicao.getLogin());
	    login.setEmail(requisicao.getEmail());
	    login.setNomecompleto(requisicao.getNomecompleto());
	    login.setSenha(requisicao.getSenha());
	    login.setConfirmasenha(requisicao.getConfirmasenha());
	    login.setEmail(requisicao.getEmail());
	    login.setImagem(requisicao.getImagem());
	    login.setUsuario(requisicao.getUsuario());
	    login.setEmail(requisicao.getEmail());
	    login.setDatadecadastro(requisicao.getDatadecadastro());
	    login.setCargo(requisicao.getCargo());
	    login.setDescricao(requisicao.getDescricao());
		elementosLogin.put(login.getId(), login);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_INCLUSAO_SUCESSO);
		LoginRetorno retorno = new LoginRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(login);
		return retorno;
	}
	/**
	 * @Objetivo O metodo readAllLogin é utilizado para obter todos os
	 *           {GroupId + 'Entity'} existentes na colecao de elementos.
	 * @return retorno
	 */
	public LoginRetorno readAllLogin() {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ_ALL);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_SUCESSO);
		LoginRetorno retorno = new LoginRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		for (Object login: elementosLogin.values()) {
			retorno.getData().add((LoginEntity) login);
		}
		return retorno;
	}
	/**
	 * @Objetivo O metodo readLogin(Long id) é utilizado para obter um elemento
	 *           LoginEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public LoginRetorno readLogin(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ + id);
		LoginEntity login = (LoginEntity) elementosLogin.get(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_ID_SUCESSO);
		LoginRetorno retorno = new LoginRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(login);
		return retorno;
	}
	/**
	 * @Objetivo O metodo updateLogin é utilizado para atualizar um elemento
	 *           LoginEntity existente na colecao de elementos.
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	public LoginRetorno updateLogin(Long id, LoginRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_UPDATE + id + "," + requisicao);
		LoginEntity login = (LoginEntity) elementosLogin.get(id);
		login.setUsuario(requisicao.getUsuario());
		login.setSenha(requisicao.getSenha());
		login.setLogin(requisicao.getLogin());
		login.setEmail(requisicao.getEmail());
		login.setNomecompleto(requisicao.getNomecompleto());
		login.setSenha(requisicao.getSenha());
		login.setConfirmasenha(requisicao.getConfirmasenha());
		login.setEmail(requisicao.getEmail());
		login.setImagem(requisicao.getImagem());
		login.setUsuario(requisicao.getUsuario());
		login.setEmail(requisicao.getEmail());
		login.setDatadecadastro(requisicao.getDatadecadastro());
		login.setCargo(requisicao.getCargo());
		login.setDescricao(requisicao.getDescricao());
		elementosLogin.remove(id);
		elementosLogin.put(id, login);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_UPDATE_SUCESSO);
		LoginRetorno retorno = new LoginRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(login);
		return retorno;
	}
	/**
	 * @Objetivo O metodo deleteLogin é utilizado para remover um elemento
	 *           LoginEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public LoginRetorno deleteLogin(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_DELETE + id);
		elementosLogin.remove(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_EXCLUSAO_SUCESSO);
		LoginRetorno retorno = new LoginRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		return retorno;
	}
}

