package gov.pppl.androidmessaginglibrary.event.redis;

import gov.pppl.androidmessaginglibrary.event.MessageReceivedEvent;

/**
 * Created by The Exerosis on 7/27/2015.
 */
public class RedisMessageReceivedEvent extends MessageReceivedEvent<String> implements RedisEvent {
    private String channel;

    public RedisMessageReceivedEvent(String message, String channel){
        super(message);
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }
}
