package test.producers;

import mlesiewski.simpledi.annotations.Inject;
import test.util.TestScope;

public class InjectionClient {

    @Inject
    UnnamedBean unnamedBean;

    @Inject(name = UnnamedBeanProducer.NAMED_A)
    UnnamedBean namedBeanA;

    @Inject(name = UnnamedBeanProducer.NAMED_B)
    UnnamedBean namedBeanB;

    @Inject(scope = TestScope.NAME)
    UnnamedBean scopedUnnamedBean;
}
