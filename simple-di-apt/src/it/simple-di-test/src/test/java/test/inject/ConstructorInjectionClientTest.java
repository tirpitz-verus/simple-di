package test.inject;

import mlesiewski.simpledi.BeanRegistry;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class ConstructorInjectionClientTest {

    @Test
    public void injectsBeansIntoConstructor() throws Exception {
        // given
        ConstructorInjectionClient injectionClient = BeanRegistry.getBean(ConstructorInjectionClient.class);
        // when
        UUID defaultScopeByNameActual = injectionClient.defaultScopeByName.call();
        UUID defaultScopeByTypeActual = injectionClient.defaultScopeByType.call();
        UUID scopedByNameActual = injectionClient.scopedByName.call();
        UUID scopedByTypeActual = injectionClient.scopedByType.call();
        // then
        assertEquals(BeanInjectedByName.CALL_UUID, defaultScopeByNameActual);
        assertEquals(BeanInjectedByName.CALL_UUID, scopedByNameActual);
        assertEquals(BeanInjectedByType.CALL_UUID, defaultScopeByTypeActual);
        assertEquals(BeanInjectedByType.CALL_UUID, scopedByTypeActual);
    }

    @Test
    public void injectsBeansIntoAnnotatedConstructor() throws Exception {
        // given
        ConstructorInjectionClient2 injectionClient = BeanRegistry.getBean(ConstructorInjectionClient2.class);
        // when
        UUID actual = injectionClient.bean.call();
        // then
        assertEquals(BeanInjectedByType.CALL_UUID, actual);
    }
}