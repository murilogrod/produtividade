package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.AvaliacaoOperacaoRespDTO;
import br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.AvaliacaoTomadorRespDTO;
import br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.PropostaOperacaoReqDTO;
import br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.PropostaOperacaoRespDTO;
import br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.PropostaSiricRespDTO;
import br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.PropostaTomadorReqDTO;
import br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.PropostaTomadorRespDTO;
import br.gov.caixa.pedesgo.arquitetura.siric.dto.propostas.QuadroValorPropostaDTO;
import br.gov.caixa.pedesgo.arquitetura.siric.enumerador.IdentificadorTipoPessoaEnum;
import br.gov.caixa.pedesgo.arquitetura.siric.enumerador.TipoSiricEnum;
import br.gov.caixa.pedesgo.arquitetura.siric.exceptions.SiricException;
import br.gov.caixa.pedesgo.arquitetura.siric.servico.SiricServico;
import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.DossieProduto;
import br.gov.caixa.simtr.modelo.entidade.RespostaDossie;
import br.gov.caixa.simtr.modelo.enumerator.CodigoSituacaoEnum;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDossieEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoEventoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric.AvaliacaoOperacaoSimplesPJDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric.AvaliacaoTomadorPJDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric.PropostaOperacaoRespostaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric.PropostaTomadorRespostaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric.ResultadoPropostaOperacaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.siric.ResultadoPropostaTomadorDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

