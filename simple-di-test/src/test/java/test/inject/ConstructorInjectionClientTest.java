package test.inject;

import mlesiewski.simpledi.core.BeanRegistry;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.UUID;

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
        assertThat(defaultScopeByNameActual, is(BeanInjectedByName.CALL_UUID));
        assertThat(scopedByNameActual, is(BeanInjectedByName.CALL_UUID));
        assertThat(defaultScopeByTypeActual, is(BeanInjectedByType.CALL_UUID));
        assertThat(scopedByTypeActual, is(BeanInjectedByType.CALL_UUID));
    }

    @Test
    public void injectsBeansIntoAnnotatedConstructor() throws Exception {
        // given
        ConstructorInjectionClient2 injectionClient = BeanRegistry.getBean(ConstructorInjectionClient2.class);
        // when
        UUID actual = injectionClient.bean.call();
        // then
        assertThat(actual, is(BeanInjectedByType.CALL_UUID));
    }
}