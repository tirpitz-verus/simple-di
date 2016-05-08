package test.annotatedbeans;

import mlesiewski.simpledi.BeanRegistry;
import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.UUID;

public class DefaultScopeNamedBeanTest {

    @Test
    public void defaultScopeNamedBeanIsInjectable() throws Exception {
        // given
        DefaultScopeNamedBean bean = BeanRegistry.getBean("default scope named bean");
        // when
        UUID actual = bean.call();
        // then
        assertThat(actual, is(DefaultScopeNamedBean.CALL_UUID));
    }
}