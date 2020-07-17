package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.processo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.Garantia;
import br.gov.caixa.simtr.modelo.entidade.Processo;
import br.gov.caixa.simtr.modelo.entidade.RelacaoProcesso;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.entidade.TipoRelacionamento;
import br.gov.caixa.simtr.modelo.enumerator.TipoPessoaEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioProcesso;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioProcesso.XML_ROOT_ELEMENT_PROCESSO_DOSSIE)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
          value = ConstantesNegocioProcesso.API_MODEL_V1_PROCESSO_DOSSIE,
          description = "Objeto utilizado para representar os processos caraterizados como geradores de dossiê de produto.")
public class ProcessoDossieDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ProcessoDossieDTO.class.getName());

    @XmlElement(name = ConstantesNegocioProcesso.ID)
    @ApiModelProperty(name = ConstantesNegocioProcesso.ID, required = true, value = "Codigo de identificação do processo.")
    private Integer id;

    @XmlElement(name = ConstantesNegocioProcesso.NOME)
    @ApiModelProperty(name = ConstantesNegocioProcesso.NOME, required = true, value = "Nome de identificação do processo.")
    private String nome;

    @XmlElement(name = ConstantesNegocioProcesso.AVATAR)
    @ApiModelProperty(name = ConstantesNegocioProcesso.AVATAR, required = false, value = "Identificação do nome da icone que representa o processo.")
    private String avatar;

    @XmlElement(name = ConstantesNegocioProcesso.GERA_DOSSIE)
    @ApiModelProperty(name = ConstantesNegocioProcesso.GERA_DOSSIE, required = true, value = "Indica se o processo pode gerar dossiê.")
    private Boolean indicadorGeracaoDossie;
    
    @XmlElement(name = ConstantesNegocioProcesso.INDICADOR_INTERFACE)
    @ApiModelProperty(name = ConstantesNegocioProcesso.INDICADOR_INTERFACE, required = true, value = "Indica se o processo deve ser apresentado para criação na interface gráfica.")
    private boolean indicadorInterface;

    @XmlElement(name = ConstantesNegocioProcesso.TIPO_PESSOA)
    @ApiModelProperty(name = ConstantesNegocioProcesso.TIPO_PESSOA, required = true, value = "Indica o o tipo de pessoa definido para o processo. F = Fisica | J = Juridica | A = Ambos")
    private TipoPessoaEnum tipoPessoaEnum;

    @XmlElement(name = ConstantesNegocioProcesso.PRIORIZADO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.PRIORIZADO, required = false,
                      value = "Indica que o processo esta priorizado e por isso a visão de captura de dossiês para tratamento será diferenciada.")
    private Boolean priorizado;

    @XmlElement(name = ConstantesNegocioProcesso.TRATAMENTO_SELETIVO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.TRATAMENTO_SELETIVO, required = true, value = "Indica que dossiê do processo pode ser capturado para tratamento de forma específica")
    private Boolean tratamentoSeletivo;

    @XmlElement(name = ConstantesNegocioProcesso.TEMPO_TRATAMENTO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.TEMPO_TRATAMENTO, required = true, value = "Indica o tempo em minutos que operador tem no ato do tratamento do dossiêr baseado neste processo")
    private Integer tempoTratamento;

    @XmlElement(name = ConstantesNegocioProcesso.UNIDADE_AUTORIZADA)
    @ApiModelProperty(name = ConstantesNegocioProcesso.UNIDADE_AUTORIZADA, required = false, value = "Indica se a unidade do solicitante esta apta a realizar ações neste processo.")
    private Boolean unidadeAutorizada;
    // *********************************************
    @XmlElementWrapper(name = ConstantesNegocioProcesso.TIPOS_RELACIONAMENTO)
    @XmlElement(name = ConstantesNegocioProcesso.TIPO_RELACIONAMENTO)
    @JsonProperty(value = ConstantesNegocioProcesso.TIPOS_RELACIONAMENTO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.TIPOS_RELACIONAMENTO, required = true,
                      value = "Lista de objetos que representam os tipos de relacionamento permitidos para os vinculos de pesoas no processo.")
    private List<TipoRelacionamentoDTO> tiposRelacionamento;

    @XmlElementWrapper(name = ConstantesNegocioProcesso.DOCUMENTOS_VINCULO)
    @XmlElement(name = ConstantesNegocioProcesso.DOCUMENTO_VINCULO)
    @JsonProperty(value = ConstantesNegocioProcesso.DOCUMENTOS_VINCULO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.DOCUMENTOS_VINCULO, required = false, value = "Lista de documentos definidos como necessários para comprovação conforme vinculo indicado.")
    private List<DocumentoVinculoDTO> documentosVinculoDTO;

    @XmlElementWrapper(name = ConstantesNegocioProcesso.PRODUTOS_VINCULADOS)
    @XmlElement(name = ConstantesNegocioProcesso.PRODUTO_VINCULADO)
    @JsonProperty(value = ConstantesNegocioProcesso.PRODUTOS_VINCULADOS)
    @ApiModelProperty(name = ConstantesNegocioProcesso.PRODUTOS_VINCULADOS, required = false, value = "Lista de objetos que representam os produtos vinculados ao processo.")
    private List<ProdutoDTO> produtosVinculadosDTO;

    @XmlElementWrapper(name = ConstantesNegocioProcesso.GARANTIAS_VINCULADAS)
    @XmlElement(name = ConstantesNegocioProcesso.GARANTIA_VINCULADA)
    @JsonProperty(value = ConstantesNegocioProcesso.GARANTIAS_VINCULADAS)
    @ApiModelProperty(name = ConstantesNegocioProcesso.GARANTIAS_VINCULADAS, required = false,
                      value = "Lista de objetos que representam as garantias associadas indicando os documentos definidos como necessarios para comprovação da garantia ofertada.")
    private List<GarantiaDTO> garantiasDTO;

    @XmlElementWrapper(name = ConstantesNegocioProcesso.ELEMENTOS_CONTEUDO)
    @XmlElement(name = ConstantesNegocioProcesso.ELEMENTO_CONTEUDO)
    @JsonProperty(value = ConstantesNegocioProcesso.ELEMENTOS_CONTEUDO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.ELEMENTOS_CONTEUDO, required = false,
                      value = "Lista de objetos que representam os documentos necessários a serem carregados pela definição do processo e disponniveis em todo o ciclo de vida da operação.")
    private List<ElementoConteudoDTO> elementosConteudoDTO;


    @XmlElementWrapper(name = ConstantesNegocioProcesso.PROCESSOS_FILHO)
    @XmlElement(name = ConstantesNegocioProcesso.PROCESSO_FILHO)
    @JsonProperty(value = ConstantesNegocioProcesso.PROCESSOS_FILHO)
    @ApiModelProperty(name = ConstantesNegocioProcesso.PROCESSOS_FILHO, required = false, value = "Lista de processos filho vinculados neste processo.")
    private List<ProcessoFaseDTO> processosFilhoDTO;

    public ProcessoDossieDTO() {
        super();
        this.tiposRelacionamento = new ArrayList<>();
        this.documentosVinculoDTO = new ArrayList<>();
        this.produtosVinculadosDTO = new ArrayList<>();
        this.garantiasDTO = new ArrayList<>();
        this.elementosConteudoDTO = new ArrayList<>();
        this.processosFilhoDTO = new ArrayList<>();
    }
    
    
    public ProcessoDossieDTO(Processo processo, boolean priorizado, Integer lotacaoAdministrativa, Integer lotacaoFisica) {
        this(processo, lotacaoAdministrativa, lotacaoFisica);
        this.priorizado = priorizado;
    }

    public ProcessoDossieDTO(Processo processo, Integer lotacaoAdministrativa, Integer lotacaoFisica) {
        this();
        if (processo != null) {
            this.id = processo.getId();
            this.nome = processo.getNome();
            this.avatar = processo.getAvatar();
            this.indicadorGeracaoDossie = processo.getIndicadorGeracaoDossie();
            this.indicadorInterface = processo.getIndicadorInterface();
            this.tipoPessoaEnum = processo.getTipoPessoa();
            this.tratamentoSeletivo = processo.getTratamentoSeletivo();
            this.tempoTratamento = processo.getTempoTratamento();
            if (tempoTratamento == null) {
                tempoTratamento = 10;
            }
            
            if(processo.getUnidadesAutorizadas() != null){
                this.unidadeAutorizada = processo.getUnidadesAutorizadas().stream()
                            .anyMatch(ua -> ua.getUnidade().equals(lotacaoAdministrativa) || ua.getUnidade().equals(lotacaoFisica));
            } else {
                this.unidadeAutorizada = Boolean.FALSE;
            }

            try {
                if (processo.getProdutos() != null) {
                    processo.getProdutos().forEach(produto -> this.produtosVinculadosDTO.add(new ProdutoDTO(produto, processo)));
                }
            } catch (RuntimeException re) {
                // Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.produtosVinculadosDTO = null;
            }

            try {
                if (processo.getElementosConteudo() != null) {
                    processo.getElementosConteudo()
                            .forEach(elementoConteudo -> this.elementosConteudoDTO.add(new ElementoConteudoDTO(elementoConteudo)));
                }
            } catch (RuntimeException re) {
                // Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.elementosConteudoDTO = null;
            }

            try {
                if (processo.getProcessoDocumentos() != null) {
                    List<TipoRelacionamento> relacionamentos = new ArrayList<>();
                    processo.getProcessoDocumentos().forEach(processoDocumento -> {
                        if (!relacionamentos.contains(processoDocumento.getTipoRelacionamento())) {
                            relacionamentos.add(processoDocumento.getTipoRelacionamento());
                        }
                        this.documentosVinculoDTO.add(new DocumentoVinculoDTO(processoDocumento));
                    });
                    relacionamentos.forEach(tipoRelacionmento -> this.tiposRelacionamento.add(new TipoRelacionamentoDTO(tipoRelacionmento, processo)));
                }
            } catch (RuntimeException re) {
                // Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.documentosVinculoDTO = null;
            }

            if (processo.getDocumentosGarantia() != null) {
                Map<Garantia, GarantiaDTO> mapaGarantias = new HashMap<>();

                processo.getDocumentosGarantia().forEach(documentoGarantia -> {

                    Garantia garantia = null;
                    TipoDocumento tipoDocumento = null;
                    FuncaoDocumental funcaoDocumental = null;
                    try {
                        if (documentoGarantia.getGarantia() != null) {
                            garantia = documentoGarantia.getGarantia();
                        }
                    } catch (RuntimeException re) {
                        // Lazy Exception ou atributos não carregados
                        LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                        garantia = null;
                    }

                    try {
                        if (documentoGarantia.getFuncaoDocumental() != null) {
                            funcaoDocumental = documentoGarantia.getFuncaoDocumental();
                        }
                    } catch (RuntimeException re) {
                        // Lazy Exception ou atributos não carregados
                        LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                        funcaoDocumental = null;
                    }

                    try {
                        if (documentoGarantia.getTipoDocumento() != null) {
                            tipoDocumento = documentoGarantia.getTipoDocumento();
                        }
                    } catch (RuntimeException re) {
                        // Lazy Exception ou atributos não carregados
                        LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                        tipoDocumento = null;
                    }

                    // Verifica se já existe um objeto mapeado tendo a garantia associada ao registro em analise como chave
                    // Caso não localize, cria um novo registro representando a garantia
                    // Caso localize, adiciona o tipo de documento ou função documental ao registro a ser enviado no retorno
                    GarantiaDTO garantiaDTO = mapaGarantias.get(documentoGarantia.getGarantia());
                    if (garantiaDTO == null) {
                        garantiaDTO = new GarantiaDTO(garantia, tipoDocumento, funcaoDocumental, processo);
                    } else {
                        if (tipoDocumento != null) {
                            garantiaDTO.addTipoDocumentoDTO(new TipoDocumentoDTO(tipoDocumento));
                        } else if (funcaoDocumental != null) {
                            garantiaDTO.addFuncaoDocumentalDTO(new FuncaoDocumentalDTO(funcaoDocumental));
                        }
                    }
                    mapaGarantias.put(garantia, garantiaDTO);

                });

                // Adiciona a lista de registros montados representando as garantias com as coleções de documentos
                this.garantiasDTO.addAll(mapaGarantias.values());
            }

            try{
                if (processo.getRelacoesProcessoVinculoPai() != null) {
                    List<RelacaoProcesso> relacoes = Arrays.asList(processo.getRelacoesProcessoVinculoPai().toArray(new RelacaoProcesso[processo.getRelacoesProcessoVinculoPai().size()]));
                    relacoes.sort(Comparator.nullsFirst(Comparator.comparing(RelacaoProcesso::getOrdem, Comparator.nullsFirst(Comparator.naturalOrder()))));
                    relacoes.forEach(relacaoProcesso -> {
                        this.priorizado = relacaoProcesso.getPrioridade() != null;
                        this.processosFilhoDTO.add(new ProcessoFaseDTO(relacaoProcesso.getProcessoFilho(), relacaoProcesso.getOrdem(), processo));
                    });
                }
            } catch (RuntimeException re) {
                //Lazy Exception ou atributos não carregados
                LOGGER.log(Level.ALL, re.getLocalizedMessage(), re);
                this.processosFilhoDTO = null;
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getIndicadorGeracaoDossie() {
        return indicadorGeracaoDossie;
    }

    public void setIndicadorGeracaoDossie(Boolean indicadorGeracaoDossie) {
        this.indicadorGeracaoDossie = indicadorGeracaoDossie;
    }

    public boolean isIndicadorInterface() {
        return indicadorInterface;
    }

    public void setIndicadorInterface(boolean indicadorInterface) {
        this.indicadorInterface = indicadorInterface;
    }
    
    public TipoPessoaEnum getTipoPessoaEnum() {
        return tipoPessoaEnum;
    }

    public void setTipoPessoaEnum(TipoPessoaEnum tipoPessoaEnum) {
        this.tipoPessoaEnum = tipoPessoaEnum;
    }

    public Boolean getPriorizado() {
        return priorizado;
    }

    public void setPriorizado(Boolean priorizado) {
        this.priorizado = priorizado;
    }

    public Boolean getTratamentoSeletivo() {
        return tratamentoSeletivo;
    }

    public void setTratamentoSeletivo(Boolean tratamentoSeletivo) {
        this.tratamentoSeletivo = tratamentoSeletivo;
    }

    public Integer getTempoTratamento() {
        return tempoTratamento;
    }

    public void setTempoTratamento(Integer tempoTratamento) {
        this.tempoTratamento = tempoTratamento;
    }

    public Boolean getUnidadeAutorizada() {
        return unidadeAutorizada;
    }

    public void setUnidadeAutorizada(Boolean unidadeAutorizada) {
        this.unidadeAutorizada = unidadeAutorizada;
    }
    
    public List<TipoRelacionamentoDTO> getTiposRelacionamento() {
        return tiposRelacionamento;
    }

    public void setTiposRelacionamento(List<TipoRelacionamentoDTO> tiposRelacionamento) {
        this.tiposRelacionamento = tiposRelacionamento;
    }

    public List<DocumentoVinculoDTO> getDocumentosVinculoDTO() {
        return documentosVinculoDTO;
    }

    public void setDocumentosVinculoDTO(List<DocumentoVinculoDTO> documentosVinculoDTO) {
        this.documentosVinculoDTO = documentosVinculoDTO;
    }

    public List<ProdutoDTO> getProdutosVinculadosDTO() {
        return produtosVinculadosDTO;
    }

    public void setProdutosVinculadosDTO(List<ProdutoDTO> produtosVinculadosDTO) {
        this.produtosVinculadosDTO = produtosVinculadosDTO;
    }

    public List<GarantiaDTO> getGarantiasDTO() {
        return garantiasDTO;
    }

    public void setGarantiasDTO(List<GarantiaDTO> garantiasDTO) {
        this.garantiasDTO = garantiasDTO;
    }

    public List<ElementoConteudoDTO> getElementosConteudoDTO() {
        return elementosConteudoDTO;
    }

    public void setElementosConteudoDTO(List<ElementoConteudoDTO> elementosConteudoDTO) {
        this.elementosConteudoDTO = elementosConteudoDTO;
    }

    public List<ProcessoFaseDTO> getProcessosFilhoDTO() {
        return processosFilhoDTO;
    }

    public void setProcessosFilhoDTO(List<ProcessoFaseDTO> processosFilhoDTO) {
        this.processosFilhoDTO = processosFilhoDTO;
    }

}
