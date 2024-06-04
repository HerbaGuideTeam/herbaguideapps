package com.capstone.herbaguideapps.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.herbaguideapps.adapter.GridHistoryAdapter
import com.capstone.herbaguideapps.adapter.ListExploreAdapter
import com.capstone.herbaguideapps.data.local.HistoryEntity
import com.capstone.herbaguideapps.data.local.dummyHistory
import com.capstone.herbaguideapps.data.remote.response.ArticlesItem
import com.capstone.herbaguideapps.databinding.FragmentHomeBinding
import com.capstone.herbaguideapps.ui.explore.ExploreViewModel
import com.capstone.herbaguideapps.utlis.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val exploreViewModel by viewModels<ExploreViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        showDataHistory()
        showDataExplore()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDataHistory() {
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHistory.layoutManager = layoutManager

        val adapter = GridHistoryAdapter()
        adapter.submitTrimmedList(dummyHistory)
        adapter.setOnItemClickCallback(object : GridHistoryAdapter.OnItemClickCallback {
            override fun onItemClickCallBack(data: HistoryEntity) {

            }
        })
        binding.rvHistory.adapter = adapter
    }

    private fun showDataExplore() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvExplore.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvExplore.addItemDecoration(itemDecoration)

        exploreViewModel.listExplore.observe(viewLifecycleOwner) {
            setExploreData(it)
        }
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