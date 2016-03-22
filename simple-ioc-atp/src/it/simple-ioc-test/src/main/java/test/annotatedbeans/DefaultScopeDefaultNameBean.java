package test.annotatedbeans;

import mlesiewski.simpleioc.annotations.Bean;

import java.util.UUID;

@Bean
public class DefaultScopeDefaultNameBean {

    public static final UUID CALL_UUID = UUID.randomUUID();

    public UUID call() {
        return CALL_UUID;
    }
}
