package com.capstone.herbaguideapps.ui.identify

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.capstone.herbaguideapps.databinding.FragmentModalBottomScanBinding
import com.capstone.herbaguideapps.utlis.getImageUri
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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
            currentImageUri = it
            moveToIdentify()
        }
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            moveToIdentify()
        }
    }

    private fun moveToIdentify() {
        currentImageUri?.let { uri ->
            IdentifyActivity.start(requireActivity(), uri.toString())
            dismiss()
        }

    }

}