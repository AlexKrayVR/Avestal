package yelm.io.avestal.auth.registration.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.auth.host.HostAuth
import yelm.io.avestal.auth.model.UserViewModel
import yelm.io.avestal.auth.registration.USER_PASSPORT_IMAGE_REQUEST_CODE
import yelm.io.avestal.auth.registration.USER_SELFIE_IMAGE_REQUEST_CODE
import yelm.io.avestal.auth.registration.common.PHOTO_FILE_NAME
import yelm.io.avestal.auth.registration.common.SUFFIX
import yelm.io.avestal.auth.registration.common.UploadRequestBody
import yelm.io.avestal.databinding.FragmentConfirmUserBinding
import yelm.io.avestal.rest.KotlinAPI
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient
import yelm.io.avestal.rest.responses.ImageLink
import java.io.File

class PassportSelfieUserFragment : Fragment(), UploadRequestBody.UploadCallback {

    private lateinit var viewModel: UserViewModel
    private var binding: FragmentConfirmUserBinding? = null
    private var hostAuth: HostAuth? = null
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
        hostAuth?.requestCameraPermissions()
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

        binding?.back?.setOnClickListener {
            hostAuth?.back()
        }

        binding?.layoutUserIdPhoto?.setOnClickListener {
            checkIfCameraPermission(USER_PASSPORT_IMAGE_REQUEST_CODE)
        }

        binding?.layoutUserSelfie?.setOnClickListener {
            checkIfCameraPermission(USER_SELFIE_IMAGE_REQUEST_CODE)
        }

        binding?.further?.setOnClickListener {
            sendUserData()
        }
    }

    /**
     * collect user data: photos, info, fio and send to server
     * success code is 201 - registration was successful
     */
    private fun sendUserData() {
        showLoading()
        val phone = viewModel.user.value?.phone?.replace("\\D".toRegex(), "")
        RetrofitClient.getClient(RestAPI.URL_API_MAIN)
            .create(RestAPI::class.java)
            .setUserData(
                phone,
                getFIO(),
                getData()
            )
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    hideLoading()
                    if (response.isSuccessful) {
                        if (response.code() == 201) {
                            Logging.logDebug("User created")
                            hostAuth?.openFinishFragment()
                        } else {
                            hostAuth?.showToast(R.string.serverError)
                            Logging.logError("Method setUserData() - by some reason response is null!")
                        }
                    } else {
                        hostAuth?.showToast(R.string.serverError)
                        Logging.logError(
                            "Method setUserData() - response is not successful. " +
                                    "Code: " + response.code() + "Message: " + response.message()
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Logging.logError("Method setUserData() - failure: $t")
                    hostAuth?.showToast(R.string.serverError)
                    hideLoading()
                }
            })
    }


    private fun getData(): JSONObject {
        val jsonData = JSONObject()
        try {
            jsonData.put("job_status", viewModel.user.value?.jobStatus)
            jsonData.put("region_name", viewModel.user.value?.region)
            jsonData.put("job_description", viewModel.user.value?.jobDescription)
            jsonData.put("profile_image", viewModel.user.value?.profilePhoto)
            jsonData.put("passport_image", viewModel.user.value?.passportPhoto)
            jsonData.put("face_image", viewModel.user.value?.selfie)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonData
    }

    private fun getFIO(): JSONObject {
        val jsonData = JSONObject()
        try {
            jsonData.put("first_name", viewModel.user.value?.jobStatus)
            jsonData.put("last_name", viewModel.user.value?.region)
            jsonData.put("surname", viewModel.user.value?.jobDescription)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonData
    }

    private fun showLoading() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding?.progressBar?.visibility = View.GONE
    }

    private fun checkIfCameraPermission(code: Int) {
        if (hostAuth?.hasCameraPermission() == true) {
            openCamera(code)
        } else {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Logging.logDebug("${data?.data}")
        if (requestCode == USER_PASSPORT_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            setLayoutPassport()
            sendPicture(photoFile, "passport")
        }
        if (requestCode == USER_SELFIE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            setLayoutSelfie()
            sendPicture(photoFile, "selfie")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setLayoutPassport() {
        binding?.layoutUserIdPhoto?.background =
            AppCompatResources.getDrawable(requireContext(), R.drawable.back_blue_button_15)
        binding?.takeUserIDPhoto?.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding?.number1?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    private fun setLayoutSelfie() {
        binding?.layoutUserSelfie?.background =
            AppCompatResources.getDrawable(requireContext(), R.drawable.back_blue_button_15)
        binding?.takeUserSelfie?.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        binding?.number2?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    private fun getPhotoFile(): File {
        val storageDirectory = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(PHOTO_FILE_NAME, SUFFIX, storageDirectory)
    }

    private fun openCamera(code: Int) {
        photoFile = getPhotoFile()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val fileProvider = FileProvider.getUriForFile(
            requireContext(),
            "yelm.io.avestal.auth.registration",
            photoFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        startActivityForResult(intent, code)
    }

    private fun sendPicture(file: File, type: String) {
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
                hostAuth?.showToast(R.string.serverError)
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
                    hostAuth?.showToast(R.string.photoUploaded)
                    setEnableFurtherButton()
                } ?: hostAuth?.showToast(R.string.serverError)
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
        hostAuth = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is HostAuth) {
            hostAuth = activity as HostAuth
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

    private fun hideLoading(type: String) {
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