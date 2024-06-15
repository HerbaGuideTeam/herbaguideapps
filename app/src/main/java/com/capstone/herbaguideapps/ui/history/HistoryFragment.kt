package com.capstone.herbaguideapps.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.herbaguideapps.adapter.ListHistoryAdapter
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.remote.response.HistoryItem
import com.capstone.herbaguideapps.databinding.FragmentHistoryBinding
import com.capstone.herbaguideapps.session.SessionViewModel
import com.capstone.herbaguideapps.ui.welcome.login.LoginActivity
import com.capstone.herbaguideapps.utlis.factory.PredictViewModelFactory
import com.capstone.herbaguideapps.utlis.factory.SessionViewModelFactory
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyViewModel by viewModels<HistoryViewModel> {
        PredictViewModelFactory.getInstance(requireActivity())
    }


    private val sessionViewModel by viewModels<SessionViewModel> {
        SessionViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getHistoryData()

        binding.btnLogin.setOnClickListener {
            LoginActivity.start(requireActivity())
            finishMainActivity()
        }
    }

    private fun getHistoryData() {
        lifecycleScope.launch {
            sessionViewModel.getSession().observe(viewLifecycleOwner) { session ->
                if (!session.isGuest && session.isLogin) {
                    binding.apply {
                        btnLogin.visibility = View.GONE
                        txtQuestion.visibility = View.GONE

                        rvHistory.visibility = View.VISIBLE
                    }

                    historyViewModel.getHistory()
                    historyViewModel.historyResult.observe(viewLifecycleOwner) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {

                                }

                                is Result.Success -> {
                                    setHistoryData(result.data.history)
                                }

                                is Result.Error -> {
                                    Toast.makeText(
                                        requireActivity(),
                                        "Error: ${result.error}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                } else {
                    binding.apply {
                        btnLogin.visibility = View.VISIBLE
                        txtQuestion.visibility = View.VISIBLE

                        rvHistory.visibility = View.GONE
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

    private fun finishMainActivity() {
        val intent = Intent("finish_main_activity")
        LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(intent)
    }
}