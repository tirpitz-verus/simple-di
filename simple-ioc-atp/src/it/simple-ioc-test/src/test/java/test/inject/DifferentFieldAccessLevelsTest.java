package test.inject;

import mlesiewski.simpleioc.BeanRegistry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DifferentFieldAccessLevelsTest {

    @Test
    public void injectsBeansIntoPrivateField() throws Exception {
        // when
        PrivateFieldAccessLevelsClient injectionClient = BeanRegistry.getBean(PrivateFieldAccessLevelsClient.class);
        // then
        assertEquals(BeanInjectedByType.CALL_UUID, injectionClient.call());
    }

    @Test
    public void injectsBeansIntoDefaultField() throws Exception {
        // when
        DefaultFieldAccessLevelsClient injectionClient = BeanRegistry.getBean(DefaultFieldAccessLevelsClient.class);
        // then
        assertEquals(BeanInjectedByType.CALL_UUID, injectionClient.call());
    }

    @Test
    public void injectsBeansIntoProtectedField() throws Exception {
        // when
        ProtectedFieldAccessLevelsClient injectionClient = BeanRegistry.getBean(ProtectedFieldAccessLevelsClient.class);
        // then
        assertEquals(BeanInjectedByType.CALL_UUID, injectionClient.call());
    }

    @Test
    public void injectsBeansIntoPublicField() throws Exception {
        // when
        PublicFieldAccessLevelsClient injectionClient = BeanRegistry.getBean(PublicFieldAccessLevelsClient.class);
        // then
        assertEquals(BeanInjectedByType.CALL_UUID, injectionClient.call());
    }
}
