package gov.pppl.androidmessaginglibrary.event.bluetooth;

import gov.pppl.androidmessaginglibrary.bluetooth.state.BluetoothState;

public class BluetoothStateChangeEvent implements BluetoothEvent {
        private BluetoothState newState;
        private BluetoothState oldState;

        public BluetoothStateChangeEvent (BluetoothState newState, BluetoothState oldState){
            this.newState = newState;
            this.oldState = oldState;
        }

        public BluetoothState getNewState() {
            return newState;
        }
        public void setNewState(BluetoothState newState) {
            this.newState = newState;
        }
        public BluetoothState getOldState() {
            return oldState;
        }
    }