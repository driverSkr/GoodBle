package com.driverskr.goodble.adapter

import android.view.View

/**
 * @Author: driverSkr
 * @Time: 2023/12/11 18:30
 * @Description: 用于实现Item的点击监听$
 */
interface OnItemClickListener {

    fun onItemClick(view: View?, position: Int)
}