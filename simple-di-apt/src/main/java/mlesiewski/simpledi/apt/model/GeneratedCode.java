package mlesiewski.simpledi.apt.model;

import javax.lang.model.element.TypeElement;

/**
 * Used to mark a {@link ClassEntity} as applicable for code generation.
 */
public interface GeneratedCode {

    /**
     * @return name of the represented class
     */
    String typeName();

    /**
     * @return name of the bean tied to this generated code
     */
    BeanName beanName();

    /**
     * @return {@code true} if and only if a source {@link TypeElement} was associated with this generated code
     */
    boolean hasSource();

    /**
     * @return a source {@link TypeElement} associated with this generated code
     */
    TypeElement getSource();
}
