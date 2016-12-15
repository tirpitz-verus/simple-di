package mlesiewski.simpledi.core.scopes;

import mlesiewski.simpledi.core.SimpleDiException;
import mlesiewski.simpledi.core.testutils.NewObjectProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;

import static mlesiewski.simpledi.core.testutils.ConcurrentTestHelper.THREAD_COUNT;
import static mlesiewski.simpledi.core.testutils.ConcurrentTestHelper.getBeanGettingThread;
import static mlesiewski.simpledi.core.testutils.ConcurrentTestHelper.getConcurrentBeanGettingThread;
import static mlesiewski.simpledi.core.testutils.ConcurrentTestHelper.getRegisteringThread;
import static mlesiewski.simpledi.core.testutils.ConcurrentTestHelper.getThreads;
import static mlesiewski.simpledi.core.testutils.ConcurrentTestHelper.runThreads;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class NewInstanceScopeTest {

    private NewInstanceScope scope;
    private CountDownLatch countDownLatch;
    private NewObjectProvider objectProvider;

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
        assertThat(provider.counter.get(), is(true));
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

    @Test(invocationCount = THREAD_COUNT)
    public void concurrentRegistersAndReadsDoNotThrowErrors() throws Exception {
        // given
        String name = "name";
        Thread t1 = getRegisteringThread(scope, objectProvider, name, countDownLatch);
        Thread t2 = getBeanGettingThread(scope, name, countDownLatch);
        // when
        runThreads(countDownLatch, t1, t2);
        // then no error
    }

    @Test
    public void concurrentReadsDoNotThrowErrors() throws Exception {
        // given
        String name = "name";
        scope.register(objectProvider, name);
        Collection<Thread> threads = getThreads(THREAD_COUNT, () -> getConcurrentBeanGettingThread(scope, name, countDownLatch));
        // when
        runThreads(countDownLatch, threads);
        // then
        assertThat(objectProvider.counter.get(), is(THREAD_COUNT));
    }

    @BeforeMethod
    public void setUp() throws Exception {
        scope = new NewInstanceScope();
        countDownLatch = new CountDownLatch(1);
        objectProvider = new NewObjectProvider();
    }
}