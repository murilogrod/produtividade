package br.gov.caixa.dossie.rs.entity;

import java.util.ArrayList;
import java.util.HashMap;
/*
	Esta classe a fronteira entre sua aplição e o seu banco de dados 	
	Aqui poderao ser implementadas todas as rotinas de persistencia necessarias a sua aplicacao
*/
import java.util.List;

/**
 * @author SIOGP
 */
public class AberturadecontaEntity implements BaseEntity {
    private static final long serialVersionUID = 6933641140920629711L;
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
    private String txtNome2 = "";
    private String cmbTipodeconta = "";
    private List<String> cmbTipodecontacontent = new ArrayList<String>();
    private String optModalidade2 = "";
    private String optSolidaria2 = "";
    private String optPossui2 = "";
    private String numInformeocpf5 = "";
    private String txtNome3 = "";
    private String numInformeocpf = "";
    private String btiLupapng3 = "";
    private String btiLupapng = "";
    private String txtNome4 = "";
    private String numInformeocpf3 = "";
    private String btiLupapng4 = "";
    private String txtNome = "";
    private String imgBarra2png = "";
    private List<String> tbl2content = new ArrayList<String>();
    private List<HashMap<String, String>> tbl2data = new ArrayList<HashMap<String, String>>();
    private String imgBarra3png = "";
    private String btiDocrendimentopng = "";
    private String btiDocresidenciapng = "";
    private String btiDocidentificacaopng = "";
    private String cmbNacionalidade2 = "";
    private List<String> cmbNacionalidade2content = new ArrayList<String>();
    private String cmbNaturalidade2 = "";
    private List<String> cmbNaturalidade2content = new ArrayList<String>();
    private String txtCidade = "";
    private String txtNomedopai2 = "";
    private String imgIdentidadejpg = "";
    private String numCep2 = "";
    private String cmbTipodelogradouro2 = "";
    private List<String> cmbTipodelogradouro2content = new ArrayList<String>();
    private String txtLogradouro = "";
    private String numNumero2 = "123456";
    private String txtComplemento2 = "";
    private String txtBairro2 = "";
    private String cmbUf2 = "";
    private List<String> cmbUf2content = new ArrayList<String>();
    private String imgCompresidenciajpg = "";
    private String txtLogradouro2 = "";
    private String cmbTipodefontepagadora = "";
    private List<String> cmbTipodefontepagadoracontent = new ArrayList<String>();
    private String numCnpj2 = "";
    private String txtNomedafontepagadora2 = "";
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
    private String txtRedigitacaosenha = "";
    private String imgBarra05png = "";
    private String cmbGraudeinstrucao = "";
    private List<String> cmbGraudeinstrucaocontent = new ArrayList<String>();
    private String cmbSexo2 = "";
    private List<String> cmbSexo2content = new ArrayList<String>();
    private String cmbProfissaocdigodaocupacao = "";
    private List<String> cmbProfissaocdigodaocupacaocontent = new ArrayList<String>();
    private String txtEnderecocomercial = "";
    private String cmbEstadocivil2 = "";
    private List<String> cmbEstadocivil2content = new ArrayList<String>();
    private String numCpfdoconjuge = "";
    private String imgLupapng2 = "";
    private String txtNomedoconjuge = "";
    private String dtDatadenascimentodoconjuge = "05/03/2018";
    private String cmbFinalidade = "";
    private List<String> cmbFinalidadecontent = new ArrayList<String>();
    private String cmbMeiosdecomunicacao = "";
    private List<String> cmbMeiosdecomunicacaocontent = new ArrayList<String>();
    private String numDddenumero = "";
    private String txtContato = "";
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
    private List<HashMap<String, String>> tbldata = new ArrayList<HashMap<String, String>>();
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
    private String numDatadedbitojuros = "";
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
    private List<String> cmbDeclaracaoCrs = new ArrayList<String>();

