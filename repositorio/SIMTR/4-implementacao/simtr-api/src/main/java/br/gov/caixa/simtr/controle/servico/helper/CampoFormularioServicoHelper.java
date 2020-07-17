package br.gov.caixa.simtr.controle.servico.helper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.controle.excecao.SimtrCadastroException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.GarantiaServico;
import br.gov.caixa.simtr.controle.servico.ProcessoServico;
import br.gov.caixa.simtr.controle.servico.ProdutoServico;
import br.gov.caixa.simtr.controle.servico.TipoRelacionamentoServico;
import br.gov.caixa.simtr.controle.vo.campoformulario.CamposEntradaValidadoVO;
import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.entidade.Garantia;
import br.gov.caixa.simtr.modelo.entidade.OpcaoCampo;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.campoformulario.AlteracaoCadastroDefinicaoCampoFormularioDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class CampoFormularioServicoHelper {

    @Inject
    private EntityManager entityManager;
    
    @EJB
    private ProcessoServico processoServico;
    
    @EJB
    private TipoRelacionamentoServico tipoRelacionamentoServico;
    
    @EJB
    private ProdutoServico produtoServico;
    
    @EJB
    private GarantiaServico garantiaServico;
    
    /**
     * Valida os valores recebidos verificando se existe mais de um ou nenhum parâmetro preenchido.
     * @param idProcessoFase
     * @param idTipoRelacionamento
     * @param idProduto
     * @param idGarantia
     */
    public void validaIntegridadeValoresEntrada(Integer idProcessoFase, Integer idTipoRelacionamento, Integer idProduto, Integer idGarantia) {
        List<Integer> parametros = new ArrayList<>();
        parametros.add(idProcessoFase);
        parametros.add(idTipoRelacionamento);
        parametros.add(idProduto);
        parametros.add(idGarantia);
        
        Integer quantidadesParamentrosRecebidos = 0;
        for(Integer parametro : parametros) {
            if(Objects.nonNull(parametro)) {
                quantidadesParamentrosRecebidos++;
            }
            if(quantidadesParamentrosRecebidos > 1) {
                String mensagem = MessageFormat.format("CFSH.vIVE.001 - Deve ser informado apenas um dos parametro de consulta: processo-dossie {0} | processo-fase {1} | produto {2} | garantia {3}", idProcessoFase, idTipoRelacionamento, idProduto, idGarantia);
                throw new SimtrRequisicaoException(mensagem);
            }
        }
        if(quantidadesParamentrosRecebidos.equals(0)) {
            String mensagem = MessageFormat.format("CFSH.vIVE.002 - Necessário informar um dos parametros de consulta: processo-dossie {0} | processo-fase {1} | produto {2} | garantia {3}", idProcessoFase, idTipoRelacionamento, idProduto, idGarantia);
            throw new SimtrRequisicaoException(mensagem);
        }
    }
    
    /**
     * Verifica se os ids passados retornam objetos de entidades existentes na base de dados.
     * @param idProcessoOrigem
     * @param idProcessoFase
     * @param idTipoRelacionamento
     * @param idProduto
     * @param idGarantia
     * @return
     */
    public CamposEntradaValidadoVO validaExistenciaValoresEntradaBaseDados(Integer idProcessoOrigem, Integer idProcessoFase, Integer idTipoRelacionamento, Integer idProduto, Integer idGarantia) {
        Processo processoOrigem = null;
        if(Objects.nonNull(idProcessoOrigem)) {
            processoOrigem = this.entityManager.find(Processo.class, idProcessoOrigem);
            if(Objects.isNull(processoOrigem)) {
                String mensagem = MessageFormat.format("CFSH.vEVEBD.001 - Processo Originador de Dossiê não localizado sob o identificador informado: {0}", idProcessoOrigem);
                throw new SimtrRecursoDesconhecidoException(mensagem);
            }
        }
        
        Processo processoFase = null;
        if(Objects.nonNull(idProcessoFase)) {
            processoFase = this.processoServico.getById(idProcessoFase);
            if(Objects.isNull(processoFase)) {
                String mensagem = MessageFormat.format("CFSH.vEVEBD.002 - Processo Fase de Dossiê não localizado sob o identificador informado: {0}", idProcessoFase);
                throw new SimtrRecursoDesconhecidoException(mensagem);
            }
        }
        
        TipoRelacionamento tipoRelacionamento = null;
        if(Objects.nonNull(idTipoRelacionamento)) {
            tipoRelacionamento = this.tipoRelacionamentoServico.getById(idTipoRelacionamento);
            if(Objects.isNull(tipoRelacionamento)) {
                String mensagem = MessageFormat.format("CFSH.vEVEBD.003 - Tipo de Relacionamento não localizado sob o identificador informado: {0}", idTipoRelacionamento);
                throw new SimtrRecursoDesconhecidoException(mensagem);
            }
        }
        
        Produto produto = null;
        if(Objects.nonNull(idProduto)) {
            produto = this.produtoServico.getById(idProduto);
            if(Objects.isNull(produto)) {
                String mensagem = MessageFormat.format("CFSH.vEVEBD.004 - Produto não localizado sob o identificador informado: {0}", idProduto);
                throw new SimtrRecursoDesconhecidoException(mensagem);
            }
        }
        
        Garantia garantia = null;
        if(Objects.nonNull(idGarantia)) {
            garantia = this.garantiaServico.getById(idGarantia);
            if(Objects.isNull(garantia)) {
                String mensagem = MessageFormat.format("CFSH.vEVEBD.005 - Garantia não localizada sob o identificador informado: {0}", idGarantia);
                throw new SimtrRecursoDesconhecidoException(mensagem);
            }
        }
        return new CamposEntradaValidadoVO(processoOrigem, processoFase, tipoRelacionamento, produto, garantia);
    }
    
    /**
     * Verifica se já existe respostas de dossiês para o campo de formulário consultado.
     * @param campoFormulario
     * @param alteracaoCadasdroCampoFormularioDTO
     */
    public void verificaExisteRespostasDossieCampoFormularioAtual(CampoFormulario campoFormulario, AlteracaoCadastroDefinicaoCampoFormularioDTO alteracaoCadasdroCampoFormularioDTO) {
        if (Objects.nonNull(campoFormulario.getRespostasDossie())
                && !campoFormulario.getRespostasDossie().isEmpty()
                && this.verificaPermissaoAlteracaoCampoFormulario(alteracaoCadasdroCampoFormularioDTO)) {
            throw new SimtrCadastroException("CFSH.vERDCFA.001 - Já existem respostas de formulário associadas com este campo de formulário. O tipo de campo e as vinculações não poderão ser alterados.");
        }
        
        Set<OpcaoCampo> opcoesCampo = campoFormulario.getCampoEntrada().getOpcoesCampo();
        if (Objects.nonNull(opcoesCampo) 
                && !opcoesCampo.isEmpty()
                && opcoesCampo.stream().anyMatch(opcaoCampo -> Objects.nonNull(opcaoCampo.getRespostasDossie()) && !opcaoCampo.getRespostasDossie().isEmpty())
                && this.verificaPermissaoAlteracaoCampoFormulario(alteracaoCadasdroCampoFormularioDTO)) {
            throw new SimtrCadastroException("CFSH.vERDCFA.002 - Já existem respostas de formulário associadas as opções de campo deste campo de formulário. O tipo de campo e as vinculações não poderão ser alterados.");
        }
    }
    
    /**
     * Retorna verificação da existência de respostas de dossiê para um campo de formulário
     * @param campoFormulario
     * @return
     */
    public Boolean existeRespostasDossieCampoFormularioAtual(CampoFormulario campoFormulario) {
        if (Objects.nonNull(campoFormulario.getRespostasDossie())
                && !campoFormulario.getRespostasDossie().isEmpty()) {
            return true;
        }
        
        Set<OpcaoCampo> opcoesCampo = campoFormulario.getCampoEntrada().getOpcoesCampo();
        return Objects.nonNull(opcoesCampo) && !opcoesCampo.isEmpty()
               && opcoesCampo.stream().anyMatch(opcaoCampo -> Objects.nonNull(opcaoCampo.getRespostasDossie()) && !opcaoCampo.getRespostasDossie().isEmpty());
    }
    
    /**
     * Verifica se objeto recebido no PATCH está preenchido com alguns campos que impedem a atualização
     * devido existir respostas de dossiê para esse campo de formulário.
     * @param alteracaoCadasdroCampoFormularioDTO
     * @return
     */
    private Boolean verificaPermissaoAlteracaoCampoFormulario(AlteracaoCadastroDefinicaoCampoFormularioDTO alteracaoCadasdroCampoFormularioDTO) {
        return Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getTipoCampo())
                || Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getIdentificadorProcessoDossie())
                || Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getIdentificadorProcessoFase())
                || Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getIdentificadorTipoRelacionamento())
                || Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getIdentificadorProduto())
                || Objects.nonNull(alteracaoCadasdroCampoFormularioDTO.getIdentificadorGarantia());
    }
}