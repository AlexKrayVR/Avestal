package yelm.io.avestal.main.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yelm.io.avestal.rest.responses.user.UserInfo

class UserInfoModel() : ViewModel() {

   private var userInfo: MutableLiveData<UserInfo> = MutableLiveData<UserInfo>()

    fun setUserInfo(userInfo: UserInfo) {
        this.userInfo.value = userInfo
    }

    fun getUserInfo(): UserInfo? {
        return userInfo.value
    }
}