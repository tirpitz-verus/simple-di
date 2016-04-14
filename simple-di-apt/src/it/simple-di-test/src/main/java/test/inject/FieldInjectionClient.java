package test.inject;

import mlesiewski.simpledi.scopes.ApplicationScope;
import mlesiewski.simpledi.annotations.Inject;

public class FieldInjectionClient {

    @Inject(name = "bean injected by name")
    BeanInjectedByName defaultScopeByName;

    @Inject()
    BeanInjectedByType defaultScopeByType;

    @Inject(name = "bean injected by name", scope = ApplicationScope.NAME)
    BeanInjectedByName scopedByName;

    @Inject(scope = ApplicationScope.NAME)
    BeanInjectedByType scopedByType;
}
