package mlesiewski.simpledi.apt;

import mlesiewski.simpledi.apt.model.GeneratedCode;
import mlesiewski.simpledi.apt.model.GeneratedCodeCollector;
import mlesiewski.simpledi.apt.processors.BeanAnnotationProcessor;
import mlesiewski.simpledi.apt.processors.CustomScopeAnnotationProcessor;
import mlesiewski.simpledi.apt.processors.InjectAnnotationProcessor;
import mlesiewski.simpledi.apt.processors.ProduceAnnotationsProcessor;
import mlesiewski.simpledi.apt.template.TemplateFactory;
import mlesiewski.simpledi.apt.writer.GeneratedCodeWriter;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        "mlesiewski.simpledi.core.annotations.Bean",
        "mlesiewski.simpledi.core.annotations.Inject",
        "mlesiewski.simpledi.core.annotations.Produce",
        "mlesiewski.simpledi.core.annotations.CustomScope"
})
public class SimpleDiProcessor extends AbstractProcessor {

    private static final boolean ANNOTATIONS_CLAIMED = true;

    private GeneratedCodeWriter codeWriter;
    private ProduceAnnotationsProcessor produceAnnotationsProcessor;
    private final GeneratedCodeCollector collector = new GeneratedCodeCollector();
    private final BeanAnnotationProcessor beanAnnotationProcessor = new BeanAnnotationProcessor(collector);
    private final InjectAnnotationProcessor injectAnnotationProcessor = new InjectAnnotationProcessor(collector);
    private final CustomScopeAnnotationProcessor customScopeAnnotationProcessor = new CustomScopeAnnotationProcessor();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Logger.set(processingEnv.getMessager());
        TemplateFactory.set(processingEnv.getFiler());
        codeWriter = new GeneratedCodeWriter(processingEnv.getFiler());
        produceAnnotationsProcessor = new ProduceAnnotationsProcessor(collector, processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Logger.note("processing mlesiewski.simpledi.core.annotations");
        try {
            // 1. process @Produce annotations - create @Produce Providers
            produceAnnotationsProcessor.process(roundEnv);
            // 2. process @Bean annotations - creating Providers for them (if no producers)
            beanAnnotationProcessor.process(roundEnv);
            // 3. process @Inject annotations - creating Providers for them and their targets if none were created already
            injectAnnotationProcessor.process(roundEnv);
            // 4. process @CustomScope annotations - just garter types
            customScopeAnnotationProcessor.process(roundEnv);
            if (roundEnv.processingOver()) {
                Collection<GeneratedCode> registrable = collector.registrable();
                // 4. write source files
                codeWriter.writeSourceFiles(registrable);
                // 5. write Registrable service loader file
                codeWriter.writeRegistrableServiceLoader(registrable);
                // 6. write Scope service loader file
                codeWriter.writeScopeServiceLoader(customScopeAnnotationProcessor.scopes());
            }
        } catch (SimpleDiAptException e) {
            log(e);
        } catch (Exception e) {
            log(e);
        }
        return ANNOTATIONS_CLAIMED;
    }

    private void log(Exception e) {
        Logger.error("unexpected error: " + e.getClass() + " " + e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            Logger.note(element.toString());
        }
    }

    private void log(SimpleDiAptException e) {
        if (e.hasElement()) {
            Logger.error(e.getMessage(), e.getElement());
        } else {
            Logger.error(e.getMessage());
        }
    }

}
