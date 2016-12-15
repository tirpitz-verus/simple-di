package mlesiewski.simpledi.core.testutils;

import mlesiewski.simpledi.core.SimpleDiException;
import mlesiewski.simpledi.core.scopes.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

public class ConcurrentTestHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConcurrentTestHelper.class);

    public static final int THREAD_COUNT = 1_000;

    public static Thread getRegisteringThread(Scope scope, NewObjectProvider objectProvider, String beanName, CountDownLatch countDownLatch) {
        return new Thread(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                LOGGER.trace("ignoring", e);
            }
            try {
                scope.register(objectProvider, beanName);
                LOGGER.trace("registered");
            } catch (SimpleDiException e) {
                LOGGER.trace("ignoring", e);
            }
        });
    }

    public static Thread getBeanGettingThread(Scope scope, String beanName, CountDownLatch countDownLatch) {
        return new Thread(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException  e) {
                LOGGER.trace("ignoring", e);
            }
            try {
                Object bean = scope.getBean(beanName);
                LOGGER.trace("got bean " + bean);
            } catch (SimpleDiException e) {
                LOGGER.trace("ignoring", e);
            }
        });
    }

    public static Thread getConcurrentBeanGettingThread(Scope scope, String beanName, CountDownLatch countDownLatch) {
        return new Thread(() -> {
            try {
                countDownLatch.await();
            } catch (InterruptedException  e) {
                LOGGER.trace("ignoring", e);
            }
            Object bean = scope.getBean(beanName);
            LOGGER.trace("got bean " + bean);
        });
    }

    public static void runThreads(CountDownLatch countDownLatch, Collection<Thread> threads) throws InterruptedException {
        threads.forEach(Thread::start);
        countDownLatch.countDown();
        for (Thread t: threads) {
            t.join();
        }
    }

    public static void runThreads(CountDownLatch countDownLatch, Thread... threads) throws InterruptedException {
        runThreads(countDownLatch, Arrays.asList(threads));
    }

    public static Collection<Thread> getThreads(int count, Supplier<Thread> threadSupplier) {
        Collection<Thread> threads = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            threads.add(threadSupplier.get());
        }
        return threads;
    }
}
