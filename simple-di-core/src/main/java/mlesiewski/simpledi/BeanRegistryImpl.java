package mlesiewski.simpledi;

import mlesiewski.simpledi.scopes.ApplicationScope;
import mlesiewski.simpledi.scopes.Scope;
import mlesiewski.simpledi.scopes.ToggledScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/** A delegate for {@link BeanRegistry}. Default scope is {@link ApplicationScope}. */
class BeanRegistryImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanRegistryImpl.class);

    final HashMap<String, Scope> scopes;
    final String DEFAULT_SCOPE;

    /**
     * Constructs a new instance initialized with "appScope" and "toggleScope".
     * Might call {@link Bootstrapper#bootstrap()}.
     */
    BeanRegistryImpl() {
        LOGGER.debug("instantiating BeanRegistryImpl");
        scopes = new HashMap<>();
        // appScope
        Scope appScope = new ApplicationScope();
        scopes.put(appScope.getName(), appScope);
        // default scope
        DEFAULT_SCOPE = appScope.getName();
        // toggleScope
        Scope toggleScope = new ToggledScope();
        scopes.put(toggleScope.getName(), toggleScope);
    }

    /**
     * Calls {@link #getBean(String,String)}.
     *
     * @return a bean instance
     */
    <T> T getBean(Class aClass, String scopeName) {
        return getBean(aClass.getName(), scopeName);
    }

    /**
     * Calls {@link #getBean(String)}.
     *
     * @return a bean instance
     */
    <T> T getBean(Class aClass) {
        return getBean(aClass.getName());
    }


    /**
     * Calls {@link #getBean(String, String)} with a default scope name.
     *
     * @return a bean instance
     */
    <T> T getBean(String name) {
        return getBean(name, DEFAULT_SCOPE);
    }

    /** @return a bean instance from the desired scope or default scope as a fallback. */
    <T> T getBean(String beanName, String scopeName) {
        LOGGER.trace("getBean({}, {})", beanName, scopeName);
        Scope scope = getScope(scopeName);
        return scope.getBean(beanName);
    }

    /**
     * Registers a {@link Scope}.
     *
     * @param beanProvider     a {@link BeanProvider} instance
     * @param beanProviderName a name under which this provider is going to be registered
     * @param scopeName        name of the {@link Scope} to register this provider with
     */
    <T> void register(BeanProvider<T> beanProvider, String beanProviderName, String scopeName) {
        LOGGER.trace("register({}, {}, {})", beanProvider, beanProviderName, scopeName);
        if (beanProviderName == null) {
            throw new SimpleDiException("Cannot register a BeanProvider with a null name");
        }
        if (beanProvider == null) {
            throw new SimpleDiException("Cannot register a null BeanProvider under name '" + beanProviderName + "'");
        }
        Scope scope = getScope(scopeName);
        scope.register(beanProvider, beanProviderName);
    }

    /** @return a scope with the given name or a default scope as a fallback. */
    private Scope getScope(String scopeName) {
        LOGGER.trace("getScope({})", scopeName);
        boolean validScopeName = scopes.containsKey(scopeName);
        if (!validScopeName) {
            LOGGER.trace("scopeName invalid, changing to default {}", DEFAULT_SCOPE);
            scopeName = DEFAULT_SCOPE;
        }
        return scopes.get(scopeName);
    }

    /**
     * Just calls {@link BeanRegistryImpl#register(BeanProvider, String, String)} with a default scope name.
     *
     * @param beanProvider     a {@link BeanProvider} instance
     * @param beanProviderName a name under which this provider is going to be registered
     */
    <T> void register(BeanProvider<T> beanProvider, String beanProviderName) {
        register(beanProvider, beanProviderName, DEFAULT_SCOPE);
    }

    /**
     * Just calls {@link BeanRegistryImpl#register(BeanProvider, String, String)}.
     *
     * @param beanProvider     a {@link BeanProvider} instance
     * @param beanProviderName a name under which this provider is going to be registered
     * @param scopeName        name of the {@link Scope} to register this provider with
     */
    <T> void register(BeanProvider<T> beanProvider, Class<T> beanProviderName, String scopeName) {
        register(beanProvider, beanProviderName.getName(), scopeName);
    }

    /**
     * Just calls {@link BeanRegistryImpl#register(BeanProvider, String)}.
     *
     * @param beanProvider     a {@link BeanProvider} instance
     * @param beanProviderName a name under which this provider is going to be registered
     */
    <T> void register(BeanProvider<T> beanProvider, Class<T> beanProviderName) {
        register(beanProvider, beanProviderName.getName());
    }
}
