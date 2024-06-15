package com.capstone.herbaguideapps.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.herbaguideapps.R
import com.capstone.herbaguideapps.data.remote.response.ArticlesItem
import com.capstone.herbaguideapps.databinding.ItemGridExploreBinding

class GridExploreAdapter :
    ListAdapter<ArticlesItem, GridExploreAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun submitTrimmedList(list: List<ArticlesItem>) {
        val trimmedList = if (list.size > 5) list.take(5) else list
        submitList(trimmedList)
    }

    class ViewHolder(private val binding: ItemGridExploreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(article: ArticlesItem) {
            binding.txtArticleTitle.text = article.title
            Glide.with(itemView)
                .load(article.urlToImage)
                .placeholder(R.drawable.ic_image_48dp)
                .into(binding.ivArticle)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemGridExploreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        fun onItemClickCallBack(data: ArticlesItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticlesItem>() {
            override fun areItemsTheSame(
                oldItem: ArticlesItem,
                newItem: ArticlesItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ArticlesItem,
                newItem: ArticlesItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}