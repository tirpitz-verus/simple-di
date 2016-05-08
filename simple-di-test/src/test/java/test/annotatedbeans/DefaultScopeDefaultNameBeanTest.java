package test.annotatedbeans;

import mlesiewski.simpledi.BeanRegistry;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class DefaultScopeDefaultNameBeanTest {

    @Test
    public void defaultScopeDefaultNameBeanIsInjectable() throws Exception {
        // given
        DefaultScopeDefaultNameBean bean = BeanRegistry.getBean(DefaultScopeDefaultNameBean.class);
        // when
        UUID actual = bean.call();
        // then
        assertThat(actual, is(DefaultScopeDefaultNameBean.CALL_UUID));
    }
}