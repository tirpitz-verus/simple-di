package mlesiewski.simpledi;

import mlesiewski.simpledi.annotations.Bean;
import mlesiewski.simpledi.scopes.Scope;

/** A class for getting beans from. It actually a static interface to the {@link BeanRegistryImpl}. */
public final class BeanRegistry {

    static {
        init();
    }

    /** only one instance */
    static BeanRegistryImpl DELEGATE;

    /** No you can't. */
    private BeanRegistry() {
    }

    /**
     * Creates new {@link BeanRegistryImpl} instance and calls {@link Bootstrapper#bootstrap()}.
     * Gets called in the static initializer.
     */
    static void init() {
        DELEGATE = new BeanRegistryImpl();
        Bootstrapper.bootstrap();
    }

    /**
     * @param beanName a beanName under which one of the {@link Scope Scopes} has a {@link BeanProvider} registered
     * @return a {@link Bean} instance
     * @throws SimpleDiException if no {@link Scope} has a {@link BeanProvider} registered under the beanName provided
     */
    public static <T> T getBean(String beanName) {
        return DELEGATE.getBean(beanName);
    }

    /**
     * @param beanName a beanName under which one of the {@link Scope Scopes} has a {@link BeanProvider} registered
     * @param scopeName a beanName of the {@link Scope} to get the {@link Bean} from
     * @return a {@link Bean} instance
     * @throws SimpleDiException if no {@link Scope} has a {@link BeanProvider} registered under the beanName provided
     */
    public static <T> T getBean(String beanName, String scopeName) {
        return DELEGATE.getBean(beanName, scopeName);
    }

    /**
     * @param beanName a beanName under which one of the {@link Scope Scopes} has a {@link BeanProvider} registered
     * @return a {@link Bean} instance
     * @throws SimpleDiException if no {@link Scope} has a {@link BeanProvider} registered under the beanName provided
     */
    public static <T> T getBean(Class<T> beanName) {
        return DELEGATE.getBean(beanName);
    }

    /**
     * @param beanName  a beanName under which one of the {@link Scope Scopes} has a {@link BeanProvider} registered
     * @param scopeName a name of the {@link Scope} to get the {@link Bean} from
     * @return a {@link Bean} instance
     * @throws SimpleDiException if no {@link Scope} has a {@link BeanProvider} registered under the beanName provided
     */
    public static <T> T getBean(Class<T> beanName, String scopeName) {
        return DELEGATE.getBean(beanName, scopeName);
    }

    /**
     * Registers a {@link BeanProvider} instance under a given name with the desired {@link Scope}.
     *
     * @param beanProvider     a {@link BeanProvider} instance to register
     * @param beanProviderName a name under which the {@link BeanProvider} instance should be registered
     * @param scopeName        a name of the {@link Scope} to register the {@link BeanProvider} with
     * @throws SimpleDiException if a {@link BeanProvider} under that name was already registered
     */
    public static <T> void register(BeanProvider<T> beanProvider, String beanProviderName, String scopeName) {
        DELEGATE.register(beanProvider, beanProviderName, scopeName);
    }

    /**
     * Registers a {@link BeanProvider} instance under a given name with the desired {@link Scope}.
     *
     * @param beanProvider     a {@link BeanProvider} instance to register
     * @param beanProviderName a name under which the {@link BeanProvider} instance should be registered
     * @param scopeName        a name of the {@link Scope} to register the {@link BeanProvider} with
     * @throws SimpleDiException if a {@link BeanProvider} under that name was already registered
     */
    public static <T> void register(BeanProvider<T> beanProvider, Class<T> beanProviderName, String scopeName) {
        DELEGATE.register(beanProvider, beanProviderName, scopeName);
    }

    /**
     * Registers a {@link BeanProvider} instance under a given name with the default {@link Scope}.
     *
     * @param beanProvider     a {@link BeanProvider} instance to register
     * @param beanProviderName a name under which the {@link BeanProvider} instance should be registered
     * @throws SimpleDiException if a {@link BeanProvider} under that name was already registered
     */
    public static <T> void register(BeanProvider<T> beanProvider, String beanProviderName) {
        DELEGATE.register(beanProvider, beanProviderName);
    }

    /**
     * Registers a {@link BeanProvider} instance under a given name with the default {@link Scope}.
     *
     * @param beanProvider     a {@link BeanProvider} instance to register
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