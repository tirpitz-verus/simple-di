package mlesiewski.simpledi.testutils;

import mlesiewski.simpledi.annotations.Inject;

public class NewObjectFinalFieldInjectionClient {

    @Inject
    final Object member = null;

    public Object getMember() {
        return member;
    }
}
