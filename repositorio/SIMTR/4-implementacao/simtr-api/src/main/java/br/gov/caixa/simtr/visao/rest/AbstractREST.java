package br.gov.caixa.simtr.visao.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ejb.EJBAccessException;
import javax.ejb.EJBException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.exception.ExceptionUtils;

import br.gov.caixa.pedesgo.arquitetura.siric.exceptions.SiricException;
import br.gov.caixa.simtr.controle.excecao.BpmException;
import br.gov.caixa.simtr.controle.excecao.PortalEmpreendedorException;
import br.gov.caixa.simtr.controle.excecao.SicliException;
import br.gov.caixa.simtr.controle.excecao.SicpfException;
import br.gov.caixa.simtr.controle.excecao.SiecmException;
import br.gov.caixa.simtr.controle.excecao.SifrcException;
import br.gov.caixa.simtr.controle.excecao.SiisoException;
import br.gov.caixa.simtr.controle.excecao.SimtrAtributoIntegracaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrCadastroException;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrEstadoImpeditivoException;
import br.gov.caixa.simtr.controle.excecao.OutsourcingException;
import br.gov.caixa.simtr.controle.excecao.SimtrPermissaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrPreCondicaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoBloqueadoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRecursoDesconhecidoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.excecao.SipesException;
import br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.PendenciaCadastroDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.comum.RetornoErroDTO;

public abstract class AbstractREST {

    protected Response montaRespostaExcecao(Throwable cause, String prefixo) {

        Throwable rootCause = cause;
        List<String> falhas = new ArrayList<>();
        while (Objects.nonNull(rootCause.getCause())) {
            falhas.add(rootCause.getLocalizedMessage());
            rootCause = rootCause.getCause();
        }
        falhas.add(rootCause.getLocalizedMessage());

        RetornoErroDTO resourceError = new RetornoErroDTO();

        //Determina o codigo HTTP de retorno baseado nas definições da exceção
        Status statusCode = Status.INTERNAL_SERVER_ERROR;
        String mensagem = "Falha não mapeada ao processar a requisição.";

        if (BpmException.class.equals(cause.getClass())) {
            BpmException be = (BpmException) cause;
            statusCode = be.isServicoDisponivel() ? Status.BAD_GATEWAY : Status.SERVICE_UNAVAILABLE;
            mensagem = "Falha na comunicação com o serviço de automação de processos (jBPM).";
        } else if (SicliException.class.equals(cause.getClass())) {
            SicliException se = (SicliException) cause;
            statusCode = se.isServicoDisponivel() ? Status.BAD_GATEWAY : Status.SERVICE_UNAVAILABLE;
            mensagem = "Falha na comunicação com o serviço de Cadastro de Clientes (SICLI).";
        } else if (SicpfException.class.equals(cause.getClass())) {
            SicpfException se = (SicpfException) cause;
            statusCode = se.isServicoDisponivel() ? Status.BAD_GATEWAY : Status.SERVICE_UNAVAILABLE;
            mensagem = "Falha na comunicação com o serviço de Consulta PF (SICPF).";
        } else if (SiecmException.class.equals(cause.getClass())) {
            SiecmException se = (SiecmException) cause;
            statusCode = se.isServicoDisponivel() ? Status.BAD_GATEWAY : Status.SERVICE_UNAVAILABLE;
            mensagem = "Falha na comunicação com o serviço de Guarda de Documentos (SIECM).";
        } else if (SifrcException.class.equals(cause.getClass())) {
            SifrcException se = (SifrcException) cause;
            statusCode = se.isServicoDisponivel() ? Status.BAD_GATEWAY : Status.SERVICE_UNAVAILABLE;
            mensagem = "Falha na comunicação com o serviço de Fraude Cadastral (SIFRC).";
        } else if (SiisoException.class.equals(cause.getClass())) {
            SiisoException se = (SiisoException) cause;
            statusCode = se.isServicoDisponivel() ? Status.BAD_GATEWAY : Status.SERVICE_UNAVAILABLE;
            mensagem = "Falha na comunicação com o serviço de Informações Sociais (SIISO).";
        } else if (SipesException.class.equals(cause.getClass())) {
            SipesException se = (SipesException) cause;
            statusCode = se.isServicoDisponivel() ? Status.BAD_GATEWAY : Status.SERVICE_UNAVAILABLE;
            mensagem = "Falha na comunicação com o serviço de Pesquisas Cadastrais (SIPES).";
        } else if (OutsourcingException.class.equals(cause.getClass())) {
            OutsourcingException oe = (OutsourcingException) cause;
            statusCode = oe.isServicoDisponivel() ? Status.BAD_GATEWAY : Status.SERVICE_UNAVAILABLE;
            mensagem = "Falha na comunicação com o serviço de Outsourcing Documental.";
        } else if (PortalEmpreendedorException.class.equals(cause.getClass())) {
            PortalEmpreendedorException pe = (PortalEmpreendedorException) cause;
            statusCode = pe.isServicoDisponivel() ? Status.BAD_GATEWAY : Status.SERVICE_UNAVAILABLE;
            mensagem = "Falha na comunicação com o serviço do Portal do Empreendedor.";
        } else if (SimtrConfiguracaoException.class.equals(cause.getClass())) {
            statusCode = Status.INTERNAL_SERVER_ERROR;
            mensagem = "Falha de configuração do SIMTR.";
        } else if (SimtrEstadoImpeditivoException.class.equals(cause.getClass())) {
            statusCode = Status.CONFLICT;
            mensagem = "O recurso indicado encontra-se num estado impeditivo de manipulação.";
        } else if (SimtrPermissaoException.class.equals(cause.getClass())) {
            statusCode = Status.FORBIDDEN;
            mensagem = "Solicitação não permitida para o solicitante.";
        } else if (SimtrPreCondicaoException.class.equals(cause.getClass())) {
            statusCode = Status.PRECONDITION_FAILED;
            mensagem = "Não foram identificadas as pré condições necessarios para execução do serviço.";
        } else if (SimtrRecursoBloqueadoException.class.equals(cause.getClass())) {
            statusCode = Status.CONFLICT;
            mensagem = "O recurso solicitado esta em utilização por outro usuário.";
        } else if (SimtrRecursoDesconhecidoException.class.equals(cause.getClass())) {
            statusCode = Status.BAD_REQUEST;
            mensagem = "O recurso solicitado não foi identificado.";
        } else if (SimtrRequisicaoException.class.equals(cause.getClass())) {
            statusCode = Status.BAD_REQUEST;
            mensagem = "Requisição Invalida.";
        }else if(SiricException.class.equals(cause.getClass())) {
        	SiricException siric = (SiricException) cause;
        	statusCode = Status.fromStatusCode(siric.getDTO().getCodigo()) ;
            mensagem = "Falha na comunicação com o serviço de SIRIC.";
            if(falhas.isEmpty()) {
            	falhas.add(siric.getDTO().getMsgErro());
            }
        }

        resourceError.setCodigoHTTP(statusCode.getStatusCode());
        resourceError.setMensagem(prefixo.concat(" - ").concat(mensagem));
        resourceError.setDetail(Arrays.toString(falhas.toArray()));
        resourceError.setStacktrace(ExceptionUtils.getStackTrace(cause));

        return Response.status(statusCode).entity(resourceError).type(MediaType.APPLICATION_JSON).build();
    }

