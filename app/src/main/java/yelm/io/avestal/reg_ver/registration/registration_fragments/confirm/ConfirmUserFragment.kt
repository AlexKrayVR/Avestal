package yelm.io.avestal.reg_ver.registration.registration_fragments.confirm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FragmentConfirmUserBinding
import yelm.io.avestal.reg_ver.model.UserViewModel
import yelm.io.avestal.reg_ver.registration.phone_registration.view.HostRegistration
import yelm.io.avestal.reg_ver.registration.registration_fragments.USER_ID_IMAGE_REQUEST_CODE
import yelm.io.avestal.reg_ver.registration.registration_fragments.USER_SELFIE_IMAGE_REQUEST_CODE
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient
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

        binding?.layoutUserIdPhoto?.setOnClickListener {
            checkIfCameraPermission(USER_ID_IMAGE_REQUEST_CODE)
        }

        binding?.layoutUserSelfie?.setOnClickListener {
            checkIfCameraPermission(USER_SELFIE_IMAGE_REQUEST_CODE)
        }

        binding?.further?.setOnClickListener {
            showProgress()
            RetrofitClient.getClient(RestAPI.URL_API_MAIN)
                .create(RestAPI::class.java)
                .setUserData(
                    //viewModel.user.value?.phone,
                    "79856185757",
                    "1",
                    getFIO(),
                    getData()
                )
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        hideProgress()
                        if (response.isSuccessful) {
                            if (response.code() == 201) {
                                Logging.logDebug("User created")
                            } else {
                                Logging.logError("Method setUserData() - by some reason response is null!")
                            }
                        } else {
                            Logging.logError(
                                "Method setUserData() - response is not successful. " +
                                        "Code: " + response.code() + "Message: " + response.message()
                            )
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Logging.logError("Method checkUser() - failure: $t")
                        hideProgress()
                    }
                })
        }
    }

    private fun getData(): JSONObject {
        val jsonData = JSONObject()
        try {
            jsonData.put("job_status", viewModel.user.value?.jobStatus)
            jsonData.put("region_name", viewModel.user.value?.region)
            jsonData.put("job_description", viewModel.user.value?.jobDescription)
        } catch (e: JSONException) {
            e.printStackTrace()
            hideProgress()
        }
        return jsonData
    }

    private fun getFIO(): JSONObject {
        val jsonData = JSONObject()
        try {
            jsonData.put("first_name", viewModel.user.value?.name)
            jsonData.put("last_name", viewModel.user.value?.surname)
            jsonData.put("surname", viewModel.user.value?.lastName)
            jsonData.put(
                "profile_image",
                MediaStore.Images.Media.getBitmap(
                    requireContext().contentResolver,
                    viewModel.user.value?.profilePhotoUri
                )
            )
            jsonData.put(
                "passport_image",
                convertingImageToBase64(BitmapFactory.decodeFile(viewModel.user.value?.passportPhoto?.absolutePath))
            )
            jsonData.put(
                "face_image",
                convertingImageToBase64(BitmapFactory.decodeFile(viewModel.user.value?.userSelfie?.absolutePath))
            )
        } catch (e: JSONException) {
            e.printStackTrace()
            hideProgress()

        }
        return jsonData
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
            binding?.layoutUserIdPhoto?.background?.setTint(requireContext().resources.getColor(R.color.colorBlue))
            binding?.takeUserIDPhoto?.setTextColor(requireContext().resources.getColor(R.color.white))
            binding?.number1?.setTextColor(requireContext().resources.getColor(R.color.white))
            viewModel.setUserIDPhoto(photoFile)
        }
        if (requestCode == USER_SELFIE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            binding?.layoutUserSelfie?.background?.setTint(requireContext().resources.getColor(R.color.colorBlue))
            binding?.takeUserSelfie?.setTextColor(requireContext().resources.getColor(R.color.white))
            binding?.number2?.setTextColor(requireContext().resources.getColor(R.color.white))
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

    private fun showProgress() {
        binding?.progressBarUserIdPhoto?.visibility = View.VISIBLE
        binding?.progressBarUserSelfie?.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding?.progressBarUserIdPhoto?.visibility = View.GONE
        binding?.progressBarUserSelfie?.visibility = View.GONE
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