    /**
     * CONSTRUTOR PADRÃO
     */
    public AberturadecontaEntity() {
	super();
	cmbTipodeconta2content.add("001 - Conta Corrente PF");
	cmbTipodeconta2content.add("003- Conta Corrente PJ");
	cmbTipodeconta2content.add("013 - Poupança");
	cmbTipodeconta2 = cmbTipodeconta2content.get(0);
	cmbTipodecontacontent.add("001 - Conta Corrente PF");
	cmbTipodecontacontent.add("003- Conta Corrente PJ");
	cmbTipodecontacontent.add("013 - Poupança");
	cmbTipodeconta = cmbTipodecontacontent.get(0);
	tbl2content.add("Situação");
	tbl2content.add("Sistema");
	tbl2content.add("Ocorrência");
	tbl2content.add("Orientações");
	HashMap<String, String> tbl2content1 = new HashMap<String, String>();
	tbl2content1.put("Situação", "itemLinha1");
	tbl2content1.put("Sistema", "itemLinha1");
	tbl2content1.put("Ocorrência", "itemLinha1");
	tbl2content1.put("Orientações", "itemLinha1");
	tbl2data.add(tbl2content1);
	HashMap<String, String> tbl2content2 = new HashMap<String, String>();
	tbl2content2.put("Situação", "itemLinha2");
	tbl2content2.put("Sistema", "itemLinha2");
	tbl2content2.put("Ocorrência", "itemLinha2");
	tbl2content2.put("Orientações", "itemLinha2");
	tbl2data.add(tbl2content2);
	HashMap<String, String> tbl2content3 = new HashMap<String, String>();
	tbl2content3.put("Situação", "itemLinha3");
	tbl2content3.put("Sistema", "itemLinha3");
	tbl2content3.put("Ocorrência", "itemLinha3");
	tbl2content3.put("Orientações", "itemLinha3");
	tbl2data.add(tbl2content3);
	HashMap<String, String> tbl2content4 = new HashMap<String, String>();
	tbl2content4.put("Situação", "itemLinha4");
	tbl2content4.put("Sistema", "itemLinha4");
	tbl2content4.put("Ocorrência", "itemLinha4");
	tbl2content4.put("Orientações", "itemLinha4");
	tbl2data.add(tbl2content4);
	HashMap<String, String> tbl2content5 = new HashMap<String, String>();
	tbl2content5.put("Situação", "itemLinha5");
	tbl2content5.put("Sistema", "itemLinha5");
	tbl2content5.put("Ocorrência", "itemLinha5");
	tbl2content5.put("Orientações", "itemLinha5");
	tbl2data.add(tbl2content5);
	HashMap<String, String> tbl2content6 = new HashMap<String, String>();
	tbl2content6.put("Situação", "itemLinha6");
	tbl2content6.put("Sistema", "itemLinha6");
	tbl2content6.put("Ocorrência", "itemLinha6");
	tbl2content6.put("Orientações", "itemLinha6");
	tbl2data.add(tbl2content6);
	HashMap<String, String> tbl2content7 = new HashMap<String, String>();
	tbl2content7.put("Situação", "itemLinha7");
	tbl2content7.put("Sistema", "itemLinha7");
	tbl2content7.put("Ocorrência", "itemLinha7");
	tbl2content7.put("Orientações", "itemLinha7");
	tbl2data.add(tbl2content7);
	HashMap<String, String> tbl2content8 = new HashMap<String, String>();
	tbl2content8.put("Situação", "itemLinha8");
	tbl2content8.put("Sistema", "itemLinha8");
	tbl2content8.put("Ocorrência", "itemLinha8");
	tbl2content8.put("Orientações", "itemLinha8");
	tbl2data.add(tbl2content8);
	HashMap<String, String> tbl2content9 = new HashMap<String, String>();
	tbl2content9.put("Situação", "itemLinha9");
	tbl2content9.put("Sistema", "itemLinha9");
	tbl2content9.put("Ocorrência", "itemLinha9");
	tbl2content9.put("Orientações", "itemLinha9");
	tbl2data.add(tbl2content9);
	HashMap<String, String> tbl2content10 = new HashMap<String, String>();
	tbl2content10.put("Situação", "itemLinha10");
	tbl2content10.put("Sistema", "itemLinha10");
	tbl2content10.put("Ocorrência", "itemLinha10");
	tbl2content10.put("Orientações", "itemLinha10");
	tbl2data.add(tbl2content10);
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
	cmbProfissaocdigodaocupacaocontent.add("Administrador");
	cmbProfissaocdigodaocupacaocontent.add("Advogado");
	cmbProfissaocdigodaocupacaocontent.add("Economiário");
	cmbProfissaocdigodaocupacaocontent.add("Médico");
	cmbProfissaocdigodaocupacaocontent.add("Motorista");
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
	cmbAutorizaoenviodeinformacesdofgts = cmbAutorizaoenviodeinformacesdofgtscontent.get(0);
	cmbGostariaderecebermaisinformacessobreprodutoseservicosdacaixa2content.add("Sim");
	cmbGostariaderecebermaisinformacessobreprodutoseservicosdacaixa2content.add("Não");
	cmbGostariaderecebermaisinformacessobreprodutoseservicosdacaixa2 = cmbGostariaderecebermaisinformacessobreprodutoseservicosdacaixa2content
		.get(0);
	cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.add("0001 Conta de Depósito/Poupança/Caução/Conta Vinculada/Judicial");
	cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.add("0002 Empréstimos/Financiamentos");
	cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.add("0003 Investimentos");
	cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.add("0004 Cartão de Crédito");
	cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.add("0005 Seguros/Previdência Privada/Capitalização/Consórcios");
	cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.add("0006 Operações Internacionais/Câmbio");
	cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.add("0012 Sócio/Administrador/Controlador/Diretor");
	cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.add("0013 Procurador/Tutor/Curador");
	cmbDeclaracaodepropsitosedanaturezadacontadepsito2 = cmbDeclaracaodepropsitosedanaturezadacontadepsito2content.get(0);
	tblcontent.add("Data");
	tblcontent.add("Código");
	tblcontent.add("Tipo da Avaliação");
	tblcontent.add("Situação Proposta");
	HashMap<String, String> tblcontent1 = new HashMap<String, String>();
	tblcontent1.put("Data", "itemLinha1");
	tblcontent1.put("Código", "itemLinha1");
	tblcontent1.put("Tipo da Avaliação", "itemLinha1");
	tblcontent1.put("Situação Proposta", "itemLinha1");
	tbldata.add(tblcontent1);
	HashMap<String, String> tblcontent2 = new HashMap<String, String>();
	tblcontent2.put("Data", "itemLinha2");
	tblcontent2.put("Código", "itemLinha2");
	tblcontent2.put("Tipo da Avaliação", "itemLinha2");
	tblcontent2.put("Situação Proposta", "itemLinha2");
	tbldata.add(tblcontent2);
	HashMap<String, String> tblcontent3 = new HashMap<String, String>();
	tblcontent3.put("Data", "itemLinha3");
	tblcontent3.put("Código", "itemLinha3");
	tblcontent3.put("Tipo da Avaliação", "itemLinha3");
	tblcontent3.put("Situação Proposta", "itemLinha3");
	tbldata.add(tblcontent3);
	HashMap<String, String> tblcontent4 = new HashMap<String, String>();
	tblcontent4.put("Data", "itemLinha4");
	tblcontent4.put("Código", "itemLinha4");
	tblcontent4.put("Tipo da Avaliação", "itemLinha4");
	tblcontent4.put("Situação Proposta", "itemLinha4");
	tbldata.add(tblcontent4);
	HashMap<String, String> tblcontent5 = new HashMap<String, String>();
	tblcontent5.put("Data", "itemLinha5");
	tblcontent5.put("Código", "itemLinha5");
	tblcontent5.put("Tipo da Avaliação", "itemLinha5");
	tblcontent5.put("Situação Proposta", "itemLinha5");
	tbldata.add(tblcontent5);
	HashMap<String, String> tblcontent6 = new HashMap<String, String>();
	tblcontent6.put("Data", "itemLinha6");
	tblcontent6.put("Código", "itemLinha6");
	tblcontent6.put("Tipo da Avaliação", "itemLinha6");
	tblcontent6.put("Situação Proposta", "itemLinha6");
	tbldata.add(tblcontent6);
	HashMap<String, String> tblcontent7 = new HashMap<String, String>();
	tblcontent7.put("Data", "itemLinha7");
	tblcontent7.put("Código", "itemLinha7");
	tblcontent7.put("Tipo da Avaliação", "itemLinha7");
	tblcontent7.put("Situação Proposta", "itemLinha7");
	tbldata.add(tblcontent7);
	HashMap<String, String> tblcontent8 = new HashMap<String, String>();
	tblcontent8.put("Data", "itemLinha8");
	tblcontent8.put("Código", "itemLinha8");
	tblcontent8.put("Tipo da Avaliação", "itemLinha8");
	tblcontent8.put("Situação Proposta", "itemLinha8");
	tbldata.add(tblcontent8);
	HashMap<String, String> tblcontent9 = new HashMap<String, String>();
	tblcontent9.put("Data", "itemLinha9");
	tblcontent9.put("Código", "itemLinha9");
	tblcontent9.put("Tipo da Avaliação", "itemLinha9");
	tblcontent9.put("Situação Proposta", "itemLinha9");
	tbldata.add(tblcontent9);
	HashMap<String, String> tblcontent10 = new HashMap<String, String>();
	tblcontent10.put("Data", "itemLinha10");
	tblcontent10.put("Código", "itemLinha10");
	tblcontent10.put("Tipo da Avaliação", "itemLinha10");
	tblcontent10.put("Situação Proposta", "itemLinha10");
	tbldata.add(tblcontent10);
	cmbModalidadedacesta2content.add("CESTA FÁCIL CAIXA");
	cmbModalidadedacesta2content.add("CESTA SUPER CAIXA");
	cmbModalidadedacesta2content.add("CESTA CONVENCIONAL CAIXA");
	cmbModalidadedacesta2content.add("CESTA ESPECIAL CAIXA");
	cmbModalidadedacesta2content.add("CESTA UNIVERSITÁRIA CAIXA");
	cmbModalidadedacesta2content.add("CESTA PADRÃO I");
	cmbModalidadedacesta2content.add("CESTA PADRÃO II");
	cmbModalidadedacesta2content.add("CESTA PADRÃO III");
	cmbModalidadedacesta2content.add("CESTA PADRÃO IV");
	cmbModalidadedacesta2 = cmbModalidadedacesta2content.get(0);
	cmbTipodecartao2content.add("Múltiplo");
	cmbTipodecartao2content.add("Crédito");
	cmbTipodecartao2content.add("Débito");
	cmbTipodecartao2 = cmbTipodecartao2content.get(0);
	cmbModalidade2content.add("Normal");
	cmbModalidade2content.add("Turismo");
	cmbModalidade2 = cmbModalidade2content.get(0);
	cmbBandeira2content.add("Elo");
	cmbBandeira2content.add("Master");
	cmbBandeira2content.add("Visa");
	cmbBandeira2 = cmbBandeira2content.get(0);
	cmbVariante2content.add("Nacional");
	cmbVariante2content.add("Internacional");
	cmbVariante2content.add("Black");
	cmbVariante2content.add("Infinite");
	cmbVariante2content.add("Naquin");
	cmbVariante2 = cmbVariante2content.get(0);
	cmbDiadovencimentodafatura2content.add("01");
	cmbDiadovencimentodafatura2content.add("05");
	cmbDiadovencimentodafatura2content.add("10");
	cmbDiadovencimentodafatura2 = cmbDiadovencimentodafatura2content.get(0);
	cmbDeclaracaoCrs.add("0017 - PESSOA NORTE-AMERICA");
	cmbDeclaracaoCrs.add("0040 - CRS - OCDE - ALEMANHA");
	cmbDeclaracaoCrs.add("0041 - CRS - OCDE - AUSTRÁLIA");
	cmbDeclaracaoCrs.add("0042 - CRS - OCDE - ÁUSTRIA");
	cmbDeclaracaoCrs.add("0043 - CRS - OCDE - BÉLGICA");
	cmbDeclaracaoCrs.add("0044 - CRS - OCDE - CANADÁ");
	cmbDeclaracaoCrs.add("0045 - CRS - OCDE - CHILE");
	cmbDeclaracaoCrs.add("0046 - CRS - OCDE - COREIA DO SUL");
	cmbDeclaracaoCrs.add("0047 - CRS - OCDE - DINAMARCA");
	cmbDeclaracaoCrs.add("0048 - CRS - OCDE - ESLOVÁQUIA");
	cmbDeclaracaoCrs.add("0049 - CRS - OCDE - ESLOVÊNIA");
	cmbDeclaracaoCrs.add("0050 - CRS - OCDE - ESPANHA");
	cmbDeclaracaoCrs.add("0051 - CRS - OCDE - ESTÔNIA");
	cmbDeclaracaoCrs.add("0052 - CRS - OCDE - FINLÂNDIA");
	cmbDeclaracaoCrs.add("0053 - CRS - OCDE - FRANÇA");
	cmbDeclaracaoCrs.add("0054 - CRS - OCDE - GRÉCIA");
	cmbDeclaracaoCrs.add("0055 - CRS - OCDE - HUNGRIA");
	cmbDeclaracaoCrs.add("0056 - CRS - OCDE - IRLANDA");
	cmbDeclaracaoCrs.add("0057 - CRS - OCDE - ISLÂNDIA");
	cmbDeclaracaoCrs.add("0058 - CRS - OCDE - ISRAEL");
	cmbDeclaracaoCrs.add("0059 - CRS - OCDE - ITÁLIA");
	cmbDeclaracaoCrs.add("0060 - CRS - OCDE - JAPÃO");
	cmbDeclaracaoCrs.add("0061 - CRS - OCDE - LUXEMBURGO");
	cmbDeclaracaoCrs.add("0062 - CRS - OCDE - MÉXICO");
	cmbDeclaracaoCrs.add("0063 - CRS - OCDE - NORUEGA");
	cmbDeclaracaoCrs.add("0064 - CRS - OCDE - NOVA ZELÂNDIA");
	cmbDeclaracaoCrs.add("0065 - CRS - OCDE - PAÍSES BAIXOS");
	cmbDeclaracaoCrs.add("0066 - CRS - OCDE - POLÔNIA");
	cmbDeclaracaoCrs.add("0067 - CRS - OCDE - PORTUGAL");
	cmbDeclaracaoCrs.add("0068 - CRS - OCDE - REINO UNIDO");
	cmbDeclaracaoCrs.add("0069 - CRS - OCDE - REPÚBLICA TCHECA");
	cmbDeclaracaoCrs.add("0070 - CRS - OCDE - SUÉCIA");
	cmbDeclaracaoCrs.add("0071 - CRS - OCDE - SUÍÇA");
	cmbDeclaracaoCrs.add("0072 - CRS - OCDE - TURQUIA");
	cmbDeclaracaoCrs.add("0073 - CRS - OCDE - USA");

    }

