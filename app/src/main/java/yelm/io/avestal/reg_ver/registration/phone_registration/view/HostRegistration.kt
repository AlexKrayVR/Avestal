package yelm.io.avestal.reg_ver.registration.phone_registration.view

interface HostRegistration {
   fun openRegistrationFragment(phone: String)
   fun openValidationFragment(phone: String)
   fun startApp()
   fun back()
   fun showToast(message: Int)
   fun openWhatIsYourWorkFragment()
   fun openFullNameFragment()
   fun openConfirmUserFragment()
   fun openRegionFragment()
   fun openInfoFragment()
   fun openUserPhotoFragment()
   fun requestCameraPermissions()
   fun requestReadExternalStoragePermission()
   fun hasCameraPermission() : Boolean
   fun hasReadExternalStoragePermission() : Boolean


}