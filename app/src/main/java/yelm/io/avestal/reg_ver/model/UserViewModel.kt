package yelm.io.avestal.reg_ver.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
//var state : SavedStateHandle
class UserViewModel()  : ViewModel() {

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

}