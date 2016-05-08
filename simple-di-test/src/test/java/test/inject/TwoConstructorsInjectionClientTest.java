package test.inject;

import mlesiewski.simpledi.BeanRegistry;
import mlesiewski.simpledi.SimpleDiException;
import org.testng.annotations.Test;

public class TwoConstructorsInjectionClientTest {

    @Test(expectedExceptions = SimpleDiException.class, expectedExceptionsMessageRegExp = ".*(too many constructors|only one constructor)+.*")
    public void exceptionThrowOnTooManyConstructors() throws Exception {
        // when
        BeanRegistry.getBean(TwoConstructorsInjectionClient.class);
        // then - exception
    }
}