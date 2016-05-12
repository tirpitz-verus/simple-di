package mlesiewski.simpledi;

import java.util.regex.Pattern;

/**
 * Validates {@link mlesiewski.simpledi.annotations.Bean} names.
 * A valid name contains only letters, numbers, dashes and underscores.
 */
public class BeanNameValidator {

    private final Pattern PATTERN = Pattern.compile("^[-\\w\\d_]+$");

    /**
     * Validates names. A valid name contains only letters, numbers, dashes and underscores.
     *
     * @param name bean name to validate
     * @return true if and only if the name provided is valid
     */
    public boolean isAValidName(String name) {
        return name != null && PATTERN.matcher(name).find();
    }
}
