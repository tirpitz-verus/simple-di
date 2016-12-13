package mlesiewski.simpledi.apt.template;

import mlesiewski.simpledi.apt.SimpleDiAptException;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Templates for Java class code generation. */
public class Template {

    public static final String START = "\\{\\{";
    public static final String END = "\\}\\}";
    public static final Pattern KEY_PATTERN = Pattern.compile("(" + START + "\\w+" + END + ")");

    /** holds a template text for compilation */
    private final String text;
    /** total key count used to validate inputValues */
    private final int keyCount;

    /**
     * Constructs new Template.
     *
     * @throws SimpleDiAptException if an empty key {{}} is found in template text
     */
    Template(String text) {
        this.text = text;
        boolean emptyKey = text.contains("{{}}");
        if (emptyKey) {
            throw new SimpleDiAptException("empty key {{}} in template");
        }
        Set<String> keys = new HashSet<>();
        Matcher matcher = KEY_PATTERN.matcher(text);
        while (matcher.find()) {
            int groupCount = matcher.groupCount();
            String group = matcher.group(groupCount);
            keys.add(group);
        }
        this.keyCount = keys.size();
    }

    /**
     * Compiles a template with a given values.
     *
     * @param inputValues map containing values to be placed in place of kays in the template
     * @return String containing compiled template
     * @throws SimpleDiAptException on unused key in template or in inputValues
     */
    public String compile(Map<String, String> inputValues) {
        if (keyCount != inputValues.size()) {
            throw new SimpleDiAptException("template has " + keyCount + " keys but " + inputValues.size() + " were provided");
        }
        String result = text;
        for (Map.Entry<String, String> entry : inputValues.entrySet()) {
            result = result.replaceAll(START + entry.getKey() + END, entry.getValue());
        }
        return result;
    }
}
