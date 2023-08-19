package digital.ivan.lightchain.core.exception;

/**
 * The OutputProcessingException class extends the RuntimeException class and is used to indicate that there
 * was an error during the processing of the output from the Large Language Model (LLM).
 */
public class OutputProcessingException extends RuntimeException {

    /**
     * Constructor to create a new instance of OutputProcessingException.
     *
     * @param msg the detail message associated with the exception.
     */
    public OutputProcessingException(String msg) {
        super(msg);
    }
}
