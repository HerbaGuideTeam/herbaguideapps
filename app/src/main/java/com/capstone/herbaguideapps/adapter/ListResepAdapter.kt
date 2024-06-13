package com.capstone.herbaguideapps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.herbaguideapps.databinding.ItemListResepBinding

class ListResepAdapter(private val itemList: List<String>) :
    RecyclerView.Adapter<ListResepAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: ItemListResepBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.txtResep.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemListResepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
