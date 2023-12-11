package com.driverskr.goodble

import android.os.Bundle
import com.driverskr.goodble.base.BaseActivity
import com.driverskr.goodble.base.viewBinding
import com.driverskr.goodble.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding.toolbar.setNavigationOnClickListener { jumpActivity(ScanActivity::class.java) }
    }
}