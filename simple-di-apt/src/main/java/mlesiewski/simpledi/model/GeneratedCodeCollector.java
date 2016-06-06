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
        String key = generatedCode.typeName();
        if (!generated.containsKey(key)) {
            generated.put(key, generatedCode);
        }
    }

    /** @return a collection of {@link GeneratedCode} instances */
    public Collection<GeneratedCode> registrable() {
        return new ArrayList<>(generated.values());
    }
}
