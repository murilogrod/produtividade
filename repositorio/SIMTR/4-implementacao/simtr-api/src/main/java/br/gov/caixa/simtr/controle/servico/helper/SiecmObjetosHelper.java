package br.gov.caixa.simtr.controle.servico.helper;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.AtributosDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.CampoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.DadosRequisicaoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.DocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMCriaDossieDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMCriaTransacaoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMDadosPessoaFisicaDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMDadosPessoaJuridicaDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.ECMGravaDocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.GravaDocumentoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.PastaDTO;
import br.gov.caixa.pedesgo.arquitetura.enumerador.AcaoECMEnum;
import br.gov.caixa.pedesgo.arquitetura.util.UtilUsuario;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.OpcaoSelecionada;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.GEDTipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE,
    ConstantesUtil.PERFIL_MTRDOSINT,
    ConstantesUtil.PERFIL_MTRDOSMTZ,
    ConstantesUtil.PERFIL_MTRDOSOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class SiecmObjetosHelper {

    @Inject
    private CalendarUtil calendarUtil;

    @Inject
    private UtilUsuario utilUsuario;
    
    private static final String CLASSE_PADRAO_DOCUMENTOS_CLIENTE = "DOCUMENTOS_CLIENTE";
    private static final String CLASSE_PADRAO_DOCUMENTOS_TRANSACAO = "CAIXA";

    public List<CampoDTO> montaCamposDocumentoSIECM(TipoDocumento tipoDocumento, Set<AtributoDocumento> atributos) {
        Map<String, String> mapaAtributosDocumento = atributos.stream().collect(Collectors.toMap(AtributoDocumento::getDescricao, ad -> {
            String retorno = ad.getConteudo() == null ? "" : ad.getConteudo();
            if (ad.getOpcoesSelecionadas() != null && !ad.getOpcoesSelecionadas().isEmpty()) {
                String opcoes = "[";
                for (OpcaoSelecionada os : ad.getOpcoesSelecionadas()) {
                    opcoes.concat(os.getValorOpcao());
                    opcoes.concat("=");
                    opcoes.concat(os.getDescricaoOpcao());
                }
                opcoes.concat("]");
            }
            return retorno;
        }));
        return this.montaCamposDocumentoSIECM(tipoDocumento, mapaAtributosDocumento);
    }

    public List<CampoDTO> montaCamposDocumentoSIECM(TipoDocumento tipoDocumento, Map<String, String> atributos) {

        List<CampoDTO> camposDocumentoECM = new ArrayList<>();

        // Monta a descrição do documento para envio em caso de exceção
        String descricaoDocumento = tipoDocumento.getCodigoTipologia() == null ? "0000000000000000 - ".concat(tipoDocumento.getNome()) : tipoDocumento.getCodigoTipologia().concat(" - ").concat(tipoDocumento.getNome());

        // Percorre a lista de atributos esperados para o tipo de documento e carrega os dados extraidos na classe documental a ser submetida ao SIECM
        tipoDocumento.getAtributosExtracao().stream()
                     // Caso o atributo indique a aplicação na classe documental do GED inclui o atributo no documento
                     .filter(atributoExtracao -> (atributoExtracao.getNomeAtributoSIECM() != null))
                     .forEachOrdered(atributoExtracao -> {
                         String valorExtraido = atributos.get(atributoExtracao.getNomeAtributoDocumento());
                         String tipoAtributoSIECM = atributoExtracao.getTipoAtributoSiecmEnum().name();
                         boolean falhaConversao = Boolean.FALSE;
                         try {
                             String valorEnvioGED = this.converteAtributoSIECM(atributoExtracao.getTipoAtributoSiecmEnum(), valorExtraido);
                             camposDocumentoECM.add(new CampoDTO(atributoExtracao.getNomeAtributoSIECM(), valorEnvioGED, tipoAtributoSIECM, atributoExtracao.getObrigatorioSIECM()));
                         } catch (ParseException | RuntimeException e) {
                             // Caso ocorra falha na conversão, o atributo seja obrigátorio e não possua valor padrão definido lança exceção por falha de configuração
                             if (Objects.nonNull(atributoExtracao.getObrigatorioSIECM()) && atributoExtracao.getObrigatorioSIECM() && atributoExtracao.getValorPadrao() == null) {
                                 String mensagem = MessageFormat.format("SOH.mCDS.001 - Falha ao converter atributo para GED. Informação obrigatória e valor padrão não definido. Tipo de Documento: {0} | Atributo: {1} | Valor Atributo: {2}", descricaoDocumento, atributoExtracao.getNomeNegocial(), valorExtraido);
                                 throw new SimtrConfiguracaoException(mensagem, e);
                             }

                             // Marca indicador que houve falha na conversão para que seja tentado converter o valor padrão.
                             falhaConversao = Boolean.TRUE;

                         }
                         // Caso o atributo seja obrigatorio e tenha ocorrido falha na conversão aplicará valor padrão definido para o campo
                         if (falhaConversao && Objects.nonNull(atributoExtracao.getObrigatorioSIECM()) && atributoExtracao.getObrigatorioSIECM()) {
                             try {
                                 String valorEnvioGED = this.converteAtributoSIECM(atributoExtracao.getTipoAtributoSiecmEnum(), atributoExtracao.getValorPadrao());
                                 camposDocumentoECM.add(new CampoDTO(atributoExtracao.getNomeAtributoSIECM(), valorEnvioGED, tipoAtributoSIECM, atributoExtracao.getObrigatorioSIECM()));
                             } catch (ParseException | RuntimeException e) {
                                 String mensagem = MessageFormat.format("SOH.mCDS.002 - Falha ao converter atributo para GED. Informação obrigatória e valor padrão definido invalido. Tipo de Documento: {0} | Atributo: {1} | Valor Atributo: {2} | Valor Padrão: {3}", descricaoDocumento, atributoExtracao.getNomeNegocial(), valorExtraido, atributoExtracao.getValorPadrao());
                                 throw new SimtrConfiguracaoException(mensagem, e);
                             }
                         }
                     });

        return camposDocumentoECM;
    }

    public ECMCriaTransacaoDTO montaObjetoTransacao(final String diretorio, final String objectStore) {
        // Inicializa o objeto que representa a solicitação de criação de uma transação perante o SIECM
        ECMCriaTransacaoDTO ecmCriaTransacaoDTO = new ECMCriaTransacaoDTO();

        // Cria o objeto que representa os dados da requisição que compõem a solicitação
        DadosRequisicaoDTO dadosRequisicaoDTO = new DadosRequisicaoDTO();
        dadosRequisicaoDTO.setIpUsuarioFinal(utilUsuario.getIpUsuario());
        dadosRequisicaoDTO.setLocalArmazenamento(objectStore);
        dadosRequisicaoDTO.setTipoRequisicao(AcaoECMEnum.GRAVA_DOCUMENTO_DOSSIE.name());
        ecmCriaTransacaoDTO.setDadosRequisicao(dadosRequisicaoDTO);

        // Define junto ao objeto que representa a solicitação a subpasta a ser criada
        ecmCriaTransacaoDTO.setTransacao(diretorio);

        return ecmCriaTransacaoDTO;
    }

    public ECMCriaDossieDTO montaObjetoDossie(final Long cpfCnpj, final TipoPessoaEnum tipoPessoaEnum, final String objectStore) {
        // Inicializa o objeto que representa a solicitação de criação de uma transação perante o SIECM
        ECMCriaDossieDTO ecmCriaDossieDTO = new ECMCriaDossieDTO();

        // Cria o objeto que representa os dados da requisição que compõem a solicitação
        DadosRequisicaoDTO dadosRequisicaoDTO = new DadosRequisicaoDTO();
        dadosRequisicaoDTO.setIpUsuarioFinal(utilUsuario.getIpUsuario());
        dadosRequisicaoDTO.setLocalArmazenamento(objectStore);
        dadosRequisicaoDTO.setTipoRequisicao(AcaoECMEnum.CRIA_DOSSIE.name());
        ecmCriaDossieDTO.setDadosRequisicaoDTO(dadosRequisicaoDTO);

        // Cria o objeto que representa a identificação da pessoa peranto o SIECM
        if (TipoPessoaEnum.F.equals(tipoPessoaEnum)) {
            ecmCriaDossieDTO.setDadosPF(new ECMDadosPessoaFisicaDTO(StringUtils.leftPad(String.valueOf(cpfCnpj), 11, '0')));
            ecmCriaDossieDTO.setTipoCliente("PF");
        } else {
            ecmCriaDossieDTO.setDadosPJ(new ECMDadosPessoaJuridicaDTO(StringUtils.leftPad(String.valueOf(cpfCnpj), 14, '0')));
            ecmCriaDossieDTO.setTipoCliente("PJ");
        }

        return ecmCriaDossieDTO;
    }

    public ECMGravaDocumentoDTO montaObjetoDocumentoCliente(final Long cpfCnpj, final TipoPessoaEnum tipoPessoaEnum, final Documento documentoSIMTR, final String binario, final String objectStore) {
        // Inicializa o objeto que representa a solicitação de criação de um documento perante o SIECM
        ECMGravaDocumentoDTO ecmGravaDocumentoDTO = new ECMGravaDocumentoDTO();

        // Cria o objeto que representa os dados da requisição que compões a solicitação
        DadosRequisicaoDTO dadosRequisicaoDTO = new DadosRequisicaoDTO();
        dadosRequisicaoDTO.setIpUsuarioFinal(utilUsuario.getIpUsuario());
        dadosRequisicaoDTO.setLocalArmazenamento(objectStore);
        dadosRequisicaoDTO.setTipoRequisicao(AcaoECMEnum.GRAVA_DOCUMENTO_DOSSIE.name());
        ecmGravaDocumentoDTO.setDadosRequisicao(dadosRequisicaoDTO);

        // Formata o CPF/CNPJ com zeros a esquerda e cria o objeto que representa a identificação da pessoa peranto o SIECM
        String cpfCnpjFormatado;
        if (TipoPessoaEnum.F.equals(tipoPessoaEnum)) {
            cpfCnpjFormatado = StringUtils.leftPad(String.valueOf(cpfCnpj), 11, '0');
            ecmGravaDocumentoDTO.setDadosPF(new ECMDadosPessoaFisicaDTO(cpfCnpjFormatado));
            ecmGravaDocumentoDTO.setTipoCliente("PF");
        } else {
            cpfCnpjFormatado = StringUtils.leftPad(String.valueOf(cpfCnpj), 14, '0');
            ecmGravaDocumentoDTO.setDadosPJ(new ECMDadosPessoaJuridicaDTO(cpfCnpjFormatado));
            ecmGravaDocumentoDTO.setTipoCliente("PJ");
        }

        // Cria o objeto que representa o documento dentro da solicitação do SIECM
        // O elemento recebe uma lista de documento, mesmo que cada requisição encaminhe apenas um registro
        final List<DocumentoDTO> listaDocumentosDTO = new ArrayList<>();
        DocumentoDTO documentoDTO = this.montaObjetoDocumentoDTO(documentoSIMTR, binario, CLASSE_PADRAO_DOCUMENTOS_CLIENTE);

        // Adiciona ou atualiza o campo que representa o identificador do cliente de vinculação do documento junto ao SIECM
        if (documentoDTO.getAtributos().getCampos().stream().noneMatch(c -> c.getNome().equals("IDENTIFICADOR_CLIENTE"))) {
            documentoDTO.getAtributos().getCampos().add(new CampoDTO("IDENTIFICADOR_CLIENTE", cpfCnpjFormatado, GEDTipoAtributoEnum.STRING.name(), Boolean.TRUE));
        } else {
            documentoDTO.getAtributos().getCampos().stream().filter(c -> c.getNome().equals("IDENTIFICADOR_CLIENTE")).findFirst().ifPresent(campo -> {
                campo.setValor(cpfCnpjFormatado);
            });
        }

        // Adiciona o documento na lista e a lista na requisção finalizando a monatagem.
        listaDocumentosDTO.add(documentoDTO);
        ecmGravaDocumentoDTO.setDocumentos(listaDocumentosDTO);

        return ecmGravaDocumentoDTO;
    }

    public GravaDocumentoDTO montaObjetoOperacao(final Documento documentoSIMTR, final String binario, final String pasta, final String subPasta, final String objectStore) {
        // Inicializa o objeto que representa a solicitação de criação de um documento perante o SIECM
        GravaDocumentoDTO gravaDocumentoDTO = new GravaDocumentoDTO();

        // Cria o objeto que representa os dados da requisição que compões a solicitação
        DadosRequisicaoDTO dadosRequisicaoDTO = new DadosRequisicaoDTO();
        dadosRequisicaoDTO.setIpUsuarioFinal(utilUsuario.getIpUsuario());
        dadosRequisicaoDTO.setLocalArmazenamento(objectStore);
        dadosRequisicaoDTO.setTipoRequisicao(AcaoECMEnum.GRAVA_DOCUMENTO_DOSSIE.name());
        gravaDocumentoDTO.setDadosRequisicao(dadosRequisicaoDTO);

        // Cria o objeto que representa a pasta de alocação do arquivo no SIECM
        PastaDTO pastaDTO = new PastaDTO();
        pastaDTO.setNome(pasta);
        pastaDTO.setSubPasta(subPasta);
        gravaDocumentoDTO.setPasta(pastaDTO);

        // Cria o objeto que representa o documento a ser vinculado na requisição
        DocumentoDTO documentoDTO = this.montaObjetoDocumentoDTO(documentoSIMTR, binario, CLASSE_PADRAO_DOCUMENTOS_TRANSACAO);

        // Adiciona o documento na requisção finalizando a monatagem.
        gravaDocumentoDTO.addDocumentoDTO(documentoDTO);

        return gravaDocumentoDTO;
    }

    // ************ METODOS PRIVADOS **************//

    private DocumentoDTO montaObjetoDocumentoDTO(Documento documentoSIMTR, String binario, String classePadrao) {
        // Cria o objeto que representa o documento dentro da solicitação do SIECM
        DocumentoDTO documentoDTO = new DocumentoDTO();

        // Monta a lista de campos para ser enviado a partir dos atributos do documento
        List<CampoDTO> camposDocumentoECM = this.montaCamposDocumentoSIECM(documentoSIMTR.getTipoDocumento(), documentoSIMTR.getAtributosDocumento());

        // Adiciona a lista os elementos obrigatorios da classe raiz caso estes não estejam definidos no documento e adiciona na lista
        if (camposDocumentoECM.stream().noneMatch(c -> c.getNome().equals("EMISSOR"))) {
            camposDocumentoECM.add(new CampoDTO("EMISSOR", "N/A", GEDTipoAtributoEnum.STRING.name(), Boolean.TRUE));
        }
        if (camposDocumentoECM.stream().noneMatch(c -> c.getNome().equals("DATA_EMISSAO"))) {
            camposDocumentoECM.add(new CampoDTO("DATA_EMISSAO", this.calendarUtil.toStringGED(documentoSIMTR.getDataHoraCaptura()), GEDTipoAtributoEnum.DATE.name(), Boolean.TRUE));
        }
        if (camposDocumentoECM.stream().noneMatch(c -> c.getNome().equals("CLASSIFICACAO_SIGILO"))) {
            camposDocumentoECM.add(new CampoDTO("CLASSIFICACAO_SIGILO", "#CONFIDENCIAL 05", GEDTipoAtributoEnum.STRING.name(), Boolean.TRUE));
        }

        camposDocumentoECM.add(new CampoDTO("RESPONSAVEL_CAPTURA", documentoSIMTR.getResponsavel(), GEDTipoAtributoEnum.STRING.name(), Boolean.TRUE));
        camposDocumentoECM.add(new CampoDTO("STATUS", documentoSIMTR.getSituacaoTemporalidade().getId().toString(), GEDTipoAtributoEnum.STRING.name(), Boolean.TRUE));

        // Define os atributos do documento (metadados) que devem ser vinculados a todos os registros e inclui os campos do documento
        String nomeArquivo = String.valueOf(documentoSIMTR.getTipoDocumento().getNome()).replaceAll(" ", "_").concat(".").concat(documentoSIMTR.getFormatoConteudoEnum().name().toLowerCase());
        AtributosDTO atributos = new AtributosDTO();
        atributos.setClasse(documentoSIMTR.getTipoDocumento().getNomeClasseSIECM() == null ? classePadrao : documentoSIMTR.getTipoDocumento().getNomeClasseSIECM());
        atributos.setTipo(documentoSIMTR.getFormatoConteudoEnum().name());
        atributos.setMimeType(documentoSIMTR.getFormatoConteudoEnum().getMimeType());
        atributos.setNome(nomeArquivo);
        atributos.setCampos(camposDocumentoECM);
        documentoDTO.setAtributos(atributos);

        // Atribui o binario do documento
        documentoDTO.setBinario(binario);

        return documentoDTO;
    }

    private String converteAtributoSIECM(TipoAtributoEnum tipoAtributoEnum, String valorAtributo) throws ParseException {
        if (valorAtributo == null) {
            throw new IllegalArgumentException("Valor atributo encaminhado nulo para conversão");
        }
        String resultado;
        switch (tipoAtributoEnum) {
            case BOOLEAN:
                resultado = Boolean.valueOf(valorAtributo).toString();
                break;
            case DATE:
                Calendar calendar = calendarUtil.toCalendar(valorAtributo, Boolean.FALSE);
                resultado = calendarUtil.toStringGED(calendar);
                break;
            case DECIMAL:
                resultado = Double.valueOf(valorAtributo).toString();
                break;
            case LONG:
                resultado = Long.valueOf(valorAtributo).toString();
                break;
            case STRING:
            default:
                resultado = String.valueOf(valorAtributo);
                break;
        }
        return resultado;
    }
}
