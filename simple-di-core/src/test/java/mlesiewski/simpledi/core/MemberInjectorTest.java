package mlesiewski.simpledi.core;

import mlesiewski.simpledi.core.testutils.NewObjectFieldInjectionClient;
import mlesiewski.simpledi.core.testutils.NewObjectFinalFieldInjectionClient;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static mlesiewski.simpledi.core.testutils.NewObjectProvider.NEW_OBJECT_PROVIDER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MemberInjectorTest {

    @BeforeMethod
    public void setUp() throws Exception {
        BeanRegistry.init();
        BeanRegistry.register(NEW_OBJECT_PROVIDER, Object.class);
    }

    @Test
    public void injectsAnnotatedMembersByField() throws Exception {
        // given
        NewObjectFieldInjectionClient injectionClient = new NewObjectFieldInjectionClient();
        // when
        MemberInjector.injectMembersInto(injectionClient);
        // then
        assertThat(injectionClient.member(), is(not(nullValue())));
        assertThat(injectionClient.staticMember(), is(nullValue()));
        assertThat(injectionClient.abstractMember(), is(not(nullValue())));
    }

    @Test
    public void doesNotCauseErrorWhenInjectingToABeanWithoutAnyAnnotatedMembers() throws Exception {
        // when
        MemberInjector.injectMembersInto(new Object());
        // then - no error
    }

    @Test(expectedExceptions = SimpleDiException.class, expectedExceptionsMessageRegExp = ".*final.*")
    public void throwsExceptionOnFinalAnnotatedMember() throws Exception {
        // given
        NewObjectFinalFieldInjectionClient injectionClient = new NewObjectFinalFieldInjectionClient();
        // when
        MemberInjector.injectMembersInto(injectionClient);
        // then - exception
    }
}