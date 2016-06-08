package test.inject;

import mlesiewski.simpledi.annotations.Inject;
import test.util.TestScope;

public class FieldInjectionClient {

    @Inject(name = "bean_injected_by_name")
    BeanInjectedByName defaultScopeByName;

    @Inject()
    BeanInjectedByType defaultScopeByType;

    @Inject(name = "bean_injected_by_name", scope = TestScope.NAME)
    BeanInjectedByName scopedByName;

    @Inject(scope = TestScope.NAME)
    BeanInjectedByType scopedByType;
}