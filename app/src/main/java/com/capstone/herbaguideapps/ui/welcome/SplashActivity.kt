package com.capstone.herbaguideapps.ui.welcome

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.capstone.herbaguideapps.MainActivity
import com.capstone.herbaguideapps.R
import com.capstone.herbaguideapps.databinding.ActivitySplashBinding
import com.capstone.herbaguideapps.session.SessionViewModel
import com.capstone.herbaguideapps.utlis.factory.SessionViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val sessionViewModel by viewModels<SessionViewModel> {
        SessionViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSession()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupSession() {
        val content: View = binding.root
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    sessionViewModel.getSession().observe(this@SplashActivity) { session ->
                        if (session.isLogin) {
                            MainActivity.start(this@SplashActivity)
                            content.viewTreeObserver.removeOnPreDrawListener(this)
                            finish()
                        } else {
                            WelcomeActivity.start(this@SplashActivity)
                            content.viewTreeObserver.removeOnPreDrawListener(this)
                            finish()
                        }
                    }
                    return true
                }
            }
        )
        setupSplashExitAnimation()
    }

    private fun setupSplashExitAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView, View.TRANSLATION_Y, 0f, splashScreenView.height.toFloat()
                )
                val alpha = ObjectAnimator.ofFloat(splashScreenView, View.ALPHA, 1f, 0f)

                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 500L
                alpha.duration = 500L

                slideUp.doOnEnd {
                    splashScreenView.remove()
                }

                alpha.start()
                slideUp.start()
            }
        }
    }

}