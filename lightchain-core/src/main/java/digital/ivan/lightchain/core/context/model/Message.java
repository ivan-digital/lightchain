package digital.ivan.lightchain.core.context.model;

public class Message {
    private String type;
    private String value;
    private long utc;

    public Message() {
    }

    public Message(String type, String value, long utc) {
        this.type = type;
        this.value = value;
        this.utc = utc;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public long getUtc() {
        return utc;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
