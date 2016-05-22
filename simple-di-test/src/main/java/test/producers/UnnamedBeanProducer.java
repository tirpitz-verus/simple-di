package test.producers;

import mlesiewski.simpledi.annotations.Produce;
import test.util.TestScope;

public class UnnamedBeanProducer {

    public static final String UNNAMED = "unnamed_bean";
    public static final String SCOPED_UNNAMED = "scoped_unnamed_bean";
    public static final String NAMED_A = "named_a";
    public static final String NAMED_B = "named_b";

    @Produce
    public UnnamedBean produceUnnamedBean() {
        return new UnnamedBean(UNNAMED);
    }

    @Produce(name = NAMED_A)
    public UnnamedBean produceBeanNamedA() {
        return new UnnamedBean(NAMED_A);
    }

    @Produce(name = NAMED_B)
    public UnnamedBean produceBeanNamedB() {
        return new UnnamedBean(NAMED_B);
    }

    @Produce(scope = TestScope.NAME)
    UnnamedBean produceScopedUnnamedBean() {
        return new UnnamedBean(SCOPED_UNNAMED);
    }
}
