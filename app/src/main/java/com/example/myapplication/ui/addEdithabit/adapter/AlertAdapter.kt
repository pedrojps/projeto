package com.example.myapplication.ui.addEdithabit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.entities.AlertCategori
import com.example.myapplication.databinding.ItemAlertBinding
import com.example.myapplication.databinding.ItemIconeBinding
import com.example.myapplication.ui.select_image.IconeViewHolder

class AlertAdapter: RecyclerView.Adapter<AlertViewHolder>()  {
    var list: ArrayList<AlertCategori> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return AlertViewHolder(ItemAlertBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val item = list[position]

        holder.onBind(item, position){
            list.remove(item)
            this.notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    public fun addItem(alert: AlertCategori){
        list.add(alert)
        this.notifyDataSetChanged()
    }

    fun setListItems(list: ArrayList<AlertCategori>){
        this.list = list
        this.notifyDataSetChanged()
    }

}