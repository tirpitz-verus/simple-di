package mlesiewski.simpledi.model;

import java.util.Objects;

/** immutable class */
public class BeanName {

    private final String name;
    private final String scope;

    public BeanName(String name, String scope) {
        this.name = name;
        this.scope = scope;
    }

    public String name() {
        return name;
    }

    public String scope() {
        return scope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BeanName)) {
            return false;
        }
        BeanName beanName = (BeanName) o;
        return Objects.equals(name(), beanName.name()) && Objects.equals(scope(), beanName.scope());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name(), scope());
    }
}
