package com.example.myapplication.ui.graficos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.common.time.LocalDate
import com.example.myapplication.databinding.ItemGraficBinding

class AdapterGrafic: RecyclerView.Adapter<ItemsGraficHolder>()  {
    var list: ArrayList<ItemsGrafic> = ArrayList()

    var startDate = LocalDate()

    var endDate = LocalDate()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsGraficHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ItemsGraficHolder(ItemGraficBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ItemsGraficHolder, position: Int) {
        val item = list[position]

        holder.onBind(item,startDate,endDate)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    public fun addItem(alert: ItemsGrafic){
        list.add(alert)
        this.notifyDataSetChanged()
    }

    fun setListItems(list: ArrayList<ItemsGrafic>,startDate:LocalDate,endDate:LocalDate){
        this.startDate = startDate
        this.endDate = endDate
        this.list = list
        this.notifyDataSetChanged()
    }

}