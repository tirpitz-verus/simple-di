package mlesiewski.simpledi.core;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static mlesiewski.simpledi.core.testutils.NewObjectProvider.NEW_OBJECT_PROVIDER;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

public class BeanRegistryTest {
    
    @Test
    public void canRetrieveABeanFormARegisteredProviderInDefaultScope() throws Exception {
        // given
        Class<Object> name = Object.class;
        BeanRegistry.register(NEW_OBJECT_PROVIDER, name);
        //when
        Object bean = BeanRegistry.getBean(name);
        // then
        assertThat(bean, is(not(nullValue())));
    }

    @Test
    public void canRetrieveABeanFormARegisteredProviderInScope() throws Exception {
        // given
        Class<Object> name = Object.class;
        BeanRegistry.register(NEW_OBJECT_PROVIDER, name);
        //when
        Object bean = BeanRegistry.getBean(name);
        // then
        assertThat(bean, is(not(nullValue())));
    }

    @Test(expectedExceptions = SimpleDiException.class)
    public void throwsExceptionIfBeanProviderRegisteredTwice() throws Exception {
        // given
        Class<Object> name = Object.class;
        BeanRegistry.register(NEW_OBJECT_PROVIDER, name);
        //when
        BeanRegistry.register(NEW_OBJECT_PROVIDER, name);
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
        //when
        BeanRegistry.register(NEW_OBJECT_PROVIDER, (String) null);
        // then - error
    }

    @BeforeMethod
    public void setUp() throws Exception {
        BeanRegistry.init();
    }
}