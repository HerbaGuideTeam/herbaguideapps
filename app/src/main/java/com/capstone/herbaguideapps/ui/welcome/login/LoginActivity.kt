package com.capstone.herbaguideapps.ui.welcome.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.capstone.herbaguideapps.MainActivity
import com.capstone.herbaguideapps.R
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.model.LoginBody
import com.capstone.herbaguideapps.databinding.ActivityLoginBinding
import com.capstone.herbaguideapps.session.SessionModel
import com.capstone.herbaguideapps.ui.welcome.WelcomeLoginActivity
import com.capstone.herbaguideapps.ui.welcome.register.RegisterActivity
import com.capstone.herbaguideapps.utlis.factory.AuthViewModelFactory
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    private val loginViewModel by viewModels<LoginViewModel> {
        AuthViewModelFactory.getInstance(this@LoginActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

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

            btnSignGoogle.setOnClickListener {
                signIn()
            }

        }
    }

    private fun signIn() {
        val credentialManager = CredentialManager.create(this)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.googleWebID))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user: FirebaseUser? = auth.currentUser

                    if (user != null) {
                        loginViewModel.saveSession(
                            SessionModel(
                                user.displayName.toString(),
                                user.email.toString(),
                                true,
                                idToken,
                                isGuest = false,
                                isGoogle = true
                            )
                        )

                        setupSession()
                    }

                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun finishWelcomeActivity() {
        val intent = Intent("finish_welcome_activity")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun setupSession() {
        loginViewModel.getSession().observe(this) { session ->
            if (session.isLogin) {
                finishWelcomeActivity()
                MainActivity.start(this)
                finish()
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "LoginActivity"
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}