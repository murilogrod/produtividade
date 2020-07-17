package br.gov.caixa.simtr.util;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;

import javax.ejb.Stateless;

@Stateless
public class CalendarUtil {
    
    private static final String SIECM_FORMATO_CAMPO_DATA = "dd-MM-yyyy HH:mm:ss";

    Map<String, String> patterns;

    /**
     * Transforma um texto que representa uma data ou data/hora em um objeto
     * Calendar
     *
     * @param texto Valor a ser convertido.
     * @param removeHora Indica se a informação de hora deve ser desconsiderada
     * na conversão
     * @return Instancia de objeto Calendar montado baseado na referência
     * encaminhada
     * @throws ParseException Exceção lançada caso o texto não consiga ser
     * convertido em um objeto Calendar
     * @throws IllegalArgumentException Exceção lançada caso o texto não
     * pertença a nenhum dos padrões de formato conhecido.
     */
    public Calendar toCalendar(String texto, boolean removeHora) throws ParseException {
        this.carregarPatterns();
        SimpleDateFormat sdf = new SimpleDateFormat();
        Calendar calendar = Calendar.getInstance();
        sdf.setTimeZone(TimeZone.getTimeZone(calendar.getTimeZone().toZoneId().getId()));

        Optional<Entry<String, String>> registro = patterns.entrySet().stream().filter(pat -> texto.matches(pat.getValue())).findFirst();
        String pattern = registro.orElseThrow(() -> {
            String mensagem = MessageFormat.format("A data encaminhada em formato texto não trata-se de um formato válido. Valor encaminhado =  {0}", texto);
            return new IllegalArgumentException(mensagem);
        }).getKey();
        sdf.applyPattern(pattern);

        calendar.setTime(sdf.parse(texto));
        if (removeHora) {
            this.removeHora(calendar);
        }
        return calendar;
    }

    /**
     * Converte um objeto Calendar representando uma data em um texto no padrão
     * de submissão ao GED
     *
     * @param calendar Objeto a ser convertido
     * @return Informação textual convertida para o padrão de texto a ser
     * submetido ao GED
     */
    public String toStringGED(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat(SIECM_FORMATO_CAMPO_DATA);
        sdf.setTimeZone(TimeZone.getTimeZone(calendar.getTimeZone().toZoneId().getId()));
        return sdf.format(calendar.getTime());
    }

    /**
     * Converte um objeto Calendar representando uma data em um texto no padrão
     * encaminhado em parametro
     *
     * @param calendar Objeto a ser convertido
     * @param mascara Mascara a ser utilizada na cpmversão
     * @return Informação textual convertida para o padrão de texto a ser
     * submetido ao GED
     */
    public String toString(Calendar calendar, String mascara) {
        SimpleDateFormat sdf = new SimpleDateFormat(mascara);
        sdf.setTimeZone(TimeZone.getTimeZone(calendar.getTimeZone().toZoneId().getId()));
        return sdf.format(calendar.getTime());
    }

