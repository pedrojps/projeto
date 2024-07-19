package com.example.myapplication.ui.graficos.adapter

import com.example.myapplication.common.time.LocalDateFormat.DATE2
import com.example.myapplication.data.entities.ItemCategoria
import com.example.myapplication.data.entities.ItemEnty
import com.example.myapplication.data.source.local.projection.ItensEntyProject

data class ItemsGrafic(
    val category : ItemCategoria,
    val list: ArrayList<ItemEnty>
){
    override fun toString(): String {

        val vt = StringBuilder("\n"+category.nome+"\n\n")

        list.sortByDescending{it.data}
        for (itemc in list) {
            vt.append(itemc.data.toString(DATE2)).append(": ").append(itemc.valor).append("\n")
        }
        return vt.toString()
    }
}
