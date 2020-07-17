package br.gov.caixa.simtr.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.Assert;
import org.junit.Test;

public class ConstantesTest {

    @Test
    public void constructorTest() throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<ConstantesUtil> constructor = ConstantesUtil.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        ConstantesUtil c = constructor.newInstance();
        Assert.assertNotNull(c);
    }
}
