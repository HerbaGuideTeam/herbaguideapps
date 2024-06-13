package com.capstone.herbaguideapps.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.herbaguideapps.adapter.ListHistoryAdapter
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.remote.response.HistoryItem
import com.capstone.herbaguideapps.databinding.FragmentHistoryBinding
import com.capstone.herbaguideapps.utlis.factory.PredictViewModelFactory

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyViewModel by viewModels<HistoryViewModel> {
        PredictViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getHistoryData()

        return root
    }

    private fun getHistoryData() {
        historyViewModel.getHistory()
        historyViewModel.history.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {

                    }

                    is Result.Success -> {
                        setHistoryData(result.data.history)
                    }

                    is Result.Error -> {

                    }
                }
            }
        }
    }

    private fun setHistoryData(list: List<HistoryItem>) {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvHistory.layoutManager = layoutManager

        val adapter = ListHistoryAdapter()

        adapter.submitList(list)
        adapter.setOnItemClickCallback(object : ListHistoryAdapter.OnItemClickCallback {
            override fun onItemClickCallBack(data: HistoryItem) {

                val bundle = Bundle()
                bundle.putParcelable(ModalBottomDetailFragment.EXTRA_DATA, data)

                val modalBottomSheet = ModalBottomDetailFragment()
                modalBottomSheet.arguments = bundle
                modalBottomSheet.show(parentFragmentManager, ModalBottomDetailFragment.TAG)
            }
        })
        binding.rvHistory.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        getHistoryData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}