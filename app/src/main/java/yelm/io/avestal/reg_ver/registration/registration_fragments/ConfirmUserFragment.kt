package yelm.io.avestal.reg_ver.registration.registration_fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FragmentConfirmUserBinding
import yelm.io.avestal.reg_ver.model.UserViewModel
import yelm.io.avestal.reg_ver.registration.phone_registration.view.HostRegistration
import java.io.File

class ConfirmUserFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    private var binding: FragmentConfirmUserBinding? = null
    private var hostRegistration: HostRegistration? = null

    private lateinit var photoFile: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConfirmUserBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        viewModel.user.observe(requireActivity(), {
            Logging.logDebug(it.toString())
        })

        if (viewModel.userFilesAdded()) {
            binding?.further?.isEnabled = true
        }

        binding?.back?.setOnClickListener {
            hostRegistration?.back()
        }

        binding?.takeUserIDPhoto?.setOnClickListener {
            checkIfCameraPermission(USER_ID_IMAGE_REQUEST_CODE)
        }

        binding?.takeUserSelfie?.setOnClickListener {
            checkIfCameraPermission(USER_SELFIE_IMAGE_REQUEST_CODE)
        }
    }

    private fun checkIfCameraPermission(code: Int) {
        if (hostRegistration?.hasCameraPermission() == true) {
            capturePicture(code)
        } else {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == USER_ID_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Logging.logDebug("${photoFile.absolutePath}")
            binding?.takeUserIDPhoto?.background?.setTint(requireContext().resources.getColor(R.color.colorBlue))
            binding?.takeUserIDPhoto?.setTextColor(requireContext().resources.getColor(R.color.white))
            viewModel.setUserIDPhoto(photoFile)
        }
        if (requestCode == USER_SELFIE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            binding?.takeUserSelfie?.background?.setTint(requireContext().resources.getColor(R.color.colorBlue))
            binding?.takeUserSelfie?.setTextColor(requireContext().resources.getColor(R.color.white))
            viewModel.setUserSelfie(photoFile)

            //val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            //binding?.userImage?.setImageBitmap(takenImage)
            Logging.logDebug("${photoFile.absolutePath}")
        }

        if (viewModel.userFilesAdded()) {
            binding?.further?.isEnabled = true
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun capturePicture(code: Int) {
        photoFile = getPhotoFile()
        val fileProvider =
            FileProvider.getUriForFile(requireContext(), "yelm.io.avestal.fileprovider", photoFile)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        startActivityForResult(intent, code)
    }

    private fun getPhotoFile(): File {
        val storageDirectory = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("photo_", ".jpg", storageDirectory)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ConfirmUserFragment().apply {
            }
    }

    override fun onDetach() {
        super.onDetach()
        hostRegistration = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is HostRegistration) {
            hostRegistration = activity as HostRegistration
        } else {
            throw RuntimeException(activity.toString() + " must implement Communicator interface")
        }
    }
}