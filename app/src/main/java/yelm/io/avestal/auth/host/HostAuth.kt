package yelm.io.avestal.auth.host

import yelm.io.avestal.rest.responses.AuthResponse
import yelm.io.avestal.rest.responses.UserInfo

interface HostAuth {
   fun openLoginFragment()
   fun openVerificationFragment(phone: String, response: AuthResponse)
   fun startApp(userInfo: UserInfo)
   fun back()
   fun closeActivity()
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