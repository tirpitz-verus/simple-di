package mlesiewski.simpleioc.scopes;

import mlesiewski.simpleioc.BeanProvider;
import mlesiewski.simpleioc.SimpleIocException;

import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * Default {@link Scope} implementation. When a {@link #getBean(String)} is called a {@link WeakHashMap} is checked
 * first to see if it contains a {@link mlesiewski.simpleioc.annotations.Bean} under the name provided. If the value is
 * {@code null} then a registered {@link BeanProvider} is asked for a new instance.
 */
class DefaultScopeImpl implements Scope {

    final String name;
    final HashMap<String, BeanProvider> providers = new HashMap<>();
    final WeakHashMap<String, Object> beanCache = new WeakHashMap<>();
    boolean started = false;

    DefaultScopeImpl(String name) {
        this.name = name;
    }

    /** {@inheritDoc} */
    @Override
    public <T> T getBean(String name) {
        if (!started) {
            throw new SimpleIocException("Scope '" + getName() + "' is not started");
        }
        if (!providers.containsKey(name)) {
            throw new SimpleIocException("Scope '" + getName() + "' does not have a BeanProvider instance registered under the name '" + name + "'");
        }
        @SuppressWarnings("unchecked") T bean = getBeanFromBeans(name);
        return bean;
    }

    /** Returns {@link mlesiewski.simpleioc.annotations.Bean} instance from cache. */
    <T> T getBeanFromBeans(String name) {
        @SuppressWarnings("unchecked")
        T bean = (T) beanCache.get(name);
        if (bean == null) {
            bean = provideBean(name);
            beanCache.put(name, bean);
        }
        return bean;
    }

    /** Calls a provider for a {@link mlesiewski.simpleioc.annotations.Bean} instance. */
    <T> T provideBean(String name) {
        T bean;
        try {
            @SuppressWarnings("unchecked")
            BeanProvider<T> provider = providers.get(name);
            bean = provider.provide();
        } catch (ClassCastException ccs) {
            throw new SimpleIocException("In Scope '" + getName() + "' BeanProvider '" + name + "' produced a value with a wrong type", ccs);
        }
        if (bean == null) {
            throw new SimpleIocException("In Scope '" + getName() + "' BeanProvider '" + name + "' produced a null value");
        }
        return bean;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasBean(String name) {
        return started && providers.containsKey(name);
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    @Override
    public <T> void register(BeanProvider<T> beanProvider, String name) {
        if (providers.containsKey(name)) {
            throw new SimpleIocException("Scope '" + getName() + "' already has a BeanProvider instance registered under the name '" + name + "'");
        }
        providers.put(name, beanProvider);
    }

    /** {@inheritDoc} */
    @Override
    public void start() {
        started = true;
    }

    /** {@inheritDoc} */
    @Override
    public void end() {
        started = false;
        providers.values().forEach(BeanProvider::scopeEnded);
    }
}
