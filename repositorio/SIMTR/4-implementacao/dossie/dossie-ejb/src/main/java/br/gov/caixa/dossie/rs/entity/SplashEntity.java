	package br.gov.caixa.dossie.rs.entity;

/**
 * @author SIOGP
 */
public class SplashEntity implements BaseEntity {
	private static final long serialVersionUID = 6933641140920629711L;
	private Long id;
	private String btiIcon013primosiogpinvertedverysmallpng2 = "";
	private String imgDossielogopng = "";
	private String timer = "";
	private String timer2 = "";
	private String link = "";
	/**
	 * CONSTRUTOR PADRÃO
	 */
	public SplashEntity() {
		super();
	}
	@Override
	public Long getId() {
		return id;
	}
	public void setId(final Long valor) {
		id = valor;
	}
	public String getBtiicon013primosiogpinvertedverysmallpng2() {
		return btiIcon013primosiogpinvertedverysmallpng2;
	}
	public void setBtiicon013primosiogpinvertedverysmallpng2(final String valor) {
		btiIcon013primosiogpinvertedverysmallpng2 = valor;
	}
	public String getImgdossielogopng() {
		return imgDossielogopng;
	}
	public void setImgdossielogopng(final String valor) {
		imgDossielogopng = valor;
	}
	public String getTimer() {
		return timer;
	}
	public void setTimer(final String valor) {
		timer = valor;
	}
	public String getTimer2() {
		return timer2;
	}
	public void setTimer2(final String valor) {
		timer2 = valor;
	}
	public String getLink() {
		return link;
	}
	public void setLink(final String valor) {
		link = valor;
	}
}

