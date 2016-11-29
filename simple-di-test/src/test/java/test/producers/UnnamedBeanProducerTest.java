package test.producers;

import mlesiewski.simpledi.BeanRegistry;
import mlesiewski.simpledi.SimpleDiException;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class UnnamedBeanProducerTest {

    @Test
    public void producesUnnamedBean() throws Exception {
        // when
        UnnamedBeanInjectionClient client = BeanRegistry.getBean(UnnamedBeanInjectionClient.class);
        // then
        assertThat(client.unnamedBean.name, is(UnnamedBeanProducer.UNNAMED));
    }

    @Test
    public void producesBeanNamedA() throws Exception {
        // when
        UnnamedBeanInjectionClient client = BeanRegistry.getBean(UnnamedBeanInjectionClient.class);
        // then
        assertThat(client.namedBeanA.name, is(UnnamedBeanProducer.NAMED_A));
    }

    @Test
    public void producesBeanNamedB() throws Exception {
        // when
        UnnamedBeanInjectionClient client = BeanRegistry.getBean(UnnamedBeanInjectionClient.class);
        // then
        assertThat(client.namedBeanB.name, is(UnnamedBeanProducer.NAMED_B));
    }

    @Test
    public void producesScopedUnnamedBean() throws Exception {
        // when
        UnnamedBeanInjectionClient client = BeanRegistry.getBean(UnnamedBeanInjectionClient.class);
        // then
        assertThat(client.scopedUnnamedBean.name, is(UnnamedBeanProducer.SCOPED_UNNAMED));
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = UnnamedBeanProducer.UNDECLARED_EXCEPTION_MESSAGE)
    public void doesNotWrapUndeclaredException() throws Exception {
        // when
        BeanRegistry.getBean(UnnamedBeanProducer.UNDECLARED_EXCEPTION_BEAN);
        // then - exception
    }

    @Test(expectedExceptions = SimpleDiException.class, expectedExceptionsMessageRegExp = ".*" + UnnamedBeanProducer.DECLARED_EXCEPTION_MESSAGE + ".*")
    public void wrapsDeclaredException() throws Exception {
        // when
        BeanRegistry.getBean(UnnamedBeanProducer.DECLARED_EXCEPTION_BEAN);
        // then - exception
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = UnnamedBeanProducer.DECLARED_UNCHECKED_EXCEPTION_MESSAGE)
    public void doesNotWrapDeclaredUncheckedException() throws Exception {
        // when
        BeanRegistry.getBean(UnnamedBeanProducer.DECLARED_UNCHECKED_EXCEPTION_BEAN);
        // then - exception
    }
}