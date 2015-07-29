package gov.pppl.androidmessaginglibrary.event;

/**
 * Created by The Exerosis on 7/27/2015.
 */
public class MessageReceivedEvent <T> {
    private T message;

    public MessageReceivedEvent(T message) {
        this.message = message;
    }

    public T getMessage() {
        return message;
    }
}
