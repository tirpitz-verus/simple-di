package test.inject;

import mlesiewski.simpledi.core.annotations.Inject;
import test.util.TestScope;

public class FieldInjectionClient extends AbstractFieldInjectionClient {

    @Inject(name = "bean_injected_by_name")
    BeanInjectedByName defaultScopeByName;

    @Inject
    BeanInjectedByType defaultScopeByType;

    @Inject(name = "test_bean_injected_by_name", scope = TestScope.NAME)
    BeanInjectedByName scopedByName;

    @Inject(scope = TestScope.NAME)
    BeanInjectedByType scopedByType;
}

abstract class AbstractFieldInjectionClient {

    @Inject
    BeanInjectedByType defaultScopeByTypeInSuperclass;
}