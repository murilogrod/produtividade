package br.gov.caixa.simtr.controle.servico;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import br.gov.caixa.pedesgo.arquitetura.util.UtilRest;
import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.modelo.entidade.Canal;

@RunWith(PowerMockRunner.class)
@PrepareForTest({System.class, UtilRest.class})
public class AdministracaoServicoTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private DataSource mockDataSource;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private Statement mockStatement;

    @Mock
    private ResultSetMetaData mockResultSetMetaData;

    @InjectMocks
    private AdministracaoServico administracaoServico;
    
    @Mock
    private CanalServico canalServico;

    private final Level logLevelALL = Level.ALL;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        PowerMockito.mockStatic(System.class, UtilRest.class);
        
        
        
        Mockito.when(this.mockDataSource.getConnection()).thenReturn(this.mockConnection);
        Mockito.when(this.mockStatement.executeQuery(Mockito.anyString())).thenReturn(this.mockResultSet);
        Mockito.when(this.mockConnection.createStatement()).thenReturn(this.mockStatement);

        Mockito.when(this.mockResultSet.next()).thenReturn(Boolean.TRUE).thenReturn(Boolean.FALSE);
        Mockito.when(this.mockResultSet.getObject(1)).thenReturn("valor-coluna-1");
        Mockito.when(this.mockResultSet.getObject(2)).thenReturn("valor-coluna-2");
        Mockito.when(this.mockResultSetMetaData.getColumnCount()).thenReturn(2);
        Mockito.when(this.mockResultSetMetaData.getColumnLabel(1)).thenReturn("coluna1");
        Mockito.when(this.mockResultSetMetaData.getColumnLabel(2)).thenReturn("coluna2");
        Mockito.when(this.mockResultSet.getMetaData()).thenReturn(this.mockResultSetMetaData);
    }

    @Test
    public void testExecutaConsultaAdministrativa() throws SQLException {
        this.administracaoServico.executaConsultaAdministrativa("SELECT * FROM tabela", this.logLevelALL);
    }

    @Test
    public void testExecutaConsultaAdministrativaException() throws Exception {
        String json;

        Mockito.doThrow(new SQLException("SQLException Lançada para Teste - mockDataSource.getConnection()"))
                .doReturn(this.mockConnection)
                .when(this.mockDataSource).getConnection();

        Mockito.doThrow(new SQLException("SQLException Lançada para Teste - mockConnection.createStatement()"))
                .doReturn(this.mockStatement)
                .when(this.mockConnection).createStatement();

        Mockito.doThrow(new SQLException("SQLException Lançada para Teste - mockStatement.executeQuery(String)"))
                .doReturn(this.mockResultSet)
                .when(this.mockStatement).executeQuery(Mockito.anyString());

        Mockito.doThrow(new SQLException("SQLException Lançada para Teste - mockResultSet.next()"))
                .doReturn(Boolean.TRUE)
                .when(this.mockResultSet).next();

        Mockito.doThrow(new SQLException("SQLException Lançada para Teste - mockResultSet.getMetaData()"))
                .doReturn(this.mockResultSetMetaData)
                .when(this.mockResultSet).getMetaData();

        Mockito.doThrow(new SQLException("SQLException Lançada para Teste - mockResultSetMetaData.getColumnCount()"))
                .doReturn(2)
                .when(this.mockResultSetMetaData).getColumnCount();

        Mockito.doThrow(new SQLException("SQLException Lançada para Teste - mockResultSetMetaData.getColumnLabel(int)"))
                .doReturn("Label do Atributo")
                .when(this.mockResultSetMetaData).getColumnLabel(Mockito.anyInt());

        Mockito.doThrow(new SQLException("SQLException Lançada para Teste - mockResultSet.getObject(int)"))
                .doReturn("Valor do Atributo")
                .when(this.mockResultSet).getObject(Mockito.anyInt());

        json = this.administracaoServico.executaConsultaAdministrativa("SELECT * FROM tabela", this.logLevelALL).toString();
        Assert.assertTrue("Consulta Administrativa - Exceção esperada SQLException lançada pelo DataSource não capturada para o metodo getConnection().", json.contains("SQLException"));

        json = this.administracaoServico.executaConsultaAdministrativa("SELECT * FROM tabela", this.logLevelALL).toString();
        Assert.assertTrue("Consulta Administrativa - Exceção esperada SQLException lançada pela Connection não capturada para o metodo createStatement().", json.contains("SQLException"));

        json = this.administracaoServico.executaConsultaAdministrativa("SELECT * FROM tabela", this.logLevelALL).toString();
        Assert.assertTrue("Consulta Administrativa - Exceção esperada SQLException lançada pelo Statement não capturada para o metodo executeQuery(String).", json.contains("SQLException"));

        json = this.administracaoServico.executaConsultaAdministrativa("SELECT * FROM tabela", this.logLevelALL).toString();
        Assert.assertTrue("Consulta Administrativa - Exceção esperada SQLException lançada pelo ResultSet não capturada para o metodo next().", json.contains("SQLException"));

        json = this.administracaoServico.executaConsultaAdministrativa("SELECT * FROM tabela", this.logLevelALL).toString();
        Assert.assertTrue("Consulta Administrativa - Exceção esperada SQLException lançada pelo ResultSet não capturada para o metodo getMetaData().", json.contains("SQLException"));

        json = this.administracaoServico.executaConsultaAdministrativa("SELECT * FROM tabela", this.logLevelALL).toString();
        Assert.assertTrue("Consulta Administrativa - Exceção esperada SQLException lançada pelo ResultSetMetaData não capturada para o metodo getColumnCount().", json.contains("SQLException"));

        json = this.administracaoServico.executaConsultaAdministrativa("SELECT * FROM tabela", this.logLevelALL).toString();
        Assert.assertTrue("Consulta Administrativa - Exceção esperada SQLException lançada pelo ResultSetMetaData não capturada para o metodo getColumnLabel(int).", json.contains("SQLException"));

        json = this.administracaoServico.executaConsultaAdministrativa("SELECT * FROM tabela", this.logLevelALL).toString();
        Assert.assertTrue("Consulta Administrativa - Exceção esperada SQLException lançada pelo ResultSetMetaData não capturada para o metodo getObject(int).", json.contains("SQLException"));
    }

    @Test
    public void testExecutaComandoAdministrativo() throws SQLException {
        Mockito.when(this.mockStatement.executeUpdate(Mockito.anyString())).thenReturn(1);
        Mockito.when(this.mockConnection.createStatement()).thenReturn(this.mockStatement);

        this.administracaoServico.executaComandoAdministrativo("DELETE FROM tabela WHERE condicao = true", this.logLevelALL);
    }

    @Test
    public void testExecutaComandoAdministrativoException() throws Exception {
        String json;

        Mockito.doThrow(new SQLException("SQLException Lançada para Teste - mockDataSource.getConnection()"))
                .doReturn(this.mockConnection)
                .when(this.mockDataSource).getConnection();

        Mockito.doThrow(new SQLException("SQLException Lançada para Teste - mockConnection.createStatement()"))
                .doReturn(this.mockStatement)
                .when(this.mockConnection).createStatement();

        Mockito.doThrow(new SQLException("SQLException Lançada para Teste - mockStatement.executeUpdatex(String)"))
                .doReturn(2)
                .when(this.mockStatement).executeUpdate(Mockito.anyString());

        json = this.administracaoServico.executaComandoAdministrativo("DELETE FROM tabela WHERE condicao = true", this.logLevelALL).toString();
        Assert.assertTrue("Exceção esperada SQLException não capturada para o metodo getConnection().", json.contains("SQLException"));

        json = this.administracaoServico.executaComandoAdministrativo("DELETE FROM tabela WHERE condicao = true", this.logLevelALL).toString();
        Assert.assertTrue("Exceção esperada SQLException não capturada para o metodo createStatement().", json.contains("SQLException"));

        json = this.administracaoServico.executaComandoAdministrativo("DELETE FROM tabela WHERE condicao = true", this.logLevelALL).toString();
        Assert.assertTrue("Exceção esperada SQLException não capturada para o metodo executeUpdate().", json.contains("SQLException"));

    }
    
    
    @Test(expected = SimtrPermissaoException.class)
    public void consumirServicoApiManagerCanalFalhaTest() {
    	mockCanalInvalido();
    	this.administracaoServico.consumirServicoApiManager("GET", "endpoint", null, null);
    }
    
    @Test(expected = SimtrPermissaoException.class)
    public void consumirServicoApiManagerCanalValidoTest() {
    	mockCanalValido(false);
    	this.administracaoServico.consumirServicoApiManager("GET", "endpoint", null, null);
    }
    
    
    @Test(expected = NullPointerException.class)
    public void consumirServicoApiManagerTest() {
    	mockCanalValido(true);
    	mockGetProperty();
    	mockUtilRest();
    	this.administracaoServico.consumirServicoApiManager("GET", "endpoint", null, null);
    }
    
    private void mockUtilRest() {
		PowerMockito.when(UtilRest.consumirServicoOAuth2JSON(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(null);
		
	}

	private void mockGetProperty() {
	    PowerMockito.when(System.getProperty(Mockito.anyString())).thenReturn("property");
    }
    
    private void mockCanalValido(Boolean outorga) {
    	Canal canal = new Canal();
    	canal.setIndicadorOutorgaApimanager(outorga);
        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(canal);
    }

    private void mockCanalInvalido() {
        Mockito.when(this.canalServico.getByClienteSSO()).thenReturn(null);
    }
    
}
