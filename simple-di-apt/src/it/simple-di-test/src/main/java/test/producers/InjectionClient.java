package test.producers;

import mlesiewski.simpleioc.scopes.ApplicationScope;
import mlesiewski.simpleioc.annotations.Inject;

public class InjectionClient {

    @Inject
    UnnamedBean unnamedBean;

    @Inject(name = UnnamedBeanProducer.NAMED_B)
    UnnamedBean namedBeanA;

    @Inject(name = UnnamedBeanProducer.NAMED_B)
    UnnamedBean namedBeanB;

    @Inject(scope = ApplicationScope.NAME)
    UnnamedBean scopedUnnamedBean;
}
