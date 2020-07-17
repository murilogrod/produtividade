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

import java.util.Calendar;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.simtr.modelo.entidade.AutorizacaoNegada;
import br.gov.caixa.simtr.modelo.entidade.Produto;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class AutorizacaoNegadaServico extends AbstractService<AutorizacaoNegada, Long> {

    private final String MOTIVO_NAO_IDENTIFICADO = "Motivo não identificado.";
    private final String PRODUTO_NAO_LOCALIZADO = "Produto não localizado";

    @Inject
    private EntityManager entityManager;

    @EJB
    private ProdutoServico produtoServico;

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE
    })
    public void createAutorizacaoNegada(final Long cpfCnpj, final TipoPessoaEnum tipoPessoaEnum, final Integer operacaoSolicitada, final Integer modalidadeSolicitada, final String sistemaSolicitante, final String motivo) {
        AutorizacaoNegada autorizacaoNegada = new AutorizacaoNegada();
        autorizacaoNegada.setCpfCnpj(cpfCnpj);
        autorizacaoNegada.setSiglaCanalSolicitacao(sistemaSolicitante);
        autorizacaoNegada.setMotivo(motivo == null ? MOTIVO_NAO_IDENTIFICADO : motivo);
        autorizacaoNegada.setTipoPessoa(tipoPessoaEnum);
        autorizacaoNegada.setProdutoOperacao(operacaoSolicitada);
        autorizacaoNegada.setProdutoModalidade(modalidadeSolicitada);
        final Produto produto = this.produtoServico.getByOperacaoModalidade(operacaoSolicitada, modalidadeSolicitada, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        autorizacaoNegada.setProdutoNome(produto == null ? PRODUTO_NAO_LOCALIZADO : produto.getNome());
        autorizacaoNegada.setDataHoraRegistro(Calendar.getInstance());
        this.save(autorizacaoNegada);
    }
}
