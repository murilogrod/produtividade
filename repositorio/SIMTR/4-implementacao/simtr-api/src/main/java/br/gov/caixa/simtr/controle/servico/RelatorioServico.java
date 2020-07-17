/*
 * Copyright (c) 2018 Caixa Econômica Federal. Todos os direitos
 * reservados.
 *
 * Caixa Econômica Federal - simtr-api
 *
 * Este software foi desenvolvido sob demanda da CAIXA e está
 * protegido por leis de direitos autorais e tratados internacionais. As
 * condições de cópia e utilização da totalidade ou partes dependem de autorização da
 * empresa. Cópias não são permitidas sem expressa autorização. Não pode ser
 * comercializado ou utilizado para propósitos particulares.
 *
 * Uso exclusivo da Caixa Econômica Federal. A reprodução ou distribuição não
 * autorizada deste programa ou de parte dele, resultará em punições civis e
 * criminais e os infratores incorrem em sanções previstas na legislação em
 * vigor.
 *
 * Histórico do TFS:
 *
 * LastChangedRevision: $Revision$
 * LastChangedBy: $Author$
 * LastChangedDate: $Date$
 *
 * HeadURL: $HeadURL$
 *
 */
package br.gov.caixa.simtr.controle.servico;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jboss.ejb3.annotation.SecurityDomain;

import com.itextpdf.text.pdf.codec.Base64;

