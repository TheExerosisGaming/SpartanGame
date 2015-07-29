package gov.pppl.androidmessaginglibrary.bluetooth.thread;

import gov.pppl.androidmessaginglibrary.bluetooth.BluetoothManager;

/**
 * Created by The Exerosis on 7/24/2015.
 */
public abstract class NetworkThread extends  Thread {
    private BluetoothManager manager;

    public NetworkThread(BluetoothManager manager){
        this.manager = manager;
    }

    public BluetoothManager getManager() {
        return manager;
    }

    public abstract void cancel();
}
