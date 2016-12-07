package mlesiewski.simpledi.testutils;

import mlesiewski.simpledi.annotations.Inject;

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
