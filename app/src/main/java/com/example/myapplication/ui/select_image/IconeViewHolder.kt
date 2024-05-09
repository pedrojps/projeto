package com.example.myapplication.ui.select_image

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemIconeBinding


class IconeViewHolder(val binding: ItemIconeBinding):
    RecyclerView.ViewHolder(binding.root) {

    constructor(inflater: LayoutInflater, parent: ViewGroup): this(
        ItemIconeBinding.inflate(inflater, parent, false)
    )

    fun onBind(item: Int, position: Int) {

        binding.apply {
            val context = root.context
            val drawable = ContextCompat.getDrawable(context, item)

            binding.imageIcon.setImageDrawable(drawable)
        }
    }
}