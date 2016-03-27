package mlesiewski.simpleioc.scopes;

import mlesiewski.simpleioc.annotations.Bean;

/** A {@link Scope} that can be toggled off or on. */
public class ToggledScope extends DefaultScopeImpl {

    /** Ties a {@link Bean} to the toggle scope. */
    public static final String NAME = "mlesiewski.simpleioc.Scope.TOGGLE_SCOPE";

    /** Creates ne instance. */
    public ToggledScope() {
        super(NAME);
    }
}
