	package br.gov.caixa.dossie.rs.requisicao;

/**
 * @author SIOGP
 */
public class SplashRequisicao extends Requisicao {
	private static final long serialVersionUID = 3075165143170327078L;
	private Long id;
	private String btiIcon013primosiogpinvertedverysmallpng2 = "";
	private String imgDossielogopng = "";
	private String timer = "";
	private String timer2 = "";
	private String link = "";
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public SplashRequisicao() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBtiicon013primosiogpinvertedverysmallpng2() {
		return btiIcon013primosiogpinvertedverysmallpng2;
	}
	public void setBtiicon013primosiogpinvertedverysmallpng2(String valor) {
		btiIcon013primosiogpinvertedverysmallpng2 = valor;
	}
	public String getImgdossielogopng() {
		return imgDossielogopng;
	}
	public void setImgdossielogopng(String valor) {
		imgDossielogopng = valor;
	}
	public String getTimer() {
		return timer;
	}
	public void setTimer(String valor) {
		timer = valor;
	}
	public String getTimer2() {
		return timer2;
	}
	public void setTimer2(String valor) {
		timer2 = valor;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String valor) {
		link = valor;
	}
}

