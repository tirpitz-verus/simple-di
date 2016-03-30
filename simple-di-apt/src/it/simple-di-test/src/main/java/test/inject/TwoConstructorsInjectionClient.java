package test.inject;

import mlesiewski.simpleioc.annotations.Inject;

public class TwoConstructorsInjectionClient {

    final BeanInjectedByType bean1;
    BeanInjectedByType bean2;

    public TwoConstructorsInjectionClient(@Inject BeanInjectedByType bean) {
        this.bean1 = bean;
    }

    public TwoConstructorsInjectionClient(@Inject BeanInjectedByType bean1, @Inject BeanInjectedByType bean2) {
        this.bean1 = bean1;
        this.bean2 = bean2;
    }
}
