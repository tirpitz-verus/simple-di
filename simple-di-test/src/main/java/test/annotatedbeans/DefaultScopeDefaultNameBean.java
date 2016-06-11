package test.annotatedbeans;

import mlesiewski.simpledi.annotations.Bean;
import mlesiewski.simpledi.annotations.Inject;

import java.util.UUID;

@Bean
public class DefaultScopeDefaultNameBean {

    public static final UUID CALL_UUID = UUID.randomUUID();

    public UUID call() {
        return CALL_UUID;
    }
}
