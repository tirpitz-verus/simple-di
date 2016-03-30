package test.inject;

import mlesiewski.simpleioc.BeanRegistry;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class FieldInjectionClientTest {

    @Test
    public void injectsBeansIntoFields() throws Exception {
        // given
        FieldInjectionClient injectionClient = BeanRegistry.getBean(FieldInjectionClient.class);
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
}