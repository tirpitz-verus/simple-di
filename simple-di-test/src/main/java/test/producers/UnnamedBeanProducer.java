package test.producers;

import mlesiewski.simpledi.core.annotations.Produce;
import mlesiewski.simpledi.core.scopes.NewInstanceScope;
import test.util.TestScope;

public class UnnamedBeanProducer {

    public static final String UNNAMED = "unnamed_bean";
    public static final String SCOPED_UNNAMED = "scoped_unnamed_bean";
    public static final String NAMED_A = "named_a";
    public static final String NAMED_B = "named_b";

    public static final String UNDECLARED_EXCEPTION_BEAN = "undeclared_exception_bean";
    public static final String UNDECLARED_EXCEPTION_MESSAGE = "undeclared_exception_message";
    public static final String DECLARED_UNCHECKED_EXCEPTION_BEAN = "declared_unchecked_exception_bean";
    public static final String DECLARED_UNCHECKED_EXCEPTION_MESSAGE = "declared_unchecked_exception_message";
    public static final String DECLARED_EXCEPTION_BEAN = "declared_exception_bean";
    public static final String DECLARED_EXCEPTION_MESSAGE = "declared_exception_message";

    @Produce
    UnnamedBean produceUnnamedBean() {
        return new UnnamedBean(UNNAMED);
    }

    @Produce(name = NAMED_A)
    UnnamedBean produceBeanNamedA() {
        return new UnnamedBean(NAMED_A);
    }

    @Produce(name = NAMED_B)
    UnnamedBean produceBeanNamedB() {
        return new UnnamedBean(NAMED_B);
    }

    @Produce(scope = TestScope.NAME)
    UnnamedBean produceScopedUnnamedBean() {
        return new UnnamedBean(SCOPED_UNNAMED);
    }

    @Produce(name = UNDECLARED_EXCEPTION_BEAN, scope = NewInstanceScope.NAME)
    UnnamedBean produceUndeclaredExceptionBean() {
        throw new RuntimeException(UNDECLARED_EXCEPTION_MESSAGE);
    }

    @Produce(name = DECLARED_UNCHECKED_EXCEPTION_BEAN, scope = NewInstanceScope.NAME)
    UnnamedBean produceDeclaredUncheckedExceptionBean() throws RuntimeException {
        throw new RuntimeException(DECLARED_UNCHECKED_EXCEPTION_MESSAGE);
    }

    @Produce(name = DECLARED_EXCEPTION_BEAN, scope = NewInstanceScope.NAME)
    UnnamedBean produceDeclaredExceptionBean() throws Exception {
        throw new Exception(DECLARED_EXCEPTION_MESSAGE);
    }
}
