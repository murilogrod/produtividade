package br.gov.caixa.simtr.modelo.mapeamento.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CalendarSimpleBRAdapter extends XmlAdapter<String, Calendar> {

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	public String marshal(Calendar calendar) throws Exception {
		synchronized (dateFormat) {
			return dateFormat.format(calendar.getTime());
		}
	}

	@Override
	public Calendar unmarshal(String calendar) throws Exception {
		synchronized (dateFormat) {
			Calendar c = Calendar.getInstance();
			c.setTime(dateFormat.parse(calendar));
			return c;
		}
	}
}