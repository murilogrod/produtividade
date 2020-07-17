	package br.gov.caixa.dossie.rs.requisicao;

/**
 * @author SIOGP
 */
public class SobreRequisicao extends Requisicao {
	private static final long serialVersionUID = 3075165143170327078L;
	private Long id;
	private String imgIcon013primosiogpinvertedsmallpng = "";
	private String lnkPrimosiogp2 = "";
	private String lbpExibeasituacaoatualdaassinaturadenotificaces2 = "";
	private String btpRecebernotificaces = "";
	private String btpReceberEmail = "";
	private String btpCancelarnotificaces = "";
	private String btpCancelarEmail = "";
	private String link = "";
	private String listagem = "";
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public SobreRequisicao() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImgicon013primosiogpinvertedsmallpng() {
		return imgIcon013primosiogpinvertedsmallpng;
	}
	public void setImgicon013primosiogpinvertedsmallpng(String valor) {
		imgIcon013primosiogpinvertedsmallpng = valor;
	}
	public String getLnkprimosiogp2() {
		return lnkPrimosiogp2;
	}
	public void setLnkprimosiogp2(String valor) {
		lnkPrimosiogp2 = valor;
	}
	public String getLbpexibeasituacaoatualdaassinaturadenotificaces2() {
		return lbpExibeasituacaoatualdaassinaturadenotificaces2;
	}
	public void setLbpexibeasituacaoatualdaassinaturadenotificaces2(String valor) {
		lbpExibeasituacaoatualdaassinaturadenotificaces2 = valor;
	}
	public String getBtprecebernotificaces() {
		return btpRecebernotificaces;
	}
	public void setBtprecebernotificaces(String valor) {
		btpRecebernotificaces = valor;
	}
	public String getBtpreceberemail() {
		return btpReceberEmail;
	}
	public void setBtpreceberemail(String valor) {
		btpReceberEmail = valor;
	}
	public String getBtpcancelarnotificaces() {
		return btpCancelarnotificaces;
	}
	public void setBtpcancelarnotificaces(String valor) {
		btpCancelarnotificaces = valor;
	}
	public String getBtpcancelaremail() {
		return btpCancelarEmail;
	}
	public void setBtpcancelaremail(String valor) {
		btpCancelarEmail = valor;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String valor) {
		link = valor;
	}
	public String getListagem() {
		return listagem;
	}
	public void setListagem(String valor) {
		listagem = valor;
	}
}

