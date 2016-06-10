package mlesiewski.simpledi.processors;

import mlesiewski.simpledi.BeanNameValidator;
import mlesiewski.simpledi.Logger;
import mlesiewski.simpledi.SimpleDiAptException;
import mlesiewski.simpledi.annotations.Produce;
import mlesiewski.simpledi.annotations._Default;
import mlesiewski.simpledi.model.*;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.Set;

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
     * @param generatedCollector new {@link GeneratedCode} classes will be added here
     */
    public void process(RoundEnvironment roundEnv, GeneratedCodeCollector generatedCollector) {
        roundEnv.getElementsAnnotatedWith(Produce.class).forEach(this::processElement);
    }

    /**
     * @param element element to process - create new bean providers for the bean producer and the bean being produced
     */
    void processElement(Element element) {
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
    void validate(Element element) {
        if (element.getKind() != ElementKind.METHOD) {
            throw new SimpleDiAptException(Produce.class.getName() + " is only applicable for methods", element);
        }
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.ABSTRACT)) {
            throw new SimpleDiAptException(Produce.class.getName() + " is only applicable for non-abstract methods", element);
        }
        if (modifiers.contains(Modifier.PRIVATE)) {
            throw new SimpleDiAptException(Produce.class.getName() + " is not applicable private methods", element);
        }
        if (modifiers.contains(Modifier.PROTECTED)) {
            throw new SimpleDiAptException(Produce.class.getName() + " is not applicable protected methods", element);
        }
        if (modifiers.contains(Modifier.STATIC)) {
            throw new SimpleDiAptException(Produce.class.getName() + " is only applicable for non-static methods", element);
        }
        Produce annotation = element.getAnnotation(Produce.class);
        BeanNameValidator validator = new BeanNameValidator();
        if (!_Default.VALUE.equals(annotation.name()) && !validator.isAValidName(annotation.name())) {
            throw new SimpleDiAptException(Produce.class.getName() + " has an invalid bean name (" + annotation.name() + ")", element);
        }
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
