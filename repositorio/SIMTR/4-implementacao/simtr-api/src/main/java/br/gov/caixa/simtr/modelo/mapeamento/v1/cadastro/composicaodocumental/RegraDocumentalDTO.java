package br.gov.caixa.simtr.modelo.mapeamento.v1.cadastro.composicaodocumental;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.simtr.controle.excecao.DossieException;
import br.gov.caixa.simtr.modelo.entidade.FuncaoDocumental;
import br.gov.caixa.simtr.modelo.entidade.RegraDocumental;
import br.gov.caixa.simtr.modelo.entidade.TipoDocumento;
import br.gov.caixa.simtr.visao.PrototypeDTO;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = "regra-documental")
@XmlAccessorType(XmlAccessType.FIELD)
public class RegraDocumentalDTO implements Serializable, PrototypeDTO<RegraDocumental>{
	
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "id_regra_documental")
	@ApiModelProperty(name = "id_regra_documental")
	private Long id;
	
	@XmlElement(name = "id_tipo_documento")
	@ApiModelProperty(name = "id_tipo_documento")
	private Integer idTipoDocumento;
	
	@XmlElement(name = "id_funcao_documental")
	@ApiModelProperty(name = "id_funcao_documental")
	private Integer idFuncaoDocumental;
	
	public RegraDocumentalDTO() {
	
	}
	
	public RegraDocumentalDTO(RegraDocumental regraDocumental) {
		this.id = regraDocumental.getId();
	    if(regraDocumental.getTipoDocumento() != null) {
			this.idTipoDocumento = regraDocumental.getTipoDocumento().getId(); 
		}
		if(regraDocumental.getFuncaoDocumental() != null) {
			this.idFuncaoDocumental =regraDocumental.getFuncaoDocumental().getId(); 
		}
	}
	
	public Long getId() {
	 return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getIdTipoDocumento() {
		return idTipoDocumento;
	}
	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}
	public Integer getIdFuncaoDocumental() {
		return idFuncaoDocumental;
	}
	public void setIdFuncaoDocumental(Integer idFuncaoDocumental) {
		this.idFuncaoDocumental = idFuncaoDocumental;
	}

	@Override
	public RegraDocumental prototype() {
		
		if((Objects.nonNull(this.idFuncaoDocumental) && Objects.nonNull(this.idTipoDocumento)) 
				|| (Objects.isNull(this.idFuncaoDocumental) && Objects.isNull(this.idTipoDocumento))) {
			throw new DossieException("Nescessário informar a função documental ou tipo de documento.", true);
		}
		
		final RegraDocumental regraDocumental = new RegraDocumental();
		regraDocumental.setId(this.id);
		
		if(Objects.nonNull(this.idFuncaoDocumental)){			
			final FuncaoDocumental funcaoDocumental = new FuncaoDocumental();
			funcaoDocumental.setId(this.idFuncaoDocumental);
			regraDocumental.setFuncaoDocumental(funcaoDocumental);
		}else {
			final TipoDocumento tipoDocumento = new TipoDocumento();
			tipoDocumento.setId(this.idTipoDocumento);
			regraDocumental.setTipoDocumento(tipoDocumento);
		}

		return regraDocumental;
	}

	 
}
