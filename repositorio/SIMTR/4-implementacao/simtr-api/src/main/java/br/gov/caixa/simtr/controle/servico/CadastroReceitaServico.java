package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.siiso.dto.RetornoPessoasFisicasDTO;
import br.gov.caixa.pedesgo.arquitetura.siiso.dto.RetornoPessoasJuridicasDTO;
import br.gov.caixa.pedesgo.arquitetura.siiso.servico.CadastroReceitaPFServico;
import br.gov.caixa.pedesgo.arquitetura.siiso.servico.CadastroReceitaPJServico;
import br.gov.caixa.pedesgo.arquitetura.util.UtilCpf;
import br.gov.caixa.simtr.controle.excecao.SicpfException;
import br.gov.caixa.simtr.controle.excecao.SimtrRequisicaoException;
import br.gov.caixa.simtr.modelo.entidade.AtributoExtracao;
import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.enumerator.SICPFModoEnum;
import br.gov.caixa.simtr.util.CalendarUtil;
import br.gov.caixa.simtr.util.ConstantesUtil;

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
public class CadastroReceitaServico {

    @EJB
    private CadastroReceitaPFServico cadastroReceitaPFServico;
    
    @EJB
    private CadastroReceitaPJServico cadastroReceitaPJServico;

    @Inject
    private CalendarUtil calendarUtil;

    private static final Logger LOGGER = Logger.getLogger(CadastroReceitaServico.class.getName());

    /**
     * Realiza a validação de um determinado documento baseado nas informações oriundas da consulta junto ao SICPF<br>
     * Para que a validação seja bem sucedida, é necessario:<br>
     * <ul>
     * <li>O documento precisa estar alimentado com as informações:</li>
     * <ul>
     * <li>Tipo de Documento com os atributos de extração</li>
     * <li>Atributos do documento preenchidos</li>
     * </ul>
     * <li>CPF informado válido</li>
     * </ul>
     *
     * @param cpf Numero do CPF de referência para consulta junto ao SICPF.
     * @param documento Documento a ser validado com as informações do SICPF.
     * @throws SicpfException Lançada em caso de falha de comunicação com o serviço do SICPF.
     * @throws SimtrRequisicaoException Lançada em caso de falha na validação dos atributos.
     */
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
    public void validaDocumentoSICPF(Long cpf, Documento documento) {

        // Executa a validação da estrutura do docmento e consistência do CPF
        this.validaEstruturaDocumento(cpf, documento);

        // Captura os atributos do documento em estrutura de mapa para facilitar a identificação dos dados
        final Map<String, String> mapaAtributosDocumento = new HashMap<>();
        documento.getAtributosDocumento().forEach(atributoDocumento -> mapaAtributosDocumento.put(atributoDocumento.getDescricao(), atributoDocumento.getConteudo()));

        // Inicializa objeto que armazenará o resultado a consulta realizada junto ao SICPF
        RetornoPessoasFisicasDTO retornoPessoasFisicasDTO = null;

        // Percorre a lista de atributos configurados para a tipologia do documento encaminhado
        Set<AtributoExtracao> atributosExtracao = documento.getTipoDocumento().getAtributosExtracao();
        for (AtributoExtracao atributoExtracao : atributosExtracao) {
            String valorAtributoDocumento = mapaAtributosDocumento.get(atributoExtracao.getNomeAtributoDocumento());

            // Caso a configuração atributo indique algum campo de batimento com o SICPF entra no bloco para verificar como a validação deverá ser feita.
            if (atributoExtracao.getSicpfCampoEnum() != null) {

                // Realiza a consulta junto ao SICPF caso ainda não esteja em posse do resultado
                retornoPessoasFisicasDTO = retornoPessoasFisicasDTO == null ? this.consultaCadastroPF(cpf) : retornoPessoasFisicasDTO;

                switch (atributoExtracao.getSicpfCampoEnum()) {
                    case NOME:
                        this.validaCampoNomeContribuinte(SICPFModoEnum.E.equals(atributoExtracao.getSicpfModoEnum()), retornoPessoasFisicasDTO.getNomeContribuinte(), atributoExtracao.getNomeAtributoDocumento(), valorAtributoDocumento);
                        break;
                    case NOME_MAE:
                        this.validaCampoNomeMae(SICPFModoEnum.E.equals(atributoExtracao.getSicpfModoEnum()), retornoPessoasFisicasDTO.getNomeMae(), atributoExtracao.getNomeAtributoDocumento(), valorAtributoDocumento);
                        break;
                    case NASCIMENTO:
                        this.validaCampoDataNascimento(retornoPessoasFisicasDTO.getDataNascimento(), atributoExtracao.getNomeAtributoDocumento(), valorAtributoDocumento);
                        break;
                    case ELEITOR:
                        this.validaCampoTituloEleitor(Long.valueOf(retornoPessoasFisicasDTO.getTituloEleitor()), atributoExtracao.getNomeAtributoDocumento(), valorAtributoDocumento);
                        break;
                    case CPF:
                    default:
                        this.validaCampoCPF(cpf, atributoExtracao.getNomeAtributoDocumento(), valorAtributoDocumento);
                        break;
                }
            }
        }
    }

