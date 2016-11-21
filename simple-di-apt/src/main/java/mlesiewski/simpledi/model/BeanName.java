package mlesiewski.simpledi.model;

import mlesiewski.simpledi.BeanRegistry;
import mlesiewski.simpledi.annotations.Bean;
import mlesiewski.simpledi.annotations.Inject;
import mlesiewski.simpledi.annotations._Default;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Objects;

import static mlesiewski.simpledi.BeanRegistry.defaultScope;

/** immutable class that represents a {@link BeanEntity} identity */
public final class BeanName {

    private final String nameFromAnnotation;
    private final String scopeFromAnnotation;
    private final String nameFromType;

    private final String name;
    private final String scope;

    public BeanName(String nameFromAnnotation, String scopeFromAnnotation, String nameFromType) {
        this.nameFromAnnotation = nameFromAnnotation;
        this.scopeFromAnnotation = scopeFromAnnotation;
        this.nameFromType = nameFromType;

        this.name = nameIsDefault() ? nameFromType : nameFromAnnotation;
        this.scope = scopeIsDefault() ? defaultScope() : scopeFromAnnotation;
    }

    public BeanName(Bean annotation, DeclaredType declaredType) {
        this(annotation.name(), annotation.scope(), declaredType.toString());
    }

    public BeanName(DeclaredType declaredType) {
        this(_Default.VALUE, _Default.VALUE, declaredType.toString());
    }

    public BeanName(Inject annotation, DeclaredType declaredType) {
        this(annotation.name(), annotation.scope(), declaredType.toString());
    }

    public String nameFromAnnotation() {
        return nameFromAnnotation;
    }

    public String scopeFromAnnotation() {
        return scopeFromAnnotation;
    }

    public String nameFromType() {
        return nameFromType;
    }

    public String name() {
        return nameIsDefault() ? nameFromType : nameFromAnnotation;
    }

    public boolean nameIsDefault() {
        return isDefault(nameFromAnnotation);
    }

    public String scope() {
        return scopeIsDefault() ? BeanRegistry.defaultScope() : scopeFromAnnotation;
    }

    public boolean scopeIsDefault() {
        return isDefault(scopeFromAnnotation);
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

    private boolean isDefault(String name) {
        return _Default.VALUE.equals(name);
    }

}
