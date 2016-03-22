package mlesiewski.simpleioc;

import mlesiewski.simpleioc.annotations.Bean;

/**
 * A Scope has registered {@link BeanProvider}. It can be inquired for a {@link Bean} by its name.
 * Only a started Scope can have a {@link Bean}.
 */
public interface Scope {

    /** Ties a {@link Bean} to the application scope. */
    String APP_SCOPE = "mlesiewski.simpleioc.Scope.APP_SCOPE";

    /** Ties a {@link Bean} to the default scope. */
    String DEFAULT_SCOPE = APP_SCOPE;

    /** Ties a {@link Bean} to the toggle scope. */
    String TOGGLE_SCOPE = "mlesiewski.simpleioc.Scope.TOGGLE_SCOPE";

    /**
     * @param name a name under which a {@link BeanProvider} was registered
     * @return a {@link Bean} instance if a {@link BeanProvider} is registered with that name
     * @throws SimpleIocException if no {@link BeanProvider} is registered with the name provided or if this scope is not started
     */
    <T> T getBean(String name);

    /**
     * @param name a name under which a {@link BeanProvider} was registered
     * @return {@code true} if and only if a {@link BeanProvider} was registered under the name provided and this Scope is started
     */
    boolean hasBean(String name);

    /** @return the name of this Scope */
    String getName();

    /**
     * Registers a {@link BeanProvider} under a name provided.
     *
     * @param name         a name under which a {@link BeanProvider} was registered
     * @param beanProvider a {@link BeanProvider} instance
     */
    <T> void register(BeanProvider<T> beanProvider, String name);

    /** Starts this Scope. */
    void start();

    /** Ends this Scope. Will signal all {@link BeanProvider BeanProviders}. */
    void end();
}
