package yelm.io.avestal.reg_ver.registration.phone_registration.view

import yelm.io.avestal.reg_ver.registration.phone_registration.model.AuthResponseKotlin

interface HostRegistration {
   fun openLoginFragment()
   fun openValidationFragment(phone: String, response: AuthResponseKotlin)
   fun startApp()
   fun back()
   fun showToast(message: Int)
   fun openWhatIsYourWorkFragment()
   fun openFullNameFragment()
   fun openConfirmUserFragment()
   fun openRegionFragment()
   fun openInfoFragment()
   fun openProfilePhotoFragment()
   fun openFinishFragment()
   fun requestCameraPermissions()
   fun requestReadExternalStoragePermission()
   fun hasCameraPermission() : Boolean
   fun hasReadExternalStoragePermission() : Boolean

}