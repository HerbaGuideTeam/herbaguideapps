package com.capstone.herbaguideapps.ui.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstone.herbaguideapps.adapter.ExpandUsabilityAdapter
import com.capstone.herbaguideapps.data.model.Usability
import com.capstone.herbaguideapps.data.remote.response.HistoryItem
import com.capstone.herbaguideapps.databinding.FragmentModalBottomDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ModalBottomDetailFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentModalBottomDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentModalBottomDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val data: HistoryItem? = arguments?.getParcelable(EXTRA_DATA)

        data?.let { historyItem ->
            val tanamanHerbal = historyItem.tanamanHerbal
            binding.apply {
                Glide.with(requireView())
                    .load(tanamanHerbal.photoUrl)
                    .into(ivPlant)

                txtName.text = tanamanHerbal.nama
                txtDescription.text = tanamanHerbal.deskripsi

                txtTitleDesc.visibility = View.VISIBLE
                txtTitleUsability.visibility = View.VISIBLE

                val usabilityList: List<Usability> =
                    tanamanHerbal.mengobatiApa.map { mengobatiApaItem ->
                        Usability(
                            penyakit = mengobatiApaItem.penyakit,
                            resep = mengobatiApaItem.resep
                        )
                    }
                setUsabilityData(usabilityList)
            }
        }
    }

    private fun setUsabilityData(usability: List<Usability>) {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUsability.layoutManager = layoutManager

        val adapter = ExpandUsabilityAdapter()

        adapter.submitList(usability)
        binding.rvUsability.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "ModalBottomDetailFragment"
        const val EXTRA_DATA = "extra_data"
    }


}