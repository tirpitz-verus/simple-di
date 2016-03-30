package mlesiewski.simpleioc;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseGenerator {

    final Set<String> created = new HashSet<>();
    Element element;

    public BaseGenerator(Element element) {
        this.element = element;
    }

    /** Creates java source files for {@link BeanProvider BeanProviders}. */
    public void generateCode(Filer filer) {
        validate();
        String className = newClassName();
        if (alreadyCreated(className)) {
            Logger.note("skipping creation of '" + className + "' - already created");
        } else {
            String text = getSourceCodeText();
            writeClassFile(filer, text, className);
        }
    }

    /** @return wether the type provided was already created */
    public boolean alreadyCreated(String className) {
        return created.contains(className);
    }

    /** @throws SimpleIocAptException if there is a problem with a given element */
    public abstract void validate();

    /** Performs some preparation logic, like gathering data and creating names. */
    public abstract String newClassName();

    /** @return a text with a source code */
    public abstract String getSourceCodeText();

    /** writes a class file */
    public void writeClassFile(Filer filer, String text, String className)  {
        Logger.note("attempting to create source file for '" + className + "'");
        try {
            JavaFileObject classFile = filer.createSourceFile(className);
            Writer writer = classFile.openWriter();
            writer.write(text);
            writer.close();
            created.add(className);
        } catch (IOException e) {
            throw new SimpleIocAptException("could not write a class '" + className + "' file because: " + e.getMessage());
        }
    }
}
