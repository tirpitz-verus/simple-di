package mlesiewski.simpledi.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Collects {@link GeneratedCode}.
 */
public class GeneratedCodeCollector {

    /** store for registrable classes */
    private LinkedHashMap<String, GeneratedCode> generated = new LinkedHashMap<>();

    /**
     * Collects new {@link GeneratedCode} instance. It can be overwritten.
     *
     * @param generatedCode instance to collect
     */
    public void registrable(GeneratedCode generatedCode) {
        generated.put(generatedCode.typeName(), generatedCode);
    }

    /** @return a collection of {@link GeneratedCode} instances */
    public Collection<GeneratedCode> registrable() {
        return new ArrayList<>(generated.values());
    }
}
