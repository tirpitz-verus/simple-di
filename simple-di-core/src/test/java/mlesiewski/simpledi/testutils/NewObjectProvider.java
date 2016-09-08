package mlesiewski.simpledi.testutils;

import mlesiewski.simpledi.BeanProvider;

public class NewObjectProvider implements BeanProvider<Object> {

    public static final NewObjectProvider NEW_OBJECT_PROVIDER = new NewObjectProvider();

    @Override
    public Object provide() {
        return new Object();
    }

    @Override
    public void setSoftDependencies(Object newInstance) {
        // empty
    }
}