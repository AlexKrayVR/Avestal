package yelm.io.avestal.auth.login

import yelm.io.avestal.rest.responses.AuthResponse

interface LoginView {
    fun showLoading();
    fun hideLoading();
    fun loginPhoneError(error: Int);
    fun loginPhoneSuccess(phone: String, response: AuthResponse);
}