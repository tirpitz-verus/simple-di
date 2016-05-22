package test.util;

import mlesiewski.simpledi.annotations.CustomScope;
import mlesiewski.simpledi.scopes.DefaultScopeImpl;

/**
 * Test scope - for tests.
 */
@CustomScope(TestScope.NAME)
public class TestScope extends DefaultScopeImpl {

    public static final String NAME = "TestScope";

    public TestScope() {
        super(NAME);
    }
}
