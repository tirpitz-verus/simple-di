package mlesiewski.simpledi;

import mlesiewski.simpledi.model.GeneratedCode;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        "mlesiewski.simpledi.annotations.Bean",
        "mlesiewski.simpledi.annotations.Inject",
        "mlesiewski.simpledi.annotations.Produce"
})
public class SimpleDiProcessor extends AbstractProcessor {

    private static final boolean ANNOTATIONS_CLAIMED = true;

    private GeneratedCodeWriter codeWriter;
    private Map<String, GeneratedCode> codeGeneratedCollector = new LinkedHashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Logger.set(processingEnv.getMessager());
        TemplateFactory.set(processingEnv.getFiler());
        codeWriter = new GeneratedCodeWriter(processingEnv.getFiler());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Logger.note("processing mlesiewski.simpledi.annotations");
        try {
            // 1. process @Produce annotations - create @Produce Providers
            ProduceAnnotationsProcessor.process(roundEnv, codeGeneratedCollector);
            // 2. process @Bean annotations - creating Providers for them (if no producers)
            // 3. process @Inject annotations - creating Providers for them and their targets if none were created already
            if (roundEnv.processingOver()) {
                // 4. write source files
                codeWriter.writeSourceFiles(codeGeneratedCollector.values());
                // 5. write service loader file
                codeWriter.writeServiceLoader(codeGeneratedCollector.values());
            }
        } catch (SimpleDiAptException e) {
            log(e);
        } catch (Exception e) {
            log(e);
        }
        return ANNOTATIONS_CLAIMED;
    }

    private void log(Exception e) {
        Logger.error("unexpected error: " + e.getMessage());
    }

    private void log(SimpleDiAptException e) {
        if (e.hasElement()) {
            Logger.error(e.getMessage(), e.getElement());
        } else {
            Logger.error(e.getMessage());
        }
    }

}
