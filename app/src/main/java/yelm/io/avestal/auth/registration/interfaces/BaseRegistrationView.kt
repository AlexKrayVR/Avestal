package yelm.io.avestal.auth.registration.interfaces

import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import yelm.io.avestal.rest.responses.user.UserInfo

@OneExecution
interface BaseRegistrationView : MvpView {
    fun showLoading();
    fun hideLoading();
    fun serverError(error: Int);
    fun startApp(userInfo: UserInfo);
}