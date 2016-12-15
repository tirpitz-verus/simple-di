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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class SingletonScopeTest {

    private SingletonScope singletonScope;
    private CountDownLatch countDownLatch;
    private NewObjectProvider objectProvider;

    @Test(expectedExceptions = SimpleDiException.class)
    public void throwsExceptionOnEnd() throws Exception {
        // when
        singletonScope.end();
        // then - exception
    }

    @Test
    public void beansAreNotInstantiatedUntilNeeded() throws Exception {
        // given
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

    @Test(invocationCount = THREAD_COUNT)
    public void concurrentRegistersAndReadsDoNotThrowErrors() throws Exception {
        // given
        String name = "name";
        Thread t1 = getRegisteringThread(singletonScope, objectProvider, name, countDownLatch);
        Thread t2 = getBeanGettingThread(singletonScope, name, countDownLatch);
        // when
        runThreads(countDownLatch, t1, t2);
        // then no error
    }

    @Test
    public void concurrentReadsDoNotThrowErrors() throws Exception {
        // given
        String name = "name";
        singletonScope.register(objectProvider, name);
        Collection<Thread> threads = getThreads(THREAD_COUNT, () -> getConcurrentBeanGettingThread(singletonScope, name, countDownLatch));
        // when
        runThreads(countDownLatch, threads);
        // then
        assertThat(objectProvider.counter.get(), is(1));
    }

    @BeforeMethod
    public void setUp() throws Exception {
        singletonScope = new SingletonScope();
        countDownLatch = new CountDownLatch(1);
        objectProvider = new NewObjectProvider();
    }
}