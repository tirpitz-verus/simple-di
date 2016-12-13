package test.annotatedbeans;

import mlesiewski.simpledi.core.BeanRegistry;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.UUID;

public class ScopedNamedBeanTest {

    @Test
    public void scopedNamedBeanIsInjectable() throws Exception {
        // given
        ScopedNamedBean bean = BeanRegistry.getBean("scoped_named_bean");
        // when
        UUID actual = bean.call();
        // then
        assertThat(actual, is(ScopedNamedBean.CALL_UUID));
    }
}