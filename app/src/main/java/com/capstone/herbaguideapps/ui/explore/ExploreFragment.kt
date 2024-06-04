package com.capstone.herbaguideapps.ui.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.herbaguideapps.adapter.ListExploreAdapter
import com.capstone.herbaguideapps.databinding.FragmentExploreBinding
import com.capstone.herbaguideapps.utlis.ViewModelFactory
import com.capstone.herbaguideapps.data.remote.response.ArticlesItem

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val exploreViewModel by viewModels<ExploreViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvExplore.layoutManager = layoutManager

        exploreViewModel.listExplore.observe(viewLifecycleOwner) {
            setExploreData(it)
        }

        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setExploreData(list: PagingData<ArticlesItem>) {
        val adapter = ListExploreAdapter()
        binding.rvExplore.adapter = adapter

        adapter.submitData(lifecycle, list)
        adapter.setOnItemClickCallback(object : ListExploreAdapter.OnItemClickCallback {
            override fun onItemClickCallBack(data: ArticlesItem) {

            }
        })
    }
}