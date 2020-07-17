package br.gov.caixa.simtr.controle.servico;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import br.gov.caixa.simtr.controle.excecao.DossieException;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.funcaodocumental.FuncaoDocumentalManutencaoDTO;
import io.jsonwebtoken.lang.Assert;

public class FuncaoDocumentalServicoTest {

    @Mock
    private EntityManager manager;

    @Mock
    private TypedQuery<FuncaoDocumental> funcaoDocumentalQuery;

    @Mock
    private TypedQuery<TipoDocumento> tipoDocumentoQuery;

    @Mock
    private TypedQuery<Calendar> calendarQuery;
    
    @Mock
    private Query query;

    @Mock
    private Logger logger;
    
    @InjectMocks
    private FuncaoDocumentalServico servico;

    @Mock
    private TipoDocumentoServico tipoDocumentoServico;

    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void getFuncaoByNomeTestSucess() {
        FuncaoDocumental funcao = criarFuncaoDocumentaoApoio();
        mockTypedQuery();
        mockResultListFuncaoDocumental();
        assertNotNull(servico.getByNome(funcao.getNome()));
    }
    
    @Test
    public void listTest() {
        FuncaoDocumental funcao = criarFuncaoDocumentaoApoio();
        mockTypedQuery();
        mockResultListFuncaoDocumental();
        assertNotNull(servico.list());
    }

    @Test
    public void getFuncaoByIdTestSucess() {
        FuncaoDocumental funcao = criarFuncaoDocumentaoApoio();
        mockTypedQuery();
        servico.getById(funcao.getId());
    }

    @Test
    public void aplicarPatchTest() {
        TipoDocumento tipoDocumento = this.mockTipoDocumento();
        mockTypedQuery();
        mockResultListFuncaoDocumental();
        when(servico.findById(1)).thenReturn(new FuncaoDocumental());

        List<Integer> listaTiposExclusao = new ArrayList<>();
        listaTiposExclusao.add(10);

        List<Integer> listaTiposInclusao = new ArrayList<>();
        listaTiposInclusao.add(20);

        FuncaoDocumentalManutencaoDTO funcaoDocumentalManutencaoDTO = new FuncaoDocumentalManutencaoDTO();
        funcaoDocumentalManutencaoDTO.setNome("NovoNome");
        funcaoDocumentalManutencaoDTO.setIndicadorApoioNegocio(Boolean.TRUE);
        funcaoDocumentalManutencaoDTO.setIndicadorDossieDigital(Boolean.TRUE);
        funcaoDocumentalManutencaoDTO.setIndicadorProcessoAdministrativo(Boolean.FALSE);
        funcaoDocumentalManutencaoDTO.setIdentificadoresTipoDocumentoExclusaoVinculo(listaTiposExclusao);
        funcaoDocumentalManutencaoDTO.setIdentificadoresTipoDocumentoInclusaoVinculo(listaTiposInclusao);

        when(tipoDocumentoServico.getById(Mockito.anyInt())).thenReturn(tipoDocumento);
        servico.aplicaPatch(1, funcaoDocumentalManutencaoDTO);
    }
    
    
    @Test
    public void findByIdTest() {
    	when(this.manager.createQuery(anyString(), eq(FuncaoDocumental.class))).thenReturn(funcaoDocumentalQuery);
    	mockSingleException();
    	servico.findById(1);
    }

    @Test
    public void getDataHoraUltimaAtualizacaoTest() {
        Assert.isNull(servico.getDataHoraUltimaAlteracao());
    }
    
    @Test
    public void deleteTeste() {
    	mockQuery();
    	servico.delete(1);
    }
    
    @Test(expected = DossieException.class)
    public void deleteTestException() {
    	mockUpdateException();
    	servico.delete(1);
    }

    
    
    
    private void mockQuery() {
		Mockito.when(this.manager.createQuery(Mockito.anyString())).thenReturn(query);
		
	}

	private FuncaoDocumental criarFuncaoDocumentaoApoio() {
        FuncaoDocumental funcao = new FuncaoDocumental();
        funcao.setNome("Funcao teste");
        funcao.setUsoApoioNegocio(Boolean.TRUE);
        funcao.setUsoDossieDigital(Boolean.FALSE);
        funcao.setUsoProcessoAdministrativo(Boolean.FALSE);
        return funcao;
    }

    private FuncaoDocumental criarFuncaoDocumentaoDossie() {
        FuncaoDocumental funcao = new FuncaoDocumental();
        funcao.setId(1);
        funcao.setNome("Funcao teste");
        funcao.setUsoApoioNegocio(Boolean.FALSE);
        funcao.setUsoDossieDigital(Boolean.TRUE);
        funcao.setUsoProcessoAdministrativo(Boolean.FALSE);
        TipoDocumento tipoDoc = new TipoDocumento();
        tipoDoc.setId(1);
        Set<TipoDocumento> tipos = new HashSet<>();
        tipos.add(tipoDoc);
        funcao.setTiposDocumento(tipos);
        return funcao;
    }

    private void mockTypedQuery() {
        when(this.manager.createQuery(anyString(), eq(TipoDocumento.class))).thenReturn(tipoDocumentoQuery);
        when(this.manager.createQuery(anyString(), eq(FuncaoDocumental.class))).thenReturn(funcaoDocumentalQuery);
        when(this.manager.createQuery(anyString(), eq(Calendar.class))).thenReturn(calendarQuery);
        when(calendarQuery.getSingleResult()).thenReturn(Calendar.getInstance());
    }

    private void mockResultListFuncaoDocumental() {
        List<FuncaoDocumental> funcoes = new ArrayList<>();
        funcoes.add(criarFuncaoDocumentaoDossie());
        when(funcaoDocumentalQuery.getResultList()).thenReturn(funcoes);
    }
    
    private void mockUpdateException() {
        Mockito.doThrow(new org.hibernate.exception.ConstraintViolationException("Mensagem", null, null)).when(this.manager).createQuery(Mockito.anyString());
    }
    
    private void mockSingleException() {
        Mockito.doThrow(new NoResultException("Mensagem")).when(this.query).getSingleResult();
    }

    private TipoDocumento mockTipoDocumento() {
        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.setId(10);
        tipoDocumento.setDataHoraUltimaAlteracao(Calendar.getInstance());
        tipoDocumento.setEnviaAvaliacaoCadastral(Boolean.FALSE);
        tipoDocumento.setEnviaAvaliacaoDocumental(Boolean.FALSE);
        tipoDocumento.setEnviaExtracaoExterna(Boolean.TRUE);
        tipoDocumento.setNome("Tipo Documento 10");
        tipoDocumento.setPermiteExtracaoM0(Boolean.FALSE);
        tipoDocumento.setReuso(Boolean.TRUE);
        tipoDocumento.setTipoPessoaEnum(TipoPessoaEnum.A);
        tipoDocumento.setUsoApoioNegocio(Boolean.TRUE);
        tipoDocumento.setUsoDossieDigital(Boolean.TRUE);
        tipoDocumento.setUsoProcessoAdministrativo(Boolean.FALSE);

        return tipoDocumento;
    }

}
