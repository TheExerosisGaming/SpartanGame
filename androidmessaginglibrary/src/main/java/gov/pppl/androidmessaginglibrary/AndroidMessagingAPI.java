package gov.pppl.androidmessaginglibrary;

import gov.pppl.androidmessaginglibrary.bluetooth.BluetoothManager;
import gov.pppl.androidmessaginglibrary.bluetooth.state.BluetoothState;
import gov.pppl.androidmessaginglibrary.event.CustomEventManager;
import gov.pppl.androidmessaginglibrary.event.bluetooth.BluetoothStateChangeEvent;
import gov.pppl.androidmessaginglibrary.util.InstancePool;

public class AndroidMessagingAPI {
    public static final String APP_UUID = "28347afe-c532-447b-97d6-8199562779da";
    public static final String REDIS_IP = "96.248.68.194";

    private static InstancePool instancePool;
    private static BluetoothState bluetoothState = BluetoothState.NONE;
    private static CustomEventManager eventManager;
    private static BluetoothManager bluetoothManager;

    static {
        instancePool = new InstancePool();

        eventManager = new CustomEventManager();
        bluetoothManager = new BluetoothManager();

        instancePool.add(eventManager);
        instancePool.add(bluetoothManager);
    }

    private AndroidMessagingAPI() {}

    public static CustomEventManager getEventManager() {
        return eventManager;
    }

    public static BluetoothManager getBluetoothManager() {
        return bluetoothManager;
    }

    public static void setState(BluetoothState state){
        eventManager.callEvent(new BluetoothStateChangeEvent(state, bluetoothState));
        bluetoothState = state;
    }
    public static BluetoothState getState() {
        return bluetoothState;
    }
    public static InstancePool getInstancePool() {
        return instancePool;
    }

}