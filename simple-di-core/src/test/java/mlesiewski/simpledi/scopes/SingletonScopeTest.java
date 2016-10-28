package mlesiewski.simpledi.scopes;


import mlesiewski.simpledi.SimpleDiException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class SingletonScopeTest {

    private SingletonScope singletonScope;

    @Test(expectedExceptions = SimpleDiException.class)
    public void throwsExceptionOnEnd() throws Exception {
        // when
        singletonScope.end();
        // then - exception
    }

    @Test
    public void beansAreNotInstantiatedUntilNeeded() throws Exception {
        // given
        ObjectProvider objectProvider = new ObjectProvider();
        String name = "name";
        // when
        singletonScope.register(objectProvider, name);
        // then
        assertThat(objectProvider.provideCalled, is(false));
        assertThat(objectProvider.softDependenciesSet, is(false));
        // when
        Object bean = singletonScope.getBean(name);
        // then
        assertThat(bean, is(not(nullValue())));
        assertThat(objectProvider.provideCalled, is(true));
        assertThat(objectProvider.softDependenciesSet, is(true));
    }

    @BeforeMethod
    public void setUp() throws Exception {
        singletonScope = new SingletonScope();
    }
}