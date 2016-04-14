package test.inject;

import mlesiewski.simpledi.BeanRegistry;
import mlesiewski.simpledi.SimpleDiException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class NameMismatchInjectionClientTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void exceptionThrowOnBeanNameMismatch() throws Exception {
        // given
        thrown.expect(SimpleDiException.class);
        thrown.expectMessage("name mismatch");
        thrown.expectMessage("BeanInjectedByType");
        thrown.expectMessage("BeanInjectedByName");
        thrown.expectMessage("bean injected by name");
        // when
        NameMismatchInjectionClient injectionClient = BeanRegistry.getBean(NameMismatchInjectionClient.class);
        // then - exception
    }
}