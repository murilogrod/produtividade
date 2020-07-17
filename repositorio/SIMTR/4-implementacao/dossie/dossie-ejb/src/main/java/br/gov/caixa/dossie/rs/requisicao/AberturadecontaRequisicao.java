	package br.gov.caixa.dossie.rs.requisicao;
import java.util.ArrayList;
import java.util.HashMap;
/*
	Esta classe armazena a informacao recebida e enviada entre o webservice e a interface
*/
import java.util.List;
/**
 * @author SIOGP
 */
public class AberturadecontaRequisicao extends Requisicao {
	private static final long serialVersionUID = 3075165143170327078L;
	private Long id;
	private String imgBarra0png = "";
	private String cmbTipodeconta2 = "";
	private List<String> cmbTipodeconta2content = new ArrayList<String>();
	private String optModalidade = "";
	private String chkOpces2 = "";
	private String optMenoremancipado2 = "";
	private String optSolidaria = "";
	private String optPossui = "";
	private String numInformeocpf2 = "";
	private String btiLupapng2 = "";
	private String txtNome2 = "Nome de Entrada";
	private String cmbTipodeconta = "";
	private List<String> cmbTipodecontacontent = new ArrayList<String>();
	private String optModalidade2 = "";
	private String optSolidaria2 = "";
	private String optPossui2 = "";
	private String numInformeocpf5 = "";
	private String txtNome3 = "Nome de Entrada";
	private String numInformeocpf = "";
	private String btiLupapng3 = "";
	private String btiLupapng = "";
	private String txtNome4 = "Nome de Entrada";
	private String numInformeocpf3 = "";
	private String btiLupapng4 = "";
	private String txtNome = "Nome de Entrada";
	private String imgBarra2png = "";
    private List<String> tbl2content = new ArrayList<String>();
    private List<HashMap<String,String>> tbl2data = new ArrayList<HashMap<String,String>>();
	private String imgBarra3png = "";
	private String btiDocrendimentopng = "";
	private String btiDocresidenciapng = "";
	private String btiDocidentificacaopng = "";
	private String cmbNacionalidade2 = "";
	private List<String> cmbNacionalidade2content = new ArrayList<String>();
	private String cmbNaturalidade2 = "";
	private List<String> cmbNaturalidade2content = new ArrayList<String>();
	private String txtCidade = "Nome de Entrada";
	private String txtNomedopai2 = "Nome de Entrada";
	private String imgIdentidadejpg = "";
	private String numCep2 = "";
	private String cmbTipodelogradouro2 = "";
	private List<String> cmbTipodelogradouro2content = new ArrayList<String>();
	private String txtLogradouro = "Nome de Entrada";
	private String numNumero2 = "123456";
	private String txtComplemento2 = "Nome de Entrada";
	private String txtBairro2 = "Nome de Entrada";
	private String cmbUf2 = "";
	private List<String> cmbUf2content = new ArrayList<String>();
	private String imgCompresidenciajpg = "";
	private String txtLogradouro2 = "Nome de Entrada";
	private String cmbTipodefontepagadora = "";
	private List<String> cmbTipodefontepagadoracontent = new ArrayList<String>();
	private String numCnpj2 = "";
	private String txtNomedafontepagadora2 = "Nome de Entrada";
	private String numCoclidafontepagadora2 = "123456";
	private String cmbOcupacao = "";
	private List<String> cmbOcupacaocontent = new ArrayList<String>();
	private String dtDatadeadmissaoinicio2 = "05/04/2018";
	private String numRendabruta2 = "";
	private String numRendaliquida2 = "";
	private String numValordoimpostoretidonafonte2 = "";
	private String imgComprendajpg = "";
	private String imgBarra04png = "";
	private String imgBarra4png = "";
	private String mskSenha = "Rapoza Dois";
	private String txtRedigitacaosenha = "Nome de Entrada";
	private String imgBarra05png = "";
	private String cmbGraudeinstrucao = "";
	private List<String> cmbGraudeinstrucaocontent = new ArrayList<String>();
	private String cmbSexo2 = "";
	private List<String> cmbSexo2content = new ArrayList<String>();
	private String cmbProfissaocdigodaocupacao = "";
	private List<String> cmbProfissaocdigodaocupacaocontent = new ArrayList<String>();
	private String txtEnderecocomercial = "Nome de Entrada";
	private String cmbEstadocivil2 = "";
	private List<String> cmbEstadocivil2content = new ArrayList<String>();
	private String numCpfdoconjuge = "";
	private String imgLupapng2 = "";
	private String txtNomedoconjuge = "Nome de Entrada";
	private String dtDatadenascimentodoconjuge = "05/03/2018";
	private String cmbFinalidade = "";
	private List<String> cmbFinalidadecontent = new ArrayList<String>();
	private String cmbMeiosdecomunicacao = "";
	private List<String> cmbMeiosdecomunicacaocontent = new ArrayList<String>();
	private String numDddenumero = "";
	private String txtContato = "Nome de Entrada";
	private String chkSelecioneasopces2 = "";
	private String cmbSaquestransfernciaspagamentosagendamentosapartirde = "";
	private List<String> cmbSaquestransfernciaspagamentosagendamentosapartirdecontent = new ArrayList<String>();
	private String cmbComprascomcartaodedbitoapartirde = "";
	private List<String> cmbComprascomcartaodedbitoapartirdecontent = new ArrayList<String>();
	private String cmbDesejareceberinformacesdocartaodecrditocaixa2 = "";
	private List<String> cmbDesejareceberinformacesdocartaodecrditocaixa2content = new ArrayList<String>();
	private String cmbAutorizaoenviodeinformacesdofgts = "";
	private List<String> cmbAutorizaoenviodeinformacesdofgtscontent = new ArrayList<String>();
	private String cmbGostariaderecebermaisinformacessobreprodutoseservicosdacaixa2 = "";
	private List<String> cmbGostariaderecebermaisinformacessobreprodutoseservicosdacaixa2content = new ArrayList<String>();
	private String optSelecioneaopcao = "";
	private String optDeclaracaofatcacrs2 = "";
	private String numNumerotin2 = "123456";
	private String cmbDeclaracaodepropsitosedanaturezadacontadepsito2 = "";
	private List<String> cmbDeclaracaodepropsitosedanaturezadacontadepsito2content = new ArrayList<String>();
	private String mskAssinaturadocliente2 = "";
	private String mskInformeasenha = "Rapoza Dois";
	private String mskInformeasenha2 = "Rapoza Dois";
	private String imgBarra06png = "";
    private List<String> tblcontent = new ArrayList<String>();
    private List<HashMap<String,String>> tbldata = new ArrayList<HashMap<String,String>>();
	private String chkSelecioneasopces = "";
	private String chkOperacaocomercial = "";
	private String imgBarra07png = "";
	private String chkSelecioneasopces8 = "";
	private String cmbModalidadedacesta2 = "";
	private List<String> cmbModalidadedacesta2content = new ArrayList<String>();
	private String numDiadodbito = "";
	private String optPacotedescontocestadeservicos2 = "";
	private String chkSelecioneasopces7 = "";
	private String numLimitecontratado3 = "";
	private String numDatadedbitojuros = "123456";
	private String chkSelecioneasopces3 = "";
	private String chkSelecioneasopces5 = "";
	private String numPrestacaomaximaadmitida2 = "";
	private String chkSelecioneasopces6 = "";
	private String numLimitecontratado = "";
	private String cmbTipodecartao2 = "";
	private List<String> cmbTipodecartao2content = new ArrayList<String>();
	private String cmbModalidade2 = "";
	private List<String> cmbModalidade2content = new ArrayList<String>();
	private String cmbBandeira2 = "";
	private List<String> cmbBandeira2content = new ArrayList<String>();
	private String cmbVariante2 = "";
	private List<String> cmbVariante2content = new ArrayList<String>();
	private String cmbDiadovencimentodafatura2 = "";
	private List<String> cmbDiadovencimentodafatura2content = new ArrayList<String>();
	private String optEnderecoparaentregadafatura2 = "";
	private String optContratasegurodocartao = "";
	private String optProgramapontoscaixa2 = "";
	private String optConcordaemparticipardecampanhasdeincentivodacaixacompremiacao2 = "";
	private String chkSelecioneasopces4 = "";
	private String chkFormularios = "";
	private String mskAssinaturadocliente = "Rapoza Dois";
	private String imgBarra08png = "";
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public AberturadecontaRequisicao() {
		super();
        cmbTipodeconta2content.add("001 - Conta Corrente PF");
        cmbTipodeconta2content.add("003- Conta Corrente PJ");
        cmbTipodeconta2content.add("013 - Poupança");
         cmbTipodeconta2 = cmbTipodeconta2content.get(0);
        cmbTipodecontacontent.add("001 - Conta Corrente PF");
        cmbTipodecontacontent.add("003- Conta Corrente PJ");
        cmbTipodecontacontent.add("013 - Poupança");
         cmbTipodeconta = cmbTipodecontacontent.get(0);
		final HashMap<String,String> tbl2dataMap = new HashMap<String,String>();
    	tbl2content.add("Situação");
    	tbl2dataMap.put("Situação","valor");
    	tbl2content.add("Sistema");
    	tbl2dataMap.put("Sistema","valor");
    	tbl2content.add("Ocorrência");
    	tbl2dataMap.put("Ocorrência","valor");
    	tbl2content.add("Orientações");
    	tbl2dataMap.put("Orientações","valor");
        tbl2data.add(tbl2dataMap);
        tbl2data.add(tbl2dataMap);
        tbl2data.add(tbl2dataMap);
        tbl2data.add(tbl2dataMap);
        tbl2data.add(tbl2dataMap);
        cmbNacionalidade2content.add("Brasil");
         cmbNacionalidade2 = cmbNacionalidade2content.get(0);
        cmbNaturalidade2content.add("Bahia");
        cmbNaturalidade2content.add("Rio de Janeiro");
        cmbNaturalidade2content.add("São Paulo");
         cmbNaturalidade2 = cmbNaturalidade2content.get(0);
        cmbTipodelogradouro2content.add("Trincheira");
        cmbTipodelogradouro2content.add("Rua");
        cmbTipodelogradouro2content.add("Avenida");
        cmbTipodelogradouro2content.add("Travessa");
         cmbTipodelogradouro2 = cmbTipodelogradouro2content.get(0);
        cmbUf2content.add("Bahia");
        cmbUf2content.add("Rio de Janeiro");
        cmbUf2content.add("São Paulo");
         cmbUf2 = cmbUf2content.get(0);
        cmbTipodefontepagadoracontent.add("Jurídica");
         cmbTipodefontepagadora = cmbTipodefontepagadoracontent.get(0);
        cmbOcupacaocontent.add("Agente Administrativo");
         cmbOcupacao = cmbOcupacaocontent.get(0);
        cmbGraudeinstrucaocontent.add("Fundamental Incompleto");
        cmbGraudeinstrucaocontent.add("Fundamental Completo");
        cmbGraudeinstrucaocontent.add("Médio Completo");
        cmbGraudeinstrucaocontent.add("Médio Incompleto");
        cmbGraudeinstrucaocontent.add("Superior Completo");
        cmbGraudeinstrucaocontent.add("Superior Incompleto");
        cmbGraudeinstrucaocontent.add("Pós-Graduação");
        cmbGraudeinstrucaocontent.add("Mestrado");
        cmbGraudeinstrucaocontent.add("Doutorado");
         cmbGraudeinstrucao = cmbGraudeinstrucaocontent.get(0);
        cmbSexo2content.add("Feminino");
        cmbSexo2content.add("Masculino");
        cmbSexo2content.add("Outros");
         cmbSexo2 = cmbSexo2content.get(0);
        cmbProfissaocdigodaocupacaocontent.add("Advogado");
        cmbProfissaocdigodaocupacaocontent.add("Item 2");
        cmbProfissaocdigodaocupacaocontent.add("Item 3");
         cmbProfissaocdigodaocupacao = cmbProfissaocdigodaocupacaocontent.get(0);
        cmbEstadocivil2content.add("Casado");
        cmbEstadocivil2content.add("Com União Estável");
        cmbEstadocivil2content.add("Divorciado");
        cmbEstadocivil2content.add("Solteiro");
         cmbEstadocivil2 = cmbEstadocivil2content.get(0);
        cmbFinalidadecontent.add("Escolha");
         cmbFinalidade = cmbFinalidadecontent.get(0);
        cmbMeiosdecomunicacaocontent.add("Telefone Fixo");
        cmbMeiosdecomunicacaocontent.add("Celular");
        cmbMeiosdecomunicacaocontent.add("Email");
         cmbMeiosdecomunicacao = cmbMeiosdecomunicacaocontent.get(0);
        cmbSaquestransfernciaspagamentosagendamentosapartirdecontent.add("R$50,00");
        cmbSaquestransfernciaspagamentosagendamentosapartirdecontent.add("R$100,00");
         cmbSaquestransfernciaspagamentosagendamentosapartirde = cmbSaquestransfernciaspagamentosagendamentosapartirdecontent.get(0);
        cmbComprascomcartaodedbitoapartirdecontent.add("R$50,00");
        cmbComprascomcartaodedbitoapartirdecontent.add("R$100,00");
         cmbComprascomcartaodedbitoapartirde = cmbComprascomcartaodedbitoapartirdecontent.get(0);
        cmbDesejareceberinformacesdocartaodecrditocaixa2content.add("Sim");
        cmbDesejareceberinformacesdocartaodecrditocaixa2content.add("Não");
         cmbDesejareceberinformacesdocartaodecrditocaixa2 = cmbDesejareceberinformacesdocartaodecrditocaixa2content.get(0);
        cmbAutorizaoenviodeinformacesdofgtscontent.add("Sim");
        cmbAutorizaoenviodeinformacesdofgtscontent.add("Não");
        cmbAutorizaoenviodeinformacesdofgtscontent.add("Item 3");
         cmbAutorizaoenviodeinformacesdofgts = cmbAutorizaoenviodeinformacesdofgtscontent.get(0);
        cmbGostariaderecebermaisinformacessobreprodutoseservicosdacaixa2content.add("Sim");
        cmbGostariaderecebermaisinformacessobreprodutoseservicosdacaixa2content.add("Não");
         cmbGostariaderecebermaisinformacessobreprodutoseservicosdacaixa2 = cmbGostariaderecebermaisinformacessobreprodutoseservicosdacaixa2content.get(0);
        cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.add("0001 Conta de Depósito/Poupança/Caução/Conta Vinculada/Judicial");
        cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.add("0002 Empréstimos/Financiamentos");
        cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.add("0003 Investimentos");
        cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.add("0004 Cartão de Crédito");
        cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.add("0005 Seguros/Previdência Privada/Capitalização/Consórcios");
        cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.add("0006 Operações Internacionais/Câmbio");
        cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.add("0012 Sócio/Administrador/Controlador/Diretor");
        cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.add("0013 Procurador/Tutor/Curador");
         cmbDeclaracaodepropsitosedanaturezadacontadepsito2 = cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.get(0);
		final HashMap<String,String> tbldataMap = new HashMap<String,String>();
    	tblcontent.add("Data");
    	tbldataMap.put("Data","valor");
    	tblcontent.add("Código");
    	tbldataMap.put("Código","valor");
    	tblcontent.add("Tipo da Avaliação");
    	tbldataMap.put("Tipo da Avaliação","valor");
    	tblcontent.add("Situação Proposta");
    	tbldataMap.put("Situação Proposta","valor");
        tbldata.add(tbldataMap);
        tbldata.add(tbldataMap);
        tbldata.add(tbldataMap);
        tbldata.add(tbldataMap);
        tbldata.add(tbldataMap);
        cmbModalidadedacesta2content.add("Negócios Integrasos");
        cmbModalidadedacesta2content.add("Item 2");
        cmbModalidadedacesta2content.add("Item 3");
         cmbModalidadedacesta2 = cmbModalidadedacesta2content.get(0);
        cmbTipodecartao2content.add("Múltiplo");
        cmbTipodecartao2content.add("Crédito");
        cmbTipodecartao2content.add("Débito");
         cmbTipodecartao2 = cmbTipodecartao2content.get(0);
        cmbModalidade2content.add("Escolha");
         cmbModalidade2 = cmbModalidade2content.get(0);
        cmbBandeira2content.add("Escolha");
         cmbBandeira2 = cmbBandeira2content.get(0);
        cmbVariante2content.add("Escolha");
         cmbVariante2 = cmbVariante2content.get(0);
        cmbDiadovencimentodafatura2content.add("01");
        cmbDiadovencimentodafatura2content.add("05");
        cmbDiadovencimentodafatura2content.add("10");
         cmbDiadovencimentodafatura2 = cmbDiadovencimentodafatura2content.get(0);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImgbarra0png() {
		return imgBarra0png;
	}
	public void setImgbarra0png(String valor) {
		imgBarra0png = valor;
	}
	public String getCmbtipodeconta2() {
		return cmbTipodeconta2;
	}
	public void setCmbtipodeconta2(String valor) {
		cmbTipodeconta2 = valor;
	}
	public List<String> getCmbtipodeconta2Content() {
        return cmbTipodeconta2content;
	}
	public void setCmbtipodeconta2Content(List<String> lista) {
		this.cmbTipodeconta2content = lista;
	}	
	public String getOptmodalidade() {
		return optModalidade;
	}
	public void setOptmodalidade(String valor) {
		optModalidade = valor;
	}
	public String getChkopces2() {
		return chkOpces2;
	}
	public void setChkopces2(String valor) {
		chkOpces2 = valor;
	}
	public String getOptmenoremancipado2() {
		return optMenoremancipado2;
	}
	public void setOptmenoremancipado2(String valor) {
		optMenoremancipado2 = valor;
	}
	public String getOptsolidaria() {
		return optSolidaria;
	}
	public void setOptsolidaria(String valor) {
		optSolidaria = valor;
	}
	public String getOptpossui() {
		return optPossui;
	}
	public void setOptpossui(String valor) {
		optPossui = valor;
	}
	public String getNuminformeocpf2() {
		return numInformeocpf2;
	}
	public void setNuminformeocpf2(String valor) {
		numInformeocpf2 = valor;
	}
	public String getBtilupapng2() {
		return btiLupapng2;
	}
	public void setBtilupapng2(String valor) {
		btiLupapng2 = valor;
	}
	public String getTxtnome2() {
		return txtNome2;
	}
	public void setTxtnome2(String valor) {
		txtNome2 = valor;
	}
	public String getCmbtipodeconta() {
		return cmbTipodeconta;
	}
	public void setCmbtipodeconta(String valor) {
		cmbTipodeconta = valor;
	}
	public List<String> getCmbtipodecontaContent() {
        return cmbTipodecontacontent;
	}
	public void setCmbtipodecontaContent(List<String> lista) {
		this.cmbTipodecontacontent = lista;
	}	
	public String getOptmodalidade2() {
		return optModalidade2;
	}
	public void setOptmodalidade2(String valor) {
		optModalidade2 = valor;
	}
	public String getOptsolidaria2() {
		return optSolidaria2;
	}
	public void setOptsolidaria2(String valor) {
		optSolidaria2 = valor;
	}
	public String getOptpossui2() {
		return optPossui2;
	}
	public void setOptpossui2(String valor) {
		optPossui2 = valor;
	}
	public String getNuminformeocpf5() {
		return numInformeocpf5;
	}
	public void setNuminformeocpf5(String valor) {
		numInformeocpf5 = valor;
	}
	public String getTxtnome3() {
		return txtNome3;
	}
	public void setTxtnome3(String valor) {
		txtNome3 = valor;
	}
	public String getNuminformeocpf() {
		return numInformeocpf;
	}
	public void setNuminformeocpf(String valor) {
		numInformeocpf = valor;
	}
	public String getBtilupapng3() {
		return btiLupapng3;
	}
	public void setBtilupapng3(String valor) {
		btiLupapng3 = valor;
	}
	public String getBtilupapng() {
		return btiLupapng;
	}
	public void setBtilupapng(String valor) {
		btiLupapng = valor;
	}
	public String getTxtnome4() {
		return txtNome4;
	}
	public void setTxtnome4(String valor) {
		txtNome4 = valor;
	}
	public String getNuminformeocpf3() {
		return numInformeocpf3;
	}
	public void setNuminformeocpf3(String valor) {
		numInformeocpf3 = valor;
	}
	public String getBtilupapng4() {
		return btiLupapng4;
	}
	public void setBtilupapng4(String valor) {
		btiLupapng4 = valor;
	}
	public String getTxtnome() {
		return txtNome;
	}
	public void setTxtnome(String valor) {
		txtNome = valor;
	}
	public String getImgbarra2png() {
		return imgBarra2png;
	}
	public void setImgbarra2png(String valor) {
		imgBarra2png = valor;
	}
	public List<String> getTbl2() {
        return tbl2content;
	}
	public void setTbl2(List<String> lista) {
		this.tbl2content = lista;
	}	
	public List<HashMap<String,String>> getTbl2Data() {
        return tbl2data;
	}
	public void setTbl2Data(List<HashMap<String,String>> lista) {
		this.tbl2data = lista;
	}
	public String getImgbarra3png() {
		return imgBarra3png;
	}
	public void setImgbarra3png(String valor) {
		imgBarra3png = valor;
	}
	public String getBtidocrendimentopng() {
		return btiDocrendimentopng;
	}
	public void setBtidocrendimentopng(String valor) {
		btiDocrendimentopng = valor;
	}
	public String getBtidocresidenciapng() {
		return btiDocresidenciapng;
	}
	public void setBtidocresidenciapng(String valor) {
		btiDocresidenciapng = valor;
	}
	public String getBtidocidentificacaopng() {
		return btiDocidentificacaopng;
	}
	public void setBtidocidentificacaopng(String valor) {
		btiDocidentificacaopng = valor;
	}
	public String getCmbnacionalidade2() {
		return cmbNacionalidade2;
	}
	public void setCmbnacionalidade2(String valor) {
		cmbNacionalidade2 = valor;
	}
	public List<String> getCmbnacionalidade2Content() {
        return cmbNacionalidade2content;
	}
	public void setCmbnacionalidade2Content(List<String> lista) {
		this.cmbNacionalidade2content = lista;
	}	
	public String getCmbnaturalidade2() {
		return cmbNaturalidade2;
	}
	public void setCmbnaturalidade2(String valor) {
		cmbNaturalidade2 = valor;
	}
	public List<String> getCmbnaturalidade2Content() {
        return cmbNaturalidade2content;
	}
	public void setCmbnaturalidade2Content(List<String> lista) {
		this.cmbNaturalidade2content = lista;
	}	
	public String getTxtcidade() {
		return txtCidade;
	}
	public void setTxtcidade(String valor) {
		txtCidade = valor;
	}
	public String getTxtnomedopai2() {
		return txtNomedopai2;
	}
	public void setTxtnomedopai2(String valor) {
		txtNomedopai2 = valor;
	}
	public String getImgidentidadejpg() {
		return imgIdentidadejpg;
	}
	public void setImgidentidadejpg(String valor) {
		imgIdentidadejpg = valor;
	}
	public String getNumcep2() {
		return numCep2;
	}
	public void setNumcep2(String valor) {
		numCep2 = valor;
	}
	public String getCmbtipodelogradouro2() {
		return cmbTipodelogradouro2;
	}
	public void setCmbtipodelogradouro2(String valor) {
		cmbTipodelogradouro2 = valor;
	}
	public List<String> getCmbtipodelogradouro2Content() {
        return cmbTipodelogradouro2content;
	}
	public void setCmbtipodelogradouro2Content(List<String> lista) {
		this.cmbTipodelogradouro2content = lista;
	}	
	public String getTxtlogradouro() {
		return txtLogradouro;
	}
	public void setTxtlogradouro(String valor) {
		txtLogradouro = valor;
	}
	public String getNumnumero2() {
		return numNumero2;
	}
	public void setNumnumero2(String valor) {
		numNumero2 = valor;
	}
	public String getTxtcomplemento2() {
		return txtComplemento2;
	}
	public void setTxtcomplemento2(String valor) {
		txtComplemento2 = valor;
	}
	public String getTxtbairro2() {
		return txtBairro2;
	}
	public void setTxtbairro2(String valor) {
		txtBairro2 = valor;
	}
	public String getCmbuf2() {
		return cmbUf2;
	}
	public void setCmbuf2(String valor) {
		cmbUf2 = valor;
	}
	public List<String> getCmbuf2Content() {
        return cmbUf2content;
	}
	public void setCmbuf2Content(List<String> lista) {
		this.cmbUf2content = lista;
	}	
	public String getImgcompresidenciajpg() {
		return imgCompresidenciajpg;
	}
	public void setImgcompresidenciajpg(String valor) {
		imgCompresidenciajpg = valor;
	}
	public String getTxtlogradouro2() {
		return txtLogradouro2;
	}
	public void setTxtlogradouro2(String valor) {
		txtLogradouro2 = valor;
	}
	public String getCmbtipodefontepagadora() {
		return cmbTipodefontepagadora;
	}
	public void setCmbtipodefontepagadora(String valor) {
		cmbTipodefontepagadora = valor;
	}
	public List<String> getCmbtipodefontepagadoraContent() {
        return cmbTipodefontepagadoracontent;
	}
	public void setCmbtipodefontepagadoraContent(List<String> lista) {
		this.cmbTipodefontepagadoracontent = lista;
	}	
	public String getNumcnpj2() {
		return numCnpj2;
	}
	public void setNumcnpj2(String valor) {
		numCnpj2 = valor;
	}
	public String getTxtnomedafontepagadora2() {
		return txtNomedafontepagadora2;
	}
	public void setTxtnomedafontepagadora2(String valor) {
		txtNomedafontepagadora2 = valor;
	}
	public String getNumcoclidafontepagadora2() {
		return numCoclidafontepagadora2;
	}
	public void setNumcoclidafontepagadora2(String valor) {
		numCoclidafontepagadora2 = valor;
	}
	public String getCmbocupacao() {
		return cmbOcupacao;
	}
	public void setCmbocupacao(String valor) {
		cmbOcupacao = valor;
	}
	public List<String> getCmbocupacaoContent() {
        return cmbOcupacaocontent;
	}
	public void setCmbocupacaoContent(List<String> lista) {
		this.cmbOcupacaocontent = lista;
	}	
	public String getDtdatadeadmissaoinicio2() {
		return dtDatadeadmissaoinicio2;
	}
	public void setDtdatadeadmissaoinicio2(String valor) {
		dtDatadeadmissaoinicio2 = valor;
	}
	public String getNumrendabruta2() {
		return numRendabruta2;
	}
	public void setNumrendabruta2(String valor) {
		numRendabruta2 = valor;
	}
	public String getNumrendaliquida2() {
		return numRendaliquida2;
	}
	public void setNumrendaliquida2(String valor) {
		numRendaliquida2 = valor;
	}
	public String getNumvalordoimpostoretidonafonte2() {
		return numValordoimpostoretidonafonte2;
	}
	public void setNumvalordoimpostoretidonafonte2(String valor) {
		numValordoimpostoretidonafonte2 = valor;
	}
	public String getImgcomprendajpg() {
		return imgComprendajpg;
	}
	public void setImgcomprendajpg(String valor) {
		imgComprendajpg = valor;
	}
	public String getImgbarra04png() {
		return imgBarra04png;
	}
	public void setImgbarra04png(String valor) {
		imgBarra04png = valor;
	}
	public String getImgbarra4png() {
		return imgBarra4png;
	}
	public void setImgbarra4png(String valor) {
		imgBarra4png = valor;
	}
	public String getMsksenha() {
		return mskSenha;
	}
	public void setMsksenha(String valor) {
		mskSenha = valor;
	}
	public String getTxtredigitacaosenha() {
		return txtRedigitacaosenha;
	}
	public void setTxtredigitacaosenha(String valor) {
		txtRedigitacaosenha = valor;
	}
	public String getImgbarra05png() {
		return imgBarra05png;
	}
	public void setImgbarra05png(String valor) {
		imgBarra05png = valor;
	}
	public String getCmbgraudeinstrucao() {
		return cmbGraudeinstrucao;
	}
	public void setCmbgraudeinstrucao(String valor) {
		cmbGraudeinstrucao = valor;
	}
	public List<String> getCmbgraudeinstrucaoContent() {
        return cmbGraudeinstrucaocontent;
	}
	public void setCmbgraudeinstrucaoContent(List<String> lista) {
		this.cmbGraudeinstrucaocontent = lista;
	}	
	public String getCmbsexo2() {
		return cmbSexo2;
	}
	public void setCmbsexo2(String valor) {
		cmbSexo2 = valor;
	}
	public List<String> getCmbsexo2Content() {
        return cmbSexo2content;
	}
	public void setCmbsexo2Content(List<String> lista) {
		this.cmbSexo2content = lista;
	}	
	public String getCmbprofissaocdigodaocupacao() {
		return cmbProfissaocdigodaocupacao;
	}
	public void setCmbprofissaocdigodaocupacao(String valor) {
		cmbProfissaocdigodaocupacao = valor;
	}
	public List<String> getCmbprofissaocdigodaocupacaoContent() {
        return cmbProfissaocdigodaocupacaocontent;
	}
	public void setCmbprofissaocdigodaocupacaoContent(List<String> lista) {
		this.cmbProfissaocdigodaocupacaocontent = lista;
	}	
	public String getTxtenderecocomercial() {
		return txtEnderecocomercial;
	}
	public void setTxtenderecocomercial(String valor) {
		txtEnderecocomercial = valor;
	}
	public String getCmbestadocivil2() {
		return cmbEstadocivil2;
	}
	public void setCmbestadocivil2(String valor) {
		cmbEstadocivil2 = valor;
	}
	public List<String> getCmbestadocivil2Content() {
        return cmbEstadocivil2content;
	}
	public void setCmbestadocivil2Content(List<String> lista) {
		this.cmbEstadocivil2content = lista;
	}	
	public String getNumcpfdoconjuge() {
		return numCpfdoconjuge;
	}
	public void setNumcpfdoconjuge(String valor) {
		numCpfdoconjuge = valor;
	}
	public String getImglupapng2() {
		return imgLupapng2;
	}
	public void setImglupapng2(String valor) {
		imgLupapng2 = valor;
	}
	public String getTxtnomedoconjuge() {
		return txtNomedoconjuge;
	}
	public void setTxtnomedoconjuge(String valor) {
		txtNomedoconjuge = valor;
	}
	public String getDtdatadenascimentodoconjuge() {
		return dtDatadenascimentodoconjuge;
	}
	public void setDtdatadenascimentodoconjuge(String valor) {
		dtDatadenascimentodoconjuge = valor;
	}
	public String getCmbfinalidade() {
		return cmbFinalidade;
	}
	public void setCmbfinalidade(String valor) {
		cmbFinalidade = valor;
	}
	public List<String> getCmbfinalidadeContent() {
        return cmbFinalidadecontent;
	}
	public void setCmbfinalidadeContent(List<String> lista) {
		this.cmbFinalidadecontent = lista;
	}	
	public String getCmbmeiosdecomunicacao() {
		return cmbMeiosdecomunicacao;
	}
	public void setCmbmeiosdecomunicacao(String valor) {
		cmbMeiosdecomunicacao = valor;
	}
	public List<String> getCmbmeiosdecomunicacaoContent() {
        return cmbMeiosdecomunicacaocontent;
	}
	public void setCmbmeiosdecomunicacaoContent(List<String> lista) {
		this.cmbMeiosdecomunicacaocontent = lista;
	}	
	public String getNumdddenumero() {
		return numDddenumero;
	}
	public void setNumdddenumero(String valor) {
		numDddenumero = valor;
	}
	public String getTxtcontato() {
		return txtContato;
	}
	public void setTxtcontato(String valor) {
		txtContato = valor;
	}
	public String getChkselecioneasopces2() {
		return chkSelecioneasopces2;
	}
	public void setChkselecioneasopces2(String valor) {
		chkSelecioneasopces2 = valor;
	}
	public String getCmbsaquestransfernciaspagamentosagendamentosapartirde() {
		return cmbSaquestransfernciaspagamentosagendamentosapartirde;
	}
	public void setCmbsaquestransfernciaspagamentosagendamentosapartirde(String valor) {
		cmbSaquestransfernciaspagamentosagendamentosapartirde = valor;
	}
	public List<String> getCmbsaquestransfernciaspagamentosagendamentosapartirdeContent() {
        return cmbSaquestransfernciaspagamentosagendamentosapartirdecontent;
	}
	public void setCmbsaquestransfernciaspagamentosagendamentosapartirdeContent(List<String> lista) {
		this.cmbSaquestransfernciaspagamentosagendamentosapartirdecontent = lista;
	}	
	public String getCmbcomprascomcartaodedbitoapartirde() {
		return cmbComprascomcartaodedbitoapartirde;
	}
	public void setCmbcomprascomcartaodedbitoapartirde(String valor) {
		cmbComprascomcartaodedbitoapartirde = valor;
	}
	public List<String> getCmbcomprascomcartaodedbitoapartirdeContent() {
        return cmbComprascomcartaodedbitoapartirdecontent;
	}
	public void setCmbcomprascomcartaodedbitoapartirdeContent(List<String> lista) {
		this.cmbComprascomcartaodedbitoapartirdecontent = lista;
	}	
	public String getCmbdesejareceberinformacesdocartaodecrditocaixa2() {
		return cmbDesejareceberinformacesdocartaodecrditocaixa2;
	}
	public void setCmbdesejareceberinformacesdocartaodecrditocaixa2(String valor) {
		cmbDesejareceberinformacesdocartaodecrditocaixa2 = valor;
	}
	public List<String> getCmbdesejareceberinformacesdocartaodecrditocaixa2Content() {
        return cmbDesejareceberinformacesdocartaodecrditocaixa2content;
	}
	public void setCmbdesejareceberinformacesdocartaodecrditocaixa2Content(List<String> lista) {
		this.cmbDesejareceberinformacesdocartaodecrditocaixa2content = lista;
	}	
	public String getCmbautorizaoenviodeinformacesdofgts() {
		return cmbAutorizaoenviodeinformacesdofgts;
	}
	public void setCmbautorizaoenviodeinformacesdofgts(String valor) {
		cmbAutorizaoenviodeinformacesdofgts = valor;
	}
	public List<String> getCmbautorizaoenviodeinformacesdofgtsContent() {
        return cmbAutorizaoenviodeinformacesdofgtscontent;
	}
	public void setCmbautorizaoenviodeinformacesdofgtsContent(List<String> lista) {
		this.cmbAutorizaoenviodeinformacesdofgtscontent = lista;
	}	
	public String getCmbgostariaderecebermaisinformacessobreprodutoseservicosdacaixa2() {
		return cmbGostariaderecebermaisinformacessobreprodutoseservicosdacaixa2;
	}
	public void setCmbgostariaderecebermaisinformacessobreprodutoseservicosdacaixa2(String valor) {
		cmbGostariaderecebermaisinformacessobreprodutoseservicosdacaixa2 = valor;
	}
	public List<String> getCmbgostariaderecebermaisinformacessobreprodutoseservicosdacaixa2Content() {
        return cmbGostariaderecebermaisinformacessobreprodutoseservicosdacaixa2content;
	}
	public void setCmbgostariaderecebermaisinformacessobreprodutoseservicosdacaixa2Content(List<String> lista) {
		this.cmbGostariaderecebermaisinformacessobreprodutoseservicosdacaixa2content = lista;
	}	
	public String getOptselecioneaopcao() {
		return optSelecioneaopcao;
	}
	public void setOptselecioneaopcao(String valor) {
		optSelecioneaopcao = valor;
	}
	public String getOptdeclaracaofatcacrs2() {
		return optDeclaracaofatcacrs2;
	}
	public void setOptdeclaracaofatcacrs2(String valor) {
		optDeclaracaofatcacrs2 = valor;
	}
	public String getNumnumerotin2() {
		return numNumerotin2;
	}
	public void setNumnumerotin2(String valor) {
		numNumerotin2 = valor;
	}
	public String getCmbdeclaracaodepropsitosedanaturezadacontadepsito2() {
		return cmbDeclaracaodepropsitosedanaturezadacontadepsito2;
	}
	public void setCmbdeclaracaodepropsitosedanaturezadacontadepsito2(String valor) {
		cmbDeclaracaodepropsitosedanaturezadacontadepsito2 = valor;
	}
	public List<String> getCmbdeclaracaodepropsitosedanaturezadacontadepsito2Content() {
        return cmbDeclaracaodepropsitosedanaturezadacontadepsito2content;
	}
	public void setCmbdeclaracaodepropsitosedanaturezadacontadepsito2Content(List<String> lista) {
		this.cmbDeclaracaodepropsitosedanaturezadacontadepsito2content = lista;
	}	
	public String getMskassinaturadocliente2() {
		return mskAssinaturadocliente2;
	}
	public void setMskassinaturadocliente2(String valor) {
		mskAssinaturadocliente2 = valor;
	}
	public String getMskinformeasenha() {
		return mskInformeasenha;
	}
	public void setMskinformeasenha(String valor) {
		mskInformeasenha = valor;
	}
	public String getMskinformeasenha2() {
		return mskInformeasenha2;
	}
	public void setMskinformeasenha2(String valor) {
		mskInformeasenha2 = valor;
	}
	public String getImgbarra06png() {
		return imgBarra06png;
	}
	public void setImgbarra06png(String valor) {
		imgBarra06png = valor;
	}
	public List<String> getTbl() {
        return tblcontent;
	}
	public void setTbl(List<String> lista) {
		this.tblcontent = lista;
	}	
	public List<HashMap<String,String>> getTblData() {
        return tbldata;
	}
	public void setTblData(List<HashMap<String,String>> lista) {
		this.tbldata = lista;
	}
	public String getChkselecioneasopces() {
		return chkSelecioneasopces;
	}
	public void setChkselecioneasopces(String valor) {
		chkSelecioneasopces = valor;
	}
	public String getChkoperacaocomercial() {
		return chkOperacaocomercial;
	}
	public void setChkoperacaocomercial(String valor) {
		chkOperacaocomercial = valor;
	}
	public String getImgbarra07png() {
		return imgBarra07png;
	}
	public void setImgbarra07png(String valor) {
		imgBarra07png = valor;
	}
	public String getChkselecioneasopces8() {
		return chkSelecioneasopces8;
	}
	public void setChkselecioneasopces8(String valor) {
		chkSelecioneasopces8 = valor;
	}
	public String getCmbmodalidadedacesta2() {
		return cmbModalidadedacesta2;
	}
	public void setCmbmodalidadedacesta2(String valor) {
		cmbModalidadedacesta2 = valor;
	}
	public List<String> getCmbmodalidadedacesta2Content() {
        return cmbModalidadedacesta2content;
	}
	public void setCmbmodalidadedacesta2Content(List<String> lista) {
		this.cmbModalidadedacesta2content = lista;
	}	
	public String getNumdiadodbito() {
		return numDiadodbito;
	}
	public void setNumdiadodbito(String valor) {
		numDiadodbito = valor;
	}
	public String getOptpacotedescontocestadeservicos2() {
		return optPacotedescontocestadeservicos2;
	}
	public void setOptpacotedescontocestadeservicos2(String valor) {
		optPacotedescontocestadeservicos2 = valor;
	}
	public String getChkselecioneasopces7() {
		return chkSelecioneasopces7;
	}
	public void setChkselecioneasopces7(String valor) {
		chkSelecioneasopces7 = valor;
	}
	public String getNumlimitecontratado3() {
		return numLimitecontratado3;
	}
	public void setNumlimitecontratado3(String valor) {
		numLimitecontratado3 = valor;
	}
	public String getNumdatadedbitojuros() {
		return numDatadedbitojuros;
	}
	public void setNumdatadedbitojuros(String valor) {
		numDatadedbitojuros = valor;
	}
	public String getChkselecioneasopces3() {
		return chkSelecioneasopces3;
	}
	public void setChkselecioneasopces3(String valor) {
		chkSelecioneasopces3 = valor;
	}
	public String getChkselecioneasopces5() {
		return chkSelecioneasopces5;
	}
	public void setChkselecioneasopces5(String valor) {
		chkSelecioneasopces5 = valor;
	}
	public String getNumprestacaomaximaadmitida2() {
		return numPrestacaomaximaadmitida2;
	}
	public void setNumprestacaomaximaadmitida2(String valor) {
		numPrestacaomaximaadmitida2 = valor;
	}
	public String getChkselecioneasopces6() {
		return chkSelecioneasopces6;
	}
	public void setChkselecioneasopces6(String valor) {
		chkSelecioneasopces6 = valor;
	}
	public String getNumlimitecontratado() {
		return numLimitecontratado;
	}
	public void setNumlimitecontratado(String valor) {
		numLimitecontratado = valor;
	}
	public String getCmbtipodecartao2() {
		return cmbTipodecartao2;
	}
	public void setCmbtipodecartao2(String valor) {
		cmbTipodecartao2 = valor;
	}
	public List<String> getCmbtipodecartao2Content() {
        return cmbTipodecartao2content;
	}
	public void setCmbtipodecartao2Content(List<String> lista) {
		this.cmbTipodecartao2content = lista;
	}	
	public String getCmbmodalidade2() {
		return cmbModalidade2;
	}
	public void setCmbmodalidade2(String valor) {
		cmbModalidade2 = valor;
	}
	public List<String> getCmbmodalidade2Content() {
        return cmbModalidade2content;
	}
	public void setCmbmodalidade2Content(List<String> lista) {
		this.cmbModalidade2content = lista;
	}	
	public String getCmbbandeira2() {
		return cmbBandeira2;
	}
	public void setCmbbandeira2(String valor) {
		cmbBandeira2 = valor;
	}
	public List<String> getCmbbandeira2Content() {
        return cmbBandeira2content;
	}
	public void setCmbbandeira2Content(List<String> lista) {
		this.cmbBandeira2content = lista;
	}	
	public String getCmbvariante2() {
		return cmbVariante2;
	}
	public void setCmbvariante2(String valor) {
		cmbVariante2 = valor;
	}
	public List<String> getCmbvariante2Content() {
        return cmbVariante2content;
	}
	public void setCmbvariante2Content(List<String> lista) {
		this.cmbVariante2content = lista;
	}	
	public String getCmbdiadovencimentodafatura2() {
		return cmbDiadovencimentodafatura2;
	}
	public void setCmbdiadovencimentodafatura2(String valor) {
		cmbDiadovencimentodafatura2 = valor;
	}
	public List<String> getCmbdiadovencimentodafatura2Content() {
        return cmbDiadovencimentodafatura2content;
	}
	public void setCmbdiadovencimentodafatura2Content(List<String> lista) {
		this.cmbDiadovencimentodafatura2content = lista;
	}	
	public String getOptenderecoparaentregadafatura2() {
		return optEnderecoparaentregadafatura2;
	}
	public void setOptenderecoparaentregadafatura2(String valor) {
		optEnderecoparaentregadafatura2 = valor;
	}
	public String getOptcontratasegurodocartao() {
		return optContratasegurodocartao;
	}
	public void setOptcontratasegurodocartao(String valor) {
		optContratasegurodocartao = valor;
	}
	public String getOptprogramapontoscaixa2() {
		return optProgramapontoscaixa2;
	}
	public void setOptprogramapontoscaixa2(String valor) {
		optProgramapontoscaixa2 = valor;
	}
	public String getOptconcordaemparticipardecampanhasdeincentivodacaixacompremiacao2() {
		return optConcordaemparticipardecampanhasdeincentivodacaixacompremiacao2;
	}
	public void setOptconcordaemparticipardecampanhasdeincentivodacaixacompremiacao2(String valor) {
		optConcordaemparticipardecampanhasdeincentivodacaixacompremiacao2 = valor;
	}
	public String getChkselecioneasopces4() {
		return chkSelecioneasopces4;
	}
	public void setChkselecioneasopces4(String valor) {
		chkSelecioneasopces4 = valor;
	}
	public String getChkformularios() {
		return chkFormularios;
	}
	public void setChkformularios(String valor) {
		chkFormularios = valor;
	}
	public String getMskassinaturadocliente() {
		return mskAssinaturadocliente;
	}
	public void setMskassinaturadocliente(String valor) {
		mskAssinaturadocliente = valor;
	}
	public String getImgbarra08png() {
		return imgBarra08png;
	}
	public void setImgbarra08png(String valor) {
		imgBarra08png = valor;
	}
}