/**
 * 
 * @author f525904
 *
 */

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE,
    ConstantesUtil.PERFIL_MTRSDNTTO,
    ConstantesUtil.PERFIL_MTRSDNTTG
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class ApoioNegocioSiricServico {

    @EJB
    private CanalServico canalServico;

    @EJB
    private SiricServico siricServicoWebService;
    
    @EJB
    private CampoFormularioServico campoFormularioServico;
    
    @EJB 
    private DossieProdutoServico dossieProdutoServico;
    
    @EJB
    private BPMServico bmpServico;
    
    private static final Integer NU_IDENTIFICADOR_BPM_SIRIC_AVALICAO_TOMADOR = 99990001;
    
    private static final Integer NU_IDENTIFICADOR_BPM_SIRIC_AVALICAO_OPERACAO = 99990003;

    public PropostaTomadorRespostaDTO solicitarAvaliacaoTomadorPJ(AvaliacaoTomadorPJDTO avaliacaoTomadorPJDTO) throws SiricException {

        // Captura o canal de comunicação baseado no client
        Canal canal = this.canalServico.getByClienteSSO();

        if (canal == null) {
            throw new SimtrRequisicaoException("ANSS.pATPJ.001 - Canal de comunicação não localizado para codigo de integração fornecido.");
        }

        if (!canal.getIndicadorOutorgaSiric()) {
            throw new SimtrPermissaoException("ANSS.pATPJ.002 - Canal de comunicação não autorizado a solicitar avaliações junto ao SIRIC utilizando a API do SIMTR.");
        }

        PropostaTomadorReqDTO requisicao = this.montarObjetoRequisicaoPropostaTomador(avaliacaoTomadorPJDTO);

        // solicitar avaliacao de tomador ao siric
        PropostaSiricRespDTO resposta = this.siricServicoWebService.realizarProposta(requisicao, TipoSiricEnum.TOMADOR);

        PropostaTomadorRespDTO propostaResposta = null;
        PropostaTomadorRespostaDTO tomadorResposta = null;
        
        if (resposta != null) {
            propostaResposta = (PropostaTomadorRespDTO) resposta;
            
            tomadorResposta = this.montarObjetoPropostaTomadorRespostaDTO(propostaResposta);
        }

        return tomadorResposta;
    }
    
    public PropostaOperacaoRespostaDTO solicitarAvaliacaoOperacaoSimplesPJ(AvaliacaoOperacaoSimplesPJDTO avaliacaoOperacaoPJDTO) throws SiricException {

        // Captura o canal de comunicação baseado no client
        Canal canal = this.canalServico.getByClienteSSO();

        if (canal == null) {
            throw new SimtrRequisicaoException("ANSS.pATPJ.001 - Canal de comunicação não localizado para codigo de integração fornecido.");
        }

        if (!canal.getIndicadorOutorgaSiric()) {
            throw new SimtrPermissaoException("ANSS.pATPJ.002 - Canal de comunicação não autorizado a solicitar avaliações junto ao SIRIC utilizando a API do SIMTR.");
        }

        PropostaOperacaoReqDTO requisicao = this.montarObjetoRequisicaoPropostaOperacaoSimples(avaliacaoOperacaoPJDTO);

        // solicitar avaliacao de operacao ao siric
        PropostaSiricRespDTO resposta = this.siricServicoWebService.realizarProposta(requisicao, avaliacaoOperacaoPJDTO.getCnpj(), TipoSiricEnum.OPERACAO);

        PropostaOperacaoRespDTO propostaResposta = null;
        PropostaOperacaoRespostaDTO operacaoResposta = null;
        
        if (resposta != null) {
            propostaResposta = (PropostaOperacaoRespDTO) resposta;
            
            operacaoResposta = this.montarObjetoPropostaOperacaoRespostaDTO(propostaResposta);
        }

        return operacaoResposta;
    }

    public void processarPropostaCallback(PropostaTomadorRespostaDTO propostaResposta) throws SiricException {
        if(propostaResposta != null) {
            
            if(propostaResposta.getProtocolo() == null) {
                throw new SimtrRequisicaoException("ANSS.pPC.001 - Resultado da Proposta sem Protocolo.");
            }
            
            if(propostaResposta.getResultado() == null) {
                throw new SimtrRequisicaoException("ANSS.pPC.002 - Proposta sem Resultado do Tomador.");  
            }
            
            if(propostaResposta.getResultado().getCodigoAvaliacaoTomador() == null) {
                throw new SimtrRequisicaoException("ANSS.pPC.003 - Resultado sem o Código da Avaliação do Tomador.");  
            }
            
            RespostaDossie respostaDossie = this.campoFormularioServico.retornaRespostaPorProtocoloSiric(propostaResposta.getProtocolo());
            
            if(respostaDossie == null ) {
                throw new SimtrRequisicaoException("ANSS.pPC.004 - Resposta do Dossiê não localizado com protocolo informado.");
            }
            
            Integer idProcessoGerador = respostaDossie.getCampoFormulario().getProcesso().getId();
            Integer idProcessoFase = respostaDossie.getCampoFormulario().getProcessoFase().getId();
            
            // Identificar o campoFormulario que vai receber o codigo da avaliação do tomador
            CampoFormulario campoFormularioSiric = this.campoFormularioServico.retornaCampoFormularioPorProcessoGeradorEProcessoFaseEIdentificadorBPM(idProcessoGerador, idProcessoFase, NU_IDENTIFICADOR_BPM_SIRIC_AVALICAO_TOMADOR);
            
            if(campoFormularioSiric == null) {
            	String msg = MessageFormat.format("ANSS.pPC.005 - Não foi encontrado campo de formulário do Código de Avaliação de Tomador. Identificador BPM = {0}.", NU_IDENTIFICADOR_BPM_SIRIC_AVALICAO_TOMADOR);
                throw new SimtrRequisicaoException(msg);
            }
            
            // Preencher resposta com código  da avalição do tomador
            RespostaDossie respostaAutomaticaSiric = new RespostaDossie();
            respostaAutomaticaSiric.setCampoFormulario(campoFormularioSiric);
            respostaAutomaticaSiric.setDossieProduto(respostaDossie.getDossieProduto());
            respostaAutomaticaSiric.setRespostaAberta(propostaResposta.getResultado().getCodigoAvaliacaoTomador().toString());
            
            // finaliza o dossie produto pra fase  'Alimentação Finalizada'
            String obsevacao = "Recebido Código de Avaliação do Tomador do SIRIC.";
            
            this.dossieProdutoServico.adicionaSituacaoDossieProduto(respostaDossie.getDossieProduto().getId(), SituacaoDossieEnum.ALIMENTACAO_FINALIZADA, obsevacao);
            
            // persiste nova respostaDossie automatica do siric
            this.campoFormularioServico.inseriRespostaDossie(respostaAutomaticaSiric);
            
            // sinalizar BPM
            this.notificaBPM(respostaDossie.getDossieProduto().getId());
        }
    }
    
    public void processarPropostaCallbackOperacao(PropostaOperacaoRespostaDTO propostaRespostaOperacao) throws SiricException {
        if(propostaRespostaOperacao != null) {
            
            if(propostaRespostaOperacao.getProtocolo() == null) {
                throw new SimtrRequisicaoException("ANSS.pPCO.001 - Resultado da Proposta sem Protocolo.");
            }
            
            if(propostaRespostaOperacao.getResultado() == null) {
                throw new SimtrRequisicaoException("ANSS.pPCO.002 - Proposta sem Resultado da Operação.");  
            }
            
            if(propostaRespostaOperacao.getResultado().getCodigoAvalicaoOperacao() == null) {
                throw new SimtrRequisicaoException("ANSS.pPCO.004 - Resultado sem o Código da Avaliação da Operação.");  
            }
            
            RespostaDossie respostaDossie = this.campoFormularioServico.retornaRespostaPorProtocoloSiric(propostaRespostaOperacao.getProtocolo());
            
            if(respostaDossie == null ) {
                throw new SimtrRequisicaoException("ANSS.pPCO.004 - Resposta do Dossiê não localizado com protocolo informado.");
            }
            
            Integer idProcessoGerador = respostaDossie.getCampoFormulario().getProcesso().getId();
            Integer idProcessoFase = respostaDossie.getCampoFormulario().getProcessoFase().getId();
            
            // Identificar o campoFormulario que vai receber o codigo da avaliação do operacao
            CampoFormulario campoFormularioAvaliacaoOperacao = this.campoFormularioServico.retornaCampoFormularioPorProcessoGeradorEProcessoFaseEIdentificadorBPM(idProcessoGerador, idProcessoFase, NU_IDENTIFICADOR_BPM_SIRIC_AVALICAO_OPERACAO);
            
            if(campoFormularioAvaliacaoOperacao == null) {
            	String msg = MessageFormat.format("ANSS.pPCO.005 - Não foi encontrado campo de formulário do Código de Avaliação de Operação. Identificador BPM = {0}.", NU_IDENTIFICADOR_BPM_SIRIC_AVALICAO_OPERACAO);
                throw new SimtrRequisicaoException(msg);
            }
            
            // Preencher resposta com código  da avalição da operacao
            RespostaDossie respostaAutomaticaAvalicaoOperacaoSiric = new RespostaDossie();
            respostaAutomaticaAvalicaoOperacaoSiric.setCampoFormulario(campoFormularioAvaliacaoOperacao);
            respostaAutomaticaAvalicaoOperacaoSiric.setDossieProduto(respostaDossie.getDossieProduto());
            respostaAutomaticaAvalicaoOperacaoSiric.setRespostaAberta(propostaRespostaOperacao.getResultado().getCodigoAvalicaoOperacao().toString());
	        
	        // finaliza o dossie produto pra fase  'Alimentação Finalizada'
            String obsevacao = "Recebido Código de Avaliação da Operacao do SIRIC.";
            
            this.dossieProdutoServico.adicionaSituacaoDossieProduto(respostaDossie.getDossieProduto().getId(), SituacaoDossieEnum.ALIMENTACAO_FINALIZADA, obsevacao);
            
            // persiste nova respostaDossie automatica avaliação da operação do siric
            this.campoFormularioServico.inseriRespostaDossie(respostaAutomaticaAvalicaoOperacaoSiric);
            
            // sinalizar BPM
            this.notificaBPM(respostaDossie.getDossieProduto().getId());
        }
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void notificaBPM(Long idDossieProduto) {
        DossieProduto dossieProduto = this.dossieProdutoServico.getById(idDossieProduto);
        this.bmpServico.notificaBPM(dossieProduto);
    }
    
    @PermitAll
    public List<AvaliacaoTomadorRespDTO> consultarResultadoAvalicaoTomador(String cpfcnpj, String codigoAvaliacao) throws SiricException {
        List<AvaliacaoTomadorRespDTO> listaAvaliacoes = this.siricServicoWebService.consultaAvaliacaoTomador(cpfcnpj, codigoAvaliacao);
        return listaAvaliacoes;
    }

    @PermitAll
    public List<AvaliacaoTomadorRespDTO> consultarResultadoAvalicaoTomador(String cnpj) throws SiricException {
        List<AvaliacaoTomadorRespDTO> listaAvaliacoes = this.siricServicoWebService.consultaAvaliacaoTomador(cnpj, null);
        return listaAvaliacoes;
    }
    
    @PermitAll
    public List<AvaliacaoOperacaoRespDTO> consultarResultadoAvalicaoOperacao(String cpfcnpj, String codigoAvaliacao) throws SiricException {
        List<AvaliacaoOperacaoRespDTO> listaAvaliacoes = this.siricServicoWebService.consultaAvaliacaoOperacao(cpfcnpj, codigoAvaliacao);
        return listaAvaliacoes;
    }

    @PermitAll
    public List<AvaliacaoOperacaoRespDTO> consultarResultadoAvalicaoOperacao(String cnpj) throws SiricException {
        List<AvaliacaoOperacaoRespDTO> listaAvaliacoes = this.siricServicoWebService.consultaAvaliacaoOperacao(cnpj, null);
        return listaAvaliacoes;
    }

    public PropostaTomadorReqDTO montarObjetoRequisicaoPropostaTomador(AvaliacaoTomadorPJDTO avaliacaoTomadorPJDTO) {
        PropostaTomadorReqDTO requisicao = new PropostaTomadorReqDTO();

        requisicao.setCallbackUrl(avaliacaoTomadorPJDTO.getCallbackUrl());
        requisicao.setClasseConsultaSicli(avaliacaoTomadorPJDTO.getClasseConsultaSicli());
        requisicao.setCodigoPontoAtendimento(avaliacaoTomadorPJDTO.getCodigoPontoAtendimento());

        List<QuadroValorPropostaDTO> quadrosValor = new ArrayList<>();

        avaliacaoTomadorPJDTO.getQuadros().forEach(quadro -> {
            QuadroValorPropostaDTO quadroProposta = new QuadroValorPropostaDTO();

            quadroProposta.setCodigoQuadroValor(quadro.getCodigoQuadroValor());
            quadroProposta.setIdentificacaoPessoa(quadro.getIdentificadorPessoa());

            if(quadro.getIdentificadorTipoPessoa() != null) {
                IdentificadorTipoPessoaEnum tipoPessoa = IdentificadorTipoPessoaEnum.fromValue(quadro.getIdentificadorTipoPessoa().getIdentificador());
                quadroProposta.setIdentificadorTipoPessoa(tipoPessoa);
            }

            quadroProposta.setLinhasQuadroValor(quadro.getLinhasQuadroValor());

            quadrosValor.add(quadroProposta);
        });

        requisicao.setQuadros(quadrosValor);
        return requisicao;
    }
    
    public PropostaOperacaoReqDTO montarObjetoRequisicaoPropostaOperacaoSimples(AvaliacaoOperacaoSimplesPJDTO avaliacaoOperacaoPJDTO) {
        PropostaOperacaoReqDTO requisicao = new PropostaOperacaoReqDTO();

        requisicao.setCallbackUrl(avaliacaoOperacaoPJDTO.getCallbackUrl());
        requisicao.setClasseConsultaSicli(avaliacaoOperacaoPJDTO.getClasseConsultaSicli());
        requisicao.setCodigoPontoAtendimento(avaliacaoOperacaoPJDTO.getCodigoPontoAtendimento());
        requisicao.setPrestacaoNecessariaFinanciamento(avaliacaoOperacaoPJDTO.getPrestacaoNecessariaFinanciamento());
        requisicao.setValorFinanciamento(avaliacaoOperacaoPJDTO.getValorFinanciamento());
        requisicao.setCodigoProposta(avaliacaoOperacaoPJDTO.getCodigoProposta());
        requisicao.setCodigoProduto(avaliacaoOperacaoPJDTO.getCodigoProduto());
        requisicao.setIndicadorTomador(avaliacaoOperacaoPJDTO.getIndicadorTomador());
        requisicao.setTaxaJuros(avaliacaoOperacaoPJDTO.getTaxaJuros());
        requisicao.setPrazoMeses(avaliacaoOperacaoPJDTO.getPrazoMeses());
        requisicao.setQuadros(new ArrayList<>());
        
        return requisicao;
    }
    
    public PropostaTomadorRespostaDTO montarObjetoPropostaTomadorRespostaDTO(PropostaTomadorRespDTO propostaResposta) {
        PropostaTomadorRespostaDTO tomadorResposta = null;
        
        if(propostaResposta != null) {
            tomadorResposta = new PropostaTomadorRespostaDTO();
            
            tomadorResposta.setProtocolo(propostaResposta.getProtocolo());
            tomadorResposta.setTipoEvento(TipoEventoEnum.fromValue(propostaResposta.getTipoEvento().getValue()));
            
            if(propostaResposta.getResultado() != null) {
                ResultadoPropostaTomadorDTO resultado = new ResultadoPropostaTomadorDTO();
                
                if(propostaResposta.getResultado().getCodigoAvaliacaoTomador() != null) {
                    resultado.setCodigoAvaliacaoTomador(propostaResposta.getResultado().getCodigoAvaliacaoTomador());
                }
                
                if(propostaResposta.getResultado().getCodigoSituacao() != null) {
                    resultado.setCodigoSituacao(CodigoSituacaoEnum.fromValue(propostaResposta.getResultado().getCodigoSituacao().getValue()));
                }
                
                resultado.setResultadoValidacao(propostaResposta.getResultado().getResultadoValidacao());

                tomadorResposta.setResultado(resultado);
            }
        }
        
        return tomadorResposta;
    }
    
    public PropostaOperacaoRespostaDTO montarObjetoPropostaOperacaoRespostaDTO(PropostaOperacaoRespDTO propostaResposta) {
        PropostaOperacaoRespostaDTO operacaoResposta = null;
        
        if(propostaResposta != null) {
            operacaoResposta = new PropostaOperacaoRespostaDTO();
            
            operacaoResposta.setProtocolo(propostaResposta.getProtocolo());
            operacaoResposta.setTipoEvento(TipoEventoEnum.fromValue(propostaResposta.getTipoEvento().getValue()));
            
            if(propostaResposta.getResultado() != null) {
                ResultadoPropostaOperacaoDTO resultado = new ResultadoPropostaOperacaoDTO();
                
                if(propostaResposta.getResultado().getCodigoAvaliacaoTomador() != null) {
                    resultado.setCodigoAvaliacaoTomador(propostaResposta.getResultado().getCodigoAvaliacaoTomador());
                }
                
                if(propostaResposta.getResultado().getCodigoSituacao() != null) {
                    resultado.setCodigoSituacao(CodigoSituacaoEnum.fromValue(propostaResposta.getResultado().getCodigoSituacao().getValue()));
                }
                
                if(propostaResposta.getResultado().getCodigoAvaliacaoOperacao() != null) {
                    resultado.setCodigoAvalicaoOperacao(propostaResposta.getResultado().getCodigoAvaliacaoOperacao());
                }
                
                resultado.setResultadoValidacao(propostaResposta.getResultado().getResultadoValidacao());
            }
        }
        
        return operacaoResposta;
    }
}
