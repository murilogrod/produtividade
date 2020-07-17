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

import br.gov.caixa.dossie.rs.entity.AberturadecontaEntity;
import br.gov.caixa.dossie.rs.requisicao.AberturadecontaRequisicao;
import br.gov.caixa.dossie.rs.retorno.AberturadecontaRetorno;
import br.gov.caixa.dossie.rs.retorno.Retorno;
import br.gov.caixa.dossie.util.DossieConstantes;

/**
 * @author SIOGP
 */
@Stateless
@LocalBean
@Named
public class AberturadecontaService {
	private static final Logger LOGGER = Logger.getLogger(AberturadecontaService.class);
	/* colecao da tela Aberturadeconta */
	private Map<Long, Object> elementosAberturadeconta;

	/**
	 * Construtor Padrão
	 */
	public AberturadecontaService() {
		super();
		/*
		 * As variaveis elementosXXXX armazenam em memoria uma coleção de objetos a
		 * serem exibidos em tela. Faz-se útil em caso de tabelas
		 */
		/* inicialização da coleção da tela Aberturadeconta */
		elementosAberturadeconta = new HashMap<Long, Object>();
		/*
		 * A inicialização dos elementos abaixo somente é necessario para a demonstracao
		 * da carga inicial dos elementos em tela. Deve ser removido quando não for mais
		 * necessário
		 */
		AberturadecontaEntity aberturadeconta = new AberturadecontaEntity();
		elementosAberturadeconta.put(aberturadeconta.getId(), aberturadeconta);
	}

