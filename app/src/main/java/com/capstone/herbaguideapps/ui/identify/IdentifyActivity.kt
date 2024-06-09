package com.capstone.herbaguideapps.ui.identify

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.capstone.herbaguideapps.R
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.databinding.ActivityIdentifyBinding
import com.capstone.herbaguideapps.utlis.reduceFileImage
import com.capstone.herbaguideapps.utlis.uriToFile
import com.capstone.herbaguideapps.utlis.viewmodelfactory.PredictViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class IdentifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIdentifyBinding

    private val predictViewModel by viewModels<PredictViewModel> {
        PredictViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityIdentifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topBar.setNavigationOnClickListener {
            @Suppress("DEPRECATION")
            onBackPressed()
        }

        val imageUri: Uri? = intent.getStringExtra(EXTRA_URI)?.toUri()

        analyzeImage(imageUri)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    @SuppressLint("SetTextI18n")
    private fun analyzeImage(currentImageUri: Uri?) {
        currentImageUri?.let { uri ->
            val file = uriToFile(uri, this)
            val compressFile = reduceFileImage(file)

            val requestImageFile = compressFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            predictViewModel.analyzeImage(multipartBody).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.linearProgress.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.linearProgress.visibility = View.GONE
                            binding.txtName.text = result.data.data.result
                            binding.txtDescription.text =
                                "Score: " + result.data.data.confidenceScore.toString()
                            Glide.with(this)
                                .load(uri)
                                .into(binding.ivPlant)
                        }

                        is Result.Error -> {
                            binding.linearProgress.visibility = View.GONE
                            Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT)
                                .show()

                        }
                    }
                }
            }

        }
    }


    companion object {
        const val EXTRA_URI = "extra_uri"
        fun start(context: Context, uri: String) {
            val intent = Intent(context, IdentifyActivity::class.java)
            intent.putExtra(EXTRA_URI, uri)
            context.startActivity(intent)
        }
    }
}