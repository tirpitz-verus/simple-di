package mlesiewski.simpleioc;

import javax.lang.model.element.Element;

public class SimpleIocAptException extends RuntimeException {

    final Element element;

    public SimpleIocAptException(String message, Element element) {
        super(message);
        this.element = element;
    }

    public SimpleIocAptException(String message) {
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
