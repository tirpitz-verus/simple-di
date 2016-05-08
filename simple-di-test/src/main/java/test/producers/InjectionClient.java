package test.producers;

import mlesiewski.simpledi.scopes.ApplicationScope;
import mlesiewski.simpledi.annotations.Inject;

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