    @Override
    public Long getId() {
	return id;
    }

    public void setId(final Long valor) {
	id = valor;
    }

    public String getImgbarra0png() {
	return imgBarra0png;
    }

    public void setImgbarra0png(final String valor) {
	imgBarra0png = valor;
    }

    public String getCmbtipodeconta2() {
	return cmbTipodeconta2;
    }

    public void setCmbtipodeconta2(final String valor) {
	cmbTipodeconta2 = valor;
    }

    public List<String> getCmbtipodeconta2Content() {
	return cmbTipodeconta2content;
    }

    public void setCmbtipodeconta2Content(List<String> lista) {
	this.cmbTipodeconta2content = lista;
    }

    public List<String> getCmbDeclaracaoCrs() {
		return cmbDeclaracaoCrs;
	}

	public void setCmbDeclaracaoCrs(List<String> cmbDeclaracaoCrs) {
		this.cmbDeclaracaoCrs = cmbDeclaracaoCrs;
	}

	public String getOptmodalidade() {
	return optModalidade;
    }

    public void setOptmodalidade(final String valor) {
	optModalidade = valor;
    }

    public String getChkopces2() {
	return chkOpces2;
    }

    public void setChkopces2(final String valor) {
	chkOpces2 = valor;
    }

    public String getOptmenoremancipado2() {
	return optMenoremancipado2;
    }

