package com.driverskr.goodble.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt

/**
 * @Author: driverSkr
 * @Time: 2023/12/11 18:25
 * @Description: $
 */
data class BleDevice(
    var realName: String? = "Unknown device", //蓝牙设备真实名称
    var macAddress: String, //蓝牙设备Mac地址
    var rssi: Int, //信号强度
    var device: BluetoothDevice,//蓝牙设备
    var gatt: BluetoothGatt? = null//gatt
)
