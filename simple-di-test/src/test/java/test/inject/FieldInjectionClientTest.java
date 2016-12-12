package test.inject;

import mlesiewski.simpledi.BeanRegistry;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.CoreMatchers.nullValue;

import org.testng.annotations.Test;

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
        assertThat(defaultScopeByNameActual, is(BeanInjectedByName.CALL_UUID));
        assertThat(scopedByNameActual, is(BeanInjectedByName.CALL_UUID));
        assertThat(defaultScopeByTypeActual, is(BeanInjectedByType.CALL_UUID));
        assertThat(scopedByTypeActual, is(BeanInjectedByType.CALL_UUID));
    }
}