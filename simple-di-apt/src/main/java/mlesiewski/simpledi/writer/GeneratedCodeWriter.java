package mlesiewski.simpledi.writer;

import mlesiewski.simpledi.Logger;
import mlesiewski.simpledi.SimpleDiAptException;
import mlesiewski.simpledi.annotations.Registerable;
import mlesiewski.simpledi.model.*;
import mlesiewski.simpledi.scopes.Scope;
import mlesiewski.simpledi.template.Template;
import mlesiewski.simpledi.template.TemplateFactory;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Writes {@link mlesiewski.simpledi.model.GeneratedCode}.
 */
public class GeneratedCodeWriter {

    private final Filer filer;

    public GeneratedCodeWriter(Filer filer) {
        this.filer = filer;
    }

    public void writeSourceFiles(Collection<GeneratedCode> generated) {
        generated.forEach(this::write);
    }

    /** writes registrable class */
    private void write(GeneratedCode generated) {
        String typeName = generated.typeName();
        int dot = typeName.lastIndexOf(".");
        String pkg = typeName.substring(0, dot);
        CharSequence relativeName = typeName.substring(dot + 1, typeName.length()) + ".java";
        Logger.note("attempting to create a source file for '" + typeName + "'");
        try {
            FileObject resource = filer.createResource(StandardLocation.SOURCE_OUTPUT, pkg, relativeName);
            Writer writer = resource.openWriter();
            Template template = getTemplateFor(generated);
            Map<String, String> params = getTemplateParametersFor(generated);
            String text = template.compile(params);
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            throw new SimpleDiAptException("could not write a class '" + typeName + "' file because: " + e.getMessage());
        }
    }

    private Map<String, String> getTemplateParametersFor(GeneratedCode generated) {
        if (generated instanceof ProducedBeanProviderEntity) {
            return new ProducedBeanProviderEntityTemplateParameters(generated);
        } else if (generated instanceof BeanProviderEntity) {
            return new BeanProviderEntityTemplateParameters(generated);
        } else {
            throw new SimpleDiAptException("could not find template for " + generated.getClass().getName());
        }
    }

    private Template getTemplateFor(GeneratedCode generated) {
        if (generated instanceof ProducedBeanProviderEntity) {
            return TemplateFactory.get("ProducedBeanProviderImplementation");
        } else if (generated instanceof BeanProviderEntity) {
            return TemplateFactory.get("BeanProviderImplementation");
        } else {
            throw new SimpleDiAptException("could not find template for " + generated.getClass().getName());
        }
    }

    /** writes service loader filer for registrable */
    public void writeRegistrableServiceLoader(Collection<GeneratedCode> registrable) {
        writeServiceLoader(registrable, Registerable.class, (Writer writer, GeneratedCode aClass) -> writer.write(aClass.typeName()));
    }

    /** writes service loader filer for custom scopes */
    public void writeScopeServiceLoader(Collection<String> scopes) {
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