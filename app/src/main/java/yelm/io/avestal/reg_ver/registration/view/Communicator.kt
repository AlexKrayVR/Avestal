package yelm.io.avestal.reg_ver.registration.view

interface Communicator {
   fun openRegistrationFragment(phone: String)
   fun openValidationFragment(phone: String)
   fun startApp()
   fun showToast(message: Int)
}