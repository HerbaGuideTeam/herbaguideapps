package com.capstone.herbaguideapps.ui.welcome.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.capstone.herbaguideapps.MainActivity
import com.capstone.herbaguideapps.R
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.model.LoginBody
import com.capstone.herbaguideapps.databinding.ActivityLoginBinding
import com.capstone.herbaguideapps.ui.welcome.WelcomeLoginActivity
import com.capstone.herbaguideapps.ui.welcome.register.RegisterActivity
import com.capstone.herbaguideapps.utlis.factory.AuthViewModelFactory
import kotlinx.coroutines.launch
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(this@LoginActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupAction() {
        binding.apply {
            btnBack.setOnClickListener {
                WelcomeLoginActivity.start(this@LoginActivity)
                finish()
            }

            btnRegister.setOnClickListener {
                RegisterActivity.start(this@LoginActivity)
                finish()
            }

            btnLogin.setOnClickListener {
                val email = edEmail.text.toString()
                val password = edPassword.text.toString()

                if (email.isEmpty()) {
                    edlEmail.error = getString(R.string.msg_email_must_filled)
                    return@setOnClickListener
                }

                if (password.isEmpty()) {
                    edlPassword.error = getString(R.string.msg_password_cannot_be_blank)
                    return@setOnClickListener
                }

                val login = LoginBody(
                    email, password
                )

                loginViewModel.login(login)
                loginViewModel.loginResult.observe(this@LoginActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                linearProgress.visibility = View.VISIBLE
                            }

                            is Result.Success -> {

                                linearProgress.visibility = View.GONE
                                setupSession()

                            }

                            is Result.Error -> {
                                linearProgress.visibility = View.GONE
                                showToast(result.error)
                            }
                        }
                    }

                }
            }

        }
    }

    private fun finishWelcomeActivity() {
        val intent = Intent("finish_welcome_activity")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun setupSession() {
        lifecycleScope.launch {
            loginViewModel.getSession().observe(this@LoginActivity) { session ->
                if (session.isLogin) {
                    finishWelcomeActivity()
                    MainActivity.start(this@LoginActivity)
                    finish()
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "AXLoginActivity"
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}
