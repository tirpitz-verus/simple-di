package test.annotatedbeans;

import mlesiewski.simpledi.core.annotations.Bean;
import test.util.TestScope;

import java.util.UUID;

@Bean(scope = TestScope.NAME)
public class ScopedDefaultNamedBean {

    public static final UUID CALL_UUID = UUID.randomUUID();

    public UUID call() {
        return CALL_UUID;
    }
}
