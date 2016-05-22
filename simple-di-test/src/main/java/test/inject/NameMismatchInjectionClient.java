package test.inject;

import mlesiewski.simpledi.annotations.Inject;

public class NameMismatchInjectionClient {

    @Inject(name = "bean_injected_by_name")
    BeanInjectedByType bean;
}
