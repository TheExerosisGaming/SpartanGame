package gov.pppl.androidmessaginglibrary.bluetooth.thread;

import android.bluetooth.BluetoothSocket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;

import gov.pppl.androidmessaginglibrary.AndroidMessagingAPI;
import gov.pppl.androidmessaginglibrary.bluetooth.BluetoothManager;
import gov.pppl.androidmessaginglibrary.event.bluetooth.BluetoothReceivedMessageEvent;
import gov.pppl.androidmessaginglibrary.event.bluetooth.conection.BluetoothConnectionLostEvent;
import gov.pppl.androidmessaginglibrary.util.StreamUtil;

/**
 * Created by The Exerosis on 7/24/2015.
 */
public class ConnectedThread extends NetworkThread {
    private final BluetoothSocket socket;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;

    public ConnectedThread(BluetoothSocket socket, BluetoothManager manager) {
        super(manager);
        this.socket = socket;
        // Get the BluetoothSocket input and output streams
        try {
            inputStream = new BufferedInputStream(socket.getInputStream());
            outputStream = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        // Keep listening to the InputStream while connected
        while (true) {
            try {
                Object object = null;
                try {
                    object = new ObjectInputStream(inputStream).readObject();
                } catch (ClassNotFoundException e) {
                }

                if (object != null) {
                    AndroidMessagingAPI.getEventManager().callEvent(new BluetoothReceivedMessageEvent(object));
                    object = null;
                }
            } catch (IOException e) {
                AndroidMessagingAPI.getEventManager().callEvent(new BluetoothConnectionLostEvent(socket.getRemoteDevice()));
                // Start the service over to restart listening mode
                getManager().start();
                break;
            }
        }
    }

    @Override
    public void cancel() {
        StreamUtil.closeQuietly(socket);
        StreamUtil.closeQuietly(inputStream);
        StreamUtil.closeQuietly(outputStream);
    }
}
