package mlesiewski.simpledi.scopes;

import mlesiewski.simpledi.BeanProvider;
import mlesiewski.simpledi.SimpleDiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * Default {@link Scope} implementation. When a {@link #getBean(String)} is called a {@link WeakHashMap} is checked
 * first to see if it contains a {@link mlesiewski.simpledi.annotations.Bean} under the name provided. If the value is
 * {@code null} then a registered {@link BeanProvider} is asked for a new instance.
 */
class DefaultScopeImpl implements Scope {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultScopeImpl.class);

    final String name;
    final HashMap<String, BeanProvider> providers = new HashMap<>();
    final WeakHashMap<String, Object> beanCache = new WeakHashMap<>();
    boolean started = false;

    DefaultScopeImpl(String name) {
        LOGGER.debug("instantiating scope with name '{}'", name);
        this.name = name;
    }

    /** {@inheritDoc} */
    @Override
    public <T> T getBean(String name) {
        LOGGER.trace("getBean({})", name);
        if (!started) {
            throw new SimpleDiException("Scope '" + getName() + "' is not started");
        }
        if (!providers.containsKey(name)) {
            throw new SimpleDiException("Scope '" + getName() + "' does not have a BeanProvider instance registered under the name '" + name + "'");
        }
        @SuppressWarnings("unchecked") T bean = getBeanFromBeans(name);
        return bean;
    }

    /** Returns {@link mlesiewski.simpledi.annotations.Bean} instance from cache. */
    <T> T getBeanFromBeans(String name) {
        LOGGER.trace("getBeanFromBeans({})", name);
        @SuppressWarnings("unchecked")
        T bean = (T) beanCache.get(name);
        if (bean == null) {
            LOGGER.trace("bean not in cache, asking provider");
            bean = provideBean(name);
            beanCache.put(name, bean);
        }
        return bean;
    }

    /** Calls a provider for a {@link mlesiewski.simpledi.annotations.Bean} instance. */
    <T> T provideBean(String name) {
        LOGGER.trace("provideBean({})", name);
        T bean;
        try {
            @SuppressWarnings("unchecked")
            BeanProvider<T> provider = providers.get(name);
            bean = provider.provide();
        } catch (ClassCastException ccs) {
            throw new SimpleDiException("In Scope '" + getName() + "' BeanProvider '" + name + "' produced a value with a wrong type", ccs);
        }
        if (bean == null) {
            throw new SimpleDiException("In Scope '" + getName() + "' BeanProvider '" + name + "' produced a null value");
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
        LOGGER.trace("register({}, name)", beanProvider, name);
        if (providers.containsKey(name)) {
            throw new SimpleDiException("Scope '" + getName() + "' already has a BeanProvider instance registered under the name '" + name + "'");
        }
        providers.put(name, beanProvider);
    }

    /** {@inheritDoc} */
    @Override
    public void start() {
        LOGGER.trace("start() on scope '{}'", name);
        started = true;
    }

    /** {@inheritDoc} */
    @Override
    public void end() {
        LOGGER.trace("end() on scope '{}'", name);
        started = false;
        providers.values().forEach(BeanProvider::scopeEnded);
    }
}
