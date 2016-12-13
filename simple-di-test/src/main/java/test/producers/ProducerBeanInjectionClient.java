package test.producers;

import mlesiewski.simpledi.core.annotations.Inject;

public class ProducerBeanInjectionClient {

    @Inject(name = ProducerBean.PRODUCER_NAME)
    ProducerBean producerBean;
}
