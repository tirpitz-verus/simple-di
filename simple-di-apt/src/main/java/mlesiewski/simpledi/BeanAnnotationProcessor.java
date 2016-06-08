package mlesiewski.simpledi;

import mlesiewski.simpledi.annotations.Bean;
import mlesiewski.simpledi.annotations._Default;
import mlesiewski.simpledi.model.BeanEntity;
import mlesiewski.simpledi.model.BeanProviderEntity;
import mlesiewski.simpledi.model.ClassEntity;
import mlesiewski.simpledi.model.GeneratedCodeCollector;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import java.util.Set;

/**
 * Processes {@link mlesiewski.simpledi.annotations.Bean} annotations.
 */
class BeanAnnotationProcessor {

    private final GeneratedCodeCollector collector;

    BeanAnnotationProcessor(GeneratedCodeCollector collector) {
        this.collector = collector;
    }

    /**
     * @param roundEnv elements to process
     */
    void process(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Bean.class).stream().forEach(this::processElement);
    }

    /** processes one element */
    private void processElement(Element element) {
        Logger.note("processing element '" + element.getSimpleName() + "'");
        validate(element);
        Bean annotation = element.getAnnotation(Bean.class);
        ClassEntity beanClass = ClassEntity.from(element.asType());
        BeanEntity bean = BeanEntity.builder().from(beanClass).withScope(annotation.scope()).withName(annotation.name()).build();
        BeanProviderEntity provider = new BeanProviderEntity(bean);
        collector.registrable(provider);
    }

    /** validates element */
    private void validate(Element element) {
        if (element.getKind() != ElementKind.CLASS) {
            throw new SimpleDiAptException(Bean.class.getName() + " is only applicable for classes", element);
        }
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.ABSTRACT)) {
            throw new SimpleDiAptException(Bean.class.getName() + " is only applicable for non-abstract classes", element);
        }
        if (modifiers.contains(Modifier.PRIVATE)) {
            throw new SimpleDiAptException(Bean.class.getName() + " is not applicable private classes", element);
        }
        Bean annotation = element.getAnnotation(Bean.class);
        BeanNameValidator validator = new BeanNameValidator();
        if (!_Default.VALUE.equals(annotation.name()) && !validator.isAValidName(annotation.name())) {
            throw new SimpleDiAptException(Bean.class.getName() + " has an invalid bean name (" + annotation.name() + ")", element);
        }
    }
}