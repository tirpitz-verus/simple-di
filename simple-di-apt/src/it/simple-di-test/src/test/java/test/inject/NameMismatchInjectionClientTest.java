package test.inject;

import mlesiewski.simpledi.BeanRegistry;
import mlesiewski.simpledi.SimpleDiException;
import org.testng.annotations.Test;


public class NameMismatchInjectionClientTest {

    @Test(expectedExceptions = SimpleDiException.class, expectedExceptionsMessageRegExp = ".*(name mismatch|BeanInjectedByType|BeanInjectedByName|bean injected by name)+.*")
    public void exceptionThrowOnBeanNameMismatch() throws Exception {
        // when
        BeanRegistry.getBean(NameMismatchInjectionClient.class);
        // then - exception
    }
}