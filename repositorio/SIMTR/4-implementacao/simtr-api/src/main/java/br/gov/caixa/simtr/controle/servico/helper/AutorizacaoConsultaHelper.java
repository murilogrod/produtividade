package br.gov.caixa.simtr.controle.servico.helper;

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

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.servico.sipes.SipesService;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SipesRequestDTO;
import br.gov.caixa.pedesgo.arquitetura.servico.sipes.dto.SipesResponseDTO;
import br.gov.caixa.pedesgo.arquitetura.siiso.dto.RetornoPessoasFisicasDTO;
import br.gov.caixa.simtr.controle.excecao.DossieAutorizacaoException;
import br.gov.caixa.simtr.controle.excecao.SicpfException;
import br.gov.caixa.simtr.controle.excecao.SipesException;
import br.gov.caixa.simtr.controle.servico.CadastroReceitaServico;
import br.gov.caixa.simtr.controle.servico.ComportamentoPesquisaServico;
import br.gov.caixa.simtr.controle.vo.autorizacao.MensagemOrientacaoVO;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.ComportamentoPesquisa;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.enumerator.SistemaPesquisaEnum;
import br.gov.caixa.simtr.modelo.enumerator.SistemaPesquisaTipoRetornoEnum;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
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
public class AutorizacaoConsultaHelper {

    @EJB
    private CadastroReceitaServico cadastroReceitaServico;

    @Inject
    private ComportamentoPesquisaServico comportamentoPesquisaServico;

    @EJB
    private SipesService sipesService;

