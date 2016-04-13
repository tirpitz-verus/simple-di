package mlesiewski.simpleioc;

import mlesiewski.simpleioc.annotations.Produce;
import mlesiewski.simpleioc.model.*;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.Map;
import java.util.Set;

/**
 * Process @Produce annotations - creates @Produce Providers.
 */
class ProduceAnnotationsProcessor {

    /**
     * Process @Produce annotations - creates @Produce Providers.
     *
     * @param roundEnv           environment to get annotated {@link Element Elements} from
     * @param generatedCollector new {@link CodeGenerated} classes will be added here
     */
    static void process(RoundEnvironment roundEnv, Map<String, CodeGenerated> generatedCollector) {
        ProduceAnnotationsProcessor processor = new ProduceAnnotationsProcessor(generatedCollector);
        roundEnv.getElementsAnnotatedWith(Produce.class).forEach(processor::processElement);
    }

    private final Map<String, CodeGenerated> generatedCollector;

    ProduceAnnotationsProcessor(Map<String, CodeGenerated> generatedCollector) {
        this.generatedCollector = generatedCollector;
    }

    /**
     * @param element element to process - create new bean providers for the bean producer and the bean being produced
     */
    void processElement(Element element) {
        Logger.note("processing element '" + element.getSimpleName() + "'");
        validate(element);
        Produce annotation = element.getAnnotation(Produce.class);
        ExecutableElement method = (ExecutableElement) element;
        BeanProviderEntity beanProvider = createBeanProducerProvider(annotation, method);
        generatedCollector.put(beanProvider.typeName(), beanProvider);
        ProducedBeanProvider producedBeanProvider = createProducedBeanProvider(annotation, method, beanProvider);
        generatedCollector.put(producedBeanProvider.typeName(), producedBeanProvider);
    }

    /**
     * @param element annotated element to validate - was the annotation applied to the right place?
     */
    public void validate(Element element) {
        if (element.getKind() != ElementKind.METHOD) {
            throw new SimpleIocAptException(Produce.class.getName() + " is only applicable for methods", element);
        }
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.ABSTRACT)) {
            throw new SimpleIocAptException(Produce.class.getName() + " is only applicable for non-abstract methods", element);
        }
        if (modifiers.contains(Modifier.PRIVATE)) {
            throw new SimpleIocAptException(Produce.class.getName() + " is not applicable private methods", element);
        }
        if (modifiers.contains(Modifier.PROTECTED)) {
            throw new SimpleIocAptException(Produce.class.getName() + " is not applicable protected methods", element);
        }
        if (modifiers.contains(Modifier.STATIC)) {
            throw new SimpleIocAptException(Produce.class.getName() + " is only applicable for non-static methods", element);
        }
    }

    /***
     * @return bean provider that provides a bean producer - the one with {@link Produce} annotated method
     */
    private BeanProviderEntity createBeanProducerProvider(Produce annotation, ExecutableElement method) {
        TypeMirror beanProducerType = method.getEnclosingElement().asType();
        ClassEntity beanProducerClass = ClassEntity.from(beanProducerType);
        BeanEntity beanProducer = BeanEntity.builder().from(beanProducerClass).withScope(annotation.scope()).build();
        return new BeanProviderEntity(beanProducer);
    }

    /**
     * @return bean provider that provides the produced bean - the one returned by {@link Produce} annotated method
     */
    private ProducedBeanProvider createProducedBeanProvider(Produce annotation, ExecutableElement method, BeanProviderEntity beanProvider) {
        ClassEntity producedBeanClass = ClassEntity.from(method.getReturnType());
        BeanEntity producedBean = BeanEntity.builder().from(producedBeanClass).withScope(annotation.scope()).withName(annotation.name()).build();
        String producerMethod = method.getSimpleName().toString();
        return new ProducedBeanProvider(producedBean, beanProvider.beanEntity(), producerMethod);
    }

}
