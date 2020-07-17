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

import br.gov.caixa.dossie.rs.entity.SobreEntity;
import br.gov.caixa.dossie.rs.requisicao.SobreRequisicao;
import br.gov.caixa.dossie.rs.retorno.SobreRetorno;
import br.gov.caixa.dossie.util.DossieConstantes;
/**
 * @author SIOGP
 */
@Stateless
@LocalBean
@Named
public class SobreService {
	private static final Logger LOGGER = Logger.getLogger(SobreService.class);
	/* colecao da tela Sobre*/
	private Map<Long, Object> elementosSobre;
	/**
	 * Construtor Padrão
	 */
	public SobreService() {
		super();
		/* As variaveis elementosXXXX armazenam em memoria uma coleção de objetos a serem exibidos em tela.
		 *	Faz-se útil em caso de tabelas	
		 */
		/* inicialização da coleção da tela Sobre*/
		elementosSobre = new HashMap<Long, Object>();
		/* A inicialização dos elementos abaixo somente é necessario para a demonstracao da carga inicial dos elementos em tela.
		 *	Deve ser removido quando não for mais necessário
		 */
		SobreEntity sobre = new SobreEntity();
		elementosSobre.put(sobre.getId(), sobre);
	}
	/**
	 * @Objetivo O metodo createSobre é utilizado para incluir um
	 *           SobreEntity na colecao de elementos.
	 * @param requisicao
	 * @return retorno
	 */
	public  SobreRetorno createSobre(SobreRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_CREATE);
		SobreEntity sobre = new SobreEntity();
		sobre.setId(System.currentTimeMillis());
	    sobre.setImgicon013primosiogpinvertedsmallpng(requisicao.getImgicon013primosiogpinvertedsmallpng());
	    sobre.setLnkprimosiogp2(requisicao.getLnkprimosiogp2());
	    sobre.setLbpexibeasituacaoatualdaassinaturadenotificaces2(requisicao.getLbpexibeasituacaoatualdaassinaturadenotificaces2());
	    sobre.setBtprecebernotificaces(requisicao.getBtprecebernotificaces());
	    sobre.setBtpreceberemail(requisicao.getBtpreceberemail());
	    sobre.setBtpcancelarnotificaces(requisicao.getBtpcancelarnotificaces());
	    sobre.setBtpcancelaremail(requisicao.getBtpcancelaremail());
	    sobre.setLink(requisicao.getLink());
	    sobre.setListagem(requisicao.getListagem());
		elementosSobre.put(sobre.getId(), sobre);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_INCLUSAO_SUCESSO);
		SobreRetorno retorno = new SobreRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(sobre);
		return retorno;
	}
	/**
	 * @Objetivo O metodo readAllSobre é utilizado para obter todos os
	 *           {GroupId + 'Entity'} existentes na colecao de elementos.
	 * @return retorno
	 */
	public SobreRetorno readAllSobre() {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ_ALL);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_SUCESSO);
		SobreRetorno retorno = new SobreRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		for (Object sobre: elementosSobre.values()) {
			retorno.getData().add((SobreEntity) sobre);
		}
		return retorno;
	}
	/**
	 * @Objetivo O metodo readSobre(Long id) é utilizado para obter um elemento
	 *           SobreEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public SobreRetorno readSobre(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ + id);
		SobreEntity sobre = (SobreEntity) elementosSobre.get(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_ID_SUCESSO);
		SobreRetorno retorno = new SobreRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(sobre);
		return retorno;
	}
	/**
	 * @Objetivo O metodo updateSobre é utilizado para atualizar um elemento
	 *           SobreEntity existente na colecao de elementos.
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	public SobreRetorno updateSobre(Long id, SobreRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_UPDATE + id + "," + requisicao);
		SobreEntity sobre = (SobreEntity) elementosSobre.get(id);
		sobre.setImgicon013primosiogpinvertedsmallpng(requisicao.getImgicon013primosiogpinvertedsmallpng());
		sobre.setLnkprimosiogp2(requisicao.getLnkprimosiogp2());
		sobre.setLbpexibeasituacaoatualdaassinaturadenotificaces2(requisicao.getLbpexibeasituacaoatualdaassinaturadenotificaces2());
		sobre.setBtprecebernotificaces(requisicao.getBtprecebernotificaces());
		sobre.setBtpreceberemail(requisicao.getBtpreceberemail());
		sobre.setBtpcancelarnotificaces(requisicao.getBtpcancelarnotificaces());
		sobre.setBtpcancelaremail(requisicao.getBtpcancelaremail());
		sobre.setLink(requisicao.getLink());
		sobre.setListagem(requisicao.getListagem());
		elementosSobre.remove(id);
		elementosSobre.put(id, sobre);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_UPDATE_SUCESSO);
		SobreRetorno retorno = new SobreRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(sobre);
		return retorno;
	}
	/**
	 * @Objetivo O metodo deleteSobre é utilizado para remover um elemento
	 *           SobreEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public SobreRetorno deleteSobre(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_DELETE + id);
		elementosSobre.remove(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_EXCLUSAO_SUCESSO);
		SobreRetorno retorno = new SobreRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		return retorno;
	}
}

