package br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tipologia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.gov.caixa.simtr.modelo.entidade.Dominio;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.modelo.mapeamento.adapter.CalendarFullISOAdapter;
import br.gov.caixa.simtr.modelo.mapeamento.constantes.ConstantesNegocioTipologia;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tipologia.funcao.FuncaoDocumentalDTO;
import br.gov.caixa.simtr.modelo.mapeamento.v1.negocio.tipologia.tipo.TipoDocumentoDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = ConstantesNegocioTipologia.XML_ROOT_ELEMENT_TIPOLOGIA_DOCUMENTAL)
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(
        value = ConstantesNegocioTipologia.API_MODEL_V1_TIPOLOGIA_DOCUMENTAL,
        description = "Objeto utilizado para representar a Tipologia Documental sob a ótica Apoio ao Negocio."
)
public class TipologiaDocumentalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "atualizacaoTipos")
    @ApiModelProperty(value = "Data/Hora da última atualização dos tipos de documento.", required = true)
    @XmlJavaTypeAdapter(value = CalendarFullISOAdapter.class)
    private Calendar dataHoraAtualizacaoTipos;

    @XmlElement(name = "atualizacaoFuncoes")
    @ApiModelProperty(value = "Data/Hora da última atualização das funções documentais.", required = true)
    @XmlJavaTypeAdapter(value = CalendarFullISOAdapter.class)
    private Calendar dataHoraAtualizacaoFuncoes;

    @XmlElement(name = ConstantesNegocioTipologia.FUNCOES_DOCUMENTAIS)
    @ApiModelProperty(name = ConstantesNegocioTipologia.FUNCOES_DOCUMENTAIS, value = "Lista de Funções Documetais definidas para o sistema com os tipos de documentos vinculados a ela.", required = false)
    private List<FuncaoDocumentalDTO> funcoesDocumentaisDTO;

    @XmlElement(name = ConstantesNegocioTipologia.TIPOS_DOCUMENTO)
    @ApiModelProperty(name = ConstantesNegocioTipologia.TIPOS_DOCUMENTO, value = "Lista de Tipos de Documento definidos para o sistema identificando as funções que o mesmo pode assumir.", required = false)
    private List<TipoDocumentoDTO> tiposDocumentoDTO;
    
    @XmlElement(name = ConstantesNegocioTipologia.DOMINIOS)
    @ApiModelProperty(name = ConstantesNegocioTipologia.DOMINIOS, value = "Lista de Domínios definidos para o sistema.", required = false)
    private List<DominioDTO> dominios;

    public TipologiaDocumentalDTO() {
        super();
        this.funcoesDocumentaisDTO = new ArrayList<>();
        this.tiposDocumentoDTO = new ArrayList<>();
        this.dominios = new ArrayList<>();
    }

    public TipologiaDocumentalDTO(Calendar dataHoraAtualizacaoTipos, Calendar dataHoraAtualizacaoFuncoes, List<FuncaoDocumental> funcoesDocumentais, List<TipoDocumento> tiposDocumento, List<Dominio> dominios) {
        this();
        this.dataHoraAtualizacaoTipos = dataHoraAtualizacaoTipos;
        this.dataHoraAtualizacaoFuncoes = dataHoraAtualizacaoFuncoes;
        if (funcoesDocumentais != null) {
            funcoesDocumentais.forEach(funcaoDocumento -> this.funcoesDocumentaisDTO.add(new FuncaoDocumentalDTO(funcaoDocumento)));
        }

        if (tiposDocumento != null) {
            tiposDocumento.forEach(tipoDocumento -> this.tiposDocumentoDTO.add(new TipoDocumentoDTO(tipoDocumento)));
        }
        
        if(dominios != null) {
        	dominios.forEach(dominio -> this.dominios.add(new DominioDTO(dominio)));
        }
    }

    public Calendar getDataHoraAtualizacaoTipos() {
        return dataHoraAtualizacaoTipos;
    }

    public void setDataHoraAtualizacaoTipos(Calendar dataHoraAtualizacaoTipos) {
        this.dataHoraAtualizacaoTipos = dataHoraAtualizacaoTipos;
    }

    public Calendar getDataHoraAtualizacaoFuncoes() {
        return dataHoraAtualizacaoFuncoes;
    }

    public void setDataHoraAtualizacaoFuncoes(Calendar dataHoraAtualizacaoFuncoes) {
        this.dataHoraAtualizacaoFuncoes = dataHoraAtualizacaoFuncoes;
    }

    public List<FuncaoDocumentalDTO> getFuncoesDocumentaisDTO() {
        return funcoesDocumentaisDTO;
    }

    public void setFuncoesDocumentaisDTO(List<FuncaoDocumentalDTO> funcoesDocumentaisDTO) {
        this.funcoesDocumentaisDTO = funcoesDocumentaisDTO;
    }

    public List<TipoDocumentoDTO> getTiposDocumentoDTO() {
        return tiposDocumentoDTO;
    }

    public void setTiposDocumentoDTO(List<TipoDocumentoDTO> tiposDocumentoDTO) {
        this.tiposDocumentoDTO = tiposDocumentoDTO;
    }

	public List<DominioDTO> getDominios() {
		return dominios;
	}

	public void setDominios(List<DominioDTO> dominios) {
		this.dominios = dominios;
	}
}
