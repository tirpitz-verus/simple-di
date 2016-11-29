package test.producers;

import mlesiewski.simpledi.annotations.Inject;

public class ProducerBeanInjectionClient {

    @Inject(name = ProducerBean.PRODUCER_NAME)
    ProducerBean producerBean;
}
