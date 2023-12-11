package com.driverskr.goodble.base

import android.app.Activity
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @Author: driverSkr
 * @Time: 2023/12/11 13:51
 * @Description: 对ViewBinding进行封装（待理解）$
 */

fun <VB : ViewBinding> viewBinding(viewInflater: (LayoutInflater) -> VB): ReadOnlyProperty<Activity, VB>
    = ActivityViewBindingProperty(viewInflater)

/**
 * 通过委托的方式进行封装
 */
class ActivityViewBindingProperty<VB: ViewBinding>(
    private val viewInflater: (LayoutInflater) -> VB
): ReadOnlyProperty<Activity, VB> {

    private var binding: VB? = null

    override fun getValue(thisRef: Activity, property: KProperty<*>): VB {
        return binding ?: viewInflater(thisRef.layoutInflater).also {
            thisRef.setContentView(it.root)
            binding = it
        }
    }
}