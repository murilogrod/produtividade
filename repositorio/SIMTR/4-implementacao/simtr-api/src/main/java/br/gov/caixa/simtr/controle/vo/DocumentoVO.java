package br.gov.caixa.simtr.controle.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.caixa.simtr.modelo.entidade.Documento;
import br.gov.caixa.simtr.modelo.enumerator.OrigemDocumentoEnum;

public class DocumentoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Documento documentoSIMTR;

    private String tipoDocumento;

    private String linkGED;

    private OrigemDocumentoEnum origemDocumentoEnum;

    private List<String> imagens;

    private Map<String, String> atributos;

    public DocumentoVO() {
	super();
	this.imagens = new ArrayList<>();
	this.atributos = new HashMap<>();
    }

    public Documento getDocumentoSIMTR() {
	return documentoSIMTR;
    }

    public void setDocumentoSIMTR(Documento documentoSIMTR) {
	this.documentoSIMTR = documentoSIMTR;
    }

    public String getTipoDocumento() {
	return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
	this.tipoDocumento = tipoDocumento;
    }

    public String getLinkGED() {
	return linkGED;
    }

    public void setLinkGED(String linkGED) {
	this.linkGED = linkGED;
    }

    public OrigemDocumentoEnum getOrigemDocumentoEnum() {
	return origemDocumentoEnum;
    }

    public void setOrigemDocumentoEnum(OrigemDocumentoEnum origemDocumentoEnum) {
	this.origemDocumentoEnum = origemDocumentoEnum;
    }

    public List<String> getImagens() {
	return imagens;
    }

    public void setImagens(List<String> imagens) {
	this.imagens = imagens;
    }

    public Map<String, String> getAtributos() {
	return atributos;
    }

    public void setAtributos(Map<String, String> atributos) {
	this.atributos = atributos;
    }

}
