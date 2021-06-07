package yelm.io.avestal.reg_ver.model

import android.net.Uri
import androidx.lifecycle.MutableLiveData
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

    fun setPhone(phone: String) {
        val temp = user.value
        temp?.phone = phone
        user.value = temp
    }

    fun setWorkType(jobStatus: String) {
        val temp = user.value
        temp?.jobStatus = jobStatus
        user.value = temp
    }

    fun setFullName(name: String, surname: String, lastName: String) {
        val temp = user.value
        temp?.name = name
        temp?.surname = surname
        temp?.lastName = lastName
        user.value = temp
    }

    fun setInfo(jobDescription: String) {
        val temp = user.value
        temp?.jobDescription = jobDescription
        user.value = temp
    }

    fun setRegion(region: String) {
        val temp = user.value
        temp?.region = region
        user.value = temp
    }


    fun setProfilePhotoUri(profilePhoto: String) {
        val temp = user.value
        temp?.profilePhoto = profilePhoto
        user.value = temp
    }

    fun setUserIDPhoto(passportPhoto: String) {
        val temp = user.value
        temp?.passportPhoto = passportPhoto
        user.value = temp
    }

    fun setUserSelfie(selfie: String) {
        val temp = user.value
        temp?.selfie = selfie
        user.value = temp
    }

    fun userFilesAdded(): Boolean {
        if (user.value?.selfie != null && user.value?.passportPhoto != null) {
            return true
        }
        return false
    }


}