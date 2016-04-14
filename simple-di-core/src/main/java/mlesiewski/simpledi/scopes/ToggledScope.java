package mlesiewski.simpledi.scopes;

import mlesiewski.simpledi.annotations.Bean;

/** A {@link Scope} that can be toggled off or on. */
public class ToggledScope extends DefaultScopeImpl {

    /** Ties a {@link Bean} to the toggle scope. */
    public static final String NAME = "mlesiewski.simpledi.Scope.TOGGLE_SCOPE";

    /** Creates ne instance. */
    public ToggledScope() {
        super(NAME);
    }
}
