package br.gov.caixa.simtr.controle.servico.helper;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.core.Response;

import org.jboss.ejb3.annotation.SecurityDomain;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.caixa.pedesgo.arquitetura.enumerador.EnumMetodoHTTP;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.pedesgo.arquitetura.util.UtilRest;
import br.gov.caixa.pedesgo.arquitetura.util.UtilWS;
import br.gov.caixa.simtr.controle.excecao.PortalEmpreendedorException;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.controle.servico.DossieClienteServico;
import br.gov.caixa.simtr.controle.servico.TipoDocumentoServico;
import br.gov.caixa.simtr.controle.servico.TipoRelacionamentoServico;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.PessoaJuridicaVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.PortalEmpreendedorVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.QuadroSocietarioVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.validacao.DossiesClienteValidadoVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.validacao.ParametrosDossieProdutoMeiVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.validacao.TiposDocumentoValidadoVO;
import br.gov.caixa.simtr.controle.vo.portalempreendedor.validacao.TiposRelacionamentoValidadoVO;
import br.gov.caixa.simtr.modelo.entidade.DossieCliente;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.enumerator.FormatoConteudoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaPortalEmpreendedorEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto.manutencao.DocumentoDTO;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNINT
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class PortalEmpreendedorHelper {

    @EJB
    private TipoRelacionamentoServico tipoRelacionamentoServico;

    @EJB
    private TipoDocumentoServico tipoDocumentoServico;
    
    @EJB
    private DossieClienteServico dossieClienteServico;
    
    private static final Logger LOGGER = Logger.getLogger(PortalEmpreendedorHelper.class.getName());

    
    /**
     * 
     * @param portalEmpreendedorVO
     * @return
     */
    public DossiesClienteValidadoVO validaDossiesClienteSolicitados(PortalEmpreendedorVO portalEmpreendedorVO) {
        Long cpfResponsavel = Long.parseLong(portalEmpreendedorVO.getPessoaJuridica().getResponsavel().getNi());
        DossieCliente dossieClienteResponsavel = this.dossieClienteServico.getByCpfCnpj(cpfResponsavel, TipoPessoaEnum.F, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (Objects.isNull(dossieClienteResponsavel)) {
            String mensagem = MessageFormat.format("PEH.vDCS.001 - CPF vinculado a solicitação não localizado junto a receita federal. CPF: {0}", portalEmpreendedorVO.getDadosComplementaresSolicitante().getCpf());
            throw new SimtrRequisicaoException(mensagem);
        }
        
        DossieCliente dossieClienteConjuge = null;
        if(Objects.nonNull(portalEmpreendedorVO.getDadosComplementaresSolicitante().getConjuge())){
            Long cpfConjuge = Long.parseLong(portalEmpreendedorVO.getDadosComplementaresSolicitante().getConjuge().getCpf());
            dossieClienteConjuge = this.dossieClienteServico.getByCpfCnpj(cpfConjuge, TipoPessoaEnum.F, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            if (Objects.isNull(dossieClienteConjuge)) {
                String mensagem = MessageFormat.format("PEH.vDCS.002 - CPF vinculado a solicitação não localizado junto a receita federal. CPF: {0}", portalEmpreendedorVO.getDadosComplementaresSolicitante().getConjuge().getCpf());
                throw new SimtrRequisicaoException(mensagem);
            }
        }
        
        List<DossieCliente> listaDossieClienteQuadroSocietarioValidado = new ArrayList<>();
        if(Objects.nonNull(portalEmpreendedorVO.getPessoaJuridica().getQuadroSocietario()) 
                && !portalEmpreendedorVO.getPessoaJuridica().getQuadroSocietario().isEmpty()) {
           this.validaInformacoesQuadroSocietario(portalEmpreendedorVO.getPessoaJuridica().getQuadroSocietario(), listaDossieClienteQuadroSocietarioValidado);
        }
        
        Long cnpj = Long.parseLong(portalEmpreendedorVO.getPessoaJuridica().getCnpj());
        DossieCliente dossieClientePJ = this.dossieClienteServico.getByCpfCnpj(cnpj, TipoPessoaEnum.J, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        if (Objects.isNull(dossieClientePJ)) {
            String mensagem = MessageFormat.format("PEH.vDCS.003 - CNPJ vinculado a solicitação não localizado junto a receita federal. CNPJ: {0}", portalEmpreendedorVO.getPessoaJuridica());
            throw new SimtrRequisicaoException(mensagem);
        }
        return new DossiesClienteValidadoVO(dossieClienteResponsavel, dossieClienteConjuge, 
                listaDossieClienteQuadroSocietarioValidado, dossieClientePJ);
    }
    
    /**
     * 
     * @param listaQuadroSocietario
     * @return
     */
    private void validaInformacoesQuadroSocietario(List<QuadroSocietarioVO> listaQuadroSocietario, List<DossieCliente> listaDossieClienteQuadroSocietarioValidado) {
        for(QuadroSocietarioVO quadroSocietario : listaQuadroSocietario) {
            if(TipoPessoaPortalEmpreendedorEnum.F.getDescricao().equals(quadroSocietario.getTipo())) {
                Long cpfSocio = Long.parseLong(quadroSocietario.getNi());
                DossieCliente dossieClienteSocioPF = this.dossieClienteServico.getByCpfCnpj(cpfSocio, TipoPessoaEnum.F, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
                if (Objects.isNull(dossieClienteSocioPF)) {
                    String mensagem = MessageFormat.format("PEH.vIQS.001 - CPF vinculado a solicitação não localizado junto a receita federal. CPF: {0}", quadroSocietario.getNi());
                    throw new SimtrRequisicaoException(mensagem);
                }
                listaDossieClienteQuadroSocietarioValidado.add(dossieClienteSocioPF);
                continue;
            }
            Long cnpjSocio = Long.parseLong(quadroSocietario.getNi());
            DossieCliente dossieClienteSocioPJ = this.dossieClienteServico.getByCpfCnpj(cnpjSocio, TipoPessoaEnum.J, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
            if (Objects.isNull(dossieClienteSocioPJ)) {
                String mensagem = MessageFormat.format("PEH.vIQS.002 - CNPJ vinculado a solicitação não localizado junto a receita federal. CNPJ: {0}", quadroSocietario.getNi());
                throw new SimtrRequisicaoException(mensagem);
            }
            listaDossieClienteQuadroSocietarioValidado.add(dossieClienteSocioPJ);
        }
    }
    
    /**
     * 
     * @param parametrosDossieProdutoMeiVO
     * @return
     */
    public TiposDocumentoValidadoVO validaTiposDocumento(ParametrosDossieProdutoMeiVO parametrosDossieProdutoMeiVO) {
        TipoDocumento identificacaoPF = this.tipoDocumentoServico.getByTipologia(parametrosDossieProdutoMeiVO.getTipologia_identificacao_pf());
        if (Objects.isNull(identificacaoPF)) {
            String mensagem = MessageFormat.format("PEH.vTD.001 - Não foi identificado tipo de documento processo sob o código de tipologia {0}.", parametrosDossieProdutoMeiVO.getTipologia_identificacao_pf());
            throw new SimtrConfiguracaoException(mensagem);
        }
        
        TipoDocumento enderecoPF = this.tipoDocumentoServico.getByTipologia(parametrosDossieProdutoMeiVO.getTipologia_endereco_pf());
        if (Objects.isNull(enderecoPF)) {
            String mensagem = MessageFormat.format("PEH.vTD.002 - Não foi identificado tipo de documento processo sob o código de tipologia {0}.", parametrosDossieProdutoMeiVO.getTipologia_endereco_pf());
            throw new SimtrConfiguracaoException(mensagem);
        }

        TipoDocumento constituicaoPJ = this.tipoDocumentoServico.getByTipologia(parametrosDossieProdutoMeiVO.getTipologia_constituicao_pj());
        if (Objects.isNull(constituicaoPJ)) {
            String mensagem = MessageFormat.format("PEH.vTD.003 - Não foi identificado tipo de documento processo sob o código de tipologia {0}.", parametrosDossieProdutoMeiVO.getTipologia_constituicao_pj());
            throw new SimtrConfiguracaoException(mensagem);
        }
        
        TipoDocumento faturamentoPJ = this.tipoDocumentoServico.getByTipologia(parametrosDossieProdutoMeiVO.getTipologia_faturamento_pj());
        if (Objects.isNull(faturamentoPJ)) {
            String mensagem = MessageFormat.format("PEH.vTD.004 - Não foi identificado tipo de documento processo sob o código de tipologia {0}.", parametrosDossieProdutoMeiVO.getTipologia_faturamento_pj());
            throw new SimtrConfiguracaoException(mensagem);
        }
        
        return new TiposDocumentoValidadoVO(identificacaoPF, enderecoPF, constituicaoPJ, faturamentoPJ);
    }

    /**
     * 
     * @param parametrosDossieProdutoMeiVO
     * @return
     */
    public TiposRelacionamentoValidadoVO validaTiposRelacionamento(ParametrosDossieProdutoMeiVO parametrosDossieProdutoMeiVO) {
        TipoRelacionamento responsavelLegal = this.tipoRelacionamentoServico.getById(parametrosDossieProdutoMeiVO.getTipo_relacionamento_responsavel_legal());
        if (Objects.isNull(responsavelLegal)) {
            String mensagem = MessageFormat.format("PEH.vTR.001 - Não foi identificado tipo de relacionamento sob o código {0} para a relação da pessoa jurídica.", parametrosDossieProdutoMeiVO.getTipo_relacionamento_responsavel_legal());
            throw new SimtrConfiguracaoException(mensagem);
        }
        
        TipoRelacionamento conjuge = this.tipoRelacionamentoServico.getById(parametrosDossieProdutoMeiVO.getTipo_relacionamento_conjuge());
        if (Objects.isNull(conjuge)) {
            String mensagem = MessageFormat.format("PEH.vTR.002 - Não foi identificado tipo de relacionamento sob o código {0} para a relação da pessoa jurídica.", parametrosDossieProdutoMeiVO.getTipo_relacionamento_conjuge());
            throw new SimtrConfiguracaoException(mensagem);
        }
        
        TipoRelacionamento tomadorContrato = this.tipoRelacionamentoServico.getById(parametrosDossieProdutoMeiVO.getTipo_relacionamento_pj());
        if (Objects.isNull(tomadorContrato)) {
            String mensagem = MessageFormat.format("PEH.vTR.003 - Não foi identificado tipo de relacionamento sob o código {0} para a relação da pessoa jurídica.", parametrosDossieProdutoMeiVO.getTipo_relacionamento_pj());
            throw new SimtrConfiguracaoException(mensagem);
        }       

        TipoRelacionamento socioPF = this.tipoRelacionamentoServico.getById(parametrosDossieProdutoMeiVO.getTipo_relacionamento_socio_pf());
        if (Objects.isNull(socioPF)) {
            String mensagem = MessageFormat.format("PEH.vTR.004 - Não foi identificado tipo de relacionamento sob o código {0} para a relação da pessoa física.", parametrosDossieProdutoMeiVO.getTipo_relacionamento_socio_pf());
            throw new SimtrConfiguracaoException(mensagem);
        }
        
        TipoRelacionamento socioPJ = this.tipoRelacionamentoServico.getById(parametrosDossieProdutoMeiVO.getTipo_relacionamento_socio_pj());
        if (Objects.isNull(socioPJ)) {
            String mensagem = MessageFormat.format("PEH.vTR.005 - Não foi identificado tipo de relacionamento sob o código {0} para a relação da pessoa física.", parametrosDossieProdutoMeiVO.getTipo_relacionamento_socio_pj());
            throw new SimtrConfiguracaoException(mensagem);
        }
        
        return new TiposRelacionamentoValidadoVO(responsavelLegal, conjuge, tomadorContrato, socioPF, socioPJ);
    }
    
    /**
     * 
     * @param pessoaJuridicaVO
     * @return
     */
    public String formataEnderecoComercialPJ(PessoaJuridicaVO pessoaJuridicaVO) {
        return MessageFormat.format("Endereço: {0} {1}, Número: {2} - Bairro: {3}, Complemento: {4} , Referencia: {5}, Cidade: {6} - UF: {7}, CEP: {8} Telefone: {9}{10}",
                pessoaJuridicaVO.getEnderecoComercial().getTipoLogradouro(),
                pessoaJuridicaVO.getEnderecoComercial().getLogradouro(), 
                pessoaJuridicaVO.getEnderecoComercial().getNumero(), 
                pessoaJuridicaVO.getEnderecoComercial().getBairroDistrito(),
                pessoaJuridicaVO.getEnderecoComercial().getComplemento(),
                pessoaJuridicaVO.getEnderecoComercial().getReferencia(), 
                pessoaJuridicaVO.getEnderecoComercial().getMunicipio().getNome(),
                pessoaJuridicaVO.getEnderecoComercial().getUf(),
                pessoaJuridicaVO.getEnderecoComercial().getCep(),
                Objects.nonNull(pessoaJuridicaVO.getTelefoneCelular().getDdd()) ? pessoaJuridicaVO.getTelefoneCelular().getDdd() : null, 
                Objects.nonNull(pessoaJuridicaVO.getTelefoneCelular().getNumero()) ? pessoaJuridicaVO.getTelefoneCelular().getNumero() : null);
    }

    /**
     * 
     * @param protocolo
     * @return
     */
    public PortalEmpreendedorVO consultaServicoPortalEmpreendedorPeloProtocolo(String protocolo) {
        String urlPortalEmpreendedor = System.getProperty("url.servico.portalempreendedor");
        String tokenPortalEmpreendedor = System.getProperty("token.servico.portalempreendedor");
        Response response = UtilRest.consumirServicoOAuth2(urlPortalEmpreendedor
                + protocolo, EnumMetodoHTTP.GET, new HashMap<>(), new HashMap<>(), null, tokenPortalEmpreendedor, UtilWS.APPLICATION_JSON, UtilWS.APPLICATION_JSON);
        PortalEmpreendedorVO portalEmpreendedorVO = null;

        if (response.getStatus() == HttpURLConnection.HTTP_OK) {
            portalEmpreendedorVO = response.readEntity(PortalEmpreendedorVO.class);
        }
        if (response.getStatus() >= HttpURLConnection.HTTP_BAD_REQUEST && response.getStatus() < HttpURLConnection.HTTP_INTERNAL_ERROR) {
            String mensagem = MessageFormat.format("PEH.cSPEPP.001 - Protocolo {0} não localizado na consulta realizada ao portal do empreendedor", protocolo);
            throw new SimtrRequisicaoException(mensagem);
        }
        if (response.getStatus() >= HttpURLConnection.HTTP_INTERNAL_ERROR) {
            String mensagem = MessageFormat.format("PEH.cSPEPP.002 - Falha ao comunicar com o portal do empreendedor. Motivo: {0}", response.readEntity(String.class));
            throw new PortalEmpreendedorException(mensagem, Boolean.FALSE);
        }
        return portalEmpreendedorVO;
    }

    /**
     * 
     * @return
     */
    public ParametrosDossieProdutoMeiVO consultaParametrosCriacaoDossieProdutoMei() {
        String parametrosDossieProdutoJson = System.getProperty("simtr.parametros.portalempreendedor");
        ParametrosDossieProdutoMeiVO parametrosDossieProdutoMeiVO = null;
        try {
            parametrosDossieProdutoMeiVO = (ParametrosDossieProdutoMeiVO) UtilJson.converterDeJson(parametrosDossieProdutoJson, ParametrosDossieProdutoMeiVO.class);
        } catch (Exception e) {
            LOGGER.log(Level.ALL, "PEH.cPCDPM.001 Falha ao fazer parse do Json para VO: " + e.getLocalizedMessage(), e);
        }
        return parametrosDossieProdutoMeiVO;
    }

    /**
     * 
     * @param nomeDocumento
     * @param documentoDTO
     * @return
     */
    public void identificaTipoExtensaoDocumento(String nomeDocumento, DocumentoDTO documentoDTO) {
        if (nomeDocumento.contains(".png")) {
            documentoDTO.setMimeType(FormatoConteudoEnum.PNG.getMimeType());
        } else if(nomeDocumento.contains(".pdf")) {
            documentoDTO.setMimeType(FormatoConteudoEnum.PDF.getMimeType());
        } else {
            documentoDTO.setMimeType(FormatoConteudoEnum.JPEG.getMimeType());
        }
    }

    /**
     * 
     * @param dataTimeZoneString
     * @return
     */
    public String convertDataTimeZoneToStringCalendar(String dataTimeZoneString) {
        dataTimeZoneString = dataTimeZoneString.replace("T", " ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date data = null;
        try {
            Date dataTimeZone = dateFormat.parse(dataTimeZoneString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(dataTimeZone);
            data = cal.getTime();
        } catch (ParseException ex) {
            LOGGER.log(Level.INFO, "PEH.cDTZTSC.001 - Falha ao converter data para string.", ex);
        }
        return dateFormat.format(data);
    }

    // Retirar esse metodo depois que resolver indisponibilidade do servico
    public PortalEmpreendedorVO mockRetornoServicoIntegracaoPoratalEmpreendedor() {
        ObjectMapper objectMapper = new ObjectMapper();
        PortalEmpreendedorVO portalEmpreendedorVO = null;

        try {
            File file = new File(getClass().getClassLoader().getResource("RJ2000000004.json").getFile());
            portalEmpreendedorVO = objectMapper.readValue(file, PortalEmpreendedorVO.class);
        } catch (JsonMappingException e) {
            LOGGER.log(Level.ALL, "Falha no mapeamento do Json para VO: " + e.getLocalizedMessage(), e);
        } catch (JsonParseException e) {
            LOGGER.log(Level.ALL, "Falha ao fazer parse do Json para VO: " + e.getLocalizedMessage(), e);
        } catch (IOException e) {
            LOGGER.log(Level.ALL, "Falha ao ler o arquivo Json: " + e.getLocalizedMessage(), e);
        }
        return portalEmpreendedorVO;
    }
}
