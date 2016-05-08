package test.inject;

import mlesiewski.simpledi.annotations.Inject;

public class ConstructorInjectionClient2 {

    BeanInjectedByType bean;

    @Inject
    public ConstructorInjectionClient2(BeanInjectedByType bean) {
        this.bean = bean;
    }
}
