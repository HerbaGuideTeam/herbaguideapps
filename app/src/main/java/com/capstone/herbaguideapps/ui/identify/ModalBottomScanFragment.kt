package com.capstone.herbaguideapps.ui.identify

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.capstone.herbaguideapps.databinding.FragmentModalBottomScanBinding
import com.capstone.herbaguideapps.utlis.getImageUri
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yalantis.ucrop.UCrop
import java.io.File
import java.time.LocalDateTime

class ModalBottomScanFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentModalBottomScanBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentModalBottomScanBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnCamera.setOnClickListener {
            startCamera()
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireActivity())
        currentImageUri?.let {
            launcherCamera.launch(it)
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            processCropImage(it)
        }
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            processCropImage(currentImageUri!!)
        }
    }

    private val uCropLauncher = object : ActivityResultContract<List<Uri>, Uri>() {
        override fun createIntent(context: Context, input: List<Uri>): Intent {
            val inputUri = input[0]
            val outputUri = input[1]

            val uCrop = UCrop.of(inputUri, outputUri)
                .withAspectRatio(1f, 1f)

            return uCrop.getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri {
            return if (resultCode == RESULT_OK && intent != null) {
                UCrop.getOutput(intent)!!
            } else {
                Uri.EMPTY
            }
        }

    }

    private val cropImage = registerForActivityResult(uCropLauncher) { uri ->
        if (uri != Uri.EMPTY) {
            currentImageUri = uri

            moveToIdentify()
        }
    }

    private fun processCropImage(uri: Uri) {
        val time = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            ""
        }
        val tempImgFile = File(requireContext().filesDir, "tempImg_$time.png")
        if (tempImgFile.exists()) {
            tempImgFile.delete()
        }

        val outputUri = tempImgFile.toUri()
        cropImage.launch(listOf(uri, outputUri))

    }
    private fun moveToIdentify() {
        currentImageUri?.let { uri ->
            IdentifyActivity.start(requireActivity(), uri.toString())
            dismiss()
        }

    }

}