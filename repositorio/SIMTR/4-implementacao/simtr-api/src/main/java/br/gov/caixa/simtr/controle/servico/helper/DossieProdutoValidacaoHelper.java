package br.gov.caixa.simtr.controle.servico.helper;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.ejb3.annotation.SecurityDomain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import br.gov.caixa.simtr.controle.excecao.SimtrDossieProdutoException;
import br.gov.caixa.simtr.controle.servico.DocumentoServico;
import br.gov.caixa.simtr.controle.servico.DossieClienteServico;
import br.gov.caixa.simtr.controle.servico.TipoDocumentoServico;
import br.gov.caixa.simtr.controle.servico.TipoRelacionamentoServico;
import br.gov.caixa.simtr.controle.vo.formulario.ConjuntoRegraVO;
import br.gov.caixa.simtr.controle.vo.formulario.RegraVO;
import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.ElementoConteudo;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.ProcessoDocumento;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoExpressaoFormulario;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.DocumentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.DossieProdutoInclusaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.ElementoConteudoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.GarantiaInformadaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.ProdutoContratadoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.RespostaFormularioDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.VinculoPessoaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao.ElementoConteudoPendenteDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao.FuncaoDocumentalDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao.PendenciaGarantiaInformadaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao.PendenciaProcessoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao.PendenciaVinculoPessoaDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao.PendenciaVinculoProdutoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao.RespostaFormularioPendenteDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao.TipoDocumentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.validacao.TipoRelacionamentoDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class DossieProdutoValidacaoHelper {

    @EJB
    private DocumentoServico documentoServico;

    @EJB
    private DossieClienteServico dossieClienteServico;

    @EJB
    private TipoDocumentoServico tipoDocumentoServico;

    @EJB
    private TipoRelacionamentoServico tipoRelacionamentoServico;

    public void validaDossieProdutoInclusao(Processo processoOrigem, DossieProdutoInclusaoDTO dossieProdutoInclusaoDTO) {
        // Identifica o processo fase inicial com base na definição do processo dossiê base para definição do dossiê
        Processo processoFaseInicial = processoOrigem.getRelacoesProcessoVinculoPai().stream()
                                                     .sorted(Comparator.comparing(rp -> rp.getOrdem()))
                                                     .findFirst().get().getProcessoFilho();

        // Valida possiveis pendências relativas aos vinculos de pessoas informados
        List<PendenciaVinculoPessoaDTO> pendenciasDocumentosPessoas = this.validaVinculosPessoas(processoOrigem, dossieProdutoInclusaoDTO.getVinculosDossieClienteDTO());

        // Valida possiveis pendências relativas aos vinculos de produtos informados
        List<PendenciaVinculoProdutoDTO> pendenciasProdutosContratados = this.validaVinculosProduto(processoOrigem.getProdutos(), dossieProdutoInclusaoDTO.getProdutosContratadosDTO());

        // Valida possiveis pendências relativas aos vinculos do processo dossiê e fase informados
        List<PendenciaProcessoDTO> pendenciasProcesso = this.validaProcesso(processoOrigem, processoFaseInicial, dossieProdutoInclusaoDTO.getElementosConteudoDTO(), dossieProdutoInclusaoDTO);

        // Captura a lista de pendências relativas ao processo dossiê
        List<PendenciaProcessoDTO> pendenciasProcessoDossie = pendenciasProcesso.stream()
                                                                                .filter(pendencia -> pendencia.getIdentificadorProcesso().equals(processoOrigem.getId()))
                                                                                .collect(Collectors.toList());

        // Captura a lista de pendências relativas ao processo fase
        List<PendenciaProcessoDTO> pendenciasProcessoFase = pendenciasProcesso.stream()
                                                                              .filter(pendencia -> pendencia.getIdentificadorProcesso().equals(processoFaseInicial.getId()))
                                                                              .collect(Collectors.toList());

        // Valida possiveis pendências relativas a garantia
        List<PendenciaGarantiaInformadaDTO> pendenciasGarantias = this.validarGarantia(processoOrigem, dossieProdutoInclusaoDTO.getGarantiasInformadasDTO());

        // Caso seja identificada alguma pendência documental para as validações realizadas, lança uma exceção relativa a validação do dossiê de produto
        if (!pendenciasDocumentosPessoas.isEmpty()
            || !pendenciasProdutosContratados.isEmpty()
            || !pendenciasProcessoDossie.isEmpty()
            || !pendenciasProcessoFase.isEmpty()
            || !pendenciasGarantias.isEmpty()) {
            String mensagem = "Existem pendências que impedem a criação do dossiê de produto solicitado.";

            throw new SimtrDossieProdutoException(mensagem, pendenciasDocumentosPessoas, pendenciasProdutosContratados, pendenciasProcessoDossie, pendenciasProcessoFase, pendenciasGarantias);
        }
    }

    // ********* Métodos privados **************

    // ************** VALIDACAO DE VINCULOS & DOCUMENTACAO **********************//

    /**
     * Valida as estruturas de obrigatóriedade para os vinculos de pessoas informados no objeto que representa o dossiê de produto e retorna a lista de pendências identificadas
     *
     * @param processoDossie Objeto que representa o processo originador do dossiê e que será utilizado para obtenção da estrutura de validação das informações
     * @param vinculosDossieClienteDTO lista de objetos que representam os vinculos de pessoas a serem incluidos no dossiê de produto e devem ser validados.
     * @return Retorna a lista de pendências identificadas ou uma lista vazia caso não haja problemas identificados na requisição.
     */
    private List<PendenciaVinculoPessoaDTO> validaVinculosPessoas(Processo processoDossie, List<VinculoPessoaDTO> vinculosDossieClienteDTO) {

        // Inicializa o objeto que representa a lista de pendencias de documentos para os vinculos de pessoas
        List<PendenciaVinculoPessoaDTO> listaPendencias = new ArrayList<>();

        // Captura o tipo de relacionamento definido como principal para o processo
        TipoRelacionamento tipoRelacionamentoPrincipal = processoDossie.getProcessoDocumentos().stream()
                                                                       .map(pd -> pd.getTipoRelacionamento())
                                                                       .filter(tr -> tr.getIndicadorPrincipal())
                                                                       .findFirst().orElse(null);

        // Verifica se o vinculo encaminhado trata-se do vinculo definido como principal
        boolean tipoRelacionamentoPrincipalEncaminhado = vinculosDossieClienteDTO.stream()
                                                                                 .anyMatch(vdc -> tipoRelacionamentoPrincipal.getId().equals(vdc.getTipoRelacionamento()));

        // Inclui pendência para identificar que o tipo de relacionamento não foi localizado
        if (!tipoRelacionamentoPrincipalEncaminhado) {
            PendenciaVinculoPessoaDTO pendenciaTipoRelacionadoPrincipal = new PendenciaVinculoPessoaDTO();
            pendenciaTipoRelacionadoPrincipal.setTipoRelacionamentoDTO(new TipoRelacionamentoDTO(tipoRelacionamentoPrincipal, Boolean.TRUE));
            pendenciaTipoRelacionadoPrincipal.setRecursoLocalizado(Boolean.FALSE);

            listaPendencias.add(pendenciaTipoRelacionadoPrincipal);
        }

        // Percorre os vinculos de pessoa encaminhados para realizar as validações necessarias
        vinculosDossieClienteDTO.forEach(vdc -> {

            TipoRelacionamento tipoRelacionamentoIdentificado = processoDossie.getProcessoDocumentos().stream()
                                                                              .map(pd -> pd.getTipoRelacionamento())
                                                                              .filter(tr -> tr.getId().equals(vdc.getTipoRelacionamento()))
                                                                              .findFirst().orElse(null);

            // Captura os registros de dossiê de cliente e dossiê cliente relacionado com base nos IDs enviados para garantir sua existência
            DossieCliente dossieCliente = this.dossieClienteServico.getById(vdc.getIdDossieCliente(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            DossieCliente dossieClienteRelacionado = null;
            if (vdc.getIdDossieClienteRelacionado() != null) {
                dossieClienteRelacionado = this.dossieClienteServico.getById(vdc.getIdDossieClienteRelacionado(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            }

            // Caso o tipo de relacionamento não seja identificado junto a definição do processo, adiciona pendênciapara o vinculo do dossiê de cliente
            if (tipoRelacionamentoIdentificado == null) {

                // Inicializa o objeto que representa o tipo de relacionamento da pendência
                TipoRelacionamento tipoRelacionamentoNaoAssociado = this.tipoRelacionamentoServico.getById(vdc.getTipoRelacionamento());
                TipoRelacionamentoDTO tipoRelacionamentoNaoIdentificadoDTO = tipoRelacionamentoNaoAssociado == null ? new TipoRelacionamentoDTO(vdc.getTipoRelacionamento())
                                                                                                                    : new TipoRelacionamentoDTO(tipoRelacionamentoNaoAssociado, Boolean.FALSE);

                // Cria o registro de pendência indicando que o tipo de reacionamento analisado não foi identificado
                PendenciaVinculoPessoaDTO pendenciaDossieClienteNaoLocalizado = new PendenciaVinculoPessoaDTO();
                pendenciaDossieClienteNaoLocalizado.setTipoRelacionamentoDTO(tipoRelacionamentoNaoIdentificadoDTO);
                pendenciaDossieClienteNaoLocalizado.setIdentificadorDossieCliente(vdc.getIdDossieCliente());
                pendenciaDossieClienteNaoLocalizado.setRecursoLocalizado(Boolean.FALSE);

                // Adiciona a pendencia a lista de pendências
                listaPendencias.add(pendenciaDossieClienteNaoLocalizado);

                // Avança para o próximo registro
                return;
            } else {
                // Verifica se o identificador de dossiê de cliente encaminhado trata-se de um identificador válido
                if (dossieCliente == null) {

                    // Cria o registro de pendência indicando que o dossiê cliente indicado para o tipo de relacionamento analisado não foi identificado
                    PendenciaVinculoPessoaDTO pendenciaDossieClienteNaoLocalizado = new PendenciaVinculoPessoaDTO();
                    pendenciaDossieClienteNaoLocalizado.setTipoRelacionamentoDTO(new TipoRelacionamentoDTO(tipoRelacionamentoIdentificado, Boolean.TRUE));
                    pendenciaDossieClienteNaoLocalizado.setIdentificadorDossieCliente(vdc.getIdDossieCliente());
                    pendenciaDossieClienteNaoLocalizado.setRecursoLocalizado(Boolean.FALSE);

                    // Adiciona a pendencia a lista de pendências
                    listaPendencias.add(pendenciaDossieClienteNaoLocalizado);

                    // Avança para o próximo registro
                    return;
                }

                // Verifica se o identificador de dossiê de cliente relacionado encaminhado trata-se de um identificador válido
                if ((vdc.getIdDossieClienteRelacionado() != null) && (dossieClienteRelacionado == null)) {

                    // Cria o registro de pendência indicando que o dossiê cliente relacionado indicado para o tipo de relacionamento analisado não foi identificado
                    PendenciaVinculoPessoaDTO pendenciaDossieRelacionadoNaoLocalizado = new PendenciaVinculoPessoaDTO();
                    pendenciaDossieRelacionadoNaoLocalizado.setTipoRelacionamentoDTO(new TipoRelacionamentoDTO(tipoRelacionamentoIdentificado, Boolean.TRUE));
                    pendenciaDossieRelacionadoNaoLocalizado.setIdentificadorDossieCliente(vdc.getIdDossieClienteRelacionado());
                    pendenciaDossieRelacionadoNaoLocalizado.setRecursoLocalizado(Boolean.FALSE);

                    // Adiciona a pendencia a lista de pendências
                    listaPendencias.add(pendenciaDossieRelacionadoNaoLocalizado);

                    // Avança para o próximo registro
                    return;
                }
            }

            // Obtem a lista de documentos indicados para reutilização na inclusão do dossiê
            List<Documento> documentosReutilizados = new ArrayList<>();
            if (vdc.getDocumentosUtilizados() != null && !vdc.getDocumentosUtilizados().isEmpty()) {
                List<Documento> documentosLocalizados = this.documentoServico.listById(vdc.getDocumentosUtilizados(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
                documentosReutilizados.addAll(documentosLocalizados);
            }

            // Obtem a lista de documentos obrigatorios esperados para o tipo de vinculo
            List<ProcessoDocumento> listaDocumentosVinculo = processoDossie.getProcessoDocumentos().stream()
                                                                           .filter(pd -> pd.getTipoRelacionamento().getId().equals(vdc.getTipoRelacionamento()))
                                                                           .filter(pd -> (pd.getTipoDocumento() != null || pd.getFuncaoDocumental() != null))
                                                                           .collect(Collectors.toList());

            // Cria a lista de registros indicados como tipo de documento e não aprovados no fluxo de avaliação dos documentos encaminhados
            List<TipoDocumentoDTO> listaTiposDocumentoNaoAprovadosCliente = new ArrayList<>();

            // Cria a lista de registros indicados como função documental e não aprovados no fluxo de avaliação dos documentos encaminhados
            List<FuncaoDocumentalDTO> listaFuncoesDocumentaisNaoAprovadasCliente = new ArrayList<>();

            // Percorre a lista de documentos novos enviados verificando se todos eles possuem tipologia esperada pelo vinculo e os atributos encaminhados são validos
            vdc.getDocumentosNovosDTO().forEach(dn -> {
                boolean documentoLocalizado = Boolean.FALSE;
                for (ProcessoDocumento pd : listaDocumentosVinculo) {
                    // Verifica se o registro a ser verificado aponta para uma função documental ou tipo de documento especifico
                    if (pd.getTipoDocumento() != null) {
                        // Caso o tipo de documento encaminhado seja identificado, passa para o proximo registro
                        if (pd.getTipoDocumento().getId().equals(dn.getCodigoTipoDocumento())) {
                            documentoLocalizado = Boolean.TRUE;
                        }
                    } else if (pd.getFuncaoDocumental() != null){
                        // Percorre todos os tipos de documento associados a função
                        for (TipoDocumento tipoDocumento : pd.getFuncaoDocumental().getTiposDocumento()) {
                            // Caso o tipo de documento encaminhado seja identificado, passa para o proximo registro
                            if (tipoDocumento.getId().equals(dn.getCodigoTipoDocumento())) {
                                documentoLocalizado = Boolean.TRUE;
                            }
                        }
                    }
                }

                if (!documentoLocalizado) {
                    listaTiposDocumentoNaoAprovadosCliente.add(new TipoDocumentoDTO(dn.getCodigoTipoDocumento(), "TIPO DE DOCUMENTO NÃO DEFINIDO PARA O VINCULO DE PESSOA"));
                } else {
                    // Captura a lista de chaves dos atributos encaminhados com o documento
                    List<String> listaAtributosDocumento = dn.getAtributosDocumento().stream().map(ad -> ad.getChave()).collect(Collectors.toList());

                    // Se o documento tiver enccaminhado os atributos, valida se a lista é valida
                    if (!listaAtributosDocumento.isEmpty()) {
                        // Captura o tipo de documento encaminhado
                        TipoDocumento tipoDocumento = tipoDocumentoServico.getById(dn.getCodigoTipoDocumento());

                        // Valida se todos os atributos encaminhados estão definidos para o tipo de documento informado
                        List<String> listaAtributosNaoEsperados = this.documentoServico.validaAtributosPertinentesDocumento(tipoDocumento, listaAtributosDocumento);
                        if (!listaAtributosNaoEsperados.isEmpty()) {
                            String mensagem = MessageFormat.format("Atributos {0} não estão definidos para este o tipo de documento.", Arrays.toString(listaAtributosNaoEsperados.toArray()));
                            listaTiposDocumentoNaoAprovadosCliente.add(new TipoDocumentoDTO(tipoDocumento, mensagem));
                        }

                        // Valida se todos os atributos obrigatorio defindos para o tipo de documento forma encaminhados junto com a solicitação
                        List<String> listaAtributosObrigatoriosNaoLocalizados = this.documentoServico.validaAtributosObrigatoriosDocumento(tipoDocumento, listaAtributosDocumento);
                        if (!listaAtributosObrigatoriosNaoLocalizados.isEmpty()) {
                            String mensagem = MessageFormat.format("Atributos {0} são obrigtórios para este o tipo de documento.", Arrays.toString(listaAtributosObrigatoriosNaoLocalizados.toArray()));
                            listaTiposDocumentoNaoAprovadosCliente.add(new TipoDocumentoDTO(tipoDocumento, mensagem));
                        }
                    }
                }

            });

            // Percorre a lista de documentos identificados como obrigatórios verificando se os documentos necessarios foram encaminhados
            listaDocumentosVinculo.stream().filter(dv -> dv.getObrigatorio()).forEach(pd -> {

                // Verifica se o registro a ser verificado aponta para uma função documental ou tipo de documento especifico
                if (pd.getTipoDocumento() != null) {

                    // Verifica se o documento enviado possui referencia ao tipo de documento esperado
                    boolean documentoLocalizado = this.avaliaPendenciaTipologiaDocumentoPessoa(pd.getTipoDocumento(), vdc.getDocumentosNovosDTO(), documentosReutilizados);

                    // Caso o tipo de documento não seja localizado, adiciona o registro na pendencia de documentos
                    if (!documentoLocalizado && pd.getObrigatorio()) {
                        listaTiposDocumentoNaoAprovadosCliente.add(new TipoDocumentoDTO(pd.getTipoDocumento(), "TIPO DE DOCUMENTO OBRIGATORIO NÃO LOCALIZADO NA REQUISICAO"));
                    }
                } else if (pd.getFuncaoDocumental() != null){

                    // Caso aponte para uma função, percorre todos os tipos a ela associados
                    boolean documentoLocalizado = Boolean.FALSE;
                    for (TipoDocumento tipoDocumento : pd.getFuncaoDocumental().getTiposDocumento()) {

                        // Verifica se o documento enviado possui referencia ao tipo de documento esperado
                        documentoLocalizado = this.avaliaPendenciaTipologiaDocumentoPessoa(tipoDocumento, vdc.getDocumentosNovosDTO(), documentosReutilizados);

                        // Caso o tipo de documento seja localizado, quebra o ciclo de verificação dos tipos de documento vinculados a função documental em analise
                        if (documentoLocalizado) {
                            break;
                        }
                    }

                    // Caso o tipo de documento nao seja localizado vinculado a função documental em analise, adiciona o registro na pendencia de documentos
                    if (!documentoLocalizado) {
                        listaFuncoesDocumentaisNaoAprovadasCliente.add(new FuncaoDocumentalDTO(pd.getFuncaoDocumental(), "FUNÇÃO DOCUMENTAL OBRIGATORIA NÃO LOCALIZADA NA REQUISICAO"));
                    }
                }
            });

            // idetinficar os campos do formularios para determinado tipo de relacionamento
            Set<CampoFormulario> camposFormularioDinamico = processoDossie.getCamposFormulario().stream()
                                                                          .filter(campo -> Objects.nonNull(campo.getTipoRelacionamento())
                                                                                           && campo.getTipoRelacionamento().getId().equals(tipoRelacionamentoIdentificado.getId()))
                                                                          .collect(Collectors.toSet());

            // Percorre a lista de campos de formulário obrigatórios identificados para o cada vinculo pessoa é verificando se a resposta para o referido campo foi encaminhada
            List<CampoFormulario> listaCamposObrigatoriosParaVinculoPessoa = this.validaFormularioVinculo(camposFormularioDinamico, vdc.getRespostasFormularioDTO());

            // Verifica se houve algum tipo de documento ou função documental definda para o tipo de vinculo que não foi aprovada
            if (!listaFuncoesDocumentaisNaoAprovadasCliente.isEmpty() || !listaTiposDocumentoNaoAprovadosCliente.isEmpty() || !listaCamposObrigatoriosParaVinculoPessoa.isEmpty()) {

                // Cria o registro de pendência para o vinculo de cliente informado
                PendenciaVinculoPessoaDTO pendenciaVinculoPessoa = new PendenciaVinculoPessoaDTO();
                pendenciaVinculoPessoa.setTipoRelacionamentoDTO(new TipoRelacionamentoDTO(tipoRelacionamentoIdentificado, Boolean.TRUE));
                pendenciaVinculoPessoa.setIdentificadorDossieCliente(vdc.getIdDossieCliente());
                pendenciaVinculoPessoa.setRecursoLocalizado(Boolean.TRUE);

                // Adiciona a lista de tipos de documentos não aprovados na pendência
                listaFuncoesDocumentaisNaoAprovadasCliente.forEach(registro -> pendenciaVinculoPessoa.addFuncaoDocumentalPendente(registro));

                // Adiciona a lista de funções documentais não aprovadas na pendência
                listaTiposDocumentoNaoAprovadosCliente.forEach(registro -> pendenciaVinculoPessoa.addTipoDocumentoPendente(registro));

                // Adiciona todas as pendências de respostas de formulário para vinculo pessoa
                listaCamposObrigatoriosParaVinculoPessoa.forEach(registro -> pendenciaVinculoPessoa.addRespostasFormularioPendentes(new RespostaFormularioPendenteDTO(registro)));

                // Adiciona a pendência na lista de pendências
                listaPendencias.add(pendenciaVinculoPessoa);

            }
        });

        return listaPendencias;
    }

    /**
     * Valida as estruturas de obrigatóriedade para os vinculos de produto informados no objeto que representa o dossiê de produto e retorna a lista de pendências identificadas
     *
     * @param processoDossie Objeto que representa o processo originador do dossiê e que será utilizado para obtenção da estrutura de validação das informações
     * @param produtosContratadosDTO lista de objetos que representam os vinculos de produtos a serem incluidos no dossiê de produto e devem ser validados quanto ao uma lista vazia será retornada.
     * @return Retorna a lista de pendências identificadas ou uma lista vazia caso não haja problemas identificados na requisição.
     */
    private List<PendenciaVinculoProdutoDTO> validaVinculosProduto(Set<Produto> produtosProcesso, List<ProdutoContratadoDTO> produtosContratadosDTO) {
        // Inicializa o objeto que representa a lista de pendencias de documentos para os vinculos de pessoas
        List<PendenciaVinculoProdutoDTO> listaPendencias = new ArrayList<>();

        // Percorre a lista de prodtos encaminhada para realizar a validação de cada registro
        produtosContratadosDTO.forEach(produtoContratadoDTO -> {
            // Identifica se o produto indicado foi localizado na estrutura do processo pelo identificador ou pelo conjunto de código de operação e modalidade
            Produto produtoAnalise = produtosProcesso.stream()
                                                     .filter(p -> {
                                                         boolean localizadoId = p.getId().equals(produtoContratadoDTO.getIdProduto());
                                                         boolean localizadoSIICO = p.getOperacao().equals(produtoContratadoDTO.getOperacao())
                                                                                   && p.getModalidade().equals(produtoContratadoDTO.getModalidade());
                                                         return (localizadoId) || (localizadoSIICO);
                                                     }).findFirst().orElse(null);

            // Caso o produto indicado não seja localizado, adiciona uma pendencia na lista
            if (produtoAnalise == null) {
                PendenciaVinculoProdutoDTO pendenciaVinculoProduto = new PendenciaVinculoProdutoDTO();
                pendenciaVinculoProduto.setIdentificadorProduto(produtoContratadoDTO.getIdProduto());
                pendenciaVinculoProduto.setCodigoOperacaoProduto(produtoContratadoDTO.getOperacao());
                pendenciaVinculoProduto.setCodigoModalidadeProduto(produtoContratadoDTO.getModalidade());
                pendenciaVinculoProduto.setProdutoLocalizado(Boolean.FALSE);

                listaPendencias.add(pendenciaVinculoProduto);
            } else {

                // Identifica os regitros de elemento de conteudo enviados que não tem relação com o produto em analise
                List<ElementoConteudoPendenteDTO> listaElementosNaoLocalizadosProduto;
                listaElementosNaoLocalizadosProduto = produtoContratadoDTO.getElementosConteudoDTO().stream()
                                                                          .filter(ec -> produtoAnalise.getElementosConteudo().stream()
                                                                                                      .noneMatch(ecpa -> ecpa.getId().equals(ec.getIdElemento())))
                                                                          .map(ecp -> new ElementoConteudoPendenteDTO(ecp.getIdElemento()))
                                                                          .collect(Collectors.toList());

                // Cria a lista de elementos de conteudo não aprovados
                List<ElementoConteudoPendenteDTO> listaElementosNaoAprovadosProduto = new ArrayList<>();

                // Cria a lista de documentos encaminhados não aprovados
                List<TipoDocumentoDTO> listaDocumentosNaoAprovadosProduto = new ArrayList<>();

                // Obtem a lista de elementos "pai" obrigatorios esperados para o produto, ou seja, aquele que não são vinculados.
                // Os elementos vinculados serão percorridos pelo fluxo de recursividade
                List<ElementoConteudo> listaObrigatoriosProduto = produtoAnalise.getElementosConteudo().stream()
                                                                                .filter(ec -> ec.getObrigatorio() != null)
                                                                                .filter(ec -> ec.getObrigatorio())
                                                                                .filter(ec -> ec.getElementoVinculador() == null)
                                                                                .collect(Collectors.toList());

                // Percorre a lista de documentos enviados para o produto verificando se todos eles possuem tipologia valida e os atributos encaminhados são validos
                produtoContratadoDTO.getElementosConteudoDTO().forEach(ec -> {
                    // Captura a lista de chaves dos atributos encaminhados com o documento
                    List<String> listaAtributosDocumento = ec.getDocumentoDTO().getAtributosDocumento().stream().map(ad -> ad.getChave()).collect(Collectors.toList());

                    // Se o documento tiver enccaminhado os atributos, valida se a lista é valida
                    if (!listaAtributosDocumento.isEmpty()) {
                        // Captura o tipo de documento encaminhado
                        TipoDocumento tipoDocumento = tipoDocumentoServico.getById(ec.getDocumentoDTO().getCodigoTipoDocumento());
                        if (tipoDocumento == null) {
                            Integer identificadorTipoDocumento = ec.getDocumentoDTO().getCodigoTipoDocumento();
                            String mensagem = MessageFormat.format("Tipo de documento {0} informado não localizado. Elemento de Conteudo: {1} | Produto: {2}", identificadorTipoDocumento, ec.getIdElemento(), produtoAnalise.getNome());
                            listaDocumentosNaoAprovadosProduto.add(new TipoDocumentoDTO(ec.getDocumentoDTO().getCodigoTipoDocumento(), mensagem));
                        }

                        // Valida se todos os atributos encaminhados estão definidos para o tipo de documento informado
                        List<String> listaAtributosNaoEsperados = this.documentoServico.validaAtributosPertinentesDocumento(tipoDocumento, listaAtributosDocumento);
                        if (!listaAtributosNaoEsperados.isEmpty()) {
                            String mensagem = MessageFormat.format("Atributos {0} não estão definidos para este o tipo de documento.", Arrays.toString(listaAtributosNaoEsperados.toArray()));
                            listaDocumentosNaoAprovadosProduto.add(new TipoDocumentoDTO(tipoDocumento, mensagem));
                        }

                        // Valida se todos os atributos obrigatorio defindos para o tipo de documento forma encaminhados junto com a solicitação
                        List<String> listaAtributosObrigatoriosNaoLocalizados = this.documentoServico.validaAtributosObrigatoriosDocumento(tipoDocumento, listaAtributosDocumento);
                        if (!listaAtributosObrigatoriosNaoLocalizados.isEmpty()) {
                            String mensagem = MessageFormat.format("Atributos {0} são obrigtórios para este o tipo de documento.", Arrays.toString(listaAtributosObrigatoriosNaoLocalizados.toArray()));
                            listaDocumentosNaoAprovadosProduto.add(new TipoDocumentoDTO(tipoDocumento, mensagem));
                        }
                    }
                });

                // Percorre a lista de documentos obrigatórios identificados para o produto verificando se o referido documento foi encaminhado
                listaObrigatoriosProduto.forEach(eco -> {

                    // Inicializa o mapa de pendências a ser devolvido após avaliação do elemento
                    Map<ElementoConteudo, Integer> mapaPendencias = new HashMap<>();

                    // Verifica se o elemento de conteudo indicado como obrigatório na parametrização foi aprovado na avaliação de documentos enviados
                    boolean aprovado = this.avaliaPendenciaTipologiaDocumentoElementoConteudo(eco, produtoContratadoDTO.getElementosConteudoDTO(), mapaPendencias);

                    // Adiciona todas as pendencias de elementos de conteudo identificados, caso o elemento não tenha sido aprovado
                    if (!aprovado) {
                        mapaPendencias.entrySet()
                                      .forEach(registro -> listaElementosNaoAprovadosProduto.add(new ElementoConteudoPendenteDTO(registro.getKey(), registro.getValue())));
                    }
                });

                // idetinficar os campos do formularios para determinado tipo de relacionamento
                Set<CampoFormulario> camposFormularioDinamico = produtoAnalise.getCamposFormulario().stream()
                                                                              .filter(campo -> Objects.nonNull(campo.getProduto())
                                                                                               && campo.getProduto().getId().equals(produtoContratadoDTO.getIdProduto()))
                                                                              .collect(Collectors.toSet());

                // Percorre a lista de campos de formulário obrigatórios identificados para o cada vinculo pessoa é verificando se a resposta para o referido campo foi encaminhada
                List<CampoFormulario> listaCamposObrigatoriosParaProduto = this.validaFormularioVinculo(camposFormularioDinamico, produtoContratadoDTO.getRespostasFormularioDTO());

                // Caso seja identificado algum elemento de conteudo enviado que não possui vinculo com o processo indicado, adiciona a pendência na lista
                if (!listaElementosNaoLocalizadosProduto.isEmpty()
                    || !listaElementosNaoAprovadosProduto.isEmpty()
                    || !listaDocumentosNaoAprovadosProduto.isEmpty()) {

                    // Cria o objeto que representa a pendência identificada para o produto
                    PendenciaVinculoProdutoDTO pendenciaVinculoProduto = new PendenciaVinculoProdutoDTO();
                    pendenciaVinculoProduto.setIdentificadorProduto(produtoContratadoDTO.getIdProduto());
                    pendenciaVinculoProduto.setCodigoOperacaoProduto(produtoContratadoDTO.getOperacao());
                    pendenciaVinculoProduto.setCodigoModalidadeProduto(produtoContratadoDTO.getModalidade());
                    pendenciaVinculoProduto.setProdutoLocalizado(Boolean.TRUE);

                    // Adiciona todas as pendências de elementos de conteudo não associados ao produto
                    listaElementosNaoAprovadosProduto.forEach(registro -> pendenciaVinculoProduto.addElementoConteudoPendente(registro));

                    // Adiciona todas as pendências de elementos de conteudo não associados ao produto
                    listaElementosNaoLocalizadosProduto.forEach(registro -> pendenciaVinculoProduto.addElementoConteudoPendente(registro));

                    // Adiciona todas as pendências de elementos de conteudo não associados ao produto
                    listaDocumentosNaoAprovadosProduto.forEach(registro -> pendenciaVinculoProduto.addDocumentosPendente(registro));

                    // Adiciona todas as pendências de campos de formulários não associados ao produto
                    listaCamposObrigatoriosParaProduto.forEach(registro -> pendenciaVinculoProduto.addRespostasFormularioPendentes(new RespostaFormularioPendenteDTO(registro)));

                    // Adiciona a pendencia do produto a lista de pendências
                    listaPendencias.add(pendenciaVinculoProduto);
                }
            }
        });
        return listaPendencias;
    }

    /**
     * Valida as estruturas de obrigatóriedade para os processos originador de dossiê e processo fase indicados no objeto que representa a inclusão do dossiê de produto e retorna a lista de pendencias
     * identificadas
     *
     * @param processoDossie Objeto que representa o processo originador do dossiê que será utilizado como base para obtenção da estrutura de validação das informações
     * @param processoFase Objeto que representa o processo fase inicial do dossiê que será utilizado como base para obtenção da estrutura de validação das informações
     * @param elementosConteudoDTO Lista de objetos que representam os documentos encaminhados para atendimento aos elementos de conteudo previstos no processo
     * @param respostasFormularioDTO Lista de objetos que representam as respostas encaminhadas para o formulário definido no processo fase
     * @return Retorna a lista de pendências identificadas ou uma lista vazia caso não haja problemas identificados na requisição.
     */
    private List<PendenciaProcessoDTO> validaProcesso(Processo processoDossie, Processo processoFase, List<ElementoConteudoDTO> elementosConteudoDTO, DossieProdutoInclusaoDTO dossieProdutoInclusaoDTO) {
        // Inicializa o objeto que representa a lista de pendencias obtidas ma validação do processo
        List<PendenciaProcessoDTO> listaPendencias = new ArrayList<>();

        // Verifica se todos os elementos de conteudo enviados são esperados
        List<ElementoConteudoPendenteDTO> listaElementosNaoLocalizadosProcesso = new ArrayList<>();
        listaElementosNaoLocalizadosProcesso = elementosConteudoDTO.stream()
                                                                   .filter(ec -> processoDossie.getElementosConteudo().stream()
                                                                                               .noneMatch(ecpd -> ecpd.getId().equals(ec.getIdElemento())))
                                                                   .filter(ec -> processoFase.getElementosConteudo().stream()
                                                                                             .noneMatch(ecpf -> ecpf.getId().equals(ec.getIdElemento())))
                                                                   .map(ecp -> new ElementoConteudoPendenteDTO(ecp.getIdElemento()))
                                                                   .collect(Collectors.toList());

        // Verifica se todos as respostas de formulário enviadas são esperadas para o processo fase
        List<RespostaFormularioPendenteDTO> listaRespostasNaoLocalizadosProcesso = dossieProdutoInclusaoDTO.getRespostasFormularioDTO().stream()
                                                                                                           .filter(rf -> processoFase.getCamposFormularioFase().stream()
                                                                                                                                     .noneMatch(cfpf -> cfpf.getId()
                                                                                                                                                            .equals(rf.getIdCampoFomulario())))
                                                                                                           .map(rf -> new RespostaFormularioPendenteDTO(rf.getIdCampoFomulario()))
                                                                                                           .collect(Collectors.toList());

        // Cria a lista de elementos de conteudo não aprovados para o processo dossie
        List<ElementoConteudoPendenteDTO> listaElementosNaoAprovadosProcessoDossie = new ArrayList<>();

        // Cria a lista de elementos de conteudo não aprovados para o processo fase
        List<ElementoConteudoPendenteDTO> listaElementosNaoAprovadosProcessoFase = new ArrayList<>();

        // Cria a lista de resposta de formulário não aprovados para o processo fase
        List<RespostaFormularioPendenteDTO> listaRespostasNaoAprovadasProcessoFase = new ArrayList<>();

        // Cria a lista de documentos enviados não aprovados
        List<TipoDocumentoDTO> listaDocumentosNaoAprovadosProcesso = new ArrayList<>();

        // Obtem a lista de elementos "pai" obrigatorios esperados para o processo dossiê, ou seja, aquele que não são vinculados
        List<ElementoConteudo> listaElementosObrigatoriosProcessoDossie = processoDossie.getElementosConteudo().stream()
                                                                                        .filter(ec -> ec.getObrigatorio() != null)
                                                                                        .filter(ec -> ec.getObrigatorio())
                                                                                        .filter(ec -> ec.getElementoVinculador() == null)
                                                                                        .collect(Collectors.toList());

        // Obtem a lista de elementos "pai" obrigatorios esperados para o processo fase, ou seja, aquele que não são vinculados
        List<ElementoConteudo> listaElementosObrigatoriosProcessoFase = processoFase.getElementosConteudo().stream()
                                                                                    .filter(ec -> ec.getObrigatorio() != null)
                                                                                    .filter(ec -> ec.getObrigatorio())
                                                                                    .filter(ec -> ec.getElementoVinculador() == null)
                                                                                    .collect(Collectors.toList());

        // Percorre a lista de campos de formulário obrigatórios identificados para o processo fase verificando se a resposta para o referido campo foi encaminhada
        List<CampoFormulario> listaCamposObrigatoriosProcessoFase = this.validaFormularioFase(processoFase.getCamposFormularioFase(), dossieProdutoInclusaoDTO.getRespostasFormularioDTO(), dossieProdutoInclusaoDTO.getVinculosDossieClienteDTO(), dossieProdutoInclusaoDTO.getProdutosContratadosDTO(), dossieProdutoInclusaoDTO.getGarantiasInformadasDTO());

        listaCamposObrigatoriosProcessoFase.forEach(campo -> listaRespostasNaoAprovadasProcessoFase.add((new RespostaFormularioPendenteDTO(campo))));

        // Percorre a lista de documentos enviados para o produto verificando se todos eles possuem tipologia valida e os atributos encaminhados são validos
        elementosConteudoDTO.forEach(ec -> {
            // Captura a lista de chaves dos atributos encaminhados com o documento
            List<String> listaAtributosDocumento = ec.getDocumentoDTO().getAtributosDocumento().stream().map(ad -> ad.getChave()).collect(Collectors.toList());

            // Se o documento tiver enccaminhado os atributos, valida se a lista é valida
            if (!listaAtributosDocumento.isEmpty()) {
                // Captura o tipo de documento encaminhado
                TipoDocumento tipoDocumento = tipoDocumentoServico.getById(ec.getDocumentoDTO().getCodigoTipoDocumento());
                if (tipoDocumento == null) {
                    Integer identificadorTipoDocumento = ec.getDocumentoDTO().getCodigoTipoDocumento();
                    String descricaoProcessoOriginador = String.valueOf(processoDossie.getId()).concat(" - ").concat(processoDossie.getNome());
                    String descricaoProcessoFase = String.valueOf(processoDossie.getId()).concat(" - ").concat(processoFase.getNome());
                    String mensagem = MessageFormat.format("Tipo de documento {0} informado não localizado. Elemento de Conteudo: {1} | Processo Originador: {2} | Processo Fase: {3}", identificadorTipoDocumento, ec.getIdElemento(), descricaoProcessoOriginador, descricaoProcessoFase);
                    listaDocumentosNaoAprovadosProcesso.add(new TipoDocumentoDTO(ec.getDocumentoDTO().getCodigoTipoDocumento(), mensagem));
                }

                // Valida se todos os atributos encaminhados estão definidos para o tipo de documento informado
                List<String> listaAtributosNaoEsperados = this.documentoServico.validaAtributosPertinentesDocumento(tipoDocumento, listaAtributosDocumento);
                if (!listaAtributosNaoEsperados.isEmpty()) {
                    String mensagem = MessageFormat.format("Atributos {0} não estão definidos para este o tipo de documento.", Arrays.toString(listaAtributosNaoEsperados.toArray()));
                    listaDocumentosNaoAprovadosProcesso.add(new TipoDocumentoDTO(tipoDocumento, mensagem));
                }

                // Valida se todos os atributos obrigatorio defindos para o tipo de documento forma encaminhados junto com a solicitação
                List<String> listaAtributosObrigatoriosNaoLocalizados = this.documentoServico.validaAtributosObrigatoriosDocumento(tipoDocumento, listaAtributosDocumento);
                if (!listaAtributosObrigatoriosNaoLocalizados.isEmpty()) {
                    String mensagem = MessageFormat.format("Atributos {0} são obrigtórios para este o tipo de documento.", Arrays.toString(listaAtributosObrigatoriosNaoLocalizados.toArray()));
                    listaDocumentosNaoAprovadosProcesso.add(new TipoDocumentoDTO(tipoDocumento, mensagem));
                }
            }
        });

        // Percorre a lista de documentos obrigatórios identificados para o processo dossiê verificando se o referido documento foi encaminhado
        listaElementosObrigatoriosProcessoDossie.forEach(eco -> {

            // Inicializa o mapa de pendências a ser devolvido após avaliação do elemento
            Map<ElementoConteudo, Integer> mapaPendencias = new HashMap<>();

            // Verifica se o elemento de conteudo indicado como obrigatório na parametrização foi aprovado na avaliação de elementos de conteudo enviados
            boolean aprovado = this.avaliaPendenciaTipologiaDocumentoElementoConteudo(eco, elementosConteudoDTO, mapaPendencias);

            // Adiciona todas as pendencias de elementos de conteudo identificados, caso o elemento não tenha sido aprovado
            if (!aprovado) {
                mapaPendencias.entrySet().stream()
                              .forEach(registro -> listaElementosNaoAprovadosProcessoDossie.add(new ElementoConteudoPendenteDTO(registro.getKey(), registro.getValue())));
            }
        });

        // Percorre a lista de documentos obrigatórios identificados para o processo fase verificando se o referido documento foi encaminhado
        listaElementosObrigatoriosProcessoFase.forEach(eco -> {

            // Inicializa o mapa de pendências a ser devolvido após avaliação do elemento
            Map<ElementoConteudo, Integer> mapaPendencias = new HashMap<>();

            // Verifica se o elemento de conteudo indicado como obrigatório na parametrização foi aprovado na avaliação de elementos de conteudo enviados
            boolean aprovado = this.avaliaPendenciaTipologiaDocumentoElementoConteudo(eco, elementosConteudoDTO, mapaPendencias);

            // Adiciona todas as pendencias de elementos de conteudo identificados, caso o elemento não tenha sido aprovado
            if (!aprovado) {
                mapaPendencias.entrySet().stream()
                              .forEach(registro -> listaElementosNaoAprovadosProcessoFase.add(new ElementoConteudoPendenteDTO(registro.getKey(), registro.getValue())));
            }
        });

        // Adiciona na lista de pendências os elemento de conteudos:
        // - Enviado que não possui vinculo com o processo dossiê indicado
        // - Enviado com tipologia não aprovada para o elemento de conteudo definido para o processo dossiê
        if (!listaElementosNaoLocalizadosProcesso.isEmpty()
            || !listaElementosNaoAprovadosProcessoDossie.isEmpty()
            || !listaDocumentosNaoAprovadosProcesso.isEmpty()) {

            // Cria o objeto que representa as pendencias de elementos de conteudos não localizados ao processo
            PendenciaProcessoDTO pendenciaProcesso = new PendenciaProcessoDTO();
            pendenciaProcesso.setIdentificadorProcesso(processoDossie.getId());

            // Adiciona todas as pendências de elementos de conteudo associados ao processo dossiê
            listaElementosNaoAprovadosProcessoDossie.forEach(registro -> pendenciaProcesso.addElementoConteudoPendente(registro));

            // Adiciona todas as pendências de elementos de conteudo não associados ao processo dossiê
            listaElementosNaoLocalizadosProcesso.forEach(registro -> pendenciaProcesso.addElementoConteudoPendente(registro));

            // Adiciona todas as pendências de elementos de conteudo não associados ao produto
            listaDocumentosNaoAprovadosProcesso.forEach(registro -> pendenciaProcesso.addDocumentosPendente(registro));

            // Adiciona a pendencia do processo dossiê a lista de pendências
            listaPendencias.add(pendenciaProcesso);
        }

        // Adiciona na lista de pendências:
        // - Elemento de conteudo enviado que não possui vinculo com o processo fase indicado
        // - Elemento de conteudo enviado com tipologia não aprovada para o elemento de conteudo definido para o processo fase
        // - Resposta de formulário encaminhada com tipo de resposta não esperado para o campo
        if (!listaElementosNaoLocalizadosProcesso.isEmpty()
            || !listaElementosNaoAprovadosProcessoFase.isEmpty()
            || !listaRespostasNaoLocalizadosProcesso.isEmpty()
            || !listaRespostasNaoAprovadasProcessoFase.isEmpty()) {

            // Cria o objeto que representa as pendencias de elementos de conteudos não localizados ao processo
            PendenciaProcessoDTO pendenciaProcesso = new PendenciaProcessoDTO();
            pendenciaProcesso.setIdentificadorProcesso(processoFase.getId());

            // Adiciona todas as pendências de elementos de conteudo associados ao processo fase
            listaElementosNaoAprovadosProcessoFase.forEach(registro -> pendenciaProcesso.addElementoConteudoPendente(registro));

            // Adiciona todas as pendências de elementos de conteudo não associados ao processo fase
            listaElementosNaoLocalizadosProcesso.forEach(registro -> pendenciaProcesso.addElementoConteudoPendente(registro));

            // Adiciona todas as pendências de respostas de formulário associados ao processo fase
            listaRespostasNaoAprovadasProcessoFase.forEach(registro -> pendenciaProcesso.addRespostaFormularioPendente(registro));

            // Adiciona todas as pendências de respostas de formulário não associados ao processo fase
            listaRespostasNaoLocalizadosProcesso.forEach(registro -> pendenciaProcesso.addRespostaFormularioPendente(registro));

            // Adiciona a pendencia do processo fase a lista de pendências
            listaPendencias.add(pendenciaProcesso);
        }

        return listaPendencias;
    }

    private List<PendenciaGarantiaInformadaDTO> validarGarantia(Processo processoOrigem, List<GarantiaInformadaDTO> garantiasInformadasDTO) {
        // Inicializa o objeto que representa a lista de pendencias obtidas ma validação da garantia
        List<PendenciaGarantiaInformadaDTO> pendenciasGarantias = new ArrayList<>();

        // Cria a lista de resposta de formulário não aprovados para o processo fase
        List<RespostaFormularioPendenteDTO> listaRespostasNaoAprovadasGarantia = new ArrayList<>();
        
        // Cria a lista de documentos enviados não aprovados
        List<TipoDocumentoDTO> listaDocumentosNaoAprovadosProcesso = new ArrayList<>();

        garantiasInformadasDTO.forEach(garantiaDTO -> {

            // identificar os campos formulario de acordo com garantia
            Set<CampoFormulario> camposFormularios = processoOrigem.getCamposFormulario().stream()
                                                                   .filter(campo -> Objects.nonNull(campo.getGarantia()) && campo.getGarantia().getId().equals(garantiaDTO.getIdentificadorGarantia()))
                                                                   .collect(Collectors.toSet());

            List<CampoFormulario> listaCamposObrigatoriosGarantias = this.validaFormularioVinculo(camposFormularios, garantiaDTO.getRespostasFormularioDTO());

            listaCamposObrigatoriosGarantias.forEach(campo -> listaRespostasNaoAprovadasGarantia.add((new RespostaFormularioPendenteDTO(campo))));
            
            // Percorre a lista de documentos enviados para o produto verificando se todos eles possuem tipologia valida e os atributos encaminhados são validos
            garantiaDTO.getDocumentosNovosDTO().forEach(dn -> {
                // Captura a lista de chaves dos atributos encaminhados com o documento
                List<String> listaAtributosDocumento = dn.getAtributosDocumento().stream().map(ad -> ad.getChave()).collect(Collectors.toList());

                // Se o documento tiver enccaminhado os atributos, valida se a lista é valida
                if (!listaAtributosDocumento.isEmpty()) {
                    // Captura o tipo de documento encaminhado
                    TipoDocumento tipoDocumento = tipoDocumentoServico.getById(dn.getCodigoTipoDocumento());
                    if (tipoDocumento == null) {
                        String mensagem;
                        if (garantiaDTO.getIdentificadorProduto() == null) {
                            mensagem = MessageFormat.format("Tipo de documento {0} informado não localizado. Garantia Associada: {1}", dn.getCodigoTipoDocumento(), garantiaDTO.getIdentificadorGarantia());
                        } else {
                            mensagem = MessageFormat.format("Tipo de documento {0} informado não localizado. Garantia Associada: {1} | Produto Associado: {2}", dn.getCodigoTipoDocumento(), garantiaDTO.getIdentificadorGarantia(), garantiaDTO.getIdentificadorProduto());
                        }
                        listaDocumentosNaoAprovadosProcesso.add(new TipoDocumentoDTO(dn.getCodigoTipoDocumento(), mensagem));
                    }

                    // Valida se todos os atributos encaminhados estão definidos para o tipo de documento informado
                    List<String> listaAtributosNaoEsperados = this.documentoServico.validaAtributosPertinentesDocumento(tipoDocumento, listaAtributosDocumento);
                    if (!listaAtributosNaoEsperados.isEmpty()) {
                        String mensagem = MessageFormat.format("Atributos {0} não estão definidos para este o tipo de documento.", Arrays.toString(listaAtributosNaoEsperados.toArray()));
                        listaDocumentosNaoAprovadosProcesso.add(new TipoDocumentoDTO(tipoDocumento, mensagem));
                    }

                    // Valida se todos os atributos obrigatorio defindos para o tipo de documento forma encaminhados junto com a solicitação
                    List<String> listaAtributosObrigatoriosNaoLocalizados = this.documentoServico.validaAtributosObrigatoriosDocumento(tipoDocumento, listaAtributosDocumento);
                    if (!listaAtributosObrigatoriosNaoLocalizados.isEmpty()) {
                        String mensagem = MessageFormat.format("Atributos {0} são obrigtórios para este o tipo de documento.", Arrays.toString(listaAtributosObrigatoriosNaoLocalizados.toArray()));
                        listaDocumentosNaoAprovadosProcesso.add(new TipoDocumentoDTO(tipoDocumento, mensagem));
                    }
                }
            });

            if (!listaRespostasNaoAprovadasGarantia.isEmpty()
                || !listaDocumentosNaoAprovadosProcesso.isEmpty()) {

                PendenciaGarantiaInformadaDTO pendenciaGarantia = new PendenciaGarantiaInformadaDTO();
                pendenciaGarantia.setIdentificadorGarantia(garantiaDTO.getIdentificadorGarantia());

                // Adiciona todas as pendências de respostas de formulário não associados a garantia
                listaRespostasNaoAprovadasGarantia.forEach(registro -> pendenciaGarantia.addRespostaFormularioPendente(registro));
             
                // Adiciona todas as pendências de elementos de conteudo não associados ao produto
                listaDocumentosNaoAprovadosProcesso.forEach(registro -> pendenciaGarantia.addDocumentosPendente(registro));

                pendenciasGarantias.add(pendenciaGarantia);
            }
        });

        return pendenciasGarantias;
    }

    /**
     * Avalia se o tipo de documento indicado esta presente como um diocumento novo presente na lista encvaminhada ou presente na lista de documento reutilizados
     *
     * @param tipoDocumento Tipo de documento a ser localizado
     * @param documentosNovos Lista de objetos que representam os novos documentos a serem incluidos no sistema
     * @param documentosReutilizados Lista de documentos reutilizados do sistema
     * @return true se tipo de documento localizado em uma das duas listas ou false caso contrario
     */
    private boolean avaliaPendenciaTipologiaDocumentoPessoa(TipoDocumento tipoDocumento, List<DocumentoDTO> documentosNovos, List<Documento> documentosReutilizados) {
        // Verifica se a lista de documentos novos enviados contem algum indicando o tipo de documento esperado esperado
        boolean documentoLocalizado = documentosNovos.stream()
                                                     .filter(doc -> doc.getCodigoTipoDocumento().equals(tipoDocumento.getId()))
                                                     .findAny().isPresent();

        // Caso não seja identificado na lista de documentos novos, verifica se algum dos documentos reutilizados possui o tipo definido
        if (!documentoLocalizado) {
            documentoLocalizado = documentosReutilizados.stream()
                                                        .filter(doc -> doc.getTipoDocumento().equals(tipoDocumento))
                                                        .findAny().isPresent();
        }

        return documentoLocalizado;
    }

    /**
     * Avalia um determinado elemento de conteudo definido para o processo ou produto de forma recursiva aos seus elementos vinculados, indicado se este foi atendido junto a lista de elemento de
     * conteúdo enviado na solicitação e não sendo aprovado o registro de pendência é incluido no mapa encainhado.
     *
     * @param elementoConteudoAvaliado Elemento de conteúdo parametrizado junto ao processo ou produto a ser avaliado quanto a presença de documento enviado
     * @param elementosConteudoDTO Lista de objetos que rerpesentam os elementos de conteudo enviados junto a solicitação
     * @param mapaPendencias Mapa de pendencias que deve ser utilizado para indicar as pendência localizadas em toda linha de elementos de conteudo vinculado ao elemento conteudo em avaliação
     * @return Indicação de elemento aprovado ou não utilizado na recursividade para indicar que o quantitativo deve ser deduzido
     */
    private boolean avaliaPendenciaTipologiaDocumentoElementoConteudo(ElementoConteudo elementoConteudoAvaliado, List<ElementoConteudoDTO> elementosConteudoDTO, Map<ElementoConteudo, Integer> mapaPendencias) {

        // Verifica se o registro a ser verificado nó folha (tipo de documento especifico) ou para uma pasta
        if (elementoConteudoAvaliado.getTipoDocumento() == null) {

            // Identifica a quantidade de elementos obrigatorios para o elemento conteudo avaliado
            int quantidadeObrigatorios = elementoConteudoAvaliado.getQuantidadeObrigatorios() == null ? 0 : elementoConteudoAvaliado.getQuantidadeObrigatorios();

            // Executa nova chamada recursiva para avaliar cada elemento filho da pasta se possui documento obrigatorio localizado
            for (ElementoConteudo ec : elementoConteudoAvaliado.getElementosVinculados()) {
                boolean localizado = this.avaliaPendenciaTipologiaDocumentoElementoConteudo(ec, elementosConteudoDTO, mapaPendencias);

                // Caso o documento seja localizado para o elemento conteudo avaliado, subtrai uma pendencia até que o valor definido seja igual a 0
                if (localizado) {
                    quantidadeObrigatorios--;
                }
            }

            // Caso ao final do processo a quantidade de elementos obrigatorios não atendidos seja superior a zero, adiciona o elemento no mapa de pendências.
            if (quantidadeObrigatorios > 0) {
                mapaPendencias.put(elementoConteudoAvaliado, quantidadeObrigatorios);
                return Boolean.FALSE;
            }

            // Retorna verdadeiro se a quantidade de pendencias ao final do ciclo for igual a zero.
            return Boolean.TRUE;
        } else {
            // Caso o elemento seja um nó folha, finalizará a recursividade do ramo.

            // Verifica se o elemento folha foi enviado
            ElementoConteudoDTO elementoConteudoFolha = elementosConteudoDTO.stream()
                                                                            .filter(ec -> elementoConteudoAvaliado.getId().equals(ec.getIdElemento()))
                                                                            .findFirst().orElse(null);

            // Caso o elemento não tenha sido localizado retorna falso para validação
            if (elementoConteudoFolha == null) {
                // Caso o elemento folha seja um elemento obrigatório, adiciona o mesmo no mapa de pendências
                if (elementoConteudoAvaliado.getObrigatorio()) {
                    mapaPendencias.put(elementoConteudoAvaliado, 1);
                }
                return Boolean.FALSE;
            }

            // Identificado nó folha enviado, verifica se o tipo de documento encaminhado é o esperado
            boolean tipoDocumentoValido = elementoConteudoFolha.getDocumentoDTO().getCodigoTipoDocumento().equals(elementoConteudoAvaliado.getTipoDocumento().getId());

            // Caso o tipo de documento não seja o esperado pelo elemento, inclui o nó folha no mapa de pendências e retorna falha na validação
            if (!tipoDocumentoValido) {
                mapaPendencias.put(elementoConteudoAvaliado, 1);
                return Boolean.FALSE;
            }

            // retorna verdadeiro se for identificado tipo de documento encaminhado conforme esperado
            return Boolean.TRUE;
        }
    }

    // ************** VALIDACAO DE FORMULARIOS **********************//

    /**
     * 1- Valida se os campos obrigatório foi preenchdio 2- Valida se os campos obrigatório que tem expressão foi atendida, se foi o campo passar a ser obrigatorio
     *
     * @param Set<CampoFormulario> Lista dos campos do formulário dinâmico
     * @param List<RespostaFormularioDTO> Lista de resposta do formulário dinâmico
     * @return Retorna lista de campos que são obrigatório e não foi preenchido
     */
    private List<CampoFormulario> validaFormularioVinculo(Set<CampoFormulario> camposFormularioDinamico, List<RespostaFormularioDTO> respostasFormularioDTO) {
        List<CampoFormulario> campoFormularioObrigatorios = new ArrayList<>();

        if (Objects.nonNull(camposFormularioDinamico) && !camposFormularioDinamico.isEmpty()) {

            // Identifica campos que tem expressao que podem ser obrigatórios, e campos obrigatórios que não tem expressão.
            camposFormularioDinamico.stream().forEach(campo -> {

                boolean existeExpressao = Objects.nonNull(campo.getExpressaoInterface()) && !campo.getExpressaoInterface().trim().isEmpty() ? true : false;

                // identificar resposta do campo
                Optional<RespostaFormularioDTO> respostaFormulario = respostasFormularioDTO.stream()
                                                                                           .filter(resposta -> resposta.getIdCampoFomulario().equals(campo.getId()))
                                                                                           .findFirst();

                if (campo.getObrigatorio()) {

                    if (existeExpressao) {

                        // verifca se expressão do campo foi atendida
                        boolean expressaoAtendida = this.validaExpresaoCampoFormulario(campo, camposFormularioDinamico, respostasFormularioDTO, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

                        if (expressaoAtendida && !this.isFormatoRespostaAdequado(respostaFormulario, campo)) {
                            // expressão foi atendida mas não tem resposta
                            campoFormularioObrigatorios.add(campo);
                        }

                    } else if (!this.isFormatoRespostaAdequado(respostaFormulario, campo)) {
                        // campo obrigatório, não tem expressao e não tem resposta
                        campoFormularioObrigatorios.add(campo);
                    }
                }

            });
        }

        return campoFormularioObrigatorios;
    }

    /**
     * 1- Valida se os campos obrigatório foi preenchdio 2- Valida se os campos obrigatório que tem expressão foi atendida, se foi o campo passar a ser obrigatorio
     *
     * @param Set<CampoFormulario> Lista dos campos do formulário dinâmico
     * @param List<RespostaFormularioDTO> Lista de resposta do formulário dinâmico
     * @param Set<VinculoPessoaDTO> Lista de clientes pra ser comparando nas expressões
     * @param Set<ProdutoContratadoDTO> Lista de produtos pra ser comparando nas exprssões
     * @param Set<GarantiaInformadaDTO> Lista de garantias pra ser comparando nas expressoes
     * @return Retorna lista de campos que são obrigatório e não foi preenchido
     */
    private List<CampoFormulario> validaFormularioFase(Set<CampoFormulario> camposFormularioDinamico, List<RespostaFormularioDTO> respostasFormularioDTO, List<VinculoPessoaDTO> vinculoPessoas, List<ProdutoContratadoDTO> produtos, List<GarantiaInformadaDTO> garantias) {

        List<CampoFormulario> campoFormularioObrigatorios = new ArrayList<>();

        if (Objects.nonNull(camposFormularioDinamico) && !camposFormularioDinamico.isEmpty()) {

            /// Identifica campos que tem expressao que podem ser obrigatórios, e campos obrigatórios que não tem expressão.
            camposFormularioDinamico.stream().forEach(campo -> {

                boolean existeExpressao = Objects.nonNull(campo.getExpressaoInterface()) && !campo.getExpressaoInterface().trim().isEmpty() ? true : false;

                // identificar resposta do campo
                Optional<RespostaFormularioDTO> respostaFormulario = respostasFormularioDTO.stream()
                                                                                           .filter(resposta -> resposta.getIdCampoFomulario().equals(campo.getId()))
                                                                                           .findFirst();

                if (campo.getObrigatorio()) {

                    if (existeExpressao) {

                        // verifca se expressão do campo foi atendida
                        boolean expressaoAtendida = this.validaExpresaoCampoFormulario(campo, camposFormularioDinamico, respostasFormularioDTO, vinculoPessoas, produtos, garantias);

                        if (expressaoAtendida && !this.isFormatoRespostaAdequado(respostaFormulario, campo)) {
                            // expressão foi atendida mas não tem resposta
                            campoFormularioObrigatorios.add(campo);
                        }

                    } else if (!this.isFormatoRespostaAdequado(respostaFormulario, campo)) {
                        // campo obrigatório, não tem expressao e não tem resposta
                        campoFormularioObrigatorios.add(campo);
                    }
                }

            });
        }

        return campoFormularioObrigatorios;
    }

    private boolean validaExpresaoCampoFormulario(CampoFormulario campo, Set<CampoFormulario> camposFormularioDinamico, List<RespostaFormularioDTO> respostas, List<VinculoPessoaDTO> vinculoPessoas, List<ProdutoContratadoDTO> produtos, List<GarantiaInformadaDTO> garantias) {
        boolean expressaoAtendida = false;
        List<ConjuntoRegraVO> conjuntoDeRregas = this.converteExpressao(campo);

        if (!conjuntoDeRregas.isEmpty()) {

            for (ConjuntoRegraVO conjunto : conjuntoDeRregas) {
                Integer totoalDeRegras = conjunto.getListaRegras().size();
                Integer quantidadeDeRegrasAprovadas = 0;

                for (RegraVO regra : conjunto.getListaRegras()) {

                    boolean expressao = this.interpretaExpressao(regra, campo, camposFormularioDinamico, respostas, vinculoPessoas, produtos, garantias);
                    if (expressao) {
                        quantidadeDeRegrasAprovadas++;
                    }

                }

                if (quantidadeDeRegrasAprovadas.equals(totoalDeRegras)) {
                    expressaoAtendida = true;
                    break;
                }
            }

        }

        return expressaoAtendida;
    }

    // verificar se a resposta está no lugar certo de acorto com seu tipo
    private boolean isFormatoRespostaAdequado(Optional<RespostaFormularioDTO> respostaFormulario, CampoFormulario campo) {
        boolean respostaOk = false;

        if (respostaFormulario.isPresent()) {

            if ((campo.getCampoEntrada().getTipo().equals(TipoCampoEnum.SELECT) || campo.getCampoEntrada().getTipo().equals(TipoCampoEnum.RADIO)
                 || campo.getCampoEntrada().getTipo().equals(TipoCampoEnum.CHECKBOX))
                && (respostaFormulario.get().getOpcoesSelecionadas() != null && !respostaFormulario.get().getOpcoesSelecionadas().isEmpty())) {
                respostaOk = true;

            } else if (!Strings.isNullOrEmpty(respostaFormulario.get().getResposta())) {
                respostaOk = true;
            }
        }

        return respostaOk;
    }

    private List<ConjuntoRegraVO> converteExpressao(CampoFormulario campoFormulario) {
        List<ConjuntoRegraVO> conjuntos = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<List<RegraVO>> expressao = objectMapper.readValue(campoFormulario.getExpressaoInterface(), new TypeReference<List<List<RegraVO>>>() {});
            for (Object conjunto : expressao) {
                List<?> lista = (ArrayList<?>) conjunto;

                ConjuntoRegraVO conjuntoDTO = new ConjuntoRegraVO();
                conjuntoDTO.setListaRegras(new ArrayList<RegraVO>());

                lista.forEach(elemento -> {
                    RegraVO regra = (RegraVO) elemento;
                    conjuntoDTO.getListaRegras().add(regra);
                });

                conjuntos.add(conjuntoDTO);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return conjuntos;
    }

    private boolean interpretaExpressao(RegraVO regra, CampoFormulario campo, Set<CampoFormulario> camposFormularioDinamico, List<RespostaFormularioDTO> respostas, List<VinculoPessoaDTO> vinculoPessoas, List<ProdutoContratadoDTO> produtos, List<GarantiaInformadaDTO> garantias) {
        boolean expressaoRegraOk = false;

        if (!Strings.isNullOrEmpty(regra.getCampoResposta())) {
            TipoCampoEnum tipoCampoResposta = null;

            // identificar a resposta da regra
            Optional<RespostaFormularioDTO> respostaDaRegra = respostas.stream().filter(elemento -> elemento.getIdCampoFomulario().equals(Long.valueOf(regra.getCampoResposta())))
                                                                       .findFirst();

            if (respostaDaRegra.isPresent()) {
                // identificar qual é o tipo do campo da resposta
                Optional<CampoFormulario> campoDaRespostaDaRegra = camposFormularioDinamico.stream()
                                                                                           .filter(elemento -> elemento.getId().equals(respostaDaRegra.get().getIdCampoFomulario()))
                                                                                           .findFirst();

                if (campoDaRespostaDaRegra.isPresent()) {
                    tipoCampoResposta = campoDaRespostaDaRegra.get().getCampoEntrada().getTipo();
                }

                expressaoRegraOk = this.validaPorTipoCampo(regra, tipoCampoResposta, respostaDaRegra.get());
            }

        } else if (!Strings.isNullOrEmpty(regra.getTipoRegra())) {

            switch (TipoExpressaoFormulario.lookup(regra.getTipoRegra())) {
                case GRID_CLIENTE:
                    expressaoRegraOk = this.verificaCliente(regra, vinculoPessoas);
                    break;
                case GRID_PRODUTO:
                    expressaoRegraOk = this.verificaProduto(regra, produtos);
                    break;
                case GRID_GARANTIA:
                    expressaoRegraOk = this.verificaGarantia(regra, garantias);
                    break;
                default:
            }
        }

        return expressaoRegraOk;
    }

    private boolean validaPorTipoCampo(RegraVO regra, TipoCampoEnum tipoCampoResposta, RespostaFormularioDTO resposta) {
        boolean regraOk = false;

        switch (tipoCampoResposta) {
            case TEXT:
                regraOk = this.validaRespostaAberta(resposta, regra);
                break;
            case TEXTAREA:
                regraOk = this.validaRespostaAberta(resposta, regra);
                break;
            case COLOR:
                regraOk = this.validaRespostaAberta(resposta, regra);
                break;
            case DATE:
                regraOk = this.validaRespostaAberta(resposta, regra);
                break;
            case CEP:
                regraOk = this.validaRespostaAberta(resposta, regra);
                break;
            case HIDDEN:
                regraOk = this.validaRespostaAberta(resposta, regra);
                break;
            case SELECT:
                regraOk = this.validaRespostaObjetiva(resposta, regra, false);
                break;
            case RADIO:
                regraOk = this.validaRespostaObjetiva(resposta, regra, false);
                break;
            case CHECKBOX:
                regraOk = this.validaRespostaObjetiva(resposta, regra, true);
                break;
            default:
            	regraOk = this.validaRespostaAberta(resposta, regra);
            	break;
        }

        return regraOk;
    }

    private boolean validaRespostaAberta(RespostaFormularioDTO resposta, RegraVO regra) {
        boolean respostaOk = false;

        if (Objects.nonNull(regra.getValores()) && !regra.getValores().isEmpty() && !Strings.isNullOrEmpty(resposta.getResposta())) {
            for (String valor : regra.getValores()) {
                if (!Strings.isNullOrEmpty(valor)) {
                    respostaOk = resposta.getResposta().equalsIgnoreCase(valor);
                    if (respostaOk) {
                        break;
                    }
                }
            }
        }

        return respostaOk;
    }

    private boolean validaRespostaObjetiva(RespostaFormularioDTO resposta, RegraVO regra, boolean isCheckbox) {
        boolean respostaOk = false;
        Integer quantidadeDeResposta = 0;

        if (Objects.nonNull(regra.getValores()) && !regra.getValores().isEmpty() && Objects.nonNull(resposta.getOpcoesSelecionadas())
            && !resposta.getOpcoesSelecionadas().isEmpty()) {

            for (String valor : regra.getValores()) {
                if (!Strings.isNullOrEmpty(valor)) {

                    if (isCheckbox) {
                        boolean valorOk = resposta.getOpcoesSelecionadas().stream().allMatch(opcaoSelecionda -> opcaoSelecionda.equalsIgnoreCase(valor));

                        if (valorOk) {
                            quantidadeDeResposta++;
                        }

                    } else {
                        for (String opcaoSelecionada : resposta.getOpcoesSelecionadas()) {
                            respostaOk = valor.equalsIgnoreCase(opcaoSelecionada);
                            if (respostaOk) {
                                break;
                            }
                        }
                    }

                }
            }
        }

        if (isCheckbox && quantidadeDeResposta.equals(regra.getValores().size())) {
            respostaOk = true;
        }

        return respostaOk;
    }

    private boolean verificaCliente(RegraVO regra, List<VinculoPessoaDTO> vinculoPessoas) {
        boolean respostaOk = false;

        if (regra.getValores() != null && !regra.getValores().isEmpty()) {
            // verificar se existe tipo relacionamento esperado

            for (VinculoPessoaDTO pessoa : vinculoPessoas) {

                if (Objects.nonNull(pessoa.getTipoRelacionamento())) {
                    for (String valor : regra.getValores()) {

                        // verificar por do null
                        TipoRelacionamento tipoRelacionamento = this.tipoRelacionamentoServico.getByNome(valor);

                        if (Objects.nonNull(tipoRelacionamento) && tipoRelacionamento.getId().equals(pessoa.getTipoRelacionamento())) {
                            respostaOk = true;
                            break;
                        }
                    }
                }

                if (respostaOk) {
                    break;
                }

            }
        }

        return respostaOk;
    }

    private boolean verificaProduto(RegraVO regra, List<ProdutoContratadoDTO> produtos) {
        boolean valorOk = false;

        if (regra.getValores() != null && !regra.getValores().isEmpty()) {

            switch (regra.getAtributo().toUpperCase()) {

                case "ID":
                    valorOk = produtos.stream().anyMatch(produto -> Objects.nonNull(produto.getIdProduto())
                                                                    && regra.getValores().stream().anyMatch(valor -> Integer.valueOf(valor).equals(produto.getIdProduto())));
                    break;
                case "CODIGO_OPERACAO":
                    valorOk = produtos.stream().anyMatch(produto -> Objects.nonNull(produto.getOperacao())
                                                                    && regra.getValores().stream().anyMatch(valor -> Integer.valueOf(valor).equals(produto.getOperacao())));
                    break;
                case "CODIGO_MODALIDADE":
                    valorOk = produtos.stream().anyMatch(produto -> Objects.nonNull(produto.getModalidade())
                                                                    && regra.getValores().stream().anyMatch(valor -> Integer.valueOf(valor).equals(produto.getModalidade())));
                    break;
                default:
                    break;
            }
        }
        return valorOk;
    }

    private boolean verificaGarantia(RegraVO regra, List<GarantiaInformadaDTO> garantias) {
        if (regra.getValores() != null && !regra.getValores().isEmpty()) {
            // verificar se existe identificador da garantia
            return garantias.stream().anyMatch(garantia -> Objects.nonNull(garantia.getIdentificadorGarantia())
                                                           && regra.getValores().stream().anyMatch(valor -> Integer.valueOf(valor).equals(garantia.getIdentificadorGarantia())));
        }
        return false;
    }

}
