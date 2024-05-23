package com.example.myapplication.ui.select_image

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SelectImageAdapter(items: List<Int>) : RecyclerView.Adapter<IconeViewHolder>()  {

    private val mItems : List<Int> = items

    private var listener: OnItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconeViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return IconeViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: IconeViewHolder, position: Int) {
        val item = mItems[position]

        holder.onBind(item)
        holder.binding.root.setOnClickListener { listener?.onItemClicked(position, item) }
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    fun setListener(listener: OnItemClick){
        this.listener = listener
    }

    fun interface OnItemClick {
        fun onItemClicked(position: Int, item: Int?)
    }


}