package test.annotatedbeans;

import mlesiewski.simpledi.core.BeanRegistry;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.UUID;

public class DefaultScopeNamedBeanTest {

    @Test
    public void defaultScopeNamedBeanIsInjectable() throws Exception {
        // given
        DefaultScopeNamedBean bean = BeanRegistry.getBean("default_scope_named_bean");
        // when
        UUID actual = bean.call();
        // then
        assertThat(actual, is(DefaultScopeNamedBean.CALL_UUID));
    }
}