package mlesiewski.simpleioc;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.HashMap;

/** Creates and caches {@link Template} instances. */
public class TemplateFactory {

    private static final HashMap<String, Template> TEMPLATES = new HashMap<>();
    private static Filer FILER;

    /** {@link Filer} is needed for getting resource files. */
    static void set(Filer filer) {
        FILER = filer;
    }

    /** Returns {@link Template} instance by name. */
    static Template get(String name) {
        Template result;
        if (!TEMPLATES.containsKey(name)) {
            try {
                result = createTemplate(name);
            } catch (IOException e) {
                throw new SimpleIocElementException("Cannot create template '" + name + "'");
            }
            TEMPLATES.put(name, result);
        } else {
            result = TEMPLATES.get(name);
        }
        return result;
    }

    /** Creates {@link Template}. */
    private static Template createTemplate(String name) throws IOException {
        FileObject resource = FILER.getResource(StandardLocation.CLASS_PATH, "", name + ".txt");
        CharSequence charContent = resource.getCharContent(false);
        StringBuilder text = new StringBuilder(charContent);
        return new Template(text);
    }

}