        /**
     * Realiza a consulta junto ao SICPF para os sub sistemas definidos no cadastro do produto. As mensagens de orientação por ventura identificadas também são assoadas ao objeto de mensagens
     * vinculado a Thread em execução para captura posterior no prosseguimento do fluxo.
     *
     * @param cpf Numero do CPF/CNPJ utilizado na solicitação de autorização
     * @param produtoSolicitado Produto identificado na solicitação da autorização
     * @param canal Canal de comunicação identificado pelo codigo de integração na solicitação de autorização
     * @return O resultado da pesquisa do SIPES para os sistemas parametrizados no cadastro do produto
     * @throws SicpfException Lançada em caso de falha na comunicação com o cadastro da receita, ou obtenção de retorno null
     * @throws DossieAutorizacaoException Lançada quando a analise de algum comportamento configurado para o produto impedir o fornecimento da autorização identificando as causas do impedimento
     *         apresentado
     */
    public RetornoPessoasFisicasDTO realizaConsultaReceitaFederal(Long cpf, Produto produtoSolicitado, Canal canal, ThreadLocal<List<MensagemOrientacaoVO>> orientacoes) {
        RetornoPessoasFisicasDTO retornoPessoasFisicasDTO;

        // ***** Passo 2.2.1 - Realiza Consulta
        retornoPessoasFisicasDTO = this.cadastroReceitaServico.consultaCadastroPF(cpf);
        if (retornoPessoasFisicasDTO == null) {
            throw new SicpfException("AS.rCS.01 - CPF não localizado no cadastro da Receita Federal.", Boolean.TRUE);
        }
        if (produtoSolicitado.getPesquisaReceita()) {
            Integer codigoRetorno = Integer.valueOf(retornoPessoasFisicasDTO.getCodigoSituacaoCpf());

            // ***** Passo 2.2.2 - Avalia Resultado
            ComportamentoPesquisa comportamentoPesquisaSICPF = this.comportamentoPesquisaServico.getBySistemaAndCodigoTipoPesquisa(produtoSolicitado, SistemaPesquisaEnum.SICPF, codigoRetorno, null);
            if (comportamentoPesquisaSICPF != null) {
                orientacoes.get().add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICPF, null, comportamentoPesquisaSICPF.getOrientacao()));
                if (comportamentoPesquisaSICPF.getBloqueio()) {
                    DossieAutorizacaoException dossieAutorizacaoException = new DossieAutorizacaoException("Impedimento por pendência junto a Receita Federal.", null, null, cpf, TipoPessoaEnum.F, produtoSolicitado, canal.getSigla(), Boolean.FALSE);
                    dossieAutorizacaoException.addMensagemOrientacao(SistemaPesquisaEnum.SICPF, null, comportamentoPesquisaSICPF.getOrientacao());
                    throw dossieAutorizacaoException;
                }
            }
        }

        return retornoPessoasFisicasDTO;
    }

    /**
     *
     * Metodo utilizado ara realizar a consulta junto ao SIPES para os sub sistemas definidos no cadastro do produto. As mensagens de orientação por ventura identificadas também são assoadas ao objeto
     * de mensagens vinculado a Thread em execução para captura posterior no prosseguimento do fluxo.
     *
     * @param cpfCnpj Numero do CPF/CNPJ utilizado na solicitação de autorização
     * @param tipoPessoaEnum Identificação do tipo de pessoa (Fisica ou Juridica)
     * @param produtoSolicitado Produto identificado na solicitação da autorização
     * @param canal Canal de comunicação identificado pelo codigo de integração na solicitação de autorização
     * @return O resultado da pesquisa do SIPES para os sistemas parametrizados no cadastro do produto
     * @throws DossieAutorizacaoException Lançada quando a analise de algum comportamento configurado para o produto impedir o fornecimento da autorização identificando as causas do impedimento
     *         apresentado
     */
    public SipesResponseDTO realizaConsultaSIPES(Long cpfCnpj, TipoPessoaEnum tipoPessoaEnum, Produto produtoSolicitado, Canal canal, ThreadLocal<List<MensagemOrientacaoVO>> orientacoes) {
        SipesResponseDTO sipesResponseDTO = null;

        SipesRequestDTO sipesRequestDTO = new SipesRequestDTO(cpfCnpj, tipoPessoaEnum.equals(TipoPessoaEnum.F));
        List<SistemaPesquisaEnum> sistemasPesquisados = new ArrayList<>();
        if (produtoSolicitado.getPesquisaCadin()) {
            sipesRequestDTO.setCadin(Boolean.TRUE);
            sistemasPesquisados.add(SistemaPesquisaEnum.CADIN);
        }
        if (produtoSolicitado.getPesquisaCcf()) {
            sipesRequestDTO.setCcf(Boolean.TRUE);
            sistemasPesquisados.add(SistemaPesquisaEnum.SICCF);
        }
        if (produtoSolicitado.getPesquisaSinad()) {
            sipesRequestDTO.setSinad(Boolean.TRUE);
            sistemasPesquisados.add(SistemaPesquisaEnum.SINAD);
        }
        if (produtoSolicitado.getPesquisaScpc()) {
            sipesRequestDTO.setScpc(Boolean.TRUE);
            sistemasPesquisados.add(SistemaPesquisaEnum.SPC);
        }
        if (produtoSolicitado.getPesquisaSerasa()) {
            sipesRequestDTO.setSerasa(Boolean.TRUE);
            sistemasPesquisados.add(SistemaPesquisaEnum.SERASA);
        }
        if (produtoSolicitado.getPesquisaSicow()) {
            sipesRequestDTO.setSicow(Boolean.TRUE);
            sistemasPesquisados.add(SistemaPesquisaEnum.SICOW);
        }

        boolean bloqueiaAutorizacao = Boolean.FALSE;
        if (!sistemasPesquisados.isEmpty()) {
            List<MensagemOrientacaoVO> mensagensOrientacao = new ArrayList<>();
            // TODO Otimizar para retornar um Map<SistemaPesquisaTipoRetornoEnum, ComportamentoPesquisa> e excluir o metodo getComportamentoPesquisa()
            final Set<ComportamentoPesquisa> listaComportamentosPesquisas = produtoSolicitado.getComportamentosPesquisas();
            try {
                sipesResponseDTO = this.sipesService.callService(sipesRequestDTO);
            } catch (Exception e) {
                throw new SipesException("Falha ao comunicar com o SIPES", e, Boolean.FALSE);
            }
            ComportamentoPesquisa comportamentoPesquisa;

            // ********* Mapeamento SINAD - Inicio
            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SINAD_2_1);
            if (Objects.nonNull(sipesResponseDTO.getSINAD()) && Objects.nonNull(sipesResponseDTO.getSINAD().getOcorrenciasSiina()) && Objects.nonNull(comportamentoPesquisa)) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SINAD, SistemaPesquisaTipoRetornoEnum.SINAD_2_1.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SINAD_2_2);
            if (Objects.nonNull(sipesResponseDTO.getSINAD()) && Objects.nonNull(sipesResponseDTO.getSINAD().getOcorrenciasSinad()) && Objects.nonNull(comportamentoPesquisa)) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SINAD, SistemaPesquisaTipoRetornoEnum.SINAD_2_2.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }
            // ********* Mapeamento SINAD - Final

            // ********* Mapeamento CADIN - Inicio
            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.CADIN_3);
            if (Objects.nonNull(sipesResponseDTO.getCADIN()) && Objects.nonNull(sipesResponseDTO.getCADIN().getRetornoOcorrencias()) && Objects.nonNull(comportamentoPesquisa)) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.CADIN, SistemaPesquisaTipoRetornoEnum.CADIN_3.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }
            // ********* Mapeamento CADIN - Final

            // ********* Mapeamento SERASA - Inicio
            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SERASA_2);
            if (Objects.nonNull(sipesResponseDTO.getSERASA()) && Objects.nonNull(sipesResponseDTO.getSERASA().getPendeciasFinanceiras())
                && Objects.nonNull(comportamentoPesquisa)) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SERASA, SistemaPesquisaTipoRetornoEnum.SERASA_2.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SERASA_3);
            if (Objects.nonNull(sipesResponseDTO.getSERASA()) && Objects.nonNull(sipesResponseDTO.getSERASA().getChequesSemFundo()) && Objects.nonNull(comportamentoPesquisa)) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SERASA, SistemaPesquisaTipoRetornoEnum.SERASA_3.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SERASA_4);
            if (Objects.nonNull(sipesResponseDTO.getSERASA()) && Objects.nonNull(sipesResponseDTO.getSERASA().getProtesto()) && Objects.nonNull(comportamentoPesquisa)) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SERASA, SistemaPesquisaTipoRetornoEnum.SERASA_4.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SERASA_5);
            if (Objects.nonNull(sipesResponseDTO.getSERASA()) && Objects.nonNull(sipesResponseDTO.getSERASA().getAcaoJudicial()) && Objects.nonNull(comportamentoPesquisa)) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SERASA, SistemaPesquisaTipoRetornoEnum.SERASA_5.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SERASA_6);
            if (Objects.nonNull(sipesResponseDTO.getSERASA()) && Objects.nonNull(sipesResponseDTO.getSERASA().getParticipacaoConcordata())
                && Objects.nonNull(comportamentoPesquisa)) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SERASA, SistemaPesquisaTipoRetornoEnum.SERASA_6.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SERASA_7);
            if (Objects.nonNull(sipesResponseDTO.getSERASA()) && Objects.nonNull(sipesResponseDTO.getSERASA().getFalenciaConcordata()) && Objects.nonNull(comportamentoPesquisa)) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SERASA, SistemaPesquisaTipoRetornoEnum.SERASA_7.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SERASA_8);
            if (Objects.nonNull(sipesResponseDTO.getSERASA()) && Objects.nonNull(sipesResponseDTO.getSERASA().getConveniosDevedores()) && Objects.nonNull(comportamentoPesquisa)) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SERASA, SistemaPesquisaTipoRetornoEnum.SERASA_8.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }
            // ********* Mapeamento SERASA - Final

            // ********* Mapeamento SICCF - Inicio
            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICCF_2);
            if (Objects.nonNull(sipesResponseDTO.getSICCF()) && Objects.nonNull(sipesResponseDTO.getSICCF().getRetornoOcorrencias()) && Objects.nonNull(comportamentoPesquisa)) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICCF, SistemaPesquisaTipoRetornoEnum.SICCF_2.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }
            // ********* Mapeamento SICCF - Final

            // ********* Mapeamento SPC - Inicio
            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SPC_2T);
            if (Objects.nonNull(sipesResponseDTO.getSPC()) && Objects.nonNull(sipesResponseDTO.getSPC().getRetornoOcorrencias()) && Objects.nonNull(comportamentoPesquisa)) {
                final String codigoEspecifico = comportamentoPesquisa.getValorCodigoRetorno().getCodigoEspecifico().toString();
                if (sipesResponseDTO.getSPC().getRetornoOcorrencias().stream().anyMatch(ro -> ro.getTipoDevedor().equals(codigoEspecifico))) {
                    mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SPC, SistemaPesquisaTipoRetornoEnum.SPC_2T.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                    bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
                }
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SPC_2A);
            if (Objects.nonNull(sipesResponseDTO.getSPC()) && Objects.nonNull(sipesResponseDTO.getSPC().getRetornoOcorrencias()) && Objects.nonNull(comportamentoPesquisa)) {
                final String codigoEspecifico = comportamentoPesquisa.getValorCodigoRetorno().getCodigoEspecifico().toString();
                if (sipesResponseDTO.getSPC().getRetornoOcorrencias().stream().anyMatch(ro -> ro.getTipoDevedor().equals(codigoEspecifico))) {
                    mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SPC, SistemaPesquisaTipoRetornoEnum.SPC_2A.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                    bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
                }
            }
            // ********* Mapeamento SPC - Final

            // ********* Mapeamento SICOW - Inicio
            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICOW_1P);
            if (Objects.nonNull(sipesResponseDTO.getSICOW()) && Objects.nonNull(sipesResponseDTO.getSICOW().getPpePrimario()) && Objects.nonNull(comportamentoPesquisa)) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICOW, SistemaPesquisaTipoRetornoEnum.SICOW_1P.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICOW_1R);
            if (Objects.nonNull(sipesResponseDTO.getSICOW()) && Objects.nonNull(sipesResponseDTO.getSICOW().getPpeRelacionados()) && Objects.nonNull(comportamentoPesquisa)) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICOW, SistemaPesquisaTipoRetornoEnum.SICOW_1R.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICOW_2);
            if (Objects.nonNull(sipesResponseDTO.getSICOW()) && Objects.nonNull(sipesResponseDTO.getSICOW().getConres()) && Objects.nonNull(comportamentoPesquisa)) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICOW, SistemaPesquisaTipoRetornoEnum.SICOW_2.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICOW_3);
            if (Objects.nonNull(sipesResponseDTO.getSICOW()) && Objects.nonNull(sipesResponseDTO.getSICOW().getProibidoContratarSetorPublico())
                && Objects.nonNull(comportamentoPesquisa)) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICOW, SistemaPesquisaTipoRetornoEnum.SICOW_3.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICOW_4);
            if (Objects.nonNull(sipesResponseDTO.getSICOW()) && Objects.nonNull(sipesResponseDTO.getSICOW().getInterdicaoJudicial()) && Objects.nonNull(comportamentoPesquisa)) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICOW, SistemaPesquisaTipoRetornoEnum.SICOW_4.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICOW_5_309);
            if (Objects.nonNull(sipesResponseDTO.getSICOW()) && Objects.nonNull(sipesResponseDTO.getSICOW().getInformacoesSeguranca()) && Objects.nonNull(comportamentoPesquisa)) {
                final Long codigoEspecifico = (Long) comportamentoPesquisa.getValorCodigoRetorno().getCodigoEspecifico();
                if (sipesResponseDTO.getSICOW().getInformacoesSeguranca().stream().anyMatch(is -> is.getRestricao().equals(codigoEspecifico))) {
                    mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICOW, SistemaPesquisaTipoRetornoEnum.SICOW_5_309.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                    bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
                }
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICOW_5_310);
            if (Objects.nonNull(sipesResponseDTO.getSICOW()) && Objects.nonNull(sipesResponseDTO.getSICOW().getInformacoesSeguranca()) && Objects.nonNull(comportamentoPesquisa)) {
                final Long codigoEspecifico = (Long) comportamentoPesquisa.getValorCodigoRetorno().getCodigoEspecifico();
                if (sipesResponseDTO.getSICOW().getInformacoesSeguranca().stream().anyMatch(is -> is.getRestricao().equals(codigoEspecifico))) {
                    mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICOW, SistemaPesquisaTipoRetornoEnum.SICOW_5_310.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                    bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
                }
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICOW_5_311);
            if (Objects.nonNull(sipesResponseDTO.getSICOW()) && Objects.nonNull(sipesResponseDTO.getSICOW().getInformacoesSeguranca()) && Objects.nonNull(comportamentoPesquisa)) {
                final Long codigoEspecifico = (Long) comportamentoPesquisa.getValorCodigoRetorno().getCodigoEspecifico();
                if (sipesResponseDTO.getSICOW().getInformacoesSeguranca().stream().anyMatch(is -> is.getRestricao().equals(codigoEspecifico))) {
                    mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICOW, SistemaPesquisaTipoRetornoEnum.SICOW_5_311.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                    bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
                }
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICOW_5_500);
            if (Objects.nonNull(sipesResponseDTO.getSICOW()) && Objects.nonNull(sipesResponseDTO.getSICOW().getInformacoesSeguranca()) && Objects.nonNull(comportamentoPesquisa)) {
                final Long codigoEspecifico = (Long) comportamentoPesquisa.getValorCodigoRetorno().getCodigoEspecifico();
                if (sipesResponseDTO.getSICOW().getInformacoesSeguranca().stream().anyMatch(is -> is.getRestricao().equals(codigoEspecifico))) {
                    mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICOW, SistemaPesquisaTipoRetornoEnum.SICOW_5_500.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                    bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
                }
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICOW_5_501);
            if (Objects.nonNull(sipesResponseDTO.getSICOW()) && Objects.nonNull(sipesResponseDTO.getSICOW().getInformacoesSeguranca()) && Objects.nonNull(comportamentoPesquisa)) {
                final Long codigoEspecifico = (Long) comportamentoPesquisa.getValorCodigoRetorno().getCodigoEspecifico();
                if (sipesResponseDTO.getSICOW().getInformacoesSeguranca().stream().anyMatch(is -> is.getRestricao().equals(codigoEspecifico))) {
                    mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICOW, SistemaPesquisaTipoRetornoEnum.SICOW_5_501.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                    bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
                }
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICOW_5_502);
            if (Objects.nonNull(sipesResponseDTO.getSICOW()) && Objects.nonNull(sipesResponseDTO.getSICOW().getInformacoesSeguranca()) && Objects.nonNull(comportamentoPesquisa)) {
                final Long codigoEspecifico = (Long) comportamentoPesquisa.getValorCodigoRetorno().getCodigoEspecifico();
                if (sipesResponseDTO.getSICOW().getInformacoesSeguranca().stream().anyMatch(is -> is.getRestricao().equals(codigoEspecifico))) {
                    mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICOW, SistemaPesquisaTipoRetornoEnum.SICOW_5_502.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                    bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
                }
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICOW_5_505);
            if (Objects.nonNull(sipesResponseDTO.getSICOW()) && Objects.nonNull(sipesResponseDTO.getSICOW().getInformacoesSeguranca()) && Objects.nonNull(comportamentoPesquisa)) {
                final Long codigoEspecifico = (Long) comportamentoPesquisa.getValorCodigoRetorno().getCodigoEspecifico();
                if (sipesResponseDTO.getSICOW().getInformacoesSeguranca().stream().anyMatch(is -> is.getRestricao().equals(codigoEspecifico))) {
                    mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICOW, SistemaPesquisaTipoRetornoEnum.SICOW_5_505.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                    bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
                }
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICOW_5_506);
            if (Objects.nonNull(sipesResponseDTO.getSICOW()) && Objects.nonNull(sipesResponseDTO.getSICOW().getInformacoesSeguranca()) && Objects.nonNull(comportamentoPesquisa)) {
                final Long codigoEspecifico = (Long) comportamentoPesquisa.getValorCodigoRetorno().getCodigoEspecifico();
                if (sipesResponseDTO.getSICOW().getInformacoesSeguranca().stream().anyMatch(is -> is.getRestricao().equals(codigoEspecifico))) {
                    mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICOW, SistemaPesquisaTipoRetornoEnum.SICOW_5_506.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                    bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
                }
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICOW_5_507);
            if (Objects.nonNull(sipesResponseDTO.getSICOW()) && Objects.nonNull(sipesResponseDTO.getSICOW().getInformacoesSeguranca()) && Objects.nonNull(comportamentoPesquisa)) {
                final Long codigoEspecifico = (Long) comportamentoPesquisa.getValorCodigoRetorno().getCodigoEspecifico();
                if (sipesResponseDTO.getSICOW().getInformacoesSeguranca().stream().anyMatch(is -> is.getRestricao().equals(codigoEspecifico))) {
                    mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICOW, SistemaPesquisaTipoRetornoEnum.SICOW_5_507.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                    bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
                }
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICOW_6);
            if (sipesResponseDTO.getSICOW() != null && sipesResponseDTO.getSICOW().getEmpregadosTrabalhoEscravo() != null && comportamentoPesquisa != null) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICOW, SistemaPesquisaTipoRetornoEnum.SICOW_6.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICOW_7);
            if (sipesResponseDTO.getSICOW() != null && sipesResponseDTO.getSICOW().getPld() != null && comportamentoPesquisa != null) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICOW, SistemaPesquisaTipoRetornoEnum.SICOW_7.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }

            comportamentoPesquisa = this.getComportamentoPesquisa(listaComportamentosPesquisas, SistemaPesquisaTipoRetornoEnum.SICOW_8);
            if (sipesResponseDTO.getSICOW() != null && sipesResponseDTO.getSICOW().getPartesRelacionadas() != null && comportamentoPesquisa != null) {
                mensagensOrientacao.add(new MensagemOrientacaoVO(SistemaPesquisaEnum.SICOW, SistemaPesquisaTipoRetornoEnum.SICOW_8.getDescricaoOcorrencia(), comportamentoPesquisa.getOrientacao()));

                bloqueiaAutorizacao = bloqueiaAutorizacao || comportamentoPesquisa.getBloqueio();
            }
            // ********* Mapeamento SICOW - Final

            // Adiciona as mensagens de orientação a lista ThreadLocal da classe
            orientacoes.get().addAll(mensagensOrientacao);

            // Caso tenha ocorrido algum bloqueio levanta exceção de forma a parar o fluxo
            if (bloqueiaAutorizacao) {
                DossieAutorizacaoException dossieAutorizacaoException = new DossieAutorizacaoException("Impedimento por pendência junto ao SIPES.", null, sipesResponseDTO, cpfCnpj, tipoPessoaEnum, produtoSolicitado, canal.getSigla(), Boolean.FALSE);
                orientacoes.get()
                           .forEach(orientacao -> dossieAutorizacaoException.addMensagemOrientacao(orientacao.getSistemaPesquisa(), orientacao.getGrupoOcorrencia(), orientacao.getMensagemOrientacao()));
                throw dossieAutorizacaoException;
            }
        }
        return sipesResponseDTO;
    }

    // ************ METODOS PRIVADOS **************//

    /**
     * Metodo utilizado para extrair um ComportamentoPesquisa de uma lista indicada baseado no tipo de retorno informado.
     *
     * @param listaComportamentosPesquisa Lista de ComportamentoPesquisa a ser percorrido
     * @param tipoRetorno Tipo de retorno a ser localizado
     * @return ComportamentoPesquisa localizado baseado no tipo de retorno indicado ou null caso não seja localizado
     */
    private ComportamentoPesquisa getComportamentoPesquisa(Set<ComportamentoPesquisa> listaComportamentosPesquisa, SistemaPesquisaTipoRetornoEnum tipoRetorno) {

        for (ComportamentoPesquisa comportamentoPesquisa : listaComportamentosPesquisa) {
            if (tipoRetorno.equals(comportamentoPesquisa.getValorCodigoRetorno())) {
                return comportamentoPesquisa;
            }
        }

        return null;
    }

}
