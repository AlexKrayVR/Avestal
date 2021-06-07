package yelm.io.avestal.reg_ver.verification.presenter

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.reg_ver.verification.view.VerificationView
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient
import yelm.io.avestal.rest.responses.AuthResponse
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class VerificationPresenter(private var view: VerificationView?) {

    fun detachView() {
        view = null
    }

    fun resendPhone(phone: String) {
        RetrofitClient.getClient(RestAPI.URL_API_MAIN)
            .create(RestAPI::class.java)
            .code(phone)
            .enqueue(object : Callback<AuthResponse?> {
                override fun onResponse(
                    call: Call<AuthResponse?>,
                    response: Response<AuthResponse?>
                ) {
                    view?.hideLoading()
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            Logging.logDebug("auth: ${response.body().toString()}")
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

    private fun getSHA2(s: String): String {
        try {
            // Create SHA2 Hash
            val digest = MessageDigest
                .getInstance("SHA-256")
            digest.update(s.toByteArray())
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

     fun compareSHA2(code: String,hash:String ) {
         Logging.logDebug("code: $code")
         val sha2 = getSHA2(code)
        Logging.logDebug("sha2: $sha2")
        Logging.logDebug("hash: $hash")
        if (hash == sha2) {
            view?.startRegistration()
        } else {
            view?.loginPhoneError(R.string.codeIncorrect)
        }
    }
}