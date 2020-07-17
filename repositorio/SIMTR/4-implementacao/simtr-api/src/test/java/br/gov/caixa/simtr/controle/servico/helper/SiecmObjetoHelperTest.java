package br.gov.caixa.simtr.controle.servico.helper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import br.gov.caixa.pedesgo.arquitetura.util.UtilUsuario;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.util.CalendarUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.xml.bind.JAXBException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.mockito.Spy;

public class SiecmObjetoHelperTest {

    @Mock
    private UtilUsuario utilUsuario;

    @Spy
    private CalendarUtil calendarUtil = new CalendarUtil();
    
    @InjectMocks
    private SiecmObjetosHelper siecmObjetosHelper;

    private final ObjectMapper mapper = new ObjectMapper();
    private static final String RESOURCE_DIR = "/mock/siecm/";
    private static final String UTF_8 = "UTF-8";

    private String cnhBase64;
    private String enderecoBase64;
    private Documento documentoCNH22199586120;
    private Documento documentoEndereco00013888000105;

    @Before
    public void setup() throws IOException, JAXBException {
        MockitoAnnotations.initMocks(this);

        this.cnhBase64 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("cnh-base64.txt")), UTF_8);
        this.enderecoBase64 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("endereco-base64.txt")), UTF_8);

        String jsonCNH22199586120 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("cnh-22199586120.json")), UTF_8);
        this.documentoCNH22199586120 = mapper.readValue(jsonCNH22199586120, Documento.class);

        String jsonEndereco00013888000105 = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("endereco-00013888000105.json")), UTF_8);
        this.documentoEndereco00013888000105 = mapper.readValue(jsonEndereco00013888000105, Documento.class);

        Mockito.when(this.utilUsuario.getIpUsuario()).thenReturn("127.0.0.1");
    }
    
    @Test
    public void testMontaCamposDocumentoGED() throws IOException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        String jsonEnderecoConfiguracaoInvalida = IOUtils.toString(this.getClass().getResourceAsStream(RESOURCE_DIR.concat("endereco-configuracao-invalida.json")), UTF_8);
        Documento documentoEnderecoConfiguracaoInvalida = mapper.readValue(jsonEnderecoConfiguracaoInvalida, Documento.class);
        
        //Execução de método com conjunto de atributos de valores validos
        try {
            this.siecmObjetosHelper.montaCamposDocumentoSIECM(this.documentoCNH22199586120.getTipoDocumento(), this.documentoCNH22199586120.getAtributosDocumento());
        } catch (Exception ite) {
            Logger.getRootLogger().error(ite.getLocalizedMessage());
            Assert.assertTrue("Montagem dos campos para o SIECM não deve levantar exceção para a definição em teste da CNH", Boolean.FALSE);
        }

        //Execução de método com conjunto de valor contendo conteudo inválido e configuração invalida.
        try {
            this.siecmObjetosHelper.montaCamposDocumentoSIECM(documentoEnderecoConfiguracaoInvalida.getTipoDocumento(), documentoEnderecoConfiguracaoInvalida.getAtributosDocumento());
            Assert.assertTrue("Montagem dos campos para o SIECM deve levantar exceção para a definição em teste com valor invalido", Boolean.FALSE);
        } catch (Exception ite) {
            Assert.assertTrue(Boolean.TRUE);
        }
    }

    @Test
    public void testConverteAtributoGED() throws IOException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Method method = this.siecmObjetosHelper.getClass().getDeclaredMethod("converteAtributoSIECM", TipoAtributoEnum.class, String.class);
        method.setAccessible(Boolean.TRUE);

        //Valor do Atributo definido como nulo
        try {
            method.invoke(this.siecmObjetosHelper, TipoAtributoEnum.BOOLEAN, null);
            Assert.assertTrue("Conversão de atributo com valor nulo deve levantar exceção do tipo IllegalArgumentException", Boolean.FALSE);
        } catch (InvocationTargetException ite) {
            if (!IllegalArgumentException.class.equals(ite.getCause().getClass())) {
                Assert.assertTrue(Boolean.FALSE);
            }
        }

        method.invoke(this.siecmObjetosHelper, TipoAtributoEnum.BOOLEAN, "true");
        method.invoke(this.siecmObjetosHelper, TipoAtributoEnum.DATE, "10/09/2019");
        method.invoke(this.siecmObjetosHelper, TipoAtributoEnum.DECIMAL, "10.5");
        method.invoke(this.siecmObjetosHelper, TipoAtributoEnum.LONG, "10");
        method.invoke(this.siecmObjetosHelper, TipoAtributoEnum.STRING, "teste");
    }
}
