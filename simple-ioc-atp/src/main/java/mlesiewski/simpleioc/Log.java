package mlesiewski.simpleioc;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

class Log {

    private static Messager messager;

    static void set(Messager m) {
        messager = m;
    }

    static void error(String message, Element element) {
        messager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    static void error(String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, message);
    }

    static void note(String message) {
        messager.printMessage(Diagnostic.Kind.NOTE, message);
    }
}
