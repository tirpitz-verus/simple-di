package mlesiewski.simpledi.model;

import java.util.*;

/**
 * Collects {@link GeneratedCode}.
 */
public class GeneratedCodeCollector {

    /** store for registrable classes */
    private LinkedHashMap<String, GeneratedCode> generated = new LinkedHashMap<>();

    /** bean providers */
    private HashMap<BeanName, BeanProviderEntity> providers = new HashMap<>();

    /**
     * Collects new {@link BeanProviderEntity} instance.
     * It cannot be overwritten.
     * Provider once inserted cannot be overwritten.
     *
     * @param provider instance to collect
     */
    public void registrable(BeanProviderEntity provider) {
        if (!providers.containsKey(provider.beanName())) {
            providers.put(provider.beanName(), provider);
            generated.put(provider.typeName(), provider);
        }
    }

    /** @return a collection of {@link GeneratedCode} instances */
    public Collection<GeneratedCode> registrable() {
        return new ArrayList<>(generated.values());
    }
}
