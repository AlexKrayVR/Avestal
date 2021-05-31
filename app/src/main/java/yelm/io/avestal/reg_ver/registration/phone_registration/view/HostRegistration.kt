package yelm.io.avestal.reg_ver.registration.phone_registration.view

interface HostRegistration {
   fun openRegistrationFragment(phone: String)
   fun openValidationFragment(phone: String)
   fun startApp()
   fun showToast(message: Int)
   fun openWhatIsYourWorkFragment()
   fun openFullNameFragment()
   fun openRegionFragment()
   fun openInfoFragment()
   fun openUserPhotoFragment()


}