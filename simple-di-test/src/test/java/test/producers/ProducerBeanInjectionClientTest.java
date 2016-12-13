package test.producers;

import mlesiewski.simpledi.core.BeanRegistry;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProducerBeanInjectionClientTest {

    @Test
    public void producerBeanCanBeABeanItself() throws Exception {
        // when
        ProducerBeanInjectionClient client = BeanRegistry.getBean(ProducerBeanInjectionClient.class);
        ProducedBean produced = client.producerBean.produce();
        // then
        assertThat(produced, is(notNullValue()));
    }
}