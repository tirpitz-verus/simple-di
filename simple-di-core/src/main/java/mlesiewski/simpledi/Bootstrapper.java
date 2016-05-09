package mlesiewski.simpledi;

import mlesiewski.simpledi.annotations.Registerable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

/**
 * This class bootstraps {@link Registerable} discovery and registration.
 */
public class Bootstrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrapper.class);

    /**
     * Bootstraps {@link BeanProvider} registration.
     */
    public static void bootstrap() {
        ServiceLoader<Registerable> registered = ServiceLoader.load(Registerable.class);
        registered.forEach((registerable) -> {
            registerable.register();
            LOGGER.trace("registered {}", registerable.getClass().getName());
        });
    }
}
