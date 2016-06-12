package mlesiewski.simpledi.processors;

import mlesiewski.simpledi.BeanNameValidator;
import mlesiewski.simpledi.Logger;
import mlesiewski.simpledi.SimpleDiAptException;
import mlesiewski.simpledi.annotations.*;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static javax.lang.model.element.Modifier.*;

/**
 * provides element validation methods
 */
class Validators {

    private Validators() {
    }

    /** @throws SimpleDiAptException if the name provided is bad - calling {@link BeanNameValidator} */
    static void validBeanName(String name, Class<?> annotation, Element element) {
        BeanNameValidator validator = new BeanNameValidator();
        if (!_Default.VALUE.equals(name) && !validator.isAValidName(name)) {
            throw new SimpleDiAptException(annotation.getName() + " has an invalid bean name (" + name + ")", element);
        }
    }

    /** @throws SimpleDiAptException if element is abstract, private or protected */
    static void validAccessibility(Element element, Class<?> annotation, String type) {
        if (!isFriendly(element)) {
            throw new SimpleDiAptException(annotation.getName() + " is only applicable for non-abstract public or friendly " + type, element);
        }
    }

    private static boolean isFriendly(Element element) {
        Set<Modifier> modifiers = element.getModifiers();
        return !(modifiers.contains(ABSTRACT) || modifiers.contains(PRIVATE) || modifiers.contains(PROTECTED));
    }

    /** @throws SimpleDiAptException if element is static */
    static void isNotStatic(Element element, Class<?> annotation, String type) {
        if (element.getModifiers().contains(STATIC)) {
            throw new SimpleDiAptException(annotation.getName() + " is only applicable for non-static " + type, element);
        }
    }

    /** @throws SimpleDiAptException if element provided is not a class */
    static void isAClass(Element element, Class<?> annotation) {
        if (element.getKind() != ElementKind.CLASS) {
            throw new SimpleDiAptException(annotation.getName() + " is only applicable for classes", element);
        }
    }

    /**
     * @throws SimpleDiAptException if the class provided does not have a constructor that is default or annotated with
     * {@link Inject} or a constructor whose all of the arguments ar annotated with {@link Inject}.
     */
    static void validBeanConstructor(DeclaredType aClass) {
        List<? extends Element> enclosed = aClass.asElement().getEnclosedElements();
        List<ExecutableElement> constructors = ElementFilter.constructorsIn(enclosed);
        List<Predicate<ExecutableElement>> predicates = Arrays.asList(
                e -> e.getParameters().isEmpty(),
                e -> e.getAnnotation(Inject.class) != null,
                e -> e.getParameters().stream().allMatch(p -> p.getAnnotation(Inject.class) != null)
        );
        Predicate<ExecutableElement> noArgsOrInject = predicates.stream().reduce(p -> false, Predicate::or);
        boolean validFound = constructors.stream()
                .filter(Validators::isFriendly)
                .anyMatch(noArgsOrInject);
        if (!validFound) {
            throw new SimpleDiAptException(aClass.toString() + " does not have a valid default or @Inject annotated constructor");
        }
    }

    /**
     * @throws SimpleDiAptException if the class provided does not have a constructor that is default or no args.
     */
    static void validScopeConstructor(DeclaredType aClass) {
        List<? extends Element> enclosed = aClass.asElement().getEnclosedElements();
        List<ExecutableElement> constructors = ElementFilter.constructorsIn(enclosed);
        boolean anyMatch = constructors.stream()
                .filter(Validators::isFriendly)
                .anyMatch(e -> e.getParameters().isEmpty());
        if (!anyMatch) {
            throw new SimpleDiAptException(aClass.toString() + " does not have a valid default or @Inject annotated constructor");
        }
    }

    /** @throws SimpleDiAptException if element provided is not a class */
    static void isAMethod(Element element, Class<?> annotation) {
        if (element.getKind() != ElementKind.METHOD) {
            throw new SimpleDiAptException(annotation.getName() + " is only applicable for methods", element);
        }
    }
}
