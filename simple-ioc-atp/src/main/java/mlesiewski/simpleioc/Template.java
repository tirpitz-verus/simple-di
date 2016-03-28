package mlesiewski.simpleioc;

import java.util.Map;

/** Templates for Java class code generation. */
class Template {

    /** holds a template text */
    private final StringBuilder text;

    /** a constructor */
    Template(StringBuilder text) {
        this.text = text;
    }

    /** Compiles a template with a given values. */
    String compile(Map<String, String> input) {
        String result = text.toString();
        for(Map.Entry<String, String> entry : input.entrySet()) {
            result = result.replaceAll("\\{\\{" + entry.getKey() + "\\}\\}", entry.getValue());
        }
        return result;
    }
}
