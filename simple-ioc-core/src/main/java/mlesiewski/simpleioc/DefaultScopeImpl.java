package mlesiewski.simpleioc;

import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * Default {@link Scope} implementation. When a {@link #getBean(String)} is called a {@link WeakHashMap} is checked
 * first to see if it contains a {@link mlesiewski.simpleioc.annotations.Bean} under the name provided. If the value is
 * {@code null} then a registered {@link BeanProvider} is asked for a new instance.
 */
class DefaultScopeImpl implements Scope {

    private final String name;
    private boolean started = false;
    private HashMap<String, BeanProvider> providers = new HashMap<>();
    WeakHashMap<String, Object> beanCache = new WeakHashMap<>();

    DefaultScopeImpl(String name) {
        this.name = name;
    }

    @Override
    public <T> T getBean(String name) {
        if (!started) {
            throw new SimpleIocException("Scope '" + getName() + "'is not started");
        }
        if (!providers.containsKey(name)) {
            throw new SimpleIocException("Scope '" + getName() + "' does not have a BeanProvider instance registered under the name '" + name + "'");
        }
        @SuppressWarnings("unchecked") T bean = getBeanFromBeans(name);
        return bean;
    }

    private <T> T getBeanFromBeans(String name) {
        @SuppressWarnings("unchecked")
        T bean = (T) beanCache.get(name);
        if (bean == null) {
            bean = provideBean(name);
            beanCache.put(name, bean);
        }
        return bean;
    }

    private <T> T provideBean(String name) {
        T bean;
        try {
            @SuppressWarnings("unchecked")
            BeanProvider<T> provider = providers.get(name);
            bean = provider.provide();
        } catch (ClassCastException ccs) {
            throw new SimpleIocException("In Scope '" + getName() + "'BeanProvider '" + name + "' produced a value with a wrong type", ccs);
        }
        if (bean == null) {
            throw new SimpleIocException("In Scope '" + getName() + "'BeanProvider '" + name + "' produced a null value");
        }
        return bean;
    }

    @Override
    public boolean hasBean(String name) {
        return started && providers.containsKey(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public <T> void register(BeanProvider<T> beanProvider, String name) {
        if (providers.containsKey(name)) {
            throw new SimpleIocException("Scope '" + getName() + "' already has a BeanProvider instance registered under the name '" + name + "'");
        }
        providers.put(name, beanProvider);
    }

    @Override
    public void start() {
        started = true;
    }

    @Override
    public void end() {
        started = false;
        providers.values().forEach(BeanProvider::scopeEnded);
    }
}
