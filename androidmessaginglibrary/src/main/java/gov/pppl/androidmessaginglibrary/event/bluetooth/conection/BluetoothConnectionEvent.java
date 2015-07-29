package gov.pppl.androidmessaginglibrary.event.bluetooth.conection;

import android.bluetooth.BluetoothDevice;

import gov.pppl.androidmessaginglibrary.event.bluetooth.BluetoothEvent;

/**
 * Created by The Exerosis on 7/27/2015.
 */
public class BluetoothConnectionEvent implements BluetoothEvent {
    private BluetoothDevice device;

    public BluetoothConnectionEvent(BluetoothDevice device){
        this.device = device;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }
}
