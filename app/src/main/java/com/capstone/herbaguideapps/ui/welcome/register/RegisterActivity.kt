package com.capstone.herbaguideapps.ui.welcome.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.capstone.herbaguideapps.utlis.viewmodelfactory.AuthViewModelFactory

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

        binding.btnBack.setOnClickListener {
            WelcomeLoginActivity.start(this@RegisterActivity)
            finish()
        }

        binding.btnLogin.setOnClickListener {
            LoginActivity.start(this@RegisterActivity)
            finish()
        }

        binding.btnRegister.setOnClickListener {
            processRegister()
        }

        binding.btnSignGoogle.setOnClickListener {

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun processRegister() {
        val user = binding.edName.text.toString()
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()

        val body = RegisterBody(
            email, user, password
        )
        registerViewModel.register(body).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {

                    }

                    is Result.Success -> {
                        Toast.makeText(
                            this,
                            result.data.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        LoginActivity.start(this)
                        finish()
                    }

                    is Result.Error -> {
                        Toast.makeText(this, result.error[0].toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }
}