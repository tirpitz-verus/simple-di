package test.annotatedbeans;

import mlesiewski.simpleioc.scopes.ApplicationScope;
import mlesiewski.simpleioc.annotations.Bean;

import java.util.UUID;

@Bean(scope = ApplicationScope.NAME)
public class ScopedDefaultNamedBean {

    public static final UUID CALL_UUID = UUID.randomUUID();

    public UUID call() {
        return CALL_UUID;
    }
}
