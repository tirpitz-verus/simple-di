package mlesiewski.simpledi.core.testutils;

import mlesiewski.simpledi.core.annotations.Inject;

public class NewObjectFinalFieldInjectionClient {

    @Inject
    final Object member = null;

    public Object getMember() {
        return member;
    }
}
