package test.annotatedbeans;

import mlesiewski.simpleioc.Scope;
import mlesiewski.simpleioc.annotations.Bean;

import java.util.UUID;

@Bean(scope = Scope.APP_SCOPE)
public class ScopedDefaultNamedBean {

    public static final UUID CALL_UUID = UUID.randomUUID();

    public UUID call() {
        return CALL_UUID;
    }
}
