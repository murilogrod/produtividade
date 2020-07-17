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

import br.gov.caixa.dossie.rs.entity.AvaliacaoriscoEntity;
import br.gov.caixa.dossie.rs.requisicao.AvaliacaoriscoRequisicao;
import br.gov.caixa.dossie.rs.retorno.AvaliacaoriscoRetorno;
import br.gov.caixa.dossie.util.DossieConstantes;
/**
 * @author SIOGP
 */
@Stateless
@LocalBean
@Named
public class AvaliacaoriscoService {
	private static final Logger LOGGER = Logger.getLogger(AvaliacaoriscoService.class);
	/* colecao da tela Avaliacaorisco*/
	private Map<Long, Object> elementosAvaliacaorisco;
	/**
	 * Construtor Padrão
	 */
	public AvaliacaoriscoService() {
		super();
		/* As variaveis elementosXXXX armazenam em memoria uma coleção de objetos a serem exibidos em tela.
		 *	Faz-se útil em caso de tabelas	
		 */
		/* inicialização da coleção da tela Avaliacaorisco*/
		elementosAvaliacaorisco = new HashMap<Long, Object>();
		/* A inicialização dos elementos abaixo somente é necessario para a demonstracao da carga inicial dos elementos em tela.
		 *	Deve ser removido quando não for mais necessário
		 */
		AvaliacaoriscoEntity avaliacaorisco = new AvaliacaoriscoEntity();
		elementosAvaliacaorisco.put(avaliacaorisco.getId(), avaliacaorisco);
	}
	/**
	 * @Objetivo O metodo createAvaliacaorisco é utilizado para incluir um
	 *           AvaliacaoriscoEntity na colecao de elementos.
	 * @param requisicao
	 * @return retorno
	 */
	public  AvaliacaoriscoRetorno createAvaliacaorisco(AvaliacaoriscoRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_CREATE);
		AvaliacaoriscoEntity avaliacaorisco = new AvaliacaoriscoEntity();
		avaliacaorisco.setId(System.currentTimeMillis());
		elementosAvaliacaorisco.put(avaliacaorisco.getId(), avaliacaorisco);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_INCLUSAO_SUCESSO);
		AvaliacaoriscoRetorno retorno = new AvaliacaoriscoRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(avaliacaorisco);
		return retorno;
	}
	/**
	 * @Objetivo O metodo readAllAvaliacaorisco é utilizado para obter todos os
	 *           {GroupId + 'Entity'} existentes na colecao de elementos.
	 * @return retorno
	 */
	public AvaliacaoriscoRetorno readAllAvaliacaorisco() {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ_ALL);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_SUCESSO);
		AvaliacaoriscoRetorno retorno = new AvaliacaoriscoRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		for (Object avaliacaorisco: elementosAvaliacaorisco.values()) {
			retorno.getData().add((AvaliacaoriscoEntity) avaliacaorisco);
		}
		return retorno;
	}
	/**
	 * @Objetivo O metodo readAvaliacaorisco(Long id) é utilizado para obter um elemento
	 *           AvaliacaoriscoEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public AvaliacaoriscoRetorno readAvaliacaorisco(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ + id);
		AvaliacaoriscoEntity avaliacaorisco = (AvaliacaoriscoEntity) elementosAvaliacaorisco.get(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_ID_SUCESSO);
		AvaliacaoriscoRetorno retorno = new AvaliacaoriscoRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(avaliacaorisco);
		return retorno;
	}
	/**
	 * @Objetivo O metodo updateAvaliacaorisco é utilizado para atualizar um elemento
	 *           AvaliacaoriscoEntity existente na colecao de elementos.
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	public AvaliacaoriscoRetorno updateAvaliacaorisco(Long id, AvaliacaoriscoRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_UPDATE + id + "," + requisicao);
		AvaliacaoriscoEntity avaliacaorisco = (AvaliacaoriscoEntity) elementosAvaliacaorisco.get(id);
		elementosAvaliacaorisco.remove(id);
		elementosAvaliacaorisco.put(id, avaliacaorisco);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_UPDATE_SUCESSO);
		AvaliacaoriscoRetorno retorno = new AvaliacaoriscoRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(avaliacaorisco);
		return retorno;
	}
	/**
	 * @Objetivo O metodo deleteAvaliacaorisco é utilizado para remover um elemento
	 *           AvaliacaoriscoEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public AvaliacaoriscoRetorno deleteAvaliacaorisco(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_DELETE + id);
		elementosAvaliacaorisco.remove(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_EXCLUSAO_SUCESSO);
		AvaliacaoriscoRetorno retorno = new AvaliacaoriscoRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		return retorno;
	}
}

