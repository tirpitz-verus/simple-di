package mlesiewski.simpledi.core;

import mlesiewski.simpledi.core.annotations.Registerable;
import mlesiewski.simpledi.core.scopes.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

/**
 * This class bootstraps {@link Registerable} discovery and registration.
 */
final class Bootstrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrapper.class);
    static boolean bootstrapped = false;

    /**
     * no you can't
     */
    private Bootstrapper() {
    }

    /**
     * Bootstraps {@link BeanProvider} registration.
     */
    static void bootstrap() {
        if (bootstrapped) {
            LOGGER.warn("already bootstrapped - skipping");
            return;
        }
        LOGGER.trace("bootstrapping custom scopes");
        ServiceLoader<Scope> scopes = ServiceLoader.load(Scope.class);
        int scopeCount = 0;
        for (Scope scope : scopes) {
            BeanRegistry.register(scope);
            LOGGER.trace("registered {}", scope.getClass().getName());
            scopeCount++;
        }
        LOGGER.trace("bootstrapping registrable classes");
        ServiceLoader<Registerable> registered = ServiceLoader.load(Registerable.class);
        int registeredCount = 0;
        for (Registerable registerable : registered) {
            registerable.register();
            LOGGER.trace("registered {}", registerable.getClass().getName());
            registeredCount++;
        }
        LOGGER.debug("bootstrapping completed, loaded {} scopes and registered {} classes", scopeCount, registeredCount);
        bootstrapped = true;
    }
}
