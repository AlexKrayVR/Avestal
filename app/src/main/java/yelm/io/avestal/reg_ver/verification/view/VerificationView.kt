package yelm.io.avestal.reg_ver.verification.view

import yelm.io.avestal.rest.responses.AuthResponse

interface VerificationView {
    fun showLoading();
    fun hideLoading();
    fun loginPhoneError(error: Int);
    fun loginPhoneSuccess(response: AuthResponse);
    fun codeIsCorrect();
    fun startApp();
}