package yelm.io.avestal.auth.registration.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.app_settings.SharedPreferencesSetting
import yelm.io.avestal.databinding.FragmentFinishBinding
import yelm.io.avestal.auth.host.HostAuth
import yelm.io.avestal.auth.model.UserViewModel
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient
import yelm.io.avestal.rest.responses.AccessToken
import yelm.io.avestal.rest.responses.UserInfo


class FinishFragment : Fragment() {

    private var binding: FragmentFinishBinding? = null
    private var mHostAuth: HostAuth? = null
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFinishBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        binding?.close?.setOnClickListener {
            mHostAuth?.closeActivity()
        }
        binding?.registrationFinished?.setOnClickListener {
            getBearerToken()
        }
    }

    /**
     * get bearer token for another query of application (lives 24h)
     * save it in app settings - key: BEARER_TOKEN
     *
     * also save phone in app settings - key: USER_PHONE
     * for auto launch of app
     *
     * thereafter start main app
     */
    private fun getBearerToken() {
        showLoading()
        val phone = viewModel.user.value?.phone?.replace("\\D".toRegex(), "")
        RetrofitClient.getClient(RestAPI.URL_API_MAIN)
            .create(RestAPI::class.java)
            .getAccessToken(
                phone
            )
            .enqueue(object : Callback<AccessToken> {
                override fun onResponse(
                    call: Call<AccessToken>,
                    response: Response<AccessToken>
                ) {
                    hideLoading()
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            SharedPreferencesSetting.setData(
                                SharedPreferencesSetting.BEARER_TOKEN,
                                response.body()!!.accessToken
                            )
                            SharedPreferencesSetting.setData(
                                SharedPreferencesSetting.USER_PHONE,
                                phone
                            )
                            getUserInfo()
                        } else {
                            mHostAuth?.showToast(R.string.serverError)
                            Logging.logError("Method getAccessToken() - by some reason response is null!")
                        }
                    } else {
                        mHostAuth?.showToast(R.string.serverError)
                        Logging.logError(
                            "Method getAccessToken() - response is not successful. " +
                                    "Code: " + response.code() + "Message: " + response.message()
                        )
                    }
                }

                override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                    Logging.logError("Method getAccessToken() - failure: $t")
                    mHostAuth?.showToast(R.string.serverError)
                    hideLoading()
                }
            })
    }


    /**
     * get all info about user: fio, rating, photos etc.
     * thereafter start main app and transfer user data
     */
    private fun getUserInfo() {
        RetrofitClient.getClient(RestAPI.URL_API_MAIN)
            .create(RestAPI::class.java)
            .getUserInfo("Bearer ${SharedPreferencesSetting.getDataString(SharedPreferencesSetting.BEARER_TOKEN)}")
            .enqueue(object : Callback<UserInfo?> {
                override fun onResponse(
                    call: Call<UserInfo?>,
                    response: Response<UserInfo?>
                ) {
                    Logging.logDebug("response.code(): ${response.code()}")
                    if (response.isSuccessful) {
                        response.body().let {
                            if (it != null) {
                                mHostAuth?.startApp(it)
                            } else {
                                mHostAuth?.showToast(R.string.serverError)
                            }
                        }
                    } else {
                        mHostAuth?.showToast(R.string.serverError)
                    }
                }

                override fun onFailure(call: Call<UserInfo?>, t: Throwable) {
                    Logging.logDebug("onFailure: ${t.message}")
                    mHostAuth?.showToast(R.string.serverError)
                }
            })
    }

    companion object {
        fun newInstance() = FinishFragment()
    }

    override fun onDetach() {
        super.onDetach()
        mHostAuth = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is HostAuth) {
            mHostAuth = activity as HostAuth
        } else {
            throw RuntimeException(activity.toString() + " must implement Communicator interface")
        }
    }

    private fun showLoading() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding?.progressBar?.visibility = View.GONE
    }
}