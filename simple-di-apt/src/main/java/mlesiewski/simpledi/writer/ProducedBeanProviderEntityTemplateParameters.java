package mlesiewski.simpledi.writer;

import mlesiewski.simpledi.model.BeanEntity;
import mlesiewski.simpledi.model.GeneratedCode;
import mlesiewski.simpledi.model.ProducedBeanProviderEntity;

import java.util.HashMap;

/**
 * represents template parameters for {@link ProducedBeanProviderEntity}
 */
class ProducedBeanProviderEntityTemplateParameters extends HashMap<String, String> {

    ProducedBeanProviderEntityTemplateParameters(GeneratedCode generated) {
        ProducedBeanProviderEntity entity = (ProducedBeanProviderEntity) generated;
        put("beanProviderPackage", entity.packageName());
        put("beanProviderSimpleName", entity.simpleName());
        BeanEntity beanEntity = entity.beanEntity();
        put("beanType", beanEntity.typeName());
        put("beanName", beanEntity.name());
        put("beanScope", beanEntity.scope());
        put("beanProducerBeanName", entity.beanProducer().typeName());
        put("beanProducerBeanScope", entity.beanProducer().scope());
        put("beanProducerMethod", entity.producerMethod());

        String tryBlock = "";
        String catchBlock = "";
        if (!entity.thrown().isEmpty()) {
            tryBlock = "try {";
            catchBlock = String.format("} catch (%s e) { throw new SimpleDiException(\"exception wrapped during calling produce() :\" + e.getMessage(), e); }", entity.thrown());
        }
        put("tryBlock", tryBlock);
        put("catchBlock", catchBlock);
    }
}
