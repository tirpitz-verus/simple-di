package mlesiewski.simpledi;

import mlesiewski.simpledi.scopes.ApplicationScope;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

public class BeanRegistryTest {

    @Test
    public void canRetrieveABeanFormARegisteredProviderInDefaultScope() throws Exception {
        // given
        BeanProvider<Object> provider = testBeanProvider();
        Class<Object> name = Object.class;
        BeanRegistry.register(provider, name);
        //when
        Object bean = BeanRegistry.getBean(name);
        // then
        assertThat(bean, is(not(nullValue())));
    }

    @Test
    public void canRetrieveABeanFormARegisteredProviderInScope() throws Exception {
        // given
        BeanProvider<Object> provider = testBeanProvider();
        Class<Object> name = Object.class;
        BeanRegistry.register(provider, name);
        //when
        Object bean = BeanRegistry.getBean(name);
        // then
        assertThat(bean, is(not(nullValue())));
    }

    @Test(expectedExceptions = SimpleDiException.class)
    public void throwsExceptionIfBeanProviderRegisteredTwice() throws Exception {
        // given
        BeanProvider<Object> provider = testBeanProvider();
        Class<Object> name = Object.class;
        BeanRegistry.register(provider, name);
        //when
        BeanRegistry.register(provider, name);
        // then - error
    }

    @Test(expectedExceptions = SimpleDiException.class)
    public void throwsExceptionIfRegisteringNullBeanProvider() throws Exception {
        //when
        BeanRegistry.register(null, "nonNull");
        // then - error
    }

    @Test(expectedExceptions = SimpleDiException.class)
    public void throwsExceptionIfRegisteringUnderNullName() throws Exception {
        // given
        BeanProvider<Object> provider = testBeanProvider();
        //when
        BeanRegistry.register(provider, (String) null);
        // then - error
    }

    private BeanProvider<Object> testBeanProvider() {
        return new BeanProvider<Object>() {
            @Override
            public Object provide() {
                return new Object();
            }

            @Override
            public void scopeEnded() {
            }
        };
    }

    @BeforeMethod
    public void setUp() throws Exception {
        BeanRegistry.DELEGATE.scopes.clear();
        ApplicationScope scope = new ApplicationScope();
        BeanRegistry.DELEGATE.scopes.put(scope.getName(), scope);
    }
}