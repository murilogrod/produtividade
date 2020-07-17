package br.gov.caixa.dossie.rs.retorno;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.dossie.rs.entity.AberturadecontaEntity;

/**
 * @author f538462
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RelatorioGestorRetorno extends Retorno {
    
	@XmlElementWrapper(name = "dires")
    @XmlElement(name = "dire")
    private List<AberturadecontaEntity> listaDires;
    
	@XmlElementWrapper(name = "srs")
    @XmlElement(name = "sr")
    private List<AberturadecontaEntity> listaSrs;
	
	@XmlElementWrapper(name = "agencias")
    @XmlElement(name = "agencia")
    private List<AberturadecontaEntity> listaAgencias;
	

}
