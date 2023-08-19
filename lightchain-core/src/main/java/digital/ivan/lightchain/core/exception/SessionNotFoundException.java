package digital.ivan.lightchain.core.exception;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(String msg) {
        super(msg);
    }
}