    /**
     * Realiza a consulta de um CPF informado junto ao serviço sdo SICPF
     *
     * @param cpf Numero do registro de CPF a ser consultado
     * @return Resposta encaminhado pelo SICPF ou null caso o CPF não seja encontrado
     * @throws SicpfException Lançada em caso de falha de comunicação com o serviço do SICPF
     */
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
    public RetornoPessoasFisicasDTO consultaCadastroPF(Long cpf) {
        RetornoPessoasFisicasDTO retornoPessoasFisicasDTO;
        String cpfFormatado = StringUtils.leftPad(String.valueOf(cpf), 11, '0');
        try {
            retornoPessoasFisicasDTO = this.cadastroReceitaPFServico.consultarPF(cpfFormatado);
            if (retornoPessoasFisicasDTO == null || retornoPessoasFisicasDTO.getNomeContribuinte() == null) {
                throw new SicpfException("CPF não localizado no SICPF.", Boolean.TRUE);
            }
            return retornoPessoasFisicasDTO;
        } catch (Exception e) {
            if (e.getLocalizedMessage().contains("DOCUMENTO NÃO LOCALIZADO NA BASE FISCAL")) {
                return null;
            }
            String mensagem = MessageFormat.format("SS.cS.001 - Falha ao comunicar com o SICPF. Causa: {0}", e.getLocalizedMessage());
            throw new SicpfException(mensagem, e, Boolean.TRUE);
        }
    }
    
    /**
     * Realiza a consulta de um CNPJ informado junto ao serviço sdo Cadastro Receita
     *
     * @param cnpj Numero do registro de CNPJ a ser consultado
     * @return Resposta encaminhado pelo Cadastro Receita ou null caso o CNPJ não seja encontrado
     * @throws SicpfException Lançada em caso de falha de comunicação com o serviço do SICPF
     */
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
    public RetornoPessoasJuridicasDTO consultaCadastroPJ(Long cnpj) {
        RetornoPessoasJuridicasDTO retornoPessoasJuridicasDTO;
        String cpfFormatado = StringUtils.leftPad(String.valueOf(cnpj), 14, '0');
        try {
            retornoPessoasJuridicasDTO = this.cadastroReceitaPJServico.consultarPJ(cpfFormatado);
            if (retornoPessoasJuridicasDTO == null || retornoPessoasJuridicasDTO.getEstabelecimento() == null || retornoPessoasJuridicasDTO.getEstabelecimento().getRazaoSocial() == null) {
                throw new SicpfException("CNPJ não localizado no Cadastro da Receita Federal.", Boolean.TRUE);
            }
            return retornoPessoasJuridicasDTO;
        } catch (Exception e) {
            if (e.getLocalizedMessage().contains("DOCUMENTO NÃO LOCALIZADO NA BASE FISCAL")) {
                return null;
            }
            String mensagem = MessageFormat.format("SS.cS.001 - Falha ao comunicar com o SICPF. Causa: {0}", e.getLocalizedMessage());
            throw new SicpfException(mensagem, e, Boolean.TRUE);
        }
    }

