package yelm.io.avestal.reg_ver.login.phone_registration.presenter

import android.widget.EditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.reg_ver.common.PhoneTextFormatter
import yelm.io.avestal.rest.responses.AuthResponse
import yelm.io.avestal.reg_ver.login.phone_registration.view.LoginView
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient

class LoginPresenter(private var view: LoginView?) {

    fun detachView() {
        view = null
    }

    fun phoneValidation(phone: String) {
        val phoneFinal = phone.replace("\\D".toRegex(), "")
        if (phoneFinal.length != 11) {
            view?.loginPhoneError(R.string.enterCorrectPhoneNumber)
        } else {
            view?.showLoading()
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
                                view?.loginPhoneSuccess(phone, response.body()!!)
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
    }

    fun setTextFormatter(editText: EditText) {
        editText.addTextChangedListener(
            PhoneTextFormatter(
                editText,
                "+7 (###) ###-##-##"
            )
        )
    }
}