import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.DadosDocumentoLocalizadoDTO;
import br.gov.caixa.pedesgo.arquitetura.entidade.dto.ged.RetornoPesquisaDTO;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.GEDService;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.pedesgo.arquitetura.util.UtilWS;
import br.gov.caixa.simtr.controle.excecao.SiecmException;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.AtributoDocumento;
import br.gov.caixa.simtr.modelo.entidade.Conteudo;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.entidade.DocumentoAdministrativo;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.util.ConstantesUtil;
import br.gov.caixa.simtr.util.KeycloakUtil;
import br.gov.caixa.simtr.visao.SimtrExceptionDTO;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRAUD,
    ConstantesUtil.PERFIL_MTRTEC,
    ConstantesUtil.PERFIL_MTRDOSINT,
    ConstantesUtil.PERFIL_MTRDOSMTZ,
    ConstantesUtil.PERFIL_MTRDOSOPE,
    ConstantesUtil.PERFIL_MTRPAEMTZ,
    ConstantesUtil.PERFIL_MTRPAEOPE,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class RelatorioServico {

    @EJB
    private DocumentoServico documentoServico;

    @EJB
    private GEDService gedService;

    @Inject
    private CalendarUtil calendarUtil;
    
    @Inject
    private KeycloakUtil keycloakUtil;

    @Resource
    private SessionContext sessionContext;

        @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public byte[] gerarRelatorioPDF(String contexto, TipoDocumento tipoDocumentoMinuta, List<AtributoDocumento> atributos, Map<String, Object> parametros) {

        if (tipoDocumentoMinuta.getNomeArquivoMinuta() == null) {
            throw new SimtrRequisicaoException(MessageFormat.format("RS.gMRP.001 - Tipo de documento definido não possui previsão de emissão de minutas. Tipo Informado: {0}", tipoDocumentoMinuta.getNome()));
        }

        // Transforma os atributos esperados ao tipo documental em um mapa para facilitar a busca
        Map<String, TipoAtributoEnum> mapaTiposAtributo = tipoDocumentoMinuta.getAtributosExtracao().stream()
                                                                             .filter(a -> a.getTipoAtributoGeralEnum() != null)
                                                                             .collect(Collectors.toMap(a -> a.getNomeAtributoDocumento(), a -> a.getTipoAtributoGeralEnum()));

        // Monta o mapa de objetos ue subsdiara a criação do datasource para a geração da minuta.
        // Valida os atributos enviados se estão definidos para o tipo documental.
        Map<String, Object> dados = new HashMap<>();
        atributos.forEach(a -> {
            TipoAtributoEnum tipoAtributoEnum = mapaTiposAtributo.get(a.getDescricao());
            if (tipoAtributoEnum == null) {
                throw new SimtrRequisicaoException(MessageFormat.format("DDS.gMRPDD.002 - Atributo informado não esperado para geração da minuta. Atributo: {0}", a.getDescricao()));
            }
            try {
                switch (tipoAtributoEnum) {
                    case BOOLEAN:
                        dados.put(a.getDescricao(), Boolean.valueOf(a.getConteudo()));
                        break;
                    case DATE:
                        dados.put(a.getDescricao(), this.calendarUtil.toCalendar(a.getConteudo(), Boolean.FALSE));
                        break;
                    case DECIMAL:
                        dados.put(a.getDescricao(), new BigDecimal(a.getConteudo()));
                        break;
                    case LONG:
                        dados.put(a.getDescricao(), Long.valueOf(a.getConteudo()));
                        break;
                    case STRING:
                    default:
                        if (a.getOpcoesSelecionadas() != null && !a.getOpcoesSelecionadas().isEmpty()) {
                            String opcoesSelecionadas = "";
                            a.getOpcoesSelecionadas().forEach(os -> opcoesSelecionadas.concat(os.getValorOpcao()).concat(";"));
                            dados.put(a.getDescricao(), opcoesSelecionadas);
                        }
                        dados.put(a.getDescricao(), a.getConteudo());
                }
            } catch (ParseException ex) {
                throw new SimtrRequisicaoException(MessageFormat.format("RS.gMRP.003 - Atributo informado para geração da minuta com valor invalido. Atributo: {0} | Valor Informado: {1}", a.getDescricao(), a.getConteudo()));
            }
        });

        // Efetua a emissão da minuta do documento solicitado.
        try {
            String json = UtilJson.converterParaJson(dados);

            String reportName = contexto.concat("/").concat(tipoDocumentoMinuta.getNomeArquivoMinuta());

            return this.gerarRelatorioPDFJsonDataSource(reportName, json, parametros);

        } catch (Exception e) {
            throw new SimtrRequisicaoException(MessageFormat.format("Falha ao gerar minuta de documento. Documento Solicitado = {0}", tipoDocumentoMinuta.getNome()));
        }
    }
    
    
    /**
     * Executa a geração de um relatorio definido pelo JasperReports presente no diretorio de recuros /relatorios do projeto.
     *
     * @param reportName Nome do relatorio presente no diretorio de recursos do projeto, sem a terminação ".jasper". Importante salientar que para os casos de
     *        relatorios presentes em subdiretorios de /relatorios esse caminho devera estar presente no nome do realtorios
     * @param jsonData String em formato JSON utilizada como fonte de dados do relatorio
     * @param parametros Mapa de parametros definidos no relatorios utilizados no preenchimento
     * @return O relatorio gerado em formato PDF como array de bytes
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    public byte[] gerarRelatorioPDFJsonDataSource(String reportName, String jsonData, Map<String, Object> parametros) {
        try {

            JasperPrint jasperPrint = getPrint(reportName, jsonData, parametros);

            // Export and save pdf to file
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (JRException | UnsupportedEncodingException ex) {
            String mensagem = MessageFormat.format("MSSD.gRPJDS.001 - Falha ao gerar relatorio {0}", reportName);
            throw new SimtrConfiguracaoException(mensagem, ex);

        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    private JasperPrint getPrint(String reportName, String jsonData, Map<String, Object> parametros) throws JRException, UnsupportedEncodingException {
        // Captura o relatorio compilado a partir do diretorio de recursos do projeto.
        String reportPath = ConstantesUtil.RELATORIO_CAMINHO_JASPER.concat(reportName).concat(".jasper");
        InputStream reportInputStream = this.getClass().getClassLoader().getResourceAsStream(reportPath);
        JasperReport report = (JasperReport) JRLoader.loadObject(reportInputStream);

        // Convert json string to byte array.
        ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(jsonData.getBytes(ConstantesUtil.CHARSET_UTF_8));

        // Create json datasource from json stream
        JsonDataSource dataSource = new JsonDataSource(jsonDataStream);

        // Create Jasper Print object passing report, parameter json data source.
        return JasperFillManager.fillReport(report, parametros, dataSource);
    }

    /**
     * Converte paginas PDF base64 para lista de imagens base64
     *
     * @param base64
     * @return List<String>)
     * @throws IOException
     * @throws Exception
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE
    })
    private List<String> convertPDFBase64ToPageBase64(Long idDocumento, String base64) throws IOException {
        List<String> paginas = new ArrayList<>();
        try (
             final PDDocument document = PDDocument.load(new ByteArrayInputStream(Base64.decode(base64)))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bi = pdfRenderer.renderImageWithDPI(page, 72, ImageType.RGB);
                // BufferedImage bi = pdfRenderer.renderImage( page );
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(bi, "JPG", os);
                paginas.add(Base64.encodeBytes(os.toByteArray()));
                bi = null;
                os = null;
            }
            document.close();
            pdfRenderer = null;

        } catch (IOException ex) {
            SimtrExceptionDTO simtrExceptionDTO = new SimtrExceptionDTO();
            simtrExceptionDTO.setFalhaSIMTR(Boolean.TRUE);
            String mensagem = MessageFormat.format("MSSD.gRPJDS.002 - Falha ao converter PDF Image. ID Dcoumento {0}", idDocumento);
            throw new SimtrConfiguracaoException(mensagem, ex);
        }

        return paginas;
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE
    })
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public byte[] getRelatorioPAE(Set<DocumentoAdministrativo> documentosAdministrativos, String nomeArquivoZip, String processoNumeroAno) throws Exception {
        List<Map<String, Object>> itens = new ArrayList<>();
        Integer totalPaginasRelatorio = 0;

        Boolean visualizarConfidencial = sessionContext.isCallerInRole(ConstantesUtil.PERFIL_MTRPAESIG);

        List<DocumentoAdministrativo> documentosOrdenados;
        documentosOrdenados = documentosAdministrativos.stream()
                                                       .sorted((doc1, doc2) -> doc1.getDocumento().getDataHoraCaptura().compareTo(doc2.getDocumento().getDataHoraCaptura()))
                                                       .collect(Collectors.toList());

        for (DocumentoAdministrativo docAdministrativo : documentosOrdenados) {
            if (docAdministrativo.getDataHoraExclusao() == null) {
                String msgDocSubstituto = null;
                String msgDocumento = null;
                Boolean docConfidencial = docAdministrativo.getConfidencial();
                Boolean mostrarPagina = (visualizarConfidencial || (!docConfidencial));
                Documento documento = docAdministrativo.getDocumento();
                String codigoGED = docAdministrativo.getDocumento().getCodigoGED();
                if (codigoGED != null) {
                    documento.setConteudos(new HashSet<>());

                    String token = this.keycloakUtil.getTokenServico();
                    String ip = this.keycloakUtil.getIpFromToken(token);
                    RetornoPesquisaDTO retornoGED = this.gedService.searchDocument(codigoGED, ConstantesUtil.SIECM_OS_PROCESSO_ADMINISTRATIVO, ip, token);

                    if (retornoGED == null) {
                        String mensagem = MessageFormat.format("RS.gRP.001 - Falha ao obter doumento do GED. ID  {0}", codigoGED);
                        throw new SiecmException(mensagem, Boolean.FALSE);
                    }

                    int ordem = 1;
                    adicionarConteudoDocumento(documento, retornoGED, ordem);
                } else {
                    Long idDocumento = docAdministrativo.getDocumento().getId();
                    documento = this.documentoServico.getById(idDocumento, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
                }

                for (Conteudo conteudo : documento.getConteudos()) {
                    try {
                        List<String> paginas = this.convertPDFBase64ToPageBase64(docAdministrativo.getId(), conteudo.getBase64());
                        totalPaginasRelatorio += paginas.size();

                        if (docAdministrativo.getDocumentoSubstituto() != null) {
                            msgDocumento = docAdministrativo.getJustificativaSubstituicao();

                            Long idDocumentoSubstituto = docAdministrativo.getDocumentoSubstituto().getId();
                            Integer[] posicaoSubstituto = localizaDocumentoSubstituto(totalPaginasRelatorio, docAdministrativo.getId(), idDocumentoSubstituto, documentosOrdenados);

                            msgDocSubstituto = "Documento Substituto início página " + posicaoSubstituto[0] + " até " + posicaoSubstituto[1];
                        } else {
                            msgDocSubstituto = null;
                        }

                        for (String pagina : paginas) {
                            Map<String, Object> item = new HashMap<>();
                            item.put("PAGINA", pagina);
                            item.put("SUBSTITUIDO", (docAdministrativo.getDocumentoSubstituto() != null));
                            item.put("JUSTIFICATIVA", msgDocumento);
                            item.put("DOC_SUBSTITUTO", msgDocSubstituto);
                            item.put("MATRICULA", conteudo.getDocumento().getResponsavel());
                            item.put("DATAHORA", (new SimpleDateFormat("dd/MM/yyyy HH:mm")).format(conteudo.getDocumento().getDataHoraCaptura().getTime()));
                            item.put("CONFIDENCIAL", docConfidencial);
                            item.put("MOSTRAR_PAGINA", mostrarPagina);
                            itens.add(item);
                        }
                    } catch (IOException ex) {
                        String mensagem = MessageFormat.format("RS.gRP.002 - Falha ao converter DocumentoAdministrativo para PDF. ID Dcoumento {0}", docAdministrativo.getId());
                        throw new SimtrConfiguracaoException(mensagem, ex);
                    }
                    break;
                }

            }

        }
        String jsonData = UtilJson.converterParaJson(itens);
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("LOGO_CAIXA", ConstantesUtil.RELATORIO_CAMINHO_IMAGENS.concat("caixa.png"));
        parametros.put("SEM_EFEITO", ConstantesUtil.RELATORIO_CAMINHO_IMAGENS.concat("sem_efeito.png"));
        parametros.put("TITULO", ConstantesUtil.PROCESSO_ADMINISTRATIVO_ELETRONICO);
        parametros.put("PROCESSO", processoNumeroAno);

        return this.gerarRelatorioPDFJsonDataSource("pae/pae_documentos", jsonData, parametros);
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE
    })
    private void adicionarConteudoDocumento(Documento documento, RetornoPesquisaDTO retornoGED, int ordem) {
        for (DadosDocumentoLocalizadoDTO dadosDocumento : retornoGED.getDadosDocumentoLocalizados()) {
            String link = dadosDocumento.getLink();
            byte[] bytes = UtilWS.obterBytes(link);
            String conteudoBase64 = Base64.encodeBytes(bytes);
            Conteudo c = new Conteudo(conteudoBase64, ordem++);
            c.setDocumento(documento);
            documento.addConteudos(c);
        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE
    })
    public ZipOutputStream adicionaArquivoZIP(byte[] dados, ZipOutputStream zipOut, String nomeArquivo, boolean incluirSemConteudo, String matricula) throws Exception {
        if (dados.length < 1000 && incluirSemConteudo) {
            List<Map<String, Object>> itens = new ArrayList<>();
            Map<String, Object> item = new HashMap<>();
            item.put("MENSAGEM", "NÃO FOI ADICIONADO DOCUMENTO PARA O ARQUIVO " + nomeArquivo);
            item.put("MATRICULA", matricula);
            item.put("DATAHORA", "");
            item.put("CONFIDENCIAL", "");

            itens.add(item);
            String jsonData = UtilJson.converterParaJson(itens);
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("LOGO_CAIXA", ConstantesUtil.RELATORIO_CAMINHO_IMAGENS.concat("caixa.png"));
            parametros.put("TITULO", ConstantesUtil.PROCESSO_ADMINISTRATIVO_ELETRONICO);
            parametros.put("PROCESSO", "");
            dados = this.gerarRelatorioPDFJsonDataSource("pae/pae_documentos", jsonData, parametros);
        }
        try (
             ByteArrayInputStream fis = new ByteArrayInputStream(dados)) {
            ZipEntry zipEntry = new ZipEntry(nomeArquivo);
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[4096];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        } catch (IOException ex) {
            String mensagem = MessageFormat.format("MSSD.gRPJDS.004 - Falha ao adicionar o arquivo de exportação. Arquivo {0}", nomeArquivo);
            throw new SimtrConfiguracaoException(mensagem, ex);
        }
        return zipOut;
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE
    })
    private Integer getNumeroDePaginasPDF(String base64) throws IOException {
        Integer paginas;
        try (
             PDDocument document = PDDocument.load(new ByteArrayInputStream(Base64.decode(base64)))) {
            paginas = document.getNumberOfPages();
        }

        return paginas;
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRPAEMTZ,
        ConstantesUtil.PERFIL_MTRPAEOPE
    })
    private Integer[] localizaDocumentoSubstituto(Integer totalPaginasRelatorio, Long idSubstituido, Long idSubstituto, List<DocumentoAdministrativo> documentosAdministrativo) throws IOException {
        boolean substituidoLocalizado = false;
        boolean substitutoLocalizado = false;
        Integer totalPaginasAteSubstituto = 0;
        Integer totalPaginasPDF = 0;
        Integer[] posicaoSubstituto = new Integer[2];

        for (DocumentoAdministrativo docAdministrativo : documentosAdministrativo) {
            if (docAdministrativo.getDataHoraExclusao() == null) {

                if (docAdministrativo.getId().equals(idSubstituido)) {
                    substituidoLocalizado = true;

                } else if (substituidoLocalizado) {
                    if (docAdministrativo.getId().equals(idSubstituto)) {
                        substitutoLocalizado = true;
                    }

                    Documento documento = docAdministrativo.getDocumento();
                    String codigoGED = docAdministrativo.getDocumento().getCodigoGED();
                    if (codigoGED != null) {
                        documento.setConteudos(new HashSet<>());
                        RetornoPesquisaDTO retornoGED = this.gedService.searchDocument(codigoGED, ConstantesUtil.SIECM_OS_PROCESSO_ADMINISTRATIVO, keycloakUtil.getIpFromToken());
                        if (retornoGED == null) {
                            String mensagem = MessageFormat.format("RS.lDSRP.001 - Falha ao obter doumento do GED. ID  {0}", codigoGED);
                            throw new SiecmException(mensagem, Boolean.FALSE);
                        }

                        int ordem = 1;
                        adicionarConteudoDocumento(documento, retornoGED, ordem);
                    } else {
                        documento = this.documentoServico.getById(docAdministrativo.getDocumento()
                                                                                   .getId(), Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
                    }
                    for (Conteudo conteudo : documento.getConteudos()) {
                        totalPaginasPDF = this.getNumeroDePaginasPDF(conteudo.getBase64());
                        totalPaginasAteSubstituto += totalPaginasPDF;
                    }
                    if (substitutoLocalizado) {
                        posicaoSubstituto[0] = totalPaginasRelatorio + totalPaginasAteSubstituto - totalPaginasPDF + 1;
                        posicaoSubstituto[1] = totalPaginasRelatorio + totalPaginasAteSubstituto;
                        break;
                    }
                }
            }
        }
        return posicaoSubstituto;
    }
}