    // *********** Métodos privados auxiliares ***************
    /**
     * Método utilizado para validar os prérequisitos e estrutura do documento
     *
     * @param cpf CPF indicado para realizar a consulta junto ao SICPF
     * @param documento Documento encaminhado para validação
     */
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
    private void validaEstruturaDocumento(Long cpf, Documento documento) {
        if ((cpf == null) || (!UtilCpf.isValidCpf(cpf))) {
            LOGGER.log(Level.WARNING, "SS.vED.001 - CPF encaminhado para validação SICPF inválido");
            throw new IllegalArgumentException("CPF encaminhado para validação SICPF inválido.");
        }
        if (documento.getTipoDocumento() == null) {
            LOGGER.log(Level.WARNING, "SS.vED.002 - Documento encaminhado para validação SICPF não contem o tipo de documento definido.");
            throw new IllegalArgumentException("Documento encaminhado para validação SICPF não contem o tipo de documento definido.");
        }
        if (documento.getTipoDocumento().getAtributosExtracao().isEmpty()) {
            LOGGER.log(Level.WARNING, "SS.vED.003 - Documento encaminhado validação SICPF não contem a lista de atributos de extração do tipo carregada.");
            throw new IllegalArgumentException("Documento encaminhado validação SICPF não contem a lista de atributos de extração do tipo carregada.");
        }
        if (documento.getAtributosDocumento().isEmpty()) {
            LOGGER.log(Level.WARNING, "SS.vED.004 - Documento encaminhado para validação SICPF não contem a lista de atributos do documento carregada.");
            throw new IllegalArgumentException("Documento encaminhado para validação SICPF não contem a lista de atributos do documento carregada.");
        }
    }

    /**
     * Método utilizado para realizar a validação de um atributo do documento com o vaor do campo "NOME CONTRIBUINTE" oriundo da consulta ao SICPF
     *
     * @param comparacaoExata Indicador se a comparação deve ser realizada baseada no valor exato ou se o campo contem o nome do contribuinte na informação.
     * @param nomeContribuinte Nome do Contribuinte obtido junto ao SICPF
     * @param atributDocumento Nome do atributo do documento a ser comparado. Este valor será utilizado para indicar na mensagem de erro qual o campo estava sendo
     *        validado
     * @param valorDocumento Valor extraido do campo do documento para ser comparado com o valor originado no SICPF
     */
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
    private void validaCampoNomeContribuinte(boolean comparacaoExata, String nomeContribuinte, String atributDocumento, String valorDocumento) {
        String mensagem = MessageFormat.format("Atributo {0} não validado com o campo 'Nome Contribuinte' do SICPF. Valor do atributo: {1}", atributDocumento, valorDocumento);

        // A expressão "\\s+" identifica espaços, quebras de linha e tabulações simples e múltiplas.
        String valorDocumentoAjustado = valorDocumento.toLowerCase().replaceAll("\\s+", " ");
        String nomeContribuinteAjustado = nomeContribuinte.toLowerCase().replaceAll("\\s+", " ");
        if (comparacaoExata) {
            if (!valorDocumentoAjustado.equalsIgnoreCase(nomeContribuinteAjustado)) {
                throw new SimtrRequisicaoException(mensagem);
            }
        } else {
            if (!valorDocumentoAjustado.contains(nomeContribuinteAjustado)) {
                throw new SimtrRequisicaoException(mensagem);
            }
        }
    }

    /**
     * Método utilizado para realizar a validação de um atributo do documento com o vaor do campo "NOME MAE" oriundo da consulta ao SICPF
     *
     * @param comparacaoExata Indicador se a comparação deve ser realizada baseada no valor exato ou se o campo contem o nome do contribuinte na informação.
     * @param nomeMae Nome da Mãe do contribuinte obtido junto ao SICPF
     * @param atributDocumento Nome do atributo do documento a ser comparado. Este valor será utilizado para indicar na mensagem de erro qual o campo estava sendo
     *        validado
     * @param valorDocumento Valor extraido do campo do documento para ser comparado com o valor originado no SICPF
     */
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
    private void validaCampoNomeMae(boolean comparacaoExata, String nomeMae, String atributDocumento, String valorDocumento) {
        String mensagem = MessageFormat.format("Atributo {0} não validado com o campo 'Nome da Mãe' do SICPF. Valor do atributo: {1}", atributDocumento, valorDocumento);

        // A expressão "\\s+" identifica espaços, quebras de linha e tabulações simples e múltiplas.
        String valorDocumentoAjustado = valorDocumento.toLowerCase().replaceAll("\\s+", " ");
        String nomeMaeAjustado = nomeMae.toLowerCase().replaceAll("\\s+", " ");
        if (comparacaoExata) {
            if (!valorDocumentoAjustado.equalsIgnoreCase(nomeMaeAjustado)) {
                throw new SimtrRequisicaoException(mensagem);
            }
        } else {
            if (!valorDocumentoAjustado.contains(nomeMaeAjustado)) {
                throw new SimtrRequisicaoException(mensagem);
            }
        }
    }

