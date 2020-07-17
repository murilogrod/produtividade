/**
 * Copyright (c) 2018 Caixa Econômica Federal. Todos os direitos
 * reservados.
 *
 * Caixa Econômica Federal - SIACG
 *
 * Este software foi desenvolvido sob demanda da CAIXA e está
 * protegido por leis de direitos autorais e tratados internacionais. As
 * condições de cópia e utilização total ou partes dependem de autorização da
 * empresa. Cópias não são permitidas sem expressa autorização. Não pode ser
 * comercializado ou utilizado para propósitos particulares.
 *
 * Uso exclusivo da Caixa Econômica Federal. A reprodução ou distribuição não
 * autorizada deste programa ou de parte dele, resultará em punições civis e
 * criminais e os infratores incorrem em sanções previstas na legislação em
 * vigor.
 *
 * Histórico do TFS:
 *
 * LastChangedRevision: $Revision$
 * LastChangedBy: $Author$
 * LastChangedDate: $Date$
 *
 * HeadURL: $HeadURL$
 *
 */	
package br.gov.caixa.simtr.util;

import java.text.ParseException;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * <p>CalendarUtilTest</p>
 *
 * <p>Descrição: Classe de teste do servico CalendarUtil</p>
 *
 * <br><b>Empresa:</b> Cef - Caixa Econômica Federal
 *
 *
 * @author f538462
 *
 * @version 1.0
*/
public class CalendarUtilTest {
	
	@InjectMocks
	CalendarUtil calendarUtil = new CalendarUtil();
	
	
	/** 
	 * <p>Método responsável por inicializar os mocks</p>.
	 *
	 * @author p541915
	 *
	 */
	@Before
	public void setUpPostConstruct() {
		MockitoAnnotations.initMocks(this);
		
	}	
	
	
	@Test
	public void removeHoraTest() {
		calendarUtil.removeHora(Calendar.getInstance());
	}
	
	@Test
	public void toStringTest() {
		Assert.assertNotNull(calendarUtil.toString(Calendar.getInstance(), "dd/MM/yyyy"));
	}
	
	@Test
	public void toStringGEDTest() {
		Assert.assertNotNull(calendarUtil.toStringGED(Calendar.getInstance()));
	}
	
	
	@Test
	public void toCalendarTest() throws ParseException {
		Assert.assertNotNull(calendarUtil.toCalendar("10-10-84 10:10:10.000", false));
	}
	
	@Test
	public void toCalendarTest4() throws ParseException {
		Assert.assertNotNull(calendarUtil.toCalendar("10-10-84 10:10:10", false));
	}
	
	@Test
	public void toCalendarTest5() throws ParseException {
		Assert.assertNotNull(calendarUtil.toCalendar("10-10-84", false));
	}
	
	@Test
	public void toCalendarTest2() throws ParseException {
		Assert.assertNotNull(calendarUtil.toCalendar("10/10/84 10:10:10.000", false));
	}
	
	@Test
	public void toCalendarTest3() throws ParseException {
		Assert.assertNotNull(calendarUtil.toCalendar("10/10/84 10:10:10", false));
	}
	
	@Test
	public void toCalendarTest6() throws ParseException {
		Assert.assertNotNull(calendarUtil.toCalendar("10/10/84", false));
	}
	
	@Test
	public void toCalendarTest7() throws ParseException {
		Assert.assertNotNull(calendarUtil.toCalendar("10-10-1984 10:10:10.000", false));
	}
	
	@Test
	public void toCalendarTest8() throws ParseException {
		Assert.assertNotNull(calendarUtil.toCalendar("10-10-1984 10:10:10", false));
	}
	
	@Test
	public void toCalendarTest9() throws ParseException {
		Assert.assertNotNull(calendarUtil.toCalendar("10-10-1984", false));
	}
	
	@Test
	public void toCalendarTest10() throws ParseException {
		Assert.assertNotNull(calendarUtil.toCalendar("10/10/1984", false));
	}
	
	@Test
	public void toCalendarTest11() throws ParseException {
		Assert.assertNotNull(calendarUtil.toCalendar("10/10/1984 10:10:10.000", false));
	}
	
	@Test
	public void toCalendarTest12() throws ParseException {
		Assert.assertNotNull(calendarUtil.toCalendar("10/10/1984 10:10:10", true));
	}
        
        @Test (expected = IllegalArgumentException.class)
	public void toCalendarTest13() throws ParseException {
		Assert.assertNotNull(calendarUtil.toCalendar("10/OUT/1984 10:10:10", true));
	}
	
}
