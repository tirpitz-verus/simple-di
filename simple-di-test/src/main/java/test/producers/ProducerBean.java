package test.producers;

import mlesiewski.simpledi.core.annotations.Bean;
import mlesiewski.simpledi.core.annotations.Produce;
import test.util.TestScope;

@Bean(name = ProducerBean.PRODUCER_NAME, scope = TestScope.NAME)
public class ProducerBean {

    public static final String PRODUCER_NAME = "producer_bean";

    @Produce(name = ProducedBean.PRODUCED_NAME)
    public ProducedBean produce() {
        return new ProducedBean();
    }
}
