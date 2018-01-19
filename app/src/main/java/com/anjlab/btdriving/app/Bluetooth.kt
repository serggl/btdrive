package com.anjlab.btdriving.app

import android.bluetooth.BluetoothAdapter

class Bluetooth {
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    fun enable() {
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled) {
            bluetoothAdapter.enable()
        }
    }

    fun disable() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled) {
            bluetoothAdapter.disable()
        }
    }
}