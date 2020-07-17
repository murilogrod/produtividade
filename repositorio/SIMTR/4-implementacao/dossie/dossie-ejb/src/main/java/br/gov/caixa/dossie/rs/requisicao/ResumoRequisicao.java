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
public class ResumoRequisicao extends Requisicao {
	private static final long serialVersionUID = 3075165143170327078L;
	private Long id;
	private String imgMenuresumodoclientepng3 = "";
	private String chkPesquisacadastral2 = "";
    private List<String> tblcontent = new ArrayList<String>();
    private List<HashMap<String,String>> tbldata = new ArrayList<HashMap<String,String>>();
	private String imgMenuresumodoclientepng2 = "";
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public ResumoRequisicao() {
		super();
		final HashMap<String,String> tbldataMap = new HashMap<String,String>();
    	tblcontent.add("Código");
    	tbldataMap.put("Código","valor");
    	tblcontent.add("Produto");
    	tbldataMap.put("Produto","valor");
    	tblcontent.add("Quantidade");
    	tbldataMap.put("Quantidade","valor");
    	tblcontent.add("Contrato");
    	tbldataMap.put("Contrato","valor");
    	tblcontent.add("Data de Contratação");
    	tbldataMap.put("Data de Contratação","valor");
        tbldata.add(tbldataMap);
        tbldata.add(tbldataMap);
        tbldata.add(tbldataMap);
        tbldata.add(tbldataMap);
        tbldata.add(tbldataMap);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImgmenuresumodoclientepng3() {
		return imgMenuresumodoclientepng3;
	}
	public void setImgmenuresumodoclientepng3(String valor) {
		imgMenuresumodoclientepng3 = valor;
	}
	public String getChkpesquisacadastral2() {
		return chkPesquisacadastral2;
	}
	public void setChkpesquisacadastral2(String valor) {
		chkPesquisacadastral2 = valor;
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
	public String getImgmenuresumodoclientepng2() {
		return imgMenuresumodoclientepng2;
	}
	public void setImgmenuresumodoclientepng2(String valor) {
		imgMenuresumodoclientepng2 = valor;
	}
}

