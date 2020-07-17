/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.caixa.simtr.visao;

import br.gov.caixa.simtr.modelo.entidade.GenericEntity;

/**
 * Essa interface visa definir um contrato para que os objetos DTO que possuem uma forte associação com uma entidade do sistema
 * forma padronizada de implementar a criação dessa entidade com base em seus atributos.
 * @author c090347
 * @param <T> Entidade a ser prototipada.
 */
public interface PrototypeDTO<T extends GenericEntity> {

    public T prototype();
    
}
