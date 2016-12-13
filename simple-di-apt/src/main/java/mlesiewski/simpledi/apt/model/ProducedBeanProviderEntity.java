package mlesiewski.simpledi.apt.model;

import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An entity representing BeanProvider that delegates calls to a BeanProducer method.
 */
public class ProducedBeanProviderEntity extends BeanProviderEntity implements GeneratedCode {

    private static final TypeElement SOURCE = null;
    private final BeanEntity beanProducer;
    private final String producerMethod;
    private final String thrown;

    /**
     * @param producedBean a bean that is going to be produced by this provider
     * @param beanProducer a bean that is going to be a delegate for bean production call
     * @param producerMethod name of the method of beanProducer that produces producedBean instances
     * @param thrown list of names of thrown types declared by the producer method
     */
    public ProducedBeanProviderEntity(BeanEntity producedBean, BeanEntity beanProducer, String producerMethod, List<String> thrown) {
        super(producedBean, "Wrapper", SOURCE);
        this.beanProducer = beanProducer;
        this.producerMethod = producerMethod;
        this.thrown = thrown.stream().collect(Collectors.joining(" | "));
    }

    public String producerMethod() {
        return producerMethod;
    }

    public BeanEntity beanProducer() {
        return beanProducer;
    }

    public String thrown() {
        return thrown;
    }
}