package yelm.io.avestal.reg_ver.login.registration_fragments.user_profile_photo

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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FragmentUserPhotoBinding
import yelm.io.avestal.reg_ver.host.HostRegistration
import yelm.io.avestal.reg_ver.login.registration_fragments.PROFILE_IMAGE_REQUEST_CODE
import yelm.io.avestal.reg_ver.model.UserViewModel
import yelm.io.avestal.rest.KotlinAPI
import yelm.io.avestal.rest.UploadRequestBody
import yelm.io.avestal.rest.getFileName
import yelm.io.avestal.rest.responses.ImageLink
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class UserProfilePhotoFragment : Fragment(), UploadRequestBody.UploadCallback {

    private lateinit var viewModel: UserViewModel
    private var binding: FragmentUserPhotoBinding? = null
    private var hostRegistration: HostRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserPhotoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    private fun checkIfHasReadExternalStoragePermission() {
        if (hostRegistration?.hasReadExternalStoragePermission() == true) {
            openImage()
        } else {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        viewModel.user.observe(requireActivity(), {
            Logging.logDebug(it.toString())
        })

        if(viewModel.user.value?.profilePhoto!=""){

            Glide.with(this)
                .load(viewModel.user.value?.profilePhoto)
                .placeholder(R.drawable.ic_user_picture_holder)
                .transform(
                    CenterCrop(), RoundedCorners(
                        resources.getDimension(R.dimen.dimens_16dp)
                            .toInt()
                    )
                )
                .into(binding?.userImage!!)
            binding?.refreshImage?.visibility = View.VISIBLE
            binding?.attachPhoto?.visibility = View.GONE
        }else{
            Glide.with(this)
                .load(
                    AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.ic_user_picture_holder
                    )
                )
                .transform(
                    CenterCrop(), RoundedCorners(
                        resources.getDimension(R.dimen.dimens_16dp)
                            .toInt()
                    )
                )
                .into(binding?.userImage!!)
        }





        hostRegistration?.requestReadExternalStoragePermission()

        binding?.userImage?.setOnClickListener {
            checkIfHasReadExternalStoragePermission()
        }
        binding?.attachPhoto?.setOnClickListener {
            checkIfHasReadExternalStoragePermission()
        }
        binding?.refreshImage?.setOnClickListener {
            checkIfHasReadExternalStoragePermission()
        }

        binding?.further?.setOnClickListener {
            hostRegistration?.openConfirmUserFragment()
        }

        binding?.back?.setOnClickListener {
            hostRegistration?.back()
        }

    }

    private fun openImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, PROFILE_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PROFILE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            sendPicture(data?.data!!)
            Logging.logDebug("${data.data}")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun sendPicture(uri: Uri) {
        val parcelFileDescriptor =
            context?.contentResolver?.openFileDescriptor(uri, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(context?.cacheDir, context?.contentResolver?.getFileName(uri)!!)
        Logging.logDebug("file.path: ${file.path}")

        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        //binding.progressBar.progress = 0
        val body = UploadRequestBody(file, "image", this)
        showLoading()
        KotlinAPI().uploadImage(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            )//,
            //RequestBody.create("multipart/form-data".toMediaTypeOrNull(), "json")
        ).enqueue(object : Callback<ImageLink> {
            override fun onFailure(call: Call<ImageLink>, t: Throwable) {
                //binding.progressBar.progress = 0
                Logging.logDebug("onFailure: $t")
                hostRegistration?.showToast(R.string.serverError)
                hideLoading()
            }

            override fun onResponse(
                call: Call<ImageLink>,
                response: Response<ImageLink>
            ) {
                hideLoading()
                Logging.logDebug("response.body(): ${response.body()}")
                response.body()?.let {
                    showImage(it.link)
                    viewModel.setProfilePhoto(it.link)
                    binding?.further?.isEnabled = true
                    hostRegistration?.showToast(R.string.photoUploaded)
                    //binding.progressBar.progress = 100
                } ?: hostRegistration?.showToast(R.string.serverError)

            }
        })
    }

    fun showImage(url: String) {
        Glide.with(this)
            .load(url)
            .override(800, 800)
            .transform(
                CenterCrop(), RoundedCorners(
                    resources.getDimension(R.dimen.dimens_16dp)
                        .toInt()
                )
            )
            .into(binding?.userImage!!)
        binding?.refreshImage?.visibility = View.VISIBLE
        binding?.attachPhoto?.visibility = View.GONE
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

    companion object {
        fun newInstance() = UserProfilePhotoFragment()
    }

    override fun onProgressUpdate(percentage: Int) {

    }

    private fun showLoading() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding?.progressBar?.visibility = View.GONE
    }
}