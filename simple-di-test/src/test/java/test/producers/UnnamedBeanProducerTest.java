package test.producers;

import mlesiewski.simpledi.BeanRegistry;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class UnnamedBeanProducerTest {

    @Test
    public void producesUnnamedBean() throws Exception {
        // when
        InjectionClient client = BeanRegistry.getBean(InjectionClient.class);
        // then
        assertThat(client.unnamedBean.name, is(UnnamedBeanProducer.UNNAMED));
    }

    @Test
    public void producesBeanNamedA() throws Exception {
        // when
        InjectionClient client = BeanRegistry.getBean(InjectionClient.class);
        // then
        assertThat(client.namedBeanA.name, is(UnnamedBeanProducer.NAMED_A));
    }

    @Test
    public void producesBeanNamedB() throws Exception {
        // when
        InjectionClient client = BeanRegistry.getBean(InjectionClient.class);
        // then
        assertThat(client.namedBeanB.name, is(UnnamedBeanProducer.NAMED_B));
    }

    @Test
    public void producesScopedUnnamedBean() throws Exception {
        // when
        InjectionClient client = BeanRegistry.getBean(InjectionClient.class);
        // then
        assertThat(client.scopedUnnamedBean.name, is(UnnamedBeanProducer.SCOPED_UNNAMED));
    }
}