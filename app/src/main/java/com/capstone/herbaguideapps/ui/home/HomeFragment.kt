package com.capstone.herbaguideapps.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.herbaguideapps.R
import com.capstone.herbaguideapps.adapter.GridExploreAdapter
import com.capstone.herbaguideapps.adapter.GridHistoryAdapter
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.local.HistoryEntity
import com.capstone.herbaguideapps.data.local.dummyHistory
import com.capstone.herbaguideapps.data.remote.body.LogoutBody
import com.capstone.herbaguideapps.data.remote.response.ArticlesItem
import com.capstone.herbaguideapps.databinding.FragmentHomeBinding
import com.capstone.herbaguideapps.session.SessionViewModel
import com.capstone.herbaguideapps.ui.explore.ExploreViewModel
import com.capstone.herbaguideapps.ui.identify.ModalBottomScanFragment
import com.capstone.herbaguideapps.ui.welcome.WelcomeLoginActivity
import com.capstone.herbaguideapps.ui.welcome.login.LoginViewModel
import com.capstone.herbaguideapps.utlis.ViewModelFactory
import com.capstone.herbaguideapps.utlis.viewmodelfactory.AuthViewModelFactory
import com.capstone.herbaguideapps.utlis.viewmodelfactory.SessionViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val sessionViewModel by viewModels<SessionViewModel> {
        SessionViewModelFactory.getInstance(requireActivity())
    }

    private val loginViewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(requireActivity())
    }


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

        auth = Firebase.auth

        binding.btnScan.setOnClickListener {
            val modalSheetFragment = ModalBottomScanFragment()
            modalSheetFragment.show(childFragmentManager, modalSheetFragment.tag)
        }

        sessionViewModel.getSession().observe(viewLifecycleOwner) { session ->
            binding.txtWelcome.text = getString(R.string.title_home_name, session.name)
        }

        binding.btnProfile.setOnClickListener {
            clearSession()
        }

        showDataHistory()
        showDataExplore()

        return root
    }

    private fun clearSession() {
        sessionViewModel.getSession().observe(viewLifecycleOwner) { session ->
            if (session.isLogin && (!session.isGuest && !session.isGoogle)) {
                val logoutBody = LogoutBody(session.token)

                loginViewModel.logout(logoutBody)
                loginViewModel.authResult.observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.linearProgress.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.linearProgress.visibility = View.GONE
                            Toast.makeText(
                                requireActivity(),
                                result.data?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            sessionViewModel.logout()
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
            } else if (session.isGoogle) {
                signOut()
            }
        }
    }

    private fun signOut() {

        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(requireActivity())
            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            WelcomeLoginActivity.start(requireActivity())
            finishMainActivity()
        }
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
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvExplore.layoutManager = layoutManager

        exploreViewModel.getTopHeadline().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {

                    }

                    is Result.Success -> {
                        val adapter = GridExploreAdapter()
                        binding.rvExplore.adapter = adapter

                        adapter.submitTrimmedList(result.data.articles)
                        adapter.setOnItemClickCallback(object :
                            GridExploreAdapter.OnItemClickCallback {
                            override fun onItemClickCallBack(data: ArticlesItem) {
                                Toast.makeText(
                                    requireActivity(),
                                    "Article: ${data.title}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    }

                    is Result.Error -> {
                        Toast.makeText(
                            requireActivity(),
                            "Hottest News Error: ${result.error}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }
    }

    private fun finishMainActivity() {
        val intent = Intent("finish_main_activity")
        LocalBroadcastManager.getInstance(requireActivity()).sendBroadcast(intent)
    }
}