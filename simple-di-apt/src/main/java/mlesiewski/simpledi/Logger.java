package mlesiewski.simpledi;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public class Logger {

    private static final String PREFIX = "|mlesiewski.simpledi.Logger| ";

    private static Messager messager;

    static void set(Messager m) {
        messager = m;
    }

    public static void error(String message, Element element) {
        messager.printMessage(Diagnostic.Kind.ERROR, PREFIX + message, element);
    }

    public static void error(String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, PREFIX + message);
    }

    public static void note(String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, PREFIX + message);
    }
}
