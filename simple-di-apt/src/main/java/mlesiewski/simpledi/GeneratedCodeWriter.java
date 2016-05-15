package mlesiewski.simpledi;

import mlesiewski.simpledi.model.BeanProviderEntity;
import mlesiewski.simpledi.model.GeneratedCode;
import mlesiewski.simpledi.model.ProducedBeanProviderEntity;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Writes {@link mlesiewski.simpledi.model.GeneratedCode}.
 */
class GeneratedCodeWriter {

    private final Filer filer;
    private Writer writer = null;

    GeneratedCodeWriter(Filer filer) {
        this.filer = filer;
    }

    void writeSourceFiles(Collection<GeneratedCode> generated) {
        generated.forEach(this::write);
    }

    private void write(GeneratedCode generated) {
        Logger.note("attempting to create a source file for '" + generated.typeName() + "'");
        try {
            JavaFileObject classFile = filer.createSourceFile(generated.typeName());
            Writer writer = classFile.openWriter();
            Template template;
            Map<String, String> params = new HashMap<>();
            if (generated instanceof ProducedBeanProviderEntity) {
                ProducedBeanProviderEntity entity = (ProducedBeanProviderEntity) generated;
                template = TemplateFactory.get("ProducedBeanProviderImplementation");
                params.put("beanProviderPackage", entity.packageName());
                params.put("beanProviderSimpleName", entity.simpleName());
                params.put("beanType", entity.beanEntity().typeName());
                params.put("beanName", entity.beanEntity().name());
                params.put("beanScope", entity.beanEntity().scope());
                params.put("beanProducerBeanName", entity.beanProducer().typeName());
                params.put("beanProducerMethod", entity.producerMethod());
            } else if (generated instanceof BeanProviderEntity) {
                BeanProviderEntity entity = (BeanProviderEntity) generated;
                template = TemplateFactory.get("BeanProviderImplementation");
                params.put("beanProviderPackage", entity.packageName());
                params.put("beanProviderSimpleName", entity.simpleName());
                params.put("beanType", entity.beanEntity().typeName());
                params.put("beanName", entity.beanEntity().name());
                params.put("beanScope", entity.beanEntity().scope());
            } else {
                throw new SimpleDiAptException("could not find template for " + generated.getClass().getName());
            }
            String text = template.compile(params);
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            throw new SimpleDiAptException("could not write a class '" + generated.typeName() + "' file because: " + e.getMessage());
        }
    }

    void writeServiceLoader(Collection<GeneratedCode> generated) {
        StandardLocation location = StandardLocation.CLASS_OUTPUT;
        String pkg = "";
        String relativeName = "META-INF/services/mlesiewski.simpledi.annotations.Registerable";
        Logger.note("attempting to write to a resource file '" + relativeName + "'");
        try {
            if (writer == null) {
                FileObject resource = filer.createResource(location, pkg, relativeName);
                writer = resource.openWriter();
            }
            for (GeneratedCode code : generated) {
                writer.write(code.typeName());
                writer.write(System.lineSeparator());
            }
            writer.flush();
        } catch (IOException e) {
            throw new SimpleDiAptException("could not write a file '" + relativeName + "' because: " + e.getMessage());
        }
    }
}
