package test.annotatedbeans;

import mlesiewski.simpleioc.BeanRegistry;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class ScopedNamedBeanTest {

    @Test
    public void scopedNamedBeanIsInjectable() throws Exception {
        // given
        ScopedNamedBean bean = BeanRegistry.getBean("scoped named bean");
        // when
        UUID actual = bean.call();
        // then
        assertEquals(ScopedNamedBean.CALL_UUID, actual);
    }
}