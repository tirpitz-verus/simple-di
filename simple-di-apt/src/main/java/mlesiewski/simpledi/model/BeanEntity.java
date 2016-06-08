package mlesiewski.simpledi.model;

import mlesiewski.simpledi.BeanRegistry;
import mlesiewski.simpledi.annotations._Default;

import java.util.Objects;

/**
 * An entity of a bean that can be retrieved from the {@link BeanRegistry}.
 */
public class BeanEntity {

    /** Name of this bean - might be assigned from annotation or have a default value. */
    private String name = _Default.VALUE;
    /** Scope of this bean - might be assigned from annotation or have a default value. */
    private String scope = _Default.VALUE;
    /** Java class denoted by this bean. */
    private final ClassEntity classEntity;

    /**
     * Creates new entity from a Java class with default scope and name.
     * <br>
     * {@link Builder} uses this constructor.
     *
     * @param classEntity Java class denoted by this bean.
     */
    public BeanEntity(ClassEntity classEntity) {
        this.classEntity = classEntity;
    }

    /**
     * @return Name of this bean that was assigned to it or a default value (type name of the Java class denoted by this bean).
     */
    public String name() {
        return defaultName() ? typeName() : name;
    }

    /**
     * @return {@code true} if name was not assigned to this bean.
     */
    public boolean defaultName() {
        return Objects.equals(name, _Default.VALUE);
    }

    /**
     * @param name Name of this bean.
     */
    public void name(String name) {
        this.name = name;
    }

    /**
     * @return Scope of this bean that was assigned to it or a default value ({@link BeanRegistry#defaultScope()}).
     */
    public String scope() {
        return Objects.equals(scope, _Default.VALUE) ? BeanRegistry.defaultScope() : scope;
    }

    /**
     * @param scope Scope of this bean.
     */
    public void scope(String scope) {
        this.scope = scope;
    }

    /**
     * @return package name of the Java class denoted by this bean.
     */
    public String packageName() {
        return classEntity.packageName();
    }

    /**
     * @return simple name of the Java class denoted by this bean.
     */
    public String simpleName() {
        return classEntity.simpleName();
    }

    /**
     * @return type name of the Java class denoted by this bean.
     */
    public String typeName() {
        return classEntity.typeName();
    }

    /**
     * @return name of this bean
     */
    public BeanName beanName() { return new BeanName(name(), scope()); }

    /**
     * @return instance of {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BeanEntity)) {
            return false;
        }
        BeanEntity that = (BeanEntity) o;
        return Objects.equals(beanName(), that.beanName());
    }

    @Override
    public int hashCode() {
        return beanName().hashCode();
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
