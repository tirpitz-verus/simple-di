package test.inject;

import mlesiewski.simpledi.core.annotations.Inject;

import java.util.UUID;

public class DefaultFieldAccessLevelsClient {

    @Inject
    BeanInjectedByType defaultField;

    public UUID call() {
        return defaultField.call();
    }
}
