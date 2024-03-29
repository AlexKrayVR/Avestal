package yelm.io.avestal.auth.verification.presenter

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.app_settings.SharedPreferencesSetting
import yelm.io.avestal.auth.verification.view.VerificationView
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient
import yelm.io.avestal.rest.responses.AccessToken
import yelm.io.avestal.rest.responses.AuthResponse
import yelm.io.avestal.rest.responses.user.UserInfo
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class VerificationPresenter(private var view: VerificationView?) {

    fun detachView() {
        view = null
    }

    /**
     * resend phone to server to get AuthResponse
     * that contain auth and hash (for compare with sha2 of sms message code)
     */
    fun resendPhone(phone: String) {
        view?.showLoading()
        RetrofitClient.getClient(RestAPI.URL_API_MAIN)
            .create(RestAPI::class.java)
            .code(phone.replace("\\D".toRegex(), ""))
            .enqueue(object : Callback<AuthResponse?> {
                override fun onResponse(
                    call: Call<AuthResponse?>,
                    response: Response<AuthResponse?>
                ) {
                    view?.hideLoading()
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            Logging.logDebug("auth: ${response.body().toString()}")

//                            response.body().let {
//                                if (it != null) {
//                                    view?.loginPhoneSuccess(it)
//                                } else {
//                                    view?.loginPhoneError(R.string.serverError)
//                                }
//                            }

                            if (response.body()?.auth == true) {
                                view?.loginPhoneSuccess(response.body()!!)
                            }
                        } else {
                            view?.loginPhoneError(R.string.serverError)
                        }
                    } else {
                        view?.loginPhoneError(R.string.serverError)
                    }
                }

                override fun onFailure(call: Call<AuthResponse?>, t: Throwable) {
                    view?.hideLoading()
                    view?.loginPhoneError(R.string.serverError)
                }
            })
    }

    /**
     * get SHA2 from our sms message code
     */
    private fun getSHA2(code: String): String {
        try {
            // Create SHA2 Hash
            val digest = MessageDigest
                .getInstance("SHA-256")
            digest.update(code.toByteArray())
            val messageDigest = digest.digest()
            // Create Hex String
            val hexString = StringBuilder()
            for (aMessageDigest in messageDigest) {
                var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            Logging.logDebug("NoSuchAlgorithmException")
        }
        return ""
    }

    /**
     * compare sha2 of sms message code with hash from server respond
     * send result to view
     */
    fun compareSHA2(code: String, hash: String) {
        Logging.logDebug("code: $code")
        val sha2 = getSHA2(code)
        Logging.logDebug("sha2: $sha2")
        Logging.logDebug("hash: $hash")
        if (hash == sha2) {
            view?.codeIsCorrect()
        } else {
            view?.loginPhoneError(R.string.codeIncorrect)
        }
    }

    /**
     * get bearer token for another query of application (lives 24h)
     * save it in app settings - key: BEARER_TOKEN
     * thereafter start main app
     */
    fun getBearerToken(phone: String) {
        view?.showLoading()
        RetrofitClient.getClient(RestAPI.URL_API_MAIN)
            .create(RestAPI::class.java)
            .getAccessToken(
                phone.replace("\\D".toRegex(), "")
            )
            .enqueue(object : Callback<AccessToken> {
                override fun onResponse(
                    call: Call<AccessToken>,
                    response: Response<AccessToken>
                ) {
                    view?.hideLoading()
                    if (response.isSuccessful) {
                        response.body().let {
                            if (it != null) {
                                Logging.logDebug("BearerToken: ${it.accessToken}")
                                SharedPreferencesSetting.setData(
                                    SharedPreferencesSetting.BEARER_TOKEN,
                                    response.body()!!.accessToken
                                )
                                SharedPreferencesSetting.setData(
                                    SharedPreferencesSetting.USER_PHONE,
                                    phone.replace("\\D".toRegex(), "")
                                )
                                getUserInfo()
                            } else {
                                view?.serverError(R.string.serverError)
                                Logging.logError("Method getBearerToken() - by some reason response is null!")
                            }
                        }
                    } else {
                        view?.serverError(R.string.serverError)
                        Logging.logError(
                            "Method getBearerToken() - response is not successful. " +
                                    "Code: " + response.code() + "Message: " + response.message()
                        )
                    }
                }

                override fun onFailure(call: Call<AccessToken>, t: Throwable) {
                    Logging.logError("Method getBearerToken() - failure: $t")
                    view?.hideLoading()
                    view?.serverError(R.string.serverError)
                }
            })
    }

    /**
     * get all info about user: fio, rating, photos etc.
     * thereafter start main app and transfer user data
     */
    private fun getUserInfo() {
        view?.showLoading()
        RetrofitClient.getClient(RestAPI.URL_API_MAIN)
            .create(RestAPI::class.java)
            .getUserInfo("Bearer ${SharedPreferencesSetting.getDataString(SharedPreferencesSetting.BEARER_TOKEN)}")
            .enqueue(object : Callback<UserInfo?> {
                override fun onResponse(
                    call: Call<UserInfo?>,
                    response: Response<UserInfo?>
                ) {
                    view?.hideLoading()
                    Logging.logDebug("response.code(): ${response.code()}")
                    if (response.isSuccessful) {
                        response.body().let {
                            if (it != null) {
                                view?.startApp(it)
                            } else {
                                view?.serverError(R.string.serverError)
                            }
                        }
                    } else {
                        view?.serverError(R.string.serverError)
                    }
                }

                override fun onFailure(call: Call<UserInfo?>, t: Throwable) {
                    view?.hideLoading()
                    Logging.logDebug("onFailure: ${t.message}")
                    view?.serverError(R.string.serverError)
                }
            })
    }


}