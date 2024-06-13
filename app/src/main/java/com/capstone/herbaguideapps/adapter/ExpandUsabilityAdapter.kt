package com.capstone.herbaguideapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.herbaguideapps.data.model.Usability
import com.capstone.herbaguideapps.databinding.ItemListUsabilityBinding

class ExpandUsabilityAdapter :
    ListAdapter<Usability, ExpandUsabilityAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemListUsabilityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @Suppress("DEPRECATION")
        fun bind(usability: Usability) {
            binding.txtPenyakit.text = usability.penyakit

            binding.rvResep.visibility = if (usability.isExpandable) View.VISIBLE else View.GONE

            binding.rvResep.layoutManager = LinearLayoutManager(binding.root.context)

            val adapter = ListResepAdapter(usability.resep)
            binding.rvResep.adapter = adapter

            binding.constraintLayout.setOnClickListener {
                isAnyItemExpanded(adapterPosition)
                usability.isExpandable = !usability.isExpandable
                notifyItemChanged(adapterPosition, Unit)
            }
        }

        fun collapseExpandedView() {
            binding.rvResep.visibility = View.GONE
        }
    }

    private fun isAnyItemExpanded(position: Int) {
        val temp = currentList.indexOfFirst {
            it.isExpandable
        }
        if (temp >= 0 && temp != position) {
            currentList[temp].isExpandable = false
            notifyItemChanged(temp, 0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListUsabilityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty() && payloads[0] == 0) {
            holder.collapseExpandedView()
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Usability>() {
            override fun areItemsTheSame(
                oldItem: Usability,
                newItem: Usability
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Usability,
                newItem: Usability
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

}