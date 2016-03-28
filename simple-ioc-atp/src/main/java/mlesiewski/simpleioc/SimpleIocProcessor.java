package mlesiewski.simpleioc;

import mlesiewski.simpleioc.annotations.Produce;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
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
            // 1. process @Produce annotations - create @Produce Providers in the first pass as not to overwrite them by generating their products
            Set<? extends Element> produceElements = roundEnv.getElementsAnnotatedWith(Produce.class);
            for (Element element : produceElements) {
                BaseGenerator generator = new ProviderForProduceGenerator(element);
                generator.generateCode(processingEnv.getFiler());
            }
            // 2. process @Bean annotations - creating Providers for them
            // 3. if any of the @Produce Providers does not have a Provider in all scopes they use, then create these for them
            // 4. process @Inject annotations  - creating Providers for them and their targets if none were created already
        } catch (SimpleIocElementException e) {
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
