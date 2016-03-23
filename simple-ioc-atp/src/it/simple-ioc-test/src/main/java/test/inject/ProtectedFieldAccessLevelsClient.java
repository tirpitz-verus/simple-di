package test.inject;

import mlesiewski.simpleioc.annotations.Inject;

import java.util.UUID;

public class ProtectedFieldAccessLevelsClient {

    @Inject
    protected BeanInjectedByType protectedField;

    public UUID call() {
        return protectedField.call();
    }
}
