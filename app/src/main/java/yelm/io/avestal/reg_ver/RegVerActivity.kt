package yelm.io.avestal.reg_ver

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import yelm.io.avestal.*
import yelm.io.avestal.app_settings.SharedPreferencesSetting
import yelm.io.avestal.databinding.RegVerActivityBinding
import yelm.io.avestal.main.host.AppActivity
import yelm.io.avestal.reg_ver.registration.phone_registration.view.HostRegistration
import yelm.io.avestal.reg_ver.registration.phone_registration.view.LoginFragment
import yelm.io.avestal.reg_ver.registration.registration_fragments.*
import yelm.io.avestal.reg_ver.registration.registration_fragments.confirm.ConfirmUserFragment
import yelm.io.avestal.reg_ver.verification.VerificationFragment

class RegVerActivity : AppCompatActivity(), HostRegistration {
    private lateinit var binding: RegVerActivityBinding

    //var verificationCode = ""
    private var toast: Toast? = null

    private val whatIsYourWorkFragment: Fragment? = null
    private val fullNameFragment: Fragment? = null
    private val regionFragment: Fragment? = null
    private val infoFragment: Fragment? = null
    private val userPhotoFragment: Fragment? = null
    private val confirmUserFragment: Fragment? = null
    private val finishFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Avestal)
        binding = RegVerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SharedPreferencesSetting.initSharedPreferencesSettings(this)

        //openWhatIsYourWorkFragment()
        //openProfilePhotoFragment()
        //TODO return to this point
        //checkUser()
        startApp()
    }

    private fun checkUser() {
        if (SharedPreferencesSetting.getSettings().contains(SharedPreferencesSetting.USER_NAME)) {
            startApp();
        } else {
            openRegistrationFragment("")
        }
    }

    override fun openRegistrationFragment(phone: String) {
        val registrationFragment: Fragment = LoginFragment.newInstance(phone)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, registrationFragment).commit()
    }

    override fun openValidationFragment(phone: String) {
        val validationFragment: Fragment = VerificationFragment.newInstance(phone)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, validationFragment).commit()
    }

    override fun startApp() {
        startActivity(Intent(this, AppActivity::class.java))
        finish()
    }

//    override fun onBackPressed() {
//        val fragmentList = supportFragmentManager.fragments
//        for (fragment in fragmentList) {
//            if (fragment is OnBackPressedListener) {
//                (fragment as OnBackPressedListener).doBack()
//                return
//            }
//        }
//        finish()
//    }



    override fun openWhatIsYourWorkFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            R.id.container,
            whatIsYourWorkFragment ?: WhatIsYourWorkFragment.newInstance()
        ).commit()
    }

    override fun openFullNameFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .add(R.id.container, fullNameFragment ?: FullNameFragment.newInstance())
            .addToBackStack("FullName")
            .commit()
    }

    override fun openFinishFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .add(R.id.container, fullNameFragment ?: FullNameFragment.newInstance())
            .addToBackStack("FullName")
            .commit()
    }


    override fun openRegionFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .add(R.id.container, regionFragment ?: RegionFragment.newInstance())
            .addToBackStack("Region")
            .commit()
    }

    override fun openInfoFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .add(R.id.container, infoFragment ?: InfoFragment.newInstance())
            .addToBackStack("Info")
            .commit()
    }

    override fun openConfirmUserFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .add(R.id.container, confirmUserFragment ?: ConfirmUserFragment.newInstance())
            .addToBackStack("ConfirmUser")
            .commit()
    }

    override fun openProfilePhotoFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .add(R.id.container, userPhotoFragment ?: UserProfilePhotoFragment.newInstance())
            .addToBackStack("UserPhoto")
            .commit()
    }

     override fun requestCameraPermissions() {
         if (!hasCameraPermission()) {
            ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSIONS,
                REQUEST_PERMISSIONS_CAMERA_CODE
            )
        }
    }

    override fun back(){
        this.onBackPressed()
    }

    override fun requestReadExternalStoragePermission() {
        if (!hasReadExternalStoragePermission()) {
            ActivityCompat.requestPermissions(
                this,
                READ_WRITE_EXTERNAL_PERMISSIONS,
                REQUEST_PERMISSIONS_READ_WRITE_STORAGE_CODE
            )
        }
    }

    override fun hasCameraPermission(): Boolean {
        val result = ContextCompat
            .checkSelfPermission(
                this,
                CAMERA_PERMISSIONS[0]
            )
        return result == PackageManager.PERMISSION_GRANTED
    }

    override fun hasReadExternalStoragePermission(): Boolean {
        val result = ContextCompat
            .checkSelfPermission(
                this,
                READ_WRITE_EXTERNAL_PERMISSIONS[0]
            )
        return result == PackageManager.PERMISSION_GRANTED
    }

    override fun showToast(message: Int) {
        toast?.cancel()
        toast = Toast.makeText(this, resources?.getString(message), Toast.LENGTH_LONG)
        toast?.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==REQUEST_PERMISSIONS_CAMERA_CODE){
            if (hasCameraPermission()){
                Logging.logDebug("Permission CAMERA added")
            }else{
                Logging.logDebug("Permission CAMERA denied")
            }
        } else if(requestCode==REQUEST_PERMISSIONS_READ_WRITE_STORAGE_CODE){
            if (hasReadExternalStoragePermission()){
                Logging.logDebug("Permission Storage added")
            }else{
                Logging.logDebug("Permission Storage denied")
            }
        }else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}