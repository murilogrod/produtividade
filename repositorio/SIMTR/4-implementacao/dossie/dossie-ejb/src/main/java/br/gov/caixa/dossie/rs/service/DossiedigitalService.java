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

import br.gov.caixa.dossie.rs.entity.DossiedigitalEntity;
import br.gov.caixa.dossie.rs.requisicao.DossiedigitalRequisicao;
import br.gov.caixa.dossie.rs.retorno.DossiedigitalRetorno;
import br.gov.caixa.dossie.util.DossieConstantes;
/**
 * @author SIOGP
 */
@Stateless
@LocalBean
@Named
public class DossiedigitalService {
	private static final Logger LOGGER = Logger.getLogger(DossiedigitalService.class);
	/* colecao da tela Dossiedigital*/
	private Map<Long, Object> elementosDossiedigital;
	/**
	 * Construtor Padrão
	 */
	public DossiedigitalService() {
		super();
		/* As variaveis elementosXXXX armazenam em memoria uma coleção de objetos a serem exibidos em tela.
		 *	Faz-se útil em caso de tabelas	
		 */
		/* inicialização da coleção da tela Dossiedigital*/
		elementosDossiedigital = new HashMap<Long, Object>();
		/* A inicialização dos elementos abaixo somente é necessario para a demonstracao da carga inicial dos elementos em tela.
		 *	Deve ser removido quando não for mais necessário
		 */
		DossiedigitalEntity dossiedigital = new DossiedigitalEntity();
		elementosDossiedigital.put(dossiedigital.getId(), dossiedigital);
	}
	/**
	 * @Objetivo O metodo createDossiedigital é utilizado para incluir um
	 *           DossiedigitalEntity na colecao de elementos.
	 * @param requisicao
	 * @return retorno
	 */
	public  DossiedigitalRetorno createDossiedigital(DossiedigitalRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_CREATE);
		DossiedigitalEntity dossiedigital = new DossiedigitalEntity();
		dossiedigital.setId(System.currentTimeMillis());
		elementosDossiedigital.put(dossiedigital.getId(), dossiedigital);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_INCLUSAO_SUCESSO);
		DossiedigitalRetorno retorno = new DossiedigitalRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(dossiedigital);
		return retorno;
	}
	/**
	 * @Objetivo O metodo readAllDossiedigital é utilizado para obter todos os
	 *           {GroupId + 'Entity'} existentes na colecao de elementos.
	 * @return retorno
	 */
	public DossiedigitalRetorno readAllDossiedigital() {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ_ALL);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_SUCESSO);
		DossiedigitalRetorno retorno = new DossiedigitalRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		for (Object dossiedigital: elementosDossiedigital.values()) {
			retorno.getData().add((DossiedigitalEntity) dossiedigital);
		}
		return retorno;
	}
	/**
	 * @Objetivo O metodo readDossiedigital(Long id) é utilizado para obter um elemento
	 *           DossiedigitalEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public DossiedigitalRetorno readDossiedigital(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ + id);
		DossiedigitalEntity dossiedigital = (DossiedigitalEntity) elementosDossiedigital.get(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_ID_SUCESSO);
		DossiedigitalRetorno retorno = new DossiedigitalRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(dossiedigital);
		return retorno;
	}
	/**
	 * @Objetivo O metodo updateDossiedigital é utilizado para atualizar um elemento
	 *           DossiedigitalEntity existente na colecao de elementos.
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	public DossiedigitalRetorno updateDossiedigital(Long id, DossiedigitalRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_UPDATE + id + "," + requisicao);
		DossiedigitalEntity dossiedigital = (DossiedigitalEntity) elementosDossiedigital.get(id);
		elementosDossiedigital.remove(id);
		elementosDossiedigital.put(id, dossiedigital);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_UPDATE_SUCESSO);
		DossiedigitalRetorno retorno = new DossiedigitalRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(dossiedigital);
		return retorno;
	}
	/**
	 * @Objetivo O metodo deleteDossiedigital é utilizado para remover um elemento
	 *           DossiedigitalEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public DossiedigitalRetorno deleteDossiedigital(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_DELETE + id);
		elementosDossiedigital.remove(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_EXCLUSAO_SUCESSO);
		DossiedigitalRetorno retorno = new DossiedigitalRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		return retorno;
	}
}

