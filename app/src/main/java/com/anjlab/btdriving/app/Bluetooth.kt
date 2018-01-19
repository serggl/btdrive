/**
 * Copyright 2018 AnjLab
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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