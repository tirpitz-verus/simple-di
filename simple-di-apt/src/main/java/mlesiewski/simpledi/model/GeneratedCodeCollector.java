package mlesiewski.simpledi.model;

import mlesiewski.simpledi.SimpleDiAptException;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Collects {@link GeneratedCode}.
 */
public class GeneratedCodeCollector {

    /** bean providers */
    private final HashMap<BeanName, BeanProviderEntity> providers = new HashMap<>();

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
        }
    }

    /** @return true if a {@link BeanProviderEntity} for the {@link BeanName} provided was registered*/
    public boolean hasBean(BeanName beanName) {
        return providers.containsKey(beanName);
    }

    /** @return {@link BeanEntity} that is beaning provided by the {@link BeanProviderEntity} registered under the {@link BeanName} provided */
    public BeanEntity getBean(BeanName beanName) {
        return providers.get(beanName).beanEntity();
    }

    /** @return a collection of {@link GeneratedCode} instances */
    public Collection<GeneratedCode> registrable() {
        HashMap<BeanName, Node> nodes = new HashMap<>(providers.size());
        for (BeanProviderEntity providerEntity : providers.values()) {
            new Node(providerEntity, nodes);
        }

        LinkedList<Node> output = new LinkedList<>();
        for (Node current : nodes.values()) {
            visit(current, output);
        }

        return output.stream().map(Node::provider).collect(Collectors.toList());
    }

    private void visit(Node current, LinkedList<Node> output) {
        if (current.mark) {
            throw new SimpleDiAptException("cycle found in entity hard dependency graph - first to occur on bean '" + current.provider.beanName() + "'");
        }
        if (!current.visited) {
            current.mark = true;
            current.forEachDependant(node -> visit(node, output));
            current.visited = true;
            current.mark = false;
            output.addFirst(current);
        }
    }

    private class Node {
        BeanProviderEntity provider;
        private final HashMap<BeanName, Node> nodes;
        boolean mark = false;
        boolean visited = false;

        Node(BeanProviderEntity provider, HashMap<BeanName, Node> nodes) {
            this.provider = provider;
            this.nodes = nodes;
            nodes.put(provider.beanName(), this);
        }

        void forEachDependant(Consumer<Node> consumer) {
            nodes.values().stream()
                    .filter(this::dependsOn)
                    .forEach(consumer);
        }

        private boolean dependsOn(Node node) {
            return node.provider.hardDependencies().contains(provider.beanName());
        }

        BeanProviderEntity provider() {
            return provider;
        }

        @Override
        public String toString() {
            return "Node{" + provider.typeName() + '}';
        }
    }
}