package digital.ivan.lightchain.storage.exception;

public class RedisParsingException extends RuntimeException {
    public RedisParsingException(String msg, Throwable e) {
        super(msg, e);
    }
}
