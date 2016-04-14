package mlesiewski.simpledi;

import mlesiewski.simpledi.scopes.Scope;

/** An interface for classes that can provide a bean. */
public interface BeanProvider<T> {

    /** @return a {@link mlesiewski.simpledi.annotations.Bean} instance */
    T provide();

    /** Used to signal this provider that the {@link Scope} has ended. */
    void scopeEnded();
}
