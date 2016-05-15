package mlesiewski.simpledi.model;

/**
 * Used to mark a {@link ClassEntity} as applicable for code generation.
 */
public interface GeneratedCode {

    /**
     * @return name of the represented class
     */
    String typeName();
}
