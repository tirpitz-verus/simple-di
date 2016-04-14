package mlesiewski.simpleioc.model;

import mlesiewski.simpleioc.BeanRegistry;
import mlesiewski.simpleioc.annotations._Default;

import java.util.Objects;

/**
 * An entity of a bean that can be retrieved from the {@link BeanRegistry}.
 */
public class BeanEntity {

    private String name = _Default.VALUE;
    private String scope = _Default.VALUE;
    private final ClassEntity classEntity;

    public BeanEntity(ClassEntity classEntity) {
        this.classEntity = classEntity;
    }

    public BeanEntity(ClassEntity classEntity, String scope, String name) {
        this.classEntity = classEntity;
        this.scope = scope;
        this.name = name;
    }

    public String name() {
        return Objects.equals(name, _Default.VALUE) ? typeName() : name;
    }

    public void name(String name) {
        this.name = name;
    }

    public String scope() {
        return Objects.equals(scope, _Default.VALUE) ? BeanRegistry.defaultScope() : scope;
    }

    public void scope(String scope) {
        this.scope = scope;
    }

    public String packageName() {
        return classEntity.packageName();
    }

    public String simpleName() {
        return classEntity.simpleName();
    }

    public String typeName() {
        return classEntity.typeName();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private ClassEntity classEntity;
        private String scope;
        private boolean scopeSet = false;
        private String name;
        private boolean nameSet = false;

        public Builder from(ClassEntity classEntity) {
            this.classEntity = classEntity;
            return this;
        }

        public Builder withScope(String scope) {
            this.scope = scope;
            this.scopeSet = true;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            this.nameSet = true;
            return this;
        }

        public BeanEntity build() {
            BeanEntity beanEntity = new BeanEntity(classEntity);
            if (scopeSet) {
                beanEntity.scope(scope);
            }
            if (nameSet) {
                beanEntity.name(name);
            }
            return beanEntity;
        }
    }
}
