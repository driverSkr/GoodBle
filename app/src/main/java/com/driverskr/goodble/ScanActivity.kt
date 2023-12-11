package com.driverskr.goodble

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.driverskr.goodble.base.BaseActivity
import com.driverskr.goodble.base.viewBinding
import com.driverskr.goodble.ble.BleCore
import com.driverskr.goodble.ble.scan.BleScanCallback
import com.driverskr.goodble.ble.scan.ReceiverCallback
import com.driverskr.goodble.ble.scan.ScanReceiver
import com.driverskr.goodble.databinding.ActivityScanBinding

class ScanActivity : BaseActivity(), View.OnClickListener, BleScanCallback, ReceiverCallback {

    private val binding by viewBinding(ActivityScanBinding::inflate)

    private lateinit var bleCore: BleCore

    //蓝牙连接权限
    private val requestConnect =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            showMsg(if (it) "可以打开蓝牙" else "Android12 中不授予此权限无法打开蓝牙")
        }

    //启用蓝牙
    private val enableBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                showMsg("蓝牙已打开")
                Log.d(TAG,": 蓝牙已打开")
                bleCore.setPhyScanCallback(this@ScanActivity)
            }
        }

    //请求定位
    private val requestLocation =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val coarseLocation = result[Manifest.permission.ACCESS_COARSE_LOCATION]
            val fineLocation = result[Manifest.permission.ACCESS_FINE_LOCATION]
            if (coarseLocation == true && fineLocation == true) {
                //开始扫描设备
                showMsg("定位权限以获取")
                if (isOpenBluetooth()) bleCore.setPhyScanCallback(this@ScanActivity)
            }
        }

    //启用定位
    private val enableLocation =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                showMsg("位置已打开")
                Log.d(TAG, ": 位置已打开")
                if (isOpenBluetooth()) bleCore.setPhyScanCallback(this@ScanActivity)
            }
        }

    //蓝牙连接权限
    private val requestScan =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            showMsg(if (it) "可以开始扫描设备了" else "Android12 Android12 中不授予此权限无法扫描蓝牙")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        initView()

        bleCore = (application as BleApplication).getBleCore()
        //设置扫描回调
        if (isOpenBluetooth()) bleCore.setPhyScanCallback(this@ScanActivity)

        //注册广播
        registerReceiver(
            ScanReceiver().apply { setCallback(this@ScanActivity) },
            IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
                addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
            })
    }

    override fun onResume() {
        super.onResume()
        if (isAndroid12()) {
            //蓝牙连接
            binding.requestBluetoothConnectLay.root.visibility = if (hasBluetoothConnect()) View.GONE else View.VISIBLE
            if (!hasBluetoothConnect()) {
                Log.d(TAG, "onResume: 未获取蓝牙连接权限")
                return
            }
            //打开蓝牙开关
            binding.enableBluetoothLay.root.visibility = if (isOpenBluetooth()) View.GONE else View.VISIBLE
            if (!isOpenBluetooth()) {
                Log.d(TAG, "onResume: 未打开蓝牙")
                return
            }
            //蓝牙扫描
            binding.requestBluetoothScanLay.root.visibility = if (hasBluetoothScan()) View.GONE else View.VISIBLE
            if (!hasBluetoothScan()) {
                Log.d(TAG, "onResume: 未获取蓝牙扫描权限")
                return
            }
        }
        //打开蓝牙
        binding.enableBluetoothLay.root.visibility = if (isOpenBluetooth()) View.GONE else View.VISIBLE
        if (!isOpenBluetooth()) {
            Log.d(TAG, "onResume: 未打开蓝牙")
            return
        }
        //打开定位
        binding.enableLocationLay.root.visibility = if (isOpenLocation()) View.GONE else View.VISIBLE
        if (!isOpenLocation()) {
            Log.d(TAG, "onResume: 未打开位置")
            return
        }
        //请求定位
        binding.requestLocationLay.root.visibility = if (hasCoarseLocation() && hasAccessFineLocation()) View.GONE else View.VISIBLE
        if (!hasAccessFineLocation()) {
            Log.d(TAG, "onResume: 未获取定位权限")
            return
        }
        binding.tvScanStatus.visibility = View.VISIBLE
        //开始扫描

    }

    override fun onStop() {
        super.onStop()
        //页面停止时停止扫描
        if (bleCore.isScanning()) stopScan()
    }

    private fun initView() {
        binding.requestBluetoothConnectLay.btnRequestConnectPermission.setOnClickListener(this)
        binding.enableBluetoothLay.btnEnableBluetooth.setOnClickListener(this)
        binding.requestLocationLay.btnRequestLocationPermission.setOnClickListener(this)
        binding.enableLocationLay.btnEnableLocation.setOnClickListener(this)
        binding.requestBluetoothScanLay.btnRequestScanPermission.setOnClickListener(this)
        binding.toolbar.setOnClickListener(this)
        binding.tvScanStatus.setOnClickListener(this)
    }

    companion object {
        private val TAG = ScanActivity::class.java.simpleName
    }

    override fun onClick(v: View) {
        when (v.id) {
            //请求蓝牙连接权限
            R.id.btn_request_connect_permission -> if (isAndroid12()) requestConnect.launch(Manifest.permission.BLUETOOTH_CONNECT)
            //打开蓝牙开关
            R.id.btn_enable_bluetooth -> enableBluetooth.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            //请求定位权限
            R.id.btn_request_location_permission -> requestLocation.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
            //打开位置开关
            R.id.btn_enable_location -> enableLocation.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            //请求蓝牙扫描权限
            R.id.btn_request_scan_permission -> if (isAndroid12()) requestScan.launch(Manifest.permission.BLUETOOTH_SCAN)
            //扫描或停止扫描
            R.id.tv_scan_status -> if (bleCore.isScanning()) stopScan() else startScan()
            else -> {}
        }
    }

    private fun startScan() {
        bleCore.startScan()
        binding.tvScanStatus.text = "停止"
        binding.pbScanLoading.visibility = View.VISIBLE
    }

    private fun stopScan() {
        bleCore.stopScan()
        binding.tvScanStatus.text = "搜索"
        binding.pbScanLoading.visibility = View.INVISIBLE
    }

    /**
     * 扫描接口
     */
    override fun onScanResult(result: ScanResult) {
        TODO("Not yet implemented")
    }

    /**
     * 蓝牙关闭
     */
    override fun bluetoothClose() {
        //蓝牙关闭时停止扫描
        if (bleCore.isScanning()) {
            stopScan()
            binding.enableBluetoothLay.root.visibility = View.VISIBLE
        }
    }

    /**
     * 位置关闭
     */
    override fun locationClose() {
        //位置关闭时停止扫描
        if (bleCore.isScanning()) {
            stopScan()
            binding.enableLocationLay.root.visibility = View.VISIBLE
        }
    }
}