    /**
     * Método utilizado para realizar a validação de um atributo do documento com o vaor do campo "DATA NASCIMENTO" oriundo da consulta ao SICPF
     *
     * @param dataNascimentoSICPF Data de nascimento do contribuinte obtido junto ao SICPF
     * @param atributDocumento Nome do atributo do documento a ser comparado. Este valor será utilizado para indicar na mensagem de erro qual o campo estava sendo
     *        validado
     * @param valorDocumento Valor extraido do campo do documento para ser comparado com o valor originado no SICPF
     */
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
    private void validaCampoDataNascimento(Calendar dataNascimentoSICPF, String atributoDocumento, String valorDocumento) {
        String mensagem = MessageFormat.format("Atributo {0} não validado com o campo 'Data de Nascimento' do SICPF. Valor do atributo: {1}", atributoDocumento, valorDocumento);

        Calendar dataDocumento;
        try {
            dataDocumento = calendarUtil.toCalendar(valorDocumento, true);
            if (dataDocumento.get(Calendar.DAY_OF_MONTH) != dataNascimentoSICPF.get(Calendar.DAY_OF_MONTH)
                || dataDocumento.get(Calendar.MONTH) != dataNascimentoSICPF.get(Calendar.MONTH)
                || dataDocumento.get(Calendar.YEAR) != dataNascimentoSICPF.get(Calendar.YEAR)) {
                throw new SimtrRequisicaoException(mensagem);
            }
        } catch (IllegalArgumentException | ParseException pe) {
            throw new SimtrRequisicaoException(mensagem, pe);
        }
    }

    /**
     * Método utilizado para realizar a validação de um atributo do documento com o vaor do campo "TITULO DE ELEITOR" oriundo da consulta ao SICPF
     *
     * @param tituloEleitorSICPF Número do titulo de eleitor do contribuinte obtido junto ao SICPF
     * @param atributDocumento Nome do atributo do documento a ser comparado. Este valor será utilizado para indicar na mensagem de erro qual o campo estava sendo
     *        validado
     * @param valorDocumento Valor extraido do campo do documento para ser comparado com o valor originado no SICPF
     */
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
    private void validaCampoTituloEleitor(Long tituloEleitorSICPF, String atributoDocumento, String valorDocumento) {
        String mensagemValidacao = MessageFormat.format("Atributo {0} não validado com o campo 'Titulo de Eleitor' do SICPF. Valor do atributo: {1}", atributoDocumento, valorDocumento);
        String mensagemFormato = MessageFormat.format("Atributo {0} não possui valor valido para comparação com o numero do TITULO DE ELEITOR. Valor do atributo: {1}", atributoDocumento, valorDocumento);

        // Retira qualquer caracter não numerico
        String patternTitulo = "[^0-9]";
        String valorTitulo = valorDocumento.replaceAll(patternTitulo, "");

        try {
            if (!Long.valueOf(valorTitulo).equals(tituloEleitorSICPF)) {
                throw new SimtrRequisicaoException(mensagemValidacao);
            }
        } catch (NumberFormatException nfe) {
            throw new SimtrRequisicaoException(mensagemFormato, nfe);
        }
    }

    /**
     * Método utilizado para realizar a validação de um atributo do documento com o vaor do campo "CPF" utilizado como base para realizar a consulta ao SICPF
     *
     * @param cpf Número do titulo de eleitor do contribuinte obtido junto ao SICPF
     * @param atributDocumento Nome do atributo do documento a ser comparado. Este valor será utilizado para indicar na mensagem de erro qual o campo estava sendo
     *        validado
     * @param valorDocumento Valor extraido do campo do documento para ser comparado com o valor originado no SICPF
     */
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
    private void validaCampoCPF(Long cpf, String atributoDocumento, String valorDocumento) {
        String mensagemValidacao = MessageFormat.format("Atributo {0} não validado com o 'CPF' do SICPF. Valor do atributo: {1}", atributoDocumento, valorDocumento);
        String mensagemFormato = MessageFormat.format("Atributo {0} não possui valor valido para comparação com o numero do CPF. Valor do atributo: {1}", atributoDocumento, valorDocumento);

        // Retira qualquer caracter não numerico
        String patternTitulo = "[^0-9]";
        String valorCPF = valorDocumento.replaceAll(patternTitulo, "");

        try {
            if (!Long.valueOf(valorCPF).equals(cpf)) {
                throw new SimtrRequisicaoException(mensagemValidacao);
            }
        } catch (NumberFormatException nfe) {
            throw new SimtrRequisicaoException(mensagemFormato, nfe);
        }
    }
}
