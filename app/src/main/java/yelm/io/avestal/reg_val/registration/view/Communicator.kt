package yelm.io.avestal.reg_val.registration.view

interface Communicator {
   fun openRegistrationFragment(phone: String)
   fun openValidationFragment(phone: String)
   fun startApp()
   fun showToast(message: Int)
}