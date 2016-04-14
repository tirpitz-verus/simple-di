package mlesiewski.simpleioc.annotations;

import mlesiewski.simpleioc.BeanRegistry;
import mlesiewski.simpleioc.scopes.Scope;

import java.lang.annotation.*;

/**
 * Marks a class as a Bean class.
 * Bean class instances can be requested from the {@link BeanRegistry}.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface Bean {

    /**
     * A {@link Scope} name from which this Bean should be available.
     * If not specified then the default scope will be used.
     */
    String scope() default _Default.VALUE;

    /** A name of this Bean. If not specified than a class name will be used. */
    String name() default _Default.VALUE;
}
