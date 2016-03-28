package mlesiewski.simpleioc;

import javax.lang.model.element.Element;

public class SimpleIocElementException extends RuntimeException {

    final Element element;

    public SimpleIocElementException(String message, Element element) {
        super(message);
        this.element = element;
    }

    public SimpleIocElementException(String message) {
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
