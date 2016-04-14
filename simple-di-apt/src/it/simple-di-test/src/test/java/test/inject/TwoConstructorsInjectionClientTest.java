package test.inject;

import mlesiewski.simpledi.BeanRegistry;
import mlesiewski.simpledi.SimpleDiException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TwoConstructorsInjectionClientTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void exceptionThrowOnTooManyConstructors() throws Exception {
        // given
        thrown.expect(SimpleDiException.class);
        thrown.expectMessage("too many contructors");
        thrown.expectMessage("only one constructor");
        // when
        TwoConstructorsInjectionClient injectionClient = BeanRegistry.getBean(TwoConstructorsInjectionClient.class);
        // then - exception
    }
}