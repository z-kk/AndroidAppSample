package com.example.androidappsample

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context

class BluetoothUtil(private val context: Context) {
    val adapter: BluetoothAdapter?
        get() = context.getSystemService(BluetoothManager::class.java).adapter
}