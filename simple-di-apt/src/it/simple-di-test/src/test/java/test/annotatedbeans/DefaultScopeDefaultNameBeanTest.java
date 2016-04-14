package test.annotatedbeans;

import mlesiewski.simpledi.BeanRegistry;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class DefaultScopeDefaultNameBeanTest {

    @Test
    public void defaultScopeDefaultNameBeanIsInjectable() throws Exception {
        // given
        DefaultScopeDefaultNameBean bean = BeanRegistry.getBean(DefaultScopeDefaultNameBean.class);
        // when
        UUID actual = bean.call();
        // then
        assertEquals(DefaultScopeDefaultNameBean.CALL_UUID, actual);
    }
}