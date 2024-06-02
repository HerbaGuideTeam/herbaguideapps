package com.capstone.herbaguideapps.ui.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.herbaguideapps.MainActivity
import com.capstone.herbaguideapps.R
import com.capstone.herbaguideapps.databinding.ActivityWelcomeLoginBinding

class WelcomeLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWelcomeLoginBinding.inflate(layoutInflater)
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
            btnLogin.setOnClickListener {

            }

            btnRegister.setOnClickListener {

            }

            btnGuest.setOnClickListener {
                MainActivity.start(this@WelcomeLoginActivity, true)
                finish()
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, WelcomeLoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}