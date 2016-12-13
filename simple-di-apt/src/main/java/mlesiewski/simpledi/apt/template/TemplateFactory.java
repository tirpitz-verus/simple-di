package mlesiewski.simpledi.apt.template;

import mlesiewski.simpledi.apt.SimpleDiAptException;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.HashMap;

/** Creates and caches {@link Template} instances. */
public class TemplateFactory {

    private static final HashMap<String, Template> TEMPLATES = new HashMap<>();
    public static final String EXTENSION = ".txt";
    private static Filer FILER;

    /** {@link Filer} is needed for getting resource files. */
    public static void set(Filer filer) {
        FILER = filer;
    }

    /** Returns {@link Template} instance by name. */
    public static Template get(String name) {
        Template result;
        if (!TEMPLATES.containsKey(name)) {
            try {
                result = createTemplate(name);
            } catch (IOException e) {
                throw new SimpleDiAptException("Cannot create template '" + name + "'");
            }
            TEMPLATES.put(name, result);
        } else {
            result = TEMPLATES.get(name);
        }
        return result;
    }

    /** Creates {@link Template}. */
    private static Template createTemplate(String name) throws IOException {
        FileObject resource = FILER.getResource(StandardLocation.CLASS_PATH, "", name + EXTENSION);
        CharSequence charContent = resource.getCharContent(false);
        return new Template(charContent.toString());
    }

}
