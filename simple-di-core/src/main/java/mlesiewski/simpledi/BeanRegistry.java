package mlesiewski.simpledi;

import mlesiewski.simpledi.annotations.Bean;
import mlesiewski.simpledi.scopes.Scope;

/** A class for getting beans from. It actually a static interface to the {@link BeanRegistryImpl}. */
public class BeanRegistry {

    /** only one instance */
    static final BeanRegistryImpl DELEGATE = new BeanRegistryImpl();

    /** No you can't. */
    private BeanRegistry() {
    }

    /**
     * @param name a name under which one of the {@link Scope Scopes} has a {@link BeanProvider} registered
     * @return a {@link Bean} instance
     * @throws SimpleDiException if no {@link Scope} has a {@link BeanProvider} registered under the name provided
     */
    public static <T> T getBean(String name) {
        return DELEGATE.getBean(name);
    }

    /**
     * @param name a name under which one of the {@link Scope Scopes} has a {@link BeanProvider} registered
     * @return a {@link Bean} instance
     * @throws SimpleDiException if no {@link Scope} has a {@link BeanProvider} registered under the name provided
     */
    public static <T> T getBean(Class<T> name) {
        return DELEGATE.getBean(name);
    }

    /** Registers a {@link BeanProvider} instance under a given name with the desired {@link Scope}.
     * @param beanProvider a {@link BeanProvider} instance to register
     * @param beanProviderName a name under which the {@link BeanProvider} instance should be registered
     * @param scopeName a name of the {@link Scope} to register the {@link BeanProvider} with
     * @throws SimpleDiException if a {@link BeanProvider} under that name was already registered
     */
    public static <T> void register(BeanProvider<T> beanProvider, String beanProviderName, String scopeName) {
        DELEGATE.register(beanProvider, beanProviderName, scopeName);
    }

    /** Registers a {@link BeanProvider} instance under a given name with the desired {@link Scope}.
     * @param beanProvider a {@link BeanProvider} instance to register
     * @param beanProviderName a name under which the {@link BeanProvider} instance should be registered
     * @param scopeName a name of the {@link Scope} to register the {@link BeanProvider} with
     * @throws SimpleDiException if a {@link BeanProvider} under that name was already registered
     */
    public static <T> void register(BeanProvider<T> beanProvider, Class<T> beanProviderName, String scopeName) {
        DELEGATE.register(beanProvider, beanProviderName, scopeName);
    }

    /** Registers a {@link BeanProvider} instance under a given name with the default {@link Scope}.
     * @param beanProvider a {@link BeanProvider} instance to register
     * @param beanProviderName a name under which the {@link BeanProvider} instance should be registered
     * @throws SimpleDiException if a {@link BeanProvider} under that name was already registered
     */
    public static <T> void register(BeanProvider<T> beanProvider, String beanProviderName) {
        DELEGATE.register(beanProvider, beanProviderName);
    }

    /** Registers a {@link BeanProvider} instance under a given name with the default {@link Scope}.
     * @param beanProvider a {@link BeanProvider} instance to register
     * @param beanProviderName a name under which the {@link BeanProvider} instance should be registered
     * @throws SimpleDiException if a {@link BeanProvider} under that name was already registered
     */
    public static <T> void register(BeanProvider<T> beanProvider, Class<T> beanProviderName) {
        DELEGATE.register(beanProvider, beanProviderName);
    }

    /**
     * @return name of the default scope
     */
    public static String defaultScope() {
        return DELEGATE.DEFAULT_SCOPE;
    }
}