package test.inject;

import mlesiewski.simpledi.annotations.Inject;

import java.util.UUID;

public class ProtectedFieldAccessLevelsClient {

    @Inject
    protected BeanInjectedByType protectedField;

    public UUID call() {
        return protectedField.call();
    }

    public void protectedField(BeanInjectedByType protectedField) {
        this.protectedField = protectedField;
    }
}
