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

import br.gov.caixa.dossie.rs.entity.ResumoEntity;
import br.gov.caixa.dossie.rs.requisicao.ResumoRequisicao;
import br.gov.caixa.dossie.rs.retorno.ResumoRetorno;
import br.gov.caixa.dossie.util.DossieConstantes;
/**
 * @author SIOGP
 */
@Stateless
@LocalBean
@Named
public class ResumoService {
	private static final Logger LOGGER = Logger.getLogger(ResumoService.class);
	/* colecao da tela Resumo*/
	private Map<Long, Object> elementosResumo;
	/**
	 * Construtor Padrão
	 */
	public ResumoService() {
		super();
		/* As variaveis elementosXXXX armazenam em memoria uma coleção de objetos a serem exibidos em tela.
		 *	Faz-se útil em caso de tabelas	
		 */
		/* inicialização da coleção da tela Resumo*/
		elementosResumo = new HashMap<Long, Object>();
		/* A inicialização dos elementos abaixo somente é necessario para a demonstracao da carga inicial dos elementos em tela.
		 *	Deve ser removido quando não for mais necessário
		 */
		ResumoEntity resumo = new ResumoEntity();
		elementosResumo.put(resumo.getId(), resumo);
	}
	/**
	 * @Objetivo O metodo createResumo é utilizado para incluir um
	 *           ResumoEntity na colecao de elementos.
	 * @param requisicao
	 * @return retorno
	 */
	public  ResumoRetorno createResumo(ResumoRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_CREATE);
		ResumoEntity resumo = new ResumoEntity();
		resumo.setId(System.currentTimeMillis());
	    resumo.setImgmenuresumodoclientepng3(requisicao.getImgmenuresumodoclientepng3());
	    resumo.setChkpesquisacadastral2(requisicao.getChkpesquisacadastral2());
	    resumo.setImgmenuresumodoclientepng2(requisicao.getImgmenuresumodoclientepng2());
		elementosResumo.put(resumo.getId(), resumo);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_INCLUSAO_SUCESSO);
		ResumoRetorno retorno = new ResumoRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(resumo);
		return retorno;
	}
	/**
	 * @Objetivo O metodo readAllResumo é utilizado para obter todos os
	 *           {GroupId + 'Entity'} existentes na colecao de elementos.
	 * @return retorno
	 */
	public ResumoRetorno readAllResumo() {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ_ALL);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_SUCESSO);
		ResumoRetorno retorno = new ResumoRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		for (Object resumo: elementosResumo.values()) {
			retorno.getData().add((ResumoEntity) resumo);
		}
		return retorno;
	}
	/**
	 * @Objetivo O metodo readResumo(Long id) é utilizado para obter um elemento
	 *           ResumoEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public ResumoRetorno readResumo(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ + id);
		ResumoEntity resumo = (ResumoEntity) elementosResumo.get(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_ID_SUCESSO);
		ResumoRetorno retorno = new ResumoRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(resumo);
		return retorno;
	}
	/**
	 * @Objetivo O metodo updateResumo é utilizado para atualizar um elemento
	 *           ResumoEntity existente na colecao de elementos.
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	public ResumoRetorno updateResumo(Long id, ResumoRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_UPDATE + id + "," + requisicao);
		ResumoEntity resumo = (ResumoEntity) elementosResumo.get(id);
		resumo.setImgmenuresumodoclientepng3(requisicao.getImgmenuresumodoclientepng3());
		resumo.setChkpesquisacadastral2(requisicao.getChkpesquisacadastral2());
		resumo.setImgmenuresumodoclientepng2(requisicao.getImgmenuresumodoclientepng2());
		elementosResumo.remove(id);
		elementosResumo.put(id, resumo);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_UPDATE_SUCESSO);
		ResumoRetorno retorno = new ResumoRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(resumo);
		return retorno;
	}
	/**
	 * @Objetivo O metodo deleteResumo é utilizado para remover um elemento
	 *           ResumoEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public ResumoRetorno deleteResumo(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_DELETE + id);
		elementosResumo.remove(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_EXCLUSAO_SUCESSO);
		ResumoRetorno retorno = new ResumoRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		return retorno;
	}
}

