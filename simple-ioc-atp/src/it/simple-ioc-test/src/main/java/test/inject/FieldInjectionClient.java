package test.inject;

import mlesiewski.simpleioc.scopes.Scope;
import mlesiewski.simpleioc.annotations.Inject;

public class FieldInjectionClient {

    @Inject(name = "bean injected by name")
    BeanInjectedByName defaultScopeByName;

    @Inject()
    BeanInjectedByType defaultScopeByType;

    @Inject(name = "bean injected by name", scope = Scope.APP_SCOPE)
    BeanInjectedByName scopedByName;

    @Inject(scope = Scope.APP_SCOPE)
    BeanInjectedByType scopedByType;
}
