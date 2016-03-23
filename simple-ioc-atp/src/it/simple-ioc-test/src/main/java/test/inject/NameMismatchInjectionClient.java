package test.inject;

import mlesiewski.simpleioc.annotations.Inject;

public class NameMismatchInjectionClient {

    @Inject(name = "bean injected by name")
    BeanInjectedByType bean;
}
