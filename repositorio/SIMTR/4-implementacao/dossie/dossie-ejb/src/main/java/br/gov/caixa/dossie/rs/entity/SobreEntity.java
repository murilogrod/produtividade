	package br.gov.caixa.dossie.rs.entity;

/**
 * @author SIOGP
 */
public class SobreEntity implements BaseEntity {
	private static final long serialVersionUID = 6933641140920629711L;
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
	public SobreEntity() {
		super();
	}
	@Override
	public Long getId() {
		return id;
	}
	public void setId(final Long valor) {
		id = valor;
	}
	public String getImgicon013primosiogpinvertedsmallpng() {
		return imgIcon013primosiogpinvertedsmallpng;
	}
	public void setImgicon013primosiogpinvertedsmallpng(final String valor) {
		imgIcon013primosiogpinvertedsmallpng = valor;
	}
	public String getLnkprimosiogp2() {
		return lnkPrimosiogp2;
	}
	public void setLnkprimosiogp2(final String valor) {
		lnkPrimosiogp2 = valor;
	}
	public String getLbpexibeasituacaoatualdaassinaturadenotificaces2() {
		return lbpExibeasituacaoatualdaassinaturadenotificaces2;
	}
	public void setLbpexibeasituacaoatualdaassinaturadenotificaces2(final String valor) {
		lbpExibeasituacaoatualdaassinaturadenotificaces2 = valor;
	}
	public String getBtprecebernotificaces() {
		return btpRecebernotificaces;
	}
	public void setBtprecebernotificaces(final String valor) {
		btpRecebernotificaces = valor;
	}
	public String getBtpreceberemail() {
		return btpReceberEmail;
	}
	public void setBtpreceberemail(final String valor) {
		btpReceberEmail = valor;
	}
	public String getBtpcancelarnotificaces() {
		return btpCancelarnotificaces;
	}
	public void setBtpcancelarnotificaces(final String valor) {
		btpCancelarnotificaces = valor;
	}
	public String getBtpcancelaremail() {
		return btpCancelarEmail;
	}
	public void setBtpcancelaremail(final String valor) {
		btpCancelarEmail = valor;
	}
	public String getLink() {
		return link;
	}
	public void setLink(final String valor) {
		link = valor;
	}
	public String getListagem() {
		return listagem;
	}
	public void setListagem(final String valor) {
		listagem = valor;
	}
}

