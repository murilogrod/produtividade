package br.gov.caixa.simtr.controle.servico;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.caixa.simtr.controle.servico.helper.CadastroHelper;
import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;

public class TipoRelacionamentoServicoTest {
    
    @InjectMocks
    TipoRelacionamentoServico servico;
    
    @Mock
    private EntityManager entityManager;
    
    @Mock
    TypedQuery<TipoRelacionamento> queryEntidade;
    
    @Mock
    TypedQuery<Long> queryLong;
    
    @Mock
    CadastroHelper cadastroHelper;
    
    public TipoRelacionamento mocNovoTipoRelacionamento(String nome){
        TipoRelacionamento entidade = new TipoRelacionamento();
        entidade.setId(1);
        entidade.setNome(nome);
        entidade.setTipoPessoaEnum(TipoPessoaEnum.F);
        entidade.setIndicadorPrincipal(Boolean.TRUE);
        entidade.setIndicadorRelacionado(Boolean.TRUE);
        entidade.setIndicadorSequencia(Boolean.TRUE);
        entidade.setIndicadorReceitaPessoaFisica(Boolean.TRUE);
        entidade.setIndicadorReceitaPessoaJuridica(Boolean.TRUE);
        return entidade;
    }
    
    @Before
    public void init() throws IOException {
        MockitoAnnotations.initMocks(this);
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(TipoRelacionamento.class))).thenReturn(queryEntidade);
    }
    
    @Test
    public void insereNovoTodosAtributos() {
        String nome = "Novo Tipo Relacionamento";
        TipoRelacionamento novo = this.mocNovoTipoRelacionamento(nome);
        
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Long.class))).thenReturn(this.queryLong);
        Mockito.when(this.queryLong.getSingleResult()).thenReturn(0L);
        TipoRelacionamento tipoRelacionamento = new TipoRelacionamento();
        tipoRelacionamento.setNome("Teste");
        boolean existeComOutroNome = this.servico.existeTipoRelacionamentoComNome(tipoRelacionamento);
        this.servico.criaNovoTipoRelacionamento(novo);
        
        Assert.assertEquals(existeComOutroNome, false);
    }
    
    @Test
    public void insereNovoComNomeJaExistented() {
        String nome = "Novo Tipo Relacionamento";
        TipoRelacionamento novo = this.mocNovoTipoRelacionamento(nome);
        
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Long.class))).thenReturn(this.queryLong);
        Mockito.when(this.queryLong.getSingleResult()).thenReturn(1L);
        
        this.servico.criaNovoTipoRelacionamento(novo);
    }
    
    @Test
    public void insereNovoComNomeMaiorDe50Caracter() {
        String nome = "Novo Tipo Relacionamento Novo Tipo Relacionamento Novo Tipo Relacionamento Novo Tipo Relacionamento";
        TipoRelacionamento novo = this.mocNovoTipoRelacionamento(nome);
        
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Long.class))).thenReturn(this.queryLong);
        Mockito.when(this.queryLong.getSingleResult()).thenReturn(0L);
        
        this.servico.criaNovoTipoRelacionamento(novo);
    }
    
    @Test
    public void insereNovoComIndicadorRelacionamentoIgualNull() {
        String nome = "Novo Tipo Relacionamento";
        TipoRelacionamento novo = this.mocNovoTipoRelacionamento(nome);
        novo.setIndicadorRelacionado(null);
        
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Long.class))).thenReturn(this.queryLong);
        Mockito.when(this.queryLong.getSingleResult()).thenReturn(0L);
        
        this.servico.criaNovoTipoRelacionamento(novo);
    }
    
    @Test
    public void insereNovoComIndicadorSequenciaIgualNull() {
        String nome = "Novo Tipo Relacionamento";
        TipoRelacionamento novo = this.mocNovoTipoRelacionamento(nome);
        novo.setIndicadorSequencia(null);
        
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Long.class))).thenReturn(this.queryLong);
        Mockito.when(this.queryLong.getSingleResult()).thenReturn(0L);
        
        this.servico.criaNovoTipoRelacionamento(novo);
    }
    
    @Test
    public void insereNovoComIndicadorPrincipalIgualNull() {
        String nome = "Novo Tipo Relacionamento";
        TipoRelacionamento novo = this.mocNovoTipoRelacionamento(nome);
        novo.setIndicadorPrincipal(null);
        
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Long.class))).thenReturn(this.queryLong);
        Mockito.when(this.queryLong.getSingleResult()).thenReturn(0L);
        
        this.servico.criaNovoTipoRelacionamento(novo);
    }
    
    @Test
    public void insereNovoComReceitaPessoaFisicaIgualNull() {
        String nome = "Novo Tipo Relacionamento";
        TipoRelacionamento novo = this.mocNovoTipoRelacionamento(nome);
        novo.setIndicadorReceitaPessoaFisica(null);
        
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Long.class))).thenReturn(this.queryLong);
        Mockito.when(this.queryLong.getSingleResult()).thenReturn(0L);
        
        this.servico.criaNovoTipoRelacionamento(novo);
    }
    
    @Test
    public void insereNovoComReceitaPessoaJuridicaIgualNull() {
        String nome = "Novo Tipo Relacionamento";
        TipoRelacionamento novo = this.mocNovoTipoRelacionamento(nome);
        novo.setIndicadorReceitaPessoaJuridica(null);
        
        Mockito.when(this.entityManager.createQuery(Mockito.anyString(), Mockito.eq(Long.class))).thenReturn(this.queryLong);
        Mockito.when(this.queryLong.getSingleResult()).thenReturn(0L);
        
        this.servico.criaNovoTipoRelacionamento(novo);
    }
}
