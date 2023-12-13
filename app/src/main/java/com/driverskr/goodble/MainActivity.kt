package com.driverskr.goodble

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.driverskr.goodble.base.BaseActivity
import com.driverskr.goodble.base.viewBinding
import com.driverskr.goodble.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    @SuppressLint("MissingPermission")
    private val scanIntent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data == null) return@registerForActivityResult
                //获取选中的设备
                val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data!!.getParcelableExtra("device")
                } else {
                    result.data!!.getParcelableExtra("device") as BluetoothDevice?
                }
                showMsg("${device?.name} , ${device?.address}")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding.toolbar.setNavigationOnClickListener { scanIntent.launch(Intent(this, ScanActivity::class.java)) }
    }
}