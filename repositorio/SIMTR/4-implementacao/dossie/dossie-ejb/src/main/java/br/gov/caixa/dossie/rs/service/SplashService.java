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

import br.gov.caixa.dossie.rs.entity.SplashEntity;
import br.gov.caixa.dossie.rs.requisicao.SplashRequisicao;
import br.gov.caixa.dossie.rs.retorno.SplashRetorno;
import br.gov.caixa.dossie.util.DossieConstantes;
/**
 * @author SIOGP
 */
@Stateless
@LocalBean
@Named
public class SplashService {
	private static final Logger LOGGER = Logger.getLogger(SplashService.class);
	/* colecao da tela Splash*/
	private Map<Long, Object> elementosSplash;
	/**
	 * Construtor Padrão
	 */
	public SplashService() {
		super();
		/* As variaveis elementosXXXX armazenam em memoria uma coleção de objetos a serem exibidos em tela.
		 *	Faz-se útil em caso de tabelas	
		 */
		/* inicialização da coleção da tela Splash*/
		elementosSplash = new HashMap<Long, Object>();
		/* A inicialização dos elementos abaixo somente é necessario para a demonstracao da carga inicial dos elementos em tela.
		 *	Deve ser removido quando não for mais necessário
		 */
		SplashEntity splash = new SplashEntity();
		elementosSplash.put(splash.getId(), splash);
	}
	/**
	 * @Objetivo O metodo createSplash é utilizado para incluir um
	 *           SplashEntity na colecao de elementos.
	 * @param requisicao
	 * @return retorno
	 */
	public  SplashRetorno createSplash(SplashRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_CREATE);
		SplashEntity splash = new SplashEntity();
		splash.setId(System.currentTimeMillis());
	    splash.setBtiicon013primosiogpinvertedverysmallpng2(requisicao.getBtiicon013primosiogpinvertedverysmallpng2());
	    splash.setImgdossielogopng(requisicao.getImgdossielogopng());
	    splash.setTimer(requisicao.getTimer());
	    splash.setTimer2(requisicao.getTimer2());
	    splash.setLink(requisicao.getLink());
		elementosSplash.put(splash.getId(), splash);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_INCLUSAO_SUCESSO);
		SplashRetorno retorno = new SplashRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(splash);
		return retorno;
	}
	/**
	 * @Objetivo O metodo readAllSplash é utilizado para obter todos os
	 *           {GroupId + 'Entity'} existentes na colecao de elementos.
	 * @return retorno
	 */
	public SplashRetorno readAllSplash() {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ_ALL);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_SUCESSO);
		SplashRetorno retorno = new SplashRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		for (Object splash: elementosSplash.values()) {
			retorno.getData().add((SplashEntity) splash);
		}
		return retorno;
	}
	/**
	 * @Objetivo O metodo readSplash(Long id) é utilizado para obter um elemento
	 *           SplashEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public SplashRetorno readSplash(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ + id);
		SplashEntity splash = (SplashEntity) elementosSplash.get(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_ID_SUCESSO);
		SplashRetorno retorno = new SplashRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(splash);
		return retorno;
	}
	/**
	 * @Objetivo O metodo updateSplash é utilizado para atualizar um elemento
	 *           SplashEntity existente na colecao de elementos.
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	public SplashRetorno updateSplash(Long id, SplashRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_UPDATE + id + "," + requisicao);
		SplashEntity splash = (SplashEntity) elementosSplash.get(id);
		splash.setBtiicon013primosiogpinvertedverysmallpng2(requisicao.getBtiicon013primosiogpinvertedverysmallpng2());
		splash.setImgdossielogopng(requisicao.getImgdossielogopng());
		splash.setTimer(requisicao.getTimer());
		splash.setTimer2(requisicao.getTimer2());
		splash.setLink(requisicao.getLink());
		elementosSplash.remove(id);
		elementosSplash.put(id, splash);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_UPDATE_SUCESSO);
		SplashRetorno retorno = new SplashRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(splash);
		return retorno;
	}
	/**
	 * @Objetivo O metodo deleteSplash é utilizado para remover um elemento
	 *           SplashEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public SplashRetorno deleteSplash(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_DELETE + id);
		elementosSplash.remove(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_EXCLUSAO_SUCESSO);
		SplashRetorno retorno = new SplashRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		return retorno;
	}
}

