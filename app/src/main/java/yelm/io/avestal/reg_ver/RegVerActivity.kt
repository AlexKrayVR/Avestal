package yelm.io.avestal.reg_ver

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import yelm.io.avestal.InfoFragment
import yelm.io.avestal.R
import yelm.io.avestal.RegionFragment
import yelm.io.avestal.UserPhotoFragment
import yelm.io.avestal.app_settings.SharedPreferencesSetting
import yelm.io.avestal.databinding.RegVerActivityBinding
import yelm.io.avestal.main.host.AppActivity
import yelm.io.avestal.reg_ver.registration.phone_registration.view.HostRegistration
import yelm.io.avestal.reg_ver.registration.phone_registration.view.LoginFragment
import yelm.io.avestal.reg_ver.registration.registration_fragments.FullNameFragment
import yelm.io.avestal.reg_ver.registration.registration_fragments.WhatIsYourWorkFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Avestal)
        binding = RegVerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SharedPreferencesSetting.initSharedPreferencesSettings(this)

        openWhatIsYourWorkFragment()

        //TODO return to this point
        //checkUser()
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
//
//
//
//        finish()
//    }

    override fun showToast(message: Int) {
        toast?.cancel()
        toast = Toast.makeText(this, resources?.getString(message), Toast.LENGTH_LONG)
        toast?.show()
    }

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

    override fun openUserPhotoFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .add(R.id.container, userPhotoFragment ?: UserPhotoFragment.newInstance())
            .addToBackStack("UserPhoto")
            .commit()
    }



}