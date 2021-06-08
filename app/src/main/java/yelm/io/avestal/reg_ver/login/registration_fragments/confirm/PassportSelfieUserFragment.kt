package yelm.io.avestal.reg_ver.login.registration_fragments.confirm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FragmentConfirmUserBinding
import yelm.io.avestal.reg_ver.model.UserViewModel
import yelm.io.avestal.reg_ver.host.HostRegistration
import yelm.io.avestal.reg_ver.login.registration_fragments.USER_PASSPORT_IMAGE_REQUEST_CODE
import yelm.io.avestal.reg_ver.login.registration_fragments.USER_SELFIE_IMAGE_REQUEST_CODE
import yelm.io.avestal.rest.KotlinAPI
import yelm.io.avestal.rest.UploadRequestBody
import yelm.io.avestal.rest.getFileName
import yelm.io.avestal.rest.responses.ImageLink
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class PassportSelfieUserFragment : Fragment(), UploadRequestBody.UploadCallback {

    private lateinit var viewModel: UserViewModel
    private var binding: FragmentConfirmUserBinding? = null
    private var hostRegistration: HostRegistration? = null

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
        hostRegistration?.requestCameraPermissions()
        viewModel.user.observe(requireActivity(), {
            Logging.logDebug(it.toString())
        })

        if (viewModel.user.value?.passportPhoto != "") {
            setLayoutPassport()
            binding?.passportPhotoUploaded?.visibility = View.VISIBLE
        }

        if (viewModel.user.value?.selfie != "") {
            setLayoutSelfie()
            binding?.selfiePhotoUploaded?.visibility = View.VISIBLE
        }

        setEnableFurtherButton()

        binding?.back?.setOnClickListener {
            hostRegistration?.back()
        }

        binding?.layoutUserIdPhoto?.setOnClickListener {
            checkIfCameraPermission(USER_PASSPORT_IMAGE_REQUEST_CODE)
        }

        binding?.layoutUserSelfie?.setOnClickListener {
            checkIfCameraPermission(USER_SELFIE_IMAGE_REQUEST_CODE)
        }

        binding?.further?.setOnClickListener {
            hostRegistration?.openFinishFragment()
        }
    }

    private fun checkIfCameraPermission(code: Int) {
        if (hostRegistration?.hasCameraPermission() == true) {
            openImage(code)
        } else {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Logging.logDebug("${data?.data}")
        if (requestCode == USER_PASSPORT_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            setLayoutPassport()
            sendPicture(data?.data!!, "passport")
        }
        if (requestCode == USER_SELFIE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            setLayoutSelfie()
            sendPicture(data?.data!!, "selfie")
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setLayoutPassport() {
        binding?.layoutUserIdPhoto?.background =
            AppCompatResources.getDrawable(requireContext(), R.drawable.back_blue_button_15)
        binding?.takeUserIDPhoto?.setTextColor(requireContext().resources.getColor(R.color.white))
        binding?.number1?.setTextColor(requireContext().resources.getColor(R.color.white))
    }

    private fun setLayoutSelfie() {
        binding?.layoutUserSelfie?.background =
            AppCompatResources.getDrawable(requireContext(), R.drawable.back_blue_button_15)
        binding?.takeUserSelfie?.setTextColor(requireContext().resources.getColor(R.color.white))
        binding?.number2?.setTextColor(requireContext().resources.getColor(R.color.white))
    }


    private fun openImage(code: Int) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, code)
    }

    private fun sendPicture(uri: Uri, type: String) {
        val parcelFileDescriptor =
            context?.contentResolver?.openFileDescriptor(uri, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(context?.cacheDir, context?.contentResolver?.getFileName(uri)!!)
        Logging.logDebug("file.path: ${file.path}")

        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        //binding.progressBar.progress = 0
        val body = UploadRequestBody(file, "image", this)
        showLoading(type)
        KotlinAPI().uploadImage(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            )//,
            //RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "json")
        ).enqueue(object : Callback<ImageLink> {
            override fun onFailure(call: Call<ImageLink>, t: Throwable) {
                Logging.logDebug("onFailure: $t")
                hostRegistration?.showToast(R.string.serverError)
                hideLoading(type)
            }

            override fun onResponse(
                call: Call<ImageLink>,
                response: Response<ImageLink>
            ) {
                hideLoading(type)
                Logging.logDebug("response.body(): ${response.body()}")
                response.body()?.let {
                    if (type == "selfie") {
                        viewModel.setUserSelfie(it.link)
                    } else {
                        viewModel.setUserPassportPhoto(it.link)
                    }
                    hostRegistration?.showToast(R.string.photoUploaded)
                    setEnableFurtherButton()
                } ?: hostRegistration?.showToast(R.string.serverError)
            }
        })
    }

    private fun setEnableFurtherButton() {
        if (viewModel.userFilesAdded()) {
            binding?.further?.isEnabled = true
        }
    }

    companion object {
        fun newInstance() = PassportSelfieUserFragment()
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

    private fun showLoading(type: String) {
        if (type == "selfie") {
            binding?.selfiePhotoUploaded?.visibility = View.GONE
            binding?.progressBarSelfie?.visibility = View.VISIBLE
        } else {
            binding?.passportPhotoUploaded?.visibility = View.GONE
            binding?.progressBarPassportPhoto?.visibility = View.VISIBLE
        }
    }

    fun hideLoading(type: String) {
        if (type == "selfie") {
            binding?.progressBarSelfie?.visibility = View.GONE
            binding?.selfiePhotoUploaded?.visibility = View.VISIBLE
        } else {
            binding?.progressBarPassportPhoto?.visibility = View.GONE
            binding?.passportPhotoUploaded?.visibility = View.VISIBLE
        }
    }

    override fun onProgressUpdate(percentage: Int) {

    }
}