package com.capstone.herbaguideapps.ui.explore

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.herbaguideapps.adapter.ListExploreAdapter
import com.capstone.herbaguideapps.data.remote.response.ArticlesItem
import com.capstone.herbaguideapps.databinding.FragmentExploreBinding
import com.capstone.herbaguideapps.utlis.ViewModelFactory
import kotlinx.coroutines.launch

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val exploreViewModel by viewModels<ExploreViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvExplore.layoutManager = layoutManager

        lifecycleScope.launch {
            exploreViewModel.listExplore.observe(viewLifecycleOwner) {
                setExploreData(it)
            }
        }
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
                val intent = Intent(requireActivity(), WebViewActivity::class.java)
                intent.putExtra(WebViewActivity.EXTRA_URL, data.url)
                startActivity(intent)
            }
        })
    }
}