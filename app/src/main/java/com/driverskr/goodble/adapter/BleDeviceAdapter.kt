package com.driverskr.goodble.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.driverskr.goodble.ble.BleDevice
import com.driverskr.goodble.databinding.ItemDeviceRvBinding
import java.util.*

/**
 * @Author: driverSkr
 * @Time: 2023/12/12 9:44
 * @Description: $
 */
class BleDeviceAdapter(private val mDevices: List<BleDevice>): RecyclerView.Adapter<BleDeviceAdapter.ViewHolder>() {

    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(mOnItemClickListener: OnItemClickListener?) {
        this.mOnItemClickListener = mOnItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder = ViewHolder(ItemDeviceRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        viewHolder.binding.itemDevice.setOnClickListener { v ->
            if (mOnItemClickListener != null) mOnItemClickListener!!.onItemClick(v, viewHolder.adapterPosition)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bleDevice: BleDevice = mDevices[position]
        val rssi: Int = bleDevice.rssi
        holder.binding.tvRssi.text = String.format(Locale.getDefault(), "%d dBm", rssi)
        //设备名称
        holder.binding.tvDeviceName.text = bleDevice.realName
        //Mac地址
        holder.binding.tvMacAddress.text = bleDevice.macAddress
    }

    override fun getItemCount() = mDevices.size

    class ViewHolder(itemView: ItemDeviceRvBinding): RecyclerView.ViewHolder(itemView.root) {
        var binding: ItemDeviceRvBinding
        init {
            binding = itemView
        }
    }
}