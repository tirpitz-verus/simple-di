package test.inject;

import mlesiewski.simpledi.annotations.Inject;

import java.util.UUID;

public class PrivateFieldAccessLevelsClient {

    @Inject
    private BeanInjectedByType privateField;

    public UUID call() {
        return privateField.call();
    }

    public void setPrivateField(BeanInjectedByType privateField) {
        this.privateField = privateField;
    }
}
