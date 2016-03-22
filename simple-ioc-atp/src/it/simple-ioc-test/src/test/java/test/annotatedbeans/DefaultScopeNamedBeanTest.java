package test.annotatedbeans;

import mlesiewski.simpleioc.BeanRegistry;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class DefaultScopeNamedBeanTest {

    @Test
    public void testCall() throws Exception {
        // given
        DefaultScopeNamedBean bean = BeanRegistry.getBean("default scope named bean");
        // when
        UUID actual = bean.call();
        // then
        assertEquals(DefaultScopeNamedBean.CALL_UUID, actual);
    }
}