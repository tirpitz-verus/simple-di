package mlesiewski.simpledi.annotations;

import mlesiewski.simpledi.scopes.Scope;

import java.lang.annotation.*;

/** Marks an instance field or a constructor argument as an injection point. */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.CONSTRUCTOR})
@Documented
public @interface Inject {

    /**
     * A {@link Scope} name from which this Bean should be injected.
     * If not specified then the default scope will be used.
     */
    String scope() default _Default.VALUE;

    /** A name of {@link Bean} that is being injected. */
    String name() default _Default.VALUE;
}
