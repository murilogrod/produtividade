package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.dossieproduto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.modelo.entidade.CampoEntrada;
import br.gov.caixa.simtr.modelo.entidade.CampoFormulario;
import br.gov.caixa.simtr.modelo.entidade.RespostaDossie;
import br.gov.caixa.simtr.modelo.enumerator.TipoCampoEnum;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioDossieProduto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlRootElement(name = ConstantesNegocioDossieProduto.XML_ROOT_ELEMENT_RESPOSTA_FORMULARIO)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
          value = ConstantesNegocioDossieProduto.API_MODEL_V1_RESPOSTA_FORMULARIO,
          description = "Objeto utilizado para representar uma resposta a um campo de formulario vinculado ao Processo fase no retorno as consultas realizadas sob a ótica Apoio ao Negocio.")
public class RespostaFormularioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = ConstantesNegocioDossieProduto.ID_CAMPO_FORMULARIO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ID_CAMPO_FORMULARIO, required = true,
                      value = "Identificador unico do campo que representa a entrada de um formulario do processo")
    private Long idCampoFormulario;

    @XmlElement(name = ConstantesNegocioDossieProduto.NOME_CAMPO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.NOME_CAMPO, required = true, value = "Nome do campo para fins de identificação programatica perante o formulario.")
    private String nome;

    @XmlElement(name = ConstantesNegocioDossieProduto.LABEL_CAMPO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.LABEL_CAMPO, required = true, value = "Label da campo a ser apresentado na interface para o usuário.")
    private String label;

    @XmlElement(name = ConstantesNegocioDossieProduto.TIPO_CAMPO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.TIPO_CAMPO, required = true,
                      value = "Indica o tipo de componente a ser utilizado na montagem do formulario para apresentação do campo.Ex: INPUT, RADIO, CHECK, etc")
    private TipoCampoEnum tipoCampo;

    @XmlElement(name = ConstantesNegocioDossieProduto.ID_PROCESSO_FASE)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ID_PROCESSO_FASE, required = false, value = "Representação do processo fase em que a resposta foi concedida.")
    private Integer identificadorProcessoFase;

    @XmlElement(name = ConstantesNegocioDossieProduto.ID_VINCULO_PESSOA)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ID_VINCULO_PESSOA, required = true, value = "Identificador do vinculo de pessoa associado a resposta concedida.")
    private Long identificadorVinculoPessoa;

    @XmlElement(name = ConstantesNegocioDossieProduto.ID_PRODUTO_CONTRATADO)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ID_PRODUTO_CONTRATADO, required = true, value = "Representação do vinculo de produto associado a resposta concedida.")
    private Long identificadorProdutoContratado;

    @XmlElement(name = ConstantesNegocioDossieProduto.ID_GARANTIA_INFORMADA)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.ID_GARANTIA_INFORMADA, required = true, value = "Representação do vinculo de garantia associado a resposta concedida.")
    private Long identificadorGarantiaInformada;

    @XmlElement(name = ConstantesNegocioDossieProduto.RESPOSTA_ABERTA)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.RESPOSTA_ABERTA, required = false,
                      value = "Contem a resposta concedida pelo usuário ao responder o campo do formulário.")
    private String respostaAberta;

    @XmlElement(name = ConstantesNegocioDossieProduto.OPCAO_SELECIONADA)
    @XmlElementWrapper(name = ConstantesNegocioDossieProduto.OPCOES_SELECIONADAS)
    @JsonProperty(value = ConstantesNegocioDossieProduto.OPCOES_SELECIONADAS)
    @ApiModelProperty(name = ConstantesNegocioDossieProduto.OPCOES_SELECIONADAS, required = false,
                      value = "Contem a lista de respostas objetivas  concedidas pelo usuário ao responder o campo do formulário.")
    private List<OpcaoCampoDTO> opcoesCampoSelecionadasDTO;

    public RespostaFormularioDTO() {
        super();
        this.opcoesCampoSelecionadasDTO = new ArrayList<>();
    }

    public RespostaFormularioDTO(RespostaDossie respostaDossie) {
        this();
        if (respostaDossie != null) {
            this.respostaAberta = respostaDossie.getRespostaAberta();
            if (respostaDossie.getOpcoesCampo() != null) {
                respostaDossie.getOpcoesCampo().forEach(opcaoCampo -> this.opcoesCampoSelecionadasDTO.add(new OpcaoCampoDTO(opcaoCampo)));
            }
            if (respostaDossie.getCampoFormulario() != null) {
                CampoFormulario campoFormulario = respostaDossie.getCampoFormulario();
                this.idCampoFormulario = campoFormulario.getId();
                this.nome = campoFormulario.getNomeCampo();
                if (campoFormulario.getCampoEntrada() != null) {
                    CampoEntrada campoEntrada = campoFormulario.getCampoEntrada();
                    this.label = campoEntrada.getLabel();
                    this.tipoCampo = campoEntrada.getTipo();
                }
            }
            if (respostaDossie.getProcessoFase() != null) {
                this.identificadorProcessoFase = respostaDossie.getProcessoFase().getId();
            }

            if (respostaDossie.getDossieClienteProduto() != null) {
                this.identificadorVinculoPessoa = respostaDossie.getDossieClienteProduto().getId();
            }

            if (respostaDossie.getProdutoDossie() != null) {
                this.identificadorProdutoContratado = respostaDossie.getProdutoDossie().getId();
            }

            if (respostaDossie.getGarantiaInformada() != null) {
                this.identificadorGarantiaInformada = respostaDossie.getGarantiaInformada().getId();
            }
        }
    }

    public Long getIdCampoFormulario() {
        return idCampoFormulario;
    }

    public void setIdCampoFormulario(Long idCampoFormulario) {
        this.idCampoFormulario = idCampoFormulario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public TipoCampoEnum getTipoCampo() {
        return tipoCampo;
    }

    public void setTipoCampo(TipoCampoEnum tipoCampo) {
        this.tipoCampo = tipoCampo;
    }

    public Integer getIdentificadorProcessoFase() {
        return identificadorProcessoFase;
    }

    public void setIdentificadorProcessoFase(Integer identificadorProcessoFase) {
        this.identificadorProcessoFase = identificadorProcessoFase;
    }

    public Long getIdentificadorVinculoPessoa() {
        return identificadorVinculoPessoa;
    }

    public void setIdentificadorVinculoPessoa(Long identificadorVinculoPessoa) {
        this.identificadorVinculoPessoa = identificadorVinculoPessoa;
    }

    public Long getIdentificadorProdutoContratado() {
        return identificadorProdutoContratado;
    }

    public void setIdentificadorProdutoContratado(Long identificadorProdutoContratado) {
        this.identificadorProdutoContratado = identificadorProdutoContratado;
    }

    public Long getIdentificadorGarantiaInformada() {
        return identificadorGarantiaInformada;
    }

    public void setIdentificadorGarantiaInformada(Long identificadorGarantiaInformada) {
        this.identificadorGarantiaInformada = identificadorGarantiaInformada;
    }

    public String getRespostaAberta() {
        return respostaAberta;
    }

    public void setRespostaAberta(String respostaAberta) {
        this.respostaAberta = respostaAberta;
    }

    public List<OpcaoCampoDTO> getOpcoesCampoSelecionadasDTO() {
        return opcoesCampoSelecionadasDTO;
    }

    public void setOpcoesCampoSelecionadasDTO(List<OpcaoCampoDTO> opcoesCampoSelecionadasDTO) {
        this.opcoesCampoSelecionadasDTO = opcoesCampoSelecionadasDTO;
    }

}
