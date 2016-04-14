package mlesiewski.simpledi;

import javax.lang.model.element.Element;

public class SimpleDiAptException extends RuntimeException {

    final Element element;

    public SimpleDiAptException(String message, Element element) {
        super(message);
        this.element = element;
    }

    public SimpleDiAptException(String message) {
        super(message);
        this.element = null;
    }

    public Element getElement() {
        return element;
    }

    public boolean hasElement() {
        return element != null;
    }
}
