package mlesiewski.simpledi.core.testutils;

import mlesiewski.simpledi.core.BeanProvider;

import java.util.concurrent.atomic.AtomicInteger;


public class NewObjectProvider implements BeanProvider<Object> {

    public static final NewObjectProvider NEW_OBJECT_PROVIDER = new NewObjectProvider();

    public boolean provideCalled;
    public AtomicInteger counter = new AtomicInteger();
    public boolean softDependenciesSet;

    @Override
    public Object provide() {
        provideCalled = true;
        counter.incrementAndGet();
        return new Object();
    }

    @Override
    public void setSoftDependencies(Object newInstance) {
        softDependenciesSet = true;
    }
}