    /**
     * Atribui valor 0 (zero) a todos os campos do objeto encaminhado referente
     * a representação da hora
     *
     * @param calendar Objeto calendario a ter seus campos referente a hora
     * zerados
     */
    public void removeHora(Calendar calendar) {
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private void carregarPatterns() {
        if (Objects.isNull(patterns)) {
            patterns = new HashMap<>();
            patterns.put("dd.MM.yyyy", "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[012])\\.[12][0-9]{3}$");
            patterns.put("dd/MM/yyyy", "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/[12][0-9]{3}$");
            patterns.put("dd/MM/yy", "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/[12][0-9]{1}$");
            patterns.put("dd-MM-yyyy", "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-[12][0-9]{3}$");
            patterns.put("dd.MM.yyyy HH:mm:ss", "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[012])\\.[12][0-9]{3} ([01][0-9]|[1][1-2])(:[0-5][0-9]){2}$");
            patterns.put("dd/MM/yyyy HH:mm:ss", "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/[12][0-9]{3} ([01][0-9]|[1][1-2])(:[0-5][0-9]){2}$");
            patterns.put("dd-MM-yyyy HH:mm:ss", "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-[12][0-9]{3} ([01][0-9]|[1][1-2])(:[0-5][0-9]){2}$");
            patterns.put("dd.MM.yyyy HH:mm:ss.SSS", "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[012])\\.[12][0-9]{3} ([01][0-9]|[1][1-2])(:[0-5][0-9]){2}\\.\\d{3}$");
            patterns.put("dd/MM/yyyy HH:mm:ss.SSS", "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/[12][0-9]{3} ([01][0-9]|[1][1-2])(:[0-5][0-9]){2}\\.\\d{3}$");
            patterns.put("dd-MM-yyyy HH:mm:ss.SSS", "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-[12][0-9]{3} ([01][0-9]|[1][1-2])(:[0-5][0-9]){2}\\.\\d{3}$");
            patterns.put("dd.MM.yy", "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[012])\\.[0-9]{2}$");
            patterns.put("dd/MM/yy", "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/[0-9]{2}$");
            patterns.put("dd-MM-yy", "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-[0-9]{2}$");
            patterns.put("dd.MM.yy HH:mm:ss", "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[012])\\.[0-9]{2} ([01][0-9]|[1][1-2])(:[0-5][0-9]){2}$");
            patterns.put("dd/MM/yy HH:mm:ss", "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/[0-9]{2} ([01][0-9]|[1][1-2])(:[0-5][0-9]){2}$");
            patterns.put("dd-MM-yy HH:mm:ss", "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-[0-9]{2} ([01][0-9]|[1][1-2])(:[0-5][0-9]){2}$");
            patterns.put("dd.MM.yy HH:mm:ss.SSS", "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[012])\\.[0-9]{2} ([01][0-9]|[1][1-2])(:[0-5][0-9]){2}\\.\\d{3}$");
            patterns.put("dd/MM/yy HH:mm:ss.SSS", "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/[0-9]{2} ([01][0-9]|[1][1-2])(:[0-5][0-9]){2}\\.\\d{3}$");
            patterns.put("dd-MM-yy HH:mm:ss.SSS", "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-[0-9]{2} ([01][0-9]|[1][1-2])(:[0-5][0-9]){2}\\.\\d{3}$");
            patterns.put("yyyy-MM-dd", "^[12][0-9]{3}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$");
            patterns.put("yyyy-MM-dd HH:mm:ss", "^[12][0-9]{3}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) ([01][0-9]|[1][1-2])(:[0-5][0-9]){2}$");
            patterns.put("yyyy-MM-dd HH:mm:ss.SSS", "^[12][0-9]{3}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) ([01][0-9]|[1][1-2])(:[0-5][0-9]){2}\\.\\d{3}$");
          
            // NOVOS VALIDADORES DE DATA
            patterns.put("dd/MMM/yy", "^(([0-9])|([0-2][0-9])|([3][0-1]))\\/(Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez)\\/\\d{2}$");
            patterns.put("d/M/yyyy", "([0-3]?\\d\\/{1})([01]?\\d\\/{1})([12]{1}\\d{3})");
            patterns.put("d/M/yyy", "([0-3]?\\d\\/{1})([01]?\\d\\/{1})([12]{1}\\d{2})");
            patterns.put("d/M/yy", "([0-3]?\\d\\/{1})([01]?\\d\\/{1})([0-9]{1}\\d{1})");
            patterns.put("dd-MMM-yyyy","^(([0-9])|([0-2][0-9])|([3][0-1]))-(Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez)-\\d{4}$");
            patterns.put("dd-MMM-yyy","^(([0-9])|([0-2][0-9])|([3][0-1]))-(Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez)-\\d{3}$");
            patterns.put("dd-MMM-yy","^(([0-9])|([0-2][0-9])|([3][0-1]))-(Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez)-\\d{2}$");
            patterns.put("d-M-yyyy","^(((0?[1-9]|[12]\\d|3[01])[\\-](0?[13578]|1[02])[\\-]((1[6-9]|[2-9]\\d)?\\d{2}|\\d))|((0?[1-9]|[12]\\d|30)[\\-](0?[13456789]|1[012])[\\-]((1[6-9]|[2-9]\\d)?\\d{2}|\\d))|((0?[1-9]|1\\d|2[0-8])[\\-]0?2[\\-]((1[6-9]|[2-9]\\d)?\\d{2}|\\d))|(29[\\-]0?2[\\-]((1[6-9]|[2-9]\\d)?(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00)|00|[048])))$");
            patterns.put("d-M-yy","^(((0?[1-9]|[12]\\d|3[01])[\\-](0?[13578]|1[02])[\\-]((1[6-9]|[2-9]\\d)?\\d{2}|\\d))|((0?[1-9]|[12]\\d|30)[\\-](0?[13456789]|1[012])[\\-]((1[6-9]|[2-9]\\d)?\\d{2}|\\d))|((0?[1-9]|1\\d|2[0-8])[\\-]0?2[\\-]((1[6-9]|[2-9]\\d)?\\d{2}|\\d))|(29[\\-]0?2[\\-]((1[6-9]|[2-9]\\d)?(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00)|00|[048])))$");
            patterns.put("dd.MMM.yyyy", "^(([0-9])|([0-2][0-9])|([3][0-1])).(Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez).\\d{4}$");
            patterns.put("dd.MMM.yyy", "^(([0-9])|([0-2][0-9])|([3][0-1])).(Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez).\\d{3}$");
            patterns.put("dd.MMM.yy", "^(([0-9])|([0-2][0-9])|([3][0-1])).(Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez).\\d{2}$");
            patterns.put("d.M.yyyy", "([0-3]?\\d\\.{1})([01]?\\d\\.{1})([12]{1}\\d{3})");
            patterns.put("d.M.yy", "([0-3]?\\d\\.{1})([01]?\\d\\.{1})([12]{1}\\d{1})");
            patterns.put("d M yyyy", "([0-3]?\\d\\ {1})([01]?\\d\\ {1})([12]{1}\\d{3})");
            patterns.put("d MM yyyy", "^(0[1-9]|[12][0-9]|3[01])\\ ([01]?\\d\\ {1})([12]{1}\\d{3})");
            patterns.put("d MM yyy", "^(0[1-9]|[12][0-9]|3[01])\\ ([01]?\\d\\ {1})([12]{1}\\d{2})");
            patterns.put("dd MMM yyyy", "^(([0-9])|([0-2][0-9])|([3][0-1])) (Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez) \\d{4}$");
            patterns.put("dd 'de' MMM 'de' yyyy", "^(([0-9])|([0-2][0-9])|([3][0-1])) de (Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez) de \\d{4}$");
            patterns.put("dd 'de' MMM 'de' yyy", "^(([0-9])|([0-2][0-9])|([3][0-1])) de (Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez) de \\d{3}$");
            patterns.put("dd 'DE' MMM 'DE' yyyy", "^(([0-9])|([0-2][0-9])|([3][0-1])) DE (Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez) DE \\d{4}$");
            patterns.put("dd 'de' MMM 'DE' yyyy", "^(([0-9])|([0-2][0-9])|([3][0-1])) de (Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez) DE \\d{4}$");
            patterns.put("dd 'DE' MMM 'de' yyyy", "^(([0-9])|([0-2][0-9])|([3][0-1])) DE (Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez) de \\d{4}$");
            patterns.put("ddMMMyyyy", "^(([0-9])|([0-2][0-9])|([3][0-1]))(Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez)\\d{4}");
            patterns.put("ddMMMyyyyHH:mm:ss", "^(([0-9])|([0-2][0-9])|([3][0-1]))(Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez)\\d{4}(?:[01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]\\b");
            patterns.put("dd/MMM/y.yyy", "^(([0-9])|([0-2][0-9])|([3][0-1]))\\/(Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez)\\/[\\d\\.]{5}");
            patterns.put("dd/MMM/y,yyy", "^(([0-9])|([0-2][0-9])|([3][0-1]))\\/(Jan|Fev|Mar|Abr|Mai|Jun|Jul|Ago|Set|Out|Nov|Dez)\\/[\\d\\,]{5}");
            patterns.put("dd/MM/y,yyy", "^(([0-9])|([0-2][0-9])|([3][0-1]))\\/(0[1-9]|1[012])\\/[\\d\\,]{5}");
            patterns.put("dd/MM/y.yyy", "^(([0-9])|([0-2][0-9])|([3][0-1]))\\/(0[1-9]|1[012])\\/[\\d\\.]{5}");
            patterns.put("yyyy-MM-dd'T'HH:mm:ss'Z'", "^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})Z$");
        }
    }
}
