package mlesiewski.simpleioc;

/** An interface for classes that can provide a bean. */
public interface BeanProvider<T> {

    /** @return a {@link mlesiewski.simpleioc.annotations.Bean} instance */
    T provide();

    /** Used to signal this provider that the {@link Scope} has ended. */
    void scopeEnded();
}
