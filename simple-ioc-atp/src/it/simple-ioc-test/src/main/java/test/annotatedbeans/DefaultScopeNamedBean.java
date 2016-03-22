package test.annotatedbeans;

import mlesiewski.simpleioc.annotations.Bean;

import java.util.UUID;

@Bean(name = "default scope named bean")
public class DefaultScopeNamedBean {

    public static final UUID CALL_UUID = UUID.randomUUID();

    public UUID call() {
        return CALL_UUID;
    }
}
