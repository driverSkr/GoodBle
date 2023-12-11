package com.driverskr.goodble

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.driverskr.goodble.ble.BleCore

/**
 * @Author: driverSkr
 * @Time: 2023/12/11 16:44
 * @Description: $
 */
class BleApplication: Application() {

    @SuppressLint("StaticFieldLeak")
    private lateinit var context: Context

    @SuppressLint("StaticFieldLeak")
    private lateinit var bleCore: BleCore

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        //初始化Ble核心库
        bleCore = BleCore.getInstance(this)
    }

    fun getBleCore() = bleCore
}