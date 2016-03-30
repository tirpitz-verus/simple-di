package test.inject;

import mlesiewski.simpleioc.annotations.Inject;

import java.util.UUID;

public class PrivateFieldAccessLevelsClient {

    @Inject
    private BeanInjectedByType privateField;

    public UUID call() {
        return privateField.call();
    }
}
