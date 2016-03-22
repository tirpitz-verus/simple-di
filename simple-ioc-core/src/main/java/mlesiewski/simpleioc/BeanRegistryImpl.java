package mlesiewski.simpleioc;

import java.util.HashMap;

/** A delegate for {@link BeanRegistry}. */
class BeanRegistryImpl {

    final HashMap<String, Scope> scopes;

    /** Constructs a new instance initialized with "appScope" and "toggleScope". */
    BeanRegistryImpl() {
        scopes = new HashMap<>();
        // appScope
        Scope appScope = new DefaultScopeImpl(Scope.APP_SCOPE);
        appScope.start();
        scopes.put(appScope.getName(), appScope);
        // toggleScope
        Scope toggleScope = new DefaultScopeImpl(Scope.TOGGLE_SCOPE);
        scopes.put(toggleScope.getName(), toggleScope);
    }

    /**
     * Calls {@link BeanRegistryImpl#getBean(Class)}.
     *
     * @return a bean instance
     */
    <T> T getBean(Class aClass) {
        return getBean(aClass.getName());
    }

    /**
     * Calls {@link BeanRegistryImpl#getBean(String, String)} with a default scope name.
     *
     * @return a bean instance
     */
    <T> T getBean(String name) {
        return getBean(name, Scope.DEFAULT_SCOPE);
    }

    /** @return a bean instance from the desired scope or default scope as a fallback. */
    <T> T getBean(String beanName, String scopeName) {
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
        Scope scope = getScope(scopeName);
        scope.register(beanProvider, beanProviderName);
    }

    /** @return a scope with the given name or a default scope as a fallback. */
    private Scope getScope(String scopeName) {
        boolean validScopeName = scopes.containsKey(scopeName);
        if (!validScopeName) {
            scopeName = Scope.DEFAULT_SCOPE;
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
        register(beanProvider, beanProviderName, Scope.DEFAULT_SCOPE);
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
