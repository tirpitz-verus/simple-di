package test.producers;

import mlesiewski.simpledi.core.annotations.Bean;
import test.util.TestScope;

@Bean(name = ProducedBean.PRODUCED_NAME, scope = TestScope.NAME)
public class ProducedBean {

    public static final String PRODUCED_NAME = "produced_bean";
}
