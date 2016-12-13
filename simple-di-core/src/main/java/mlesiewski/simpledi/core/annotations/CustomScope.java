package mlesiewski.simpledi.core.annotations;

import java.lang.annotation.*;

/**
 * Marks a class as a custom scope.
 * This class needs to implement {@link mlesiewski.simpledi.core.scopes.Scope}.
 * Name cannot be left empty.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
@Documented
public @interface CustomScope {

    /** Name of this scope. It needs to be equal to the value returned by getName() method. */
    String value();
}
