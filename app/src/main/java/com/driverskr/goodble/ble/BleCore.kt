package com.driverskr.goodble.ble

import android.annotation.SuppressLint
import android.content.Context
import com.driverskr.goodble.ble.scan.BleScan
import com.driverskr.goodble.ble.scan.BleScanCallback

/**
 * @Author: driverSkr
 * @Time: 2023/12/11 14:55
 * @Description: $
 */
class BleCore private constructor(private val context: Context) {

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: BleCore? = null

        @SuppressLint("StaticFieldLeak")
        private lateinit var bleScan: BleScan

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: BleCore(context).also {
                instance = it
                //蓝牙扫描
                bleScan = BleScan.getInstance(context)
            }
        }
    }

    fun setPhyScanCallback(bleScanCallback: BleScanCallback) {
        bleScan.setPhyScanCallback(bleScanCallback)
    }

    fun isScanning() = bleScan.isScanning()

    fun startScan() = bleScan.startScan()

    fun stopScan() = bleScan.stopScan()
}