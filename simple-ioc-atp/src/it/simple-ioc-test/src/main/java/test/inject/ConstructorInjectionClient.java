package test.inject;

import mlesiewski.simpleioc.scopes.ApplicationScope;
import mlesiewski.simpleioc.annotations.Inject;

public class ConstructorInjectionClient {

    final BeanInjectedByName defaultScopeByName;
    final BeanInjectedByType defaultScopeByType;
    final BeanInjectedByName scopedByName;
    final BeanInjectedByType scopedByType;

    public ConstructorInjectionClient(
            @Inject(name = "bean injected by name") BeanInjectedByName defaultScopeByName,
            @Inject BeanInjectedByType defaultScopeByType,
            @Inject(name = "bean injected by name", scope = ApplicationScope.NAME) BeanInjectedByName scopedByName,
            @Inject(scope = ApplicationScope.NAME) BeanInjectedByType scopedByType) {
        this.defaultScopeByName = defaultScopeByName;
        this.defaultScopeByType = defaultScopeByType;
        this.scopedByName = scopedByName;
        this.scopedByType = scopedByType;
    }
}