	/**
	 * @Objetivo O metodo createAberturadeconta é utilizado para incluir um
	 *           AberturadecontaEntity na colecao de elementos.
	 * @param requisicao
	 * @return retorno
	 */
	public AberturadecontaRetorno createAberturadeconta(AberturadecontaRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_CREATE);
		AberturadecontaEntity aberturadeconta = new AberturadecontaEntity();
		aberturadeconta.setId(System.currentTimeMillis());
		aberturadeconta.setImgbarra0png(requisicao.getImgbarra0png());
		aberturadeconta.setCmbtipodeconta2(requisicao.getCmbtipodeconta2());
		aberturadeconta.setOptmodalidade(requisicao.getOptmodalidade());
		aberturadeconta.setChkopces2(requisicao.getChkopces2());
		aberturadeconta.setOptmenoremancipado2(requisicao.getOptmenoremancipado2());
		aberturadeconta.setImgbarra0png(requisicao.getImgbarra0png());
		aberturadeconta.setCmbtipodeconta2(requisicao.getCmbtipodeconta2());
		aberturadeconta.setOptmodalidade(requisicao.getOptmodalidade());
		aberturadeconta.setOptsolidaria(requisicao.getOptsolidaria());
		aberturadeconta.setOptpossui(requisicao.getOptpossui());
		aberturadeconta.setNuminformeocpf2(requisicao.getNuminformeocpf2());
		aberturadeconta.setBtilupapng2(requisicao.getBtilupapng2());
		aberturadeconta.setTxtnome2(requisicao.getTxtnome2());
		aberturadeconta.setImgbarra0png(requisicao.getImgbarra0png());
		aberturadeconta.setCmbtipodeconta(requisicao.getCmbtipodeconta());
		aberturadeconta.setOptmodalidade2(requisicao.getOptmodalidade2());
		aberturadeconta.setOptsolidaria2(requisicao.getOptsolidaria2());
		aberturadeconta.setOptpossui2(requisicao.getOptpossui2());
		aberturadeconta.setNuminformeocpf5(requisicao.getNuminformeocpf5());
		aberturadeconta.setBtilupapng2(requisicao.getBtilupapng2());
		aberturadeconta.setTxtnome3(requisicao.getTxtnome3());
		aberturadeconta.setNuminformeocpf(requisicao.getNuminformeocpf());
		aberturadeconta.setBtilupapng3(requisicao.getBtilupapng3());
		aberturadeconta.setTxtnome2(requisicao.getTxtnome2());
		aberturadeconta.setNuminformeocpf2(requisicao.getNuminformeocpf2());
		aberturadeconta.setBtilupapng(requisicao.getBtilupapng());
		aberturadeconta.setTxtnome4(requisicao.getTxtnome4());
		aberturadeconta.setNuminformeocpf3(requisicao.getNuminformeocpf3());
		aberturadeconta.setBtilupapng4(requisicao.getBtilupapng4());
		aberturadeconta.setTxtnome(requisicao.getTxtnome());
		aberturadeconta.setImgbarra2png(requisicao.getImgbarra2png());
		aberturadeconta.setImgbarra3png(requisicao.getImgbarra3png());
		aberturadeconta.setImgbarra3png(requisicao.getImgbarra3png());
		aberturadeconta.setBtidocrendimentopng(requisicao.getBtidocrendimentopng());
		aberturadeconta.setBtidocresidenciapng(requisicao.getBtidocresidenciapng());
		aberturadeconta.setBtidocidentificacaopng(requisicao.getBtidocidentificacaopng());
		aberturadeconta.setCmbnacionalidade2(requisicao.getCmbnacionalidade2());
		aberturadeconta.setCmbnaturalidade2(requisicao.getCmbnaturalidade2());
		aberturadeconta.setTxtcidade(requisicao.getTxtcidade());
		aberturadeconta.setTxtnomedopai2(requisicao.getTxtnomedopai2());
		aberturadeconta.setImgbarra3png(requisicao.getImgbarra3png());
		aberturadeconta.setBtidocrendimentopng(requisicao.getBtidocrendimentopng());
		aberturadeconta.setBtidocresidenciapng(requisicao.getBtidocresidenciapng());
		aberturadeconta.setBtidocidentificacaopng(requisicao.getBtidocidentificacaopng());
		aberturadeconta.setImgidentidadejpg(requisicao.getImgidentidadejpg());
		aberturadeconta.setCmbnacionalidade2(requisicao.getCmbnacionalidade2());
		aberturadeconta.setCmbnaturalidade2(requisicao.getCmbnaturalidade2());
		aberturadeconta.setTxtcidade(requisicao.getTxtcidade());
		aberturadeconta.setTxtnomedopai2(requisicao.getTxtnomedopai2());
		aberturadeconta.setImgbarra3png(requisicao.getImgbarra3png());
		aberturadeconta.setBtidocrendimentopng(requisicao.getBtidocrendimentopng());
		aberturadeconta.setBtidocresidenciapng(requisicao.getBtidocresidenciapng());
		aberturadeconta.setBtidocidentificacaopng(requisicao.getBtidocidentificacaopng());
		aberturadeconta.setNumcep2(requisicao.getNumcep2());
		aberturadeconta.setCmbtipodelogradouro2(requisicao.getCmbtipodelogradouro2());
		aberturadeconta.setTxtlogradouro(requisicao.getTxtlogradouro());
		aberturadeconta.setNumnumero2(requisicao.getNumnumero2());
		aberturadeconta.setTxtcomplemento2(requisicao.getTxtcomplemento2());
		aberturadeconta.setTxtbairro2(requisicao.getTxtbairro2());
		aberturadeconta.setCmbuf2(requisicao.getCmbuf2());
		aberturadeconta.setTxtcidade(requisicao.getTxtcidade());
		aberturadeconta.setImgbarra3png(requisicao.getImgbarra3png());
		aberturadeconta.setBtidocrendimentopng(requisicao.getBtidocrendimentopng());
		aberturadeconta.setBtidocresidenciapng(requisicao.getBtidocresidenciapng());
		aberturadeconta.setBtidocidentificacaopng(requisicao.getBtidocidentificacaopng());
		aberturadeconta.setImgcompresidenciajpg(requisicao.getImgcompresidenciajpg());
		aberturadeconta.setNumcep2(requisicao.getNumcep2());
		aberturadeconta.setCmbtipodelogradouro2(requisicao.getCmbtipodelogradouro2());
		aberturadeconta.setTxtlogradouro2(requisicao.getTxtlogradouro2());
		aberturadeconta.setNumnumero2(requisicao.getNumnumero2());
		aberturadeconta.setTxtcomplemento2(requisicao.getTxtcomplemento2());
		aberturadeconta.setTxtbairro2(requisicao.getTxtbairro2());
		aberturadeconta.setCmbuf2(requisicao.getCmbuf2());
		aberturadeconta.setTxtcidade(requisicao.getTxtcidade());
		aberturadeconta.setImgbarra3png(requisicao.getImgbarra3png());
		aberturadeconta.setBtidocrendimentopng(requisicao.getBtidocrendimentopng());
		aberturadeconta.setBtidocresidenciapng(requisicao.getBtidocresidenciapng());
		aberturadeconta.setBtidocidentificacaopng(requisicao.getBtidocidentificacaopng());
		aberturadeconta.setCmbtipodefontepagadora(requisicao.getCmbtipodefontepagadora());
		aberturadeconta.setNumcnpj2(requisicao.getNumcnpj2());
		aberturadeconta.setTxtnomedafontepagadora2(requisicao.getTxtnomedafontepagadora2());
		aberturadeconta.setNumcoclidafontepagadora2(requisicao.getNumcoclidafontepagadora2());
		aberturadeconta.setCmbocupacao(requisicao.getCmbocupacao());
		aberturadeconta.setDtdatadeadmissaoinicio2(requisicao.getDtdatadeadmissaoinicio2());
		aberturadeconta.setNumrendabruta2(requisicao.getNumrendabruta2());
		aberturadeconta.setNumrendaliquida2(requisicao.getNumrendaliquida2());
		aberturadeconta.setNumvalordoimpostoretidonafonte2(requisicao.getNumvalordoimpostoretidonafonte2());
		aberturadeconta.setImgbarra3png(requisicao.getImgbarra3png());
		aberturadeconta.setBtidocrendimentopng(requisicao.getBtidocrendimentopng());
		aberturadeconta.setBtidocresidenciapng(requisicao.getBtidocresidenciapng());
		aberturadeconta.setBtidocidentificacaopng(requisicao.getBtidocidentificacaopng());
		aberturadeconta.setImgcomprendajpg(requisicao.getImgcomprendajpg());
		aberturadeconta.setCmbtipodefontepagadora(requisicao.getCmbtipodefontepagadora());
		aberturadeconta.setNumcnpj2(requisicao.getNumcnpj2());
		aberturadeconta.setTxtnomedafontepagadora2(requisicao.getTxtnomedafontepagadora2());
		aberturadeconta.setNumcoclidafontepagadora2(requisicao.getNumcoclidafontepagadora2());
		aberturadeconta.setCmbocupacao(requisicao.getCmbocupacao());
		aberturadeconta.setDtdatadeadmissaoinicio2(requisicao.getDtdatadeadmissaoinicio2());
		aberturadeconta.setNumrendabruta2(requisicao.getNumrendabruta2());
		aberturadeconta.setNumrendaliquida2(requisicao.getNumrendaliquida2());
		aberturadeconta.setNumvalordoimpostoretidonafonte2(requisicao.getNumvalordoimpostoretidonafonte2());
		aberturadeconta.setImgbarra04png(requisicao.getImgbarra04png());
		aberturadeconta.setImgbarra4png(requisicao.getImgbarra4png());
		aberturadeconta.setMsksenha(requisicao.getMsksenha());
		aberturadeconta.setTxtredigitacaosenha(requisicao.getTxtredigitacaosenha());
		aberturadeconta.setImgbarra05png(requisicao.getImgbarra05png());
		aberturadeconta.setCmbgraudeinstrucao(requisicao.getCmbgraudeinstrucao());
		aberturadeconta.setCmbsexo2(requisicao.getCmbsexo2());
		aberturadeconta.setCmbprofissaocdigodaocupacao(requisicao.getCmbprofissaocdigodaocupacao());
		aberturadeconta.setTxtenderecocomercial(requisicao.getTxtenderecocomercial());
		aberturadeconta.setCmbestadocivil2(requisicao.getCmbestadocivil2());
		aberturadeconta.setNumcpfdoconjuge(requisicao.getNumcpfdoconjuge());
		aberturadeconta.setImglupapng2(requisicao.getImglupapng2());
		aberturadeconta.setTxtnomedoconjuge(requisicao.getTxtnomedoconjuge());
		aberturadeconta.setDtdatadenascimentodoconjuge(requisicao.getDtdatadenascimentodoconjuge());
		aberturadeconta.setCmbfinalidade(requisicao.getCmbfinalidade());
		aberturadeconta.setCmbmeiosdecomunicacao(requisicao.getCmbmeiosdecomunicacao());
		aberturadeconta.setNumdddenumero(requisicao.getNumdddenumero());
		aberturadeconta.setTxtcontato(requisicao.getTxtcontato());
		aberturadeconta.setChkopces2(requisicao.getChkopces2());
		aberturadeconta.setChkselecioneasopces2(requisicao.getChkselecioneasopces2());
		aberturadeconta.setCmbsaquestransfernciaspagamentosagendamentosapartirde(
				requisicao.getCmbsaquestransfernciaspagamentosagendamentosapartirde());
		aberturadeconta.setCmbcomprascomcartaodedbitoapartirde(requisicao.getCmbcomprascomcartaodedbitoapartirde());
		aberturadeconta.setCmbdesejareceberinformacesdocartaodecrditocaixa2(
				requisicao.getCmbdesejareceberinformacesdocartaodecrditocaixa2());
		aberturadeconta.setCmbautorizaoenviodeinformacesdofgts(requisicao.getCmbautorizaoenviodeinformacesdofgts());
		aberturadeconta.setCmbgostariaderecebermaisinformacessobreprodutoseservicosdacaixa2(
				requisicao.getCmbgostariaderecebermaisinformacessobreprodutoseservicosdacaixa2());
		aberturadeconta.setOptselecioneaopcao(requisicao.getOptselecioneaopcao());
		aberturadeconta.setOptdeclaracaofatcacrs2(requisicao.getOptdeclaracaofatcacrs2());
		aberturadeconta.setNumnumerotin2(requisicao.getNumnumerotin2());
		aberturadeconta.setCmbdeclaracaodepropsitosedanaturezadacontadepsito2(
				requisicao.getCmbdeclaracaodepropsitosedanaturezadacontadepsito2());
		aberturadeconta.setMskassinaturadocliente2(requisicao.getMskassinaturadocliente2());
		aberturadeconta.setMskinformeasenha(requisicao.getMskinformeasenha());
		aberturadeconta.setMskinformeasenha2(requisicao.getMskinformeasenha2());
		aberturadeconta.setImgbarra05png(requisicao.getImgbarra05png());
		aberturadeconta.setImgbarra06png(requisicao.getImgbarra06png());
		aberturadeconta.setImgbarra06png(requisicao.getImgbarra06png());
		aberturadeconta.setChkselecioneasopces(requisicao.getChkselecioneasopces());
		aberturadeconta.setImgbarra06png(requisicao.getImgbarra06png());
		aberturadeconta.setImgbarra06png(requisicao.getImgbarra06png());
		aberturadeconta.setChkoperacaocomercial(requisicao.getChkoperacaocomercial());
		aberturadeconta.setImgbarra07png(requisicao.getImgbarra07png());
		aberturadeconta.setChkselecioneasopces8(requisicao.getChkselecioneasopces8());
		aberturadeconta.setCmbmodalidadedacesta2(requisicao.getCmbmodalidadedacesta2());
		aberturadeconta.setNumdiadodbito(requisicao.getNumdiadodbito());
		aberturadeconta.setOptpacotedescontocestadeservicos2(requisicao.getOptpacotedescontocestadeservicos2());
		aberturadeconta.setChkselecioneasopces(requisicao.getChkselecioneasopces());
		aberturadeconta.setChkselecioneasopces7(requisicao.getChkselecioneasopces7());
		aberturadeconta.setNumlimitecontratado3(requisicao.getNumlimitecontratado3());
		aberturadeconta.setNumdatadedbitojuros(requisicao.getNumdatadedbitojuros());
		aberturadeconta.setChkselecioneasopces3(requisicao.getChkselecioneasopces3());
		aberturadeconta.setChkselecioneasopces5(requisicao.getChkselecioneasopces5());
		aberturadeconta.setNumprestacaomaximaadmitida2(requisicao.getNumprestacaomaximaadmitida2());
		aberturadeconta.setChkselecioneasopces6(requisicao.getChkselecioneasopces6());
		aberturadeconta.setNumlimitecontratado(requisicao.getNumlimitecontratado());
		aberturadeconta.setCmbtipodecartao2(requisicao.getCmbtipodecartao2());
		aberturadeconta.setCmbmodalidade2(requisicao.getCmbmodalidade2());
		aberturadeconta.setCmbbandeira2(requisicao.getCmbbandeira2());
		aberturadeconta.setCmbvariante2(requisicao.getCmbvariante2());
		aberturadeconta.setCmbdiadovencimentodafatura2(requisicao.getCmbdiadovencimentodafatura2());
		aberturadeconta.setOptenderecoparaentregadafatura2(requisicao.getOptenderecoparaentregadafatura2());
		aberturadeconta.setOptcontratasegurodocartao(requisicao.getOptcontratasegurodocartao());
		aberturadeconta.setOptprogramapontoscaixa2(requisicao.getOptprogramapontoscaixa2());
		aberturadeconta.setOptconcordaemparticipardecampanhasdeincentivodacaixacompremiacao2(
				requisicao.getOptconcordaemparticipardecampanhasdeincentivodacaixacompremiacao2());
		aberturadeconta.setChkselecioneasopces4(requisicao.getChkselecioneasopces4());
		aberturadeconta.setChkformularios(requisicao.getChkformularios());
		aberturadeconta.setMskassinaturadocliente(requisicao.getMskassinaturadocliente());
		aberturadeconta.setMskinformeasenha(requisicao.getMskinformeasenha());
		aberturadeconta.setMskinformeasenha2(requisicao.getMskinformeasenha2());
		aberturadeconta.setImgbarra08png(requisicao.getImgbarra08png());
		elementosAberturadeconta.put(aberturadeconta.getId(), aberturadeconta);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_INCLUSAO_SUCESSO);
		AberturadecontaRetorno retorno = new AberturadecontaRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(aberturadeconta);
		return retorno;
	}

	/**
	 * @Objetivo O metodo readAllAberturadeconta é utilizado para obter todos os
	 *           {GroupId + 'Entity'} existentes na colecao de elementos.
	 * @return retorno
	 */
	public AberturadecontaRetorno readAllAberturadeconta() {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ_ALL);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_SUCESSO);
		AberturadecontaRetorno retorno = new AberturadecontaRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		for (Object aberturadeconta : elementosAberturadeconta.values()) {
			retorno.getData().add((AberturadecontaEntity) aberturadeconta);
		}
		return retorno;
	}

	/**
	 * @Objetivo O metodo readAberturadeconta(Long id) é utilizado para obter um
	 *           elemento AberturadecontaEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public AberturadecontaRetorno readAberturadeconta(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_READ + id);
		AberturadecontaEntity aberturadeconta = (AberturadecontaEntity) elementosAberturadeconta.get(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_LEITURA_ID_SUCESSO);
		AberturadecontaRetorno retorno = new AberturadecontaRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(aberturadeconta);
		return retorno;
	}

	/**
	 * @Objetivo O metodo updateAberturadeconta é utilizado para atualizar um
	 *           elemento AberturadecontaEntity existente na colecao de elementos.
	 * @param id
	 * @param requisicao
	 * @return retorno
	 */
	public AberturadecontaRetorno updateAberturadeconta(Long id, AberturadecontaRequisicao requisicao) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_UPDATE + id + "," + requisicao);
		AberturadecontaEntity aberturadeconta = (AberturadecontaEntity) elementosAberturadeconta.get(id);
		aberturadeconta.setImgbarra0png(requisicao.getImgbarra0png());
		aberturadeconta.setCmbtipodeconta2(requisicao.getCmbtipodeconta2());
		aberturadeconta.setOptmodalidade(requisicao.getOptmodalidade());
		aberturadeconta.setChkopces2(requisicao.getChkopces2());
		aberturadeconta.setOptmenoremancipado2(requisicao.getOptmenoremancipado2());
		aberturadeconta.setImgbarra0png(requisicao.getImgbarra0png());
		aberturadeconta.setCmbtipodeconta2(requisicao.getCmbtipodeconta2());
		aberturadeconta.setOptmodalidade(requisicao.getOptmodalidade());
		aberturadeconta.setOptsolidaria(requisicao.getOptsolidaria());
		aberturadeconta.setOptpossui(requisicao.getOptpossui());
		aberturadeconta.setNuminformeocpf2(requisicao.getNuminformeocpf2());
		aberturadeconta.setBtilupapng2(requisicao.getBtilupapng2());
		aberturadeconta.setTxtnome2(requisicao.getTxtnome2());
		aberturadeconta.setImgbarra0png(requisicao.getImgbarra0png());
		aberturadeconta.setCmbtipodeconta(requisicao.getCmbtipodeconta());
		aberturadeconta.setOptmodalidade2(requisicao.getOptmodalidade2());
		aberturadeconta.setOptsolidaria2(requisicao.getOptsolidaria2());
		aberturadeconta.setOptpossui2(requisicao.getOptpossui2());
		aberturadeconta.setNuminformeocpf5(requisicao.getNuminformeocpf5());
		aberturadeconta.setBtilupapng2(requisicao.getBtilupapng2());
		aberturadeconta.setTxtnome3(requisicao.getTxtnome3());
		aberturadeconta.setNuminformeocpf(requisicao.getNuminformeocpf());
		aberturadeconta.setBtilupapng3(requisicao.getBtilupapng3());
		aberturadeconta.setTxtnome2(requisicao.getTxtnome2());
		aberturadeconta.setNuminformeocpf2(requisicao.getNuminformeocpf2());
		aberturadeconta.setBtilupapng(requisicao.getBtilupapng());
		aberturadeconta.setTxtnome4(requisicao.getTxtnome4());
		aberturadeconta.setNuminformeocpf3(requisicao.getNuminformeocpf3());
		aberturadeconta.setBtilupapng4(requisicao.getBtilupapng4());
		aberturadeconta.setTxtnome(requisicao.getTxtnome());
		aberturadeconta.setImgbarra2png(requisicao.getImgbarra2png());
		aberturadeconta.setImgbarra3png(requisicao.getImgbarra3png());
		aberturadeconta.setImgbarra3png(requisicao.getImgbarra3png());
		aberturadeconta.setBtidocrendimentopng(requisicao.getBtidocrendimentopng());
		aberturadeconta.setBtidocresidenciapng(requisicao.getBtidocresidenciapng());
		aberturadeconta.setBtidocidentificacaopng(requisicao.getBtidocidentificacaopng());
		aberturadeconta.setCmbnacionalidade2(requisicao.getCmbnacionalidade2());
		aberturadeconta.setCmbnaturalidade2(requisicao.getCmbnaturalidade2());
		aberturadeconta.setTxtcidade(requisicao.getTxtcidade());
		aberturadeconta.setTxtnomedopai2(requisicao.getTxtnomedopai2());
		aberturadeconta.setImgbarra3png(requisicao.getImgbarra3png());
		aberturadeconta.setBtidocrendimentopng(requisicao.getBtidocrendimentopng());
		aberturadeconta.setBtidocresidenciapng(requisicao.getBtidocresidenciapng());
		aberturadeconta.setBtidocidentificacaopng(requisicao.getBtidocidentificacaopng());
		aberturadeconta.setImgidentidadejpg(requisicao.getImgidentidadejpg());
		aberturadeconta.setCmbnacionalidade2(requisicao.getCmbnacionalidade2());
		aberturadeconta.setCmbnaturalidade2(requisicao.getCmbnaturalidade2());
		aberturadeconta.setTxtcidade(requisicao.getTxtcidade());
		aberturadeconta.setTxtnomedopai2(requisicao.getTxtnomedopai2());
		aberturadeconta.setImgbarra3png(requisicao.getImgbarra3png());
		aberturadeconta.setBtidocrendimentopng(requisicao.getBtidocrendimentopng());
		aberturadeconta.setBtidocresidenciapng(requisicao.getBtidocresidenciapng());
		aberturadeconta.setBtidocidentificacaopng(requisicao.getBtidocidentificacaopng());
		aberturadeconta.setNumcep2(requisicao.getNumcep2());
		aberturadeconta.setCmbtipodelogradouro2(requisicao.getCmbtipodelogradouro2());
		aberturadeconta.setTxtlogradouro(requisicao.getTxtlogradouro());
		aberturadeconta.setNumnumero2(requisicao.getNumnumero2());
		aberturadeconta.setTxtcomplemento2(requisicao.getTxtcomplemento2());
		aberturadeconta.setTxtbairro2(requisicao.getTxtbairro2());
		aberturadeconta.setCmbuf2(requisicao.getCmbuf2());
		aberturadeconta.setTxtcidade(requisicao.getTxtcidade());
		aberturadeconta.setImgbarra3png(requisicao.getImgbarra3png());
		aberturadeconta.setBtidocrendimentopng(requisicao.getBtidocrendimentopng());
		aberturadeconta.setBtidocresidenciapng(requisicao.getBtidocresidenciapng());
		aberturadeconta.setBtidocidentificacaopng(requisicao.getBtidocidentificacaopng());
		aberturadeconta.setImgcompresidenciajpg(requisicao.getImgcompresidenciajpg());
		aberturadeconta.setNumcep2(requisicao.getNumcep2());
		aberturadeconta.setCmbtipodelogradouro2(requisicao.getCmbtipodelogradouro2());
		aberturadeconta.setTxtlogradouro2(requisicao.getTxtlogradouro2());
		aberturadeconta.setNumnumero2(requisicao.getNumnumero2());
		aberturadeconta.setTxtcomplemento2(requisicao.getTxtcomplemento2());
		aberturadeconta.setTxtbairro2(requisicao.getTxtbairro2());
		aberturadeconta.setCmbuf2(requisicao.getCmbuf2());
		aberturadeconta.setTxtcidade(requisicao.getTxtcidade());
		aberturadeconta.setImgbarra3png(requisicao.getImgbarra3png());
		aberturadeconta.setBtidocrendimentopng(requisicao.getBtidocrendimentopng());
		aberturadeconta.setBtidocresidenciapng(requisicao.getBtidocresidenciapng());
		aberturadeconta.setBtidocidentificacaopng(requisicao.getBtidocidentificacaopng());
		aberturadeconta.setCmbtipodefontepagadora(requisicao.getCmbtipodefontepagadora());
		aberturadeconta.setNumcnpj2(requisicao.getNumcnpj2());
		aberturadeconta.setTxtnomedafontepagadora2(requisicao.getTxtnomedafontepagadora2());
		aberturadeconta.setNumcoclidafontepagadora2(requisicao.getNumcoclidafontepagadora2());
		aberturadeconta.setCmbocupacao(requisicao.getCmbocupacao());
		aberturadeconta.setDtdatadeadmissaoinicio2(requisicao.getDtdatadeadmissaoinicio2());
		aberturadeconta.setNumrendabruta2(requisicao.getNumrendabruta2());
		aberturadeconta.setNumrendaliquida2(requisicao.getNumrendaliquida2());
		aberturadeconta.setNumvalordoimpostoretidonafonte2(requisicao.getNumvalordoimpostoretidonafonte2());
		aberturadeconta.setImgbarra3png(requisicao.getImgbarra3png());
		aberturadeconta.setBtidocrendimentopng(requisicao.getBtidocrendimentopng());
		aberturadeconta.setBtidocresidenciapng(requisicao.getBtidocresidenciapng());
		aberturadeconta.setBtidocidentificacaopng(requisicao.getBtidocidentificacaopng());
		aberturadeconta.setImgcomprendajpg(requisicao.getImgcomprendajpg());
		aberturadeconta.setCmbtipodefontepagadora(requisicao.getCmbtipodefontepagadora());
		aberturadeconta.setNumcnpj2(requisicao.getNumcnpj2());
		aberturadeconta.setTxtnomedafontepagadora2(requisicao.getTxtnomedafontepagadora2());
		aberturadeconta.setNumcoclidafontepagadora2(requisicao.getNumcoclidafontepagadora2());
		aberturadeconta.setCmbocupacao(requisicao.getCmbocupacao());
		aberturadeconta.setDtdatadeadmissaoinicio2(requisicao.getDtdatadeadmissaoinicio2());
		aberturadeconta.setNumrendabruta2(requisicao.getNumrendabruta2());
		aberturadeconta.setNumrendaliquida2(requisicao.getNumrendaliquida2());
		aberturadeconta.setNumvalordoimpostoretidonafonte2(requisicao.getNumvalordoimpostoretidonafonte2());
		aberturadeconta.setImgbarra04png(requisicao.getImgbarra04png());
		aberturadeconta.setImgbarra4png(requisicao.getImgbarra4png());
		aberturadeconta.setMsksenha(requisicao.getMsksenha());
		aberturadeconta.setTxtredigitacaosenha(requisicao.getTxtredigitacaosenha());
		aberturadeconta.setImgbarra05png(requisicao.getImgbarra05png());
		aberturadeconta.setCmbgraudeinstrucao(requisicao.getCmbgraudeinstrucao());
		aberturadeconta.setCmbsexo2(requisicao.getCmbsexo2());
		aberturadeconta.setCmbprofissaocdigodaocupacao(requisicao.getCmbprofissaocdigodaocupacao());
		aberturadeconta.setTxtenderecocomercial(requisicao.getTxtenderecocomercial());
		aberturadeconta.setCmbestadocivil2(requisicao.getCmbestadocivil2());
		aberturadeconta.setNumcpfdoconjuge(requisicao.getNumcpfdoconjuge());
		aberturadeconta.setImglupapng2(requisicao.getImglupapng2());
		aberturadeconta.setTxtnomedoconjuge(requisicao.getTxtnomedoconjuge());
		aberturadeconta.setDtdatadenascimentodoconjuge(requisicao.getDtdatadenascimentodoconjuge());
		aberturadeconta.setCmbfinalidade(requisicao.getCmbfinalidade());
		aberturadeconta.setCmbmeiosdecomunicacao(requisicao.getCmbmeiosdecomunicacao());
		aberturadeconta.setNumdddenumero(requisicao.getNumdddenumero());
		aberturadeconta.setTxtcontato(requisicao.getTxtcontato());
		aberturadeconta.setChkopces2(requisicao.getChkopces2());
		aberturadeconta.setChkselecioneasopces2(requisicao.getChkselecioneasopces2());
		aberturadeconta.setCmbsaquestransfernciaspagamentosagendamentosapartirde(
				requisicao.getCmbsaquestransfernciaspagamentosagendamentosapartirde());
		aberturadeconta.setCmbcomprascomcartaodedbitoapartirde(requisicao.getCmbcomprascomcartaodedbitoapartirde());
		aberturadeconta.setCmbdesejareceberinformacesdocartaodecrditocaixa2(
				requisicao.getCmbdesejareceberinformacesdocartaodecrditocaixa2());
		aberturadeconta.setCmbautorizaoenviodeinformacesdofgts(requisicao.getCmbautorizaoenviodeinformacesdofgts());
		aberturadeconta.setCmbgostariaderecebermaisinformacessobreprodutoseservicosdacaixa2(
				requisicao.getCmbgostariaderecebermaisinformacessobreprodutoseservicosdacaixa2());
		aberturadeconta.setOptselecioneaopcao(requisicao.getOptselecioneaopcao());
		aberturadeconta.setOptdeclaracaofatcacrs2(requisicao.getOptdeclaracaofatcacrs2());
		aberturadeconta.setNumnumerotin2(requisicao.getNumnumerotin2());
		aberturadeconta.setCmbdeclaracaodepropsitosedanaturezadacontadepsito2(
				requisicao.getCmbdeclaracaodepropsitosedanaturezadacontadepsito2());
		aberturadeconta.setMskassinaturadocliente2(requisicao.getMskassinaturadocliente2());
		aberturadeconta.setMskinformeasenha(requisicao.getMskinformeasenha());
		aberturadeconta.setMskinformeasenha2(requisicao.getMskinformeasenha2());
		aberturadeconta.setImgbarra05png(requisicao.getImgbarra05png());
		aberturadeconta.setImgbarra06png(requisicao.getImgbarra06png());
		aberturadeconta.setImgbarra06png(requisicao.getImgbarra06png());
		aberturadeconta.setChkselecioneasopces(requisicao.getChkselecioneasopces());
		aberturadeconta.setImgbarra06png(requisicao.getImgbarra06png());
		aberturadeconta.setImgbarra06png(requisicao.getImgbarra06png());
		aberturadeconta.setChkoperacaocomercial(requisicao.getChkoperacaocomercial());
		aberturadeconta.setImgbarra07png(requisicao.getImgbarra07png());
		aberturadeconta.setChkselecioneasopces8(requisicao.getChkselecioneasopces8());
		aberturadeconta.setCmbmodalidadedacesta2(requisicao.getCmbmodalidadedacesta2());
		aberturadeconta.setNumdiadodbito(requisicao.getNumdiadodbito());
		aberturadeconta.setOptpacotedescontocestadeservicos2(requisicao.getOptpacotedescontocestadeservicos2());
		aberturadeconta.setChkselecioneasopces(requisicao.getChkselecioneasopces());
		aberturadeconta.setChkselecioneasopces7(requisicao.getChkselecioneasopces7());
		aberturadeconta.setNumlimitecontratado3(requisicao.getNumlimitecontratado3());
		aberturadeconta.setNumdatadedbitojuros(requisicao.getNumdatadedbitojuros());
		aberturadeconta.setChkselecioneasopces3(requisicao.getChkselecioneasopces3());
		aberturadeconta.setChkselecioneasopces5(requisicao.getChkselecioneasopces5());
		aberturadeconta.setNumprestacaomaximaadmitida2(requisicao.getNumprestacaomaximaadmitida2());
		aberturadeconta.setChkselecioneasopces6(requisicao.getChkselecioneasopces6());
		aberturadeconta.setNumlimitecontratado(requisicao.getNumlimitecontratado());
		aberturadeconta.setCmbtipodecartao2(requisicao.getCmbtipodecartao2());
		aberturadeconta.setCmbmodalidade2(requisicao.getCmbmodalidade2());
		aberturadeconta.setCmbbandeira2(requisicao.getCmbbandeira2());
		aberturadeconta.setCmbvariante2(requisicao.getCmbvariante2());
		aberturadeconta.setCmbdiadovencimentodafatura2(requisicao.getCmbdiadovencimentodafatura2());
		aberturadeconta.setOptenderecoparaentregadafatura2(requisicao.getOptenderecoparaentregadafatura2());
		aberturadeconta.setOptcontratasegurodocartao(requisicao.getOptcontratasegurodocartao());
		aberturadeconta.setOptprogramapontoscaixa2(requisicao.getOptprogramapontoscaixa2());
		aberturadeconta.setOptconcordaemparticipardecampanhasdeincentivodacaixacompremiacao2(
				requisicao.getOptconcordaemparticipardecampanhasdeincentivodacaixacompremiacao2());
		aberturadeconta.setChkselecioneasopces4(requisicao.getChkselecioneasopces4());
		aberturadeconta.setChkformularios(requisicao.getChkformularios());
		aberturadeconta.setMskassinaturadocliente(requisicao.getMskassinaturadocliente());
		aberturadeconta.setMskinformeasenha(requisicao.getMskinformeasenha());
		aberturadeconta.setMskinformeasenha2(requisicao.getMskinformeasenha2());
		aberturadeconta.setImgbarra08png(requisicao.getImgbarra08png());
		elementosAberturadeconta.remove(id);
		elementosAberturadeconta.put(id, aberturadeconta);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_UPDATE_SUCESSO);
		AberturadecontaRetorno retorno = new AberturadecontaRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		retorno.getData().add(aberturadeconta);
		return retorno;
	}

	/**
	 * @Objetivo O metodo deleteAberturadeconta é utilizado para remover um elemento
	 *           AberturadecontaEntity existente na colecao de elementos.
	 * @param id
	 * @return retorno
	 */
	public AberturadecontaRetorno deleteAberturadeconta(Long id) {
		LOGGER.info(DossieConstantes.INFO_CHAMANDO_METODO_DELETE + id);
		elementosAberturadeconta.remove(id);
		List<String> msgsErro = new ArrayList<String>();
		msgsErro.add(DossieConstantes.INFO_EXCLUSAO_SUCESSO);
		AberturadecontaRetorno retorno = new AberturadecontaRetorno();
		retorno.setTemErro(Boolean.FALSE);
		retorno.setMsgsErro(msgsErro);
		return retorno;
	}

	public Retorno readAllRelatorios() {
		// TODO Auto-generated method stub
		return null;
	}
}
