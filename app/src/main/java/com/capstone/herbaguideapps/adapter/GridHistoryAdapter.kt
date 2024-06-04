package com.capstone.herbaguideapps.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.herbaguideapps.R
import com.capstone.herbaguideapps.data.local.HistoryEntity
import com.capstone.herbaguideapps.databinding.ItemHistoryGridBinding

class GridHistoryAdapter: ListAdapter<HistoryEntity, GridHistoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun submitTrimmedList(list: List<HistoryEntity>) {
        val trimmedList = if (list.size > 3) list.take(3) else list
        submitList(trimmedList)
    }

    class ViewHolder(private val binding: ItemHistoryGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(history: HistoryEntity) {
            binding.txtName.text = history.name
            binding.ivHistory.setImageResource(R.drawable.welcome1)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHistoryGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userList = getItem(position)
        holder.bind(userList)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClickCallBack(userList)
        }

    }
    interface OnItemClickCallback {
        fun onItemClickCallBack(data: HistoryEntity)
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryEntity>() {
            override fun areItemsTheSame(
                oldItem: HistoryEntity,
                newItem: HistoryEntity
            ): Boolean {
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