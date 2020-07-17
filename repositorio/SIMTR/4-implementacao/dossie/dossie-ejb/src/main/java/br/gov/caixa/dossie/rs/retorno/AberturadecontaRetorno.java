package br.gov.caixa.dossie.rs.retorno;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.caixa.dossie.rs.entity.AberturadecontaEntity;

/**
 * @author SIOGP
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AberturadecontaRetorno extends Retorno {
    @XmlElementWrapper(name = "tipos")
    @XmlElement(name = "tipo")
    private List<AberturadecontaEntity> data;

    /**
     * CONSTRUTOR PADRÃO
     */
    public AberturadecontaRetorno() {
	data = new ArrayList<AberturadecontaEntity>();
    }

    public List<AberturadecontaEntity> getData() {
	return data;
    }

    public void setData(List<AberturadecontaEntity> data) {
	this.data = data;
    }
}
