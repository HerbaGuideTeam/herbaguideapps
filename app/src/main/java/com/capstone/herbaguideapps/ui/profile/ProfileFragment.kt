package com.capstone.herbaguideapps.ui.profile

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
import com.capstone.herbaguideapps.R
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.model.LogoutBody
import com.capstone.herbaguideapps.databinding.FragmentHomeBinding
import com.capstone.herbaguideapps.databinding.FragmentProfileBinding
import com.capstone.herbaguideapps.session.SessionViewModel
import com.capstone.herbaguideapps.ui.welcome.WelcomeLoginActivity
import com.capstone.herbaguideapps.ui.welcome.login.LoginViewModel
import com.capstone.herbaguideapps.utlis.factory.AuthViewModelFactory
import com.capstone.herbaguideapps.utlis.factory.SessionViewModelFactory
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private val sessionViewModel by viewModels<SessionViewModel> {
        SessionViewModelFactory.getInstance(requireContext())
    }

    private val loginViewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(requireContext())
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

            binding.apply {
                txtName.text = session.name
                txtEmail.text = session.email
            }

        }

        binding.btnLogout.setOnClickListener {
            clearSession()
        }
    }

    private fun clearSession() {

        lifecycleScope.launch {
            sessionViewModel.getSession().observe(viewLifecycleOwner) { session ->
                if (session.isLogin && !session.isGuest) {
                    val logoutBody = LogoutBody(session.token)

                    loginViewModel.logout(logoutBody)
                    loginViewModel.authResult.observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Result.Loading -> {
                                binding.linearProgress.visibility = View.VISIBLE
                            }

                            is Result.Success -> {
                                binding.linearProgress.visibility = View.GONE
                                sessionViewModel.logout()
                                Toast.makeText(
                                    requireActivity(),
                                    result.data.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                WelcomeLoginActivity.start(requireActivity())
                                finishMainActivity()
                            }

                            is Result.Error -> {
                                binding.linearProgress.visibility = View.GONE
                                Toast.makeText(requireActivity(), result.error, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }

                } else if (session.isGuest) {
                    sessionViewModel.logout()
                    WelcomeLoginActivity.start(requireActivity())
                    finishMainActivity()
                }
            }
        }

    }


    private fun finishMainActivity() {
        val intent = Intent("finish_main_activity")
        LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(intent)
    }


}