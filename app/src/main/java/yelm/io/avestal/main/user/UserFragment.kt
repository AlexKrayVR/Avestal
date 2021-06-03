package yelm.io.avestal.main.user

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import yelm.io.avestal.R
import yelm.io.avestal.databinding.FragmentUserBinding
import yelm.io.avestal.main.user.photo.PickPhotoActivity
import yelm.io.avestal.main.user.region.RegionActivity

class UserFragment : Fragment() {

    private var binding: FragmentUserBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var first = TempWorkType("name", "3")
        var second = TempWorkType("name", "5")
        var third = TempWorkType("name", "2")

        val typesArray = arrayListOf<TempWorkType>(first, second, third)
        val adapter = WorkTypeAdapter(typesArray, requireContext())

        binding?.recyclerWorkTypes?.adapter = adapter

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

        binding?.imageView?.setOnClickListener {
            requireContext().startActivity(Intent(activity, PickPhotoActivity::class.java))
        }

        binding?.layoutUserRegion?.setOnClickListener{
            requireContext().startActivity(Intent(activity, RegionActivity::class.java))
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}