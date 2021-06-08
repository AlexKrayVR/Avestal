package yelm.io.avestal.main.user

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.app_settings.SharedPreferencesSetting
import yelm.io.avestal.databinding.FragmentUserBinding
import yelm.io.avestal.main.user.photo.PickPhotoActivity
import yelm.io.avestal.main.user.region.RegionActivity
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient
import yelm.io.avestal.rest.responses.Offer

class UserFragment : Fragment() {

    private var binding: FragmentUserBinding? = null

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

        binding?.layoutUserRegion?.setOnClickListener {
            requireContext().startActivity(Intent(activity, RegionActivity::class.java))
        }

        getUserInfo()
    }

    private fun getUserInfo() {
        RetrofitClient.getClient(RestAPI.URL_API_MAIN)
            .create(RestAPI::class.java)
            .getOrders("Bearer ${SharedPreferencesSetting.getDataString(SharedPreferencesSetting.BEARER_TOKEN)}")
            .enqueue(object : Callback<Offer?> {
                override fun onResponse(
                    call: Call<Offer?>,
                    response: Response<Offer?>
                ) {
                    hideLoading()
                    Logging.logDebug("response.code(): ${response.code()}")
                    if (response.isSuccessful) {
                        //initAdapter(response.body()!!.data)
                        //setOffersSize(response.body()!!.data.size)
                    }
                }

                override fun onFailure(call: Call<Offer?>, t: Throwable) {
                    Logging.logDebug("onFailure: ${t.message}")
                    hideLoading()
                }
            })

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