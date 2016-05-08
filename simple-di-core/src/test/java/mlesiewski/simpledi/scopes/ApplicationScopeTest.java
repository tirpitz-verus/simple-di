package mlesiewski.simpledi.scopes;

import mlesiewski.simpledi.BeanProvider;
import mlesiewski.simpledi.SimpleDiException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertTrue;

public class ApplicationScopeTest {

    ApplicationScope applicationScope;
    boolean providerCalled = false;

    @Test(expectedExceptions = SimpleDiException.class)
    public void throwsExceptionOnStart() throws Exception {
        // when
        applicationScope.start();
        // then - exception
    }

    @Test(expectedExceptions = SimpleDiException.class)
    public void throwsExceptionOnEnd() throws Exception {
        // when
        applicationScope.start();
        // then - exception
    }

    @Test
    public void registerCallsProviderImmediately() throws Exception {
        // given
        BeanProvider<Object> provider = () -> {
            providerCalled = true;
            return new Object();
        };
        // when
        applicationScope.register(provider, "name");
        // then
        assertTrue(providerCalled);
    }

    @Test
    public void getBeanUsesEagerBeanCache() throws Exception {
        // given
        String name = "name";
        applicationScope.eagerBeanCache.put(name, name);
        // when
        String bean = applicationScope.getBean(name);
        // then
        assertThat(bean, is(name));
    }

    @BeforeMethod
    public void setUp() throws Exception {
        applicationScope = new ApplicationScope();
    }
}