package mlesiewski.simpleioc;

import mlesiewski.simpleioc.annotations.Inject;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

public class SimpleInjectionCase {

    @Test
    public void testName() throws Exception {
        // when
        Parent parent = BeanRegistry.getBean(Parent.class);
        // then
        assertThat(parent, is(not(nullValue())));
        assertThat(parent.child, is(not(nullValue())));
    }

    public static class Parent {

        @Inject
        Child child;
    }

    public static class ParentBeanProvider implements BeanProvider<Parent> {

        static {
            BeanRegistry.register(new ParentBeanProvider(), Parent.class);
        }

        Parent bean;

        @Override
        public Parent provide() {
            if (bean == null) {
                bean = new Parent();
                bean.child = BeanRegistry.getBean(Child.class);
            }
            return bean;
        }

        @Override
        public void scopeEnded() {

        }
    }

    public static class Child {

    }

    public static class ChildBeanProvider implements BeanProvider<Child> {

        static {
            BeanRegistry.register(new ChildBeanProvider(), Child.class);
        }

        Child bean = new Child();

        @Override
        public Child provide() {
            return bean;
        }

        @Override
        public void scopeEnded() {

        }
    }
}
