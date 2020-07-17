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
public class ResumoEntity implements BaseEntity {
	private static final long serialVersionUID = 6933641140920629711L;
	private Long id;
	private String imgMenuresumodoclientepng3 = "";
	private String chkPesquisacadastral2 = "";
	private List<String> tblcontent = new ArrayList<String>();
    private List<HashMap<String,String>> tbldata = new ArrayList<HashMap<String,String>>();
	private String imgMenuresumodoclientepng2 = "";
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public ResumoEntity() {
		super();
		 tblcontent.add("Código");
		 tblcontent.add("Produto");
		 tblcontent.add("Quantidade");
		 tblcontent.add("Contrato");
		 tblcontent.add("Data de Contratação");
		HashMap<String, String> tblcontent1 = new HashMap<String,String>();
		tblcontent1.put("Código", "itemLinha1");
		tblcontent1.put("Produto", "itemLinha1");
		tblcontent1.put("Quantidade", "itemLinha1");
		tblcontent1.put("Contrato", "itemLinha1");
		tblcontent1.put("Data de Contratação", "itemLinha1");
    	tbldata.add(tblcontent1);
		HashMap<String, String> tblcontent2 = new HashMap<String,String>();
		tblcontent2.put("Código", "itemLinha2");
		tblcontent2.put("Produto", "itemLinha2");
		tblcontent2.put("Quantidade", "itemLinha2");
		tblcontent2.put("Contrato", "itemLinha2");
		tblcontent2.put("Data de Contratação", "itemLinha2");
    	tbldata.add(tblcontent2);
		HashMap<String, String> tblcontent3 = new HashMap<String,String>();
		tblcontent3.put("Código", "itemLinha3");
		tblcontent3.put("Produto", "itemLinha3");
		tblcontent3.put("Quantidade", "itemLinha3");
		tblcontent3.put("Contrato", "itemLinha3");
		tblcontent3.put("Data de Contratação", "itemLinha3");
    	tbldata.add(tblcontent3);
		HashMap<String, String> tblcontent4 = new HashMap<String,String>();
		tblcontent4.put("Código", "itemLinha4");
		tblcontent4.put("Produto", "itemLinha4");
		tblcontent4.put("Quantidade", "itemLinha4");
		tblcontent4.put("Contrato", "itemLinha4");
		tblcontent4.put("Data de Contratação", "itemLinha4");
    	tbldata.add(tblcontent4);
		HashMap<String, String> tblcontent5 = new HashMap<String,String>();
		tblcontent5.put("Código", "itemLinha5");
		tblcontent5.put("Produto", "itemLinha5");
		tblcontent5.put("Quantidade", "itemLinha5");
		tblcontent5.put("Contrato", "itemLinha5");
		tblcontent5.put("Data de Contratação", "itemLinha5");
    	tbldata.add(tblcontent5);
		HashMap<String, String> tblcontent6 = new HashMap<String,String>();
		tblcontent6.put("Código", "itemLinha6");
		tblcontent6.put("Produto", "itemLinha6");
		tblcontent6.put("Quantidade", "itemLinha6");
		tblcontent6.put("Contrato", "itemLinha6");
		tblcontent6.put("Data de Contratação", "itemLinha6");
    	tbldata.add(tblcontent6);
		HashMap<String, String> tblcontent7 = new HashMap<String,String>();
		tblcontent7.put("Código", "itemLinha7");
		tblcontent7.put("Produto", "itemLinha7");
		tblcontent7.put("Quantidade", "itemLinha7");
		tblcontent7.put("Contrato", "itemLinha7");
		tblcontent7.put("Data de Contratação", "itemLinha7");
    	tbldata.add(tblcontent7);
		HashMap<String, String> tblcontent8 = new HashMap<String,String>();
		tblcontent8.put("Código", "itemLinha8");
		tblcontent8.put("Produto", "itemLinha8");
		tblcontent8.put("Quantidade", "itemLinha8");
		tblcontent8.put("Contrato", "itemLinha8");
		tblcontent8.put("Data de Contratação", "itemLinha8");
    	tbldata.add(tblcontent8);
		HashMap<String, String> tblcontent9 = new HashMap<String,String>();
		tblcontent9.put("Código", "itemLinha9");
		tblcontent9.put("Produto", "itemLinha9");
		tblcontent9.put("Quantidade", "itemLinha9");
		tblcontent9.put("Contrato", "itemLinha9");
		tblcontent9.put("Data de Contratação", "itemLinha9");
    	tbldata.add(tblcontent9);
		HashMap<String, String> tblcontent10 = new HashMap<String,String>();
		tblcontent10.put("Código", "itemLinha10");
		tblcontent10.put("Produto", "itemLinha10");
		tblcontent10.put("Quantidade", "itemLinha10");
		tblcontent10.put("Contrato", "itemLinha10");
		tblcontent10.put("Data de Contratação", "itemLinha10");
    	tbldata.add(tblcontent10);
	}
	@Override
	public Long getId() {
		return id;
	}
	public void setId(final Long valor) {
		id = valor;
	}
	public String getImgmenuresumodoclientepng3() {
		return imgMenuresumodoclientepng3;
	}
	public void setImgmenuresumodoclientepng3(final String valor) {
		imgMenuresumodoclientepng3 = valor;
	}
	public String getChkpesquisacadastral2() {
		return chkPesquisacadastral2;
	}
	public void setChkpesquisacadastral2(final String valor) {
		chkPesquisacadastral2 = valor;
	}
	public List<String> getTbl() {
        return tblcontent;
	}
	public void setTbl(final List<String> lista) {
		tblcontent = lista;
	}	
	public List<HashMap<String,String>> getTblData() {
        return tbldata;
	}
	public void setTblData(final List<HashMap<String,String>> lista) {
		tbldata = lista;
	}		
	public String getImgmenuresumodoclientepng2() {
		return imgMenuresumodoclientepng2;
	}
	public void setImgmenuresumodoclientepng2(final String valor) {
		imgMenuresumodoclientepng2 = valor;
	}
}

