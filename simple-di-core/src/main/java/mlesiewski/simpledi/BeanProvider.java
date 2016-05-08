package mlesiewski.simpledi;

/** An interface for classes that can provide a bean. */
public interface BeanProvider<T> {

    /** @return a {@link mlesiewski.simpledi.annotations.Bean} instance */
    T provide();
}
