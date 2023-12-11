package com.driverskr.goodble.ble.scan

import android.bluetooth.le.ScanResult

/**
 * @Author: driverSkr
 * @Time: 2023/12/11 14:56
 * @Description: 扫描回调接口$
 */
interface BleScanCallback {
    /**
     * 扫描接口
     */
    fun onScanResult(result: ScanResult)

    /**
     * 批量扫描结果
     */
    fun onBatchScanResults(results: List<ScanResult>) {}

    /**
     * 扫描错误
     */
    fun onScanFailed(failed: String) {}
}