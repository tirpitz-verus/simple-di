package mlesiewski.simpleioc;

import mlesiewski.simpleioc.annotations.Produce;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.*;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        "mlesiewski.simpleioc.Inject",
        "mlesiewski.simpleioc.Produce"
})
public class SimpleIocProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Logger.set(processingEnv.getMessager());

        Filer filer = processingEnv.getFiler();
        FileObject resource = null;

        try {
            resource = filer.getResource(StandardLocation.CLASS_PATH, "", "ProduceBeanProvider.txt");
            Logger.note("3 is null " + (resource == null));
        } catch (IOException e) {
            Logger.error("3 " + e.getMessage());
        }

        Logger.note("wrotki");

        Set<? extends Element> elementsAnnotatedWithProduce = roundEnv.getElementsAnnotatedWith(Produce.class);
        for (Element element : elementsAnnotatedWithProduce) {
            // validate
            if (element.getKind() != ElementKind.METHOD) {
                Logger.error("mlesiewski.simpleioc.Produce is only applicable for methods", element);
                return true;
            }
            Set<Modifier> modifiers = element.getModifiers();
            if (modifiers.contains(Modifier.ABSTRACT)) {
                Logger.error("mlesiewski.simpleioc.Produce is only applicable for non-abstract methods", element);
                return true;
            }
            if (!modifiers.contains(Modifier.PUBLIC)) {
                Logger.error("mlesiewski.simpleioc.Produce is only applicable public methods", element);
                return true;
            }
            if (modifiers.contains(Modifier.STATIC)) {
                Logger.error("mlesiewski.simpleioc.Produce is only applicable for non-static methods", element);
                return true;
            }

            // create provider class
            ExecutableElement method = (ExecutableElement) element;
            TypeMirror returnType = method.getReturnType();
            String returnTypeName = returnType.toString();
            String beanProviderName = returnTypeName + "BeanProvider";
            String beanProviderSimpleName = beanProviderName.substring(beanProviderName.lastIndexOf("."));
            String producerTypeName = element.getEnclosingElement().asType().toString();
            String producerMethodName = element.getSimpleName().toString();
            try {
                JavaFileObject classFile = processingEnv.getFiler().createClassFile(beanProviderName);
                Writer writer = classFile.openWriter();
                Template produceBeanProvider = Template.get("ProduceBeanProvider");
                HashMap<String, String> params = new HashMap<>();
                params.put("returnTypeName", returnTypeName);
                params.put("beanProviderSimpleName", beanProviderSimpleName);
                params.put("producerTypeName", producerTypeName);
                params.put("producerMethodName", producerMethodName);
                String text = produceBeanProvider.compile(params);
                writer.write(text);
                writer.close();

            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "dupa wielka " + e.getMessage(), element);
            }
        }

        return true;
    }
}
