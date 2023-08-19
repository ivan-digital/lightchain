package digital.ivan.lightchain.storage.exception;

public class MessageParsingException extends RuntimeException {
    public MessageParsingException(Exception msg) {
        super(msg);
    }
}
