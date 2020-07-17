package br.gov.caixa.dossie;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;

/**
 * @author SIOGP
 */
public class Authentication {

	private static final Logger LOG = Logger.getLogger(Authentication.class);

	private int code = -1;
	private Date validTo = null;
	private static final int MIN_DEFALT = 1000;
	private static final int MAX_DEFALT = 9999;
	private static final int VALIDTOHOURS_DEFALT = 48;

	private int min = MIN_DEFALT;
	private int max = MAX_DEFALT;
	private int validToHours = VALIDTOHOURS_DEFALT;

	private static Authentication auth = null;

	/**
	 * Construtor padrão
	 */
	public Authentication() {
		LOG.debug("Authentication()");
	}

	public static Authentication getInstance() {
		if (auth == null) {
			auth = new Authentication();
		}
		return auth;
	}

	/**
	 * generate
	 */
	public void generate() {
		generate(min, max, validToHours);
	}

	/**
	 * @param min
	 * @param max
	 * @param validToHours
	 */
	public void generate(int min, int max, int validToHours) {
		this.min = min;
		this.max = max;
		this.validToHours = validToHours;

		generateDate(validToHours);
		generateCode(min, max);
	}

	/**
	 * @param min
	 * @param max
	 */
	public void generateCode(int min, int max) {
		int newcode = (int) (Math.floor(Math.random() * (max - min + 1)) + min);
		if (code != -1) {
			LOG.info("--------------> new authCode: " + Integer.toString(newcode));
		}
		code = newcode;
		code = 100;
	}

	/**
	 * @param validToHours
	 */
	public void generateDate(int validToHours) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR_OF_DAY, validToHours);
		validTo = calendar.getTime();
	}

	public boolean isExpired() {
		boolean result = false;

		Date now = new Date();
		if (validTo == null || now.after(validTo)) {
			result = true;
		}
		return result;
	}

	public int getCode() {
		if (isExpired()) {
			generate(min, max, validToHours);
		}
		return code;
	}

	public Date getDate() {
		if (isExpired()) {
			generate(min, max, validToHours);
		}
		return validTo;
	}
}
