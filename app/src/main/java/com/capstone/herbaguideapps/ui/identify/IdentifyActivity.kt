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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstone.herbaguideapps.R
import com.capstone.herbaguideapps.adapter.ExpandUsabilityAdapter
import com.capstone.herbaguideapps.data.Result
import com.capstone.herbaguideapps.data.model.Usability
import com.capstone.herbaguideapps.databinding.ActivityIdentifyBinding
import com.capstone.herbaguideapps.session.SessionViewModel
import com.capstone.herbaguideapps.utlis.factory.PredictViewModelFactory
import com.capstone.herbaguideapps.utlis.factory.SessionViewModelFactory
import com.capstone.herbaguideapps.utlis.reduceFileImage
import com.capstone.herbaguideapps.utlis.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class IdentifyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIdentifyBinding

    private val predictViewModel by viewModels<PredictViewModel> {
        PredictViewModelFactory.getInstance(this)
    }


    private val sessionViewModel by viewModels<SessionViewModel> {
        SessionViewModelFactory.getInstance(this)
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

        sessionViewModel.getSession().observe(this) { session ->
            analyzeImage(imageUri, session.isGuest)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    @SuppressLint("SetTextI18n")
    private fun analyzeImage(currentImageUri: Uri?, isGuest: Boolean) {
        currentImageUri?.let { uri ->
            val file = uriToFile(uri, this)
            val compressFile = reduceFileImage(file)

            val requestImageFile = compressFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            predictViewModel.predictImage(multipartBody, isGuest)
            predictViewModel.predictResult.observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.linearProgress.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.linearProgress.visibility = View.GONE

                            val tanamanHerbal = result.data.prediction.tanamanHerbal
                            binding.txtName.text = tanamanHerbal.nama

                            binding.txtDescription.text = tanamanHerbal.deskripsi
                            Glide.with(this)
                                .load(tanamanHerbal.photoUrl)
                                .into(binding.ivPlant)

                            binding.txtTitleDesc.visibility = View.VISIBLE
                            binding.txtTitleUsability.visibility = View.VISIBLE

                            val usabilityList: List<Usability> =
                                tanamanHerbal.mengobatiApa.map { mengobatiApaItem ->
                                    Usability(
                                        penyakit = mengobatiApaItem.penyakit,
                                        resep = mengobatiApaItem.resep
                                    )
                                }
                            setUsabilityData(usabilityList)
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

    private fun setUsabilityData(usability: List<Usability>) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvUsability.layoutManager = layoutManager

        val adapter = ExpandUsabilityAdapter()

        adapter.submitList(usability)
        binding.rvUsability.adapter = adapter
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