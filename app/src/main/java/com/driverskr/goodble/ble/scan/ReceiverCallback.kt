package com.driverskr.goodble.ble.scan

/**
 * @Author: driverSkr
 * @Time: 2023/12/11 15:09
 * @Description: 广播接收器的ReceiverCallback 接口$
 */
interface ReceiverCallback {
    /**
     * 蓝牙关闭
     */
    fun bluetoothClose()

    /**
     * 位置关闭
     */
    fun locationClose()
}