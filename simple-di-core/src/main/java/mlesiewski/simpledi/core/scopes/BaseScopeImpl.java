package mlesiewski.simpledi.core.scopes;

import mlesiewski.simpledi.core.BeanProvider;
import mlesiewski.simpledi.core.SimpleDiException;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * Base {@link Scope} implementation. When a {@link #getBean(String)} is called a {@link WeakHashMap} is checked
 * first to see if it contains a {@link mlesiewski.simpledi.core.annotations.Bean} under the name provided. If the value is
 * {@code null} then a registered {@link BeanProvider} is asked for a new instance.
 */
public class BaseScopeImpl implements Scope {

    private final Logger logger;
    protected final String name;
    protected final HashMap<String, BeanProvider> providers = new HashMap<>();
    protected final WeakHashMap<String, Object> beanCache = new WeakHashMap<>();
    protected boolean started = false;

    protected BaseScopeImpl(String name, Logger logger) {
        this.logger = logger;
        this.name = name;
        logger.debug("instantiating scope with name '{}'", name);
    }

    /** {@inheritDoc} */
    @Override
    public <T> T getBean(String name) {
        logger.trace("getBean({})", name);
        if (!started) {
            throw new SimpleDiException("Scope '" + getName() + "' is not started");
        }
        if (!providers.containsKey(name)) {
            throw new SimpleDiException("Scope '" + getName() + "' does not have a BeanProvider instance registered under the name '" + name + "'");
        }
        @SuppressWarnings("unchecked") T bean = getBeanFromBeans(name);
        return bean;
    }

    /** Returns {@link mlesiewski.simpledi.core.annotations.Bean} instance from cache. */
    protected <T> T getBeanFromBeans(String name) {
        logger.trace("getBeanFromBeans({})", name);
        @SuppressWarnings("unchecked")
        T bean = (T) beanCache.get(name);
        if (bean == null) {
            logger.trace("bean not in cache, asking provider");
            bean = provideBean(name);
            beanCache.put(name, bean);
        }
        return bean;
    }

    /** Calls a provider for a {@link mlesiewski.simpledi.core.annotations.Bean} instance. */
    protected <T> T provideBean(String name) {
        logger.trace("provideBean({})", name);
        T bean;
        try {
            @SuppressWarnings("unchecked")
            BeanProvider<T> provider = providers.get(name);
            bean = provider.provide();
            provider.setSoftDependencies(bean);
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
        logger.trace("register({}, {})", beanProvider, name);
        if (providers.containsKey(name)) {
            throw new SimpleDiException("Scope '" + getName() + "' already has a BeanProvider instance registered under the name '" + name + "'");
        }
        providers.put(name, beanProvider);
    }

    /** {@inheritDoc} */
    @Override
    public void start() {
        logger.trace("start() on scope '{}'", name);
        started = true;
    }

    /** {@inheritDoc} */
    @Override
    public void end() {
        logger.trace("end() on scope '{}'", name);
        started = false;
    }
}
