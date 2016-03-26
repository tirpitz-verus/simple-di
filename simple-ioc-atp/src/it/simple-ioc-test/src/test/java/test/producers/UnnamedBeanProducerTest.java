package test.producers;

import mlesiewski.simpleioc.BeanRegistry;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnnamedBeanProducerTest {

    @Test
    public void producesUnnamedBean() throws Exception {
        // when
        InjectionClient client = BeanRegistry.getBean(InjectionClient.class);
        // then
        assertEquals(UnnamedBeanProducer.UNNAMED, client.unnamedBean.name);
    }

    @Test
    public void producesBeanNamedA() throws Exception {
        // when
        InjectionClient client = BeanRegistry.getBean(InjectionClient.class);
        // then
        assertEquals(UnnamedBeanProducer.NAMED_A, client.namedBeanA.name);
    }

    @Test
    public void producesBeanNamedB() throws Exception {
        // when
        InjectionClient client = BeanRegistry.getBean(InjectionClient.class);
        // then
        assertEquals(UnnamedBeanProducer.NAMED_B, client.namedBeanB.name);
    }

    @Test
    public void producesScopedUnnamedBean() throws Exception {
        // when
        InjectionClient client = BeanRegistry.getBean(InjectionClient.class);
        // then
        assertEquals(UnnamedBeanProducer.SCOPED_UNNAMED, client.scopedUnnamedBean.name);
    }
}