package mlesiewski.simpledi.model;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;

class GeneratedOrderMatcher extends BaseMatcher<GeneratedCodeCollector> {

    private final List<String> expected;
    private Object matched;
    private List<String> generated;

    private GeneratedOrderMatcher(List<String> expected) {
        this.expected = expected;
    }

    @Override
    public boolean matches(Object o) {
        matched = o;
        if (!matchingGeneratedCodeCollector()) {
            return false;
        }
        GeneratedCodeCollector collector = (GeneratedCodeCollector) o;
        Collection<GeneratedCode> registrable = collector.registrable();
        generated = registrable.stream().map(GeneratedCode::typeName).collect(Collectors.toList());
        return is(expected).matches(generated);
    }

    private boolean matchingGeneratedCodeCollector() {
        return matched instanceof GeneratedCodeCollector;
    }

    @Override
    public void describeTo(Description description) {
        if (!matchingGeneratedCodeCollector()) {
            description.appendText("matched value ").appendValue(matched).appendText(" is not an instance of class ").appendValue(GeneratedCodeCollector.class);
        } else {
            description.appendText("expected ").appendValue(expected).appendText(" but generated ").appendValue(generated);
        }
    }

    static GeneratedOrderMatcher returnsRegistrableInOrder(BeanEntity... entities) {
        List<String> expected = Stream.of(entities)
                .map(e -> e.typeName() + "Provider")
                .collect(Collectors.toList());
        return new GeneratedOrderMatcher(expected);
    }
}
