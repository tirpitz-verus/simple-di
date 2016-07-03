package mlesiewski.simpledi.processors;

import mlesiewski.simpledi.Logger;
import mlesiewski.simpledi.annotations.Bean;
import mlesiewski.simpledi.annotations.Produce;
import mlesiewski.simpledi.model.*;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Process @Produce annotations - creates @Produce Providers.
 */
public class ProduceAnnotationsProcessor {

    private final GeneratedCodeCollector generatedCollector;

    public ProduceAnnotationsProcessor(GeneratedCodeCollector generatedCollector) {
        this.generatedCollector = generatedCollector;
    }

    /**
     * Process @Produce annotations - creates @Produce Providers.
     *
     * @param roundEnv           environment to get annotated {@link Element Elements} from
     */
    public void process(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Produce.class).forEach(this::processElement);
    }

    /**
     * @param element element to process - create new bean providers for the bean producer and the bean being produced
     */
    private void processElement(Element element) {
        Logger.note("processing element '" + element.getSimpleName() + "'");
        validate(element);
        Produce annotation = element.getAnnotation(Produce.class);
        ExecutableElement method = (ExecutableElement) element;
        BeanProviderEntity beanProvider = createBeanProducerProvider(method);
        generatedCollector.registrable(beanProvider);
        ProducedBeanProviderEntity producedBeanProvider = createProducedBeanProvider(annotation, method, beanProvider);
        generatedCollector.registrable(producedBeanProvider);
    }

    /**
     * @param element annotated element to validate - was the annotation applied to the right place?
     */
    private void validate(Element element) {
        Produce annotation = element.getAnnotation(Produce.class);
        Validators.validBeanName(annotation.name(), Produce.class, element);
        Validators.validAccessibility(element, Bean.class, "methods");
        Validators.isNotStatic(element, Produce.class, "methods");
        Validators.isAMethod(element, Produce.class);
    }

    /***
     * @return bean provider that provides a bean producer - the one with {@link Produce} annotated method
     */
    private BeanProviderEntity createBeanProducerProvider(ExecutableElement method) {
        TypeMirror beanProducerType = method.getEnclosingElement().asType();
        ClassEntity beanProducerClass = ClassEntity.from(beanProducerType);
        BeanEntity beanProducer = BeanEntity.builder().from(beanProducerClass).build();
        return new BeanProviderEntity(beanProducer);
    }

    /**
     * @return bean provider that provides the produced bean - the one returned by {@link Produce} annotated method
     */
    private ProducedBeanProviderEntity createProducedBeanProvider(Produce annotation, ExecutableElement method, BeanProviderEntity beanProvider) {
        ClassEntity producedBeanClass = ClassEntity.from(method.getReturnType());
        BeanEntity producedBean = BeanEntity.builder().from(producedBeanClass).withScope(annotation.scope()).withName(annotation.name()).build();
        String producerMethod = method.getSimpleName().toString();
        return new ProducedBeanProviderEntity(producedBean, beanProvider.beanEntity(), producerMethod);
    }

}
