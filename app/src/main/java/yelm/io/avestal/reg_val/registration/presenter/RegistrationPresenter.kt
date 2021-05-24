package yelm.io.avestal.reg_val.registration.presenter

import yelm.io.avestal.R
import yelm.io.avestal.reg_val.registration.view.RegistrationView

class RegistrationPresenter(private var view: RegistrationView?) {

    fun detachView() {
        view = null
    }

    fun phoneValidation(phone: String) {
        val phoneFinal = phone.replace("\\D".toRegex(), "")
        if (phoneFinal.length != 11) {
            view?.validationPhoneError(R.string.enter_correct_phone_number)
        } else {
            view?.validationPhoneSuccess(phoneFinal)
        }
    }


}