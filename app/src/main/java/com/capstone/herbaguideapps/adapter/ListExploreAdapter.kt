package com.capstone.herbaguideapps.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.herbaguideapps.data.remote.response.ArticlesItem
import com.capstone.herbaguideapps.databinding.ItemListExploreBinding

class ListExploreAdapter :
    PagingDataAdapter<ArticlesItem, ListExploreAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(private val binding: ItemListExploreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticlesItem) {
            binding.txtTitle.text = article.title
            Glide.with(itemView)
                .load(article.urlToImage)
                .circleCrop()
                .into(binding.ivExplore)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListExploreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val articleList = getItem(position)
        holder.bind(articleList!!)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClickCallBack(articleList)
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