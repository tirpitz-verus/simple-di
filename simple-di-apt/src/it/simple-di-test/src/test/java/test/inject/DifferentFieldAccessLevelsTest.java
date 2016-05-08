package test.inject;

import mlesiewski.simpledi.BeanRegistry;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DifferentFieldAccessLevelsTest {

    @Test
    public void injectsBeansIntoPrivateField() throws Exception {
        // when
        PrivateFieldAccessLevelsClient injectionClient = BeanRegistry.getBean(PrivateFieldAccessLevelsClient.class);
        // then
        assertThat(injectionClient.call(), is(BeanInjectedByType.CALL_UUID));
    }

    @Test
    public void injectsBeansIntoDefaultField() throws Exception {
        // when
        DefaultFieldAccessLevelsClient injectionClient = BeanRegistry.getBean(DefaultFieldAccessLevelsClient.class);
        // then
        assertThat(injectionClient.call(), is(BeanInjectedByType.CALL_UUID));
    }

    @Test
    public void injectsBeansIntoProtectedField() throws Exception {
        // when
        ProtectedFieldAccessLevelsClient injectionClient = BeanRegistry.getBean(ProtectedFieldAccessLevelsClient.class);
        // then
        assertThat(injectionClient.call(), is(BeanInjectedByType.CALL_UUID));
    }

    @Test
    public void injectsBeansIntoPublicField() throws Exception {
        // when
        PublicFieldAccessLevelsClient injectionClient = BeanRegistry.getBean(PublicFieldAccessLevelsClient.class);
        // then
        assertThat(injectionClient.call(), is(BeanInjectedByType.CALL_UUID));
    }
}
