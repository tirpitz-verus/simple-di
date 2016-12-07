package mlesiewski.simpledi.testutils;

import mlesiewski.simpledi.annotations.Inject;

public abstract class AbstractNewObjectFieldInjectionClient {

    @Inject
    Object abstractMember;

    public Object abstractMember() {
        return abstractMember;
    }
}
