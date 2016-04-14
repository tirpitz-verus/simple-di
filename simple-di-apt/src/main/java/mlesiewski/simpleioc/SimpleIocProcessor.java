package mlesiewski.simpleioc;

import mlesiewski.simpleioc.model.BeanProviderEntity;
import mlesiewski.simpleioc.model.CodeGenerated;
import mlesiewski.simpleioc.model.ProducedBeanProviderEntity;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        "mlesiewski.simpleioc.annotations.Bean",
        "mlesiewski.simpleioc.annotations.Inject",
        "mlesiewski.simpleioc.annotations.Produce"
})
public class SimpleIocProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Logger.set(processingEnv.getMessager());
        TemplateFactory.set(processingEnv.getFiler());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Logger.note("processing mlesiewski.simpleioc.annotations");
        try {
            Map<String, CodeGenerated> codeGeneratedCollector = new HashMap<>();
            // 1. process @Produce annotations - create @Produce Providers
            ProduceAnnotationsProcessor.process(roundEnv, codeGeneratedCollector);
            // 2. process @Bean annotations - creating Providers for them (if no producers)
            // 3. process @Inject annotations  - creating Providers for them and their targets if none were created already
            // 4. write source files
            codeGeneratedCollector.values().forEach(this::write);
        } catch (SimpleIocAptException e) {
            if (e.hasElement()) {
                Logger.error(e.getMessage(), e.getElement());
            } else {
                Logger.error(e.getMessage());
            }
        } catch (Exception e) {
            Logger.error("unexpected error: " + e.getMessage());
        }
        return true;
    }

    // todo poc - change to some more sensible implementation
    private void write(CodeGenerated generated) {
        Logger.note("attempting to create source file for '" + generated.typeName() + "'");
        try {
            JavaFileObject classFile = processingEnv.getFiler().createSourceFile(generated.typeName());
            Writer writer = classFile.openWriter();
            String text;
            if (generated instanceof ProducedBeanProviderEntity) {
                ProducedBeanProviderEntity entity = (ProducedBeanProviderEntity) generated;
                Template template = TemplateFactory.get("ProducedBeanProviderImplementation");
                Map<String, String> params = new HashMap<>();
                params.put("beanProviderPackage", entity.packageName());
                params.put("beanProviderSimpleName", entity.simpleName());
                params.put("beanType", entity.beanEntity().typeName());
                params.put("beanName", entity.beanEntity().name());
                params.put("beanScope", entity.beanEntity().scope());
                params.put("beanProducerBeanName", entity.beanProducer().typeName());
                params.put("beanProducerMethod", entity.producerMethod());
                text = template.compile(params);
            } else if (generated instanceof BeanProviderEntity) {
                BeanProviderEntity entity = (BeanProviderEntity) generated;
                Template template = TemplateFactory.get("BeanProviderImplementation");
                Map<String, String> params = new HashMap<>();
                params.put("beanProviderPackage", entity.packageName());
                params.put("beanProviderSimpleName", entity.simpleName());
                params.put("beanType", entity.beanEntity().typeName());
                params.put("beanName", entity.beanEntity().name());
                params.put("beanScope", entity.beanEntity().scope());
                text = template.compile(params);
            } else {
                throw new SimpleIocAptException("could not find template for " + generated.getClass().getName());
            }
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            throw new SimpleIocAptException("could not write a class '" + generated.typeName() + "' file because: " + e.getMessage());
        }
    }
}
