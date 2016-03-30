package mlesiewski.simpleioc.scopes;

import mlesiewski.simpleioc.BeanProvider;
import mlesiewski.simpleioc.SimpleIocException;
import mlesiewski.simpleioc.annotations.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/**
 * Global application scope - beans will be created eagerly. This scope is created started and will not end.
 */
public class ApplicationScope extends DefaultScopeImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationScope.class);

    /** Ties a {@link Bean} to the application scope. */
    public static final String NAME = "mlesiewski.simpleioc.Scope.APP_SCOPE";

    /** Strongly referenced eager bean cache. */
    final HashMap<String, Object> eagerBeanCache = new HashMap<>();

    /** Creates new Application Scope and starts it. */
    public ApplicationScope() {
        super(NAME);
        super.start();
    }

    /** @throws SimpleIocException always */
    @Override
    public void start() {
        throw new SimpleIocException(NAME + " cannot be started");
    }

    /** @throws SimpleIocException always */
    @Override
    public void end() {
        throw new SimpleIocException(NAME + " cannot be ended");
    }

    /** Calls {@link BeanProvider#provide()} immediately. */
    @Override
    public <T> void register(BeanProvider<T> beanProvider, String name) {
        LOGGER.trace("register({}, {})", beanProvider, name);
        if (eagerBeanCache.containsKey(name)) {
            throw new SimpleIocException("Scope '" + getName() + "' already has a BeanProvider instance registered under the name '" + name + "'");
        }
        T bean = beanProvider.provide();
        if (bean == null) {
            throw new SimpleIocException("In Scope '" + getName() + "' BeanProvider '" + name + "' produced a null value");
        }
        eagerBeanCache.put(name, bean);
    }

    /** Gets {@link Bean} from the eager bean cache. */
    @Override
    public <T> T getBean(String name) {
        LOGGER.trace("getBean({})", name);
        if (!eagerBeanCache.containsKey(name)) {
            throw new SimpleIocException("Scope '" + getName() + "' does not have a BeanProvider instance registered under the name '" + name + "'");
        }
        try {
            @SuppressWarnings("unchecked")
            T bean = (T) eagerBeanCache.get(name);
            return bean;
        } catch (ClassCastException ccs) {
            throw new SimpleIocException("In Scope '" + getName() + "' bean '" + name + "' has a different type to requested", ccs);
        }
    }
}
