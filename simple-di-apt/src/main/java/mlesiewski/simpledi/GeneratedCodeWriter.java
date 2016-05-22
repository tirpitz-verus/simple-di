package mlesiewski.simpledi;

import mlesiewski.simpledi.annotations.Registerable;
import mlesiewski.simpledi.model.BeanProviderEntity;
import mlesiewski.simpledi.model.GeneratedCode;
import mlesiewski.simpledi.model.ProducedBeanProviderEntity;
import mlesiewski.simpledi.scopes.Scope;

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

    GeneratedCodeWriter(Filer filer) {
        this.filer = filer;
    }

    void writeSourceFiles(Collection<GeneratedCode> generated) {
        generated.forEach(this::write);
    }

    /** writes registrable class */
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
                params.put("beanProducerBeanScope", entity.beanProducer().scope());
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

    /** writes service loader filer for registrable */
    void writeRegistrableServiceLoader(Collection<GeneratedCode> registrable) {
        writeServiceLoader(registrable, Registerable.class, (Writer writer, GeneratedCode aClass) -> writer.write(aClass.typeName()));
    }

    /** writes service loader filer for custom scopes */
    void writeScopeServiceLoader(Collection<String> scopes) {
        writeServiceLoader(scopes, Scope.class, Writer::write);
    }

    /** writes a service loader file */
    private <T> void writeServiceLoader(Collection<T> generated, Class service, WritingOperation<T> operation) {
        StandardLocation location = StandardLocation.CLASS_OUTPUT;
        String pkg = "";
        String relativeName = "META-INF/services/" + service.getName();
        Logger.note("attempting to write to a resource file '" + relativeName + "'");
        try {
            FileObject resource = filer.createResource(location, pkg, relativeName);
            Writer writer = resource.openWriter();
            for (T code : generated) {
                operation.apply(writer, code);
                writer.write(System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            throw new SimpleDiAptException("could not write a file '" + relativeName + "' because: " + e.getMessage());
        }
    }
}

@FunctionalInterface
interface WritingOperation<T> {
    void apply(Writer writer, T object) throws IOException;
}
