package yelm.io.avestal.reg_ver.login.registration_fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import yelm.io.avestal.app_settings.SharedPreferencesSetting
import yelm.io.avestal.databinding.FragmentFinishBinding
import yelm.io.avestal.reg_ver.host.HostRegistration
import yelm.io.avestal.reg_ver.model.UserViewModel
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient
import yelm.io.avestal.rest.responses.AccessToken


class FinishFragment : Fragment() {

    private var binding: FragmentFinishBinding? = null
    private var hostRegistration: HostRegistration? = null
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
            hostRegistration?.startApp()
        }
        binding?.registrationFinished?.setOnClickListener {
            getBearerToken()
        }
        sendUserData()
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
                            hostRegistration?.startApp()
                        } else {
                            hostRegistration?.showToast(R.string.serverError)
                            Logging.logError("Method getAccessToken() - by some reason response is null!")
                        }
                    } else {
                        hostRegistration?.showToast(R.string.serverError)
                        Logging.logError(
                            "Method getAccessToken() - response is not successful. " +
                                    "Code: " + response.code() + "Message: " + response.message()
                        )
                    }
                }

                override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                    Logging.logError("Method getAccessToken() - failure: $t")
                    hostRegistration?.showToast(R.string.serverError)
                    hideLoading()
                }
            })
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
                "1",
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
                            binding?.registrationFinished?.isEnabled = true
                            Logging.logDebug("User created")
                        } else {
                            hostRegistration?.showToast(R.string.serverError)
                            Logging.logError("Method setUserData() - by some reason response is null!")
                        }
                    } else {
                        hostRegistration?.showToast(R.string.serverError)
                        Logging.logError(
                            "Method setUserData() - response is not successful. " +
                                    "Code: " + response.code() + "Message: " + response.message()
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Logging.logError("Method setUserData() - failure: $t")
                    hostRegistration?.showToast(R.string.serverError)
                    hideLoading()
                }
            })
    }

    companion object {
        fun newInstance() = FinishFragment()
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

    private fun showLoading() {
        binding?.progressBar?.visibility = View.VISIBLE
    }

    fun hideLoading() {
        binding?.progressBar?.visibility = View.GONE
    }


}