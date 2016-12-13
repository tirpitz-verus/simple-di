package mlesiewski.simpledi.core.annotations;

import mlesiewski.simpledi.core.BeanProvider;
import mlesiewski.simpledi.core.BeanRegistry;

/**
 * Instances of this interface will be discovered and loaded by the ServiceLoader and their {@link #register()} method
 * will be called during framework bootstrap by the {@link mlesiewski.simpledi.core.Bootstrapper}.
 */
public interface Registerable {

    /**
     * Called by during Simple-DI bootstrap this method registers {@link BeanProvider}
     * with {@link BeanRegistry}.
     */
    void register();
}
