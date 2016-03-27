package test.annotatedbeans;

import mlesiewski.simpleioc.scopes.Scope;
import mlesiewski.simpleioc.annotations.Bean;

import java.util.UUID;

@Bean(scope = Scope.APP_SCOPE, name = "scoped named bean")
public class ScopedNamedBean {

    public static final UUID CALL_UUID = UUID.randomUUID();

    public UUID call() {
        return CALL_UUID;
    }
}
