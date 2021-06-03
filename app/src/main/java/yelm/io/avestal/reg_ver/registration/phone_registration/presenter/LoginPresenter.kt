package yelm.io.avestal.reg_ver.registration.phone_registration.presenter

import android.widget.EditText
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import yelm.io.avestal.Logging
import yelm.io.avestal.R
import yelm.io.avestal.reg_ver.common.PhoneTextFormatter
import yelm.io.avestal.reg_ver.registration.phone_registration.model.AuthResponse
import yelm.io.avestal.reg_ver.registration.phone_registration.model.AuthResponseKotlin
import yelm.io.avestal.reg_ver.registration.phone_registration.view.RegistrationView
import yelm.io.avestal.rest.RestAPI
import yelm.io.avestal.rest.RetrofitClient

class LoginPresenter(private var view: RegistrationView?) {

    fun detachView() {
        view = null
    }

//sha256
//1111

    fun phoneValidation(phone: String) {
        val phoneFinal = phone.replace("\\D".toRegex(), "")
        if (phoneFinal.length != 11) {
            view?.validationPhoneError(R.string.enterCorrectPhoneNumber)
        } else {
            view?.showLoading()
            RetrofitClient.getClient(RestAPI.URL_API_MAIN)
                .create(RestAPI::class.java)
                .code(phone)
                .enqueue(object : Callback<AuthResponseKotlin?> {
                    override fun onResponse(
                        call: Call<AuthResponseKotlin?>,
                        response: Response<AuthResponseKotlin?>
                    ) {
                        view?.hideLoading()
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                Logging.logDebug("auth: ${response.body().toString()}")
                                if (response.body()?.auth == true) {
                                    view?.authPhoneSuccess()
                                } else {
                                    view?.validationPhoneSuccess(phone, response.body()!!)
                                }
                            } else {
                                view?.validationPhoneError(R.string.serverError)
                            }

                        } else {
                            view?.validationPhoneError(R.string.serverError)
                        }
                    }

                    override fun onFailure(call: Call<AuthResponseKotlin?>, t: Throwable) {
                        view?.hideLoading()
                        view?.validationPhoneError(R.string.serverError)
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