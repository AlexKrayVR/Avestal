package yelm.io.avestal.reg_ver.registration.phone_registration.view

import yelm.io.avestal.reg_ver.registration.phone_registration.model.AuthResponseKotlin

interface RegistrationView {
    fun auth();
    fun showLoading();
    fun hideLoading();
    fun validationPhoneError(error: Int);
    fun validationPhoneSuccess(phone: String, response: AuthResponseKotlin);
    fun authPhoneSuccess();
}