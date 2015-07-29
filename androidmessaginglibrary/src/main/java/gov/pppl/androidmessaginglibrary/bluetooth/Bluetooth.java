package gov.pppl.androidmessaginglibrary.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by The Exerosis on 7/23/2015.
 */
public class Bluetooth {

    public static HashMap<String, BluetoothDevice> getDeviceNames(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices =  bluetoothAdapter.getBondedDevices();
        HashMap<String, BluetoothDevice> deviceNames = new HashMap<String, BluetoothDevice>();

        for(BluetoothDevice bluetoothDevice: devices)
            deviceNames.put(bluetoothDevice.toString(), bluetoothDevice);
        return deviceNames;
    }
}