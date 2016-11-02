package mlesiewski.simpledi.testutils;

import mlesiewski.simpledi.BeanProvider;

public class NewObjectProvider implements BeanProvider<Object> {

    public static final NewObjectProvider NEW_OBJECT_PROVIDER = new NewObjectProvider();

    public boolean provideCalled;
    public boolean softDependenciesSet;

    @Override
    public Object provide() {
        provideCalled = true;
        return new Object();
    }

    @Override
    public void setSoftDependencies(Object newInstance) {
        softDependenciesSet = true;
    }
}