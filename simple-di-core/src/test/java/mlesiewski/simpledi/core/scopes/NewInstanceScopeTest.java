package mlesiewski.simpledi.core.scopes;

import mlesiewski.simpledi.core.SimpleDiException;
import mlesiewski.simpledi.core.testutils.NewObjectProvider;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class NewInstanceScopeTest {

    private NewInstanceScope scope = new NewInstanceScope();

    @Test(expectedExceptions = SimpleDiException.class)
    public void scopeCanNotBeStarted() throws Exception {
        // when
        scope.start();
        // then - exception
    }

    @Test(expectedExceptions = SimpleDiException.class)
    public void scopeCanNotBeEnded() throws Exception {
        // when
        scope.end();
        // then - exception
    }

    @Test
    public void alwaysProvidesNewInstances() throws Exception {
        // given
        NewObjectProvider provider = new NewObjectProvider();
        String name = "name";
        scope.register(provider, name);

        // when
        Object bean1 = scope.getBean(name);

        // then
        assertThat(bean1, not(nullValue()));
        assertThat(provider.provideCalled, is(true));
        assertThat(provider.softDependenciesSet, is(true));

        // given
        provider.provideCalled = false;
        provider.softDependenciesSet = false;

        // when
        Object bean2 = scope.getBean(name);

        // then
        assertThat(bean2, not(nullValue()));
        assertThat(provider.provideCalled, is(true));
        assertThat(provider.softDependenciesSet, is(true));
    }
}