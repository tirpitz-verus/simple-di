package mlesiewski.simpleioc.annotations;

import mlesiewski.simpleioc.Scope;

import java.lang.annotation.*;

/** Marks a method as a bean producer. */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
@Documented
public @interface Produce {

    /**
     * A {@link Scope} name from which this Bean should be available.
     * If not specified then the default scope will be used.
     */
    String scope() default "";

    /** A name of {@link Bean} that is being produced. */
    String name() default "";
}
