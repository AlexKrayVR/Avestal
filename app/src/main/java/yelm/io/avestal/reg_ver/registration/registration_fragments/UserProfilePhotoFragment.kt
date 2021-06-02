package yelm.io.avestal.reg_ver.registration.registration_fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.FileUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FragmentUserPhotoBinding
import yelm.io.avestal.reg_ver.model.UserViewModel
import yelm.io.avestal.reg_ver.registration.phone_registration.view.HostRegistration
import yelm.io.avestal.rest.UploadImage
import java.io.File


class UserProfilePhotoFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    private var binding: FragmentUserPhotoBinding? = null
    private var hostRegistration: HostRegistration? = null


    private var imageUri: Uri? = null

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
            if (imageUri != null) {
                viewModel.setProfilePhotoUri(imageUri!!)
                hostRegistration?.openConfirmUserFragment()
            } else {
                hostRegistration?.showToast(R.string.photoIsEmpty)
            }
        }
    }

    private fun openImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(intent, PROFILE_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PROFILE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = data!!.data
            Glide.with(this)
                .load(data.data)
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
            Logging.logDebug("${data?.data}")
            //var bitmap =  MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data?.data);

            Logging.logDebug("${imageUri?.toString()}")
            Logging.logDebug("${imageUri?.path}")

            val file1 = File(imageUri?.path)
            Logging.logDebug("${file1?.path}")

            val file2 = yelm.io.avestal.FileUtils.getFile(requireContext(), imageUri);
            Logging.logDebug("${file2?.path}")


            UploadImage.upload(file2, imageUri, requireContext())

        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun sendPicture(){
        val file: File? = imageUri?.toFile()


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
        @JvmStatic
        fun newInstance() =
            UserProfilePhotoFragment().apply {
            }
    }
}