package test.inject;

import mlesiewski.simpleioc.BeanRegistry;
import mlesiewski.simpleioc.SimpleIocException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class NameMismatchInjectionClientTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void exceptionThrowOnBeanNameMismatch() throws Exception {
        // given
        thrown.expect(SimpleIocException.class);
        thrown.expectMessage("name mismatch");
        thrown.expectMessage("BeanInjectedByType");
        thrown.expectMessage("BeanInjectedByName");
        thrown.expectMessage("bean injected by name");
        // when
        NameMismatchInjectionClient injectionClient = BeanRegistry.getBean(NameMismatchInjectionClient.class);
        // then - exception
    }
}