package gov.pppl.androidmessaginglibrary.event.bluetooth.conection;

import android.bluetooth.BluetoothDevice;

/**
 * Created by The Exerosis on 7/24/2015.
 */
public class BluetoothConnectionLostEvent extends BluetoothConnectionEvent {
    public BluetoothConnectionLostEvent(BluetoothDevice device){
        super(device);
    }
}
