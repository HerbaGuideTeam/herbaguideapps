package com.capstone.herbaguideapps.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.herbaguideapps.R
import com.capstone.herbaguideapps.databinding.FragmentHomeBinding
import com.capstone.herbaguideapps.databinding.FragmentProfileBinding
import com.capstone.herbaguideapps.session.SessionViewModel
import com.capstone.herbaguideapps.utlis.factory.SessionViewModelFactory

class ProfileFragment : Fragment() {

    private val sessionViewModel by viewModels<SessionViewModel> {
        SessionViewModelFactory.getInstance(requireActivity())
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sessionViewModel.getSession().observe(viewLifecycleOwner) { session ->
            binding.txtName.text = session.name

        }
    }

}