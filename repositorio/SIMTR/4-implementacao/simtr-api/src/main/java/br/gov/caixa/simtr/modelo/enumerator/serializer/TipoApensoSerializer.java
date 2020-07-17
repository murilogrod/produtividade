/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.caixa.simtr.modelo.enumerator.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import br.gov.caixa.simtr.modelo.enumerator.TipoApensoEnum;

/**
 *
 * @author c090347
 */
public class TipoApensoSerializer extends JsonSerializer<TipoApensoEnum> {

    @Override
    public void serialize(TipoApensoEnum tipoApensoEnum, JsonGenerator generator, SerializerProvider provider) throws IOException { //JsonProcessingException
        generator.writeStartObject();
        generator.writeFieldName("sigla");
        generator.writeString(tipoApensoEnum.name());
        generator.writeFieldName("descricao");
        generator.writeString(tipoApensoEnum.getDescricao());
        generator.writeEndObject();
    }

}
