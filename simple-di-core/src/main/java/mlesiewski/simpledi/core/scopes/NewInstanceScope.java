package mlesiewski.simpledi.core.scopes;

import mlesiewski.simpledi.core.BeanProvider;
import mlesiewski.simpledi.core.SimpleDiException;
import mlesiewski.simpledi.core.annotations.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

/** A scope that always returns new instances - which translates into always calling the BeanProvider for a new instance. */
public class NewInstanceScope implements Scope {

    /** Ties a {@link Bean} to the new instance scope. */
    public static final String NAME = "mlesiewski.simpledi.Scope.NEW_SCOPE";

    private static final Logger LOGGER = LoggerFactory.getLogger(NewInstanceScope.class);

    private final HashMap<String, BeanProvider> providers = new HashMap<>();

    @Override
    public <T> T getBean(String name) {
        LOGGER.trace("getBean({})", name);
        if (!providers.containsKey(name)) {
            throw new SimpleDiException("Scope '" + NAME+ "' does not have a BeanProvider instance registered under the name '" + name + "'");
        }
        @SuppressWarnings("unchecked")
        BeanProvider<T> provider = (BeanProvider<T>) providers.get(name);
        T bean = provider.provide();
        provider.setSoftDependencies(bean);
        return bean;
    }

    @Override
    public boolean hasBean(String name) {
        return providers.containsKey(name);
    }

    @Override
    public String getName() {
        return NAME;
    }

    /** {@inheritDoc} */
    @Override
    public <T> void register(BeanProvider<T> beanProvider, String name) {
        LOGGER.trace("register({}, {})", beanProvider, name);
        if (providers.containsKey(name)) {
            throw new SimpleDiException("Scope '" + NAME + "' already has a BeanProvider instance registered under the name '" + name + "'");
        }
        providers.put(name, beanProvider);
    }

    /** @throws SimpleDiException always */
    @Override
    public void start() {
        throw new SimpleDiException("Scope '" + NAME +"' cannot be started");
    }

    /** @throws SimpleDiException always */
    @Override
    public void end() {
        throw new SimpleDiException("Scope '" + NAME +"' cannot be ended");
    }
}
