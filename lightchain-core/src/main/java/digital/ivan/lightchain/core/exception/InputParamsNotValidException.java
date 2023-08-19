package digital.ivan.lightchain.core.exception;

/**
 * The InputParamsNotValidException class extends the RuntimeException class and is used to indicate that the
 * input parameters provided to a method are not valid.
 */
public class InputParamsNotValidException extends RuntimeException {

    /**
     * Constructor to create a new instance of InputParamsNotValidException.
     *
     * @param msg the detail message associated with the exception.
     */
    public InputParamsNotValidException(String msg) {
        super(msg);
    }
}
