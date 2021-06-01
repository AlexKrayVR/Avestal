package yelm.io.avestal.reg_ver.model

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import java.io.File

//var state : SavedStateHandle
class UserViewModel() : ViewModel() {

    var user: MutableLiveData<User> = MutableLiveData<User>()

    init {
        user.value = User()
    }

//    fun saveUser() {
//        state.set("user", user.value)
//    }
//
//    fun getUser(): User {
//        return state.get("user")?: User()
//    }

    fun setWorkType(workType: String) {
        val temp = user.value
        temp?.workType = workType
        user.value = temp
    }

    fun setFullName(name: String, surname: String, lastName: String) {
        val temp = user.value
        temp?.name = name
        temp?.surname = surname
        temp?.lastName = lastName
        user.value = temp
    }

    fun setInfo(info: String) {
        val temp = user.value
        temp?.info = info
        user.value = temp
    }

    fun setProfilePhotoUri(profilePhotoUri: Uri) {
        val temp = user.value
        temp?.profilePhotoUri = profilePhotoUri
        user.value = temp
    }

    fun setUserIDPhoto(userIDPhoto: File) {
        val temp = user.value
        temp?.userIDPhoto = userIDPhoto
        user.value = temp
    }

    fun setUserSelfie(userSelfie: File) {
        val temp = user.value
        temp?.userSelfie = userSelfie
        user.value = temp
    }

    fun userFilesAdded(): Boolean {
        if (user.value?.userSelfie != null && user.value?.userIDPhoto != null) {
            return true
        }
        return false
    }


}