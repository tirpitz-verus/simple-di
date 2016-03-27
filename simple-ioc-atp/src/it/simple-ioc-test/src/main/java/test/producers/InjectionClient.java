package test.producers;

import mlesiewski.simpleioc.scopes.Scope;
import mlesiewski.simpleioc.annotations.Inject;

public class InjectionClient {

    @Inject
    UnnamedBean unnamedBean;

    @Inject(name = UnnamedBeanProducer.NAMED_B)
    UnnamedBean namedBeanA;

    @Inject(name = UnnamedBeanProducer.NAMED_B)
    UnnamedBean namedBeanB;

    @Inject(scope = Scope.APP_SCOPE)
    UnnamedBean scopedUnnamedBean;
}
