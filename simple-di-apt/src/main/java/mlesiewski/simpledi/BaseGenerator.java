package mlesiewski.simpledi;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

public abstract class BaseGenerator {

    /** Creates java source files for {@link BeanProvider BeanProviders}. */
    public void generateCode(Filer filer) {
        validate();
        String className = newClassName();
        String text = getSourceCodeText();
        writeClassFile(filer, text, className);
    }

    /** @throws SimpleDiAptException if there is a problem with a given element */
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
        } catch (IOException e) {
            throw new SimpleDiAptException("could not write a class '" + className + "' file because: " + e.getMessage());
        }
    }
}
