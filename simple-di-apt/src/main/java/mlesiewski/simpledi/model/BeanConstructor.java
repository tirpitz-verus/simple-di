package mlesiewski.simpledi.model;

import mlesiewski.simpledi.annotations.Inject;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * Constructor to use while creating {@link BeanEntity}.
 */
public class BeanConstructor {

    public static final BeanConstructor DEFAULT = new BeanConstructor();

    private final LinkedHashMap<String, BeanName> parameters = new LinkedHashMap<>();
    private boolean throwsExceptions = false;

    /** @return true if this {@link BeanConstructor} is equal to the {@link BeanConstructor#DEFAULT}. */
    public boolean isDefault() {
        return this.equals(DEFAULT);
    }

    /**
     * adds parameter to constructor
     *
     * @param paramName     name of the parameter
     * @param paramBeanName {@link BeanName} ot he argument
     * @throws IllegalArgumentException if there is a parameter with that paramName
     */
    public void add(String paramName, BeanName paramBeanName) {
        if (parameters.containsKey(paramName)) {
            throw new IllegalArgumentException("no param with that name " + paramName);
        }
        Objects.requireNonNull(paramName);
        Objects.requireNonNull(paramBeanName);
        parameters.put(paramName, paramBeanName);
    }

    /** @param throwsExceptions indicates whether this constructor can throw executions */
    public void throwsExceptions(boolean throwsExceptions) {
        this.throwsExceptions = throwsExceptions;
    }

    /**
     * sets constructor parameter beanName
     *
     * @param paramName     name of the parameter
     * @param paramBeanName {@link BeanName} ot he argument
     * @throws IllegalArgumentException if there is no parameter with that paramName
     */
    public void set(String paramName, BeanName paramBeanName) {
        if (!parameters.containsKey(paramName)) {
            throw new IllegalArgumentException("no param with that name " + paramName);
        }
        Objects.requireNonNull(paramName);
        Objects.requireNonNull(paramBeanName);
        parameters.put(paramName, paramBeanName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BeanConstructor)) {
            return false;
        }
        BeanConstructor that = (BeanConstructor) o;
        return throwsExceptions == that.throwsExceptions && Objects.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameters, throwsExceptions);
    }

    public Collection<BeanName> list() {
        return parameters.values();
    }

    public boolean hasDifferentParametersTo(ExecutableElement constructor) {
        List<? extends VariableElement> parameters = constructor.getParameters();
        if (parameters.size() != this.parameters.size()) {
            return true;
        }
        CounterWrapper i = new CounterWrapper();
        ResultWrapper result = new ResultWrapper();
        this.parameters.forEach((name, beanName) -> {
            VariableElement parameter = parameters.get(i.param);
            if (!parameter.getSimpleName().toString().equals(name)) {
                result.isTrue();
            } else {
                Inject annotation = parameter.getAnnotation(Inject.class);
                DeclaredType paramType = (DeclaredType) parameter.asType();
                BeanName paramBeanName = getBeanName(annotation, paramType);
                if (!paramBeanName.equals(beanName) || !paramBeanName.nameFromType().equals(beanName.nameFromType())) {
                    result.isTrue();
                }
            }
            i.param++;
        });
        return result.value;
    }

    private BeanName getBeanName(Inject annotation, DeclaredType paramType) {
        return annotation != null ? new BeanName(annotation, paramType) : new BeanName(paramType);
    }

    private class CounterWrapper {
        int param;
    }

    private class ResultWrapper {
        boolean value = false;

        void isTrue() {
            value = true;
        }
    }
}
