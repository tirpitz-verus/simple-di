package mlesiewski.simpledi;

/** A technical exception. */
public class SimpleDiException extends RuntimeException {

    public SimpleDiException(String message) {
        super(message);
    }

    public SimpleDiException(String message, Throwable cause) {
        super(message, cause);
    }

    public SimpleDiException(Throwable cause) {
        super(cause);
    }
}
