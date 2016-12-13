package test.inject;

import mlesiewski.simpledi.core.BeanRegistry;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class FieldInjectionClientTest {

    @Test
    public void injectsBeansIntoFields() {
        // given
        FieldInjectionClient injectionClient = BeanRegistry.getBean(FieldInjectionClient.class);
        // when
        UUID defaultScopeByNameActual = injectionClient.defaultScopeByName.call();
        UUID defaultScopeByTypeActual = injectionClient.defaultScopeByType.call();
        UUID scopedByNameActual = injectionClient.scopedByName.call();
        UUID scopedByTypeActual = injectionClient.scopedByType.call();
        UUID defaultScopeByTypeInSuperclassActual = injectionClient.defaultScopeByTypeInSuperclass.call();
        // then
        assertThat(defaultScopeByNameActual, is(BeanInjectedByName.CALL_UUID));
        assertThat(scopedByNameActual, is(BeanInjectedByName.CALL_UUID));
        assertThat(defaultScopeByTypeActual, is(BeanInjectedByType.CALL_UUID));
        assertThat(scopedByTypeActual, is(BeanInjectedByType.CALL_UUID));
        assertThat(defaultScopeByTypeInSuperclassActual, is(BeanInjectedByType.CALL_UUID));
    }
}