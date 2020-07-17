package br.gov.caixa.simtr.controle.servico;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.ejb3.annotation.SecurityDomain;

import br.gov.caixa.pedesgo.arquitetura.servico.impl.KeycloakService;
import br.gov.caixa.simtr.controle.excecao.SimtrConfiguracaoException;
import br.gov.caixa.simtr.modelo.entidade.Canal;
import br.gov.caixa.simtr.modelo.entidade.InstanciaDocumento;
import br.gov.caixa.simtr.modelo.entidade.SituacaoDocumento;
import br.gov.caixa.simtr.modelo.entidade.SituacaoInstanciaDocumento;
import br.gov.caixa.simtr.modelo.enumerator.SituacaoDocumentoEnum;
import br.gov.caixa.simtr.util.ConstantesUtil;

@Stateless
@RolesAllowed({
    ConstantesUtil.PERFIL_MTRADM,
    ConstantesUtil.PERFIL_MTRSDNINT,
    ConstantesUtil.PERFIL_MTRSDNMTZ,
    ConstantesUtil.PERFIL_MTRSDNOPE
})
@SecurityDomain(ConstantesUtil.KEYCLOAK_SECURITY_DOMAIN)
public class SituacaoDocumentoServico extends AbstractService<SituacaoDocumento, Integer> {

    @EJB
    private CanalServico canalServico;
    
    @EJB
    private KeycloakService keycloakService;

    @Inject
    private EntityManager entityManager;

    private final Map<SituacaoDocumentoEnum, SituacaoDocumento> mapaSituacaoDocumentoEnum = new HashMap<>();
    private Calendar dataHoraUltimaAlteracao;

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * Realiza a carga das Situação de Documento configurados no banco e mantem preenchido em um map ára evitar consultas excessivas no banco de dados com
     * informações pouco alteradas
     */
    @PostConstruct
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void carregarMapas() {
        String jpqlUltimaAlteracao = " SELECT MAX(sd.dataHoraUltimaAlteracao) FROM SituacaoDocumento sd ";
        TypedQuery<Calendar> queryUltimaAlteracao = this.entityManager.createQuery(jpqlUltimaAlteracao, Calendar.class);
        Calendar ultimaAlteracao = queryUltimaAlteracao.getSingleResult();

        if (Objects.isNull(this.dataHoraUltimaAlteracao) || dataHoraUltimaAlteracao.before(ultimaAlteracao)) {

            StringBuilder jpql = new StringBuilder();
            jpql.append(" SELECT sd FROM SituacaoDocumento sd ");

            List<SituacaoDocumento> situacoes = this.entityManager.createQuery(jpql.toString(), SituacaoDocumento.class).getResultList();

            mapaSituacaoDocumentoEnum.clear();

            situacoes.forEach(situacaoDocumento -> {
                try {
                    SituacaoDocumentoEnum situacaoDocumentoEnum = SituacaoDocumentoEnum.getByDescricao(situacaoDocumento.getNome());
                    mapaSituacaoDocumentoEnum.put(situacaoDocumentoEnum, situacaoDocumento);
                } catch (IllegalArgumentException iae) {
                    String mensagem = MessageFormat.format("SDS.cM.001 - Falha ao localizar enumeração para a situação do documento no carregamento do mapa. Situação Documento = {0}", situacaoDocumento.getNome());
                    throw new SimtrConfiguracaoException(mensagem, iae);
                }
            });

            this.dataHoraUltimaAlteracao = ultimaAlteracao;
        }
    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE
    })
    @Override
    public List<SituacaoDocumento> list() {
        return new ArrayList<>(this.mapaSituacaoDocumentoEnum.values());
    }

    /**
     * Captura a situação do documento pela representação do Enumerador. Caso não seja localizado realiza uma nova carga no mapa e tenta localizar o registro
     * novamente, se a ausencia ainda persistir retorna null.
     *
     * @param situacaoDocumentoEnum Registro do enumerador que representa situação do documento a ser localizado
     * @return Tipo de situação localizado ou null caso a mesma não seja localizado
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRDOSINT,
        ConstantesUtil.PERFIL_MTRDOSMTZ,
        ConstantesUtil.PERFIL_MTRDOSOPE,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    public SituacaoDocumento getBySituacaoDocumentoEnum(SituacaoDocumentoEnum situacaoDocumentoEnum) {

        SituacaoDocumento situacaoDocumento = mapaSituacaoDocumentoEnum.get(situacaoDocumentoEnum);
        if (situacaoDocumento == null) {
            this.carregarMapas();
        } else {
            return situacaoDocumento;
        }
        return mapaSituacaoDocumentoEnum.get(situacaoDocumentoEnum);
    }

    /**
     * Registra uma nova situação do tipo indicado vinculada ao dossiê de produto informado. Caso seja encaminhado um valor na observação, a mensagem será definido
     * no registro da situação.
     *
     * @param instanciaDocumento Instancia de documento a ser associada a situação criada
     * @param situacaoDocumento Situação do documento a ser associada como situação da instancia
     */
    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO

    })
    public void registraNovaSituacaoInstanciaDocumento(InstanciaDocumento instanciaDocumento, SituacaoDocumento situacaoDocumento) {
        // Captura o canal de comunicação baseado no client ID para usar a sigla caso a matricula seja superior a 20 caracteres
        Canal canal = this.canalServico.getByClienteSSO();
        
        // Cria o registro da nova situação do dossiê de produto
        SituacaoInstanciaDocumento situacaoInstanciaDocumento = new SituacaoInstanciaDocumento();
        situacaoInstanciaDocumento.setDataHoraInclusao(Calendar.getInstance());
        situacaoInstanciaDocumento.setInstanciaDocumento(instanciaDocumento);
        String responsavel = this.keycloakService.getMatricula().length() < 20 ? this.keycloakService.getMatricula() : canal.getSigla();
        situacaoInstanciaDocumento.setResponsavel(responsavel);
        situacaoInstanciaDocumento.setSituacaoDocumento(situacaoDocumento);
        situacaoInstanciaDocumento.setUnidade(this.keycloakService.getLotacaoAdministrativa());

        // Salva o registro da nova situação criada.
        this.entityManager.persist(situacaoInstanciaDocumento);

    }

    @RolesAllowed({
        ConstantesUtil.PERFIL_MTRADM,
        ConstantesUtil.PERFIL_MTRSDNINT,
        ConstantesUtil.PERFIL_MTRSDNMTZ,
        ConstantesUtil.PERFIL_MTRSDNOPE,
        ConstantesUtil.PERFIL_MTRSDNTTG,
        ConstantesUtil.PERFIL_MTRSDNTTO
    })
    public void registraNovaSituacaoInstanciaDocumento(InstanciaDocumento instanciaDocumento, SituacaoDocumentoEnum situacaoDocumentoEnum) {
        SituacaoDocumento situacaoDocumento = this.getBySituacaoDocumentoEnum(situacaoDocumentoEnum);
        this.registraNovaSituacaoInstanciaDocumento(instanciaDocumento, situacaoDocumento);
    }

}
