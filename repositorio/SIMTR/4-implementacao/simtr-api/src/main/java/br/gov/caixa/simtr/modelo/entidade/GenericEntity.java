package br.gov.caixa.simtr.modelo.entidade;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import br.gov.caixa.pedesgo.arquitetura.entidade.Entidade;
import io.swagger.annotations.ApiModelProperty;

@MappedSuperclass
public abstract class GenericEntity extends Entidade {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Campo de controle das versões do registro para viabilizar a concorrencia otimista", required = true)
    private Integer versao = 1;
    
    @Inject
    private Logger logger;

    @Version
    @Column(name = "nu_versao")
    public Integer getVersao() {
        return versao;
    }

    public void setVersao(Integer versao) {
        this.versao = versao;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    @Transient
    public String getChavePrimaria() {
        Method[] metodos = this.getClass().getMethods();
        for (Method metodo : metodos) {
            Annotation[] anotacoes = metodo.getAnnotations();
            for (Annotation anotacao : anotacoes) {
                if (anotacao instanceof Id) {
                    return metodo.getName().substring(3).toLowerCase();
                }
            }
        }
        return null;
    }

    @Override
    @Transient
    public Serializable getIdentificador() {
        Method[] metodos = this.getClass().getMethods();
        for (Method metodo : metodos) {
            Annotation[] anotacoes = metodo.getAnnotations();
            for (Annotation anotacao : anotacoes) {
                if (anotacao instanceof Id) {
                    try {
                        return (Serializable) metodo.invoke(this, new Object[]{});
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    	this.logger.log(Level.SEVERE, e.getMessage(), e);
                        return null;
                    }
                }
            }
        }
        return null;
    }

}
