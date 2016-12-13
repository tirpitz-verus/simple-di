package mlesiewski.simpledi.scopes;

import mlesiewski.simpledi.BeanProvider;
import mlesiewski.simpledi.SimpleDiException;
import mlesiewski.simpledi.annotations.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Global application scope - beans will be created eagerly after this scope was started.
 * Beans based on this scope should not have hard (constructor) dependencies on beans from other scopes.
 */
public class ApplicationScope extends BaseScopeImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationScope.class);

    /** {@link BeanProvider BeanProviders} waiting for {@link #start()}. */
    private LinkedHashMap<String, BeanProvider> waitingProviders = new LinkedHashMap<>();

    /** Ties a {@link Bean} to the application scope. */
    public static final String NAME = "mlesiewski.simpledi.Scope.APP_SCOPE";

    /** Strongly referenced eager bean cache. */
    final HashMap<String, Object> eagerBeanCache = new HashMap<>();

    /**
     * Creates new Application Scope. Can now register new {@link BeanProvider BeanProvider's} that won't be called
     * until {@link #start()}.
     */
    public ApplicationScope() {
        super(NAME, LOGGER);
    }

    /**
     * Calls all previously registered {@link BeanProvider BeanProvider's}.
     * Any new {@link BeanProvider BeanProvider's} will be called eagerly and discarded.
     */
    @Override
    public void start() {
        super.start();
        waitingProviders.forEach(this::cacheBeanInstance);
        waitingProviders.forEach(this::setSoftDependencies);
        waitingProviders = null;
    }

    private <T> void setSoftDependencies(String name, BeanProvider<T> beanProvider) {
        T bean = getBean(name);
        beanProvider.setSoftDependencies(bean);
    }

    /** @throws SimpleDiException always */
    @Override
    public void end() {
        throw new SimpleDiException(NAME + " cannot be ended");
    }

    /**
     * If the scope was instantiated then it schedules calling of {@link BeanProvider#provide()} for the moment of
     * {@link #start()} being called. It the scope was already started then calls {@link BeanProvider#provide()}
     * immediately.
     */
    @Override
    public <T> void register(BeanProvider<T> beanProvider, String name) {
        LOGGER.trace("register({}, {})", beanProvider, name);
        if (started) {
            T bean = cacheBeanInstance(name, beanProvider);
            beanProvider.setSoftDependencies(bean);
        } else {
            waitingProviders.put(name, beanProvider);
        }
    }

    private <T> T cacheBeanInstance(String name, BeanProvider<T> beanProvider) {
        if (eagerBeanCache.containsKey(name)) {
            throw new SimpleDiException("Scope '" + getName() + "' already has a Bean instance registered under the name '" + name + "'");
        }
        T bean = beanProvider.provide();
        if (bean == null) {
            throw new SimpleDiException("In Scope '" + getName() + "' BeanProvider '" + name + "' produced a null value");
        }
        eagerBeanCache.put(name, bean);
        return bean;
    }

    /** Gets {@link Bean} from the eager bean cache. */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        LOGGER.trace("getBean({})", name);
        if (!eagerBeanCache.containsKey(name)) {
            throw new SimpleDiException("Scope '" + getName() + "' does not have a BeanProvider instance registered under the name '" + name + "'");
        }
        try {
            return (T) eagerBeanCache.get(name);
        } catch (ClassCastException ccs) {
            throw new SimpleDiException("In Scope '" + getName() + "' bean '" + name + "' has a different type to requested", ccs);
        }
    }

    @Override
    public boolean hasBean(String name) {
        return eagerBeanCache.containsKey(name);
    }
}
