/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.caixa.simtr.controle.servico;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.servico.impl.GEDService;
import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.pedesgo.arquitetura.util.UtilJson;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.enumerator.TipoAtributoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.v1.dossiedigital.AtributoDocumentoDTO;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.util.ConstantesUtil;

/**
 * Classe criada para realizar os ajustes na chamada do dosiê digital. Após a remoção da classe br.gov.caixa.simtr.dossie.visao.rest.v1.DocumentoRES F) e os
 * métodos criados adicionados ao DocumentoServico, esta classe deverá ser excluida.
 *
 * @author c090347
 */
@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRAUD,
    ConstantesUtil.PERFIL_MTRTEC,
    ConstantesUtil.PERFIL_MTRDOSINT,
    ConstantesUtil.PERFIL_MTRDOSMTZ,
    ConstantesUtil.PERFIL_MTRDOSOPE,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ManutencaoDossieDigitalServico {

    @EJB
    private AutorizacaoServico autorizacaoServico;

    @EJB
    private AvaliacaoFraudeServico avaliacaoFraudeServico;

    @EJB
    private CanalServico canalService;

    @EJB
    private CalendarUtil calendarUtil;

    @EJB
    private ComposicaoDocumentalServico composicaoDocumentalServico;

    @EJB
    private CadastroCaixaServico cadastroCaixaServico;

    @EJB
    private DocumentoServico documentoServico;

    @EJB
    private DossieClienteServico dossieClienteServico;

    @EJB
    private GEDService gedService;

    @EJB
    private KeycloakService keycloakService;

    @EJB
    private RelatorioServico relatorioServico;

    @EJB
    private SiecmServico siecmServico;

    @EJB
    private CadastroReceitaServico sicpfServico;

    @EJB
    private TipoDocumentoServico tipoDocumentoServico;
 
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public String geraMinutaBase64(String tipoDocumento, List<AtributoDocumentoDTO> atributosDocumento) {
        final byte[] bytes = geraPDFMinutaDocumento(tipoDocumento, atributosDocumento);
        return new String(Base64.getEncoder().encode(bytes));
    }
    
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRAUD,
        ConstantesUtil.PERFIL_MTRTEC,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public byte[] geraPDFMinutaDocumento(String tipoDocumento, List<AtributoDocumentoDTO> atributosDocumento) {
        // Captura do canal de comunicação
        Canal canal = this.canalService.getByClienteSSO();
        this.canalService.validaRecursoLocalizado(canal, "MDDS.gPMD.001 - Canal de comunicação não localizado para o Cliente ID Autenticado.");

        // Identifica o tipo de documento desejado para a geração da minuta
        final TipoDocumento tipoDocumentoMinuta = this.tipoDocumentoServico.getByNome(tipoDocumento);
        if (tipoDocumentoMinuta == null) {
            throw new SimtrRequisicaoException(MessageFormat.format("Tipo de documento definido não encontrado no sistema. Tipo Informado: {0}", tipoDocumento));
        }

        // Verifica se o tipo de documento desejado prevê a geração da minuta
        if (tipoDocumentoMinuta.getNomeArquivoMinuta() == null) {
            throw new SimtrRequisicaoException(MessageFormat.format("Tipo de documento definido não possui previsão de emissão de minutas. Tipo Informado: {0}", tipoDocumento));
        }

        // Transforma os atributos esperados ao tipo documental em um mapa para facilitar a busca
        Map<String, TipoAtributoEnum> mapaTiposAtributo = tipoDocumentoMinuta.getAtributosExtracao().stream()
                                                                             .filter(a -> a.getTipoAtributoGeralEnum() != null)
                                                                             .collect(Collectors.toMap(a -> a.getNomeAtributoDocumento(), a -> a.getTipoAtributoGeralEnum()));

        // Monta o mapa de objetos ue subsdiara a criação do datasource para a geração da minuta.
        // Valida os atributos enviados se estão definidos para o tipo documental.
        Map<String, Object> dados = atributosDocumento.stream().collect(Collectors.toMap(a -> a.getChave(), a -> {
            TipoAtributoEnum tipoAtributoEnum = mapaTiposAtributo.get(a.getChave());
            if (tipoAtributoEnum == null) {
                throw new SimtrRequisicaoException(MessageFormat.format("Atributo informado não esperado para geração da minuta. Atributo: {0}", a.getChave()));
            }
            try {
                switch (tipoAtributoEnum) {
                    case BOOLEAN:
                        return Boolean.valueOf(a.getValor());
                    case DATE:
                        return this.calendarUtil.toCalendar(a.getValor(), Boolean.FALSE);
                    case DECIMAL:
                        return new BigDecimal(a.getValor());
                    case LONG:
                        return Long.valueOf(a.getValor());
                    case STRING:
                    default:
                        return a.getValor();
                }
            } catch (ParseException ex) {
                throw new SimtrRequisicaoException(MessageFormat.format("Atributo informado para geração da minuta com valor invalido. Atributo: {0}", a.getChave()));
            }
        }));

        // Efetua a emissão da minuta do documento solicitado.
        try {
            String json = UtilJson.converterParaJson(dados);

            Map<String, Object> parametros = new HashMap<>();
            parametros.put("LOGO_CAIXA", ConstantesUtil.RELATORIO_CAMINHO_IMAGENS.concat("caixa.png"));

            String reportName = "dossiedigital/".concat(tipoDocumentoMinuta.getNomeArquivoMinuta());

            return this.relatorioServico.gerarRelatorioPDFJsonDataSource(reportName, json, parametros);

        } catch (Exception e) {
            throw new SimtrConfiguracaoException(MessageFormat.format("Falha ao gerar minuta de documento. Documento Solicitado = {0}", tipoDocumento), e);
        }
    }

}
