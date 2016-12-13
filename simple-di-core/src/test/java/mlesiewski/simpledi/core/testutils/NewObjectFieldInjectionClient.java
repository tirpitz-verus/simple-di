package mlesiewski.simpledi.core.testutils;

import mlesiewski.simpledi.core.annotations.Inject;

public class NewObjectFieldInjectionClient extends AbstractNewObjectFieldInjectionClient {

    @Inject
    private Object member;

    @Inject
    static Object staticMember;

    public Object member() {
        return member;
    }

    public Object staticMember() {
        return staticMember;
    }
}
