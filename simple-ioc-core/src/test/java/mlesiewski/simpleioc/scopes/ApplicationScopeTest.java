package mlesiewski.simpleioc.scopes;

import mlesiewski.simpleioc.BeanProvider;
import mlesiewski.simpleioc.SimpleIocException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.junit.Assert.*;

public class ApplicationScopeTest {

    ApplicationScope applicationScope;
    boolean providerCalled = false;

    @Test(expectedExceptions = SimpleIocException.class)
    public void throwsExceptionOnStart() throws Exception {
        // when
        applicationScope.start();
        // then - exception
    }

    @Test(expectedExceptions = SimpleIocException.class)
    public void throwsExceptionOnEnd() throws Exception {
        // when
        applicationScope.start();
        // then - exception
    }

    @Test
    public void registerCallsProviderImmediately() throws Exception {
        // given
        BeanProvider<Object> provider = getTestProvider();
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
        assertEquals(name, bean);
    }

    @BeforeMethod
    public void setUp() throws Exception {
        applicationScope = new ApplicationScope();
    }

    private BeanProvider<Object> getTestProvider() {
        return new BeanProvider<Object>() {

            @Override
            public Object provide() {
                providerCalled = true;
                return new Object();
            }

            @Override
            public void scopeEnded() {

            }
        };
    }
}