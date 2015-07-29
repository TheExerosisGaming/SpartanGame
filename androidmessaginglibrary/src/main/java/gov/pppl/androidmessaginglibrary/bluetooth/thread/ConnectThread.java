package gov.pppl.androidmessaginglibrary.bluetooth.thread;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

import gov.pppl.androidmessaginglibrary.AndroidMessagingAPI;
import gov.pppl.androidmessaginglibrary.bluetooth.BluetoothManager;
import gov.pppl.androidmessaginglibrary.event.bluetooth.conection.BluetoothConnectionFailedEvent;
import gov.pppl.androidmessaginglibrary.util.StreamUtil;

/**
 * Created by The Exerosis on 7/24/2015.
 */
public class ConnectThread extends NetworkThread {
    private BluetoothAdapter adapter;
    private BluetoothSocket socket = null;
    private final BluetoothDevice device;

    public ConnectThread(BluetoothDevice device, BluetoothManager manager){
        super(manager);
        adapter = BluetoothAdapter.getDefaultAdapter();
        this.device = device;

        // Get a BluetoothSocket for a connection with the given BluetoothDevice
        try {
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString(AndroidMessagingAPI.APP_UUID));
        } catch (IOException e) {e.printStackTrace();}
    }

    public void run() {
        // Always cancel discovery because it will slow down a connection
        adapter.cancelDiscovery();

        // Make a connection to the BluetoothSocket
        try {
            // This is a blocking call and will only return on a successful connection or an exception
            socket.connect();
        } catch (IOException e) {
            StreamUtil.closeQuietly(socket);
            AndroidMessagingAPI.getEventManager().callEvent(new BluetoothConnectionFailedEvent(device));
            return;
        }

        // Reset the ConnectThread because we're done
        synchronized (getManager()) {
            getManager().cancelThread(this);
        }

        // Start the connected thread
        getManager().connected(socket, device);
    }

    public void cancel() {
        StreamUtil.closeQuietly(socket);
    }
}