    public void setOptmenoremancipado2(final String valor) {
	optMenoremancipado2 = valor;
    }

    public String getOptsolidaria() {
	return optSolidaria;
    }

    public void setOptsolidaria(final String valor) {
	optSolidaria = valor;
    }

    public String getOptpossui() {
	return optPossui;
    }

    public void setOptpossui(final String valor) {
	optPossui = valor;
    }

    public String getNuminformeocpf2() {
	return numInformeocpf2;
    }

    public void setNuminformeocpf2(final String valor) {
	numInformeocpf2 = valor;
    }

    public String getBtilupapng2() {
	return btiLupapng2;
    }

    public void setBtilupapng2(final String valor) {
	btiLupapng2 = valor;
    }

    public String getTxtnome2() {
	return txtNome2;
    }

    public void setTxtnome2(final String valor) {
	txtNome2 = valor;
    }

    public String getCmbtipodeconta() {
	return cmbTipodeconta;
    }

    public void setCmbtipodeconta(final String valor) {
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

    public void setOptmodalidade2(final String valor) {
	optModalidade2 = valor;
    }

    public String getOptsolidaria2() {
	return optSolidaria2;
    }

    public void setOptsolidaria2(final String valor) {
	optSolidaria2 = valor;
    }

    public String getOptpossui2() {
	return optPossui2;
    }

    public void setOptpossui2(final String valor) {
	optPossui2 = valor;
    }

    public String getNuminformeocpf5() {
	return numInformeocpf5;
    }

    public void setNuminformeocpf5(final String valor) {
	numInformeocpf5 = valor;
    }

    public String getTxtnome3() {
	return txtNome3;
    }

    public void setTxtnome3(final String valor) {
	txtNome3 = valor;
    }

    public String getNuminformeocpf() {
	return numInformeocpf;
    }

    public void setNuminformeocpf(final String valor) {
	numInformeocpf = valor;
    }

    public String getBtilupapng3() {
	return btiLupapng3;
    }

    public void setBtilupapng3(final String valor) {
	btiLupapng3 = valor;
    }

    public String getBtilupapng() {
	return btiLupapng;
    }

    public void setBtilupapng(final String valor) {
	btiLupapng = valor;
    }

    public String getTxtnome4() {
	return txtNome4;
    }

    public void setTxtnome4(final String valor) {
	txtNome4 = valor;
    }

    public String getNuminformeocpf3() {
	return numInformeocpf3;
    }

    public void setNuminformeocpf3(final String valor) {
	numInformeocpf3 = valor;
    }

    public String getBtilupapng4() {
	return btiLupapng4;
    }

    public void setBtilupapng4(final String valor) {
	btiLupapng4 = valor;
    }

    public String getTxtnome() {
	return txtNome;
    }

    public void setTxtnome(final String valor) {
	txtNome = valor;
    }

    public String getImgbarra2png() {
	return imgBarra2png;
    }

    public void setImgbarra2png(final String valor) {
	imgBarra2png = valor;
    }

    public List<String> getTbl2() {
	return tbl2content;
    }

    public void setTbl2(final List<String> lista) {
	tbl2content = lista;
    }

    public List<HashMap<String, String>> getTbl2Data() {
	return tbl2data;
    }

    public void setTbl2Data(final List<HashMap<String, String>> lista) {
	tbl2data = lista;
    }

    public String getImgbarra3png() {
	return imgBarra3png;
    }

    public void setImgbarra3png(final String valor) {
	imgBarra3png = valor;
    }

    public String getBtidocrendimentopng() {
	return btiDocrendimentopng;
    }

    public void setBtidocrendimentopng(final String valor) {
	btiDocrendimentopng = valor;
    }

    public String getBtidocresidenciapng() {
	return btiDocresidenciapng;
    }

    public void setBtidocresidenciapng(final String valor) {
	btiDocresidenciapng = valor;
    }

    public String getBtidocidentificacaopng() {
	return btiDocidentificacaopng;
    }

    public void setBtidocidentificacaopng(final String valor) {
	btiDocidentificacaopng = valor;
    }

    public String getCmbnacionalidade2() {
	return cmbNacionalidade2;
    }

    public void setCmbnacionalidade2(final String valor) {
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

    public void setCmbnaturalidade2(final String valor) {
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

    public void setTxtcidade(final String valor) {
	txtCidade = valor;
    }

    public String getTxtnomedopai2() {
	return txtNomedopai2;
    }

    public void setTxtnomedopai2(final String valor) {
	txtNomedopai2 = valor;
    }

    public String getImgidentidadejpg() {
	return imgIdentidadejpg;
    }

    public void setImgidentidadejpg(final String valor) {
	imgIdentidadejpg = valor;
    }

    public String getNumcep2() {
	return numCep2;
    }

    public void setNumcep2(final String valor) {
	numCep2 = valor;
    }

    public String getCmbtipodelogradouro2() {
	return cmbTipodelogradouro2;
    }

    public void setCmbtipodelogradouro2(final String valor) {
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

    public void setTxtlogradouro(final String valor) {
	txtLogradouro = valor;
    }

    public String getNumnumero2() {
	return numNumero2;
    }

    public void setNumnumero2(final String valor) {
	numNumero2 = valor;
    }

    public String getTxtcomplemento2() {
	return txtComplemento2;
    }

    public void setTxtcomplemento2(final String valor) {
	txtComplemento2 = valor;
    }

    public String getTxtbairro2() {
	return txtBairro2;
    }

    public void setTxtbairro2(final String valor) {
	txtBairro2 = valor;
    }

    public String getCmbuf2() {
	return cmbUf2;
    }

    public void setCmbuf2(final String valor) {
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

    public void setImgcompresidenciajpg(final String valor) {
	imgCompresidenciajpg = valor;
    }

    public String getTxtlogradouro2() {
	return txtLogradouro2;
    }

    public void setTxtlogradouro2(final String valor) {
	txtLogradouro2 = valor;
    }

    public String getCmbtipodefontepagadora() {
	return cmbTipodefontepagadora;
    }

    public void setCmbtipodefontepagadora(final String valor) {
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

    public void setNumcnpj2(final String valor) {
	numCnpj2 = valor;
    }

    public String getTxtnomedafontepagadora2() {
	return txtNomedafontepagadora2;
    }

    public void setTxtnomedafontepagadora2(final String valor) {
	txtNomedafontepagadora2 = valor;
    }

    public String getNumcoclidafontepagadora2() {
	return numCoclidafontepagadora2;
    }

    public void setNumcoclidafontepagadora2(final String valor) {
	numCoclidafontepagadora2 = valor;
    }

    public String getCmbocupacao() {
	return cmbOcupacao;
    }

    public void setCmbocupacao(final String valor) {
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

    public void setDtdatadeadmissaoinicio2(final String valor) {
	dtDatadeadmissaoinicio2 = valor;
    }

    public String getNumrendabruta2() {
	return numRendabruta2;
    }

    public void setNumrendabruta2(final String valor) {
	numRendabruta2 = valor;
    }

    public String getNumrendaliquida2() {
	return numRendaliquida2;
    }

    public void setNumrendaliquida2(final String valor) {
	numRendaliquida2 = valor;
    }

    public String getNumvalordoimpostoretidonafonte2() {
	return numValordoimpostoretidonafonte2;
    }

    public void setNumvalordoimpostoretidonafonte2(final String valor) {
	numValordoimpostoretidonafonte2 = valor;
    }

    public String getImgcomprendajpg() {
	return imgComprendajpg;
    }

    public void setImgcomprendajpg(final String valor) {
	imgComprendajpg = valor;
    }

    public String getImgbarra04png() {
	return imgBarra04png;
    }

    public void setImgbarra04png(final String valor) {
	imgBarra04png = valor;
    }

    public String getImgbarra4png() {
	return imgBarra4png;
    }

    public void setImgbarra4png(final String valor) {
	imgBarra4png = valor;
    }

    public String getMsksenha() {
	return mskSenha;
    }

    public void setMsksenha(final String valor) {
	mskSenha = valor;
    }

    public String getTxtredigitacaosenha() {
	return txtRedigitacaosenha;
    }

    public void setTxtredigitacaosenha(final String valor) {
	txtRedigitacaosenha = valor;
    }

    public String getImgbarra05png() {
	return imgBarra05png;
    }

    public void setImgbarra05png(final String valor) {
	imgBarra05png = valor;
    }

    public String getCmbgraudeinstrucao() {
	return cmbGraudeinstrucao;
    }

    public void setCmbgraudeinstrucao(final String valor) {
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

    public void setCmbsexo2(final String valor) {
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

    public void setCmbprofissaocdigodaocupacao(final String valor) {
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

    public void setTxtenderecocomercial(final String valor) {
	txtEnderecocomercial = valor;
    }

    public String getCmbestadocivil2() {
	return cmbEstadocivil2;
    }

    public void setCmbestadocivil2(final String valor) {
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

    public void setNumcpfdoconjuge(final String valor) {
	numCpfdoconjuge = valor;
    }

    public String getImglupapng2() {
	return imgLupapng2;
    }

    public void setImglupapng2(final String valor) {
	imgLupapng2 = valor;
    }

    public String getTxtnomedoconjuge() {
	return txtNomedoconjuge;
    }

    public void setTxtnomedoconjuge(final String valor) {
	txtNomedoconjuge = valor;
    }

    public String getDtdatadenascimentodoconjuge() {
	return dtDatadenascimentodoconjuge;
    }

    public void setDtdatadenascimentodoconjuge(final String valor) {
	dtDatadenascimentodoconjuge = valor;
    }

    public String getCmbfinalidade() {
	return cmbFinalidade;
    }

    public void setCmbfinalidade(final String valor) {
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

    public void setCmbmeiosdecomunicacao(final String valor) {
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

    public void setNumdddenumero(final String valor) {
	numDddenumero = valor;
    }

    public String getTxtcontato() {
	return txtContato;
    }

    public void setTxtcontato(final String valor) {
	txtContato = valor;
    }

    public String getChkselecioneasopces2() {
	return chkSelecioneasopces2;
    }

    public void setChkselecioneasopces2(final String valor) {
	chkSelecioneasopces2 = valor;
    }

    public String getCmbsaquestransfernciaspagamentosagendamentosapartirde() {
	return cmbSaquestransfernciaspagamentosagendamentosapartirde;
    }

    public void setCmbsaquestransfernciaspagamentosagendamentosapartirde(final String valor) {
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

    public void setCmbcomprascomcartaodedbitoapartirde(final String valor) {
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

    public void setCmbdesejareceberinformacesdocartaodecrditocaixa2(final String valor) {
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

    public void setCmbautorizaoenviodeinformacesdofgts(final String valor) {
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

    public void setCmbgostariaderecebermaisinformacessobreprodutoseservicosdacaixa2(final String valor) {
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

    public void setOptselecioneaopcao(final String valor) {
	optSelecioneaopcao = valor;
    }

    public String getOptdeclaracaofatcacrs2() {
	return optDeclaracaofatcacrs2;
    }

    public void setOptdeclaracaofatcacrs2(final String valor) {
	optDeclaracaofatcacrs2 = valor;
    }

    public String getNumnumerotin2() {
	return numNumerotin2;
    }

    public void setNumnumerotin2(final String valor) {
	numNumerotin2 = valor;
    }

    public String getCmbdeclaracaodepropsitosedanaturezadacontadepsito2() {
	return cmbDeclaracaodepropsitosedanaturezadacontadepsito2;
    }

    public void setCmbdeclaracaodepropsitosedanaturezadacontadepsito2(final String valor) {
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

    public void setMskassinaturadocliente2(final String valor) {
	mskAssinaturadocliente2 = valor;
    }

    public String getMskinformeasenha() {
	return mskInformeasenha;
    }

    public void setMskinformeasenha(final String valor) {
	mskInformeasenha = valor;
    }

    public String getMskinformeasenha2() {
	return mskInformeasenha2;
    }

    public void setMskinformeasenha2(final String valor) {
	mskInformeasenha2 = valor;
    }

    public String getImgbarra06png() {
	return imgBarra06png;
    }

    public void setImgbarra06png(final String valor) {
	imgBarra06png = valor;
    }

    public List<String> getTbl() {
	return tblcontent;
    }

    public void setTbl(final List<String> lista) {
	tblcontent = lista;
    }

    public List<HashMap<String, String>> getTblData() {
	return tbldata;
    }

    public void setTblData(final List<HashMap<String, String>> lista) {
	tbldata = lista;
    }

    public String getChkselecioneasopces() {
	return chkSelecioneasopces;
    }

    public void setChkselecioneasopces(final String valor) {
	chkSelecioneasopces = valor;
    }

    public String getChkoperacaocomercial() {
	return chkOperacaocomercial;
    }

    public void setChkoperacaocomercial(final String valor) {
	chkOperacaocomercial = valor;
    }

    public String getImgbarra07png() {
	return imgBarra07png;
    }

    public void setImgbarra07png(final String valor) {
	imgBarra07png = valor;
    }

    public String getChkselecioneasopces8() {
	return chkSelecioneasopces8;
    }

    public void setChkselecioneasopces8(final String valor) {
	chkSelecioneasopces8 = valor;
    }

    public String getCmbmodalidadedacesta2() {
	return cmbModalidadedacesta2;
    }

    public void setCmbmodalidadedacesta2(final String valor) {
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

    public void setNumdiadodbito(final String valor) {
	numDiadodbito = valor;
    }

    public String getOptpacotedescontocestadeservicos2() {
	return optPacotedescontocestadeservicos2;
    }

    public void setOptpacotedescontocestadeservicos2(final String valor) {
	optPacotedescontocestadeservicos2 = valor;
    }

    public String getChkselecioneasopces7() {
	return chkSelecioneasopces7;
    }

    public void setChkselecioneasopces7(final String valor) {
	chkSelecioneasopces7 = valor;
    }

    public String getNumlimitecontratado3() {
	return numLimitecontratado3;
    }

    public void setNumlimitecontratado3(final String valor) {
	numLimitecontratado3 = valor;
    }

    public String getNumdatadedbitojuros() {
	return numDatadedbitojuros;
    }

    public void setNumdatadedbitojuros(final String valor) {
	numDatadedbitojuros = valor;
    }

    public String getChkselecioneasopces3() {
	return chkSelecioneasopces3;
    }

    public void setChkselecioneasopces3(final String valor) {
	chkSelecioneasopces3 = valor;
    }

    public String getChkselecioneasopces5() {
	return chkSelecioneasopces5;
    }

    public void setChkselecioneasopces5(final String valor) {
	chkSelecioneasopces5 = valor;
    }

    public String getNumprestacaomaximaadmitida2() {
	return numPrestacaomaximaadmitida2;
    }

    public void setNumprestacaomaximaadmitida2(final String valor) {
	numPrestacaomaximaadmitida2 = valor;
    }

    public String getChkselecioneasopces6() {
	return chkSelecioneasopces6;
    }

    public void setChkselecioneasopces6(final String valor) {
	chkSelecioneasopces6 = valor;
    }

    public String getNumlimitecontratado() {
	return numLimitecontratado;
    }

    public void setNumlimitecontratado(final String valor) {
	numLimitecontratado = valor;
    }

    public String getCmbtipodecartao2() {
	return cmbTipodecartao2;
    }

    public void setCmbtipodecartao2(final String valor) {
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

    public void setCmbmodalidade2(final String valor) {
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

    public void setCmbbandeira2(final String valor) {
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

    public void setCmbvariante2(final String valor) {
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

    public void setCmbdiadovencimentodafatura2(final String valor) {
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

    public void setOptenderecoparaentregadafatura2(final String valor) {
	optEnderecoparaentregadafatura2 = valor;
    }

    public String getOptcontratasegurodocartao() {
	return optContratasegurodocartao;
    }

    public void setOptcontratasegurodocartao(final String valor) {
	optContratasegurodocartao = valor;
    }

    public String getOptprogramapontoscaixa2() {
	return optProgramapontoscaixa2;
    }

    public void setOptprogramapontoscaixa2(final String valor) {
	optProgramapontoscaixa2 = valor;
    }

    public String getOptconcordaemparticipardecampanhasdeincentivodacaixacompremiacao2() {
	return optConcordaemparticipardecampanhasdeincentivodacaixacompremiacao2;
    }

    public void setOptconcordaemparticipardecampanhasdeincentivodacaixacompremiacao2(final String valor) {
	optConcordaemparticipardecampanhasdeincentivodacaixacompremiacao2 = valor;
    }

    public String getChkselecioneasopces4() {
	return chkSelecioneasopces4;
    }

    public void setChkselecioneasopces4(final String valor) {
	chkSelecioneasopces4 = valor;
    }

    public String getChkformularios() {
	return chkFormularios;
    }

    public void setChkformularios(final String valor) {
	chkFormularios = valor;
    }

    public String getMskassinaturadocliente() {
	return mskAssinaturadocliente;
    }

    public void setMskassinaturadocliente(final String valor) {
	mskAssinaturadocliente = valor;
    }

    public String getImgbarra08png() {
	return imgBarra08png;
    }

    public void setImgbarra08png(final String valor) {
	imgBarra08png = valor;
    }
}
