package mlesiewski.simpledi.apt.processors;

import mlesiewski.simpledi.apt.Logger;
import mlesiewski.simpledi.core.annotations.Bean;
import mlesiewski.simpledi.core.annotations.Produce;
import mlesiewski.simpledi.apt.model.BeanEntity;
import mlesiewski.simpledi.apt.model.BeanProviderEntity;
import mlesiewski.simpledi.apt.model.ClassEntity;
import mlesiewski.simpledi.apt.model.GeneratedCodeCollector;
import mlesiewski.simpledi.apt.model.ProducedBeanProviderEntity;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Process @Produce annotations - creates @Produce Providers.
 */
public class ProduceAnnotationsProcessor {

    private final GeneratedCodeCollector generatedCollector;
    private final Elements elementUtils;
    private final Types typeUtils;

    public ProduceAnnotationsProcessor(GeneratedCodeCollector generatedCollector, ProcessingEnvironment environment) {
        this.generatedCollector = generatedCollector;
        this.elementUtils = environment.getElementUtils();
        this.typeUtils = environment.getTypeUtils();
    }

    /**
     * Process @Produce annotations - creates @Produce Providers.
     *
     * @param roundEnv environment to get annotated {@link Element Elements} from
     */
    public void process(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Produce.class).forEach(this::processElement);
    }

    /**
     * @param element element to processSupertypes - create new bean providers for the bean producer and the bean being produced
     */
    private void processElement(Element element) {
        Logger.note("processing element '" + element.getSimpleName() + "'");
        validate(element);
        Produce annotation = element.getAnnotation(Produce.class);
        ExecutableElement method = (ExecutableElement) element;
        BeanProviderEntity beanProvider = createBeanProducerProvider(method);
        createProducedBeanProvider(annotation, method, beanProvider);
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
        TypeElement enclosingElement = (TypeElement) method.getEnclosingElement();
        TypeMirror beanProducerType = enclosingElement.asType();
        ClassEntity beanProducerClass = ClassEntity.from(beanProducerType);
        BeanEntity beanProducer = BeanEntity.builder().from(beanProducerClass).build();
        BeanProviderEntity beanProvider = new BeanProviderEntity(beanProducer, enclosingElement);
        generatedCollector.registrable(beanProvider);
        return beanProvider;
    }

    /**
     * creates bean provider that provides the produced bean - the one returned by {@link Produce} annotated method
     */
    private void createProducedBeanProvider(Produce annotation, ExecutableElement method, BeanProviderEntity beanProvider) {
        ClassEntity producedBeanClass = ClassEntity.from(method.getReturnType());
        BeanEntity producedBean = BeanEntity.builder().from(producedBeanClass).withScope(annotation.scope()).withName(annotation.name()).build();
        producedBean.hardDependency(beanProvider.beanName());
        String producerMethod = method.getSimpleName().toString();
        List<String> thrown = method.getThrownTypes().stream().filter(this::isCheckedException).map(TypeMirror::toString).collect(Collectors.toList());
        ProducedBeanProviderEntity producedBeanProvider = new ProducedBeanProviderEntity(producedBean, beanProvider.beanEntity(), producerMethod, thrown);
        generatedCollector.registrable(producedBeanProvider);
    }

    /**
     * @return {@code true} if type provided is an unchecked exception
     */
    private boolean isCheckedException(TypeMirror typeMirror) {
        TypeMirror runtimeException = elementUtils.getTypeElement("java.lang.RuntimeException").asType();
        return !typeUtils.isAssignable(typeMirror, runtimeException);
    }
}
