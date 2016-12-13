package mlesiewski.simpledi.core.testutils;

import mlesiewski.simpledi.core.annotations.Inject;

public abstract class AbstractNewObjectFieldInjectionClient {

    @Inject
    Object abstractMember;

    public Object abstractMember() {
        return abstractMember;
    }
}
