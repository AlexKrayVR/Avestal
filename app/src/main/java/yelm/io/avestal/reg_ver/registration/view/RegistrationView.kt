package yelm.io.avestal.reg_ver.registration.view

interface RegistrationView {
    fun registration();
    fun validationPhoneError(error: Int);
    fun validationPhoneSuccess(phone: String);
}