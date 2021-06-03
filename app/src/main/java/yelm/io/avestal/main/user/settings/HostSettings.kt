package yelm.io.avestal.main.user.settings

interface HostSettings {

    fun openPhoneFragment(phone: String)
    fun openSettingsFragment()
    fun openChatNotificationFragment()
    fun openOrderNotificationFragment()
    fun openMailFragment()
    fun logOut()
    fun finishActivity()
    fun back()
    fun showToast (message: Int)
}