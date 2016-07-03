package mlesiewski.simpledi.processors;

import mlesiewski.simpledi.Logger;
import mlesiewski.simpledi.SimpleDiAptException;
import mlesiewski.simpledi.annotations.Bean;
import mlesiewski.simpledi.annotations.Inject;
import mlesiewski.simpledi.annotations._Default;
import mlesiewski.simpledi.model.*;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.DeclaredType;
import java.util.Optional;
import java.util.Set;

import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PROTECTED;

/**
 * Processes @Inject annotations.
 */
public class InjectAnnotationProcessor {

    /** collector ref */
    private final GeneratedCodeCollector collector;

    public InjectAnnotationProcessor(GeneratedCodeCollector collector) {
        this.collector = collector;
    }

    /** @param roundEnv elements to process */
    public void process(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Inject.class).forEach(this::processElement);
    }

    /** Top level process method with common validation. Delegates to other methods. */
    private void processElement(Element element) {
        Logger.note("processing element '" + element.getSimpleName() + "'");
        Inject annotation = element.getAnnotation(Inject.class);
        Validators.validBeanName(annotation.name(), Inject.class, element);
        switch (element.getKind()) {
            case FIELD: processField(element); break;
            case PARAMETER: processParameter(element); break;
            case CONSTRUCTOR: processConstructor(element); break;
            default: throw new SimpleDiAptException(Inject.class.getName() + " can only be applied to fields, constructors and constructor parameters", element);
        }
    }

    /** Processes annotated constructors. */
    private void processConstructor(Element element) {
        Validators.validAccessibility(element, Inject.class, "fields, constructors and constructor parameter");
        BeanEntity beanEntity = getEnclosingBeanEntity(element);

        ExecutableElement constructor = (ExecutableElement) element;
        BeanConstructor beanConstructor = makeBeanConstructor(constructor);
        beanEntity.constructor(beanConstructor);
        if (beanEntity.constructorIsDefault()) {
            beanEntity.constructor(beanConstructor);
        } else if (!beanEntity.constructor().equals(beanConstructor)) {
            throw new SimpleDiAptException("bean constructor redefinition", element);
        }
    }

    /** Processes annotated parameters of a constructor. */
    private void processParameter(Element parameter) {
        Validators.isNotAPrimitive(parameter, Inject.class);
        ExecutableElement constructor = (ExecutableElement) parameter.getEnclosingElement();
        BeanEntity beanEntity = getEnclosingBeanEntity(constructor);

        BeanConstructor beanConstructor;
        if (beanEntity.constructorIsDefault()) {
            beanConstructor = makeBeanConstructor(constructor);
            beanEntity.constructor(beanConstructor);
        } else {
            beanConstructor = beanEntity.constructor();
            if (!beanConstructor.equals(constructor)) {
                throw new SimpleDiAptException("a bean can have only one injection constructor", parameter);
            }
        }

        Inject annotation = parameter.getAnnotation(Inject.class);
        BeanName paramBeanName = makeBeanName(annotation.name(), annotation.scope(), parameter);
        String paramName = parameter.getSimpleName().toString();
        beanConstructor.set(paramName, paramBeanName);

        DeclaredType injectedType = (DeclaredType) parameter.asType();
        registerInjected(paramBeanName, injectedType);
    }

    /** Processes class fields. */
    private void processField(Element field) {
        Validators.isNotAPrimitive(field, Inject.class);
        BeanEntity beanEntity = getEnclosingBeanEntity(field);

        Inject annotation = field.getAnnotation(Inject.class);
        BeanName beanName = makeBeanName(annotation.name(), annotation.scope(), field);
        String fieldName = field.getSimpleName().toString();

        Set<Modifier> modifiers = field.getModifiers();
        boolean inaccessible = modifiers.contains(PRIVATE) || modifiers.contains(PROTECTED);
        if (inaccessible) {
            Optional<ExecutableElement> setter = getSetterMethodFor(field, fieldName);
            if (setter.isPresent()) {
                ExecutableElement method = setter.get();
                beanEntity.setter(method.getSimpleName().toString(), beanName);
            } else {
                throw new SimpleDiAptException("private or protected field with no setter cannot be annotated with " + Inject.class.getSimpleName(), field);
            }
        } else {
            beanEntity.field(fieldName, beanName);
        }
    }

    // helpers

    /** if not already registered than registers a new bean provider for the type of the element*/
    private void registerInjected(BeanName beanName, DeclaredType injectedType) {
        if (!collector.hasBean(beanName)) {
            Validators.validBeanConstructor(injectedType);
            ClassEntity injectedClassEntity = ClassEntity.from(injectedType);
            BeanEntity injectedEntity = BeanEntity.builder().from(injectedClassEntity).withName(beanName.name()).withScope(beanName.scope()).build();
            BeanProviderEntity provider = new BeanProviderEntity(injectedEntity);
            collector.registrable(provider);
        }
    }

    /** @return element representing a setter method - or not */
    private Optional<ExecutableElement> getSetterMethodFor(Element field, String fieldName) {
        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Element aClassElement = field.getEnclosingElement();
        return aClassElement.getEnclosedElements().stream()
                .filter(e -> e.getKind().equals(ElementKind.METHOD))
                .map(e -> (ExecutableElement) e)
                .filter(e -> e.getModifiers().isEmpty() || e.getModifiers().contains(Modifier.PUBLIC))
                .filter(e -> !e.getModifiers().contains(Modifier.STATIC))
                .filter(e -> {
                    String methodName = e.getSimpleName().toString();
                    return methodName.equals(setterName) || methodName.equals(fieldName);
                })
                .filter(e -> e.getParameters().size() == 1 && e.getParameters().get(0).asType().equals(field.asType()))
                .findFirst();
    }

    /** @return new {@link BeanConstructor} from the constructor provided. */
    private BeanConstructor makeBeanConstructor(ExecutableElement constructor) {
        BeanConstructor beanConstructor = new BeanConstructor();
        constructor.getParameters().stream().forEachOrdered(parameter -> {
            Validators.isNotAPrimitive(parameter, Inject.class);
            String paramName = parameter.getSimpleName().toString();
            Inject annotation = parameter.getAnnotation(Inject.class);
            BeanName paramBeanName;
            if (annotation != null) {
                paramBeanName = makeBeanName(annotation.name(), annotation.scope(), parameter);
            } else {
                paramBeanName = new BeanName(parameter.asType().toString());
            }
            beanConstructor.add(paramName, paramBeanName);
            DeclaredType paramType = (DeclaredType) parameter.asType();
            registerInjected(paramBeanName, paramType);
        });
        boolean throwsExceptions = constructor.getThrownTypes().isEmpty();
        beanConstructor.throwsExceptions(throwsExceptions);
        return beanConstructor;
    }

    /** @return {@link BeanEntity} with a correct name */
    private BeanEntity getEnclosingBeanEntity(Element element) {
        Element aBeanClass = element.getEnclosingElement();
        Validators.validBeanConstructor((DeclaredType) aBeanClass.asType());
        Bean annotation = aBeanClass.getAnnotation(Bean.class);
        BeanName beanName;
        if (annotation != null) {
            beanName = makeBeanName(annotation.name(), annotation.scope(), aBeanClass);
        } else {
            beanName = new BeanName(aBeanClass.asType().toString());
        }
        return getBeanEntity(aBeanClass, beanName);
    }

    /** @return {@link BeanEntity} form the {@link #collector} - if its not there than a new {@link BeanEntity} will be created and pun into {@link #collector} */
    private BeanEntity getBeanEntity(Element beanClass, BeanName beanClassName) {
        if (collector.hasBean(beanClassName)) {
            return collector.getBean(beanClassName);
        } else {
            ClassEntity beanClassEntity = ClassEntity.from(beanClass.asType());
            BeanEntity beanEntity = BeanEntity.builder().from(beanClassEntity).withName(beanClassName.name()).withScope(beanClassName.scope()).build();
            BeanProviderEntity provider = new BeanProviderEntity(beanEntity);
            collector.registrable(provider);
            return beanEntity;
        }
    }

    /** @return correct {@link BeanName} - if name isn't provided it will be derived from the type */
    private BeanName makeBeanName(String name, String scope, Element element) {
        String namePart = name.equals(_Default.VALUE) ? element.asType().toString() : name;
        return new BeanName(namePart, scope);
    }
}
