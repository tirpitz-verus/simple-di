package mlesiewski.simpledi.processors;

import mlesiewski.simpledi.Logger;
import mlesiewski.simpledi.annotations.Bean;
import mlesiewski.simpledi.model.BeanEntity;
import mlesiewski.simpledi.model.BeanProviderEntity;
import mlesiewski.simpledi.model.ClassEntity;
import mlesiewski.simpledi.model.GeneratedCodeCollector;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;

/**
 * Processes {@link mlesiewski.simpledi.annotations.Bean} annotations.
 */
public class BeanAnnotationProcessor {

    private final GeneratedCodeCollector collector;

    public BeanAnnotationProcessor(GeneratedCodeCollector collector) {
        this.collector = collector;
    }

    /**
     * @param roundEnv elements to process
     */
    public void process(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Bean.class).forEach(this::processElement);
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
        Bean annotation = element.getAnnotation(Bean.class);
        Validators.validBeanName(annotation.name(), Bean.class, element);
        Validators.validAccessibility(element, Bean.class, "classes");
        Validators.isAClass(element, Bean.class);
        Validators.validBeanConstructor((DeclaredType) element.asType());
    }
}
