package mlesiewski.simpleioc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;

/** Templates for Java class code generation. */
class Template {

    /** holds a template text */
    private final StringBuilder text;

    /** a constructor */
    private Template(StringBuilder text) {
        this.text = text;
    }

    /** Creates a template instance by name */
    static Template get(String name) throws IOException {

        InputStream templateAsStream = Template.class.getClass().getResourceAsStream("/" + name + ".txt");
        if (templateAsStream == null) {
            throw new SimpleIocException("no template named '" + name + "'");
        }
        Scanner scanner = new Scanner(templateAsStream);
        StringBuilder text = new StringBuilder();
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            text.append(line).append(System.lineSeparator());
        }
        return new Template(text);
    }

    /** Compiles a template with a given values. */
    String compile(Map<String, String> input) {
        String result = text.toString();
        for(Map.Entry<String, String> entry : input.entrySet()) {
            result = result.replaceAll("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return result;
    }
}
