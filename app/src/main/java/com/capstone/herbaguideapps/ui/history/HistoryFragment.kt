package com.capstone.herbaguideapps.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.herbaguideapps.adapter.ListHistoryAdapter
import com.capstone.herbaguideapps.data.local.HistoryEntity
import com.capstone.herbaguideapps.data.local.dummyHistory
import com.capstone.herbaguideapps.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvHistory.layoutManager = layoutManager

        val adapter = ListHistoryAdapter()
        binding.rvHistory.adapter = adapter

        adapter.submitList(dummyHistory)
        adapter.setOnItemClickCallback(object : ListHistoryAdapter.OnItemClickCallback {
            override fun onItemClickCallBack(data: HistoryEntity) {
                Toast.makeText(requireActivity(), "Nama: ${data.name}", Toast.LENGTH_SHORT).show()
            }
        })

        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}