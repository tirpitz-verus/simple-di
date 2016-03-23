package test.inject;

import mlesiewski.simpleioc.Scope;
import mlesiewski.simpleioc.annotations.Inject;

public class ConstructorInjectionClient {

    final BeanInjectedByName defaultScopeByName;
    final BeanInjectedByType defaultScopeByType;
    final BeanInjectedByName scopedByName;
    final BeanInjectedByType scopedByType;

    public ConstructorInjectionClient(
            @Inject(name = "bean injected by name") BeanInjectedByName defaultScopeByName,
            @Inject BeanInjectedByType defaultScopeByType,
            @Inject(name = "bean injected by name", scope = Scope.APP_SCOPE) BeanInjectedByName scopedByName,
            @Inject(scope = Scope.APP_SCOPE) BeanInjectedByType scopedByType) {
        this.defaultScopeByName = defaultScopeByName;
        this.defaultScopeByType = defaultScopeByType;
        this.scopedByName = scopedByName;
        this.scopedByType = scopedByType;
    }
}
