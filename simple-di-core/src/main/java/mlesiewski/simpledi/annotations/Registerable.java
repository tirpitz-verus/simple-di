package mlesiewski.simpledi.annotations;

/**
 * Instances of this interface will be discovered and loaded by the ServiceLoader and their {@link #register()} method
 * will be called during framework bootstrap by the {@link mlesiewski.simpledi.Bootstrapper}.
 */
public interface Registerable {

    /**
     * Called by during Simple-DI bootstrap this method registers {@link mlesiewski.simpledi.BeanProvider}
     * with {@link mlesiewski.simpledi.BeanRegistry}.
     */
    void register();
}
