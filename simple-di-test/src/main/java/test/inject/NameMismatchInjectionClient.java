package test.inject;

import mlesiewski.simpledi.annotations.Inject;

public class NameMismatchInjectionClient {

    @Inject(name = "bean injected by name")
    BeanInjectedByType bean;
}
