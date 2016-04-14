package mlesiewski.simpleioc.model;

/**
 * Used to mark a {@link ClassEntity} as applicable for code generation.
 */
public interface CodeGenerated {

    /**
     * @return name of the represented class
     */
    String typeName();
}
