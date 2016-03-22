package test.annotatedbeans;

import mlesiewski.simpleioc.BeanRegistry;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class ScopedDefaultNamedBeanTest {

    @Test
    public void testCall() throws Exception {
        // given
        ScopedDefaultNamedBean bean = BeanRegistry.getBean(ScopedDefaultNamedBean.class);
        // when
        UUID actual = bean.call();
        // then
        assertEquals(ScopedDefaultNamedBean.CALL_UUID, actual);
    }
}