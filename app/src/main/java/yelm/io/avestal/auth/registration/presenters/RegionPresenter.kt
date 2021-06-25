package yelm.io.avestal.auth.registration.presenters

import moxy.InjectViewState
import moxy.MvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.app_settings.SharedPreferencesSetting
import yelm.io.avestal.auth.registration.interfaces.RegionView
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient
import yelm.io.avestal.rest.responses.AccessToken
import yelm.io.avestal.rest.responses.user.UserInfo


@InjectViewState
class RegionPresenter : MvpPresenter<RegionView>() {

    /**
     * validate region - must not be empty
     */
    fun validateInput(region: String) {
        if (region.trim().isEmpty()) {
            viewState.validationRegionError(R.string.addressEmpty)
        } else {
            viewState.validationRegionSuccess(region.trim())
        }
    }

    /**
     * get all info about user: fio, rating, photos etc.
     * thereafter start main app and transfer user data
     */
    fun getUserInfo() {
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
                                viewState.startApp(it)
                            } else {
                                viewState.serverError(R.string.serverError)
                            }
                        }
                    } else {
                        viewState.serverError(R.string.serverError)
                    }
                }

                override fun onFailure(call: Call<UserInfo?>, t: Throwable) {
                    Logging.logDebug("onFailure: ${t.message}")
                    viewState.serverError(R.string.serverError)
                }
            })
    }

    /**
     * get bearer token for another query of application (lives 24h)
     * save it in app settings - key: BEARER_TOKEN
     *
     * if response success - call method getUserInfo()
     * otherwise show toast with error
     */
    private fun getBearerToken(phone: String) {
        viewState.showLoading()
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
                    viewState.hideLoading()
                    if (response.isSuccessful) {
                        response.body().let {
                            if (it != null) {
                                Logging.logDebug("BearerToken: ${it.accessToken}")
                                SharedPreferencesSetting.setData(
                                    SharedPreferencesSetting.BEARER_TOKEN,
                                    it.accessToken
                                )
                                getUserInfo()
                            } else {
                                viewState.serverError(R.string.serverError)
                            }
                        }
                    } else {
                        viewState.serverError(R.string.serverError)
                        Logging.logError(
                            "Method getBearerToken() - response is not successful. " +
                                    "Code: " + response.code() + "Message: " + response.message()
                        )
                    }
                }

                override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                    Logging.logError("Method getBearerToken() - failure: $t")
                    viewState.hideLoading()
                    viewState.serverError(R.string.serverError)
                }
            })
    }


}