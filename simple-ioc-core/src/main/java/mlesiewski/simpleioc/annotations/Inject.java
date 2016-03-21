package mlesiewski.simpleioc.annotations;

import mlesiewski.simpleioc.Scope;

import java.lang.annotation.*;

/** Marks a field or a constructor argument as an injection point. */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
@Documented
public @interface Inject {

    /**
     * A {@link Scope} name from which this Bean should be injected.
     * If not specified then the default scope will be used.
     */
    String scope() default "";

    /** A name of {@link Bean} that is being injected. */
    String name() default "";
}
