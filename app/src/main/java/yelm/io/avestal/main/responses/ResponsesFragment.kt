package yelm.io.avestal.main.responses

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.app_settings.SharedPreferencesSetting
import yelm.io.avestal.common.ItemOffsetTopBottomDecoration
import yelm.io.avestal.databinding.FragmentResponsesBinding
import yelm.io.avestal.main.host.AppHost
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient
import yelm.io.avestal.rest.responses.user_responses.UserResponse


class ResponsesFragment : Fragment() {
    private var _binding: FragmentResponsesBinding? = null
    private val binding get() = _binding!!
    private var appHost: AppHost? = null
    private var adapter: ResponsesAdapter? = null

       override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResponsesBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerResponses.addItemDecoration(
            ItemOffsetTopBottomDecoration(
                resources.getDimension(R.dimen.dimens_12dp).toInt(),
                resources.getDimension(R.dimen.dimens_8dp).toInt()
                )
        )
    }

    private fun getResponses() {
        adapter?.clear()
        showLoading()
        RetrofitClient.getClient(RestAPI.URL_API_MAIN)
            .create(RestAPI::class.java)
            .getResponses("Bearer ${SharedPreferencesSetting.getDataString(SharedPreferencesSetting.BEARER_TOKEN)}")
            .enqueue(object : Callback<List<UserResponse>> {
                override fun onResponse(
                    call: Call<List<UserResponse>>,
                    response: Response<List<UserResponse>>
                ) {
                    hideLoading()
                    Logging.logDebug("response.code(): ${response.code()}")
                    if (response.isSuccessful) {
                        Logging.logDebug("size: ${response.body()?.size}")
                        adapter = ResponsesAdapter(
                            response.body()!! as MutableList<UserResponse>,
                            requireContext()
                        )
                        binding.recyclerResponses.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                    Logging.logDebug("onFailure: ${t.message}")
                    hideLoading()
                }
            })
    }

    fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        getResponses()
    }

    override fun onResume() {
        super.onResume()
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            ResponsesFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is AppHost) {
            appHost = activity as AppHost
        } else {
            throw RuntimeException(activity.toString() + " must implement AppHost interface")
        }
    }

    override fun onDetach() {
        super.onDetach()
        appHost = null
    }


}