package mlesiewski.simpledi.core.testutils;

import mlesiewski.simpledi.core.BeanProvider;

import java.util.function.Supplier;

public class TestBeanProvider <T> implements BeanProvider<T> {

    private final Supplier<T> beanSupplier;

    public TestBeanProvider(Supplier<T> beanSupplier) {
        this.beanSupplier = beanSupplier;
    }

    @Override
    public T provide() {
        return beanSupplier.get();
    }

    @Override
    public void setSoftDependencies(T newInstance) {
        // empty
    }
}
