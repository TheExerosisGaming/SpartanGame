package gov.pppl.androidmessaginglibrary.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;

import gov.pppl.androidmessaginglibrary.AndroidMessagingAPI;
import gov.pppl.androidmessaginglibrary.bluetooth.state.BluetoothState;
import gov.pppl.androidmessaginglibrary.bluetooth.thread.AcceptThread;
import gov.pppl.androidmessaginglibrary.bluetooth.thread.ConnectThread;
import gov.pppl.androidmessaginglibrary.bluetooth.thread.ConnectedThread;
import gov.pppl.androidmessaginglibrary.bluetooth.thread.NetworkThread;
import gov.pppl.androidmessaginglibrary.event.bluetooth.conection.BluetoothConnectionMadeEvent;

/**
 * Created by The Exerosis on 7/24/2015.
 */
public class BluetoothManager {
    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;

    public BluetoothManager() {}

    /**
     * Start AcceptThread to begin a session in listening (server) mode.
     * Called by the Activity onResume()
     */
    public synchronized void start() {
        // Cancel any thread attempting to make a connection
        cancelThread(connectThread);

        // Cancel any thread currently running a connection
        cancelThread(connectedThread);

        AndroidMessagingAPI.setState(BluetoothState.LISTENING);

        // Start the thread to listen on a BluetoothServerSocket
        startThread(acceptThread);
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        cancelThread(acceptThread);
        cancelThread(connectThread);
        cancelThread(connectedThread);
        AndroidMessagingAPI.setState(BluetoothState.NONE);
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     */
    public synchronized void connect(BluetoothDevice device) {
        // Cancel any thread attempting to make a connection
        if (AndroidMessagingAPI.getState().equals(BluetoothState.CONNECTING)) {
            cancelThread(connectThread);
        }
        cancelThread(connectedThread);

        // Start the thread to connect with the given device
        startThread(connectThread, device);
        AndroidMessagingAPI.setState(BluetoothState.CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        // Cancel the thread that completed the connection
        cancelThread(acceptThread);

        // Cancel any thread currently running a connection
        cancelThread(connectedThread);

        // Cancel the accept thread because we only want to connect to one device
        cancelThread(acceptThread);

        // Start the thread to manage the connection and perform transmissions
        startThread(connectedThread);

        // Send the name of the connected device back to the UI Activity
        AndroidMessagingAPI.getEventManager().callEvent(new BluetoothConnectionMadeEvent(device));
        AndroidMessagingAPI.setState(BluetoothState.CONNECTED);
    }

    public void cancelThread(NetworkThread thread) {
        if (thread != null) {
            thread.cancel();
            thread = null;
        }
    }

    private void startThread(NetworkThread thread, Object... params) {
        if (thread == null) {
            try {
                thread = thread.getClass().getConstructor(Bundle.class).newInstance(params);
            } catch (Exception e) {
                e.printStackTrace();
            }

            thread.start();
        } else
            cancelThread(thread);
        startThread(thread, params);
    }
}