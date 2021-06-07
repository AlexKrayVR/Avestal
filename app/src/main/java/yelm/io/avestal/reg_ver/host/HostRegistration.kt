package yelm.io.avestal.reg_ver.host

import yelm.io.avestal.rest.responses.AuthResponse

interface HostRegistration {
   fun openLoginFragment()
   fun openVerificationFragment(phone: String, response: AuthResponse)
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