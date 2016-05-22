package test.annotatedbeans;

import mlesiewski.simpledi.annotations.Bean;

import java.util.UUID;

@Bean(name = "default_scope_named_bean")
public class DefaultScopeNamedBean {

    public static final UUID CALL_UUID = UUID.randomUUID();

    public UUID call() {
        return CALL_UUID;
    }
}
