package yelm.io.avestal.auth.verification.view

import yelm.io.avestal.rest.responses.AuthResponse
import yelm.io.avestal.rest.responses.user.UserInfo

interface VerificationView {
    fun showLoading();
    fun hideLoading();
    fun loginPhoneError(error: Int);
    fun serverError(error: Int);
    fun loginPhoneSuccess(response: AuthResponse);
    fun codeIsCorrect();
    fun startApp(userInfo: UserInfo);
}