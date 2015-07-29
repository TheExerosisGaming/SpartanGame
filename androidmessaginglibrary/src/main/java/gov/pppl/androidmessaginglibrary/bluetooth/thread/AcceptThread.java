package gov.pppl.androidmessaginglibrary.bluetooth.thread;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

import gov.pppl.androidmessaginglibrary.AndroidMessagingAPI;
import gov.pppl.androidmessaginglibrary.bluetooth.BluetoothManager;
import gov.pppl.androidmessaginglibrary.bluetooth.state.BluetoothState;
import gov.pppl.androidmessaginglibrary.util.StreamUtil;

/**
 * Created by The Exerosis on 7/24/2015.
 */
public class AcceptThread extends NetworkThread {
    private BluetoothServerSocket serverSocket;

    public AcceptThread(BluetoothManager manager) {
        super(manager);
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        try {
            serverSocket = adapter.listenUsingRfcommWithServiceRecord("GameConnection", UUID.fromString(AndroidMessagingAPI.APP_UUID));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        BluetoothSocket socket = null;

        while (!AndroidMessagingAPI.getState().equals(BluetoothState.CONNECTED)) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (socket != null)
            switch (AndroidMessagingAPI.getState()) {
                case LISTENING:
                case CONNECTING:
                    // Situation normal. Start the connected thread.
                    getManager().connected(socket, socket.getRemoteDevice());
                    break;
                case NONE:
                case CONNECTED:
                    // Either not ready or already connected. Terminate new socket.
                    StreamUtil.closeQuietly(socket);
                    break;
            }


    }

    public void cancel() {
        StreamUtil.closeQuietly(serverSocket);
    }
}