package mlesiewski.simpleioc;

/** A technical exception. */
public class SimpleIocException extends RuntimeException {

    public SimpleIocException(String message) {
        super(message);
    }

    public SimpleIocException(String message, Throwable cause) {
        super(message, cause);
    }

    public SimpleIocException(Throwable cause) {
        super(cause);
    }
}