    protected Response montaRespostaExcecao(EJBException ee, String prefixoMensagem) {

        if (EJBAccessException.class.equals(ee.getClass())) {
            RetornoErroDTO resourceError = new RetornoErroDTO();
            resourceError.setCodigoHTTP(Status.FORBIDDEN.getStatusCode());
            resourceError.setMensagem(prefixoMensagem.concat(" - Acesso negado para o perfil."));
            resourceError.setDetail(ee.getLocalizedMessage());
            resourceError.setStacktrace(ExceptionUtils.getStackTrace(ee));

            return Response.status(Response.Status.FORBIDDEN).entity(resourceError).type(MediaType.APPLICATION_JSON).build();

        }

        Throwable causa = ee.getCause();

        if (causa == null) {
            RetornoErroDTO resourceError = new RetornoErroDTO();
            resourceError.setCodigoHTTP(Status.INTERNAL_SERVER_ERROR.getStatusCode());
            resourceError.setMensagem(prefixoMensagem.concat(" - Falha não mapeada ao processar a requisição"));
            resourceError.setDetail(ee.getLocalizedMessage());
            resourceError.setStacktrace(ExceptionUtils.getStackTrace(ee));

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(resourceError).build();
        }
        
        while (EJBException.class.equals(causa.getClass())) {
            causa = causa.getCause();
        }

        //Monta um padrão de resposta diferenciado para os casos de exceção de CRUD
        if (SimtrCadastroException.class.equals(causa.getClass())) {
            SimtrCadastroException cadastroException = (SimtrCadastroException) causa;

            List<PendenciaCadastroDTO> pendencias = cadastroException.getPendencias().stream()
                    .map(pendenciaVO -> new PendenciaCadastroDTO(pendenciaVO)).collect(Collectors.toList());

            return Response.status(Response.Status.BAD_REQUEST).entity(pendencias).build();
        }
        
        //Monta um padrão de resposta diferenciado para montagem de json dinamico para atributos de integração 
        if (SimtrAtributoIntegracaoException.class.equals(causa.getClass())) {
            SimtrAtributoIntegracaoException atributoIntegracao = (SimtrAtributoIntegracaoException) causa;

            List<String> pendencias = atributoIntegracao.getPendencias();

            return Response.status(Response.Status.BAD_REQUEST).entity(pendencias).build();
        }

        return this.montaRespostaExcecao(causa, prefixoMensagem);
    }
    
    /**
     * Captura do headers o valor a ser enviado para o analytics. Separa todos os valores por , e retorna.
     * @param headers
     * @return String com valores separados por ;
     */
    protected String capturaEngineCliente(HttpHeaders headers) {
    	List<String> agentes = headers.getRequestHeader("user-agent");
    	if(Objects.isNull(agentes) || agentes.isEmpty()) {
    		return "N/A";
    	}
    	return agentes.stream().map(agente -> agente).collect(Collectors.joining(";"));
    }
    
    /**
     * Monta uma String a ser enviada como valor para o Analytics.
     * 
     * @param valores PathParams, QueryParms, HeaderParams enviados para o serviço.
     * @return
     */
    protected String montarValores(String...  valores) {
    	if(Objects.nonNull(valores)) {
    		String valor = "[";
    		for(int cont = 0; cont < valores.length; cont++) {
    			valor = valor + valores[cont++] + "=";
    			valor = valor + valores[cont] + ";";
    		}
    		return valor + "]";
    	}
    	return null;
    }
    
    


}
