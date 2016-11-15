package test.inject;

import mlesiewski.simpledi.annotations.Inject;
import test.util.TestScope;

public class ConstructorInjectionClient {

    final BeanInjectedByName defaultScopeByName;
    final BeanInjectedByType defaultScopeByType;
    final BeanInjectedByName scopedByName;
    final BeanInjectedByType scopedByType;

    public ConstructorInjectionClient(
            @Inject(name = "bean_injected_by_name") BeanInjectedByName defaultScopeByName,
            @Inject BeanInjectedByType defaultScopeByType,
            @Inject(name = "test_bean_injected_by_name", scope = TestScope.NAME) BeanInjectedByName scopedByName,
            @Inject(scope = TestScope.NAME) BeanInjectedByType scopedByType) {
        this.defaultScopeByName = defaultScopeByName;
        this.defaultScopeByType = defaultScopeByType;
        this.scopedByName = scopedByName;
        this.scopedByType = scopedByType;
    }
}
