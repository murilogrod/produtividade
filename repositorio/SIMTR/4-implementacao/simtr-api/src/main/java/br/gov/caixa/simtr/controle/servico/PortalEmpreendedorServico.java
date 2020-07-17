package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.util.UtilCpf;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.helper.PortalEmpreendedorHelper;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.NecessidadeVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.PortalEmpreendedorVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.validacao.DossiesClienteValidadoVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.validacao.ParametrosDossieProdutoMeiVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.validacao.SolicitacaoValidadaDossieProdutoMeiVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.validacao.TiposDocumentoValidadoVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.validacao.TiposRelacionamentoValidadoVO;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.DocumentoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.DossieProdutoInclusaoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.ProdutoContratadoDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.RespostaFormularioDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.VinculoPessoaDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNINT
})

@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PortalEmpreendedorServico {

    @EJB
    private ProcessoServico processoServico;

    @EJB
    private DossieClienteServico dossieClienteServico;

    @EJB
    private ProdutoServico produtoServico;
    
    @EJB
    private DossieProdutoServico dossieProdutoServico;
    
    @EJB
    private PortalEmpreendedorHelper portalEmpreendedorHelper;

    /**
     * Realiza todo o fluxo da criação do dossie de produto do MEI.
     * Consulta o portal do empreendedor pelo protocolo.
     * Busca propriedades definidas para criação do dossie.
     * Valida as informações obtidas no retorno da consulta do portal de empreendedor.
     * Monta o dossie de Produto do MEI a partir das informações validadas.
     * @param protocolo usado para consultar o serviço externo do portal do empreendedor.
     */
    public void montaDossieProdutoMEI(String protocolo) {
        PortalEmpreendedorVO portalEmpreendedorVO = this.portalEmpreendedorHelper.consultaServicoPortalEmpreendedorPeloProtocolo(protocolo);
        ParametrosDossieProdutoMeiVO parametrosDossieProdutoMeiVO = this.portalEmpreendedorHelper.consultaParametrosCriacaoDossieProdutoMei();
        
        SolicitacaoValidadaDossieProdutoMeiVO solicitacaoValidadaDossieProdutoMeiVO = this.validaInformacoesDossieProdutoSolicitado(portalEmpreendedorVO, parametrosDossieProdutoMeiVO);
        DossieProdutoInclusaoDTO dossieProdutoInclusaoDTO = this.criaDossieProdutoMEI(portalEmpreendedorVO, parametrosDossieProdutoMeiVO, solicitacaoValidadaDossieProdutoMeiVO);

        this.dossieProdutoServico.novoDossieProduto(dossieProdutoInclusaoDTO);
    }
    
    /**
     * Valida todas as informações contidas no retorno da consulta no portal do empreendedor.
     * @param portalEmpreendedorVO
     * @param parametrosDossieProdutoMeiVO
     * @return
     */
    private SolicitacaoValidadaDossieProdutoMeiVO validaInformacoesDossieProdutoSolicitado(PortalEmpreendedorVO portalEmpreendedorVO, ParametrosDossieProdutoMeiVO parametrosDossieProdutoMeiVO) {
        DossiesClienteValidadoVO dossiesClienteValidadoVO = this.portalEmpreendedorHelper.validaDossiesClienteSolicitados(portalEmpreendedorVO);
        
        String enderecoPJFormatado = this.portalEmpreendedorHelper.formataEnderecoComercialPJ(portalEmpreendedorVO.getPessoaJuridica());
        
        this.atualizaEmailDossieClientePJ(portalEmpreendedorVO.getPessoaJuridica().getEmail(), dossiesClienteValidadoVO.getDossieClientePJ());

        StringBuilder necessidadesNaoEncontradas = new StringBuilder();
        List<Produto> listaProdutosMEI = this.consultaProdutosMEIByCodigoNecessidade(portalEmpreendedorVO.getNecessidades(), necessidadesNaoEncontradas);
        if (listaProdutosMEI.isEmpty()) {
            throw new SimtrRequisicaoException("PES.vIDPS.001 - Não foram identificados produtos parametrizadas para nenhuma das necessidades encaminhadas.");
        }

        Processo processoGeraDossie = this.processoServico.getById(parametrosDossieProdutoMeiVO.getProcesso_originador());
        if (Objects.isNull(processoGeraDossie)) {
            String mensagem = MessageFormat.format("PES.vIDPS.002 - Não foi identificado processo originador sob identificador parametrizado. Código de Processo: {0}", parametrosDossieProdutoMeiVO.getProcesso_originador());
            throw new SimtrConfiguracaoException(mensagem);
        }
        
        String agenciaContaFormatada = MessageFormat.format("{0}.{1}", portalEmpreendedorVO.getConta().getAgencia(), portalEmpreendedorVO.getConta().getConta());

        TiposDocumentoValidadoVO tiposDocumentoValidadoVO = this.portalEmpreendedorHelper.validaTiposDocumento(parametrosDossieProdutoMeiVO);
        
        TiposRelacionamentoValidadoVO tiposRelacionamentoValidadoVO = this.portalEmpreendedorHelper.validaTiposRelacionamento(parametrosDossieProdutoMeiVO);
        
        return new SolicitacaoValidadaDossieProdutoMeiVO(dossiesClienteValidadoVO, agenciaContaFormatada, 
                    enderecoPJFormatado, listaProdutosMEI, necessidadesNaoEncontradas, processoGeraDossie,
                    tiposDocumentoValidadoVO, tiposRelacionamentoValidadoVO);
    }   
    
    
    /**
     * 
     * @param email
     * @param dossieClientePJ
     */
    private void atualizaEmailDossieClientePJ(String email, DossieCliente dossieClientePJ) {
        if (Objects.nonNull(email)) {
            dossieClientePJ.setEmail(email);
            this.dossieClienteServico.aplicaPatch(dossieClientePJ.getId(), dossieClientePJ);
        }
    }

    /**
     * 
     * @param necessidades
     * @param necessidadesNaoEncontradas
     * @return
     */
    private List<Produto> consultaProdutosMEIByCodigoNecessidade(List<NecessidadeVO> necessidades, StringBuilder necessidadesNaoEncontradas) {
        List<Produto> listaProdutosMEI = new ArrayList<>();
        for (NecessidadeVO necessidade : necessidades) {
            Produto produto = this.produtoServico.getByCodigoPortalEmpreendedor(necessidade.getCodigo());
            if (Objects.isNull(produto)) {
                necessidadesNaoEncontradas.append(necessidade.getCodigo());
                necessidadesNaoEncontradas.append(" - ");
                necessidadesNaoEncontradas.append(necessidade.getDescricao());
                necessidadesNaoEncontradas.append("; ");
                continue;
            }
            listaProdutosMEI.add(produto);
        }
        return listaProdutosMEI;
    }

    /**
     * 
     * @param portalEmpreendedorVO
     * @param parametrosDossieProdutoMeiVO
     * @param solicitacaoValidadaDossieProdutoMeiVO
     * @return
     */
    private DossieProdutoInclusaoDTO criaDossieProdutoMEI(PortalEmpreendedorVO portalEmpreendedorVO, ParametrosDossieProdutoMeiVO parametrosDossieProdutoMeiVO, SolicitacaoValidadaDossieProdutoMeiVO solicitacaoValidadaDossieProdutoMeiVO) {

        DossieProdutoInclusaoDTO dossieProdutoInclusaoDTO = new DossieProdutoInclusaoDTO();
        dossieProdutoInclusaoDTO.setRascunho(Boolean.FALSE);
        dossieProdutoInclusaoDTO.setProcessoOrigem(solicitacaoValidadaDossieProdutoMeiVO.getProcessoGeraDossie().getId());

        List<VinculoPessoaDTO> listaVinculoPessoa = new ArrayList<>();
        VinculoPessoaDTO vinculoPessoaPJ = this.montaVinculoTomadorPJ(portalEmpreendedorVO, parametrosDossieProdutoMeiVO, solicitacaoValidadaDossieProdutoMeiVO);
        listaVinculoPessoa.add(vinculoPessoaPJ);
        VinculoPessoaDTO vinculoResonsavelLegal = this.montaVinculoResponsavelLegal(portalEmpreendedorVO, parametrosDossieProdutoMeiVO, solicitacaoValidadaDossieProdutoMeiVO);
        listaVinculoPessoa.add(vinculoResonsavelLegal);
        
        if(Objects.nonNull(solicitacaoValidadaDossieProdutoMeiVO.getDossiesClienteValidadoVO().getDossieClienteConjuge())) {
            VinculoPessoaDTO vinculoConjujeResponsavelLegal = this.montaVinculoConjugeResponsavelLegal(solicitacaoValidadaDossieProdutoMeiVO);
            listaVinculoPessoa.add(vinculoConjujeResponsavelLegal);
        }
        
        solicitacaoValidadaDossieProdutoMeiVO.getDossiesClienteValidadoVO().getListaDossieClienteQuadroSocietarioValidado().forEach(dossieClienteSocio -> {
            VinculoPessoaDTO vinculoPessoaSocio = this.montaVinculoQuadroSocietario(dossieClienteSocio, solicitacaoValidadaDossieProdutoMeiVO);
            listaVinculoPessoa.add(vinculoPessoaSocio);
        });
        dossieProdutoInclusaoDTO.setVinculosDossieClienteDTO(listaVinculoPessoa);
        
        List<ProdutoContratadoDTO> listaProdutosContratados = this.montaProdutosContratados(solicitacaoValidadaDossieProdutoMeiVO);
        dossieProdutoInclusaoDTO.setProdutosContratadosDTO(listaProdutosContratados);

        List<RespostaFormularioDTO> listaRespostaFormulario = this.montaRespostasFormulario(portalEmpreendedorVO, parametrosDossieProdutoMeiVO, solicitacaoValidadaDossieProdutoMeiVO);
        dossieProdutoInclusaoDTO.setRespostasFormularioDTO(listaRespostaFormulario);
        return dossieProdutoInclusaoDTO;
    }

    /**
     * 
     * @param portalEmpreendedorVO
     * @param parametrosDossieProdutoMeiVO
     * @param solicitacaoValidadaDossieProdutoMeiVO
     * @return
     */
    private List<RespostaFormularioDTO> montaRespostasFormulario(PortalEmpreendedorVO portalEmpreendedorVO, ParametrosDossieProdutoMeiVO parametrosDossieProdutoMeiVO, SolicitacaoValidadaDossieProdutoMeiVO solicitacaoValidadaDossieProdutoMeiVO) {

        List<RespostaFormularioDTO> listaRespostaFormulario = new ArrayList<>();

        RespostaFormularioDTO respostaFormulario1 = new RespostaFormularioDTO();
        respostaFormulario1.setIdCampoFomulario(parametrosDossieProdutoMeiVO.getFormulario_data());
        String calendarDataString = this.portalEmpreendedorHelper.convertDataTimeZoneToStringCalendar(portalEmpreendedorVO.getData());
        respostaFormulario1.setResposta(calendarDataString);
        listaRespostaFormulario.add(respostaFormulario1);

        RespostaFormularioDTO respostaFormulario2 = new RespostaFormularioDTO();
        respostaFormulario2.setIdCampoFomulario(parametrosDossieProdutoMeiVO.getFormulario_protocolo());
        respostaFormulario2.setResposta(portalEmpreendedorVO.getProtocolo());
        listaRespostaFormulario.add(respostaFormulario2);

        RespostaFormularioDTO respostaFormulario3 = new RespostaFormularioDTO();
        respostaFormulario3.setIdCampoFomulario(parametrosDossieProdutoMeiVO.getFormulario_necessidades());
        respostaFormulario3.setResposta(solicitacaoValidadaDossieProdutoMeiVO.getNecessidadesNaoEncontradas().toString());
        listaRespostaFormulario.add(respostaFormulario3);
        
        RespostaFormularioDTO respostaFormulario4 = new RespostaFormularioDTO();
        respostaFormulario4.setIdCampoFomulario(parametrosDossieProdutoMeiVO.getFormulario_conta());
        respostaFormulario4.setResposta(solicitacaoValidadaDossieProdutoMeiVO.getAgenciaContaFormatada());
        listaRespostaFormulario.add(respostaFormulario4);
        return listaRespostaFormulario;
    }

    /**
     * 
     * @param solicitacaoValidadaDossieProdutoMeiVO
     * @return
     */
    private List<ProdutoContratadoDTO> montaProdutosContratados(SolicitacaoValidadaDossieProdutoMeiVO solicitacaoValidadaDossieProdutoMeiVO) {
        List<ProdutoContratadoDTO> listaProdutosContratados = new ArrayList<>();
        for (Produto produto : solicitacaoValidadaDossieProdutoMeiVO.getListaProdutosMEI()) {
            ProdutoContratadoDTO produtoContratadoDTO = new ProdutoContratadoDTO();
            produtoContratadoDTO.setIdProduto(produto.getId());
            listaProdutosContratados.add(produtoContratadoDTO);
        }
        return listaProdutosContratados;
    }
    
    /**
     * 
     * @param portalEmpreendedorVO
     * @param parametrosDossieProdutoMeiVO
     * @param solicitacaoValidadaDossieProdutoMeiVO
     * @return
     */
    private VinculoPessoaDTO montaVinculoResponsavelLegal(PortalEmpreendedorVO portalEmpreendedorVO, ParametrosDossieProdutoMeiVO parametrosDossieProdutoMeiVO, SolicitacaoValidadaDossieProdutoMeiVO solicitacaoValidadaDossieProdutoMeiVO) {
        VinculoPessoaDTO vinculoPessoaResponsavelLegal = new VinculoPessoaDTO();
        vinculoPessoaResponsavelLegal.setIdDossieCliente(solicitacaoValidadaDossieProdutoMeiVO.getDossiesClienteValidadoVO().getDossieClienteResponsavel().getId());
        vinculoPessoaResponsavelLegal.setIdDossieClienteRelacionado(solicitacaoValidadaDossieProdutoMeiVO.getDossiesClienteValidadoVO().getDossieClientePJ().getId());
        vinculoPessoaResponsavelLegal.setTipoRelacionamento(solicitacaoValidadaDossieProdutoMeiVO.getTiposRelacionamentoValidadoVO().getResponsavelLegal().getId());

        List<DocumentoDTO> listaDocumentosNovosResponsavelLegal = new ArrayList<>();
        
        DocumentoDTO documentoIdentificacaoPF = new DocumentoDTO();
        documentoIdentificacaoPF.setCodigoTipoDocumento(solicitacaoValidadaDossieProdutoMeiVO.getTiposDocumentoValidadoVO().getIdentificacaoPF().getId());
        documentoIdentificacaoPF.setOrigemDocumentoEnum(OrigemDocumentoEnum.S);
        this.portalEmpreendedorHelper.identificaTipoExtensaoDocumento(portalEmpreendedorVO.getDocumentoIdentidade().getNome(), documentoIdentificacaoPF);
        documentoIdentificacaoPF.setBinario(portalEmpreendedorVO.getDocumentoIdentidade().getConteudo());
        listaDocumentosNovosResponsavelLegal.add(documentoIdentificacaoPF);
        
        DocumentoDTO documentoEnderecoPF = new DocumentoDTO();
        documentoEnderecoPF.setCodigoTipoDocumento(solicitacaoValidadaDossieProdutoMeiVO.getTiposDocumentoValidadoVO().getEnderecoPF().getId());
        documentoEnderecoPF.setOrigemDocumentoEnum(OrigemDocumentoEnum.S);
        this.portalEmpreendedorHelper.identificaTipoExtensaoDocumento(portalEmpreendedorVO.getComprovanteResidencia().getNome(), documentoEnderecoPF);
        documentoEnderecoPF.setBinario(portalEmpreendedorVO.getComprovanteResidencia().getConteudo());
        listaDocumentosNovosResponsavelLegal.add(documentoEnderecoPF);
        
        vinculoPessoaResponsavelLegal.setDocumentosNovosDTO(listaDocumentosNovosResponsavelLegal);

        List<RespostaFormularioDTO> listaRespostasFormulario = new ArrayList<>();
        
        RespostaFormularioDTO formularioCampoEstadoCivil = new RespostaFormularioDTO();
        formularioCampoEstadoCivil.setIdCampoFomulario(parametrosDossieProdutoMeiVO.getFormulario_estado_civil());
        formularioCampoEstadoCivil.setResposta(portalEmpreendedorVO.getDadosComplementaresSolicitante().getEstadoCivil());
        listaRespostasFormulario.add(formularioCampoEstadoCivil);
        
        RespostaFormularioDTO formularioCampoEscolaridade = new RespostaFormularioDTO();
        formularioCampoEscolaridade.setIdCampoFomulario(parametrosDossieProdutoMeiVO.getFormulario_escolaridade());
        formularioCampoEscolaridade.setResposta(portalEmpreendedorVO.getDadosComplementaresSolicitante().getEscolaridade());
        listaRespostasFormulario.add(formularioCampoEscolaridade);
        
        vinculoPessoaResponsavelLegal.setRespostasFormularioDTO(listaRespostasFormulario);
        return vinculoPessoaResponsavelLegal;
    }

    /**
     * 
     * @param solicitacaoValidadaDossieProdutoMeiVO
     * @return
     */
    private VinculoPessoaDTO montaVinculoConjugeResponsavelLegal(SolicitacaoValidadaDossieProdutoMeiVO solicitacaoValidadaDossieProdutoMeiVO) {
        VinculoPessoaDTO vinculoPessoaConjuge = new VinculoPessoaDTO();
        vinculoPessoaConjuge.setIdDossieCliente(solicitacaoValidadaDossieProdutoMeiVO.getDossiesClienteValidadoVO().getDossieClienteConjuge().getId());
        vinculoPessoaConjuge.setIdDossieClienteRelacionado(solicitacaoValidadaDossieProdutoMeiVO.getDossiesClienteValidadoVO().getDossieClienteResponsavel().getId());
        vinculoPessoaConjuge.setTipoRelacionamento(solicitacaoValidadaDossieProdutoMeiVO.getTiposRelacionamentoValidadoVO().getConjuge().getId());
        return vinculoPessoaConjuge;
    }
    
    /**
     * 
     * @param dossieClienteSocioAtual
     * @param solicitacaoValidadaDossieProdutoMeiVO
     * @return
     */
    private VinculoPessoaDTO montaVinculoQuadroSocietario(DossieCliente dossieClienteSocioAtual, SolicitacaoValidadaDossieProdutoMeiVO solicitacaoValidadaDossieProdutoMeiVO) {
        VinculoPessoaDTO vinculoSocio = new VinculoPessoaDTO();
        vinculoSocio.setIdDossieCliente(dossieClienteSocioAtual.getId());
        vinculoSocio.setIdDossieClienteRelacionado(solicitacaoValidadaDossieProdutoMeiVO.getDossiesClienteValidadoVO().getDossieClientePJ().getId());
        if(UtilCpf.isValidCpf(dossieClienteSocioAtual.getCpfCnpj())) {
            vinculoSocio.setTipoRelacionamento(solicitacaoValidadaDossieProdutoMeiVO.getTiposRelacionamentoValidadoVO().getSocioPF().getId());
        } else {
            vinculoSocio.setTipoRelacionamento(solicitacaoValidadaDossieProdutoMeiVO.getTiposRelacionamentoValidadoVO().getSocioPJ().getId());
        }
        return vinculoSocio;
    }
    
    /**
     * 
     * @param portalEmpreendedorVO
     * @param parametrosDossieProdutoMeiVO
     * @param solicitacaoValidadaDossieProdutoMeiVO
     * @return
     */
    private VinculoPessoaDTO montaVinculoTomadorPJ(PortalEmpreendedorVO portalEmpreendedorVO, ParametrosDossieProdutoMeiVO parametrosDossieProdutoMeiVO, SolicitacaoValidadaDossieProdutoMeiVO solicitacaoValidadaDossieProdutoMeiVO) {
        VinculoPessoaDTO vinculoPessoaPJ = new VinculoPessoaDTO();
        vinculoPessoaPJ.setIdDossieCliente(solicitacaoValidadaDossieProdutoMeiVO.getDossiesClienteValidadoVO().getDossieClientePJ().getId());
        vinculoPessoaPJ.setTipoRelacionamento(solicitacaoValidadaDossieProdutoMeiVO.getTiposRelacionamentoValidadoVO().getTomadorContrato().getId());

        List<DocumentoDTO> listaDocumentosNovosPJ = new ArrayList<>();
        DocumentoDTO documentoConstituicaoPJ = new DocumentoDTO();
        documentoConstituicaoPJ.setCodigoTipoDocumento(solicitacaoValidadaDossieProdutoMeiVO.getTiposDocumentoValidadoVO().getConstituicaoPJ().getId());
        documentoConstituicaoPJ.setOrigemDocumentoEnum(OrigemDocumentoEnum.S);
        this.portalEmpreendedorHelper.identificaTipoExtensaoDocumento(portalEmpreendedorVO.getDocumentoConstituicaoEmpresa().getNome(), documentoConstituicaoPJ);
        documentoConstituicaoPJ.setBinario(portalEmpreendedorVO.getDocumentoConstituicaoEmpresa().getConteudo());
        listaDocumentosNovosPJ.add(documentoConstituicaoPJ);
        
        DocumentoDTO documentoFaturamentoPJ = new DocumentoDTO();
        documentoFaturamentoPJ.setCodigoTipoDocumento(solicitacaoValidadaDossieProdutoMeiVO.getTiposDocumentoValidadoVO().getFaturamentoPJ().getId());
        documentoFaturamentoPJ.setOrigemDocumentoEnum(OrigemDocumentoEnum.S);
        this.portalEmpreendedorHelper.identificaTipoExtensaoDocumento(portalEmpreendedorVO.getDeclaracaoFaturamento().getNome(), documentoFaturamentoPJ);
        documentoFaturamentoPJ.setBinario(portalEmpreendedorVO.getDeclaracaoFaturamento().getConteudo());
        listaDocumentosNovosPJ.add(documentoFaturamentoPJ);
        
        vinculoPessoaPJ.setDocumentosNovosDTO(listaDocumentosNovosPJ);

        List<RespostaFormularioDTO> listaRespostasFormulario = new ArrayList<>();
        
        RespostaFormularioDTO formularioCampoCnae = new RespostaFormularioDTO();
        formularioCampoCnae.setIdCampoFomulario(parametrosDossieProdutoMeiVO.getFormulario_cnae());
        formularioCampoCnae.setResposta(portalEmpreendedorVO.getPessoaJuridica().getCnaePrincipal().getCodigo() 
                + "-" + portalEmpreendedorVO.getPessoaJuridica().getCnaePrincipal().getDescricao());
        listaRespostasFormulario.add(formularioCampoCnae);
        
        RespostaFormularioDTO formularioCampoContato = new RespostaFormularioDTO();
        formularioCampoContato.setIdCampoFomulario(parametrosDossieProdutoMeiVO.getFormulario_contato());
        formularioCampoContato.setResposta(solicitacaoValidadaDossieProdutoMeiVO.getEnderecoPJFormatado());
        listaRespostasFormulario.add(formularioCampoContato);
        
        RespostaFormularioDTO formularioCampoMEI = new RespostaFormularioDTO();
        formularioCampoMEI.setIdCampoFomulario(parametrosDossieProdutoMeiVO.getFormulario_indicador_mei());
        formularioCampoMEI.setResposta(portalEmpreendedorVO.isMicroEmpresarioIndividual() ? "Sim" : "Nao");
        listaRespostasFormulario.add(formularioCampoMEI);
        
        vinculoPessoaPJ.setRespostasFormularioDTO(listaRespostasFormulario);
        return vinculoPessoaPJ;
    }
}
