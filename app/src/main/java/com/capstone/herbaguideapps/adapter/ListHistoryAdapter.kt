package com.capstone.herbaguideapps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.herbaguideapps.R
import com.capstone.herbaguideapps.data.local.HistoryEntity
import com.capstone.herbaguideapps.databinding.ItemListHistoryBinding

class ListHistoryAdapter :
    ListAdapter<HistoryEntity, ListHistoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(private val binding: ItemListHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryEntity) {
            binding.txtTitle.text = history.name
            binding.txtDate.text = history.createAt
            binding.ivExplore.setImageResource(R.drawable.welcome1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClickCallBack(history)
        }
    }

    interface OnItemClickCallback {
        fun onItemClickCallBack(data: HistoryEntity)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryEntity>() {
            override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: HistoryEntity,
                newItem: HistoryEntity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}