package mlesiewski.simpleioc;

import mlesiewski.simpleioc.model.CodeGenerated;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
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
            Map<String, CodeGenerated> beanClassesCollector = new HashMap<>();
            // 1. process @Produce annotations - create @Produce Providers
            ProduceAnnotationsProcessor.process(roundEnv, beanClassesCollector);
            // 2. process @Bean annotations - creating Providers for them (if no producers)
            // 3. process @Inject annotations  - creating Providers for them and their targets if none were created already
            // 4. write source files
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
}
