package mlesiewski.simpledi;

import mlesiewski.simpledi.model.BeanProviderEntity;
import mlesiewski.simpledi.model.CodeGenerated;
import mlesiewski.simpledi.model.ProducedBeanProviderEntity;

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
        "mlesiewski.simpledi.annotations.Bean",
        "mlesiewski.simpledi.annotations.Inject",
        "mlesiewski.simpledi.annotations.Produce"
})
public class SimpleDiProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Logger.set(processingEnv.getMessager());
        TemplateFactory.set(processingEnv.getFiler());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Logger.note("processing mlesiewski.simpledi.annotations");
        try {
            Map<String, CodeGenerated> codeGeneratedCollector = new HashMap<>();
            // 1. process @Produce annotations - create @Produce Providers
            ProduceAnnotationsProcessor.process(roundEnv, codeGeneratedCollector);
            // 2. process @Bean annotations - creating Providers for them (if no producers)
            // 3. process @Inject annotations  - creating Providers for them and their targets if none were created already
            // 4. write source files
            codeGeneratedCollector.values().forEach(this::write);
        } catch (SimpleDiAptException e) {
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
                throw new SimpleDiAptException("could not find template for " + generated.getClass().getName());
            }
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            throw new SimpleDiAptException("could not write a class '" + generated.typeName() + "' file because: " + e.getMessage());
        }
    }
}
