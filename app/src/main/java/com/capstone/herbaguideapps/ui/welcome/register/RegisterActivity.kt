package com.capstone.herbaguideapps.ui.welcome.register

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
import com.capstone.herbaguideapps.R
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.remote.body.RegisterBody
import com.capstone.herbaguideapps.databinding.ActivityRegisterBinding
import com.capstone.herbaguideapps.ui.welcome.WelcomeLoginActivity
import com.capstone.herbaguideapps.ui.welcome.login.LoginActivity
import com.capstone.herbaguideapps.utlis.factory.AuthViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel by viewModels<RegisterViewModel> {
        AuthViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
                WelcomeLoginActivity.start(this@RegisterActivity)
                finish()
            }

            btnLogin.setOnClickListener {
                LoginActivity.start(this@RegisterActivity)
                finish()
            }

            btnRegister.setOnClickListener {
                processRegister()
            }
        }
    }

    private fun processRegister() {
        val name = binding.edName.text.toString()
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()
        val confirmPassword = binding.edConfirmPassword.text.toString()

        if (email.isEmpty()) {
            binding.edlEmail.error = getString(R.string.msg_email_must_filled)
            return
        }

        if (password.isEmpty()) {
            binding.edlPassword.error = getString(R.string.msg_password_cannot_be_blank)
            return
        }

        if (name.isEmpty()) {
            binding.edlName.error = getString(R.string.name_cannot_be_blank)
            return
        }

        if (confirmPassword.isEmpty()) {
            binding.edlConfirmPassword.error = getString(R.string.confirm_password_cannot_be_blank)
            return
        }

        if (password != confirmPassword) {
            binding.edlConfirmPassword.error = getString(R.string.password_not_same)
            return
        }

        val body = RegisterBody(
            email, name, password
        )

        registerViewModel.register(body)
        registerViewModel.authResult.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.linearProgress.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.linearProgress.visibility = View.GONE
                        showToast(result.data.message)

                        LoginActivity.start(this)
                        finish()
                    }

                    is Result.Error -> {
                        binding.linearProgress.visibility = View.GONE
                        showToast(result.error)
                    }
                }
            }

        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }
}