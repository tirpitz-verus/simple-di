package mlesiewski.simpledi.core;

/** An interface for classes that can provide a bean. */
public interface BeanProvider<T> {

    /** @return a {@link mlesiewski.simpledi.core.annotations.Bean} instance */
    T provide();

    /**
     * for new instances sets soft dependencies (through setters or field access)
     * @param newInstance
     */
    void setSoftDependencies(T newInstance);
}
