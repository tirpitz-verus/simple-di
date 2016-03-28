package mlesiewski.simpleioc;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by tirpitz on 2016-03-28.
 */
public abstract class BaseGenerator {
    Element element;

    public BaseGenerator(Element element) {
        this.element = element;
    }

    /** Creates java source files for {@link BeanProvider BeanProviders}. */
    public void generateCode(Filer filer) {
        validate();
        String className = newClassName();
        String text = getSourceCodeText();
        writeClassFile(filer, text, className);
    }

    /** @throws SimpleIocElementException if there is a problem with a given element */
    public abstract void validate();
    /** Performs some preparation logic, like gathering data and creating names. */
    public abstract String newClassName();
    /** @return a text with a source code */
    public abstract String getSourceCodeText();

    /** writes a class file */
    public void writeClassFile(Filer filer, String text, String className)  {
        try {
            JavaFileObject classFile = filer.createClassFile(className);
            Writer writer = classFile.openWriter();
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            throw new SimpleIocElementException("could not write a class '" + className + "' file because: " + e.getMessage());
        }
    }
}
