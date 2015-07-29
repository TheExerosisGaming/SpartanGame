package gov.pppl.androidmessaginglibrary.event.bluetooth;

import gov.pppl.androidmessaginglibrary.event.MessageReceivedEvent;

/**
 * Created by The Exerosis on 7/24/2015.
 */
public class BluetoothReceivedMessageEvent extends MessageReceivedEvent<Object> implements BluetoothEvent {
    public BluetoothReceivedMessageEvent(Object message){
        super(message);
    }
}
