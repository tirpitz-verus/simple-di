package mlesiewski.simpledi.model;

import java.util.Collection;
import java.util.LinkedHashMap;
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
        if (this == o) return true;
        if (!(o instanceof BeanConstructor)) return false;
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
}
