package test.inject;

import mlesiewski.simpledi.annotations.Inject;

import java.util.UUID;

public class PublicFieldAccessLevelsClient {

    @Inject
    public BeanInjectedByType publicField;

    public UUID call() {
        return publicField.call();
    }
}
