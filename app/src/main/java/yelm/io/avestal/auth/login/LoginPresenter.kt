package yelm.io.avestal.auth.login

import android.widget.EditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.auth.common.PhoneTextFormatter
import yelm.io.avestal.rest.responses.AuthResponse
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient

class LoginPresenter(private var view: LoginView?) {

    fun detachView() {
        view = null
    }

    /**
     * clean phone mask then validate the phone - must be 11 numbers
     */
    fun phoneValidation(phone: String) {
        if (phone.replace("\\D".toRegex(), "").length != 11) {
            view?.loginPhoneError(R.string.enterCorrectPhoneNumber)
        } else {
            getCode(phone)
        }
    }

    /**
     * get code from server to enter the app
     */
    private fun getCode(phone: String) {
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


    fun setTextFormatter(editText: EditText) {
        editText.addTextChangedListener(
            PhoneTextFormatter(
                editText,
                "+7 (###) ###-##-##"
            )
        )
    }




}