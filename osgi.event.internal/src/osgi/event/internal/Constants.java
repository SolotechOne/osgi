package osgi.event.internal;

public final class Constants {
 
    private Constants() {
        // private default constructor for constants class
        // to avoid someone extends the class
    }
 
    public static final String TOPIC_BASE = "net/globus/automic/shell/message/event/constants/";
    public static final String TOPIC_MESSAGE = TOPIC_BASE + "MESSAGE";
    public static final String TOPIC_ALL = TOPIC_BASE + "*";
 
    public static final String PROPERTY_KEY_MESSAGE_TEXT = "text";
    public static final String PROPERTY_KEY_MESSAGE_TIMESTAMP = "timestamp";
    public static final String PROPERTY_KEY_MESSAGE_TYPE = "type";
    public static final String PROPERTY_KEY_MESSAGE_NUMBER = "number";
    public static final String PROPERTY_KEY_MESSAGE_INSERTS = "inserts";
}