package test.annotatedbeans;

import mlesiewski.simpledi.core.BeanRegistry;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.UUID;

public class ScopedDefaultNamedBeanTest {

    @Test
    public void scopedDefaultNamedBeanIsInjectable() throws Exception {
        // given
        ScopedDefaultNamedBean bean = BeanRegistry.getBean(ScopedDefaultNamedBean.class);
        // when
        UUID actual = bean.call();
        // then
        assertThat(actual, is(ScopedDefaultNamedBean.CALL_UUID));
    }
}