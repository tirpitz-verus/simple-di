package mlesiewski.simpledi;

import mlesiewski.simpledi.annotations.Inject;
import mlesiewski.simpledi.annotations._Default;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;
import static mlesiewski.simpledi.BeanRegistry.getBean;

/**
 * Static utility that can inject members (soft dependencies) annotated with {@link Inject}.
 */
public final class MemberInjector {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberInjector.class);

    /** please use static methods instead */
    private MemberInjector() {
    }

    /**
     * Injects members (instance fields) that were annotated with {@link Inject} annotation.
     * Members might not be {@code public} but cannot be {@code final}.
     * Setters will not be called.
     * @param bean a bean that members should be injected into
     * @return the bean that was provided
     * @throws SimpleDiException if the field was final or if the required dependency could not be acquired
     */
    public static <T> T injectMembersInto(T bean) {
        Class<?> aClass = bean.getClass();
        LOGGER.debug("injecting members into instance of class '{}'", aClass);
        fieldsOf(aClass).stream()
                .map(InjectedField::new)
                .filter(InjectedField::isNotStatic)
                .filter(InjectedField::hasAtInjectAnnotation)
                .peek(InjectedField::assureAccessibility)
                .forEach(field -> {
                    LOGGER.trace("setting dependency for a field '{}'", field.name());
                    Object dependency = getBean(field.beanName(), field.scope());
                    field.value(bean, dependency);
                });
        return bean;
    }

    /** a recursive way to traverse class hierarchy in search for declared fields */
    private static List<Field> fieldsOf(Class aClass) {
        LOGGER.trace("searching for members in a class '{}'", aClass);
        Field[] declaredFields = aClass.getDeclaredFields();
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(declaredFields));
        Class superclass = aClass.getSuperclass();
        if (canHaveMembers(superclass)) {
            List<Field> fieldsOfSuper = fieldsOf(superclass);
            fields.addAll(fieldsOfSuper);
        }
        return fields;
    }

    /** not a null / not an interface / not an Object */
    private static boolean canHaveMembers(Class superclass) {
        return superclass != null && !superclass.isInterface() && !superclass.equals(Object.class);
    }

    /** a simple annotated field abstraction */
    private static class InjectedField {

        private final Field field;
        private final Inject annotation;

        private InjectedField(Field field) {
            this.field = field;
            Inject[] annotations = field.getAnnotationsByType(Inject.class);
            this.annotation = annotations.length == 1 ? annotations[0] : null;
        }

        private boolean hasAtInjectAnnotation() {
            return annotation != null;
        }

        /** will not set accessibility for a final field */
        private void assureAccessibility() {
            if (!field.isAccessible() && !isFinal(field.getModifiers())) {
                field.setAccessible(true);
            }
        }

        private String name() {
            return field.getName();
        }

        private String beanName() {
            String beanName = annotation.name();
            return _Default.VALUE.equals(beanName) ? field.getType().getTypeName() : beanName;
        }

        private String scope() {
            String scopeName = annotation.scope();
            return _Default.VALUE.equals(scopeName) ? field.getType().getTypeName() : scopeName;
        }

        private void value(Object bean, Object dependency) {
            try {
                field.set(bean, dependency);
            } catch (IllegalAccessException e) {
                throw new SimpleDiException("could not set a value of the final field '" + name() + "'");
            }
        }

        private boolean isNotStatic() {
            return !isStatic(field.getModifiers());
        }
    }
}
