package yelm.io.avestal.reg_ver.registration.phone_registration.presenter

import yelm.io.avestal.R
import yelm.io.avestal.reg_ver.registration.phone_registration.view.RegistrationView

class LoginPresenter(private var view: RegistrationView?) {

    fun detachView() {
        view = null
    }

    fun phoneValidation(phone: String) {
        val phoneFinal = phone.replace("\\D".toRegex(), "")
        if (phoneFinal.length != 11) {
            view?.validationPhoneError(R.string.enterCorrectPhoneNumber)
        } else {
            view?.validationPhoneSuccess(phone)
        }
    }


}