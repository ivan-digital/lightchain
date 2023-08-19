package digital.ivan.lightchain.core.exception;

/**
 * The NoRouteAvailableException class extends the RuntimeException class and is used to indicate that no
 * routing option is available for the request at hand.
 */
public class NoRouteAvailableException extends RuntimeException {

    /**
     * Constructor to create a new instance of NoRouteAvailableException.
     *
     * @param msg the detail message associated with the exception.
     */
    public NoRouteAvailableException(String msg) {
        super(msg);
    }
}
