package mlesiewski.simpledi.scopes;

import mlesiewski.simpledi.SimpleDiException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.testng.Assert.assertFalse;

public class DefaultScopeImplTest {

    static final Object BEAN = new Object();
    DefaultScopeImpl scope;
    int timesBeanWasProvided = 0;

    @Test
    public void doesNotHaveAnUnregisteredBean() throws Exception {
        // given
        scope.start();
        // when
        boolean hasBean = scope.hasBean("not registered");
        // then
        assertThat(hasBean, is(false));
    }

    @Test
    public void hasARegisteredBeanWhenStated() throws Exception {
        // given
        String name = "registered";
        scope.register(Object::new, name);
        scope.start();
        // when
        boolean hasBean = scope.hasBean(name);
        // then
        assertThat(hasBean, is(true));
    }

    @Test(expectedExceptions = SimpleDiException.class)
    public void throwsExceptionIfGettingNotRegisteredName() throws Exception {
        // given
        String name = "not registered";
        // when
        scope.getBean(name);
        // then - exception
    }

    @Test(expectedExceptions = SimpleDiException.class)
    public void throwsExceptionIfGettingBeanAndNotStarted() throws Exception {
        // given
        String name = "registered";
        scope.register(Object::new, name);
        scope.end();
        // when
        scope.getBean(name);
        // then - exception
    }

    @Test
    public void returnsRegisteredBeanWhenStated() throws Exception {
        // given
        String name = "registered";
        scope.register(() -> BEAN, name);
        scope.start();
        // when
        Object bean = scope.getBean(name);
        // then
        assertThat(bean, is(BEAN));
    }

    @Test
    public void doesNotHaveARegisteredBeanWhenNotStated() throws Exception {
        // given
        String name = "registered";
        scope.register(Object::new, name);
        scope.end();
        // when
        boolean hasBean = scope.hasBean(name);
        // then
        assertFalse(hasBean);
    }

    @Test
    public void providerIsNotCalledIfBeanIsInCache(){
        // given
        String name = "bean";
        scope.beanCache.put(name, BEAN);
        scope.register(() -> BEAN, name);
        scope.start();
        // when
        scope.getBean(name);
        // than
        assertThat(timesBeanWasProvided, is(0));
    }

    @Test
    public void beanCachingDoesNotCauseMemoryLeaks() {
        // given
        String name = "bean";
        Object bean = new Object();
        scope.beanCache.put(name, bean);
        // when
        bean = null;
        System.gc();
        bean = scope.beanCache.get(name);
        // then
        assertThat(bean, is(not(nullValue())));
    }

    @BeforeMethod
    public void setUp() throws Exception {
        scope = new DefaultScopeImpl("default");
        timesBeanWasProvided = 0;
    }
}