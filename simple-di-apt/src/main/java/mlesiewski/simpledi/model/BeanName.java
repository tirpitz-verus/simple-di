package mlesiewski.simpledi.model;

import mlesiewski.simpledi.annotations._Default;
import mlesiewski.simpledi.scopes.Scope;

import javax.lang.model.type.TypeMirror;
import java.util.Objects;

/** immutable class */
public final class BeanName {

    private final String name;
    private final String scope;

    public BeanName(String name, String scope) {
        this.name = name;
        this.scope = scope;
    }

    public BeanName(String name) {
        this.name = name;
        this.scope = _Default.VALUE;
    }

    public String name() {
        return name;
    }

    public boolean nameIsDefault() {
        return _Default.VALUE.equals(name);
    }

    public String scope() {
        return scope;
    }

    public boolean scopeIsDefault() {
        return _Default.VALUE.equals(scope);
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

    @Override
    public String toString() {
        return String.format("BeanName{%s, %s}", name(), scope());
    }
}
