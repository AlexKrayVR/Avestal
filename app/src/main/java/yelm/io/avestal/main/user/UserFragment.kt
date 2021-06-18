package yelm.io.avestal.main.user

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FragmentUserBinding
import yelm.io.avestal.main.model.UserInfoModel
import yelm.io.avestal.main.user.photo.PickPhotoActivity
import yelm.io.avestal.main.user.region.RegionActivity
import yelm.io.avestal.main.user.settings.ActivitySettings

class UserFragment : Fragment() {

    private var binding: FragmentUserBinding? = null
    private lateinit var viewModel: UserInfoModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(UserInfoModel::class.java)


        var first = TempWorkType("name", "3")
        var second = TempWorkType("name", "5")
        var third = TempWorkType("name", "2")

        val typesArray = arrayListOf<TempWorkType>(first, second, third)
        val adapter = WorkTypeAdapter(typesArray, requireContext())

        binding?.recyclerWorkTypes?.adapter = adapter

        setProfileImage()
        setPassportStatus()
        setUserName()
        setUserLocation()

        binding?.imageView?.setOnClickListener {
            requireContext().startActivity(Intent(activity, PickPhotoActivity::class.java))
        }

        binding?.layoutUserRegion?.setOnClickListener {
            requireContext().startActivity(Intent(activity, RegionActivity::class.java))
        }


        binding?.layoutSettings?.setOnClickListener {
            requireContext().startActivity(Intent(activity, ActivitySettings::class.java))
        }
    }

    private fun setUserName() {
        val fio =
            viewModel.getUserInfo()?.userFIO?.surname + " " +
                    viewModel.getUserInfo()?.userFIO?.firstName + " " +
                    viewModel.getUserInfo()?.userFIO?.lastName
        binding?.name?.text = fio
    }

    private fun setUserLocation() {
        val location = viewModel.getUserInfo()?.data?.regionName
        binding?.location?.text = location
    }


    private fun setPassportStatus() {

        if (viewModel.getUserInfo()?.isVerified == true) {
            binding?.status?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_passport_confirmed
                )
            )
            binding?.passport?.text = getText(R.string.passportVerified)
            binding?.passport?.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color121212
                )
            )
        } else {
            binding?.status?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_close
                )
            )
            binding?.passport?.text = getText(R.string.passportNotVerified)
            binding?.passport?.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorBDBDBD
                )
            )
        }
    }

    private fun setProfileImage() {
        val image = viewModel.getUserInfo()?.data?.profileImage

        image?.let {
            if (it.isEmpty()) {
                binding?.imageCamera?.visibility = View.VISIBLE
                Glide.with(requireContext())
                    .load(
                        ColorDrawable(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.colorUserPictureHolder
                            )
                        )
                    )
                    .transform(
                        CenterCrop(), RoundedCorners(
                            requireContext().resources.getDimension(R.dimen.dimens_10dp)
                                .toInt()
                        )
                    )
                    .into(binding?.imageView!!)
            } else {
                binding?.imageCamera?.visibility = View.GONE
                Glide.with(requireContext())
                    .load(image)
                    .transform(
                        CenterCrop(), RoundedCorners(
                            requireContext().resources.getDimension(R.dimen.dimens_10dp)
                                .toInt()
                        )
                    )
                    .into(binding?.imageView!!)
            }
        }
    }


    fun showLoading() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding?.progressBar?.visibility = View.GONE
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    companion object {
        fun newInstance() = UserFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}